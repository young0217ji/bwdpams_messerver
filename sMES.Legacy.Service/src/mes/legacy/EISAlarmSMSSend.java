package mes.legacy;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import mes.constant.Constant;
import mes.legacy.util.MESAlarmDefinition;
import mes.util.EISParse;
import mes.util.EventInfoUtil;

import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class EISAlarmSMSSend implements ObjectExecuteService
{
	private Logger  log = LoggerFactory.getLogger(EISAlarmSMSSend.class);
	
    /**
     * recvDoc 파싱해  EISAlarmSMS send
     * 
     * @param recvDoc
     * @return Object 
     * @throws Exception
     * 
    */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Document recvDoc)
	{		
		try
		{
			HashMap<String, Object> bodyMap = EISParse.getEISXmlParse(recvDoc);
			TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
			
			ArrayList<HashMap> listDataMap = (ArrayList<HashMap>) bodyMap.get("DATALIST");
			
			//TnxIFSMS ifsms = new TnxIFSMS();
			MESAlarmDefinition alarmDef = new MESAlarmDefinition();
			
			for ( int i = 0; i < listDataMap.size(); i++ )
			{
				HashMap<String, Object> infoMap =  listDataMap.get(i);

				String sPlantID             = Constant.PLANTID;
				String sAlarmType			= ConvertUtil.Object2String(infoMap.get("ALARMTYPE"));
				String sStationName		 	= ConvertUtil.Object2String(infoMap.get("STATIONNAME"));
				String sAlarmID				= ConvertUtil.Object2String(infoMap.get("ALARMID"));
				String sAlarmTime			= ConvertUtil.Object2String(infoMap.get("ALARMTIME"));
				String sDescription			= ConvertUtil.Object2String(infoMap.get("DESCRIPTION"));
				String sEISAlarmID			= ConvertUtil.Object2String(infoMap.get("EISALARMID"));
				String sCheckType			= "LocalEIS";
				
				/*
				// EventUser
				txnInfo.setTxnUser( sStationName );
				
				// EventTime
				if ( sAlarmTime != null && !sAlarmTime.isEmpty() )
				{
					txnInfo.setEventTime( DateUtil.getTimestamp(sAlarmTime) );
				}
				
				txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );				
				
				HashMap<String, String> paramMap = new HashMap();
				paramMap.put("SENDERID", sAlarmID);
				paramMap.put("SENDERNUMBER", "010-3455-0942");
				paramMap.put("MESSAGE", sDescription);
				paramMap.put("RECEIVEERID", sAlarmID);
				paramMap.put("DEPARTMENT", "MES");
				paramMap.put("TYPE", "MES");
				*/						

				//ifsms.sendMessage(paramMap, ",");
				
				alarmDef.SendMESAlarmDefinitionMsg(sPlantID, sAlarmID, "", sStationName, sAlarmType, "", sCheckType);
				
				//Thread.sleep(1500);
			}
		}
		catch ( Exception e )
		{
			log.error("EISAlarmSMSSend Error", e);
		}
		
		return null;
	}
}
