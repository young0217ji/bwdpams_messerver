package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.equipment.data.DY_EQUIPMENTPMCODEMAP;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.12 
 * 
 * @see
 */
public class TxnEquipmentPMCodeMap implements ObjectExecuteService
{
	/**
	 * 설비와 PM Code의 연결 트랜잭션 실행
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
        
        for (int i = 0 ; i < masterData.size(); i ++) {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_EQUIPMENTPMCODEMAP oPMCodeMapInfo = new DY_EQUIPMENTPMCODEMAP();
            // Key Value Setting
            oPMCodeMapInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            oPMCodeMapInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            oPMCodeMapInfo.setKeyPMCODEID(dataMap.get("PMCODEID"));

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") ) {
            	oPMCodeMapInfo = (DY_EQUIPMENTPMCODEMAP) oPMCodeMapInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            // Data Value Setting
            oPMCodeMapInfo.setPMPLANSTARTTIME( DateUtil.getTimestamp(dataMap.get("PMPLANSTARTTIME")) );
            oPMCodeMapInfo.setPMPLANENDTIME( DateUtil.getTimestamp(dataMap.get("PMPLANENDTIME")) );
            oPMCodeMapInfo.setMANAGERUSERID(dataMap.get("MANAGERUSERID"));
            oPMCodeMapInfo.setWORKERUSERID(dataMap.get("WORKERUSERID"));
            oPMCodeMapInfo.setSCHEDULETYPE(dataMap.get("SCHEDULETYPE"));
            oPMCodeMapInfo.setSCHEDULETVALUE(dataMap.get("SCHEDULETVALUE"));
            oPMCodeMapInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            oPMCodeMapInfo.setLASTUPDATEUSERID( txnInfo.getTxnUser() );
            oPMCodeMapInfo.setLASTUPDATETIME( txnInfo.getEventTime() );

            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") ) {
            	oPMCodeMapInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") ) { 
            	oPMCodeMapInfo.setCREATEUSERID( txnInfo.getTxnUser() );
            	oPMCodeMapInfo.setCREATETIME( txnInfo.getEventTime() );
				
            	oPMCodeMapInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") ) {
            	oPMCodeMapInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            history = new AddHistory();
            history.addHistory(oPMCodeMapInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }
}
