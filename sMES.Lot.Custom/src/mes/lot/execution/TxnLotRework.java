package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.REWORKINFORMATION;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.transaction.LotService;
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
public class TxnLotRework implements ObjectExecuteService
{
	/**
	 * Lot을 Rework 처리하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		
		LotValidation validation = new LotValidation();
		LotService lotService = new LotService();
		
		validation.checkListNull(dataList);
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataLotMap = (HashMap<String, String>) dataList.get(i);

			validation.checkKeyNull(dataLotMap , new Object[] {"LOTID", "TIMEKEY", "CHANGEPROCESSROUTEID", "REASONCODE", "REASONCODETYPE"});

			String lotID 						= dataLotMap.get("LOTID");
			String timeKey 						= dataLotMap.get("TIMEKEY");
			
			String changeProcessRouteID 		= dataLotMap.get("CHANGEPROCESSROUTEID");
			String reasonCode			 		= dataLotMap.get("REASONCODE");
			String reasonCodeType		 		= dataLotMap.get("REASONCODETYPE");
			String reasonProcessID			 	= dataLotMap.get("REASONPROCESSID");
			String reasonProcessSequence		= dataLotMap.get("REASONPROCESSSEQUENCE");
			
			String reworkEquipmentID			= dataLotMap.get("REWORKEQUIPMENTID");
			String startProcessID		 		= dataLotMap.get("STARTPROCESSID");
			String startProcessSequence 		= dataLotMap.get("STARTPROCESSSEQUENCE");
			String endProcessID 				= dataLotMap.get("ENDPROCESSID");
			String endProcessSequence 			= dataLotMap.get("ENDPROCESSSEQUENCE");
			
			String returnProcessID 				= dataLotMap.get("RETURNPROCESSID");
			String returnProcessSequence 		= dataLotMap.get("RETURNPROCESSSEQUENCE");
			String description			 		= dataLotMap.get("DESCRIPTION");
			
			
			txnInfo.setTxnReasonCode( reasonCode );
			txnInfo.setTxnReasonCodeType( reasonCodeType );
			txnInfo.setTxnComment( description );

			
			// LotInformation
			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID(lotID);
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

			validation.validationLotState(lotInfo.getKeyLOTID(), lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED});

			
			VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
			viewLotInfo.setKeyLOTID(lotID);
			viewLotInfo.setKeyTIMEKEY(timeKey);
			viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
//			if ( Constant.OBJECTTYPE_OPERATION.equalsIgnoreCase(viewLotInfo.getINPUTMODE()) && 
//					Constant.EVENT_START.equalsIgnoreCase(viewLotInfo.getACTIONTYPE()) && 
//					Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
//			{
//				// 공정시작 전에만 처리가능?
//				// 현재 작업지시번호 ({0}) 는 재작업을 진행할 수 없는 상태입니다.
//				throw new CustomException("OM-133", new Object[] {lotInfo.getWORKORDER()});
//			}

			
			REWORKINFORMATION reworkInfo = new REWORKINFORMATION();
			reworkInfo.setKeyLOTID(lotInfo.getKeyLOTID());
			reworkInfo.setKeyPROCESSROUTEID(changeProcessRouteID);
			reworkInfo.setKeyRELATIONSEQUENCE(lotInfo.getREWORKCOUNT() + 1);
			reworkInfo.setSTARTTIME( txnInfo.getEventTime() );
			if ( reworkEquipmentID != null && !reworkEquipmentID.isEmpty() )
			{
				reworkInfo.setREWORKEQUIPMENTID( reworkEquipmentID );
			}
			else
			{
				reworkInfo.setREWORKEQUIPMENTID( viewLotInfo.getEQUIPMENTID() );
			}
			reworkInfo.setREWORKSTATE(Constant.REWORKSTATE_PROCESSING);
			reworkInfo.setREASONCODETYPE( txnInfo.getTxnReasonCodeType() );
			reworkInfo.setREASONCODE( txnInfo.getTxnReasonCode() );
			reworkInfo.setREASONPROCESSID(reasonProcessID);
			reworkInfo.setREASONPROCESSSEQUENCE( ConvertUtil.Object2Long(reasonProcessSequence) );
			
			// 재진행 트리구조 관리
			if ( Constant.LOTREWORKSTATE_NOTINREWORK.equalsIgnoreCase(lotInfo.getREWORKSTATE()) )
			{
				// 재진행 중이 아닌 경우
				reworkInfo.setDEPTHLEVEL((long) 1);
				reworkInfo.setFROMID((long) 1); // MAX 값 치환
				reworkInfo.setTOID((long) 1);
				
				// FROMID 에 대한 MAX +1 값 설정
				REWORKINFORMATION searchReworkInfo = new REWORKINFORMATION();
				searchReworkInfo.setKeyLOTID(lotInfo.getKeyLOTID());
				searchReworkInfo.setDEPTHLEVEL((long) 1);
				
				String suffix = " ORDER BY depthLevel DESC, fromID DESC, toID DESC ";
				
				List<Object> listReworkInfo = (List<Object>) searchReworkInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
				if ( listReworkInfo.size() > 0 )
				{
					searchReworkInfo = (REWORKINFORMATION) listReworkInfo.get(0);
					reworkInfo.setFROMID(searchReworkInfo.getFROMID() + 1);
				}
			}
			else
			{
				// 재진행 중인 경우
				REWORKINFORMATION searchReworkInfo = new REWORKINFORMATION();
				searchReworkInfo.setKeyLOTID(lotInfo.getKeyLOTID());
				searchReworkInfo.setREWORKSTATE(Constant.REWORKSTATE_PROCESSING);
				
				String suffix = " ORDER BY depthLevel DESC, fromID DESC, toID DESC ";
				
				List<Object> listReworkInfo = (List<Object>) searchReworkInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
				if ( listReworkInfo.size() > 0 )
				{
					searchReworkInfo = (REWORKINFORMATION) listReworkInfo.get(0);
					
					reworkInfo.setDEPTHLEVEL(searchReworkInfo.getDEPTHLEVEL() + 1);
					reworkInfo.setFROMID(searchReworkInfo.getTOID()); // MAX 값 치환
					reworkInfo.setTOID((long) 1); // MAX 값 치환
					
					
					// TOID  에 대한 MAX +1 값 설정
					REWORKINFORMATION searchReworkInfo2 = new REWORKINFORMATION();
					searchReworkInfo2.setKeyLOTID(lotInfo.getKeyLOTID());
					searchReworkInfo2.setDEPTHLEVEL(reworkInfo.getDEPTHLEVEL());
					searchReworkInfo2.setFROMID(reworkInfo.getFROMID());
					List<Object> listReworkInfo2 = (List<Object>) searchReworkInfo2.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
					if ( listReworkInfo2.size() > 0 )
					{
						searchReworkInfo2 = (REWORKINFORMATION) listReworkInfo2.get(0);
						reworkInfo.setTOID(searchReworkInfo2.getTOID() + 1);
					}
				}
				else
				{
					// 필요한 데이터가 존재하지 않습니다. 담당자에게 문의 바랍니다.
					throw new CustomException("CM-007", new Object[] {});
				}
			}
			
			reworkInfo.setEVENTUSERID( txnInfo.getTxnUser() );
			
			reworkInfo.setREWORKPROCESSID( viewLotInfo.getPROCESSID() );
			reworkInfo.setREWORKPROCESSSEQUENCE( viewLotInfo.getPROCESSSEQUENCE() );
			reworkInfo.setRELATIONTIMEKEY( viewLotInfo.getKeyTIMEKEY() );
			reworkInfo.setSTARTPROCESSID(startProcessID);
			reworkInfo.setSTARTPROCESSSEQUENCE( ConvertUtil.Object2Long(startProcessSequence) );
			reworkInfo.setENDPROCESSID(endProcessID);
			reworkInfo.setENDPROCESSSEQUENCE( ConvertUtil.Object2Long(endProcessSequence) );
			
			reworkInfo.setRETURNPROCESSROUTEID( viewLotInfo.getPROCESSROUTEID() );
			reworkInfo.setRETURNPROCESSID(returnProcessID);
			reworkInfo.setRETURNPROCESSSEQUENCE( ConvertUtil.Object2Long(returnProcessSequence) );
			reworkInfo.setDESCRIPTION( txnInfo.getTxnComment() );
			
			reworkInfo.excuteDML(SqlConstant.DML_INSERT);

			// 재작업 진행
			lotService.makeLotInRework(lotInfo, reworkInfo, txnInfo);
		}
		
		return recvDoc;
	}
}
