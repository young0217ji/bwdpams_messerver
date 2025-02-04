package mes.lot.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.lot.data.FUTUREACTION;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.LOTPROCESSHISTORY;
import mes.lot.data.REWORKINFORMATION;
import mes.lot.data.SUBLOTDATA;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.lotdata.LotCreateInfo;
import mes.lot.validation.LotValidation;
import mes.master.data.MODELINGCONFIRM;
import mes.master.data.MODELINGCONFIRMDETAIL;
import mes.master.data.PROCESSDEFINITION;
import mes.master.data.PROCESSROUTE;
import mes.master.data.ROCCOMPOSITION;
import mes.master.data.ROCOMPOSITION;
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
public class LotService
{
	private static final transient Logger logger = LoggerFactory.getLogger(LotService.class);
	
	/**
	 * Lot 상태 Released로 subLotData 추가
	 * SUBLOTDATA에 이력 기록
	 * 
	 * @param lotInfo
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void createLot(LOTINFORMATION lotInfo, TxnInfo txnInfo)
	{
		// LotInformation Insert
		lotInfo.excuteDML(SqlConstant.DML_INSERT);
		
		if ( Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(lotInfo.getPRODUCTTYPE()) )
		{
			// History 기록이 필요한 경우 사용
			AddHistory history = new AddHistory();
			NameGenerator nameGenerator = new NameGenerator();
			
			SUBLOTDATA subLotData = new SUBLOTDATA();
			
			// Lot 진행 Type 인 경우 Sub Lot 만들기
			for ( int i = 0; i < ConvertUtil.Object2Int(lotInfo.getCURRENTQUANTITY()); i++ )
			{
				String subLotID = nameGenerator.nameGenerate(lotInfo.getPLANTID(), "SubLotID", new Object[] {lotInfo.getKeyLOTID()});
				
				String[] arrayLocationID = subLotID.split("\\.");
				String locationID = arrayLocationID[arrayLocationID.length-1];
				
				subLotData.setKeySUBLOTID(subLotID);
				subLotData.setLOTID(lotInfo.getKeyLOTID());
				subLotData.setLOCATIONID(ConvertUtil.Object2Long(locationID));
				subLotData.setSTATUS(Constant.LOTSTATE_RELEASED);
				
				subLotData.excuteDML(SqlConstant.DML_INSERT);
				
				history.addHistory(subLotData, txnInfo, SqlConstant.DML_INSERT);
			}
		}
	}
	
	/**
	 * 받아온 데이터로 값 설정 후 createViewLotRow 함수에서 반환된 데이터 추가
	 * 
	 * @param lotInfo
	 * @param createLotInfo
	 * @param modelingConfirm
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void insertViewLotTracking(LOTINFORMATION lotInfo, LotCreateInfo createLotInfo, MODELINGCONFIRM modelingConfirm)
	{
		List<List<Object>> insertViewLotData = new ArrayList<List<Object>>();

		
		MODELINGCONFIRMDETAIL modelingConfirmDetail = new MODELINGCONFIRMDETAIL();
		modelingConfirmDetail.setKeyPLANTID(modelingConfirm.getKeyPLANTID());
		modelingConfirmDetail.setKeyPRODUCTID(modelingConfirm.getKeyPRODUCTID());
		modelingConfirmDetail.setKeyBOMID(modelingConfirm.getKeyBOMID());
		modelingConfirmDetail.setKeyBOMVERSION(modelingConfirm.getKeyBOMVERSION());
		modelingConfirmDetail.setKeyPOLICYNAME(modelingConfirm.getKeyPOLICYNAME());
		modelingConfirmDetail.setKeyPOLICYVALUE(modelingConfirm.getKeyPOLICYVALUE());
		modelingConfirmDetail.setKeyCONDITIONTYPE(modelingConfirm.getKeyCONDITIONTYPE());
		modelingConfirmDetail.setKeyCONDITIONID(modelingConfirm.getKeyCONDITIONID());
		modelingConfirmDetail.setPROCESSROUTEID(lotInfo.getPROCESSROUTEID());
		
		List<Object> listModelingConfirmDetail = (List<Object>) modelingConfirmDetail.excuteDML(SqlConstant.DML_SELECTLIST);
		
		for ( int i = 0; i < listModelingConfirmDetail.size(); i++ )
		{
			modelingConfirmDetail = (MODELINGCONFIRMDETAIL) listModelingConfirmDetail.get(i);
			
			// 공정 Info 구성
			VIEWLOTTRACKING viewLot = new VIEWLOTTRACKING();
			
			// 전체 공통 //
			viewLot.setKeyLOTID( lotInfo.getKeyLOTID() );
			viewLot.setKeyTIMEKEY( DateUtil.getCurrentEventTimeKey() );
			viewLot.setPROCESSROUTEID( modelingConfirmDetail.getPROCESSROUTEID() );
			viewLot.setPROCESSROUTETYPE( modelingConfirmDetail.getPROCESSROUTETYPE() );
			viewLot.setROUTERELATIONTYPE( modelingConfirmDetail.getROUTERELATIONTYPE() );
			viewLot.setROUTERELATIONSEQUENCE( modelingConfirmDetail.getROUTERELATIONSEQUENCE() );
//			viewLot.setEQUIPMENTID( modelingConfirmDetail.getEQUIPMENTID() );
			viewLot.setEQUIPMENTID( createLotInfo.getEQUIPMENTID() );
			viewLot.setINPUTMODE( modelingConfirmDetail.getINPUTMODE() );
			viewLot.setACTIONTYPE( modelingConfirmDetail.getACTIONTYPE() );
			viewLot.setAUTOTRACKINGFLAG( modelingConfirmDetail.getAUTOTRACKINGFLAG() );
			viewLot.setDESCRIPTION( modelingConfirmDetail.getDESCRIPTION() );
			
			viewLot.setCURRENTFLAG( Constant.FLAG_YN_NO );
			viewLot.setREWORKCOUNT( Long.valueOf(0) );
			viewLot.setSTATUS( Constant.OPERATIONSTATE_WAITING );
			
			// 공정 진행 시간 Check 여부
			viewLot.setPROCESSTIMECHECK( Constant.FLAG_YESNO_NO );
			// 전체 공통 //
			
			// 공정 정보 //
			viewLot.setCOMPOSITIONID( modelingConfirmDetail.getCOMPOSITIONID() );
			viewLot.setPROCESSID( modelingConfirmDetail.getPROCESSID() );
			viewLot.setPROCESSNAME( modelingConfirmDetail.getPROCESSNAME() );
			viewLot.setPROCESSSEQUENCE( modelingConfirmDetail.getPROCESSSEQUENCE() );
			viewLot.setPROCESSTYPE( modelingConfirmDetail.getPROCESSTYPE() );
			viewLot.setDETAILPROCESSTYPE( modelingConfirmDetail.getDETAILPROCESSTYPE() );
			viewLot.setAUTOTRACKIN( modelingConfirmDetail.getAUTOTRACKIN() );
			viewLot.setAUTOTRACKOUT( modelingConfirmDetail.getAUTOTRACKOUT() );
			viewLot.setCREATELOTFLAG( modelingConfirmDetail.getCREATELOTFLAG() );
			viewLot.setCREATELOTRULE( modelingConfirmDetail.getCREATELOTRULE() );
			viewLot.setBASICPROCESSID( modelingConfirmDetail.getBASICPROCESSID() );
			viewLot.setPACKINGFLAG( modelingConfirmDetail.getPACKINGFLAG() );
			// 마지막 공정 여부 체크
			viewLot.setENDOFROUTE( modelingConfirmDetail.getENDOFROUTE() );
			// Route 분기
			viewLot.setCHANGEROUTEFLAG( modelingConfirmDetail.getCHANGEROUTEFLAG() );
			// 공정 진행 시간 수집 여부
			viewLot.setPROCESSTIMEFLAG( modelingConfirmDetail.getPROCESSTIMEFLAG() );
			// 현재 공정 혹은 스텝에 대한 자동/수동 진행 지시 구분
			viewLot.setAUTOMODE( modelingConfirmDetail.getAUTOMODE() );
			viewLot.setPROCESSPRINTINDEX( modelingConfirmDetail.getPROCESSPRINTINDEX() );
			// 동시 진행 공정 설정
			viewLot.setCONCURRENCYPROCESSID( modelingConfirmDetail.getCONCURRENCYPROCESSID() );
			viewLot.setCONCURRENCYSEQUENCE( modelingConfirmDetail.getCONCURRENCYSEQUENCE() );
			// ERP공정코드 정보 설정
			viewLot.setERPPROCESSCODE( modelingConfirmDetail.getERPPROCESSCODE() );
			// 공정 정보 //
			
			// 스텝 정보 //
			viewLot.setRECIPEID( modelingConfirmDetail.getRECIPEID() );
			viewLot.setRECIPENAME( modelingConfirmDetail.getRECIPENAME() );
			viewLot.setRECIPESEQUENCE( modelingConfirmDetail.getRECIPESEQUENCE() );
			viewLot.setRECIPETYPE( modelingConfirmDetail.getRECIPETYPE() );
			viewLot.setRECIPERELATIONCODE( modelingConfirmDetail.getRECIPERELATIONCODE() );
			viewLot.setRECIPETYPECODE( modelingConfirmDetail.getRECIPETYPECODE() );
			viewLot.setRECIPEPRINTINDEX( modelingConfirmDetail.getRECIPEPRINTINDEX() );
			viewLot.setCONSUMETYPE( modelingConfirmDetail.getCONSUMETYPE() );
			viewLot.setCONSUMEID( modelingConfirmDetail.getCONSUMEID() );
			// 스텝 정보 //
			
			// 원료 정보 //
			viewLot.setORDERINDEX( modelingConfirmDetail.getORDERINDEX() );
			
			if ( modelingConfirmDetail.getFEEDINGRATE() != null )
			{
				viewLot.setFEEDINGRATE( modelingConfirmDetail.getFEEDINGRATE() );
			}
			else
			{
				viewLot.setFEEDINGRATE(100.0);
			}
			// 원료 정보 //
			
			// 조건 정보 //
			viewLot.setRECIPEPARAMETERID( modelingConfirmDetail.getRECIPEPARAMETERID() );
			viewLot.setRECIPEPARAMETERNAME( modelingConfirmDetail.getRECIPEPARAMETERNAME() );
			
			if ( modelingConfirmDetail.getSTANDARDVALUE() != null && modelingConfirmDetail.getCONSUMABLEVALUE() != null )
			{
				// 기준생산량의 BOM 투입량 비율로 산출하다 보니 그 비율이 낮은 경우 오차가 발생함.
				//Double bomRate = ConvertUtil.doubleDivide( modelingConfirmDetail.getCONSUMABLEVALUE(), ConvertUtil.Object2Double(modelingConfirmDetail.getSTANDARDVALUE()) );
				//Double feedingRate = ConvertUtil.doubleDivide( viewLot.getFEEDINGRATE(), 100.0 );
				//Double dTargetbomRate = ConvertUtil.doubleMultiply(ConvertUtil.doubleMultiply(ConvertUtil.Object2Double(lotInfo.getPRODUCTUNITQUANTITY()), bomRate), feedingRate) ;
				//viewLot.setTARGET(ConvertUtil.Object2String( dTargetbomRate ));
				
				Double prodRate = ConvertUtil.doubleDivide( ConvertUtil.Object2Double(lotInfo.getPRODUCTUNITQUANTITY()), ConvertUtil.Object2Double(modelingConfirmDetail.getSTANDARDVALUE()) );
				Double feedingRate = ConvertUtil.doubleDivide( viewLot.getFEEDINGRATE(), 100.0 );
				Double dTargetprodRate = ConvertUtil.doubleMultiply(ConvertUtil.doubleMultiply(modelingConfirmDetail.getCONSUMABLEVALUE(), prodRate), feedingRate) ;
				viewLot.setTARGET(ConvertUtil.Object2String( dTargetprodRate ));
			}
			else
			{
				viewLot.setTARGET( modelingConfirmDetail.getTARGET() );
			}
			
			
			viewLot.setLOWERSPECLIMIT( modelingConfirmDetail.getLOWERSPECLIMIT() );
			viewLot.setUPPERSPECLIMIT( modelingConfirmDetail.getUPPERSPECLIMIT() );
			viewLot.setLOWERCONTROLLIMIT( modelingConfirmDetail.getLOWERCONTROLLIMIT() );
			viewLot.setUPPERCONTROLLIMIT( modelingConfirmDetail.getUPPERCONTROLLIMIT() );
			viewLot.setLOWERSCREENLIMIT( modelingConfirmDetail.getLOWERSCREENLIMIT() );
			viewLot.setUPPERSCREENLIMIT( modelingConfirmDetail.getUPPERSCREENLIMIT() );
			viewLot.setSPECTYPE( modelingConfirmDetail.getSPECTYPE() );
			viewLot.setUNITOFMEASURE( modelingConfirmDetail.getUNITOFMEASURE() );
			viewLot.setCTPFLAG( modelingConfirmDetail.getCTPFLAG() );
			// 조건 정보 //
			
			insertViewLotData.add(createViewLotRow(viewLot));
		}
		
		createViewLotTracking(insertViewLotData);
		logger.info("ViewLotTracking 정보 구성 완료");
	}
	
	/**
	 * 받아온 viewLotInfo를 row에 추가 후 반환
	 * 
	 * @param viewLotInfo
	 * @return List
	 * 
	 */
	public List<Object> createViewLotRow(VIEWLOTTRACKING viewLotInfo)	
	{
		List<Object> row = new ArrayList<Object>();
		row.add(viewLotInfo.getKeyLOTID());
		row.add(viewLotInfo.getKeyTIMEKEY());
		row.add(viewLotInfo.getPROCESSROUTEID());
		row.add(viewLotInfo.getPROCESSROUTETYPE());
		row.add(viewLotInfo.getROUTERELATIONTYPE());
		row.add(viewLotInfo.getROUTERELATIONSEQUENCE());
		row.add(viewLotInfo.getCOMPOSITIONID());
		row.add(viewLotInfo.getPROCESSID());
		row.add(viewLotInfo.getPROCESSNAME());
		row.add(viewLotInfo.getPROCESSSEQUENCE());
		row.add(viewLotInfo.getRECIPEID());
		row.add(viewLotInfo.getRECIPENAME());
		row.add(viewLotInfo.getRECIPESEQUENCE());
		row.add(viewLotInfo.getRECIPETYPE());
		row.add(viewLotInfo.getRECIPERELATIONCODE());
		row.add(viewLotInfo.getRECIPETYPECODE());
		row.add(viewLotInfo.getRECIPEPARAMETERID());
		row.add(viewLotInfo.getRECIPEPARAMETERNAME());
		row.add(viewLotInfo.getPROCESSTYPE());
		row.add(viewLotInfo.getDETAILPROCESSTYPE());
		row.add(viewLotInfo.getAUTOTRACKIN());
		row.add(viewLotInfo.getAUTOTRACKOUT());
		row.add(viewLotInfo.getCREATELOTFLAG());
		row.add(viewLotInfo.getCREATELOTRULE());
		row.add(viewLotInfo.getBASICPROCESSID());
		row.add(viewLotInfo.getPACKINGFLAG());
		row.add(viewLotInfo.getENDOFROUTE());
		row.add(viewLotInfo.getFIRSTPROCESSFLAG());
		row.add(viewLotInfo.getCHANGEROUTEFLAG());
		row.add(viewLotInfo.getPROCESSTIMEFLAG());
		row.add(viewLotInfo.getAUTOMODE());
		row.add(viewLotInfo.getPROCESSPRINTINDEX());
		row.add(viewLotInfo.getEQUIPMENTID());
		row.add(viewLotInfo.getCONCURRENCYPROCESSID());
		row.add(viewLotInfo.getCONCURRENCYSEQUENCE());
		row.add(viewLotInfo.getCONSUMETYPE());
		row.add(viewLotInfo.getCONSUMEID());
		row.add(viewLotInfo.getRECIPEPRINTINDEX());
		row.add(viewLotInfo.getRELATIONTIMEKEY());
		row.add(viewLotInfo.getRELATIONTIME());
		row.add(viewLotInfo.getINPUTMODE());
		row.add(viewLotInfo.getCURRENTFLAG());
		row.add(viewLotInfo.getACTIONTYPE());
		row.add(viewLotInfo.getPASTMODE());
		row.add(viewLotInfo.getRESULTVALUE());
		row.add(viewLotInfo.getTARGET());
		row.add(viewLotInfo.getLOWERSPECLIMIT());
		row.add(viewLotInfo.getUPPERSPECLIMIT());
		row.add(viewLotInfo.getLOWERCONTROLLIMIT());
		row.add(viewLotInfo.getUPPERCONTROLLIMIT());
		row.add(viewLotInfo.getLOWERSCREENLIMIT());
		row.add(viewLotInfo.getUPPERSCREENLIMIT());
		row.add(viewLotInfo.getSPECTYPE());
		row.add(viewLotInfo.getUNITOFMEASURE());
		row.add(viewLotInfo.getORDERINDEX());
		row.add(viewLotInfo.getCONSUMABLEUSERID());
		row.add(viewLotInfo.getCONSUMABLESTARTTIME());
		row.add(viewLotInfo.getCONSUMABLEENDTIME());
		row.add(viewLotInfo.getGETRESULTTYPE());
		row.add(viewLotInfo.getFEEDINGRATE());
		row.add(viewLotInfo.getADDQUANTITY());
		row.add(viewLotInfo.getALTERNATIVECONSUMABLE());
		row.add(viewLotInfo.getREWORKCOUNT());
		row.add(viewLotInfo.getSTATUS());
		row.add(viewLotInfo.getCTPFLAG());
		row.add(viewLotInfo.getPROCESSTIMECHECK());
		row.add(viewLotInfo.getAUTOTRACKINGFLAG());
		row.add(viewLotInfo.getREASONCODETYPE());
		row.add(viewLotInfo.getREASONCODE());
		row.add(viewLotInfo.getEVENTUSERID());
		row.add(viewLotInfo.getDESCRIPTION());
		row.add(viewLotInfo.getERPPROCESSCODE());
		
		return row;
	}
	
