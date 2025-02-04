package mes.lot.tracking;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.event.MessageParse;
import mes.lot.custom.LotCustomService;
import mes.lot.data.LOTINFORMATION;
import mes.lot.validation.LotValidation;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnLotStartCancel implements ObjectExecuteService
{
	/**
	 * 생산시작된 Lot 대상으로 시작취소하는 트랜잭션 실행
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
		
		txnInfo.setTxnId(Constant.EVENT_LOTSTARTCANCEL);
		
		validation.checkListNull(dataList);
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);

			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );

			validation.checkKeyNull( dataMap, new Object[] {"PLANTID", "LOTID"} );

			String lotID	 		= dataMap.get("LOTID");

			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID(lotID);
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			lotCustomService.startCancelLot(lotInfo.getKeyLOTID(), txnInfo);
		}
		
		return recvDoc;
	}
}
