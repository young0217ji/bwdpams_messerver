package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import mes.event.MessageParse;
import mes.lot.custom.LotCustomService;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.SUBLOTDATA;
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
public class TxnTransferToLocation implements ObjectExecuteService
{
	/**
	 * Lot의 Sub Lot을 Target Lot으로 이동하는 트랜잭션 실행
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
		
		// Source 정보
		HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(0);
		
		validation.checkKeyNull(dataMap, new Object[] {"SOURCELOTID", "TARGETLOTID"});
		
		String sourceLotID 		= dataMap.get("SOURCELOTID");
		String targetLotID 		= dataMap.get("TARGETLOTID");
		
		LOTINFORMATION sourceLotInfo = new LOTINFORMATION();
		sourceLotInfo.setKeyLOTID( sourceLotID );
		sourceLotInfo = (LOTINFORMATION) sourceLotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		LOTINFORMATION targetLotInfo = new LOTINFORMATION();
		targetLotInfo.setKeyLOTID( targetLotID );
		targetLotInfo = (LOTINFORMATION) targetLotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		HashMap<Long, String> mapSubLot = new HashMap<Long, String>();
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataSubMap = (HashMap<String, String>) dataList.get(i);
			
			String locationID 					= dataSubMap.get("LOCATIONID");
			String changeLocationID 			= dataSubMap.get("CHANGELOCATIONID");
			String subLotID 					= dataSubMap.get("SUBLOTID");
			
			SUBLOTDATA subLotInfo = new SUBLOTDATA();
			subLotInfo.setKeySUBLOTID(subLotID);
			subLotInfo.setLOTID(sourceLotInfo.getKeyLOTID());
			subLotInfo.setLOCATIONID( ConvertUtil.Object2Long(locationID) );
			subLotInfo = (SUBLOTDATA) subLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
			
			mapSubLot.put(ConvertUtil.Object2Long(changeLocationID), subLotID);
		}
		
		lotCustomService.makeLotTransfer(sourceLotInfo, targetLotInfo, mapSubLot, txnInfo);
		
		return recvDoc;
	}
}
