package mes.lot.durable;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.durable.transaction.DurableService;
import mes.event.MessageParse;
import mes.master.data.DURABLEDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnCreateDurable implements ObjectExecuteService
{
	/**
	 * Durable 을 생성하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
    public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		DurableService durableService = new DurableService();
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			DURABLEDEFINITION durableDef = new DURABLEDEFINITION();
			durableDef.setKeyPLANTID(dataMap.get("PLANTID"));
			durableDef.setKeySTANDARDDURABLEID(dataMap.get("STANDARDDURABLEID"));
			durableDef = (DURABLEDEFINITION) durableDef.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			durableService.create(durableDef, txnInfo);
		}
		
		return recvDoc;
	}
}
