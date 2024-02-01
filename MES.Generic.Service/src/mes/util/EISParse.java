package mes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class EISParse 
{
	/**
	 * 
	 * doc의 depth가 1,2,3인 노드를 HashMap형태로 변환하여 리턴합니다
	 *
	 * @param doc
	 * @return HashMap<String, Object>
	 *
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> getEISXmlParse(Document doc)
	{
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		
		Element root = doc.getDocument().getRootElement();
		Element element = root.getChild("body");
		
		// Body 의 Depth No.1 - Body 하위
		for ( Iterator iterator = element.getChildren().iterator(); iterator.hasNext(); )
		{
			// Body 하위 Element
			Element oneMember = (Element) iterator.next();
			
			if ( oneMember.getChildren().size() > 0 )
			{
				ArrayList<HashMap> twoList;
				twoList = new ArrayList<HashMap>();
				// Body 의 Depth No.2 - DataList
				for ( Iterator iteratorList = oneMember.getChildren().iterator(); iteratorList.hasNext(); )
				{
					// List 하위 Element
					Element twoMember = (Element) iteratorList.next();
					
					if ( twoMember.getChildren().size() > 0 )
					{
						HashMap<String, Object> threeHashMap = new HashMap<String, Object>();
						
						// Body 의 Depth No.3 - DataInfo
						for ( Iterator iteratorSub = twoMember.getChildren().iterator(); iteratorSub.hasNext(); )
						{
							// Info 하위 Element
							Element threeMember = (Element) iteratorSub.next();
							
							if ( threeMember.getChildren().size() > 0 )
							{
								// Depth 추가시 Logic 부분
							}
							else
							{
								threeHashMap.put(threeMember.getName(), threeMember.getText());
							}
						}
						twoList.add(threeHashMap);
						
					}
					else
					{
						//twoList.add(twoMember.getText());
					}
				}
				resultHashMap.put(oneMember.getName().toUpperCase(), twoList);
				
			}
			else
			{
				resultHashMap.put(oneMember.getName().toUpperCase(), oneMember.getText());
			}
		}
		
		return resultHashMap;
	}
}

