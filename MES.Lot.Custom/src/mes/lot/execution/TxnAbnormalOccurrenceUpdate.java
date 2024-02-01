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
import mes.lot.custom.DYInterfaceManager;
import mes.master.data.DY_ABNORMALOCCURRENCE;
import mes.master.data.DY_ABNORMALOCCURRENCEDETAIL;
import mes.util.EventInfoUtil;

/**
 * 
 * @author LSMESSolution
 * 
 * @since 2023.04.04
 * 
 * @see
 *
 */

public class TxnAbnormalOccurrenceUpdate implements ObjectExecuteService {

	/**
	 * 이상발생을 수정하는 트랜젝션 실행
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
		
		DY_ABNORMALOCCURRENCE dy_abnormalOccurrence = new DY_ABNORMALOCCURRENCE();
		DY_ABNORMALOCCURRENCEDETAIL dy_abnormalOccurrenceDetail = new DY_ABNORMALOCCURRENCEDETAIL();
	
		String plantID = MessageParse.getParam(recvDoc, "PLANTID");
		
		for(int i = 0; i< masterData.size(); i++)
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) masterData.get(i);
			
			String _rowStatus = ConvertUtil.Object2String(dataMap.get("_ROWSTATUS"));	
			String dataKey = ConvertUtil.Object2String(dataMap.get("DATAKEY"));
			String workorderID = ConvertUtil.Object2String(dataMap.get("WORKORDERID"));
			String productID = ConvertUtil.Object2String(dataMap.get("PRODUCTID"));
			String processID = ConvertUtil.Object2String(dataMap.get("PROCESSID"));
			String equipmentID = ConvertUtil.Object2String(dataMap.get("EQUIPMENTID"));
			String abnormalType = ConvertUtil.Object2String(dataMap.get("ABNORMALTYPE"));
			String abnormalState = ConvertUtil.Object2String(dataMap.get("ABNORMALSTATE"));
			String occurrenceCode = ConvertUtil.Object2String(dataMap.get("OCCURRENCECODE"));
			String occurrenceComment = ConvertUtil.Object2String(dataMap.get("OCCURRENCECOMMENT"));
			String occurrenceTime = ConvertUtil.Object2String(dataMap.get("OCCURRENCETIME"));
			String occurrenceUserID = ConvertUtil.Object2String(dataMap.get("OCCURRENCEUSERID"));
			String arrivalTime = ConvertUtil.Object2String(dataMap.get("ARRIVALTIME"));
			String arrivalUserID = ConvertUtil.Object2String(dataMap.get("ARRIVALUSERID"));
			String arrivalResult = ConvertUtil.Object2String(dataMap.get("ARRIVALRESULT"));
			String finishTime = ConvertUtil.Object2String(dataMap.get("FINISHTIME"));
			String finishUserID = ConvertUtil.Object2String(dataMap.get("FINISHUSERID"));
			String finishResult = ConvertUtil.Object2String(dataMap.get("FINISHRESULT"));
			String finishComment = ConvertUtil.Object2String(dataMap.get("FINISHCOMMENT"));
			String description = ConvertUtil.Object2String(dataMap.get("DESCRIPTION"));
			String itemID = ConvertUtil.Object2String(dataMap.get("ITEMID"));

			// DY_ABNORMALOCCURRENCE
			// Key Value Setting
			dy_abnormalOccurrence.setKeyPLANTID(plantID);
			dy_abnormalOccurrence.setKeyDATAKEY(dataKey);
	
			// Data Value Setting
			dy_abnormalOccurrence.setWORKORDERID(workorderID);
			dy_abnormalOccurrence.setPRODUCTID(productID);
			dy_abnormalOccurrence.setPROCESSID(processID);
			dy_abnormalOccurrence.setEQUIPMENTID(equipmentID);
			dy_abnormalOccurrence.setABNORMALTYPE(abnormalType);
			dy_abnormalOccurrence.setABNORMALSTATE(abnormalState);
			dy_abnormalOccurrence.setOCCURRENCECODE(occurrenceCode);
			dy_abnormalOccurrence.setOCCURRENCECOMMENT(occurrenceComment);
			dy_abnormalOccurrence.setOCCURRENCETIME(DateUtil.getTimestamp(occurrenceTime));
			dy_abnormalOccurrence.setOCCURRENCEUSERID(txnInfo.getTxnUser());
					
			
			dy_abnormalOccurrence.setABNORMALSTATE(abnormalState);
			dy_abnormalOccurrence.setARRIVALTIME(DateUtil.getTimestamp(arrivalTime));
			dy_abnormalOccurrence.setARRIVALUSERID(arrivalUserID);
			dy_abnormalOccurrence.setARRIVALRESULT(arrivalResult);
			dy_abnormalOccurrence.setFINISHTIME(DateUtil.getTimestamp(finishTime));
			dy_abnormalOccurrence.setFINISHUSERID(finishUserID);
			dy_abnormalOccurrence.setFINISHRESULT(finishResult);
			dy_abnormalOccurrence.setFINISHCOMMENT(finishComment);
			dy_abnormalOccurrence.setDESCRIPTION(description);
			dy_abnormalOccurrence.setLASTUPDATETIME(txnInfo.getEventTime());
			dy_abnormalOccurrence.setLASTUPDATEUSERID(txnInfo.getTxnUser());
				
				
			dy_abnormalOccurrence.excuteDML(SqlConstant.DML_UPDATE);
	    		
			history.addHistory(dy_abnormalOccurrence, txnInfo, SqlConstant.DML_UPDATE);
		}
		
		return recvDoc;
	}

}