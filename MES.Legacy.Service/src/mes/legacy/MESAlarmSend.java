package mes.legacy;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
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
public class MESAlarmSend implements ObjectExecuteService
{
	private Logger  log = LoggerFactory.getLogger(MESAlarmSend.class);
	
	/**
	 * recvDoc String 형식으로 변환한 후 MES Alarm Send
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

				String sPlantID             = ConvertUtil.Object2String(infoMap.get("PLANTID"));
				String sAlarmID				= ConvertUtil.Object2String(infoMap.get("ALARMID"));
				String sAlarmType			= ConvertUtil.Object2String(infoMap.get("ALARMTYPE"));
				String sRelationTimeKey  	= ConvertUtil.Object2String(infoMap.get("RELATIONTIMEKEY"));
				String sCheckType		  	= ConvertUtil.Object2String(infoMap.get("CHECKTYPE"));
				
				if ( sCheckType.equals("") || sCheckType == null )
				{
					sCheckType = "LocalEIS";					
				}
				
				alarmDef.SendMESAlarmDefinitionMsg(sPlantID, sAlarmID, "", "", sAlarmType, sRelationTimeKey, sCheckType);
				
				//Thread.sleep(1500);
			}
		}
		catch ( Exception e )
		{
			log.error("MESAlarmSend Error", e);
		}
		
		return null;
	}
}
