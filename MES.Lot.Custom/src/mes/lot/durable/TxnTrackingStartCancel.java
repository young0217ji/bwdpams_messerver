package mes.lot.durable;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import mes.constant.Constant;
import mes.durable.transaction.DurableService;
import mes.event.MessageParse;
import mes.lot.custom.LotCustomService;
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
public class TxnTrackingStartCancel implements ObjectExecuteService
{
	/**
	 * Durable (or Carrier) 단위로 생산시작을 취소하는 트랜잭션 실행
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
		LotCustomService lotCustomService = new LotCustomService();
		DurableService durableService = new DurableService();
		
		txnInfo.setTxnId(Constant.EVENT_LOTSTARTCANCEL);
		
		validation.checkListNull(dataList);
		
		if ( dataList.size() > 0 ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(0);

			validation.checkKeyNull( dataMap, new Object[] {"DURABLEID"} );

			String durableID 				= dataMap.get("DURABLEID");

			DURABLEINFORMATION durableInfo = new DURABLEINFORMATION();
			durableInfo.setKeyDURABLEID(durableID);
			durableInfo = (DURABLEINFORMATION) durableInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			List<Object> listLotInfo = durableService.getLotList(durableInfo);
			
			for ( int i = 0; i < listLotInfo.size(); i++ )
			{
				LOTINFORMATION lotInfo = (LOTINFORMATION) listLotInfo.get(i);

				lotCustomService.startCancelLot(lotInfo.getKeyLOTID(), txnInfo);
			}
		}
		
		return recvDoc;
	}
}
