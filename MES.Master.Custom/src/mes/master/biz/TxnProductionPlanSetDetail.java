package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_PLANOPERLINE;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.10.12
 * 
 * @see
 */

public class TxnProductionPlanSetDetail implements ObjectExecuteService {

	/**
	 * 라인별 생산계획 설정을 등록, 수정, 삭제 하는 트랜잭션 실행
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
            
            DY_PLANOPERLINE dataInfo = new DY_PLANOPERLINE();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPLANOPERID(dataMap.get("PLANOPERID"));
            dataInfo.setKeyWORKCENTER(dataMap.get("WORKCENTER"));
            
            
            // Key에 대한 DataInfo 조회
            if(!dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo = (DY_PLANOPERLINE) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            dataInfo.setMAXOPERATINGTIME(ConvertUtil.String2Double(dataMap.get("MAXOPERATINGTIME")));
            dataInfo.setSTDOPERATINGTIME(ConvertUtil.String2Double(dataMap.get("STDOPERATINGTIME")));
            dataInfo.setCYCLETIME(ConvertUtil.String2Double(dataMap.get("CYCLETIME")));
            dataInfo.setLEADTIME(ConvertUtil.String2Double(dataMap.get("LEADTIME")));
            dataInfo.setEQUIPMENTCOUNT(Integer.parseInt(dataMap.get("EQUIPMENTCOUNT")));
            dataInfo.setUSEFLAG(dataMap.get("USEFLAG"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setCREATETIME(txnInfo.getEventTime());
            dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            
            
            // DataInfo에  대한 CUD 실행
            if(dataMap.get("_ROWSTATUS").equals("D")) {
            	dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }else if(dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }else if(dataMap.get("_ROWSTATUS").equals("U")) {
            	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
            
		
			}

			return recvDoc;
	}

}
