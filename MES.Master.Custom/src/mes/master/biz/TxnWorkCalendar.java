package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_WORKCALLENDER;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.14
 * 
 * @see
 */
public class TxnWorkCalendar implements ObjectExecuteService {

	/**
	 * 근무시간관리 등록, 수정, 삭제 트랜잭션 실행
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
            
            DY_WORKCALLENDER dataInfo = new DY_WORKCALLENDER();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyWORKCENTER(dataMap.get("WORKCENTER"));
            dataInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            dataInfo.setKeyWORKDATE(dataMap.get("WORKDATE"));
            
            // Key에 대한 DataInfo 조회
            if(!dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo = (DY_WORKCALLENDER) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);	
            }
            dataInfo.setSHIFTID(dataMap.get("SHIFTID"));
            dataInfo.setCONFIRMFLG(Integer.parseInt(dataMap.get("CONFIRMFLG")));
            dataInfo.setWORKTIME(0); // WORKITME 입력값 확인 필요
            dataInfo.setCREATETIME(txnInfo.getEventTime());
            dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            
            if(dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }else if(dataMap.get("_ROWSTATUS").equals("U")) {
            	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
            
            
        }
	
    
        return recvDoc;
	}

}
