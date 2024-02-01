package mes.material.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import mes.event.MessageParse;
import mes.material.data.MATERIALSTOCK;
import mes.material.transaction.MaterialService;
import mes.material.validation.MaterialValidation;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnMaterialReceiveCancel implements ObjectExecuteService
{
	@Override
	public Object execute( Document recvDoc )
	{
		List<HashMap<String, String>> stockList = MessageParse.getDefaultXmlParse( recvDoc );
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo( recvDoc );
		MaterialService service = new MaterialService();

		for( int i = 0; i < stockList.size(); i++ )
		{
			HashMap<String, String> stockMap = stockList.get( i );

			MaterialValidation.checkKeyNull( stockMap, new Object[] { "PLANTID", "YYYYMM", "WAREHOUSEID", "MATERIALTYPE", "MATERIALID", "MATERIALLOTID" } );

			// MATERIALSTOCK
			MATERIALSTOCK stockInfo = new MATERIALSTOCK();

			stockInfo.setKeyPLANTID( stockMap.get( "PLANTID" ) );
			stockInfo.setKeyYYYYMM( stockMap.get( "YYYYMM" ) );
			stockInfo.setKeyWAREHOUSEID( stockMap.get( "WAREHOUSEID" ) );
			stockInfo.setKeyMATERIALTYPE( stockMap.get( "MATERIALTYPE" ) );
			stockInfo.setKeyMATERIALID( stockMap.get( "MATERIALID" ) );
			stockInfo.setKeyMATERIALLOTID( stockMap.get( "MATERIALLOTID" ) );

			stockInfo = ( MATERIALSTOCK )stockInfo.excuteDML( SqlConstant.DML_SELECTFORUPDATE );

			if( MaterialValidation.checkStockPolicy( stockInfo, MaterialValidation.CHECK_MATERIALRECEIVECANCEL | MaterialValidation.CHECK_MINUSSTOCK | MaterialValidation.CHECK_MONTHSTATE ) )
			{
				stockInfo.excuteDML( SqlConstant.DML_DELETE );
				service.addMaterialHistory( stockInfo, txnInfo, stockInfo.getOPENINGQTY() );
			}
		}

		return recvDoc;
	}
}
