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

public class TxnDefectReport implements ObjectExecuteService
{
	
	/**
	 * 부적합 등록 하는 트랜젝션 실행
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
        DY_DEFECTREPORT dy_defectReport = new DY_DEFECTREPORT();
        
        
        HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(0);
        
        String plantID = MessageParse.getParam(recvDoc, "PLANTID");
        String DefectReportID = nameGenerator.nameGenerate( plantID, "DEFECTREPORTID", new Object[] {} );
        String productID = MessageParse.getParam(recvDoc, "PRODUCTID");
        String workorderID = MessageParse.getParam(recvDoc, "WORKORDERID");
        String processID = MessageParse.getParam(recvDoc, "PROCESSID");
        String erpProcessID = MessageParse.getParam(recvDoc, "ERPPROCESSID");
        String lotID = MessageParse.getParam(recvDoc, "LOTID");
        String equipmentID = MessageParse.getParam(recvDoc, "EQUIPMENTID");
        String defectType = MessageParse.getParam(recvDoc, "DEFECTTYPE");
        String defectStartDate = MessageParse.getParam(recvDoc, "DEFECTSTARTDATE");
        String defectEndDate = MessageParse.getParam(recvDoc, "DEFECTENDDATE");
        String defectQTY = MessageParse.getParam(recvDoc, "DEFECTQTY");
        String defectDescription = MessageParse.getParam(recvDoc, "DEFECTDESCRIPTION");
        String qualityReportTarget = MessageParse.getParam(recvDoc, "QUALITYREPORTTARGET");
        String createUserID = MessageParse.getParam(recvDoc, "CREATEUSERID");
        String lastUpdateUserID = MessageParse.getParam(recvDoc, "LASTUPDATEUSERID");
        
        txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
        
        // Key Value Setting
        dy_defectReport.setKeyPLANTID(plantID);
        dy_defectReport.setKeyDEFECTREPORTID(DefectReportID);
        
        // Data Value Setting
        dy_defectReport.setPRODUCTID(productID);
        dy_defectReport.setWORKORDERID(workorderID);
        dy_defectReport.setPROCESSID(processID);
        dy_defectReport.setERPPROCESSID(erpProcessID);
        dy_defectReport.setLOTID(lotID);
        dy_defectReport.setEQUIPMENTID(equipmentID);
        dy_defectReport.setDEFECTTYPE(defectType);
        dy_defectReport.setDEFECTSTARTDATE(defectStartDate);
        dy_defectReport.setDEFECTENDDATE(defectEndDate);
        dy_defectReport.setDEFECTQTY(ConvertUtil.Object2Int(defectQTY));
        dy_defectReport.setDEFECTDESCRIPTION(defectDescription);
        dy_defectReport.setQUALITYREPORTTARGET(qualityReportTarget);
        
        dy_defectReport.setCREATEUSERID(txnInfo.getTxnUser());
        dy_defectReport.setLASTUPDATEUSERID(txnInfo.getTxnUser());
        dy_defectReport.setREPORTDATE(DateUtil.getCurrentTimestamp());
        dy_defectReport.setCREATETIME(DateUtil.getCurrentTimestamp());
        dy_defectReport.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
    	dy_defectReport.excuteDML(SqlConstant.DML_INSERT);
    	
    	// History 기록이 필요한 경우 사용
    	history.addHistory(dy_defectReport, txnInfo, SqlConstant.DML_INSERT);
        
    	for (int i = 0 ; i < masterData.size(); i ++)
        {
    		HashMap<String, String> dataMaps = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_DEFECTREPORTITEM dy_defectReportItem = new DY_DEFECTREPORTITEM();

            // Key Value Setting
            dy_defectReportItem.setKeyPLANTID(plantID);
            dy_defectReportItem.setKeyDEFECTREPORTID(DefectReportID);
            dy_defectReportItem.setKeyMATERIALLOTID(dataMaps.get("MATERIALLOTID"));
            
            // Data Value Setting
            dy_defectReportItem.setSTOCKLOCATION(dataMaps.get("STOCKLOCATION"));
            dy_defectReportItem.setPLANQUANTITY(ConvertUtil.Object2Int(dataMaps.get("PLANQUANTITY")));
            dy_defectReportItem.setDEFECTQUANTITY(ConvertUtil.Object2Int(dataMaps.get("DEFECTQUANTITY")));
            
            dy_defectReportItem.setCREATEUSERID(txnInfo.getTxnUser());
            dy_defectReportItem.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            dy_defectReportItem.setCREATETIME(DateUtil.getCurrentTimestamp());
            dy_defectReportItem.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
            dy_defectReportItem.excuteDML(SqlConstant.DML_INSERT);
            
            // History 기록이 필요한 경우 사용
            history.addHistory(dy_defectReportItem, txnInfo, SqlConstant.DML_INSERT);
        }

        return recvDoc;
    }

}
