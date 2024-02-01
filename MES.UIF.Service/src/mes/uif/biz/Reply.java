package mes.uif.biz;

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
	private static final transient Logger  log = LoggerFactory.getLogger(Reply.class);
	
	public Object execute(Document recvDoc)
	{
		log.debug("Before GetNodeText");
		String replySubjectName = MessageUtil.selectText(recvDoc, "//message/header/replysubject", "");
		log.debug("After GetNodeText");
		if ( replySubjectName != null && !replySubjectName.trim().isEmpty() )
		{
			return recvDoc;
		}
		else
		{
			log.info("SERP :: " + MessageUtil.toString(recvDoc));
			log.info("Message 전송에 실패했습니다.");
			return null;
		}
	}

}
