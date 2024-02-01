package mes.DBConnection;

import java.sql.*;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class ORCLDBConnectUtil
{
	 private static ORCLDBConnectUtil instance = null;
	 
	 /**
	  * 생성자
	  * 
	  * @param
	  * @return
	  */
	 public ORCLDBConnectUtil() {
	     String oracleDriver = "oracle.jdbc.driver.OracleDriver";
	     try {
	         Class.forName(oracleDriver);
	     } catch (ClassNotFoundException e) {}


	 }	 

	 /**
	  * 
	  * instance 생성 후 리턴
	  *
	  * @param
	  * @return
	  *
	  */
	 public static synchronized ORCLDBConnectUtil getInstance() {
	     if (instance == null) {
	         synchronized (ORCLDBConnectUtil.class) {
	         instance = new ORCLDBConnectUtil();
	         }
	     }
	     return instance;
	 }
	 

	 /**
	  * 
	  * Connection 열기
	  *
	  * @param
	  * @return
	  *
	  */
	 public static synchronized Connection getConnection() {
	     String oracleURL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=" +
//	     		"(LOAD_BALANCE=ON)" +
//	     		"(FAILOVER=ON)" +

			     // 실제 DB
			     "(ADDRESS=(PROTOCOL = TCP)(HOST = 70.42.22.106)(PORT = 1521))" +
			     ")" +
			     "(CONNECT_DATA = (SERVICE_NAME = orcl)" +
	     		"))";

	     // 기존
	     String user = "SCOTT";
	     String password = "TIGER";
	     Connection conn = null;
	 
	     try {
	         conn = DriverManager.getConnection(oracleURL, user, password);
	         //System.out.println("connect...");
	     } catch (SQLException e)
	     {
	    	 System.out.println("connect error:" + e.getMessage());
	     }
	     return conn;

	     
	     }
}
