package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.equipment.data.EQUIPMENTPMSCHEDULE;
import mes.equipment.transaction.EquipmentService;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;
import mes.util.QueryResult;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.14 
 * 
 * @see
 */
public class TxnEquipmentPMScheduleCreate implements ObjectExecuteService
{
	QueryResult oQueryExecute = new QueryResult();
	EquipmentService oEquipmentService = new EquipmentService();
	AddHistory history = new AddHistory();
	
	/**
	 * PM작업 스케줄을 신규 생성 합니다
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		String sPlantID = MessageParse.getParam(recvDoc, "PLANTID");
		String sEquipmentID = MessageParse.getParam(recvDoc, "EQUIPMENTID");
		String sEquipmentName = MessageParse.getParam(recvDoc, "EQUIPMENTNAME");
		String sWorkCenter = MessageParse.getParam(recvDoc, "WORKCENTER");
		
		HashMap<String, Object> mapBindData = new HashMap<String, Object>();
		mapBindData.put("PLANTID", sPlantID);
		mapBindData.put("EQUIPMENTID", sEquipmentID);
		mapBindData.put("EQUIPMENTNAME", sEquipmentName);
		mapBindData.put("WORKCENTER", sWorkCenter);
		
		// 진행중 PM 여부 체크
		oEquipmentService.checkRunningPMEquipment(sPlantID, sEquipmentID, sEquipmentName, sWorkCenter);
		
		// 삭제 설비 목록 조회
		List<HashMap<String, String>> listScheduleEQP = oEquipmentService.getPMEquipment(sPlantID, sEquipmentID, sEquipmentName, sWorkCenter);
		
		if ( listScheduleEQP != null && listScheduleEQP.size() > 0 ) {
			for ( int ii = 0; ii < listScheduleEQP.size(); ii++ ) {
				String sDeleteEquipmentID = listScheduleEQP.get(ii).get("EQUIPMENTID");
				
				// 미진행 PM 스케줄 작업에 대한 삭제
				EQUIPMENTPMSCHEDULE oDataInfo = new EQUIPMENTPMSCHEDULE();
				oDataInfo.setKeyPLANTID(sPlantID);
				oDataInfo.setKeyEQUIPMENTID(sDeleteEquipmentID);
				oDataInfo.setPMSCHEDULETYPE("Modeling");
				oDataInfo.setPMSTARTTIME(null);
				oDataInfo.setPMENDTIME(null);
				List<EQUIPMENTPMSCHEDULE> listDataMap = (List<EQUIPMENTPMSCHEDULE>) oDataInfo.excuteDML(SqlConstant.DML_SELECTLIST, " AND PMPLANNEDTIME > GETDATE() ");
				for ( int i = 0 ; i < listDataMap.size(); i++ ) {
					oDataInfo = listDataMap.get(i);
					oDataInfo.excuteDML(SqlConstant.DML_DELETE);
					
					txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
					
					history = new AddHistory();
					txnInfo.setTxnId("DeletePMSchedule");
					history.addHistory(oDataInfo, txnInfo, SqlConstant.DML_INSERT);
				}
			}
		}
		
		// PM 스케쥴 작업 조회 및 생성
		List<LinkedCaseInsensitiveMap> liQueryResult = oQueryExecute.getQueryResult(sPlantID, "GetPMScheduleCreate", Constant.VERSION_DEFAULT, mapBindData);
		
		for ( int i = 0 ; i < liQueryResult.size(); i ++ ) {
			HashMap<String, String> dataMap = liQueryResult.get(i);
			
			createScheduleData(dataMap, txnInfo);
		}

		return recvDoc;
	}
	
	private void createScheduleData(HashMap<String, String> dataMap, TxnInfo txnInfo) {
		NameGenerator nameGenerator = new NameGenerator();
		txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
		
		String sCurrentDay = DateUtil.getCurrentTime(DateUtil.FORMAT_SIMPLE_DAY);
		
		String sPlannedDate = DateUtil.dateToString(DateUtil.FORMAT_SIMPLE_DAY, DateUtil.getTimestamp(dataMap.get("PMPLANNEDTIME")));
		if ( sPlannedDate != null && sPlannedDate.isEmpty() == false ) {
			String sYearMonth = sCurrentDay.substring(0, 6); // YYYYMM
			String sPlannedMonth = sPlannedDate.substring(0, 6); // YYYYMM
			
			// 현재 월 이외의 계획에 대해서는 생성하지 않음!
			if ( sYearMonth.equals(sPlannedMonth) == false ) {
				return;
			}
		}
		else {
			// 계획일자가 없이 생성되는 경우 Return
			return;
		}
		
		EQUIPMENTPMSCHEDULE dataInfo = new EQUIPMENTPMSCHEDULE();
		dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
		dataInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
		
		String pmScheduleID = nameGenerator.nameGenerate(dataMap.get("PLANTID"), "PMScheduleID", new Object[] {});
		dataInfo.setKeyPMSCHEDULEID(pmScheduleID);

		String managerID = dataMap.get("MANAGERUSERID");
		if ( managerID.equals("") ) {
			dataInfo.setMANAGERUSERID(txnInfo.getTxnUser());
		}
		else {
			dataInfo.setMANAGERUSERID(dataMap.get("MANAGERUSERID"));
		}

		dataInfo.setPMSCHEDULETYPE(dataMap.get("PMSCHEDULETYPE"));
		dataInfo.setPMPLANNEDTIME(DateUtil.getTimestamp(dataMap.get("PMPLANNEDTIME")));
		dataInfo.setWORKERUSERID(dataMap.get("WORKERUSERID"));
		dataInfo.setWORKCOMMENT(dataMap.get("WORKCOMMENT"));
		dataInfo.setREFERENCETYPE("PMCODEID");
		dataInfo.setREFERENCEVALUE(dataMap.get("REFERENCEVALUE"));
		dataInfo.excuteDML(SqlConstant.DML_INSERT);

		history = new AddHistory();
		txnInfo.setTxnId("CreatePMSchedule");
		history.addHistory(dataInfo, txnInfo, SqlConstant.DML_INSERT);
		
		// Next Schedule Create
		HashMap<String, Object> mapBindData = new HashMap<String, Object>();
		mapBindData.put("PLANTID", dataInfo.getKeyPLANTID());
		mapBindData.put("EQUIPMENTID", dataInfo.getKeyEQUIPMENTID());
		mapBindData.put("PMCODEID", dataInfo.getREFERENCEVALUE());
		mapBindData.put("PMPLANNEDTIME", dataInfo.getPMPLANNEDTIME());
		List<LinkedCaseInsensitiveMap> liQueryResult = oQueryExecute.getQueryResult(dataMap.get("PLANTID"), "GetPMScheduleCreateNext", Constant.VERSION_DEFAULT, mapBindData);
		
		createScheduleData(liQueryResult.get(0), txnInfo);
	}
}
