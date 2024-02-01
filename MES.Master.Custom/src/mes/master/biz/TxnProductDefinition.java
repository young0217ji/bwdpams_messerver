package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.BOMDEFINITION;
import mes.master.data.CONSUMABLEDEFINITION;
import mes.master.data.PRODUCTDEFINITION;
import mes.userdefine.data.PRODUCTDEFINITION_UDF;
import mes.util.CustomQueryUtil;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnProductDefinition implements ObjectExecuteService
{
	/**
	 * 제품 기준정보를  등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute( Document recvDoc )
	{
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse( recvDoc );
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo( recvDoc );

		for( int i = 0; i < masterData.size(); i++ )
		{
			HashMap<String, String> dataMap = ( HashMap<String, String> )masterData.get( i );
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );

			PRODUCTDEFINITION dataInfo = new PRODUCTDEFINITION();

			// Key Value Setting
			dataInfo.setKeyPLANTID( dataMap.get( "PLANTID" ) );
			dataInfo.setKeyPRODUCTID( dataMap.get( "PRODUCTID" ) );

			// Key 에 대한 DataInfo 조회
			if( !dataMap.get( "_ROWSTATUS" ).equals( "C" ) )
			{
				dataInfo = ( PRODUCTDEFINITION )dataInfo.excuteDML( SqlConstant.DML_SELECTFORUPDATE );
			}

			// Data Value Setting
			dataInfo = ( PRODUCTDEFINITION )SqlQueryUtil.returnRowToData( dataInfo, dataMap );
			PRODUCTDEFINITION_UDF prodUDF = new PRODUCTDEFINITION_UDF( dataInfo );
			prodUDF = ( PRODUCTDEFINITION_UDF )SqlQueryUtil.returnRowToData( prodUDF, dataMap );

			/*
			 * dataInfo.setPRODUCTNAME(dataMap.get("PRODUCTNAME"));
			 * dataInfo.setPRODUCTGROUPID(dataMap.get("PRODUCTGROUPID"));
			 * dataInfo.setACTIVESTATE(dataMap.get("ACTIVESTATE"));
			 * dataInfo.setPRODUCTIONTYPE(dataMap.get("PRODUCTIONTYPE"));
			 * dataInfo.setPRODUCTTYPE(dataMap.get("PRODUCTTYPE"));
			 * dataInfo.setPRODUCTQUANTITY
			 * (ConvertUtil.String2Long(dataMap.get("PRODUCTQUANTITY")));
			 * dataInfo
			 * .setESTIMATEDCYCLETIME(ConvertUtil.String2Long(dataMap.get
			 * ("ESTIMATEDCYCLETIME")));
			 * dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
			 * prodUDF.setPACKINGTYPE(dataMap.get("PACKINGTYPE"));
			 */

			dataInfo.setLASTUPDATEUSERID( txnInfo.getTxnUser() );
			dataInfo.setLASTUPDATETIME( txnInfo.getEventTime() );
			// dataInfo.setDESCRIPTION("1");

			// DataInfo에 대한 CUD 실행
			if( dataMap.get( "_ROWSTATUS" ).equals( "D" ) )
			{
				dataInfo.excuteDML( SqlConstant.DML_DELETE );

				// [2023.08.17-구자윤] Bom Data 미사용
				// 세품코드 삭제시 BOM 정보도 삭제한다.
//				deleteBOMData( dataInfo );

			}
			else if( dataMap.get( "_ROWSTATUS" ).equals( "C" ) )
			{
				dataInfo.setCREATEUSERID( txnInfo.getTxnUser() );
				dataInfo.setCREATETIME( txnInfo.getEventTime() );
				dataInfo.excuteDML( SqlConstant.DML_INSERT );

				// [2023.08.17-구자윤] Bom Data 미사용 및 미사용 로직 제거
				// 제품 생성시 기본 BOM 정보 생성한다.
//				makeBOMDefinition( dataInfo, txnInfo );
//				if( dataInfo.getPRODUCTIONTYPE().equals( "Half-Product" ) )
//				{
//					makeConsumableDefinition( dataInfo, txnInfo );
//				}

			}
			else if( dataMap.get( "_ROWSTATUS" ).equals( "U" ) )
			{
				dataInfo.excuteDML( SqlConstant.DML_UPDATE );
			}

			// History 기록이 필요한 경우 사용
			AddHistory history = new AddHistory();
			history.addHistory( dataInfo, txnInfo, dataMap.get( "_ROWSTATUS" ) );
		}

		return recvDoc;
	}

	@SuppressWarnings( "unchecked" )
	public void makeConsumableDefinition( PRODUCTDEFINITION productInfo, TxnInfo txnInfo )
	{
		CONSUMABLEDEFINITION consumableInfo = new CONSUMABLEDEFINITION();
		// Key Value Setting
		consumableInfo.setKeyPLANTID( productInfo.getKeyPLANTID() );
		consumableInfo.setKeyCONSUMABLEID( productInfo.getKeyPRODUCTID() );
		
		List<CONSUMABLEDEFINITION> checkInfo = ( List<CONSUMABLEDEFINITION> )consumableInfo.excuteDML( SqlConstant.DML_SELECTLIST );
		
		if( checkInfo.size() > 0 )
		{
			return;
		}
		
		consumableInfo.setCONSUMABLENAME(productInfo.getPRODUCTNAME());
		consumableInfo.setCONSUMABLETYPE("HalfProduct");
		consumableInfo.setUNIT(productInfo.getPRODUCTUNIT());
		consumableInfo.setCREATETIME(DateUtil.getCurrentTimestamp());
		consumableInfo.setCREATEUSERID(txnInfo.getTxnUser());
		consumableInfo.setLASTUPDATETIME(productInfo.getCREATETIME());
		consumableInfo.setLASTUPDATEUSERID(productInfo.getCREATEUSERID());
		consumableInfo.excuteDML(SqlConstant.DML_INSERT);
	}

	/***************************************************************************
	 * 
	 * @param productInfo
	 * @param txnInfo
	 */
	@SuppressWarnings( "unchecked" )
	public void makeBOMDefinition( PRODUCTDEFINITION productInfo, TxnInfo txnInfo )
	{
		BOMDEFINITION dataInfo = new BOMDEFINITION();
		// Key Value Setting
		dataInfo.setKeyPLANTID( productInfo.getKeyPLANTID() );
		dataInfo.setKeyPRODUCTID( productInfo.getKeyPRODUCTID() );
		dataInfo.setKeyBOMID( "BOM-" + productInfo.getKeyPRODUCTID() );
		dataInfo.setKeyBOMVERSION( Constant.VERSION_DEFAULT );

		// Data Value Setting
		dataInfo.setBOMTYPE( Constant.BOMTYPE_MAIN );
		dataInfo.setCONDITIONTYPE( "None" );
		dataInfo.setCONDITIONID( "None" );
		dataInfo.setSTANDARDVALUE( productInfo.getPRODUCTQUANTITY() );
		dataInfo.setSTANDARDUNIT( productInfo.getPRODUCTUNIT() );
		dataInfo.setACTIVESTATE( Constant.ACTIVESTATE_INACTIVE );

		// 해당 PLANTID, BOMID, BOMVersion 으로 존재하는지 Validation 한다.
		BOMDEFINITION checkInfo = new BOMDEFINITION();
		// Key Value Setting
		checkInfo.setKeyPLANTID( dataInfo.getKeyPLANTID() );
		checkInfo.setKeyBOMID( dataInfo.getKeyBOMID() );
		checkInfo.setKeyBOMVERSION( dataInfo.getKeyBOMVERSION() );
		List<BOMDEFINITION> listBOMDefinition = ( List<BOMDEFINITION> )checkInfo.excuteDML( SqlConstant.DML_SELECTLIST );
		if( listBOMDefinition.size() > 0 )
		{
			// 해당 정보 ({0}) 가 이미 존재합니다. 확인해주시기 바랍니다.
			String sExistData = checkInfo.getKeyPLANTID() + "," + checkInfo.getKeyBOMID() + "," + checkInfo.getKeyBOMVERSION();
			throw new CustomException( "MD-015", new Object[] { sExistData } );
		}

		dataInfo.setCREATEUSERID( txnInfo.getTxnUser() );
		dataInfo.setCREATETIME( txnInfo.getEventTime() );
		dataInfo.excuteDML( SqlConstant.DML_INSERT );

	}

	/***************************************************************************
	 * BOMDEFINITION, BOMDETAIL 데이터 삭제 (PLANTID, PRODUCTID 기준 삭제)
	 * @param productInfo
	 * @param txnInfo
	 */
	public void deleteBOMData( PRODUCTDEFINITION productInfo )
	{
		String sPlantID = productInfo.getKeyPLANTID();
		String sProductID = productInfo.getKeyPRODUCTID();

		CustomQueryUtil.deleteProductDataSetTable( "BOMDEFINITION", sPlantID, sProductID );
		CustomQueryUtil.deleteProductDataSetTable( "BOMDETAIL", sPlantID, sProductID );
	}

}
