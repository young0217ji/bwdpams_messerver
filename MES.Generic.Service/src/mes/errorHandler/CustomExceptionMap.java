package mes.errorHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kr.co.mesframe.exception.MESFrameException;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.ExceptionKey;

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
public class CustomExceptionMap implements InitializingBean
{
	private static final transient Logger logger = LoggerFactory.getLogger(CustomExceptionMap.class);
	
	private Locale locale;
	private static Map<String, CustomExceptionDef> customExceptionMap = new HashMap<String, CustomExceptionDef>();

	private static String SqlCustomException = "SELECT exceptionID, localeID, exceptionType, exceptionMessage FROM customException ORDER BY exceptionID";

	public CustomExceptionMap()
	{
		initDefaultStrategies();
	}
	/**
	 * 
	 * locale을 세팅합니다
	 *
	 * @param locale
	 * @return
	 *
	 */
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}
	/**
	 * 
	 * locale을 default값으로 세팅합니다
	 *
	 * @param
	 * @return
	 *
	 */
	private void initDefaultStrategies()
	{
		setLocale(Locale.getDefault());
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
	public static void reload()
	{
		load();
	}
	
	/**
	 * 
	 * customExceptionMap을 세팅합니다
	 *
	 * @param
	 * @return
	 *
	 */
	@SuppressWarnings("rawtypes")
	public static void load()
	{
		List resultList = SqlMesTemplate.queryForList(SqlCustomException);

		if ( resultList == null ) 
		{
			return;
		}

		Map<String, CustomExceptionDef> exceptionMap = new HashMap<String, CustomExceptionDef>();
		for ( int i = 0 ; i < resultList.size(); i++)
		{
			LinkedCaseInsensitiveMap orderMap= (LinkedCaseInsensitiveMap)resultList.get(i);
		
			String exceptionID = ConvertUtil.Object2String(orderMap.get("exceptionID"));
			String localeID = ConvertUtil.Object2String(orderMap.get("localeID"));
			String exceptionType = ConvertUtil.Object2String(orderMap.get("exceptionType"));
			String exceptionMessage = ConvertUtil.Object2String(orderMap.get("exceptionMessage"));
		
			CustomExceptionDef customExceptionDef = new CustomExceptionDef();
			customExceptionDef.setExceptionID(exceptionID);
			customExceptionDef.setLocaleID(localeID);
			customExceptionDef.setExceptionType(exceptionType);
			customExceptionDef.setExceptionMessage(exceptionMessage);
		
			exceptionMap.put(exceptionID + "." + localeID, customExceptionDef);
		}
		
		customExceptionMap = exceptionMap;
	}
	/**
	 * 
	 * exceptionID에 해당하는 CustomExceptionDef를 반환합니다
	 *
	 * @param exceptionID
	 * @return CustomExceptionDef
	 *
	 */
	public CustomExceptionDef getCustomExceptionDef(String exceptionID)
	{
		return getCustomExceptionDef(exceptionID, this.locale.getLanguage());
	}
	/**
	 * 
	 * exceptionID, localeID에 해당하는 CustomExceptionDef를 반환합니다
	 *
	 * @param exceptionID
	 * @param localeID
	 * @return CustomExceptionDef
	 *
	 */
	public CustomExceptionDef getCustomExceptionDef(String exceptionID, String localeID)
	{
		if ( customExceptionMap.get(exceptionID + "." + localeID) != null )
		{
			return customExceptionMap.get(exceptionID + "." + localeID);
		}
		else
		{
			logger.info(" =============== Language (" + localeID + ") is not registered. =============== ");
			return customExceptionMap.get(exceptionID + ".ko");
		}
	}
	/**
	 * 
	 * exceptionID에 해당하는 CustomExceptionDef를 반환합니다
	 *
	 * @param exceptionID
	 * @return CustomExceptionDef
	 *
	 */
	public CustomExceptionDef getExceptionMessageDef(String exceptionID)
	{
		return getExceptionMessageDef(exceptionID, this.locale.getLanguage());
	}
	/**
	 * 
	 * exceptionID, localeID에 해당하는 CustomExceptionDef를 반환합니다
	 *
	 * @param exceptionID
	 * @return CustomExceptionDef
	 * @throws MESFrameException
	 *
	 */
	public CustomExceptionDef getExceptionMessageDef(String exceptionID, String localeID)
	{
		if ( getCustomExceptionDef(exceptionID, localeID) == null )
		{
			if ( getCustomExceptionDef("CM-000", localeID) == null )
			{
				// [{1}] 데이터는 [{0}] 테이블에 존재하지 않습니다.
				throw new MESFrameException(ExceptionKey.NotFoundObjectException, exceptionID, "CUSTOMEXCEPTION");
			}
			else
			{
				// 등록되지 않은 예외코드로 오류가 발생되었습니다.
				return getCustomExceptionDef("CM-000", localeID);
			}
		}
		else
		{
			return getCustomExceptionDef(exceptionID, localeID);
		}
	}
}

