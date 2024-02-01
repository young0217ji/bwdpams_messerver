package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.lot.data.DY_DEFECTREPORT;
import mes.lot.data.DY_DEFECTREPORTITEM;
import mes.lot.data.DY_INSPECTIONREPORT;
import mes.lot.data.DY_INSPECTIONREPORTSERIAL;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

/**
 * 
 * @author LSMESSolution
 * 
 * @since 2023.04.04
 * 
 * @see
 *
 */

public class TxnInspectionReportIPC implements ObjectExecuteService
{
	
	/**
	 * 검사성적등록 트랜젝션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 */
    @Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        AddHistory history = new AddHistory();
        
        NameGenerator nameGenerator = new NameGenerator();
        DY_INSPECTIONREPORT dyInspectionReport = new DY_INSPECTIONREPORT();
        
        HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(0);
        
        String plantID = MessageParse.getParam(recvDoc, "PLANTID");
        String WorkOrderID = MessageParse.getParam(recvDoc, "WORKORDERID");
        String InspectionSequence = "";
        if(dataMap.get("_ROWSTATUS").equals("C")) {
        	InspectionSequence = nameGenerator.nameGenerate( dataMap.get("PLANTID"), "InspectionSequence", new Object[] {dataMap.get("PLANTID")} );
        }else {
        	InspectionSequence = dataMap.get("INSPECTIONSEQUENCE");
        }
        
        InspectionSequence = InspectionSequence.replace(dataMap.get("PLANTID") + "-", "");
        
        String InspectionDate = MessageParse.getParam(recvDoc, "INSPECTIONDATE");
        String ProductId = MessageParse.getParam(recvDoc, "PRODUCTID");
        String Workcenter = MessageParse.getParam(recvDoc, "WORKCENTER");
        String InspectionSerialType = MessageParse.getParam(recvDoc, "INSPECTIONSERIALTYPE");
        String EndProductID = MessageParse.getParam(recvDoc, "ENDPRODUCTID");
        String USERPRODUCTID = MessageParse.getParam(recvDoc, "USERPRODUCTID");
        String InspectionQuantity = MessageParse.getParam(recvDoc, "INSPECTIONQUANTITY");
        String GoodQuantity = MessageParse.getParam(recvDoc, "GOODQUANTITY");
        String InspectionMethod = MessageParse.getParam(recvDoc, "INSPECTIONMETHOD");
        String HcInsideDia = MessageParse.getParam(recvDoc, "HC_INSIDE_DIA");
        String HcSidth = MessageParse.getParam(recvDoc, "HC_SIDTH");
        String KcInsideDia = MessageParse.getParam(recvDoc, "KC_INSIDE_DIA");
        String KcSidth = MessageParse.getParam(recvDoc, "KC_SIDTH");
        String ClosedLength = MessageParse.getParam(recvDoc, "CLOSED_LENTH");
        String LengthRcToKc = MessageParse.getParam(recvDoc, "LENGTH_RC_TO_KC");
        String Description = MessageParse.getParam(recvDoc, "DESCRIPTION");
        String createUserID = MessageParse.getParam(recvDoc, "CREATEUSERID");
        String lastUpdateUserID = MessageParse.getParam(recvDoc, "LASTUPDATEUSERID");
        
        txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
        
        // Key Value Setting
        dyInspectionReport.setKeyPLANTID(plantID);
        dyInspectionReport.setKeyWORKORDERID(WorkOrderID);
        dyInspectionReport.setKeyINSPECTIONSEQUENCE(ConvertUtil.Object2Int(InspectionSequence));
        
