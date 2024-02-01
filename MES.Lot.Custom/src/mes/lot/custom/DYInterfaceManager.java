package mes.lot.custom;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.DBConnection.MongoDBConnectUtil;
import mes.constant.Constant;
import mes.lot.data.DY_SENDPUSH_LOG;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.VIEWLOTTRACKING;
import mes.master.data.DY_MASTERPRODUCTIONSCHEDULE;

/**
 * @author LSMESSolution
 * 
 * @since 2023.12.14 
 * 
 * @see
 */
public class DYInterfaceManager
{
	private static Log log = LogFactory.getLog(DYInterfaceManager.class);
	
	
	/**
	 * DYP Service 호출
	 * @param oLotInfo
	 * @param oViewLotInfo
	 * @param oTxnInfo
     * @return 
	 * @throws Exception 
	 */
	public static void callService(LOTINFORMATION oLotInfo, VIEWLOTTRACKING oViewLotInfo, TxnInfo oTxnInfo)
			throws Exception
	{
		log.debug("DYP Mongo DB Service Start");
		
		MongoClient mongoClient = (MongoClient) MongoDBConnectUtil.getConnection();
		MongoDatabase database = mongoClient.getDatabase("DYP");
		MongoCollection<Document> collection = database.getCollection("LOTPROCESSINGDATA_TEST");
		
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
		
		DY_MASTERPRODUCTIONSCHEDULE oProductInfo = new DY_MASTERPRODUCTIONSCHEDULE();
		oProductInfo.setKeyPLANTID(oLotInfo.getPLANTID());
		oProductInfo.setKeyPRODUCTID(oLotInfo.getPRODUCTID());
		try {
		oProductInfo = (DY_MASTERPRODUCTIONSCHEDULE) oProductInfo.excuteDML(SqlConstant.DML_SELECTROW);
		}
		catch ( Exception e ) {
			// 기준정보 데이터 없음
		}
		
        Document document = new Document("PLANTID", oLotInfo.getPLANTID())
                .append("LOTID", oLotInfo.getKeyLOTID())
                .append("PRODUCTID", oLotInfo.getPRODUCTID())
                .append("SCANID", oLotInfo.getMATERIALLOTID())
                .append("WORKORDERID", oLotInfo.getWORKORDER())
                .append("PROCESSROUTEID", oLotInfo.getPROCESSROUTEID())
                .append("PROCESSID", oViewLotInfo.getPROCESSID())
                .append("EQUIPMENTID", oViewLotInfo.getEQUIPMENTID())
                .append("PRODUCTTYPE", oLotInfo.getLOTTYPE())
                .append("RODTHICK", oProductInfo.getRODTHICK())
                .append("WELDINGPASS", oProductInfo.getWELDINGPASS())
                .append("GROOVE", oProductInfo.getGROOVE())
                .append("WELDINGMATRERIAL", oProductInfo.getWELDINGMATERIAL())
                .append("WIREDIA", oProductInfo.getWIREDIA())
                .append("ACTIONTYPE", oViewLotInfo.getACTIONTYPE())
                .append("EVENTTIME", DateUtil.dateToString(DateUtil.FORMAT_DEFAULT, oTxnInfo.getEventTime()))
                ;

        // 문서를 컬렉션에 삽입
        collection.insertOne(document);

        System.out.println("데이터가 성공적으로 삽입되었습니다.");
        log.debug("DYP Mongo DB Service End");
	}
	
	/**
	 * DYP SMS 전송 메세지 Input Service 호출
	 * @param sPlantID
	 * @param sUserID
	 * @param sMessage
     * @return 
	 * @throws Exception 
	 */
	public static void callSMSInputService(String sPlantID, String sUserID, String sMessage)
	{
		callSMSInputService(sPlantID, sUserID, sMessage, "");
	}
	
	/**
	 * DYP SMS 전송 메세지 Input Service 호출
	 * @param sPlantID
	 * @param sUserID
	 * @param sMessage
	 * @param eventName
     * @return 
	 * @throws Exception 
	 */
	public static void callSMSInputService(String sPlantID, String sUserID, String sMessage, String eventName)
	{
		/*
		INSERT INTO [172.31.15.169].[PUSH_log].[dbo].[SEND_PUSH]
                (USERID          -- 사번
                ,MESSAGE         -- 내용
                ,PLANT           -- '6000'
                ,IFFLG           -- 'N' 고정
                ,INSERTDT        -- GETDATE()
                ,UPDATEDT        -- GETDATE()
                ,SYSTEM_GUBUN    -- 'MES'
                ,STATUS)         -- 0으로고정
                VALUES
                (
                :USERID
                ,:MESSAGE
                ,:PLANT
                ,'N'
                ,GETDATE()
                ,GETDATE()
                ,'MES'
                ,'0'
                );
        */
		
		String sInsertSql = "";
		int iCount = 0;
		String sErrorComment = "";
	
		try
		{
			sInsertSql = " INSERT INTO [172.31.15.169].[PUSH_log].[dbo].[SEND_PUSH] "
				+ " (USERID "
				+ " ,MESSAGE "
				+ " ,PLANT "
				+ " ,IFFLG "
				+ " ,INSERTDT "
				+ " ,UPDATEDT "
				+ " ,SYSTEM_GUBUN "
				+ " ,STATUS) "
				+ " VALUES "
				+ " ( "
				+ " :USERID "
				+ " ,:MESSAGE "
				+ " ,:PLANTID "
				+ " ,'N' "
				+ " ,GETDATE() "
				+ " ,GETDATE() "
				+ " ,'MES' "
				+ " ,'0' "
				+ " ); ";
		
				HashMap<String, String> oBindMap = new HashMap<String, String>();
				oBindMap.put("PLANTID", sPlantID);
				oBindMap.put("USERID", sUserID);
				oBindMap.put("MESSAGE", sMessage);
				
				iCount = SqlMesTemplate.update(sInsertSql, oBindMap);
		}
		catch(Exception err)
		{
			sErrorComment = ConvertUtil.Object2String(err);
			iCount = 0;
		}
		  
		log.debug("DYP SMS Input Complete! : Count - " + iCount);
		 
		
		// Send Message 이력 등록 (DY_SENDPUSH_LOG)
		//DY_SENDPUSH_LOG		
		DY_SENDPUSH_LOG dy_sendpush_log = new DY_SENDPUSH_LOG();
		
		// Key Value Setting
		dy_sendpush_log.setKeyPLANTID(sPlantID);
		dy_sendpush_log.setKeyDATAKEY(DateUtil.getCurrentEventTimeKey());

		// Data Value Setting
		dy_sendpush_log.setUSERID(sUserID);
		dy_sendpush_log.setMESSAGE(sMessage);
		dy_sendpush_log.setIFFLG("N");
		dy_sendpush_log.setINSERTDT(DateUtil.getCurrentTimestamp());
		dy_sendpush_log.setUPDATEDT(DateUtil.getCurrentTimestamp());
		dy_sendpush_log.setSYSTEM_GUBUN("MES");
		dy_sendpush_log.setSTATUS("0");
		dy_sendpush_log.setEVENTNAME(eventName);
		dy_sendpush_log.setRESULTVALUE(ConvertUtil.Object2String(iCount));
		dy_sendpush_log.setERRORCOMMENT(sErrorComment);
		
		dy_sendpush_log.excuteDML(SqlConstant.DML_INSERT);	
	}
}
