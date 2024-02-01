package mes.lot.tracking;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.event.MessageParse;
import mes.lot.custom.LotCustomService;
import mes.lot.custom.ProcessCustomService;
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
public class TxnOperStart implements ObjectExecuteService
{
	/**
	 * 공정시작을 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		LotValidation validation = new LotValidation();
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		ProcessCustomService operationService = new ProcessCustomService();
		LotCustomService lotCustomService = new LotCustomService();
		
		validation.checkListNull(dataList);
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);

			validation.checkKeyNull(dataMap, new Object[] {"LOTID", "PROCESSID", "PROCESSSEQUENCE"});
			
			String lotID 					= dataMap.get("LOTID");
			String eventTime 				= dataMap.get("EVENTTIME");
			String processID 				= dataMap.get("PROCESSID");
			String processSequence			= dataMap.get("PROCESSSEQUENCE");
			Long routeRelationSequence		= ConvertUtil.Object2Long(dataMap.get("ROUTERELATIONSEQUENCE"));
			
			// 공정 시작 수량 ( SubProductGroup 관리의 Lot 인 경우 만 사용 )
			Double inQuantity				= ConvertUtil.Object2Double(dataMap.get("CHANGEQUANTITY"));
			
			// EventTime
			if ( eventTime != null && !eventTime.isEmpty() )
			{
				txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			}
			
			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID(lotID);
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			if ( inQuantity != null && lotInfo.getCURRENTQUANTITY().compareTo(inQuantity) != 0 )
			{
				TxnInfo sTxnInfo = new TxnInfo();
				sTxnInfo.setTxnId(txnInfo.getTxnId());
				sTxnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
				sTxnInfo.setEventTime(txnInfo.getEventTime());
				sTxnInfo.setTxnUser(txnInfo.getTxnUser());
				sTxnInfo.setTxnReasonCode( dataMap.get("REASONCODE") );
				sTxnInfo.setTxnReasonCodeType( dataMap.get("REASONCODETYPE") );
				sTxnInfo.setTxnComment( dataMap.get("COMMENT") );

				lotCustomService.makeScrapQuantity(lotInfo, ConvertUtil.doubleSubtract(inQuantity, lotInfo.getCURRENTQUANTITY()), sTxnInfo);
			}
			
			if ( dataMap.get("CHANGEEQUIPMENTID") != null && !dataMap.get("CHANGEEQUIPMENTID").isEmpty() )
			{
				TxnInfo sTxnInfo = new TxnInfo();
				sTxnInfo.setTxnId(Constant.EVENT_CHANGEEQUIPMENT);
				sTxnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
				sTxnInfo.setEventTime(txnInfo.getEventTime());
				sTxnInfo.setTxnUser(txnInfo.getTxnUser());
				sTxnInfo.setTxnComment( dataMap.get("COMMENT") );

				operationService.changeEquipment(lotInfo, processID, processSequence, routeRelationSequence, dataMap.get("CHANGEEQUIPMENTID"), sTxnInfo);
			}
			txnInfo.setTxnComment(dataMap.get("COMMENT"));
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
			
			operationService.processStart(lotID, processID, processSequence, routeRelationSequence, inQuantity, txnInfo, Constant.CONTROL_MODE_MANUAL);
		}
		
		return recvDoc;
	}
}


