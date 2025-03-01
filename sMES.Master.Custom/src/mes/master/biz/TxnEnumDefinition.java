package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.ENUMDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnEnumDefinition implements ObjectExecuteService
{
	/**
	 * Enum 기준정보를 등록, 수정, 삭제 하는 트랜잭션 실행
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

            ENUMDEFINITION dataInfo = new ENUMDEFINITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyENUMID(dataMap.get("ENUMID"));


            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (ENUMDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            dataInfo.setENUMNAME(dataMap.get("ENUMNAME"));
            dataInfo.setACCESSTYPE(dataMap.get("ACCESSTYPE"));
            dataInfo.setENUMGROUP(dataMap.get("ENUMGROUP"));
            dataInfo.setCONSTANTFLAG(dataMap.get("CONSTANTFLAG"));
            dataInfo.setKOREAN(dataMap.get("KOREAN"));
            dataInfo.setENGLISH(dataMap.get("ENGLISH"));
            dataInfo.setNATIVE1(dataMap.get("NATIVE1"));
            dataInfo.setNATIVE2(dataMap.get("NATIVE2"));


            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
                
                // EnumValue 데이터 삭제
                
                
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
            //AddHistory history = new AddHistory();
            //history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));


        }

        return recvDoc;
    }

}
