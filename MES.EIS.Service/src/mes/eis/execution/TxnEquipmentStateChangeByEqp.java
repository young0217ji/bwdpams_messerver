package mes.eis.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import kr.co.mesframe.util.StringUtil;
import mes.eis.common.EISCommonUtil;
import mes.equipment.transaction.EquipmentService;
import mes.event.MessageParse;
import mes.lot.validation.LotValidation;
import mes.master.data.EQUIPMENT;
import mes.util.EventInfoUtil;

/**
 * EIS - 설비상태 변경 I/F_R
 * @author ITIER-HSLEE
 * @since 2023.09.06
 */
public class TxnEquipmentStateChangeByEqp implements ObjectExecuteService
{
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Document recvDoc) 
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		LotValidation validation = new LotValidation();
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		EquipmentService equipmentService = new EquipmentService();
		EISCommonUtil eisCommonUtil = new EISCommonUtil();
		validation.checkListNull(dataList);
		
		String sSQL = "";
		HashMap<String, String> bindMap = new HashMap();
		List<Object> resultList;
		LinkedCaseInsensitiveMap resultMap;
		
		String plantID = "";
		String equipmentID = "";
		String state = "";
		String eventTime = "";
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			//필수항목 Validation.
			validation.checkKeyNull(dataMap, new Object[] {"PLANTID", "EQUIPMENTID", "STATE"});
			
			plantID = dataMap.get("PLANTID");
			equipmentID = dataMap.get("EQUIPMENTID");
			state = dataMap.get("STATE");
			eventTime = dataMap.get("EVENTTIME");
			
			if(StringUtil.isNullOrEmpty(eventTime))
				eventTime = DateUtil.getCurrentTime();
			
			//EIS Message Log 등록 Map.
			HashMap<String, String> mapEISMsgInfo = (HashMap<String, String>) dataList.get(i);
			mapEISMsgInfo.put("MESSAGENAME", "TxnEquipmentStateChangeByEqp");
			mapEISMsgInfo.put("EISEVENTRESULT", "SUCC");
			mapEISMsgInfo.put("EISEVENTUSER", eisCommonUtil.getIPCCurrentUser(plantID, equipmentID));
			
			
			//Set txnInfo
			txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
			txnInfo.setTxnComment("EISIF-TxnEquipmentStateChangeByEqp");

			EQUIPMENT equipmentInfo = new EQUIPMENT();
			equipmentInfo.setKeyEQUIPMENTID(equipmentID);
			equipmentInfo.setKeyPLANTID(plantID);
			
			equipmentInfo = (EQUIPMENT) equipmentInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			equipmentService.stateChange(equipmentInfo, state, txnInfo);
			
			//EIS Log 등록.
			eisCommonUtil.setEISMessageLog(mapEISMsgInfo);
		}
		
		return recvDoc;
	}
}
;