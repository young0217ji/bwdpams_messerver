package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_SCHEDULERESULT;
import mes.util.EventInfoUtil;

public class TxnScheduleReorder implements ObjectExecuteService{

	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++)
        {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);      
        	txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
        	
        	DY_SCHEDULERESULT dataInfo = new DY_SCHEDULERESULT();
        	
        	dataInfo.setKeyDATAKEY(Integer.parseInt(dataMap.get("DATAKEY")));
        	dataInfo = (DY_SCHEDULERESULT)dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
        	
        	dataInfo.setDUEDTDATE(DateUtil.getTimestamp(dataMap.get("DUEDATE")));
        	dataInfo.setDAYSEQUENCE(dataMap.get("DAYSEQUENCE"));
        	dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
        	dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
        	
        	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
        }
        HashMap<String,String> pMap = (HashMap<String, String>)masterData.get(0);
        pMap.put("PLNDATE", pMap.get("PLANVERSION")); // PLANVERSION
        pMap.put("WORKCENTER", pMap.get("WORKCENTER")); // WORKCENTER
        pMap.put("STDATE", pMap.get("SEARCHSTDATE")); // SEARCH START DATE
        pMap.put("EDDATE", pMap.get("SEARCHEDDATE")); // SEARCH END DATE
        
        SqlMesTemplate.executeProcedure("SP_DYPDBR_SCHEDULEDAYSQ",pMap);
        return recvDoc;
	}

}
