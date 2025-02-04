package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.lot.custom.LotCustomService;
import mes.lot.custom.ProcessCustomService;
import mes.lot.data.FUTUREACTION;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.LOTPROCESSHISTORY;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.lotdata.LotCreateInfo;
import mes.lot.transaction.LotHistory;
import mes.lot.transaction.LotService;
import mes.lot.transaction.LotTrackingService;
import mes.lot.validation.LotValidation;
import mes.master.data.AREA;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnCreateLot implements ObjectExecuteService
{
	/**
	 * 생산을 진행할 Lot을 생성하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
    public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		NameGenerator nameGenerator = new NameGenerator();
		LotCustomService lotCustomService = new LotCustomService();
		LotValidation validation = new LotValidation();
		
		validation.checkListNull(dataList);
		
		int iCreateLotCount = ConvertUtil.Object2Int4Zero(MessageParse.getParam(recvDoc, "CREATELOTCOUNT"));
		String sStartProcessID = MessageParse.getParam(recvDoc, "STARTPROCESSID");
		String sStartProcessSeq = MessageParse.getParam(recvDoc, "STARTPROCESSSEQUENCE");
		boolean bChangeProcessFlag = false;
		if ( sStartProcessSeq != null && sStartProcessSeq.length() > 0 ) {
			bChangeProcessFlag = true;
		}
		

		// Lot 생성 로직 개선
		HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(0);

		validation.checkKeyNull( dataMap, new Object[] {"PLANTID", "PRODUCTID", "WORKORDER"} );
		
		String plantID 				= dataMap.get("PLANTID");
		String workOrder 			= dataMap.get("WORKORDER");
		String productID 			= dataMap.get("PRODUCTID");
		String department 			= dataMap.get("WORKCENTER");
		String areaID				= dataMap.get("WORKCENTER");
		String equipmentID			= dataMap.get("EQUIPMENTID");
		Double startQuantity		= ConvertUtil.Object2Double(dataMap.get("STARTQUANTITY"), 0.0);
		
		// ProcessRote 결정 정보
		String bomID				= dataMap.get("BOMID");
		String bomVersion			= dataMap.get("BOMVERSION");
		String policyName			= dataMap.get("POLICYNAME");
		String policyValue			= dataMap.get("POLICYVALUE");
		String conditionType		= dataMap.get("CONDITIONTYPE");
		String conditionID			= dataMap.get("CONDITIONID");
		String lotType		 		= dataMap.get("LOTTYPE");
		String sPriorty		 		= dataMap.get("PRIORITY");
		String sWorkCenter		 	= dataMap.get("WORKCENTER");
		
		// WORKORDER 정보 연계
		
		// WorkCenter 정보로 Line의 대표문자 조회
		AREA oAreaInfo = new AREA();
		oAreaInfo.setKeyPLANTID(plantID);
		oAreaInfo.setKeyAREAID(areaID);
		oAreaInfo = (AREA) oAreaInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		AREA oLineAreaInfo = new AREA();
		oLineAreaInfo.setKeyPLANTID(oAreaInfo.getKeyPLANTID());
		oLineAreaInfo.setKeyAREAID(oAreaInfo.getSUPERAREAID());
		oLineAreaInfo = (AREA) oLineAreaInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		// 대표문자
		String sRepChar = oLineAreaInfo.getREPRESENTATIVECHAR();
		if ( sRepChar == null || sRepChar.isEmpty() ) {
			sRepChar = sWorkCenter.substring(0, 1);
		}
		
		
		// 요청 수량 만큼 생성 로직
		for ( int i = 0; i < iCreateLotCount; i++ ) 
		{
			txnInfo.setTxnId("TxnCreateLot");
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			// P: 양산, D: Dummy, E: Engineer, T: Test 구분
//			String productionType 		= "E";
//			if ( lotType != null && lotType.length() > 1 )
//			{
//				productionType = lotType.substring(0, 1);
//			}
			
			String lotID = nameGenerator.nameGenerate( plantID, "LotID", new Object[] {sRepChar} );
			
			LotCreateInfo createLotInfo = new LotCreateInfo();
			createLotInfo.setLOTID(lotID);
			createLotInfo.setPLANTID(plantID);
			createLotInfo.setWORKORDER(workOrder);
			createLotInfo.setPRODUCTID(productID);
			createLotInfo.setDEPARTMENT(department);
			createLotInfo.setAREAID(areaID);
			createLotInfo.setEQUIPMENTID(equipmentID);
			createLotInfo.setSTARTQUANTITY(startQuantity);
			
			createLotInfo.setBOMID(bomID);
			createLotInfo.setBOMVERSION(bomVersion);
			createLotInfo.setPOLICYNAME(policyName);
			createLotInfo.setPOLICYVALUE(policyValue);
			createLotInfo.setCONDITIONTYPE(conditionType);
			createLotInfo.setCONDITIONID(conditionID);
			createLotInfo.setLOTTYPE(lotType);
			createLotInfo.setPRIORITY(sPriorty);
			
			
		    LOTINFORMATION lotInfo = lotCustomService.createLot(createLotInfo, bChangeProcessFlag, txnInfo);
			
			if ( bChangeProcessFlag ) {
				makeReposition(lotInfo.getKeyLOTID(), sStartProcessID, ConvertUtil.Object2Long(sStartProcessSeq), txnInfo);
			}
			
//			InterfaceUtil.sendMailMSSQL("MSSQL MAIL", "jygu@blws.pe.kr", "TEST", "테스트 생성 Lot은 " + lotInfo.getKeyLOTID() + " 입니다.", "HTML");
		}

		return recvDoc;
	}
	
	/**
	 * LotTrackingService의 getProcessInfo를 통해 받아온 VIEWLOTTRACKING에 CurrentFlag Yes로 수정해 재배치
	 * LOTPROCESSHISTORY에 이력 기록
	 *  
	 * @param lotID
	 * @param changeProcessID
	 * @param changeProcessSeq
	 * @param txnInfo
	 * @throws CustomException
	 * 
	 */
	public void makeReposition(String lotID, String changeProcessID, Long changeProcessSeq, TxnInfo txnInfo)
	{
		LotValidation validation = new LotValidation();
		LotHistory addHistory = new LotHistory();
		LotTrackingService trackingService = new LotTrackingService();
		
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );

		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		// Status Check
		// 변경 후 공정의 공정 시작 상태 체크 및 CurrentFlag 변경
		// 변경 전 공정의 전체 CurrentFlag 변경 ( 추후 결정 )
		
		VIEWLOTTRACKING changeViewLotInfo = 
				trackingService.getProcessInfo(lotID, lotInfo.getPROCESSROUTEID(), changeProcessID, changeProcessSeq, null, Constant.EVENT_START);
		
		if ( Constant.FLAG_YN_YES.equalsIgnoreCase(changeViewLotInfo.getCURRENTFLAG()) || 
				Constant.OPERATIONSTATE_COMPLETE.equalsIgnoreCase(changeViewLotInfo.getSTATUS()) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
//		validation.validationLotState( lotID, viewLotInfo.getSTATUS(), new Object[] {Constant.OPERATIONSTATE_COMPLETE});
		validation.validationNotLotState( lotID, lotInfo.getHOLDSTATE(), new Object[] {Constant.LOTHOLDSTATE_ONHOLD} );
		
		if ( changeViewLotInfo != null )
		{
			// 예약 작업 설정에 대한 처리
			if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(changeViewLotInfo.getFUTUREACTIONFLAG()) )
			{
				LotService lotService = new LotService();
				
				FUTUREACTION futureAction = new FUTUREACTION();
				futureAction.setKeyLOTID(changeViewLotInfo.getKeyLOTID());
				futureAction.setKeyRELATIONTIMEKEY(changeViewLotInfo.getKeyTIMEKEY());
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
			
			// CurrentFlag Update
			changeViewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
			changeViewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
		}
		
		// Event Name
		txnInfo.setTxnId( Constant.EVENT_REPOSITION );
		txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
		
		// History Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		lotProcessHistory.setOLDCURRENTQUANTITY( lotInfo.getCURRENTQUANTITY() );
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, changeViewLotInfo, txnInfo);
		
		if ( changeViewLotInfo != null )
		{
			// 자동 공정 시작 로직
			if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(changeViewLotInfo.getAUTOTRACKIN()) )
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

				ProcessCustomService operationService = new ProcessCustomService();
				operationService.processStart(lotID, changeViewLotInfo.getPROCESSID(), 
						String.valueOf(changeViewLotInfo.getPROCESSSEQUENCE()), changeViewLotInfo.getROUTERELATIONSEQUENCE(), sTxnInfo, Constant.CONTROL_MODE_MANUAL);
			}
		}
	}
}
