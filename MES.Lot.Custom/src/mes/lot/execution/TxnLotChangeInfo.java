package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.lot.data.LOTINFORMATION;
import mes.lot.validation.LotValidation;
import mes.userdefine.data.LOTINFORMATION_UDF;
import mes.util.EventInfoUtil;

import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnLotChangeInfo implements ObjectExecuteService
{
	private static final transient Logger logger = LoggerFactory.getLogger(TxnLotChangeInfo.class);
	
	/**
	 * Lot의 ATTR을 변경하는 트랜잭션 실행
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
		
		LotValidation validation = new LotValidation();
		
		validation.checkListNull(dataList);
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			String lotID 			= dataMap.get("LOTID");
			String attr 			= dataMap.get("ATTR");
			
			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID( lotID );
			
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			LOTINFORMATION_UDF lotADD = new LOTINFORMATION_UDF(lotInfo);
			lotADD.setATTR(attr);
			
			lotInfo.excuteDML(SqlConstant.DML_UPDATE);
			
			
			LOTINFORMATION lotInfo2 = new LOTINFORMATION();
			lotInfo2.setKeyLOTID( lotID );
			
			lotInfo2 = (LOTINFORMATION) lotInfo2.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			logger.info("ATTR :: " + lotInfo2.getATTR());
			
			lotInfo2.excuteDML(SqlConstant.DML_UPDATE);
		}
		
		return recvDoc;
	}
}
