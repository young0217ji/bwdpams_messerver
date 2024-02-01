package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.equipment.data.DY_PMCODEDEFINITION;
import mes.equipment.data.DY_PMCODEDETAIL;
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
public class TxnPMCodeDefinition implements ObjectExecuteService
{
	/**
	 * PM Code의 기본 속성을 등록, 수정, 삭제 하는 트랜잭션 실행
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
        TxnInfo oDetailTxnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        AddHistory history = new AddHistory();
        
        for (int i = 0 ; i < masterData.size(); i ++) {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_PMCODEDEFINITION oPMCodeInfo = new DY_PMCODEDEFINITION();
            // Key Value Setting
            oPMCodeInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            oPMCodeInfo.setKeyPMCODEID(dataMap.get("PMCODEID"));

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") ) {
            	oPMCodeInfo = (DY_PMCODEDEFINITION) oPMCodeInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            // Data Value Setting
            oPMCodeInfo.setPMCODENAME(dataMap.get("PMCODENAME"));
            oPMCodeInfo.setPMCYCLETYPE(dataMap.get("PMCYCLETYPE"));
            oPMCodeInfo.setPMCYCLEVALUE( ConvertUtil.Object2Long(dataMap.get("PMCYCLEVALUE")) );
            oPMCodeInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            oPMCodeInfo.setLASTUPDATEUSERID( txnInfo.getTxnUser() );
            oPMCodeInfo.setLASTUPDATETIME( txnInfo.getEventTime() );

            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") ) {
            	// Detail Data 삭제
                DY_PMCODEDETAIL oPMCodeDetailInfo = new DY_PMCODEDETAIL();
                oPMCodeDetailInfo.setKeyPLANTID(dataMap.get("PLANTID"));
                oPMCodeDetailInfo.setKeyPMCODEID(dataMap.get("PMCODEID"));
                List<DY_PMCODEDETAIL> listPMCodeDetail = (List<DY_PMCODEDETAIL>) oPMCodeDetailInfo.excuteDML(SqlConstant.DML_SELECTLIST);
                
                for ( int j = 0; j < listPMCodeDetail.size(); j++ ) {
                	oPMCodeDetailInfo = listPMCodeDetail.get(j);
                	oPMCodeDetailInfo.excuteDML(SqlConstant.DML_DELETE);
                	
                	oDetailTxnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
                	
                	history = new AddHistory();
                    history.addHistory(oPMCodeDetailInfo, txnInfo, SqlConstant.DML_DELETE);
        		}
            	
            	oPMCodeInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") ) { 
            	oPMCodeInfo.setCREATEUSERID( txnInfo.getTxnUser() );
            	oPMCodeInfo.setCREATETIME( txnInfo.getEventTime() );
				
            	oPMCodeInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") ) {
            	oPMCodeInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            history = new AddHistory();
            history.addHistory(oPMCodeInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }
}
