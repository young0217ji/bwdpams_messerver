package mes.eis.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import kr.co.mesframe.util.StringUtil;
import mes.constant.Constant;
import mes.event.MessageParse;
import mes.eis.common.EISCommonUtil;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.PROCESSDATARESULT;
import mes.lot.validation.LotValidation;
import mes.util.EventInfoUtil;

/**
 * EIS - 설비에서 발생한 공정 조건 값 I/F_R
 * @author ITIER-HSLEE
 * @since 2023.09.07
 */
public class TxnDataResultByEqp implements ObjectExecuteService
{
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Document recvDoc) 
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		LotValidation validation = new LotValidation();
		EISCommonUtil eisCommonUtil = new EISCommonUtil();

		validation.checkListNull(dataList);
		
		String plantID = "";
		String equipmentID = "";
		String scanID = "";
		String tagID = "";
		String tagValue = "";
		String eventTime = "";
		
		String lotID = "";
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);

			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );			

			validation.checkKeyNull(dataMap, new Object[] {"PLANTID", "EQUIPMENTID", "SCANID", "TAGID", "TAGVALUE"});
			
			plantID = dataMap.get("PLANTID");
			equipmentID = dataMap.get("EQUIPMENTID");
			scanID = dataMap.get("SCANID");
			tagID = dataMap.get("TAGID"); //RECIPEPARAMETERID
			tagValue = dataMap.get("TAGVALUE"); //RESULTVALUE
			eventTime = dataMap.get("EVENTTIME");
			
			if(StringUtil.isNullOrEmpty(eventTime))
				eventTime = DateUtil.getCurrentTime();
			
			//EIS Message Log 등록 Map.
			HashMap<String, String> mapEISMsgInfo = (HashMap<String, String>) dataList.get(i);
			mapEISMsgInfo.put("MESSAGENAME", "TxnDataResultByEqp");
			mapEISMsgInfo.put("EISEVENTRESULT", "SUCC");
			mapEISMsgInfo.put("EISEVENTUSER", eisCommonUtil.getIPCCurrentUser(plantID, equipmentID));	

			
			//Set txnInfo
			txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
			txnInfo.setTxnComment("EIS_IFMSG_R-TxnDataResultByEqp");
			
			//****************************************
			// 공정 진행 가능한 LOTID 정보 조회.
			//****************************************
			HashMap<String, String> mapLotInfo = eisCommonUtil.getMappingLotInfo(plantID, equipmentID, scanID, Constant.OBJECTTYPE_RECIPE, Constant.EVENT_START);

			lotID = ConvertUtil.Object2String(mapLotInfo.get("LOTID"));
			

			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID( lotID );
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

			PROCESSDATARESULT processDataResult = new PROCESSDATARESULT();
			processDataResult.setKeyLOTID( lotID );
			processDataResult.setKeyRECIPEPARAMETERID( tagID );
			processDataResult.setKeyTIMEKEY( txnInfo.getEventTimeKey() );
			processDataResult.setEQUIPMENTID( equipmentID );
			processDataResult.setRESULTVALUE( tagValue );
			processDataResult.setRELATIONTIMEKEY( txnInfo.getEventTimeKey() );
			processDataResult.setEVENTTIME( txnInfo.getEventTime() );
			processDataResult.setEVENTUSERID( txnInfo.getTxnUser() );
			processDataResult.setEVENTCOMMENT( txnInfo.getTxnComment() );
			processDataResult.excuteDML(SqlConstant.DML_INSERT);
			
			//EIS Log 등록.
			//eisCommonUtil.setEISMessageLog(mapEISMsgInfo);
		}
		
		return recvDoc;
	}
}
;