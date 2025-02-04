package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.SF_ROLEUSERMAP;
import mes.util.EventInfoUtil;

/**
 * 
 * @author LSMESSolution
 * 
 * @since 2023.04.04
 * 
 * @see
 *
 */

public class TxnSFRoleUserMap implements ObjectExecuteService
{
	
	/**
	 * SF_UserRole을 등록, 수정, 삭제 하는 트랜젝션 실행
	 * 
	 * @param recvDoc
	 * @return Object
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

            SF_ROLEUSERMAP dataInfo = new SF_ROLEUSERMAP();
            // Key Value Setting
        	dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        	dataInfo.setKeyROLEID(dataMap.get("ROLEID"));
        	dataInfo.setKeyUSERID(dataMap.get("USERID"));

        	String sCheckFlag = dataMap.get("CHECKFLAG");
        	
        	// Key 에 대한 DataInfo 조회
        	List<SF_ROLEUSERMAP> oRoleMenuMapList = (List<SF_ROLEUSERMAP>) dataInfo.excuteDML(SqlConstant.DML_SELECTLIST);

        	if ( oRoleMenuMapList != null && oRoleMenuMapList.size() > 0 ) {
        		
        		if ( "FALSE".equalsIgnoreCase(sCheckFlag) ) {
        			// Map 데이터 삭제
        			dataInfo = oRoleMenuMapList.get(0);
        			dataInfo.excuteDML(SqlConstant.DML_DELETE);
        			
                	// History 기록이 필요한 경우 사용
                	AddHistory history = new AddHistory();
                	history.addHistory(dataInfo, txnInfo, SqlConstant.DML_DELETE);
        		}
        		else {
        			// 잘못 체크된 데이터 - 확인 후 수정 필요
        		}
        	}
        	else {
        		if ( "TRUE".equalsIgnoreCase(sCheckFlag) ) {
        			// Map 데이터 생성
        			// Data Value Setting
                	dataInfo.setUSEFLAG(dataMap.get("USEFLAG"));
            		dataInfo.setCREATETIME(txnInfo.getEventTime());
            		dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            		dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
            		dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());

            		dataInfo.excuteDML(SqlConstant.DML_INSERT);
            		
                	// History 기록이 필요한 경우 사용
                	AddHistory history = new AddHistory();
                	history.addHistory(dataInfo, txnInfo, SqlConstant.DML_INSERT);
        		}
        		else {
        			// 잘못 체크된 데이터 - 확인 후 수정 필요
        		}
        	}
        }

        return recvDoc;
    }

}
