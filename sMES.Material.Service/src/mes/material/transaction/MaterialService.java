package mes.material.transaction;

import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import mes.material.data.MATERIALSTOCK;
import mes.material.data.MATERIALSTOCKHISTORY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class MaterialService
{

	private static final transient Logger log = LoggerFactory.getLogger( MaterialService.class );

	public void addMaterialHistory( MATERIALSTOCK stockInfo, TxnInfo txnInfo, Double qty )
	{
		addMaterialHistory( stockInfo, txnInfo, txnInfo.getTxnId(), "", qty );
	}

	public void addMaterialHistory( MATERIALSTOCK stockInfo, TxnInfo txnInfo, String txnName, Double qty )
	{
		addMaterialHistory( stockInfo, txnInfo, txnName, "", qty );
	}

	public void addMaterialHistory( MATERIALSTOCK stockInfo, TxnInfo txnInfo, String reasonCode, Double qty, boolean haveReasonCode )
	{
		if( haveReasonCode )
		{
			addMaterialHistory( stockInfo, txnInfo, txnInfo.getTxnId(), reasonCode, qty );
		}
		else
		{
			addMaterialHistory( stockInfo, txnInfo, txnInfo.getTxnId(), "", qty );
		}
	}

	public void addMaterialHistory( MATERIALSTOCK stockInfo, TxnInfo txnInfo, String txnName, String reasonCode, Double qty )
	{
		MATERIALSTOCKHISTORY materialHistory = new MATERIALSTOCKHISTORY();

		// input Key
		materialHistory.setKeyPLANTID( stockInfo.getKeyPLANTID() );
		materialHistory.setKeyYYYYMM( stockInfo.getKeyYYYYMM() );
		materialHistory.setKeyWAREHOUSEID( stockInfo.getKeyWAREHOUSEID() );
		materialHistory.setKeyMATERIALTYPE( stockInfo.getKeyMATERIALTYPE() );
		materialHistory.setKeyMATERIALID( stockInfo.getKeyMATERIALID() );
		materialHistory.setKeyMATERIALLOTID( stockInfo.getKeyMATERIALLOTID() );
		materialHistory.setKeyTIMEKEY( txnInfo.getEventTimeKey() );

		// input Data
		// MATERIALSTOCK Data
		materialHistory.setRECEIPTDATE( stockInfo.getRECEIPTDATE() );
		materialHistory.setVENDOR( stockInfo.getVENDOR() );
		materialHistory.setOPENINGQTY( stockInfo.getOPENINGQTY() );
		materialHistory.setINQTY( stockInfo.getINQTY() );
		materialHistory.setBONUSQTY( stockInfo.getBONUSQTY() );
		materialHistory.setCONSUMEQTY( stockInfo.getCONSUMEQTY() );
		materialHistory.setSCRAPQTY( stockInfo.getSCRAPQTY() );
		materialHistory.setOUTQTY( stockInfo.getOUTQTY() );
		materialHistory.setHOLDQTY( stockInfo.getHOLDQTY() );
		materialHistory.setSTOCKQTY( stockInfo.getSTOCKQTY() );

		// Data created new
		materialHistory.setREASONCODE( reasonCode );
		materialHistory.setQTY( qty );
		materialHistory.setREFERENCETIMEKEY( "" );
		materialHistory.setCONSUMABLETIMEKEY( "" );
		materialHistory.setEVENTNAME( txnName );
		materialHistory.setEVENTTIME( txnInfo.getEventTime() );
		materialHistory.setEVENTUSERID( txnInfo.getTxnUser() );
		materialHistory.setEVENTCOMMENT( txnInfo.getTxnComment() );

		// insert into MaterialHistory
		materialHistory.excuteDML( SqlConstant.DML_INSERT );
	}

}
