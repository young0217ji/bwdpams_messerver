package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.PROCESSROUTE;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnProcessRoute implements ObjectExecuteService
{
	/**
	 * 생산 라우팅 기준정보를  등록, 수정, 삭제 하는 트랜잭션 실행
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
        NameGenerator nameGenerator = new NameGenerator();

        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            PROCESSROUTE dataInfo = new PROCESSROUTE();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPROCESSROUTEID(dataMap.get("PROCESSROUTEID"));


            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (PROCESSROUTE) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            else
            {
            	String processRouteCode = nameGenerator.nameGenerate(dataInfo.getKeyPLANTID(), "ProcessRouteCode", new Object[] {});
            	
            	dataInfo.setPROCESSROUTECODE(processRouteCode);
            }
            
            // Data Value Setting
            dataInfo.setPROCESSROUTENAME(dataMap.get("PROCESSROUTENAME"));
            dataInfo.setACTIVESTATE(dataMap.get("ACTIVESTATE"));
            dataInfo.setCREATETIME(DateUtil.getTimestamp(dataMap.get("CREATETIME")));
            dataInfo.setCREATEUSERID(dataMap.get("CREATEUSERID"));
            dataInfo.setPROCESSROUTETYPE(dataMap.get("PROCESSROUTETYPE"));
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setCOMMONFLAG(dataMap.get("COMMONFLAG"));

            
            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") )
            {
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));


        }

        return recvDoc;
    }

}
