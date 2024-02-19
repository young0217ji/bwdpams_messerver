package mes.lot.custom;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import mes.constant.Constant;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.LOTPROCESSHISTORY;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.transaction.LotHistory;
import mes.lot.transaction.LotTrackingService;
import mes.lot.transaction.ProcessService;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class ProcessCustomService
{
	private static Log log = LogFactory.getLog(ProcessCustomService.class);
	
	/**
	 * 공정 시작
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param txnInfo
	 * @param pastMode
	 * @return 
	 * 
	 */
	public void processStart(String lotID, String processID, String processSequence, Long routeRelationSequence, TxnInfo txnInfo, String pastMode)
	{
		processStart(lotID, processID, processSequence, routeRelationSequence, null, txnInfo, pastMode);
	}
	
	/**
	 * lotID의 processID 공정 시작
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param inQuantity
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 * 
	 */
	public void processStart(String lotID, String processID, String processSequence, Long routeRelationSequence, Double inQuantity, TxnInfo txnInfo, String pastMode)
	{
		ProcessService processService = new ProcessService();
		
		// LotInformation
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		

		VIEWLOTTRACKING viewLotInfo = processService.makeTrackIn(lotInfo, processID, processSequence, routeRelationSequence, inQuantity, txnInfo, pastMode);
		
		
		// 자동 진행 여부 판별 후 진행 Process
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getAUTOTRACKINGFLAG()) )
		{
			processService.autoTrackingProcess(lotInfo, viewLotInfo, txnInfo);
		}
	}
	
	/**
	 * 공정 종료
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 * 
	 */
	public void processEnd(String lotID, String processID, String processSequence, Long routeRelationSequence, TxnInfo txnInfo, String pastMode)
	{
		processEnd(lotID, processID, processSequence, routeRelationSequence, null, txnInfo, pastMode);
	}
	
	/**
	 * lotID의  processID 공정 종료 
	 * 다음 공정에 대한 처리
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param outQuantity
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 * 
	 */
	public void processEnd(String lotID, String processID, String processSequence, Long routeRelationSequence, Double outQuantity, TxnInfo txnInfo, String pastMode)
	{
		ProcessService processService = new ProcessService();
		LotTrackingService trackingService = new LotTrackingService();
		
		// LotInformation
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		
		VIEWLOTTRACKING viewLotInfo = processService.makeTrackOut(lotInfo, processID, processSequence, routeRelationSequence, outQuantity, txnInfo, pastMode);
		
		
		if ( Constant.RECIPE_ID_PACKING.equalsIgnoreCase(viewLotInfo.getPROCESSTYPE()) &&  
				Constant.RECIPE_ID_PACKING.equalsIgnoreCase(viewLotInfo.getPACKINGFLAG()) )
		{
			// 포장여부 확인
//			if ( !checkExistPacking(lotInfo) )
//			{
//				// 진행중인 Lot({0}) 에 대한 포장내역이 등록되지 않아 처리할 수 없습니다.
//				throw new CustomException("LOT-009", new Object[] {lotInfo.getKeyLOTID()});
//			}
		}
		
		
		// Repeat Operation TrackOut
		// Measure Operation TrackOut
		boolean inspectionFlag = false;
		String resultValue = "";
		
		// Measure 일 경우 결과에 따라 Repeat 공정으로 이동
		// Repeat 공정인 경우 재측정을 위해 Measure 공정으로 이동 로직 필요
		if ( Constant.RECIPE_ID_REPEAT.equalsIgnoreCase(viewLotInfo.getPROCESSTYPE()) )
		{
			if ( trackingService.repeatProcess(lotInfo, viewLotInfo, txnInfo) != null )
			{
				return;
			}
		}
		else if ( Constant.RECIPE_ID_MEASURE.equalsIgnoreCase(viewLotInfo.getPROCESSTYPE()) )
		{
			VIEWLOTTRACKING inspectionLotInfo = trackingService.getInspectionParameter(viewLotInfo);
			
			if ( inspectionLotInfo != null )
			{
				inspectionFlag = true;
			}
		}

		
		// 다음 공정에 대한 처리 Logic
		VIEWLOTTRACKING nextViewLotInfo = 
				trackingService.getNextProcess(lotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_START, viewLotInfo.getPROCESSSEQUENCE());
		
		if ( nextViewLotInfo == null )
		{
			processService.processEndDetailCaseNull(lotInfo, viewLotInfo, txnInfo);
		}
		else
		{
			processService.processEndDetailCaseNotNull(lotInfo, viewLotInfo, nextViewLotInfo, inspectionFlag, resultValue, txnInfo);
		}
	}
	
	/**
	 * lotID의 공정 시작한  processID 시작 취소
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param txnInfo
	 * @return List
	 * 
	 */
	public List<LOTINFORMATION> processStartCancel(String lotID, String processID, String processSequence, Long routeRelationSequence, TxnInfo txnInfo)
	{
		ProcessService processService = new ProcessService();
		
		ArrayList<LOTINFORMATION> interfaceList = new ArrayList<LOTINFORMATION>();
		
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		// ViewLotTracking
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( lotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_START );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		processService.makeTrackInCancel(lotInfo, viewLotInfo, txnInfo);
		if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(viewLotInfo.getFIRSTPROCESSFLAG()) )
		{
			interfaceList.add(lotInfo);
		}
		
		return interfaceList;
	}
	
	/**
	 * lotID의 공정 종료한  processID 종료 취소
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void processEndCancel(String lotID, String processID, String processSequence, Long routeRelationSequence, TxnInfo txnInfo)
	{
		ProcessService processService = new ProcessService();
		
		// LotInformation
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		processService.makeTrackOutCancel(lotInfo, processID, processSequence, routeRelationSequence, txnInfo);
	}
	
	/**
	 * lotInfo의 설비 변경
	 * LOTPROCESSHISTORY에 이력 기록
	 * 
	 * @param lotInfo
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param changeEquipmentID
	 * @param txnInfo
	 * @return
	 * 
	 */
	public void changeEquipment(LOTINFORMATION lotInfo, String processID, String processSequence, Long routeRelationSequence, String changeEquipmentID, TxnInfo txnInfo)
	{
		ProcessService processService = new ProcessService();
		LotHistory addHistory = new LotHistory();
		
		VIEWLOTTRACKING viewLotInfo = processService.changeEquipment(lotInfo, processID, processSequence, routeRelationSequence, changeEquipmentID, txnInfo);
		
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, viewLotInfo, txnInfo);
	}
}
