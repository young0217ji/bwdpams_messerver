package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_FOCUSCONTROLDOCUMENT;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.26
 * 
 * @see
 */

public class TxnFocusControlDocument implements ObjectExecuteService {

	/**
	 * 중점관리사항 문서 관리를 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++) {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
            
            DY_FOCUSCONTROLDOCUMENT dataInfo = new DY_FOCUSCONTROLDOCUMENT();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPRODUCTID(dataMap.get("PRODUCTID"));
            
            
            // Key에 대한 DataInfo 조회
            if(!dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo = (DY_FOCUSCONTROLDOCUMENT) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            
            dataInfo.setP_FILELOCATION(dataMap.get("P_FILELOCATION"));
            dataInfo.setP_FILENAME(dataMap.get("P_FILENAME"));
            dataInfo.setQ_FILELOCATION(dataMap.get("Q_FILELOCATION"));
            dataInfo.setQ_FILENAME(dataMap.get("Q_FILENAME"));
            dataInfo.setUSEFLAG(dataMap.get("USEFLAG"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setCREATETIME(txnInfo.getEventTime());
            dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            
            
            // DataInfo에  대한 CUD 실행
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
