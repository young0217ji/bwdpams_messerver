package mes.lot.transaction;

import java.util.List;

import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.LOTPROCESSHISTORY;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.validation.LotValidation;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class RecipeService
{
	/**
	 * 받아온 데이터로 VIEWLOTTRACKING값 설정 후 스텝 시작 (InputMode : Recipe, ActionType : Start)
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void recipeStart(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();

		String lotID				= lotInfo.getKeyLOTID();

		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setRECIPEID( recipeID );
		viewLotInfo.setRECIPESEQUENCE(  ConvertUtil.String2Long(recipeSequence) );
		viewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
		viewLotInfo.setRECIPETYPECODE( recipeTypeCode );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_START );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);

		if ( !Constant.RECIPESTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		
		// 순차 진행 여부 체크
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(lotInfo.getGOINORDERREQUIRED()) )
		{
			trackingService.checkGoInOrder(viewLotInfo, txnInfo);
		}
		validation.validationLotState( lotID, lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED} );
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );

		// Recipe Info Update
		viewLotInfo.setRELATIONTIMEKEY( txnInfo.getEventTimeKey() );
		viewLotInfo.setRELATIONTIME( txnInfo.getEventTime() );
		viewLotInfo.setSTATUS( Constant.RECIPESTATE_COMPLETE );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
		viewLotInfo.setPASTMODE( pastMode );
		viewLotInfo.setEVENTUSERID( txnInfo.getTxnUser() );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);

		// RecipeParameter Flag Update
		List<VIEWLOTTRACKING> listRecipeParameterInfo = trackingService.getRecipeParameterList(viewLotInfo);
		if ( listRecipeParameterInfo != null && listRecipeParameterInfo.size() > 0 )
		{
			for ( int i = 0; i < listRecipeParameterInfo.size(); i++ )
			{
				VIEWLOTTRACKING recipeParameterInfo = listRecipeParameterInfo.get(i);
				recipeParameterInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				recipeParameterInfo.setRELATIONTIMEKEY( viewLotInfo.getRELATIONTIMEKEY() );
				recipeParameterInfo.setRELATIONTIME( viewLotInfo.getRELATIONTIME() );
				recipeParameterInfo.setSTATUS( Constant.RECIPEPARAMETERSTATE_PROCESSING );
				recipeParameterInfo.excuteDML(SqlConstant.DML_UPDATE);

				// Time 공정조건에 의한 QueueTime 관리
				if ( Constant.RECIPEPARAMETER_ID_TIME.equalsIgnoreCase(recipeParameterInfo.getRECIPEPARAMETERID()) )
				{
					LotQueueTimeManager.startLotQueueTime(viewLotInfo, recipeParameterInfo, txnInfo);
				}
			}
		}
		
		// Recipe Flag Update
		VIEWLOTTRACKING nextRecipeInfo = trackingService.getNextRecipe(viewLotInfo, viewLotInfo.getRECIPESEQUENCE());
		if ( nextRecipeInfo != null )
		{
			if ( Constant.RECIPE_ID_CONSUMABLE.equalsIgnoreCase(nextRecipeInfo.getRECIPETYPE()) && nextRecipeInfo.getORDERINDEX() != null )
			{
				RecipeService recipeService = new RecipeService();
				recipeService.updateNextConsumableRecipe(nextRecipeInfo);
			}
			else
			{
				nextRecipeInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				nextRecipeInfo.excuteDML(SqlConstant.DML_UPDATE);
			}
		}
		
		// Event Name
		txnInfo.setTxnId( viewLotInfo.getRECIPEID() + viewLotInfo.getRECIPETYPE() );
		if ( Constant.CONTROL_MODE_AUTO.equalsIgnoreCase(pastMode) )
		{
			txnInfo.setTxnUser( viewLotInfo.getEQUIPMENTID() );
		}

		// Lot ProcessHistory Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);

		
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

		
		// 동시 진행 라우팅에 대하여 Consumed 설정이 되어있는 경우 로직
		consumeLotProcess(lotInfo, viewLotInfo, txnInfo, pastMode);
		
		
		if ( nextRecipeInfo == null && Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getAUTOTRACKOUT()) )
		{
			// 현재 공정의 자동 공정종료에 관한 처리
			VIEWLOTTRACKING endOperationLotInfo = 
					trackingService.getProcessInfo(viewLotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getPROCESSID(), viewLotInfo.getPROCESSSEQUENCE(), viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_END);
			
			if ( endOperationLotInfo != null )
			{
				checkAutoTrackOut(endOperationLotInfo, txnInfo);
			}
		}
	}
	
	/**
	 * 받아온 데이터로 VIEWLOTTRACKING값 설정 후 스텝 종료 (InputMode : Recipe, ActionType : End)
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void recipeEnd(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();
	
		String lotID				= lotInfo.getKeyLOTID();
	
		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setRECIPEID( recipeID );
		viewLotInfo.setRECIPESEQUENCE(  ConvertUtil.String2Long(recipeSequence) );
		viewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
		viewLotInfo.setRECIPETYPECODE( recipeTypeCode );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_END );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
	
		// Recipe Start Info
		VIEWLOTTRACKING startViewLotInfo = new VIEWLOTTRACKING();
		startViewLotInfo.setKeyLOTID( lotID );
		startViewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		startViewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		startViewLotInfo.setRECIPEID( recipeID );
		startViewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
		startViewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		startViewLotInfo.setACTIONTYPE( Constant.EVENT_START );
		
		startViewLotInfo = (VIEWLOTTRACKING) startViewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		if ( Constant.RECIPESTATE_WAITING.equalsIgnoreCase(startViewLotInfo.getSTATUS()) )
		{
			// 현재 스텝은 해당 스텝의 시작({0})이 이루어 지지 않은 상태 입니다. 다시 상태를 확인해 주세요.
			throw new CustomException("LOT-013", new Object[] {startViewLotInfo.getRECIPENAME()});
		}
		
		if ( !Constant.RECIPESTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		
		// 순차 진행 여부 체크
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(lotInfo.getGOINORDERREQUIRED()) )
		{
			trackingService.checkGoInOrder(viewLotInfo, txnInfo);
		}
		validation.validationLotState( lotID, lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED} );
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );
		
		
		// Recipe Info Update
		viewLotInfo.setRELATIONTIMEKEY( txnInfo.getEventTimeKey() );
		viewLotInfo.setRELATIONTIME( txnInfo.getEventTime() );
		viewLotInfo.setSTATUS( Constant.RECIPESTATE_COMPLETE );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
		viewLotInfo.setPASTMODE( pastMode );
		viewLotInfo.setEVENTUSERID( txnInfo.getTxnUser() );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// Start Or Change RecipeParameter Info Update
		trackingService.recipeParameterComplete(viewLotInfo, startViewLotInfo, txnInfo);
		
		// Recipe Flag Update
		VIEWLOTTRACKING recipeLotInfo = trackingService.getNextRecipe(viewLotInfo, viewLotInfo.getRECIPESEQUENCE());
		if ( recipeLotInfo != null )
		{
			if ( Constant.RECIPE_ID_CONSUMABLE.equalsIgnoreCase(recipeLotInfo.getRECIPETYPE()) &&  recipeLotInfo.getORDERINDEX() != null )
			{
				RecipeService recipeService = new RecipeService();
				recipeService.updateNextConsumableRecipe(recipeLotInfo);
			}
			else
			{
				recipeLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				recipeLotInfo.excuteDML(SqlConstant.DML_UPDATE);
			}
		}
	
		// Event Name
		txnInfo.setTxnId( viewLotInfo.getRECIPEID() + viewLotInfo.getRECIPETYPE() );
		if ( Constant.CONTROL_MODE_AUTO.equalsIgnoreCase(pastMode) )
		{
			txnInfo.setTxnUser( viewLotInfo.getEQUIPMENTID() );
		}
	
		// Lot ProcessHistory Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);
	
	
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
	
		
		// 동시 진행 라우팅에 대하여 Consumed 설정이 되어있는 경우 로직
		consumeLotProcess(lotInfo, viewLotInfo, txnInfo, pastMode);
		
		
		if ( recipeLotInfo == null && Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getAUTOTRACKOUT()) )
		{
			// 다음 공정에 관한 처리
			VIEWLOTTRACKING endOperationLotInfo = 
					trackingService.getProcessInfo(viewLotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getPROCESSID(), viewLotInfo.getPROCESSSEQUENCE(), viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_END);
			
			if ( endOperationLotInfo != null )
			{
				checkAutoTrackOut(endOperationLotInfo, txnInfo);
			}
		}
	}

	/**
	 * 받아온 데이터로 VIEWLOTTRACKING값 설정 후 스텝 변경 (InputMode : Recipe, ActionType : Change)
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void recipeChange(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();

		String lotID				= lotInfo.getKeyLOTID();

		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setRECIPEID( recipeID );
		viewLotInfo.setRECIPESEQUENCE(  ConvertUtil.String2Long(recipeSequence) );
		viewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
		viewLotInfo.setRECIPETYPECODE( recipeTypeCode );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_CHANGE );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);

		// Recipe Start Info
		VIEWLOTTRACKING startViewLotInfo = new VIEWLOTTRACKING();
		startViewLotInfo.setKeyLOTID( lotID );
		startViewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		startViewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		startViewLotInfo.setRECIPEID( recipeID );
		startViewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
		startViewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		startViewLotInfo.setACTIONTYPE( Constant.EVENT_START );
		
		startViewLotInfo = (VIEWLOTTRACKING) startViewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);

		if ( Constant.RECIPESTATE_WAITING.equalsIgnoreCase(startViewLotInfo.getSTATUS()) )
		{
			// 현재 스텝은 해당 스텝의 시작({0})이 이루어 지지 않은 상태 입니다. 다시 상태를 확인해 주세요.
			throw new CustomException("LOT-013", new Object[] {startViewLotInfo.getRECIPENAME()});
		}
		
		if ( !Constant.RECIPESTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		
		// 순차 진행 여부 체크
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(lotInfo.getGOINORDERREQUIRED()) )
		{
			trackingService.checkGoInOrder(viewLotInfo, txnInfo);
		}
		validation.validationLotState( lotID, lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED} );
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );

		// Recipe Info Update
		viewLotInfo.setRELATIONTIMEKEY( txnInfo.getEventTimeKey() );
		viewLotInfo.setRELATIONTIME( txnInfo.getEventTime() );
		viewLotInfo.setSTATUS( Constant.RECIPESTATE_COMPLETE );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
		viewLotInfo.setPASTMODE( pastMode );
		viewLotInfo.setEVENTUSERID( txnInfo.getTxnUser() );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// Time Parameter 에 대한 Queue 관리
//		LotTimeManager.changeLotQueueTime(viewLotInfo);

		// Event Name
		txnInfo.setTxnId( viewLotInfo.getRECIPEID() + viewLotInfo.getRECIPETYPE() );
		if ( Constant.CONTROL_MODE_AUTO.equalsIgnoreCase(pastMode) )
		{
			txnInfo.setTxnUser( viewLotInfo.getEQUIPMENTID() );
		}

		// Lot ProcessHistory Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);


		// Change RecipeParameterList
		List<VIEWLOTTRACKING> listChangeRecipeParameter = trackingService.getRecipeParameterList(viewLotInfo);
		
		if ( listChangeRecipeParameter != null && listChangeRecipeParameter.size() > 0 )
		{
			for ( int i = 0; i < listChangeRecipeParameter.size(); i++ )
			{
				// RecipeParameter Flag Update
				VIEWLOTTRACKING changeRecipeParameter = listChangeRecipeParameter.get(i);
				changeRecipeParameter.setRELATIONTIMEKEY( viewLotInfo.getRELATIONTIMEKEY() );
				changeRecipeParameter.setRELATIONTIME( viewLotInfo.getRELATIONTIME() );
				changeRecipeParameter.setSTATUS( Constant.RECIPEPARAMETERSTATE_PROCESSING );
				changeRecipeParameter.setCURRENTFLAG( Constant.FLAG_YN_YES );
				changeRecipeParameter.excuteDML(SqlConstant.DML_UPDATE);
				
				// Time 공정조건에 의한 QueueTime 관리
				if ( Constant.RECIPEPARAMETER_ID_TIME.equalsIgnoreCase(changeRecipeParameter.getRECIPEPARAMETERID()) )
				{
					LotQueueTimeManager.changeLotQueueTime(startViewLotInfo, viewLotInfo, changeRecipeParameter, txnInfo);
				}

				// Get ChangeSpecHistory
			}
		}

		// Recipe Flag Update
		VIEWLOTTRACKING recipeLotInfo = trackingService.getNextRecipe(viewLotInfo, viewLotInfo.getRECIPESEQUENCE());
		if ( recipeLotInfo != null )
		{
			if ( Constant.RECIPE_ID_CONSUMABLE.equalsIgnoreCase(recipeLotInfo.getRECIPETYPE()) &&  recipeLotInfo.getORDERINDEX() != null )
			{
				RecipeService recipeService = new RecipeService();
				recipeService.updateNextConsumableRecipe(recipeLotInfo);
			}
			else
			{
				recipeLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				recipeLotInfo.excuteDML(SqlConstant.DML_UPDATE);
			}
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
		
		
		// 동시 진행 라우팅에 대하여 Consumed 설정이 되어있는 경우 로직
		consumeLotProcess(lotInfo, viewLotInfo, txnInfo, pastMode);
		
		
		if ( recipeLotInfo == null && Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getAUTOTRACKOUT()) )
		{
			// 다음 공정에 관한 처리
			VIEWLOTTRACKING endOperationLotInfo = 
					trackingService.getProcessInfo(viewLotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getPROCESSID(), viewLotInfo.getPROCESSSEQUENCE(), viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_END);
			
			if ( endOperationLotInfo != null )
			{
				checkAutoTrackOut(endOperationLotInfo, txnInfo);
			}
		}
	}
	
	/**
	 * 받아온 데이터로 VIEWLOTTRACKING값 설정 후 상태 Complete, CurrentFlag No 수정
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void recipeCheck(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();

		String lotID				= lotInfo.getKeyLOTID();

		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setRECIPEID( recipeID );
		viewLotInfo.setRECIPESEQUENCE(  ConvertUtil.String2Long(recipeSequence) );
		viewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
		viewLotInfo.setRECIPETYPECODE( recipeTypeCode );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);

		if ( !Constant.RECIPESTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		
		// 순차 진행 여부 체크
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(lotInfo.getGOINORDERREQUIRED()) )
		{
			trackingService.checkGoInOrder(viewLotInfo, txnInfo);
		}
		validation.validationLotState( lotID, lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED} );
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );

		// Recipe Info Update
		viewLotInfo.setRELATIONTIMEKEY( txnInfo.getEventTimeKey() );
		viewLotInfo.setRELATIONTIME( txnInfo.getEventTime() );
		viewLotInfo.setSTATUS( Constant.RECIPESTATE_COMPLETE );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
		viewLotInfo.setPASTMODE( pastMode );
		viewLotInfo.setEVENTUSERID( txnInfo.getTxnUser() );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);

		// Recipe Flag Update
		VIEWLOTTRACKING nextRecipeInfo = trackingService.getNextRecipe(viewLotInfo, viewLotInfo.getRECIPESEQUENCE());
		if ( nextRecipeInfo != null )
		{
			if ( Constant.RECIPE_ID_CONSUMABLE.equalsIgnoreCase(nextRecipeInfo.getRECIPETYPE()) && nextRecipeInfo.getORDERINDEX() != null )
			{
				RecipeService recipeService = new RecipeService();
				recipeService.updateNextConsumableRecipe(nextRecipeInfo);
			}
			else
			{
				nextRecipeInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				nextRecipeInfo.excuteDML(SqlConstant.DML_UPDATE);
			}
		}
		
		// Event Name
		txnInfo.setTxnId( viewLotInfo.getRECIPEID() + viewLotInfo.getRECIPETYPE() );
		if ( Constant.CONTROL_MODE_AUTO.equalsIgnoreCase(pastMode) )
		{
			txnInfo.setTxnUser( viewLotInfo.getEQUIPMENTID() );
		}

		// Lot ProcessHistory Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);

		
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

		
		// 동시 진행 라우팅에 대하여 Consumed 설정이 되어있는 경우 로직
		consumeLotProcess(lotInfo, viewLotInfo, txnInfo, pastMode);
		
		
		if ( nextRecipeInfo == null && Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getAUTOTRACKOUT()) )
		{
			// 현재 공정의 자동 공정종료에 관한 처리
			VIEWLOTTRACKING endOperationLotInfo = 
					trackingService.getProcessInfo(viewLotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getPROCESSID(), viewLotInfo.getPROCESSSEQUENCE(), viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_END);
			
			if ( endOperationLotInfo != null )
			{
				checkAutoTrackOut(endOperationLotInfo, txnInfo);
			}
		}
	}
	
	/**
	 * 받아온 데이터로  VIEWLOTTRACKING값 설정 후 상태를 Waiting로 수정하여 스텝 취소
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 * @throws CustomException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void recipeProcessCancel(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();

		String lotID				= lotInfo.getKeyLOTID();

		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setRECIPEID( recipeID );
		viewLotInfo.setRECIPESEQUENCE(  ConvertUtil.String2Long(recipeSequence) );
		viewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
		viewLotInfo.setRECIPETYPECODE( recipeTypeCode );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);

		if ( !Constant.EVENT_END.equalsIgnoreCase(viewLotInfo.getACTIONTYPE()) )
		{
			// 이후 스텝에 대하여 완료된 상태로 남아 있는지 여부 확인
			VIEWLOTTRACKING nextViewLotInfo = new VIEWLOTTRACKING();
			nextViewLotInfo.setKeyLOTID( lotID );
			nextViewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
			nextViewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
			nextViewLotInfo.setRECIPEID( recipeID );
			nextViewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
			nextViewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
			nextViewLotInfo.setSTATUS( Constant.RECIPESTATE_COMPLETE );

			String suffix = " AND RECIPESEQUENCE > " + recipeSequence;
			
			List<Object> listNextInfo = (List<Object>) nextViewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);

			if ( listNextInfo.size() > 0 )
			{
				// 현재 스텝({0})은 진행이 취소되지 않은 이후 스텝이 존재합니다. 다시 상태를 확인해 주세요.
				throw new CustomException("LOT-014", new Object[] {viewLotInfo.getRECIPENAME()});
			}
		}
		
		if ( Constant.RECIPESTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
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
		
		
		// 스텝 취소인 경우 다음 스텝의 Flag 변경
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
			
			if ( Constant.OBJECTTYPE_OPERATION.equalsIgnoreCase(changeViewLotInfo.getINPUTMODE()) )
			{
				if ( Constant.EVENT_END.equalsIgnoreCase(changeViewLotInfo.getACTIONTYPE()) )
				{
					changeViewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_NO);
					changeViewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
				}
			}
			else
			{
				if ( changeViewLotInfo.getRECIPESEQUENCE() != null && changeViewLotInfo.getRECIPESEQUENCE() > viewLotInfo.getRECIPESEQUENCE() )
				{
					changeViewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_NO);
					changeViewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
				}
			}
		}
		
		
		// Recipe Info Update
		viewLotInfo.setRELATIONTIMEKEY( "" );
		viewLotInfo.setRELATIONTIME( null );
		viewLotInfo.setSTATUS( Constant.RECIPESTATE_WAITING );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
		viewLotInfo.setPASTMODE( "" );
		viewLotInfo.setEVENTUSERID( "" );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// Event Name
		txnInfo.setTxnId( viewLotInfo.getRECIPEID() + viewLotInfo.getRECIPETYPE() + "Cancel" );

		// Lot ProcessHistory Insert
		lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);
	}
	
	/**
	 * 받아온 데이터로  VIEWLOTTRACKING값 설정 후 상태를 Complete로 수정하여 스텝 진행 
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void recipeProcess(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();

		String lotID				= lotInfo.getKeyLOTID();

		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setRECIPEID( recipeID );
		viewLotInfo.setRECIPESEQUENCE(  ConvertUtil.String2Long(recipeSequence) );
		viewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
		viewLotInfo.setRECIPETYPECODE( recipeTypeCode );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);

		if ( !Constant.RECIPESTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		
		validation.validationLotState( lotID, lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED} );
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );

		// Recipe Flag Update
		VIEWLOTTRACKING recipeLotInfo = trackingService.getNextRecipe(viewLotInfo, viewLotInfo.getRECIPESEQUENCE());
		if ( recipeLotInfo != null )
		{
			if ( Constant.RECIPE_ID_CONSUMABLE.equalsIgnoreCase(recipeLotInfo.getRECIPETYPE()) &&  recipeLotInfo.getORDERINDEX() != null )
			{
				RecipeService recipeService = new RecipeService();
				recipeService.updateNextConsumableRecipe(recipeLotInfo);
			}
			else
			{
				recipeLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				recipeLotInfo.excuteDML(SqlConstant.DML_UPDATE);
			}
		}
		
		// Recipe Info Update
		viewLotInfo.setRELATIONTIMEKEY( txnInfo.getEventTimeKey() );
		viewLotInfo.setRELATIONTIME( txnInfo.getEventTime() );
		viewLotInfo.setSTATUS( Constant.RECIPESTATE_COMPLETE );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
		viewLotInfo.setPASTMODE( pastMode );
		viewLotInfo.setEVENTUSERID( txnInfo.getTxnUser() );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// Lot ProcessHistory Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);
	}
	
	public void consumeLotProcess(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo, String pastMode)
	{
		if ( viewLotInfo.getCONSUMEID() != null && !viewLotInfo.getCONSUMEID().isEmpty() )
		{
//			try 
//			{
//				CONSUMECOMPOSITION consumeComposition = new CONSUMECOMPOSITION();
//				consumeComposition.setKeyPLANTID(lotInfo.getPLANTID());
//				consumeComposition.setKeyCONSUMEVERSION(viewLotInfo.getCONSUMETYPE());
//				consumeComposition.setKeyCONSUMEID(viewLotInfo.getCONSUMEID());
//
//				List<Object> listConsumeComposition = (List) consumeComposition.excuteDML(SqlConstant.DML_SELECTLIST);
//
//				for ( int i = 0; i < listConsumeComposition.size(); i++ )
//				{
//					consumeComposition = (CONSUMECOMPOSITION) listConsumeComposition.get(i);
//
//					if ( !viewLotInfo.getPROCESSROUTEID().equalsIgnoreCase(consumeComposition.getKeyPROCESSROUTEID()) )
//					{
//						VIEWLOTTRACKING viewConsumeLotInfo = new VIEWLOTTRACKING();
//						viewConsumeLotInfo.setKeyWORKORDER( viewLotInfo.getKeyWORKORDER() );
//						viewConsumeLotInfo.setCOMPOSITIONID( consumeComposition.getKeyROCCOMPOSITIONID() );
//						viewConsumeLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
//
//						viewConsumeLotInfo = (VIEWLOTTRACKING) viewConsumeLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
//
//						if ( !viewConsumeLotInfo.getSTATUS().equalsIgnoreCase(Constant.RECIPESTATE_COMPLETE) && 
//								(
//										viewConsumeLotInfo.getRECIPETYPE().equalsIgnoreCase(Constant.EVENT_START) || 
//										viewConsumeLotInfo.getRECIPETYPE().equalsIgnoreCase(Constant.EVENT_END) || 
//										viewConsumeLotInfo.getRECIPETYPE().equalsIgnoreCase(Constant.EVENT_CHANGE) 
//										)
//								)
//						{
//							boolean flag = false;
//
//							if ( viewConsumeLotInfo.getCURRENTFLAG().equalsIgnoreCase(Constant.FLAG_YN_NO) )
//							{
//								ProcessRouteService processRouteService = new ProcessRouteService();
//								PROCESSROUTE processRoute = processRouteService.getProcessRouteInfo(lotInfo.getPLANTID(), viewConsumeLotInfo.getPROCESSROUTEID());
//
//								// 동시 진행 처리될 스텝에 대한 현재 진행 스텝이 아닌 경우 진행 불가
//								// 동시 진행 라우팅({0} : {1})의 공정진행이 이루어지지 않아 해당 라우팅의 공정({2} : {3}) 진행이 불가능합니다.
//								throw new CustomException("OM-098", 
//										new Object[] {processRoute.getDESCRIPTION(), viewConsumeLotInfo.getPROCESSROUTETYPE(), viewConsumeLotInfo.getPROCESSID(), viewConsumeLotInfo.getPROCESSNAME()});
//							}
//
//							if ( viewConsumeLotInfo.getRECIPETYPE().equalsIgnoreCase(Constant.EVENT_START) )
//							{
//								recipeStart(viewConsumeLotInfo.getKeyLOTID(), viewConsumeLotInfo.getPROCESSID(), String.valueOf(viewConsumeLotInfo.getPROCESSSEQUENCE()), 
//										viewConsumeLotInfo.getRECIPEID(), String.valueOf(viewConsumeLotInfo.getRECIPESEQUENCE()), viewConsumeLotInfo.getRECIPERELATIONCODE(), viewConsumeLotInfo.getRECIPETYPECODE(), 
//										txnInfo, pastMode);
//
//								flag = true;
//							}
//							else if ( viewConsumeLotInfo.getRECIPETYPE().equalsIgnoreCase(Constant.EVENT_END) )
//							{
//								// 적가 물량 이송
//
//								recipeEnd(viewConsumeLotInfo.getKeyLOTID(), viewConsumeLotInfo.getPROCESSID(), String.valueOf(viewConsumeLotInfo.getPROCESSSEQUENCE()), 
//										viewConsumeLotInfo.getRECIPEID(), String.valueOf(viewConsumeLotInfo.getRECIPESEQUENCE()), viewConsumeLotInfo.getRECIPERELATIONCODE(), viewConsumeLotInfo.getRECIPETYPECODE(), 
//										txnInfo, pastMode);
//
//								flag = true;
//							}
//							else if ( viewConsumeLotInfo.getRECIPETYPE().equalsIgnoreCase(Constant.EVENT_CHANGE) )
//							{
//								recipeChange(viewConsumeLotInfo.getKeyLOTID(), viewConsumeLotInfo.getPROCESSID(), String.valueOf(viewConsumeLotInfo.getPROCESSSEQUENCE()), 
//										viewConsumeLotInfo.getRECIPEID(), String.valueOf(viewConsumeLotInfo.getRECIPESEQUENCE()), viewConsumeLotInfo.getRECIPERELATIONCODE(), viewConsumeLotInfo.getRECIPETYPECODE(), 
//										txnInfo, pastMode);
//
//								flag = true;
//							}
//						}
//					}
//				}
//			}
//			catch ( Exception e )
//			{
//				// 임시 적가 Process 사용 중지
//			}
		}
	}
	
	/**
	 * 받아온 데이터로  VIEWLOTTRACKING값 설정 후 다음 Recipe값 상태 waiting으로 수정
	 * 
	 * @param viewLotInfo
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void updateNextConsumableRecipe(VIEWLOTTRACKING viewLotInfo)
	{
		VIEWLOTTRACKING consumableViewLotInfo = new VIEWLOTTRACKING();
		consumableViewLotInfo.setKeyLOTID( viewLotInfo.getKeyLOTID() );
		consumableViewLotInfo.setPROCESSROUTEID( viewLotInfo.getPROCESSROUTEID() );
		consumableViewLotInfo.setPROCESSID( viewLotInfo.getPROCESSID() );
		consumableViewLotInfo.setPROCESSSEQUENCE( viewLotInfo.getPROCESSSEQUENCE() );
		consumableViewLotInfo.setROUTERELATIONSEQUENCE( viewLotInfo.getROUTERELATIONSEQUENCE() );
		consumableViewLotInfo.setRECIPEID( viewLotInfo.getRECIPEID() );
		consumableViewLotInfo.setRECIPERELATIONCODE( viewLotInfo.getRECIPERELATIONCODE() );
		consumableViewLotInfo.setRECIPETYPECODE( viewLotInfo.getRECIPETYPECODE() );
		consumableViewLotInfo.setRECIPESEQUENCE( viewLotInfo.getRECIPESEQUENCE() );
		consumableViewLotInfo.setORDERINDEX( viewLotInfo.getORDERINDEX() );
		consumableViewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		consumableViewLotInfo.setSTATUS( Constant.RECIPESTATE_WAITING );
		
		List<Object> listViewLotInfo = (List<Object>) consumableViewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( listViewLotInfo != null && listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				viewLotElement.setCURRENTFLAG( Constant.FLAG_YN_YES );
				viewLotElement.excuteDML(SqlConstant.DML_UPDATE);
			}
		}
		else
		{
			viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
			viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
	}
	
	/**
	 * 받아온 데이터로 VIEWLOTTRACKING값 설정 후 자동으로 공정 종료
	 * 
	 * @param endOperationLotInfo
	 * @param txnInfo
	 * @return 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void checkAutoTrackOut(VIEWLOTTRACKING endOperationLotInfo, TxnInfo txnInfo)
	{
		// 전체 스텝 진행 여부 체크
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID(endOperationLotInfo.getKeyLOTID());
		viewLotInfo.setPROCESSID(endOperationLotInfo.getPROCESSID());
		viewLotInfo.setPROCESSSEQUENCE(endOperationLotInfo.getPROCESSSEQUENCE());
		viewLotInfo.setPROCESSROUTEID(endOperationLotInfo.getPROCESSROUTEID());
		viewLotInfo.setPROCESSROUTETYPE(endOperationLotInfo.getPROCESSROUTETYPE());
		viewLotInfo.setROUTERELATIONSEQUENCE(endOperationLotInfo.getROUTERELATIONSEQUENCE());
		viewLotInfo.setINPUTMODE(Constant.OBJECTTYPE_RECIPE);
		viewLotInfo.setSTATUS(Constant.RECIPESTATE_WAITING);
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		// 자동 공정 시작 로직
		if ( listViewLotInfo.size() < 1 && Constant.FLAG_YESNO_YES.equalsIgnoreCase(endOperationLotInfo.getAUTOTRACKOUT()) )
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
			
			
			ProcessService processService = new ProcessService();
			LotTrackingService trackingService = new LotTrackingService();
			
			
			// LotInformation
			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID( endOperationLotInfo.getKeyLOTID() );
			
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			VIEWLOTTRACKING trackingLotInfo = processService.makeTrackOut(lotInfo, endOperationLotInfo.getPROCESSID(), String.valueOf(endOperationLotInfo.getPROCESSSEQUENCE()), 
					endOperationLotInfo.getROUTERELATIONSEQUENCE(), null, sTxnInfo, Constant.CONTROL_MODE_MANUAL);
			
			
			if ( Constant.RECIPE_ID_PACKING.equalsIgnoreCase(trackingLotInfo.getPROCESSTYPE()) &&  
					Constant.RECIPE_ID_PACKING.equalsIgnoreCase(trackingLotInfo.getPACKINGFLAG()) )
			{
				// 포장여부 확인
//				if ( !checkExistPacking(lotInfo) )
//				{
//					// 진행중인 Lot({0}) 에 대한 포장내역이 등록되지 않아 처리할 수 없습니다.
//					throw new CustomException("LOT-009", new Object[] {lotInfo.getKeyLOTID()});
//				}
			}
			
			
			// Repeat Operation TrackOut
			// Measure Operation TrackOut
			boolean inspectionFlag = false;
			String resultValue = "";
			
			// Measure 일 경우 결과에 따라 Repeat 공정으로 이동
			// Repeat 공정인 경우 재측정을 위해 Measure 공정으로 이동 로직 필요
			if ( Constant.RECIPE_ID_REPEAT.equalsIgnoreCase(trackingLotInfo.getPROCESSTYPE()) )
			{
				if ( trackingService.repeatProcess(lotInfo, trackingLotInfo, txnInfo) != null )
				{
					return;
				}
			}
			else if ( Constant.RECIPE_ID_MEASURE.equalsIgnoreCase(trackingLotInfo.getPROCESSTYPE()) )
			{
				VIEWLOTTRACKING inspectionLotInfo = trackingService.getInspectionParameter(trackingLotInfo);
				
				if ( inspectionLotInfo != null )
				{
					inspectionFlag = true;
				}
			}

			
			// 다음 공정에 대한 처리 Logic
			VIEWLOTTRACKING nextViewLotInfo = 
					trackingService.getNextProcess(lotInfo.getKeyLOTID(), trackingLotInfo.getPROCESSROUTEID(), trackingLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_START, trackingLotInfo.getPROCESSSEQUENCE());
			
			if ( nextViewLotInfo == null )
			{
				processService.processEndDetailCaseNull(lotInfo, trackingLotInfo, txnInfo);
			}
			else
			{
				processService.processEndDetailCaseNotNull(lotInfo, trackingLotInfo, nextViewLotInfo, inspectionFlag, resultValue, txnInfo);
			}
		}
	}
}
