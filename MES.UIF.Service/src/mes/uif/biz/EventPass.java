package mes.uif.biz;

import kr.co.mesframe.esb.ObjectExecuteService;
import mes.errorHandler.CustomException;
import mes.uif.generic.UIFServiceProxy;
import mes.util.SendMessageUtil;

import org.jdom.Document;
import org.jdom.Element;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class EventPass implements ObjectExecuteService
{
	public Object execute(Document recvDoc)
	{
		TargetSubjectMap map = UIFServiceProxy.getTargetSubjectMap();

		String eventName = recvDoc.getDocument().getRootElement().getChild("header").getChildText("messagename");
		String sendSubjectName = map.getSubjectName(eventName);

		if ( sendSubjectName == null || sendSubjectName.isEmpty() )
		{
			throw new CustomException("CM-005", new Object[] {eventName});
		}

		
//		Timestamp currentTime = TimeStampUtil.getCurrentTimestamp();
//		Timestamp targetTime = TimeStampUtil.getTimestamp("20110801000001");
//		
//		if ( ( targetTime.getTime() / 1000 - currentTime.getTime() / 1000 ) > 0 )
//		{
//			log.info( "서버 구동까지 다음 시간이 남았습니다. : " + ( targetTime.getTime() / 1000 - currentTime.getTime() / 1000 ));
//			
//			JdomUtils.setNodeText(recvDoc, "//message/header/messagename", 
//					JdomUtils.getNodeText(recvDoc, "//message/header/messagename") + "Reply");
//			
//			return recvDoc;
//		}
//		else
		{
			SendMessageUtil.send(sendSubjectName, recvDoc);
			
			if ( "PackingEquipmentOrder".equalsIgnoreCase(eventName) )
			{
				// Element 만들기
				Element elementData = new Element("DATA");
				Element elementList = new Element("DATALIST");
				elementList.addContent(elementData);
				
				return elementList;
			}
			
			return null;
		}
	}

}
