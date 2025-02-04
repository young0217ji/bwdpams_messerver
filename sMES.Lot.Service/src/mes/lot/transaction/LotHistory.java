package mes.lot.transaction;

import kr.co.mesframe.exception.MESFrameException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.LOTPROCESSHISTORY;
import mes.lot.data.VIEWLOTTRACKING;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class LotHistory
{
	/**
	 * lotInformation과 viewLotInfo에 받아온 정보를 LOTPROCESSHISTORY에 이력 추가
	 * 
	 * @param lotProcessHistory
	 * @param lotInformation
	 * @param viewLotInfo
	 * @param txnInfo
	 * @return 
	 * @throws MESFrameException
	 * @throws Exception
	 * 
	 */
	public void addLotProcessHistory(LOTPROCESSHISTORY lotProcessHistory, LOTINFORMATION lotInformation, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo) 
	{
		if ( viewLotInfo == null)
		{
			viewLotInfo = new VIEWLOTTRACKING();
		}
		
		if ( lotProcessHistory != null && lotProcessHistory.getOLDCURRENTQUANTITY() == null )
		{
			lotProcessHistory.setOLDCURRENTQUANTITY(lotInformation.getCURRENTQUANTITY());
		}
		
		// 기본정보
		lotProcessHistory.setKeyLOTID( lotInformation.getKeyLOTID() );
		lotProcessHistory.setKeyTIMEKEY( txnInfo.getEventTimeKey() );
		lotProcessHistory.setWORKORDER( lotInformation.getWORKORDER() );
		lotProcessHistory.setROOTLOTID( lotInformation.getROOTLOTID() );
		lotProcessHistory.setPROCESSROUTEID( lotInformation.getPROCESSROUTEID() );
		lotProcessHistory.setPROCESSROUTETYPE( lotInformation.getPROCESSROUTETYPE() );
		lotProcessHistory.setPRODUCTID( lotInformation.getPRODUCTID() );
		lotProcessHistory.setLOCATION( lotInformation.getLOCATION() );
		lotProcessHistory.setCARRIERID( lotInformation.getCARRIERID() );
		lotProcessHistory.setASSIGNSLOT( lotInformation.getASSIGNSLOT() );
		
		// 공정정보
		lotProcessHistory.setPROCESSID( viewLotInfo.getPROCESSID() );
		lotProcessHistory.setPROCESSNAME( viewLotInfo.getPROCESSNAME() );
		lotProcessHistory.setPROCESSSEQUENCE( viewLotInfo.getPROCESSSEQUENCE() );
		
		// 스텝정보
		lotProcessHistory.setRECIPEID( viewLotInfo.getRECIPEID() );
		lotProcessHistory.setRECIPENAME( viewLotInfo.getRECIPENAME() );
		lotProcessHistory.setRECIPESEQUENCE( viewLotInfo.getRECIPESEQUENCE() );
		lotProcessHistory.setRECIPETYPE( viewLotInfo.getRECIPETYPE() );
		lotProcessHistory.setRECIPERELATIONCODE( viewLotInfo.getRECIPERELATIONCODE() );
		lotProcessHistory.setRECIPETYPECODE( viewLotInfo.getRECIPETYPECODE() );
		
		// 조건정보
		lotProcessHistory.setRECIPEPARAMETERID( viewLotInfo.getRECIPEPARAMETERID() );
		lotProcessHistory.setRECIPEPARAMETERNAME( viewLotInfo.getRECIPEPARAMETERNAME() );
		
		// 이벤트정보
		lotProcessHistory.setEVENTNAME( txnInfo.getTxnId() );
		lotProcessHistory.setEVENTUSERID( txnInfo.getTxnUser() );
		lotProcessHistory.setEVENTCOMMENT( txnInfo.getTxnComment() );
		lotProcessHistory.setEVENTTIME( txnInfo.getEventTime() );
		lotProcessHistory.setEVENTFLAG( "" );
		
		// 진행정보
		lotProcessHistory.setEQUIPMENTID( viewLotInfo.getEQUIPMENTID() );
		lotProcessHistory.setCURRENTQUANTITY( lotInformation.getCURRENTQUANTITY() );
		lotProcessHistory.setDUEDATE( DateUtil.toTimeString(lotInformation.getENDDUEDATE(), DateUtil.FORMAT_SIMPLE_DAY) );
		lotProcessHistory.setPRIORITY( lotInformation.getPRIORITY() );
		lotProcessHistory.setHOLDSTATE( lotInformation.getHOLDSTATE() );
		lotProcessHistory.setREWORKSTATE( lotInformation.getREWORKSTATE() );
		lotProcessHistory.setLOTSTATE( lotInformation.getLOTSTATE() );
		lotProcessHistory.setSHIPSTATE( lotInformation.getSHIPSTATE() );
		lotProcessHistory.setREASONCODETYPE( txnInfo.getTxnReasonCodeType() );
		lotProcessHistory.setREASONCODE( txnInfo.getTxnReasonCode() );
		lotProcessHistory.setREWORKCOUNT( lotInformation.getREWORKCOUNT() );
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(lotProcessHistory.getCANCELFLAG()) )
		{
			lotProcessHistory.setCANCELFLAG( Constant.FLAG_YESNO_YES );
		}
		else
		{
			lotProcessHistory.setCANCELFLAG( Constant.FLAG_YESNO_NO );
		}
		lotProcessHistory.setPROCESSTIMEFLAG( viewLotInfo.getPROCESSTIMEFLAG() );
		
		// 사전 입력 정보
//		lotProcessHistory.setSOURCELOTNAME();
//		lotProcessHistory.setDESTINATIONLOTNAME();
//		lotProcessHistory.setOLDCURRENTQUANTITY( oldLotHistoryInfo.getCURRENTQUANTITY() );
//		lotProcessHistory.setCONSUMEDLOTNAME( "" );
//		lotProcessHistory.setREPEATCOUNT(null);
//		lotProcessHistory.setREPRINTREASONCODE( "" );
//		lotProcessHistory.setREPRINTREASONDESC( "" );
//		lotProcessHistory.setFEEDBACKCOMMENT( "" );
		
		lotProcessHistory.excuteDML(SqlConstant.DML_INSERT);
	}
}
