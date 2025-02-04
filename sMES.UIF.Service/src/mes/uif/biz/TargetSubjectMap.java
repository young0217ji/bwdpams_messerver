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
		String subjectSql = "SELECT EVENT_NM, TGT_SBJT_NM FROM DSPCH_EVENT WHERE FACTORY_ID = ? AND SVR_NM = ?"; 
			
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
		
			String eventName = orderMap.get("EVENT_NM").toString();
			if ( orderMap.get("TGT_SBJT_NM") != null )
			{
				String subjectName = orderMap.get("TGT_SBJT_NM").toString();
				
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