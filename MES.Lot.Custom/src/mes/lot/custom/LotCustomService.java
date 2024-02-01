package mes.lot.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import kr.co.mesframe.util.object.ObjectUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.lot.data.FUTUREACTION;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.LOTPROCESSHISTORY;
import mes.lot.data.SUBLOTDATA;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.data.WORKORDER;
import mes.lot.lotdata.LotCreateInfo;
import mes.lot.transaction.LotHistory;
import mes.lot.transaction.LotService;
import mes.lot.transaction.LotTrackingService;
import mes.lot.transaction.ProcessRouteService;
import mes.lot.transaction.ProductService;
import mes.lot.validation.LotValidation;
import mes.master.data.DY_AUTOLOTCREATEERROR;
import mes.master.data.MODELINGCONFIRM;
import mes.master.data.PROCESSROUTE;
import mes.master.data.PRODUCTDEFINITION;
import mes.master.data.TPPOLICY;
import mes.util.NameGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12
 * 
 * @see
 */
public class LotCustomService {
	private static final transient Logger logger = LoggerFactory.getLogger(LotCustomService.class);

	/**
	 * LotCreateInfo에 받아온 데이터로 LOTINFORMATION 값 설정 후 Lot 생성 LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param createLotInfo
	 * @param bChangeProcessFlag
	 * @param txnInfo
	 * @return LOTINFORMATION
	 * @throws CustomException
	 * 
	 */
	public LOTINFORMATION createLot(LotCreateInfo createLotInfo, Boolean bChangeProcessFlag, TxnInfo txnInfo) {
//		LotValidation validation = new LotValidation();
		ProcessRouteService processRouteService = new ProcessRouteService();
		LotHistory addHistory = new LotHistory();
		LotService lotService = new LotService();

		String plantID = createLotInfo.getPLANTID();
		String workOrder = createLotInfo.getWORKORDER();
		String productID = createLotInfo.getPRODUCTID();
		String department = createLotInfo.getDEPARTMENT();
		String areaID = createLotInfo.getAREAID();
		String lotID = createLotInfo.getLOTID();

		// PLANQUANTITY 생산 지시량
		// WOPLANSTARTTIME 생산 계획 시작 일시
		// WOPLANENDTIME 생산 계획 종료 일시

		// Data 객체 생성
		LOTINFORMATION lotInfo = new LOTINFORMATION();

		// Key Value Setting
		lotInfo.setKeyLOTID(lotID);

		ProductService productService = new ProductService();
		PRODUCTDEFINITION productInfo = productService.getProductInfo(plantID, productID);

		// Data Value Setting
		lotInfo.setPRODUCTID(productInfo.getKeyPRODUCTID());
		lotInfo.setPRODUCTTYPE(productInfo.getPRODUCTTYPE());

		// Get ProcessRoute And Type
		MODELINGCONFIRM modelingConfirm = new MODELINGCONFIRM();
		modelingConfirm.setKeyPLANTID(plantID);
		modelingConfirm.setKeyPRODUCTID(productInfo.getKeyPRODUCTID());
		modelingConfirm.setKeyBOMID(createLotInfo.getBOMID());
		modelingConfirm.setKeyBOMVERSION(createLotInfo.getBOMVERSION());
		modelingConfirm.setKeyPOLICYNAME(createLotInfo.getPOLICYNAME());
		modelingConfirm.setKeyPOLICYVALUE(createLotInfo.getPOLICYVALUE());
		modelingConfirm.setKeyCONDITIONTYPE(createLotInfo.getCONDITIONTYPE());
		modelingConfirm.setKeyCONDITIONID(createLotInfo.getCONDITIONID());

		// 기준정보가 확정인지 여부 체크하여 진행
		try {
			modelingConfirm = (MODELINGCONFIRM) modelingConfirm.excuteDML(SqlConstant.DML_SELECTROW);
		} catch (Exception e) {
			// 공정 기준정보가 확정정보를 가져올수 없습니다. 기준정보 등록여부를 확인하시고 확정하여 사용하시기 바랍니다.
			throw new CustomException("MD-016", new Object[] {});
		}
		if (Constant.FLAG_YESNO_NO.equals(modelingConfirm.getCONFIRMFLAG())) {
			// 공정 기준정보가 확정되지 않아 진행이 불가능합니다. 기준정보 등록여부를 확인하시고 확정하여 사용하시기 바랍니다.
			throw new CustomException("MD-017", new Object[] {});
		}

		String processRouteID = modelingConfirm.getPROCESSROUTEID();
		PROCESSROUTE processRoute = processRouteService.getProcessRouteInfo(plantID, processRouteID);

		// ProcessFlow 가 Rep, Main, Partial 여부에 따라 로직 변경
		lotInfo.setREPROUTEID(processRouteID);
		lotInfo.setBOMID(createLotInfo.getBOMID());
		lotInfo.setBOMVERSION(createLotInfo.getBOMVERSION());
		lotInfo.setPOLICYNAME(createLotInfo.getPOLICYNAME());
		lotInfo.setPOLICYVALUE(createLotInfo.getPOLICYVALUE());
		lotInfo.setCONDITIONTYPE(createLotInfo.getCONDITIONTYPE());
		lotInfo.setCONDITIONID(createLotInfo.getCONDITIONID());
		lotInfo.setLOTTYPE(createLotInfo.getLOTTYPE());
		lotInfo.setCONDITIONVALUE("");
		lotInfo.setWORKORDER(workOrder);
		lotInfo.setROOTLOTID(lotID);

		if (Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(productInfo.getPRODUCTTYPE())) {
			lotInfo.setPRODUCTUNITQUANTITY(ConvertUtil.Object2String(createLotInfo.getSTARTQUANTITY()));
			lotInfo.setCURRENTQUANTITY(createLotInfo.getSTARTQUANTITY());
		} else {
			lotInfo.setPRODUCTUNITQUANTITY(ConvertUtil.Object2String(productInfo.getPRODUCTQUANTITY()));
			lotInfo.setCURRENTQUANTITY(createLotInfo.getSTARTQUANTITY());
		}
//		lotInfo.setSTARTDUEDATE( woPlanStartTime );
//		lotInfo.setENDDUEDATE( woPlanEndTime );
		if (createLotInfo.getPRIORITY() != null && createLotInfo.getPRIORITY().isEmpty() == false) {
			lotInfo.setPRIORITY(createLotInfo.getPRIORITY());
		} else {
			lotInfo.setPRIORITY("5");
		}
		lotInfo.setPLANTID(plantID);
		lotInfo.setAREAID(areaID);
		lotInfo.setDEPARTMENT(department);
		lotInfo.setCREATETIME(txnInfo.getEventTime());
		lotInfo.setCREATEUSERID(txnInfo.getTxnUser());
//		lotInfo.setRATIOCUTTINGPERCENT( productDefinition.getRATIOCUTTINGPERCENT() );
		lotInfo.setLOTGRADE(Constant.LOTGRADE_GOOD);
		lotInfo.setHOLDSTATE(Constant.LOTHOLDSTATE_NOTONHOLD);
		lotInfo.setREWORKSTATE(Constant.LOTREWORKSTATE_NOTINREWORK);
		lotInfo.setLOTSTATE(Constant.LOTSTATE_CREATED);
		lotInfo.setSHIPSTATE(Constant.SHIPPINGSTATE_WAIT);
		lotInfo.setREWORKCOUNT(Long.valueOf(0));
		lotInfo.setDESCRIPTION("");
		lotInfo.setGOINORDERREQUIRED(Constant.FLAG_YESNO_YES);

		// 이력 기록
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setOLDCURRENTQUANTITY(0.0);

		// Make Lot ViewLotTracking Data
		if (Constant.PROCESSROUTETYPE_REP.equalsIgnoreCase(processRoute.getPROCESSROUTETYPE())) {
//			// Get Process Flow List By RepProcessFlowName (Multi)
//			List<ROUTECOMPOSITION> listRouteComposition = processRouteService.getRouteCompositionList( plantID, processRouteID );
//			
//			for ( int i = 0; i < listRouteComposition.size(); i++ )
//			{
//				ROUTECOMPOSITION reouteComposition = listRouteComposition.get(i);
//
//				// Condition 인 경우 조건에 맞는 지 확인 후 처리
//				if ( reouteComposition.getRELATIONTYPE().equalsIgnoreCase(Constant.PROCESSROUTERELATION_CONDITION) )
//				{
//					if ( reouteComposition.getCOMPOSITIONID().equalsIgnoreCase(lotInfo.getCONDITIONID()) && 
//							reouteComposition.getCOMPOSITIONVALUE().equalsIgnoreCase(lotInfo.getCONDITIONVALUE()) )
//					{
//					}
//					else
//					{
//						continue;
//					}
//				}
//				
//				// Process Flow Information
//				PROCESSROUTE processRouteInfo = processRouteService.getProcessRouteInfo(plantID, reouteComposition.getKeyPROCESSROUTEID());
//				
//				// Rep ProcessRoute 구성인 경우 LotID + EquipmentID 으로 LotID 발번 ( Partail 라우팅 일 경우 )
//				if ( !processRouteInfo.getPROCESSROUTETYPE().equalsIgnoreCase(Constant.PROCESSROUTETYPE_MAIN) && 
//						reouteComposition.getRELATIONTYPE().equalsIgnoreCase(Constant.PROCESSROUTERELATION_CONCURRENCY) )
//				{
//					lotInfo.setKeyLOTID( lotID + "_" + processRouteInfo.getPROCESSROUTECODE() );
//				}
//				else
//				{
//					lotInfo.setKeyLOTID( lotID );
//				}
//				
//				lotInfo.setPROCESSROUTEID( processRouteInfo.getKeyPROCESSROUTEID() );
//				lotInfo.setPROCESSROUTETYPE( processRouteInfo.getPROCESSROUTETYPE() );
//				
//				if ( !previewFlag && !reouteComposition.getRELATIONTYPE().equalsIgnoreCase(Constant.PROCESSROUTERELATION_CONDITION) )
//				{
//					createLot(lotInfo);
//				}
//
//				trackingService.insertViewLotTracking(lotInfo, productDefinition, reouteComposition, processRouteInfo.getKeyPROCESSROUTEID(), 
//						processRouteInfo.getPROCESSROUTETYPE(), listConsumableOrderInfo, routePolicyInfo, previewFlag);
//				
//				// 이력 기록
//				if ( !previewFlag && !reouteComposition.getRELATIONTYPE().equalsIgnoreCase(Constant.PROCESSROUTERELATION_CONDITION) )
//				{
//					addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
//				}
//			}
		} else {
			lotInfo.setPROCESSROUTEID(processRouteID);
			lotInfo.setPROCESSROUTETYPE(processRoute.getPROCESSROUTETYPE());

			lotService.createLot(lotInfo, txnInfo);

			// Single Flow
			lotService.insertViewLotTracking(lotInfo, createLotInfo, modelingConfirm);

			// 이력 기록
			addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
		}

		logger.info("Lot 생성 완료");

		// Event Name
		txnInfo.setTxnId(Constant.EVENT_LOTSTART);
		txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
		startLot(lotID, bChangeProcessFlag, txnInfo);

		logger.info("Lot 시작 완료");

		return lotInfo;
	}

