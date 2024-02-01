package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.lot.data.WORKORDER;
import mes.lot.validation.LotValidation;
import mes.master.data.EQUIPMENT;
import mes.constant.Constant;
import mes.equipment.transaction.EquipmentService;
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
public class TxnEquipmentSetUserInfo implements ObjectExecuteService
{
    /**
     * 장비에 UserID정보 Mapping.
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
					
		HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(0);
		
		EQUIPMENT equipment = new EQUIPMENT();
		equipment.setKeyPLANTID(dataMap.get("PLANTID"));
		equipment.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
		equipment = (EQUIPMENT) equipment.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

		equipment.setCURRENTUSERID(dataMap.get("CURRENTUSERID"));
		
		equipment.excuteDML(SqlConstant.DML_UPDATE);
		
	    // EQUIPMENT History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(equipment, txnInfo, "U");
	
		return recvDoc;
	}
}
