package mes.lot.transaction;

import java.util.List;

import kr.co.mesframe.exception.NoDataFoundException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.equipment.transaction.EquipmentService;
import mes.errorHandler.CustomException;
import mes.lot.data.DY_PROCESSRESULT;
import mes.lot.data.FUTUREACTION;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.LOTPROCESSHISTORY;
import mes.lot.data.REWORKINFORMATION;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.validation.LotValidation;
import mes.master.data.EQUIPMENT;
import mes.master.data.PRODUCTDEFINITION;
import mes.util.TimeCalcUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class ProcessService
{
	/**
	 * 받아온 데이터로 VIEWLOTTRACKING 값 설정 후 공정시작 (InputMode : Operation, ActionType : Start)
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param inQuantity
	 * @param txnInfo
	 * @param pastMode
	 * @return VIEWLOTTRACKING
	 * @throws CustomException
	 * 
	 */
	public VIEWLOTTRACKING makeTrackIn(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, Double inQuantity, TxnInfo txnInfo, String pastMode)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();
		
		String lotID 				= lotInfo.getKeyLOTID();
		
		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_START );
		
		try
		{
			viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		}
		catch (NoDataFoundException e)
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		
		// Status Check
		if ( !Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		validation.validationLotState( lotID, lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED} );
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );

		// 순차 진행 여부 체크
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(lotInfo.getGOINORDERREQUIRED()) )
		{
			trackingService.checkGoInOrder(viewLotInfo, txnInfo);
		}
		
		// Operation Info Update
		viewLotInfo.setRELATIONTIMEKEY( txnInfo.getEventTimeKey() );
		viewLotInfo.setRELATIONTIME( txnInfo.getEventTime() );
		viewLotInfo.setSTATUS( Constant.OPERATIONSTATE_COMPLETE );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
		viewLotInfo.setPASTMODE( pastMode );
		viewLotInfo.setEVENTUSERID( txnInfo.getTxnUser() );
		
		// Event Name
		txnInfo.setTxnId( Constant.EVENT_TRACKIN );
		if ( Constant.CONTROL_MODE_AUTO.equalsIgnoreCase(pastMode) )
		{
			txnInfo.setTxnUser( viewLotInfo.getEQUIPMENTID() );
		}
		
		
		// 첫 공정 Start 시에 Time 및 User 정보 입력 로직 추가
		if ( lotInfo.getFIRSTPROCESSSTARTTIME() == null && 
				(lotInfo.getFIRSTPROCESSSTARTUSERID() == null || lotInfo.getFIRSTPROCESSSTARTUSERID().isEmpty()) )
		{
			lotInfo.setFIRSTPROCESSSTARTTIME( txnInfo.getEventTime() );
			lotInfo.setFIRSTPROCESSSTARTUSERID( txnInfo.getTxnUser() );
			
			lotInfo.excuteDML(SqlConstant.DML_UPDATE);
			
			// Main 로트에 대해서 첫공정 진행시의 작업지시번호 시작 시간 기록
			if ( Constant.PROCESSROUTETYPE_MAIN.equalsIgnoreCase(lotInfo.getPROCESSROUTETYPE()) )
			{
				viewLotInfo.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_YES);
			}
		}

		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		
		//2012.04.25 Repeat 인경우 RepeatCount 증가
		if ( Constant.RECIPE_ID_REPEAT.equalsIgnoreCase(viewLotInfo.getPROCESSTYPE()) )
		{
			Long iRepeatCount = lotInfo.getREPEATCOUNT();
			lotInfo.setREPEATCOUNT(iRepeatCount +1);
			
			lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
		
		// OperationParameter Flag Update
		List<VIEWLOTTRACKING> listProcessParameter = trackingService.getProcessParameterList(viewLotInfo);
		if ( listProcessParameter != null )
		{
			for ( int i = 0; i < listProcessParameter.size(); i++ )
			{
				VIEWLOTTRACKING processParameter = listProcessParameter.get(i);
				processParameter.setCURRENTFLAG( Constant.FLAG_YN_YES );
				processParameter.setRELATIONTIMEKEY( viewLotInfo.getRELATIONTIMEKEY() );
				processParameter.setRELATIONTIME( viewLotInfo.getRELATIONTIME() );
				processParameter.setSTATUS( Constant.RECIPEPARAMETERSTATE_PROCESSING );
				processParameter.excuteDML(SqlConstant.DML_UPDATE);
				
				// 자동 진행 여부 판별 후 진행
				
				// Time 공정조건에 의한 QueueTime 관리
				if ( Constant.RECIPEPARAMETER_ID_TIME.equalsIgnoreCase(processParameter.getRECIPEPARAMETERID()) )
				{
					LotQueueTimeManager.startLotQueueTime(viewLotInfo, processParameter, txnInfo);
				}
			}
		}
		
		// Recipe Flag Update
		VIEWLOTTRACKING recipeLotInfo = trackingService.getNextRecipe(viewLotInfo);
		if ( recipeLotInfo != null )
		{
			if ( recipeLotInfo.getRECIPETYPE() != null &&  recipeLotInfo.getORDERINDEX() != null && 
					recipeLotInfo.getRECIPETYPE().equalsIgnoreCase(Constant.RECIPE_ID_CONSUMABLE) )
			{
				RecipeService recipeService = new RecipeService();
				recipeService.updateNextConsumableRecipe(recipeLotInfo);
			}
			else
			{
				recipeLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				recipeLotInfo.excuteDML(SqlConstant.DML_UPDATE);
			}
			
			// 자동 진행 여부 판별 후 진행
		}
		
		
		// Check ConCurrentOperationName
		if(  viewLotInfo.getCONCURRENCYPROCESSID() != null && !viewLotInfo.getCONCURRENCYPROCESSID().isEmpty() 
				&& viewLotInfo.getCONCURRENCYSEQUENCE() != null && viewLotInfo.getCONCURRENCYSEQUENCE() > 0 )
		{
			VIEWLOTTRACKING concurrencyInfo = 
					trackingService.getProcessInfo(lotID, viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getCONCURRENCYPROCESSID(), viewLotInfo.getCONCURRENCYSEQUENCE(), viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_START);
			if ( concurrencyInfo != null && !Constant.OPERATIONSTATE_COMPLETE.equalsIgnoreCase(concurrencyInfo.getSTATUS()) )
			{
				concurrencyInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				concurrencyInfo.excuteDML(SqlConstant.DML_UPDATE);
			}
		}
		
		
		// LotProcessHistory
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		// SubProductGroup 관리의 Lot 인 경우 만 사용
		if ( Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(lotInfo.getPRODUCTTYPE()) && inQuantity != null )
		{
			Double oldQty = lotInfo.getCURRENTQUANTITY();
			
			lotInfo.setCURRENTQUANTITY(inQuantity);
			lotInfo.excuteDML(SqlConstant.DML_UPDATE);
			
			lotProcessHistory.setOLDCURRENTQUANTITY(oldQty);
			addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);
		}
		else
		{
			addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);
		}
		
		// Equipment Service
		if (viewLotInfo.getEQUIPMENTID().isEmpty()==false)
		{
			EquipmentService equipmentService = new EquipmentService();
			EQUIPMENT equipmentInfo = new EQUIPMENT();
			equipmentInfo.setKeyEQUIPMENTID(viewLotInfo.getEQUIPMENTID());
			try
			{
				equipmentInfo = (EQUIPMENT) equipmentInfo.excuteDML(SqlConstant.DML_SELECTROW);
			}
			catch (NoDataFoundException e)
			{
				// ({0}) 은 존재하지 않습니다.
				throw new CustomException("EQP-001", new Object[] {viewLotInfo.getEQUIPMENTID()});
			}
			equipmentService.EquipmentProcessStart(equipmentInfo, lotInfo, txnInfo, true);
		}
		return viewLotInfo;
	}
	
	/**
	 * 마지막 공정인 경우 LotService의 makeLotCompleted 함수 호출해 Lot 완료 처리
	 * 
	 * @param lotInfo
	 * @param viewLotInfo
	 * @param txnInfo
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void processEndDetailCaseNull(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
		LotTrackingService trackingService = new LotTrackingService();
		LotService lotService = new LotService();
		
		if ( Constant.PROCESSROUTETYPE_REWORK.equalsIgnoreCase(viewLotInfo.getPROCESSROUTETYPE()) )
		{
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
			
			// Rework 진행 중인 경우 처리 로직 추가
			REWORKINFORMATION reworkInfo = new REWORKINFORMATION();
			reworkInfo.setKeyLOTID(lotInfo.getKeyLOTID());
			reworkInfo.setKeyPROCESSROUTEID(viewLotInfo.getPROCESSROUTEID());
			reworkInfo.setKeyRELATIONSEQUENCE(viewLotInfo.getROUTERELATIONSEQUENCE());
			reworkInfo = (REWORKINFORMATION) reworkInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			reworkInfo.setENDTIME( txnInfo.getEventTime() );
			reworkInfo.setREWORKSTATE(Constant.REWORKSTATE_COMPLETE);
			reworkInfo.excuteDML(SqlConstant.DML_UPDATE);

			// 재작업 진행 완료
			lotService.makeLotNotInRework(lotInfo, reworkInfo, txnInfo);

			return;
		}
		
		// 전체 공정에 대한 진행 여부 체크
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getENDOFROUTE()) && processCompleteCheck(viewLotInfo) )
		{
			// 현재 진행 Lot({0})에 대한 미진행 공정이 존재하여 공정진행을 완료할 수 없습니다.
			throw new CustomException("LOT-010", new Object[] {viewLotInfo.getKeyLOTID()});
		}
		
		// 마지막 공정인 경우의 처리 로직
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getENDOFROUTE()) )
		{
			if ( Constant.PROCESSROUTETYPE_PARTIAL.equalsIgnoreCase(viewLotInfo.getPROCESSROUTETYPE()) )
			{
				VIEWLOTTRACKING mainViewLotInfo = 
						trackingService.getCurrentViewLotInfo(lotInfo.getKeyLOTID(), Constant.PROCESSROUTETYPE_MAIN);

				lotService.transferLot(viewLotInfo, mainViewLotInfo, lotInfo.getCURRENTQUANTITY(), txnInfo);
				lotService.terminateLot(viewLotInfo, lotInfo, txnInfo);
			}
			else
			{
				if ( Constant.PROCESSROUTETYPE_MAIN.equalsIgnoreCase(viewLotInfo.getPROCESSROUTETYPE()) )
				{
					// 메인 라우팅의 경우 Partial 라우팅의 완료여부를 체크하여 완료되지 않은 경우 종료 불가
//					if ( false )
//					{
//						// 다음 작업지시번호 ({0}), 라우팅 ({1}) 의 부분 라우팅이 진행 완료되지 않았습니다.
//						throw new CustomException("OM-095", new Object[] {lotInfo.getWORKORDER(), lotInfo.getPROCESSROUTEID()});
//					}
				}

				// 입고 로직으로 재 구성
				lotService.makeLotCompleted(lotInfo, viewLotInfo, txnInfo);
			}
		}
		else
		{
			// 마지막 공정이 아닌 경우 종료 후 처리 로직 없음!
		}
	}
	
	/**
	 * 다음 공정이 완료되어 있지 않고 Flag 설정이 되어있지 않을 경우 처리
	 * 
	 * @param lotInfo
	 * @param viewLotInfo
	 * @param nextViewLotInfo
	 * @param inspectionFlag
	 * @param resultValue
	 * @param txnInfo
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void processEndDetailCaseNotNull(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, VIEWLOTTRACKING nextViewLotInfo, 
			boolean inspectionFlag, String resultValue, TxnInfo txnInfo)
	{
		LotTrackingService trackingService = new LotTrackingService();
		
		// 다음 공정이 이미 완료되어 있거나 Flag 설정이 되어 있는 경우 Skip 로직
		if ( Constant.OPERATIONSTATE_COMPLETE.equalsIgnoreCase(nextViewLotInfo.getSTATUS()) || Constant.FLAG_YN_YES.equalsIgnoreCase(nextViewLotInfo.getCURRENTFLAG()) )
		{
			// Skip
		}
		else
		{
			// Merge Lot Cancel Process 진행
			if ( Constant.RECIPE_ID_PACKING.equalsIgnoreCase(nextViewLotInfo.getPROCESSTYPE()) && Constant.FLAG_YESNO_YES.equalsIgnoreCase(lotInfo.getMERGEFLAG()) )
			{
//				LotService lotService = new LotService();
//				lotService.mergeLotCancel(lotID, txnInfo);
			}
			
			boolean repeatProcessFlag = false;
			if ( Constant.RECIPE_ID_REPEAT.equalsIgnoreCase(nextViewLotInfo.getPROCESSTYPE()) )
			{
				repeatProcessFlag = true;
				
				// 이전 공정이 계측 공정인지 여부 체크
				if ( !inspectionFlag )
				{
					VIEWLOTTRACKING previewLot = 
							trackingService.getPreviewProcess(nextViewLotInfo.getKeyLOTID(), nextViewLotInfo.getPROCESSROUTEID(), nextViewLotInfo.getPROCESSSEQUENCE(), nextViewLotInfo.getROUTERELATIONSEQUENCE());
					
					if ( !Constant.RECIPE_ID_MEASURE.equalsIgnoreCase(previewLot.getPROCESSTYPE()) )
					{
						inspectionFlag = true;
						resultValue = Constant.FLAG_YN_YES;
					}
				}
			}
			
			// 다음 공정에 관한 처리
			if ( repeatProcessFlag && inspectionFlag && Constant.FLAG_YN_YES.equalsIgnoreCase(resultValue) )
			{
				// Repeat 공정의 Complete 처리
				VIEWLOTTRACKING repeatLotInfo = new VIEWLOTTRACKING();
				repeatLotInfo.setKeyLOTID( nextViewLotInfo.getKeyLOTID() );
				repeatLotInfo.setPROCESSROUTEID( nextViewLotInfo.getPROCESSROUTEID() );
				repeatLotInfo.setPROCESSID( nextViewLotInfo.getPROCESSID() );
				repeatLotInfo.setPROCESSSEQUENCE( nextViewLotInfo.getPROCESSSEQUENCE() );
				repeatLotInfo.setROUTERELATIONSEQUENCE( nextViewLotInfo.getROUTERELATIONSEQUENCE() );

				List<Object> listRepeatLotInfo = (List<Object>) repeatLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
				for ( int i = 0; i < listRepeatLotInfo.size(); i++ )
				{
					repeatLotInfo = (VIEWLOTTRACKING) listRepeatLotInfo.get(i);

					repeatLotInfo.setSTATUS( Constant.RECIPESTATE_COMPLETE );
					repeatLotInfo.setEVENTUSERID( txnInfo.getTxnUser() );
					repeatLotInfo.excuteDML(SqlConstant.DML_UPDATE);
				}

				
				VIEWLOTTRACKING nextLoopViewLotInfo = 
						trackingService.getNextProcess(lotInfo.getKeyLOTID(), nextViewLotInfo.getPROCESSROUTEID(), nextViewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_START, nextViewLotInfo.getPROCESSSEQUENCE());
				
				if ( nextLoopViewLotInfo == null )
				{
					processEndDetailCaseNull(lotInfo, nextViewLotInfo, txnInfo);
				}
				else
				{
					// Next Operation Flag Update
					// 처음으로 Loop
					processEndDetailCaseNotNull(lotInfo, nextViewLotInfo, nextLoopViewLotInfo, inspectionFlag, resultValue, txnInfo);
				}
			}
			else
			{
				// Next Operation Flag Update
				nextViewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				nextViewLotInfo.excuteDML(SqlConstant.DML_UPDATE);

				// 예약 작업 설정에 대한 처리
				if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(nextViewLotInfo.getFUTUREACTIONFLAG()) )
				{
					LotService lotService = new LotService();
					
					FUTUREACTION futureAction = new FUTUREACTION();
					futureAction.setKeyLOTID(nextViewLotInfo.getKeyLOTID());
					futureAction.setKeyRELATIONTIMEKEY(nextViewLotInfo.getKeyTIMEKEY());
					futureAction.setACTIONSTATE(Constant.ACTION_STATE_RESERVE);

					List<Object> listFutureAction = (List<Object>) futureAction.excuteDML(SqlConstant.DML_SELECTLIST);
					
					for ( int i = 0; i < listFutureAction.size(); i++ )
					{
						futureAction = (FUTUREACTION) listFutureAction.get(i);
						
						
						TxnInfo sTxnInfo = new TxnInfo();
						sTxnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
						sTxnInfo.setEventTime(txnInfo.getEventTime());
						sTxnInfo.setTxnUser(txnInfo.getTxnUser());
						sTxnInfo.setTxnComment(txnInfo.getTxnComment());
						sTxnInfo.setTxnReasonCodeType(futureAction.getREASONCODETYPE());
						sTxnInfo.setTxnReasonCode(futureAction.getREASONCODE());
						
						if ( Constant.ACTION_TYPE_HOLD.equalsIgnoreCase(futureAction.getACTIONTYPE()) )
						{
							sTxnInfo.setTxnId(Constant.EVENT_MAKELOTHOLD);
							
							lotService.makeLotHold(lotInfo, sTxnInfo);
						}
						else if ( Constant.ACTION_TYPE_SCRAP.equalsIgnoreCase(futureAction.getACTIONTYPE()) )
						{
							sTxnInfo.setTxnId(Constant.EVENT_MAKELOTSCRAP);
							
							lotService.makeLotScrap(lotInfo, sTxnInfo);
						}
						
						lotService.releaseFutureAction(lotInfo, futureAction.getKeyRELATIONTIMEKEY(), futureAction.getKeySEQUENCE(), Constant.ACTION_STATE_COMPLETE, sTxnInfo);
					}
				}
				
				// 자동 공정 시작 로직
				if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(nextViewLotInfo.getAUTOTRACKIN()) )
				{
					TxnInfo sTxnInfo = new TxnInfo();
					sTxnInfo.setTxnId(txnInfo.getTxnId());
					sTxnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
					sTxnInfo.setEventTime(txnInfo.getEventTime());
					sTxnInfo.setTxnUser(txnInfo.getTxnUser());
					sTxnInfo.setTxnComment(txnInfo.getTxnComment());
					sTxnInfo.setScanFlag(txnInfo.getScanFlag());
					
					if ( txnInfo.getWorkType() != null && !txnInfo.getWorkType().isEmpty() )
					{
						String[] arType = txnInfo.getWorkType().split("_");
						sTxnInfo.setWorkType(arType[0] + "_ROUTING");
					}
					else
					{
						sTxnInfo.setWorkType(txnInfo.getWorkType());
					}
					
					sTxnInfo.setDeviceAddress(txnInfo.getDeviceAddress());
					sTxnInfo.setDeviceCode(txnInfo.getDeviceCode());
					sTxnInfo.setTxnReasonCode(txnInfo.getTxnReasonCode());
					sTxnInfo.setTxnReasonCodeType(txnInfo.getTxnReasonCodeType());
					
					
					VIEWLOTTRACKING trackingLotInfo = makeTrackIn(lotInfo, nextViewLotInfo.getPROCESSID(), String.valueOf(nextViewLotInfo.getPROCESSSEQUENCE()), 
							nextViewLotInfo.getROUTERELATIONSEQUENCE(), null, sTxnInfo, Constant.CONTROL_MODE_MANUAL);
					
					
					// 자동 진행 여부 판별 후 진행 Process
					if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(trackingLotInfo.getAUTOTRACKINGFLAG()) )
					{
						autoTrackingProcess(lotInfo, trackingLotInfo, sTxnInfo);
					}
				}
			}
		}
	}
	
	/**
	 * 받아온 데이터로 VIEWLOTTRACKING 값 설정 후 공정종료 (InputMode : Operation, ActionType : End)
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param outQuantity
	 * @param txnInfo
	 * @param pastMode
	 * @return VIEWLOTTRACKING
	 * @throws CustomException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING makeTrackOut(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, Double outQuantity, TxnInfo txnInfo, String pastMode)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();
		
		String lotID 				= lotInfo.getKeyLOTID();
		
		// 공정 시작여부 체크
		// ViewLotTracking
		VIEWLOTTRACKING startViewLotInfo = new VIEWLOTTRACKING();
		startViewLotInfo.setKeyLOTID( lotID );
		startViewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		startViewLotInfo.setPROCESSID( processID );
		startViewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		startViewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		startViewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		startViewLotInfo.setACTIONTYPE( Constant.EVENT_START );
		
		startViewLotInfo = (VIEWLOTTRACKING) startViewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		// Status Check
		if ( !Constant.OPERATIONSTATE_COMPLETE.equalsIgnoreCase(startViewLotInfo.getSTATUS()) )
		{
			// 현재 공정({0})에서 미진행 항목이 존재하여 종료 처리가 불가능 합니다.
			throw new CustomException("LOT-011", new Object[] {startViewLotInfo.getPROCESSNAME()});
		}
		
		
		if ( !txnInfo.getWorkType().contains("EIS") && !txnInfo.getWorkType().contains("ROUTING") )
		{
			// 원료 투입 여부 체크
			// ViewLotTracking
			VIEWLOTTRACKING inputViewLotInfo = new VIEWLOTTRACKING();
			inputViewLotInfo.setKeyLOTID( lotID );
			inputViewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
			inputViewLotInfo.setPROCESSID( processID );
			inputViewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
			inputViewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
			inputViewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
			inputViewLotInfo.setACTIONTYPE( Constant.RECIPE_ID_CONSUMABLE );
			inputViewLotInfo.setSTATUS( Constant.RECIPESTATE_WAITING );
			
			List<Object> listCheck = (List<Object>) inputViewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
			
			// Status Check
			if ( listCheck.size() > 0 )
			{
				inputViewLotInfo = (VIEWLOTTRACKING) listCheck.get(0);
				
				// 현재 공정({0})에서 미진행 항목이 존재하여 종료 처리가 불가능 합니다.
				throw new CustomException("LOT-011", new Object[] {inputViewLotInfo.getPROCESSNAME()});
			}
		}
		
		
		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_END );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		// Status Check
		if ( !Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		validation.validationLotState( lotID, lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED} );
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );
		
		
		// Event Name
		txnInfo.setTxnId( Constant.EVENT_TRACKOUT );
		if ( Constant.CONTROL_MODE_AUTO.equalsIgnoreCase(pastMode) )
		{
			txnInfo.setTxnUser( viewLotInfo.getEQUIPMENTID() );
		}
		
		
		// Operation Info Update
		viewLotInfo.setRELATIONTIMEKEY( txnInfo.getEventTimeKey() );
		viewLotInfo.setRELATIONTIME( txnInfo.getEventTime() );
		viewLotInfo.setSTATUS( Constant.OPERATIONSTATE_COMPLETE );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
		viewLotInfo.setPASTMODE( pastMode );
		viewLotInfo.setEVENTUSERID( txnInfo.getTxnUser() );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		
		// 순차 진행 여부 체크
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(lotInfo.getGOINORDERREQUIRED()) )
		{
			trackingService.checkGoInOrder(viewLotInfo, txnInfo);
		}
		
		
		// OperationParameter Info Update
		trackingService.processParameterComplete(viewLotInfo, startViewLotInfo, txnInfo);
		
		
		// 추가원료투입 스텝에 대한 CurrentFlag 해제 로직
		VIEWLOTTRACKING addInfo = new VIEWLOTTRACKING();
		addInfo.setKeyLOTID( lotID );
		addInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		addInfo.setPROCESSID( processID );
		addInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		addInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		addInfo.setRECIPEID(Constant.RECIPE_ID_ADDMATERIAL);
		addInfo.setCURRENTFLAG(Constant.FLAG_YN_YES);
		List<Object> listAddInfo = (List<Object>) addInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		for ( int i = 0; i < listAddInfo.size(); i++ )
		{
			addInfo = (VIEWLOTTRACKING) listAddInfo.get(i);
			addInfo.setCURRENTFLAG(Constant.FLAG_YN_NO);
			addInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
		
		
		// LotProcessHistory
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		// SubProductGroup 관리의 Lot 인 경우 만 사용
		if ( Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(lotInfo.getPRODUCTTYPE()) && outQuantity != null )
		{
			Double oldQty = lotInfo.getCURRENTQUANTITY();

			lotInfo.setCURRENTQUANTITY(outQuantity);
			lotInfo.excuteDML(SqlConstant.DML_UPDATE);

			lotProcessHistory.setOLDCURRENTQUANTITY(oldQty);
			addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);
		}
		else
		{
			addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);
		}

		// Equipment Service
		if (viewLotInfo.getEQUIPMENTID().isEmpty()==false)
		{
			EquipmentService equipmentService = new EquipmentService();
			EQUIPMENT equipmentInfo = new EQUIPMENT();
			equipmentInfo.setKeyEQUIPMENTID(viewLotInfo.getEQUIPMENTID());
			try
			{
				equipmentInfo = (EQUIPMENT) equipmentInfo.excuteDML(SqlConstant.DML_SELECTROW);
			}
			catch (NoDataFoundException e)
			{
				// ({0}) 은 존재하지 않습니다.
				throw new CustomException("EQP-001", new Object[] {viewLotInfo.getEQUIPMENTID()});
			}
			equipmentService.EquipmentProcessEnd(equipmentInfo, txnInfo, true);
		}
		
		// DYP 전용 I/F 실적 로직
		ProductService oProductService = new ProductService();
		PRODUCTDEFINITION oProductData = oProductService.getProductInfo(lotInfo.getPLANTID(), lotInfo.getPRODUCTID());
		
		DY_PROCESSRESULT oProcessResult = new DY_PROCESSRESULT();
		oProcessResult.setKeyPLANTID(lotInfo.getPLANTID());
		oProcessResult.setKeyLOTID(lotInfo.getKeyLOTID());
		oProcessResult.setKeyPROCESSID(viewLotInfo.getPROCESSID());
		oProcessResult.setKeyPROCESSSEQUENCE(viewLotInfo.getPROCESSSEQUENCE());
		oProcessResult.setKeyREWORKCOUNT(viewLotInfo.getREWORKCOUNT());
		
		oProcessResult.setWORKORDERID(lotInfo.getWORKORDER());
		oProcessResult.setERPPROCESSCODE(viewLotInfo.getERPPROCESSCODE());
		oProcessResult.setPRODUCTORDERTYPE(lotInfo.getLOTTYPE());
		oProcessResult.setPRODUCTQUANTITY(lotInfo.getCURRENTQUANTITY());
		oProcessResult.setPRODUCTUNIT(oProductData.getPRODUCTUNIT());
		oProcessResult.setSCRAPQUANTITY( lotInfo.getLOTSTATE() != Constant.LOTSTATE_SCRAPPED ? 0.0 : 1.0 ); // 임시 로직 불량 수량 집계 필요
		oProcessResult.setSTARTTIME(startViewLotInfo.getRELATIONTIME());
		oProcessResult.setENDTIME(viewLotInfo.getRELATIONTIME());
		oProcessResult.setPROCESSTIME(TimeCalcUtil.subtractTimestampInMinute(viewLotInfo.getRELATIONTIME(), startViewLotInfo.getRELATIONTIME())); 
		oProcessResult.setWORKTIME(oProcessResult.getPROCESSTIME());
		oProcessResult.setMACHINETIME(oProcessResult.getPROCESSTIME());
		oProcessResult.setINTERFACEKEY(null);
		oProcessResult.setLASTUPDATETIME(txnInfo.getEventTime());
		oProcessResult.setLASTUPDATEUSERID(txnInfo.getTxnUser());
		oProcessResult.setEQUIPMENTID(viewLotInfo.getEQUIPMENTID());
		
		oProcessResult.excuteDML(SqlConstant.DML_INSERT);
		
		return viewLotInfo;
	}
	
	/**
	 * 받아온 데이터로 VIEWLOTTRACKING 값 설정 후 공정시작 취소 (CurrentFlag : Yes, Status : Waiting)
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param viewLotInfo
	 * @param txnInfo
	 * @throws CustomException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void makeTrackInCancel(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();
		
		String lotID =		lotInfo.getKeyLOTID();
		
		// Status Check
		if ( Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		validation.validationLotState( lotID, lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED} );
		validation.validationLotState( lotID, viewLotInfo.getSTATUS(), new Object[] {Constant.OPERATIONSTATE_COMPLETE});
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );
		// 자동 처리된 경우 수동 취소할 수 없음!
		validation.validationNotState( txnInfo.getTxnId(), Constant.CONTROL_MODE_AUTO, viewLotInfo.getPASTMODE() );
		

		// OperationParameter Flag Update
		List<VIEWLOTTRACKING> listProcessParameter = trackingService.getProcessParameterList(viewLotInfo, Constant.FLAG_YESNO_YES);
		if ( listProcessParameter != null )
		{
			for ( int i = 0; i < listProcessParameter.size(); i++ )
			{
				VIEWLOTTRACKING processParameter = listProcessParameter.get(i);
				
//				if ( processParameter.getSTATUS().equalsIgnoreCase(Constant.RECIPEPARAMETERSTATE_COMPLETE) )
//				{
//					// 공정 진행 Lot({0}) 의 공정정보 ({1}) 는 세부 공정을 진행 중으로 취소 할 수 없습니다.
//					throw new CustomException("OM-014", new Object[] {lotID, lotInfo.getWORKORDER()});
//				}
				
				processParameter.setCURRENTFLAG( Constant.FLAG_YN_NO );
				processParameter.setSTATUS( Constant.RECIPEPARAMETERSTATE_WAITING );
				processParameter.excuteDML(SqlConstant.DML_UPDATE);
			}
		}
		
		List<VIEWLOTTRACKING> listRecipe = trackingService.getRecipeList(viewLotInfo);
		if ( listRecipe != null )
		{
			for ( int i = 0; i < listRecipe.size(); i++ )
			{
				VIEWLOTTRACKING recipeInfo = listRecipe.get(i);

				if ( Constant.RECIPESTATE_COMPLETE.equalsIgnoreCase(recipeInfo.getSTATUS()) )
				{
					// 공정 진행이 이루어진 Lot({0}) 에 대해서는 작업시작 취소를 진행 할 수 없습니다.
					throw new CustomException("LOT-012", new Object[] {lotInfo.getKeyLOTID()});
				}
				else if ( Constant.FLAG_YN_YES.equalsIgnoreCase(recipeInfo.getCURRENTFLAG()) )
				{
					// Recipe Flag Update
					recipeInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
					recipeInfo.excuteDML(SqlConstant.DML_UPDATE);
				}
			}
		}
		
		//2012.04.25 Repeat 인경우 RepeatCount 차감
		if ( Constant.RECIPE_ID_REPEAT.equalsIgnoreCase(viewLotInfo.getPROCESSTYPE()) )
		{
			Long iRepeatCount = lotInfo.getREPEATCOUNT();
			if ( iRepeatCount > 0 )
			{	
				lotInfo.setREPEATCOUNT(iRepeatCount - 1);
			}
			else
			{	
				lotInfo.setREPEATCOUNT( (long) 0 );
			}
			lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
		
		// 공정종료가 열린경우 닫기
		VIEWLOTTRACKING processEndInfo = 
				trackingService.getProcessInfo(lotID, viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getPROCESSID(), viewLotInfo.getPROCESSSEQUENCE(), viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_END);
		
		if ( Constant.FLAG_YN_YES.equalsIgnoreCase(processEndInfo.getCURRENTFLAG()) )
		{
			processEndInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
			processEndInfo.excuteDML(SqlConstant.DML_UPDATE);
		}

		
		// 첫 공정 Start 시에 Time 및 User 정보 삭제 로직 추가
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getFIRSTPROCESSFLAG()) )
		{
			lotInfo.setFIRSTPROCESSSTARTTIME( null );
			lotInfo.setFIRSTPROCESSSTARTUSERID( "" );
			
			lotInfo.excuteDML(SqlConstant.DML_UPDATE);
			
			// Main 로트에 대해서 첫공정 진행시의 작업지시번호 시작 시간 기록
			if ( Constant.PROCESSROUTETYPE_MAIN.equalsIgnoreCase(lotInfo.getPROCESSROUTETYPE()) )
			{
				viewLotInfo.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_YES);
			}
		}
		
		
		// Lot ProcessHistory Cancel Update
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setKeyLOTID( lotID );
		lotProcessHistory.setKeyTIMEKEY( viewLotInfo.getRELATIONTIMEKEY() );
		lotProcessHistory = (LOTPROCESSHISTORY) lotProcessHistory.excuteDML(SqlConstant.DML_SELECTROW);
		
		lotProcessHistory.setCANCELFLAG(Constant.FLAG_YESNO_YES);
		lotProcessHistory.excuteDML(SqlConstant.DML_UPDATE);
		
		
		// 공정시작 취소인 경우 해당 공정의 모든 Flag 변경
		VIEWLOTTRACKING changeViewLotInfo = new VIEWLOTTRACKING();
		changeViewLotInfo.setKeyLOTID(viewLotInfo.getKeyLOTID());
		changeViewLotInfo.setPROCESSID(viewLotInfo.getPROCESSID());
		changeViewLotInfo.setPROCESSSEQUENCE(viewLotInfo.getPROCESSSEQUENCE());
		changeViewLotInfo.setROUTERELATIONSEQUENCE(viewLotInfo.getROUTERELATIONSEQUENCE());
		changeViewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_YES);
		changeViewLotInfo.setSTATUS(Constant.RECIPESTATE_WAITING);
		List<Object> listChangeViewLot = (List<Object>) changeViewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		for( int i = 0; i < listChangeViewLot.size(); i++ )
		{
			changeViewLotInfo = (VIEWLOTTRACKING) listChangeViewLot.get(i);
			changeViewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_NO);
			changeViewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
		
		
		// Operation Info Update
		viewLotInfo.setRELATIONTIMEKEY( "" );
		viewLotInfo.setRELATIONTIME( null );
		viewLotInfo.setSTATUS( Constant.OPERATIONSTATE_WAITING );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
		viewLotInfo.setPASTMODE( "" );
		viewLotInfo.setEVENTUSERID( "" );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// Event Name
		txnInfo.setTxnId( Constant.EVENT_TRACKINCANCEL );
		
		// LotProcessHistory
		lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setCANCELFLAG(Constant.FLAG_YESNO_YES);
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);
		
		// Equipment Service
		if (viewLotInfo.getEQUIPMENTID().isEmpty()==false)
		{
			EquipmentService equipmentService = new EquipmentService();
			EQUIPMENT equipmentInfo = new EQUIPMENT();
			equipmentInfo.setKeyEQUIPMENTID(viewLotInfo.getEQUIPMENTID());
			try
			{
				equipmentInfo = (EQUIPMENT) equipmentInfo.excuteDML(SqlConstant.DML_SELECTROW);
			}
			catch (NoDataFoundException e)
			{
				// ({0}) 은 존재하지 않습니다.
				throw new CustomException("EQP-001", new Object[] {viewLotInfo.getEQUIPMENTID()});
			}
			equipmentService.EquipmentProcessStartCancel(equipmentInfo, txnInfo);
		}
		
		
	}
	
	/**
	 * 받아온 데이터로 VIEWLOTTRACKING 값 설정 후 공정종료 취소 (CurrentFlag : Yes, Status : Waiting)
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param txnInfo
	 * @return VIEWLOTTRACKING
	 * @throws CustomException
	 * 
	 */
	public VIEWLOTTRACKING makeTrackOutCancel(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, TxnInfo txnInfo)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		
		String lotID 				= lotInfo.getKeyLOTID();
		
		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_END );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		// Status Check
		if ( Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		validation.validationLotState( lotID, lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED} );
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );
		
		
		// Lot ProcessHistory Cancel Update
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setKeyLOTID( lotID );
		lotProcessHistory.setKeyTIMEKEY( viewLotInfo.getRELATIONTIMEKEY() );
		lotProcessHistory = (LOTPROCESSHISTORY) lotProcessHistory.excuteDML(SqlConstant.DML_SELECTROW);
		
		lotProcessHistory.setCANCELFLAG(Constant.FLAG_YESNO_YES);
		lotProcessHistory.excuteDML(SqlConstant.DML_UPDATE);
		
		
		// Event Name
		txnInfo.setTxnId( Constant.EVENT_TRACKOUTCANCEL );
		
		// Operation Info Update
		viewLotInfo.setRELATIONTIMEKEY( "" );
		viewLotInfo.setRELATIONTIME( null );
		viewLotInfo.setSTATUS( Constant.OPERATIONSTATE_WAITING );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
		viewLotInfo.setPASTMODE( "" );
		viewLotInfo.setEVENTUSERID( "" );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// 다음 공정시작에 대한 Flag 판단하여 변경
		// 공정시작인 경우 동시진행이 문제가 발생될 요지가 있으므로 변경하지 않음
		
		// LotProcessHistory
		lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);

		
		// Equipment Service
		if (viewLotInfo.getEQUIPMENTID().isEmpty()==false)
		{
			EquipmentService equipmentService = new EquipmentService();
			EQUIPMENT equipmentInfo = new EQUIPMENT();
			equipmentInfo.setKeyEQUIPMENTID(viewLotInfo.getEQUIPMENTID());
			try
			{
				equipmentInfo = (EQUIPMENT) equipmentInfo.excuteDML(SqlConstant.DML_SELECTROW);
			}
			catch (NoDataFoundException e)
			{
				// ({0}) 은 존재하지 않습니다.
				throw new CustomException("EQP-001", new Object[] {viewLotInfo.getEQUIPMENTID()});
			}
			equipmentService.EquipmentProcessEndCancel(equipmentInfo, lotInfo, txnInfo);
		}
		
		// DYP 전용 I/F 실적 로직
		DY_PROCESSRESULT oProcessResult = new DY_PROCESSRESULT();
		oProcessResult.setKeyPLANTID(lotInfo.getPLANTID());
		oProcessResult.setKeyLOTID(lotInfo.getKeyLOTID());
		oProcessResult.setKeyPROCESSID(viewLotInfo.getPROCESSID());
		oProcessResult.setKeyPROCESSSEQUENCE(viewLotInfo.getPROCESSSEQUENCE());
		oProcessResult.setKeyREWORKCOUNT(viewLotInfo.getREWORKCOUNT());
		
		oProcessResult.excuteDML(SqlConstant.DML_DELETE);
		
		return viewLotInfo;
	}
	
	/**
	 * 공정이 완료되었는지 공정Type으로 구분하여 확인
	 * 
	 * @param viewLotInfo
	 * @return boolean
	 * 
	 */
	@SuppressWarnings("unchecked")
	public boolean processCompleteCheck(VIEWLOTTRACKING viewLotInfo)
	{
		VIEWLOTTRACKING operationInfo = new VIEWLOTTRACKING();
		operationInfo.setKeyLOTID(viewLotInfo.getKeyLOTID());
		operationInfo.setPROCESSROUTEID(viewLotInfo.getPROCESSROUTEID());
		operationInfo.setROUTERELATIONSEQUENCE(viewLotInfo.getROUTERELATIONSEQUENCE());
		operationInfo.setINPUTMODE(Constant.OBJECTTYPE_OPERATION);
		operationInfo.setSTATUS(Constant.OPERATIONSTATE_WAITING);
		
		String suffix = " AND processRouteType != '" + Constant.PROCESSROUTETYPE_REWORK + "'";
		List<Object> listOperationInfo = (List<Object>) operationInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
		
		if ( listOperationInfo.size() > 0 )
		{
			boolean returnValue = false;
			for ( int i = 0; i < listOperationInfo.size(); i++ )
			{
				operationInfo = (VIEWLOTTRACKING) listOperationInfo.get(i);
				
				if ( !Constant.RECIPE_ID_REPEAT.equalsIgnoreCase(operationInfo.getPROCESSTYPE()) )
				{
					returnValue = true;
					break;
				}
			}
			
			return returnValue;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * LotTrackingService의 getProcessInfo를 통해 받아온 VIEWLOTTRACKING에 CurrentFlag Yes로 수정해 재배치
	 * LOTPROCESSHISTORY에 이력 기록
	 *  
	 * @param lotInfo
	 * @param viewLotInfo
	 * @param changeProcessID
	 * @param changeProcessSeq
	 * @param txnInfo
	 * @throws CustomException
	 * 
	 */
	public void makeReposition(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, String changeProcessID, Long changeProcessSeq, TxnInfo txnInfo)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();
		
		String lotID =		lotInfo.getKeyLOTID();
		
		// Status Check
		// 변경 후 공정의 공정 시작 상태 체크 및 CurrentFlag 변경
		// 변경 전 공정의 전체 CurrentFlag 변경 ( 추후 결정 )
		
		VIEWLOTTRACKING changeViewLotInfo = 
				trackingService.getProcessInfo(lotID, lotInfo.getPROCESSROUTEID(), changeProcessID, changeProcessSeq, viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_START);
		
		if ( Constant.FLAG_YN_YES.equalsIgnoreCase(changeViewLotInfo.getCURRENTFLAG()) || 
				Constant.OPERATIONSTATE_COMPLETE.equalsIgnoreCase(changeViewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
//		validation.validationLotState( lotID, viewLotInfo.getSTATUS(), new Object[] {Constant.OPERATIONSTATE_COMPLETE});
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );
		

		// CurrentFlag Update
		changeViewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
		changeViewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// Event Name
		txnInfo.setTxnId( Constant.EVENT_REPOSITION );
		
		// LotProcessHistory
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, changeViewLotInfo, txnInfo);
	}
	
	/**
	 * 받아온 정보로 VIEWLOTTRACKING 값을 설정 하고 설비를 changeEquipmentID로 수정
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param changeEquipmentID
	 * @param txnInfo
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING changeEquipment(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, String changeEquipmentID, TxnInfo txnInfo)
	{
		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		
		String suffix = " ORDER BY routerelationSequence, processPrintIndex, recipePrintIndex, orderIndex, timeKey ";
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
		for ( int i = 0; i < listViewLotInfo.size(); i++ )
		{
			viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(i);
			
			viewLotInfo.setEQUIPMENTID(changeEquipmentID);
			viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
		
		return viewLotInfo;
	}
	
	public void autoTrackingProcess(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
	}
}
