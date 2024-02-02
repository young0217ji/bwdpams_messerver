package mes.eis.common;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import kr.co.mesframe.util.StringUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.transaction.RecipeService;
import mes.master.data.DURABLEINFORMATION;
import mes.master.data.EQUIPMENT;
import mes.material.data.MATERIALSTOCK;
import mes.util.NameGenerator;
import mes.lot.data.LOTCONSUMABLERATIOHISTORY;

/**
 * @author hslee
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class EISCommonUtil
{
	/**
	 * 장비-ScanID기준으로 진행 가능한 Lot정보를 Return한다.
	 * @param plantID
	 * @param equipmentID
	 * @param scanID
	 * @param inputMode
	 * @param actionType
	 * @return SCANIDTYPE, PLANTID, WORKORDER, LOTID, PROCESSID, PROCESSSEQUENCE, ROUTERELATIONSEQUENCE, EQUIPMENTID
	 */
	public HashMap<String, String> getMappingLotInfo(String plantID, String equipmentID, String scanID, String inputMode, String actionType)
	{
		String sSQL = "";
		HashMap bindMap = new HashMap();
		List resultList;
		LinkedCaseInsensitiveMap resultMap;		
		HashMap<String, String> returnMap = new HashMap<String, String>();		
		
		/* MES LOTID - MATERIALLOTID 매핑정보 확인..
		 * 1. DURABLEINFORMATION에 등록된 RFID 코드 확인 후 등록된 RFID코드가 있을경우..
		 *     --> 해당 LOTID가 장비에서 진행중인 WORKORDER에 매핑된 LOTID인 경우 해당 LOTID로 공정 진행.
		 * 2. LOTINFORMATION에 등록된 MATERIALLOTID가 있는경우 해당 LOTID로 공정 진행. 
		 * 위 2가지에 해당하는 LOTID가 없을경우 Message에 올라온 장바에 해당하는 Workorder로 진행 가능한 LOTID중 첫번째 LOT을 가져와 작업 진행 한다.
		 */
		sSQL = "";
		// 1. check RFID Mapping LOT
		sSQL += " SELECT A.DURABLEID AS SCANID                                                                    \n";
		sSQL += " 	 , a.LOTID                                                                                   \n";
		sSQL += " 	 , b.MATERIALLOTID                                                                           \n";
		sSQL += " 	 , b.LOTSTATE                                                                                \n";
		sSQL += " 	 , 1 AS SCANIDTYPE                                                                           \n";
		sSQL += " FROM DURABLEINFORMATION A  WITH(NOLOCK)                                                         \n";
		sSQL += " INNER JOIN LOTINFORMATION B WITH(NOLOCK) ON A.LOTID = B.LOTID                                   \n";
		sSQL += " WHERE 1=1                                                                                       \n";
		sSQL += "   AND A.DURABLEID = :SCANID                                                                     \n";
		//2. check scanID in RFID 
		sSQL += " UNION ALL                                                                                       \n";
		sSQL += " SELECT A.DURABLEID AS SCANID                                                                    \n";
		sSQL += " 	 , '' AS LOTID                                                                               \n";
		sSQL += " 	 , '' AS MATERIALLOTID                                                                       \n";
		sSQL += " 	 , '' AS LOTSTATE                                                                            \n";
		sSQL += " 	 , 2 AS SCANIDTYPE                                                                           \n";
		sSQL += " FROM DURABLEINFORMATION A  WITH(NOLOCK)                                                        \n";
		sSQL += " WHERE 1=1                                                                                      \n";
		sSQL += "   AND A.DURABLEID = :SCANID                                                                    \n";
		//3. check scanID Mappin LOT
		sSQL += " UNION ALL                                                                                              \n";
		sSQL += " SELECT B.MATERIALLOTID AS SCANID                                                                       \n";
		sSQL += " 	 , b.LOTID                                                                                           \n";
		sSQL += " 	 , b.MATERIALLOTID                                                                                   \n";
		sSQL += " 	 , b.LOTSTATE                                                                                        \n";
		sSQL += " 	 , 3 AS SCANIDTYPE                                                                                   \n";
		sSQL += " FROM EQUIPMENT A WITH(NOLOCK)                                                                          \n";
		sSQL += " INNER JOIN LOTINFORMATION B WITH(NOLOCK) ON A.PLANTID = B.PLANTID AND A.CURRENTWORKORDER = B.WORKORDER \n";
		sSQL += " WHERE 1=1                                                                                              \n";
		sSQL += "   AND A.PLANTID = :PLANTID                                                                             \n";
		sSQL += "   AND A.EQUIPMENTID = :EQUIPMENTID                                                                     \n";
		sSQL += "   AND ISNULL(A.CURRENTWORKORDER, '') != ''                                                             \n";
		sSQL += "   AND ISNULL(B.MATERIALLOTID, '') = :SCANID                                                            \n";
		sSQL += " ORDER BY SCANIDTYPE                                                                                    \n";
		
		bindMap.clear();
		bindMap.put("PLANTID", plantID);
		bindMap.put("EQUIPMENTID", equipmentID);
		bindMap.put("SCANID", scanID);
		
		resultList = (List) SqlMesTemplate.queryForList(sSQL, bindMap);
		
		if(resultList.equals(null) || resultList.size() <= 0)  // 매핑된 LOTID가 없는 ScanID(BARCODE Type으로 신규 Start공정 진행.)
		{
			//ScanID와 매핑된 LotID가 없는 경우 장비에 매핑된 Workorder중 진행 가능한 첫번째 LotID를 가져온다.
			returnMap = getProcessStartLotInfo(plantID, equipmentID, "4"); //LOTID와 매핑된 정보가 없는 BARCODE.
			
		}
		else
		{
			resultMap = (LinkedCaseInsensitiveMap)resultList.get(0); //<-- 기존 작업 진행중인 Lot정보.

			String sScanIDType = ConvertUtil.Object2String(resultMap.get("SCANIDTYPE"));
			String sLotID = ConvertUtil.Object2String(resultMap.get("LOTID"));
			String sLotState = ConvertUtil.Object2String(resultMap.get("LOTSTATE"));
			
			if(sScanIDType.equalsIgnoreCase("1"))  //매핑된 LOTID가 있는 RFID.
			{
				if(sLotState.equals(Constant.LOTSTATE_RELEASED))
				{
					//기존 Lot정보를 가져와 return한다.
					returnMap = getLotInfo(plantID, equipmentID, sLotID, inputMode, actionType);
					returnMap.put("SCANIDTYPE", sScanIDType);
				}
				else //LotState가 Released가 아닌경우 신규Lot매핑. scanIDType='2'와 동일하게 처리.
				{
					returnMap = getProcessStartLotInfo(plantID, equipmentID, "2");
				}
			}
			else if (sScanIDType.equalsIgnoreCase("2"))  //매핑된 LOTID가 없는 RFID.
			{
				returnMap = getProcessStartLotInfo(plantID, equipmentID, sScanIDType);
			}
			else //LOTID와 매핑되어 있는 BARCODE 
			{
				//기존 Lot정보를 가져와 return한다.
				returnMap = getLotInfo(plantID, equipmentID, sLotID, inputMode, actionType);
				returnMap.put("SCANIDTYPE", sScanIDType);
			}
		}
		return returnMap;
	}
	
	/**
	 * LOTID에 매핑된 ScanID가 없는경우 - 해당 장비에 매핑된 Workorder로 진행 가능한 첫번째 LOTID를 return 한다. 
	 * @param plantID
	 * @param equipmentID
	 * @param scanID
	 * @return SCANIDTYPE, PLANTID, WORKORDER, LOTID, PROCESSID, PROCESSSEQUENCE, ROUTERELATIONSEQUENCE, EQUIPMENTID
	 */
	public HashMap<String, String> getProcessStartLotInfo(String plantID, String equipmentID, String scanIDType)
	{
		String sSQL = "";
		HashMap bindMap = new HashMap();
		List resultList;
		LinkedCaseInsensitiveMap resultMap;		
		HashMap<String, String> returnMap = new HashMap<String, String>();

		sSQL = "";
		sSQL += " SELECT ROW_NUMBER() OVER(PARTITION BY D.PROCESSSEQUENCE, C.LOTID  ORDER BY (SELECT 1)) AS SEQ \n";
		sSQL += " 	 , A.PLANTID                                                                             \n";
		sSQL += " 	 , C.PRODUCTID                                                                           \n";
		sSQL += " 	 , A.CURRENTWORKORDER AS WORKORDER                                                       \n";
		sSQL += " 	 , C.LOTID                                                                               \n";
		sSQL += " 	 , D.PROCESSID                                                                           \n";
		sSQL += " 	 , D.PROCESSNAME                                                                         \n";
		sSQL += " 	 , D.INPUTMODE                                                                           \n";
		sSQL += " 	 , D.ACTIONTYPE                                                                          \n";
		sSQL += " 	 , D.CURRENTFLAG                                                                         \n";
		sSQL += " 	 , D.PROCESSSEQUENCE                                                                     \n";
		sSQL += " 	 , D.PROCESSPRINTINDEX                                                                   \n";
		sSQL += " 	 , D.RECIPEPRINTINDEX                                                                    \n";
		sSQL += " 	 , D.ROUTERELATIONSEQUENCE                                                               \n";
		sSQL += " 	 , D.ORDERINDEX                                                                          \n";
		sSQL += " 	 , A.EQUIPMENTID                                                                         \n";
		sSQL += " 	 , C.MATERIALLOTID                                                                       \n";
		sSQL += " FROM EQUIPMENT A WITH(NOLOCK)                                                                            \n";
		sSQL += " INNER JOIN AVAILABLEEQUIPMENT B WITH(NOLOCK) ON A.PLANTID = B.PLANTID AND A.EQUIPMENTID = B.EQUIPMENTID  \n";
		sSQL += " INNER JOIN LOTINFORMATION C WITH(NOLOCK) ON A.PLANTID = C.PLANTID AND A.CURRENTWORKORDER = C.WORKORDER   \n";
		sSQL += " INNER JOIN VIEWLOTTRACKING D WITH(NOLOCK) ON B.PROCESSID = D.PROCESSID AND C.LOTID = D.LOTID             \n";
		sSQL += " WHERE 1=1                                                                                   \n";
		sSQL += "   AND A.PLANTID = :PLANTID                                                                  \n";
		sSQL += "   AND A.EQUIPMENTID = :EQUIPMENTID                                                          \n";
		sSQL += "   AND C.PROCESSROUTETYPE = 'Main'                                                           \n";
		sSQL += "   AND D.INPUTMODE = 'OPERATION'                                                             \n";
		sSQL += "   AND D.ACTIONTYPE = 'START'                                                                \n";
		sSQL += "   AND D.CURRENTFLAG = 'Y'                                                                   \n";
		sSQL += " ORDER BY SEQ, C.LOTID, D.PROCESSSEQUENCE                                                    \n";
		
		bindMap.clear();
		bindMap.put("PLANTID", plantID);
		bindMap.put("EQUIPMENTID", equipmentID);
		
		resultList = (List) SqlMesTemplate.queryForList(sSQL, bindMap);
		resultMap = (LinkedCaseInsensitiveMap)resultList.get(0); //<-- 작업 진행 가능 첫번째 LOT정보.

		returnMap.put("SCANIDTYPE", scanIDType); //기존 매핑된 정보가 없는 ScanID..
		returnMap.put("PLANTID", plantID);
		returnMap.put("WORKORDER", ConvertUtil.Object2String(resultMap.get("WORKORDER")));
		returnMap.put("LOTID", ConvertUtil.Object2String(resultMap.get("LOTID")));
		returnMap.put("PROCESSID", ConvertUtil.Object2String(resultMap.get("PROCESSID")));
		returnMap.put("PROCESSSEQUENCE", ConvertUtil.Object2String(resultMap.get("PROCESSSEQUENCE")));
		returnMap.put("ROUTERELATIONSEQUENCE", ConvertUtil.Object2String(resultMap.get("ROUTERELATIONSEQUENCE")));
		returnMap.put("EQUIPMENTID", equipmentID);

		return returnMap;
	}
	
	public HashMap<String, String> getLotInfo(String plantID, String equipmentID, String lotid, String inputMode, String actionType)
	{
		String sSQL = "";
		HashMap bindMap = new HashMap();
		List resultList;
		LinkedCaseInsensitiveMap resultMap;		
		HashMap<String, String> returnMap = new HashMap<String, String>();
		
		sSQL += " SELECT A.PLANTID                                                                                        \n";
		sSQL += " 	 , A.CURRENTWORKORDER AS WORKORDER                                                                   \n";
		sSQL += " 	 , D.LOTID                                                                                           \n";
		sSQL += " 	 , D.PROCESSID                                                                                       \n";
		sSQL += " 	 , D.PROCESSSEQUENCE                                                                                 \n";
		sSQL += " 	 , D.ROUTERELATIONSEQUENCE                                                                           \n";
		sSQL += " 	 , A.EQUIPMENTID                                                                                     \n";
		sSQL += " FROM EQUIPMENT A WITH(NOLOCK)                                                                           \n";
		sSQL += " INNER JOIN AVAILABLEEQUIPMENT B WITH(NOLOCK) ON A.PLANTID = B.PLANTID AND A.EQUIPMENTID = B.EQUIPMENTID \n";
		sSQL += " INNER JOIN LOTINFORMATION C WITH(NOLOCK) ON A.PLANTID = C.PLANTID AND A.CURRENTWORKORDER = C.WORKORDER  \n";
		sSQL += " INNER JOIN VIEWLOTTRACKING D WITH(NOLOCK) ON B.PROCESSID = D.PROCESSID AND C.LOTID = D.LOTID AND CURRENTFLAG = 'Y' \n";
		sSQL += " WHERE 1=1                                                                                               \n";
		sSQL += "   AND D.LOTID = :LOTID                                                                                  \n";
		sSQL += " GROUP BY A.PLANTID                                                                                      \n";
		sSQL += " 		, A.CURRENTWORKORDER 			                                                                 \n";
		sSQL += " 		, D.LOTID                                                                                        \n";
		sSQL += " 		, D.PROCESSID                                                                                    \n";
		sSQL += " 		, D.PROCESSSEQUENCE                                                                              \n";
		sSQL += " 		, D.ROUTERELATIONSEQUENCE                                                                        \n";
		sSQL += " 		, A.EQUIPMENTID                                                                                  \n";
		
		bindMap.clear();
		bindMap.put("PLANTID", plantID);
		bindMap.put("LOTID", lotid);
		bindMap.put("EQUIPMENTID", equipmentID);
		
		resultList = (List) SqlMesTemplate.queryForList(sSQL, bindMap);
		
		if(resultList.size() <= 0)
		{
			// 해당 장비({0})에서 진행 가능한 Lot이 존재하지 않습니다.
			throw new CustomException("LOT-016", new Object[] {equipmentID});
		}
			
		resultMap = (LinkedCaseInsensitiveMap)resultList.get(0); 

		returnMap.put("PLANTID", plantID);
		returnMap.put("WORKORDER", ConvertUtil.Object2String(resultMap.get("WORKORDER")));
		returnMap.put("LOTID", ConvertUtil.Object2String(resultMap.get("LOTID")));
		returnMap.put("PROCESSID", ConvertUtil.Object2String(resultMap.get("PROCESSID")));
		returnMap.put("PROCESSSEQUENCE", ConvertUtil.Object2String(resultMap.get("PROCESSSEQUENCE")));
		returnMap.put("ROUTERELATIONSEQUENCE", ConvertUtil.Object2String(resultMap.get("ROUTERELATIONSEQUENCE")));
		returnMap.put("EQUIPMENTID", equipmentID);
		
		return returnMap;		
	}
	
	public void setMaterial(String plantID, String lotID, String processRouteID, String processID, String processSequence, Long routeRelationSequence, String actionType, String scanID)
	{
		String materialVendor = "";
		String materialLotID = "";
		String materialDate = "";
		String materialSequence = "";
		String materialSpecialCode = "";
		
		//ScanID가 몇자리로 올라올 지 미확인..for문 돌면서 순서대로 갯수 만큼만 등록 한다.(2023.09.20)
		if(scanID.contains("*"))
		{
			String[] listScanID = scanID.split("\\*");
			
			for(int j = 0; j < listScanID.length; j++)
			{
				if(j == 0) {materialVendor = ConvertUtil.Object2String(listScanID[j]);}
				if(j == 1) {materialLotID = ConvertUtil.Object2String(listScanID[j]);}
				if(j == 2) {materialDate = ConvertUtil.Object2String(listScanID[j]);}
				if(j == 3) {materialSequence = ConvertUtil.Object2String(listScanID[j]);}
				if(j == 4) {materialSpecialCode = ConvertUtil.Object2String(listScanID[j]);}
			}
		}
		
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( processRouteID );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( ConvertUtil.String2Long(processSequence) );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( actionType );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);

		//Set LOTCONSUMABLERATIOHISTORY
		LOTCONSUMABLERATIOHISTORY consumableInfo = new LOTCONSUMABLERATIOHISTORY();		
		consumableInfo.setKeyLOTID(lotID);
		consumableInfo.setKeyTIMEKEY(viewLotInfo.getKeyTIMEKEY());
		
		if(ConvertUtil.isEmptyForTrim(materialSequence))
			consumableInfo.setKeyCONSUMABLESEQUENCE("1");
		else
			consumableInfo.setKeyCONSUMABLESEQUENCE(ConvertUtil.Object2String(materialSequence));
			
		
		List<Object> listconsumableInfo = (List<Object>) consumableInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		if ( listconsumableInfo.size() <= 0 )
		{
			consumableInfo.setCONSUMABLEID(scanID);
			consumableInfo.setCONSUMABLELOTID(ConvertUtil.Object2String(materialLotID));
			consumableInfo.setCONSUMABLEQUANTITY(ConvertUtil.Object2Double(1));
			
			consumableInfo.excuteDML(SqlConstant.DML_INSERT);
		}
		
		//Set MATERIALSTOCK
		MATERIALSTOCK materialStock = new MATERIALSTOCK();
		
		// Key Value Setting
		materialStock.setKeyPLANTID(plantID);
		
		if(ConvertUtil.isEmptyForTrim(materialDate))
			materialStock.setKeyYYYYMM(DateUtil.getCurrentTime(DateUtil.FORMAT_SIMPLE_DAY));
		else
			materialStock.setKeyYYYYMM(ConvertUtil.Object2String(materialDate));		
		
		materialStock.setKeyWAREHOUSEID("MESSTOCK");
		materialStock.setKeyMATERIALTYPE("Materials");
		materialStock.setKeyMATERIALID(ConvertUtil.Object2String(scanID));
		
		if(!ConvertUtil.isEmptyForTrim(materialLotID))
			materialStock.setKeyMATERIALLOTID(ConvertUtil.Object2String(materialLotID));
		else
			materialStock.setKeyMATERIALLOTID(ConvertUtil.Object2String(scanID));
        
        // Data Value Setting
		List<Object> listmaterialStock = (List<Object>) materialStock.excuteDML(SqlConstant.DML_SELECTLIST);
		
		if ( listmaterialStock.size() <= 0 )
		{
			materialStock.setRECEIPTDATE(DateUtil.getCurrentTime(DateUtil.FORMAT_SIMPLE_DAY));
			materialStock.setVENDOR(ConvertUtil.Object2String(materialVendor));			
			materialStock.setLASTEVENTTIMEKEY(DateUtil.getCurrentEventTimeKey());
			materialStock.setOPENINGQTY(ConvertUtil.String2Double("1"));
			materialStock.setINQTY(ConvertUtil.String2Double("1"));
			materialStock.setBONUSQTY(ConvertUtil.String2Double("0"));
			materialStock.setCONSUMEQTY(ConvertUtil.String2Double("0"));
			materialStock.setSCRAPQTY(ConvertUtil.String2Double("0"));
			materialStock.setOUTQTY(ConvertUtil.String2Double("0"));
			materialStock.setHOLDQTY(ConvertUtil.String2Double("0"));
			materialStock.setSTOCKQTY(ConvertUtil.String2Double("1"));
			
			materialStock.excuteDML(SqlConstant.DML_INSERT);
		}			
	}
	
	public String getIPCCurrentUser(String plantID, String equipmentID)
	{
		String currentUser = "";

		EQUIPMENT equipment = new EQUIPMENT();
		
		equipment.setKeyPLANTID(plantID);
		equipment.setKeyEQUIPMENTID(equipmentID);
		
		equipment.excuteDML(SqlConstant.DML_SELECTROW);
		
		if(equipment != null)
			currentUser = ConvertUtil.Object2String(equipment.getCURRENTUSERID());
		
		return currentUser;
	}
}
