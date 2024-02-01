package mes.lot.tracking;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.lot.data.LOTCONSUMABLERATIOHISTORY;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.LOTPROCESSHISTORY;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.transaction.LotHistory;
import mes.lot.transaction.LotTrackingService;
import mes.lot.transaction.RecipeService;
import mes.lot.validation.LotValidation;
import mes.material.transaction.StockService;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnConsumable implements ObjectExecuteService
{
	/**
	 * 원료 투입 처리하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
	@Override
	public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		
		validation.checkListNull(dataList);
		
		HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(0);
		
		validation.checkKeyNull(dataMap, new Object[] {"LOTID", "TIMEKEY", "STARTTIME", "ENDTIME"});
		
		String lotID 					= dataMap.get("LOTID");
		String timeKey 					= dataMap.get("TIMEKEY");
		String startTime 				= dataMap.get("STARTTIME");
		String eventTime 				= dataMap.get("ENDTIME");
		// EventTime
		if ( eventTime != null && !eventTime.isEmpty() )
		{
			txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
		}
		txnInfo.setTxnComment( dataMap.get("COMMENT") );
		String resultValue 				= dataMap.get("RESULTVALUE");
		String alternativeConsumable	= dataMap.get("ALTERNATIVECONSUMABLE");
		

		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		lotInfo.setDESCRIPTION(dataMap.get("COMMENT"));

		
		// Get ViewLotTracking Info
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setKeyTIMEKEY( timeKey );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		// Status Check
		if ( Constant.RECIPEPARAMETERSTATE_COMPLETE.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		
		if ( alternativeConsumable != null && !alternativeConsumable.isEmpty() )
		{
			String originConsumable			= viewLotInfo.getRECIPEPARAMETERID();
			
			viewLotInfo.setRECIPEPARAMETERID( alternativeConsumable );
			viewLotInfo.setRECIPEPARAMETERNAME( alternativeConsumable + "*" );
			viewLotInfo.setALTERNATIVECONSUMABLE( originConsumable );
		}
		
		viewLotInfo.setRELATIONTIMEKEY( txnInfo.getEventTimeKey() );
		viewLotInfo.setRELATIONTIME( txnInfo.getEventTime() );
		viewLotInfo.setSTATUS( Constant.RECIPEPARAMETERSTATE_COMPLETE );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
		viewLotInfo.setPASTMODE( Constant.CONTROL_MODE_MANUAL );
		viewLotInfo.setRESULTVALUE( resultValue );
		viewLotInfo.setREASONCODE( txnInfo.getTxnReasonCode() );
		viewLotInfo.setREASONCODETYPE( txnInfo.getTxnReasonCodeType() );
		viewLotInfo.setCONSUMABLESTARTTIME( DateUtil.getTimestamp(startTime) );
		viewLotInfo.setCONSUMABLEENDTIME( DateUtil.getTimestamp(eventTime) );
		viewLotInfo.setEVENTUSERID( txnInfo.getTxnUser() );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		
		// 투입량 처리
		Double oldQuantity = lotInfo.getCURRENTQUANTITY();
		if ( Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(lotInfo.getPRODUCTTYPE()) )
		{
			// 진행량 증가 생략
		}
		else
		{
			lotInfo.setCURRENTQUANTITY( ConvertUtil.doubleAdd(oldQuantity, ConvertUtil.Object2Double(resultValue, 0.0)) );
			lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
		
		//원료 이력 관리
		for(int i = 0; i < dataList.size(); i++ ) {
			
			HashMap<String, String> mConsumableLot = (HashMap<String, String>) dataList.get(i);
			
			LOTCONSUMABLERATIOHISTORY consumableInfo = new LOTCONSUMABLERATIOHISTORY();
			
			consumableInfo.setKeyLOTID(lotInfo.getKeyLOTID());
			consumableInfo.setKeyTIMEKEY(viewLotInfo.getRELATIONTIMEKEY());
			consumableInfo.setKeyCONSUMABLESEQUENCE( ConvertUtil.Object2String(i) );
			consumableInfo.setCONSUMABLEID(viewLotInfo.getRECIPEPARAMETERID());
			consumableInfo.setCONSUMABLELOTID(mConsumableLot.get("MATERIALLOTID"));
			consumableInfo.setCONSUMABLEQUANTITY( ConvertUtil.Object2Double(mConsumableLot.get("MATERIALQTY"), 1.0) );
			consumableInfo.excuteDML(SqlConstant.DML_INSERT);
		}

		// MaterialStock 재고 처리
		StockService stockService = new StockService();
		stockService.ConsumableProcess(lotInfo, viewLotInfo, txnInfo);

		
		// History Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setOLDCURRENTQUANTITY( oldQuantity );
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);

		
		
		
		// Next Recipe Flag Update
		VIEWLOTTRACKING recipeLotInfo = trackingService.getNextRecipe(viewLotInfo, viewLotInfo.getRECIPESEQUENCE());
		if ( recipeLotInfo != null )
		{
			// OrderIndex 가 미지정인 경우
			// 동일 Order Index 가 완료된 경우
			if ( Constant.RECIPE_ID_CONSUMABLE.equalsIgnoreCase(recipeLotInfo.getRECIPETYPE()) && recipeLotInfo.getORDERINDEX() != null )
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
		
		if ( recipeLotInfo == null && Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getAUTOTRACKOUT()) )
		{
			// 다음 공정에 관한 처리
			VIEWLOTTRACKING endOperationLotInfo = 
					trackingService.getNextProcess(viewLotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_END, viewLotInfo.getPROCESSSEQUENCE()-1);
			
			if ( endOperationLotInfo != null )
			{
				RecipeService recipeService = new RecipeService();
				recipeService.checkAutoTrackOut(endOperationLotInfo, txnInfo);
			}
		}
		
		return recvDoc;
	}
}
