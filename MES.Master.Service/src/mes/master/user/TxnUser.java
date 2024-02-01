package mes.master.user;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.USERDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnUser implements ObjectExecuteService
{
	/**
	 * 사용자를 등록, 수정, 삭제하는 트랜잭션 실행
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

            USERDEFINITION dataInfo = new USERDEFINITION();
            // Key Value Setting
            dataInfo.setKeyUSERID(dataMap.get("USERID"));

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (USERDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
                
                if ( !dataInfo.getUSERPASSWORD().equals(dataMap.get("USERPASSWORD")) )
        		{
        			// ({0}) 사용자의 비밀번호가 일치하지 않습니다.
        			throw new CustomException("USER-002", dataMap.get("USERID"));
        		}
            }
            else
            {
            	dataInfo.setUSERPASSWORD(dataMap.get("USERPASSWORD"));
            }

            // Data Value Setting
            dataInfo.setUSERNAME(dataMap.get("USERNAME"));
            dataInfo.setPLANTID(dataMap.get("PLANTID"));
            dataInfo.setUSERGROUPID(dataMap.get("USERGROUPID"));
            dataInfo.setAREAID(dataMap.get("AREAID"));
            dataInfo.setDEPARTMENT(dataMap.get("DEPARTMENT"));
            dataInfo.setWORKPOSITION(dataMap.get("WORKPOSITION"));
            dataInfo.setEMAIL(dataMap.get("EMAIL"));
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());

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
        }

        return recvDoc;
    }

}
