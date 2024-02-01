package mes.time.biz;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.lot.custom.LotCustomService;
import mes.lot.data.LOTINFORMATION;
import mes.lot.lotdata.LotCreateInfo;
import mes.master.data.AREA;
import mes.master.data.DY_PROCWORKORDER;
import mes.util.NameGenerator;

/*
 ****************************************************************************
 *  PACKAGE : mes.time.biz;
 *  NAME    : EmLoadingQISDataTimer.java
 *  TYPE    : JAVA
 *  DESCRIPTION : 2023-09-25. WorkOrder의 Plan수량만큼 부족한 LotID를 자동 생성한다.
 *	MODIFY	: 2023-09-25. 이홍상
 ****************************************************************************
 */

public class AutoCreateLot implements Job 
{
	private static final transient Logger logger = LoggerFactory.getLogger(AutoCreateLot.class);
		
	public void execute(JobExecutionContext arg0) throws JobExecutionException 
	{	
		try
		{
			MESFrameProxy.getTxManager().begin();
			executeCreateLot();
			MESFrameProxy.getTxManager().lastCommitForce();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			MESFrameProxy.getTxManager().lastRollbackForce();
		}
	}
	
	public void executeCreateLot() throws Exception
	{	
		if(System.getProperty("mode").equalsIgnoreCase("DEV"))
			return;
		
		logger.info("########## executeAutoCreateLot Start ##########");
		
		TxnInfo txnInfo = new TxnInfo();
		NameGenerator nameGenerator = new NameGenerator();
		LotCustomService lotCustomService = new LotCustomService();
		
		String sSQL = "";
		HashMap bindMap = new HashMap();
		List resultList;
		LinkedCaseInsensitiveMap resultMap;		
		HashMap<String, String> returnMap = new HashMap<String, String>();
		
		sSQL = "";
		sSQL += " SELECT A.PLANTID                                               \n";
		sSQL += " 	 , A.WORKORDERID AS WORKORDER                                \n";
		sSQL += " 	 , A.PLANQUANTITY                                            \n";
		sSQL += " 	 , ISNULL(C.LOTCNT, 0) AS LOTCNT                             \n";
		sSQL += " 	 , A.PLANQUANTITY - ISNULL(C.LOTCNT, 0) AS MAKELOTCOUNT      \n";
		sSQL += " FROM WORKORDER A                                               \n";
		//[2024.01.24-이홍상] WORKORDER리스트만 가져오고, 확정 유무는 ERROR체크 부분에서 확인한다.
//		sSQL += " INNER JOIN MODELINGCONFIRM B ON A.PLANTID = B.PLANTID          \n";
//		sSQL += " 							AND A.PRODUCTID = B.PRODUCTID        \n";
//		sSQL += " 							and A.ERPROUTINGID = B.CONDITIONTYPE \n";
//		sSQL += " 							and A.ERPROUTINGCNT = B.CONDITIONID  \n";
		sSQL += " LEFT OUTER JOIN (SELECT WORKORDER, COUNT(LOTID) AS LOTCNT      \n";
		sSQL += " 				 FROM LOTINFORMATION                             \n";
		sSQL += " 				 GROUP BY WORKORDER) C ON A.WORKORDERID = C.WORKORDER  \n";
		sSQL += " WHERE 1=1                                                      \n";
		sSQL += " AND A.PLANQUANTITY > ISNULL(C.LOTCNT, 0)                       \n";
		sSQL += " GROUP BY A.PLANTID, A.WORKORDERID, A.PLANQUANTITY, C.LOTCNT    \n";
		sSQL += " ORDER BY A.WORKORDERID                                         \n";
		
		resultList = (List) SqlMesTemplate.queryForList(sSQL, bindMap);

		if(resultList.equals(null) || resultList.size() <= 0)
		{
			//이력 테이블 생성 필요???
			
			logger.info("########## AutoCreateLot Job Count(Workorder) : " + ConvertUtil.Object2String(resultList.size()) + " ##########");
			logger.info("########## executeAutoCreateLot End ##########");

			return;
		}
		
		logger.info("########## AutoCreateLot Job Count(Workorder) : " + ConvertUtil.Object2String(resultList.size()) + " ##########");

		List listMakeLotInfo;
		LinkedCaseInsensitiveMap mapMakeLotInfo;
		
		for (int i = 0; i < resultList.size(); i++)
		{
			resultMap = (LinkedCaseInsensitiveMap)resultList.get(i);
			
			String plantID = ConvertUtil.Object2String(resultMap.get("PLANTID"));
			String workOrder = ConvertUtil.Object2String(resultMap.get("WORKORDER"));
			int makeLotCount = ConvertUtil.Object2Int4Zero(resultMap.get("MAKELOTCOUNT"));
			
			// lot 생성 시 누락된 기준정보 체크
			if(lotCustomService.checkAutoLotCreate(plantID, workOrder) == false) 
			{
				continue;
			}
			
			//PROCWORKORDER Data 생성.
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
//			txnInfo.setTxnUser("System");
			createProcWorkorder(plantID, workOrder, txnInfo);
			
			sSQL = "";
			sSQL += " SELECT A.PLANTID                                                                                                          \n";
			sSQL += " 	 , A.PRODUCTID                                                                                                         \n";
			sSQL += " 	 , C.ERPROUTINGID                                                                                                      \n";
			sSQL += " 	 , C.ERPROUTINGCNT                                                                                                     \n";
			sSQL += " 	 , B.PROCESSROUTEID                                                                                                    \n";
			sSQL += " 	 , B.PROCESSID                                                                                                         \n";
			sSQL += " 	 , B.PROCESSSEQUENCE                                                                                                   \n";
			sSQL += " 	 , C.WORKCENTER                                                                                                        \n";
			sSQL += " FROM TPPOLICY A                                                                                                           \n";
			sSQL += " INNER JOIN ROCOMPOSITION B ON A.PLANTID = B.PLANTID                                                                       \n";
			sSQL += " 						  AND A.PROCESSROUTEID = B.PROCESSROUTEID                                                          \n";
			sSQL += " INNER JOIN (SELECT A.PLANTID, A.PRODUCTID, A.PRODUCTORDERID, A.WORKORDERID, B.ERPROUTINGID, B.ERPROUTINGCNT, c.WORKCENTER \n";
			sSQL += " 			FROM WORKORDER A                                                                                               \n";
			sSQL += " 			INNER JOIN IF_POHEADER_R B ON A.PLANTID = B.PLANTID                                                            \n";
			sSQL += " 									  AND A.PRODUCTORDERID = B.PRODUCTORDERID                                              \n";
			sSQL += " 			INNER JOIN IF_POOPERATION_R c ON b.PLANTID = c.PLANTID                                                         \n";
			sSQL += " 										 AND b.PRODUCTORDERID = c.PRODUCTORDERID) C ON A.PLANTID = C.PLANTID               \n";
			sSQL += " 										  											AND A.PRODUCTID = C.PRODUCTID          \n";
			sSQL += " 										  											AND A.CONDITIONTYPE = C.ERPROUTINGID   \n";
			sSQL += " 										  											AND A.CONDITIONID = C.ERPROUTINGCNT    \n";
			sSQL += " WHERE 1=1                                                                                                                 \n";
			sSQL += "   AND A.PLANTID = :PLANTID                                                                                                \n";
			sSQL += "   AND C.WORKORDERID = :WORKORDER                                                                                          \n";
			sSQL += " ORDER BY C.ERPROUTINGID, C.ERPROUTINGCNT, B.PROCESSROUTEID, B.PROCESSSEQUENCE                                             \n";
			
			bindMap.clear();
			bindMap.put("PLANTID", plantID);
			bindMap.put("WORKORDER", workOrder);
			
			listMakeLotInfo = (List) SqlMesTemplate.queryForList(sSQL, bindMap);
			
			if(listMakeLotInfo.size() <= 0)
				continue;

			//첫번째 시작 공정 기준으로 LOT생성 한다.
			mapMakeLotInfo = (LinkedCaseInsensitiveMap)listMakeLotInfo.get(0); 
			
			String productID = ConvertUtil.Object2String(mapMakeLotInfo.get("PRODUCTID"));			
			String erpRoutingID = ConvertUtil.Object2String(mapMakeLotInfo.get("ERPROUTINGID"));			
			String erpRoutingCnt = ConvertUtil.Object2String(mapMakeLotInfo.get("ERPROUTINGCNT"));			
			String processRouteID = ConvertUtil.Object2String(mapMakeLotInfo.get("PROCESSROUTEID"));
			String processID = ConvertUtil.Object2String(mapMakeLotInfo.get("PROCESSID"));
			String processSeq = ConvertUtil.Object2String(mapMakeLotInfo.get("PROCESSSEQUENCE"));
			String workCenter = ConvertUtil.Object2String(mapMakeLotInfo.get("WORKCENTER"));
			
			//WorkOrder의 계획 수량보다 부족한 Lot수량만큼 추가 생성.
			for(int j = 0; j < makeLotCount; j++)
			{
				txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
				
				// WorkCenter 정보로 Line의 대표문자 조회
				AREA oAreaInfo = new AREA();
				oAreaInfo.setKeyPLANTID(plantID);
				oAreaInfo.setKeyAREAID(workCenter);
				oAreaInfo = (AREA) oAreaInfo.excuteDML(SqlConstant.DML_SELECTROW);
				
				AREA oLineAreaInfo = new AREA();
				oLineAreaInfo.setKeyPLANTID(oAreaInfo.getKeyPLANTID());
				oLineAreaInfo.setKeyAREAID(oAreaInfo.getSUPERAREAID());
				oLineAreaInfo = (AREA) oLineAreaInfo.excuteDML(SqlConstant.DML_SELECTROW);
				
				// 대표문자
				String sRepChar = oLineAreaInfo.getREPRESENTATIVECHAR();
				if ( sRepChar == null || sRepChar.isEmpty() ) {
					sRepChar = workCenter.substring(0, 1);
				}
				
				String lotID = nameGenerator.nameGenerate( plantID, "LotID", new Object[] {sRepChar} );
				
				LotCreateInfo createLotInfo = new LotCreateInfo();
				createLotInfo.setLOTID(lotID);
				createLotInfo.setPLANTID(plantID);
				createLotInfo.setWORKORDER(workOrder);
				createLotInfo.setPRODUCTID(productID);
				createLotInfo.setDEPARTMENT(workCenter);
				createLotInfo.setAREAID(workCenter);
				createLotInfo.setEQUIPMENTID("");
				createLotInfo.setSTARTQUANTITY(ConvertUtil.Object2Double(1, 0.0));
				
				createLotInfo.setBOMID("BOMID");
				createLotInfo.setBOMVERSION("00001");
				createLotInfo.setPOLICYNAME("None");
				createLotInfo.setPOLICYVALUE("None");
				createLotInfo.setCONDITIONTYPE(erpRoutingID);
				createLotInfo.setCONDITIONID(erpRoutingCnt);
				createLotInfo.setLOTTYPE("P");
				createLotInfo.setPRIORITY("5");

			    LOTINFORMATION lotInfo = lotCustomService.createLot(createLotInfo, false, txnInfo);
			}
		}
		
		logger.info("########## executeAutoCreateLot End ##########");

	}
	