	/**
	 * insertViewLotData를 ViewLotTracking에 생성
	 * 
	 * @param insertViewLotData
	 * @return 
	 * 
	 */
	public void createViewLotTracking(List<List<Object>> insertViewLotData)	
	{
		createViewLotTracking(insertViewLotData, "VIEWLOTTRACKING");
	}
	
	/**
	 * 받아온 insertViewLotData를 tableName에 Insert 처리
	 * 
	 * @param insertViewLotData
	 * @param tableName
	 * @return
	 * 
	 */
	public void createViewLotTracking(List<List<Object>> insertViewLotData, String tableName)	
	{
		String sql = "INSERT INTO " + tableName +
				" ( " +
				"LOTID, " +
				"TIMEKEY, " +
				"PROCESSROUTEID, " +
				"PROCESSROUTETYPE, " +
				"ROUTERELATIONTYPE, " +
				"ROUTERELATIONSEQUENCE, " +
				"COMPOSITIONID, " +
				"PROCESSID, " +
				"PROCESSNAME, " +
				"PROCESSSEQUENCE, " +
				"RECIPEID, " +
				"RECIPENAME, " +
				"RECIPESEQUENCE, " +
				"RECIPETYPE, " +
				"RECIPERELATIONCODE, " +
				"RECIPETYPECODE, " +
				"RECIPEPARAMETERID, " +
				"RECIPEPARAMETERNAME, " +
				"PROCESSTYPE, " +
				"DETAILPROCESSTYPE, " +
				"AUTOTRACKIN, " +
				"AUTOTRACKOUT, " +
				"CREATELOTFLAG, " +
				"CREATELOTRULE, " +
				"BASICPROCESSID, " +
				"PACKINGFLAG, " +
				"ENDOFROUTE, " +
				"FIRSTPROCESSFLAG, " +
				"CHANGEROUTEFLAG, " +
				"PROCESSTIMEFLAG, " +
				"AUTOMODE, " +
				"PROCESSPRINTINDEX, " +
				"EQUIPMENTID, " +
				"CONCURRENCYPROCESSID, " +
				"CONCURRENCYSEQUENCE, " +
				"CONSUMETYPE, " +
				"CONSUMEID, " +
				"RECIPEPRINTINDEX, " +
				"RELATIONTIMEKEY, " +
				"RELATIONTIME, " +
				"INPUTMODE, " +
				"CURRENTFLAG, " +
				"ACTIONTYPE, " +
				"PASTMODE, " +
				"RESULTVALUE, " +
				"TARGET, " +
				"LOWERSPECLIMIT, " +
				"UPPERSPECLIMIT, " +
				"LOWERCONTROLLIMIT, " +
				"UPPERCONTROLLIMIT, " +
				"LOWERSCREENLIMIT, " +
				"UPPERSCREENLIMIT, " +
				"SPECTYPE, " +
				"UNITOFMEASURE, " +
				"ORDERINDEX, " +
				"CONSUMABLEUSERID, " +
				"CONSUMABLESTARTTIME, " +
				"CONSUMABLEENDTIME, " +
				"GETRESULTTYPE, " +
				"FEEDINGRATE, " +
				"ADDQUANTITY, " +
				"ALTERNATIVECONSUMABLE, " +
				"REWORKCOUNT, " +
				"STATUS, " +
				"CTPFLAG, " +
				"PROCESSTIMECHECK, " +
				"AUTOTRACKINGFLAG, " +
				"REASONCODETYPE, " +
				"REASONCODE, " +
				"EVENTUSERID, " +
				"DESCRIPTION, " +
				"ERPPROCESSCODE " +
				") VALUES ( " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ? " +
				") ";
		
		SqlMesTemplate.updateBatchByList(sql, insertViewLotData);
	}
	
