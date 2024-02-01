package mes.lot.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.StringUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.lot.data.DY_DEFECTREPORT;
import mes.lot.data.DY_PROCESSRESULT;
import mes.lot.data.IF_ERP_CONFIRM;
import mes.lot.data.IF_ERP_GR;
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
		
		
		// AI 정보 Mongo DB 저장
		try {
			DYInterfaceManager.callService(lotInfo, viewLotInfo, txnInfo);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.info(e.getStackTrace());
			
			// LOT({0})의 공정({1}) 진행정보를 AI 정보 DB에 저장시 오류가 발생하였습니다. 담당자 확인이 필요합니다.
			throw new CustomException("LOT-019", new Object[] {lotInfo.getKeyLOTID(), viewLotInfo.getPROCESSID()});
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		
		
		// AI 정보 Mongo DB 저장
		try {
			DYInterfaceManager.callService(lotInfo, viewLotInfo, txnInfo);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.info(e.getStackTrace());
			
			// LOT({0})의 공정({1}) 진행정보를 AI 정보 DB에 저장시 오류가 발생하였습니다. 담당자 확인이 필요합니다.
			throw new CustomException("LOT-019", new Object[] {lotInfo.getKeyLOTID(), viewLotInfo.getPROCESSID()});
		}
		
		
		// 공정 종료 이후 해당 공정이 실적 발생 공정인 경우 실적 발생 호출
		// 실적 발생 이후 취소는 별개 진행
		// 공정 취소시 실적 발생된 경우 취소 금지
		// 실적 발생 여부 Flag 확인
		
		// 1. 실적 발생 공정 여부 판단
		// 마지막 공정이거나 현재 공정의 ERP 공정코드와 다음 공정의 ERP 공정코드가 다른 경우 실적 보고 
		if ( nextViewLotInfo == null || viewLotInfo.getERPPROCESSCODE().equals(nextViewLotInfo.getERPPROCESSCODE()) == false ) {
			
			// 2. 중단 혹은 마지막 진행 Lot 여부 체크 ( 중단인 경우 Flag 처리 추후 로직 추가 )
			String sLotQTYSql = " SELECT W.PLANTID "
					+ "	,W.PRODUCTORDERID "
					+ "	,W.WORKORDERID "
					+ "	,MAX(W.WORKORDERSTATE) AS WORKORDERSTATE "
					+ "	,MAX(W.PLANQUANTITY) AS PLANQUANTITY "
					+ "	,MAX(R.ERPPROCESSCODE) AS ERPPROCESSCODE "
					+ "	,SUM(R.PRODUCTQUANTITY) + SUM(R.SCRAPQUANTITY) AS LOTQUANTITY "
					+ "	FROM WORKORDER W "
					// Lot 상태가 Scrap 이거나 Hold 상태인 경우 생산 수량 집계 제외 - 공정부적합
					+ "		LEFT OUTER JOIN ( SELECT A.* FROM DY_PROCESSRESULT A INNER JOIN LOTINFORMATION B ON A.LOTID = B.LOTID AND B.LOTSTATE NOT IN ('Scrapped', 'Terminated') AND B.HOLDSTATE = 'NotOnHold' ) R "
					+ " 			ON W.PLANTID = R.PLANTID AND W.WORKORDERID = R.WORKORDERID "
					+ "				AND R.PROCESSID = ? AND R.PROCESSSEQUENCE = ? AND R.REWORKCOUNT = 0 "
					+ "	WHERE 1 = 1 "
					+ "	AND W.PLANTID = ? "
					+ "	AND W.WORKORDERID = ? "
					+ "	GROUP BY W.PLANTID "
					+ "		,W.PRODUCTORDERID "
					+ "		,W.WORKORDERID ";

	    	 List<LinkedCaseInsensitiveMap> oResultList = SqlMesTemplate.queryForList(sLotQTYSql, new Object[] { viewLotInfo.getPROCESSID(), viewLotInfo.getPROCESSSEQUENCE(), lotInfo.getPLANTID(), lotInfo.getWORKORDER() });

	    	 // 동일 공정에 ERPPROCESSCODE 값이 2개 이상이면 안됨 해당 경우 처리 방안 고려
	    	if( oResultList.size() >= 1 ) {
	    		LinkedCaseInsensitiveMap oResultMap = (LinkedCaseInsensitiveMap) oResultList.get(0);
	    		Long lPlanQuantity = ConvertUtil.Object2Long4Zero(oResultMap.get("PLANQUANTITY"));
	    		Long lLotQuantity = ConvertUtil.Object2Long4Zero(oResultMap.get("LOTQUANTITY"));
	    		String sERPProcessCode = ConvertUtil.Object2String(oResultMap.get("ERPPROCESSCODE"));
	    		String sProductOrderID = ConvertUtil.Object2String(oResultMap.get("PRODUCTORDERID"));
	    		
	    		// 계획 수량과 생산 수량 비교 ( 생산수량이 크거나 같은 경우 실적 보고 )
	    		// Rework 인 경우 Main 실적 발생 시 포함하여 발생, 미포함시에는 공정 종료시 마다 개별 발생
	    		// 중단인 경우 로직 추가 필요!!! ( 중단 Flag 관리 기능 추가 필요 )
	    		boolean bManualStopFlag = true;
	    		if ( lPlanQuantity <= lLotQuantity || bManualStopFlag ) {
	    			
	    			// 3. 비가동 시간 계산
	    			String sProcessDownTimeSql = " ";
	    			sProcessDownTimeSql += " SELECT DD.PLANTID ";
	    			sProcessDownTimeSql += " 		,DD.WORKORDER ";
	    			sProcessDownTimeSql += " 		,DD.EQUIPMENTID ";
	    			sProcessDownTimeSql += " ,SUM(DATEDIFF(MI, DD.LINESTOPSTARTTIME, DD.LINESTOPENDTIME)) AS PROCESSDOWNTIME ";
	    			sProcessDownTimeSql += " FROM ( ";
	    			sProcessDownTimeSql += " SELECT D.PLANTID ";
	    			sProcessDownTimeSql += " 		,D.WORKORDER ";
	    			sProcessDownTimeSql += " 		,D.EQUIPMENTID ";
	    			sProcessDownTimeSql += " 		,D.PROCESSID ";
	    			sProcessDownTimeSql += " 		,D.LINESTOPCODE ";
	    			sProcessDownTimeSql += " 		,CASE WHEN D.LINESTOPSTARTTIME <= CONVERT(DATETIME, :PROCESSSTARTTIME) THEN CONVERT(DATETIME, :PROCESSSTARTTIME) ELSE D.LINESTOPSTARTTIME END AS LINESTOPSTARTTIME ";
	    			sProcessDownTimeSql += " 		,CASE WHEN D.LINESTOPENDTIME >= CONVERT(DATETIME, :PROCESSENDTIME) THEN CONVERT(DATETIME, :PROCESSSTARTTIME) ELSE D.LINESTOPENDTIME END AS LINESTOPENDTIME ";
	    			sProcessDownTimeSql += " FROM DY_LINESTOP D ";
	    			sProcessDownTimeSql += " WHERE 1 = 1 ";
	    			sProcessDownTimeSql += " AND D.PLANTID = :PLANTID ";
	    			sProcessDownTimeSql += " AND D.WORKORDER  = :WORKORDER ";
	    			sProcessDownTimeSql += " AND D.EQUIPMENTID = :EQUIPMENTID ";
	    			sProcessDownTimeSql += " AND ( D.LINESTOPSTARTTIME BETWEEN CONVERT(DATETIME, :PROCESSSTARTTIME) AND CONVERT(DATETIME, :PROCESSENDTIME) ";
	    			sProcessDownTimeSql += " 	OR D.LINESTOPENDTIME BETWEEN CONVERT(DATETIME, :PROCESSSTARTTIME) AND CONVERT(DATETIME, :PROCESSENDTIME) ";
	    			sProcessDownTimeSql += " 	OR CONVERT(DATETIME, :PROCESSSTARTTIME) BETWEEN D.LINESTOPSTARTTIME AND D.LINESTOPENDTIME ";
	    			sProcessDownTimeSql += " 	OR CONVERT(DATETIME, :PROCESSENDTIME) BETWEEN D.LINESTOPSTARTTIME AND D.LINESTOPENDTIME ) ";
	    			sProcessDownTimeSql += " 	) DD ";
	    			sProcessDownTimeSql += " WHERE 1 = 1 ";
	    			sProcessDownTimeSql += " GROUP BY DD.PLANTID ";
	    			sProcessDownTimeSql += " ,DD.WORKORDER ";
	    			sProcessDownTimeSql += " ,DD.EQUIPMENTID ";
	    			
	    			DY_PROCESSRESULT oResultDetailInfo = new DY_PROCESSRESULT();
	    			oResultDetailInfo.setKeyPLANTID(lotInfo.getPLANTID());
	    			oResultDetailInfo.setWORKORDERID(lotInfo.getWORKORDER());
	    			oResultDetailInfo.setERPPROCESSCODE(sERPProcessCode);
	    			
	    			List<DY_PROCESSRESULT> listResultDetail = (List<DY_PROCESSRESULT>) oResultDetailInfo.excuteDML(SqlConstant.DML_SELECTLIST);
	    			
	    			String sIFID = "";
	    			String sInterfaceDate = txnInfo.getEventTimeKey().substring(0, 8);
	    			
	    			for ( int i = 0; i < listResultDetail.size(); i++ ) {
	    				oResultDetailInfo = listResultDetail.get(i);
	    				
	    				// 이미 Interface를 진행한 경우 다음 Row로 Skip
	    				if ( StringUtil.isNullOrEmpty(oResultDetailInfo.getINTERFACEKEY()) == false ) {
	    					continue;
	    				}
	    				
	    				// sIFID 최초인 경우만 발번
	    				if ( StringUtil.isNullOrEmpty(sIFID) ) {
	    					String sInsertSql = " INSERT INTO DY_ERPPORESULT ( "
	    							+ " PLANTID "
	    							+ " ,INTERFACEDATE "
	    							+ " ,WORKORDERID "
	    							+ " ,ERPPROCESSCODE "
	    							+ " ,CREATETIME "
	    							+ " ,CREATEUSERID "
	    							+ " ,LASTUPDATETIME "
	    							+ " ,LASTUPDATEUSERID "
	    							+ " ) VALUES ( "
	    							+ " ?, ?, ?, ?, GETDATE(), ?, GETDATE(), ?) ";
	    					
	    					int insertRow = SqlMesTemplate.update(sInsertSql, new Object[] {lotInfo.getPLANTID(), sInterfaceDate, lotInfo.getWORKORDER(), sERPProcessCode, txnInfo.getTxnUser(), txnInfo.getTxnUser()});
	    					
	    					if ( insertRow == 1 ) {
	    						String sSelectSql = " SELECT MAX(IFID) AS IFID FROM DY_ERPPORESULT WHERE 1 = 1 "
	    								+ " AND PLANTID = ? "
	    								+ " AND INTERFACEDATE = ? "
	    								+ " AND WORKORDERID = ? "
	    								+ " AND ERPPROCESSCODE = ? ";
	    						List<LinkedCaseInsensitiveMap> oSelectList = SqlMesTemplate.queryForList(sSelectSql, new Object[] {lotInfo.getPLANTID(), sInterfaceDate, lotInfo.getWORKORDER(), sERPProcessCode});
	    						
	    						if ( oSelectList.size() > 0 ) {
	    							sIFID = ConvertUtil.Object2String(oSelectList.get(0).get("IFID"));
	    						}
	    						else {
	    							// 실적의 ERP 보고를 위한 Key 발급에 실패하였습니다. 담당자에게 문의해 주시기 바랍니다.
	    							throw new CustomException("LOT-017", new Object[] {});
	    						}
	    					}
	    					else {
    							// 실적의 ERP 보고를 위한 Key 발급에 실패하였습니다. 담당자에게 문의해 주시기 바랍니다.
    							throw new CustomException("LOT-017", new Object[] {});
	    					}
	    				}
	    				
	    				HashMap<String, Object> oBindMap = new HashMap<String, Object>();
	    				oBindMap.put("PLANTID", lotInfo.getPLANTID());
	    				oBindMap.put("WORKORDER", lotInfo.getWORKORDER());
	    				oBindMap.put("EQUIPMENTID", oResultDetailInfo.getEQUIPMENTID());
	    				oBindMap.put("PROCESSSTARTTIME", oResultDetailInfo.getSTARTTIME());
	    				oBindMap.put("PROCESSENDTIME", oResultDetailInfo.getENDTIME());
	    				
	    				List<LinkedCaseInsensitiveMap> oDownTimeList = SqlMesTemplate.queryForList(sProcessDownTimeSql, oBindMap);
	    				
	    				// 비가동 시간 저장
	    				if ( oDownTimeList.size() > 0 ) {
	    					Long lProcessDownTime = ConvertUtil.Object2Long4Zero(oDownTimeList.get(0).get("PROCESSDOWNTIME"));
	    					
	    					// Interface ID 발번 후 저장
	    					oResultDetailInfo.setINTERFACEKEY(sIFID);
	    					oResultDetailInfo.setPROCESSDOWNTIME(lProcessDownTime);
	    				}
	    				else {
	    					// 비가동 시간 없음! 0
	    					// Interface ID 발번 후 저장
	    					oResultDetailInfo.setINTERFACEKEY(sIFID);
	    					oResultDetailInfo.setPROCESSDOWNTIME((long) 0);
	    				}
	    				
	    				// 완성부적합 수량 저장
	    				DY_DEFECTREPORT oDefectInfo = new DY_DEFECTREPORT();
	    				oDefectInfo.setKeyPLANTID(oResultDetailInfo.getKeyPLANTID());
	    				oDefectInfo.setLOTID(oResultDetailInfo.getKeyLOTID());
	    				oDefectInfo.setERPPROCESSID(oResultDetailInfo.getERPPROCESSCODE());
	    				oDefectInfo.setDEFECTTYPE("3F"); // 완성부적합
	    				List<DY_DEFECTREPORT> listDefect = (List<DY_DEFECTREPORT>) oDefectInfo.excuteDML(SqlConstant.DML_SELECTLIST);
	    				if ( listDefect.size() > 0 ) {
	    					// 완성부적합 수량 저장
	    					oResultDetailInfo.setDEFECTQUANTITY(1.0);
	    				}
	    				else {
	    					oResultDetailInfo.setDEFECTQUANTITY(0.0);
	    				}
	    				oResultDetailInfo.setPRODUCTORDERID(sProductOrderID);
	    				
	    				oResultDetailInfo.excuteDML(SqlConstant.DML_UPDATE);
	    			}
	    			
	    			// 4. Interface 실적 등록
	    			// sIFID 값이 존재하는 경우에만 실적 보고
	    			if ( StringUtil.isNullOrEmpty(sIFID) == false ) {
	    				
	    				String sSummarySql = " "
	    						+ " SELECT A.PLANTID "
	    						+ " ,A.WORKORDERID "
	    						+ "	,A.ERPPROCESSCODE "
	    						+ "	,A.PRODUCTORDERTYPE "
	    						+ " ,SUM(A.PRODUCTQUANTITY) AS PRODUCTQUANTITY "
	    						+ " ,A.PRODUCTUNIT "
	    						+ " ,SUM(A.SCRAPQUANTITY) AS SCRAPQUANTITY "
	    						+ " ,SUM(A.DEFECTQUANTITY) AS DEFECTQUANTITY "
	    						+ " ,MIN(A.STARTTIME) AS STARTTIME "
	    						+ " ,MAX(A.ENDTIME) AS ENDTIME "
	    						+ " ,SUM(A.PROCESSTIME) AS PROCESSTIME "
	    						+ " ,SUM(A.WORKTIME) AS WORKTIME "
	    						+ " ,SUM(A.RESULTWORKTIME) AS RESULTWORKTIME "
	    						+ " ,SUM(A.MACHINETIME) AS MACHINETIME "
	    						+ " ,SUM(A.RESULTMACHINETIME) AS RESULTMACHINETIME "
	    						+ " ,SUM(A.PROCESSDOWNTIME) AS PROCESSDOWNTIME "
	    						+ " ,A.LASTOPERFLAG "
	    						+ " ,A.PRODUCTORDERID "
	    						+ " ,A.STOCKLOCATION "
	    						+ " FROM ( "
	    						
	    						// Lot 별 집계
	    						+ " SELECT R.PLANTID "
	    						+ "	,R.WORKORDERID "
	    						+ "	,R.ERPPROCESSCODE "
	    						+ "	,R.PRODUCTORDERTYPE "
	    						+ " ,R.LOTID "
	    						+ " ,COUNT(DISTINCT R.LOTID) AS LOTCOUNT "
//	    						+ "	,SUM(CASE WHEN ISNULL(R.DEFECTQUANTITY, 0) > 0 THEN 0 ELSE R.PRODUCTQUANTITY END) AS PRODUCTQUANTITY "
								+ "	,CASE WHEN SUM(ISNULL(R.DEFECTQUANTITY, 0)) > 0 THEN 0 ELSE (CASE WHEN SUM(ISNULL(R.PRODUCTQUANTITY, 0)) > 0 THEN 1 ELSE 0 END) END AS PRODUCTQUANTITY "
	    						+ "	,R.PRODUCTUNIT "
//	    						+ "	,SUM(R.SCRAPQUANTITY) AS SCRAPQUANTITY "
								+ "	,CASE WHEN SUM(ISNULL(R.SCRAPQUANTITY, 0)) > 0 THEN 1 ELSE 0 END AS SCRAPQUANTITY "
//	    						+ " ,SUM(ISNULL(R.DEFECTQUANTITY, 0)) AS DEFECTQUANTITY "
								+ " ,CASE WHEN SUM(ISNULL(R.DEFECTQUANTITY, 0)) > 0 THEN 1 ELSE 0 END AS DEFECTQUANTITY "
	    						+ "	,CONVERT(NVARCHAR(8), MIN(R.STARTTIME), 112) AS STARTTIME "
	    						+ "	,CONVERT(NVARCHAR(8), MAX(R.ENDTIME), 112) AS ENDTIME "
	    						+ "	,SUM(R.PROCESSTIME) AS PROCESSTIME "
	    						+ "	,SUM(R.WORKTIME) AS WORKTIME "
	    						+ "	,SUM(R.WORKTIME) - SUM(R.PROCESSDOWNTIME) AS RESULTWORKTIME "
	    						+ "	,SUM(R.MACHINETIME) AS MACHINETIME "
	    						+ "	,SUM(R.MACHINETIME) - SUM(R.PROCESSDOWNTIME) AS RESULTMACHINETIME "
	    						+ "	,SUM(R.PROCESSDOWNTIME) AS PROCESSDOWNTIME "
	    						+ " ,WO.LASTOPERFLAG "
	    						+ " ,W.PRODUCTORDERID "
	    						+ " ,W.STOCKLOCATION "
	    						+ "	FROM DY_PROCESSRESULT R "
	    						+ " 	INNER JOIN WORKORDER W ON R.PLANTID = W.PLANTID AND R.WORKORDERID = W.WORKORDERID "
	    						+ " 	LEFT OUTER JOIN WORKORDEROPERATION WO ON R.PLANTID = WO.PLANTID AND R.WORKORDERID = WO.WORKORDERID AND R.ERPPROCESSCODE = WO.ERPOPERID "
	    						+ "	WHERE 1 = 1 "
	    						+ "	AND R.PLANTID = ? "
	    						+ "	AND R.WORKORDERID = ? "
	    						+ "	AND R.ERPPROCESSCODE = ? "
	    						+ "	AND R.INTERFACEKEY = ? "
	    						+ "	GROUP BY R.PLANTID "
	    						+ "		,R.WORKORDERID  "
	    						+ "		,R.ERPPROCESSCODE "
	    						+ "		,R.PRODUCTORDERTYPE "
	    						+ " 	,R.LOTID "
	    						+ "		,R.PRODUCTUNIT "
	    						+ "		,WO.LASTOPERFLAG "
	    						+ " 	,W.PRODUCTORDERID "
	    						+ " 	,W.STOCKLOCATION "
	    						
	    						+ " ) A WHERE 1 = 1 "
	    						+ "	GROUP BY A.PLANTID "
	    						+ "		,A.WORKORDERID  "
	    						+ "		,A.ERPPROCESSCODE "
	    						+ "		,A.PRODUCTORDERTYPE "
	    						+ "		,A.PRODUCTUNIT "
	    						+ "		,A.LASTOPERFLAG "
	    						+ " 	,A.PRODUCTORDERID "
	    						+ " 	,A.STOCKLOCATION "
	    						+ " ";

	    		    	List<LinkedCaseInsensitiveMap> oSummaryList = SqlMesTemplate.queryForList(sSummarySql, new Object[] { lotInfo.getPLANTID(), lotInfo.getWORKORDER(), sERPProcessCode, sIFID });
	    				
	    				IF_ERP_CONFIRM oConfirmResult = new IF_ERP_CONFIRM();
	    				oConfirmResult.setKeyIFID( ConvertUtil.Object2Long(sIFID) );
	    				oConfirmResult.setKeySEQ( (long) 1 );
	    				oConfirmResult.setMANDT( "100" );
	    				oConfirmResult.setWERKS( ConvertUtil.Object2String(oSummaryList.get(0).get("PLANTID")) );
	    				oConfirmResult.setAUFNR( ConvertUtil.Object2String(oSummaryList.get(0).get("PRODUCTORDERID")) );
	    				oConfirmResult.setVORNR( sERPProcessCode );
	    				oConfirmResult.setBUDAT( sInterfaceDate );
	    				oConfirmResult.setVGWTS( ConvertUtil.Object2String(oSummaryList.get(0).get("PRODUCTORDERTYPE")) );
	    				
	    				Double dDefectQty = ConvertUtil.Object2Double(oSummaryList.get(0).get("DEFECTQUANTITY"));
	    				// AUERU - Activate Final Confirmation : WORKORDEROPERATION.LASTOPERFLAG = 'Y' & 계획수량 적합 = 'X', WORKORDEROPERATION.LASTOPERFLAG != 'Y' & 계획수량 적합 = '1', 그외 ''
	    				if ( Constant.FLAG_YN_YES.equals(ConvertUtil.Object2String(oSummaryList.get(0).get("LASTOPERFLAG"))) && dDefectQty.compareTo(0.0) == 0 && lPlanQuantity <= lLotQuantity ) {
	    					oConfirmResult.setAUERU( "X" );
	    				}
	    				else if ( Constant.FLAG_YN_YES.equals(ConvertUtil.Object2String(oSummaryList.get(0).get("LASTOPERFLAG"))) == false && dDefectQty.compareTo(0.0) == 0 && lPlanQuantity <= lLotQuantity ) {
	    					oConfirmResult.setAUERU( "1" );
	    				}
	    				else {
	    					oConfirmResult.setAUERU( "" );
	    				}
	    				
	    				oConfirmResult.setLMNGA( ConvertUtil.Object2Double(oSummaryList.get(0).get("PRODUCTQUANTITY")) );
	    				oConfirmResult.setINSMK( "" ); // Stock : 부적합='S', 양품=NULL 고정값
	    				oConfirmResult.setXMNGA( ConvertUtil.Object2Double(oSummaryList.get(0).get("SCRAPQUANTITY")) );
	    				oConfirmResult.setMEINS( ConvertUtil.Object2String(oSummaryList.get(0).get("PRODUCTUNIT")) );
	    				oConfirmResult.setISM01( ConvertUtil.Object2Long(oSummaryList.get(0).get("RESULTWORKTIME")) );
	    				oConfirmResult.setISM03( ConvertUtil.Object2Long(oSummaryList.get(0).get("RESULTMACHINETIME")) );
	    				oConfirmResult.setILE01( "MIN" );
	    				oConfirmResult.setISDD( ConvertUtil.Object2String(oSummaryList.get(0).get("STARTTIME")) );
	    				oConfirmResult.setIEDD( ConvertUtil.Object2String(oSummaryList.get(0).get("ENDTIME")) );
	    				oConfirmResult.setADD_DTTM( txnInfo.getEventTime() );
	    				oConfirmResult.setADD_USERID( txnInfo.getTxnUser() );
	    				oConfirmResult.setERP_STATUS( "N" );
	    				
	    				if ( ConvertUtil.Object2Double(oSummaryList.get(0).get("PRODUCTQUANTITY")).compareTo(0.0) != 0 || 
	    						ConvertUtil.Object2Double(oSummaryList.get(0).get("SCRAPQUANTITY")).compareTo(0.0) != 0 ) {
	    					oConfirmResult.excuteDML(SqlConstant.DML_INSERT);
	    				}
	    				
	    				if ( dDefectQty.compareTo(0.0) != 0 ) {
	    					IF_ERP_CONFIRM oConfirmDefectResult = new IF_ERP_CONFIRM();
	    					oConfirmDefectResult.setKeyIFID( ConvertUtil.Object2Long(sIFID) );
		    				// 부적합에 대한 2 로직
	    					oConfirmDefectResult.setKeySEQ( (long) 2 );
	    					oConfirmDefectResult.setMANDT( "100" );
	    					oConfirmDefectResult.setWERKS( ConvertUtil.Object2String(oSummaryList.get(0).get("PLANTID")) );
	    					oConfirmDefectResult.setAUFNR( ConvertUtil.Object2String(oSummaryList.get(0).get("PRODUCTORDERID")) );
	    					oConfirmDefectResult.setVORNR( sERPProcessCode );
	    					oConfirmDefectResult.setBUDAT( sInterfaceDate );
	    					oConfirmDefectResult.setVGWTS( ConvertUtil.Object2String(oSummaryList.get(0).get("PRODUCTORDERTYPE")) );
		    				
		    				// AUERU - Activate Final Confirmation : WORKORDEROPERATION.LASTOPERFLAG = 'Y' & 계획수량 적합 = 'X', WORKORDEROPERATION.LASTOPERFLAG != 'Y' & 계획수량 적합 = '1', 그외 ''
		    				if ( Constant.FLAG_YN_YES.equals(ConvertUtil.Object2String(oSummaryList.get(0).get("LASTOPERFLAG"))) && lPlanQuantity <= lLotQuantity ) {
		    					oConfirmDefectResult.setAUERU( "X" );
		    				}
		    				else if ( Constant.FLAG_YN_YES.equals(ConvertUtil.Object2String(oSummaryList.get(0).get("LASTOPERFLAG"))) == false && lPlanQuantity <= lLotQuantity ) {
		    					oConfirmDefectResult.setAUERU( "1" );
		    				}
		    				else {
		    					oConfirmDefectResult.setAUERU( "" );
		    				}
		    				
		    				oConfirmDefectResult.setLMNGA( dDefectQty );
		    				oConfirmDefectResult.setINSMK( "S" ); // Stock : 부적합='S', 양품=NULL 고정값
		    				oConfirmDefectResult.setXMNGA( 0.0 );
		    				oConfirmDefectResult.setMEINS( ConvertUtil.Object2String(oSummaryList.get(0).get("PRODUCTUNIT")) );
		    				oConfirmDefectResult.setISM01( (long) 0 );
		    				oConfirmDefectResult.setISM03( (long) 0 );
		    				oConfirmDefectResult.setILE01( "MIN" );
		    				oConfirmDefectResult.setISDD( ConvertUtil.Object2String(oSummaryList.get(0).get("STARTTIME")) );
		    				oConfirmDefectResult.setIEDD( ConvertUtil.Object2String(oSummaryList.get(0).get("ENDTIME")) );
		    				oConfirmDefectResult.setADD_DTTM( txnInfo.getEventTime() );
		    				oConfirmDefectResult.setADD_USERID( txnInfo.getTxnUser() );
		    				oConfirmDefectResult.setERP_STATUS( "N" );
		    				
		    				oConfirmDefectResult.excuteDML(SqlConstant.DML_INSERT);
	    				}
	    				
	    				
	    				IF_ERP_GR oGRResult = new IF_ERP_GR();
	    				oGRResult.setKeyIFID( ConvertUtil.Object2Long(sIFID) );
	    				oGRResult.setKeySEQ( (long) 1 );
	    				oGRResult.setMANDT( "100" );
	    				oGRResult.setWERKS( ConvertUtil.Object2String(oSummaryList.get(0).get("PLANTID")) );
	    				oGRResult.setAUFNR( ConvertUtil.Object2String(oSummaryList.get(0).get("PRODUCTORDERID")) );
	    				oGRResult.setBUDAT( sInterfaceDate );
	    				oGRResult.setLGORT( ConvertUtil.Object2String(oSummaryList.get(0).get("STOCKLOCATION")) );
	    				oGRResult.setERFMG( ConvertUtil.Object2Double(oSummaryList.get(0).get("PRODUCTQUANTITY")) );
	    				oGRResult.setMEINS( ConvertUtil.Object2String(oSummaryList.get(0).get("PRODUCTUNIT")) );
	    				oGRResult.setINSMK( "" ); // Stock : 부적합='S', 양품=NULL 고정값
	    				oGRResult.setADD_DTTM( txnInfo.getEventTime() );
	    				oGRResult.setADD_USERID( txnInfo.getTxnUser() );
	    				oGRResult.setERP_STATUS( "N" );
	    				
	    				if ( ConvertUtil.Object2Double(oSummaryList.get(0).get("PRODUCTQUANTITY")).compareTo(0.0) != 0 ) {
	    					oGRResult.excuteDML(SqlConstant.DML_INSERT);
	    				}
	    				
	    				if ( dDefectQty.compareTo(0.0) != 0 ) {
	    					IF_ERP_GR oGRDefectResult = new IF_ERP_GR();
	    					oGRDefectResult.setKeyIFID( ConvertUtil.Object2Long(sIFID) );
		    				// 부적합에 대한 2 로직
	    					oGRDefectResult.setKeySEQ( (long) 2 );
	    					oGRDefectResult.setMANDT( "100" );
	    					oGRDefectResult.setWERKS( ConvertUtil.Object2String(oSummaryList.get(0).get("PLANTID")) );
	    					oGRDefectResult.setAUFNR( ConvertUtil.Object2String(oSummaryList.get(0).get("PRODUCTORDERID")) );
	    					oGRDefectResult.setBUDAT( sInterfaceDate );
	    					oGRDefectResult.setLGORT( ConvertUtil.Object2String(oSummaryList.get(0).get("STOCKLOCATION")) );
	    					oGRDefectResult.setERFMG( dDefectQty );
	    					oGRDefectResult.setMEINS( ConvertUtil.Object2String(oSummaryList.get(0).get("PRODUCTUNIT")) );
	    					oGRDefectResult.setINSMK( "S" ); // Stock : 부적합='S', 양품=NULL 고정값
	    					oGRDefectResult.setADD_DTTM( txnInfo.getEventTime() );
	    					oGRDefectResult.setADD_USERID( txnInfo.getTxnUser() );
	    					oGRDefectResult.setERP_STATUS( "N" );
		    				
	    					oGRDefectResult.excuteDML(SqlConstant.DML_INSERT);
	    				}
	    			}
	    			// 5. ERP I/F 수행
	    		}
	    		// 그외의 경우 Skip
	    	}
			
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
		
		VIEWLOTTRACKING viewLotInfo = processService.makeTrackOutCancel(lotInfo, processID, processSequence, routeRelationSequence, txnInfo);
		
		// ERP Interface 여부를 체크하여 오류 발생
		DY_PROCESSRESULT oResultDetailInfo = new DY_PROCESSRESULT();
		oResultDetailInfo.setKeyPLANTID(lotInfo.getPLANTID());
		oResultDetailInfo.setKeyLOTID(lotInfo.getKeyLOTID());
		oResultDetailInfo.setKeyPROCESSID(viewLotInfo.getPROCESSID());
		oResultDetailInfo.setKeyPROCESSSEQUENCE(viewLotInfo.getPROCESSSEQUENCE());
		oResultDetailInfo.setKeyREWORKCOUNT(viewLotInfo.getREWORKCOUNT());
		
		oResultDetailInfo = (DY_PROCESSRESULT) oResultDetailInfo.excuteDML(SqlConstant.DML_SELECTROW);
		if ( oResultDetailInfo.getINTERFACEKEY() != null && oResultDetailInfo.getINTERFACEKEY().isEmpty() == false ) {
			// LOT({0})의 해당 공정에 대한 실적이 ERP 실적보고가 완료되어 공정취소를 진행할 수 없습니다.
			throw new CustomException("LOT-018", new Object[] {lotInfo.getKeyLOTID()});
		}
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
