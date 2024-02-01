package mes.legacy;

import java.util.HashMap;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.legacy.util.InterfaceManager;
import mes.util.SendMessageUtil;

import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class KAISInterface implements ObjectExecuteService
{
	private static final transient Logger log = LoggerFactory.getLogger(KAISInterface.class);
	
	/**
	 * recvDoc 파싱해 KAIS Web Service 호출
	 * 
	 * @param recvDoc
	 * @return Object 
	 * 
	 */
	@Override
	public Object execute(Document recvDoc)
	{
		// ============================================================================
		// Message 파싱
		// ============================================================================

		String componentName = SendMessageUtil.getParam(recvDoc, "COMPONENTNAME");
		String serviceName = SendMessageUtil.getParam(recvDoc, "SERVICENAME");
		String userId = SendMessageUtil.getParam(recvDoc, "USERID");
		String param1 = SendMessageUtil.getParam(recvDoc, "PARAMETER1");
		String param2 = SendMessageUtil.getParam(recvDoc, "PARAMETER2");
		String param3 = SendMessageUtil.getParam(recvDoc, "PARAMETER3");
		String param4 = SendMessageUtil.getParam(recvDoc, "PARAMETER4");
		String param5 = SendMessageUtil.getParam(recvDoc, "PARAMETER5");
		String param6 = SendMessageUtil.getParam(recvDoc, "PARAMETER6");
		String ifTargetFlag = SendMessageUtil.getParam(recvDoc, "IFTARGETFLAG");

		// ============================================================================
		// 1. Validation
		// ============================================================================
		if ( componentName.isEmpty() || serviceName.isEmpty() )
		{
			return null;
		}

		HashMap<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("PARAMETER1", ConvertUtil.Object2String(param1));
		parameterMap.put("PARAMETER2", ConvertUtil.Object2String(param2));
		parameterMap.put("PARAMETER3", ConvertUtil.Object2String(param3));
		parameterMap.put("PARAMETER4", ConvertUtil.Object2String(param4));
		parameterMap.put("PARAMETER5", ConvertUtil.Object2String(param5));
		parameterMap.put("PARAMETER6", ConvertUtil.Object2String(param6));

		
		if ( "T".equalsIgnoreCase(ifTargetFlag) || "A".equalsIgnoreCase(ifTargetFlag) || 
				ifTargetFlag == null || ifTargetFlag == "" )
		{
			//	2012.05.21 11:00 병행 웹서비스 호출 삭제
//			if ( serviceName.equalsIgnoreCase("interFaceMesFsMatchInfo") || serviceName.equalsIgnoreCase("interFaceMesWorkOrder") || serviceName.equalsIgnoreCase("interFaceMesFsStdInfo") )
//			{
				String eventDate = DateUtil.getCurrentTime(DateUtil.FORMAT_SIMPLE_DAY);
				log.info("KAIS 병행 Web Service 호출 대기");
				try
				{
					// KAIS 병행 임시 Web Service 호출
					InterfaceManager.callKAISWebServiceTemp(componentName, serviceName, userId, parameterMap, eventDate);
					log.info("KAIS 병행 Web Service Function Call 완료");
					insertTS03_MES_IF_SERVICE(serviceName, componentName, serviceName, parameterMap, "KAISMES", eventDate);
					log.info("KAIS 병행 Web Service 호출 성공");
				}
				catch ( Exception e )
				{
					log.info("KAIS 병행 Web Service Function Call 실패");
					log.debug( e.getMessage(), e );
					insertTS03_MES_IF_SERVICE(serviceName, componentName, serviceName, parameterMap, "KAISMES", eventDate, "Error : " + e.getMessage());
					log.info("KAIS 병행 Web Service 호출 실패");
					log.error( e.getMessage(), e );
				}
//			}
		}

		
		
		if ( "L".equalsIgnoreCase(ifTargetFlag) || "A".equalsIgnoreCase(ifTargetFlag) || 
				ifTargetFlag == null || ifTargetFlag == "" )
		{
//			if ( Constant.JJ3_PLANTCODE.equalsIgnoreCase(Constant.PLANTID) )
//			{
//				String eventDate = DateUtil.getCurrentTime(DateUtil.FORMAT_SIMPLE_DAY);
//				log.info("KAIS 병행 Web Service 호출 대기");
//				try
//				{
//					// KAIS 병행 임시 Web Service 호출
//					InterfaceManager.callKAISWebServiceTemp(componentName, serviceName, userId, parameterMap, eventDate);
//					log.info("KAIS 병행 Web Service Function Call 완료");
//					insertTS03_MES_IF_SERVICE(serviceName, componentName, serviceName, parameterMap, "KAISMES", eventDate);
//					log.info("KAIS 병행 Web Service 호출 성공");
//				}
//				catch ( Exception e )
//				{
//					log.info("KAIS 병행 Web Service Function Call 실패");
//					log.debug( e.getMessage(), e );
//					insertTS03_MES_IF_SERVICE(serviceName, componentName, serviceName, parameterMap, "KAISMES", eventDate, "Error : " + e.getMessage());
//					log.info("KAIS 병행 Web Service 호출 실패");
//					log.error( e.getMessage(), e );
//				}
//			}
//			else
//			{
				// KAIS Web Service 호출
				String eventDate = DateUtil.getCurrentTime(DateUtil.FORMAT_SIMPLE_DAY);
				log.info("KAIS Web Service 호출 대기");
				try
				{
					InterfaceManager.callKAISWebService(componentName, serviceName, userId, parameterMap, eventDate);
					log.info("KAIS Web Service Function Call 완료");
					insertTS03_MES_IF_SERVICE(serviceName, componentName, serviceName, parameterMap, "KAIS", eventDate);
					log.info("KAIS Web Service 호출 성공");
				}
				catch ( Exception e )
				{
					log.info("KAIS Web Service Function Call 실패");
					log.debug( e.getMessage(), e );
					insertTS03_MES_IF_SERVICE(serviceName, componentName, serviceName, parameterMap, "KAIS", eventDate, "Error : " + e.getMessage());
					log.info("KAIS Web Service 호출 실패");
					log.error( e.getMessage(), e );
				}
//			}
		}
		

		// Reply필요 없음
		return null;
	}
	
	/**
	 * TS03_MES_IF_SERVICE에 insert 처리
	 * 
	 * @param tableName
	 * @param componentName
	 * @param serviceName
	 * @param parameterMap
	 * @param serverName
	 * @param eventDate
	 */
	private static void insertTS03_MES_IF_SERVICE(String tableName, String componentName, String serviceName, 
			HashMap<String, String> parameterMap, String serverName, String eventDate)
	{
		insertTS03_MES_IF_SERVICE(tableName, componentName, serviceName, parameterMap, serverName, eventDate, "");
	}
	
	/**
	 * TS03_MES_IF_SERVICE에 insert 처리
	 * 
	 * @param tableName
	 * @param componentName
	 * @param serviceName
	 * @param parameterMap
	 * @param serverName
	 * @param eventDate
	 * @param errorMsg
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private static void insertTS03_MES_IF_SERVICE(String tableName, String componentName, String serviceName, 
			HashMap<String, String> parameterMap, String serverName, String eventDate, String errorMsg)
	{
		String seqSql = "SELECT seqs03_mes_if_service.NEXTVAL AS seq FROM DUAL";
		
		LinkedCaseInsensitiveMap seqMap = (LinkedCaseInsensitiveMap) SqlMesTemplate.queryForList(seqSql, new Object[] {}).get(0);
		
		String seq = ConvertUtil.Object2String(seqMap.get("seq"));
		
		String insertSql = "INSERT INTO ts03_mes_if_service " +
				"( seq_mes_if_service, source_table, component_name, service_name, last_job_datetime, target_server, param, mes_remark ) " +
				"VALUES " +
				"( :SEQ_MES_IF_SERVICE, :SOURCE_TABLE, :COMPONENT_NAME, :SERVICE_NAME, sysdate, :TARGET_SERVER, :PARAM, :MES_REMARK ) ";
		
		HashMap<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("SEQ_MES_IF_SERVICE", seq);
		argMap.put("SOURCE_TABLE", tableName);
		argMap.put("COMPONENT_NAME", componentName);
		argMap.put("SERVICE_NAME", serviceName);
		argMap.put("TARGET_SERVER", serverName);
		
		String param = "param1=" + parameterMap.get("PARAMETER1"); // 매개변수 1
		param = param + ",param2=" + parameterMap.get("PARAMETER2"); // 매개변수 2
		param = param + ",param3=" + parameterMap.get("PARAMETER3"); // 매개변수 3
		param = param + ",param4=" + parameterMap.get("PARAMETER4"); // 매개변수 4
		param = param + ",param5=" + parameterMap.get("PARAMETER5"); // 매개변수 5
		param = param + ",param6=" + parameterMap.get("PARAMETER6"); // 매개변수 6
		param = param + ",taskDate=" + eventDate; // 매개변수 6
		argMap.put("PARAM", param);
		argMap.put("MES_REMARK", errorMsg);
		
		SqlMesTemplate.update(insertSql, argMap);
	}
}
