package mes.lot.execution;

import java.util.HashMap;
import java.util.List;
import org.jdom.Document;
import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.lot.data.DY_PROCESSDATARESULT;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.24
 * 
 * @see
 */
public class TxnSelfInspection implements ObjectExecuteService{
	
	/**
	 * 자주검사 성적서를 등록, 수정 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */

	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        AddHistory history = new AddHistory();
        
        for (int i = 0 ; i < masterData.size(); i ++)
        {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
            
            DY_PROCESSDATARESULT dy_processDataResult = new DY_PROCESSDATARESULT();
            
            dy_processDataResult.setKeyPLANTID(dataMap.get("PLANTID"));
            dy_processDataResult.setKeyLOTID(dataMap.get("LOTID"));
            dy_processDataResult.setKeyPROCESSID(dataMap.get("PROCESSID"));
            dy_processDataResult.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            dy_processDataResult.setKeyRECIPEPARAMETERID(dataMap.get("RECIPEPARAMETERID"));
            
         	List<Object> ProcessDataResult = (List<Object>) dy_processDataResult.excuteDML(SqlConstant.DML_SELECTLIST);
         	
         	dy_processDataResult.setRESULTVALUE(dataMap.get("RESULTVALUE"));
            dy_processDataResult.setCREATETIME(txnInfo.getEventTime());
            dy_processDataResult.setCREATEUSERID(txnInfo.getTxnUser());
            dy_processDataResult.setLASTUPDATETIME(txnInfo.getEventTime());
            dy_processDataResult.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            
			if (ProcessDataResult.size() > 0) 
			{
				dy_processDataResult.excuteDML(SqlConstant.DML_UPDATE);
			} else 
			{
				dy_processDataResult.excuteDML(SqlConstant.DML_INSERT);
			}
			
			history.addHistory(dy_processDataResult, txnInfo, SqlConstant.DML_INSERT);
			
        }
        return recvDoc;
	}

}
