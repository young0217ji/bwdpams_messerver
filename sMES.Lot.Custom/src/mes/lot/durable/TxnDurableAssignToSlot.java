package mes.lot.durable;

import java.util.HashMap;
import java.util.List;

import mes.durable.transaction.DurableService;
import mes.event.MessageParse;
import mes.lot.data.LOTINFORMATION;
import mes.master.data.DURABLEINFORMATION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnDurableAssignToSlot  implements ObjectExecuteService
{
	/**
	 * Durable Slot에 Lot을 지정하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
    public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		
		DurableService durableService = new DurableService();
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			DURABLEINFORMATION durableInfo = new DURABLEINFORMATION();
			durableInfo.setKeyDURABLEID(dataMap.get("DURABLEID"));
			durableInfo = (DURABLEINFORMATION) durableInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			LOTINFORMATION LotInfo = new LOTINFORMATION();
			
			LotInfo.setKeyLOTID(dataMap.get("LOTID"));
			LotInfo = (LOTINFORMATION) LotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			if(dataMap.get("_ROWSTATUS").equalsIgnoreCase("C"))
			{
				durableService.assignToSlot(LotInfo, durableInfo, dataMap.get("ASSIGNSLOT"), txnInfo);
			}
			else if(dataMap.get("_ROWSTATUS").equalsIgnoreCase("D"))
			{
				durableService.deassign(LotInfo, durableInfo, txnInfo);
			}
		}
		
		return recvDoc;
	}
}
