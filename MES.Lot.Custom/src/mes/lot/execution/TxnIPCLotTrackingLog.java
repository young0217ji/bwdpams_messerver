package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.lot.data.DY_IPCLOTTRACKING_LOG;
import mes.util.EventInfoUtil;

/**
 * IPC 바코드 화면에서 Log 찍어줄때 필요
 * 
 * @author LSMESSolution
 * 
 * @since 2023.04.04
 * 
 * @see
 *
 */

public class TxnIPCLotTrackingLog implements ObjectExecuteService
{
	
	/**
	 * DY_IPCLOTTRACKING_LOG를 추가하는 트랜젝션 실행
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

            DY_IPCLOTTRACKING_LOG dataInfo = new DY_IPCLOTTRACKING_LOG();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyUSERID(dataMap.get("USERID"));
            dataInfo.setKeySCANID(dataMap.get("SCANID"));
            dataInfo.setKeyTIMEKEY(DateUtil.getCurrentEventTimeKey());

            // Data Value Setting
            dataInfo.setPRODUCTID(dataMap.get("PRODUCTID"));
            dataInfo.setWORKORDER(dataMap.get("WORKORDER"));
            dataInfo.setLOTID(dataMap.get("LOTID"));
            dataInfo.setPROCESSID(dataMap.get("PROCESSID"));
            dataInfo.setPROCESSSEQUENCE(dataMap.get("PROCESSSEQUENCE"));
            dataInfo.setEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            dataInfo.setACTIONTYPE(dataMap.get("ACTIONTYPE"));
            dataInfo.setEVENTNAME(dataMap.get("EVENTNAME"));
            dataInfo.setEVENTRESULT(dataMap.get("EVENTRESULT"));
            dataInfo.setEVENTCOMMENT(dataMap.get("EVENTCOMMENT"));
            dataInfo.setLOCALPCNAME(dataMap.get("LOCALPCNAME"));
            dataInfo.setLOCALIP(dataMap.get("LOCALIP"));
            dataInfo.setLOTTRACKINGID(dataMap.get("LOTTRACKINGID"));

            dataInfo.setEVENTTIME(DateUtil.getCurrentTimestamp());
        	
            dataInfo.excuteDML(SqlConstant.DML_INSERT);
        }

        return recvDoc;
    }

}
