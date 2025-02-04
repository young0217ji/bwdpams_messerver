package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.equipment.transaction.EquipmentService;
import mes.errorHandler.CustomException;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnEquipmentPMStart implements ObjectExecuteService
{
    /**
     * 해당설비의 PM작업을 시작합니다
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
		EquipmentService equipmentService = new EquipmentService();

        HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(0);
        
        txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
        equipmentService.pmStart(dataMap, txnInfo);

        return recvDoc;
    }
}
