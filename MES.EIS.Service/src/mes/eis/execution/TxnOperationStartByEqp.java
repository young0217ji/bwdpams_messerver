package mes.eis.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.esb.ObjectExecuteService;
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
import mes.lot.transaction.LotService;
import mes.lot.transaction.LotTrackingService;
import mes.lot.transaction.ProcessService;
import mes.lot.validation.LotValidation;
import mes.master.data.DURABLEINFORMATION;
import mes.util.EventInfoUtil;
import mes.util.SendMessageUtil;

/**
 * EIS - 공정 시작 메세지 처리.
 * @author ITIER-HSLEE
 * @since 2023.08.25
 */
public class TxnOperationStartByEqp implements ObjectExecuteService
{
	@Override
    public Object execute(Document recvDoc)
	{		
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		LotValidation validation = new LotValidation();
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		ProcessCustomService operationService = new ProcessCustomService();
		ProcessService processService = new ProcessService();
		LotTrackingService lotTrackingService = new LotTrackingService();
		EISCommonUtil eisCommonUtil = new EISCommonUtil();
		SendMessageUtil sendMessageUtil = new SendMessageUtil();
	    AddHistory history = new AddHistory();

		validation.checkListNull(dataList);
		
		String sSQL = "";
		HashMap bindMap = new HashMap();
		List resultList;
		LinkedCaseInsensitiveMap resultMap;
		
		String plantID = "";
		String equipmentID = "";
		String scanID = "";
		String eventTime = "";
		String scnIDType = "";  // 1:RFID, 2:BARCODE..
		
		String lotID = "";
		String processID = "";
		String processSequence = "";
		Long routeRelationSequence;
		
		String materialVendor = "";
		String materialLotID = "";
		String materialDate = "";
		String materialSerial = "";
		String materialSpecialCode = "";
		
		List<HashMap<String, String>> listResultMap = new ArrayList<HashMap<String, String>>();
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);

			//필수항목 Validation.
			validation.checkKeyNull(dataMap, new Object[] {"PLANTID", "EQUIPMENTID", "SCANID"});
			
			plantID = dataMap.get("PLANTID");
			equipmentID = dataMap.get("EQUIPMENTID");
			scanID = dataMap.get("SCANID");
			eventTime = dataMap.get("EVENTTIME");
			
			materialVendor = "";
			materialLotID = "";
			materialDate = "";
			materialSerial = "";
			materialSpecialCode = "";
			
			if(StringUtil.isNullOrEmpty(eventTime))
				eventTime = DateUtil.getCurrentTime();
						
			//EIS Message Log 등록 Map.
			HashMap<String, String> mapEISMsgInfo = (HashMap<String, String>) dataList.get(i);
			mapEISMsgInfo.put("MESSAGENAME", "TxnOperationStartByEqp");
			mapEISMsgInfo.put("EISEVENTRESULT", "SUCC");
			mapEISMsgInfo.put("EISEVENTUSER", eisCommonUtil.getIPCCurrentUser(plantID, equipmentID));	

			//ScanID가 몇자리로 올라올 지 미확인..for문 돌면서 순서대로 갯수 만큼만 등록 한다.(2023.09.20)
			if(scanID.contains("*"))
			{
				String[] listScanID = scanID.split("\\*");
				
				for(int j = 0; j < listScanID.length; j++)
				{
					if(j == 0) {materialVendor = ConvertUtil.Object2String(listScanID[j]);}
					if(j == 1) {materialLotID = ConvertUtil.Object2String(listScanID[j]);}
					if(j == 2) {materialDate = ConvertUtil.Object2String(listScanID[j]);}
					if(j == 3) {materialSerial = ConvertUtil.Object2String(listScanID[j]);}
					if(j == 4) {materialSpecialCode = ConvertUtil.Object2String(listScanID[j]);}
				}
			}
			
			//Set txnInfo
			txnInfo.setTxnUser(equipmentID);
			txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
			txnInfo.setTxnComment("EISIF-TxnOperationStartByEqp");
			
