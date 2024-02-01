package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_SHIFT;
import mes.master.data.DY_SHIFTDETAIL;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.05
 * 
 * @see
 */
public class TxnShift implements ObjectExecuteService {

	/**
	 * Shift 기준정보를 등록, 수정, 삭제하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		
        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
            
            DY_SHIFT dataInfo = new DY_SHIFT();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeySHIFTID(dataMap.get("SHIFTID"));
            
            // Key에 대한 DataInfo 조회
            if(!dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo = (DY_SHIFT) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            dataInfo.setSHIFTTIME(dataMap.get("SHIFTTIME"));
            dataInfo.setSHIFTNAME(dataMap.get("SHIFTNAME"));
            dataInfo.setDESCRIPTIONDAY(dataMap.get("DESCRIPTIONDAY"));
            dataInfo.setDESCRIPTIONNIGHT(dataMap.get("DESCRIPTIONNIGHT"));
            dataInfo.setCREATETIME(txnInfo.getEventTime());
            dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            
            // DataInfo에 대한 CUD 실행
            if(dataMap.get("_ROWSTATUS").equals("D")) {
            	executeShiftDetailDelete(dataInfo);
            	dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }else if(dataMap.get("_ROWSTATUS").equals("C")) {
            	dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }else if(dataMap.get("_ROWSTATUS").equals("U")) {
            	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
        }
        
        return recvDoc;
	}
	
	// SHIFT ID로 하위 SHIFT DETAIL 데이터 삭제
	private void executeShiftDetailDelete(DY_SHIFT dataInfo) {
		DY_SHIFTDETAIL shiftDetailInfo = new DY_SHIFTDETAIL();
		shiftDetailInfo.setKeyPLANTID(dataInfo.getKeyPLANTID());
		shiftDetailInfo.setKeySHIFTID(dataInfo.getKeySHIFTID());
		
		List<DY_SHIFTDETAIL> listDataInfo = (List<DY_SHIFTDETAIL>) shiftDetailInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		for(int i=0;i<listDataInfo.size();i++) {
			listDataInfo.get(i).excuteDML(SqlConstant.DML_DELETE);
		}
	}

}
