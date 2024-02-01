package mes.lot.durable;

import java.util.HashMap;
import java.util.List;

import mes.constant.Constant;
import mes.durable.transaction.DurableService;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
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
public class TxnDurableCleanDirty implements ObjectExecuteService
{
	/**
	 * Durable의 Clean상태를 변경하는 트랜잭션 실행 (Clean <-> dirty)
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
			
			DURABLEINFORMATION durableInfo = new DURABLEINFORMATION();
			durableInfo.setKeyDURABLEID(dataMap.get("DURABLEID"));
			String sCleanState = dataMap.get("CLEANSTATE");
			durableInfo = (DURABLEINFORMATION) durableInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			if("Clean".equalsIgnoreCase(sCleanState))
			{
				if (durableInfo.getDURABLESTATE().equals(Constant.DURABLE_STATE_DIRTY))
				{
					durableService.clean(durableInfo, txnInfo);
				}
				else
				{
					// ({0}) 은 이미 ({1}) 인 상태입니다.
					throw new CustomException("DUR-006", new Object[] {durableInfo.getKeyDURABLEID(), sCleanState});
				}
			}
			else if("Dirty".equalsIgnoreCase(sCleanState))
			{
				if (durableInfo.getDURABLESTATE().equals(Constant.DURABLE_STATE_DIRTY))
				{
					// ({0}) 은 이미 ({1}) 인 상태입니다.
					throw new CustomException("DUR-006", new Object[] {durableInfo.getKeyDURABLEID(), Constant.DURABLE_STATE_DIRTY});
				}
				else
				{
					durableService.dirty(durableInfo, txnInfo);
				}
			}
		}
		
		return recvDoc;
	}
}
