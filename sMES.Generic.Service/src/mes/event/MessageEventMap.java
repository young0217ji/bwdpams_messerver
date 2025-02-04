package mes.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.mesframe.orm.sql.SqlMesTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class MessageEventMap implements InitializingBean
{
	private static final transient Logger logger = LoggerFactory.getLogger(MessageEventMap.class);
	private String							serverName 			= System.getProperty("svr");
	private Map<String, String>				eventMap			= new HashMap<String, String>();
	
	/**
	 * 
	 * serverName을 불러옵니다
	 *
	 * @param
	 * @return String
	 *
	 */
	public String getServerName()
	{
		return serverName;
	}
	/**
	 * 
	 * serverName을 세팅합니다
	 *
	 * @param serverName
	 * @return
	 *
	 */
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}
	/**
	 * 
	 * load함수를 실행합니다
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * 
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		load();
	}
	
	/**
	 * 
	 * eventMap을 세팅합니다
	 *
	 * @param
	 * @return
	 *
	 */
	@SuppressWarnings("rawtypes")
	public void load()
	{
		if ( serverName == null )
		{
			return;
		}
		
		logger.info("Server Name : {}", serverName);
		String eventSql = "SELECT EVENT_NM, CLASS_NM FROM DSPCH_EVENT WHERE FACTORY_ID = ? AND SVR_NM = ? ";
			
		List resultList = null;
		
		resultList = SqlMesTemplate.queryForList(eventSql, new Object[] {System.getProperty("group"), serverName});
		logger.info(System.getProperty("group") + " - Plant Event List Size : {}", resultList.size());
		
		HashMap<String, String> dispatchMap = new HashMap<String, String>();
		
		for ( int i = 0 ; i < resultList.size(); i++)
		{
			LinkedCaseInsensitiveMap orderMap= (LinkedCaseInsensitiveMap)resultList.get(i);
		
			String eventName = orderMap.get("EVENT_NM").toString();
			String className = orderMap.get("CLASS_NM").toString();
			
			dispatchMap.put(eventName, className);
		}
		
		this.eventMap = dispatchMap;
	}
	
	/**
	 * 
	 * eventMap을 불러옵니다
	 *
	 * @param
	 * @return Map<String, String>
	 *
	 */
	public Map<String,String> getEventMap()
	{
		return this.eventMap;
	}
}

