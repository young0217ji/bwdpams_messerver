package mes.equipment.transaction;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.mesframe.exception.NoDataFoundException;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.equipment.data.EQUIPMENTPMSCHEDULE;
import mes.errorHandler.CustomException;
import mes.lot.data.LOTINFORMATION;
import mes.master.data.EQUIPMENT;
import mes.master.data.EQUIPMENTIMAGE;
import mes.util.NameGenerator;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class EquipmentService
{
	private static final transient Logger log = LoggerFactory.getLogger(EquipmentService.class);
	
    /**
     * equipmentInfo의 상태를 changeState로 변경합니다
     * 
     * @param equipmentInfo
     * @param changeState
     * @param txnInfo
     * @return
     * 
    */
	public void stateChange(EQUIPMENT equipmentInfo, String changeState, TxnInfo txnInfo)
	{
		stateChange(equipmentInfo, changeState, "", "", txnInfo);
	}
	
    /**
     * equipmentInfo의 상태를 changeState로 변경하고 reasonCode와 reasonCodeType을 저장합니다
     * 
     * @param equipmentInfo
     * @param changeState
     * @param reasonCode
     * @param reasonCodeType
     * @param txnInfo
     * @return
     * 
    */
	public void stateChange(EQUIPMENT equipmentInfo, String changeState, String reasonCode, String reasonCodeType, TxnInfo txnInfo)
	{
		equipmentInfo.setEQUIPMENTSTATE(changeState);
		equipmentInfo.setREASONCODE(reasonCode);
		equipmentInfo.setREASONCODETYPE(reasonCodeType);
		equipmentInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(equipmentInfo, txnInfo, "U");
	}
	
    /**
     * equipmentInfo에 equipmentPMScheduleInfo의 정보를 set하고 상태를 changeState로 변경합니다
     * 
     * @param equipmentInfo
     * @param equipmentPMScheduleInfo
     * @param changeState
     * @param txnInfo
     * @param pmWork
     * @return
     * 
    */
	public void pmWork_Equipment(EQUIPMENT equipmentInfo, EQUIPMENTPMSCHEDULE equipmentPMScheduleInfo, String changeState, TxnInfo txnInfo)
	{
		equipmentInfo.setREFERENCETYPE(Constant.EQUIPMENT_REFERENCETYPE_PMSCHEDULE);
		equipmentInfo.setREFERENCEVALUE(equipmentPMScheduleInfo.getKeyPMSCHEDULEID());
		equipmentInfo.setEQUIPMENTSTATE(changeState);
		equipmentInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		AddHistory history = new AddHistory();
	    history.addHistory(equipmentInfo, txnInfo, "U");
	}
	
    /**
     * equipmentInfo의 진행중인 PM작업을 취소합니다
     * 
     * @param equipmentInfo
     * @param equipmentPMScheduleInfo
     * @param txnInfo
     * @param sCancelEventType
     * @return
     * @throws CusotmException
     * 
    */
	public void pmWorkCancel(EQUIPMENT equipmentInfo, EQUIPMENTPMSCHEDULE equipmentPMScheduleInfo, TxnInfo txnInfo, String sCancelEventType) {
		String pmScheduleID = equipmentPMScheduleInfo.getKeyPMSCHEDULEID();
		String currEquipmentState = equipmentInfo.getEQUIPMENTSTATE();
		
		List<Object> pmScheduleHistoryList = GetPMScheduleHistory(pmScheduleID, sCancelEventType);
		if ( pmScheduleHistoryList.size() < 1 ) {
			// 해당 설비({0})에 대한 긴급/예방 보전 작업({1})의 이력을 조회할 수 없습니다. 관리자의 확인이 필요합니다.
			throw new CustomException("EQP-009",new Object[] {equipmentPMScheduleInfo.getKeyEQUIPMENTID(), equipmentPMScheduleInfo.getREFERENCEVALUE()});
		}
		
		if ( sCancelEventType.equals(Constant.PM_REFERENCETYPE_PMSTARTCANCEL) && currEquipmentState.equals(Constant.EQUIPMENTSTATE_PM) ) {
			equipmentInfo.setEQUIPMENTSTATE(Constant.EQUIPMENTSTATE_IDLE);
			equipmentInfo.setREFERENCETYPE(null);
			equipmentInfo.setREFERENCEVALUE(null);
			equipmentInfo.excuteDML(SqlConstant.DML_UPDATE);
			
			AddHistory history = new AddHistory();
		    history.addHistory(equipmentInfo, txnInfo, SqlConstant.DML_UPDATE);
			
			equipmentPMScheduleInfo.setPMSTARTTIME(null);
			equipmentPMScheduleInfo.excuteDML(SqlConstant.DML_UPDATE);
			
			history = new AddHistory();
			history.addHistory(equipmentPMScheduleInfo, txnInfo, SqlConstant.DML_UPDATE);
		}
		else if ( sCancelEventType.equals(Constant.PM_REFERENCETYPE_PMENDCANCEL) && currEquipmentState.equals(Constant.EQUIPMENTSTATE_IDLE) ) {
			equipmentInfo.setEQUIPMENTSTATE(Constant.EQUIPMENTSTATE_PM);
			equipmentInfo.setREFERENCETYPE(Constant.EQUIPMENT_REFERENCETYPE_PMSCHEDULE);
			equipmentInfo.setREFERENCEVALUE(pmScheduleID);
			equipmentInfo.setLASTMAINTENANCETIME(null);
			equipmentInfo.excuteDML(SqlConstant.DML_UPDATE);
			
			AddHistory history = new AddHistory();
		    history.addHistory(equipmentInfo, txnInfo, SqlConstant.DML_UPDATE);
			
			equipmentPMScheduleInfo.setPMENDTIME(null);
			equipmentPMScheduleInfo.excuteDML(SqlConstant.DML_UPDATE);
			
			history = new AddHistory();
			history.addHistory(equipmentPMScheduleInfo, txnInfo, SqlConstant.DML_UPDATE);
		}
		else {
			// 해당 설비({0})에 대한 상태와 긴급/예방 보전 작업({1})의 상태가 일치하지 않아 작업을 진행 할 수 없습니다. 관리자의 확인이 필요합니다.
			throw new CustomException("EQP-010",new Object[] {equipmentPMScheduleInfo.getKeyEQUIPMENTID(), equipmentPMScheduleInfo.getREFERENCEVALUE()});
		}
	}
	
    /**
     * pmScheduleID를 가진 PM스케줄의 히스토리를를 불러옵니다
     * 
     * @param pmScheduleID
     * @param sCancelEventType
     * @return List<Object>
     * 
    */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> GetPMScheduleHistory(String pmScheduleID, String sCancelEventType)
	{
		String eventName = "";
		if( sCancelEventType.equals(Constant.PM_REFERENCETYPE_PMSTARTCANCEL))
		{
			eventName = Constant.PM_REFERENCETYPE_PMSTART;
		}
		else if( sCancelEventType.equals(Constant.PM_REFERENCETYPE_PMENDCANCEL))
		{
			eventName = Constant.PM_REFERENCETYPE_PMEND;
		}
		
		String sSql = "SELECT * "
				+ "FROM EQUIPMENTPMSCHEDULEHISTORY "
				+ "WHERE PLANTID = :PLANTID AND "
				+ "PMSCHEDULEID = :PMSCHEDULEID AND "
				+ "EVENTNAME = :EVENTNAME "
				+ "ORDER BY EVENTTIME DESC";
		HashMap bindMap = new HashMap();
		bindMap.put("PLANTID", Constant.PLANTID);
		bindMap.put("PMSCHEDULEID", pmScheduleID);
		bindMap.put("EVENTNAME", eventName);
		List<Object> DataList = SqlMesTemplate.queryForList(sSql, bindMap);

		return DataList;
	}
	
    /**
     * pmScheduleInfo와 연관된 설비부품을 리스트로 불러옵니다
     * 
     * @param pmScheduleInfo
     * @return List<Object>
     * 
    */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> GetSparepartInfo(EQUIPMENTPMSCHEDULE pmScheduleInfo)
	{
		String pmScheduleID = pmScheduleInfo.getKeyPMSCHEDULEID();
		String equipmentID = pmScheduleInfo.getKeyEQUIPMENTID();
		String sSql = "SELECT * "
				+ "FROM SPAREPARTINFORMATION "
				+ "WHERE PLANTID = :PLANTID AND "
				+ "LOCATION = :LOCATION AND "
				+ "REFERENCETYPE = '" + Constant.SPAREPART_REFERENCETYPE_PMSCHEDULE + "' AND "
				+ "REFERENCEVALUE = :PMSCHEDULEID ";
		HashMap bindMap = new HashMap();
		bindMap.put("PLANTID", Constant.PLANTID);
		bindMap.put("LOCATION", equipmentID);
		bindMap.put("PMSCHEDULEID", pmScheduleID);
		List<Object> DataList = SqlMesTemplate.queryForList(sSql, bindMap);

		return DataList;
	}
	
    /**
     * equipmentInfo에 lotInfo를 할당, BATCHCOUNT를 1만큼 증가시킵니다
     * 
     * @param equipmentInfo
     * @param lotInfo
     * @param txnInfo
     * @return
     * 
    */
	@SuppressWarnings("null")
	public void increaseBatchCount(EQUIPMENT equipmentInfo, LOTINFORMATION lotInfo, TxnInfo txnInfo)
	{
		Long lBatchCount = equipmentInfo.getBATCHCOUNT();
		if (lotInfo==null)
		{
			equipmentInfo.setLOTID(lotInfo.getKeyLOTID());
			equipmentInfo.setWORKORDER(lotInfo.getWORKORDER());
		}
		else
		{
			equipmentInfo.setLOTID(lotInfo.getKeyLOTID());
			equipmentInfo.setWORKORDER(lotInfo.getWORKORDER());
		}
		equipmentInfo.setBATCHCOUNT(lBatchCount+1);
		equipmentInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(equipmentInfo, txnInfo, "U");
	}

    /**
     * equipmentInfo에 BATCHCOUNT를 1만큼 감소시킵니다
     * 
     * @param equipmentInfo
     * @param txnInfo
     * @return
     * 
    */
	public void decreaseBatchCount(EQUIPMENT equipmentInfo, TxnInfo txnInfo)
	{
		Long lBatchCount = equipmentInfo.getBATCHCOUNT();
		if (lBatchCount > 0)
		{
			equipmentInfo.setBATCHCOUNT(lBatchCount-1);
			equipmentInfo.excuteDML(SqlConstant.DML_UPDATE);
	
		    // History 기록
		    AddHistory history = new AddHistory();
		    history.addHistory(equipmentInfo, txnInfo, "U");
		}
		else
		{
			log.info("Equipment Can't Decrease BatchCount. BatchCount is 0 ! (" + equipmentInfo.getKeyEQUIPMENTID().toString() + ")");
		}	
	}

    /**
     * equipmentInfo에 lotInfo를 할당하고 공정진행을 시작합니다
     * 
     * @param equipmentInfo
     * @param lotInfo
     * @param txnInfo
     * @param bChangeEquipmentState
     * @return
     * @throws CustomException
     * 
    */
	public void EquipmentProcessStart(EQUIPMENT equipmentInfo, LOTINFORMATION lotInfo, TxnInfo txnInfo, boolean bChangeEquipmentState)
	{
		equipmentInfo.setLOTID(lotInfo.getKeyLOTID());
		equipmentInfo.setWORKORDER(lotInfo.getWORKORDER());
		equipmentInfo.setREFERENCETYPE("");
		equipmentInfo.setREFERENCEVALUE("");
		if (bChangeEquipmentState)
		{
			// Equipment State Validation 
			if (Constant.EQUIPMENTSTATE_IDLE.equals(equipmentInfo.getEQUIPMENTSTATE()))
			{
				equipmentInfo.setEQUIPMENTSTATE(Constant.EQUIPMENTSTATE_RUN);
			}
			else
			{
				// ({0}) 은 ({1})상태가 아닙니다. 상태를 확인해 주시기 바랍니다.
				//throw new CustomException("EQP-002", new Object[] {equipmentInfo.getKeyEQUIPMENTID(), Constant.EQUIPMENTSTATE_IDLE});
			}
		}
		equipmentInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
	    history.addHistory(equipmentInfo, txnInfo, "U");
		
	}

    /**
     * equipemntInfo를 공정진행시작 전의 상태로 되돌립니다
     * 
     * @param equipmentInfo
     * @param txnInfo
     * @return
     * @throws CustomException
     * 
    */
	public void EquipmentProcessStartCancel(EQUIPMENT equipmentInfo, TxnInfo txnInfo)
	{
		equipmentInfo.setLOTID("");
		equipmentInfo.setWORKORDER("");
		equipmentInfo.setREFERENCETYPE("");
		equipmentInfo.setREFERENCEVALUE("");
		// Equipment State Validation 
		if (Constant.EQUIPMENTSTATE_RUN.equals(equipmentInfo.getEQUIPMENTSTATE()))
		{
			equipmentInfo.setEQUIPMENTSTATE(Constant.EQUIPMENTSTATE_IDLE);
		}
		else
		{
			// ({0}) 은 ({1})상태가 아닙니다. 상태를 확인해 주시기 바랍니다.
			//throw new CustomException("EQP-002", new Object[] {equipmentInfo.getKeyEQUIPMENTID(), Constant.EQUIPMENTSTATE_RUN});
		}
		equipmentInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
	    history.addHistory(equipmentInfo, txnInfo, "U");
		
	}
	
    /**
     * equipmentInfo의 공정진행을 종료합니다
     * 
     * @param equipmentInfo
     * @param txnInfo
     * @param bChangeEquipmentState
     * @return 
     * @throws CustomException
     * 
    */
	public void EquipmentProcessEnd(EQUIPMENT equipmentInfo, TxnInfo txnInfo, boolean bChangeEquipmentState)
	{
		Long lBatchCount = ConvertUtil.Object2Long4Zero(equipmentInfo.getBATCHCOUNT());
		equipmentInfo.setLOTID("");
		equipmentInfo.setWORKORDER("");
		equipmentInfo.setREFERENCETYPE("");
		equipmentInfo.setREFERENCEVALUE("");
		if (bChangeEquipmentState)
		{
			// Equipment State Validation 
			if (Constant.EQUIPMENTSTATE_RUN.equals(equipmentInfo.getEQUIPMENTSTATE()))
			{
				equipmentInfo.setEQUIPMENTSTATE(Constant.EQUIPMENTSTATE_IDLE);
			}
			else
			{
				// ({0}) 은 ({1})상태가 아닙니다. 상태를 확인해 주시기 바랍니다.
				//throw new CustomException("EQP-002", new Object[] {equipmentInfo.getKeyEQUIPMENTID(), Constant.EQUIPMENTSTATE_RUN});
			}
		}
		equipmentInfo.setBATCHCOUNT(lBatchCount+1);
		equipmentInfo.excuteDML(SqlConstant.DML_UPDATE);

		// 해당 설비의 PM 주기가 Lot 진행수 일 경우 자동으로 처리할지 여부에 따라 자동으로 PM 상태로 변경한다.
		// -> PM 주기를 조회하여 PM을 해야 하는 설비에 대한 UI 개발 예정
		
	    // History 기록
	    AddHistory history = new AddHistory();
	    txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
	    history.addHistory(equipmentInfo, txnInfo, "U");
	}

    /**
     * equipemntInfo를 공정진행종료 전의 상태로 되돌립니다
     * 
     * @param EQUIPMENT
     * @param LOTINFORMATION
     * @param TxnInfo
     * @return
     * @throws CusotmException
     * 
    */
	public void EquipmentProcessEndCancel(EQUIPMENT equipmentInfo, LOTINFORMATION lotInfo, TxnInfo txnInfo)
	{
		Long lBatchCount = equipmentInfo.getBATCHCOUNT();
		if (lBatchCount > 0)
		{
			equipmentInfo.setBATCHCOUNT(lBatchCount-1);
		}
		else
		{
			log.info("Equipment Can't Decrease BatchCount. BatchCount is 0 ! (" + equipmentInfo.getKeyEQUIPMENTID().toString() + ")");
		}
		
		// 마지막 진행 Lot ID 가져오기 
		equipmentInfo.setLOTID(lotInfo.getKeyLOTID());
		equipmentInfo.setWORKORDER(lotInfo.getWORKORDER());
		equipmentInfo.setREFERENCETYPE("");
		equipmentInfo.setREFERENCEVALUE("");
		
		// Equipment State Validation 
		if (Constant.EQUIPMENTSTATE_IDLE.equals(equipmentInfo.getEQUIPMENTSTATE()))
		{
			equipmentInfo.setEQUIPMENTSTATE(Constant.EQUIPMENTSTATE_RUN);
		}
		else
		{
			// ({0}) 은 ({1})상태가 아닙니다. 상태를 확인해 주시기 바랍니다.
			//throw new CustomException("EQP-002", new Object[] {equipmentInfo.getKeyEQUIPMENTID(), Constant.EQUIPMENTSTATE_IDLE});
		}
		
		equipmentInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
	    history.addHistory(equipmentInfo, txnInfo, "U");
	}
	
    /**
     * PM 작업 시작
     * 
     * @param HashMap<String, String>
     * @param TxnInfo
     * @return
     * 
    */
	@SuppressWarnings("unchecked")
	public void pmStart(HashMap<String, String> dataMap, TxnInfo txnInfo) {
		txnInfo.setTxnId(Constant.PM_REFERENCETYPE_PMSTART);
		
        EQUIPMENTPMSCHEDULE equipmentPMScheduleInfo = new EQUIPMENTPMSCHEDULE();
        equipmentPMScheduleInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        equipmentPMScheduleInfo.setKeyPMSCHEDULEID(dataMap.get("PMSCHEDULEID"));
        equipmentPMScheduleInfo = (EQUIPMENTPMSCHEDULE) equipmentPMScheduleInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
        
        Timestamp pmStartTime = equipmentPMScheduleInfo.getPMSTARTTIME();
        if ( !ConvertUtil.toString(pmStartTime).equals("") ) {
        	// 해당 설비({0})에 대한 긴급/예방 보전({1})이 이미 시작되어 보전작업이 진행중입니다.
			throw new CustomException("EQP-005", new Object[] {equipmentPMScheduleInfo.getKeyEQUIPMENTID(), equipmentPMScheduleInfo.getREFERENCEVALUE()});
        }
        
        // 이전 미진행 PM 체크
        checkPMEquipment(equipmentPMScheduleInfo.getKeyPLANTID(), equipmentPMScheduleInfo.getKeyEQUIPMENTID(), equipmentPMScheduleInfo.getKeyPMSCHEDULEID());
        
        EQUIPMENTPMSCHEDULE equipmentPMCheckInfo = new EQUIPMENTPMSCHEDULE();
        equipmentPMCheckInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        equipmentPMCheckInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
        equipmentPMCheckInfo.setPMENDTIME(null);
        List<Object> listEquipmentPMCheckInfo = (List<Object>)(equipmentPMCheckInfo.excuteDML(SqlConstant.DML_SELECTLIST));
        
        for ( int i = 0; i < listEquipmentPMCheckInfo.size(); i++ ) {
        	Timestamp checkPMStartTime = ((EQUIPMENTPMSCHEDULE)listEquipmentPMCheckInfo.get(i)).getPMSTARTTIME();
        	Timestamp checkPMEndTime = ((EQUIPMENTPMSCHEDULE)listEquipmentPMCheckInfo.get(i)).getPMENDTIME();
            if ( !ConvertUtil.toString(checkPMStartTime).equals("") && ConvertUtil.toString(checkPMEndTime).equals("") ) {
            	// 동일 장비에 긴급/예방 보전 작업이 이미 진행중입니다. 스케줄 코드 : ({0})
				throw new CustomException("EQP-006",new Object[] {((EQUIPMENTPMSCHEDULE)listEquipmentPMCheckInfo.get(i)).getKeyPMSCHEDULEID()});
            }
        }
        
		equipmentPMScheduleInfo.setWORKERUSERID(dataMap.get("WORKERUSERID"));
		equipmentPMScheduleInfo.setWORKCOMMENT(dataMap.get("WORKCOMMENT"));
		equipmentPMScheduleInfo.setWORKRESULT(dataMap.get("WORKRESULT"));
		equipmentPMScheduleInfo.setPMSTARTTIME(DateUtil.getTimestamp(dataMap.get("PMSTARTTIME")));
		equipmentPMScheduleInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		AddHistory oAddHistory = new AddHistory();
		oAddHistory.addHistory(equipmentPMScheduleInfo, txnInfo, SqlConstant.DML_UPDATE);
		
		EQUIPMENT equipmentInfo = new EQUIPMENT();
		equipmentInfo.setKeyPLANTID(dataMap.get("PLANTID"));
		equipmentInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
		equipmentInfo = (EQUIPMENT) equipmentInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		this.pmWork_Equipment(equipmentInfo, equipmentPMScheduleInfo, Constant.EQUIPMENTSTATE_PM, txnInfo);
    }
	
    /**
     * PM 작업 종료
     * 
     * @param HashMap<String, String>
     * @param TxnInfo
     * @return
     * 
    */
	public void pmEnd(HashMap<String, String> dataMap, TxnInfo txnInfo) {
		txnInfo.setTxnId(Constant.PM_REFERENCETYPE_PMEND);
		
        EQUIPMENTPMSCHEDULE equipmentPMScheduleInfo = new EQUIPMENTPMSCHEDULE();
        equipmentPMScheduleInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        equipmentPMScheduleInfo.setKeyPMSCHEDULEID(dataMap.get("PMSCHEDULEID"));
        equipmentPMScheduleInfo = (EQUIPMENTPMSCHEDULE) equipmentPMScheduleInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
        Timestamp pmStartTime = equipmentPMScheduleInfo.getPMSTARTTIME();
        Timestamp pmEndTime = equipmentPMScheduleInfo.getPMENDTIME();
        if ( !ConvertUtil.toString(pmEndTime).equals("") ) {
        	// 해당 긴급/예방 보전 작업({0})이 이미 완료되었습니다. 스케줄 코드 : ({1})
			throw new CustomException("EQP-007", new Object[] {equipmentPMScheduleInfo.getREFERENCEVALUE(), equipmentPMScheduleInfo.getKeyPMSCHEDULEID()});
        }
        else if ( ConvertUtil.toString(pmStartTime).equals("") ) {
			// 해당 긴급/예방 보전 작업({0})이 시작 취소되었습니다. 스케줄 코드 : ({1})
        	throw new CustomException("EQP-008", new Object[] {equipmentPMScheduleInfo.getREFERENCEVALUE(), equipmentPMScheduleInfo.getKeyPMSCHEDULEID()});
        }
        
        // 이전 미진행 PM 체크
        checkPMEquipment(equipmentPMScheduleInfo.getKeyPLANTID(), equipmentPMScheduleInfo.getKeyEQUIPMENTID(), equipmentPMScheduleInfo.getKeyPMSCHEDULEID());
        
        equipmentPMScheduleInfo.setWORKERUSERID(dataMap.get("WORKERUSERID"));
		equipmentPMScheduleInfo.setWORKCOMMENT(dataMap.get("WORKCOMMENT"));
		equipmentPMScheduleInfo.setWORKRESULT(dataMap.get("WORKRESULT"));
		equipmentPMScheduleInfo.setPMENDTIME(DateUtil.getTimestamp(dataMap.get("PMENDTIME")));
		equipmentPMScheduleInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		AddHistory oAddHistory = new AddHistory();
		oAddHistory.addHistory(equipmentPMScheduleInfo, txnInfo, SqlConstant.DML_UPDATE);
		
		EQUIPMENT equipmentInfo = new EQUIPMENT();
		equipmentInfo.setKeyPLANTID(dataMap.get("PLANTID"));
		equipmentInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
		equipmentInfo = (EQUIPMENT) equipmentInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		equipmentInfo.setLASTMAINTENANCETIME(equipmentPMScheduleInfo.getPMENDTIME());
		equipmentInfo.setLASTMAINTENANCEUSERID(equipmentPMScheduleInfo.getWORKERUSERID());
		
		this.pmWork_Equipment(equipmentInfo, equipmentPMScheduleInfo, Constant.EQUIPMENTSTATE_IDLE, txnInfo);
	}
	
	public void uploadImage(HashMap<String, String> dataMap, TxnInfo txnInfo, String pmWorkType)
	{
        NameGenerator nameGenerator = new NameGenerator();
    	String plantID = dataMap.get("PLANTID");
    	String pmScheduleID = dataMap.get("PMSCHEDULEID");
    	String equipmentID = dataMap.get("EQUIPMENTID");
    	String equipmentImageType = dataMap.get("EQUIPMENTIMAGETYPE");
    	String equipmentImageID = nameGenerator.nameGenerate(plantID, "EquipmentImageID", new Object[] {});
    	String equipmentImage = dataMap.get("EQUIPMENTIMAGE");
    	Timestamp createTime = DateUtil.getTimestamp(dataMap.get("CREATETIME"));
    	String description = dataMap.get("DESCRIPTION");
        
    	EQUIPMENTIMAGE equipmentImageInfo = new EQUIPMENTIMAGE();
    	equipmentImageInfo.setKeyPLANTID(plantID);
    	equipmentImageInfo.setKeyEQUIPMENTIMAGEID(equipmentImageID);
    	equipmentImageInfo.setPMSCHEDULEID(pmScheduleID);
    	equipmentImageInfo.setEQUIPMENTID(equipmentID);
    	equipmentImageInfo.setEQUIPMENTIMAGETYPE(equipmentImageType);
    	equipmentImageInfo.setEQUIPMENTIMAGE(equipmentImage);
    	equipmentImageInfo.setIMAGECREATETIME(createTime);
    	equipmentImageInfo.setDESCRIPTION(description);
    	equipmentImageInfo.excuteDML(SqlConstant.DML_INSERT);
    	
    	AddHistory history = new AddHistory();
    	history.addHistory(equipmentImageInfo, txnInfo, "C");
	}

    /**
     * PM 미진행 작업 여부 체크
     * 
     * @param String
     * @param String
     * @param String
     * @param String
     * @return
     * 
    */
	public void checkPMEquipment(String sPlantID, String sEquipmentID, String sPMScheduleID) {
        String sQuerySql = " SELECT A.PLANTID"
        				+ " ,A.EQUIPMENTID "
        				+ " ,A.PMSCHEDULEID "
        				+ " ,CONVERT(CHAR(19), A.PMPLANNEDTIME, 120) AS PMPLANNEDTIME "
        				+ " ,A.REFERENCEVALUE "
        		+ " FROM EQUIPMENTPMSCHEDULE A, EQUIPMENTPMSCHEDULE B "
				+ " WHERE A.PLANTID = :PLANTID "
				+ " AND A.EQUIPMENTID = :EQUIPMENTID "
				+ " AND A.PLANTID = B.PLANTID "
				+ " AND A.EQUIPMENTID = B.EQUIPMENTID "
				+ " AND A.REFERENCEVALUE = B.REFERENCEVALUE "
				+ " AND B.PMSCHEDULEID = :PMSCHEDULEID "
				+ " AND A.PMPLANNEDTIME < B.PMPLANNEDTIME "
				+ " AND ( A.PMSTARTTIME IS NULL OR A.PMENDTIME IS NULL ) "
        		+ " ORDER BY A.PMPLANNEDTIME ";
				
		HashMap<String, Object> mapBindData = new HashMap<String, Object>();
		mapBindData.put("PLANTID", sPlantID);
		mapBindData.put("EQUIPMENTID", sEquipmentID);
		mapBindData.put("PMSCHEDULEID", sPMScheduleID);
		
		List<HashMap<String, String>> listScheduleData = SqlMesTemplate.queryForList(sQuerySql, mapBindData);
		
		if ( listScheduleData.size() > 0 ) {
			// 현재 진행 PM 이전의 미진행 PM이 남아있는 경우
			
			HashMap<String, String> mapScheduleData = listScheduleData.get(0);
			
			// 장비({0}), 보전 작업({1})과 동일한 이전 미진행 작업이 존재하여 처리 할 수 없습니다. 이전 미진행 작업({2})을 진행하여 주시기 바랍니다.
			throw new CustomException("EQP-011", new Object[] {mapScheduleData.get("EQUIPMENTID"), mapScheduleData.get("REFERENCEVALUE"), mapScheduleData.get("PMPLANNEDTIME")});
		}
	}
	
    /**
     * 현재 시간 이후 PM 진행중 작업 여부 체크
     * 
     * @param String
     * @param String
     * @param String
     * @param String
     * @return
     * 
    */
	public void checkRunningPMEquipment(String sPlantID, String sEquipmentID, String sEquipmentName, String sWorkCenter) {
        String sQuerySql = " SELECT A.PLANTID"
        				+ " ,A.EQUIPMENTID "
        				+ " ,B.EQUIPMENTNAME "
        				+ " ,A.PMSCHEDULEID "
        				+ " ,CONVERT(CHAR(19), A.PMPLANNEDTIME, 120) AS PMPLANNEDTIME "
        				+ " ,A.REFERENCEVALUE "
        		+ " FROM EQUIPMENTPMSCHEDULE A "
        		+ " 	INNER JOIN EQUIPMENTDEFINITION B ON A.PLANTID = B.PLANTID AND A.EQUIPMENTID = B.EQUIPMENTID "
        		+ " 		AND B.EQUIPMENTID LIKE '%' + IIF(:EQUIPMENTID IS NULL, '', :EQUIPMENTID) + '%' "
        		+ " 		AND B.EQUIPMENTNAME LIKE '%' + IIF(:EQUIPMENTNAME IS NULL, '', :EQUIPMENTNAME) + '%' "
        		+ " 		AND ISNULL(B.AREAID, '') = IIF(ISNULL(:WORKCENTER, '') = '', ISNULL(B.AREAID, ''), :WORKCENTER) "
				+ " WHERE A.PLANTID = :PLANTID "
				+ " AND A.PMPLANNEDTIME > GETDATE() "
				+ " AND ( A.PMSTARTTIME IS NOT NULL OR A.PMENDTIME IS NOT NULL ) "
				+ " AND A.PMSCHEDULETYPE = 'Modeling' "
        		+ " ORDER BY PMPLANNEDTIME, EQUIPMENTID ";
				
		HashMap<String, Object> mapBindData = new HashMap<String, Object>();
		mapBindData.put("PLANTID", sPlantID);
		mapBindData.put("EQUIPMENTID", sEquipmentID);
		mapBindData.put("EQUIPMENTNAME", sEquipmentName);
		mapBindData.put("WORKCENTER", sWorkCenter);
		
		List<HashMap<String, String>> listScheduleData = SqlMesTemplate.queryForList(sQuerySql, mapBindData);
		
		if ( listScheduleData.size() > 0 ) {
			// 현재 시간 이후의 진행중 PM 작업이 존재
			HashMap<String, String> mapScheduleData = listScheduleData.get(0);
			
			// 현재 시간 이후의 진행중인 보전 작업이 존재합니다.( 설비 : ({0}), 보전작업항목코드 : ({1}), 보전작업계획시간 : ({2}) ) 확인 후 초기화 진행해 주시기 바랍니다.
			throw new CustomException("EQP-012", new Object[] {mapScheduleData.get("EQUIPMENTID"), mapScheduleData.get("REFERENCEVALUE"), mapScheduleData.get("PMPLANNEDTIME")});
		}
	}
	
    /**
     * 현재 시간 이후 PM 설정된 설비 목록 조회
     * 
     * @param String
     * @param String
     * @param String
     * @param String
     * @return List<HashMap<String, String>>
     * 
    */
	public List<HashMap<String, String>> getPMEquipment(String sPlantID, String sEquipmentID, String sEquipmentName, String sWorkCenter) {
        String sQuerySql = " SELECT A.PLANTID"
        				+ " ,A.EQUIPMENTID "
        				+ " ,B.EQUIPMENTNAME "
        				+ " ,A.PMSCHEDULEID "
        				+ " ,CONVERT(CHAR(19), A.PMPLANNEDTIME, 120) AS PMPLANNEDTIME "
        				+ " ,A.REFERENCEVALUE "
        		+ " FROM EQUIPMENTPMSCHEDULE A "
        		+ " 	INNER JOIN EQUIPMENTDEFINITION B ON A.PLANTID = B.PLANTID AND A.EQUIPMENTID = B.EQUIPMENTID "
        		+ " 		AND B.EQUIPMENTID LIKE '%' + IIF(:EQUIPMENTID IS NULL, '', :EQUIPMENTID) + '%' "
        		+ " 		AND B.EQUIPMENTNAME LIKE '%' + IIF(:EQUIPMENTNAME IS NULL, '', :EQUIPMENTNAME) + '%' "
        		+ " 		AND ISNULL(B.AREAID, '') = IIF(ISNULL(:WORKCENTER, '') = '', ISNULL(B.AREAID, ''), :WORKCENTER) "
				+ " WHERE A.PLANTID = :PLANTID "
				+ " AND A.PMPLANNEDTIME > GETDATE() "
				+ " AND A.PMSCHEDULETYPE = 'Modeling' "
        		+ " ORDER BY EQUIPMENTID ";
				
		HashMap<String, Object> mapBindData = new HashMap<String, Object>();
		mapBindData.put("PLANTID", sPlantID);
		mapBindData.put("EQUIPMENTID", sEquipmentID);
		mapBindData.put("EQUIPMENTNAME", sEquipmentName);
		mapBindData.put("WORKCENTER", sWorkCenter);
		
		List<HashMap<String, String>> listScheduleEQP = SqlMesTemplate.queryForList(sQuerySql, mapBindData);
		
		return listScheduleEQP;
	}
}