			//****************************************
			// 공정 진행 가능한 LOTID 정보 조회.
			//****************************************
			HashMap<String, String> mapLotInfo = eisCommonUtil.getMappingLotInfo(plantID, equipmentID, scanID, Constant.OBJECTTYPE_OPERATION, Constant.EVENT_START);
						
			lotID = ConvertUtil.Object2String(mapLotInfo.get("LOTID"));
			processID = ConvertUtil.Object2String(mapLotInfo.get("PROCESSID"));
			processSequence = ConvertUtil.Object2String(mapLotInfo.get("PROCESSSEQUENCE"));
			routeRelationSequence = ConvertUtil.Object2Long(mapLotInfo.get("ROUTERELATIONSEQUENCE"));

			//Get LOT Info..
			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID(lotID);
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			//장비 등록..
			if ( dataMap.get("EQUIPMENTID") != null && !dataMap.get("EQUIPMENTID").isEmpty() )
			{
				TxnInfo sTxnInfo = new TxnInfo();
				sTxnInfo.setTxnId(Constant.EVENT_CHANGEEQUIPMENT);
				sTxnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
				sTxnInfo.setEventTime(txnInfo.getEventTime());
				sTxnInfo.setTxnUser(txnInfo.getTxnUser());
				sTxnInfo.setTxnComment( txnInfo.getTxnComment() );

				operationService.changeEquipment(lotInfo, processID, processSequence, routeRelationSequence, dataMap.get("EQUIPMENTID"), sTxnInfo);
			}
			
			//*************************************
			//TrackIn
			//*************************************
			operationService.processStart(lotID, processID, processSequence, routeRelationSequence, txnInfo, Constant.CONTROL_MODE_MANUAL);
						
			VIEWLOTTRACKING viewLotInfo = lotTrackingService.getProcessInfo(lotID, lotInfo.getPROCESSROUTEID(), processID, ConvertUtil.Object2Long4Zero(processSequence), routeRelationSequence, Constant.EVENT_START);
			
			//*************************************
			//MaterialLOTID 매핑
			//*************************************			
			/*
			 *	SET	LOTINFORMATION 테이블 - 진행중인 ScanID와 매핑.
			 *		ScanIDType = "1" : RFID에 매핑된 기존 LOTID 정보가 있을경우 MATERIALLOTID만 UPDATE..(현재 작업 진행중인 MaterialLOTID 등록..)
			 *		ScanIDType = "2" : RFID에 매핑된 LOTID 정보가 없을경우 INITMATERIALLOTID, MATERIALLOTID 모두 등록.
			 *		ScanIDType = "3" : BARCODE 와 매핑된 LOTID 정보가 있는경우 MATERIALLOTID만 UPDATE..
			 *		ScanIDType = "4" : BARCODE 와 매핑된 LOTID 정보가 없는경우 INITMATERIALLOTID, MATERIALLOTID 모두 등록.
			 */
			lotInfo.setMATERIALLOTID(scanID);

			//BARCODE Type 자재Lot추가 시 LOTINFIMATION에 신규 등록.
			if(ConvertUtil.Object2String(mapLotInfo.get("SCANIDTYPE")).equals("4"))
			{
				lotInfo.setMATERIALLOTID(scanID);
				
				lotInfo.excuteDML(SqlConstant.DML_UPDATE);
			}

			/*
 			 *	SET DURABLEINFORMATION 테이블 - RFID와 LOTID 매핑.
			 *		RFID - LOTID 매핑 정보 UPDATE.
			 */
		    if(ConvertUtil.Object2String(mapLotInfo.get("SCANIDTYPE")).equals("2"))
		    {
				DURABLEINFORMATION durableInfo = new DURABLEINFORMATION();
				durableInfo.setKeyDURABLEID(scanID);
				durableInfo = (DURABLEINFORMATION) durableInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
				
				durableInfo.setLOTID(lotID);
				durableInfo.excuteDML(SqlConstant.DML_UPDATE);
				
			    history = new AddHistory();
			    history.addHistory(durableInfo, txnInfo, "U");
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
			eisCommonUtil.setEISMessageLog(mapEISMsgInfo);
		}
		
		Element element = sendMessageUtil.createElementFromList(listResultMap);
		
		return element;
	}
}
