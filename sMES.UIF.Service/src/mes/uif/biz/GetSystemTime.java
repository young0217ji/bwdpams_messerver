package mes.uif.biz;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;

import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class GetSystemTime implements ObjectExecuteService
{
	private static final transient Logger logger = LoggerFactory.getLogger(GetSystemTime.class);
	
	public Object execute(Document recvDoc)
	{
		String timeKey = DateUtil.getCurrentEventTimeKey();
		
		//============================================================================
		//Message 파싱
		//============================================================================
		
		logger.info(">> Timekey : " + timeKey);
		logger.info(">> PlantID : " + Constant.PLANTID);
		
		// Element 만들기
		Element elementData = new Element("DATA");
		
		Element newCode = new Element("TIMEKEY");
		
		newCode.setText(timeKey);
		
		elementData.addContent(newCode);
		
		Element elementList = new Element("DATALIST");
		elementList.addContent(elementData);
		
		return elementList;
	}	
}