        // Data Value Setting
        dyInspectionReport.setINSPECTIONDATE(InspectionDate);
        dyInspectionReport.setPRODUCTID(ProductId);
        dyInspectionReport.setWORKCENTER(Workcenter);
        dyInspectionReport.setINSPECTIONSERIALTYPE(InspectionSerialType);
        dyInspectionReport.setENDPRODUCTID(EndProductID);
        dyInspectionReport.setUSERPRODUCTID(USERPRODUCTID);
        dyInspectionReport.setINSPECTIONQUANTITY(ConvertUtil.String2Int(InspectionQuantity));
        dyInspectionReport.setGOODQUANTITY(ConvertUtil.String2Int(GoodQuantity));
        dyInspectionReport.setINSPECTIONMETHOD(InspectionMethod);
        dyInspectionReport.setHC_INSIDE_DIA(HcInsideDia);
        dyInspectionReport.setHC_SIDTH(HcSidth);
        dyInspectionReport.setKC_INSIDE_DIA(KcInsideDia);
        dyInspectionReport.setKC_SIDTH(KcSidth);
        dyInspectionReport.setCLOSED_LENTH(ClosedLength);
        dyInspectionReport.setLENGTH_RC_TO_KC(LengthRcToKc);
        dyInspectionReport.setDESCRIPTION(Description);
        dyInspectionReport.setCREATEUSERID(txnInfo.getTxnUser());
        dyInspectionReport.setLASTUPDATEUSERID(txnInfo.getTxnUser());

        dyInspectionReport.setCREATETIME(DateUtil.getCurrentTimestamp());
        dyInspectionReport.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
        
        if(dataMap.get("_ROWSTATUS").equals("C")) {
            dyInspectionReport.excuteDML(SqlConstant.DML_INSERT);
        }else if(dataMap.get("_ROWSTATUS").equals("U")) {
        	dyInspectionReport.excuteDML(SqlConstant.DML_UPDATE);
        }

    	
    	// History 기록이 필요한 경우 사용
    	history.addHistory(dyInspectionReport, txnInfo, SqlConstant.DML_INSERT);
        
    	for (int i = 0 ; i < masterData.size(); i ++)
        {
    		HashMap<String, String> dataMaps = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_INSPECTIONREPORTSERIAL dyInspectionReportSerial = new DY_INSPECTIONREPORTSERIAL();
            
            // Key Value Setting
            dyInspectionReportSerial.setKeyPLANTID(dataMaps.get("PLANTID"));
            dyInspectionReportSerial.setKeyWORKORDERID(dataMaps.get("WORKORDERID"));
            dyInspectionReportSerial.setKeyINSPECTIONSEQUENCE(ConvertUtil.Object2Int(InspectionSequence));
            dyInspectionReportSerial.setKeySERIALNUMBER(dataMaps.get("SERIALNUMBER"));

            // Data Value Setting
            dyInspectionReportSerial.setAPPEARN(dataMaps.get("APPEARN"));
            dyInspectionReportSerial.setWORKABILITY(dataMaps.get("WORKABILITY"));
            dyInspectionReportSerial.setPROOF_TEST(dataMaps.get("PROOF_TEST"));
            dyInspectionReportSerial.setINTERNAL_LEAKAGE(dataMaps.get("INTERNAL_LEAKAGE"));
            dyInspectionReportSerial.setEXTERNAL_LEAKAGE(dataMaps.get("EXTERNAL_LEAKAGE"));
            dyInspectionReportSerial.setMIN_OPER_PRESS(dataMaps.get("MIN_OPER_PRESS"));
            dyInspectionReportSerial.setWELDING(dataMaps.get("WELDING"));
            dyInspectionReportSerial.setROD_SURFACE(dataMaps.get("ROD_SURFACE"));
            dyInspectionReportSerial.setTHREAD(dataMaps.get("THREAD"));
            dyInspectionReportSerial.setSTROKE(dataMaps.get("STROKE"));
            dyInspectionReportSerial.setMARK_POINT(dataMaps.get("MARK_POINT"));
            dyInspectionReportSerial.setLOTID(dataMaps.get("LOTID"));
            dyInspectionReportSerial.setDESCRIPTION(dataMaps.get("DESCRIPTION"));
            
            dyInspectionReportSerial.setCREATEUSERID(txnInfo.getTxnUser());
            dyInspectionReportSerial.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            dyInspectionReportSerial.setCREATETIME(DateUtil.getCurrentTimestamp());
            dyInspectionReportSerial.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
            
            if(dataMap.get("_ROWSTATUS").equals("C")) {
            	dyInspectionReportSerial.excuteDML(SqlConstant.DML_INSERT);
            }else if(dataMap.get("_ROWSTATUS").equals("U")) {
            	dyInspectionReportSerial.excuteDML(SqlConstant.DML_UPDATE);
            }
            
            // History 기록이 필요한 경우 사용
            history.addHistory(dyInspectionReportSerial, txnInfo, SqlConstant.DML_INSERT);
        }

        return recvDoc;
    }

}
