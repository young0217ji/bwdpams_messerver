package mes.lot.tracking;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.PROCESSDATARESULT;
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
public class TxnProcessDataResult implements ObjectExecuteService
{
	/**
	 * 공정조건 결과값 입력하는 트랜잭션 실행
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

			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
			
			validation.checkKeyNull(dataMap, new Object[] {"LOTID", "RECIPEPARAMETERID"});
			
			String lotID 					= dataMap.get("LOTID");
			String recipeParameterID		= dataMap.get("RECIPEPARAMETERID");
			
			String equipmentID 				= dataMap.get("EQUIPMENTID");
			String resultValue				= dataMap.get("RESULTVALUE");
			String relationTimeKey			= dataMap.get("RELATIONTIMEKEY");
			
			String eventTime 				= dataMap.get("EVENTTIME");
			// EventTime
			if ( eventTime != null && !eventTime.isEmpty() )
			{
				txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			}

			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID( lotID );
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

			PROCESSDATARESULT processDataResult = new PROCESSDATARESULT();
			processDataResult.setKeyLOTID( lotID );
			processDataResult.setKeyRECIPEPARAMETERID( recipeParameterID );
			processDataResult.setKeyTIMEKEY( txnInfo.getEventTimeKey() );
			processDataResult.setEQUIPMENTID( equipmentID );
			processDataResult.setRESULTVALUE( resultValue );
			processDataResult.setRELATIONTIMEKEY( relationTimeKey );
			processDataResult.setEVENTTIME( txnInfo.getEventTime() );
			processDataResult.setEVENTUSERID( txnInfo.getTxnUser() );
			processDataResult.setEVENTCOMMENT( txnInfo.getTxnComment() );
			processDataResult.excuteDML(SqlConstant.DML_INSERT);
		}
		
		return recvDoc;
	}
}