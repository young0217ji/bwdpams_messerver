package mes.master.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import kr.co.mesframe.util.object.ObjectUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.CONSUMABLECONDITIONSET;
import mes.master.data.CONSUMABLEDEFINITION;
import mes.master.data.MODELINGCONFIRM;
import mes.master.data.MODELINGCONFIRMDETAIL;
import mes.master.data.RECIPEPARAMETER;
import mes.master.data.TGCPOLICY;
import mes.master.data.TPCPOLICY;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnModelingConfirm implements ObjectExecuteService 
{
	/**
	 * 제품/제품군 라우팅설정 정보를 확인하고 확정하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Document recvDoc) 
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		for (int i = 0; i < dataList.size(); i++) 
		{

			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);

			// Data 객체 생성
			MODELINGCONFIRM ModelingConfirm = new MODELINGCONFIRM();
			// Key Value Setting
			ModelingConfirm.setKeyPLANTID(dataMap.get("PLANTID"));
			ModelingConfirm.setKeyPRODUCTID(dataMap.get("PRODUCTID"));
			ModelingConfirm.setKeyBOMID(dataMap.get("BOMID"));
			ModelingConfirm.setKeyBOMVERSION(dataMap.get("BOMVERSION"));
			ModelingConfirm.setKeyPOLICYNAME(dataMap.get("POLICYNAME"));
			ModelingConfirm.setKeyPOLICYVALUE(dataMap.get("POLICYVALUE"));
			ModelingConfirm.setKeyCONDITIONTYPE(dataMap.get("CONDITIONTYPE"));
			ModelingConfirm.setKeyCONDITIONID(dataMap.get("CONDITIONID"));

			// select List 형태로 받아서
			List<Object> mapModelingConfirm = (List<Object>) ModelingConfirm.excuteDML(SqlConstant.DML_SELECTLIST);

			// Data Value Setting
			ModelingConfirm.setPOLICYTYPE(dataMap.get("POLICYTYPE"));
			ModelingConfirm.setPROCESSROUTEID(dataMap.get("PROCESSROUTEID"));
			ModelingConfirm.setCONFIRMFLAG(dataMap.get("CONFIRMFLAG"));
			ModelingConfirm.setDESCRIPTION(dataMap.get("DESCRIPTION"));
			ModelingConfirm.setLASTUPDATEUSERID(txnInfo.getTxnUser());
			ModelingConfirm.setLASTUPDATETIME(txnInfo.getEventTime());

			if (dataMap.get("_ROWSTATUS").equals("U")) 
			{
				// ModelingConfirm에 있는지 조회 후 있으면 Update 없으면 Insert
				if (mapModelingConfirm.size() > 0) 
				{
					ModelingConfirm.excuteDML(SqlConstant.DML_UPDATE);
				} else 
				{
					ModelingConfirm.excuteDML(SqlConstant.DML_INSERT);
				}
			
				// Modeling Confirm Detail 정보 삭제후 다시 생성
				ModelingConfirmDetailDelete(ModelingConfirm);
				if(Constant.FLAG_YESNO_YES.equals(dataMap.get("CONFIRMFLAG")))
				{
					// ActiveState Check
					ModelingDataActiveStateCheck(ModelingConfirm);
					
					ModelingConfirmDetailCreate(ModelingConfirm,txnInfo);
				}
			}

			// History 기록이 필요한 경우 사용
			 AddHistory history = new AddHistory();
			 history.addHistory(ModelingConfirm, txnInfo, dataMap.get("_ROWSTATUS"));

		}

		return recvDoc;
	}

	// ProductID, BOM, ProcessRoute, Process 의 ActiveState가 Active인지 확인한다. 
	@SuppressWarnings("unchecked")
	public void ModelingDataActiveStateCheck(MODELINGCONFIRM oData) 
	{
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.clear();
		bindMap.put("PLANTID", oData.getKeyPLANTID());
		bindMap.put("PRODUCTID", oData.getKeyPRODUCTID());
		bindMap.put("BOMID", oData.getKeyBOMID());
		bindMap.put("BOMVERSION", oData.getKeyBOMVERSION());
		bindMap.put("PROCESSROUTEID", oData.getPROCESSROUTEID());

		// 1. ProductID ActiveStae Check
		String Sql = " SELECT PRODUCTID, ACTIVESTATE "
				   + " FROM   PRODUCTDEFINITION "
				   + " WHERE  PLANTID = :PLANTID "		
				   + " AND    PRODUCTID = :PRODUCTID ";		
		List<Object> DataList = SqlMesTemplate.queryForList(Sql, bindMap);
		String sMessage = "ProductID (" + oData.getKeyPRODUCTID() + ")";
		if (DataList.size()>0)
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(0);
			if (!Constant.ACTIVESTATE_ACTIVE.equals(dataMap.get("ACTIVESTATE")))
			{
				throw new CustomException("CM-102", new Object[] { sMessage, Constant.ACTIVESTATE_ACTIVE });
			}
		}
		else
		{
    		throw new CustomException("CM-101", new Object[] { sMessage });
		}

//		// 2. BOM ActiveStae Check
//		Sql = "     SELECT PRODUCTID, BOMID, BOMVERSION, ACTIVESTATE "
//				+ " FROM   BOMDEFINITION "
//				+ " WHERE  PLANTID = :PLANTID "		
//				+ " AND    PRODUCTID = :PRODUCTID "
//			    + " AND    BOMID = :BOMID "
//			    + " AND    BOMVERSION = :BOMVERSION ";
//		DataList = SqlMesTemplate.queryForList(Sql, bindMap);
//		sMessage = "BOMData (" + oData.getKeyPRODUCTID() + "," + oData.getKeyBOMID() + "," + oData.getKeyBOMVERSION() + ")";
//		if (DataList.size()>0)
//		{
//			HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(0);
//			if (!Constant.ACTIVESTATE_ACTIVE.equals(dataMap.get("ACTIVESTATE")))
//			{
//				throw new CustomException("CM-102", new Object[] { sMessage, Constant.ACTIVESTATE_ACTIVE });
//			}
//		}
//		else
//		{
//			throw new CustomException("CM-101", new Object[] { sMessage });
//		}

		// 3. ProcessRoute ActiveStae Check
		Sql = "     SELECT PROCESSROUTEID, ACTIVESTATE "
				+ " FROM   PROCESSROUTE "
				+ " WHERE  PLANTID = :PLANTID "		
				+ " AND    PROCESSROUTEID = :PROCESSROUTEID ";
		DataList = SqlMesTemplate.queryForList(Sql, bindMap);
		sMessage = "ProcessRouteID (" + oData.getPROCESSROUTEID() + ")";
		if (DataList.size()>0)
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(0);
			if (!Constant.ACTIVESTATE_ACTIVE.equals(dataMap.get("ACTIVESTATE")))
			{
				throw new CustomException("CM-102", new Object[] { sMessage, Constant.ACTIVESTATE_ACTIVE });
			}
		}
		else
		{
			throw new CustomException("CM-101", new Object[] { sMessage });
		}

		// 4. Process ActiveStae Check
		Sql = "     SELECT RO.PROCESSID , P.ACTIVESTATE "
				+ " FROM   ROCOMPOSITION RO "
				+ "        LEFT OUTER JOIN PROCESSDEFINITION P ON P.PLANTID = RO.PLANTID AND P.PROCESSID = RO.PROCESSID "
				+ " WHERE  RO.PLANTID = :PLANTID "		
				+ " AND    RO.PROCESSROUTEID = :PROCESSROUTEID ";
		DataList = SqlMesTemplate.queryForList(Sql, bindMap);
		sMessage = "RouteCompositionData (" + oData.getPROCESSROUTEID() + ")";
		if (DataList.size()>0)
		{
			for (int i=0; i<DataList.size(); i++)
			{
				HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(i);
				sMessage = " ProcessID (" + dataMap.get("PROCESSID") + ")";
				if (dataMap.get("ACTIVESTATE")==null)
				{
					throw new CustomException("CM-101", new Object[] { sMessage });
				}
				else
				{
					if (!Constant.ACTIVESTATE_ACTIVE.equals(dataMap.get("ACTIVESTATE")))
					{
						throw new CustomException("CM-102", new Object[] { sMessage, Constant.ACTIVESTATE_ACTIVE });
					}
				}
			}
		}
		else
		{
			throw new CustomException("CM-101", new Object[] { sMessage });
		}
		
	}
	
	// 관련 Detail을 삭제합니다.
	public void ModelingConfirmDetailDelete(MODELINGCONFIRM DelData) 
	{
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.clear();

		bindMap.put("PLANTID", DelData.getKeyPLANTID());
		bindMap.put("PRODUCTID", DelData.getKeyPRODUCTID());
		bindMap.put("BOMID", DelData.getKeyBOMID());
		bindMap.put("BOMVERSION", DelData.getKeyBOMVERSION());
		bindMap.put("POLICYNAME", DelData.getKeyPOLICYNAME());
		bindMap.put("POLICYVALUE", DelData.getKeyPOLICYVALUE());
		bindMap.put("CONDITIONTYPE", DelData.getKeyCONDITIONTYPE());
		bindMap.put("CONDITIONID", DelData.getKeyCONDITIONID());
		bindMap.put("CONDITIONTYPE", DelData.getKeyCONDITIONTYPE());

		String Sql = " DELETE FROM MODELINGCONFIRMDETAIL " 
				+ " WHERE 1 = 1"
				+ " AND PLANTID = :PLANTID"
				+ " AND PRODUCTID = :PRODUCTID" 
				+ " AND BOMID = :BOMID"
				+ " AND BOMVERSION = :BOMVERSION"
				+ " AND POLICYNAME = :POLICYNAME"
				+ " AND POLICYVALUE = :POLICYVALUE"
				+ " AND CONDITIONTYPE = :CONDITIONTYPE"
				+ " AND CONDITIONID = :CONDITIONID";

		SqlMesTemplate.update(Sql, bindMap);

	}

	// Detail Data를 생성합니다.
	public void ModelingConfirmDetailCreate(MODELINGCONFIRM DelData,TxnInfo txnInfo) 
	{
		List<MODELINGCONFIRMDETAIL> DetailDataList = new ArrayList<MODELINGCONFIRMDETAIL>(); // 최종 셋팅 데이터
		List<MODELINGCONFIRMDETAIL> RODataList = ModelingConfirmDetailROComposition(DelData); // ROComposition Data를 가져옵니다.

		for (int i = 0; i < RODataList.size(); i++) 
		{
			MODELINGCONFIRMDETAIL ROData = RODataList.get(i);
			
			String sProcessID = ROData.getPROCESSID();
			List<MODELINGCONFIRMDETAIL> ROCDataList = ModelingConfirmDetailROCComposition(DelData,sProcessID);
			
			// Process start
			ROData.setACTIONTYPE("Start");
			ROData.setPROCESSPRINTINDEX(ConvertUtil.Object2Double(i+1));
			ROData.setRECIPEPRINTINDEX(ConvertUtil.Object2Long(0));
			ROData.setINPUTMODE("Operation");
			 // 생성시 제일 처음에 FirstProcessFlag에 Yes를찍어준다.
			if(i == 0)
			{
				ROData.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_YES);
			}
			else
			{
				ROData.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_NO);
			}
			DetailDataList.add(ROData);
			
			
			
			// RoCompositionID 셋팅
//			List<CONSUMABLECONDITIONSET> sConditionSetDataList = ModelConfirmDetailConditionSet(ROData.getKeyPLANTID(),ROData.getCOMPOSITIONID());
			List<TPCPOLICY> sTPCDataList = null;
			List<TGCPOLICY> sTGCDataList = null;
			
			int count = 0;	
			
			// 기존 라우팅이 제품코드로 설정되었으면 공정조건 데이터를  TPCPOLICY에서 가져오고
			//        제품군으로 설정되었으면 공정조건 데이터를 TGCPOLIY에서 가져온 로직 삭제
			// 공정조건은 TPCPOLICY로만 구성한다.
			// 제품군 공정조건(TGCPOLICY)은 제품공정조건(TPCPOLICY) 구성하는데 참조 데이터임. 
			sTPCDataList = ModelConfirmDetailTPCPolicy(ROData.getKeyPLANTID(),ROData.getKeyPRODUCTID(),ROData.getCOMPOSITIONID());
			
			//TPCPolicy
			if(sTPCDataList.size() > 0)
			{
				for(int k = 0 ; k < sTPCDataList.size() ; k++)
				{
					count++;
					TPCPOLICY TPCData = sTPCDataList.get(k);
					
					MODELINGCONFIRMDETAIL TPCDataCopy = new MODELINGCONFIRMDETAIL();
					TPCDataCopy = (MODELINGCONFIRMDETAIL) ObjectUtil.copyORMData(ROData);
					
					TPCDataCopy.setRECIPEPARAMETERID(TPCData.getKeyRECIPEPARAMETERID());
					TPCDataCopy.setRECIPEPARAMETERNAME(RecipeParameterNameSearch(TPCData.getKeyPLANTID(),TPCData.getKeyRECIPEPARAMETERID()));
					TPCDataCopy.setSPECTYPE(TPCData.getSPECTYPE());
					TPCDataCopy.setTARGET(TPCData.getTARGET());
					TPCDataCopy.setLOWERSPECLIMIT(TPCData.getLOWERSPECLIMIT());
					TPCDataCopy.setUPPERSPECLIMIT(TPCData.getUPPERSPECLIMIT());
					TPCDataCopy.setLOWERCONTROLLIMIT(TPCData.getLOWERCONTROLLIMIT());			
					TPCDataCopy.setUPPERCONTROLLIMIT(TPCData.getUPPERCONTROLLIMIT());
					TPCDataCopy.setLOWERSCREENLIMIT(TPCData.getLOWERSCREENLIMIT());
					TPCDataCopy.setUPPERSCREENLIMIT(TPCData.getUPPERSCREENLIMIT());
					//TPCDataCopy.setOBJECTTYPE(ConditionData.getOBJECTTYPE());
					TPCDataCopy.setCTPFLAG(TPCData.getCTPFLAG());
					//TPCDataCopy.setRECIPEPARAMETERMODE(ConditionData.getRECIPEPARAMETERMODE());
					//TPCDataCopy.setORDERDESCRIPTION(ConditionData.getORDERDESCRIPTION());
					TPCDataCopy.setORDERINDEX(TPCData.getORDERINDEX());
					//TPCDataCopy.setALARMID(TPCData.getALARMID());
					//TPCDataCopy.setAREAID(TPCData.getAREAID());
					TPCDataCopy.setDESCRIPTION(TPCData.getDESCRIPTION());
					TPCDataCopy.setPROCESSPRINTINDEX(ConvertUtil.Object2Double(i+1));
					TPCDataCopy.setRECIPEPRINTINDEX(ConvertUtil.Object2Long(0));
					TPCDataCopy.setINPUTMODE("OperationParameter");
					TPCDataCopy.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_NO);
					TPCDataCopy.setDESCRIPTION(TPCData.getDESCRIPTION());
					TPCDataCopy.setACTIONTYPE("None");
					
					DetailDataList.add(TPCDataCopy);
					
				}
			}
			/*
			// 기존 라우팅이 제품코드로 설정되었으면 공정조건 데이터를  TPCPOLICY에서 가져오고
			//        제품군으로 설정되었으면 공정조건 데이터를 TGCPOLIY에서 가져온 로직 삭제
			if(DelData.getPOLICYTYPE().equals("Product")) // 제품, 제품군 타입에 따라 공정조건을 다르게 가져오도록 
			{
				sTPCDataList = ModelConfirmDetailTPCPolicy(ROData.getKeyPLANTID(),ROData.getKeyPRODUCTID(),ROData.getCOMPOSITIONID());
			
				//TPCPolicy
				if(sTPCDataList.size() > 0)
				{
					for(int k = 0 ; k < sTPCDataList.size() ; k++)
					{
						count++;
						TPCPOLICY TPCData = sTPCDataList.get(k);
						
						MODELINGCONFIRMDETAIL TPCDataCopy = new MODELINGCONFIRMDETAIL();
						TPCDataCopy = (MODELINGCONFIRMDETAIL) ObjectUtil.copyORMData(ROData);
						
						TPCDataCopy.setRECIPEPARAMETERID(TPCData.getKeyRECIPEPARAMETERID());
						TPCDataCopy.setRECIPEPARAMETERNAME(RecipeParameterNameSearch(TPCData.getKeyPLANTID(),TPCData.getKeyRECIPEPARAMETERID()));
						TPCDataCopy.setSPECTYPE(TPCData.getSPECTYPE());
						TPCDataCopy.setTARGET(TPCData.getTARGET());
						TPCDataCopy.setLOWERSPECLIMIT(TPCData.getLOWERSPECLIMIT());
						TPCDataCopy.setUPPERSPECLIMIT(TPCData.getUPPERSPECLIMIT());
						TPCDataCopy.setLOWERCONTROLLIMIT(TPCData.getLOWERCONTROLLIMIT());			
						TPCDataCopy.setUPPERCONTROLLIMIT(TPCData.getUPPERCONTROLLIMIT());
						TPCDataCopy.setLOWERSCREENLIMIT(TPCData.getLOWERSCREENLIMIT());
						TPCDataCopy.setUPPERSCREENLIMIT(TPCData.getUPPERSCREENLIMIT());
						//TPCDataCopy.setOBJECTTYPE(ConditionData.getOBJECTTYPE());
						TPCDataCopy.setCTPFLAG(TPCData.getCTPFLAG());
						//TPCDataCopy.setRECIPEPARAMETERMODE(ConditionData.getRECIPEPARAMETERMODE());
						//TPCDataCopy.setORDERDESCRIPTION(ConditionData.getORDERDESCRIPTION());
						TPCDataCopy.setORDERINDEX(TPCData.getORDERINDEX());
						//TPCDataCopy.setALARMID(TPCData.getALARMID());
						//TPCDataCopy.setAREAID(TPCData.getAREAID());
						TPCDataCopy.setDESCRIPTION(TPCData.getDESCRIPTION());
						TPCDataCopy.setPROCESSPRINTINDEX(ConvertUtil.Object2Double(i+1));
						TPCDataCopy.setRECIPEPRINTINDEX(ConvertUtil.Object2Long(0));
						TPCDataCopy.setINPUTMODE("OperationParameter");
						TPCDataCopy.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_NO);
						TPCDataCopy.setDESCRIPTION(TPCData.getDESCRIPTION());
						TPCDataCopy.setACTIONTYPE("None");
						
						DetailDataList.add(TPCDataCopy);
						
					}
				}
			}
			else
			{
				sTGCDataList = ModelConfirmDetailTGCPolicy(ROData.getKeyPLANTID(),ROData.getKeyPRODUCTID(),ROData.getCOMPOSITIONID());
				//TGCPolicy
				if(sTGCDataList.size() > 0)
				{
					for(int k = 0 ; k < sTGCDataList.size() ; k++)
					{
						count++;
						TGCPOLICY TGCData = sTGCDataList.get(k);
						
						MODELINGCONFIRMDETAIL TGCDataCopy = new MODELINGCONFIRMDETAIL();
						TGCDataCopy = (MODELINGCONFIRMDETAIL) ObjectUtil.copyORMData(ROData);
						
						TGCDataCopy.setRECIPEPARAMETERID(TGCData.getKeyRECIPEPARAMETERID());
						TGCDataCopy.setRECIPEPARAMETERNAME(RecipeParameterNameSearch(TGCData.getKeyPLANTID(),TGCData.getKeyRECIPEPARAMETERID()));
						TGCDataCopy.setSPECTYPE(TGCData.getSPECTYPE());
						TGCDataCopy.setTARGET(TGCData.getTARGET());
						TGCDataCopy.setLOWERSPECLIMIT(TGCData.getLOWERSPECLIMIT());
						TGCDataCopy.setUPPERSPECLIMIT(TGCData.getUPPERSPECLIMIT());
						TGCDataCopy.setLOWERCONTROLLIMIT(TGCData.getLOWERCONTROLLIMIT());			
						TGCDataCopy.setUPPERCONTROLLIMIT(TGCData.getUPPERCONTROLLIMIT());
						TGCDataCopy.setLOWERSCREENLIMIT(TGCData.getLOWERSCREENLIMIT());
						TGCDataCopy.setUPPERSCREENLIMIT(TGCData.getUPPERSCREENLIMIT());
						//TPCDataCopy.setOBJECTTYPE(ConditionData.getOBJECTTYPE());
						TGCDataCopy.setCTPFLAG(TGCData.getCTPFLAG());
						//TPCDataCopy.setRECIPEPARAMETERMODE(ConditionData.getRECIPEPARAMETERMODE());
						//TPCDataCopy.setORDERDESCRIPTION(ConditionData.getORDERDESCRIPTION());
						TGCDataCopy.setORDERINDEX(TGCData.getORDERINDEX());
						//TPCDataCopy.setALARMID(TPCData.getALARMID());
						//TPCDataCopy.setAREAID(TPCData.getAREAID());
						TGCDataCopy.setDESCRIPTION(TGCData.getDESCRIPTION());
						TGCDataCopy.setPROCESSPRINTINDEX(ConvertUtil.Object2Double(i+1));
						TGCDataCopy.setRECIPEPRINTINDEX(ConvertUtil.Object2Long(999-count));
						TGCDataCopy.setINPUTMODE("OperationParameter");
						TGCDataCopy.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_NO);
						TGCDataCopy.setDESCRIPTION(TGCData.getDESCRIPTION());
						TGCDataCopy.setACTIONTYPE("None");
						
						DetailDataList.add(TGCDataCopy);
							
					}
				}
			}
			*/
			
			// Recipe RocCompositionID 셋팅
			for(int j = 0 ; j < ROCDataList.size() ; j++)
			{
				MODELINGCONFIRMDETAIL ROCData = ROCDataList.get(j);
				
				MODELINGCONFIRMDETAIL RODataCopy = new MODELINGCONFIRMDETAIL();
				RODataCopy = (MODELINGCONFIRMDETAIL) ObjectUtil.copyORMData(ROData);
				
				//MODELINGCONFIRMDETAIL RODataEnd = ROCDataList.get(i);
				RODataCopy.setCOMPOSITIONID(ROCData.getCOMPOSITIONID());
				RODataCopy.setRECIPEID(ROCData.getRECIPEID());
				RODataCopy.setRECIPENAME(ROCData.getRECIPENAME());
				RODataCopy.setRECIPESEQUENCE(ROCData.getRECIPESEQUENCE());
				RODataCopy.setRECIPETYPE(ROCData.getRECIPETYPE());
				RODataCopy.setRECIPERELATIONCODE(ROCData.getRECIPERELATIONCODE());
				RODataCopy.setRECIPETYPECODE(ROCData.getRECIPETYPECODE());
				RODataCopy.setCONCURRENCYPROCESSID(ROCData.getCONCURRENCYPROCESSID());
				RODataCopy.setCONCURRENCYSEQUENCE(ROCData.getCONCURRENCYSEQUENCE());
				RODataCopy.setCONSUMETYPE(ROCData.getCONSUMETYPE());
				RODataCopy.setCONSUMEID(ROCData.getCONSUMEID());
				RODataCopy.setPROCESSPRINTINDEX(ConvertUtil.Object2Double(i+1));
				RODataCopy.setRECIPEPRINTINDEX(ConvertUtil.Object2Long(j+1));
				RODataCopy.setINPUTMODE("Recipe");
				RODataCopy.setACTIONTYPE(ROCData.getRECIPETYPE());
				RODataCopy.setDESCRIPTION(ROCData.getDESCRIPTION());
				RODataCopy.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_NO);
				
				if(!RODataCopy.getRECIPETYPE().equalsIgnoreCase("Consumable"))
				{
					DetailDataList.add(RODataCopy);
				}
			
				List<CONSUMABLECONDITIONSET> ConditionSetDataList = null;
				List<TPCPOLICY> TPCDataList = null;
				List<TGCPOLICY> TGCDataList = null;
				

				TPCDataList = ModelConfirmDetailTPCPolicy(RODataCopy.getKeyPLANTID(), RODataCopy.getKeyPRODUCTID(), RODataCopy.getCOMPOSITIONID());
				ConditionSetDataList = ModelConfirmDetailConditionSet(RODataCopy.getKeyPLANTID(), RODataCopy.getKeyPRODUCTID(), RODataCopy.getCOMPOSITIONID());
				//ConsumableConditionSet
				if(ConditionSetDataList.size() > 0)
				{
					for(int k = 0 ; k < ConditionSetDataList.size() ; k++)
					{
						CONSUMABLECONDITIONSET ConditionData = ConditionSetDataList.get(k);
						
						MODELINGCONFIRMDETAIL ConditionDataCopy = new MODELINGCONFIRMDETAIL();
						ConditionDataCopy = (MODELINGCONFIRMDETAIL) ObjectUtil.copyORMData(RODataCopy);
						
						ConditionDataCopy.setCOMPOSITIONID(ConditionData.getKeyROCCOMPOSITIONID());
						ConditionDataCopy.setRECIPEPARAMETERID(ConditionData.getKeyCONSUMABLEID());
						ConditionDataCopy.setRECIPEPARAMETERNAME(ConsumableNameSearch(ConditionData.getKeyPLANTID(),ConditionData.getKeyCONSUMABLEID()));
						ConditionDataCopy.setORDERINDEX(ConditionData.getORDERINDEX());
						//ConditionDataCopy.setFEEDINGMODE(ConditionData.getFEEDINGMODE());
						ConditionDataCopy.setDESCRIPTION(ConditionData.getFEEDINGDESCRIPTION());
						ConditionDataCopy.setFEEDINGRATE(ConditionData.getFEEDINGRATE());
						//ConditionDataCopy.setRECYCLEFLAG(ConditionData.getRECYCLEFLAG());
						//ConditionDataCopy.setDESCRIPTION(ConditionData.getDESCRIPTION());
						ConditionDataCopy.setPROCESSPRINTINDEX(ConvertUtil.Object2Double(i+1));
						ConditionDataCopy.setRECIPEPRINTINDEX(ConvertUtil.Object2Long(j+1));
						ConditionDataCopy.setSTANDARDVALUE(BomStandardValueSet(ConditionData)); // BomDefinition의 StandardValue
						ConditionDataCopy.setCONSUMABLEVALUE(BomDetailConsumableValueSet(ConditionData)); // BomDetail
						ConditionDataCopy.setINPUTMODE("Recipe");
						ConditionDataCopy.setACTIONTYPE("Consumable");
						ConditionDataCopy.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_NO);
						
						//해당 원료투입 Recipe를 복사 후 기존 등록되어있는 항목을 지웁니다.						
						DetailDataList.add(ConditionDataCopy);
						
					}
				}
				
				
				// 기존 라우팅이 제품코드로 설정되었으면 공정조건 데이터를  TPCPOLICY에서 가져오고
				//        제품군으로 설정되었으면 공정조건 데이터를 TGCPOLIY에서 가져온 로직 삭제
				// 공정조건은 TPCPOLICY로만 구성한다.
				// 제품군 공정조건(TGCPOLICY)은 제품공정조건(TPCPOLICY) 구성하는데 참조 데이터임. 
				//TPCPolicy
				if(TPCDataList.size() > 0)
				{
					for(int k = 0 ; k < TPCDataList.size() ; k++)
					{
						TPCPOLICY TPCData = TPCDataList.get(k);
						
						MODELINGCONFIRMDETAIL TPCDataCopy = new MODELINGCONFIRMDETAIL();
						TPCDataCopy = (MODELINGCONFIRMDETAIL) ObjectUtil.copyORMData(RODataCopy);
						
						TPCDataCopy.setRECIPEPARAMETERID(TPCData.getKeyRECIPEPARAMETERID());
						TPCDataCopy.setRECIPEPARAMETERNAME(RecipeParameterNameSearch(TPCData.getKeyPLANTID(),TPCData.getKeyRECIPEPARAMETERID()));
						TPCDataCopy.setSPECTYPE(TPCData.getSPECTYPE());
						TPCDataCopy.setTARGET(TPCData.getTARGET());
						TPCDataCopy.setLOWERSPECLIMIT(TPCData.getLOWERSPECLIMIT());
						TPCDataCopy.setUPPERSPECLIMIT(TPCData.getUPPERSPECLIMIT());
						TPCDataCopy.setLOWERCONTROLLIMIT(TPCData.getLOWERCONTROLLIMIT());			
						TPCDataCopy.setUPPERCONTROLLIMIT(TPCData.getUPPERCONTROLLIMIT());
						TPCDataCopy.setLOWERSCREENLIMIT(TPCData.getLOWERSCREENLIMIT());
						TPCDataCopy.setUPPERSCREENLIMIT(TPCData.getUPPERSCREENLIMIT());
						//TPCDataCopy.setOBJECTTYPE(ConditionData.getOBJECTTYPE());
						TPCDataCopy.setCTPFLAG(TPCData.getCTPFLAG());
						//TPCDataCopy.setRECIPEPARAMETERMODE(ConditionData.getRECIPEPARAMETERMODE());
						//TPCDataCopy.setORDERDESCRIPTION(ConditionData.getORDERDESCRIPTION());
						TPCDataCopy.setORDERINDEX(TPCData.getORDERINDEX());
						//TPCDataCopy.setALARMID(TPCData.getALARMID());
						//TPCDataCopy.setAREAID(TPCData.getAREAID());
						TPCDataCopy.setDESCRIPTION(TPCData.getDESCRIPTION());
						TPCDataCopy.setPROCESSPRINTINDEX(ConvertUtil.Object2Double(i+1));
						TPCDataCopy.setRECIPEPRINTINDEX(ConvertUtil.Object2Long(j+1));
						TPCDataCopy.setINPUTMODE("RecipeParameter");
						TPCDataCopy.setACTIONTYPE("None");
						TPCDataCopy.setDESCRIPTION(TPCData.getDESCRIPTION());
						TPCDataCopy.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_NO);
						
						DetailDataList.add(TPCDataCopy);
						
					}
				}
				
				/*
				// 기존 라우팅이 제품코드로 설정되었으면 공정조건 데이터를  TPCPOLICY에서 가져오고
				//        제품군으로 설정되었으면 공정조건 데이터를 TGCPOLIY에서 가져온 로직 삭제
				if(DelData.getPOLICYTYPE().equals("Product")) // 제품, 제품군 타입에 따라 공정조건을 다르게 가져오도록
				{
					//TPCPolicy
					if(TPCDataList.size() > 0)
					{
						for(int k = 0 ; k < TPCDataList.size() ; k++)
						{
							TPCPOLICY TPCData = TPCDataList.get(k);
							
							MODELINGCONFIRMDETAIL TPCDataCopy = new MODELINGCONFIRMDETAIL();
							TPCDataCopy = (MODELINGCONFIRMDETAIL) ObjectUtil.copyORMData(RODataCopy);
							
							TPCDataCopy.setRECIPEPARAMETERID(TPCData.getKeyRECIPEPARAMETERID());
							TPCDataCopy.setRECIPEPARAMETERNAME(RecipeParameterNameSearch(TPCData.getKeyPLANTID(),TPCData.getKeyRECIPEPARAMETERID()));
							TPCDataCopy.setSPECTYPE(TPCData.getSPECTYPE());
							TPCDataCopy.setTARGET(TPCData.getTARGET());
							TPCDataCopy.setLOWERSPECLIMIT(TPCData.getLOWERSPECLIMIT());
							TPCDataCopy.setUPPERSPECLIMIT(TPCData.getUPPERSPECLIMIT());
							TPCDataCopy.setLOWERCONTROLLIMIT(TPCData.getLOWERCONTROLLIMIT());			
							TPCDataCopy.setUPPERCONTROLLIMIT(TPCData.getUPPERCONTROLLIMIT());
							TPCDataCopy.setLOWERSCREENLIMIT(TPCData.getLOWERSCREENLIMIT());
							TPCDataCopy.setUPPERSCREENLIMIT(TPCData.getUPPERSCREENLIMIT());
							//TPCDataCopy.setOBJECTTYPE(ConditionData.getOBJECTTYPE());
							TPCDataCopy.setCTPFLAG(TPCData.getCTPFLAG());
							//TPCDataCopy.setRECIPEPARAMETERMODE(ConditionData.getRECIPEPARAMETERMODE());
							//TPCDataCopy.setORDERDESCRIPTION(ConditionData.getORDERDESCRIPTION());
							TPCDataCopy.setORDERINDEX(TPCData.getORDERINDEX());
							//TPCDataCopy.setALARMID(TPCData.getALARMID());
							//TPCDataCopy.setAREAID(TPCData.getAREAID());
							TPCDataCopy.setDESCRIPTION(TPCData.getDESCRIPTION());
							TPCDataCopy.setPROCESSPRINTINDEX(ConvertUtil.Object2Double(i+1));
							TPCDataCopy.setRECIPEPRINTINDEX(ConvertUtil.Object2Long(j+1));
							TPCDataCopy.setINPUTMODE("RecipeParameter");
							TPCDataCopy.setACTIONTYPE("None");
							TPCDataCopy.setDESCRIPTION(TPCData.getDESCRIPTION());
							TPCDataCopy.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_NO);
							
							DetailDataList.add(TPCDataCopy);
							
						}
					}
				}
				else
				{
					TGCDataList = ModelConfirmDetailTGCPolicy(RODataCopy.getKeyPLANTID(),RODataCopy.getKeyPRODUCTID(),RODataCopy.getCOMPOSITIONID());
				
					//TGCPolicy
					if(TGCDataList.size() > 0)
					{
						for(int k = 0 ; k < TGCDataList.size() ; k++)
						{
							TGCPOLICY TGCData = TGCDataList.get(k);
							
							MODELINGCONFIRMDETAIL TGCDataCopy = new MODELINGCONFIRMDETAIL();
							TGCDataCopy = (MODELINGCONFIRMDETAIL) ObjectUtil.copyORMData(RODataCopy);
							
							TGCDataCopy.setRECIPEPARAMETERID(TGCData.getKeyRECIPEPARAMETERID());
							TGCDataCopy.setRECIPEPARAMETERNAME(RecipeParameterNameSearch(TGCData.getKeyPLANTID(),TGCData.getKeyRECIPEPARAMETERID()));
							TGCDataCopy.setSPECTYPE(TGCData.getSPECTYPE());
							TGCDataCopy.setTARGET(TGCData.getTARGET());
							TGCDataCopy.setLOWERSPECLIMIT(TGCData.getLOWERSPECLIMIT());
							TGCDataCopy.setUPPERSPECLIMIT(TGCData.getUPPERSPECLIMIT());
							TGCDataCopy.setLOWERCONTROLLIMIT(TGCData.getLOWERCONTROLLIMIT());			
							TGCDataCopy.setUPPERCONTROLLIMIT(TGCData.getUPPERCONTROLLIMIT());
							TGCDataCopy.setLOWERSCREENLIMIT(TGCData.getLOWERSCREENLIMIT());
							TGCDataCopy.setUPPERSCREENLIMIT(TGCData.getUPPERSCREENLIMIT());
							//TPCDataCopy.setOBJECTTYPE(ConditionData.getOBJECTTYPE());
							TGCDataCopy.setCTPFLAG(TGCData.getCTPFLAG());
							//TPCDataCopy.setRECIPEPARAMETERMODE(ConditionData.getRECIPEPARAMETERMODE());
							//TPCDataCopy.setORDERDESCRIPTION(ConditionData.getORDERDESCRIPTION());
							TGCDataCopy.setORDERINDEX(TGCData.getORDERINDEX());
							//TPCDataCopy.setALARMID(TPCData.getALARMID());
							//TPCDataCopy.setAREAID(TPCData.getAREAID());
							TGCDataCopy.setDESCRIPTION(TGCData.getDESCRIPTION());
							TGCDataCopy.setPROCESSPRINTINDEX(ConvertUtil.Object2Double(i+1));
							TGCDataCopy.setRECIPEPRINTINDEX(ConvertUtil.Object2Long(j+1));
							TGCDataCopy.setINPUTMODE("RecipeParameter");
							TGCDataCopy.setACTIONTYPE("None");
							TGCDataCopy.setDESCRIPTION(TGCData.getDESCRIPTION());
							TGCDataCopy.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_NO);
							
							DetailDataList.add(TGCDataCopy);
							
						}
					}
				}
				*/

			
			}
			
			
			// Process End
			MODELINGCONFIRMDETAIL RODataEnd = new MODELINGCONFIRMDETAIL();
			RODataEnd = (MODELINGCONFIRMDETAIL) ObjectUtil.copyORMData(ROData);
			
			RODataEnd.setACTIONTYPE("End");
			RODataEnd.setPROCESSPRINTINDEX(ConvertUtil.Object2Double(i+1));
			RODataEnd.setRECIPEPRINTINDEX(ConvertUtil.Object2Long(999));
			RODataEnd.setFIRSTPROCESSFLAG(Constant.FLAG_YESNO_NO);
			RODataEnd.setINPUTMODE("Operation");
			RODataEnd.setDESCRIPTION("");
			DetailDataList.add(RODataEnd);
		}

		
		for(int i = 0 ; i < DetailDataList.size() ; i++)
		{
			MODELINGCONFIRMDETAIL DetailData = DetailDataList.get(i);
			DetailData.setKeyTIMEKEY(DateUtil.getCurrentEventTimeKey());
			
			DetailData.excuteDML(SqlConstant.DML_INSERT);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<MODELINGCONFIRMDETAIL> ModelingConfirmDetailROComposition(MODELINGCONFIRM DelData) 
	{

		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.clear();

		String Sql = " SELECT MC.PLANTID,"
				+ " 		 MC.PRODUCTID,"
				+ " 		 MC.BOMID,"
				+ " 		 MC.BOMVERSION,"
				+ " 		 MC.POLICYNAME,"
				+ " 		 MC.POLICYVALUE,"
				+ " 		 MC.CONDITIONTYPE,"
				+ " 		 MC.CONDITIONID,"
				+ " 		 MC.PROCESSROUTEID,"
				+ " 		 PR.PROCESSROUTETYPE,"
				+ " 		 RC.ROCOMPOSITIONID AS COMPOSITIONID,"
				+ " 		 RC.PROCESSID,"
				+ " 		 RC.PROCESSNAME,"
				+ " 		 RC.PROCESSSEQUENCE,"
				+ " 		 RC.CONCURRENCYPROCESSID,"
				+ " 		 RC.CONCURRENCYSEQUENCE,"
				+ " 		 RC.AUTOTRACKIN,"
				+ " 		 RC.AUTOTRACKOUT,"
				+ " 		 RC.CREATELOTFLAG,"
				+ " 		 RC.CREATELOTRULE,"
				+ " 		 RC.ENDOFROUTE,"
				+ "			 PD.PROCESSTYPE,"
				+ " 		 PD.DETAILPROCESSTYPE,"
				+ "		     PD.PACKINGFLAG,"
				+ "		     RC.ERPPROCESSCODE,"
				+ "          RC.DESCRIPTION"
				+ "  FROM MODELINGCONFIRM MC, ROCOMPOSITION RC, PROCESSROUTE PR, PROCESSDEFINITION PD"
				+ " WHERE 1 = 1" 
				+ "   AND MC.PLANTID = RC.PLANTID"
				+ "   AND MC.PLANTID = PR.PLANTID"
				+ "   AND MC.PLANTID = PD.PLANTID"
				+ "   AND RC.PROCESSID = PD.PROCESSID"
				+ "   AND MC.PROCESSROUTEID = PR.PROCESSROUTEID"
				+ "   AND MC.PROCESSROUTEID = RC.PROCESSROUTEID"
				+ "   AND MC.PLANTID = :PLANTID"
				+ "	  AND MC.PRODUCTID = :PRODUCTID"
				+ "   AND MC.BOMID = :BOMID"
				+ "   AND MC.BOMVERSION = :BOMVERSION"
				+ "   AND MC.PROCESSROUTEID = :PROCESSROUTEID"
				+ "   AND MC.POLICYNAME = :POLICYNAME "	
				+ "   AND MC.POLICYVALUE = :POLICYVALUE "
				+ "	  AND MC.CONDITIONTYPE = :CONDITIONTYPE"
				+ "	  AND MC.CONDITIONID = :CONDITIONID"
				+ " ORDER BY RC.PROCESSSEQUENCE";

		bindMap.put("PLANTID", DelData.getKeyPLANTID());
		bindMap.put("PRODUCTID", DelData.getKeyPRODUCTID());
		bindMap.put("PROCESSROUTEID", DelData.getPROCESSROUTEID());
		bindMap.put("BOMID", DelData.getKeyBOMID());
		bindMap.put("BOMVERSION", DelData.getKeyBOMVERSION());
		bindMap.put("POLICYNAME", DelData.getKeyPOLICYNAME());
		bindMap.put("POLICYVALUE", DelData.getKeyPOLICYVALUE());
		bindMap.put("CONDITIONTYPE", DelData.getKeyCONDITIONTYPE());
		bindMap.put("CONDITIONID", DelData.getKeyCONDITIONID());
		
		List<Object> DataList = SqlMesTemplate.queryForList(Sql, bindMap);

		List<MODELINGCONFIRMDETAIL> RocDetailList = new ArrayList<MODELINGCONFIRMDETAIL>();

		for (int i = 0; i < DataList.size(); i++) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(i);

			// Data 객체 생성
			MODELINGCONFIRMDETAIL ModelingConfirmDetail = new MODELINGCONFIRMDETAIL();

			ModelingConfirmDetail.setKeyPLANTID(dataMap.get("PLANTID"));
			ModelingConfirmDetail.setKeyBOMID(dataMap.get("BOMID"));
			ModelingConfirmDetail.setKeyPRODUCTID(dataMap.get("PRODUCTID"));
			ModelingConfirmDetail.setKeyBOMVERSION(dataMap.get("BOMVERSION"));
			ModelingConfirmDetail.setKeyPOLICYNAME(dataMap.get("POLICYNAME"));
			ModelingConfirmDetail.setKeyPOLICYVALUE(dataMap.get("POLICYVALUE"));
			ModelingConfirmDetail.setKeyCONDITIONTYPE(dataMap.get("CONDITIONTYPE"));
			ModelingConfirmDetail.setKeyCONDITIONID(dataMap.get("CONDITIONID"));
			ModelingConfirmDetail.setPROCESSROUTEID(dataMap.get("PROCESSROUTEID"));
			ModelingConfirmDetail.setPROCESSROUTETYPE(dataMap.get("PROCESSROUTETYPE"));
			ModelingConfirmDetail.setCOMPOSITIONID(dataMap.get("COMPOSITIONID"));
			ModelingConfirmDetail.setPROCESSID(dataMap.get("PROCESSID"));
			ModelingConfirmDetail.setPROCESSNAME(dataMap.get("PROCESSNAME"));
			ModelingConfirmDetail.setPROCESSSEQUENCE(ConvertUtil.Object2Long(dataMap.get("PROCESSSEQUENCE")));
			ModelingConfirmDetail.setPROCESSTYPE(dataMap.get("PROCESSTYPE"));
			ModelingConfirmDetail.setDETAILPROCESSTYPE(dataMap.get("DETAILPROCESSTYPE"));
			ModelingConfirmDetail.setCONCURRENCYPROCESSID(dataMap.get("CONCURRENCYPROCESSID"));
			ModelingConfirmDetail.setCONCURRENCYSEQUENCE(ConvertUtil.Object2Long(dataMap.get("CONCURRENCYSEQUENCE")));
			ModelingConfirmDetail.setAUTOTRACKIN(dataMap.get("AUTOTRACKIN"));
			ModelingConfirmDetail.setAUTOTRACKOUT(dataMap.get("AUTOTRACKOUT"));
			ModelingConfirmDetail.setCREATELOTFLAG(dataMap.get("CREATELOTFLAG"));
			ModelingConfirmDetail.setCREATELOTRULE(dataMap.get("CREATELOTRULE"));
			ModelingConfirmDetail.setENDOFROUTE(dataMap.get("ENDOFROUTE"));
			ModelingConfirmDetail.setPACKINGFLAG(dataMap.get("PACKINGFLAG"));
			ModelingConfirmDetail.setDESCRIPTION(dataMap.get("DESCRIPTION"));
			ModelingConfirmDetail.setERPPROCESSCODE(dataMap.get("ERPPROCESSCODE"));

			RocDetailList.add(ModelingConfirmDetail);
			
		}

		return RocDetailList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<MODELINGCONFIRMDETAIL> ModelingConfirmDetailROCComposition(MODELINGCONFIRM DelData, String sProcessID) 
	{
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.clear();

		String Sql = " SELECT MC.PLANTID,"
					+"        MC.PRODUCTID,"
					+"        MC.BOMID,"
					+"        MC.BOMVERSION,"
					+"        MC.POLICYNAME,"
					+"        MC.POLICYVALUE,"
					+"        MC.CONDITIONTYPE,"
					+"        MC.CONDITIONID,"
					+"        MC.PROCESSROUTEID,"
					+"        RCC.ROCCOMPOSITIONID AS COMPOSITIONID,"
					+"        RCC.PROCESSID,"
					+"        RCC.PROCESSSEQUENCE,"
					+"        RCC.RECIPEID,"
					+"        RCC.RECIPENAME,"
					+"        RCC.RECIPESEQUENCE,"
					+"        RCC.RECIPETYPE,"
					+"        RCC.RECIPERELATIONCODE,"
					+"        RCC.RECIPETYPECODE,"
					+"        RCC.CONCURRENCYPROCESSID," 
					+"        RCC.CONCURRENCYSEQUENCE,"
					+"        RCC.CONSUMETYPE,"
					+"        RCC.CONSUMEID,"
					+"        RCC.DESCRIPTION"
					+"   FROM MODELINGCONFIRM MC, ROCCOMPOSITION RCC" 
					+"  WHERE     1 = 1"
					+"        AND MC.PLANTID = RCC.PLANTID"
					+"        AND MC.PROCESSROUTEID = RCC.PROCESSROUTEID"
					+"        AND MC.PRODUCTID = :PRODUCTID"
					+"        AND MC.PROCESSROUTEID = :PROCESSROUTEID"
					+"        AND MC.BOMID = :BOMID"
					+"        AND MC.BOMVERSION = :BOMVERSION"
					+"        AND MC.POLICYNAME = :POLICYNAME "	
					+" 		  AND MC.POLICYVALUE = :POLICYVALUE "
					+"		  AND MC.CONDITIONTYPE = :CONDITIONTYPE"
					+"		  AND MC.CONDITIONID = :CONDITIONID"
					+"        AND RCC.PROCESSID = :PROCESSID"
					+" ORDER BY RCC.RECIPESEQUENCE"
					;

		bindMap.put("PLANTID", DelData.getKeyPLANTID());
		bindMap.put("PRODUCTID", DelData.getKeyPRODUCTID());
		bindMap.put("PROCESSROUTEID", DelData.getPROCESSROUTEID());
		bindMap.put("BOMID", DelData.getKeyBOMID());
		bindMap.put("BOMVERSION", DelData.getKeyBOMVERSION());
		bindMap.put("POLICYNAME", DelData.getKeyPOLICYNAME());
		bindMap.put("POLICYVALUE", DelData.getKeyPOLICYVALUE());
		bindMap.put("CONDITIONTYPE", DelData.getKeyCONDITIONTYPE());
		bindMap.put("CONDITIONID", DelData.getKeyCONDITIONID());
		bindMap.put("PROCESSID", sProcessID);
		
		List<Object> DataList = SqlMesTemplate.queryForList(Sql, bindMap);


		List<MODELINGCONFIRMDETAIL> RoccDetailList = new ArrayList<MODELINGCONFIRMDETAIL>();

		for (int i = 0; i < DataList.size(); i++) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(i);

			// Data 객체 생성
			MODELINGCONFIRMDETAIL ModelingConfirmDetail = new MODELINGCONFIRMDETAIL();

			ModelingConfirmDetail.setKeyPLANTID(dataMap.get("PLANTID"));
			ModelingConfirmDetail.setKeyPRODUCTID(dataMap.get("PRODUCTID"));
			ModelingConfirmDetail.setKeyBOMID(dataMap.get("BOMID"));
			ModelingConfirmDetail.setKeyBOMVERSION(dataMap.get("BOMVERSION"));
			ModelingConfirmDetail.setKeyPOLICYNAME(dataMap.get("POLICYNAME"));
			ModelingConfirmDetail.setKeyPOLICYVALUE(dataMap.get("POLICYVALUE"));
			ModelingConfirmDetail.setKeyCONDITIONTYPE(dataMap.get("CONDITIONTYPE"));
			ModelingConfirmDetail.setKeyCONDITIONID(dataMap.get("CONDITIONID"));
			ModelingConfirmDetail.setPROCESSROUTEID(dataMap.get("PROCESSROUTEID"));
			ModelingConfirmDetail.setPROCESSROUTETYPE(dataMap.get("PROCESSROUTETYPE"));
			ModelingConfirmDetail.setCOMPOSITIONID(dataMap.get("COMPOSITIONID"));
			ModelingConfirmDetail.setPROCESSID(dataMap.get("PROCESSID"));
			ModelingConfirmDetail.setPROCESSSEQUENCE(ConvertUtil.Object2Long(dataMap.get("PROCESSSEQUENCE")));
			ModelingConfirmDetail.setRECIPEID(dataMap.get("RECIPEID"));
			ModelingConfirmDetail.setRECIPENAME(dataMap.get("RECIPENAME"));
			ModelingConfirmDetail.setRECIPESEQUENCE(ConvertUtil.Object2Long(dataMap.get("RECIPESEQUENCE")));
			ModelingConfirmDetail.setRECIPETYPE(dataMap.get("RECIPETYPE"));
			ModelingConfirmDetail.setRECIPERELATIONCODE(dataMap.get("RECIPERELATIONCODE"));
			ModelingConfirmDetail.setRECIPETYPECODE(dataMap.get("RECIPETYPECODE"));
			ModelingConfirmDetail.setCONCURRENCYPROCESSID(dataMap.get("CONCURRENCYPROCESSID"));
			ModelingConfirmDetail.setCONCURRENCYSEQUENCE(ConvertUtil.Object2Long(dataMap.get("CONCURRENCYSEQUENCE")));
			ModelingConfirmDetail.setCONSUMETYPE(dataMap.get("CONSUMETYPE"));
			ModelingConfirmDetail.setCONSUMEID(dataMap.get("CONSUMEID"));
			ModelingConfirmDetail.setDESCRIPTION(dataMap.get("DESCRIPTION"));
			
			
			RoccDetailList.add(ModelingConfirmDetail);
		}

		return RoccDetailList;
		
	}
	
	
	
	// 원료투입 ConsumableConditionSet 목록
	@SuppressWarnings("unchecked")
	public List<CONSUMABLECONDITIONSET> ModelConfirmDetailConditionSet(String sPlantID, String sProductID, String CompositionID) 
	{
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.clear();

		String Sql = "SELECT CC.PLANTID," 
				+ "		 CC.PRODUCTID,"
				+ "		 CC.ROCCOMPOSITIONID," 
				+ "		 CC.BOMID,"
				+ "		 CC.BOMVERSION," 
				+ "		 CC.BOMINDEX,"
				+ "		 CC.CONSUMABLEID," 
				+ "		 CC.ORDERINDEX,"
				+ "		 CC.FEEDINGMODE," 
				+ "		 CC.FEEDINGDESCRIPTION,"
				+ "		 CC.FEEDINGRATE," 
				+ "		 CC.RECYCLEFLAG,"
				+ "		 CC.DESCRIPTION"
				+ "	FROM CONSUMABLECONDITIONSET CC "
				+ "	WHERE     1 = 1" 
				+ "	  AND CC.PLANTID = :PLANTID"
				+ "   AND CC.PRODUCTID = :PRODUCTID"
				+ "	  AND CC.ROCCOMPOSITIONID = :COMPOSITIONID"
				+ "	ORDER BY ORDERINDEX"
				;

		bindMap.put("PLANTID", sPlantID);
		bindMap.put("PRODUCTID", sProductID);
		bindMap.put("COMPOSITIONID", CompositionID);

		List<Object> DataList = SqlMesTemplate.queryForList(Sql, bindMap);

		List<CONSUMABLECONDITIONSET> ConditionDataList = new ArrayList<CONSUMABLECONDITIONSET>();

		for (int i = 0; i < DataList.size(); i++) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(i);

			// Data 객체 생성
			CONSUMABLECONDITIONSET ConditionSet = new CONSUMABLECONDITIONSET();

			ConditionSet.setKeyPLANTID(dataMap.get("PLANTID"));
			ConditionSet.setKeyPRODUCTID(dataMap.get("PRODUCTID"));
			ConditionSet.setKeyROCCOMPOSITIONID(dataMap.get("ROCCOMPOSITIONID"));
			ConditionSet.setKeyBOMID(dataMap.get("BOMID"));
			ConditionSet.setKeyBOMVERSION(dataMap.get("BOMVERSION"));
			ConditionSet.setKeyBOMINDEX(ConvertUtil.Object2Long(dataMap.get("BOMINDEX")));
			ConditionSet.setKeyCONSUMABLEID(dataMap.get("CONSUMABLEID"));
			ConditionSet.setORDERINDEX(ConvertUtil.Object2Long(dataMap.get("ORDERINDEX")));
			ConditionSet.setFEEDINGMODE(dataMap.get("FEEDINGMODE"));
			ConditionSet.setFEEDINGDESCRIPTION(dataMap.get("FEEDINGDESCRIPTION"));			
			ConditionSet.setFEEDINGRATE(ConvertUtil.Object2Double(dataMap.get("FEEDINGRATE")));
			ConditionSet.setRECYCLEFLAG(dataMap.get("RECYCLEFLAG"));
			ConditionSet.setDESCRIPTION(dataMap.get("DESCRIPTION"));

			ConditionDataList.add(ConditionSet);

		}
		return ConditionDataList;
	}
	
	// BomDefinition의 StandardValue를 가져옵니다.
	@SuppressWarnings("unchecked")
	public Long BomStandardValueSet(CONSUMABLECONDITIONSET conditionSet)
	{
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.clear();

		String Sql = "SELECT STANDARDVALUE"
				+ "	FROM BOMDEFINITION BD "
				+ "	WHERE 1 = 1" 
				+ "	  AND BD.PLANTID = :PLANTID"
				+ "   AND BD.PRODUCTID = :PRODUCTID"
				+ "	  AND BD.BOMID = :BOMID"
				+ "   AND BD.BOMVERSION = :BOMVERSION"
				;
		

		bindMap.put("PLANTID", conditionSet.getKeyPLANTID());
		bindMap.put("PRODUCTID", conditionSet.getKeyPRODUCTID());
		bindMap.put("BOMID", conditionSet.getKeyBOMID());
		bindMap.put("BOMVERSION", conditionSet.getKeyBOMVERSION());
		
		List<Object> DataList = SqlMesTemplate.queryForList(Sql, bindMap);

		HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(0);

		Long sStandardValue = ConvertUtil.Object2Long(dataMap.get("STANDARDVALUE"));
		return sStandardValue;
		
	}
	
	// BomDetail의 ConsumableValue를 가져옵니다.
	@SuppressWarnings("unchecked")
	public Double BomDetailConsumableValueSet(CONSUMABLECONDITIONSET conditionSet)
	{
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.clear();

		String Sql = "SELECT BD.CONSUMABLEVALUE" 
				+ "	FROM BOMDETAIL BD "
				+ "	WHERE     1 = 1" 
				+ "	  AND BD.PLANTID = :PLANTID"
				+ "   AND BD.PRODUCTID = :PRODUCTID"
				+ "	  AND BD.BOMID = :BOMID"
				+ "   AND BD.BOMVERSION = :BOMVERSION"
				+ "   AND BD.BOMINDEX = :BOMINDEX"
				;
		

		bindMap.put("PLANTID", conditionSet.getKeyPLANTID());
		bindMap.put("PRODUCTID", conditionSet.getKeyPRODUCTID());
		bindMap.put("BOMID", conditionSet.getKeyBOMID());
		bindMap.put("BOMVERSION", conditionSet.getKeyBOMVERSION());
		bindMap.put("BOMINDEX", conditionSet.getKeyBOMINDEX());
		
		List<Object> DataList = SqlMesTemplate.queryForList(Sql, bindMap);
		

		HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(0);

		Double sConsumableValue = ConvertUtil.Object2Double(dataMap.get("CONSUMABLEVALUE"));
		
		return sConsumableValue;
				
	}
	
	// 공정조건 목록 TPCPolicy 목록
	@SuppressWarnings("unchecked")
	public List<TPCPOLICY> ModelConfirmDetailTPCPolicy(String sPlantID, String sProductID, String CompositionID) 
	{
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.clear();

		String Sql = "SELECT TPC.PLANTID,"
					+"       TPC.PRODUCTID,"
					+"       TPC.COMPOSITIONID,"
					+"       TPC.RECIPEPARAMETERID,"
					+"       TPC.BOMID,"
					+"       TPC.BOMVERSION,"
					+"       TPC.SPECTYPE,"
					+"       TPC.TARGET,"
					+"       TPC.LOWERSPECLIMIT,"
					+"       TPC.UPPERSPECLIMIT,"
					+"       TPC.LOWERCONTROLLIMIT,"
					+"       TPC.UPPERCONTROLLIMIT,"
					+"       TPC.LOWERSCREENLIMIT,"
					+"       TPC.UPPERSCREENLIMIT,"
					+"       TPC.OBJECTTYPE,"
					+"       TPC.CTPFLAG,"
					+"       TPC.RECIPEPARAMETERMODE,"
					+"       TPC.ORDERINDEX,"
					+"       TPC.ALARMID,"
					+"       TPC.DESCRIPTION"
					+"  FROM TPCPOLICY TPC "
					+"  WHERE  1 = 1"
					+"    AND TPC.PLANTID = :PLANTID"
					+"    AND TPC.COMPOSITIONID = :COMPOSITIONID"
					+"    AND TPC.PRODUCTID = :PRODUCTID"
					+"   ORDER BY ORDERINDEX"
					;
		
		bindMap.put("PLANTID", sPlantID);
		bindMap.put("COMPOSITIONID", CompositionID);
		bindMap.put("PRODUCTID", sProductID);

		List<Object> DataList = SqlMesTemplate.queryForList(Sql, bindMap);
		
		List<TPCPOLICY> TPCPolicyDataList = new ArrayList<TPCPOLICY>();

		for (int i = 0; i < DataList.size(); i++) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(i);

			// Data 객체 생성
			TPCPOLICY TPCPolicyData = new TPCPOLICY();

			TPCPolicyData.setKeyPLANTID(dataMap.get("PLANTID"));
			TPCPolicyData.setKeyPRODUCTID(dataMap.get("PRODUCTID"));
			TPCPolicyData.setKeyCOMPOSITIONID(dataMap.get("COMPOSITIONID"));
			TPCPolicyData.setKeyRECIPEPARAMETERID(dataMap.get("RECIPEPARAMETERID"));
			TPCPolicyData.setKeyBOMID(dataMap.get("BOMID"));
			TPCPolicyData.setKeyBOMVERSION(dataMap.get("BOMVERSION"));
			TPCPolicyData.setSPECTYPE(dataMap.get("SPECTYPE"));
			TPCPolicyData.setTARGET(dataMap.get("TARGET"));
			TPCPolicyData.setLOWERSPECLIMIT(dataMap.get("LOWERSPECLIMIT"));
			TPCPolicyData.setUPPERSPECLIMIT(dataMap.get("UPPERSPECLIMIT"));
			TPCPolicyData.setLOWERCONTROLLIMIT(dataMap.get("LOWERCONTROLLIMIT"));			
			TPCPolicyData.setUPPERCONTROLLIMIT(dataMap.get("UPPERCONTROLLIMIT"));
			TPCPolicyData.setLOWERSCREENLIMIT(dataMap.get("LOWERSCREENLIMIT"));
			TPCPolicyData.setUPPERSCREENLIMIT(dataMap.get("UPPERSCREENLIMIT"));
			TPCPolicyData.setOBJECTTYPE(dataMap.get("OBJECTTYPE"));
			TPCPolicyData.setCTPFLAG(dataMap.get("CTPFLAG"));
			TPCPolicyData.setRECIPEPARAMETERMODE(dataMap.get("RECIPEPARAMETERMODE"));
			TPCPolicyData.setORDERINDEX(ConvertUtil.Object2Long(dataMap.get("ORDERINDEX")));
			TPCPolicyData.setALARMID(dataMap.get("ALARMID"));
			TPCPolicyData.setDESCRIPTION(dataMap.get("DESCRIPTION"));

			TPCPolicyDataList.add(TPCPolicyData);

		}

		return TPCPolicyDataList;
		
	}
	
	
	// 공정조건 목록 TGCPolicy 목록
		@SuppressWarnings("unchecked")
		public List<TGCPOLICY> ModelConfirmDetailTGCPolicy(String sPlantID, String sProductID, String CompositionID) 
		{
			HashMap<String, Object> bindMap = new HashMap<String, Object>();
			bindMap.clear();

			String Sql = "SELECT TGC.PLANTID,"
						+"       TGC.PRODUCTGROUPID, "
						+"       TGC.COMPOSITIONID,"
						+"       TGC.RECIPEPARAMETERID,"
						+"       TGC.SPECTYPE,"
						+"       TGC.TARGET,"
						+"       TGC.LOWERSPECLIMIT,"
						+"       TGC.UPPERSPECLIMIT,"
						+"       TGC.LOWERCONTROLLIMIT,"
						+"       TGC.UPPERCONTROLLIMIT,"
						+"       TGC.LOWERSCREENLIMIT,"
						+"       TGC.UPPERSCREENLIMIT,"
						+"       TGC.OBJECTTYPE,"
						+"       TGC.CTPFLAG,"
						+"       TGC.RECIPEPARAMETERMODE,"
						+"       TGC.ORDERINDEX,"
						+"       TGC.ALARMID,"
						+"       TGC.DESCRIPTION"
						+"  FROM TGCPOLICY TGC "
						+"  WHERE  1 = 1"
						+"    AND TGC.PLANTID = :PLANTID"
						+"    AND TGC.COMPOSITIONID = :COMPOSITIONID"
						+"    AND TGC.PRODUCTGROUPID = (SELECT PRODUCTGROUPID FROM PRODUCTDEFINITION WHERE PRODUCTID = :PRODUCTID)"
						+"   ORDER BY ORDERINDEX"
						;
			
			bindMap.put("PLANTID", sPlantID);
			bindMap.put("COMPOSITIONID", CompositionID);
			bindMap.put("PRODUCTID", sProductID);

			List<Object> DataList = SqlMesTemplate.queryForList(Sql, bindMap);
			
			List<TGCPOLICY> TGCPolicyDataList = new ArrayList<TGCPOLICY>();

			for (int i = 0; i < DataList.size(); i++) 
			{
				HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(i);

				// Data 객체 생성
				TGCPOLICY TGCPolicyData = new TGCPOLICY();

				TGCPolicyData.setKeyPLANTID(dataMap.get("PLANTID"));
				TGCPolicyData.setKeyPRODUCTGROUPID(dataMap.get("PRODUCTGROUPID"));
				TGCPolicyData.setKeyCOMPOSITIONID(dataMap.get("COMPOSITIONID"));
				TGCPolicyData.setKeyRECIPEPARAMETERID(dataMap.get("RECIPEPARAMETERID"));
				TGCPolicyData.setSPECTYPE(dataMap.get("SPECTYPE"));
				TGCPolicyData.setTARGET(dataMap.get("TARGET"));
				TGCPolicyData.setLOWERSPECLIMIT(dataMap.get("LOWERSPECLIMIT"));
				TGCPolicyData.setUPPERSPECLIMIT(dataMap.get("UPPERSPECLIMIT"));
				TGCPolicyData.setLOWERCONTROLLIMIT(dataMap.get("LOWERCONTROLLIMIT"));			
				TGCPolicyData.setUPPERCONTROLLIMIT(dataMap.get("UPPERCONTROLLIMIT"));
				TGCPolicyData.setLOWERSCREENLIMIT(dataMap.get("LOWERSCREENLIMIT"));
				TGCPolicyData.setUPPERSCREENLIMIT(dataMap.get("UPPERSCREENLIMIT"));
				TGCPolicyData.setOBJECTTYPE(dataMap.get("OBJECTTYPE"));
				TGCPolicyData.setCTPFLAG(dataMap.get("CTPFLAG"));
				TGCPolicyData.setRECIPEPARAMETERMODE(dataMap.get("RECIPEPARAMETERMODE"));
				TGCPolicyData.setORDERINDEX(ConvertUtil.Object2Long(dataMap.get("ORDERINDEX")));
				TGCPolicyData.setALARMID(dataMap.get("ALARMID"));
				TGCPolicyData.setDESCRIPTION(dataMap.get("DESCRIPTION"));

				TGCPolicyDataList.add(TGCPolicyData);

			}

			return TGCPolicyDataList;
			
		}
		
		// RecipeName을 DetailData에 추가하여 줍니다.
		@SuppressWarnings("unchecked")
		public String RecipeParameterNameSearch(String PlantID, String ReicpeID) 
		{
			// Data 객체 생성
			RECIPEPARAMETER RecipeParameter = new RECIPEPARAMETER();
						// Key Value Setting
			RecipeParameter.setKeyPLANTID(PlantID);
			RecipeParameter.setKeyRECIPEPARAMETERID(ReicpeID);
			
			List<Object> RecipeParameterList = (List<Object>) RecipeParameter.excuteDML(SqlConstant.DML_SELECTLIST);
			RECIPEPARAMETER SelectData = (RECIPEPARAMETER) RecipeParameterList.get(0);

			return SelectData.getRECIPEPARAMETERNAME();
		}
		
		// ConsumableName을 DetailData에 추가하여 줍니다.
		@SuppressWarnings("unchecked")
		public String ConsumableNameSearch(String PlantID, String ConsumableID) 
		{
			// Data 객체 생성
			CONSUMABLEDEFINITION ConsumableDefinition = new CONSUMABLEDEFINITION();
						// Key Value Setting
			ConsumableDefinition.setKeyPLANTID(PlantID);
			ConsumableDefinition.setKeyCONSUMABLEID(ConsumableID);
			
			List<Object> ConsumableList = (List<Object>) ConsumableDefinition.excuteDML(SqlConstant.DML_SELECTLIST);
			CONSUMABLEDEFINITION SelectData = (CONSUMABLEDEFINITION) ConsumableList.get(0);

			return SelectData.getCONSUMABLENAME();
		}

}