	/**
	 * sourceViewLotInfo에서 받아온 Lot 생산수량을 targetViewLotInfo로 이동
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param sourceViewLotInfo
	 * @param targetViewLotInfo
	 * @param qty
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void transferLot(VIEWLOTTRACKING sourceViewLotInfo, VIEWLOTTRACKING targetViewLotInfo, Double qty, TxnInfo txnInfo)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		
		txnInfo.setTxnId(Constant.EVENT_TRANSFER);
		txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
		
		LOTINFORMATION sourceLotInfo = new LOTINFORMATION();
		sourceLotInfo.setKeyLOTID( sourceViewLotInfo.getKeyLOTID() );
		sourceLotInfo = (LOTINFORMATION) sourceLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		
		LOTINFORMATION targetLotInfo = new LOTINFORMATION();
		targetLotInfo.setKeyLOTID( targetViewLotInfo.getKeyLOTID() );
		targetLotInfo = (LOTINFORMATION) targetLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		
		Double sourceOldQty = sourceLotInfo.getCURRENTQUANTITY();
		Double targetOldQty = targetLotInfo.getCURRENTQUANTITY();
		
		
		validation.validationNumber("ConsumeQuantity", null, sourceOldQty, String.valueOf(qty));
		
		
		// 적가 합산 물량
		targetLotInfo.setCURRENTQUANTITY( ConvertUtil.doubleAdd(targetOldQty, qty) );
		targetLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		
		validation.validationNumber("TotalQuantity", 0, null, String.valueOf(targetLotInfo.getCURRENTQUANTITY()));
		

		LOTPROCESSHISTORY targetLotHistory = new LOTPROCESSHISTORY();
		targetLotHistory.setOLDCURRENTQUANTITY( targetOldQty );
		targetLotHistory.setSOURCELOTID( sourceLotInfo.getKeyLOTID() );
		targetLotHistory.setDESTINATIONLOTID( targetLotInfo.getKeyLOTID() );
		addHistory.addLotProcessHistory(targetLotHistory, targetLotInfo, targetViewLotInfo, txnInfo);
		
		
		// Source Lot Purge
		sourceLotInfo.setCURRENTQUANTITY( ConvertUtil.doubleSubtract(sourceOldQty, qty) );
		if ( sourceLotInfo.getCURRENTQUANTITY().compareTo(0.0) == 0 )
		{
			sourceLotInfo.setLOTSTATE(Constant.LOTSTATE_EMPTIED);
		}
		sourceLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		LOTPROCESSHISTORY sourceLotHistory = new LOTPROCESSHISTORY();
		sourceLotHistory.setOLDCURRENTQUANTITY( sourceOldQty );
		sourceLotHistory.setSOURCELOTID( sourceLotInfo.getKeyLOTID() );
		sourceLotHistory.setDESTINATIONLOTID( targetLotInfo.getKeyLOTID() );
		addHistory.addLotProcessHistory(sourceLotHistory, sourceLotInfo, sourceViewLotInfo, txnInfo);
	}
	
	/**
	 * 받아온 LOTINFORMATION의 LotID 삳태를 Terminated 처리
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param viewLotInfo
	 * @param lotInfo
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void terminateLot(VIEWLOTTRACKING viewLotInfo, LOTINFORMATION lotInfo, TxnInfo txnInfo)
	{
		LotHistory addHistory = new LotHistory();
		txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
		
		lotInfo.setLOTSTATE(Constant.LOTSTATE_TERMINATED);
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		LOTPROCESSHISTORY lotHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotHistory, lotInfo, viewLotInfo, txnInfo);
	}
	
	/**
	 * 공정진행 시 순차 진행 여부 반환
	 * 
	 * @param department
	 * @return String
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getOrderRequired(String department) 
	{
		String sql = "SELECT goinorderrequired FROM area WHERE areaID = ? ";
		List<LinkedCaseInsensitiveMap> listAreaOrder = SqlMesTemplate.queryForList(sql, new Object[] {department});
		LinkedCaseInsensitiveMap mapAreaOrder = listAreaOrder.get(0);
		
		String areaOrderRequired = ConvertUtil.Object2String(mapAreaOrder.get("goinorderrequired"));
		
		return areaOrderRequired;
	}
	
	/**
	 * 받아온 lotInfo의 상태를 Completed로 변경해 완료 처리
	 * LOTPROCESSHISTORY에 이력기록
	 * 
	 * @param lotInfo
	 * @param viewLotInfo
	 * @param txnInfo
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void makeLotCompleted(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
		// 미진행 공정 여부 체크
//		VIEWLOTTRACKING checkViewLot = new VIEWLOTTRACKING();
//		checkViewLot.setKeyWORKORDER(viewLotInfo.getKeyWORKORDER());
//		checkViewLot.setKeyLOTID(viewLotInfo.getKeyLOTID());
//		checkViewLot.setPROCESSROUTEID(viewLotInfo.getPROCESSROUTEID());
//		checkViewLot.setINPUTMODE(viewLotInfo.getINPUTMODE());
//		checkViewLot.setSTATUS(Constant.OPERATIONSTATE_WAITING);
//		List<Object> listCheckViewLot = (List<Object>) checkViewLot.excuteDML(SqlConstant.DML_SELECTLIST);
//		if ( listCheckViewLot.size() > 0 )
//		{
//			checkViewLot = (VIEWLOTTRACKING) listCheckViewLot.get(0);
//			
//			// 다음 작업지시번호({0})는 ({1})공정이 종료되지 않아 생산작업을 종료할 수 없습니다.
//			throw new CustomException("OM-105", new Object[] {checkViewLot.getKeyWORKORDER(), checkViewLot.getPROCESSNAME()});
//		}
		
		
		LotHistory addHistory = new LotHistory();
		
		TxnInfo sTxnInfo = new TxnInfo();
		sTxnInfo.setTxnId(Constant.EVENT_MAKELOTCOMPLETED);
		sTxnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
		sTxnInfo.setEventTime(txnInfo.getEventTime());
		sTxnInfo.setTxnUser(txnInfo.getTxnUser());
		sTxnInfo.setTxnComment(txnInfo.getTxnComment());
		
		
		lotInfo.setCOMPLETETIME( txnInfo.getEventTime() );
		lotInfo.setCOMPLETEUSERID( txnInfo.getTxnUser() );
		lotInfo.setLOTSTATE( Constant.LOTSTATE_COMPLETED );

		lotInfo.setLASTJUDGECODE("OK");
		lotInfo.setSHIPSTATE(Constant.SHIPPINGSTATE_SHIPPING);
		lotInfo.setSHIPPINGUSERID(txnInfo.getTxnUser());
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// LotProcessHistory
		LOTPROCESSHISTORY lotProcessHisotry = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHisotry, lotInfo, viewLotInfo, sTxnInfo);
		
		// Sub Lot 에 대한 상태 변경
		if ( Constant.PRODUCTTYPE_LOT.equalsIgnoreCase(lotInfo.getPRODUCTTYPE()) )
		{
			// History 기록이 필요한 경우 사용
			AddHistory history = new AddHistory();
						
			SUBLOTDATA subLotData = new SUBLOTDATA();
			subLotData.setLOTID(lotInfo.getKeyLOTID());
			List<Object> listSubLotData = (List<Object>) subLotData.excuteDML(SqlConstant.DML_SELECTLIST);
			
			for ( int i = 0; i < listSubLotData.size(); i++ )
			{
				subLotData = (SUBLOTDATA) listSubLotData.get(i);
				subLotData.setSTATUS(Constant.LOTSTATE_COMPLETED);
				
				subLotData.excuteDML(SqlConstant.DML_UPDATE);
				
				history.addHistory(subLotData, sTxnInfo, SqlConstant.DML_UPDATE);
			}
		}
	}
	
	/**
	 * 받아온 lotInfo의 상태를 Released로 변경해 완료 취소 처리
	 * LOTPROCESSHISTORY에 이력기록
	 * 
	 * @param lotInfo
	 * @param viewLotInfo
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void makeLotCompleteCancel(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
		LotHistory addHistory = new LotHistory();
		
		TxnInfo sTxnInfo = new TxnInfo();
		sTxnInfo.setTxnId(Constant.EVENT_MAKELOTCOMPLETECANCEL);
		sTxnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
		sTxnInfo.setEventTime(txnInfo.getEventTime());
		sTxnInfo.setTxnUser(txnInfo.getTxnUser());
		sTxnInfo.setTxnComment(txnInfo.getTxnComment());
		
		
		lotInfo.setCOMPLETETIME( null );
		lotInfo.setCOMPLETEUSERID( txnInfo.getTxnUser() );
		lotInfo.setLOTSTATE( Constant.LOTSTATE_RELEASED );

		lotInfo.setLASTJUDGECODE("");
		lotInfo.setSHIPSTATE(Constant.SHIPPINGSTATE_WAIT);
		lotInfo.setSHIPPINGUSERID("");
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// LotProcessHistory
		LOTPROCESSHISTORY lotProcessHisotry = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHisotry, lotInfo, viewLotInfo, sTxnInfo);
	}
	
	/**
	 * 재작업 중인 Lot 처리
	 * LOTPROCESSHISTORY에 이력기록
	 * 
	 * @param lotInfo
	 * @param reworkInfo
	 * @param txnInfo
	 * @return 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void makeLotInRework(LOTINFORMATION lotInfo, REWORKINFORMATION reworkInfo, TxnInfo txnInfo)
	{
		LotHistory addHistory = new LotHistory();
		
		// ViewLotTracking Insert
		List<List<Object>> insertViewLotData = new ArrayList<List<Object>>();
		

		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID(lotInfo.getKeyLOTID());
		
		
		// ProcessRouteID 변경 여부 체크
		if ( lotInfo.getPROCESSROUTEID().equalsIgnoreCase(reworkInfo.getKeyPROCESSROUTEID()) )
		{
			// ProcessRouteID 가 같은 경우
			lotInfo.setPROCESSROUTETYPE(Constant.PROCESSROUTETYPE_REWORK);

			
			// 공정 진행 구성
			viewLotInfo.setPROCESSROUTEID(lotInfo.getPROCESSROUTEID());
			String suffix = " AND processRouteType != '" + Constant.PROCESSROUTETYPE_REWORK + "'" +
					" AND processSequence >= " + String.valueOf(reworkInfo.getSTARTPROCESSSEQUENCE()) +
					" AND processSequence <= " + String.valueOf(reworkInfo.getENDPROCESSSEQUENCE()) +
					" ORDER BY routerelationSequence, processPrintIndex, recipePrintIndex, orderIndex, timeKey ";
			
			List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				viewLotInfo.setKeyTIMEKEY( DateUtil.getCurrentEventTimeKey() );
				viewLotInfo.setPROCESSROUTETYPE(Constant.PROCESSROUTETYPE_REWORK);
				viewLotInfo.setROUTERELATIONTYPE(Constant.PROCESSROUTETYPE_REWORK);
				viewLotInfo.setROUTERELATIONSEQUENCE(reworkInfo.getKeyRELATIONSEQUENCE());
				viewLotInfo.setRELATIONTIMEKEY( "" );
				viewLotInfo.setRELATIONTIME( null );
				if ( i == 0 )
				{
					viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				}
				else
				{
					viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
				}
				viewLotInfo.setSTATUS( Constant.RECIPESTATE_WAITING );

				insertViewLotData.add(createViewLotRow(viewLotInfo));
			}
			
			viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(0);
		}
		else
		{
			ProcessDefinitionService processService = new ProcessDefinitionService();
			
			// ProcessRouteID 가 다른 경우
			lotInfo.setPROCESSROUTETYPE(Constant.PROCESSROUTETYPE_REWORK);
			lotInfo.setPROCESSROUTEID(reworkInfo.getKeyPROCESSROUTEID());
			
			
			HashMap<Long, ROCOMPOSITION> listROComposition = getROCompositionList( lotInfo.getPLANTID(), lotInfo.getPROCESSROUTEID() );

			double operationIndex = 1.0;
			for ( Iterator iterator = new TreeMap(listROComposition).entrySet().iterator(); iterator.hasNext(); )
			{
				Map.Entry<Long, ROCOMPOSITION> iteratorMap = (Map.Entry<Long, ROCOMPOSITION>) iterator.next();
				ROCOMPOSITION roComposition = iteratorMap.getValue();
				
				// 공정 시작 Info 구성
				VIEWLOTTRACKING viewLot = new VIEWLOTTRACKING();
				
				// 전체 공통 //
				viewLot.setKeyLOTID( lotInfo.getKeyLOTID() );
				viewLot.setKeyTIMEKEY( DateUtil.getCurrentEventTimeKey() );
				viewLot.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
				viewLot.setPROCESSROUTETYPE( Constant.PROCESSROUTETYPE_REWORK );
				viewLot.setROUTERELATIONTYPE( Constant.PROCESSROUTETYPE_REWORK );
				viewLot.setROUTERELATIONSEQUENCE( reworkInfo.getKeyRELATIONSEQUENCE() );
				
				if ( Double.compare(operationIndex, 1.0) == 0 )
				{
					viewLot.setCURRENTFLAG( Constant.FLAG_YN_YES );
				}
				else
				{
					viewLot.setCURRENTFLAG( Constant.FLAG_YN_NO );
				}
				
				viewLot.setREWORKCOUNT( Long.valueOf(0) );
				viewLot.setSTATUS( Constant.OPERATIONSTATE_WAITING );
				// 현재 공정 혹은 스텝에 대한 자동/수동 진행 지시 구분
				viewLot.setAUTOMODE( Constant.CONTROL_MODE_MANUAL );
				viewLot.setEQUIPMENTID( reworkInfo.getREWORKEQUIPMENTID() );
				// 전체 공통 //
				
				// Process Operation Spec Information
				PROCESSDEFINITION processDefinition = processService.getProcessDefinition(lotInfo.getPLANTID(), roComposition.getKeyPROCESSID());
				
				// 공정 정보 //
				viewLot.setCOMPOSITIONID( roComposition.getROCOMPOSITIONID() );
				viewLot.setPROCESSID( roComposition.getKeyPROCESSID() );
				viewLot.setPROCESSNAME( roComposition.getPROCESSNAME() );
				viewLot.setPROCESSSEQUENCE( roComposition.getKeyPROCESSSEQUENCE() );
				viewLot.setPROCESSTYPE( processDefinition.getPROCESSTYPE() );
				viewLot.setDETAILPROCESSTYPE( processDefinition.getDETAILPROCESSTYPE() );
				viewLot.setAUTOTRACKIN( roComposition.getAUTOTRACKIN() );
				viewLot.setAUTOTRACKOUT( roComposition.getAUTOTRACKOUT() );
				viewLot.setCREATELOTFLAG( roComposition.getCREATELOTFLAG() );
				viewLot.setCREATELOTRULE( roComposition.getCREATELOTRULE() );
				viewLot.setBASICPROCESSID( processDefinition.getBASICPROCESSID() );
				viewLot.setPACKINGFLAG( processDefinition.getPACKINGFLAG() );
				// 마지막 공정 여부 체크
				viewLot.setENDOFROUTE( roComposition.getENDOFROUTE() );
				// Route 분기
				viewLot.setCHANGEROUTEFLAG( Constant.FLAG_YESNO_NO );
				// 공정 진행 시간 수집 여부
				viewLot.setPROCESSTIMEFLAG( Constant.FLAG_YESNO_NO );
				viewLot.setPROCESSPRINTINDEX( operationIndex );
				viewLot.setRECIPEPRINTINDEX( Long.valueOf(0) );
				// 동시 진행 공정 설정
				viewLot.setCONCURRENCYPROCESSID( roComposition.getCONCURRENCYPROCESSID() );
				viewLot.setCONCURRENCYSEQUENCE( roComposition.getCONCURRENCYSEQUENCE() );
				viewLot.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
				viewLot.setACTIONTYPE( Constant.EVENT_START );
				viewLot.setORDERINDEX( null );
				// ERP공정코드 정보 설정
				viewLot.setERPPROCESSCODE( processDefinition.getERPPROCESSCODE() );
				// 공정 정보 //
				
				// 공정 시작 Insert
				insertViewLotData.add(createViewLotRow(viewLot));
				
				
				// Step Information List
				HashMap<Long, ROCCOMPOSITION> listROCComposition = getROCCompositionList( lotInfo.getPLANTID(), roComposition );

				if ( listROCComposition != null )
				{
					long recipeIndex = 1;
					for ( Iterator iteratorRecipe = new TreeMap(listROCComposition).entrySet().iterator(); iteratorRecipe.hasNext(); )
					{
						Map.Entry<Long, ROCCOMPOSITION> iteratorRecipeMap = (Map.Entry<Long, ROCCOMPOSITION>) iteratorRecipe.next();
						ROCCOMPOSITION rocComposition = iteratorRecipeMap.getValue();

						viewLot.setKeyTIMEKEY( DateUtil.getCurrentEventTimeKey() );
						viewLot.setCOMPOSITIONID( rocComposition.getROCCOMPOSITIONID() );
						
						// 스텝 정보 //
						viewLot.setRECIPEID( rocComposition.getKeyRECIPEID() );
						viewLot.setRECIPEPARAMETERID( "" );
						viewLot.setRECIPEPARAMETERNAME( "" );
						
						viewLot.setRECIPENAME( rocComposition.getRECIPENAME() );
						viewLot.setRECIPESEQUENCE( rocComposition.getKeyRECIPESEQUENCE() );
						viewLot.setRECIPETYPE( rocComposition.getKeyRECIPETYPE() );
						viewLot.setRECIPERELATIONCODE( rocComposition.getKeyRECIPERELATIONCODE() );
						viewLot.setRECIPETYPECODE( rocComposition.getKeyRECIPETYPECODE() );
						viewLot.setPROCESSPRINTINDEX( operationIndex );
						viewLot.setRECIPEPRINTINDEX( recipeIndex );
						viewLot.setCONCURRENCYPROCESSID( rocComposition.getCONCURRENCYPROCESSID() );
						viewLot.setCONCURRENCYSEQUENCE( rocComposition.getCONCURRENCYSEQUENCE() );
						viewLot.setCONSUMETYPE( rocComposition.getCONSUMETYPE() );
						viewLot.setCONSUMEID( rocComposition.getCONSUMEID() );
						viewLot.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
						viewLot.setCURRENTFLAG( Constant.FLAG_YN_NO );
						viewLot.setACTIONTYPE( rocComposition.getKeyRECIPETYPE() );
						// 스텝 정보 //
						
						viewLot.setTARGET( "" );
						viewLot.setLOWERSPECLIMIT( "" );
						viewLot.setUPPERSPECLIMIT( "" );
						viewLot.setLOWERCONTROLLIMIT( "" );
						viewLot.setUPPERCONTROLLIMIT( "" );
						viewLot.setLOWERSCREENLIMIT( "" );
						viewLot.setUPPERSCREENLIMIT( "" );
						viewLot.setSPECTYPE( "" );
						viewLot.setUNITOFMEASURE( "" );
						viewLot.setCTPFLAG( "" );
						viewLot.setREWORKCOUNT( Long.valueOf(0) );
						
						// 원료 정보 //
						viewLot.setORDERINDEX( null );
						viewLot.setGETRESULTTYPE( "" );
						viewLot.setFEEDINGRATE( null );
						// 원료 정보 //
						
						// Step 이벤트 Insert
						insertViewLotData.add(createViewLotRow(viewLot));
						
						recipeIndex++;
					}
				}
				
				
				// 공정 종료 Info 구성
				// 1-2) 대표설비로 설비 Online 상태 확인하기
				viewLot.setKeyTIMEKEY( DateUtil.getCurrentEventTimeKey() );
				viewLot.setCOMPOSITIONID( roComposition.getROCOMPOSITIONID() );
				viewLot.setRECIPEPARAMETERID( "" );
				viewLot.setRECIPEPARAMETERNAME( "" );
				viewLot.setRECIPEID( "" );
				viewLot.setRECIPENAME( "" );
				viewLot.setRECIPESEQUENCE( null );
				viewLot.setRECIPETYPE( "" );
				viewLot.setRECIPERELATIONCODE( "" );
				viewLot.setRECIPETYPECODE( "" );
				viewLot.setPROCESSPRINTINDEX( operationIndex );
				viewLot.setRECIPEPRINTINDEX( Long.valueOf(999) );
				viewLot.setCONCURRENCYPROCESSID( "" );
				viewLot.setCONCURRENCYSEQUENCE( null );
				viewLot.setCONSUMETYPE( "" );
				viewLot.setCONSUMEID( "" );
				viewLot.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
				viewLot.setCURRENTFLAG( Constant.FLAG_YN_NO );
				viewLot.setACTIONTYPE( Constant.EVENT_END );
				
				viewLot.setTARGET( "" );
				viewLot.setLOWERSPECLIMIT( "" );
				viewLot.setUPPERSPECLIMIT( "" );
				viewLot.setLOWERCONTROLLIMIT( "" );
				viewLot.setUPPERCONTROLLIMIT( "" );
				viewLot.setLOWERSCREENLIMIT( "" );
				viewLot.setUPPERSCREENLIMIT( "" );
				viewLot.setSPECTYPE( "" );
				viewLot.setUNITOFMEASURE( "" );
				viewLot.setCTPFLAG( "" );
				viewLot.setREWORKCOUNT( Long.valueOf(0) );
				viewLot.setSTATUS( Constant.OPERATIONSTATE_WAITING );
				
				viewLot.setORDERINDEX( Long.valueOf(999) );
				viewLot.setGETRESULTTYPE( "" );
				viewLot.setFEEDINGRATE( null );

				// 공정 종료 Insert
				insertViewLotData.add(createViewLotRow(viewLot));
				
				operationIndex = ConvertUtil.doubleAdd(operationIndex, 1.0);
			}
			logger.info("ViewLotTracking 정보 구성 완료");
		}
		
		createViewLotTracking(insertViewLotData);
		
		
		lotInfo.setREWORKSTATE(Constant.LOTREWORKSTATE_INREWORK);
		lotInfo.setREWORKCOUNT( lotInfo.getREWORKCOUNT() + 1 );
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		
		// LotProcessHistory
		LOTPROCESSHISTORY lotProcessHisotry = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHisotry, lotInfo, viewLotInfo, txnInfo);
	}
	
	/**
	 * 재작업 하지 않는 Lot 처리
	 * LOTPROCESSHISTORY에 이력기록
	 * 
	 * @param lotInfo
	 * @param reworkInfo
	 * @param txnInfo
	 * @return 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void makeLotNotInRework(LOTINFORMATION lotInfo, REWORKINFORMATION reworkInfo, TxnInfo txnInfo)
	{
		VIEWLOTTRACKING currentViewLotInfo = new VIEWLOTTRACKING();
		currentViewLotInfo.setKeyLOTID(lotInfo.getKeyLOTID());
		currentViewLotInfo.setPROCESSROUTEID(reworkInfo.getKeyPROCESSROUTEID());
		currentViewLotInfo.setPROCESSROUTETYPE(Constant.PROCESSROUTETYPE_REWORK);
		currentViewLotInfo.setROUTERELATIONTYPE(Constant.PROCESSROUTETYPE_REWORK);
		currentViewLotInfo.setROUTERELATIONSEQUENCE(reworkInfo.getKeyRELATIONSEQUENCE());
		currentViewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_YES);
		List<Object> listCurrentViewLotInfo = (List<Object>) currentViewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		for ( int i = 0; i < listCurrentViewLotInfo.size(); i++ )
		{
			currentViewLotInfo = (VIEWLOTTRACKING) listCurrentViewLotInfo.get(i);
			currentViewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_NO);
			currentViewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
		
		
		LotHistory addHistory = new LotHistory();

		// 미완료 재진행 정보 여부 체크
		REWORKINFORMATION checkReworkCount = new REWORKINFORMATION();
		checkReworkCount.setKeyLOTID(lotInfo.getKeyLOTID());
		checkReworkCount.setREWORKSTATE(Constant.REWORKSTATE_PROCESSING);
		
		String suffixRework = "  ORDER BY relationSequence DESC ";
		List<Object> listCheckReworkCount = (List<Object>) checkReworkCount.excuteDML(SqlConstant.DML_SELECTLIST, suffixRework);
		
		
		lotInfo.setPROCESSROUTEID(reworkInfo.getRETURNPROCESSROUTEID());
		if ( listCheckReworkCount.size() > 0 )
		{
			lotInfo.setREWORKSTATE(Constant.LOTREWORKSTATE_INREWORK);
		}
		else
		{
			ProcessRouteService processRouteService = new ProcessRouteService();
			PROCESSROUTE routeInfo = processRouteService.getProcessRouteInfo(lotInfo.getPLANTID(), lotInfo.getPROCESSROUTEID());
			
			lotInfo.setPROCESSROUTETYPE(routeInfo.getPROCESSROUTETYPE());
			lotInfo.setREWORKSTATE(Constant.LOTREWORKSTATE_NOTINREWORK);
		}
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID(lotInfo.getKeyLOTID());
		viewLotInfo.setPROCESSROUTEID(reworkInfo.getRETURNPROCESSROUTEID());
		if ( listCheckReworkCount.size() > 0 )
		{
			viewLotInfo.setPROCESSROUTETYPE(Constant.PROCESSROUTETYPE_REWORK);
			viewLotInfo.setROUTERELATIONTYPE(Constant.PROCESSROUTETYPE_REWORK);
			
			checkReworkCount = (REWORKINFORMATION) listCheckReworkCount.get(0);
			viewLotInfo.setROUTERELATIONSEQUENCE(checkReworkCount.getKeyRELATIONSEQUENCE());
		}
		else
		{
			viewLotInfo.setROUTERELATIONTYPE(null);
			viewLotInfo.setROUTERELATIONSEQUENCE(null);
		}
		
		// 반환공정이 지정된 경우
		if ( reworkInfo.getRETURNPROCESSID() != null && reworkInfo.getRETURNPROCESSSEQUENCE() != null )
		{
			viewLotInfo.setPROCESSID(reworkInfo.getRETURNPROCESSID());
			viewLotInfo.setPROCESSSEQUENCE(reworkInfo.getRETURNPROCESSSEQUENCE());
			viewLotInfo.setINPUTMODE(Constant.OBJECTTYPE_OPERATION);
			viewLotInfo.setACTIONTYPE(Constant.EVENT_START);
			viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
			
			if ( Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
			{
				if ( Constant.FLAG_YN_NO.equalsIgnoreCase(viewLotInfo.getCURRENTFLAG()) )
				{
					viewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_YES);
					viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
				}
			}
		}
		else
		{
			viewLotInfo.setCURRENTFLAG(Constant.FLAG_YN_YES);
			
			String suffix = "  ORDER BY routerelationSequence, processPrintIndex, recipePrintIndex, orderIndex, timeKey ";
			List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
			
			viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(0);
		}
		
		
		// LotProcessHistory
		LOTPROCESSHISTORY lotProcessHisotry = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHisotry, lotInfo, viewLotInfo, txnInfo);
	}
	
	/**
	 * Routing, 공정 구성을 관리하는 리스트 반환
	 * 
	 * @param plantID
	 * @param processRouteID
	 * @return HashMap
	 * @throws CustomException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public HashMap<Long, ROCOMPOSITION> getROCompositionList(String plantID, String processRouteID)
	{
		HashMap<Long, ROCOMPOSITION> listROComposition = new HashMap<Long, ROCOMPOSITION>();
		
		ROCOMPOSITION roComposition = new ROCOMPOSITION();
		roComposition.setKeyPLANTID(plantID);
		roComposition.setKeyPROCESSROUTEID( processRouteID );

		List<Object> listComposition = (List<Object>) roComposition.excuteDML(SqlConstant.DML_SELECTLIST);

		if ( listComposition.size() > 0 )
		{
			for ( int i = 0; i < listComposition.size(); i++ )
			{
				ROCOMPOSITION roCompositionElement = (ROCOMPOSITION) listComposition.get(i);

				listROComposition.put(roCompositionElement.getKeyPROCESSSEQUENCE(), roCompositionElement);
			}

			return listROComposition;
		}
		else
		{
			// 필요한 기준정보가 존재하지 않아 진행이 불가능합니다. (기준정보테이블 : {0})
			throw new CustomException("CHECK-006", new Object[] {"ROComposition"});
		}
	}
	
	/**
	 * Routing, 공정, Step 구성을 관리하는 리스트 반환
	 * 
	 * @param plantID
	 * @param roCompositionElement
	 * @return HashMap
	 * 
	 */
	@SuppressWarnings("unchecked")
	public HashMap<Long, ROCCOMPOSITION> getROCCompositionList(String plantID, ROCOMPOSITION roCompositionElement)
	{
		HashMap<Long, ROCCOMPOSITION> listROCComposition = new HashMap<Long, ROCCOMPOSITION>();
		
		ROCCOMPOSITION rocComposition = new ROCCOMPOSITION();
		rocComposition.setKeyPLANTID(plantID);
		rocComposition.setKeyPROCESSROUTEID( roCompositionElement.getKeyPROCESSROUTEID() );
		rocComposition.setKeyPROCESSID( roCompositionElement.getKeyPROCESSID() );
		rocComposition.setKeyPROCESSSEQUENCE( roCompositionElement.getKeyPROCESSSEQUENCE() );

		List<Object> listComposition = (List<Object>) rocComposition.excuteDML(SqlConstant.DML_SELECTLIST);

		if ( listComposition.size() > 0 )
		{
			for ( int i = 0; i < listComposition.size(); i++ )
			{
				ROCCOMPOSITION rocCompositionElement = (ROCCOMPOSITION) listComposition.get(i);

				listROCComposition.put(rocCompositionElement.getKeyRECIPESEQUENCE(), rocCompositionElement);
			}

			return listROCComposition;
		}
		else
		{
			// 없어도 오류 없음 공정만 존재하는 경우!
			return null;
		}
	}
	
