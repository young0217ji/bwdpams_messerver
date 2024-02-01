package mes.legacy.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import mes.DBConnection.MongoDBConnectUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.10.16 
 * 
 * @see
 */
public class DYInterfaceManager
{
	private static Log log = LogFactory.getLog(DYInterfaceManager.class);
	
	
	/**
	 * DYP Service 호출
	 * @throws Exception 
	 * 
	 */
	public static void callService()
			throws Exception
	{
		log.debug("DYP Mongo DB Service Start");
		
		MongoClient mongoClient = (MongoClient) MongoDBConnectUtil.getConnection();
		MongoDatabase database = mongoClient.getDatabase("DYP");
		MongoCollection<Document> collection = database.getCollection("LOTPROCESSINGDATA");
		
		/*
		PLANTID	공장코드
		LOTID	LOTID
		PRODUCTID	품번
		SCANID	설비에서 투입되는 자재의 QR code, Barcode 등의 ID 
		WORKORDERID	작업지시서
		PROCESSROUTEID	MES Routing ID
		PROCESSID	MES 공정
		EQUIPMENTID	진행 설비
		PRODUCTTYPE	제품 구분 (제품 도면 번호 등 제품을 구분하기 위한 정보)
		RODTHICK	Rod 두께 (용접될 Rod의 두께)
		WELDINGPASS	용접 패스 수
		GROOVE	그루브 형상 정보
		WELDINGMATRERIAL	용접재 정보
		WIREDIA	용접재(와이어) 지름
		ACTIONTYPE	진행 구분 [ Start | End ]
		EVENTTIME	진행 시간
		
	{
	    _id: ObjectId('655adc7e843fdf38db27f667'),
	    PLANTID: '6000',
	    LOTID: '',
	    PRODUCTID: 'H32392500',
	    SCANID: '20231108155526802',
	    WORKORDERID: '002102578800',
	    PROCESSROUTEID: '50125538',
	    PROCESSID: '10',
	    EQUIPMENTID: 'W220',
	    ACTIONTYPE: 'Start',
	    EVENTTIME: '2023-11-09 16:27:18',
	    _msgid: null,
	    CONV_STATUS: 'Y',
	    CONV_MSG: '',
	    CONV_TS: '2023-12-07 21:25:53'
	}
		*/
		
        Document document = new Document("PLANTID", "PLANTID")
                .append("LOTID", "LOTID")
                .append("PRODUCTID", "PRODUCTID")
                .append("SCANID", "SCANID")
                .append("WORKORDERID", "WORKORDERID")
                .append("PROCESSROUTEID", "PROCESSROUTEID")
                .append("PROCESSID", "PROCESSID")
                .append("EQUIPMENTID", "EQUIPMENTID")
                .append("PRODUCTTYPE", "PRODUCTTYPE")
                .append("RODTHICK", "RODTHICK")
                .append("WELDINGPASS", "WELDINGPASS")
                .append("GROOVE", "GROOVE")
                .append("WELDINGMATRERIAL", "WELDINGMATRERIAL")
                .append("WIREDIA", "WIREDIA")
                .append("ACTIONTYPE", "ACTIONTYPE")
                .append("EVENTTIME", "EVENTTIME")
                ;

        // 문서를 컬렉션에 삽입
        collection.insertOne(document);

        System.out.println("데이터가 성공적으로 삽입되었습니다.");
        log.debug("DYP Mongo DB Service End");
	}
}
