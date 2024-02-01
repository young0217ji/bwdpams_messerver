package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.equipment.data.DY_EQUIPMENTMAINTENANCE;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.22 
 * 
 * @see
 */
public class TxnEquipmentMaintenance implements ObjectExecuteService
{
	/**
	 * 설비의 BM에 대한 요청을 등록, 수정 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
	@Override
    public Object execute(Document recvDoc) {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        AddHistory history = new AddHistory();
        NameGenerator nameGenerator = new NameGenerator();
        
        for (int i = 0 ; i < masterData.size(); i ++) {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_EQUIPMENTMAINTENANCE oBMInfo = new DY_EQUIPMENTMAINTENANCE();
            // Key Value Setting
            oBMInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            oBMInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            oBMInfo.setKeyREQUESTID(dataMap.get("REQUESTID"));
            
            String sNewStatus = dataMap.get("TARGETSTATUS");
            if ( sNewStatus == null || sNewStatus.isEmpty() ) {
            	sNewStatus = Constant.EQUIPMENT_BM_REQUEST;
            }
            
            // Key 에 대한 DataInfo 조회
            if ( Constant.EQUIPMENT_BM_REQUEST.equals(sNewStatus) == false ) {
            	oBMInfo = (DY_EQUIPMENTMAINTENANCE) oBMInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            else {
            	String sBMRequestID = nameGenerator.nameGenerate(dataMap.get("PLANTID"), "BMRequestID", new Object[] {});
            	
            	oBMInfo.setKeyREQUESTID(sBMRequestID);
            }
            
            String sOldStatus = oBMInfo.getSTATUS();
            txnInfo.setTxnId(sNewStatus);
            
            // BM 요청
            if ( Constant.EQUIPMENT_BM_REQUEST.equals(sNewStatus) ) {
            	// Data Value Setting
            	oBMInfo.setREASONCODE(dataMap.get("REASONCODE"));
            	oBMInfo.setREASONCODENAME(dataMap.get("REASONCODENAME"));
            	oBMInfo.setREQUESTDETAIL(dataMap.get("REQUESTDETAIL"));
            	oBMInfo.setREQUESTUSERID(dataMap.get("REQUESTUSERID"));
            	oBMInfo.setREQUESTTIME( DateUtil.getTimestamp(dataMap.get("REQUESTTIME")) );
            	oBMInfo.setSTATUS(sNewStatus);
            	oBMInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            	
            	oBMInfo.excuteDML(SqlConstant.DML_INSERT);
            	
            	// History 기록이 필요한 경우 사용
                history = new AddHistory();
                history.addHistory(oBMInfo, txnInfo, SqlConstant.DML_INSERT);
            }
            // BM 요청 취소
            else if ( Constant.EQUIPMENT_BM_CANCEL.equals(sNewStatus) ) {
            	// BM 요청 취소 처리는 Request 상태에서만 가능
            	if ( Constant.EQUIPMENT_BM_REQUEST.equals(sOldStatus) == false ) {
            		// 긴급보전설비({0})의 요청상태({1})는 ({2})상태가 아닙니다. 상태를 확인해 주시기 바랍니다.
    				throw new CustomException("EQP-013", new Object[] {oBMInfo.getKeyEQUIPMENTID(), oBMInfo.getSTATUS(), Constant.EQUIPMENT_BM_REQUEST});
            	}
            	
            	oBMInfo.setSTATUS(sNewStatus);
            	oBMInfo.setWORKERUSERID(dataMap.get("WORKERUSERID"));
            	oBMInfo.setWORKTIME(txnInfo.getEventTime());
            	oBMInfo.setWORKCOMMENT(dataMap.get("WORKCOMMENT"));
            	oBMInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            	oBMInfo.excuteDML(SqlConstant.DML_UPDATE);
            	
            	// History 기록이 필요한 경우 사용
                history = new AddHistory();
                history.addHistory(oBMInfo, txnInfo, SqlConstant.DML_UPDATE);
            }
            // BM 보류
            else if ( Constant.EQUIPMENT_BM_HOLD.equals(sNewStatus) ) { 
            	// BM 보류 처리는 Request 상태에서만 가능
            	if ( Constant.EQUIPMENT_BM_REQUEST.equals(sOldStatus) == false ) {
            		// 긴급보전설비({0})의 요청상태({1})는 ({2})상태가 아닙니다. 상태를 확인해 주시기 바랍니다.
    				throw new CustomException("EQP-013", new Object[] {oBMInfo.getKeyEQUIPMENTID(), oBMInfo.getSTATUS(), Constant.EQUIPMENT_BM_REQUEST});
            	}
            	
            	oBMInfo.setSTATUS(sNewStatus);
            	oBMInfo.setWORKERUSERID(dataMap.get("WORKERUSERID"));
            	oBMInfo.setWORKTIME(txnInfo.getEventTime());
            	oBMInfo.setWORKCOMMENT(dataMap.get("WORKCOMMENT"));
            	oBMInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            	oBMInfo.excuteDML(SqlConstant.DML_UPDATE);
            	
            	// History 기록이 필요한 경우 사용
                history = new AddHistory();
                history.addHistory(oBMInfo, txnInfo, SqlConstant.DML_UPDATE);
            }
            // BM 반려
            else if ( Constant.EQUIPMENT_BM_REJECT.equals(sNewStatus) ) { 
            	// BM 반려 처리는 Request, Hold 상태에서만 가능
            	if ( Constant.EQUIPMENT_BM_REQUEST.equals(sOldStatus) ||  Constant.EQUIPMENT_BM_HOLD.equals(sOldStatus) ) {
            		// Skip
            	}
            	else {
            		// 긴급보전설비({0})의 요청상태({1})는 ({2})상태가 아닙니다. 상태를 확인해 주시기 바랍니다.
    				throw new CustomException("EQP-013", new Object[] {oBMInfo.getKeyEQUIPMENTID(), oBMInfo.getSTATUS(), Constant.EQUIPMENT_BM_REQUEST + ", " + Constant.EQUIPMENT_BM_HOLD});            		
            	}
            	
            	oBMInfo.setSTATUS(sNewStatus);
            	oBMInfo.setWORKERUSERID(dataMap.get("WORKERUSERID"));
            	oBMInfo.setWORKTIME(txnInfo.getEventTime());
            	oBMInfo.setWORKCOMMENT(dataMap.get("WORKCOMMENT"));
            	oBMInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            	oBMInfo.excuteDML(SqlConstant.DML_UPDATE);
            	
            	// History 기록이 필요한 경우 사용
                history = new AddHistory();
                history.addHistory(oBMInfo, txnInfo, SqlConstant.DML_UPDATE);
            }
            // BM 완료
            else if ( Constant.EQUIPMENT_BM_COMPLETE.equals(sNewStatus) ) { 
            	// BM 완료 처리는 Request, Hold 상태에서만 가능
            	if ( Constant.EQUIPMENT_BM_REQUEST.equals(sOldStatus) ||  Constant.EQUIPMENT_BM_HOLD.equals(sOldStatus) ) {
            		// Skip
            	}
            	else {
            		// 긴급보전설비({0})의 요청상태({1})는 ({2})상태가 아닙니다. 상태를 확인해 주시기 바랍니다.
    				throw new CustomException("EQP-013", new Object[] {oBMInfo.getKeyEQUIPMENTID(), oBMInfo.getSTATUS(), Constant.EQUIPMENT_BM_REQUEST + ", " + Constant.EQUIPMENT_BM_HOLD});            		
            	}
            	
            	oBMInfo.setSTATUS(sNewStatus);
            	oBMInfo.setWORKERUSERID(dataMap.get("WORKERUSERID"));
            	oBMInfo.setWORKTIME( DateUtil.getTimestamp(dataMap.get("WORKTIME")) );
            	oBMInfo.setWORKCOMMENT(dataMap.get("WORKCOMMENT"));
            	oBMInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            	oBMInfo.excuteDML(SqlConstant.DML_UPDATE);
            	
            	// History 기록이 필요한 경우 사용
                history = new AddHistory();
                history.addHistory(oBMInfo, txnInfo, SqlConstant.DML_UPDATE);
            }
        }

        return recvDoc;
    }
}
