package mes.lot.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.lot.data.DY_PAINTINGPROCESS;
import mes.lot.data.LOTINFORMATION;
import mes.util.EventInfoUtil;
import mes.util.SendMessageUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.24
 * 
 * @see
 */
public class TxnPaintingProcess implements ObjectExecuteService{
	
	/**
	 * 도장실적을 등록, 수정 하는 트랜잭션 실행
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
        AddHistory history = new AddHistory();
        
        SendMessageUtil sendMessageUtil = new SendMessageUtil();
        List<HashMap<String, String>> listResultMap = new ArrayList<HashMap<String, String>>();
        
        HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(0);
        txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
        
        LOTINFORMATION lotinfomation = new LOTINFORMATION();

        String plantID = ConvertUtil.Object2String(dataMap.get("PLANTID"));
        String productID = "";
        String inputType = ConvertUtil.Object2String(dataMap.get("INPUTTYPE"));
        String scanID = ConvertUtil.Object2String(dataMap.get("SCANID"));
        
        // INPUTTYPE가 BarCode 일때
        if(inputType.equalsIgnoreCase("BarCode")) 
        {
        	DY_PAINTINGPROCESS dy_paintingProcess = new DY_PAINTINGPROCESS();
			dy_paintingProcess.setKeyPLANTID(dataMap.get("PLANTID"));
            dy_paintingProcess.setKeySCANID(dataMap.get("SCANID"));
            
            List<Object> dy_paintingProcessResult = (List<Object>) dy_paintingProcess.excuteDML(SqlConstant.DML_SELECTLIST);
            
            if(dy_paintingProcessResult != null && dy_paintingProcessResult.size() > 0)
            {
            	//skip (중복된 scanID 입니다.)
            	throw new CustomException("IPC-001", new Object[] {dy_paintingProcess.getKeySCANID()});
            }
            else
            {
            	//get ProductID : 해당 ScanID가 등록된 LOTID의 제품코드(ProductID)를 가져온다. 
            	String strSql = "";
            	strSql += " SELECT PRODUCTID                      ";
            	strSql += " FROM LOTINFORMATION L                 ";
            	strSql += " WHERE 1=1                             ";
            	strSql += "   AND PLANTID = :PLANTID              ";
            	strSql += "   AND MATERIALLOTID = :MATERIALLOTID  ";
            	strSql += " ORDER BY PLANTID, LOTID               ";
            	
            	HashMap<String, Object> bindMap = new HashMap<String, Object>();
        		bindMap.put("PLANTID",plantID);
        		bindMap.put("MATERIALLOTID",scanID);
        		
        		List<LinkedCaseInsensitiveMap> resultList = SqlMesTemplate.queryForList(strSql, bindMap);
        		
        		if (resultList != null && resultList.size() > 0 )
        		{
        			LinkedCaseInsensitiveMap resultMap = (LinkedCaseInsensitiveMap) resultList.get(0);
        			
        			productID = ConvertUtil.Object2String(resultMap.get("PRODUCTID"));
        			
        			//set Data
        			dy_paintingProcess.setPROCESSID(dataMap.get("PROCESSID"));
        			dy_paintingProcess.setEQUIPMENTID(dataMap.get("EQUIPMENTID"));
                 	dy_paintingProcess.setINPUTTYPE(dataMap.get("INPUTTYPE"));
                 	dy_paintingProcess.setDESCRIPTION(dataMap.get("DESCRIPTION"));
                 	dy_paintingProcess.setCREATETIME(DateUtil.getCurrentTimestamp());
                 	dy_paintingProcess.setCREATEUSERID(txnInfo.getTxnUser());                 	
                 	dy_paintingProcess.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
                 	dy_paintingProcess.setLASTUPDATEUSERID(txnInfo.getTxnUser());           
                 	                 	
        			dy_paintingProcess.setINPUTPRODUCTID(productID);
                 	dy_paintingProcess.setSTATE(dataMap.get("STATE"));
                 	dy_paintingProcess.setPROCESSSTARTTIME(DateUtil.getCurrentTimestamp());
                 	dy_paintingProcess.setPROCESSENDTIME(DateUtil.getCurrentTimestamp());
                 	
                 	dy_paintingProcess.excuteDML(SqlConstant.DML_INSERT);
             		
             		history.addHistory(dy_paintingProcess, txnInfo, SqlConstant.DML_INSERT);     
        		}
        		
        		// 2024.01.10-이수현 
        		// NG 저장 안해서 주석처리
				/*
				 * else { //등록된 제품코드가 없으면 실패 정보 등록.
				 * dy_paintingProcess.setINPUTTYPE(dataMap.get("INPUTTYPE"));
				 * dy_paintingProcess.setDESCRIPTION(dataMap.get("DESCRIPTION"));
				 * dy_paintingProcess.setCREATETIME(DateUtil.getCurrentTimestamp());
				 * dy_paintingProcess.setCREATEUSERID(txnInfo.getTxnUser());
				 * dy_paintingProcess.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
				 * dy_paintingProcess.setLASTUPDATEUSERID(txnInfo.getTxnUser());
				 * 
				 * dy_paintingProcess.setPROCESSFLAG(Constant.INPUTRESULT_NG);
				 * dy_paintingProcess.setPROCESSSTARTTIME(DateUtil.getCurrentTimestamp()); }
				 */

        		         
            }
        }
        // INPUTTYPE가 Manual 일때
        else if(inputType.equalsIgnoreCase("Manual"))
        {
        	DY_PAINTINGPROCESS dy_paintingProcess = new DY_PAINTINGPROCESS();
        	dy_paintingProcess.setKeyPLANTID(dataMap.get("PLANTID"));
            dy_paintingProcess.setKeySCANID(dataMap.get("SCANID"));
        	
            List<Object> ProcessDataResult = (List<Object>) dy_paintingProcess.excuteDML(SqlConstant.DML_SELECTLIST);
            
            dy_paintingProcess.setPROCESSID(dataMap.get("PROCESSID"));
            dy_paintingProcess.setEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            dy_paintingProcess.setINPUTPRODUCTID(dataMap.get("INPUTPRODUCTID"));
         	dy_paintingProcess.setINPUTTYPE(dataMap.get("INPUTTYPE"));
         	dy_paintingProcess.setSTATE(dataMap.get("STATE"));
         	dy_paintingProcess.setPROCESSSTARTTIME(DateUtil.getCurrentTimestamp());
         	dy_paintingProcess.setPROCESSENDTIME(DateUtil.getCurrentTimestamp());
         	dy_paintingProcess.setDESCRIPTION(dataMap.get("DESCRIPTION"));
         	dy_paintingProcess.setCREATETIME(DateUtil.getCurrentTimestamp());
         	dy_paintingProcess.setCREATEUSERID(txnInfo.getTxnUser());
         	
         	if (ProcessDataResult.size() > 0) 
    		{
    			dy_paintingProcess.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
    			dy_paintingProcess.setLASTUPDATEUSERID(txnInfo.getTxnUser());
    			dy_paintingProcess.excuteDML(SqlConstant.DML_UPDATE);
    			history.addHistory(dy_paintingProcess, txnInfo, SqlConstant.DML_UPDATE);
    		} else 
    		{
    			dy_paintingProcess.excuteDML(SqlConstant.DML_INSERT);
    			history.addHistory(dy_paintingProcess, txnInfo, SqlConstant.DML_INSERT);
    		}
        }
        return recvDoc;
	}

}
