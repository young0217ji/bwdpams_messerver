package mes.uif.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.mesframe.orm.sql.SqlMesTemplate;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TargetSubjectMap implements InitializingBean
{
	private static Map<String, String>	targetSubjectMap	= new HashMap<String, String>();
	
	public void afterPropertiesSet()
			throws Exception
	{
		load();
	}

	public void reload()
	{
		load();
	}
	
	@SuppressWarnings("rawtypes")
	public static void load()
	{
		String subjectSql = "SELECT eventName, targetSubjectName FROM dispatchEvent WHERE plantID = ? AND serverName = ?"; 
			
		List resultList = null;
		
		resultList = SqlMesTemplate.queryForList(subjectSql, new Object[] {System.getProperty("group"), System.getProperty("svr")});

		if ( resultList == null )
		{
			return;
		}
		
		HashMap<String, String> eventMap = new HashMap<String, String>();
		
		for ( int i = 0 ; i < resultList.size(); i++)
		{
			LinkedCaseInsensitiveMap orderMap= (LinkedCaseInsensitiveMap)resultList.get(i);
		
			String eventName = orderMap.get("eventName").toString();
			if ( orderMap.get("targetSubjectName") != null )
			{
				String subjectName = orderMap.get("targetSubjectName").toString();
				
				eventMap.put(eventName, subjectName);
			}
		}
		
		targetSubjectMap = eventMap;
	}
	
	public String getSubjectName(String eventName)
	{
		return targetSubjectMap.get(eventName);
	}
}