package mes.eis.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import kr.co.mesframe.util.StringUtil;
import mes.constant.Constant;
import mes.event.MessageParse;
import mes.eis.common.EISCommonUtil;
import mes.lot.custom.LotCustomService;
import mes.lot.custom.ProcessCustomService;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.transaction.LotTrackingService;
import mes.lot.transaction.ProcessService;
import mes.lot.validation.LotValidation;
import mes.master.data.DURABLEINFORMATION;
import mes.util.EventInfoUtil;
import mes.util.SendMessageUtil;

public class TxnOperationEndByEqp implements ObjectExecuteService
{
	/**
	 * EIS - 공정 종료 메세지 처리.
	 * @author ITIER-HSLEE
	 * @since 2023.08.25
	 */
	@SuppressWarnings("unchecked")
	@Override
    public Object execute(Document recvDoc) 
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		LotValidation validation = new LotValidation();
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		
		ProcessCustomService operationService = new ProcessCustomService();		
		LotCustomService lotCustomService = new LotCustomService();
		ProcessService processService = new ProcessService();
		LotTrackingService lotTrackingService = new LotTrackingService();
		EISCommonUtil eisCommonUtil = new EISCommonUtil();
		SendMessageUtil sendMessageUtil = new SendMessageUtil();
		AddHistory history = new AddHistory();
		
		validation.checkListNull(dataList);
		
		String sSQL = "";
		HashMap<String, String> bindMap = new HashMap();
		List<Object> resultList;
		LinkedCaseInsensitiveMap resultMap;
		
		String plantID = "";
		String equipmentID = "";
		String scanID = "";
		String endScanID = "";
		String eventTime = "";
		
		String lotID = "";
		String processID = "";
		String processSequence = "";
		Long routeRelationSequence;
		String endOfRoute = "";
		
		List<HashMap<String, String>> listResultMap = new ArrayList<HashMap<String, String>>();
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			//필수항목 Validation.
			validation.checkKeyNull(dataMap, new Object[] {"PLANTID", "EQUIPMENTID", "SCANID"});
			
			plantID = dataMap.get("PLANTID");
			equipmentID = dataMap.get("EQUIPMENTID");
			scanID = dataMap.get("SCANID");
			endScanID = dataMap.get("ENDSCANID");
			eventTime = dataMap.get("EVENTTIME");
			
			if(StringUtil.isNullOrEmpty(eventTime))
				eventTime = DateUtil.getCurrentTime();
			
			//EIS Message Log 등록 Map.
			HashMap<String, String> mapEISMsgInfo = (HashMap<String, String>) dataList.get(i);
			mapEISMsgInfo.put("MESSAGENAME", "TxnOperationEndByEqp");
			mapEISMsgInfo.put("EISEVENTRESULT", "SUCC");
			mapEISMsgInfo.put("EISEVENTUSER", eisCommonUtil.getIPCCurrentUser(plantID, equipmentID));
			
			//Set txnInfo
			txnInfo.setTxnUser(equipmentID);
			txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
			txnInfo.setTxnComment("EISIF-TxnOperationEndByEqp");
			
			//****************************************
			// 공정 진행 가능한 LOTID 정보 조회.
			//****************************************
			HashMap<String, String> mapLotInfo = eisCommonUtil.getMappingLotInfo(plantID, equipmentID, scanID, Constant.OBJECTTYPE_OPERATION, Constant.EVENT_END);

			lotID = ConvertUtil.Object2String(mapLotInfo.get("LOTID"));
			processID = ConvertUtil.Object2String(mapLotInfo.get("PROCESSID"));
			processSequence = ConvertUtil.Object2String(mapLotInfo.get("PROCESSSEQUENCE"));
			routeRelationSequence = ConvertUtil.Object2Long(mapLotInfo.get("ROUTERELATIONSEQUENCE"));
			
			//Default 수량 '1'로 고정.
			Double outQuantity = ConvertUtil.Object2Double(1);
			
			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID(lotID);
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			if ( outQuantity != null && lotInfo.getCURRENTQUANTITY().compareTo(outQuantity) != 0 )
			{
				TxnInfo sTxnInfo = new TxnInfo();
				sTxnInfo.setTxnId(txnInfo.getTxnId());
				sTxnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
				sTxnInfo.setEventTime(txnInfo.getEventTime());
				sTxnInfo.setTxnUser(txnInfo.getTxnUser());
				sTxnInfo.setTxnReasonCode( dataMap.get("REASONCODE") );
				sTxnInfo.setTxnReasonCodeType( dataMap.get("REASONCODETYPE") );
				sTxnInfo.setTxnComment( dataMap.get("COMMENT") );

				lotCustomService.makeScrapQuantity(lotInfo, ConvertUtil.doubleSubtract(outQuantity, lotInfo.getCURRENTQUANTITY()), sTxnInfo);
			}
			
			//*************************************
			//TrackOut
			//*************************************
			operationService.processEnd(lotID, processID, processSequence, routeRelationSequence, txnInfo, Constant.CONTROL_MODE_MANUAL);
			
			VIEWLOTTRACKING viewLotInfo = lotTrackingService.getProcessInfo(lotID, lotInfo.getPROCESSROUTEID(), processID, ConvertUtil.Object2Long4Zero(processSequence), routeRelationSequence, Constant.EVENT_END);

			//*************************************
			//MaterialLOTID 매핑
			//*************************************		
			if(ConvertUtil.Object2String(mapLotInfo.get("SCANIDTYPE")).equals("1"))  //RFID 매핑 Lot일경우
			{
				if(!ConvertUtil.isEmptyForTrim(ConvertUtil.Object2String(endScanID)))
				{
					DURABLEINFORMATION durableInfo = new DURABLEINFORMATION();
					durableInfo.setKeyDURABLEID(endScanID);
					durableInfo = (DURABLEINFORMATION) durableInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
					
					durableInfo.setLOTID(lotID);
					durableInfo.excuteDML(SqlConstant.DML_UPDATE);
					
				    history = new AddHistory();
				    history.addHistory(durableInfo, txnInfo, "U");					
				}
			}
			
			//투입 자재 관리.
		    eisCommonUtil.setMaterial(plantID, lotID, lotInfo.getPROCESSROUTEID(), processID, processSequence, routeRelationSequence, Constant.EVENT_START, scanID);
		    
		    //Client에 결과값 Return.
			LinkedCaseInsensitiveMap resultLotList = new LinkedCaseInsensitiveMap();
			resultLotList.put("PLANTID", plantID);
			resultLotList.put("SCANID", scanID);
			resultLotList.put("TIMEKEY", viewLotInfo.getKeyTIMEKEY());
			resultLotList.put("LOTID", lotID);
			resultLotList.put("PRODUCTID", lotInfo.getPRODUCTID());
			resultLotList.put("EQUIPMENTID", equipmentID);
			resultLotList.put("PROCESSID", processID);
			resultLotList.put("SCANID", scanID);			
			resultLotList.put("PROCESSSEQUENCE", processSequence);		
			
			listResultMap.add(resultLotList);
			
			//EIS Log 등록.
			//eisCommonUtil.setEISMessageLog(mapEISMsgInfo);
		}
		
		Element element = sendMessageUtil.createElementFromList(listResultMap);
		
		return element;
	}

}
