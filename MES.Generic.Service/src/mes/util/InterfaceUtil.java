package mes.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;

import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class InterfaceUtil
{
	/**
	 * MSSQL 서버 Mail 발송서비스를 이용한 메일발송
	 * 
	 * @param sProfileName
	 * @param sRecipients
	 * @param sSubject
	 * @param sBody
	 * @param sFormat
	 */
	public static void sendMailMSSQL(String sProfileName, String sRecipients, String sSubject, String sBody, String sFormat)
	{
		Map<String, Object> oMainBind = new HashMap<String, Object>();
		oMainBind.put("profile_name", sProfileName);
		oMainBind.put("recipients", sRecipients);
		oMainBind.put("subject", sSubject);
		oMainBind.put("body", sBody);
		if ( sFormat == null || sFormat.isEmpty() ) {
			oMainBind.put("body_format", "HTML");	
		}
		else {
			oMainBind.put("body_format", sFormat);
		}
		
		SqlMesTemplate.executeProcedure("msdb.dbo.sp_send_dbmail", oMainBind);
	}

	/**
	 * KAIS 대상 Recode Set의 인터페이스 실패를 기록한다.
	 * 
	 * @param flagIUD
	 * @param rsSourceData - 대상 Recode Set
	 * @param errorMessage - 실패 오류 메시지
	 * @throws SQLException
	 */
	public static void recordFailIfInKais(String flagIUD, ResultSet rsSourceData, String errorMessage)
			throws SQLException
	{
			if ( errorMessage.length() >= 300 )
			{
				errorMessage = errorMessage.substring(0, 150);
			}
			if ( flagIUD.equalsIgnoreCase("I") )
			{
				rsSourceData.updateTimestamp("last_if_datetime", DateUtil.getCurrentTimestamp());
			}
			else
			{
				rsSourceData.updateTimestamp("mlast_if_datetime", DateUtil.getCurrentTimestamp());
			}
			rsSourceData.updateString("if_complet_id", "E");
			rsSourceData.updateString("error_log", errorMessage);
			rsSourceData.updateRow();
	}
	
	/**
	 * KAIS 대상 Recode Set의 인터페이스 성공을 기록한다.
	 * 
	 * @param con - 연결
	 * @param flgUID
	 * @param rsSourceData - 대상 Recode Set
	 * @param errorMessage
	 * @throws SQLException
	 */
	public static void recordOrderFailIfInKais(Connection con, String flagIUD, ResultSet rsSourceData, String errorMessage)
			throws SQLException
	{
		if ( errorMessage.length() >= 300 )
		{
			errorMessage = errorMessage.substring(0, 150);
		}
		
		String sqlNNData = "";
		if ( flagIUD.equalsIgnoreCase("I") )
		{
			// DATA Update
			sqlNNData =  " UPDATE TS03_MES_IF_ORDER "+
						 "    SET IF_COMPLET_ID    = ? " +
						 "      , LAST_IF_DATETIME = ? " +
						 "      , ERROR_LOG        = ? " +
						 "  WHERE REP_PROD_PLANT   = ? " +
						 "    AND ORDER_NO         = ? " ;
		}
		else
		{
			// DATA Update
			sqlNNData =  " UPDATE TS03_MES_IF_ORDER "+
						 "    SET IF_COMPLET_ID     = ? " +
						 "      , MLAST_IF_DATETIME = ? " +
						 "      , ERROR_LOG         = ? " +
						 "  WHERE REP_PROD_PLANT    = ? " +
						 "    AND ORDER_NO          = ? " ;
		}
		
		PreparedStatement stat = null;
		
		stat = con.prepareStatement(sqlNNData, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		stat.setString(1, "E");
		stat.setTimestamp(2, DateUtil.getCurrentTimestamp());
		stat.setString(3, errorMessage);
		stat.setString(4, rsSourceData.getString("rep_prod_plant"));
		stat.setString(5, rsSourceData.getString("order_no"));

		stat.setQueryTimeout(5);
		
		stat.executeUpdate();
		
		if ( stat != null )
		{
			stat.close();
		}		
	}		

	/**
	 * KAIS 대상 Recode Set의 인터페이스 성공을 기록한다.
	 * 
	 * @param falgUID
	 * @param rsSourceData - 대상 Recode Set
	 * @throws SQLException
	 */
	public static void recordSucessIfInKais(String flagIUD, ResultSet rsSourceData)
			throws SQLException
	{
		if ( flagIUD.equalsIgnoreCase("I") )
		{
			rsSourceData.updateTimestamp("last_if_datetime", DateUtil.getCurrentTimestamp());
		}
		else
		{
			rsSourceData.updateTimestamp("mlast_if_datetime", DateUtil.getCurrentTimestamp());
		}
		rsSourceData.updateString("if_complet_id", "Y");
		rsSourceData.updateString("error_log", "");
		rsSourceData.updateRow();
	}
	
	
	/**
	 * KAIS 대상 Recode Set의 인터페이스 성공을 기록한다.
	 * 
	 * @param con
	 * @param falgUID
	 * @param rsSourceData - 대상 Recode Set
	 * @throws SQLException
	 */
	public static void recordOrderSucessIfInKais(Connection con, String flagIUD, ResultSet rsSourceData)
			throws SQLException
	{
		String sqlNNData = "";
		if ( flagIUD.equalsIgnoreCase("I") )
		{
			// DATA Update
			sqlNNData =  " UPDATE TS03_MES_IF_ORDER "+
						 "    SET IF_COMPLET_ID    = ? " +
						 "      , LAST_IF_DATETIME = ? " +
						 "      , ERROR_LOG        = null " +
						 "  WHERE REP_PROD_PLANT   = ? " +
						 "    AND ORDER_NO         = ? " ; 
		}
		else
		{
			// DATA Update
			sqlNNData =  " UPDATE TS03_MES_IF_ORDER "+
						 "    SET IF_COMPLET_ID     = ? " +
						 "      , MLAST_IF_DATETIME = ? " +
						 "      , ERROR_LOG         = null " +
						 "  WHERE REP_PROD_PLANT    = ? " +
						 "    AND ORDER_NO          = ? " ;
		}
		
		PreparedStatement stat = null;
		
		stat = con.prepareStatement(sqlNNData, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		stat.setString(1, "Y");
		stat.setTimestamp(2, DateUtil.getCurrentTimestamp());
		stat.setString(3, rsSourceData.getString("rep_prod_plant"));
		stat.setString(4, rsSourceData.getString("order_no"));

		stat.setQueryTimeout(5);
		
		stat.executeUpdate();
		
		if ( stat != null )
		{
			stat.close();
		}		
	}	


	/**
	 * KAIS 인터페이스 마스터 테이블의 해당 IFID에 대하여 인터페이스 완료를 표시한다.
	 * 
	 * @param con - 현재열고 있는 DB connection
	 * @param ifId - 완료 처리 대상 IF ID
	 * @throws SQLException
	 */
	public static void recodeCompleteIfMasterInKais(Connection con, String ifId)
			throws SQLException
	{

		if ( !ifId.equals("0") )
		{

			String sql = "";
			sql += "UPDATE ts03_mes_if_monitoring ";
			sql += "SET change_id = 'N' ";
			sql += "WHERE if_id = ? ";

			PreparedStatement stat = null;
			stat = con.prepareStatement(sql);
			stat.setString(1, ifId);
			stat.executeUpdate();
			stat.close();
		}
	}


	/**
	 * 인터페이스 테이블 데이터 수정후 인터페이스 마스터 테이블 표시
	 * 
	 * @param tableName - 테이블 명
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static String markIfObMasterFromTableName(String tableName)
	{

		String checkSql = "";
		checkSql += "SELECT call_function, change_id  \n";
		checkSql += "FROM ts03_mes_if_monitoring \n";
		checkSql += "WHERE UPPER(source_table) = :TABLENAME ";

		Map<String, Object> checkBindMap = new HashMap<String, Object>();
		checkBindMap.put("TABLENAME", tableName.toUpperCase());

		@SuppressWarnings("unchecked")
		List<LinkedCaseInsensitiveMap> resultList = (List<LinkedCaseInsensitiveMap>) SqlMesTemplate.queryForList(checkSql,
				checkBindMap);

		String callFunctionName = "";
		String changeFlag = Constant.FLAG_YN_NO;

		if ( resultList.size() > 0 )
		{

			if ( resultList.get(0).get("call_function") != null )
			{
				callFunctionName = resultList.get(0).get("call_function").toString();
			}
			
			if ( resultList.get(0).get("change_id") != null )
			{
				changeFlag = resultList.get(0).get("change_id").toString();
			}

			if ( changeFlag.equals(Constant.FLAG_YN_NO) )
			{
				String updateSql = "";
				updateSql += "UPDATE ts03_mes_if_monitoring \n";
				updateSql += "SET change_id = :CHANGEFLAG \n";
				updateSql += "WHERE UPPER(source_table) = :TABLENAME ";

				Map<String, Object> updateBindMap = new HashMap<String, Object>();
				updateBindMap.put("TABLENAME", tableName.toUpperCase());
				updateBindMap.put("CHANGEFLAG", Constant.FLAG_YN_YES);

				SqlMesTemplate.update(updateSql, updateBindMap);
			}
		}

		return callFunctionName;
	}

}

