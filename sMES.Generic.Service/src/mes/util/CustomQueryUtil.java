package mes.util;

import java.util.HashMap;

import kr.co.mesframe.orm.sql.SqlMesTemplate;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class CustomQueryUtil {

	/**
	 * 
	 * db의 sTableName 테이블에서 
	 * sPlantID, sProductID에 해당하는 제품을 삭제합니다
	 *
	 * @param sTableName
	 * @param sPlantID
	 * @param sProductID
	 * @return
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void deleteProductDataSetTable (String sTableName, String sPlantID, String sProductID)
	{
		String sDeleteSql = " " +
		" DELETE FROM " + sTableName + 
		" WHERE " +
		" PLANTID = :PLANTID AND " +
		" PRODUCTID = :PRODUCTID ";
		
		HashMap bindMap = new HashMap();
		bindMap.put("PLANTID", sPlantID);
		bindMap.put("PRODUCTID", sProductID);
		SqlMesTemplate.update(sDeleteSql, bindMap);
		
	}
	/**
	 * 
	 * db의 sTableName 테이블에서 
	 * sPlantID, sWarehouseID에 해당하는 창고를 삭제합니다
	 *
	 * @param sTableName
	 * @param sPlantID
	 * @param sWarehouseID
	 * @return
	 *
	 */
	public static void deleteWarehouseMapTable (String sTableName, String sPlantID, String sWarehouseID)
	{
		String sDeleteSql = " " +
		" DELETE FROM " + sTableName + 
		" WHERE " +
		" PLANTID = :PLANTID AND " +
		" WAREHOUSEID = :WAREHOUSEID ";
		
		HashMap bindMap = new HashMap();
		bindMap.put("PLANTID", sPlantID);
		bindMap.put("WAREHOUSEID", sWarehouseID);
		SqlMesTemplate.update(sDeleteSql, bindMap);
	}
}

