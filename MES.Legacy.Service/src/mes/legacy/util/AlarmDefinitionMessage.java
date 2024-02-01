package mes.legacy.util;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import kr.co.mesframe.orm.sql.SqlMesTemplate;
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
public class AlarmDefinitionMessage
{
	/*************************************************************************************
    [VARIABLE]
	 *************************************************************************************/
	private Log  log = LogFactory.getLog(getClass());	//Log
	private String strSql;									//Query String
	
	//Version 2.0 used VAR
	private String strPlantId;
	private String strAlarmId;
	private String strRevDept;;
	private List<Object> liMsgKeyParam;
	private String strEmWorkDivision;			//보전작업구분.(Common : 공통  Machine : 기계  Electric : 전기)
	private String strExtMsgContents;			//2014-09-26 추가. 표준 포멧이 아닌 공장별 또는 알람ID 별로 추가 전송되어야 할 내용. 
	
	/*************************************************************************************
    [CONSTRUCTOR]
	 *************************************************************************************/
	public AlarmDefinitionMessage()
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
	public String SendAlarmDefinitionMsg(String strPlantId, String strAlarmId, String strRevDept, String strMsgSubject, String strMsgContents)
	throws Exception
	{
		Timestamp curTime = DateUtil.getCurrentTimestamp();
		String curTimeKey = DateUtil.getCurrentEventTimeKey();
		String strMsgUser = "";
		String strEmlUser = "";
		String strSmsUser = "";
		//String strPdaUser = "";
		
		//Step)1. 부서별로 구분된 AlarmId 설정정보를 모두 조회 한다.
		List resultList = GetAlarmDefinitionList(strPlantId, strAlarmId,strRevDept);
		this.log.info("Alam수신자건수:"+String.valueOf(resultList.size()));
		
		for(int i=0;i<resultList.size();i++)
		{
			/*
			   A.ALARMID,				--알람id
               A.RECEIVEDEPARTMENT,			--수신부서
               A.USERSEQUENCE, 				--순번
               A.EMPLOYEE,     				--사번
               B.EMP_NM, 				--성명
               A.MESSENGERACTIONFLAG, 	--금란지교플레그
               A.EMAILACTIONFLAG,	  	--이메일플레그
               A.SMSACTIONFLAG,			--SMS플레그
               A.LOTACTIONFLAG, 		--로트HOLD플레그
               A.EQPACTIONFLAG, 	--설비HOLD플레그
               B.USERID,            	--그룹웨어(금란지교)USERID
               B.EMAIL,             	--EMAIL
               B.MOBILE,            	--핸드폰번호
               A.ETCCONTACTNUMBERS, 	--기타연락처
               DECODE(A.ETCCONTACTNUMBERS,NULL,B.MOBILE,B.MOBILE||','||A.ETCCONTACTNUMBERS) AS SMSNUMBER, --SMS수신처(기본핸드폰번호와기타연락처조합)
               A.REMARK
			 */
			
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
			
			//Step)3. 각 메시지 유형별 수신자 조합
			//Step)3-1. 금란지교
			if(queryMap.get("MESSENGERACTIONFLAG").toString().equals("1"))
			{
				if(strMsgUser.isEmpty())
				{
					strMsgUser =  queryMap.get("EMPLOYEE").toString();
				}
				else
				{
					strMsgUser += ","+ queryMap.get("EMPLOYEE").toString();
				}
			}
			//Step)3-2. 이메일
			if(queryMap.get("EMAILACTIONFLAG").toString().equals("1"))
			{
				if(strEmlUser.isEmpty())
				{
					strEmlUser =  queryMap.get("EMAIL").toString();
				}
				else
				{
					strEmlUser += ";"+ queryMap.get("EMAIL").toString();
				}
			}
			//Step)3-3. SMS
			if(queryMap.get("SMSACTIONFLAG").toString().equals("1"))
			{
				// (Ver 1.0) 최초 버전
				/*if(strSmsUser.isEmpty())
				{
					//strSmsUser =  queryMap.get("SMSNUMBER").toString();
					strSmsUser =  queryMap.get("EMPLOYEE").toString();
				}
				else
				{
					//strSmsUser += ","+ queryMap.get("SMSNUMBER").toString();
					strSmsUser += ","+ queryMap.get("EMPLOYEE").toString();
				}*/
				// (Ver 2.0) 2014.10.21 KJH 수정
				// 요청내용: 보전작업 SMS 발송 時 시간 지정을 할 수 있게 변경 (야간, 주말 등 퇴근 이후 시간)
				// 상세설명: [ALMD0200M]알람코드관리(부서기준)(ALARMMASTER)에 SMS 발송시간에대한 기준정보값을 입력하면, 해당 데이터를 기준으로 SMS 발송 대상자 Filter 처리.
				// 구현: Method 'GetAlarmDefinitionList'를 호출하면 수행되는 Query에 위 상세설명에 나와있는 조건에 따라 'SMSUSEYN'이란 명칭으로 'Y'/'N'을 가져오도록 수정.
				//      'Y':발송 / 'N':발송안함
				if(queryMap.get("SMSUSEYN").toString().equals("Y"))
				{
					if(strSmsUser.isEmpty())
					{
						//strSmsUser =  queryMap.get("SMSNUMBER").toString();
						strSmsUser =  queryMap.get("EMPLOYEE").toString();
					}
					else
					{
						//strSmsUser += ","+ queryMap.get("SMSNUMBER").toString();
						strSmsUser += ","+ queryMap.get("EMPLOYEE").toString();
					}
				}
			}
			//Step)3-4. 로트HOLD플레그
			if(queryMap.get("LOTACTIONFLAG").toString().equals("1"))
			{
				//ToDo...
			}
			//Step)3-5. 설비HOLD플레그
			if(queryMap.get("EQPACTIONFLAG").toString().equals("1"))
			{
				//ToDo...
			}
		}
		
		//Step)4.Sevice Call
		//금란
		if(!strMsgUser.isEmpty())
		{
			try
			{
				//수신자사번리스트,송신자사번(실제존재하는유저사번지정),송신자이름("MES"일괄통일),메세지내용
				String strMsgTemp = strMsgSubject +"vbCrLf"+ strMsgContents;
				SendMsg(strMsgUser, "10080982","MES",strMsgTemp.replace("\n", "vbCrLf"));	//김달영
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
		if(!strEmlUser.isEmpty())
		{
			try
			{
				//수신자이메일리스트,송신자정보("MES"일괄통일),메일타이틀,메일내용
				//SendEmail(strEmlUser, "MES",strMsgSubject,strMsgContents);
				SendEmail(strEmlUser, "MES",strMsgSubject,strMsgContents.replace("\r\n", "<BR>\n"));
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
		if(!strSmsUser.isEmpty())
		{
			try
			{
				//(v1.0)전화번호 기준.수신자전화번호리스트,송신자정보("MES"일괄통일),송신자전화번호("00000000000"일괄통일),메세지내용
				/*
				String strMsgTemp = strMsgSubject +"\r\n"+ strMsgContents;
				SendSms(strSmsUser, "MES","00000000000",strMsgTemp);
				*/
				//(v2.0)사용자ID기준.수신자사번리스트,송신자사번,송신자전화번호,메세지내용,시스템구분(MES or QIS),송신자부서코드
				String strMsgTemp = strMsgSubject +"\r\n"+ strMsgContents;
				SendSms(strSmsUser,"10080982","00000000000",strMsgTemp,"MES","");
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
		
		return curTimeKey;
	}
	
	/**
	 * @MethodName SendAlarmDefinitionMsg : v2.0
     * @param strPlantId -> 공장코드 strAlarmId->알람ID,strRevDept->수신부서,liMsgKeyPararm->메세지제목및내용 조합시 사용될 Data Select Key List
     * @return String->메시지전송시간
     * @throws Exception
     * @description 알람ID에 정의된 제목 및 내용 Format에 의해, liMsgKeyParam 리스트를 이용 주요 정보를 조회 후, 해당 Format에 맞게 메세지를 조합하고, 이를 각 모듈별로 전송한다.
     * [liMsgKeyParam 정의]
     * =>알람리스트 기준
        - ASSIGNWORKREQUEST  :  보전작업ID(WORKREQUESTID)
		- EQPDOWN					:  알람ID(ALARMID)
		- REQUESTBM				:  보전작업ID(WORKREQUESTID)
		- ACCEPTBM					:  보전작업ID(WORKREQUESTID)
		- STARTBM					:  보전작업ID(WORKREQUESTID)
		- ENDBM						:  보전작업ID(WORKREQUESTID)
		- CLOSEBM					:  보전작업ID(WORKREQUESTID)
		- REQUESTNR				:  일반작업ID(WORKREQUESTID)
		- ACCEPTNR					:  일반작업ID(WORKREQUESTID)
		- ENDNR						:  일반작업ID(WORKREQUESTID)
		- STARTPM					:  예방보전ID(WORKREQUESTID)
		- ENDPM						:  예방보전ID(WORKREQUESTID)
		- ESTABLISHPM				:  예방보전ID(WORKREQUESTID)
		- SYMPTOM					:  대상설비코드(EQUIPMENTID),이상유형(TROUBLETYPE),이상징후(CHECKPOINT),상세설명(TROUBLEDESCRIPTION)
     */
	public String SendAlarmDefinitionMsg(String strPlantId, String strAlarmId, String strRevDept, List<Object> liMsgKeyParam)
	throws Exception
	{
		//Common Param Init
		this.strPlantId =  strPlantId;
		this.strAlarmId = strAlarmId;
		this.strRevDept = strRevDept;
		this.liMsgKeyParam = liMsgKeyParam;
		this.strEmWorkDivision = "";				//작업구분. 공무부 수신대상 조회시 사용.메시지 내용 조합시 조회되는 보전작업정보 조회시 할당됨. 
		this.strExtMsgContents = "";				//알람추가내용.
		
		Timestamp curTime = DateUtil.getCurrentTimestamp();
		String curTimeKey = DateUtil.getCurrentEventTimeKey();
		String strMsgUser = "";
		String strEmlUser = "";
		String strSmsUser = "";
		//String strPdaUser = "";
		HashMap<String, String> hmTargetRowData; 
		
		/**********************************************[PART 0. 알람 메시지 포멧 조회]************************************************************/
		String strMsgSubjectFormat = "";				//메세지타이틀포멧
		String strMsgContentsFormat = "";			//메세지컨텐츠포멧
		String strPdaMsgSubjectFormat = "";			//PDA메세지타이틀포멧
		String strPdaMsgContentsFormat = "";		//PDA메세지컨텐츠포멧
		
		List resultListFormat = GetAlarmMsgFormatList(strPlantId, strAlarmId);			//메시지포멧 조회
		
		if(resultListFormat.size() > 0)					//해당 메세지포멧이 존재
		{
			hmTargetRowData 				= (HashMap<String, String>) resultListFormat.get(0);
			strMsgSubjectFormat 			= hmTargetRowData.get("MSGSUBJECTFORMAT");			//메세지타이틀포멧
			strMsgContentsFormat 			= hmTargetRowData.get("MSGCONTENTSFORMAT");			//메세지컨텐츠포멧
			strPdaMsgSubjectFormat 			= hmTargetRowData.get("PDAMSGSUBJECTFORMAT");		//PDA메세지타이틀포멧
			strPdaMsgContentsFormat 		= hmTargetRowData.get("PDAMSGCONTENTSFORMAT");		//PDA메세지컨텐츠포멧
		}
		else
		{
			this.log.info("메시지포멧 존재하지 않음.(AlarmID:"+ strAlarmId);
			return "";
		}
		
		this.log.info("strAlarmId:"+strAlarmId);
		this.log.info("strMsgSubjectFormat:"+strMsgSubjectFormat);
		this.log.info("strMsgContentsFormat:"+strMsgContentsFormat);
		this.log.info("strPdaMsgSubjectFormat:"+strPdaMsgSubjectFormat);
		this.log.info("strPdaMsgContentsFormat:"+strPdaMsgContentsFormat);
		
		/**********************************************[PART 1. 일반 메세지 처리]************************************************************/
		//Step)1. AlarmId에 설정된 메세지포멧을 이용 메세지 내용을 가공한다.
		String strMsgSubject 			= GetMsgSubject(strMsgSubjectFormat);			//메세지 제목
		String strMsgContents 			= GetMsgContents(strMsgContentsFormat);			//메세지 내용
		this.log.info("Msg Subject:"+strMsgSubject);
		this.log.info("Msg Contents:"+strMsgContents);
		
		//Step)2. 부서별로 구분된 AlarmId 설정정보를 모두 조회 한다.
		//List resultList = GetAlarmDefinitionList(strPlantId, strAlarmId,strRevDept);			//v1.0
		//==>메세지 내용 조회 후 해당 Alarm 유형이 보전작업과 상관없는 설비다운(EQPDOWN) 또는 징후관리등록(SYMPTOM)  알람 일 경우엔 작업구분(EmWorkDivision)이 null 이 되어,
		//==>알람수신 대상자를 조회 할수 없기에, 메세지 내용 조회 후  EmWorkDivision 가 null 일 경우엔 강제로 Common 값으로 할당하여 수신부서가 공무부일경우엔, 
		//==>공무부  전체다 알람을 발송할수 있도록 한다.
		if(this.strEmWorkDivision.isEmpty() ||  this.strEmWorkDivision == null)
		{
			this.strEmWorkDivision = "Common";
		}
		List resultList = GetAlarmDefinitionList(strPlantId, strAlarmId,strRevDept,this.strEmWorkDivision);				//v2.0
		this.log.info("Alam수신자건수: "+String.valueOf(resultList.size()));
		this.log.info("strPlantId: "+strPlantId);
		this.log.info("strAlarmId: "+strAlarmId);
		this.log.info("strRevDept: "+strRevDept);
		this.log.info("strEmWorkDivision: "+this.strEmWorkDivision);
		
		for(int i=0;i<resultList.size();i++)
		{
			/*
			   A.ALARMID,				--알람id
               A.RECEIVEDEPARTMENT,			--수신부서
               A.USERSEQUENCE, 				--순번
               A.EMPLOYEE,     				--사번
               B.EMP_NM, 				--성명
               A.MESSENGERACTIONFLAG, 	--금란지교플레그
               A.EMAILACTIONFLAG,	  	--이메일플레그
               A.SMSACTIONFLAG,			--SMS플레그
               A.LOTACTIONFLAG, 		--로트HOLD플레그
               A.EQPACTIONFLAG, 	--설비HOLD플레그
               B.USERID,            	--그룹웨어(금란지교)USERID
               B.EMAIL,             	--EMAIL
               B.MOBILE,            	--핸드폰번호
               A.ETCCONTACTNUMBERS, 	--기타연락처
               DECODE(A.ETCCONTACTNUMBERS,NULL,B.MOBILE,B.MOBILE||','||A.ETCCONTACTNUMBERS) AS SMSNUMBER, --SMS수신처(기본핸드폰번호와기타연락처조합)
               A.REMARK
			 */
			
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
			
			//Step)3. 각 메시지 유형별 수신자 조합
			//Step)3-1. 금란지교
			if(queryMap.get("MESSENGERACTIONFLAG").toString().equals("1"))
			{
				if(strMsgUser.isEmpty())
				{
					strMsgUser =  queryMap.get("EMPLOYEE").toString();
				}
				else
				{
					strMsgUser += ","+ queryMap.get("EMPLOYEE").toString();
				}
			}
			//Step)3-2. 이메일
			if(queryMap.get("EMAILACTIONFLAG").toString().equals("1"))
			{
				if(strEmlUser.isEmpty())
				{
					strEmlUser =  queryMap.get("EMAIL").toString();
				}
				else
				{
					strEmlUser += ";"+ queryMap.get("EMAIL").toString();
				}
			}
			//Step)3-3. SMS
			if(queryMap.get("SMSACTIONFLAG").toString().equals("1"))
			{

				// (Ver 1.0) 최초 버전
				/*if(strSmsUser.isEmpty())
				{
					//strSmsUser =  queryMap.get("SMSNUMBER").toString();
					strSmsUser =  queryMap.get("EMPLOYEE").toString();
				}
				else
				{
					//strSmsUser += ","+ queryMap.get("SMSNUMBER").toString();
					strSmsUser += ","+ queryMap.get("EMPLOYEE").toString();
				}*/
				// (Ver 2.0) 2014.10.21 KJH 수정
				// 요청내용: 보전작업 SMS 발송 時 시간 지정을 할 수 있게 변경 (야간, 주말 등 퇴근 이후 시간)
				// 상세설명: [ALMD0200M]알람코드관리(부서기준)(ALARMMASTER)에 SMS 발송시간에대한 기준정보값을 입력하면, 해당 데이터를 기준으로 SMS 발송 대상자 Filter 처리.
				// 구현: Method 'GetAlarmDefinitionList'를 호출하면 수행되는 Query에 위 상세설명에 나와있는 조건에 따라 'SMSUSEYN'이란 명칭으로 'Y'/'N'을 가져오도록 수정.
				//      'Y':발송 / 'N':발송안함
				if(queryMap.get("SMSUSEYN").toString().equals("Y"))
				{
					if(strSmsUser.isEmpty())
					{
						//strSmsUser =  queryMap.get("SMSNUMBER").toString();
						strSmsUser =  queryMap.get("EMPLOYEE").toString();
					}
					else
					{
						//strSmsUser += ","+ queryMap.get("SMSNUMBER").toString();
						strSmsUser += ","+ queryMap.get("EMPLOYEE").toString();
					}
				}
			}
			//Step)3-4. 로트HOLD플레그
			if(queryMap.get("LOTACTIONFLAG").toString().equals("1"))
			{
				//ToDo...
			}
			//Step)3-5. 설비HOLD플레그
			if(queryMap.get("EQPACTIONFLAG").toString().equals("1"))
			{
				//ToDo...
			}
		}
		
		//Step)4.Sevice Call
		//금란
		if(!strMsgUser.isEmpty())
		{
			try
			{
				//수신자사번리스트,송신자사번(실제존재하는유저사번지정),송신자이름("MES"일괄통일),메세지내용
				//String strMsgTemp = strMsgSubject +"vbCrLf"+ strMsgContents;
				//SendMsg(strMsgUser, "10080982","MES",strMsgTemp.replace("\n", "vbCrLf"));	//김달영
				////SendMsg(strMsgUser, "51010279","MES",strMsgTemp.replace("\n", "vbCrLf"));	//MES 메일링 유저
				//(2014-09-26 추가)보전작업요청 / 보전작업종료 시엔 추가전송내용을 포함하여 전송한다.
				String strMsgTemp = "";
				if(this.strAlarmId.equals("REQUESTBM"))
				{
					strMsgTemp = strMsgSubject +"vbCrLf"+ strMsgContents +"vbCrLf"+"[요청내용]"+"vbCrLf"+strExtMsgContents;
				}
				else if(this.strAlarmId.equals("ENDBM"))
				{
					strMsgTemp = strMsgSubject +"vbCrLf"+ strMsgContents +"vbCrLf"+"[조치내용]"+"vbCrLf"+strExtMsgContents;
				}
				else
				{
					strMsgTemp = strMsgSubject +"vbCrLf"+ strMsgContents;
				}
				SendMsg(strMsgUser, "10080982","MES",strMsgTemp.replace("\n", "vbCrLf"));	//김달영
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
		if(!strEmlUser.isEmpty())
		{
			try
			{
				//수신자이메일리스트,송신자정보("MES"일괄통일),메일타이틀,메일내용
				//SendEmail(strEmlUser, "MES",strMsgSubject,strMsgContents);
				//SendEmail(strEmlUser, "MES",strMsgSubject,strMsgContents.replace("\r\n", "<BR>\n"));
				//(2014-09-26 추가)보전작업요청 / 보전작업종료 시엔 추가전송내용을 포함하여 전송한다.
				if(this.strAlarmId.equals("REQUESTBM"))
				{
					SendEmail(strEmlUser, "MES",strMsgSubject,strMsgContents.replace("\r\n", "<BR>\n") +"<BR>\n"+"[요청내용]"+"<BR>\n"+ strExtMsgContents.replace("\r\n", "<BR>\n"));
				}
				else if(this.strAlarmId.equals("ENDBM"))
				{
					SendEmail(strEmlUser, "MES",strMsgSubject,strMsgContents.replace("\r\n", "<BR>\n") +"<BR>\n"+"[조치내용]"+"<BR>\n"+ strExtMsgContents.replace("\r\n", "<BR>\n"));
				}
				else
				{
					SendEmail(strEmlUser, "MES",strMsgSubject,strMsgContents.replace("\r\n", "<BR>\n"));
				}
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
		if(!strSmsUser.isEmpty())
		{
			try
			{
				//(v1.0)전화번호 기준.수신자전화번호리스트,송신자정보("MES"일괄통일),송신자전화번호("00000000000"일괄통일),메세지내용
				/*
				String strMsgTemp = strMsgSubject +"\r\n"+ strMsgContents;
				SendSms(strSmsUser, "MES","00000000000",strMsgTemp);
				*/
				//(v2.0)사용자ID기준.수신자사번리스트,송신자사번,송신자전화번호,메세지내용,시스템구분(MES or QIS),송신자부서코드
				//String strMsgTemp = strMsgSubject +"\r\n"+ strMsgContents;
				//SendSms(strSmsUser,"10080982","00000000000",strMsgTemp,"MES","");
				
				//(2014-09-26 추가)보전작업요청 / 보전작업종료 시엔 추가전송내용을 포함하여 전송한다.
				String strMsgTemp = "";
				if(this.strAlarmId.equals("REQUESTBM"))
				{
					strMsgTemp = strMsgSubject +"\r\n"+ strMsgContents +"\r\n"+"[요청내용]"+"\r\n"+strExtMsgContents;
				}
				else if(this.strAlarmId.equals("ENDBM"))
				{
					strMsgTemp = strMsgSubject +"\r\n"+ strMsgContents +"\r\n"+"[조치내용]"+"\r\n"+strExtMsgContents;
				}
				else
				{
					strMsgTemp = strMsgSubject +"\r\n"+ strMsgContents;
				}
				SendSms(strSmsUser,"10080982","00000000000",strMsgTemp,"MES","");
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
		
		/**********************************************[PART 2. PDA 메세지 처리]************************************************************/
		strSmsUser = "";
		//Boolean bIsPdaMsgUseYn = GetPdaMsgUseYn(strPlantId, strAlarmId,strRevDept);
		Boolean bIsPdaMsgUseYn = GetPdaMsgUseYn(strPlantId, strAlarmId,strRevDept,this.strEmWorkDivision);
		this.log.info("bIsPdaMsgUseYn:"+String.valueOf(bIsPdaMsgUseYn));
		/*(V1.0)CDMA 패턴*/
//		if(bIsPdaMsgUseYn)			//해당알람 메시지 PDA 전송 가능
//		{
//			//Step)1. AlarmId에 설정된 메세지포멧을 이용 메세지 내용을 가공한다.
//			String strPdaMsgSubject 		= GetPdaMsgSubject(strPdaMsgSubjectFormat);			//메세지 제목
//			String strPdaMsgContents 	= GetPdaMsgContents(strPdaMsgContentsFormat);		//메세지 내용
//			this.log.info("Msg PDA Subject:"+strPdaMsgSubject);
//			this.log.info("Msg PDA Contents:"+strPdaMsgContents);
//			
//			//Step)2. 부서별로 정의된 PDA 수신정보를 모두 조회 한다.
//			//List resultListPda = GetAlarmDefinitionListPda(strPlantId,strRevDept);	//v1.0
//			List resultListPda = GetAlarmDefinitionListPda(strPlantId,strRevDept,this.strEmWorkDivision);	//v2.0
//			this.log.info("AlamPDA수신자건수:"+String.valueOf(resultListPda.size()));
//			
//			for(int i=0;i<resultListPda.size();i++)
//			{
//				/*
//				 A.PLANTID,
//		         A.PDAID,
//		         A.DESCRIPTION,
//		         A.USEDDEPARTMENT,			--사용부서
//		         A.MODEL,
//		         A.SERIALNO,
//		         A.PDACALLNO,				--PDA전화번호
//		         A.MESSOFTWAREVERSION,
//		         A.LOGINUSER,
//		         A.LOGINTIME,
//		         A.USEYN,					--사용유무
//		         A.REMARK
//				 */
//				//Step)3. ListOrderMap Casting && Null Parsing
//				LinkedCaseInsensitiveMap queryMap = (LinkedCaseInsensitiveMap) resultListPda.get(i);
//				for ( Iterator iterator = queryMap.entrySet().iterator(); iterator.hasNext(); )
//				{
//					Map.Entry<String, Object> iterMap =  (Map.Entry<String, Object>) iterator.next();
//					
//					if ( iterMap.getValue() == null )
//					{
//						iterMap.setValue("");
//					}
//					
//					queryMap.put(iterMap.getKey(), iterMap.getValue());
//				}
//				//Step)4. 해당수신부서의 PDA 전화번호 조합(SMS)
//				if(queryMap.get("USEYN").toString().equals("Y"))
//				{
//					if(strSmsUser.isEmpty())
//					{
//						//strSmsUser =  queryMap.get("SMSNUMBER").toString();
//						strSmsUser =  queryMap.get("PDACALLNO").toString();					//PDA 전화번호
//					}
//					else
//					{
//						//strSmsUser += ","+ queryMap.get("SMSNUMBER").toString();
//						strSmsUser += ","+ queryMap.get("PDACALLNO").toString();
//					}
//				}
//			}
//			
//			//Step)5.Sevice Call
//			if(!strSmsUser.isEmpty())
//			{
//				try
//				{
//					//(v1.0)전화번호 기준.수신자전화번호리스트,송신자정보("MES"일괄통일),송신자전화번호("00000000000"일괄통일),메세지내용
//					String strMsgTemp = "";
//					if(!strPdaMsgSubject.isEmpty())
//					{
//						strMsgTemp = strPdaMsgSubject +"\r\n"+ strPdaMsgContents;
//					}
//					else
//					{
//						strMsgTemp = strPdaMsgContents;
//					}
//					SendSmsUseCallNumber(strPlantId, strSmsUser, "MES","00000000000",strMsgTemp);
//					
//					//(v2.0)사용자ID기준.수신자사번리스트,송신자사번,송신자전화번호,메세지내용,시스템구분(MES or QIS),송신자부서코드
//					/*
//					String strMsgTemp = strPdaMsgSubject +"\r\n"+ strPdaMsgContents;
//					SendSms(strSmsUser,"10080982","00000000000",strMsgTemp,"MES","");
//					*/
//				}
//				catch( RemoteException e)
//				{
//					// PASS
//				}
//				catch (ServiceException e)
//				{
//					// PASS
//				}
//				catch (Exception e)
//				{
//					
//				}
//			}
//			
//		}
        
		/*(V2.0)CDMA 패턴 + TCP/IP 패턴*/
		//cf)특정공장에선 PDA가 CDMA에 의한 고유전화번호가 없이 내부망을 이용한 MES데이터만 처리함.
		//   이에 알람전송 방식이 CDMA 방식이 아닐경우엔, PDADEFINITION 테이블의 PDASMSSENDGUBUN 필드인 알람전송방식(S1:CDMA , S2:TCP/IP)을 참조하여,
		//   S2 TCP/IP 방식일 경우엔 PDA SMS 전송관리 테이블 PDASMSSENDMANAGE 에 알람데이터를 입력해 두고, 이를 PDA서버에서 각각의 PDA에 TCP/IP 형태로 전송 처리하도록 함.
		//   (MES서버는 PDASMSSENDMANAGE 테이블에 알람데이터 생성만 담당하고, 이후 처리는 PDA 서버에서 처리하도록한다.)
		if(bIsPdaMsgUseYn)			//해당알람 메시지 PDA 전송 가능
		{
			//Step)1. AlarmId에 설정된 메세지포멧을 이용 메세지 내용을 가공한다.
			String strPdaMsgSubject = "";
			String strPdaMsgContents = "";
			//(건재공장):PDA 메세지포멧에 의한 알람내용 PARSING
			//strPdaMsgSubject 	= GetPdaMsgSubject(strPdaMsgSubjectFormat);			//메세지 제목
			//strPdaMsgContents = GetPdaMsgContents(strPdaMsgContentsFormat);		//메세지 내용
			//(대죽공장):PDA 메세지포멧을 이용하지 않고 일반 SMS 내용을 그대로 전송 처리 한다.
			if(this.strAlarmId.equals("REQUESTBM"))
			{
				strPdaMsgSubject  = strMsgSubject;
				strPdaMsgContents = strMsgContents +"\r\n"+"[요청내용]"+"\r\n"+strExtMsgContents;
			}
			else if(this.strAlarmId.equals("ENDBM"))
			{
				strPdaMsgSubject  = strMsgSubject;
				strPdaMsgContents = strMsgContents +"\r\n"+"[조치내용]"+"\r\n"+strExtMsgContents;
			}
			else
			{
				strPdaMsgSubject  = strMsgSubject;
				strPdaMsgContents = strMsgContents;
			}
			this.log.info("Msg PDA Subject:"+strPdaMsgSubject);
			this.log.info("Msg PDA Contents:"+strPdaMsgContents);
			
			//Step)2. CDMA 방식 처리 : 현재 전송구분이 CDMA 방식으로 지정된 항목 처리. S1.
			//Step)2-1.부서별로 정의된 PDA 수신정보를 모두 조회 한다.(S1)
			List resultListPda = null;
			//resultListPda = GetAlarmDefinitionListPda(strPlantId,strRevDept);	//v1.0
			resultListPda = GetAlarmDefinitionListPda(strPlantId,strRevDept,this.strEmWorkDivision,"S1");	//v2.0
			this.log.info("AlamPDA수신자건수(CDMA):"+String.valueOf(resultListPda.size()));
			for(int i=0;i<resultListPda.size();i++)
			{
				//Step)2-2. ListOrderMap Casting && Null Parsing
				LinkedCaseInsensitiveMap queryMap = (LinkedCaseInsensitiveMap) resultListPda.get(i);
				for ( Iterator iterator = queryMap.entrySet().iterator(); iterator.hasNext(); )
				{
					Map.Entry<String, Object> iterMap =  (Map.Entry<String, Object>) iterator.next();
					
					if ( iterMap.getValue() == null )
					{
						iterMap.setValue("");
					}
					
					queryMap.put(iterMap.getKey(), iterMap.getValue());
				}
				
				//Step)2-3. 해당수신부서의 PDA 전화번호 조합(SMS)
				if(queryMap.get("USEYN").toString().equals("Y"))
				{
					if(strSmsUser.isEmpty())
					{
						//strSmsUser =  queryMap.get("SMSNUMBER").toString();
						strSmsUser =  queryMap.get("PDACALLNO").toString();					//PDA 전화번호
					}
					else
					{
						//strSmsUser += ","+ queryMap.get("SMSNUMBER").toString();
						strSmsUser += ","+ queryMap.get("PDACALLNO").toString();
					}
				}
			}
			//Step)2-4.Sevice Call(CDMA)
			if(!strSmsUser.isEmpty())
			{
				try
				{
					//(v1.0)전화번호 기준.수신자전화번호리스트,송신자정보("MES"일괄통일),송신자전화번호("00000000000"일괄통일),메세지내용
					String strMsgTemp = "";
					if(!strPdaMsgSubject.isEmpty())
					{
						strMsgTemp = strPdaMsgSubject +"\r\n"+ strPdaMsgContents;
					}
					else
					{
						strMsgTemp = strPdaMsgContents;
					}
					SendSmsUseCallNumber(strPlantId, strSmsUser, "MES","00000000000",strMsgTemp);
					
					//(v2.0)사용자ID기준.수신자사번리스트,송신자사번,송신자전화번호,메세지내용,시스템구분(MES or QIS),송신자부서코드
					/*
					String strMsgTemp = strPdaMsgSubject +"\r\n"+ strPdaMsgContents;
					SendSms(strSmsUser,"10080982","00000000000",strMsgTemp,"MES","");
					*/
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
			
			//Step)3. TCP/IP 방식 처리 : 현재 전송구분이 TCP/IP 방식으로 지정된 항목 처리. S2.
			//Step)3-1.부서별로 정의된 PDA 수신정보를 모두 조회 한다.(S2)
			resultListPda = GetAlarmDefinitionListPda(strPlantId,strRevDept,this.strEmWorkDivision,"S2");	//v2.0
			//Step)3-2.조회된 PDA 수신정보가 존재한다면, PDA SMS 전송관리 테이블 PDASMSSENDMANAGE 에 insert 를 진행한다.
			if(resultListPda.size() != 0)
			{
				this.log.info("PDA 메세지 TCP/IP 방식 처리 Start!!!"+strPdaMsgSubject);
				//ALARMID 별 PARAM
				String strPaWorkRequestId = "";		//보전작업요청ID
				String strPaAlarmTimeKey  = "";		//알람타임키
				String strPaMaxPdaMsgSequence = "";	//현재까지 등록된 PDA 메시지 시퀀스 MAX값
				if(!this.strAlarmId.equals("EQPDOWN")&&!this.strAlarmId.equals("EQPCHECK")&&!this.strAlarmId.equals("SYMPTOM"))
				{
					strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				}
				if(this.strAlarmId.equals("EQPDOWN")&&this.strAlarmId.equals("EQPCHECK"))
				{
					strPaAlarmTimeKey = (String)this.liMsgKeyParam.get(1);
				}
				strPaMaxPdaMsgSequence = GetMaxPdaMsgSequence(strPlantId);
				
				this.strSql = 
						" INSERT INTO PDASMSSENDMANAGE   " +
						" ( PLANTID,MSGSEQUENCE,SERIALNO,ALARMID,WORKREQUESTID,REFKEY,MSGSUBJECT,MSGCONTENTS,REMARK,DELFLAG,SENDFLAG,CREATETIME,CREATEUSERID,LASTUPDATETIME,LASTUPDATEUSERID )  " +
						" SELECT A.PLANTID " +
						//" ,EM03_GET_NEW_PDAMSGSEQUENCE(A.PLANTID,ROWNUM) AS MSGSEQUENCE" +		//수정되고 있는 테이블을 다시 조회할경우 MUTATING TABEL ACCESS ERROR 발생 하여 동시처리를 진행할수없어, 현재까지 등록된 MSGSEQUENCE MAX값을 가져와 신규 SEQ를 할당한다.
						" ,:MAXMSGSEQUENCE + ROWNUM AS MSGSEQUENCE " +
						" ,A.SERIALNO,:ALARMID,:WORKREQUESTID,:REFKEY  " +
						" ,:MSGSUBJECT,:MSGCONTENTS,'' AS REMARK,'No' AS DELFLAG  " +
						" ,'No' AS SENDFLAG,SYSDATE AS CREATETIME,'MES' AS CREATEUSERID,SYSDATE AS LASTUPDATETIME,'MES' AS LASTUPDATEUSERID  " +
						" FROM PDADEFINITION A  " +
						" WHERE 1=1  " +
						" AND A.PLANTID = :PLANTID  " +
						" AND NVL(A.USEYN,'N') = 'Y' " +
						//" AND A.USEDDEPARTMENT = :USEDDEPARTMENT";
						" AND NVL(A.PDASMSSENDGUBUN,'S1') = 'S2' ";			//전송구분.TCP/IP
				AlarmUtil alarmUtil = new AlarmUtil();
				if(strRevDept.equals(alarmUtil.getRepairDept(strPlantId)))  //수신부서가 공무부 일경우 작업구분을 따져 공무부 기계/전기 또는 전체로 선별하여 진행한다.
				//if(strRevDept.equals(mes.constant.Constant.EM_DEPT_OFFICE))  //수신부서가 공무부 일경우 작업구분을 따져 공무부 기계/전기 또는 전체로 선별하여 진행한다.
				{
					//작업구분이 전체 Common 일경우엔  기계 / 전기 과에 셋팅된 모든 유저대상으로 알람을 발송
					if(strEmWorkDivision.equals("Common"))
					{
						this.strSql +=" AND NVL(A.USEDDEPARTMENT,' ') IN (EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , 'Machine') , EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , 'Electric'))  ";
					}
					//아닐경우엔 EMWORKDIVISION에 지정된 해당과의 알람대상자에게만 알람을 발송
					else
					{
						this.strSql +=" AND NVL(A.USEDDEPARTMENT,' ') LIKE '%'|| EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , :EMWORKDIVISION)||'%'  ";
					}
				}
				else
				{
					this.strSql +=" AND NVL(A.USEDDEPARTMENT,' ') LIKE '%'|| :USEDDEPARTMENT ||'%'  ";		//일반수신부서
				}
				HashMap<String, Object>map = new HashMap<String, Object>();
				map.put("ALARMID",this.strAlarmId);
				map.put("WORKREQUESTID",strPaWorkRequestId);
				map.put("REFKEY",strPaAlarmTimeKey);
				map.put("MSGSUBJECT",strPdaMsgSubject);
				map.put("MSGCONTENTS",strPdaMsgContents);
				map.put("PLANTID",strPlantId);
				map.put("USEDDEPARTMENT",strRevDept);
				map.put("EMWORKDIVISION",this.strEmWorkDivision);
				map.put("MAXMSGSEQUENCE",strPaMaxPdaMsgSequence);
				
				this.log.info("ALARMID:"+this.strAlarmId);
				this.log.info("WORKREQUESTID:"+strPaWorkRequestId);
				this.log.info("REFKEY:"+strPaAlarmTimeKey);
				this.log.info("MSGSUBJECT:"+strPdaMsgSubject);
				this.log.info("MSGCONTENTS:"+strPdaMsgContents);
				this.log.info("PLANTID:"+strPlantId);
				this.log.info("USEDDEPARTMENT:"+strRevDept);
				this.log.info("EMWORKDIVISION:"+this.strEmWorkDivision);
				this.log.info("MAXMSGSEQUENCE:"+strPaMaxPdaMsgSequence);

				//nanoFrameServiceProxy.getSqlTemplate().update(this.strSql, map);
				SqlMesTemplate.update(this.strSql, map);
				this.log.info("PDA 메세지 TCP/IP 방식 처리 End!!!"+strPdaMsgSubject);
			}

		}
		
		//(원본)
		//return curTimeKey;
		//(수정)20150111 알람ID 가 UTDOWN / UVRDOWN 일 경우, 타임키|메세지내용|수신자리스트 형태로 반환한다.
		if(this.strAlarmId.equals("UTDOWN")||this.strAlarmId.equals("UVRDOWN"))
		{
			this.log.info("Alarm Return Value:"+curTimeKey+";"+strMsgContents+";"+strMsgUser);
			return curTimeKey+";"+strMsgContents+";"+strMsgUser;
		}
		else
		{
			this.log.info("Alarm Return Value:"+curTimeKey);
			return curTimeKey;
		}
	}
	
	/*************************************************************************************
    [ALARM_MSG_COMBINATION_METHOD]
	 *************************************************************************************/
	/**
	 * @MethodName GetMsgSubject
     * @param strMsgSubjectFormat->메세지 제목 포멧 문자열
     * @return String
     * @throws Exception
     * @description Alarm 메세지 제목 조합
     */
	protected String GetMsgSubject(String strMsgSubjectFormat) throws Exception
	{
		String strResult = "";	//Return Value
		
		/*Get Rplace List*/
		String strPaWorkRequestId 	= "";	//보전작업ID
		String strPaAlarmId			= "";	//알람ID
		String strPaEquipmentId		= "";	//대상설비코드
		String strPaEquipmentName	= "";	//대상설비명
		
		//(추가)20150107. 유틸리티다운 / UVR다운 관련 파라미터 추가.
		String strPaUtlDownLevel    = "";	//유틸리티다운레벨(HIGHHIGH, HIGH ,LOW,LOWLOW)
		
		/*Alarm Msg Subject Combination*/
		switch (this.strAlarmId) 
		{
			//1.보전작업 지시
			case "ASSIGNWORKREQUEST":
				/*
				 MES 시스템 보전작업 지시 알람 정보 입니다.(보전작업ID:%s)
				 MES 시스템 보전작업 지시 알람 정보 입니다.(보전작업ID:WRBM201308210004)
				 */
				 strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				 strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//2.돌발보전 발생
			case "EQPDOWN":
				/*
				 MES 시스템 설비다운 알람 정보 입니다.(알람ID:%s)
				 MES 시스템 설비다운 알람 정보 입니다.(알람ID:EQPDOWN)
				 */
				 strPaAlarmId = (String)this.liMsgKeyParam.get(0);
				 strResult = String.format(strMsgSubjectFormat, strPaAlarmId);
			break;
			
			//3.보전작업 요청
			case "REQUESTBM":
				  /*
				  MES 시스템 보전작업 요청 알람 정보 입니다.(보전작업ID:%s)
				  MES 시스템 보전작업 요청 알람 정보 입니다.(보전작업ID:WRBM201308210004)
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//4.보전작업 승인
			case "ACCEPTBM":
				 /*
				  MES 시스템 보전작업 승인 알람 정보 입니다.(보전작업ID:%s)
				  MES 시스템 보전작업 승인 알람 정보 입니다.(보전작업ID:WRBM201308210004)"
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//5.보전작업 시작
			case "STARTBM":
				 /*
				  MES 시스템 보전작업 시작 알람 정보 입니다.(보전작업ID:%s)
				  MES 시스템 보전작업 시작 알람 정보 입니다.(보전작업ID:WRBM201308210004)"
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//6.보전작업 종료
			case "ENDBM":
				/*
				  MES 시스템 보전작업 종료 알람 정보 입니다.(보전작업ID:%s)
				  MES 시스템 보전작업 종료 알람 정보 입니다.(보전작업ID:WRBM201308210004)"
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//7.보전작업 마감
			case "CLOSEBM":
				/*
				  MES 시스템 보전작업 마감 알람 정보 입니다.(보전작업ID:%s)
				 MES 시스템 보전작업 마감 알람 정보 입니다.(보전작업ID:WRBM201308210004)"
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//8.일반작업 요청
			case "REQUESTNR":
				/*
				  MES 시스템 일반작업 요청 알람 정보 입니다.(일반작업ID:%s)
				  MES 시스템 일반작업 요청 알람 정보 입니다.(일반작업ID:WRNR201206260020)"
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//9.일반작업 승인
			case "ACCEPTNR":
				/*
				  MES 시스템 일반작업 승인 알람 정보 입니다.(일반작업ID:%s)
				MES 시스템 일반작업 승인 알람 정보 입니다.(일반작업ID:WRNR201206260020)"
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//10.일반작업 완료
			case "ENDNR":
				/*
				  MES 시스템 일반작업 완료 알람 정보 입니다.(일반작업ID:%s)
				  MES 시스템 일반작업 완료 알람 정보 입니다.(일반작업ID:WRNR201206260020)"
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//11.예방보전 시작
			case "STARTPM":
				/*
				   MES 시스템 예방보전 시작 알람 정보 입니다.(예방보전ID:%s)
					MES 시스템 예방보전 시작 알람 정보 입니다.(예방보전ID:WKPM201206150002)"
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;

			//12.예방보전 종료
			case "ENDPM":
				/*
				   MES 시스템 예방보전 종료 알람 정보 입니다.(예방보전ID:%s)
					MES 시스템 예방보전 종료 알람 정보 입니다.(예방보전ID:WKPM201206150002)
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//13.예방보전 수립
			case "ESTABLISHPM":
				/*
			   MES 시스템 예방보전 수립 알람 정보 입니다.(예방보전ID:%s)
				MES 시스템 예방보전 수립 알람 정보 입니다.(예방보전ID:WKPM201206150002)"
				   */
				  strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				  strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
			break;
			
			//14.징후관리 등록
			case "SYMPTOM":
				/*
				  MES 시스템 징후관리 등록 알람 정보 입니다.(대상설비:%s)
					MES 시스템 징후관리 등록 알람 정보 입니다.(대상설비:MM-04)"
				   */
				  strPaEquipmentId = (String)this.liMsgKeyParam.get(0);
				  strPaEquipmentName = GetEquipmentName(this.strPlantId,strPaEquipmentId);
				  strResult = String.format(strMsgSubjectFormat, strPaEquipmentId);
			break;
			
			//15.돌발보전 발생(체크)
			case "EQPCHECK":
				/*
				 MES 시스템 설비체크 알람 정보 입니다.(알람ID:%s)
				 MES 시스템 설비체크 알람 정보 입니다.(알람ID:EQPCHECK)
				 */
				 strPaAlarmId = (String)this.liMsgKeyParam.get(0);
				 strResult = String.format(strMsgSubjectFormat, strPaAlarmId);
			break;
			
			//16.유틸리티다운
			case "UTDOWN":
				/*
				 MES 유틸리티 다운 알람 정보 입니다.(%s 발생)
				 MES 유틸리티 다운 알람 정보 입니다.(HIGHHIGH 발생)
				 */
				 strPaUtlDownLevel = (String)this.liMsgKeyParam.get(0);
				 strResult = String.format(strMsgSubjectFormat, strPaUtlDownLevel);
			break;
			
			//17.UVR다운
			case "UVRDOWN":
				/*
				 MES UVR 다운 알람 정보 입니다.
				 MES UVR 다운 알람 정보 입니다.
				 */
				 //strPaUtlDownLevel = (String)this.liMsgKeyParam.get(0);
				 //strResult = String.format(strMsgSubjectFormat, strPaUtlDownLevel);
				 strResult = strMsgSubjectFormat;
			break;
			
			default:
			break;
		}
		
		return strResult;
	}

	/**
	 * @MethodName GetPdaMsgSubject
     * @param strPdaMsgSubjectFormat->PDA용 메세지 제목 포멧 문자열
     * @return String
     * @throws Exception
     * @description Alarm PDA용 메세지 제목 조합(2013-09-25 현재 PDA용 메세지 포멧 내용은 정의된 형태가 없고, 80 byte 용량 제약 때문에 Contents 영역만 이용한다.)
     */
	protected String GetPdaMsgSubject(String strPdaMsgSubjectFormat) throws Exception
	{
		String strResult = "";	//Return Value
		
		/*Get Rplace List*/
		String strPaWorkRequestId 	= "";	//보전작업ID
		String strPaAlarmId				= "";	//알람ID
		String strPaEquipmentId		= "";	//대상설비코드
		String strPaEquipmentName	= "";	//대상설비명
		
		/*Alarm Msg Subject Combination*/
		switch (this.strAlarmId) 
		{
			//1.보전작업 지시
			case "ASSIGNWORKREQUEST":
				/*(example)
				 MES 시스템 보전작업 지시 알람 정보 입니다.(보전작업ID:%s)
				 MES 시스템 보전작업 지시 알람 정보 입니다.(보전작업ID:WRBM201308210004)
				 */
				/*
				 strPaWorkRequestId = (String)this.liMsgKeyParam.get(0);
				 strResult = String.format(strMsgSubjectFormat, strPaWorkRequestId);
				 */
				 strResult = "";
			break;
			
			//2.돌발보전 발생
			case "EQPDOWN":
				strResult = "";
			break;
			
			//3.보전작업 요청
			case "REQUESTBM":
				strResult = "";
			break;
			
			//4.보전작업 승인
			case "ACCEPTBM":
				strResult = "";
			break;
			
			//5.보전작업 시작
			case "STARTBM":
				strResult = "";
			break;
			
			//6.보전작업 종료
			case "ENDBM":
				strResult = "";
			break;
			
			//7.보전작업 마감
			case "CLOSEBM":
				strResult = "";
			break;
			
			//8.일반작업 요청
			case "REQUESTNR":
				strResult = "";
			break;
			
			//9.일반작업 승인
			case "ACCEPTNR":
				strResult = "";
			break;
			
			//10.일반작업 완료
			case "ENDNR":
				strResult = "";
			break;
			
			//11.예방보전 시작
			case "STARTPM":
				strResult = "";
			break;

			//12.예방보전 종료
			case "ENDPM":
				strResult = "";
			break;
			
			//13.예방보전 수립
			case "ESTABLISHPM":
				strResult = "";
			break;
			
			//14.징후관리 등록
			case "SYMPTOM":
				strResult = "";
			break;
			
			//15.돌발보전 발생(체크)
			case "EQPCHECK":
				strResult = "";
			break;
			
			//16.유틸리티다운
			case "UTDOWN":
				strResult = "";
			break;
			
			//17.UVR다운
			case "UVRDOWN":
				strResult = "";
			break;
			
			default:
			break;
		}
		
		return strResult;
	}
	
	/**
	 * @MethodName GetMsgContents
     * @param strMsgContentsFormat->메세지 내용 포멧 문자열
     * @return String
     * @throws Exception
     * @description Alarm 메세지 내용 조합
     */
	protected String GetMsgContents(String strMsgContentsFormat) throws Exception
	{
		String strResult = "";	//Return Value
		
		/*Get Param List*/
		String strPaWorkRequestId 		= "";	//보전작업ID
		String strPaWorkRequestName 	= "";	//보전작업명 
		
		String strPaRequestUserName		= "";	//보전작업요청 또는 알람접수자 명
		String strPaConfirmUserName		= "";	//보전작업지시 또는 처리자 명
		
		String strPaEquipmentId			= "";	//대상설비코드
		String strPaEquipmentName		= "";	//대상설비명
		
		String strPaReasonCode			= "";	//고장코드
		String strPaReasonName			= "";	//고장명
		
		String strPaCauseCode			= "";	//원인코드
		String strPaCauseName			= "";	//원인명
		
		String strPaRequestDeptCode		= "";	//요청부서코드
		String strPaRequestDeptName		= "";	//요청부서명
		
		String strPaConfirmDeptCode		= "";	//처리부서코드
		String strPaComfirmDeptName		= "";	//처리부서명
		
		String strPaTroubleTypeCode		= "";	//이상유형코드
		String strPaTroubleTypeName		= "";	//이상유형명
		String strPaCheckPoint			= "";	//이상징후내용
		
		String strPaAlarmId				= "";	//알람ID
		String strPaAlarmTimeKey		= "";	//알람타임키
		
		String strRequestComment        = "";	//2014-09-26추가.요청내용.
		String strActionComment			= "";	//2014-09-26추가.조치내용.
		
		//(추가)20150107.유틸리티다운 / UVR다운관련 파라미터 추가
		String strPaUtlDownLevel		= "";	//유틸리티다운 레벨
		String strPaEisTagValue			= "";	//EIS에서 게더링된 현재값
		String strPaUvrClass			= "";   //UVR다운 구분
		String strPaUvrTagNo			= "";	//UVR다운 TAGNO

		List liQueryResult;
		LinkedCaseInsensitiveMap QueryMap;
		
		/*Alarm Msg Contents Combination*/
		switch (this.strAlarmId) 
		{
			//1.보전작업 지시
			case "ASSIGNWORKREQUEST":
				/*
				  보전작업 '%s(보전작업ID:%s 대상설비:%s)' 에 대해, 공무부 %s 님에 의해 '지시'처리 되었습니다.
				  보전작업 '[고장내용] Tank 하단부 누수/긴급조치바랍니다.(보전작업ID:WRBM201308210004 대상설비:AG-SU10-002)' 에 대해, 공무부 한영균 님에 의해 '지시'처리 되었습니다.
				 */
				 strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
				 }
				 //strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();								//대상설비명
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaEquipmentId , strPaConfirmUserName);
				 
			break;
			
			//2.돌발보전 발생
			case "EQPDOWN":
				/*
				 설비 '%s(%s)' 가 %s(%s) 고장으로 '설비다운'이 발생하였습니다.(접수자:%s)
				 설비 'AGING TANK#2(AG-SU10-002)' 가 인버터 불량(D670) 고장으로 '설비다운'이 발생하였습니다.(접수자:김정수)
				 */
				 strPaAlarmId 					= (String)this.liMsgKeyParam.get(0);													//알람ID
				 strPaAlarmTimeKey 			= (String)this.liMsgKeyParam.get(1);													//알람TimeKey
				 
				 liQueryResult 				= GetWorkAlarmtInfo(this.strPlantId,strPaAlarmId,strPaAlarmTimeKey);				//알람기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found Alarm Data.", this.strPlantId, strPaAlarmId, strPaAlarmTimeKey);
					//throw new MESFrameException("Can Not Found Alarm Data.",this.strPlantId, strPaAlarmId, strPaAlarmTimeKey);
					 this.log.info("Can Not Found Alarm Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("EQUIPMENTNAME") !=  null)
				 {
					 strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();									//대상설비명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
				 }
				 if(QueryMap.get("REASONCODE") !=  null)
				 {
					 strPaReasonCode				= QueryMap.get("REASONCODE").toString();										//고장코드
				 }
				 if(QueryMap.get("REASONCODENAME") !=  null)
				 {
					 strPaReasonName				= QueryMap.get("REASONCODENAME").toString();								//고장코드명
				 }
				 if(QueryMap.get("REQUESTUSERNAME") !=  null)
				 {
					 strPaRequestUserName	    = QueryMap.get("REQUESTUSERNAME").toString();								//접수자명
				 }
				 strResult = String.format(strMsgContentsFormat, strPaEquipmentName,strPaEquipmentId,strPaReasonCode,strPaReasonName,strPaRequestUserName);
				 
			break;
			
			//3.보전작업 요청
			case "REQUESTBM":
				  /*
				  설비 '%s(%s)' 가 %s(%s) (으)로 %s %s님에 의해 보전작업 '요청'이 접수 되었습니다.
				  설비 'AGING TANK#2(AG-SU10-002)' 가 부식(C150) (으)로 PVC 생산과 성훈님에 의해 보전작업 '요청'이 접수 되었습니다.
				   */
				 strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 					= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);						//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 //throw new MESFrameException("Can Not Found WorkRequest Data.",this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("EQUIPMENTNAME") !=  null)
				 {
					 strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();								    //대상설비명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
				 }
				 if(QueryMap.get("REASONCODE") !=  null)
				 {
					 strPaReasonCode				= QueryMap.get("REASONCODE").toString();										//고장코드
				 }
				 if(QueryMap.get("REASONCODENAME") !=  null)
				 {
					 strPaReasonName				= QueryMap.get("REASONCODENAME").toString();								//고장코드명
				 }
				 if(QueryMap.get("RESPONSIBLEDEPTNAME") !=  null)
				 {
					 //strPaRequestDeptCode		= QueryMap.get("RESPONSIBLEDEPT").toString();									//요청부서
					 strPaRequestDeptName		= QueryMap.get("RESPONSIBLEDEPTNAME").toString();							//요청부서명
				 }
				 if(QueryMap.get("REQUESTUSERNAME") !=  null)
				 {
					 strPaRequestUserName	    = QueryMap.get("REQUESTUSERNAME").toString();								//요청자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 
				 //(2014-09-26 추가)보전작업 요청시 요청내용 추가 표시.(메일서비스만 적용. 추가전송내용 사용.) 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 this.strExtMsgContents		= QueryMap.get("REQUESTCOMMENT").toString();								//요청내용
				 }
				 
				 strResult = String.format(strMsgContentsFormat, strPaEquipmentName , strPaEquipmentId , strPaReasonName , strPaReasonCode, strPaRequestDeptName,strPaRequestUserName);

			break;
			
			//4.보전작업 승인
			case "ACCEPTBM":
				 /*
				  보전작업 '%s(보전작업ID:%s 대상설비:%s)' 에 대해, 공무부 %s 님에 의해 '승인'처리 되었습니다.
				  보전작업 '[고장내용] Tank 하단부 누수/긴급조치바랍니다.(보전작업ID:WRBM201308210004 대상설비:AG-SU10-002)' 에 대해, 공무부 한영균 님에 의해 '승인'처리 되었습니다."
				   */
				 strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
					 //strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();								//대상설비명
				 }
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaEquipmentId , strPaConfirmUserName);
			break;
			
			//5.보전작업 시작
			case "STARTBM":
				 /*
				  보전작업 '%s(보전작업ID:%s 대상설비:%s)' 에 대해, 공무부 %s 님에 의해 '시작'처리 되었습니다.
				  보전작업 '[고장내용] Tank 하단부 누수/긴급조치바랍니다.(보전작업ID:WRBM201308210004 대상설비:AG-SU10-002)' 에 대해, 공무부 한영균 님에 의해 '시작'처리 되었습니다.
				   */
				strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					this.log.info("Can Not Found WorkRequest Data.");
					return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
					 //strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();								//대상설비명
				 }
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaEquipmentId , strPaConfirmUserName);
			break;
			
			//6.보전작업 종료
			case "ENDBM":
				 /*
				  보전작업 '%s(보전작업ID:%s 대상설비:%s)' 에 대해, 공무부 %s 님에 의해 '종료'처리 되었습니다.
				  보전작업 '[고장내용] Tank 하단부 누수/긴급조치바랍니다.(보전작업ID:WRBM201308210004 대상설비:AG-SU10-002)' 에 대해, 공무부 한영균 님에 의해 '종료'처리 되었습니다.
				   */
				strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
					 //strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();								//대상설비명
				 }
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 
				 //(2014-09-26 추가)보전작업 종료시 조치내용 추가 표시.(메일서비스만 적용. 추가전송내용 사용.) 
				 if(QueryMap.get("ACTIONCOMMENT") !=  null)
				 {
					 this.strExtMsgContents		= QueryMap.get("ACTIONCOMMENT").toString();								//요청내용
				 }
				 
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaEquipmentId , strPaConfirmUserName);
			break;
			
			//7.보전작업 마감
			case "CLOSEBM":
				 /*
				  보전작업 '%s(보전작업ID:%s 대상설비:%s)' 에 대해, 공무부 %s 님에 의해 '마감'처리 되었습니다.
				  보전작업 '[고장내용] Tank 하단부 누수/긴급조치바랍니다.(보전작업ID:WRBM201308210004 대상설비:AG-SU10-002)' 에 대해, 공무부 한영균 님에 의해 '마감'처리 되었습니다.
				   */
				strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
					 //strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();								//대상설비명
				 }
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaEquipmentId , strPaConfirmUserName);
			break;
			
			//8.일반작업 요청
			case "REQUESTNR":
				/*
				  요청내용 %s(%s) (으)로 %s %s님에 의해 일반작업 '요청'이 접수 되었습니다.
				  요청내용 에폭시 3층 T-5001 탱크 접지 집게 파손으로 수리요청(WRNR201206260020) (으)로 서무과 성훈님에 의해 일반작업 '요청'이 접수 되었습니다. 
				   */
				strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("RESPONSIBLEDEPTNAME") !=  null)
				 {
					 strPaRequestDeptName		= QueryMap.get("RESPONSIBLEDEPTNAME").toString();							//요청부서명
				 }
				 if(QueryMap.get("REQUESTUSERNAME") !=  null)
				 {
					 strPaRequestUserName	    = QueryMap.get("REQUESTUSERNAME").toString();								//요청자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaRequestDeptName , strPaRequestUserName);
			break;
			
			//9.일반작업 승인
			case "ACCEPTNR":
				/*
				 요청내용 %s(%s) 에 대해, 공무부 %s님에 의해 일반작업 '승인'처리 되었습니다.
				 요청내용 에폭시 3층 T-5001 탱크 접지 집게 파손으로 수리요청(WRNR201206260020) 에 대해 공무부 한영균님에 의해 일반작업 '승인'처리 되었습니다.
				   */
				strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaConfirmUserName);
			break;
			
			//10.일반작업 완료
			case "ENDNR":
				/*
				  요청내용 %s(%s) 에 대해, 공무부 %s님에 의해 일반작업 '완료'처리 되었습니다.
				  요청내용 에폭시 3층 T-5001 탱크 접지 집게 파손으로 수리요청(WRNR201206260020) 에 대해 공무부 한영균님에 의해 일반작업 '완료'처리 되었습니다.
				   */
				strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaConfirmUserName);
			break;
			
			//11.예방보전 시작
			case "STARTPM":
				/*
				    예방보전 '%s(예방보전ID:%s 대상설비:%s)' 에 대해, 공무부 %s 님에 의해 '시작'처리 되었습니다.
					예방보전 'Rotor & Rotor pin 교체(예방보전ID:WKPM201206150002 대상설비:MM-04)' 에 대해, 공무부 한영균 님에 의해 '시작'처리 되었습니다.
				   */
				 strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
				 	//strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();								//대상설비명
				 }
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaEquipmentId , strPaConfirmUserName);
			break;

			//12.예방보전 종료
			case "ENDPM":
				/*
			    예방보전 '%s(예방보전ID:%s 대상설비:%s)' 에 대해, 공무부 %s 님에 의해 '종료'처리 되었습니다.
				예방보전 'Rotor & Rotor pin 교체(예방보전ID:WKPM201206150002 대상설비:MM-04)' 에 대해, 공무부 한영균 님에 의해 '종료'처리 되었습니다.
			   */
				 strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
					 //strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();								//대상설비명
				 }
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaEquipmentId , strPaConfirmUserName);
			break;
			
