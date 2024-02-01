package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_STANDARDDOCUMENT;
import mes.util.EventInfoUtil;
/**
 * @author LSMESSolution
 * 
 * @since 2023.09.22
 * 
 * @see
 */
public class TxnStandardDocument implements ObjectExecuteService {
	/**
	 * 표준문서 정보를 등록, 수정, 삭제하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++)
        {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
        	
        	DY_STANDARDDOCUMENT dataInfo = new DY_STANDARDDOCUMENT();
        	
        	// Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyDOCUMENTID(dataMap.get("DOCUMENTID"));
            
            // Key에 대한 DataInfo 조회
            if(!dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo = (DY_STANDARDDOCUMENT) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            dataInfo.setSTANDARDCATEGORY(dataMap.get("STANDARDCATEGORY"));
            dataInfo.setDOCUMENTNAME(dataMap.get("DOCUMENTNAME"));    
            dataInfo.setFILELOCATION(dataMap.get("FILELOCATION"));     
            dataInfo.setFILENAME(dataMap.get("FILENAME"));         
            dataInfo.setUSEFLAG(dataMap.get("USEFLAG"));          
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
