package mes.util;

import java.util.HashMap;

import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class EventInfoUtil
{
	/**
	 * 
	 * XML Message를 HashMap으로 Parsing하지 않고 TxnInfo 생성
	 * 
	 * @param doc
	 * @return TxnInfo
	 * @throws Exception
	 * 
	 */
	public static TxnInfo setTxnInfo( Document doc )
	{
		TxnInfo txnInfo = new TxnInfo();
		
		txnInfo.setBehaviorName( "" );
		txnInfo.setTxnId( doc.getDocument().getRootElement().getChild("header").getChildText("messagename") );
		
		if ( doc.getDocument().getRootElement().getChild("body").getChildText("EVENTUSER") != null )
		{
			txnInfo.setTxnUser( doc.getDocument().getRootElement().getChild("body").getChildText("EVENTUSER") );
		}
		else
		{
			txnInfo.setTxnUser( doc.getDocument().getRootElement().getChild("body").getChildText("MACHINENAME") );
		}
		
		if ( doc.getDocument().getRootElement().getChild("body").getChildText("EVENTCOMMENT") != null )
		{
			txnInfo.setTxnComment( doc.getDocument().getRootElement().getChild("body").getChildText("EVENTCOMMENT") );
		}
		else
		{
			txnInfo.setTxnComment( "" );
		}
		
		if ( doc.getDocument().getRootElement().getChild("body").getChildText("REASONCODETYPE") != null )
		{
			txnInfo.setTxnReasonCodeType( doc.getDocument().getRootElement().getChild("body").getChildText("REASONCODETYPE") );
		}
		else
		{
			txnInfo.setTxnReasonCodeType( "" );
		}
		
		if ( doc.getDocument().getRootElement().getChild("body").getChildText("REASONCODE") != null )
		{
			txnInfo.setTxnReasonCode( doc.getDocument().getRootElement().getChild("body").getChildText("REASONCODE") );
		}
		else
		{
			txnInfo.setTxnReasonCode( "" );
		}
		
		txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
		txnInfo.setEventTime(DateUtil.getTimestamp(txnInfo.getEventTimeKey().substring(0, 17)));
//		txnInfo.setEventTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
		/* 
		 	현재 DB 에 저장되어 있는 TimeKey 보다 추가하려는 TimeKey 가 작으면 에러 발생(즉, 서버 노드가 2대 이상이고 시간 역전현상 발생에 대한 체크 여부)
			false 로 설정할 경우 에러 발생하지 않고 유효한 TimeKey 재생성
		 	기본값은 true 로 설정되어 있음.
		*/
		txnInfo.setCheckTimekeyValidation(true);
		
		// WORKTYPE 에 대한 값 세팅
		if ( doc.getDocument().getRootElement().getChild("body").getChildText("WORKTYPE") != null )
		{
			txnInfo.setWorkType( doc.getDocument().getRootElement().getChild("body").getChildText("WORKTYPE") );
		}
		else
		{
			txnInfo.setWorkType( "" );
		}
		
		// DEVICECODE 에 대한 값 세팅
		if ( doc.getDocument().getRootElement().getChild("body").getChildText("DEVICECODE") != null )
		{
			txnInfo.setDeviceCode( doc.getDocument().getRootElement().getChild("body").getChildText("DEVICECODE") );
		}
		else
		{
			txnInfo.setDeviceCode( "" );
		}
		
		// DEVICEADDRESS 에 대한 값 세팅
		if ( doc.getDocument().getRootElement().getChild("body").getChildText("DEVICEADDRESS") != null )
		{
			txnInfo.setDeviceAddress( doc.getDocument().getRootElement().getChild("body").getChildText("DEVICEADDRESS") );
		}
		else
		{
			txnInfo.setDeviceAddress( "" );
		}
		
		// SCANFLAG 에 대한 값 세팅
		if ( doc.getDocument().getRootElement().getChild("body").getChildText("SCANFLAG") != null )
		{
			txnInfo.setScanFlag( doc.getDocument().getRootElement().getChild("body").getChildText("SCANFLAG") );
		}
		else
		{
			txnInfo.setScanFlag( "" );
		}
		
		// Language 에 대한 값 세팅
		if ( doc.getDocument().getRootElement().getChild("body").getChildText("LANGUAGE") != null )
		{
			txnInfo.setLanguage( doc.getDocument().getRootElement().getChild("body").getChildText("LANGUAGE") );
		}
		else
		{
			txnInfo.setLanguage( "ko" );
		}
		
		return txnInfo;
		
	}
	
	/**
	 * 
	 * XML Message를 HashMap으로 Parsing후 TxnInfo 생성
	 * 
	 * @param doc
	 * @return TxnInfo
	 * 
	 */
	public static TxnInfo createTxnInfo( Document doc )
	{
		HashMap<String, String> hashMapXml = MessageParse.getHashXmlParse(doc);
		
		TxnInfo txnInfo = new TxnInfo();
		
		String eventName = hashMapXml.get("EVENTNAME");
		String eventUser = hashMapXml.get("EVENTUSER");
		String eventComment = hashMapXml.get("EVENTCOMMENT");
		String reasonCodeType = hashMapXml.get("REASONCODETYPE");
		String reasonCode = hashMapXml.get("REASONCODE");
		String workType = hashMapXml.get("WORKTYPE");
		String deviceCode = hashMapXml.get("DEVICECODE");
		String deviceAddress = hashMapXml.get("DEVICEADDRESS");
		String scanFlag = hashMapXml.get("SCANFLAG");
		String language = hashMapXml.get("LANGUAGE");
		
		txnInfo.setBehaviorName( "" );
		txnInfo.setTxnId( eventName );
		txnInfo.setTxnUser( eventUser );
		
		if ( eventComment == null )
			eventComment = "";
		
		txnInfo.setTxnComment( eventComment );
		txnInfo.setEventTime( DateUtil.getCurrentTimestamp());
		
		if ( reasonCodeType == null )
			reasonCodeType = "";
		
		txnInfo.setTxnReasonCodeType( reasonCodeType );
		
		if ( reasonCode == null )
			reasonCode = "";
		
		txnInfo.setTxnReasonCode( reasonCode );
		
		txnInfo.setEventTime(DateUtil.getTimestamp(DateUtil.getCurrentEventTimeKey()));
		txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
//		txnInfo.setEventTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
		// 현재 DB 에 저장되어 있는 TimeKey 보다 추가하려는 TimeKey 가 작으면 에러 발생(즉, 서버 노드가 2대 이상이고 시간 역전현상 발생에 대한 체크 여부)
		// false 로 설정할 경우 에러 발생하지 않고 유효한 TimeKey 재생성
		// 기본값은 true 로 설정되어 있음.
		//txnInfo.setCheckTimekeyValidation(true);
		
		// WORKTYPE 에 대한 값 세팅
		if ( workType == null )
			workType = "";

		txnInfo.setWorkType( workType );
		
		// DEVICECODE 에 대한 값 세팅
		if ( deviceCode == null )
			deviceCode = "";

		txnInfo.setDeviceCode( deviceCode );
		
		// DEVICEADDRESS 에 대한 값 세팅
		if ( deviceAddress == null )
			deviceAddress = "";

		txnInfo.setDeviceAddress( deviceAddress );
		
		// SCANFLAG 에 대한 값 세팅
		if ( scanFlag == null )
			scanFlag = "";

		txnInfo.setScanFlag( scanFlag );
		
		// LANGUAGE 에 대한 값 세팅
		if ( language == null )
			language = "ko";

		txnInfo.setLanguage( language );
		
		return txnInfo;
	}
	
	/**
	 * 
	 * HashMap으로 Parsing된 Argument를 받아 TxnInfo 생성
	 * 
	 * @param hashMap
	 * @return TxnInfo
	 * 
	 */
	public static TxnInfo createTxnInfo(HashMap<String, String> hashMap)
	{
		TxnInfo txnInfo = new TxnInfo();
		
		String eventName = hashMap.get("EVENTNAME");
		String eventUser = hashMap.get("EVENTUSER");
		String eventComment = hashMap.get("EVENTCOMMENT");
		String reasonCodeType = hashMap.get("REASONCODETYPE");
		String reasonCode = hashMap.get("REASONCODE");
		String workType = hashMap.get("WORKTYPE");
		String deviceCode = hashMap.get("DEVICECODE");
		String deviceAddress = hashMap.get("DEVICEADDRESS");
		String scanFlag = hashMap.get("SCANFLAG");
		String language = hashMap.get("LANGUAGE");
		
		txnInfo.setBehaviorName( "" );
		txnInfo.setTxnId( eventName );
		txnInfo.setTxnUser( eventUser );
		
		if ( eventComment == null )
			eventComment = "";
		
		txnInfo.setTxnComment( eventComment );
		txnInfo.setEventTime( DateUtil.getCurrentTimestamp());
		
		if ( reasonCodeType == null )
			reasonCodeType = "";
		
		txnInfo.setTxnReasonCodeType( reasonCodeType );
		
		if ( reasonCode == null )
			reasonCode = "";
		
		txnInfo.setTxnReasonCode( reasonCode );
		
		txnInfo.setEventTime(DateUtil.getTimestamp(DateUtil.getCurrentEventTimeKey()));
		txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
//		txnInfo.setEventTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
		// 현재 DB 에 저장되어 있는 TimeKey 보다 추가하려는 TimeKey 가 작으면 에러 발생(즉, 서버 노드가 2대 이상이고 시간 역전현상 발생에 대한 체크 여부)
		// false 로 설정할 경우 에러 발생하지 않고 유효한 TimeKey 재생성
		// 기본값은 true 로 설정되어 있음.
		//txnInfo.setCheckTimekeyValidation(true);
		
		// WORKTYPE 에 대한 값 세팅
		if ( workType == null )
			workType = "";

		txnInfo.setWorkType( workType );
		
		// DEVICECODE 에 대한 값 세팅
		if ( deviceCode == null )
			deviceCode = "";

		txnInfo.setDeviceCode( deviceCode );
		
		// DEVICEADDRESS 에 대한 값 세팅
		if ( deviceAddress == null )
			deviceAddress = "";

		txnInfo.setDeviceAddress( deviceAddress );
		
		// SCANFLAG 에 대한 값 세팅
		if ( scanFlag == null )
			scanFlag = "";

		txnInfo.setScanFlag( scanFlag );
		
		// LANGUAGE 에 대한 값 세팅
		if ( language == null )
			language = "ko";

		txnInfo.setLanguage( language );
		
		return txnInfo;
	}
	/**
	 * 
	 * 새로운 txnInfo를 생성합니다
	 *
	 * @param sEventName
	 * @param sEventUser
	 * @param sTimeKey
	 * @return txnInfo
	 *
	 */
	public static TxnInfo makeTxnInfo(String sEventName, String sEventUser, String sTimeKey)
	{
		TxnInfo oTxnInfo = new TxnInfo();
		if (sTimeKey.equals(""))
		{
			sTimeKey = DateUtil.getCurrentEventTimeKey();
		}
		oTxnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
		oTxnInfo.setEventTime(DateUtil.getTimestamp(sTimeKey));
		oTxnInfo.setTxnId(sEventName);
		oTxnInfo.setTxnUser(sEventUser);
		oTxnInfo.setTxnComment("");
		oTxnInfo.setWorkType("");
		oTxnInfo.setDeviceCode("");
		oTxnInfo.setDeviceAddress("");
		oTxnInfo.setScanFlag("");
		oTxnInfo.setLanguage("ko");
		return oTxnInfo;
	}
	
	
}

