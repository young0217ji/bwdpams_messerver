package mes.event;

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
public class TargetPlantMap implements InitializingBean
{
	private static Map<String, String>	targetPlantMap	= new HashMap<String, String>();
	
	/**
	 * 
	 * load함수를 실행합니다
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * 
	 */
	public void afterPropertiesSet()
			throws Exception
	{
		load();
	}
	/**
	 * 
	 * load함수를 실행합니다
	 * 
	 * @param
	 * @return
	 * 
	 */
	public void reload()
	{
		load();
	}
	
	/**
	 * 
	 * targetPlantMap을 세팅합니다
	 *
	 * @param
	 * @return
	 *
	 */
	@SuppressWarnings("rawtypes")
	public static void load()
	{
		String subjectSql = "SELECT eventName, targetSubjectName, targetPlant FROM dispatchEvent WHERE targetPlant IS NOT NULL "; 
			
		List resultList = null;
		
		resultList = SqlMesTemplate.queryForList(subjectSql);

		if ( resultList == null )
		{
			return;
		}
		
		HashMap<String, String> eventMap = new HashMap<String, String>();
		
		for ( int i = 0 ; i < resultList.size(); i++)
		{
			LinkedCaseInsensitiveMap orderMap= (LinkedCaseInsensitiveMap)resultList.get(i);
		
			String eventName = orderMap.get("eventName").toString();
			if ( orderMap.get("targetPlant") != null )
			{
				String targetPlantID = orderMap.get("targetPlant").toString();
				
				eventMap.put(eventName, targetPlantID);
			}
		}
		
		targetPlantMap = eventMap;
	}
	/**
	 * 
	 * targetPlantMap에서 eventName에 해당하는 값을 불러옵니다
	 *
	 * @param eventName
	 * @return String
	 *
	 */
	public String getSubjectName(String eventName)
	{
		return targetPlantMap.get(eventName);
	}
}