	/**
	 * 받아온 LotID의 상태를 Terminated로 변경해 생성된 Lot 취소 LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotID
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void createCancelLot(String lotID, TxnInfo txnInfo) {
		txnInfo.setTxnId(Constant.EVENT_LOTSTARTCANCEL);
		txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

		startCancelLot(lotID, txnInfo);

		txnInfo.setTxnId(Constant.EVENT_CREATELOTCANCEL);
		txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

//		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
//		LotTrackingService trackingService = new LotTrackingService();

		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID(lotID);

		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

//		validation.validationState( txnInfo.getTxnId(), Constant.STATE_WO_LOTCREATED, woState );
//		validation.validationState( txnInfo.getTxnId(), Constant.STATE_WO_DEPLOYED, woState );
//		validation.validationNotState( txnInfo.getTxnId(), Constant.STATE_WO_COMPLETED, woState );
//		validation.validationNotState( txnInfo.getTxnId(), Constant.STATE_WO_CONFIRMED, woState );
//		validation.validationNotState( txnInfo.getTxnId(), Constant.STATE_WO_CREATED, woState );
//		validation.validationNotState( txnInfo.getTxnId(), Constant.STATE_WO_DELETED, woState );
//		validation.validationNotState( txnInfo.getTxnId(), Constant.STATE_WO_RELEASED, woState );
//		validation.validationNotState( txnInfo.getTxnId(), Constant.STATE_WO_RESERVED, woState );
//		validation.validationMergeLot(lotInfo);

//		trackingService.deleteViewLotTracking( lotInfo.getPRODUCTREQUESTNAME(), false );

		lotInfo.setLOTSTATE(Constant.LOTSTATE_TERMINATED);
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);

		// 2012.05.16 StartCancelLot 시점으로 변경
		// SMessageUtil.send( CreateMessage.getAreaInterfaceType(lotInfo.getPLANTID(),
		// lotInfo.getAREAID()), CreateMessage.createLotComplete(lotName,
		// lotInfo.getPRODUCTREQUESTNAME()) );

		// History Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setOLDCURRENTQUANTITY(lotInfo.getCURRENTQUANTITY());
		lotInfo.setCURRENTQUANTITY(0.0);
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
	}

	/**
	 * 생성된 Lot의 상태를 Released로 변경해 생산진행 시작 VIEWLOTTRACKING의 Current Flag를 Y로 설정
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotID
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void startLot(String lotID, TxnInfo txnInfo) {
		startLot(lotID, false, txnInfo);
	}

	/**
	 * 생성된 Lot의 상태를 Released로 변경해 생산진행 시작 VIEWLOTTRACKING의 Current Flag를 Y로 설정
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotID
	 * @param bChangeProcessFlag
	 * @param txnInfo
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void startLot(String lotID, Boolean bChangeProcessFlag, TxnInfo txnInfo) {
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();

		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID(lotID);

		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

		validation.validationLotState(lotID, lotInfo.getLOTSTATE(), new Object[] { Constant.LOTSTATE_CREATED });

		lotInfo.setLOTSTATE(Constant.LOTSTATE_RELEASED);
		lotInfo.setRELEASETIME(txnInfo.getEventTime());
		lotInfo.setRELEASEUSERID(txnInfo.getTxnUser());

		lotInfo.excuteDML(SqlConstant.DML_UPDATE);

		// Current Flag Update
		VIEWLOTTRACKING viewLotInfo = trackingService.getNextProcess(lotID, lotInfo.getPROCESSROUTEID(), null,
				Constant.EVENT_START);
		if (viewLotInfo != null && bChangeProcessFlag == false) {
			// 예약 작업 설정에 대한 처리
			if (Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getFUTUREACTIONFLAG())) {
				LotService lotService = new LotService();

				FUTUREACTION futureAction = new FUTUREACTION();
				futureAction.setKeyLOTID(viewLotInfo.getKeyLOTID());
				futureAction.setKeyRELATIONTIMEKEY(viewLotInfo.getKeyTIMEKEY());
				futureAction.setACTIONSTATE(Constant.ACTION_STATE_RESERVE);

				List<Object> listFutureAction = (List<Object>) futureAction.excuteDML(SqlConstant.DML_SELECTLIST);

				for (int i = 0; i < listFutureAction.size(); i++) {
					futureAction = (FUTUREACTION) listFutureAction.get(i);

					TxnInfo sTxnInfo = new TxnInfo();
					sTxnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
					sTxnInfo.setEventTime(txnInfo.getEventTime());
					sTxnInfo.setTxnUser(txnInfo.getTxnUser());
					sTxnInfo.setTxnComment(txnInfo.getTxnComment());
					sTxnInfo.setTxnReasonCodeType(futureAction.getREASONCODETYPE());
					sTxnInfo.setTxnReasonCode(futureAction.getREASONCODE());

					if (Constant.ACTION_TYPE_HOLD.equalsIgnoreCase(futureAction.getACTIONTYPE())) {
						sTxnInfo.setTxnId(Constant.EVENT_MAKELOTHOLD);

						lotService.makeLotHold(lotInfo, sTxnInfo);
					} else if (Constant.ACTION_TYPE_SCRAP.equalsIgnoreCase(futureAction.getACTIONTYPE())) {
						sTxnInfo.setTxnId(Constant.EVENT_MAKELOTSCRAP);

						lotService.makeLotScrap(lotInfo, sTxnInfo);
					}

					lotService.releaseFutureAction(lotInfo, futureAction.getKeyRELATIONTIMEKEY(),
							futureAction.getKeySEQUENCE(), Constant.ACTION_STATE_COMPLETE, sTxnInfo);
				}
			}

			viewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_YES);
			viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}

		// History Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setOLDCURRENTQUANTITY(lotInfo.getCURRENTQUANTITY());
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);

		if (viewLotInfo != null && bChangeProcessFlag == false) {
			// 자동 공정 시작 로직
			if (Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getAUTOTRACKIN())) {
				TxnInfo sTxnInfo = new TxnInfo();
				sTxnInfo.setTxnId(txnInfo.getTxnId());
				sTxnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
				sTxnInfo.setEventTime(txnInfo.getEventTime());
				sTxnInfo.setTxnUser(txnInfo.getTxnUser());
				sTxnInfo.setTxnComment(txnInfo.getTxnComment());
				sTxnInfo.setScanFlag(txnInfo.getScanFlag());

				if (txnInfo.getWorkType() != null && !txnInfo.getWorkType().isEmpty()) {
					String[] arType = txnInfo.getWorkType().split("_");
					sTxnInfo.setWorkType(arType[0] + "_ROUTING");
				} else {
					sTxnInfo.setWorkType(txnInfo.getWorkType());
				}

				sTxnInfo.setDeviceAddress(txnInfo.getDeviceAddress());
				sTxnInfo.setDeviceCode(txnInfo.getDeviceCode());
				sTxnInfo.setTxnReasonCode(txnInfo.getTxnReasonCode());
				sTxnInfo.setTxnReasonCodeType(txnInfo.getTxnReasonCodeType());

				ProcessCustomService operationService = new ProcessCustomService();
				operationService.processStart(lotID, viewLotInfo.getPROCESSID(),
						String.valueOf(viewLotInfo.getPROCESSSEQUENCE()), viewLotInfo.getROUTERELATIONSEQUENCE(),
						sTxnInfo, Constant.CONTROL_MODE_MANUAL);
			}
		}
	}

	/**
	 * 생산시작된 Lot의 상태를 Created로 변경해 Lot 시작 취소 VIEWLOTTRACKING의 Y인 Current Flag를 N으로
	 * 변경 LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotID
	 * @param txnInfo
	 * @return
	 * @throws CustomException
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startCancelLot(String lotID, TxnInfo txnInfo) {
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();

		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID(lotID);

		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

		// Lot 상태 및 최종 이력, 뱃치 구성 여부 체크 로직
		validation.validationLotState(lotID, lotInfo.getLOTSTATE(), new Object[] { Constant.LOTSTATE_RELEASED });

		String historySql = "SELECT * FROM lotprocesshistory "
				+ "WHERE lotID = ? AND IIF(ISNULL(cancelflag, '') = '', 'No', cancelflag) = 'No' "
				+ "ORDER BY timekey DESC ";

		List<LinkedCaseInsensitiveMap> listLotProcessHistory = SqlMesTemplate.queryForList(historySql,
				new Object[] { lotID });
		LinkedCaseInsensitiveMap mapLotprocessHistory = listLotProcessHistory.get(0);

		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory = (LOTPROCESSHISTORY) SqlQueryUtil.returnRowToData(lotProcessHistory, mapLotprocessHistory);

		String lastEventName = lotProcessHistory.getEVENTNAME();
		if (!Constant.EVENT_LOTSTART.equalsIgnoreCase(lastEventName)) {
			VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
			viewLotInfo.setKeyLOTID(lotID);

			String surfix = " AND STATUS != '" + Constant.OPERATIONSTATE_WAITING + "' ";

			List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, surfix);

			if (listViewLotInfo.size() > 0) {
				// 이미 공정진행 된 Lot ({0}) 에 대해서는 시작 취소를 진행 할 수 없습니다. (최종 이벤트 : {1})
				throw new CustomException("LOT-003", new Object[] { lotInfo.getKeyLOTID(), lastEventName });
			} else {
				lotProcessHistory = new LOTPROCESSHISTORY();
				lotProcessHistory.setKeyLOTID(lotID);
				lotProcessHistory.setCANCELFLAG(Constant.FLAG_YESNO_NO);
				lotProcessHistory.setEVENTNAME(Constant.EVENT_LOTSTART);
				lotProcessHistory = (LOTPROCESSHISTORY) lotProcessHistory.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

				// Lot ProcessHistory Cancel Update
				lotProcessHistory.setCANCELFLAG(Constant.FLAG_YESNO_YES);
				lotProcessHistory.excuteDML(SqlConstant.DML_UPDATE);
			}
		} else {
			// Lot ProcessHistory Cancel Update
			lotProcessHistory.setCANCELFLAG(Constant.FLAG_YESNO_YES);
			lotProcessHistory.excuteDML(SqlConstant.DML_UPDATE);
		}

		String updateSql = "UPDATE VIEWLOTTRACKING SET CURRENTFLAG = '" + Constant.FLAG_YN_NO + "' " + "WHERE LOTID = '"
				+ lotID + "' AND CURRENTFLAG = '" + Constant.FLAG_YN_YES + "' ";
		SqlMesTemplate.update(updateSql);

		lotInfo.setLOTSTATE(Constant.LOTSTATE_CREATED);
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);

		// History Insert
		lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setOLDCURRENTQUANTITY(0.0);
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
	}

	/**
	 * Lot을 Hold 처리
	 * 
	 * @param lotInfo
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void makeLotHold(LOTINFORMATION lotInfo, TxnInfo txnInfo) {
		LotService lotService = new LotService();
		lotService.makeLotHold(lotInfo, txnInfo);
	}

	/**
	 * Hold Lot을 Release 처리 LOTPROCESSHISTORY에 이력기록
	 * 
	 * @param lotInfo
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void makeLotRelease(LOTINFORMATION lotInfo, TxnInfo txnInfo) {
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();

		validation.validationLotState(lotInfo.getKeyLOTID(), lotInfo.getLOTSTATE(),
				new Object[] { Constant.LOTSTATE_CREATED, Constant.LOTSTATE_RELEASED, Constant.LOTSTATE_EMPTIED });
		validation.validationLotState(lotInfo.getKeyLOTID(), lotInfo.getHOLDSTATE(),
				new Object[] { Constant.LOTHOLDSTATE_ONHOLD });

		lotInfo.setHOLDSTATE(Constant.LOTHOLDSTATE_NOTONHOLD);
		lotInfo.setDESCRIPTION("(" + txnInfo.getTxnReasonCode() + ") " + txnInfo.getTxnComment());

		lotInfo.excuteDML(SqlConstant.DML_UPDATE);

		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
	}

	/**
	 * Lot을 Scrap 처리
	 * 
	 * @param lotInfo
	 * @param txnInfo
	 * @return
	 */
	public void makeLotScrap(LOTINFORMATION lotInfo, TxnInfo txnInfo) {
		LotService lotService = new LotService();
		lotService.makeLotScrap(lotInfo, txnInfo);
	}

