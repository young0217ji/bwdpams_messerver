package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.PRODUCTGROUPDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnProductGroupDefinition  implements ObjectExecuteService
{
	/**
	 * 제품군 기준정보를  등록, 수정, 삭제 하는 트랜잭션 실행
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
	        
            PRODUCTGROUPDEFINITION dataInfo = new PRODUCTGROUPDEFINITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPRODUCTGROUPID(dataMap.get("PRODUCTGROUPID"));

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (PRODUCTGROUPDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
	            // Data Value Setting
//            dataInfo.setPRODUCTGROUPNAME(dataMap.get("PRODUCTGROUPNAME"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
           
            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
//            	ProductGroupIDDelete(dataMap.get("PLANTID") ,dataMap.get("PRODUCTGROUPID"));
//            	
//                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
//                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
//                dataInfo.setCREATETIME(txnInfo.getEventTime());
//                
//                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") )
            {
            	dataInfo.setACTIVESTATE(dataMap.get("ACTIVESTATE"));
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
	
	//제품군 삭제시 제품의 제품그룹ID도 같이 삭제 해준다.
	public void ProductGroupIDDelete(String sPlantID , String sProductGroupID)
	{
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.clear();

		bindMap.put("PLANTID", sPlantID);
		bindMap.put("PRODUCTGROUPID", sProductGroupID);


		String Sql = " UPDATE PRODUCTDEFINITION SET PRODUCTGROUPID = ''"
					 +" WHERE 1 = 1"
					 +" AND PLANTID = :PLANTID"
					 +" AND PRODUCTGROUPID = :PRODUCTGROUPID"
					 ;

		SqlMesTemplate.update(Sql, bindMap);
		
	}

}