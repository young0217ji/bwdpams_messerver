package mes.legacy.util;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;

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
public class MESAlarmDefinition
{
	/*************************************************************************************
    [VARIABLE]
	 *************************************************************************************/
	private Log  log = LogFactory.getLog(getClass());	//Log
	private String strSql;									//Query String
	
	/*************************************************************************************
    [CONSTRUCTOR]
	 *************************************************************************************/
	public MESAlarmDefinition()
	{
	}
	
	/*************************************************************************************
    [ALARM_EXECUTE_METHOD]
	 *************************************************************************************/
	/**
	 * @MethodName SendAlarmDefinitionMsg : v1.0
     * @param strPlantId -> 공장코드 strAlarmId->알람ID,strMsgSubject->메시지제목,strMsgContents->메시지내용,strRevDept->수신부서
     * @return String->메시지전송시간
     * @throws Exception
     * @description 알람ID 설정기준의 메세지를 유형별로 일괄 처리 한다.(기본)
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String SendMESAlarmDefinitionMsg(String sPlantID, String sAlarmID, String sTitle, String sContent, String sAlarmType, String sRelationTimeKey, String sCheckType)
	throws Exception
	{
//		Timestamp curTime = DateUtil.getCurrentTimestamp();
		String curTimeKey = DateUtil.getCurrentEventTimeKey();
		String sMsgUser = "";
		String sEmlUser = "";
		String sEmlUserComma = "";
		String sSmsUser = "";
		String sDefaultSendUserID = "";		
		String sAlarmTitle = "";
		
		if ("Alarm".equals(sAlarmType))
		{
			sAlarmTitle = "발생";
		}
		else if ("Release".equals(sAlarmType))
		{
			sAlarmTitle = "해제";
		}
		
		//Step)1. 부서별로 구분된 AlarmId 설정정보를 모두 조회 한다.
		List resultList = GetAlarmDefinitionList(sPlantID, sAlarmID);
		log.info("Alam수신자건수:"+String.valueOf(resultList.size()));
		
		for(int i=0;i<resultList.size();i++)
		{
			//Step)2. ListOrderMap Casting && Null Parsing
			LinkedCaseInsensitiveMap queryMap = (LinkedCaseInsensitiveMap) resultList.get(i);
			for ( Iterator iterator = queryMap.entrySet().iterator(); iterator.hasNext(); )
			{
				Map.Entry<String, Object> iterMap =  (Map.Entry<String, Object>) iterator.next();
				
				if ( iterMap.getValue() == null )
				{
					iterMap.setValue("");
				}
				
				queryMap.put(iterMap.getKey(), iterMap.getValue());
			}
			
			//Step)3-2. 이메일
			if(ConvertUtil.Object2String(queryMap.get("EMAILFLAG")).equals("Yes"))
			{
				if(sEmlUser.isEmpty())
				{
					sEmlUser =  ConvertUtil.Object2String(queryMap.get("EMAIL"));
					
					sEmlUserComma =  ConvertUtil.Object2String(queryMap.get("EMAIL"));
				}
				else
				{
					sEmlUser += ";"+ ConvertUtil.Object2String(queryMap.get("EMAIL"));
					
					sEmlUserComma += ","+ ConvertUtil.Object2String(queryMap.get("EMAIL"));
				}
			}
			//Step)3-3. SMS
			if(queryMap.get("SMSFLAG").toString().equals("Yes"))
			{	
				if(sSmsUser.isEmpty())
				{
					//strSmsUser =  queryMap.get("SMSNUMBER").toString();
					sSmsUser =  ConvertUtil.Object2String(queryMap.get("EMP"));
				}
				else
				{
					//strSmsUser += ","+ queryMap.get("SMSNUMBER").toString();
					sSmsUser += ","+ ConvertUtil.Object2String(queryMap.get("EMP"));
				}
			}
			//Step)3-3. SMS
			if(queryMap.get("MSGFLAG").toString().equals("Yes"))
			{	
				if(sMsgUser.isEmpty())
				{
					//strSmsUser =  queryMap.get("SMSNUMBER").toString();
					sMsgUser =  ConvertUtil.Object2String(queryMap.get("EMP"));
				}
				else
				{
					//strSmsUser += ","+ queryMap.get("SMSNUMBER").toString();
					sMsgUser += ","+ ConvertUtil.Object2String(queryMap.get("EMP"));
				}
			}
			
			if( sTitle.equals("") )
			{
				//sTitle = ConvertUtil.Object2String(queryMap.get("DEFAULTTITLE"));
				sTitle = MessageFormat.format(ConvertUtil.Object2String(queryMap.get("DEFAULTTITLE")), sAlarmTitle);
			}
			
			if( sContent.equals("") )
			{
				//sContent = ConvertUtil.Object2String(queryMap.get("DEFAULTCONTENT"));
				sContent = MessageFormat.format(ConvertUtil.Object2String(queryMap.get("DEFAULTCONTENT")), sAlarmTitle);
			}
			
			sDefaultSendUserID = ConvertUtil.Object2String(queryMap.get("DEFAULTSENDUSERID"));
		}
		
		//Step)4.Sevice Call
		//금란
		if(!sMsgUser.isEmpty())
		{
			try
			{
				//수신자사번리스트,송신자사번(실제존재하는유저사번지정),송신자이름("MES"일괄통일),메세지내용
				String strMsgTemp = sTitle +"vbCrLf"+ sContent;
				SendMsg(sMsgUser, sDefaultSendUserID,"MES",strMsgTemp.replace("\n", "vbCrLf"));	//김달영
				//SendMsg(strMsgUser, "51010279","MES",strMsgTemp.replace("\n", "vbCrLf"));	//MES 메일링 유저
			}
			catch( RemoteException e)
			{
				// PASS
			}
			catch (ServiceException e)
			{
				// PASS
			}
			catch (Exception e)
			{
				
			}
		}
		//이메일
		if(!sEmlUser.isEmpty())
		{
			try
			{
				//수신자이메일리스트,송신자정보("MES"일괄통일),메일타이틀,메일내용
				//SendEmail(strEmlUser, "MES",strMsgSubject,strMsgContents);
				SendEmail(sEmlUser, "MES", sTitle, sContent.replace("\r\n", "<BR>\n"));
			}
			catch( RemoteException e)
			{
				// PASS
			}
			catch (ServiceException e)
			{
				// PASS
			}
			catch (Exception e)
			{
				
			}
		}
		//SMS
		if(!sSmsUser.isEmpty())
		{
			try
			{
				//(v1.0)전화번호 기준.수신자전화번호리스트,송신자정보("MES"일괄통일),송신자전화번호("00000000000"일괄통일),메세지내용
				/*
				String strMsgTemp = strMsgSubject +"\r\n"+ strMsgContents;
				SendSms(strSmsUser, "MES","00000000000",strMsgTemp);
				*/
				//(v2.0)사용자ID기준.수신자사번리스트,송신자사번,송신자전화번호,메세지내용,시스템구분(MES or QIS),송신자부서코드
				//String strMsgTemp = sTitle +"\r\n"+ sContent;
				String strMsgTemp = sTitle;
				SendSms(sSmsUser, sDefaultSendUserID,"00000000000",strMsgTemp,"MES","");
			}
			catch( RemoteException e)
			{
				// PASS
			}
			catch (ServiceException e)
			{
				// PASS
			}
			catch (Exception e)
			{
				
			}
		}
		
		if(!sMsgUser.isEmpty() || !sEmlUser.isEmpty() || !sSmsUser.isEmpty())
		{
			// 메시지별 사용자 전송 메시지 이력 관리
			addAlarmSetHistory(sPlantID, sAlarmID, sTitle, sContent, sAlarmType, sMsgUser, sEmlUserComma, sSmsUser, sRelationTimeKey, sCheckType);
		}
		
		return curTimeKey;
	}
	
	/**
	 * @MethodName GetAlarmDefinitionList
     * @param sPlantId->공장코드 sAlarmId->알람ID
     * @return String
     * @throws Exception
     * @description AlarmID 설정정보 조회
     */
	@SuppressWarnings("rawtypes")
	protected List GetAlarmDefinitionList(String strPlantId, String strAlarmId) throws Exception
    {
		try
		{
			List liResult = null;		// Return Value
			HashMap<String, Object> map = new HashMap<String, Object>();
		
			this.strSql = 	
					" SELECT A.PLANTID  " +
					"      , A.ALARMID  " +
					"      , B.USERID AS EMP  " +
					"      , A.DEFAULTTITLE  " +
					"      , A.DEFAULTCONTENT  " +
					"      , A.DEFAULTSENDUSERID  " +
					"      , B.EMAILFLAG  " +
					"      , B.SMSFLAG  " +
					"      , B.MSGFLAG  " +
					"      , C.USERID  " +
					"      , C.DEPT  " +
					"      , C.EMP_NM  " +
					"      , C.EMAIL  " +
					"      , C.MOBILE  " +     
					"   FROM MESALARM A  " +
					"      , MESALARMUSER B  " +
					"      , (SELECT EMP  " +
					"              , ID_USER AS USERID  " +
					"              , DEPT  " +
					"              , EMP_NM  " +
					"              , ID_USER||'@kccworld.co.kr' AS EMAIL  " +
					"              , MOBILE  " +  
					"           FROM TC00_GW_USER  " +  
					"          WHERE 1=1  " +  
					"            AND DFLAG = 'Y'  " +  
					"            AND ID_USER IS NOT NULL  " +  
					"            AND MOBILE IS NOT NULL) C  " +
					"  WHERE A.PLANTID = B.PLANTID  " +
					"    AND A.ALARMID = B.ALARMID  " +
					"    AND B.USERID = C.EMP  " +                    
					"    AND A.PLANTID = :PLANTID  " +
					"    AND A.ALARMID = :ALARMID  " ;
			
			map.put("PLANTID",strPlantId);			//공장코드
			map.put("ALARMID",strAlarmId);			//알람Id
			liResult = SqlMesTemplate.queryForList(this.strSql, map);
		
			return liResult;
		}
		catch(Exception ex)
		{	
			throw ex;
		}
    }
	
	/*************************************************************************************
    [ALARM_TRANSFER_MODULE]
	 *************************************************************************************/
	/**
	 * @MethodName SendMsg
     * @param strRevUserList->메신져수신자리스트사번,strSendUser->송신자사번,strSendUserName->송신자성명,strMsg->메세지내용
     * @return void
     * @throws Exception
     * @description 금란지교Send메서드
     */
	public void SendMsg(String strRevUserList,String strSendUser,String strSendUserName, String strMsg) throws Exception
	{
		try
		{
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	/**
	 * @MethodName SendEmail
     * @param strRevUserList->이메일수신자리스트ID,strSendUser->송신자ID,strMsgSubject->이메일제목,strMsgContents->이메일내용
     * @return void
     * @throws Exception
     * @description 이메일Send메서드
     */
	public void SendEmail(String strRevUserList,String strSendUser,String strMsgSubject, String strMsgContents) throws Exception
	{
		try
		{
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	/**
	 * @MethodName SendSms : V1.0
     * @param strRevUserList->메신져수신자리스트전화번호,strSendUser->송신자명,strSendUserCallNumber->송신자전화번호,strMsg->메세지내용
     * @return void
     * @throws Exception
     * @description SMSSend메서드
     */
	public void SendSms(String strRevUserList,String strSendUser,String strSendUserCallNumber, String strMsg) throws Exception
	{
		try
		{
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	
	/**
	 * @MethodName SendSms : V2.0(유저EMPID를 이용한 SMS전송)
     * @param strRevUserList->메신져수신자사번리스트,strSendUser->송신자사번,strSendUserCallNumber->송신자전화번호,strMsg->메세지내용,strType->시스템구분(MES or QIS),strSenderDept->송신자부서코드
     * @return void
     * @throws Exception
     * @description SMSSend메서드
     */
	public void SendSms(String strRevUserList,String strSendUser,String strSendUserCallNumber, String strMsg,String strType,String strSenderDept) throws Exception
	{
		try
		{
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	/**
	 * AlarmSetHistory에 이력 추가
	 * 
	 * @param sPlantid
	 * @param sAlarmID
	 * @param sTitle
	 * @param sContents
	 * @param sAlarmType
	 * @param sMsgUser
	 * @param sEmlUserComma
	 * @param sSmsUser
	 * @param sRelationTimeKey
	 * @param sCheckType
	 * @return
	 * @throws Exception
	 * 
	 */
	public void addAlarmSetHistory(String sPlantid, String sAlarmID, String sTitle, String sContents, String sAlarmType, 
			String sMsgUser, String sEmlUserComma, String sSmsUser, String sRelationTimeKey, String sCheckType)
	{
		try
		{
			String sql = "INSERT INTO MESALARMSETHISTORY " +
			"( " +
			"PLANTID, ALARMID, CHECKTYPE, TIMEKEY, TITLE, CONTENT, SENDUSERID, CREATEUSERID, CREATETIME, ALARMTYPE, GETUSERIDSMS, GETUSERIDEMAIL, GETUSERIDMSG, RELATIONTIMEKEY " +
			") VALUES ( " +
			":PLANTID, :ALARMID, :CHECKTYPE, :TIMEKEY, :TITLE, :CONTENT, :SENDUSERID, :CREATEUSERID, :CREATETIME, :ALARMTYPE, :GETUSERIDSMS, :GETUSERIDEMAIL, :GETUSERIDMSG, :RELATIONTIMEKEY " +
			") ";
		
			HashMap<String, Object> bindMap = new HashMap<String, Object>();
			bindMap.put("PLANTID", sPlantid);
			bindMap.put("ALARMID", sAlarmID);
			bindMap.put("CHECKTYPE", sCheckType);
			bindMap.put("TIMEKEY", DateUtil.getCurrentEventTimeKey());
			
			bindMap.put("TITLE", sTitle);
			bindMap.put("CONTENT", sContents);		
			bindMap.put("SENDUSERID", "MES");
			bindMap.put("CREATEUSERID", "MES");
			bindMap.put("CREATETIME", DateUtil.getCurrentTimestamp());
			bindMap.put("ALARMTYPE", sAlarmType);
			bindMap.put("GETUSERIDSMS", sSmsUser);
			bindMap.put("GETUSERIDEMAIL", sEmlUserComma);
			bindMap.put("GETUSERIDMSG", sMsgUser);
			bindMap.put("RELATIONTIMEKEY", sRelationTimeKey);
			
			SqlMesTemplate.update(sql, bindMap);
			
			MESFrameProxy.getTxManager().commitForce();
		}
		catch(Exception e)
		{
			throw e;
		}
	}
}
