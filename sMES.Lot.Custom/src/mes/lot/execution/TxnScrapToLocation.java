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
public class TxnScrapToLocation implements ObjectExecuteService
{
	/**
	 * Lot 의 SubLot을 Scrap 처리하는 트랜잭션 실행
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
		
		// Source Lot 정보
		HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(0);
		
		validation.checkKeyNull(dataMap, new Object[] {"LOTID", "REASONCODE", "REASONCODETYPE"});
		
		String lotID 			= dataMap.get("LOTID");
		String reasonCodeType 	= dataMap.get("REASONCODETYPE");
		String reasonCode 		= dataMap.get("REASONCODE");
		String comment 			= dataMap.get("COMMENT");
		Double scrapQuantity 	= ConvertUtil.Object2Double(dataList.size());
		
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		txnInfo.setTxnReasonCode( reasonCode );
		txnInfo.setTxnReasonCodeType( reasonCodeType );
		txnInfo.setTxnComment( comment );
		
		HashMap<Long, String> mapSubLot = new HashMap<Long, String>();
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataSubMap = (HashMap<String, String>) dataList.get(i);
			
			String locationID 					= dataSubMap.get("LOCATIONID");
			String subLotID 					= dataSubMap.get("SUBLOTID");
			
			SUBLOTDATA subLotInfo = new SUBLOTDATA();
			subLotInfo.setKeySUBLOTID(subLotID);
			subLotInfo.setLOTID(lotInfo.getKeyLOTID());
			subLotInfo.setLOCATIONID( ConvertUtil.Object2Long(locationID) );
			subLotInfo = (SUBLOTDATA) subLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
			
			mapSubLot.put(ConvertUtil.Object2Long(locationID), subLotID);
		}
		
		lotCustomService.makeScrapQuantity(lotInfo, ConvertUtil.doubleMultiply(scrapQuantity, -1.0), mapSubLot, txnInfo);
		
		return recvDoc;
	}
}