	/**
	 * Scrap된 Lot을 Unscrap 처리 LOTPROCESSHISTORY에 이력기록
	 * 
	 * @param lotInfo
	 * @param txnInfo
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void makeLotUnscrap(LOTINFORMATION lotInfo, TxnInfo txnInfo) {
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();

		validation.validationLotState(lotInfo.getKeyLOTID(), lotInfo.getLOTSTATE(),
				new Object[] { Constant.LOTSTATE_SCRAPPED });

		LOTPROCESSHISTORY lotHistory = new LOTPROCESSHISTORY();
		lotHistory.setKeyLOTID(lotInfo.getKeyLOTID());

		String suffix = " AND lotState != '" + Constant.LOTSTATE_SCRAPPED + "' ORDER BY timeKey DESC ";

		List<Object> listLotHistory = (List<Object>) lotHistory.excuteDML(SqlConstant.DML_SELECTLIST, suffix);

		String oldState = "";
		if (listLotHistory.size() > 0) {
			lotHistory = (LOTPROCESSHISTORY) listLotHistory.get(0);
			oldState = lotHistory.getLOTSTATE();
		} else {
			// 오류처리
		}

		lotInfo.setLOTSTATE(oldState);
		lotInfo.setDESCRIPTION("(" + txnInfo.getTxnReasonCode() + ") " + txnInfo.getTxnComment());

		lotInfo.excuteDML(SqlConstant.DML_UPDATE);

		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
	}

	/**
	 * Lot을 Split 처리
	 * 
	 * @param lotInfo
	 * @param splitQuantity
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void makeLotSplit(LOTINFORMATION lotInfo, Double splitQuantity, TxnInfo txnInfo) {
		makeLotSplit(lotInfo, splitQuantity, null, txnInfo);
	}

	/**
	 * splitQuantity를 mapSubLot에 분할 LOTPROCESSHISTORY에 이력기록
	 * 
	 * @param lotInfo
	 * @param splitQuantity
	 * @param mapSubLot
	 * @param txnInfo
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void makeLotSplit(LOTINFORMATION lotInfo, Double splitQuantity, HashMap<Long, String> mapSubLot,
			TxnInfo txnInfo) {
		NameGenerator nameGenerator = new NameGenerator();
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotService lotService = new LotService();

		validation.validationLotState(lotInfo.getKeyLOTID(), lotInfo.getLOTSTATE(),
				new Object[] { Constant.LOTSTATE_CREATED, Constant.LOTSTATE_RELEASED });
		validation.validationNumber("분할수량", 0, lotInfo.getCURRENTQUANTITY(), ConvertUtil.Object2String(splitQuantity));

		String splitLotID = nameGenerator.nameGenerate(lotInfo.getPLANTID(), "SplitLotID",
				new Object[] { lotInfo.getKeyLOTID() });
		Double oldQty = lotInfo.getCURRENTQUANTITY();

		lotInfo.setCURRENTQUANTITY(ConvertUtil.doubleSubtract(lotInfo.getCURRENTQUANTITY(), splitQuantity));
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);

		// History Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setOLDCURRENTQUANTITY(oldQty);
		lotProcessHistory.setSOURCELOTID(lotInfo.getKeyLOTID());
		lotProcessHistory.setDESTINATIONLOTID(splitLotID);
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);

		// Split Lot Generation
		LOTINFORMATION splitLotInfo = new LOTINFORMATION();
		splitLotInfo = (LOTINFORMATION) ObjectUtil.copyORMData(lotInfo);

		splitLotInfo.setKeyLOTID(splitLotID);
		splitLotInfo.setSOURCELOTID(lotInfo.getKeyLOTID());
		splitLotInfo.setCURRENTQUANTITY(splitQuantity);
		splitLotInfo.setREWORKCOUNT(Long.valueOf(0));
		splitLotInfo.setCARRIERID("");
		splitLotInfo.setASSIGNSLOT("");
		splitLotInfo.setCREATEUSERID(txnInfo.getTxnUser());
		splitLotInfo.setCREATETIME(txnInfo.getEventTime());
		splitLotInfo.excuteDML(SqlConstant.DML_INSERT);

		// Target Sub Lot 에 대한 처리
		if (Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(lotInfo.getPRODUCTTYPE()) && mapSubLot != null
				&& mapSubLot.size() > 0) {
			// Sub Lot 에 대한 처리
			transferSubLot(lotInfo, splitLotID, mapSubLot, txnInfo);
		}

		// ViewLotTracking Insert
		List<List<Object>> insertViewLotData = new ArrayList<List<Object>>();

		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID(lotInfo.getKeyLOTID());
		viewLotInfo.setPROCESSROUTEID(lotInfo.getPROCESSROUTEID());
		viewLotInfo.setROUTERELATIONSEQUENCE(null);

		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		for (int i = 0; i < listViewLotInfo.size(); i++) {
			viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(i);

			viewLotInfo.setKeyLOTID(splitLotInfo.getKeyLOTID());

			insertViewLotData.add(lotService.createViewLotRow(viewLotInfo));
		}
		lotService.createViewLotTracking(insertViewLotData);

		// History Insert
		LOTPROCESSHISTORY splitLotProcessHistory = new LOTPROCESSHISTORY();
		splitLotProcessHistory.setOLDCURRENTQUANTITY(0.0);
		splitLotProcessHistory.setSOURCELOTID(lotInfo.getKeyLOTID());
		splitLotProcessHistory.setDESTINATIONLOTID(splitLotID);
		addHistory.addLotProcessHistory(splitLotProcessHistory, splitLotInfo, null, txnInfo);
	}

	/**
	 * Lot을 Merge 처리
	 * 
	 * @param sourceLotInfo
	 * @param targetLotInfo
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void makeLotMerge(LOTINFORMATION sourceLotInfo, LOTINFORMATION targetLotInfo, TxnInfo txnInfo) {
		makeLotMerge(sourceLotInfo, targetLotInfo, null, txnInfo);
	}

	/**
	 * Lot의 CURRENTQUANTITY가 Target Lot으로 Merge LOTPROCESSHISTORY에 이력기록
	 * 
	 * @param sourceLotInfo
	 * @param targetLotInfo
	 * @param mapSubLot
	 * @param txnInfo
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void makeLotMerge(LOTINFORMATION sourceLotInfo, LOTINFORMATION targetLotInfo,
			HashMap<Long, String> mapSubLot, TxnInfo txnInfo) {
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();

		if (!sourceLotInfo.getLOTSTATE().equalsIgnoreCase(targetLotInfo.getLOTSTATE())) {
			// ({0}) 로트의 상태 ({2}) 와 ({1}) 로트의 상태 ({3}) 가 달라 처리 할수 없습니다.
			throw new CustomException("LOT-015", new Object[] { sourceLotInfo.getKeyLOTID(),
					sourceLotInfo.getLOTSTATE(), targetLotInfo.getKeyLOTID(), targetLotInfo.getLOTSTATE() });
		}

		validation.validationLotState(sourceLotInfo.getKeyLOTID(), sourceLotInfo.getLOTSTATE(),
				new Object[] { Constant.LOTSTATE_CREATED, Constant.LOTSTATE_RELEASED });
		validation.validationLotState(targetLotInfo.getKeyLOTID(), targetLotInfo.getLOTSTATE(),
				new Object[] { Constant.LOTSTATE_CREATED, Constant.LOTSTATE_RELEASED });

		Double sourceOldQty = sourceLotInfo.getCURRENTQUANTITY();
		Double targetOldQty = targetLotInfo.getCURRENTQUANTITY();

		targetLotInfo.setCURRENTQUANTITY(ConvertUtil.doubleAdd(sourceOldQty, targetOldQty));
		targetLotInfo.excuteDML(SqlConstant.DML_UPDATE);

		LOTPROCESSHISTORY targetLotProcessHistory = new LOTPROCESSHISTORY();
		targetLotProcessHistory.setOLDCURRENTQUANTITY(targetOldQty);
		targetLotProcessHistory.setSOURCELOTID(sourceLotInfo.getKeyLOTID());
		targetLotProcessHistory.setDESTINATIONLOTID(targetLotInfo.getKeyLOTID());
		addHistory.addLotProcessHistory(targetLotProcessHistory, targetLotInfo, null, txnInfo);

		// Source Lot Purge
		sourceLotInfo.setCURRENTQUANTITY(0.0);
		sourceLotInfo.setLOTSTATE(Constant.LOTSTATE_EMPTIED);
		sourceLotInfo.excuteDML(SqlConstant.DML_UPDATE);

		LOTPROCESSHISTORY sourceLotProcessHistory = new LOTPROCESSHISTORY();
		sourceLotProcessHistory.setOLDCURRENTQUANTITY(sourceOldQty);
		sourceLotProcessHistory.setSOURCELOTID(sourceLotInfo.getKeyLOTID());
		sourceLotProcessHistory.setDESTINATIONLOTID(targetLotInfo.getKeyLOTID());
		addHistory.addLotProcessHistory(sourceLotProcessHistory, sourceLotInfo, null, txnInfo);

		// Sub Lot 에 대한 처리
		if (Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(sourceLotInfo.getPRODUCTTYPE()) && mapSubLot != null
				&& mapSubLot.size() > 0) {
			transferSubLot(sourceLotInfo, targetLotInfo.getKeyLOTID(), mapSubLot, txnInfo);
		}
	}

	/**
	 * Lot의 CURRENTQUANTITY가 Target Lot의 CURRENTQUANTITY으로 이동 LOTPROCESSHISTORY에
	 * 이력기록
	 * 
	 * @param sourceLotInfo
	 * @param targetLotInfo
	 * @param mapSubLot
	 * @param txnInfo
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void makeLotTransfer(LOTINFORMATION sourceLotInfo, LOTINFORMATION targetLotInfo,
			HashMap<Long, String> mapSubLot, TxnInfo txnInfo) {
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();

		if (!sourceLotInfo.getLOTSTATE().equalsIgnoreCase(targetLotInfo.getLOTSTATE())) {
			// ({0}) 로트의 상태 ({2}) 와 ({1}) 로트의 상태 ({3}) 가 달라 처리 할수 없습니다.
			throw new CustomException("LOT-015", new Object[] { sourceLotInfo.getKeyLOTID(),
					sourceLotInfo.getLOTSTATE(), targetLotInfo.getKeyLOTID(), targetLotInfo.getLOTSTATE() });
		}

		validation.validationLotState(sourceLotInfo.getKeyLOTID(), sourceLotInfo.getLOTSTATE(),
				new Object[] { Constant.LOTSTATE_CREATED, Constant.LOTSTATE_RELEASED });
		validation.validationLotState(targetLotInfo.getKeyLOTID(), targetLotInfo.getLOTSTATE(),
				new Object[] { Constant.LOTSTATE_CREATED, Constant.LOTSTATE_RELEASED });

		Double sourceOldQty = sourceLotInfo.getCURRENTQUANTITY();
		Double targetOldQty = targetLotInfo.getCURRENTQUANTITY();
		Double transferQty = ConvertUtil.Object2Double(mapSubLot.size());

		validation.validationNumber("TransferQuantity", 0, sourceOldQty, String.valueOf(transferQty));

		// Target Lot
		targetLotInfo.setCURRENTQUANTITY(ConvertUtil.doubleAdd(transferQty, targetOldQty));
		targetLotInfo.excuteDML(SqlConstant.DML_UPDATE);

		LOTPROCESSHISTORY targetLotProcessHistory = new LOTPROCESSHISTORY();
		targetLotProcessHistory.setOLDCURRENTQUANTITY(targetOldQty);
		targetLotProcessHistory.setSOURCELOTID(sourceLotInfo.getKeyLOTID());
		targetLotProcessHistory.setDESTINATIONLOTID(targetLotInfo.getKeyLOTID());
		addHistory.addLotProcessHistory(targetLotProcessHistory, targetLotInfo, null, txnInfo);

		// Source Lot
		sourceLotInfo.setCURRENTQUANTITY(ConvertUtil.doubleSubtract(sourceOldQty, transferQty));
		if (sourceLotInfo.getCURRENTQUANTITY().compareTo(0.0) == 0) {
			sourceLotInfo.setLOTSTATE(Constant.LOTSTATE_EMPTIED);
		}
		sourceLotInfo.excuteDML(SqlConstant.DML_UPDATE);

		LOTPROCESSHISTORY sourceLotProcessHistory = new LOTPROCESSHISTORY();
		sourceLotProcessHistory.setOLDCURRENTQUANTITY(sourceOldQty);
		sourceLotProcessHistory.setSOURCELOTID(sourceLotInfo.getKeyLOTID());
		sourceLotProcessHistory.setDESTINATIONLOTID(targetLotInfo.getKeyLOTID());
		addHistory.addLotProcessHistory(sourceLotProcessHistory, sourceLotInfo, null, txnInfo);

		// Sub Lot 에 대한 처리
		if (Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(sourceLotInfo.getPRODUCTTYPE()) && mapSubLot != null
				&& mapSubLot.size() > 0) {
			transferSubLot(sourceLotInfo, targetLotInfo.getKeyLOTID(), mapSubLot, txnInfo);
		}
	}

	/**
	 * Quantity를 changeQuantity로 변경하여 scrp 처리
	 * 
	 * @param lotInfo
	 * @param changeQuantity
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void makeScrapQuantity(LOTINFORMATION lotInfo, Double changeQuantity, TxnInfo txnInfo) {
		makeScrapQuantity(lotInfo, changeQuantity, null, txnInfo);
	}

	/**
	 * lotInfo의 CURRENTQUANTITY를 받아온 changeQuantity 값을 더한 값으로 변경하며 Lot 상태도 변경
	 * LOTPROCESSHISTORY에 이력기록
	 * 
	 * @param lotInfo
	 * @param changeQuantity
	 * @param mapSubLot
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void makeScrapQuantity(LOTINFORMATION lotInfo, Double changeQuantity, HashMap<Long, String> mapSubLot,
			TxnInfo txnInfo) {
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();

		validation.validationLotState(lotInfo.getKeyLOTID(), lotInfo.getLOTSTATE(),
				new Object[] { Constant.LOTSTATE_CREATED, Constant.LOTSTATE_RELEASED, Constant.LOTSTATE_EMPTIED });

		Double oldQuantity = lotInfo.getCURRENTQUANTITY();
		if (changeQuantity.compareTo(0.0) > 0) {
			// 합산인 경우
			txnInfo.setTxnId(Constant.EVENT_TYPE_UNSCRAP);

			// Sub Lot 에 대한 처리
			if (Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(lotInfo.getPRODUCTTYPE()) && mapSubLot != null
					&& mapSubLot.size() > 0) {
				unscrapSubLot(lotInfo, mapSubLot, txnInfo);
			}
		} else if (changeQuantity.compareTo(0.0) < 0) {
			validation.validationNumber("불량처리수량", 0.0, oldQuantity,
					ConvertUtil.Object2String(Math.abs(changeQuantity)));

			// 차감인 경우
			txnInfo.setTxnId(Constant.EVENT_TYPE_SCRAP);

			// Sub Lot 에 대한 처리
			if (Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(lotInfo.getPRODUCTTYPE()) && mapSubLot != null
					&& mapSubLot.size() > 0) {
				scrapSubLot(lotInfo, mapSubLot, txnInfo);
			}
		}

		lotInfo.setCURRENTQUANTITY(ConvertUtil.doubleAdd(oldQuantity, changeQuantity));
		if (lotInfo.getCURRENTQUANTITY().compareTo(0.0) <= 0) {
			lotInfo.setLOTSTATE(Constant.LOTSTATE_EMPTIED);
		} else {
			if (Constant.LOTSTATE_EMPTIED.equalsIgnoreCase(lotInfo.getLOTSTATE())) {
				if (lotInfo.getRELEASETIME() != null) {
					lotInfo.setLOTSTATE(Constant.LOTSTATE_RELEASED);
				} else {
					lotInfo.setLOTSTATE(Constant.LOTSTATE_CREATED);
				}
			}
		}
		lotInfo.setDESCRIPTION("(" + txnInfo.getTxnReasonCode() + ") " + txnInfo.getTxnComment());
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);

		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setOLDCURRENTQUANTITY(oldQuantity);
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
	}

	/**
	 * 받아온 LotID와 timeKey를 가지고 예약작업 리스트를 가져옴
	 * 
	 * @param lotID
	 * @param timeKey
	 * @return String
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String getFutureActionSeq(String lotID, String timeKey) {
		FUTUREACTION futureAction = new FUTUREACTION();
		futureAction.setKeyLOTID(lotID);
		futureAction.setKeyRELATIONTIMEKEY(timeKey);

		List<Object> listFutureAction = (List<Object>) futureAction.excuteDML(SqlConstant.DML_SELECTLIST);

		return ConvertUtil.Object2String(listFutureAction.size() + 1);
	}

	/**
	 * 받아온 lotInfo의 LotID를 가지고 VIEWLOTTRACKING의 FUTUREACTIONFLAG를 Yes로 설정
	 * LOTPROCESSHISTORY에 이력기록
	 * 
	 * @param lotInfo
	 * @param futureAction
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void setFutureAction(LOTINFORMATION lotInfo, FUTUREACTION futureAction, TxnInfo txnInfo) {
		LotHistory addHistory = new LotHistory();

		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID(futureAction.getKeyLOTID());
		viewLotInfo.setKeyTIMEKEY(futureAction.getKeyRELATIONTIMEKEY());
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);

		viewLotInfo.setFUTUREACTIONFLAG(Constant.FLAG_YESNO_YES);
		viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);

		// History Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
	}

	/**
	 * 예약작업 해제 LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param timeKey
	 * @param sequence
	 * @param type
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void releaseFutureAction(LOTINFORMATION lotInfo, String timeKey, String sequence, String type,
			TxnInfo txnInfo) {
		LotService lotService = new LotService();
		lotService.releaseFutureAction(lotInfo, timeKey, sequence, type, txnInfo);

		// History Insert
		LotHistory addHistory = new LotHistory();
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
	}

	/**
	 * 받아온 lotInfo의 LotID SubLot을 transferLotID로 이동 SUBLOTDATA에 이력 기록
	 * 
	 * @param lotInfo
	 * @param transferLotID
	 * @param mapSubLot
	 * @param txnInfo
	 * @return
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void transferSubLot(LOTINFORMATION lotInfo, String transferLotID, HashMap<Long, String> mapSubLot,
			TxnInfo txnInfo) {
		// History 기록이 필요한 경우 사용
		AddHistory history = new AddHistory();

		SUBLOTDATA subLotInfo = new SUBLOTDATA();
		subLotInfo.setLOTID(lotInfo.getKeyLOTID());
		List<Object> listSubLot = (List<Object>) subLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);

		for (int i = 0; i < listSubLot.size(); i++) {
			subLotInfo = (SUBLOTDATA) listSubLot.get(i);

			for (Iterator iterator = mapSubLot.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Long, String> iteratorMap = (Map.Entry<Long, String>) iterator.next();
				if (subLotInfo.getKeySUBLOTID().equalsIgnoreCase(iteratorMap.getValue())) {
					subLotInfo.setLOTID(transferLotID);
					subLotInfo.setLOCATIONID(iteratorMap.getKey());
					subLotInfo.excuteDML(SqlConstant.DML_UPDATE);

					history.addHistory(subLotInfo, txnInfo, SqlConstant.DML_UPDATE);

					break;
				}
			}
		}
	}

	/**
	 * 받아온 lotInfo의 LotID를 가지고 SUBLOTDATA의 LotID 상태를 Scrapped로 설정해 scrap 처리
	 * SUBLOTDATA에 이력 기록
	 * 
	 * @param lotInfo
	 * @param mapSubLot
	 * @param txnInfo
	 * @return
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void scrapSubLot(LOTINFORMATION lotInfo, HashMap<Long, String> mapSubLot, TxnInfo txnInfo) {
		// History 기록이 필요한 경우 사용
		AddHistory history = new AddHistory();

		SUBLOTDATA subLotInfo = new SUBLOTDATA();
		subLotInfo.setLOTID(lotInfo.getKeyLOTID());
		List<Object> listSubLot = (List<Object>) subLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);

		for (int i = 0; i < listSubLot.size(); i++) {
			subLotInfo = (SUBLOTDATA) listSubLot.get(i);

			for (Iterator iterator = mapSubLot.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Long, String> iteratorMap = (Map.Entry<Long, String>) iterator.next();
				if (subLotInfo.getKeySUBLOTID().equalsIgnoreCase(iteratorMap.getValue())) {
					subLotInfo.setSTATUS(Constant.LOTSTATE_SCRAPPED);
					subLotInfo.excuteDML(SqlConstant.DML_UPDATE);

					history.addHistory(subLotInfo, txnInfo, SqlConstant.DML_UPDATE);

					break;
				}
			}
		}
	}

	/**
	 * 받아온 lotInfo의 LotID를 가지고 SUBLOTDATA의 LotID 상태를 Released로 설정해 unscrap 처리
	 * SUBLOTDATA에 이력 기록
	 * 
	 * @param lotInfo
	 * @param mapSubLot
	 * @param txnInfo
	 * @return
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void unscrapSubLot(LOTINFORMATION lotInfo, HashMap<Long, String> mapSubLot, TxnInfo txnInfo) {
		// History 기록이 필요한 경우 사용ㅇ
		AddHistory history = new AddHistory();

		SUBLOTDATA subLotInfo = new SUBLOTDATA();
		subLotInfo.setLOTID(lotInfo.getKeyLOTID());
		List<Object> listSubLot = (List<Object>) subLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);

		for (int i = 0; i < listSubLot.size(); i++) {
			subLotInfo = (SUBLOTDATA) listSubLot.get(i);

			for (Iterator iterator = mapSubLot.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Long, String> iteratorMap = (Map.Entry<Long, String>) iterator.next();
				if (subLotInfo.getKeySUBLOTID().equalsIgnoreCase(iteratorMap.getValue())) {
					subLotInfo.setSTATUS(Constant.LOTSTATE_RELEASED);
					subLotInfo.excuteDML(SqlConstant.DML_UPDATE);

					history.addHistory(subLotInfo, txnInfo, SqlConstant.DML_UPDATE);

					break;
				}
			}
		}
	}

	/**
	 * 받아온 ()와 ()를 가지고 lot 생성 시 누락된 값 체크
	 * 
	 * @param
	 * @param
	 * @return Boolean
	 * 
	 */
	public boolean checkAutoLotCreate(String plant, String workorder) {
		boolean result = true;

		// 1. workorder로 제품 찾기(WORKORDER테이블)
		WORKORDER workorderInfo = new WORKORDER();

		workorderInfo.setKeyPLANTID(plant);
		workorderInfo.setKeyWORKORDERID(workorder);

		List<WORKORDER> listWorkorderInfo = (List<WORKORDER>) workorderInfo.excuteDML(SqlConstant.DML_SELECTLIST);

		if (listWorkorderInfo == null || listWorkorderInfo.size() <= 0)
		{
			result = false;
		}

		String productID = listWorkorderInfo.get(0).getPRODUCTID();

		// check 제품정보 - 1
		// SELECT PRODUCTDEFINITION
		DY_AUTOLOTCREATEERROR autoLotCreateError = new DY_AUTOLOTCREATEERROR();
		autoLotCreateError.setKeyPLANTID(plant);
		autoLotCreateError.setKeyWORKORDER(workorder);
		List<DY_AUTOLOTCREATEERROR> listAutoLotCreateError = (List<DY_AUTOLOTCREATEERROR>) autoLotCreateError.excuteDML(SqlConstant.DML_SELECTLIST);

		PRODUCTDEFINITION productDefinition = new PRODUCTDEFINITION();

		productDefinition.setKeyPLANTID(plant);
		productDefinition.setKeyPRODUCTID(productID);

		List<PRODUCTDEFINITION> listProductDefinition = (List<PRODUCTDEFINITION>) productDefinition.excuteDML(SqlConstant.DML_SELECTLIST);
		
		
		if (listProductDefinition == null || listProductDefinition.size() <= 0) 
		{
			checkAutoCreateLotError(listAutoLotCreateError, plant, workorder, "1", "NG");

			result = false;
		}else 
		{
			checkAutoCreateLotError(listAutoLotCreateError, plant, workorder, "1", "OK");
		}

		
		// check 라우팅 정보 확인. - 2
		// SELECT TPPOLICY
		TPPOLICY tppolicyInfo = new TPPOLICY();
		
		tppolicyInfo.setKeyPLANTID(plant);
		tppolicyInfo.setKeyPRODUCTID(productID);
		
		List<TPPOLICY> listTPpolicyInfo = (List<TPPOLICY>) tppolicyInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		if (listTPpolicyInfo == null || listTPpolicyInfo.size() <= 0) 
		{
			checkAutoCreateLotError(listAutoLotCreateError, plant, workorder, "2", "NG");
			
			result = false;
			
		}else 
		{
			checkAutoCreateLotError(listAutoLotCreateError, plant, workorder, "2", "OK");
		}
		
		
		
		//check 라우팅 확정 정보 가져오기.(from routing기준정보.) - 3
		for(int i = 0; i < listTPpolicyInfo.size(); i++) 
		{
			// SELECT MODELINGCONFIRM
			MODELINGCONFIRM modelingConfirm = new MODELINGCONFIRM();
			modelingConfirm.setKeyPLANTID(plant);
			modelingConfirm.setKeyPRODUCTID(productID);
			modelingConfirm.setPROCESSROUTEID(listTPpolicyInfo.get(i).getPROCESSROUTEID());
			modelingConfirm.setCONFIRMFLAG(Constant.FLAG_YESNO_YES);
			List<MODELINGCONFIRM> listModelingConfirm = (List<MODELINGCONFIRM>) modelingConfirm.excuteDML(SqlConstant.DML_SELECTLIST);
			
			if (listModelingConfirm == null || listModelingConfirm.size() <= 0) 
			{
				checkAutoCreateLotError(listAutoLotCreateError, plant, workorder, "3", "NG");
				
				result = false;
				
			}else 
			{
				checkAutoCreateLotError(listAutoLotCreateError, plant, workorder, "3", "OK");
			}
		}
		
		return result;
	}
	
