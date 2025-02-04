package mes.legacy.util;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import kr.co.mesframe.util.DateUtil;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class InterfaceManager
{
	private static Log log = LogFactory.getLog(InterfaceManager.class);
	
	/**
	 * KAIS Web Service 호출
	 * @throws ServiceException 
	 * @throws RemoteException 
	 * 
	 */
	public static void callKAISWebService(String componentName, String serviceName, String userId, HashMap<String, String> parameterMap)
			throws RemoteException, ServiceException
	{
		callKAISWebService(componentName, serviceName, userId, parameterMap, DateUtil.getCurrentTime(DateUtil.FORMAT_SIMPLE_DAY));
	}
	
	/**
	 * KAIS Web Service 호출
	 * @throws ServiceException 
	 * @throws RemoteException 
	 * 
	 */
	public static void callKAISWebService(String componentName, String serviceName, String userId, HashMap<String, String> parameterMap, String eventDate)
			throws ServiceException, RemoteException
	{
		log.debug("KAIS Web Service Parameter 설정 시작");
		
		/**
		 * KAIS 웹서비스 정보 ※※※※※※※ 해당 정보를 XML 파일로 관리 ※※※※※※※※※※※
		 */
		String namespaceURI = "http://www.kais.com/kaisWebService";
		String soapActionURI = "";
		Boolean sendTypeAttr = true;
		String operationName = "invoke";
		// 서버
//		String endPoint = "http://80.42.11.20:7002/services/WebServiceChannelAdapter?WSDL";
		String endPoint = "http://kais.kccworld.co.kr/services/WebServiceChannelAdapter?WSDL";
		// Test Server
		// String endPoint = "http://70.11.89.156:7001/services/WebServiceChannelAdapter?WSDL";
		// 주상원 DR 로컬
		// String endPoint = "http://70.11.10.109:7001/services/WebServiceChannelAdapter?WSDL";

		/* 파라메터 설정 */
		Map<String, String> sortedParamMap = new HashMap<String, String>();
		sortedParamMap.put("0", "req");

		/******* 웹서비스 정보 끝 ****************************************/

		// SMS 파라미터 값
		String[] params = { "^_datatype=" + "XML" // ※※※데이터리턴타입 ( XML or CSV)※※※

				+ "^_common.componentName=" + componentName // 컴포넌트명
				+ "^_common.serviceName=" + serviceName // 서비스명
				+ "^_common.userID=" + userId // ※※※필수 : 사용자ID
				+ "^_common.empNum=" + userId // ※※※필수 : 사용자ID
				+ "^_common.taskDate=" + eventDate // 현재년월일(yyyyMMdd)
				+ "^_param1=" + parameterMap.get("PARAMETER1") // 매개변수 1
				+ "^_param2=" + parameterMap.get("PARAMETER2") // 매개변수 2
				+ "^_param3=" + parameterMap.get("PARAMETER3") // 매개변수 3
				+ "^_param4=" + parameterMap.get("PARAMETER4") // 매개변수 4
				+ "^_param5=" + parameterMap.get("PARAMETER5") // 매개변수 5
				+ "^_param6=" + parameterMap.get("PARAMETER6") // 매개변수 6
		};

		log.debug("KAIS Web Service Parameter 설정 완료");
		
		OperationDesc oper;
		ParameterDesc param;

		oper = new OperationDesc();
		oper.setName(operationName);

		if ( sortedParamMap != null )
		{
			// 파라메터 개수 검사
			int definedParamCnt = sortedParamMap.size();

			if ( params == null || definedParamCnt != params.length )
			{
				System.out.println("Error");
				// throw new FWException(ErrorConstants.ERR0019, ErrorConstants.ERR0019_MSG);
			}

			/* 파라메터 입력 순서 따라 paramName을 설정한다. */
			for ( int i = 0; i < params.length; i++ )
			{
				String paramName = (String) sortedParamMap.get("" + i);
				System.out.println(paramName);
				param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(namespaceURI, paramName),
						ParameterDesc.IN, XMLType.XSD_STRING, java.lang.String.class, false, false);

				param.setOmittable(true);
				oper.addParameter(param);
			}
		}

		log.debug("KAIS Web Service Parameter 검사 완료");
		
		/* 반환 타입 설정-반환 타입은 항상 String으로 한다. */
		oper.setReturnType(XMLType.XSD_STRING);
		oper.setReturnClass(java.lang.String.class);

		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);

		log.debug("KAIS Web Service 객체 생성 시작");
		
		/* service 객체 생성 */
		Service service = new Service();

		/* apache axis client Call 객체 생성 */
		Call call = (Call) service.createCall();

		call.setOperation(oper);
		if ( soapActionURI != null )
		{
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(soapActionURI);
		}
		else
		{
			call.setUseSOAPAction(false);
		}

		call.setEncodingStyle(null);
		call.setProperty(Call.SEND_TYPE_ATTR, sendTypeAttr);
		call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		call.setOperationName(new QName(namespaceURI, operationName));

		/* endpoint 설정 */
		call.setTargetEndpointAddress(endPoint);
		
		log.info("KAIS Web Service 객체 생성 완료");
		log.info("KAIS Web Service 호출 시작  : " + DateUtil.getCurrentTime());

		/* Operation 호출 */
		Object ret = call.invoke(params);

		log.info("KAIS Web Service 호출 완료  : " + DateUtil.getCurrentTime());
		
		/* 데이터 리턴 */
		String result = ret.toString();
		System.out.println(" ####### Outbound I/F Test Return : " + result);
		log.debug(" ####### Outbound I/F Test Return : " + result);
		if ( result.startsWith("Success^") )
		{
			// 조회성공
			@SuppressWarnings("unused")
			String[] rtnData = result.split("Success^");

			// ※ CSV 로 리턴시 구분자로 split
			// String TABLE_SPLITTER = "\\|\\@\\|"; //테이블 구분
			// String HEADER_SPLITTER = "\\|\\$\\|"; //컬럼정보와데이터구분
			// String COLUMN_SPLITTER = "\\|\\*\\|"; //컬럼별 구분
			// String ROW_SPLITTER = "\\|\\^\\|"; //로우 구분

			// System.out.println(rtnData[1]);
		}
		else
		{
			// 조회실패
			String[] rtnData = result.split("Exception\\^");
			// System.out.println(rtnData[1]);
			// throw new Exception(rtnData[1]);
		}
	}

	/**
	 * KAIS Web Service 호출 - 병행 임시
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public static void callKAISWebServiceTemp(String componentName, String serviceName, String userId, HashMap<String, String> parameterMap)
			throws RemoteException, ServiceException
	{
		callKAISWebServiceTemp(componentName, serviceName, userId, parameterMap, DateUtil.getCurrentTime(DateUtil.FORMAT_SIMPLE_DAY));
	}
	
	/**
	 * KAIS Web Service 호출 - 병행 임시
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public static void callKAISWebServiceTemp(String componentName, String serviceName, String userId, HashMap<String, String> parameterMap, String eventDate)
			throws ServiceException, RemoteException
	{
		log.debug("KAIS 병행 Web Service Parameter 설정 시작");
		
		/**
		 * KAIS 웹서비스 정보 ※※※※※※※ 해당 정보를 XML 파일로 관리 ※※※※※※※※※※※
		 */
		String namespaceURI = "http://www.kais.com/kaisWebService";
		String soapActionURI = "";
		Boolean sendTypeAttr = true;
		String operationName = "invoke";
		// Test Server
