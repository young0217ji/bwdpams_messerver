package mes.lot.durable;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.durable.transaction.DurableService;
import mes.event.MessageParse;
import mes.lot.validation.LotValidation;
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
public class TxnDurableStateChange implements ObjectExecuteService
{
	/**
	 * Durable의 상태를 변경하는 트랜잭션 실행
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

		LotValidation validation = new LotValidation();
		DurableService durableService = new DurableService();
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			validation.checkKeyNull(dataMap, new Object[] {"DURABLEID"});
			
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			DURABLEINFORMATION durableInfo = new DURABLEINFORMATION();
			durableInfo.setKeyDURABLEID(dataMap.get("DURABLEID"));
			durableInfo = (DURABLEINFORMATION) durableInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			durableInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
			
			String changeState = dataMap.get("CHANGEDURABLESTATE");
			if ( Constant.DURABLE_STATE_RELEASED.equalsIgnoreCase(changeState) )
			{
				durableService.stateChange(durableInfo, changeState, txnInfo);
			}
			else
			{
				validation.checkKeyNull(dataMap, new Object[] {"REASONCODE", "REASONCODETYPE"});
				
				durableService.stateChange(durableInfo, changeState, dataMap.get("REASONCODE"), dataMap.get("REASONCODETYPE"), txnInfo);
			}
			
		}
		
		return recvDoc;
	}
}
