package mes.lot.execution;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.util.MessageUtil;

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
public class Reply implements ObjectExecuteService
{
	private static final transient Logger logger = LoggerFactory.getLogger(Reply.class);
	
	/**
	 * 받아온 Document를 Reply
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc)
	{
		logger.info("Reply :: " + MessageUtil.toString(recvDoc));
		return null;
	}

}
