package mes.lot.transaction;

import kr.co.mesframe.exception.NoDataFoundException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import mes.constant.Constant;
import mes.lot.data.LOTQUEUETIME;
import mes.lot.data.VIEWLOTTRACKING;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class LotQueueTimeManager
{
	/**
	 * startViewLotInfo와 paramViewLotInfo에 받아온 데이터를 LOTQUEUETIME에 추가/수정
	 * 
	 * @param startViewLotInfo
	 * @param paramViewLotInfo
	 * @param txnInfo
	 * @return
	 * @throws NoDataFoundException
	 * 
	 */
	public static void startLotQueueTime(VIEWLOTTRACKING startViewLotInfo, VIEWLOTTRACKING paramViewLotInfo, TxnInfo txnInfo)
	{
		if ( paramViewLotInfo.getTARGET() != null && !paramViewLotInfo.getTARGET().isEmpty() )
		{
			LOTQUEUETIME timeInfo = new LOTQUEUETIME();
			timeInfo.setKeyLOTID( startViewLotInfo.getKeyLOTID() );
			timeInfo.setKeyRELATIONTIMEKEY( startViewLotInfo.getKeyTIMEKEY() );
			timeInfo.setKeySEQUENCE((long) 0);
			
			try
			{
				timeInfo = (LOTQUEUETIME) timeInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
				
				timeInfo.setPROCESSID( startViewLotInfo.getPROCESSID() );
				timeInfo.setPROCESSNAME( startViewLotInfo.getPROCESSNAME() );
				timeInfo.setPROCESSSEQUENCE( startViewLotInfo.getPROCESSSEQUENCE() );
				
				timeInfo.setRECIPEID( startViewLotInfo.getRECIPEID() );
				timeInfo.setRECIPENAME( startViewLotInfo.getRECIPENAME() );
				timeInfo.setRECIPESEQUENCE( startViewLotInfo.getRECIPESEQUENCE() );
				
				timeInfo.setEQUIPMENTID( paramViewLotInfo.getEQUIPMENTID() );
				timeInfo.setTARGET( paramViewLotInfo.getTARGET() );
				timeInfo.setSPECTYPE( paramViewLotInfo.getSPECTYPE() );
				
				timeInfo.setEVENTTYPE( Constant.EVENT_START );
				timeInfo.setSTARTTIME( txnInfo.getEventTime() );
				timeInfo.setENDTIME(null);
				timeInfo.setSTATUS( Constant.EVENT_START );
				timeInfo.setALARMTIME(null);
				timeInfo.setALARMSTATUS( Constant.ALARM_STATUS_NONE );
				timeInfo.setDESCRIPTION("");

				timeInfo.excuteDML(SqlConstant.DML_UPDATE);
			}
			catch ( NoDataFoundException e )
			{
				timeInfo.setPROCESSID( startViewLotInfo.getPROCESSID() );
				timeInfo.setPROCESSNAME( startViewLotInfo.getPROCESSNAME() );
				timeInfo.setPROCESSSEQUENCE( startViewLotInfo.getPROCESSSEQUENCE() );
				
				timeInfo.setRECIPEID( startViewLotInfo.getRECIPEID() );
				timeInfo.setRECIPENAME( startViewLotInfo.getRECIPENAME() );
				timeInfo.setRECIPESEQUENCE( startViewLotInfo.getRECIPESEQUENCE() );
				
				timeInfo.setEQUIPMENTID( paramViewLotInfo.getEQUIPMENTID() );
				timeInfo.setTARGET( paramViewLotInfo.getTARGET() );
				timeInfo.setSPECTYPE( paramViewLotInfo.getSPECTYPE() );
				
				timeInfo.setEVENTTYPE( Constant.EVENT_START );
				timeInfo.setSTARTTIME( txnInfo.getEventTime() );
				timeInfo.setENDTIME(null);
				timeInfo.setSTATUS( Constant.EVENT_START );
				timeInfo.setALARMTIME(null);
				timeInfo.setALARMSTATUS( Constant.ALARM_STATUS_NONE );
				timeInfo.setDESCRIPTION("");

				timeInfo.excuteDML(SqlConstant.DML_INSERT);
			}
		}
	}
	
	/**
	 * 받아온 정보의 LotID를 가지고 LOTQUEUETIME 순서번호가 null이 아닐 때 생성구분을 Change로 추가
	 * 
	 * @param startViewLotInfo
	 * @param changeViewLotInfo
	 * @param paramViewLotInfo
	 * @param txnInfo
	 * @return 
	 * 
	 */
	public static void changeLotQueueTime(VIEWLOTTRACKING startViewLotInfo, VIEWLOTTRACKING changeViewLotInfo, VIEWLOTTRACKING paramViewLotInfo, TxnInfo txnInfo)
	{
		if ( paramViewLotInfo.getTARGET() != null && !paramViewLotInfo.getTARGET().isEmpty() )
		{
			// Start 정보
			LOTQUEUETIME startTimeInfo = new LOTQUEUETIME();
			startTimeInfo = endLotQueueTime(startViewLotInfo, txnInfo);
			
			LOTQUEUETIME timeInfo = new LOTQUEUETIME();
			timeInfo.setKeyLOTID( startViewLotInfo.getKeyLOTID() );
			timeInfo.setKeyRELATIONTIMEKEY( startViewLotInfo.getKeyTIMEKEY() );
			
			if ( startTimeInfo.getKeySEQUENCE() != null )
			{
				timeInfo.setKeySEQUENCE( startTimeInfo.getKeySEQUENCE() + 1 );
				
				timeInfo.setPROCESSID( changeViewLotInfo.getPROCESSID() );
				timeInfo.setPROCESSNAME( changeViewLotInfo.getPROCESSNAME() );
				timeInfo.setPROCESSSEQUENCE( changeViewLotInfo.getPROCESSSEQUENCE() );
				
				timeInfo.setRECIPEID( changeViewLotInfo.getRECIPEID() );
				timeInfo.setRECIPENAME( changeViewLotInfo.getRECIPENAME() );
				timeInfo.setRECIPESEQUENCE( changeViewLotInfo.getRECIPESEQUENCE() );
				
				timeInfo.setEQUIPMENTID( paramViewLotInfo.getEQUIPMENTID() );
				timeInfo.setTARGET( paramViewLotInfo.getTARGET() );
				timeInfo.setSPECTYPE( paramViewLotInfo.getSPECTYPE() );
				
				timeInfo.setEVENTTYPE( Constant.EVENT_CHANGE );
				timeInfo.setSTARTTIME( txnInfo.getEventTime() );
				timeInfo.setENDTIME(null);
				timeInfo.setSTATUS( Constant.EVENT_START );
				timeInfo.setALARMTIME(null);
				timeInfo.setALARMSTATUS( Constant.ALARM_STATUS_NONE );
				timeInfo.setDESCRIPTION("");

				timeInfo.excuteDML(SqlConstant.DML_INSERT);
			}
			else
			{
				startLotQueueTime(startViewLotInfo, paramViewLotInfo, txnInfo);
			}
		}
	}
	
	/**
	 * startViewLotInfo에 받아온 LotID를 가지고 LOTQUEUETIME 상태를 End로 수정
	 * 
	 * @param startViewLotInfo
	 * @param txnInfo
	 * @return LOTQUEUETIME
	 * @throws NoDataFoundException
	 * 
	 */
	public static LOTQUEUETIME endLotQueueTime(VIEWLOTTRACKING startViewLotInfo, TxnInfo txnInfo)
	{
		LOTQUEUETIME timeInfo = new LOTQUEUETIME();
		timeInfo.setKeyLOTID( startViewLotInfo.getKeyLOTID() );
		timeInfo.setKeyRELATIONTIMEKEY( startViewLotInfo.getKeyTIMEKEY() );
		timeInfo.setSTATUS( Constant.EVENT_START );
		
		try
		{
			timeInfo = (LOTQUEUETIME) timeInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

			timeInfo.setENDTIME( txnInfo.getEventTime() );
			timeInfo.setSTATUS( Constant.EVENT_END );
			timeInfo.setALARMTIME(null);
//			timeInfo.setALARMSTATUS( Constant.ALARM_STATUS_NONE );
			timeInfo.setDESCRIPTION("");

			timeInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
		catch ( NoDataFoundException e )
		{
			// Start 데이터 없음!
		}
		
		return timeInfo;
	}
}
