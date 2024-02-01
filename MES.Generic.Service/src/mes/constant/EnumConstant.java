package mes.constant;

import java.util.List;

import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.util.ConvertUtil;

import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class EnumConstant
{
//	private static final transient Logger logger = LoggerFactory.getLogger(EnumConstant.class);

	/**
	 * 
	 * ID가 sPlantID인 공장이 가진 열거형변수를 모두 불러와
	 * sEnumID에 맞는 이름을 리턴합니다
	 *
	 * @param sPlantID
	 * @param sEnumID
	 * @param sEnumValue
	 * @return String
	 *
	 */
	@SuppressWarnings("rawtypes")
	public String getDescriptionByEnumValue(String sPlantID, String sEnumID, String sEnumValue)
	{
		String sReturn = "";
		List<LinkedCaseInsensitiveMap> dataList = getEnumValueMap(sPlantID, sEnumID);
		for (int i=0; i<dataList.size(); i++)
		{
			LinkedCaseInsensitiveMap oData = dataList.get(i);
			String sCheckEnumValue = ConvertUtil.Object2String(oData.get("ENUMVALUE"));
			if (sEnumValue.equals(sCheckEnumValue))
			{
				sReturn =  ConvertUtil.Object2String(oData.get("ENUMVALUENAME"));
			}
		}
		return sReturn;
	}
	
	/**
	 * 
	 * ID가 sPlantID인 공장이 가진 열거형변수를 모두 불러와 List로 리턴합니다
	 *
	 * @param sPlantID
	 * @param sEnumID
	 * @return List<LinkedCaseInsenstiveMap>
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<LinkedCaseInsensitiveMap> getEnumValueMap(String sPlantID, String sEnumID)
	{
		String sql = "SELECT * FROM enumValue WHERE plantID = ? AND enumID = ? ";
		List<LinkedCaseInsensitiveMap> dataList = SqlMesTemplate.queryForList(sql, new Object[] {sPlantID, sEnumID});
		return dataList;
	}
	
	/**
	 * UserID(EnumValue)의 권한(EnumName)을 조회하여 권한이 있으면 'true'를 return한다..
	 * @param sUserID
	 * @param sUserLevel
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean checkUserLevelByEnumValue(String sPlantID, String sUserID, String sUserLevel)
	{
		boolean bUserCheck = false;
		
		List<LinkedCaseInsensitiveMap> dataList = getEnumValueMap(sPlantID, sUserLevel);
		for (int i=0; i<dataList.size(); i++)
		{
			LinkedCaseInsensitiveMap oData = dataList.get(i);
			String sCheckEnumValue = ConvertUtil.Object2String(oData.get("ENUMVALUE"));
			if (sUserID.equals(sCheckEnumValue.trim()))
			{
				bUserCheck = true;
				break;
			}
		}
		
		return bUserCheck;
	}
}

