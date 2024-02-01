package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
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
public class TxnPMCodeDetail implements ObjectExecuteService
{
	/**
	 * PM Code 상세의 기본 속성을 등록, 수정, 삭제 하는 트랜잭션 실행
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
        
        for (int i = 0 ; i < masterData.size(); i ++) {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_PMCODEDETAIL oPMCodeDetailInfo = new DY_PMCODEDETAIL();
            // Key Value Setting
            oPMCodeDetailInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            oPMCodeDetailInfo.setKeyPMCODEID(dataMap.get("PMCODEID"));

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") ) {
            	oPMCodeDetailInfo.setKeyCHECKSEQUENCE(dataMap.get("CHECKSEQUENCE"));
            	
            	oPMCodeDetailInfo = (DY_PMCODEDETAIL) oPMCodeDetailInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            else {
                String usrSql = "SELECT ISNULL(MAX(CHECKSEQUENCE), 0) AS CHECKSEQUENCE FROM DY_PMCODEDETAIL WHERE PLANTID = ? AND PMCODEID = ? ";

            	Integer iMaxIndex = SqlMesTemplate.queryForInt(usrSql, new Object[] { dataMap.get("PLANTID"), dataMap.get("PMCODEID") });
                
            	oPMCodeDetailInfo.setKeyCHECKSEQUENCE(ConvertUtil.Object2String( (ConvertUtil.Object2Long4Zero(iMaxIndex) + 1) ));
            }
            
            // Data Value Setting
            oPMCodeDetailInfo.setCHECKITEM(dataMap.get("CHECKITEM"));
            oPMCodeDetailInfo.setCHECKTIME( ConvertUtil.Object2Long(dataMap.get("CHECKTIME")) );
            oPMCodeDetailInfo.setMEASURETOOL(dataMap.get("MEASURETOOL"));
            oPMCodeDetailInfo.setUPPERLIMIT(dataMap.get("UPPERLIMIT"));
            oPMCodeDetailInfo.setTARGETVALUE(dataMap.get("TARGETVALUE"));
            oPMCodeDetailInfo.setLOWERLIMIT(dataMap.get("LOWERLIMIT"));
            oPMCodeDetailInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            oPMCodeDetailInfo.setLASTUPDATEUSERID( txnInfo.getTxnUser() );
            oPMCodeDetailInfo.setLASTUPDATETIME( txnInfo.getEventTime() );

            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") ) {
            	oPMCodeDetailInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") ) { 
            	oPMCodeDetailInfo.setCREATEUSERID( txnInfo.getTxnUser() );
            	oPMCodeDetailInfo.setCREATETIME( txnInfo.getEventTime() );
				
            	oPMCodeDetailInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") ) {
            	oPMCodeDetailInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(oPMCodeDetailInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }
}