	@SuppressWarnings("unchecked")
	private void createProcWorkorder(String plantID, String workOrder, TxnInfo txnInfo)
	{
		String sSQL = "";
		HashMap bindMap = new HashMap();
		List resultList;
		LinkedCaseInsensitiveMap resultMap;		
		HashMap<String, String> returnMap = new HashMap<String, String>();
		AddHistory history = new AddHistory();
		
		sSQL = "";
		sSQL += " SELECT A.PLANTID                                                                       \r\n";
//		sSQL += " 	 , :DATAKEY AS DATAKEY                                                              \r\n";
		sSQL += " 	 , A.WORKORDERID                                                                    \r\n";
//		sSQL += " 	 , NULL PLANSTARTPRIOR                                                              \r\n";
		sSQL += " 	 , A.PRODUCTID                                                                      \r\n";
		sSQL += " 	 , A.STARTORDERDATE AS PLANSTARTDATE                                                \r\n";
		sSQL += " 	 , A.STARTORDERTIME AS PLANSTARTTIME                                                \r\n";
		sSQL += " 	 , A.ENDPLANDATE AS PLANENDDATE                                                     \r\n";
		sSQL += " 	 , A.ENDORDERTIME AS PLANENDTIME                                                    \r\n";
//		sSQL += " 	 , NULL EXECUTESTARTDATE                                                            \r\n";
//		sSQL += " 	 , NULL EXECUTESTARTTIME                                                            \r\n";
//		sSQL += " 	 , NULL EXECUTEENDDATE                                                              \r\n";
//		sSQL += " 	 , NULL EXECUTEENDTIME                                                              \r\n";
		sSQL += " 	 , B.ERPOPERID AS ERPOPERID                                                         \r\n";
		sSQL += " 	 , B.WORKCENTER AS WORKCENTER                                                       \r\n";
		sSQL += " 	 , C.EQUIPMENTID                                                                    \r\n";
		sSQL += " 	 , A.PLANQUANTITY AS QUANTITY                                                       \r\n";
//		sSQL += " 	 , NULL QUANTITYPRODUCT                                                             \r\n";
//		sSQL += " 	 , NULL QUANTITYNOTOK                                                               \r\n";
//		sSQL += " 	 , NULL QUANTITYSEPARATION                                                          \r\n";
//		sSQL += " 	 , NULL CYCLETIME                                                                   \r\n";
//		sSQL += " 	 , NULL NEEDTIME                                                                    \r\n";
//		sSQL += " 	 , NULL PROCESSSTATE                                                                \r\n";
//		sSQL += " 	 , NULL ERPSTARTDATE                                                                \r\n";
//		sSQL += " 	 , NULL SCHEDULEALTERCODE                                                           \r\n";
//		sSQL += " 	 , NULL SCHEDULEALTERDESCRIPTION                                                    \r\n";
//		sSQL += " 	 , NULL ISSUE                                                                       \r\n";
//		sSQL += " 	 , NULL ROD2MATERIAL                                                                \r\n";
//		sSQL += " 	 , NULL ROD2MANUFACTURE                                                             \r\n";
//		sSQL += " 	 , NULL ROD2PLATING                                                                 \r\n";
//		sSQL += " 	 , NULL TUBEREMARK                                                                  \r\n";
//		sSQL += " 	 , NULL ROD2MOVEMENT                                                                \r\n";
//		sSQL += " 	 , NULL RODASSYREMARK                                                               \r\n";
//		sSQL += " 	 , NULL HCSCHEDULE                                                                  \r\n";
//		sSQL += " 	 , NULL CLSCHEDULE                                                                  \r\n";
//		sSQL += " 	 , NULL KLSCHEDULE                                                                  \r\n";
//		sSQL += " 	 , NULL RCSCHEDULE                                                                  \r\n";
//		sSQL += " 	 , NULL PTSCHEDULE                                                                  \r\n";
//		sSQL += " 	 , NULL PIPESCHEDULE                                                                \r\n";
//		sSQL += " 	 , NULL ASSEMBLYDATE                                                                \r\n";
//		sSQL += " 	 , NULL ROD2MOVEMENTTOOLTIP                                                         \r\n";
//		sSQL += " 	 , NULL PARTSGRREMARK                                                               \r\n";
//		sSQL += " 	 , NULL BUSHSCHEDULE                                                                \r\n";
//		sSQL += " 	 , NULL VALVESCHEDULE                                                               \r\n";
//		sSQL += " 	 , NULL NEEDTIMEONASSEMBLY                                                          \r\n";
//		sSQL += " 	 , NULL CHECK20PRODUCT                                                              \r\n";
//		sSQL += " 	 , NULL PARTSREMARK                                                                 \r\n";
//		sSQL += " 	 , NULL DESCRIPTION                                                                 \r\n";
		sSQL += " 	 , A.CREATETIME                                                                     \r\n";
		sSQL += " 	 , A.CREATEUSERID                                                                   \r\n";
		sSQL += " 	 , A.LASTUPDATETIME                                                                 \r\n";
		sSQL += " 	 , A.LASTUPDATEUSERID                                                               \r\n";
		sSQL += " 	 , A.STARTORDERDATE AS PLANPRODUCTDATE                                              \r\n";
		sSQL += " FROM WORKORDER A                                                                       \r\n";
		sSQL += " INNER JOIN WORKORDEROPERATION B ON A.PLANTID = B.PLANTID                               \r\n";
		sSQL += " 							   AND A.PRODUCTORDERID = B.PRODUCTORDERID                              \r\n";
		sSQL += " 							   AND A.WORKORDERID = B.WORKORDERID                                    \r\n";
		sSQL += " LEFT OUTER JOIN ( 	SELECT TOP 1 RESULTSEQ                                            \r\n";
		sSQL += " 						 , PLANTID                                                                \r\n";
		sSQL += " 						 , PRODUCTID                                                              \r\n";
		sSQL += " 						 , WORKCENTER                                                             \r\n";
		sSQL += " 						 , PROCESSID                                                              \r\n";
		sSQL += " 						 , EQUIPMENTID                                                            \r\n";
		sSQL += " 					FROM (                                                                      \r\n";
		sSQL += " 							SELECT 1 AS RESULTSEQ                                                   \r\n";
		sSQL += " 								 , AA.PLANTID                                                         \r\n";
		sSQL += " 								 , BB.PRODUCTID                                                       \r\n";
		sSQL += " 								 , AA.AREAID AS WORKCENTER                                            \r\n";
		sSQL += " 								 , BB.PROCESSID                                                       \r\n";
		sSQL += " 								 , BB.EQUIPMENTID                                                     \r\n";
		sSQL += " 							FROM AREA AA                                                            \r\n";
		sSQL += " 							INNER JOIN AVAILABLEEQUIPMENT BB ON AA.PLANTID = BB.PLANTID             \r\n";
		sSQL += " 															AND AA.REPRESENTATIVEPROCESSID = BB.PROCESSID           \r\n";
		sSQL += " 							  AND AA.AREATYPE = 'WORKCENTER'                                        \r\n";
		sSQL += " 							  AND BB.PRODUCTID != 'ALL'                                             \r\n";
		sSQL += " 							UNION ALL                                                               \r\n";
		sSQL += " 							SELECT 2 AS RESULTSEQ                                                   \r\n";
		sSQL += " 								 , AA.PLANTID                                                         \r\n";
		sSQL += " 								 , BB.PRODUCTID                                                       \r\n";
		sSQL += " 								 , AA.AREAID AS WORKCENTER                                            \r\n";
		sSQL += " 								 , BB.PROCESSID                                                       \r\n";
		sSQL += " 								 , BB.EQUIPMENTID                                                     \r\n";
		sSQL += " 							FROM AREA AA                                                            \r\n";
		sSQL += " 							INNER JOIN AVAILABLEEQUIPMENT BB ON AA.PLANTID = BB.PLANTID             \r\n";
		sSQL += " 															AND AA.REPRESENTATIVEPROCESSID = BB.PROCESSID           \r\n";
		sSQL += " 							  AND AA.AREATYPE = 'WORKCENTER'                                        \r\n";
		sSQL += " 							  AND AA.USEFLAG = 'YES'                                                \r\n";
		sSQL += " 							  AND BB.PRODUCTID = 'ALL'                                              \r\n";
		sSQL += " 							) A                                                                     \r\n";
		sSQL += " 					WHERE 1=1                                                                   \r\n";
		sSQL += " 					ORDER BY A.RESULTSEQ, A.EQUIPMENTID) C ON B.PLANTID = C.PLANTID             \r\n";
		sSQL += " 														  AND B.WORKCENTER = C.WORKCENTER                         \r\n";
		sSQL += " 														  AND (C.PRODUCTID = 'ALL' OR C.PRODUCTID = A.PRODUCTID)  \r\n";
		sSQL += " WHERE 1 = 1                                                                          \r\n";
		sSQL += "   AND A.PLANTID = :PLANTID                                                           \r\n";
		sSQL += "   AND A.WORKORDERID = :WORKORDERID                                                   \r\n";

		bindMap.clear();
		bindMap.put("PLANTID", plantID);
		bindMap.put("WORKORDERID", workOrder);

		resultList = (List<Object>) SqlMesTemplate.queryForList(sSQL, bindMap);
		
		if(resultList.size() <= 0)
		{
			logger.info("########## AutoCreateLot Create PROCWORKORDER : 0 ##########");

			return;
		}

		String sPLANTID = "";
		String sDATAKEY = "";
		String sWORKORDERID = "";
		String sPRODUCTID = "";
		String sPLANSTARTDATE = "";
		String sPLANSTARTTIME = "";
		String sPLANENDDATE = "";
		String sPLANENDTIME = "";
		String sERPOPERID = "";
		String sWORKCENTER = "";
		String sEQUIPMENTID = "";
		int iQUANTITY;
		Timestamp dCREATETIME;
		String sCREATEUSERID = "";
		Timestamp dLASTUPDATETIME;
		String sLASTUPDATEUSERID = "";
		String sPLANPRODUCTDATE = "";
		
		for(int i = 0; i < resultList.size(); i++)
		{
			resultMap = (LinkedCaseInsensitiveMap)resultList.get(i); 
			
			sPLANTID = ConvertUtil.Object2String(resultMap.get("PLANTID"));         
			sDATAKEY = DateUtil.getCurrentEventTimeKey();         
			sWORKORDERID = ConvertUtil.Object2String(resultMap.get("WORKORDERID"));     
			sPRODUCTID = ConvertUtil.Object2String(resultMap.get("PRODUCTID"));       
			sPLANSTARTDATE = ConvertUtil.Object2String(resultMap.get("PLANSTARTDATE"));   
			sPLANSTARTTIME = ConvertUtil.Object2String(resultMap.get("PLANSTARTTIME"));   
			sPLANENDDATE = ConvertUtil.Object2String(resultMap.get("PLANENDDATE"));     
			sPLANENDTIME = ConvertUtil.Object2String(resultMap.get("PLANENDTIME"));     
			sERPOPERID = ConvertUtil.Object2String(resultMap.get("ERPOPERID"));       
			sWORKCENTER = ConvertUtil.Object2String(resultMap.get("WORKCENTER"));      
			sEQUIPMENTID = ConvertUtil.Object2String(resultMap.get("EQUIPMENTID"));     
			iQUANTITY = ConvertUtil.Object2Int4Zero(resultMap.get("QUANTITY"));        
			dCREATETIME = ConvertUtil.convertToCIMTimeStamp(ConvertUtil.Object2String(resultMap.get("CREATETIME")));
			sCREATEUSERID = ConvertUtil.Object2String(resultMap.get("CREATEUSERID"));    
			dLASTUPDATETIME = ConvertUtil.convertToCIMTimeStamp(ConvertUtil.Object2String(resultMap.get("LASTUPDATETIME")));  
			sLASTUPDATEUSERID = ConvertUtil.Object2String(resultMap.get("LASTUPDATEUSERID"));
			sPLANPRODUCTDATE = ConvertUtil.Object2String(resultMap.get("PLANPRODUCTDATE")); 
			
			DY_PROCWORKORDER dy_procworkorder = new DY_PROCWORKORDER();
			
			dy_procworkorder.setKeyPLANTID(sPLANTID);
			dy_procworkorder.setKeyDATAKEY(sDATAKEY);
			
			dy_procworkorder.setWORKORDERID(sWORKORDERID);
			dy_procworkorder.setPRODUCTID(sPRODUCTID);
			dy_procworkorder.setPLANSTARTDATE(sPLANSTARTDATE);
			dy_procworkorder.setPLANSTARTTIME(sPLANSTARTTIME);
			dy_procworkorder.setPLANENDDATE(sPLANENDDATE);
			dy_procworkorder.setPLANENDTIME(sPLANENDTIME);
			dy_procworkorder.setERPOPERID(sERPOPERID);
			dy_procworkorder.setWORKCENTER(sWORKCENTER);
			dy_procworkorder.setEQUIPMENTID(sEQUIPMENTID);
			dy_procworkorder.setCREATETIME(dCREATETIME);
			dy_procworkorder.setCREATEUSERID(sCREATEUSERID);
			dy_procworkorder.setLASTUPDATETIME(dLASTUPDATETIME);
			dy_procworkorder.setLASTUPDATEUSERID(sLASTUPDATEUSERID);
			dy_procworkorder.setPLANPRODUCTDATE(sPLANPRODUCTDATE);
			
			dy_procworkorder.excuteDML(SqlConstant.DML_INSERT);
			
			history.addHistory(dy_procworkorder, txnInfo, SqlConstant.DML_INSERT);
		}

		logger.info("########## AutoCreateLot Create PROCWORKORDER : " + ConvertUtil.Object2String(resultList.size()) + "##########");
	}
}