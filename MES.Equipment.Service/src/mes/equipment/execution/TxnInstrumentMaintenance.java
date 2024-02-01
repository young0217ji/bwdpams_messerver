package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.equipment.data.DY_INSTRUMENTDETAIL;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.22 
 * 
 * @see
 */
public class TxnInstrumentMaintenance implements ObjectExecuteService
{
	/**
	 * 계측기에 대한 요청을 등록하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
	@Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        DY_INSTRUMENTDETAIL dy_InstrumentDetail = new DY_INSTRUMENTDETAIL();
        AddHistory history = new AddHistory();
	        
        //검교정 등록.
        for (int i = 0 ; i < masterData.size(); i ++)
        {
        	 HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
        	 txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
 	        
 	        String splantID = ConvertUtil.Object2String(dataMap.get("PLANTID"));
 	        String sitemNO = ConvertUtil.Object2String(dataMap.get("ITEMNO"));
 	        
 	        String eventType = ConvertUtil.Object2String(dataMap.get("ACTIONTYPE"));
 	        
 	        // eventType = 검교정 일때
        	if(eventType.equalsIgnoreCase("CALIBRATION"))
    	    {
	        	//set PK
	        	dy_InstrumentDetail.setKeyPLANTID(splantID);
	        	dy_InstrumentDetail.setKeyITEMNO(sitemNO);
	        	dy_InstrumentDetail.setKeyRELATIONTIMEKEY(DateUtil.getCurrentEventTimeKey());
		        	
	        	dy_InstrumentDetail.setACTIONTYPE(dataMap.get("ACTIONTYPE"));
	        	dy_InstrumentDetail.setACTIONTYPEDETAIL(dataMap.get("ACTIONTYPEDETAIL"));
	        	dy_InstrumentDetail.setACTIONRESULT(dataMap.get("ACTIONRESULT"));
	        	dy_InstrumentDetail.setACTIONCOMPANY(dataMap.get("ACTIONCOMPANY"));
	            dy_InstrumentDetail.setACTIONDEPARTMENT(dataMap.get("ACTIONDEPARTMENT"));
	            dy_InstrumentDetail.setACTIONCOMPANYUSERID(dataMap.get("ACTIONCOMPANYUSERID"));
	            dy_InstrumentDetail.setACTIONCOST(ConvertUtil.Object2Int(dataMap.get("ACTIONCOST")));
	            dy_InstrumentDetail.setDESCRIPTION(dataMap.get("DESCRIPTION"));
	            
	            dy_InstrumentDetail.setCREATETIME(DateUtil.getCurrentTimestamp());
	            dy_InstrumentDetail.setCREATEUSERID(txnInfo.getTxnUser());
	            dy_InstrumentDetail.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
	            dy_InstrumentDetail.setLASTUPDATEUSERID(txnInfo.getTxnUser());
	            	
	            dy_InstrumentDetail.excuteDML(SqlConstant.DML_INSERT);
	            
	            history.addHistory(dy_InstrumentDetail, txnInfo, SqlConstant.DML_INSERT);
    	    }
        	// eventType = 수리 일때
        	else if(eventType.equalsIgnoreCase("REPAIR"))
    	    {
	        	//set PK
	        	dy_InstrumentDetail.setKeyPLANTID(splantID);
	        	dy_InstrumentDetail.setKeyITEMNO(sitemNO);
	        	dy_InstrumentDetail.setKeyRELATIONTIMEKEY(DateUtil.getCurrentEventTimeKey());
	        	
	        	dy_InstrumentDetail.setACTIONTYPE(dataMap.get("ACTIONTYPE"));
	        	dy_InstrumentDetail.setACTIONTYPEDETAIL(dataMap.get("ACTIONTYPEDETAIL"));
	        	dy_InstrumentDetail.setACTIONRESULT(dataMap.get("ACTIONRESULT"));
	        	dy_InstrumentDetail.setACTIONCOMPANY(dataMap.get("ACTIONCOMPANY"));
	            dy_InstrumentDetail.setACTIONDEPARTMENT(dataMap.get("ACTIONDEPARTMENT"));
	            dy_InstrumentDetail.setACTIONCOMPANYUSERID(dataMap.get("ACTIONCOMPANYUSERID"));
	            dy_InstrumentDetail.setACTIONCOST(ConvertUtil.String2Int(dataMap.get("ACTIONCOST")));
	            dy_InstrumentDetail.setDESCRIPTION(dataMap.get("DESCRIPTION"));
	            
	            dy_InstrumentDetail.setCREATETIME(DateUtil.getCurrentTimestamp());
	            dy_InstrumentDetail.setCREATEUSERID(txnInfo.getTxnUser());
	            dy_InstrumentDetail.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
	            dy_InstrumentDetail.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            	
	            dy_InstrumentDetail.excuteDML(SqlConstant.DML_INSERT);
		            
	            history.addHistory(dy_InstrumentDetail, txnInfo, SqlConstant.DML_INSERT);
    	    }
        	// eventType = 폐기 일때
        	else if(eventType.equalsIgnoreCase("DISUSE"))
    	    {
	        	//set PK
	        	dy_InstrumentDetail.setKeyPLANTID(splantID);
	        	dy_InstrumentDetail.setKeyITEMNO(sitemNO);
	        	dy_InstrumentDetail.setKeyRELATIONTIMEKEY(DateUtil.getCurrentEventTimeKey());
	        	
	        	dy_InstrumentDetail.setACTIONTYPE(dataMap.get("ACTIONTYPE"));
	        	dy_InstrumentDetail.setACTIONTYPEDETAIL(dataMap.get("ACTIONTYPEDETAIL"));
	        	dy_InstrumentDetail.setACTIONRESULT(dataMap.get("ACTIONRESULT"));
	        	dy_InstrumentDetail.setACTIONCOMPANY(dataMap.get("ACTIONCOMPANY"));
	            dy_InstrumentDetail.setACTIONDEPARTMENT(dataMap.get("ACTIONDEPARTMENT"));
	            dy_InstrumentDetail.setACTIONCOMPANYUSERID(dataMap.get("ACTIONCOMPANYUSERID"));
	            dy_InstrumentDetail.setACTIONCOST(ConvertUtil.String2Int(dataMap.get("ACTIONCOST")));
	            dy_InstrumentDetail.setDESCRIPTION(dataMap.get("DESCRIPTION"));
	            
	            dy_InstrumentDetail.setCREATETIME(DateUtil.getCurrentTimestamp());
	            dy_InstrumentDetail.setCREATEUSERID(txnInfo.getTxnUser());
	            dy_InstrumentDetail.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
	            dy_InstrumentDetail.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            	
	            dy_InstrumentDetail.excuteDML(SqlConstant.DML_INSERT);
		            
	            history.addHistory(dy_InstrumentDetail, txnInfo, SqlConstant.DML_INSERT);
    	    }
        	// eventType = 대여 일때
        	else if(eventType.equalsIgnoreCase("RETAL"))
    	    {
	        	//set PK
	        	dy_InstrumentDetail.setKeyPLANTID(splantID);
	        	dy_InstrumentDetail.setKeyITEMNO(sitemNO);
	        	dy_InstrumentDetail.setKeyRELATIONTIMEKEY(DateUtil.getCurrentEventTimeKey());
	        	
	        	dy_InstrumentDetail.setACTIONTYPE(dataMap.get("ACTIONTYPE"));
	        	dy_InstrumentDetail.setACTIONTYPEDETAIL(dataMap.get("ACTIONTYPEDETAIL"));
	        	dy_InstrumentDetail.setACTIONRESULT(dataMap.get("ACTIONRESULT"));
	        	dy_InstrumentDetail.setACTIONCOMPANY(dataMap.get("ACTIONCOMPANY"));
	            dy_InstrumentDetail.setACTIONDEPARTMENT(dataMap.get("ACTIONDEPARTMENT"));
	            dy_InstrumentDetail.setACTIONCOMPANYUSERID(dataMap.get("ACTIONCOMPANYUSERID"));
	            dy_InstrumentDetail.setACTIONCOST(ConvertUtil.String2Int(dataMap.get("ACTIONCOST")));
	            dy_InstrumentDetail.setDESCRIPTION(dataMap.get("DESCRIPTION"));
	            
	            dy_InstrumentDetail.setCREATETIME(DateUtil.getCurrentTimestamp());
	            dy_InstrumentDetail.setCREATEUSERID(txnInfo.getTxnUser());
	            dy_InstrumentDetail.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
	            dy_InstrumentDetail.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            	
	            dy_InstrumentDetail.excuteDML(SqlConstant.DML_INSERT);
		            
	            history.addHistory(dy_InstrumentDetail, txnInfo, SqlConstant.DML_INSERT);
    	    }   
        	// eventType = 반납 일때
        	else if(eventType.equalsIgnoreCase("RETURN"))
    	    {
	        	//set PK
	        	dy_InstrumentDetail.setKeyPLANTID(splantID);
	        	dy_InstrumentDetail.setKeyITEMNO(sitemNO);
	        	dy_InstrumentDetail.setKeyRELATIONTIMEKEY(DateUtil.getCurrentEventTimeKey());
	        	
	        	dy_InstrumentDetail.setACTIONTYPE(dataMap.get("ACTIONTYPE"));
	        	dy_InstrumentDetail.setACTIONTYPEDETAIL(dataMap.get("ACTIONTYPEDETAIL"));
	        	dy_InstrumentDetail.setACTIONRESULT(dataMap.get("ACTIONRESULT"));
	        	dy_InstrumentDetail.setACTIONCOMPANY(dataMap.get("ACTIONCOMPANY"));
	            dy_InstrumentDetail.setACTIONDEPARTMENT(dataMap.get("ACTIONDEPARTMENT"));
	            dy_InstrumentDetail.setACTIONCOMPANYUSERID(dataMap.get("ACTIONCOMPANYUSERID"));
	            dy_InstrumentDetail.setACTIONCOST(ConvertUtil.String2Int(dataMap.get("ACTIONCOST")));
	            dy_InstrumentDetail.setDESCRIPTION(dataMap.get("DESCRIPTION"));
	            
	            dy_InstrumentDetail.setCREATETIME(DateUtil.getCurrentTimestamp());
	            dy_InstrumentDetail.setCREATEUSERID(txnInfo.getTxnUser());
	            dy_InstrumentDetail.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
	            dy_InstrumentDetail.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            	
	            dy_InstrumentDetail.excuteDML(SqlConstant.DML_INSERT);
		            
	            history.addHistory(dy_InstrumentDetail, txnInfo, SqlConstant.DML_INSERT);
    	    }
        }
        	return recvDoc;
    }
}
