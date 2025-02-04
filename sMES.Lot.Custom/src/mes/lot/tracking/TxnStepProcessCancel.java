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
import mes.lot.custom.RecipeCustomService;
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
public class TxnStepProcessCancel implements ObjectExecuteService
{
	/**
	 * 스텝을 취소하는 트랜잭션 실행
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
		RecipeCustomService recipeCustomService = new RecipeCustomService();
		
		validation.checkListNull(dataList);
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);

			validation.checkKeyNull(dataMap, new Object[] {"LOTID", "PROCESSID", "PROCESSSEQUENCE", "RECIPEID", "RECIPESEQUENCE", "RECIPERELATIONCODE", "RECIPETYPECODE"});

			String lotID 					= dataMap.get("LOTID");
			String eventTime 				= dataMap.get("EVENTTIME");
			String processID 				= dataMap.get("PROCESSID");
			String processSequence			= dataMap.get("PROCESSSEQUENCE");
			Long routeRelationSequence		= ConvertUtil.Object2Long(dataMap.get("ROUTERELATIONSEQUENCE"));
			String recipeID					= dataMap.get("RECIPEID");
			String recipeSequence			= dataMap.get("RECIPESEQUENCE");
			String recipeRelationCode		= dataMap.get("RECIPERELATIONCODE");
			String recipeTypeCode			= dataMap.get("RECIPETYPECODE");

			// EventTime
			if ( eventTime != null && !eventTime.isEmpty() )
			{
				txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			}

			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID(lotID);
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

			recipeCustomService.recipeProcessCancel(lotID, processID, processSequence, routeRelationSequence, recipeID, recipeSequence, 
					recipeRelationCode, recipeTypeCode, txnInfo, Constant.CONTROL_MODE_MANUAL);
		}
		
		return recvDoc;
	}
}
