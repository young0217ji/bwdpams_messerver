package mes.master.biz;

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

public class TxnDisplayManagement implements ObjectExecuteService {

	/**
	 * 현황판 Display관리 등록 및 수정, 삭제
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
            
            DY_DISPLAYMANAGEMENT dataInfo = new DY_DISPLAYMANAGEMENT();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPROCESSID(dataMap.get("PROCESSID"));
            dataInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            dataInfo.setKeySEQUENCE(dataMap.get("SEQUENCE"));
            
            // Value Setting
            dataInfo.setUSEFLAG(dataMap.get("USEFLAG"));
            dataInfo.setDISPLAYTYPE(dataMap.get("DISPLAYTYPE"));
            dataInfo.setFILEPATH(dataMap.get("FILEPATH"));
            dataInfo.setORIGINALFILEPATH(dataMap.get("ORIGINALFILEPATH"));
            dataInfo.setPUBLICFLAG(dataMap.get("PUBLICFLAG"));
            dataInfo.setDISPLAYPROCESSNAME(dataMap.get("DISPLAYPROCESSNAME"));
            dataInfo.setDISPLAYEQUIPMENTNAME(dataMap.get("DISPLAYEQUIPMENTNAME"));
            dataInfo.setSTARTTIME(dataMap.get("STARTTIME"));
            dataInfo.setENDTIME(dataMap.get("ENDTIME"));
            dataInfo.setDAYFLAG(dataMap.get("DAYFLAG"));
            dataInfo.setDISPLAYTIME(ConvertUtil.Object2Double(dataMap.get("DISPLAYTIME")));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());

            
            
            // DataInfo에  대한 CUD 실행
            if(dataMap.get("_ROWSTATUS").equals("D")) {
            	dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if(dataMap.get("_ROWSTATUS").equals("C")) {
                dataInfo.setCREATETIME(txnInfo.getEventTime());
                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if(dataMap.get("_ROWSTATUS").equals("U")) {
            	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
		
			}

			return recvDoc;
	}

}
