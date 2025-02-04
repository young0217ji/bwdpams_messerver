package mes.lot.durable;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.durable.transaction.DurableService;
import mes.event.MessageParse;
import mes.lot.custom.ProcessCustomService;
import mes.lot.data.LOTINFORMATION;
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
public class TxnTrackOut implements ObjectExecuteService
{
	/**
	 * Durable ID 기준으로 Multi Lot에 대한 공정종료하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		LotValidation validation = new LotValidation();
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		ProcessCustomService operationService = new ProcessCustomService();
		DurableService durableService = new DurableService();
		
		validation.checkListNull(dataList);
		
		if ( dataList.size() > 0 ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(0);

			validation.checkKeyNull(dataMap, new Object[] {"DURABLEID", "PROCESSID", "PROCESSSEQUENCE"});
			
			String durableID 				= dataMap.get("DURABLEID");
			String eventTime 				= dataMap.get("EVENTTIME");
			String processID 				= dataMap.get("PROCESSID");
			String processSequence			= dataMap.get("PROCESSSEQUENCE");
			Long routeRelationSequence		= ConvertUtil.Object2Long(dataMap.get("ROUTERELATIONSEQUENCE"));
			
			// EventTime
			if ( eventTime != null && !eventTime.isEmpty() )
			{
				txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			}
			
			
			DURABLEINFORMATION durableInfo = new DURABLEINFORMATION();
			durableInfo.setKeyDURABLEID(durableID);
			durableInfo = (DURABLEINFORMATION) durableInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			durableService.moveOut(durableInfo, txnInfo);
			
			List<Object> listLotInfo = durableService.getLotList(durableInfo);
			
			for ( int i = 0; i < listLotInfo.size(); i++ )
			{
				LOTINFORMATION lotInfo = (LOTINFORMATION) listLotInfo.get(i);
				
				operationService.processEnd(lotInfo.getKeyLOTID(), processID, processSequence, routeRelationSequence, txnInfo, Constant.CONTROL_MODE_MANUAL);
			}
		}
		
		return recvDoc;
	}
}
