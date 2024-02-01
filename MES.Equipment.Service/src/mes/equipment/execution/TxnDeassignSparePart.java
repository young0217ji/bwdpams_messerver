package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.equipment.data.EQUIPMENTPMSCHEDULE;
import mes.equipment.data.SPAREPARTINFORMATION;
import mes.equipment.transaction.SparePartService;
import mes.event.MessageParse;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnDeassignSparePart implements ObjectExecuteService {
    /**
     * 장비에서 설비부품을 해제합니다
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
		SparePartService sparePartService = new SparePartService();
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			txnInfo.setTxnComment( dataMap.get("EVENTCOMMENT") );
			
			SPAREPARTINFORMATION sparepartInfo = new SPAREPARTINFORMATION();
			sparepartInfo.setKeyPLANTID(dataMap.get("PLANTID"));
			sparepartInfo.setKeySPAREPARTID(dataMap.get("SPAREPARTID"));
			sparepartInfo.setKeySPAREPARTLOTID(dataMap.get("SPAREPARTLOTID"));
			sparepartInfo = (SPAREPARTINFORMATION) sparepartInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			String changeState = sparepartInfo.getSPAREPARTLOTSTATE();
			String pmScheduleID = ConvertUtil.Object2String(dataMap.get("PMSCHEDULEID"));
			if ( changeState.equals(Constant.SPAREPART_LOTSTATE_INUSE))
			{
				changeState = Constant.SPAREPART_LOTSTATE_WAITING;
			}
			
			if( pmScheduleID.equals("") )
			{
				sparePartService.deassignSparePart(sparepartInfo, changeState, txnInfo);
			}
			else
			{
	            EQUIPMENTPMSCHEDULE equipmentPMScheduleInfo = new EQUIPMENTPMSCHEDULE();
	            equipmentPMScheduleInfo.setKeyPLANTID(dataMap.get("PLANTID"));
	            equipmentPMScheduleInfo.setKeyPMSCHEDULEID(dataMap.get("PMSCHEDULEID"));
	            equipmentPMScheduleInfo = (EQUIPMENTPMSCHEDULE) equipmentPMScheduleInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
				sparePartService.pmWork_DeassignSparePart(sparepartInfo, equipmentPMScheduleInfo, changeState, txnInfo);
			}
		}
		return recvDoc;
	}
}
