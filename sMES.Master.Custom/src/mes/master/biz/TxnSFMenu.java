package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.SF_MENU;
import mes.master.data.SF_ROLEMENUMAP;
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

public class TxnSFMenu implements ObjectExecuteService
{
	List<SF_MENU> listMenu;
	
	/**
	 * SF_Menu를 등록, 수정, 삭제 하는 트랜젝션 실행
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

        	SF_MENU dataInfo = new SF_MENU();
        	// Key Value Setting
        	dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        	dataInfo.setKeyMENUID(dataMap.get("MENUID"));

        	// Key 에 대한 DataInfo 조회
        	if ( !dataMap.get("_ROWSTATUS").equals("C") )
        	{
        		dataInfo = (SF_MENU) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
        	}

        	// Data Value Setting
        	dataInfo.setMENUNAME(dataMap.get("MENUNAME"));
        	dataInfo.setPARENTSID(dataMap.get("PARENTSID"));
        	dataInfo.setMENUTYPE(dataMap.get("MENUTYPE"));
        	dataInfo.setURL(dataMap.get("URL"));
        	dataInfo.setPOSITION(ConvertUtil.String2Long4Zero(dataMap.get("POSITION")));
        	dataInfo.setMENUNAME_EN(dataMap.get("MENUNAME_EN"));
        	dataInfo.setICON_CLASS(dataMap.get("ICON_CLASS"));
        	dataInfo.setUSEFLAG(dataMap.get("USEFLAG"));

        	// DataInfo에  대한 CUD 실행
        	if ( dataMap.get("_ROWSTATUS").equals("D") )
        	{
        		// Menu 삭제 시 RoleMenuMap 삭제
            	SF_ROLEMENUMAP oRoleMenuMap = new SF_ROLEMENUMAP();
            	// Key Value Setting
            	oRoleMenuMap.setKeyPLANTID(dataMap.get("PLANTID"));
            	oRoleMenuMap.setKeyROLEID(dataMap.get("MENUID"));
            	// Key 에 대한 DataInfo 조회
            	List<SF_ROLEMENUMAP> oRoleMenuMapList = (List<SF_ROLEMENUMAP>) oRoleMenuMap.excuteDML(SqlConstant.DML_SELECTLIST);
            	
            	for ( int ii = 0; ii < oRoleMenuMapList.size(); ii++ ) {
            		oRoleMenuMap = oRoleMenuMapList.get(ii);
            		oRoleMenuMap.excuteDML(SqlConstant.DML_DELETE);
        			
                	// History 기록이 필요한 경우 사용
                	AddHistory history = new AddHistory();
                	history.addHistory(oRoleMenuMap, txnInfo, SqlConstant.DML_DELETE);
            	}
        		
        		// 하위 메뉴에 대한 전체 삭제
        		SF_MENU oDataList = new SF_MENU();
        		oDataList.setKeyPLANTID(dataInfo.getKeyPLANTID());
        		listMenu = (List<SF_MENU>) oDataList.excuteDML(SqlConstant.DML_SELECTLIST);
        		
        		RecursiveDelete( dataInfo, txnInfo );
        		
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

    // 하위노드 삭제 Recursive 함수
	private void RecursiveDelete( SF_MENU dataInfo, TxnInfo txnInfo )
	{
		for (int j = listMenu.size()-1; j >= 0; j--)
		{
			if (listMenu.get(j).getPARENTSID().equals(dataInfo.getKeyMENUID()))
			{
				SF_MENU subDataInfo = new SF_MENU();
				subDataInfo.setKeyPLANTID(dataInfo.getKeyPLANTID());
				subDataInfo.setKeyMENUID(listMenu.get(j).getKeyMENUID());
				subDataInfo.excuteDML(SqlConstant.DML_DELETE);
				
	        	// History 기록이 필요한 경우 사용
	        	AddHistory history = new AddHistory();
	        	history.addHistory(subDataInfo, txnInfo, SqlConstant.DML_DELETE);
				
				RecursiveDelete(subDataInfo, txnInfo);
			}
		}
	}
}
