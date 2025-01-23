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
public class PlantConstant implements InitializingBean
{
	private static final transient Logger logger = LoggerFactory.getLogger(PlantConstant.class);
	
	/*********************
	 * PLANT ID Information
	 * PLANT Table 조회
	 */
	private static String PlantID = "";
	
	public static String getPlantID()
	{
		return PlantID;
	}
	public void setPlantID(String PlantID)
	{
		PlantConstant.PlantID = PlantID;
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
		String sqlPlant = "SELECT * FROM PLANT WHERE PLANTTYPE = 'Site' AND RESOURCESTATE = 'InService' ";
		
		List<LinkedCaseInsensitiveMap> resultListPlant = SqlMesTemplate.queryForList(sqlPlant);
		
		if ( resultListPlant.size() > 0 )
		{
			String sPlantID = System.getProperty("group");
			if ( sPlantID != null && sPlantID.length() > 0 )
			{
				setPlantID(sPlantID);
			}
			else
			{
				setPlantID(resultListPlant.get(0).get("PLANTID").toString());
			}
			
			logger.info(" =============== Plant ID is " + getPlantID() + " =============== ");
		}
		else
		{
			logger.error(sqlPlant + " Excute is Fail!");
			throw new Exception(" =============== Plant ID cannot find data! Confirm Plant Table =============== ");
		}
	}
}
