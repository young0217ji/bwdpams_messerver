package mes.lot.durable;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.durable.transaction.DurableService;
import mes.event.MessageParse;
import mes.lot.data.LOTINFORMATION;
import mes.master.data.DURABLEINFORMATION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnDurableAssign implements ObjectExecuteService
{
	/**
	 * Durable에 Lot을 지정하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
    public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		
		TxnInfo txnInfoLot = EventInfoUtil.setTxnInfo(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		DurableService durableService = new DurableService();
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			DURABLEINFORMATION durableInfo = new DURABLEINFORMATION();
			durableInfo.setKeyDURABLEID(dataMap.get("DURABLEID"));
			durableInfo = (DURABLEINFORMATION) durableInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			LOTINFORMATION LotInfo = new LOTINFORMATION();
			
			LotInfo.setKeyLOTID(dataMap.get("LOTID"));
			LotInfo = (LOTINFORMATION) LotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			// 다른 Carrier에 속해 있을경우 Deassign 해준다.
			if(LotInfo.getCARRIERID() != null && !LotInfo.getCARRIERID().isEmpty())
			{
				txnInfoLot.setTxnId("TxnDurableDeassign");
				DURABLEINFORMATION durableInfoLot = new DURABLEINFORMATION();
				durableInfoLot.setKeyDURABLEID(LotInfo.getCARRIERID());
				durableInfoLot = (DURABLEINFORMATION) durableInfoLot.excuteDML(SqlConstant.DML_SELECTFORUPDATE); //Lot CarrierID
				
				durableService.deassign(LotInfo, durableInfoLot, txnInfoLot);
			}
			
			durableService.assign(LotInfo, durableInfo, txnInfo);
		}
		
		return recvDoc;
	}
}
