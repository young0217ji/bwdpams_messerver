package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_SELFINSPECTIONHEADER;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.24
 * 
 * @see
 */
public class TxnSelfInspectionHeader implements ObjectExecuteService{
	
	/**
	 * 자주검사 Header를 등록, 수정, 삭제 하는 트랜잭션 실행
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
            
            DY_SELFINSPECTIONHEADER dataInfo = new DY_SELFINSPECTIONHEADER();
            
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPROCESSID(dataMap.get("PROCESSID"));
            dataInfo.setKeyHEADERID(dataMap.get("HEADERID"));
            
            // Key에 대한 DataInfo 조회
            if(!dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo = (DY_SELFINSPECTIONHEADER)dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            dataInfo.setPARENTHEADERID(dataMap.get("PARENTHEADERID"));
            dataInfo.setHEADERNAME(dataMap.get("HEADERNAME"));
            dataInfo.setHEADERLEVEL(Integer.parseInt(dataMap.get("HEADERLEVEL")));
            dataInfo.setCELLTYPE(dataMap.get("CELLTYPE"));
            dataInfo.setENUMID(dataMap.get("ENUMID"));
            dataInfo.setCELLWIDTH(dataMap.get("CELLWIDTH"));
            dataInfo.setSEQUENCE(Integer.parseInt(dataMap.get("SEQUENCE")));
            dataInfo.setRECIPEPARAMETERID(dataMap.get("RECIPEPARAMETERID"));
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
