package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.txninfo.TxnInfo;
import mes.event.MessageParse;
import mes.util.EventInfoUtil;

public class TxnCreatePlanVersion implements ObjectExecuteService{
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++)
        {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);      
        	SqlMesTemplate.executeProcedure("SP_DYPDBR_PLANNINGVERSION", dataMap);
        }
        return recvDoc;
	}
}
