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
import mes.master.data.DY_NOTICE;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

/**
 * @author LSMESSolution
 * 
 * @since 2024.01.05
 * 
 * @see
 */

public class TxnNotice implements ObjectExecuteService {

	/**
	 * 공지사항
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++) {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
            
            NameGenerator nameGenerator = new NameGenerator();
            
            DY_NOTICE dataInfo = new DY_NOTICE();
            
            
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyNOTICEID(dataMap.get("NOTICEID"));

            List<Object> NoticeDataResult = (List<Object>) dataInfo.excuteDML(SqlConstant.DML_SELECTLIST);

            // Value Setting
            dataInfo.setTITLE(dataMap.get("TITLE"));
            dataInfo.setCONTENTS(dataMap.get("CONTENTS"));
            dataInfo.setTYPE(dataMap.get("TYPE"));
            dataInfo.setSEARCHCOUNT(ConvertUtil.Object2Int(dataMap.get("SEARCHCOUNT")));
            dataInfo.setDISPLAYFLAG(dataMap.get("DISPLAYFLAG"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setLASTUPDATETIME(DateUtil.getTimestamp((ConvertUtil.Object2String(dataMap.get("LASTUPDATETIME")))));
            dataInfo.setLASTUPDATEUSERID(dataMap.get("LASTUPDATEUSERID"));
            
            if(NoticeDataResult.size() >0) {
            	
            	dataInfo.setLASTUPDATETIME(dataInfo.getLASTUPDATETIME());
                dataInfo.setLASTUPDATEUSERID(dataInfo.getLASTUPDATEUSERID());
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
            else {
            	String NoticeID = nameGenerator.nameGenerate(dataMap.get("PLANTID"), "NoticeID", new Object[] {dataMap.get("PLANTID")});
                NoticeID = NoticeID.replace(dataMap.get("PLANTID") + "-", "");

                int tempNoticeID = Integer.parseInt(NoticeID);
                NoticeID = String.valueOf(tempNoticeID);
                
                dataInfo.setKeyNOTICEID(NoticeID);
                dataInfo.setCREATETIME(txnInfo.getEventTime());
                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
    
            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, SqlConstant.DML_INSERT);
		
			}

			return recvDoc;
	}

}
