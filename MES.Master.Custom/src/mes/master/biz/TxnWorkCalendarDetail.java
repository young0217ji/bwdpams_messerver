package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_PRODUCTCYCLETIME;
import mes.master.data.DY_SHIFTDETAIL;
import mes.master.data.DY_WORKCALLENDERDETAIL;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.19
 * 
 * @see
 */

public class TxnWorkCalendarDetail implements ObjectExecuteService {

	/**
	 * 작업자 근무 계획을 등록, 수정, 삭제 하는 트랜잭션 실행
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
            
            DY_WORKCALLENDERDETAIL dataInfo = new DY_WORKCALLENDERDETAIL();
            
            // Key Value Setting
            dataInfo.setPLANTID(dataMap.get("PLANTID"));
            dataInfo.setWORKDATE(dataMap.get("WORKDATE"));
            dataInfo.setPROCESSID(dataMap.get("PROCESSID"));
            dataInfo.setEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            dataInfo.setSHIFTTYPE(dataMap.get("SHIFTTYPE"));
            
            
            // Key에 대한 DataInfo 조회
            if(!dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo = (DY_WORKCALLENDERDETAIL) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            
            dataInfo.setWORKERID(dataMap.get("WORKERID"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
            dataInfo.setCREATETIME(txnInfo.getEventTime());
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            
            
            // DataInfo에  대한 CUD 실행
            if(dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }else if(dataMap.get("_ROWSTATUS").equals("U")) {
            	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
            
		
			}

			return recvDoc;
	}

}
