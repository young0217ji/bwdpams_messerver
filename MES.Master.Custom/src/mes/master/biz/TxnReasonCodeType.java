package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.REASONCODE;
import mes.master.data.REASONCODETYPE;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnReasonCodeType implements ObjectExecuteService
{
	List<REASONCODE> listReasonCode;
	
	/**
	 * 사유코드구분을 등록, 수정, 삭제 하는 트랜잭션 실행
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

            REASONCODETYPE dataInfo = new REASONCODETYPE();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyREASONCODETYPEID(dataMap.get("REASONCODETYPEID"));

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (REASONCODETYPE) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }


            dataInfo.setREASONCODETYPENAME(dataMap.get("REASONCODETYPENAME"));
			
            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") ) {
            	executeReasonCodeDelete( dataInfo );
            	
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") ) {
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") ) {
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
        }

        return recvDoc;
    }
    
    // 하위노드 삭제 함수
	private void executeReasonCodeDelete( REASONCODETYPE dataInfo )
	{
		REASONCODE oReasonCodeInfo = new REASONCODE();
		oReasonCodeInfo.setKeyPLANTID(dataInfo.getKeyPLANTID());
		oReasonCodeInfo.setKeyREASONCODETYPE(dataInfo.getKeyREASONCODETYPEID());
		
		List<REASONCODE> listDataInfo = (List<REASONCODE>) oReasonCodeInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		for ( int i = 0; i < listDataInfo.size(); i++ ) {

			oReasonCodeInfo = listDataInfo.get(i);
			oReasonCodeInfo.excuteDML(SqlConstant.DML_DELETE);
		}
	}
}
