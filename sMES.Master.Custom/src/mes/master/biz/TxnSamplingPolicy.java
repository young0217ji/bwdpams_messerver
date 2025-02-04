package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.SAMPLINGPOLICY;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnSamplingPolicy implements ObjectExecuteService
{
	/**
	 * 샘플링 기준정보와 공정 및 제품의 연결 정보를 등록, 수정, 삭제 하는 트랜잭션 실행
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

            SAMPLINGPOLICY dataInfo = new SAMPLINGPOLICY();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPOLICYCODE(dataMap.get("POLICYCODE"));
            dataInfo.setKeyPROCESSID(dataMap.get("PROCESSID")); // SAMPLING_ANYTHING
            dataInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID")); // SAMPLING_ANYTHING

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (SAMPLINGPOLICY) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else
            {
            	// Data Value Setting
            	dataInfo.setPOLICYTYPE(dataMap.get("POLICYTYPE")); // 기준정보 연결구분 [ Product | ProductGroup ]
            	dataInfo.setSAMPLINGID(dataMap.get("SAMPLINGID"));
            	dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));

            	// DataInfo에  대한 CUD 실행
            	if ( dataMap.get("_ROWSTATUS").equals("C") )
            	{
            		dataInfo.excuteDML(SqlConstant.DML_INSERT);
            	}
            	else if ( dataMap.get("_ROWSTATUS").equals("U") )
            	{
            		dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            	}
            }

            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }

}