	public void checkAutoCreateLotError(List<DY_AUTOLOTCREATEERROR> listAutoLotCreateError, String plant, String workorder,
										String errorCode, String resultFlag) 
	{
		// 해당 에러코드의 데이터 유무 확인
		boolean checkData = false;
		
		for(int i = 0; i < listAutoLotCreateError.size(); i++)
		{

			// 해당 에러코드의 데이터가 있는지 체크
			if(listAutoLotCreateError.get(i).getKeyERRORCODE().equals(errorCode))
			{
				// 있다면 true
				checkData = true;
				
				// 그 데이터의 RESULTFLAG가 NG 이고, parameter로 보낸 RESULTFLAG가 OK라면 UPDATE
				if(listAutoLotCreateError.get(i).getRESULTFLAG().equals("NG") && resultFlag.equals("OK"))
				{
					listAutoLotCreateError.get(i).setKeyPLANTID(plant);
					listAutoLotCreateError.get(i).setKeyWORKORDER(workorder);
					listAutoLotCreateError.get(i).setKeyERRORCODE(errorCode);
					listAutoLotCreateError.get(i).setRESULTFLAG(resultFlag);
					listAutoLotCreateError.get(i).setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
					listAutoLotCreateError.get(i).setLASTUPDATEUSERID("System");
					
					listAutoLotCreateError.get(i).excuteDML(SqlConstant.DML_UPDATE);
				}
			}
			
		}
		if(!checkData && resultFlag.equals("NG")) 
		{
			DY_AUTOLOTCREATEERROR autoLotCreateError = new DY_AUTOLOTCREATEERROR();
			autoLotCreateError.setKeyPLANTID(plant);
			autoLotCreateError.setKeyWORKORDER(workorder);
			autoLotCreateError.setKeyERRORCODE(errorCode);
			autoLotCreateError.setRESULTFLAG("NG");
			autoLotCreateError.setCREATEUSERID("System");
			autoLotCreateError.setCREATETIME(DateUtil.getCurrentTimestamp());
			
			if(errorCode == "1") 
			{
				autoLotCreateError.setERRORCOMMENT("품번기준정보가 누락되었습니다.");
				autoLotCreateError.setERRORTABLE("PRODUCTDEFINITION");
			}
			else if(errorCode == "2")
			{
				autoLotCreateError.setERRORCOMMENT("라우팅기준정보가 누락되었습니다.");
				autoLotCreateError.setERRORTABLE("TPPOLICY");
			}
			else if(errorCode == "3")
			{
				autoLotCreateError.setERRORCOMMENT("라우팅확정정보가 누락되었습니다.");
				autoLotCreateError.setERRORTABLE("MODELINGCONFIRM");
			}

			autoLotCreateError.excuteDML(SqlConstant.DML_INSERT);
		}
	}
}
