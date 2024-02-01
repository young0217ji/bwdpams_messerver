package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_CONCESSIONRESULT;
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

public class TxnConcessionResult implements ObjectExecuteService
{
	
	/**
	 * 특채 등록 하는 트랜젝션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 */
    @Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_CONCESSIONRESULT dy_ConcessionResult = new DY_CONCESSIONRESULT();
            // Key Value Setting
            dy_ConcessionResult.setKeyPLANTID(dataMap.get("PLANTID"));
            dy_ConcessionResult.setKeyCONCESSIONID(dataMap.get("CONCESSIONID"));
            dy_ConcessionResult.setKeyLOTID(dataMap.get("LOTID"));

            // Data Value Setting
            dy_ConcessionResult.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dy_ConcessionResult.setCREATETIME(txnInfo.getEventTime());
            dy_ConcessionResult.setCREATEUSERID(txnInfo.getTxnUser());
            dy_ConcessionResult.setLASTUPDATETIME(txnInfo.getEventTime());
            dy_ConcessionResult.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            
            dy_ConcessionResult.excuteDML(SqlConstant.DML_INSERT);

            // History 기록이 필요한 경우 사용
            //AddHistory history = new AddHistory();
            //history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }

}