//		String endPoint = "http://70.20.21.47:7002/services/WebServiceChannelAdapter?WSDL";
		String endPoint = "http://80.33.11.20:7002/services/WebServiceChannelAdapter?WSDL";

		/* 파라메터 설정 */
		Map<String, String> sortedParamMap = new HashMap<String, String>();
		sortedParamMap.put("0", "req");

		/******* 웹서비스 정보 끝 ****************************************/

		// SMS 파라미터 값
		String[] params = { "^_datatype=" + "XML" // ※※※데이터리턴타입 ( XML or CSV)※※※

				+ "^_common.componentName=" + componentName // 컴포넌트명
				+ "^_common.serviceName=" + serviceName // 서비스명
				+ "^_common.userID=" + userId // ※※※필수 : 사용자ID
				+ "^_common.empNum=" + userId // ※※※필수 : 사용자ID
				+ "^_common.taskDate=" + eventDate // 현재년월일(yyyyMMdd)
				+ "^_param1=" + parameterMap.get("PARAMETER1") // 매개변수 1
				+ "^_param2=" + parameterMap.get("PARAMETER2") // 매개변수 2
				+ "^_param3=" + parameterMap.get("PARAMETER3") // 매개변수 3
				+ "^_param4=" + parameterMap.get("PARAMETER4") // 매개변수 4
				+ "^_param5=" + parameterMap.get("PARAMETER5") // 매개변수 5
				+ "^_param6=" + parameterMap.get("PARAMETER6") // 매개변수 6
		};

		log.debug("KAIS 병행 Web Service Parameter 설정 완료");
		
		OperationDesc oper;
		ParameterDesc param;

		oper = new OperationDesc();
		oper.setName(operationName);

		if ( sortedParamMap != null )
		{
			// 파라메터 개수 검사
			int definedParamCnt = sortedParamMap.size();

			if ( params == null || definedParamCnt != params.length )
			{
				System.out.println("Error");
				// throw new FWException(ErrorConstants.ERR0019, ErrorConstants.ERR0019_MSG);
			}

			/* 파라메터 입력 순서 따라 paramName을 설정한다. */
			for ( int i = 0; i < params.length; i++ )
			{
				String paramName = (String) sortedParamMap.get("" + i);
				System.out.println(paramName);
				param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(namespaceURI, paramName),
						ParameterDesc.IN, XMLType.XSD_STRING, java.lang.String.class, false, false);

				param.setOmittable(true);
				oper.addParameter(param);
			}
		}

		log.debug("KAIS 병행 Web Service Parameter 검사 완료");
		
		/* 반환 타입 설정-반환 타입은 항상 String으로 한다. */
		oper.setReturnType(XMLType.XSD_STRING);
		oper.setReturnClass(java.lang.String.class);

		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);

		log.debug("KAIS 병행 Web Service 객체 생성 시작");
		
		/* service 객체 생성 */
		Service service = new Service();

		/* apache axis client Call 객체 생성 */
		Call call = (Call) service.createCall();

		call.setOperation(oper);
		if ( soapActionURI != null )
		{
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(soapActionURI);
		}
		else
		{
			call.setUseSOAPAction(false);
		}

		call.setEncodingStyle(null);
		call.setProperty(Call.SEND_TYPE_ATTR, sendTypeAttr);
		call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		call.setOperationName(new QName(namespaceURI, operationName));

		/* endpoint 설정 */
		call.setTargetEndpointAddress(endPoint);
		
		log.info("KAIS 병행 Web Service 객체 생성 완료");
		log.info("KAIS 병행 Web Service 호출 시작  : " + DateUtil.getCurrentTime());

		/* Operation 호출 */
		Object ret = call.invoke(params);

		log.info("KAIS 병행 Web Service 호출 완료  : " + DateUtil.getCurrentTime());
		
		/* 데이터 리턴 */
		String result = ret.toString();
		System.out.println(" ####### Outbound I/F Test Return : " + result);
		log.debug(" ####### Outbound I/F Test Return : " + result);
		if ( result.startsWith("Success^") )
		{
			// 조회성공
			@SuppressWarnings("unused")
			String[] rtnData = result.split("Success^");

			// ※ CSV 로 리턴시 구분자로 split
			// String TABLE_SPLITTER = "\\|\\@\\|"; //테이블 구분
			// String HEADER_SPLITTER = "\\|\\$\\|"; //컬럼정보와데이터구분
			// String COLUMN_SPLITTER = "\\|\\*\\|"; //컬럼별 구분
			// String ROW_SPLITTER = "\\|\\^\\|"; //로우 구분

			// System.out.println(rtnData[1]);
		}
		else
		{
			// 조회실패
			String[] rtnData = result.split("Exception\\^");
			// System.out.println(rtnData[1]);
			// throw new Exception(rtnData[1]);
		}
	}
}
