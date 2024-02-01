package mes.master.biz;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_PLANLINERESULT;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.09
 * 
 * @see
 */
public class TxnLineGanttChart implements ObjectExecuteService {
	
	/**
	 * Gantt Chart 화면에서 라인 생산계획 날짜를 수정하는 트랜잭션 실행
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
            
            DY_PLANLINERESULT dataInfo = new DY_PLANLINERESULT();
            
            dataInfo.setKeyDATAKEY(Integer.parseInt(dataMap.get("DATAKEY")));
            
            dataInfo = (DY_PLANLINERESULT)dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            // INSERT, DELETE 없이 UPDATE만 하는 화면
            dataInfo.setPLANSTARTTIME(new Timestamp(Long.parseLong(dataMap.get("PLANSTARTTIME"))));
            dataInfo.setPLANENDTIME(new Timestamp(Long.parseLong(dataMap.get("PLANENDTIME"))));
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            
            dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            
        }
        
        return recvDoc;
	}

}
