package mes.material.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.exception.NoDataFoundException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.material.data.MATERIALSTOCK;
import mes.material.transaction.MaterialService;
import mes.material.validation.MaterialValidation;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.20 
 * 
 * @see
 */
public class TxnMaterialOutput implements ObjectExecuteService
{
	/**
	 * 예비품 출고 관리
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws 
	 * 
	 */
	@Override
	public Object execute( Document recvDoc )
	{
		List<HashMap<String, String>> stockList = MessageParse.getDefaultXmlParse( recvDoc );
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo( recvDoc );
		MaterialService service = new MaterialService();
		MaterialValidation.checkListNull( stockList );

		for ( int i = 0; i < stockList.size(); i++ ) {
			HashMap<String, String> stockMap = stockList.get( i );
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );

			MaterialValidation.checkKeyNull( stockMap, new Object[] { "PLANTID", "WAREHOUSEID", "MATERIALID", "OUTQTY" } );

			// Key Value Setting
			String plantID = stockMap.get("PLANTID");
			String yyyymm = "-";
			String warehouseID = stockMap.get("WAREHOUSEID");
			String materialType = "SparePart";
			String materialID = stockMap.get("MATERIALID");
			String materialLotID = "Z99999999";
			
			// 출고수량
			Double outQty = ConvertUtil.String2Double4Zero(stockMap.get("OUTQTY"));

			MATERIALSTOCK stockInfo = new MATERIALSTOCK();
			stockInfo.setKeyPLANTID( plantID );
			stockInfo.setKeyYYYYMM( yyyymm );
			stockInfo.setKeyWAREHOUSEID( warehouseID );
			stockInfo.setKeyMATERIALTYPE( materialType );
			stockInfo.setKeyMATERIALID( materialID );
			stockInfo.setKeyMATERIALLOTID( materialLotID );
			
			try {
				stockInfo = (MATERIALSTOCK) stockInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
				
				// Data Value Setting
				stockInfo.setLASTEVENTTIMEKEY( txnInfo.getEventTimeKey() );
				stockInfo.setOUTQTY( ConvertUtil.doubleSubtract(stockInfo.getOUTQTY(), outQty) );
				stockInfo.setSTOCKQTY( ConvertUtil.doubleSubtract(stockInfo.getSTOCKQTY(), outQty) );
				
				stockInfo.excuteDML( SqlConstant.DML_UPDATE );
				
				service.addMaterialHistory( stockInfo, txnInfo, outQty );
			}
			catch ( NoDataFoundException e ) {
				// 재고없음으로 생성
				// Data Value Setting
				String receiptDate = "-";
				Double inQty = 0.00;
				Double openingQty = 0.00;
				Double bonusQty = 0.00;
				Double consumeQty = 0.00;
				Double scrapQty = 0.00;
				Double holdQty = 0.00;

				stockInfo.setRECEIPTDATE( receiptDate );
				stockInfo.setLASTEVENTTIMEKEY( txnInfo.getEventTimeKey() );
				stockInfo.setOPENINGQTY( openingQty );
				stockInfo.setINQTY( inQty );
				stockInfo.setBONUSQTY( bonusQty );
				stockInfo.setCONSUMEQTY( consumeQty );
				stockInfo.setSCRAPQTY( scrapQty );
				stockInfo.setOUTQTY( outQty );
				stockInfo.setHOLDQTY( holdQty );
				stockInfo.setSTOCKQTY( ConvertUtil.doubleSubtract(inQty, outQty) );
				
				stockInfo.excuteDML( SqlConstant.DML_INSERT );
				
				service.addMaterialHistory( stockInfo, txnInfo, outQty );
			}
		}

		return recvDoc;
	}
}
