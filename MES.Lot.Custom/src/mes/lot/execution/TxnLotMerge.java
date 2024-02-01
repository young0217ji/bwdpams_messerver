package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
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
public class TxnLotMerge implements ObjectExecuteService
{
	/**
	 * Source Lot을 Target Lot에 Merge하는 트랜잭션 실행
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
		
		validation.checkListNull(dataList);
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			validation.checkKeyNull(dataMap, new Object[] {"SOURCELOTID", "TARGETLOTID"});
			
			String sourceLotID 		= dataMap.get("SOURCELOTID");
			String targetLotID 		= dataMap.get("TARGETLOTID");
			
			LOTINFORMATION sourceLotInfo = new LOTINFORMATION();
			sourceLotInfo.setKeyLOTID( sourceLotID );
			sourceLotInfo = (LOTINFORMATION) sourceLotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			LOTINFORMATION targetLotInfo = new LOTINFORMATION();
			targetLotInfo.setKeyLOTID( targetLotID );
			targetLotInfo = (LOTINFORMATION) targetLotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			lotCustomService.makeLotMerge(sourceLotInfo, targetLotInfo, txnInfo);
		}
		
		return recvDoc;
	}
}