			//13.예방보전 수립
			case "ESTABLISHPM":
					/*
			   	  예방보전 '%s(예방보전ID:%s 대상설비:%s)' 에 대해, %s 님에 의해 '수립'처리 되었습니다.
				  예방보전 'Rotor & Rotor pin 교체(예방보전ID:WKPM201206150002 대상설비:MM-04)' 에 대해, 한영균 님에 의해 '수립'처리 되었습니다.
				   */
				 strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);							//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found WorkRequest Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
				 	//strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();								//대상설비명
				 }
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 if(QueryMap.get("EMWORKDIVISION") !=  null)
				 {
					 this.strEmWorkDivision		= QueryMap.get("EMWORKDIVISION").toString();								//보전작업작업구분(Common :  공통 Machine : 기계 Electric :  전자)
				 }
				 strResult = String.format(strMsgContentsFormat, strPaWorkRequestName , strPaWorkRequestId , strPaEquipmentId , strPaConfirmUserName);
			break;
			
			//14.징후관리 등록
			case "SYMPTOM":
				/*
				  징후관리 대상설비 '%s'에 대해, 이상유형 '%s(%s)' 항목이 %s %s 님에 의해 신규 추가 되었습니다.
				  징후관리 대상설비 'MM-04'에 대해, 이상유형 '소음(점검방법)' 항목이 공무부 한영균 님에 의해 신규 추가 되었습니다.
				   */
				 strPaEquipmentId 				= (String)this.liMsgKeyParam.get(0);			//대상설비코드
				 strPaTroubleTypeCode			= (String)this.liMsgKeyParam.get(1);			//이상유형코드
				 strPaCheckPoint				= (String)this.liMsgKeyParam.get(2);			//이상징후내용
				 
				 liQueryResult 					= GetTroubleShootingInfo(this.strPlantId,strPaEquipmentId,strPaTroubleTypeCode,strPaCheckPoint);		//징후관리기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found TroubleShooting Data.", this.strPlantId, strPaWorkRequestId);
					 this.log.info("Can Not Found TroubleShooting Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("EQUIPMENTNAME") !=  null)
				 {
					 strPaEquipmentName			= QueryMap.get("EQUIPMENTNAME").toString();		//설비명
				 }
				 if(QueryMap.get("TROUBLETYPENAME") !=  null)
				 {
					 strPaTroubleTypeName		= QueryMap.get("TROUBLETYPENAME").toString();		//이상유형명
				 }
				 if(QueryMap.get("DEPT") !=  null)
				 {
					 strPaConfirmDeptCode		= QueryMap.get("DEPT").toString();						//처리부서코드
				 }
				 if(QueryMap.get("DEPTNAME") !=  null)
				 {
					 strPaComfirmDeptName		= QueryMap.get("DEPTNAME").toString();				//처리부서명
				 }
				 if(QueryMap.get("CREATEUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CREATEUSERNAME").toString();		//처리등록유저명
				 }
				 
				 strResult = String.format(strMsgContentsFormat, strPaEquipmentName , strPaTroubleTypeName , strPaCheckPoint , strPaComfirmDeptName, strPaConfirmUserName);
			break;
			
			//15.돌발보전 발생(체크)
			case "EQPCHECK":
				/*
				 설비 '%s(%s)' 가 %s(%s) 고장으로 알람이 발생하였습니다.(접수자:%s)
				 설비 'AGING TANK#2(AG-SU10-002)' 가 인버터 불량(D670) 고장으로 알람이 발생하였습니다.(접수자:김정수)
				 */
				 strPaAlarmId 					= (String)this.liMsgKeyParam.get(0);													//알람ID
				 strPaAlarmTimeKey 			= (String)this.liMsgKeyParam.get(1);													//알람TimeKey
				 
				 liQueryResult 				= GetWorkAlarmtInfo(this.strPlantId,strPaAlarmId,strPaAlarmTimeKey);				//알람기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found Alarm Data.", this.strPlantId, strPaAlarmId, strPaAlarmTimeKey);
					//throw new MESFrameException("Can Not Found Alarm Data.",this.strPlantId, strPaAlarmId, strPaAlarmTimeKey);
					 this.log.info("Can Not Found Alarm Data.");
					 return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("EQUIPMENTNAME") !=  null)
				 {
					 strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();									//대상설비명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
				 }
				 if(QueryMap.get("REASONCODE") !=  null)
				 {
					 strPaReasonCode				= QueryMap.get("REASONCODE").toString();										//고장코드
				 }
				 if(QueryMap.get("REASONCODENAME") !=  null)
				 {
					 strPaReasonName				= QueryMap.get("REASONCODENAME").toString();								//고장코드명
				 }
				 if(QueryMap.get("REQUESTUSERNAME") !=  null)
				 {
					 strPaRequestUserName	    = QueryMap.get("REQUESTUSERNAME").toString();								//접수자명
				 }
				 strResult = String.format(strMsgContentsFormat, strPaEquipmentName,strPaEquipmentId,strPaReasonCode,strPaReasonName,strPaRequestUserName);
				 
			break;
			
			//16.유틸리티다운
			case "UTDOWN":
				/*
				 %s & %s & %s ALARM 발생 [현재 값:%s]
				 LT-6104 & B-6101 LEVEL & HIGHHIGH ALARM 발생 [현재 값 : 100]
				 */
				 strPaEquipmentId 		= (String)this.liMsgKeyParam.get(0);		//대상설비코드
				 strPaEquipmentName		= (String)this.liMsgKeyParam.get(1);		//대상설비명
				 strPaUtlDownLevel		= (String)this.liMsgKeyParam.get(2);		//유틸리티다운레벨
				 strPaEisTagValue		= (String)this.liMsgKeyParam.get(3);		//유틸리티현재값
				 strResult = String.format(strMsgContentsFormat, strPaEquipmentId,strPaEquipmentName,strPaUtlDownLevel,strPaEisTagValue);
				 
			break;
			
			//17.UVR다운
			case "UVRDOWN":
				/*
				 %s & %s & %s ALARM 발생 하였습니다.
				 154kV & INCOMING TS19 & 27 (ph.-to-ph. undervoltage) relay ALARM 발생 하였습니다.
				 */
				 strPaUvrClass 			= (String)this.liMsgKeyParam.get(4); 		//UVR구분명
				 strPaUvrTagNo 			= (String)this.liMsgKeyParam.get(5); 		//TAGNO
				 strPaEquipmentName 	= (String)this.liMsgKeyParam.get(1); 		//설비명
				 strResult = String.format(strMsgContentsFormat, strPaUvrClass,strPaUvrTagNo,strPaEquipmentName);
				 
			break;
			
			default:
			break;
		}
		
		return strResult;
	}

	/**
	 * @MethodName GetPdaMsgContents
     * @param strPdaMsgContentsFormat->PDA용 메세지 내용 포멧 문자열
     * @return String
     * @throws Exception
     * @description PDA용 Alarm 메세지 내용 조합
     */
	protected String GetPdaMsgContents(String strPdaMsgContentsFormat) throws Exception
	{
		String strResult = "";	//Return Value
		
		/*Get Param List*/
		String strPaWorkRequestId 		= "";	//보전작업ID
		String strPaWorkRequestName 	= "";	//보전작업명 
		
		String strPaRequestUserName		= "";	//보전작업요청 또는 알람접수자 명
		String strPaConfirmUserName		= "";	//보전작업지시 또는 처리자 명
		
		String strPaEquipmentId			= "";	//대상설비코드
		String strPaEquipmentName		= "";	//대상설비명
		
		String strPaReasonCode			= "";	//고장코드
		String strPaReasonName			= "";	//고장명
		
		String strPaCauseCode				= "";	//원인코드
		String strPaCauseName			= "";	//원인명
		
		String strPaRequestDeptCode		= "";	//요청부서코드
		String strPaRequestDeptName	= "";	//요청부서명
		
		String strPaConfirmDeptCode		= "";	//처리부서코드
		String strPaComfirmDeptName	= "";	//처리부서명
		
		String strPaTroubleTypeCode		= "";	//이상유형코드
		String strPaTroubleTypeName		= "";	//이상유형명
		String strPaCheckPoint				= "";	//이상징후내용
		
		String strPaAlarmId					= "";	//알람ID
		String strPaAlarmTimeKey			= "";	//알람타임키
		
		List liQueryResult;
		LinkedCaseInsensitiveMap QueryMap;
		
		/*Alarm Msg Contents Combination*/
		switch (this.strAlarmId) 
		{
			//1.보전작업 지시
			case "ASSIGNWORKREQUEST":
				/*
				  [작업지시]%s / %s
				  [작업지시]WRBM201308210004 / AG-SU10-002
				 */
				 strPaWorkRequestId 			= (String)this.liMsgKeyParam.get(0);													//보전작업ID
				 
				 liQueryResult 				= GetWorkRequestInfo(this.strPlantId,strPaWorkRequestId);						//보전작업기본정보 조회
				 if (liQueryResult.size() == 0)
				 {
					//throw new CustomException("Can Not Found WorkRequest Data.", this.strPlantId, strPaWorkRequestId);
					this.log.info("Can Not Found WorkRequest Data.");
					return "";
				 }
				 QueryMap = (LinkedCaseInsensitiveMap) liQueryResult.get(0);
				 
				 if(QueryMap.get("REQUESTCOMMENT") !=  null)
				 {
					 strPaWorkRequestName		= QueryMap.get("REQUESTCOMMENT").toString();									//보전작업명
				 }
				 if(QueryMap.get("EQUIPMENTID") !=  null)
				 {
					 strPaEquipmentId 				= QueryMap.get("EQUIPMENTID").toString();										//대상설비코드
				 }
				 //strPaEquipmentName 			= QueryMap.get("EQUIPMENTNAME").toString();								//대상설비명
				 if(QueryMap.get("CONFIRMUSERNAME") !=  null)
				 {
					 strPaConfirmUserName		= QueryMap.get("CONFIRMUSERNAME").toString();								//처리자명
				 }
				 strResult = String.format(strPdaMsgContentsFormat, strPaWorkRequestId , strPaEquipmentId );
				 
			break;
			
			//2.돌발보전 발생
			case "EQPDOWN":
				strResult = "";
			break;
			
			//3.보전작업 요청
			case "REQUESTBM":
				strResult = "";
			break;
			
			//4.보전작업 승인
			case "ACCEPTBM":
				strResult = "";
			break;
			
			//5.보전작업 시작
			case "STARTBM":
				strResult = "";
			break;
			
			//6.보전작업 종료
			case "ENDBM":
				strResult = "";
			break;
			
			//7.보전작업 마감
			case "CLOSEBM":
				strResult = "";
			break;
			
			//8.일반작업 요청
			case "REQUESTNR":
				strResult = "";
			break;
			
			//9.일반작업 승인
			case "ACCEPTNR":
				strResult = "";
			break;
			
			//10.일반작업 완료
			case "ENDNR":
				strResult = "";
			break;
			
			//11.예방보전 시작
			case "STARTPM":
				strResult = "";
			break;

			//12.예방보전 종료
			case "ENDPM":
				strResult = "";
			break;
			
			//13.예방보전 수립
			case "ESTABLISHPM":
				strResult = "";
			break;
			
			//14.징후관리 등록
			case "SYMPTOM":
				strResult = "";
			break;
			
			//15.돌발보전 발생(체크)
			case "EQPCHECK":
				strResult = "";
			break;
			
			//16.유틸리티다운
			case "UTDOWN":
				strResult = "";
			break;
			
			//17.UVR다운
			case "UVRDOWN":
				strResult = "";
			break;
			
			default:
			break;
		}
		
		return strResult;
	}
	
	/*************************************************************************************
    [ALARM_DATA_SELECT_METHOD]
	 *************************************************************************************/
	/**
	 * @MethodName GetMaxPdaMsgSequence
	 * @param strPlantId->공장코드
	 * @return String
	 * @throws Exception
	 * @description PDA 메시지 Max Sequence 반환
	 */
	protected String GetMaxPdaMsgSequence(String strPlantId) throws Exception
	{
		String strResult = "";
		String strSql = "SELECT EM03_GET_NEW_PDAMSGSEQUENCE(:PLANTID,'0') AS NEW_MSGSEQUENCE  FROM DUAL";
		
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.put("PLANTID",strPlantId);
		List<LinkedCaseInsensitiveMap> resultList = SqlMesTemplate.queryForList(strSql, bindMap);
		if (resultList.size() > 0 )
		{
			LinkedCaseInsensitiveMap woInfo = (LinkedCaseInsensitiveMap) resultList.get(0);
			strResult = (String) woInfo.get("NEW_MSGSEQUENCE");
		}
		
		return strResult;
	}
	
	/**
	 * @MethodName GetWorkRequestInfo
     * @param strPlantId->공장코드 strWorkRequestId->보전작업ID
     * @return String
     * @throws Exception
     * @description 보전작업기본정보 조회
     */
	protected List GetWorkRequestInfo(String strPlantId, String strWorkRequestId) throws Exception
    {
		try
		{
			List liResult = null;		// Return Value
			HashMap<String, Object> map = new HashMap<String, Object>();
			this.strSql =
					" SELECT A.WORKREQUESTID ,  " +
			
					"    A.REQUESTCOMMENT ,  " +				//요청내용
					"    A.ACTIONCOMMENT ,  " +				//조치내용
					
					"   A.EQUIPMENTID ,  " +
					"   B.EQUIPMENTNAME ,  " +
					"   (SELECT EMP_NM  " +
					"     FROM TC00_EMP  " +
					"    WHERE 1=1  " +
					"          AND TO_CHAR(SYSDATE,'YYYYMMDD') BETWEEN FROM_DATE AND TO_DATE  " +
					"          AND A.LASTUPDATEUSERID = EMP  " +
					"   ) AS CONFIRMUSERNAME,  " +
					"   A.CAUSECODE,  " +
					"   (SELECT DESCRIPTION  " +
					"     FROM REASONCODE  " +
					"    WHERE REASONCODE = A.CAUSECODE  " +
					"          AND REASONCODETYPE = 'CAUSE'  " +
					"          AND PLANTID = A.PLANTID  " +
					"   ) AS CAUSECODENAME,  " +
					"   A.REQUESTREASONCODE AS REASONCODE,  " +
					"   (SELECT DESCRIPTION  " +
					"     FROM REASONCODE  " +
					"    WHERE REASONCODE = A.REQUESTREASONCODE  " +
					"          AND REASONCODETYPE = 'DEFECT'  " +
					"          AND PLANTID = A.PLANTID  " +
					"   ) AS REASONCODENAME,  " +
					"   A.RESPONSIBLEDEPT,  " +
					"   (SELECT AREANAME  " +
					"     FROM AREA  " +
					"    WHERE 1=1  " +
					"          AND PLANTID = A.PLANTID  " +
					"          AND AREAID = A.RESPONSIBLEDEPT  " +
					"   ) AS RESPONSIBLEDEPTNAME,  " +
					"   A.REQUESTUSERID,  " +
					"   (SELECT EMP_NM  " +
					"     FROM TC00_EMP  " +
					"    WHERE 1=1  " +
					"          AND TO_CHAR(SYSDATE,'YYYYMMDD') BETWEEN FROM_DATE AND TO_DATE  " +
					"          AND A.REQUESTUSERID = EMP  " +
					"   ) AS REQUESTUSERNAME,  " +
					"   NVL(A.REQUESTTYPE,'') AS EMWORKDIVISION  " +				//보전작업작업구분(공무부일경우 알람수신 USER 선별시 사용)
					" FROM EMWORKREQUEST A  " +
					" LEFT OUTER JOIN EQUIPMENTDEFINITION B  " +
					"   ON(  " +
					"        A.EQUIPMENTID = B.EQUIPMENTID  " +
					"   )  " +
					" WHERE 1=1  " +
					"    AND A.PLANTID = :PLANTID  " +
					"    AND A.WORKREQUESTID = :WORKREQUESTID  ";
					
			map.put("PLANTID",strPlantId);									//공장코드
			map.put("WORKREQUESTID",strWorkRequestId);				//보전작업id
			liResult = SqlMesTemplate.queryForList(this.strSql, map);	
			return liResult;
		}
		catch(Exception ex)
		{	
			throw ex;
		}
    }
	 
	 /**
		 * @MethodName GetWorkAlarmtInfo
	     * @param strPlantId->공장코드 strAlarmId->알람ID strAlarmTimeKey->알람타임키
	     * @return String
	     * @throws Exception
	     * @description 알람기본정보 조회
	     */
		protected List GetWorkAlarmtInfo(String strPlantId, String strAlarmId, String strAlarmTimeKey) throws Exception
	    {
			try
			{
				List liResult = null;		// Return Value
				HashMap<String, Object> map = new HashMap<String, Object>();
				this.strSql =
						" SELECT A.ALARMID ,   " +
						"   A.ALARMTIMEKEY ,   " +
						"  A.EQUIPMENTID ,   " +
						"  B.EQUIPMENTNAME ,   " +
						"  A.REASONCODE,   " +
						"  (SELECT DESCRIPTION   " +
						"    FROM REASONCODE   " +
						"   WHERE REASONCODE = A.REASONCODE   " +
						"         AND REASONCODETYPE = A.REASONCODETYPE   " +
						"         AND PLANTID = A.PLANTID   " +
						"  ) AS REASONCODENAME,   " +
						"  A.REQUESTUSERID,   " +
						"  (SELECT EMP_NM   " +
						"    FROM TC00_EMP   " +
						"   WHERE 1=1   " +
						"         AND TO_CHAR(SYSDATE,'YYYYMMDD') BETWEEN FROM_DATE AND TO_DATE   " +
						"         AND A.REQUESTUSERID = EMP   " +
						"  ) AS REQUESTUSERNAME   " +
						" FROM ALARM A   " +
						" LEFT OUTER JOIN EQUIPMENTDEFINITION B   " +
						"  ON(   " +
						"      A.EQUIPMENTID = B.EQUIPMENTID   " +
						"  )   " +
						" WHERE 1=1   " +
						"  AND A.PLANTID = :PLANTID   " +
						"  AND A.ALARMID = :ALARMID   " +
						"  AND A.ALARMTIMEKEY = :ALARMTIMEKEY   ";
						
				map.put("PLANTID",strPlantId);			//공장코드
				map.put("ALARMID",strAlarmId);			//알람Id
				map.put("ALARMTIMEKEY",strAlarmTimeKey);			//알람Timekey
				liResult = SqlMesTemplate.queryForList(this.strSql, map);	
				return liResult;
			}
			catch(Exception ex)
			{	
				throw ex;
			}
	    }
	 
	 /**
	 * @MethodName GetTroubleShootingInfo
     * @param strPlantId->공장코드 strEquipmentId->대상설비 strTroubleTypeCode->이상유형코드  strCheckPoint->이상징후
     * @return String
     * @throws Exception
     * @description 징후관리기본정보 조회
     */
	protected List GetTroubleShootingInfo(String strPlantId, String strEquipmentId, String strTroubleTypeCode, String strCheckPoint) throws Exception
    {
		try
		{
			List liResult = null;		// Return Value
			HashMap<String, Object> map = new HashMap<String, Object>();
			this.strSql =
					" SELECT   " +
					" 	   A.EQUIPMENTID ,   " +
					"      B.EQUIPMENTNAME ,   " +
					"      A.TROUBLETYPE,   " +
					"      (SELECT DESCRIPTION FROM ENUMVALUE WHERE ENUMNAME = 'EMTroubleType' AND ENUMVALUE = A.TROUBLETYPE) AS TROUBLETYPENAME,   " +
					"      A.CHECKPOINT,   " +
					"      A.ACTION,   " +
					"      C.DEPT,   " +
					"      (SELECT AREANAME FROM AREA WHERE PLANTID = A.PLANTID AND AREAID = C.DEPT) AS DEPTNAME,   " +
					"      A.CREATEUSERID,   " +
					"      C.EMP_NM AS CREATEUSERNAME   " +
					"  FROM EMTROUBLESHOOTING A   " +
					"   LEFT OUTER JOIN EQUIPMENTDEFINITION B   " +
					"  ON(   " +
					"      A.EQUIPMENTID = B.EQUIPMENTID   " +
					"  )   " +
					"  	LEFT OUTER JOIN TC00_EMP C   " +
					"   ON(A.CREATEUSERID = C.EMP)   " +
					" WHERE 1=1   " +
					" AND TO_CHAR(SYSDATE,'YYYYMMDD') BETWEEN C.FROM_DATE AND C.TO_DATE   " +
					"  AND A.PLANTID 		= :PLANTID   " +
					"  AND A.TROUBLETYPE 	= :TROUBLETYPE   " +
					"  AND A.CHECKPOINT 	= :CHECKPOINT   ";
					
			map.put("PLANTID",strPlantId);							//공장코드
			map.put("TROUBLETYPE",strTroubleTypeCode);			//이상유형코드
			map.put("CHECKPOINT",strCheckPoint);					//이상징후
			liResult = SqlMesTemplate.queryForList(this.strSql, map);	
			return liResult;
		}
		catch(Exception ex)
		{	
			throw ex;
		}
    }
	 
	/**
	 * @MethodName GetAlarmMsgFormatList
     * @param strPlantId->공장코드 strAlarmId->알람ID
     * @return String
     * @throws Exception
     * @description AlarmID 메세지 포멧 정보 조회(Pool정보)
     */
	protected List GetAlarmMsgFormatList(String strPlantId, String strAlarmId) throws Exception
    {
		try
		{
			List liResult = null;		// Return Value
			HashMap<String, Object> map = new HashMap<String, Object>();
			this.strSql =
					" SELECT    " +
					" 	A.MSGSUBJECTFORMAT ,   " +
					"   A.MSGCONTENTSFORMAT,   " +
					"   A.PDAMSGSUBJECTFORMAT,   " +
					"   A.PDAMSGCONTENTSFORMAT   " +
					" FROM ALARMDEFINITIONPOOL A   " +
					" WHERE A.PLANTID = :PLANTID   " +
					" AND A.ALARMID = :ALARMID   ";
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

	/**
	 * @MethodName GetPdaMsgUseYn
	 * @param strPlantId->공장코드 strAlarmId->알람ID,strRevDept->수신부서
	 * @return String
	 * @throws Exception
	 * @description 해당 부서에 지정된 PDA가 알람을 수신할지를 조회 
	 */
	protected Boolean GetPdaMsgUseYn(String strPlantId, String strAlarmId, String strRevDept) throws Exception
	{
		try
		{
			Boolean bIsResult = false;		// Return Value
			HashMap<String, Object> map = new HashMap<String, Object>();			
			this.strSql = " SELECT NVL(PDAMSGUSEYN,'N') AS PDAMSGUSEYN FROM ALARMMASTER A WHERE 1=1 AND A.PLANTID = :PLANTID AND A.ALARMID = :ALARMID AND A.RECEIVEDEPARTMENT = :RECEIVEDEPARTMENT ";
			map.put("PLANTID",strPlantId);						//공장코드
			map.put("ALARMID",strAlarmId);						//알람Id
			map.put("RECEIVEDEPARTMENT",strRevDept);		//수신부서	
			List<LinkedCaseInsensitiveMap> resultList = SqlMesTemplate.queryForList(strSql, map);
			if (resultList.size() > 0 )
			{
				LinkedCaseInsensitiveMap liResult = (LinkedCaseInsensitiveMap) resultList.get(0);
				if( ((String) liResult.get("PDAMSGUSEYN")).equals("Y") )
				{
					bIsResult = true;
				}
				else
				{
					bIsResult = false;
				}
			}
			return bIsResult;
		}
		catch(Exception ex)
		{	
			throw ex;
		}
	}

	/**
	 * @MethodName GetPdaMsgUseYn
	 * @param strPlantId->공장코드 strAlarmId->알람ID,strRevDept->수신부서  strEmWorkDivision->작업구분(Common : 공통 Machine : 기계 Electric : 전기l)
	 * @return String
	 * @throws Exception
	 * @description 해당 부서에 지정된 PDA가 알람을 수신할지를 조회  (수신부서가 공무부 일경우엔 작업구분을 이용하여 전기 기계 과로 선별하여 수신유무를 조회 한다..)
	 */
	protected Boolean GetPdaMsgUseYn(String strPlantId, String strAlarmId, String strRevDept ,String strEmWorkDivision) throws Exception
	{
		try
		{
			Boolean bIsResult = false;		// Return Value
			HashMap<String, Object> map = new HashMap<String, Object>();			
			this.strSql = " SELECT NVL(PDAMSGUSEYN,'N') AS PDAMSGUSEYN FROM ALARMMASTER A WHERE 1=1 AND A.PLANTID = :PLANTID AND A.ALARMID = :ALARMID ";
			
			AlarmUtil alarmUtil = new AlarmUtil();
			//if(strRevDept.equals(mes.constant.Constant.EM_DEPT_OFFICE))  //수신부서가 공무부 일경우 작업구분을 따져 공무부 기계/전기 또는 전체로 선별하여 진행한다.
			if(strRevDept.equals(alarmUtil.getRepairDept(strPlantId)))  //수신부서가 공무부 일경우 작업구분을 따져 공무부 기계/전기 또는 전체로 선별하여 진행한다.
			{
				//작업구분이 전체 Common 일경우엔  기계 / 전기 과에 셋팅된 모든 유저대상으로 알람을 발송
				if(strEmWorkDivision.equals("Common"))
				{
					this.strSql +=" AND NVL(A.RECEIVEDEPARTMENT,' ') IN (EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , 'Machine') , EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , 'Electric'))  ";
				}
				//아닐경우엔 EMWORKDIVISION에 지정된 해당과의 알람대상자에게만 알람을 발송
				else
				{
					this.strSql +=" AND NVL(A.RECEIVEDEPARTMENT,' ') LIKE '%'|| EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , :EMWORKDIVISION)||'%'  ";
				}
			}
			else
			{
				this.strSql +=" AND NVL(A.RECEIVEDEPARTMENT,' ') LIKE '%'|| :RECEIVEDEPARTMENT ||'%'  ";		//일반수신부서
			}
			
			map.put("PLANTID",strPlantId);						//공장코드
			map.put("ALARMID",strAlarmId);						//알람Id
			map.put("RECEIVEDEPARTMENT",strRevDept);		//수신부서
			map.put("EMWORKDIVISION",strEmWorkDivision);			//작업구분
			List<LinkedCaseInsensitiveMap> resultList = SqlMesTemplate.queryForList(strSql, map);
			if (resultList.size() > 0 )
			{
				LinkedCaseInsensitiveMap liResult = (LinkedCaseInsensitiveMap) resultList.get(0);
				if( ((String) liResult.get("PDAMSGUSEYN")).equals("Y") )
				{
					bIsResult = true;
				}
				else
				{
					bIsResult = false;
				}
			}
			return bIsResult;
		}
		catch(Exception ex)
		{	
			throw ex;
		}
	}
	
	/**
	 * @MethodName GetAlarmDefinitionListPda
	 * @param strPlantId->공장코드 , strRevDept->수신부서
	 * @return String
	 * @throws Exception
	 * @description 해당 수신부서에 사용되고 있는 PDA 리스트 조회 
	 */
	protected List GetAlarmDefinitionListPda(String strPlantId, String strRevDept) throws Exception
	{
		try
		{
			List liResult = null;		// Return Value
			HashMap<String, Object> map = new HashMap<String, Object>();
			this.strSql =
					" SELECT A.PLANTID,   " +
					"  A.PDAID,   " +
					" A.DESCRIPTION,   " +
					" A.USEDDEPARTMENT,   " +
					" A.MODEL,   " +
					" A.SERIALNO,   " +
					" A.PDACALLNO,		   " +
					" A.MESSOFTWAREVERSION,   " +
					" A.LOGINUSER,   " +
					" A.LOGINTIME,   " +
					" A.USEYN,			   " +
					" A.REMARK   " +
					" FROM PDADEFINITION A   " +
					" WHERE 1=1   " +
					" AND A.PLANTID = :PLANTID   " +
					" AND NVL(A.USEYN,'N') = 'Y'			   " +
					" AND A.USEDDEPARTMENT = :USEDDEPARTMENT";
			map.put("PLANTID",strPlantId);						//공장코드
			map.put("USEDDEPARTMENT",strRevDept);		//수신부서
			liResult = SqlMesTemplate.queryForList(this.strSql, map);	
			return liResult;
		}
		catch(Exception ex)
		{	
			throw ex;
		}
	}

	/**
	 * @MethodName GetAlarmDefinitionListPda
	 * @param strPlantId->공장코드 , strRevDept->수신부서 , strEmWorkDivision->작업구분(Common : 공통 Machine : 기계 Electric : 전기l)
	 * @return String
	 * @throws Exception
	 * @description 해당 수신부서에 사용되고 있는 PDA 리스트 조회 (수신부서가 공무부 일경우엔 작업구분을 이용하여 전기 기계 과로 선별하여 전송한다.)
	 */
	protected List GetAlarmDefinitionListPda(String strPlantId, String strRevDept, String strEmWorkDivision, String strPdaSmsSandGubun) throws Exception
	{
		try
		{
			List liResult = null;		// Return Value
			HashMap<String, Object> map = new HashMap<String, Object>();
			this.strSql =
					" SELECT A.PLANTID,   " +
					"  A.PDAID,   " +
					" A.DESCRIPTION,   " +
					" A.USEDDEPARTMENT,   " +
					" A.MODEL,   " +
					" A.SERIALNO,   " +
					" A.PDACALLNO,		   " +
					" A.MESSOFTWAREVERSION,   " +
					" A.LOGINUSER,   " +
					" A.LOGINTIME,   " +
					" A.USEYN,			   " +
					" A.REMARK   " +
					" FROM PDADEFINITION A   " +
					" WHERE 1=1   " +
					" AND A.PLANTID = :PLANTID   " +
					" AND NVL(A.USEYN,'N') = 'Y'			   " +
					//" AND A.USEDDEPARTMENT = :USEDDEPARTMENT";
					" AND NVL(A.PDASMSSENDGUBUN,'S1') = :PDASMSSENDGUBUN";			//전송구분
			AlarmUtil alarmUtil = new AlarmUtil();
			if(strRevDept.equals(alarmUtil.getRepairDept(strPlantId)))  //수신부서가 공무부 일경우 작업구분을 따져 공무부 기계/전기 또는 전체로 선별하여 진행한다.
			//if(strRevDept.equals(mes.constant.Constant.EM_DEPT_OFFICE))  //수신부서가 공무부 일경우 작업구분을 따져 공무부 기계/전기 또는 전체로 선별하여 진행한다.
			{
				//작업구분이 전체 Common 일경우엔  기계 / 전기 과에 셋팅된 모든 유저대상으로 알람을 발송
				if(strEmWorkDivision.equals("Common"))
				{
					this.strSql +=" AND NVL(A.USEDDEPARTMENT,' ') IN (EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , 'Machine') , EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , 'Electric'))  ";
				}
				//아닐경우엔 EMWORKDIVISION에 지정된 해당과의 알람대상자에게만 알람을 발송
				else
				{
					this.strSql +=" AND NVL(A.USEDDEPARTMENT,' ') LIKE '%'|| EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , :EMWORKDIVISION)||'%'  ";
				}
			}
			else	//일반 생산부 또는 기타부서
			{
				this.strSql +=" AND NVL(A.USEDDEPARTMENT,' ') LIKE '%'|| :USEDDEPARTMENT ||'%'  ";		//일반수신부서
			}		
			
			map.put("PLANTID",strPlantId);						//공장코드
			map.put("USEDDEPARTMENT",strRevDept);				//수신부서
			map.put("EMWORKDIVISION",strEmWorkDivision);		//작업구분(Common:공통 Machine:기계 )
			map.put("PDASMSSENDGUBUN",strPdaSmsSandGubun);		//전송구분(S1:CDMA S2:TCP/IP)
			liResult = SqlMesTemplate.queryForList(this.strSql, map);	
			return liResult;
		}
		catch(Exception ex)
		{	
			throw ex;
		}
	}

	/**
	 * @MethodName GetEquipmentName
	 * @param strPlantId->공장코드 strEquipmentId->설비코드
	 * @return String
	 * @throws Exception
	 * @description 설비명 반환
	 */
	protected String GetEquipmentName(String strPlantId, String strEquipmentId) throws Exception
	{
		String strResult = "";
		String strSql = "SELECT A.EQUIPMENTNAME FROM EQUIPMENTDEFINITION A WHERE 1=1 AND A.PLANTID  =  :PLANTID AND A.EQUIPMENTID = :EQUIPMENTID";
		
		HashMap<String, Object> bindMap = new HashMap<String, Object>();
		bindMap.put("PLANTID",strPlantId);
		bindMap.put("EQUIPMENTID",strEquipmentId);
		List<LinkedCaseInsensitiveMap> resultList = SqlMesTemplate.queryForList(strSql, bindMap);
		if (resultList.size() > 0 )
		{
			LinkedCaseInsensitiveMap woInfo = (LinkedCaseInsensitiveMap) resultList.get(0);
			strResult = (String) woInfo.get("DEPT");
		}
		
		return strResult;
	}

	/**
	 * @MethodName GetAlarmDefinitionList
     * @param strPlantId->공장코드 strAlarmId->알람ID,strRevDept->수신부서
     * @return String
     * @throws Exception
     * @description AlarmID 설정정보 조회
     */
	protected List GetAlarmDefinitionList(String strPlantId, String strAlarmId,String strRevDept) throws Exception
    {
		try
		{
			List liResult = null;		// Return Value
			HashMap<String, Object> map = new HashMap<String, Object>();
		
			this.strSql = 	
				" SELECT   " +
				"    A.ALARMID,	  " +			//알람id
				"    A.RECEIVEDEPARTMENT,	  " +		//수신부서
				"    A.USERSEQUENCE,   " +				//순번
				"    A.EMPLOYEE,   " +    				//사번
				"    B.EMP_NM,  " + 				//성명
				" 	 A.MESSENGERACTIONFLAG,  " + 	//금란지교플레그
				"    A.EMAILACTIONFLAG,  " +	  	//이메일플레그
				"    A.SMSACTIONFLAG,  " +			//SMS플레그
				"    A.LOTACTIONFLAG,  " + 		//로트HOLD플레그
				"    A.EQPACTIONFLAG,  " + 	//설비HOLD플레그
				"    B.USERID,  " +            	//그룹웨어(금란지교)USERID
				"    B.EMAIL,  " +             	//EMAIL
				"    B.MOBILE,  " +            	//핸드폰번호
				"    A.ETCCONTACTNUMBERS,  " + 	//기타연락처
				"    DECODE(A.ETCCONTACTNUMBERS,NULL,B.MOBILE,B.MOBILE||','||A.ETCCONTACTNUMBERS) AS SMSNUMBER,  " + //SMS수신처(기본핸드폰번호와기타연락처조합)
				"    A.REMARK,  " +
				// 2014.10.21(KJH) 'SMSUSEYN'항목 추가
				// 요청내용: 보전작업 SMS 발송 時 시간 지정을 할 수 있게 변경 (야간, 주말 등 퇴근 이후 시간)
				// 상세설명: [ALMD0200M]알람코드관리(부서기준)(ALARMMASTER)에 SMS 발송시간에대한 기준정보값을 입력하면, 해당 데이터를 기준으로 SMS 발송 대상자 Filter 처리.
				// 조건: TIMECUTYN이 'Y'이면서 현시각이 TIMECUT_FROM, TIMECUT_TO 시간범위에 들어있는 대상이거나, 
				//       TIMECUTYN가 'N'인 대상에게만 SMS 전송.
				"    CASE WHEN ( (C.TIMECUTYN = 'Y' AND TO_CHAR(SYSDATE, 'HH24MI') BETWEEN C.TIMECUT_FROM AND C.TIMECUT_TO)	 " +
				"                 OR	 " +
				"                 NVL(C.TIMECUTYN, 'N') = 'N'	 " +
				"              )" +
				"         THEN 'Y'" +
				"         ELSE 'N'" +
				"    END AS SMSUSEYN 	" +
				" FROM ALARMDEFINITIONDETAIL A  " +
				" LEFT OUTER JOIN  " +
				" (  " +
				" SELECT EMP,  " +
				"       ID_USER AS USERID,  " +
				"       DEPT,  " +
				"       EMP_NM,  " +
				"       ID_USER||'@kccworld.co.kr' AS EMAIL,  " +
				"       MOBILE  " +
				"  FROM TC00_GW_USER  " +
				" WHERE 1=1  " +
				"       AND DFLAG = 'Y'  " +
				"      AND ID_USER IS NOT NULL  " +
				"       AND MOBILE IS NOT NULL  " +
				" )B   " +							//ERP유저정보
				" ON(  " +
				"    A.EMPLOYEE = B.EMP  " +
				" )  " +
				" LEFT OUTER JOIN ALARMMASTER C  " +
				" ON(A.PLANTID = C.PLANTID AND A.ALARMID = C.ALARMID  AND A.RECEIVEDEPARTMENT = C.RECEIVEDEPARTMENT)  " +
				" WHERE 1=1  " +
				" AND C.USEYN = 'Y'	  " +						//사용중인AlarmId만 조회
				" AND A.PLANTID = :PLANTID " +				//공장코드
				" AND A.ALARMID = :ALARMID	  " +			//알람ID
				" AND NVL(A.RECEIVEDEPARTMENT,' ') LIKE '%'|| :RECEIVEDEPARTMENT ||'%'  ";		//일반수신부서
			
			map.put("PLANTID",strPlantId);			//공장코드
			map.put("ALARMID",strAlarmId);			//알람Id
			map.put("RECEIVEDEPARTMENT",strRevDept);		//수신부서	
			liResult = SqlMesTemplate.queryForList(this.strSql, map);
		
			return liResult;
		}
		catch(Exception ex)
		{	
			throw ex;
		}
    }
	
	/**
	 * @MethodName GetAlarmDefinitionList
     * @param strPlantId->공장코드 strAlarmId->알람ID,strRevDept->수신부서 strEmWorkDivision->작업구분(Common : 공통 Machine : 기계 Electric : 전기l)
     * @return String
     * @throws Exception
     * @description AlarmID 설정정보 (수신부서가 공무부 일경우엔 작업구분을 이용하여 전기 기계 과로 선별하여 전송한다.)
     */
	protected List GetAlarmDefinitionList(String strPlantId, String strAlarmId,String strRevDept, String strEmWorkDivision) throws Exception
    {
		try
		{
			List liResult = null;		// Return Value
			HashMap<String, Object> map = new HashMap<String, Object>();
		
			this.strSql = 	
				" SELECT   " +
				"    A.ALARMID,	  " +			//알람id
				"    A.RECEIVEDEPARTMENT,	  " +		//수신부서
				"    A.USERSEQUENCE,   " +				//순번
				"    A.EMPLOYEE,   " +    				//사번
				"    B.EMP_NM,  " + 				//성명
				" 	 A.MESSENGERACTIONFLAG,  " + 	//금란지교플레그
				"    A.EMAILACTIONFLAG,  " +	  	//이메일플레그
				"    A.SMSACTIONFLAG,  " +			//SMS플레그
				"    A.LOTACTIONFLAG,  " + 		//로트HOLD플레그
				"    A.EQPACTIONFLAG,  " + 	//설비HOLD플레그
				"    B.USERID,  " +            	//그룹웨어(금란지교)USERID
				"    B.EMAIL,  " +             	//EMAIL
				"    B.MOBILE,  " +            	//핸드폰번호
				"    A.ETCCONTACTNUMBERS,  " + 	//기타연락처
				"    DECODE(A.ETCCONTACTNUMBERS,NULL,B.MOBILE,B.MOBILE||','||A.ETCCONTACTNUMBERS) AS SMSNUMBER,  " + //SMS수신처(기본핸드폰번호와기타연락처조합)
				"    A.REMARK,  " +
				// 2014.10.21(KJH) 'SMSUSEYN'항목 추가
				// 요청내용: 보전작업 SMS 발송 時 시간 지정을 할 수 있게 변경 (야간, 주말 등 퇴근 이후 시간)
				// 상세설명: [ALMD0200M]알람코드관리(부서기준)(ALARMMASTER)에 SMS 발송시간에대한 기준정보값을 입력하면, 해당 데이터를 기준으로 SMS 발송 대상자 Filter 처리.
				// 조건: TIMECUTYN이 'Y'이면서 현시각이 TIMECUT_FROM, TIMECUT_TO 시간범위에 들어있는 대상이거나, 
				//       TIMECUTYN가 'N'인 대상에게만 SMS 전송.
				"    CASE WHEN ( (C.TIMECUTYN = 'Y' AND TO_CHAR(SYSDATE, 'HH24MI') BETWEEN C.TIMECUT_FROM AND C.TIMECUT_TO)	 " +
				"                 OR	 " +
				"                 NVL(C.TIMECUTYN, 'N') = 'N'	 " +
				"              )" +
				"         THEN 'Y'" +
				"         ELSE 'N'" +
				"    END AS SMSUSEYN 	" +
				" FROM ALARMDEFINITIONDETAIL A  " +
				" LEFT OUTER JOIN  " +
				" (  " +
				" SELECT EMP,  " +
				"       ID_USER AS USERID,  " +
				"       DEPT,  " +
				"       EMP_NM,  " +
				"       ID_USER||'@kccworld.co.kr' AS EMAIL,  " +
				"       MOBILE  " +
				"  FROM TC00_GW_USER  " +
				" WHERE 1=1  " +
				"       AND DFLAG = 'Y'  " +
				"      AND ID_USER IS NOT NULL  " +
				"       AND MOBILE IS NOT NULL  " +
				" )B   " +							//ERP유저정보
				" ON(  " +
				"    A.EMPLOYEE = B.EMP  " +
				" )  " +
				" LEFT OUTER JOIN ALARMMASTER C  " +
				" ON(A.PLANTID = C.PLANTID AND A.ALARMID = C.ALARMID  AND A.RECEIVEDEPARTMENT = C.RECEIVEDEPARTMENT)  " +
				" WHERE 1=1  " +
				" AND C.USEYN = 'Y'	  " +			//사용중인AlarmId만 조회
				" AND A.PLANTID = :PLANTID " +			//공장코드
				" AND A.ALARMID = :ALARMID	  ";		//알람ID
			AlarmUtil alarmUtil = new AlarmUtil();
			if(strRevDept.equals(alarmUtil.getRepairDept(strPlantId)))  //수신부서가 공무부 일경우 작업구분을 따져 공무부 기계/전기 또는 전체로 선별하여 진행한다.
			//if(strRevDept.equals(mes.constant.Constant.EM_DEPT_OFFICE))  //수신부서가 공무부 일경우 작업구분을 따져 공무부 기계/전기 또는 전체로 선별하여 진행한다.
			{
				//작업구분이 전체 Common 일경우엔  기계 / 전기 과에 셋팅된 모든 유저대상으로 알람을 발송
				if(strEmWorkDivision.equals("Common"))
				{
					this.strSql +=" AND NVL(A.RECEIVEDEPARTMENT,' ') IN (EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , 'Machine') , EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , 'Electric'))  ";
				}
				//아닐경우엔 EMWORKDIVISION에 지정된 해당과의 알람대상자에게만 알람을 발송
				else
				{
					this.strSql +=" AND NVL(A.RECEIVEDEPARTMENT,' ') LIKE '%'|| EM03_GET_DIV_REPAIR_DEPTCODE(:PLANTID , :EMWORKDIVISION)||'%'  ";
				}
			}
			else
			{
				this.strSql +=" AND NVL(A.RECEIVEDEPARTMENT,' ') LIKE '%'|| :RECEIVEDEPARTMENT ||'%'  ";		//일반수신부서
			}
		
			map.put("PLANTID",strPlantId);			//공장코드
			map.put("ALARMID",strAlarmId);			//알람Id
			map.put("RECEIVEDEPARTMENT",strRevDept);		//수신부서	
			map.put("EMWORKDIVISION",strEmWorkDivision);		//작업구분(Common:공통 Machine:기계 )
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
	 * @MethodName SendSms : V1.0(유저CALL NUMBER를 이용한 SMS전송)
     * @param strRevUserList->메신져수신자리스트전화번호,strSendUser->송신자명,strSendUserCallNumber->송신자전화번호,strMsg->메세지내용
     * @return void
     * @throws Exception
     * @description SMSSend메서드
     */
	public void SendSmsUseCallNumber(String plantId,String strRevUserList,String strSendUser,String strSendUserCallNumber, String strMsg) throws Exception
	{
		try
		{
			
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	
}
