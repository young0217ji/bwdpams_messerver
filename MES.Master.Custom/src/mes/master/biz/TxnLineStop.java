package mes.master.biz;

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
import mes.master.data.DY_LINESTOP;
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

public class TxnLineStop implements ObjectExecuteService
{
	
	/**
	 * 비가동을 등록하는 트랜젝션 실행
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
        
        HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(0);
        txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
        
        NameGenerator nameGenerator = new NameGenerator();
        
        DY_LINESTOP dy_lineStop = new DY_LINESTOP();
        
        
        String lineStopID = nameGenerator.nameGenerate( dataMap.get("PLANTID"), "LineStopID", new Object[] {dataMap.get("PLANTID")} );
        lineStopID = lineStopID.replace(dataMap.get("PLANTID") + "-", "");

        // Key Value Setting
        dy_lineStop.setKeyLINESTOPID(ConvertUtil.Object2Int(lineStopID));
        
        // Data Value Setting
        dy_lineStop.setPLANTID(dataMap.get("PLANTID"));
        dy_lineStop.setWORKORDER(dataMap.get("WORKORDER"));
        dy_lineStop.setEQUIPMENTID(dataMap.get("EQUIPMENTID"));
        dy_lineStop.setPROCESSID(dataMap.get("PROCESSID"));
        dy_lineStop.setLINESTOPCODE(ConvertUtil.Object2Int(dataMap.get("LINESTOPCODE")));
        dy_lineStop.setLINESTOPSTARTTIME(DateUtil.getTimestamp(dataMap.get("LINESTOPSTARTTIME")));
        dy_lineStop.setLINESTOPENDTIME(DateUtil.getTimestamp(dataMap.get("LINESTOPENDTIME")));
        dy_lineStop.setDESCRIPTION(dataMap.get("DESCRIPTION"));
        dy_lineStop.setRESULT_FLAG(dataMap.get("RESULT_FLAG"));
        dy_lineStop.setPORESULT_ID(ConvertUtil.String2Int(dataMap.get("PORESULT_ID")));
        dy_lineStop.setCREATETIME(DateUtil.getCurrentTimestamp());
        dy_lineStop.setCREATEUSERID(txnInfo.getTxnUser());
        dy_lineStop.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
        dy_lineStop.setLASTUPDATEUSERID(txnInfo.getTxnUser());
    	
        dy_lineStop.excuteDML(SqlConstant.DML_INSERT);

        // History 기록이 필요한 경우 사용
        
        history.addHistory(dy_lineStop, txnInfo, SqlConstant.DML_INSERT);
        return recvDoc;
    }

}
