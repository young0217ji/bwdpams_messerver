package mes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.util.SqlLogUtil;
import mes.constant.Constant;
import mes.constant.FactoryConstant;
import mes.errorHandler.CustomException;
import mes.generic.GenericServiceProxy;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class QueryResult
{
	private static final transient Logger logger = LoggerFactory.getLogger(QueryResult.class);
	
	/**
	 * 
	 * queryID, bindMap을 입력받아 해당 쿼리에 대한 결과를
	 * 리스트로 리턴합니다
	 *
	 * @param queryID
	 * @param bindMap
	 * @return List<LinkedCaseInsensitiveMap>
	 *
	 */
	@SuppressWarnings("rawtypes")
	public List<LinkedCaseInsensitiveMap> getQueryResult(String queryID, HashMap<String, Object> bindMap) {
		return getQueryResult(FactoryConstant.getFactoryId(), queryID, Constant.VERSION_DEFAULT, bindMap);
	}
	
	/**
	 * 
	 * queryID, version, bindMap을 입력받아 해당 쿼리에 대한 결과를
	 * 리스트로 리턴합니다
	 *
	 * @param queryID
	 * @param version
	 * @param bindMap
	 * @return List<LinkedCaseInsensitiveMap>
	 *
	 */
	@SuppressWarnings("rawtypes")
	public List<LinkedCaseInsensitiveMap> getQueryResult(String queryID, String version, HashMap<String, Object> bindMap) {
		return getQueryResult(FactoryConstant.getFactoryId(), queryID, version, bindMap);
	}
	
	/**
	 * 
	 * queryID, version, bindMap을 입력받아 해당 쿼리에 대한 결과를
	 * 리스트로 리턴합니다
	 *
	 * @param plantID
	 * @param queryID
	 * @param version
	 * @param bindMap
	 * @return List<LinkedCaseInsensitiveMap>
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<LinkedCaseInsensitiveMap> getQueryResult(String plantID, String queryID, String version, HashMap<String, Object> bindMap) {
		Object[] args = new Object[] { plantID, queryID, version };

    	String usrSql = "SELECT queryString FROM customQuery WHERE plantID = ? AND queryID = ? AND queryVersion = ? ";

    	List resultList = null;
    	resultList = SqlMesTemplate.queryForList(usrSql, args);

    	
    	if( resultList.size() < 1 )
    	{
    		logger.error("-----------------------------------------------------------------------------");
    		logger.error("PlantID - " + plantID + ", GetQueryResult : " + queryID + " QueryID Not Found Error");
    		logger.error("-----------------------------------------------------------------------------");
    		throw new CustomException("CM-002", new Object[] { queryID, version });
    	}

    	LinkedCaseInsensitiveMap queryMap = (LinkedCaseInsensitiveMap) resultList.get(0);
    	String dbSql = (String) queryMap.get("queryString");
    	
    	if( bindMap.isEmpty() || bindMap == null )
    	{
    		if( dbSql.indexOf(":") > -1 )
    		{
				throw new CustomException("CM-003", new Object[] {});
    		}
    		logger.info(">> SQL = [" + dbSql + "]");
    	}
    	else
    	{
    		logger.info(">> SQL = [" + SqlLogUtil.getLogFormatSqlStatement(dbSql, bindMap, logger) + "]");
    	}
    	
    	// Bind 변수없이 이벤트가 호출되는 경우 NULL로 처리되도록 설정
    	ArrayList<String> bindParamList = GenericServiceProxy.getMessageAdaptor().getBindParamList(dbSql);
    	for ( int j = 0; j < bindParamList.size(); j++ )
    	{
    		String stringBind = bindParamList.get(j);
    		if ( !bindMap.containsKey(stringBind) )
    		{
    			bindMap.put(stringBind, "");
    		}
    	}

    	resultList.clear();
    	resultList = SqlMesTemplate.queryForList(dbSql, bindMap);
    	
    	return resultList;
	}
}

