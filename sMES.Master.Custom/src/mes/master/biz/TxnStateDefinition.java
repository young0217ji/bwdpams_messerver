package mes.master.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
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
public class TxnStateDefinition implements ObjectExecuteService
{
	/**
	 * 상태 기준정보를 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
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
            String state = dataMap.get("STATE");
            
            STATEDEFINITION dataInfo = new STATEDEFINITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(plantID);
            dataInfo.setKeySTATEOBJECT(stateObject);
            dataInfo.setKeySTATE(state);

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (STATEDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
            	// StateChangePolicy 에서 사용중인지 여부 체크
            	STATECHANGEPOLICY changePolicy = new STATECHANGEPOLICY();
            	changePolicy.setKeyPLANTID(dataInfo.getKeyPLANTID());
            	changePolicy.setKeySTATEOBJECT(dataInfo.getKeySTATEOBJECT());

            	String suffix = " AND ( resourceState = '" + state + "' OR changeState = '" + state + "' ) ";
            	List<Object> listChangePolicy = (List<Object>) changePolicy.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
            	if ( listChangePolicy.size() > 0 )
            	{
            		// 해당 상태 ({0}) 의 기준정보가 상태변경 기준정보에서 사용중입니다.
            		throw new CustomException("MD-010", new Object[] {dataInfo.getKeySTATE()});
            	}
            	
            	// 해당 기준정보의 사용여부 체크
            	String checkSql = "SELECT 1 FROM " + dataInfo.getTABLENAME() + " WHERE " + dataInfo.getCOLUMNNAME() + " = '" + state + "' ";
            	List listCheckSql = new ArrayList();
            	try
            	{
            		listCheckSql = SqlMesTemplate.queryForList(checkSql);
            	}
            	catch (Exception e)
            	{
            		// 테이블이 없거나 잘못 넣은 경우
            	}
            	if( listCheckSql != null && listCheckSql.size() > 0 )
            	{
            		// 해당 상태 ({0}) 의 기준정보가 사용중입니다. TableName : ({1}), ColumnName : ({2})
            		throw new CustomException("MD-012", new Object[] {dataInfo.getKeySTATE()});
            	}

            	dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else
            {
            	// Data Value Setting
                dataInfo.setSTATENAME(dataMap.get("STATENAME"));
                dataInfo.setTABLENAME(dataMap.get("TABLENAME"));
                dataInfo.setCOLUMNNAME(dataMap.get("COLUMNNAME"));
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
