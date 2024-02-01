package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_SHIFTDETAIL;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.05
 * 
 * @see
 */
public class TxnShiftDetail implements ObjectExecuteService {

	/**
	 * Shift 상세 기준정보를 등록, 수정, 삭제하는 트랜잭션 실행
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
            
            DY_SHIFTDETAIL dataInfo = new DY_SHIFTDETAIL();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeySHIFTID(dataMap.get("SHIFTID"));
            dataInfo.setKeySEQUENCE(Integer.parseInt(dataMap.get("SEQUENCE")));
            
            // Key에 대한 DataInfo 조회
            if(!dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo = (DY_SHIFTDETAIL) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            dataInfo.setWORKTYPE(dataMap.get("WORKTYPE"));
            dataInfo.setSTARTTIME(dataMap.get("STARTTIME"));
            dataInfo.setENDTIME(dataMap.get("ENDTIME"));
            dataInfo.setWORKTIME(Integer.parseInt(dataMap.get("WORKTIME")));
            dataInfo.setALLDAY(dataMap.get("ALLDAY"));
            dataInfo.setMONDAY(dataMap.get("MONDAY"));
            dataInfo.setTUESDAY(dataMap.get("TUESDAY"));
            dataInfo.setWEDNESDAY(dataMap.get("WEDNESDAY"));
            dataInfo.setTHURSDAY(dataMap.get("THURSDAY"));
            dataInfo.setFRIDAY(dataMap.get("FRIDAY"));
            dataInfo.setSATURDAY(dataMap.get("SATURDAY"));
            dataInfo.setSUNDAY(dataMap.get("SUNDAY"));
            dataInfo.setCREATETIME(txnInfo.getEventTime());
            dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            
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
