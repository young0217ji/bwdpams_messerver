package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.equipment.data.SPAREPARTINFORMATION;
import mes.equipment.transaction.SparePartService;
import mes.event.MessageParse;
import mes.lot.validation.LotValidation;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnSparePartStateChange implements ObjectExecuteService 
{
    /**
     * 해당설비부품의 상태를 변경합니다
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
		SparePartService sparePartService = new SparePartService();
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			validation.checkKeyNull(dataMap, new Object[] {"SPAREPARTID"});
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			txnInfo.setTxnComment( dataMap.get("COMMENT") );
			
			SPAREPARTINFORMATION sparepartInfo = new SPAREPARTINFORMATION();
			sparepartInfo.setKeyPLANTID(dataMap.get("PLANTID"));
			sparepartInfo.setKeySPAREPARTID(dataMap.get("SPAREPARTID"));
			sparepartInfo.setKeySPAREPARTLOTID(dataMap.get("SPAREPARTLOTID"));
			sparepartInfo = (SPAREPARTINFORMATION) sparepartInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			String currentState = dataMap.get("CURRENTSTATE");
			
			if ( (sparepartInfo.getSPAREPARTLOTSTATE() != null) && (sparepartInfo.getSPAREPARTLOTSTATE().equals(currentState)) )
			{
				String changeState = dataMap.get("CHANGESTATE");
				String reasonCodeType = dataMap.get("REASONCODETYPE");
				String reasonCode = dataMap.get("REASONCODE");
				txnInfo.setTxnComment( dataMap.get("COMMENT") );

				sparePartService.SparePartStateChange(sparepartInfo, changeState, reasonCode, reasonCodeType, txnInfo);
				
			}
			else
			{
				// ({0}) 은 ({1})상태가 아닙니다. 상태를 확인해 주시기 바랍니다.
//					throw new CustomException("EQP-002", new Object[] {sparepartInfo.getKeySPAREPARTLOTID()(), currentState});
			}
		}
		
		return recvDoc;
	}
}
