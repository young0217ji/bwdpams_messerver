package mes.DBConnection;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * @author LSMESSolution
 * 
 * @since 2023.10.16 
 * 
 * @see
 */
public class MongoDBConnectUtil
{
	/**
	 * 
	 * Connection 열기
	 *
	 * @param
	 * @return
	 *
	 */
	public static synchronized MongoClient getConnection() {

		String sUrl = "mongodb://interx:interx%40504@10.21.1.25:27017/?retryWrites=true&serverSelectionTimeoutMS=5000&connectTimeoutMS=10000&authSource=BW&authMechanism=SCRAM-SHA-1";
		MongoClientURI oConnectURL = new MongoClientURI(sUrl);
		return new MongoClient(oConnectURL);
	}
}
