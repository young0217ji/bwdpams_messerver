package mes.material.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.material.data.MATERIALSTOCK;
import mes.material.transaction.MaterialService;
import mes.material.validation.MaterialValidation;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnMaterialReceive implements ObjectExecuteService
{
	@SuppressWarnings( "static-access" )
	@Override
	public Object execute( Document recvDoc )
	{
		List<HashMap<String, String>> stockList = MessageParse.getDefaultXmlParse( recvDoc );
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo( recvDoc );
		NameGenerator nameGenerator = new NameGenerator();
		MaterialValidation validation = new MaterialValidation();
		MaterialService service = new MaterialService();

		validation.checkListNull( stockList );

		for( int i = 0; i < stockList.size(); i++ )
		{
			HashMap<String, String> stockMap = stockList.get( i );
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );

			validation.checkKeyNull( stockMap, new Object[] { "PLANTID", "YYYYMM", "WAREHOUSEID", "MATERIALTYPE", "MATERIALID" } );

			// Key Value Setting
			String plantID = stockMap.get( "PLANTID" );
			String yyyymm = stockMap.get( "YYYYMM" );
			String warehouseID = stockMap.get( "WAREHOUSEID" );
			String materialType = stockMap.get( "MATERIALTYPE" );
			String materialID = stockMap.get( "MATERIALID" );
			String materialLotID = nameGenerator.MaterialLotIDGenerate( plantID, materialType, warehouseID );

			// Data Value Setting
			String receiptDate = stockMap.get( "RECEIPTDATE" );
			String vendor = stockMap.get( "VENDOR" );
			String lastEventTimeKey = txnInfo.getEventTimeKey();
			Double openingQty = ConvertUtil.Object2Double( stockMap.get( "OPENINGQTY" ) );
			Double inQty = ( double )0;
			Double bonusQty = ( double )0;
			Double consumeQty = ( double )0;
			Double scrapQty = ( double )0;
			Double outQty = ( double )0;
			Double holdQty = ( double )0;
			Double stockQty = openingQty;

			MATERIALSTOCK stockInfo = new MATERIALSTOCK();
			stockInfo.setKeyPLANTID( plantID );
			stockInfo.setKeyYYYYMM( yyyymm );
			stockInfo.setKeyWAREHOUSEID( warehouseID );
			stockInfo.setKeyMATERIALTYPE( materialType );
			stockInfo.setKeyMATERIALID( materialID );
			stockInfo.setKeyMATERIALLOTID( materialLotID );

			stockInfo.setRECEIPTDATE( receiptDate );
			stockInfo.setVENDOR( vendor );
			stockInfo.setLASTEVENTTIMEKEY( lastEventTimeKey );
			stockInfo.setOPENINGQTY( openingQty );
			stockInfo.setINQTY( inQty );
			stockInfo.setBONUSQTY( bonusQty );
			stockInfo.setCONSUMEQTY( consumeQty );
			stockInfo.setSCRAPQTY( scrapQty );
			stockInfo.setOUTQTY( outQty );
			stockInfo.setHOLDQTY( holdQty );
			stockInfo.setSTOCKQTY( stockQty );

			if( MaterialValidation.checkStockPolicy( stockInfo, MaterialValidation.CHECK_MINUSSTOCK | MaterialValidation.CHECK_MONTHSTATE ) )
			{
				stockInfo.excuteDML( SqlConstant.DML_INSERT );
				service.addMaterialHistory( stockInfo, txnInfo, openingQty );
			}
		}

		return recvDoc;
	}
}
