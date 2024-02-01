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
import mes.event.MessageParse;
import mes.eis.common.EISCommonUtil;
import mes.eis.data.DY_EQUIPMENTALARM;
import mes.eis.data.DY_STANDBYINPUTCONSUMABLE;
import mes.lot.custom.RecipeCustomService;
import mes.lot.validation.LotValidation;
import mes.util.EventInfoUtil;

/**
 * EIS - 설비에 투입 예정인 자재 정보 I/F_R
 * 
 * @author ITIER-HSLEE
 * @since 2023.09.01
 */
public class TxnEquipmentAlarmByEqp implements ObjectExecuteService {
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		LotValidation validation = new LotValidation();
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		RecipeCustomService recipeCustomService = new RecipeCustomService();
		EISCommonUtil eisCommonUtil = new EISCommonUtil();
		validation.checkListNull(dataList);

		String sSQL = "";
		HashMap<String, String> bindMap = new HashMap();
		List<Object> resultList;
		LinkedCaseInsensitiveMap resultMap;

		String plantID = "";
		String equipmentID = "";
		String alarmID = "";
		String alarmComment = "";
		String eventTime = "";

		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);

			// 필수항목 Validation.
			validation.checkKeyNull(dataMap, new Object[] { "PLANTID", "EQUIPMENTID", "ALARMID" });

			plantID = dataMap.get("PLANTID");
			equipmentID = dataMap.get("EQUIPMENTID");
			alarmID = dataMap.get("ALARMID");
			alarmComment = dataMap.get("ALARMCOMMENT");
			eventTime = dataMap.get("EVENTTIME");

			if (StringUtil.isNullOrEmpty(eventTime))
				eventTime = DateUtil.getCurrentTime();

			// EIS Message Log 등록 Map.
			HashMap<String, String> mapEISMsgInfo = (HashMap<String, String>) dataList.get(i);
			mapEISMsgInfo.put("MESSAGENAME", "TxnEquipmentAlarmByEqp");
			mapEISMsgInfo.put("EISEVENTRESULT", "SUCC");
			mapEISMsgInfo.put("EISEVENTUSER", eisCommonUtil.getIPCCurrentUser(plantID, equipmentID));

			// Set txnInfo
			txnInfo.setEventTime(DateUtil.getTimestamp(eventTime));
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			txnInfo.setTxnComment("EIS_IFMSG_R-TxnEquipmentAlarmByEqp");

			DY_EQUIPMENTALARM equipmentAlarm = new DY_EQUIPMENTALARM();
			equipmentAlarm.setKeyPLANTID(plantID);
			equipmentAlarm.setKeyDATAKEY(DateUtil.getCurrentEventTimeKey());
			equipmentAlarm.setEQUIPMENTID(equipmentID);
			equipmentAlarm.setALARMID(alarmID);
			equipmentAlarm.setALARMCOMMENT(alarmComment);

			equipmentAlarm.excuteDML(SqlConstant.DML_INSERT);

			// EIS Log 등록.
			eisCommonUtil.setEISMessageLog(mapEISMsgInfo);
		}

		return recvDoc;
	}
};