package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.event.MessageParse;
import mes.lot.custom.LotCustomService;
import mes.lot.data.FUTUREACTION;
import mes.lot.data.LOTINFORMATION;
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
public class TxnLotFutureAction implements ObjectExecuteService
{
	/**
	 * Lot의 예약 작업을 생성 및 삭제하는 트랜잭션 실행
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
			
			validation.checkKeyNull(dataMap, new Object[] {"LOTID", "TIMEKEY", "ACTIONTYPE"});
			
			String lotID 			= dataMap.get("LOTID");
			String relationTimeKey 	= dataMap.get("TIMEKEY");
			String rowStatus 		= dataMap.get("_ROWSTATUS");
			String sequence 		= dataMap.get("SEQUENCE");
			
			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID( lotID );
			
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			if ( "C".equalsIgnoreCase(rowStatus) )
			{
				txnInfo.setTxnId("SetFutuerAction");
				
				String seq = lotCustomService.getFutureActionSeq(lotID, relationTimeKey);
				
				FUTUREACTION futureAction = new FUTUREACTION();
				futureAction.setKeyLOTID(lotID);
				futureAction.setKeyRELATIONTIMEKEY(relationTimeKey);
				futureAction.setKeySEQUENCE(seq);
				futureAction.setACTIONTYPE(dataMap.get("ACTIONTYPE"));
				futureAction.setACTIONSTATE(Constant.ACTION_STATE_RESERVE);
				futureAction.setACTIONUSERID(txnInfo.getTxnUser());
				futureAction.setACTIONTIME(txnInfo.getEventTime());
				futureAction.setREASONCODETYPE(dataMap.get("REASONCODETYPE"));
				futureAction.setREASONCODE(dataMap.get("REASONCODE"));
				futureAction.setDESCRIPTION(dataMap.get("DESCRIPTION"));
				futureAction.excuteDML(SqlConstant.DML_INSERT);
				
				lotCustomService.setFutureAction(lotInfo, futureAction, txnInfo);
			}
			else if ( "D".equalsIgnoreCase(rowStatus) )
			{
				txnInfo.setTxnId("CancelFutuerAction");
				
				lotCustomService.releaseFutureAction(lotInfo, relationTimeKey, sequence, Constant.ACTION_STATE_CANCEL, txnInfo);
			}
		}
		
		return recvDoc;
	}
}
