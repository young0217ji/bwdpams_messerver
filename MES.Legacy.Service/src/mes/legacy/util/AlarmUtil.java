package mes.legacy.util;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.orm.sql.SqlMesTemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class AlarmUtil
{
	Log  log = LogFactory.getLog(getClass());
	
	public static void ActionAlarm( )
	{
		//Timestamp curTime = TimeUtils.getCurrentTimestamp();
		//String curTimeKey = TimeUtils.getCurrentEventTimeKey();
		
//	}
//	
//	public void sendEmail(AlarmDefinition alarmDef, Alarm alarmData) throws UnsupportedEncodingException, MessagingException
//	{
//		log.info("=========================== SEND EMAIL ========================================");
//		
//		TnxIFEMail email = new TnxIFEMail();
//		
//		HashMap<String, String> udfMap = (HashMap<String, String>) alarmDef.getUdfs();
//		String userIds = udfMap.get("USERIDS");
//		
//		String[] userList = userIds.split(",");
//		String subject = alarmDef.getDescription();
//		String toUser = "";
//		
//		/*
//		String message = subject + "\n"
//			+ "설비 명 : " + alarmData.getMachineName() + "\n"
//			+ "알람 명 : " +  alarmDef.getDescription() + "\n"
//			+ "알람 확인후 조치 바랍니다.";
//		*/
//		
//		String errorCode = "EM-006";
//		ErrorDef errorDef = GenericServiceProxy.getErrorDefMap().getErrorDef(errorCode);
//		if ( errorDef == null )
//		{
//			errorDef = GenericServiceProxy.getErrorDefMap().getErrorDef("UndefinedCode");
//		}
//		String korTempMsg = errorDef.getKor_errorMessage();
//		String message = MessageFormat.format(korTempMsg, alarmData.getMachineName(),alarmDef.getDescription());
//		log.info("Error Code : " + errorCode);
//		
//		if (alarmData.getKey().getAlarmId().equals("EQPDOWN"))
//		{
//			String reasonCodeDesc = getReasonCodeDescription(alarmData.getReasonCodeType(), alarmData.getReasonCode());
//			
//			message = subject + "\r\n"
//			+ "설비명:" + alarmData.getMachineName() + "\r\n"
//			+ "고장코드:" +"["+ alarmData.getReasonCode()+"]" + reasonCodeDesc + "\r\n"// 고장코드 + 고장내용
//			+ "요청내용:" + alarmData.getUdfs().get("RequestComment");
//		}
//		
//		UserProfile userProfile = new UserProfile();
//		
//		for(int i = 0; i < userList.length; i++)
//		{
//			String userId = userList[i];
//			
//			UserProfileKey userKey = new UserProfileKey();
//			userKey.setUserId(userId);
//			
//			try
//			{
//				userProfile = UserServiceProxy.getUserProfileService().selectByKey(userKey);
//			}
//			catch(Exception e )
//			{
//				continue;
//			}
//			
//			
//			HashMap<String, String> userMap = (HashMap<String, String>) userProfile.getUdfs();
//			
//			String userAccount = userMap.get("USERACCOUNT");
//			String emailAdd = userMap.get("EMAIL");
//			
//			log.info("USERACCOUNT" + i + " : " + userAccount);
//			
//			if (userAccount != null && !userAccount.isEmpty())
//			{
//				toUser += userMap.get("USERACCOUNT") + "@kccworld.co.kr";
//			}
//			else
//			{	
//				log.info("USERINFO : " + userProfile.getUserName() + " " + emailAdd);
//				
//				toUser += emailAdd;
//			}
//			
//			if (i < userList.length -1 )
//			{
//				toUser += ";";
//			}
//		}
//		
//		HashMap<String, String> dataMap = new HashMap<String, String>();
//		
//		dataMap.put("FROMUSER", "MES");
//		dataMap.put("TOUSER", toUser);
//		dataMap.put("SUBJECT", subject);
//		dataMap.put("MESSAGE", message);
//		
//		log.info("TOUSER : " + toUser);
//		log.info("SUBJECT : " + subject);
//		log.info("MESSAGE"+ message);
//		
//		email.sendMail(dataMap, ";");
//	}
//	
//	public void sendMessage(AlarmDefinition alarmDef, Alarm alarmData) throws RemoteException, ServiceException
//	{
//		log.info("=========================== SEND Message ========================================");
//		
//		// 금란지교
//		TnxIFMessage messageGR = new TnxIFMessage();
//		
//		HashMap<String, String> udfMap = (HashMap<String, String>) alarmDef.getUdfs();
//		String userIds = udfMap.get("USERIDS");
//		String subject = alarmDef.getDescription();
//		String[] userList = userIds.split(",");
//		String toUser = "";
//		
//		String message = subject + "vbCrLf" //"\n"
//			+ "설비 명 : " + alarmData.getMachineName() + "vbCrLf"  //"\n"
//			+ "고장 코드 :" + alarmData.getReasonCode() + "vbCrLf"  //"\n"
//			+ "알람 명 : " +  alarmDef.getDescription() + "vbCrLf" //"\n"
//			+ "알람 상세 : " +  alarmData.getLastEventComment() + "vbCrLf" //"\n"
//			+ "알람 확인후 조치 바랍니다.";
//		
//		if (alarmData.getKey().getAlarmId().equals("EQPDOWN"))
//		{
//			String reasonCodeDesc = getReasonCodeDescription(alarmData.getReasonCodeType(), alarmData.getReasonCode());
//			
//			message = subject + "vbCrLf"
//			+ "설비명:" + alarmData.getMachineName() + "vbCrLf"
//			+ "고장코드:" +"["+ alarmData.getReasonCode()+"]" + reasonCodeDesc + "vbCrLf"// 고장코드 + 고장내용
//			+ "요청내용:" + alarmData.getUdfs().get("RequestComment");
//		}
//		
//		/*
//		String errorCode = "EM-006";
//		ErrorDef errorDef = GenericServiceProxy.getErrorDefMap().getErrorDef(errorCode);
//		if ( errorDef == null )
//		{
//			errorDef = GenericServiceProxy.getErrorDefMap().getErrorDef("UndefinedCode");
//		}
//		String korTempMsg = errorDef.getKor_errorMessage();
//		String message = MessageFormat.format(korTempMsg, alarmData.getMachineName(),alarmDef.getDescription());
//		log.info("Error Code : " + errorCode);
//		*/
//		
////		UserProfile userProfile = new UserProfile();
//		
//		for(int i = 0; i < userList.length; i++)
//		{
//			String userId = userList[i];
//			
//			UserProfileKey userKey = new UserProfileKey();
//			userKey.setUserId(userId);
//			
//			try
//			{
////				userProfile = UserServiceProxy.getUserProfileService().selectByKey(userKey);
//			}
//			catch(Exception e )
//			{
//				continue;
//			}
//			
//			toUser += userId;
//			
//			if (i < userList.length -1 )
//			{
//				toUser += ",";
//			}
//		}
//		
//		HashMap<String, String> dataMap = new HashMap<String, String>();
//		
//		// 메시지 보낼때 보내는 사람의 ID가 필요함
//		// 시스템에서 보낼때 00000000 - 메시지 안감 확인 필요
//		// 등록된 사용자 ID를 보내는 User로 했을 경우, 개인PC에서 테스트 할때는 Message가 가지만 테스트 서버에서는 Message 안날라감
//		
//		String sendUserID = "10080982";
//		
//		dataMap.put("RECEIVEERID", toUser);
//		dataMap.put("SENDERID", sendUserID);
//		dataMap.put("SENDERNAME", "MES");
//		dataMap.put("MESSAGE", message);
//		
//		log.info("SEND USER : " + sendUserID);
//    	log.info("TOUSER : " + toUser);
//		log.info("MESSAGE"+ message);
//		
//		messageGR.sendMessage(dataMap, ",");
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void sendPDASMS(AlarmDefinition alarmDef, Alarm alarmData) throws RemoteException, ServiceException
//	{
//		log.info("=========================== SEND SMS ========================================");
//		// PDA 80 Byte 메세지 제한
//		
//		// SMS
//		TnxIFSMS sms = new TnxIFSMS();
//		String userName;
//		
////		HashMap<String, String> udfMap = (HashMap<String, String>) alarmDef.getUdfs();
////		String userIds = udfMap.get("USERIDS");
//		String subject = "라인 긴급상황!!!";
//		String phoneNumber= "";
//		
//		// PDA 전화번호 가져오기 - 최근 하루동안 로그인한 PDA전화 번호만 가져오기
//		String pdaSql = "SELECT" +
//				" PHONENUMBER" +
//				" FROM TS03_MES_EMPDAMANAGER" +
//				" WHERE LOGINTIME > SYSDATE - 1";
//		
//		//HashMap<String, Object> bindMap = new HashMap<String, Object>();
//        //bindMap.put("MACHINENAME", machineName);
//        //bindMap.put("RELATIONTYPE", relationType);
//        
//        List resultList = nanoFrameServiceProxy.getSqlMesTemplate().queryForList(pdaSql);
//
//        if (resultList.size() > 0 )
//        {
//        	for(int i = 0; i<resultList.size(); i++ )
//        	{
//        		LinkedCaseInsensitiveMap result = (LinkedCaseInsensitiveMap) resultList.get(i);
//        		
//        		phoneNumber += result.get("PHONENUMBER");
//        		
//        		if (i < resultList.size() -1 )
//        		{
//        			phoneNumber += ",";
//        		}
//        	}
//        }
//        
//        UserProfileKey userKey = new UserProfileKey();
//        userKey.setUserId(alarmData.getLastEventUser());
//        
//        try
//        {
//        	UserProfile user = UserServiceProxy.getUserProfileService().selectByKey(userKey);
//        	userName = user.getUserName();
//        }
//        catch ( Exception e)
//        {
//        	userName = alarmData.getLastEventUser();
//        }
//        
//        // PDA - 80Byte Size 제한
//		String message = subject + "\r\n"
//			+ "설비명 : " + alarmData.getMachineName() + "\r\n"
//			+ "발생자: " + userName + "\r\n"
//			+ "관계자 긴급 조치바람!!";
//		
//		HashMap<String, String> dataMap = new HashMap<String, String>();
//		
//		// 메시지 보낼때 보내는 사람의 ID가 필요함
//		// 시스템에서 보낼때 00000000000 - PDA에서 스팸 구분하기 위해 
//		
//		// 알람 발생한 USER
//		String sendUserID = alarmData.getLastEventUser();
//		
//		dataMap.put("RECEIVEERNUMBER",phoneNumber);
//		dataMap.put("SENDERID", sendUserID);
//		dataMap.put("SENDERNUMBER", "00000000000");
//		dataMap.put("MESSAGE", message);
//		
//		log.info("SEND USER : " + sendUserID);
//    	log.info("Mobile Number : " + phoneNumber);
//		log.info("MESSAGE"+ message);
//		
//		sms.sendMessage(dataMap, ",");
//	}
//	
//	public void sendSMS(AlarmDefinition alarmDef, Alarm alarmData) throws RemoteException, ServiceException
//	{
//		log.info("=========================== SEND SMS ========================================");
//		
//		// SMS
//		TnxIFSMS sms = new TnxIFSMS();
//		
//		HashMap<String, String> udfMap = (HashMap<String, String>) alarmDef.getUdfs();
//		String userIds = udfMap.get("USERIDS");
//		String subject = alarmDef.getDescription();
//		String[] userList = userIds.split(",");
//		String toUser = "";
//		String phoneNumber= "";
//		
//		String reasonCodeDesc = getReasonCodeDescription(alarmData.getReasonCodeType(), alarmData.getReasonCode());
//		
//		String message = subject + "\r\n"
//			+ "설비명:" + alarmData.getMachineName() + "\r\n"
//			//+ "고장 코드 :" + alarmData.getReasonCode() + "\r\n"
//			+ "고장 코드:" + reasonCodeDesc + "\r\n"
//			+ "알람명:" +  alarmDef.getDescription() + "\r\n";
//			//+ "알람 확인후 조치 바랍니다.";
//		
//		if (alarmData.getKey().getAlarmId().equals("HOTLINEALARM"))
//		{
//			String userName = "";
//			
//			UserProfileKey userKey = new UserProfileKey();
//	        userKey.setUserId(alarmData.getLastEventUser());
//	        
//	        try
//	        {
//	        	UserProfile user = UserServiceProxy.getUserProfileService().selectByKey(userKey);
//	        	userName = user.getUserName();
//	        }
//	        catch ( Exception e)
//	        {
//	        	userName = alarmData.getLastEventUser();
//	        }
//	        
//			message = subject + "\r\n"
//			+ "설비명 : " + alarmData.getMachineName() + "\r\n"
//			+ "발생자: " + userName + "\r\n"
//			+ "관계자 긴급 조치바람!!";
//		}
//		else if (alarmData.getKey().getAlarmId().equals("EQPDOWN"))
//		{
//			message = subject + "\r\n"
//			+ "설비명:" + alarmData.getMachineName() + "\r\n"
//			+ "고장코드:" +"["+ alarmData.getReasonCode()+"]" + reasonCodeDesc + "\r\n" // 고장코드 + 고장내용
//			// 알람 내용 
//			// 요청내용
//			+ "요청내용:" + alarmData.getUdfs().get("RequestComment");
//		}
//		
//		UserProfile userProfile = new UserProfile();
//		
//		for(int i = 0; i < userList.length; i++)
//		{
//			String userId = userList[i];
//			
//			UserProfileKey userKey = new UserProfileKey();
//			userKey.setUserId(userId);
//			
//			userProfile = UserServiceProxy.getUserProfileService().selectByKey(userKey);
//			HashMap<String, String> userUdfs = (HashMap<String, String>) userProfile.getUdfs();
//			
//			toUser += userId;
//			phoneNumber += userUdfs.get("MOBILENUMBER");
//			
//			if (i < userList.length -1 )
//			{
//				toUser += ",";
//				phoneNumber += ",";
//			}
//		}
//		
//		HashMap<String, String> dataMap = new HashMap<String, String>();
//		
//		// 메시지 보낼때 보내는 사람의 ID가 필요함
//		// 시스템에서 보낼때 00000000000 - PDA에서 스팸 구분하기 위해 
//		
//		String sendUserID = "MES";
//		
//		dataMap.put("RECEIVEERNUMBER",phoneNumber);
//		dataMap.put("SENDERID", sendUserID);
//		dataMap.put("SENDERNUMBER", "00000000000");
//		dataMap.put("MESSAGE", message);
//		
//		log.info("SEND USER : " + sendUserID);
//    	log.info("TOUSER : " + toUser);
//    	log.info("Mobile Number : " + phoneNumber);
//		log.info("MESSAGE"+ message);
//		
//		sms.sendMessage(dataMap, ",");
//	}
//	
//	public void sendSMS(AlarmDefinition alarmDef, Alarm alarmData, String sendComment) throws RemoteException, 
//	ServiceException, NotFoundSignal
//	{
//		log.info("=========================== SEND SMS ========================================");
//		
//		// SMS
//		TnxIFSMS sms = new TnxIFSMS();
//		
//		HashMap<String, String> udfMap = (HashMap<String, String>) alarmDef.getUdfs();
//		String userIds = udfMap.get("USERIDS");
//		String[] userList = userIds.split(",");
//		String toUser = "";
//		String phoneNumber= "";
//		
//		String reasonCodeDesc = getReasonCodeDescription(alarmData.getReasonCodeType(), alarmData.getReasonCode());
//		
//		String message = "돌발 보전 요청" + "\r\n"
//			+ "설비명:" + alarmData.getMachineName() + "\r\n"
//		//	+ "고장코드:" + alarmData.getReasonCode() + "\r\n"
//			+ "고장코드:" + reasonCodeDesc + "\r\n"
//			+ "요청내용:" + sendComment;
//		
//		if (alarmData.getKey().getAlarmId().equals("EQPDOWN"))
//		{
//			message = "돌발 보전 요청" + "\r\n"
//			+ "설비명:" + alarmData.getMachineName() + "\r\n"
//			+ "고장코드:" +"["+ alarmData.getReasonCode()+"]" + reasonCodeDesc + "\r\n" // 고장코드 고장내용
//			// 알람 내용 
//			// 요청내용
//			+ "요청내용:" + alarmData.getUdfs().get("RequestComment");
//		}
//		
//		UserProfile userProfile = new UserProfile();
//		
//		for(int i = 0; i < userList.length; i++)
//		{
//			String userId = userList[i];
//			
//			UserProfileKey userKey = new UserProfileKey();
//			userKey.setUserId(userId);
//			
//			try
//			{
//				userProfile = UserServiceProxy.getUserProfileService().selectByKey(userKey);
//			}
//			catch(Exception e)
//			{
//				continue;
//			}
//			HashMap<String, String> userUdfs = (HashMap<String, String>) userProfile.getUdfs();
//			
//			toUser += userId;
//			phoneNumber += userUdfs.get("MOBILENUMBER");
//			
//			if (i < userList.length -1 )
//			{
//				toUser += ",";
//				phoneNumber += ",";
//			}
//		}
//		
//		HashMap<String, String> dataMap = new HashMap<String, String>();
//		
//		// 메시지 보낼때 보내는 사람의 ID가 필요함
//		// 시스템에서 보낼때 00000000000 - PDA에서 스팸 구분하기 위해 
//		
//		// SMS일경우는 확인 필요
//		
//		String sendUserID = "MES";
//		
//		dataMap.put("RECEIVEERNUMBER",phoneNumber);
//		dataMap.put("SENDERID", sendUserID);
//		dataMap.put("SENDERNUMBER", "00000000000");
//		dataMap.put("MESSAGE", message);
//		
//		log.info("SEND USER : " + sendUserID);
//    	log.info("TOUSER : " + toUser);
//    	log.info("Mobile Number : " + phoneNumber);
//		log.info("MESSAGE"+ message);
//		
//		if(!phoneNumber.isEmpty())
//		{
//			sms.sendMessage(dataMap, ",");
//		}
//	}
//	
//	public void sendSMS(AlarmDefinition alarmDef, Alarm alarmData, String subject, String sendComment, String sDirectSMSFlag) throws RemoteException, 
//	ServiceException, NotFoundSignal
//	{
//		log.info("=========================== SEND SMS ========================================");
//		
//		// SMS
//		TnxIFSMS sms = new TnxIFSMS();
//		
//		HashMap<String, String> udfMap = (HashMap<String, String>) alarmDef.getUdfs();
//		String userIds = udfMap.get("USERIDS");
//		String[] userList = userIds.split(",");
//		String phoneNumber= "";
//		
//		// PDA 전송이기 때문에 80 Byte 제약
//		String message = subject +"\r\n"
//			+ "설비명:" + alarmData.getMachineName() + " 고장코드:"  + alarmData.getReasonCode() +"\r\n"
//			
//		//	+ "고장코드:" + alarmData.getReasonCode() + "\r\n"
//			+ sendComment;
//		
//		UserProfile userProfile = new UserProfile();
//		
//		if (sDirectSMSFlag.equals("Y"))
//		{
//			// 알람코드 - 담당자필드에 전화번호 직접입력
//			phoneNumber = userIds;
//		}
//		else
//		{
//			for(int i = 0; i < userList.length; i++)
//			{
//				String userId = userList[i];
//				
//				UserProfileKey userKey = new UserProfileKey();
//				userKey.setUserId(userId);
//				
//				try
//				{
//					userProfile = UserServiceProxy.getUserProfileService().selectByKey(userKey);
//				}
//				catch(Exception e)
//				{
//					continue;
//				}
//				HashMap<String, String> userUdfs = (HashMap<String, String>) userProfile.getUdfs();
//				
//				//toUser += userId;
//				phoneNumber += userUdfs.get("MOBILENUMBER");
//				
//				if (i < userList.length -1 )
//				{
//					phoneNumber += ",";
//				}
//			}
//		}
//		
//		HashMap<String, String> dataMap = new HashMap<String, String>();
//		
//		// 메시지 보낼때 보내는 사람의 ID가 필요함
//		// 시스템에서 보낼때 00000000000 - PDA에서 스팸 구분하기 위해 
//		
//		// SMS일경우는 확인 필요
//		
//		String sendUserID = "MES";
//		
//		dataMap.put("RECEIVEERNUMBER",phoneNumber);
//		dataMap.put("SENDERID", sendUserID);
//		dataMap.put("SENDERNUMBER", "00000000000");
//		dataMap.put("MESSAGE", message);
//		
//		log.info("SEND USER : " + sendUserID);
//    	log.info("Mobile Number : " + phoneNumber);
//		log.info("MESSAGE : "+ message);
//		
//		sms.sendMessage(dataMap, ",");
//	}
//	
//	@SuppressWarnings("unchecked")
//	public String createAlarm(String alarmId, String reasonCode, String reasonCodeType,
//			String lotName, String machineName, String eventUser, String eventComment, String requestImage)
//	throws Exception
//	{
//		Timestamp curTime = TimeUtils.getCurrentTimestamp();
//		String curTimeKey = TimeUtils.getCurrentEventTimeKey();
//		
//		// Alarm Definition 찾기
//		AlarmDefinitionKey alarmDefKey = new AlarmDefinitionKey();
//		alarmDefKey.setAlarmId(alarmId);
//		
//		AlarmDefinition alarmDef;
//		try
//		{
//			alarmDef = 
//				AlarmServiceProxy.getAlarmDefinitionService().selectByKey(alarmDefKey);
//		}
//		catch(NotFoundSignal e)
//		{
//			return "";
//		}
//		
//		AlarmKey alarmKey = new AlarmKey();
//		alarmKey.setAlarmId(alarmId);
//		alarmKey.setAlarmTimeKey(curTimeKey);
//		
//		Alarm alarmData = new Alarm();
//		alarmData.setKey(alarmKey);
//		
//		
//		alarmData.setAlarmLevel(alarmDef.getAlarmLevel());
//		alarmData.setAlarmState(Constant.ALARM_STATE_CREATED);
//		alarmData.setAlarmType(alarmDef.getAlarmType());
//		
//		alarmData.setDescription(alarmDef.getDescription());
//		alarmData.setLotName(lotName);
//		alarmData.setMachineName(machineName);
//		alarmData.setReasonCode(reasonCode);
//		alarmData.setReasonCodeType(reasonCodeType);
//		
//		HashMap<String, String> udfs = new HashMap<String, String>();
//		
//		log.info(">> REQUESTIMAGE = [" + requestImage + "]");
//		
//		udfs.put("RequestUser", eventUser);
//		udfs.put("RequestTime", curTime.toString());
//		udfs.put("RequestComment", eventComment);
//		
//		if( !requestImage.isEmpty())
//		{
//			udfs.put("RequestImage", requestImage);
//		}
//		
//		alarmData.setUdfs(udfs);
//		
//		alarmData.setLastEventComment(eventComment);
//		alarmData.setLastEventTime(curTime);
//		alarmData.setLastEventTimeKey(curTimeKey);
//		alarmData.setLastEventUser(eventUser);
//		alarmData.setLastEventName("CreateAlarm");
//		
//		AlarmServiceProxy.getAlarmService().insert(alarmData);
//		
//		insertAlarmHistory(alarmData,curTime, eventUser, "CreateAlarm", eventComment, requestImage);
//		
//		log.info( "MESSAGE ACTION :"+ alarmDef.getMessageActionFlag());
//		log.info( "EMAIL ACTION :"+alarmDef.getEmailActionFlag());
//		log.info( "USERDEFINE ACTION :"+ alarmDef.getUserActionFlag());
//		
//		// Relation 설비로 등록된 장비는 모두 Alarm 발생
//		List relationMachineList = getRelationMachineList(machineName,"Alarm");
//		
//		if (relationMachineList.size() > 0 )
//		{
//			for (int i=0; i < relationMachineList.size(); i++)
//			{
//				LinkedCaseInsensitiveMap orderMap = (LinkedCaseInsensitiveMap) relationMachineList.get(i);
//				try
//				{
//					String relationMachine = (String) orderMap.getValue(0);
//				
//					String tmpTimeKey = TimeUtils.getCurrentEventTimeKey();
//					
//					alarmKey.setAlarmTimeKey(tmpTimeKey);
//					
//					alarmData.setKey(alarmKey);
//					alarmData.setMachineName(relationMachine);
//					
//					AlarmServiceProxy.getAlarmService().insert(alarmData);
//					
//					insertAlarmHistory(alarmData,curTime, eventUser, "CreateAlarm", 
//							"RelationMachine("+machineName+")", requestImage);
//					
//				}
//				catch(Exception e)
//				{
//					log.info("Relation Machine Change Error Continue");
//				}
//			}
//		}
//		
//		// Alarm Definition에 따른 Action
//		if ( alarmDef.getLotActionFlag().equals("1"))
//		{
//			// Lot Hold
//			
//		}
//		if (alarmDef.getMachineActionFlag().equals("1"))
//		{
//			// Machine Hold
//			
//		}
//		if (alarmDef.getMessageActionFlag().equals("1"))
//		{
//			// 금란지교
//			try
//			{
//				sendMessage(alarmDef, alarmData);
//			}
//			catch( RemoteException e)
//			{
//				// PASS
//			}
//			catch (ServiceException e)
//			{
//				// PASS
//			}
//			catch (Exception e)
//			{
//				
//			}
//		}
//		if (alarmDef.getEmailActionFlag().equals("1"))
//		{
//			log.info("SEND EMAIL");
//			
//			// EMAIL
//			try
//			{
//				sendEmail(alarmDef, alarmData);
//			}
//			catch( UnsupportedEncodingException e)
//			{
//				// PASS
//			}
//			catch (MessagingException e)
//			{
//				// PASS
//			}
//			catch (Exception e)
//			{
//				
//			}
//		}
//		if (alarmDef.getUserActionFlag().equals("1"))
//		{
//			// SMS
//			try
//			{
//				//sendSMS(alarmDef, alarmData);
//			}
//			catch (Exception e)
//			{
//				//PASS
//			}
//		}
//		
//		// 라인 긴급 상황 발생 시 PDA로 SMS 전송
//		if (alarmId.equals("HOTLINEALARM"))
//		{
//			 //sendPDASMS(alarmDef, alarmData);
//		}
//		
//		return curTimeKey;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List getRelationMachineList(String machineName, String relationType)
//	{
//		String query = "SELECT RELATIONMACHINENAME FROM EMMACHINERELATION"
//			+ " WHERE MACHINENAME = :MACHINENAME " 
//			+ " AND   RELATIONTYPE = :RELATIONTYPE "
//			+ " AND  RELATIONGROUPNAME = 'Machine' "
//			+ " UNION "
//			+ " SELECT "
//			+ "   M.SUPERMACHINENAME  "
//			+ " FROM EMMACHINERELATION MR, MACHINE M "
//			+ " WHERE MR.MACHINENAME = :MACHINENAME "
//			+ " AND   RELATIONTYPE = :RELATIONTYPE "
//			+ " AND  RELATIONGROUPNAME = 'MainMachine' "
//			+ " AND MR.MACHINENAME = M.MACHINENAME "
//			+ " UNION "
//			+ " SELECT "
//			+ "    M.MACHINENAME "
//			+ " FROM EMMACHINERELATION MR, MACHINE M "
//			+ " WHERE MR.MACHINENAME = :MACHINENAME "
//			+ " AND   RELATIONTYPE = :RELATIONTYPE "
//			+ " AND  RELATIONGROUPNAME = 'SubMachine' "
//			+ " AND MR.MACHINENAME = M.SUPERMACHINENAME ";
//		
//		HashMap<String, Object> bindMap = new HashMap<String, Object>();
//        bindMap.put("MACHINENAME", machineName);
//        bindMap.put("RELATIONTYPE", relationType);
//        
//        List resultList = nanoFrameServiceProxy.getSqlMesTemplate().queryForList(query, bindMap);
//
//        return resultList;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public String getReasonCodeDescription(String reasonCodeType, String reasonCode)
//	{
//		String desc ="";
//		
//		String queryID = "GetReasonCodeList_AllGroup";
//		String version = "00001";
//		
//		Object[] args = new Object[] { queryID, version };
//		
//		String customSql = "SELECT queryString FROM customQuery WHERE queryId = ? and version = ? ";
//
//		List queryList =
//			nanoFrameServiceProxy.getSqlMesTemplate().queryForList(customSql, args);
//
//		if (queryList.size() == 0)
//		{
//			return desc;
//		}
//
//		LinkedCaseInsensitiveMap queryMap = (LinkedCaseInsensitiveMap) queryList.get(0);
//		String dbSql = queryMap.get("queryString").toString();
//		
//		HashMap<String, Object> bindMap = new HashMap<String, Object>();
//		bindMap.put("REASONCODETYPE", reasonCodeType);
//		bindMap.put("CLASSNAME2", "");
//		bindMap.put("CLASSNAME3", "");
//		bindMap.put("CLASSNAME4", reasonCode);
//		
//		List resultList = nanoFrameServiceProxy.getSqlMesTemplate().queryForList(dbSql, bindMap);
//		
//		// PM Scheldule 처리
//		if (resultList.size() > 0 )
//		{
//			for (int ii = 0; ii < resultList.size(); ii++)
//			{
//				LinkedCaseInsensitiveMap codeInfo = (LinkedCaseInsensitiveMap) resultList.get(ii);
//				
//				String s1 = (String) codeInfo.get("CLASSNAME2");
//				String s2 = (String) codeInfo.get("CLASSNAME3");
//				String s3 = (String) codeInfo.get("CLASSNAME4");
//				
//				desc = "["+ s1 + "/" + s2 + "]"+s3;
//			}
//		}
//		
//        return desc;
//	}
//		
//	public void insertAlarmHistory(Alarm alarmData, Timestamp curTime, String eventUser, String eventName, String eventComment, String requestImage)
//	{
//		String timeKey = TimeStampUtil.getCurrentEventTimeKey();
//		
//		AlarmKey alarmKey = alarmData.getKey();
//		
//		AlarmHistoryKey historyKey = new AlarmHistoryKey();
//		
//		historyKey.setAlarmId(alarmKey.getAlarmId());
//		historyKey.setAlarmTimeKey(alarmKey.getAlarmTimeKey());
//		historyKey.setTimeKey(timeKey);
//		
//		AlarmHistory historyData = new AlarmHistory();
//		
//		historyData.setKey(historyKey);
//		historyData.setAlarmLevel(alarmData.getAlarmLevel());
//		historyData.setAlarmState(alarmData.getAlarmState());
//		historyData.setAlarmType(alarmData.getAlarmType());
//		
//		historyData.setDescription(alarmData.getDescription());
//		historyData.setEventTime(curTime);
//		historyData.setEventComment(eventComment);
//		historyData.setEventName(eventName);
//		historyData.setEventUser(eventUser);
//		historyData.setLotName(alarmData.getLotName());
//		historyData.setMachineName(alarmData.getMachineName());
//		historyData.setReasonCode(alarmData.getReasonCode());
//		historyData.setReasonCodeType(alarmData.getReasonCodeType());
//		
//		historyData.setRemoveDuration("180");
//		historyData.setUserMessage(alarmData.getUserMessage());
//		
//		HashMap<String, String> udfs = (HashMap<String, String>) alarmData.getUdfs();
//		
//		udfs.put("RequestImage", requestImage);
//		historyData.setUdfs(udfs);
//		
//		AlarmServiceProxy.getAlarmHistoryService().insert(historyData);
//		
	}

	@SuppressWarnings("unchecked")
	public String getRepairDept(String plantId)
	{
		//공무부 부서 조회
		String query 	= " SELECT AREAID AS REPAIRDEPTCODE FROM AREA WHERE PLANTID = :PLANTID AND AREATYPE = 'EqpDepartment' ";
		
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.put("PLANTID", plantId);
        
        List resultList = SqlMesTemplate.queryForList(query, bindMap);
        
        String repairDept = (String)((LinkedCaseInsensitiveMap)resultList.get(0)).get("REPAIRDEPTCODE");

        return repairDept;
	}
}
