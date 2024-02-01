package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.txninfo.TxnInfo;
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
public class TxnCreateSparePart implements ObjectExecuteService
{
    /**
     * 새로운 설비부품을 생성합니다
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
			sparePartService.create(dataMap, txnInfo);
		}
		
		return recvDoc;
	}
}
