package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_CERTIFICATIONMANAGEMENT;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;
/**
 * @author LSMESSolution
 * 
 * @since 2023.11.01
 * 
 * @see
 */
public class TxnCertificationManagement implements ObjectExecuteService {
	/**
	 * 자격 인증관리를 등록, 수정, 삭제하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        NameGenerator nameGenerator = new NameGenerator();
        
        for (int i = 0 ; i < masterData.size(); i ++)
        {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
        	
            DY_CERTIFICATIONMANAGEMENT dataInfo = new DY_CERTIFICATIONMANAGEMENT();
        	
        	// Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyCERTIFICATIONID(nameGenerator.CertificationIDGenerate(dataMap.get("PLANTID"), dataMap.get("CERTIFICATIONTYPE")));
            
            
            // Key에 대한 DataInfo 조회
            if(!dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo = (DY_CERTIFICATIONMANAGEMENT) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            dataInfo.setCERTIFICATIONDATE(dataMap.get("CERTIFICATIONDATE"));
            dataInfo.setCERTIFICATIONTYPE(dataMap.get("CERTIFICATIONTYPE"));    
            dataInfo.setCERTIFICATIONSUBTYPE(dataMap.get("CERTIFICATIONSUBTYPE"));     
            dataInfo.setWORKCENTER(dataMap.get("WORKCENTER"));         
            dataInfo.setCERTIFICATIONWORKER(dataMap.get("CERTIFICATIONWORKER"));          
            dataInfo.setCERTIFICATIONSTATE(dataMap.get("CERTIFICATIONSTATE")); 
            dataInfo.setCHANGEOPERDATE(dataMap.get("CHANGEOPERDATE")); 
            dataInfo.setDISQUALIFICATIONEXPECTDATE(dataMap.get("DISQUALIFICATIONEXPECTDATE")); 
            dataInfo.setDISQUALIFICATIONDATE(dataMap.get("DISQUALIFICATIONDATE")); 
            dataInfo.setDISQUALIFICATIONCOMMENT(dataMap.get("DISQUALIFICATIONCOMMENT")); 
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION")); 
            dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
            dataInfo.setCREATETIME(txnInfo.getEventTime());
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            
            // DataInfo에 대한 CUD 실행
            if(dataMap.get("_ROWSTATUS").equals("D")) {
            	dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }else if(dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }else if(dataMap.get("_ROWSTATUS").equals("U")) {
            	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
        }
        
        return recvDoc;
	}

}
