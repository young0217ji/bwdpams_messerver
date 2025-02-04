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
import mes.master.data.STATECHANGEPOLICY;
import mes.master.data.STATEDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnStatePolicy implements ObjectExecuteService
{
	/**
	 * 가능한 상태변경을 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
    @SuppressWarnings("unchecked")
	@Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            String plantID = dataMap.get("PLANTID");
            String stateObject = dataMap.get("STATEOBJECT");
            String resourceState = dataMap.get("RESOURCESTATE");
            String changeState = dataMap.get("CHANGESTATE");
            
            STATECHANGEPOLICY dataInfo = new STATECHANGEPOLICY();
            // Key Value Setting
            dataInfo.setKeyPLANTID(plantID);
            dataInfo.setKeySTATEOBJECT(stateObject);
            dataInfo.setKeyRESOURCESTATE(resourceState);
            dataInfo.setKeyCHANGESTATE(changeState);
            
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (STATECHANGEPOLICY) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            else
            {
            	// 상태기준정보 존재여부 체크
                STATEDEFINITION stateDefinition = new STATEDEFINITION();
                stateDefinition.setKeyPLANTID(plantID);
                stateDefinition.setKeySTATEOBJECT(stateObject);
                stateDefinition.setKeySTATE(resourceState);
                List<Object> listStateInfo = (List<Object>) stateDefinition.excuteDML(SqlConstant.DML_SELECTLIST);
                if ( listStateInfo.size() < 1)
                {
                	// 해당 상태 ({0}) 의 기준정보가 등록되어 있지 않습니다.
            		throw new CustomException("MD-011", new Object[] {resourceState});
                }
                
                stateDefinition = new STATEDEFINITION();
                stateDefinition.setKeyPLANTID(plantID);
                stateDefinition.setKeySTATEOBJECT(stateObject);
                stateDefinition.setKeySTATE(changeState);
                listStateInfo = (List<Object>) stateDefinition.excuteDML(SqlConstant.DML_SELECTLIST);
                if ( listStateInfo.size() < 1 )
                {
                	// 해당 상태 ({0}) 의 기준정보가 등록되어 있지 않습니다.
            		throw new CustomException("MD-011", new Object[] {changeState});
                }
            }

            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
            	dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else
            {
            	// Data Value Setting
            	dataInfo.setREASONCODETYPE(dataMap.get("REASONCODETYPE"));
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
