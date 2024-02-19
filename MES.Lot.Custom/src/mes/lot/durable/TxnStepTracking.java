package mes.lot.durable;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.durable.transaction.DurableService;
import mes.event.MessageParse;
import mes.lot.custom.RecipeCustomService;
import mes.lot.data.LOTINFORMATION;
import mes.lot.validation.LotValidation;
import mes.master.data.DURABLEINFORMATION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnStepTracking implements ObjectExecuteService
{
	/**
	 * Durable ID 기준으로 Multi Lot에 대한 스텝을 진행하는 트랜잭션 실행
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
		DurableService durableService = new DurableService();
		
		validation.checkListNull(dataList);
		
		if ( dataList.size() > 0 ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(0);

			validation.checkKeyNull(dataMap, new Object[] {"DURABLEID", "PROCESSID", "PROCESSSEQUENCE", "RECIPEID", "RECIPESEQUENCE", "RECIPERELATIONCODE", "RECIPETYPECODE"});

			String durableID 				= dataMap.get("DURABLEID");
			String eventTime 				= dataMap.get("EVENTTIME");
			String processID 				= dataMap.get("PROCESSID");
			String processSequence			= dataMap.get("PROCESSSEQUENCE");
			Long routeRelationSequence		= ConvertUtil.Object2Long(dataMap.get("ROUTERELATIONSEQUENCE"));
			String recipeID					= dataMap.get("RECIPEID");
			String recipeType				= dataMap.get("RECIPETYPE");
			String recipeSequence			= dataMap.get("RECIPESEQUENCE");
			String recipeRelationCode		= dataMap.get("RECIPERELATIONCODE");
			String recipeTypeCode			= dataMap.get("RECIPETYPECODE");

			// EventTime
			if ( eventTime != null && !eventTime.isEmpty() )
			{
				txnInfo.setEventTime( DateUtil.getTimestamp(eventTime) );
			}

			DURABLEINFORMATION durableInfo = new DURABLEINFORMATION();
			durableInfo.setKeyDURABLEID(durableID);
			durableInfo = (DURABLEINFORMATION) durableInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			durableService.checkState(durableInfo.getPROCESSSTATE(), Constant.DURABLE_PROCESSSTATE_PROCESSING);
			
			List<Object> listLotInfo = durableService.getLotList(durableInfo);
			
			for ( int i = 0; i < listLotInfo.size(); i++ )
			{
				LOTINFORMATION lotInfo = (LOTINFORMATION) listLotInfo.get(i);
				
				if ( recipeType.equalsIgnoreCase(Constant.EVENT_START) )
				{
					recipeCustomService.recipeStart(lotInfo.getKeyLOTID(), processID, processSequence, routeRelationSequence, recipeID, recipeSequence, 
							recipeRelationCode, recipeTypeCode, txnInfo, Constant.CONTROL_MODE_MANUAL);
				}
				else if ( recipeType.equalsIgnoreCase(Constant.EVENT_CHANGE) )
				{
					recipeCustomService.recipeChange(lotInfo.getKeyLOTID(), processID, processSequence, routeRelationSequence, recipeID, recipeSequence, 
							recipeRelationCode, recipeTypeCode, txnInfo, Constant.CONTROL_MODE_MANUAL);
				}
				else if ( recipeType.equalsIgnoreCase(Constant.EVENT_END) )
				{
					recipeCustomService.recipeEnd(lotInfo.getKeyLOTID(), processID, processSequence, routeRelationSequence, recipeID, recipeSequence, 
							recipeRelationCode, recipeTypeCode, txnInfo, Constant.CONTROL_MODE_MANUAL);
				}
				else
				{
					recipeCustomService.recipeCheck(lotInfo.getKeyLOTID(), processID, processSequence, routeRelationSequence, recipeID, recipeSequence, 
							recipeRelationCode, recipeTypeCode, txnInfo, Constant.CONTROL_MODE_MANUAL);
				}
			}
		}
		
		return recvDoc;
	}
}
