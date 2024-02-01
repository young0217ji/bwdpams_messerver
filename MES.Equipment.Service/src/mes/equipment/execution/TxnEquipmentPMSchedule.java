package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.equipment.data.EQUIPMENTPMSCHEDULE;
import mes.errorHandler.CustomException;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnEquipmentPMSchedule implements ObjectExecuteService
{
	/**
	 * PM작업 스케줄을 생성/수정/삭제 합니다
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		NameGenerator nameGenerator = new NameGenerator();

		for ( int i = 0 ; i < masterData.size(); i ++ ) {
			HashMap<String, String> dataMap = masterData.get(i);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			EQUIPMENTPMSCHEDULE dataInfo = new EQUIPMENTPMSCHEDULE();
			dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
			dataInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
			
			if ( !dataMap.get("_ROWSTATUS").equals("C") ) {
				dataInfo.setKeyPMSCHEDULEID(dataMap.get("PMSCHEDULEID"));
				dataInfo = (EQUIPMENTPMSCHEDULE) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			}
			else {
				String pmScheduleID = nameGenerator.nameGenerate(dataMap.get("PLANTID"), "PMScheduleID", new Object[] {});
				dataInfo.setKeyPMSCHEDULEID(pmScheduleID);
			}

			String managerID = dataMap.get("MANAGERUSERID");
			if ( managerID.equals("") ) {
				dataInfo.setMANAGERUSERID(txnInfo.getTxnUser());
			}
			else {
				dataInfo.setMANAGERUSERID(dataMap.get("MANAGERUSERID"));
			}

			AddHistory history = new AddHistory();
			if ( dataMap.get("_ROWSTATUS").equals("C") ) {
				dataInfo.setPMSCHEDULETYPE(dataMap.get("PMSCHEDULETYPE"));
				dataInfo.setPMPLANNEDTIME(DateUtil.getTimestamp(dataMap.get("PMPLANNEDTIME")));
				dataInfo.setWORKERUSERID(dataMap.get("WORKERUSERID"));
				dataInfo.setWORKCOMMENT(dataMap.get("WORKCOMMENT"));
				dataInfo.setREFERENCETYPE("PMCODEID");
				dataInfo.setREFERENCEVALUE(dataMap.get("REFERENCEVALUE"));
				dataInfo.excuteDML(SqlConstant.DML_INSERT);

				txnInfo.setTxnId("CreatePMSchedule");
				history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
			}
			else if ( dataMap.get("_ROWSTATUS").equals("U") ) {
				if( dataInfo.getPMENDTIME() != null && !ConvertUtil.Object2String(dataInfo.getPMENDTIME()).equals("") ) {
					// 해당 설비({0})에 대한 긴급/예방 보전({1})이 완료되어 수정할 수 없습니다.
					throw new CustomException("EQP-003", new Object[] {dataInfo.getKeyEQUIPMENTID(), dataInfo.getREFERENCEVALUE()});
				}
				
				dataInfo.setPMPLANNEDTIME(DateUtil.getTimestamp(dataMap.get("PMPLANNEDTIME")));
				dataInfo.setWORKERUSERID(dataMap.get("WORKERUSERID"));
				dataInfo.setWORKCOMMENT(dataMap.get("WORKCOMMENT"));
				dataInfo.excuteDML(SqlConstant.DML_UPDATE);

				txnInfo.setTxnId("UpdatePMSchedule");
				history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
			}
			else if ( dataMap.get("_ROWSTATUS").equals("D") ) {
				if( dataInfo.getPMSTARTTIME() != null && !ConvertUtil.Object2String(dataInfo.getPMSTARTTIME()).equals("") ) {
					// 해당 설비({0})에 대한 긴급/예방 보전({1})이 이미 진행중으로 취소 할 수 없습니다.
					throw new CustomException("EQP-004", new Object[] {dataInfo.getKeyEQUIPMENTID(), dataInfo.getREFERENCEVALUE()});
				}
				if( dataInfo.getPMENDTIME() != null && !ConvertUtil.Object2String(dataInfo.getPMENDTIME()).equals("") ) {
					// 해당 설비({0})에 대한 긴급/예방 보전({1})이 완료되어 수정할 수 없습니다.
					throw new CustomException("EQP-003", new Object[] {dataInfo.getKeyEQUIPMENTID(), dataInfo.getREFERENCEVALUE()});
				}
				
				dataInfo.excuteDML(SqlConstant.DML_DELETE);

				txnInfo.setTxnId("DeletePMSchedule");
				history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
			}
		}

		return recvDoc;
	}
}
