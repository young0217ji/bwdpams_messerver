package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_WORKERCHANGE;
import mes.util.EventInfoUtil;

public class TxnWorkerChange implements ObjectExecuteService{

	/**
	 * 5M 인원변동 조회 수정 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		
        for (int i = 0 ; i < masterData.size(); i ++){
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
            
            DY_WORKERCHANGE dataInfo = new DY_WORKERCHANGE();
            
	         // Key Value Setting
	         dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
	         dataInfo.setKeyPROCESSID(dataMap.get("PROCESSID"));
	         dataInfo.setKeyCHANGEDATE(dataMap.get("CHANGEDATE"));
	         dataInfo.setKeyCHANGEWORKER(dataMap.get("CHANGEWORKER"));
	         
	         // Key에 대한 DataInfo 조회
	         if(!dataMap.get("_ROWSTATUS").equals("C")) {
	        	 dataInfo = (DY_WORKERCHANGE)dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
	         }
	         
	         dataInfo.setPRVWORKER(dataMap.get("PRVWORKER"));
	         dataInfo.setCHANGETYPE(dataMap.get("CHANGETYPE"));
	         dataInfo.setSPECIALQUALIFICATIONFLAG(dataMap.get("SPECIALQUALIFICATIONFLAG"));
	         dataInfo.setEDUCATIONFLAG(dataMap.get("EDUCATIONFLAG"));
	         dataInfo.setSTANDARDDOCUMENTFLAG(dataMap.get("STANDARDDOCUMENTFLAG"));
	         dataInfo.setDRAWINGFLAG(dataMap.get("DRAWINGFLAG"));
	         dataInfo.setINSPECTIONSTANDARDFLAG(dataMap.get("INSPECTIONSTANDARDFLAG"));
	         dataInfo.setINSPECTIONDOCUMENTFLAG(dataMap.get("INSPECTIONDOCUMENTFLAG"));
	         dataInfo.setMEASURINGINSTRUMENTFLAG(dataMap.get("MEASURINGINSTRUMENTFLAG"));
	         dataInfo.setMONITORINGRESULT(dataMap.get("MONITORINGRESULT"));
	         dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
	         dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
             dataInfo.setCREATETIME(txnInfo.getEventTime());
             dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
             dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
	         
	         // DataInfo에 대한 CUD 실행
            if(dataMap.get("_ROWSTATUS").equals("D")) {
            	dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }else if(dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }else if(dataMap.get("_ROWSTATUS").equals("U")) {
            	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
	         
        }
        
        return recvDoc;
            
            
    }
}













