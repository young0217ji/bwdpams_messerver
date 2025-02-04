package mes.lot.tracking;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
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
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnProcessStart implements ObjectExecuteService
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
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		LotValidation validation = new LotValidation();
		ProcessCustomService operationService = new ProcessCustomService();
		//LotCustomService lotCustomService = new LotCustomService();
		
		validation.checkListNull(dataList);
		
		String sSQL = "";
		HashMap bindMap = new HashMap();
		List resultList;
		LinkedCaseInsensitiveMap resultMap;
		HashMap<String, String> returnMap = new HashMap<String, String>();
		
		String lotId = "";

		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			
			lotId = ConvertUtil.Object2String(dataMap.get("LOTID"));
			
			sSQL = "";
			sSQL += " SELECT *              				                         \n";
			sSQL += " FROM VIEWLOTTRACKING A                                         \n";
			sSQL += " WHERE 1 = 1	                                                 \n";
			sSQL += " AND INPUTMODE='Operation'                                      \n";
			sSQL += " AND LOTID=:LOTID                                        	 \n";
			
			bindMap.clear();
			bindMap.put("LOTID", lotId);
			//bindMap.put("필요한변수명",값);
			//bindMap.put("필요한변수명",값);
			
			
			resultList = (List) SqlMesTemplate.queryForList(sSQL, bindMap);
			
	
			for(int j = 0; j < resultList.size(); j++) {
				resultList = (List) SqlMesTemplate.queryForList(sSQL, bindMap);

				// 쿼리 조회 결과 리스트
				resultMap = (LinkedCaseInsensitiveMap)resultList.get(j);
				
				String currentFlag = ConvertUtil.Object2String(resultMap.get("CURRENTFLAG"));
				String actionType = ConvertUtil.Object2String(resultMap.get("ACTIONTYPE"));
				String processName = ConvertUtil.Object2String(resultMap.get("PROCESSNAME"));

				
				if(currentFlag.equals("Y") && (actionType.equals("Start") || actionType.equals("End"))) 
				{
					validation.checkKeyNull(resultMap, new Object[] {"LOTID", "PROCESSID", "PROCESSSEQUENCE"});
					
					String lotID 					= ConvertUtil.Object2String(resultMap.get("LOTID"));
					//String eventTime 				= dataMap.get("EVENTTIME");
					String processID 				= ConvertUtil.Object2String(resultMap.get("PROCESSID"));
					String processSequence			= ConvertUtil.Object2String(resultMap.get("PROCESSSEQUENCE"));
					Long routeRelationSequence		= ConvertUtil.Object2Long(resultMap.get("ROUTERELATIONSEQUENCE"));
					
					String equipmentID				= ConvertUtil.Object2String(dataMap.get("EQUIPMENTID"));
					String startTime 				= ConvertUtil.Object2String(dataMap.get("PROCESSSTARTTIME"));
					String endTime 					= ConvertUtil.Object2String(dataMap.get("PROCESSENDTIME"));
					
					// EventTime
					if ( (startTime != null && !startTime.isEmpty()) && (endTime != null && !endTime.isEmpty()))
					{
						if(processName.contains("PAINTING")) {
							if(actionType.equals("Start")) {
								txnInfo.setEventTime(DateUtil.getTimestamp(startTime));		
								txnInfo.setTxnUser(ConvertUtil.Object2String(dataMap.get("LASTUPDATEUSERID")));
							}else {
								txnInfo.setEventTime(DateUtil.getTimestamp(endTime));																
							}
						}else if(processName.contains("PACKING")) {
							txnInfo.setEventTime(DateUtil.getTimestamp(endTime));
						}
						
					}
					
					LOTINFORMATION lotInfo = new LOTINFORMATION();
					lotInfo.setKeyLOTID(lotID);
					lotInfo.setPROCESSROUTEID(ConvertUtil.Object2String(resultMap.get("PROCESSROUTEID")));
					String a = ConvertUtil.Object2String(resultMap.get("PROCESSROUTEID"));
					lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
					
					if ( dataMap.get("EQUIPMENTID") != null && !dataMap.get("EQUIPMENTID").isEmpty() )
					{
						TxnInfo sTxnInfo = new TxnInfo();
						sTxnInfo.setTxnId(Constant.EVENT_CHANGEEQUIPMENT);
						sTxnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
						sTxnInfo.setEventTime(txnInfo.getEventTime());
						sTxnInfo.setTxnUser(txnInfo.getTxnUser());
						sTxnInfo.setTxnComment( dataMap.get("COMMENT") );

						operationService.changeEquipment(lotInfo, processID, processSequence, routeRelationSequence, dataMap.get("EQUIPMENTID"), sTxnInfo);
					}
					txnInfo.setTxnComment(dataMap.get("COMMENT"));
					txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );
					
					if(actionType.equals("Start")) {
						operationService.processStart(lotID, processID, processSequence, routeRelationSequence, txnInfo, Constant.CONTROL_MODE_MANUAL);
						lotInfo.setMATERIALLOTID(ConvertUtil.Object2String(dataMap.get("SCANID")));
						lotInfo.excuteDML(SqlConstant.DML_UPDATE);
					}
					else if(actionType.equals("End")) {
						operationService.processEnd(lotID, processID, processSequence, routeRelationSequence, txnInfo, Constant.CONTROL_MODE_MANUAL);
						lotInfo.setMATERIALLOTID(ConvertUtil.Object2String(dataMap.get("SCANID")));
						if(j == resultList.size()-1) {
							lotInfo.setLOTSTATE(Constant.LOTSTATE_COMPLETED);
						}
						lotInfo.excuteDML(SqlConstant.DML_UPDATE);							
					}
				}
			}
		}
		
		return recvDoc;
	}
}


