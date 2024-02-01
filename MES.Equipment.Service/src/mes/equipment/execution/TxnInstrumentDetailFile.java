package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.equipment.data.DY_INSTRUMENTDETAILFILE;
import mes.event.MessageParse;
import mes.master.data.DY_DISPLAYMANAGEMENT;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.11
 * 
 * @see
 */

public class TxnInstrumentDetailFile implements ObjectExecuteService {

	/**
	 * 계측기상태관리 등록 및 수정, 삭제
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
            
            DY_INSTRUMENTDETAILFILE dy_instrumentDetailFile = new DY_INSTRUMENTDETAILFILE();
            
            // Key Value Setting
            dy_instrumentDetailFile.setKeyPLANTID(dataMap.get("PLANTID"));
            dy_instrumentDetailFile.setKeyRELATIONTIMEKEY(dataMap.get("RELATIONTIMEKEY"));
            dy_instrumentDetailFile.setKeyFILENAME(dataMap.get("FILENAME"));
            
            // Value Setting
            dy_instrumentDetailFile.setFILELOCATION(dataMap.get("FILELOCATION"));
            dy_instrumentDetailFile.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dy_instrumentDetailFile.setLASTUPDATETIME(txnInfo.getEventTime());
            dy_instrumentDetailFile.setLASTUPDATEUSERID(txnInfo.getTxnUser());

            
            
            // DataInfo에  대한 CUD 실행
            if(dataMap.get("_ROWSTATUS").equals("D")) {
            	dy_instrumentDetailFile.excuteDML(SqlConstant.DML_DELETE);
            }
            else if(dataMap.get("_ROWSTATUS").equals("C")) {
            	dy_instrumentDetailFile.setCREATETIME(txnInfo.getEventTime());
            	dy_instrumentDetailFile.setCREATEUSERID(txnInfo.getTxnUser());
                
            	dy_instrumentDetailFile.excuteDML(SqlConstant.DML_INSERT);
            }
            else if(dataMap.get("_ROWSTATUS").equals("U")) {
            	dy_instrumentDetailFile.excuteDML(SqlConstant.DML_UPDATE);
            }
            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dy_instrumentDetailFile, txnInfo, dataMap.get("_ROWSTATUS"));
		
			}

			return recvDoc;
	}

}