	/**
	 * 받아온 lotInfo의 삳태를 Hold 처리
	 * LotHistory에 이력기록
	 * 
	 * @param lotInfo
	 * @param txnInfo
	 * @return 
	 */
	public void makeLotHold(LOTINFORMATION lotInfo, TxnInfo txnInfo)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		
		validation.validationLotState(lotInfo.getKeyLOTID(), lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_CREATED, Constant.LOTSTATE_RELEASED, Constant.LOTSTATE_EMPTIED});
		validation.validationLotState(lotInfo.getKeyLOTID(), lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_NOTONHOLD});

		lotInfo.setHOLDSTATE( Constant.LOTHOLDSTATE_ONHOLD );
		lotInfo.setDESCRIPTION( "(" + txnInfo.getTxnReasonCode() + ") " + txnInfo.getTxnComment() );
		
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
	}
	
	/**
	 * 받아온 lotInfo의 삳태를 Scrap 처리
	 * LotHistory에 이력기록
	 * 
	 * @param lotInfo
	 * @param txnInfo
	 * @return 
	 */
	public void makeLotScrap(LOTINFORMATION lotInfo, TxnInfo txnInfo)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		
		validation.validationLotState(lotInfo.getKeyLOTID(), lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_CREATED, Constant.LOTSTATE_RELEASED, Constant.LOTSTATE_COMPLETED, Constant.LOTSTATE_EMPTIED});

		lotInfo.setLOTSTATE( Constant.LOTSTATE_SCRAPPED );
		lotInfo.setDESCRIPTION( "(" + txnInfo.getTxnReasonCode() + ") " + txnInfo.getTxnComment() );
		
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
	}
	
	/**
	 * 받아온 lotID로 예약 작업 리스트 사이즈 반환
	 * 
	 * @param lotID
	 * @param timeKey
	 * @return int
	 * 
	 */
	@SuppressWarnings("unchecked")
	public int getFutureActionList(String lotID, String timeKey)
	{
		FUTUREACTION futureAction = new FUTUREACTION();
		futureAction.setKeyLOTID(lotID);
		futureAction.setKeyRELATIONTIMEKEY(timeKey);
		futureAction.setACTIONSTATE(Constant.ACTION_STATE_RESERVE);
		
		List<Object> listFutureAction = (List<Object>) futureAction.excuteDML(SqlConstant.DML_SELECTLIST);
		
		return listFutureAction.size();
	}
	
	/**
	 * 받아온 데이터를 FUTUREACTION에 갑 설정 후 FutureActionFlag No로 예약작업 해제 처리
	 * 
	 * @param lotInfo
	 * @param timeKey
	 * @param sequence
	 * @param type
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void releaseFutureAction(LOTINFORMATION lotInfo, String timeKey, String sequence, String type, TxnInfo txnInfo)
	{
		FUTUREACTION futureAction = new FUTUREACTION();
		futureAction.setKeyLOTID(lotInfo.getKeyLOTID());
		futureAction.setKeyRELATIONTIMEKEY(timeKey);
		futureAction.setKeySEQUENCE(sequence);
		futureAction = (FUTUREACTION) futureAction.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		futureAction.setACTIONSTATE(type);
		futureAction.setCHANGEUSERID(txnInfo.getTxnUser());
		futureAction.setCHANGETIME(txnInfo.getEventTime());
		futureAction.excuteDML(SqlConstant.DML_UPDATE);
		
		if ( getFutureActionList(lotInfo.getKeyLOTID(), timeKey) < 1 )
		{
			VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
			viewLotInfo.setKeyLOTID(futureAction.getKeyLOTID());
			viewLotInfo.setKeyTIMEKEY(futureAction.getKeyRELATIONTIMEKEY());
			viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);

			viewLotInfo.setFUTUREACTIONFLAG(Constant.FLAG_YESNO_NO);
			viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
	}
}
