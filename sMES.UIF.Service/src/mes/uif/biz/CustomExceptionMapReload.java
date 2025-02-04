package mes.uif.biz;

import kr.co.mesframe.esb.ObjectExecuteService;
import mes.errorHandler.CustomExceptionMap;
import mes.util.SendMessageUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class CustomExceptionMapReload implements ObjectExecuteService
{
	public Object execute(Document recvDoc)
	{
		String targetServer = SendMessageUtil.getParam(recvDoc, "SERVERNAME");

		if ( targetServer.equalsIgnoreCase("UIFsvr") || targetServer.equalsIgnoreCase("TESTsvr") )
		{
			CustomExceptionMap.reload();

			return recvDoc;
		}
		else
		{
			SendMessageUtil.send(targetServer, recvDoc);

			return null;
		}
	}
	
}
