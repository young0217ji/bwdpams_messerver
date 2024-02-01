package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.PROCESSDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnProcessDefinition implements ObjectExecuteService
{
	/**
	 * 생산공정 기준정보를  등록, 수정, 삭제 하는 트랜잭션 실행
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

        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            PROCESSDEFINITION dataInfo = new PROCESSDEFINITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPROCESSID(dataMap.get("PROCESSID"));


            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (PROCESSDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            dataInfo.setPROCESSNAME(dataMap.get("PROCESSNAME"));
            dataInfo.setACTIVESTATE(dataMap.get("ACTIVESTATE"));
            dataInfo.setPROCESSTYPE(dataMap.get("PROCESSTYPE"));
            dataInfo.setDETAILPROCESSTYPE(dataMap.get("DETAILPROCESSTYPE"));
            dataInfo.setBASICPROCESSID(dataMap.get("BASICPROCESSID"));
            dataInfo.setCLASSIFICATION(dataMap.get("CLASSIFICATION"));
            dataInfo.setPACKINGFLAG(dataMap.get("PACKINGFLAG"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setWORKCENTER(dataMap.get("WORKCENTER"));
            dataInfo.setREPRESENTATIVECHAR(dataMap.get("PROCESSOPERTYPE"));
            dataInfo.setCYCLETIME( ConvertUtil.Object2Double(dataMap.get("CYCLETIME")) );
            dataInfo.setSTANDARDTIME( ConvertUtil.Object2Double(dataMap.get("STANDARDTIME")) );
            dataInfo.setMAXTIME( ConvertUtil.Object2Double(dataMap.get("MAXTIME")) );
            dataInfo.setERPPROCESSCODE(dataMap.get("ERPPROCESSCODE"));

            
            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo.setCREATETIME(txnInfo.getEventTime());
                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") )
            {
                dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }

}
