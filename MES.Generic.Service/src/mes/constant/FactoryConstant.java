package mes.constant;

import java.util.List;

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
public class FactoryConstant implements InitializingBean
{
	private static final transient Logger logger = LoggerFactory.getLogger(FactoryConstant.class);
	
	/*********************
	 * Factory ID Information
	 * Factory Table 조회
	 */
	private static String factoryId = "";
	
	public static String getFactoryId()
	{
		return factoryId;
	}
	public void setFactoryId(String factoryId)
	{
		FactoryConstant.factoryId = factoryId;
	}
	
	/**
	 * db에 공장이 등록되었는지 확인합니다
	 * 
     * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void afterPropertiesSet()
			throws Exception
	{
		// 공장 코드 정보
		String sqlFactory = "SELECT * FROM FACTORY WHERE FACTORY_TYPE = 'Site' AND RSRC_STAT = 'InService' ";
		
		List<LinkedCaseInsensitiveMap> resultListPlant = SqlMesTemplate.queryForList(sqlFactory);
		
		if ( resultListPlant.size() > 0 )
		{
			String sFactoryId = System.getProperty("group");
			if ( sFactoryId != null && sFactoryId.length() > 0 )
			{
				setFactoryId(sFactoryId);
			}
			else
			{
				setFactoryId(resultListPlant.get(0).get("FACTORY_ID").toString());
			}
			
			logger.info(" =============== Factory ID is " + getFactoryId() + " =============== ");
		}
		else
		{
			logger.error(sqlFactory + " Excute is Fail!");
			throw new Exception(" =============== Factory ID cannot find data! Confirm Plant Table =============== ");
		}
	}
}
