package mes.uif.biz;

import kr.co.mesframe.esb.ObjectExecuteService;
import mes.util.SendMessageUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class ServerPatch implements ObjectExecuteService
{
	
	@Override
	public Object execute(Document recvDoc)
	{
		// ServerName Update & TargetSubjectName Update
		String targetServer = "Client";
		
		// EventMapReload Event Send - Sync Message
		SendMessageUtil.send(targetServer, recvDoc);
		
		return null;
	}

}
