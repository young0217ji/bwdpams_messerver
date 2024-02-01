package mes.event;

import kr.co.mesframe.esb.ObjectExecuteService;
import mes.generic.GenericServiceProxy;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class MessageEventMapReload implements ObjectExecuteService
{	
	/**
	 * MessageEventMap을 reload합니다
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc)
	{
		GenericServiceProxy.getMessageEventMap().load();
		
		return recvDoc;
	}
}

