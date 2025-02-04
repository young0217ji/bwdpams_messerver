package mes.uif.biz;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.util.MessageUtil;
import mes.generic.GenericServiceProxy;
import mes.uif.generic.UIFServiceProxy;

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
public class ServerPatchReply implements ObjectExecuteService
{
	private static final transient Logger  log = LoggerFactory.getLogger(ServerPatchReply.class);
	
	public Object execute(Document recvDoc)
	{
		// Reply Code Confirm
		
		if ( false )
		{
			// ({0}) 서버에서 ({1}) 서버로의 이벤트 분기에 대한 이동에 실패하였습니다.
//			Object[] arg = { sourceServer, targetServer };
//			throw new CustomException("CM-006", arg);
		}
		
		GenericServiceProxy.getMessageEventMap().load();
		
		UIFServiceProxy.getTargetSubjectMap().reload();
		
		String replySubjectName = MessageUtil.selectText(recvDoc, "//message/header/replysubject", "");
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
