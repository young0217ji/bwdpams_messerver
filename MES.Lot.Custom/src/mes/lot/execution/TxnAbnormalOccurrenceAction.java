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
import mes.lot.data.DY_ABNORMALOCCURRENCEACTION;
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

public class TxnAbnormalOccurrenceAction implements ObjectExecuteService
{
	
	/**
	 * DY_AbnormalOccurrenceAction를 등록하는 트랜젝션 실행
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

        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_ABNORMALOCCURRENCEACTION dy_abnormalOccurrenceAction = new DY_ABNORMALOCCURRENCEACTION();

            //DY_ABNORMALOCCURRENCEACTION
            dy_abnormalOccurrenceAction.setKeyPLANTID(dataMap.get("PLANTID"));
            dy_abnormalOccurrenceAction.setKeyDATAKEY(dataMap.get("DATAKEY"));

            List<Object> DataResult = (List<Object>) dy_abnormalOccurrenceAction.excuteDML(SqlConstant.DML_SELECTLIST);
            
            dy_abnormalOccurrenceAction.setACTIONCODE(dataMap.get("ACTIONCODE"));
            dy_abnormalOccurrenceAction.setACTIONCOMMENT(dataMap.get("ACTIONCOMMENT"));
            dy_abnormalOccurrenceAction.setLINESTOPCODE(ConvertUtil.Object2Int(dataMap.get("LINESTOPCODE")));
            dy_abnormalOccurrenceAction.setDEFECTREPORTID(dataMap.get("DEFECTREPORTID"));
            dy_abnormalOccurrenceAction.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dy_abnormalOccurrenceAction.setCREATETIME(DateUtil.getCurrentTimestamp());
            dy_abnormalOccurrenceAction.setCREATEUSERID(txnInfo.getTxnUser());
            //dy_abnormalOccurrenceAction.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
            //dy_abnormalOccurrenceAction.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            
            if (DataResult.size() > 0) 
    		{
            	dy_abnormalOccurrenceAction.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
            	dy_abnormalOccurrenceAction.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            	
            	dy_abnormalOccurrenceAction.excuteDML(SqlConstant.DML_UPDATE);
            	
    			history.addHistory(dy_abnormalOccurrenceAction, txnInfo, SqlConstant.DML_UPDATE);
    		} else 
    		{
    			dy_abnormalOccurrenceAction.excuteDML(SqlConstant.DML_INSERT);
    			history.addHistory(dy_abnormalOccurrenceAction, txnInfo, SqlConstant.DML_INSERT);
    		}
        }

        return recvDoc;
    }

}
