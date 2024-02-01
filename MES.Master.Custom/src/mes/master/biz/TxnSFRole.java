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
import mes.master.data.SF_ROLE;
import mes.master.data.SF_ROLEMENUMAP;
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

public class TxnSFRole implements ObjectExecuteService
{
	
	/**
	 * SF_Role를 등록, 수정, 삭제 하는 트랜젝션 실행
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

        	SF_ROLE dataInfo = new SF_ROLE();
        	// Key Value Setting
        	dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        	dataInfo.setKeyROLEID(dataMap.get("ROLEID"));


        	// Key 에 대한 DataInfo 조회
        	if ( !dataMap.get("_ROWSTATUS").equals("C") )
        	{
        		dataInfo = (SF_ROLE) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
        	}

        	// Data Value Setting
        	dataInfo.setROLENAME(dataMap.get("ROLENAME"));
        	dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
        	dataInfo.setUSEFLAG(dataMap.get("USEFLAG"));
        	if ( dataMap.get("ROLETYPE") != null && dataMap.get("ROLETYPE").isEmpty() == false ) {
        		dataInfo.setROLETYPE(dataMap.get("ROLETYPE"));        		
        	}
        	else {
        		dataInfo.setROLETYPE("MENU");
        	}

        	// DataInfo에  대한 CUD 실행
        	if ( dataMap.get("_ROWSTATUS").equals("D") )
        	{
        		// Role 삭제 시 RoleMenuMap 삭제
            	SF_ROLEMENUMAP oRoleMenuMap = new SF_ROLEMENUMAP();
            	// Key Value Setting
            	oRoleMenuMap.setKeyPLANTID(dataMap.get("PLANTID"));
            	oRoleMenuMap.setKeyROLEID(dataMap.get("ROLEID"));
            	// Key 에 대한 DataInfo 조회
            	List<SF_ROLEMENUMAP> oRoleMenuMapList = (List<SF_ROLEMENUMAP>) oRoleMenuMap.excuteDML(SqlConstant.DML_SELECTLIST);
            	
            	for ( int ii = 0; ii < oRoleMenuMapList.size(); ii++ ) {
            		oRoleMenuMap = oRoleMenuMapList.get(ii);
            		oRoleMenuMap.excuteDML(SqlConstant.DML_DELETE);
        			
                	// History 기록이 필요한 경우 사용
                	AddHistory history = new AddHistory();
                	history.addHistory(oRoleMenuMap, txnInfo, SqlConstant.DML_DELETE);
            	}
            	
            	// Role 삭제 시 RoleUserMap 삭제
            	SF_ROLEUSERMAP oRoleUserMap = new SF_ROLEUSERMAP();
            	// Key Value Setting
            	oRoleUserMap.setKeyPLANTID(dataMap.get("PLANTID"));
            	oRoleUserMap.setKeyROLEID(dataMap.get("ROLEID"));
            	// Key 에 대한 DataInfo 조회
            	List<SF_ROLEUSERMAP> oRoleUserMapList = (List<SF_ROLEUSERMAP>) oRoleUserMap.excuteDML(SqlConstant.DML_SELECTLIST);
            	
            	for ( int jj = 0; jj < oRoleUserMapList.size(); jj++ ) {
            		oRoleUserMap = oRoleUserMapList.get(jj);
            		oRoleUserMap.excuteDML(SqlConstant.DML_DELETE);
        			
                	// History 기록이 필요한 경우 사용
                	AddHistory history = new AddHistory();
                	history.addHistory(oRoleUserMap, txnInfo, SqlConstant.DML_DELETE);
            	}
        		
        		dataInfo.excuteDML(SqlConstant.DML_DELETE);
        	}
        	else if ( dataMap.get("_ROWSTATUS").equals("C") )
        	{
        		dataInfo.setCREATETIME(txnInfo.getEventTime());
        		dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
        		dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
        		dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());

        		dataInfo.excuteDML(SqlConstant.DML_INSERT);
        	}
        	else if ( dataMap.get("_ROWSTATUS").equals("U") )
        	{
        		dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
        		dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());

        		dataInfo.excuteDML(SqlConstant.DML_UPDATE);
        	}

        	// History 기록이 필요한 경우 사용
        	AddHistory history = new AddHistory();
        	history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }

}
