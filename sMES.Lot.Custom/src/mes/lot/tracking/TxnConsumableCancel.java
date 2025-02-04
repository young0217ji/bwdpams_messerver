package mes.lot.tracking;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.lot.data.LOTCONSUMABLERATIOHISTORY;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.LOTPROCESSHISTORY;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.transaction.LotHistory;
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
public class TxnConsumableCancel implements ObjectExecuteService
{
	/**
	 * 원료 투입 취소하는 트랜잭션 실행
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
		
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		
		validation.checkListNull(dataList);
		
		HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(0);
		
		validation.checkKeyNull(dataMap, new Object[] {"LOTID", "TIMEKEY"});
		
		String lotID 					= dataMap.get("LOTID");
		String timeKey 					= dataMap.get("TIMEKEY");

		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

		// Get ViewLotTracking Info
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setKeyTIMEKEY( timeKey );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		// Status Check
		if ( !Constant.RECIPEPARAMETERSTATE_COMPLETE.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
		
		
		// Lot ProcessHistory Cancel Update
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setKeyLOTID( lotID );
		lotProcessHistory.setKeyTIMEKEY( viewLotInfo.getRELATIONTIMEKEY() );
		lotProcessHistory = (LOTPROCESSHISTORY) lotProcessHistory.excuteDML(SqlConstant.DML_SELECTROW);
		
		lotProcessHistory.setCANCELFLAG(Constant.FLAG_YESNO_YES);
		lotProcessHistory.excuteDML(SqlConstant.DML_UPDATE);
		
		
		//원료 이력 관리
		LOTCONSUMABLERATIOHISTORY consumableInfo = new LOTCONSUMABLERATIOHISTORY();
		consumableInfo.setKeyLOTID(lotInfo.getKeyLOTID());
		consumableInfo.setKeyTIMEKEY(viewLotInfo.getRELATIONTIMEKEY());
		List<Object> listConsumableInfo = (List<Object>) consumableInfo.excuteDML(SqlConstant.DML_SELECTLIST);

		for ( int i = 0; i < listConsumableInfo.size(); i++ ) {
			consumableInfo = (LOTCONSUMABLERATIOHISTORY) listConsumableInfo.get(i);
			consumableInfo.excuteDML(SqlConstant.DML_DELETE);
		}
		
		// MaterialStock 재고 처리
		StockService stockService = new StockService();
		stockService.ConsumableCancelProcess(lotInfo, viewLotInfo, txnInfo);
	
		// 투입량 처리
		Double oldQuantity = lotInfo.getCURRENTQUANTITY();
		if ( Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(lotInfo.getPRODUCTTYPE()) )
		{
			// 진행량 증가 생략
		}
		else
		{
			lotInfo.setCURRENTQUANTITY( ConvertUtil.doubleSubtract(oldQuantity, ConvertUtil.Object2Double(viewLotInfo.getRESULTVALUE(), 0.0)) );
			lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
		
		
		// 원료투입 취소인 경우 다음 투입의 Flag 변경
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
				if ( changeViewLotInfo.getRECIPESEQUENCE() != null )
				{
					if ( changeViewLotInfo.getRECIPESEQUENCE() > viewLotInfo.getRECIPESEQUENCE() )
					{
						changeViewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_NO);
						changeViewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
					}
					else if ( changeViewLotInfo.getORDERINDEX() != null && changeViewLotInfo.getRECIPESEQUENCE().equals(viewLotInfo.getRECIPESEQUENCE()) )
					{
						if ( changeViewLotInfo.getORDERINDEX() > viewLotInfo.getORDERINDEX() )
						{
							changeViewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_NO);
							changeViewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
						}
					}
				}
			}
		}
		
		
		viewLotInfo.setRELATIONTIMEKEY( "" );
		viewLotInfo.setRELATIONTIME( null );
		viewLotInfo.setSTATUS( Constant.RECIPEPARAMETERSTATE_WAITING );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
		viewLotInfo.setPASTMODE( "" );
		viewLotInfo.setRESULTVALUE( "" );
		viewLotInfo.setREASONCODE( "" );
		viewLotInfo.setREASONCODETYPE( "" );
		viewLotInfo.setCONSUMABLESTARTTIME( null );
		viewLotInfo.setCONSUMABLEENDTIME( null );
		viewLotInfo.setEVENTUSERID( "" );
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// History Insert
		lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setOLDCURRENTQUANTITY( oldQuantity );
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);
		
		return recvDoc;
	}
}
