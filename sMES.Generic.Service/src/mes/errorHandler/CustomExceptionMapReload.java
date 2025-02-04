package mes.errorHandler;

import kr.co.mesframe.esb.ObjectExecuteService;

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
	/**
	 * CustomExcetptionMap을 reload합니다
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc)
	{
		CustomExceptionMap.reload();
		
		return recvDoc;
	}
}

