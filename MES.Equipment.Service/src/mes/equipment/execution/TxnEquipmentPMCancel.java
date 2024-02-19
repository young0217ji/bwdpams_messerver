package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.equipment.data.EQUIPMENTPMSCHEDULE;
import mes.equipment.transaction.EquipmentService;
import mes.event.MessageParse;
import mes.master.data.EQUIPMENT;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnEquipmentPMCancel implements ObjectExecuteService
{
    /**
     * 해당설비의 PM작업을 취소합니다
     * 
     * @param recvDoc
     * @return Object
     * 
    */
	@Override
    public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		EquipmentService equipmentService = new EquipmentService();
		
		for ( int i = 0; i < dataList.size(); i++ ) {
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			// 취소사유 입력
			txnInfo.setTxnComment(dataMap.get("COMMENT"));
			// 이벤트 구분 - PMStartCancel, PMEndCancel
			String sCancelEventType = dataMap.get("CANCELTYPE");
			txnInfo.setTxnId(sCancelEventType);
			
            EQUIPMENTPMSCHEDULE equipmentPMScheduleInfo = new EQUIPMENTPMSCHEDULE();
            equipmentPMScheduleInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            equipmentPMScheduleInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            equipmentPMScheduleInfo.setKeyPMSCHEDULEID(dataMap.get("PMSCHEDULEID"));
            equipmentPMScheduleInfo = (EQUIPMENTPMSCHEDULE) equipmentPMScheduleInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            
            EQUIPMENT equipmentInfo = new EQUIPMENT();
            equipmentInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            equipmentInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            equipmentInfo = (EQUIPMENT) equipmentInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
    		
    		equipmentService.pmWorkCancel(equipmentInfo, equipmentPMScheduleInfo, txnInfo, sCancelEventType);
		}
		
		return recvDoc;
	}
}
