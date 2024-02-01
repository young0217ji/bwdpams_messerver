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
import mes.lot.data.DY_ABNORMALOCCURRENCEFILE;
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

public class TxnAbnormalOccurrenceFile implements ObjectExecuteService
{
	
	/**
	 * DY_AbnormalOccurrenceFile을 등록, 수정, 삭제하는 트랜젝션 실행
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

            DY_ABNORMALOCCURRENCEFILE dy_abnormalOccurrenceFile = new DY_ABNORMALOCCURRENCEFILE();

            //DY_ABNORMALOCCURRENCEACTION
            dy_abnormalOccurrenceFile.setKeyPLANTID(dataMap.get("PLANTID"));
            dy_abnormalOccurrenceFile.setKeyDATAKEY(dataMap.get("DATAKEY"));
            dy_abnormalOccurrenceFile.setKeyFILEKEY(DateUtil.getCurrentEventTimeKey());
            dy_abnormalOccurrenceFile.setKeyFILENAME(dataMap.get("FILENAME"));

            dy_abnormalOccurrenceFile.setFILELOCATION(dataMap.get("FILELOCATION"));
            dy_abnormalOccurrenceFile.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            
            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
            	dy_abnormalOccurrenceFile.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
            	dy_abnormalOccurrenceFile.setCREATETIME(DateUtil.getCurrentTimestamp());
            	dy_abnormalOccurrenceFile.setCREATEUSERID(txnInfo.getTxnUser());
            	dy_abnormalOccurrenceFile.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") )
            {   
            	dy_abnormalOccurrenceFile.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
            	dy_abnormalOccurrenceFile.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            	dy_abnormalOccurrenceFile.excuteDML(SqlConstant.DML_UPDATE);
            }
          
            // History 기록이 필요한 경우 사용
            history.addHistory(dy_abnormalOccurrenceFile, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }

}
