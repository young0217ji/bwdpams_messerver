package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import kr.co.mesframe.util.StringUtil;
import mes.event.MessageParse;
import mes.master.data.DY_PROCWORKORDER;
import mes.master.data.DY_PROCWORKORDERLOG;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.12.07
 * 
 * @see
 */
public class TxnProcWorkOrderUpdate implements ObjectExecuteService {


	/**
	 * 생산계획 Edit input 
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		for (int i = 0; i < masterData.size(); i++) {
			HashMap<String, String> dataMap = (HashMap<String, String>) masterData.get(i);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

			if(StringUtil.isNullOrEmpty(dataMap.get("ASSEMBLYDATAKEY"))) {

				DY_PROCWORKORDER procworkorder = new DY_PROCWORKORDER();
				procworkorder.setKeyPLANTID(dataMap.get("PLANTID"));
				procworkorder.setKeyDATAKEY(dataMap.get("DATAKEY"));
				
				DY_PROCWORKORDER currentdata = (DY_PROCWORKORDER) procworkorder.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

				procworkorder.setISSUE(dataMap.get("ISSUE")); // ISSUE
				procworkorder.setRODASSYREMARK(dataMap.get("RODASSYREMARK")); // RODASSY 비고 
				procworkorder.setROD2MATERIAL(dataMap.get("ROD2MATERIAL")); // ROD2공장 소재
				procworkorder.setROD2MANUFACTURE(dataMap.get("ROD2MANUFACTURE")); // ROD2공장 가공
				procworkorder.setROD2PLATING(dataMap.get("ROD2PLATING")); // ROD2공장 도금
				procworkorder.setROD2MOVEMENT(dataMap.get("ROD2MOVEMENT")); // ROD2공장 이동
				procworkorder.setTUBEREMARK(dataMap.get("TUBEREMARK")); // Tube 비고
				procworkorder.setPARTSGRREMARK(dataMap.get("PARTSGRREMARK")); // Parts Gr 비고
				procworkorder.setHCSCHEDULE(dataMap.get("HCSCHEDULE")); // HC 일정
				procworkorder.setCLSCHEDULE(dataMap.get("CLSCHEDULE")); // CL 일정
				procworkorder.setKLSCHEDULE(dataMap.get("KLSCHEDULE")); // KL 일정
				procworkorder.setRCSCHEDULE(dataMap.get("RCSCHEDULE")); // RC 일정
				procworkorder.setPTSCHEDULE(dataMap.get("PTSCHEDULE")); // PT 일정
				procworkorder.setPIPESCHEDULE(dataMap.get("PIPESCHEDULE")); // PIPE 일정
				procworkorder.setBUSHSCHEDULE(dataMap.get("BUSHSCHEDULE")); // BUSH 일정
				procworkorder.setVALVESCHEDULE(dataMap.get("VALVESCHEDULE")); // VALVE 일정
				procworkorder.setPARTSREMARK(dataMap.get("PARTSREMARK")); // 부품기타

				procworkorder.excuteDML(SqlConstant.DML_UPDATE);
				
				new AddHistory().addHistory(procworkorder, txnInfo, "U");
	            
	            insertProcworkorderlog(currentdata, procworkorder, txnInfo);
	            
			} else {

				DY_PROCWORKORDER procworkorder = new DY_PROCWORKORDER();
				procworkorder.setKeyPLANTID(dataMap.get("PLANTID"));
				procworkorder.setKeyDATAKEY(dataMap.get("DATAKEY"));
				
				DY_PROCWORKORDER currentdata = (DY_PROCWORKORDER) procworkorder.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

				
				procworkorder.setISSUE(dataMap.get("ISSUE")); // ISSUE
				procworkorder.setBUSHSCHEDULE(dataMap.get("BUSHSCHEDULE")); // BUSH 일정
				procworkorder.excuteDML(SqlConstant.DML_UPDATE);
				
				new AddHistory().addHistory(procworkorder, txnInfo, "U");
	            
	            insertProcworkorderlog(currentdata, procworkorder, txnInfo);
	            
				DY_PROCWORKORDER assemblyProcworkorder = new DY_PROCWORKORDER();
				assemblyProcworkorder.setKeyPLANTID(dataMap.get("PLANTID"));
				assemblyProcworkorder.setKeyDATAKEY(dataMap.get("ASSEMBLYDATAKEY"));
				
				DY_PROCWORKORDER assemblyCurrentdata = (DY_PROCWORKORDER) procworkorder.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
				
				assemblyProcworkorder.setROD2MATERIAL(dataMap.get("ROD2MATERIAL")); // ROD2공장 소재
				assemblyProcworkorder.setROD2MANUFACTURE(dataMap.get("ROD2MANUFACTURE")); // ROD2공장 가공
				assemblyProcworkorder.setROD2PLATING(dataMap.get("ROD2PLATING")); // ROD2공장 도금
				assemblyProcworkorder.setPARTSREMARK(dataMap.get("PARTSREMARK")); // 부품기타
				assemblyProcworkorder.setKLSCHEDULE(dataMap.get("KLSCHEDULE")); // KL 일정
				assemblyProcworkorder.setTUBEREMARK(dataMap.get("TUBEREMARK")); // Tube 비고		
				assemblyProcworkorder.excuteDML(SqlConstant.DML_UPDATE);
				
				new AddHistory().addHistory(assemblyProcworkorder, txnInfo, "U");
	            
	            insertProcworkorderlog(assemblyCurrentdata, assemblyProcworkorder, txnInfo);
				
			}
			
		}
		
		return recvDoc;

	}
	
	private void insertProcworkorderlog(DY_PROCWORKORDER current, DY_PROCWORKORDER modified, TxnInfo txnInfo) {
		DY_PROCWORKORDERLOG log = new DY_PROCWORKORDERLOG();
		log.setKeyPLANTID(modified.getKeyPLANTID());
		log.setKeyDATAKEY(modified.getKeyDATAKEY());
		log.setKeyTIMEKEY(txnInfo.getEventTimeKey());
		log.setEVENTNAME("TxnProcWorkOrderUpdate");
		log.setEVENTTYPE(SqlConstant.DML_UPDATE);
		log.setEVENTUSERID(txnInfo.getTxnUser());
		log.setEVENTCOMMENT(txnInfo.getTxnComment());
		log.setEVENTTIME(txnInfo.getEventTime());
		
		current.getDATAMAP().forEach((key, value)->{
			if(modified.getDATAMAP().get(key) != null && !value.equals(modified.getDATAMAP().get(key))) {
				log.setCOLUMNNAME(key);
				log.setCONTENT(modified.getDATAMAP().get(key).toString());
				log.excuteDML(SqlConstant.DML_INSERT);
			}
		});
		
		
	}
	
}
