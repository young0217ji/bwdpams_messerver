package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import kr.co.mesframe.util.MessageUtil;
import mes.constant.Constant;
import mes.equipment.transaction.EquipmentService;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.lot.validation.LotValidation;
import mes.master.data.EQUIPMENT;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnEquipmentStateChange implements ObjectExecuteService
{
    /**
     * 해당설비의 상태를 변경합니다
     * 
     * @param recvDoc
     * @return Object
     * @throws CustomException
     * 
    */
	@Override
    public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		LotValidation validation = new LotValidation();
		EquipmentService equipmentService = new EquipmentService();
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			validation.checkKeyNull(dataMap, new Object[] {"PLANTID", "EQUIPMENTID"});
			
			EQUIPMENT equipmentInfo = new EQUIPMENT();
			equipmentInfo.setKeyPLANTID(dataMap.get("PLANTID"));
			equipmentInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
			equipmentInfo = (EQUIPMENT) equipmentInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

			String currentState = dataMap.get("CURRENTSTATE");
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			// EventTime 
			String eventTime = dataMap.get("EVENTTIME");
			if ( eventTime != null && !eventTime.isEmpty() )
			{
				txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			}

			if ( (equipmentInfo.getEQUIPMENTSTATE() != null) &&
			     (equipmentInfo.getEQUIPMENTSTATE().equals(currentState)) )
			{
				String changeState = dataMap.get("CHANGESTATE");
				String reasonCodeType = dataMap.get("REASONCODETYPE");
				String reasonCode = dataMap.get("REASONCODE");
				
				// PM 종료인 경우
				if (Constant.EQUIPMENTSTATE_PM.equals(currentState))
				{
					equipmentInfo.setBATCHCOUNT(ConvertUtil.Object2Long("0"));
					equipmentInfo.setLASTMAINTENANCETIME(txnInfo.getEventTime());
					equipmentInfo.setLASTMAINTENANCEUSERID(txnInfo.getTxnUser());
				}
				txnInfo.setTxnComment( dataMap.get("COMMENT") );

				equipmentService.stateChange(equipmentInfo, changeState, reasonCode, reasonCodeType, txnInfo);
				
				// 모니터링 테스트용 코딩
				String sMonitorMessage = "MachineStateChange " + equipmentInfo.getKeyEQUIPMENTID() + ":" + equipmentInfo.getEQUIPMENTSTATE(); 
				MESFrameProxy.getMessageService().reply("monitorTopic", "monitorTopic", sMonitorMessage);
				
			}
			else
			{
				// ({0}) 은 ({1})상태가 아닙니다. 상태를 확인해 주시기 바랍니다.
				throw new CustomException("EQP-002", new Object[] {equipmentInfo.getKeyEQUIPMENTID(), currentState});
			}
		}
		
		return recvDoc;
	}
}
