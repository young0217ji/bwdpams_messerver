package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.COMPANYDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnCompanyDefinition implements ObjectExecuteService
{
	/**
	 * 업체 기준정보를 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
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

            COMPANYDEFINITION dataInfo = new COMPANYDEFINITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyCOMPANYID(dataMap.get("COMPANYID"));


            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (COMPANYDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            dataInfo.setCOMPANYNAME(dataMap.get("COMPANYNAME"));
            dataInfo.setCOMPANYTYPE(dataMap.get("COMPANYTYPE"));
            dataInfo.setADDRESS(dataMap.get("ADDRESS"));
            dataInfo.setDEALER(dataMap.get("DEALER"));
            dataInfo.setDEALERMOBILE(dataMap.get("DEALERMOBILE"));
            dataInfo.setDEALEREMAIL(dataMap.get("DEALEREMAIL"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));


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
