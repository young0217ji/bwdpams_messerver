package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.SPREADDETAIL;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnSpreadDetail implements ObjectExecuteService
{
	/**
	 * Spread의 Column을 등록, 수정, 삭제 하는 트랜잭션 실행
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
	        SPREADDETAIL dataInfo = new SPREADDETAIL();

            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeySPREADID(dataMap.get("SPREADID"));
            dataInfo.setKeySPREADCOLUMNID(dataMap.get("SPREADCOLUMNID"));
            
            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
            	dataInfo = (SPREADDETAIL)dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else
            {
	            // Data Value Setting
	            dataInfo.setSPREADCOLUMNNAME(dataMap.get("SPREADCOLUMNNAME"));
	            dataInfo.setSPREADCOLUMNSIZE(ConvertUtil.String2Long(dataMap.get("SPREADCOLUMNSIZE")));
	            dataInfo.setVISIBLEFLAG(dataMap.get("VISIBLEFLAG"));
	            dataInfo.setEDITFLAG(dataMap.get("EDITFLAG"));
	            dataInfo.setCELLTYPE(dataMap.get("CELLTYPE"));
	            dataInfo.setCOMBOENUMID(dataMap.get("COMBOENUMID"));
	            dataInfo.setPOSITION(ConvertUtil.String2Long(dataMap.get("POSITION")));
	            dataInfo.setALIGN(dataMap.get("ALIGN"));
	            dataInfo.setCLASSNAME(dataMap.get("CLASSNAME"));
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

        }
	        return recvDoc;
    }
  
}
