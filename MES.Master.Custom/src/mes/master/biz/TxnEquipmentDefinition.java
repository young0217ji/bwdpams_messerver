package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.event.MessageParse;
import mes.master.data.AREA;
import mes.master.data.EQUIPMENT;
import mes.master.data.EQUIPMENTDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnEquipmentDefinition implements ObjectExecuteService
{
	/**
	 * 설비의 기준정보를 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	 @Override
	 public Object execute(Document recvDoc)
	 {
		 List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
		 TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		 String sPlantID = MessageParse.getParam(recvDoc, "PLANTID");
		 String sEquipmentID = MessageParse.getParam(recvDoc, "EQUIPMENTID");
		 String sEquipmentName = MessageParse.getParam(recvDoc, "EQUIPMENTNAME");
		 String sEquipmentType = MessageParse.getParam(recvDoc, "EQUIPMENTTYPE");
		 String sEquipmentDetailType = MessageParse.getParam(recvDoc, "EQUIPMENTDETAILTYPE");
		 String sActiveState = MessageParse.getParam(recvDoc, "ACTIVESTATE");
		 String sWorkCenter = MessageParse.getParam(recvDoc, "AREAID");
		 String sCommunicationState = MessageParse.getParam(recvDoc, "COMMUNICATIONSTATE");
		 String sPMCycleType = MessageParse.getParam(recvDoc, "PMCYCLETYPE");
		 String sPMCycleValue = MessageParse.getParam(recvDoc, "PMCYCLEVALUE");
		 String sRowStatus = MessageParse.getParam(recvDoc, "ROWSTATUS");
		 
		 EQUIPMENTDEFINITION oMainDataInfo = new EQUIPMENTDEFINITION();
         // Key Value Setting
		 oMainDataInfo.setKeyPLANTID(sPlantID);
		 oMainDataInfo.setKeyEQUIPMENTID(sEquipmentID);
		 
         // Key 에 대한 DataInfo 조회
         if ( "C".equals(sRowStatus) == false ) {
        	 oMainDataInfo = (EQUIPMENTDEFINITION) oMainDataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
         }
         
         oMainDataInfo.setEQUIPMENTNAME(sEquipmentName);
         oMainDataInfo.setEQUIPMENTTYPE(sEquipmentType);
         oMainDataInfo.setEQUIPMENTDETAILTYPE(sEquipmentDetailType);
         oMainDataInfo.setACTIVESTATE(sActiveState);
         oMainDataInfo.setAREAID(sWorkCenter);
         oMainDataInfo.setSUPEREQUIPMENTID("");      
         oMainDataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
         oMainDataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
		 
         // DataInfo에  대한 CUD 실행
         if ( "D".equals(sRowStatus) ) {
        	 oMainDataInfo.excuteDML(SqlConstant.DML_DELETE);
             
             //삭제시 같이 EQUIPMENT항목 같이 삭제
             EquipmentDelete(oMainDataInfo, txnInfo);
             
             // 전체 하위 설비 삭제
             EQUIPMENTDEFINITION subDataInfo = new EQUIPMENTDEFINITION();
             subDataInfo.setKeyPLANTID(oMainDataInfo.getKeyPLANTID());
             subDataInfo.setSUPEREQUIPMENTID(oMainDataInfo.getKeyEQUIPMENTID());
             
             List<EQUIPMENTDEFINITION> listSubEquipment = (List<EQUIPMENTDEFINITION>) subDataInfo.excuteDML(SqlConstant.DML_SELECTLIST);
             
             for ( int i = 0; i < listSubEquipment.size(); i++ ) {
            	 txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
            	 
            	 subDataInfo = listSubEquipment.get(i);
            	 subDataInfo.excuteDML(SqlConstant.DML_DELETE);
            	 
            	//삭제시 같이 EQUIPMENT항목 같이 삭제
	             EquipmentDelete(subDataInfo, txnInfo);
             }
         }
         else if ( "C".equals(sRowStatus) ) {
        	 oMainDataInfo.setCREATEUSERID(txnInfo.getTxnUser());
             oMainDataInfo.setCREATETIME(txnInfo.getEventTime());

             oMainDataInfo.excuteDML(SqlConstant.DML_INSERT);
             
             //성성시 같이 EQUIPMENT항목 같이 생성
             EquipmentInsert(oMainDataInfo, txnInfo, sCommunicationState, sPMCycleType, ConvertUtil.Object2Long(sPMCycleValue));
         }
         else if ( "U".equals(sRowStatus) ) {
        	 oMainDataInfo.excuteDML(SqlConstant.DML_UPDATE);
             
             // 수정시 같이 EUQIPMENT항목 같이 수정
	         EquipmentUpdate(oMainDataInfo, txnInfo, sCommunicationState, sPMCycleType, ConvertUtil.Object2Long(sPMCycleValue));
         }
         
         if ( "D".equals(sRowStatus) == false ) {
        	 for (int i = 0 ; i < masterData.size(); i ++) {

        		 HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
        		 txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

        		 EQUIPMENTDEFINITION oSubDataInfo = new EQUIPMENTDEFINITION();
        		 // Key Value Setting
        		 oSubDataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        		 oSubDataInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));

        		 // Key 에 대한 DataInfo 조회
        		 if ( "C".equals(dataMap.get("_ROWSTATUS")) == false ) {
        			 oSubDataInfo = (EQUIPMENTDEFINITION) oSubDataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
        		 }

        		 // Data Value Setting
        		 oSubDataInfo.setEQUIPMENTNAME(dataMap.get("EQUIPMENTNAME"));
        		 oSubDataInfo.setEQUIPMENTTYPE(dataMap.get("EQUIPMENTTYPE"));
        		 oSubDataInfo.setEQUIPMENTDETAILTYPE(dataMap.get("EQUIPMENTDETAILTYPE"));
        		 oSubDataInfo.setACTIVESTATE(dataMap.get("ACTIVESTATE"));
        		 oSubDataInfo.setAREAID(dataMap.get("AREAID"));
        		 oSubDataInfo.setSUPEREQUIPMENTID(oMainDataInfo.getKeyEQUIPMENTID());
        		 oSubDataInfo.setEQUIPMENTGROUPID(dataMap.get("EQUIPMENTGROUPID"));
        		 oSubDataInfo.setPROCESSUNIT(dataMap.get("PROCESSUNIT"));
        		 oSubDataInfo.setPROCESSCAPACITY(dataMap.get("PROCESSCAPACITY"));
        		 oSubDataInfo.setPROCESSGROUPSIZEMIN(ConvertUtil.Object2Long(dataMap.get("PROCESSGROUPSIZEMIN")));
        		 oSubDataInfo.setPROCESSGROUPSIZEMAX(ConvertUtil.Object2Long(dataMap.get("PROCESSGROUPSIZEMAX")));
        		 oSubDataInfo.setDEFUALTRECIPENAMESPACENAME(dataMap.get("DEFUALTRECIPENAMESPACENAME"));
        		 oSubDataInfo.setSTATEMODELID(dataMap.get("STATEMODELID"));
        		 oSubDataInfo.setCOSTCENTER(dataMap.get("COSTCENTER"));
        		 oSubDataInfo.setVOLUME(ConvertUtil.Object2Long(dataMap.get("VOLUME")));
        		 oSubDataInfo.setPRODUCTIONTYPE(dataMap.get("PRODUCTIONTYPE"));
        		 oSubDataInfo.setAUTOFLAG(dataMap.get("AUTOFLAG"));
        		 oSubDataInfo.setLOCATIONID(dataMap.get("LOCATIONID"));
        		 oSubDataInfo.setMODELNUMBER(dataMap.get("MODELNUMBER"));
        		 oSubDataInfo.setSERIALNUMBER(dataMap.get("SERIALNUMBER"));
        		 oSubDataInfo.setPURCHASEDATE(dataMap.get("PURCHASEDATE"));
        		 oSubDataInfo.setVENDOR(dataMap.get("VENDOR"));
        		 oSubDataInfo.setVENDORDESCRIPTION(dataMap.get("VENDORDESCRIPTION"));
        		 oSubDataInfo.setVENDORADDRESS(dataMap.get("VENDORADDRESS"));
        		 oSubDataInfo.setMAKEDATE(dataMap.get("MAKEDATE"));
        		 oSubDataInfo.setMAKER(dataMap.get("MAKER"));
        		 oSubDataInfo.setMAKERADDRESS(dataMap.get("MAKERADDRESS"));
        		 oSubDataInfo.setSUBCONTRACTOR(dataMap.get("SUBCONTRACTOR"));
        		 oSubDataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
        		 oSubDataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
        		 oSubDataInfo.setLASTUPDATETIME(txnInfo.getEventTime());

        		 // DataInfo에  대한 CUD 실행
        		 if ( "D".equals(dataMap.get("_ROWSTATUS")) ) {
        			 oSubDataInfo.excuteDML(SqlConstant.DML_DELETE);

        			 //삭제시 같이 EQUIPMENT항목 같이 삭제
        			 EquipmentDelete(oSubDataInfo, txnInfo);
        		 }
        		 else if ( "C".equals(dataMap.get("_ROWSTATUS")) ) {
        			 oSubDataInfo.setCREATEUSERID(txnInfo.getTxnUser());
        			 oSubDataInfo.setCREATETIME(txnInfo.getEventTime());

        			 oSubDataInfo.excuteDML(SqlConstant.DML_INSERT);

        			 //성성시 같이 EQUIPMENT항목 같이 생성
        			 EquipmentInsert(oSubDataInfo, txnInfo, dataMap.get("COMMUNICATIONSTATE"), dataMap.get("PMCYCLETYPE"), ConvertUtil.Object2Long(dataMap.get("PMCYCLEVALUE")));
        		 }
        		 else if ( "U".equals(dataMap.get("_ROWSTATUS")) ) {
        			 oSubDataInfo.excuteDML(SqlConstant.DML_UPDATE);

        			 // 수정시 같이 EUQIPMENT항목 같이 수정
        			 EquipmentUpdate(oSubDataInfo, txnInfo, dataMap.get("COMMUNICATIONSTATE"), dataMap.get("PMCYCLETYPE"), ConvertUtil.Object2Long(dataMap.get("PMCYCLEVALUE")));
        		 }
        	 }
         }
		 
        return recvDoc;
	 }
	 
	 // Equipment테이블 데이터 생성
	 public void EquipmentInsert(EQUIPMENTDEFINITION sEquipmentDefinition, TxnInfo txnInfo, String CommunicationState, String PMCycleType, Long PMCycleValue) {
		 
		 EQUIPMENT EquipmentData = new EQUIPMENT();
		 
         // Key Value Setting
		 EquipmentData.setKeyPLANTID(sEquipmentDefinition.getKeyPLANTID());
		 EquipmentData.setKeyEQUIPMENTID(sEquipmentDefinition.getKeyEQUIPMENTID());
		 EquipmentData.setCOMMUNICATIONSTATE(CommunicationState);
		 EquipmentData.setAREAID(sEquipmentDefinition.getAREAID());
		 EquipmentData.setEQUIPMENTSTATE(Constant.EQUIPMENTSTATE_IDLE);
		 EquipmentData.setBATCHCOUNT(ConvertUtil.Object2Long("0"));
		 EquipmentData.setPMCYCLETYPE(PMCycleType);
		 EquipmentData.setPMCYCLEVALUE(PMCycleValue);
		 EquipmentData.setLASTMAINTENANCETIME(txnInfo.getEventTime());
		 EquipmentData.setLASTMAINTENANCEUSERID(txnInfo.getTxnUser());
		 EquipmentData.setLASTUPDATEUSERID(txnInfo.getTxnUser());
		 EquipmentData.setLASTUPDATETIME(txnInfo.getEventTime());
		 EquipmentData.setCREATEUSERID(txnInfo.getTxnUser());
		 EquipmentData.setCREATETIME(txnInfo.getEventTime());

		 EquipmentData.excuteDML(SqlConstant.DML_INSERT);
		 
         AddHistory history = new AddHistory();
         history.addHistory(EquipmentData, txnInfo,"C");
	 }
	 
	 // Equipment테이블 데이터 삭제
	 public void EquipmentDelete(EQUIPMENTDEFINITION sEquipmentDefinition, TxnInfo txnInfo) {
		 
		 EQUIPMENT EquipmentData = new EQUIPMENT();
         // Key Value Setting
		 EquipmentData.setKeyPLANTID(sEquipmentDefinition.getKeyPLANTID());
		 EquipmentData.setKeyEQUIPMENTID(sEquipmentDefinition.getKeyEQUIPMENTID());
		 EquipmentData = (EQUIPMENT) EquipmentData.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
         
		 EquipmentData.excuteDML(SqlConstant.DML_DELETE);
		 
         AddHistory history = new AddHistory();
         history.addHistory(EquipmentData, txnInfo,"D");
	 }
	 
	 // Equipment테이블 데이터 수정
	 public void EquipmentUpdate(EQUIPMENTDEFINITION sEquipmentDefinition, TxnInfo txnInfo, String CommunicationState, String PMCycleType, Long PMCycleValue) {
		 
		 EQUIPMENT EquipmentData = new EQUIPMENT();
         
		 EquipmentData.setKeyPLANTID(sEquipmentDefinition.getKeyPLANTID());
		 EquipmentData.setKeyEQUIPMENTID(sEquipmentDefinition.getKeyEQUIPMENTID());
		 EquipmentData = (EQUIPMENT) EquipmentData.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
         
		 EquipmentData.setCOMMUNICATIONSTATE(CommunicationState);
		 EquipmentData.setAREAID(sEquipmentDefinition.getAREAID());
		 EquipmentData.setPMCYCLETYPE(PMCycleType);
		 EquipmentData.setPMCYCLEVALUE(PMCycleValue);
		 EquipmentData.setAREAID(sEquipmentDefinition.getAREAID());
		 EquipmentData.setLASTUPDATEUSERID(txnInfo.getTxnUser());
		 EquipmentData.setLASTUPDATETIME(txnInfo.getEventTime());
		 
		 EquipmentData.excuteDML(SqlConstant.DML_UPDATE);
		 
         AddHistory history = new AddHistory();
         history.addHistory(EquipmentData, txnInfo,"U");
	 }
}
