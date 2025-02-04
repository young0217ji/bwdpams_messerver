package mes.material.validation;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.util.ConvertUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.master.data.STOCKMONTH;
import mes.master.data.WAREHOUSE;
import mes.material.data.MATERIALSTOCK;
import mes.material.data.MATERIALSTOCKHISTORY;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class MaterialValidation
{
	public static final int CHECK_MINUSSTOCK = 1;
	public static final int CHECK_MATERIALRECEIVECANCEL = 2;
	public static final int CHECK_MONTHSTATE = 4;

	@SuppressWarnings( "rawtypes" )
	public static void checkKeyNull( HashMap hashMap, Object... arg )
	{
		for( int i = 0; i < arg.length; i++ )
		{
			if( hashMap.get( arg[i] ) == null || hashMap.get( arg[i] ) == "" )
			{
				// 필수 입력 항목 ({0}) 의 입력된 값이 없습니다.
				throw new CustomException( "CHECK-001", arg[i] );
			}
		}
	}

	@SuppressWarnings( "rawtypes" )
	public static void checkListNull( List list )
	{
		checkListNull( list, "" );
	}

	@SuppressWarnings( "rawtypes" )
	public static void checkListNull( List list, String listName )
	{
		if( list == null || list.isEmpty() )
		{
			// 필수 입력 항목에 대한 정의가 약속 되지 않았습니다. DATALIST ID = "{0}"
			throw new CustomException( "CHECK-002", new Object[] { listName } );
		}
	}

	public static boolean checkMinusStock( MATERIALSTOCK stockInfo )
	{
		String plantID = stockInfo.getKeyPLANTID();
		String warehouseID = stockInfo.getKeyWAREHOUSEID();
		Double availableStockQty = ConvertUtil.doubleSubtract(stockInfo.getSTOCKQTY(),  stockInfo.getHOLDQTY());

		WAREHOUSE warehouseInfo = new WAREHOUSE();
		warehouseInfo.setKeyPLANTID( plantID );
		warehouseInfo.setKeyWAREHOUSEID( warehouseID );
		warehouseInfo = ( WAREHOUSE )warehouseInfo.excuteDML( SqlConstant.DML_SELECTROW );

		if( warehouseInfo == null )
		{
			// 해당 창고는 존재하지 않습니다.
			throw new CustomException( "MM-002", new Object[] { warehouseID } );
		}

		if( warehouseInfo.getSTOCKCHECKFLAG().equals( Constant.FLAG_YESNO_YES ) )
		{
			if( availableStockQty < 0 )
			{
				// 해당 창고에 마이너스 재고를 입력할 수 없습니다.
				throw new CustomException( "MM-001", new Object[] { warehouseID } );
			}
			else
			{
				return true;
			}
		}
		else
		{
			return true;
		}
	}

	@SuppressWarnings( "unchecked" )
	public static boolean checkMaterialReceiveCancel( MATERIALSTOCK stockInfo )
	{
		// MATERIALSTOCKHISTORY
		MATERIALSTOCKHISTORY historyInfo = new MATERIALSTOCKHISTORY();
		historyInfo.setKeyPLANTID( stockInfo.getKeyPLANTID() );
		historyInfo.setKeyYYYYMM( stockInfo.getKeyYYYYMM() );
		historyInfo.setKeyWAREHOUSEID( stockInfo.getKeyWAREHOUSEID() );
		historyInfo.setKeyMATERIALTYPE( stockInfo.getKeyMATERIALTYPE() );
		historyInfo.setKeyMATERIALID( stockInfo.getKeyMATERIALID() );
		historyInfo.setKeyMATERIALLOTID( stockInfo.getKeyMATERIALLOTID() );

		List<MATERIALSTOCKHISTORY> historyList = ( List<MATERIALSTOCKHISTORY> )historyInfo.excuteDML( SqlConstant.DML_SELECTLIST );
		if( historyList.size() > 1 )
		{
			// 해당 Lot은 삭제할 수 없습니다.
			throw new CustomException( "MM-003", new Object[] { historyInfo.getKeyMATERIALLOTID() } );
		}
		else if( historyList.size() == 1 )
		{
			return true;
		}
		else
		{
			// 해당 Lot은 존재하지 않습니다.
			throw new CustomException( "MM-004", new Object[] { historyInfo.getKeyMATERIALLOTID() } );
		}
	}

	public static boolean checkMonthState( MATERIALSTOCK stockInfo )
	{
		STOCKMONTH stockMonth = new STOCKMONTH();
		stockMonth.setKeyPLANTID( Constant.PLANTID );
		stockMonth = ( STOCKMONTH )stockMonth.excuteDML( SqlConstant.DML_SELECTROW );

		if( !stockMonth.getKeyYYYYMM().equals( stockInfo.getKeyYYYYMM() ) )
		{
			throw new CustomException( "MM-005", new Object[] { stockMonth.getKeyYYYYMM(), stockInfo.getKeyYYYYMM() } );
		}
		else
		{
			if( stockMonth.getSTATE().equals( "Closing" ) )
			{
				throw new CustomException( "MM-006", new Object[] { stockMonth.getSTATE() } );
			}
			else
			{
				return true;
			}
		}
	}

	
	public static boolean checkMonthState( String yyyyMM )
	{
		STOCKMONTH stockMonth = new STOCKMONTH();
		stockMonth.setKeyPLANTID( Constant.PLANTID );
		stockMonth = ( STOCKMONTH )stockMonth.excuteDML( SqlConstant.DML_SELECTROW );

		if( !stockMonth.getKeyYYYYMM().equals( yyyyMM ) )
		{
			throw new CustomException( "MM-005", new Object[] { stockMonth.getKeyYYYYMM(), yyyyMM } );
		}
		else
		{
			if( stockMonth.getSTATE().equals( "Closing" ) )
			{
				throw new CustomException( "MM-006", new Object[] { stockMonth.getSTATE() } );
			}
			else
			{
				return true;
			}
		}
	}

	public static boolean checkStockPolicy( MATERIALSTOCK stockInfo, int condition )
	{
		if( ( condition & CHECK_MINUSSTOCK ) == CHECK_MINUSSTOCK )
		{
			checkMinusStock( stockInfo );
		}
		if( ( condition & CHECK_MATERIALRECEIVECANCEL ) == CHECK_MATERIALRECEIVECANCEL )
		{
			checkMaterialReceiveCancel( stockInfo );
		}
		if( ( condition & CHECK_MONTHSTATE ) == CHECK_MONTHSTATE )
		{
			checkMonthState( stockInfo );
		}

		return true;
	}
}
