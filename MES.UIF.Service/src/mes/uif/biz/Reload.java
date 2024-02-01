package mes.uif.biz;

import kr.co.mesframe.esb.ObjectExecuteService;
import mes.generic.GenericServiceProxy;
import mes.uif.generic.UIFServiceProxy;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class Reload implements ObjectExecuteService
{
	public Object execute(Document recvDoc)
	{
		GenericServiceProxy.getMessageEventMap().load();
		
		UIFServiceProxy.getTargetSubjectMap().reload();
		
		return recvDoc;
	}

}
