package mes.eis.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import kr.co.mesframe.util.StringUtil;
import mes.constant.Constant;
import mes.event.MessageParse;
import mes.eis.common.EISCommonUtil;
import mes.lot.custom.RecipeCustomService;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.validation.LotValidation;
import mes.util.EventInfoUtil;

/**
 * EIS - 장비 종료 메세지 처리.
 * @author ITIER-HSLEE
 * @since 2023.09.01
 */
public class TxnEquipmentEndByEqp implements ObjectExecuteService
{
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Document recvDoc) 
	{
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
		String scanID = "";
		String eventTime = "";
		
		String lotID = "";
		String processID = "";
		String processSequence = "";
		Long routeRelationSequence;
		String recipeID = "";
		String recipeType = "";
		String recipeSequence = "";
		String recipeRelationCode = "";
		String recipeTypeCode = "";
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			//필수항목 Validation.
			validation.checkKeyNull(dataMap, new Object[] {"PLANTID", "EQUIPMENTID", "SCANID"});
			
			plantID = dataMap.get("PLANTID");
			equipmentID = dataMap.get("EQUIPMENTID");
			scanID = dataMap.get("SCANID");
			eventTime = dataMap.get("EVENTTIME");
			
			if(StringUtil.isNullOrEmpty(eventTime))
				eventTime = DateUtil.getCurrentTime();
			
			//EIS Message Log 등록 Map.
			HashMap<String, String> mapEISMsgInfo = (HashMap<String, String>) dataList.get(i);
			mapEISMsgInfo.put("MESSAGENAME", "TxnEquipmentEndByEqp");
			mapEISMsgInfo.put("EISEVENTRESULT", "SUCC");
			mapEISMsgInfo.put("EISEVENTUSER", eisCommonUtil.getIPCCurrentUser(plantID, equipmentID));
			
			//Set txnInfo
			txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
			txnInfo.setTxnComment("EISIF-TxnEquipmentEndByEqp");
			
			//****************************************
			// 공정 진행 가능한 LOTID 정보 조회.
			//****************************************
			HashMap<String, String> mapLotInfo = eisCommonUtil.getMappingLotInfo(plantID, equipmentID, scanID, Constant.OBJECTTYPE_RECIPE, Constant.EVENT_END);

			lotID = ConvertUtil.Object2String(mapLotInfo.get("LOTID"));
			
			VIEWLOTTRACKING viewlot = new VIEWLOTTRACKING();
			viewlot.setKeyLOTID(lotID);
			
			viewlot = (VIEWLOTTRACKING) viewlot.excuteDML(SqlConstant.DML_SELECTROW);

			processID = viewlot.getPROCESSID();
			processSequence = ConvertUtil.Object2String(viewlot.getPROCESSSEQUENCE());
			routeRelationSequence = viewlot.getROUTERELATIONSEQUENCE();
			recipeID = viewlot.getRECIPEID();
			recipeType = viewlot.getRECIPETYPE();
			recipeSequence = ConvertUtil.Object2String(viewlot.getRECIPESEQUENCE());
			recipeRelationCode = viewlot.getRECIPERELATIONCODE();
			recipeTypeCode = viewlot.getRECIPETYPECODE();
			
			recipeCustomService.recipeEnd(lotID, processID, processSequence, routeRelationSequence, recipeID, recipeSequence, 
					recipeRelationCode, recipeTypeCode, txnInfo, Constant.CONTROL_MODE_MANUAL);
			
			//EIS Log 등록.
			eisCommonUtil.setEISMessageLog(mapEISMsgInfo);
		}
		
		return recvDoc;
	}
}
