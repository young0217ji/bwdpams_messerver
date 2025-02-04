package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.lot.custom.LotCustomService;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.WORKORDER;
import mes.lot.validation.LotValidation;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnCreatedLotCancel implements ObjectExecuteService
{
	/**
	 * 생성된 Lot을  취소하는 트랜잭션 실행
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
		LotCustomService lotCustomService = new LotCustomService();
		
		validation.checkListNull(dataList);
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);

			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

			validation.checkKeyNull(dataMap, new Object[] {"PLANTID", "LOTID"});

			String lotID = dataMap.get("LOTID");

			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID(lotID);
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

			
//			// WorkOrder 정보 연계
//			WORKORDER oWorkOrder = new WORKORDER();
//			oWorkOrder.setKeyPLANTID(lotInfo.getPLANTID());
//			// oWorkOrder.setKeyPRODUCTORDERID(lotInfo);
//			oWorkOrder.setKeyWORKORDERID(lotInfo.getWORKORDER());
//			
//			List<Object> liDataInfo = (List<Object>) oWorkOrder.excuteDML(SqlConstant.DML_SELECTLIST);
//			
//			if ( liDataInfo.size() < 1 ) {
//				// {0}는 존재하지 않습니다.
//				throw new CustomException("CM-101", new Object[] {"WorkOrder ID : " + lotInfo.getWORKORDER()});
//			}
//			
//			oWorkOrder.setKeyPRODUCTORDERID(((WORKORDER) liDataInfo.get(0)).getKeyPRODUCTORDERID());
//			oWorkOrder.excuteDML(SqlConstant.DML_DELETE);
//			
//		    // History 기록이 필요한 경우 사용
//		    AddHistory oHistory = new AddHistory();
//		    oHistory.addHistory(oWorkOrder, txnInfo, SqlConstant.DML_DELETE);

			
			lotCustomService.createCancelLot(lotInfo.getKeyLOTID(), txnInfo);
		}
		
		return recvDoc;
	}
}
