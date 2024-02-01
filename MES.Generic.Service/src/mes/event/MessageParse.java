package mes.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class MessageParse
{
    /**
     * 
     * doc의 body->BINDV에서 keyValue에 해당하는 값을 찾아 리턴합니다
     * 
     * @param doc
     * @param keyValue
     * @return String
     * 
     */
    public static String getBindParam(Document doc, String keyValue)
    {
        String paramValue = doc.getDocument().getRootElement().getChild("body").getChild("BINDV").getChildText(keyValue);

        return paramValue;
    }

    /**
     * 
     * doc의 body에서 keyValue에 해당하는 값을 찾아 리턴합니다
     * 
     * @param doc
     * @param keyValue
     * @return String
     * 
     */
    public static String getParam(Document doc, String keyValue)
    {
        String paramValue = doc.getDocument().getRootElement().getChild("body").getChildText(keyValue);

        return paramValue;
    }
	
	/**
	 * 
	 * doc의 body부분을 dataSetName을 "DATASET"으로 하여 HashMap형태로 만들어 리턴합니다
	 * 
	 * @param doc
	 * @return HashMap<String, String>
	 * @throws FrameworkErrorSignal, NotFoundSignal, Exception
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap getDataSetXml(Document doc)
	{
		return getDataSetXml(doc, "DATASET");
	}
	
	/**
	 * 
	 * doc의 body부분을 dataSetName을 key로 하여 HashMap형태로 만들어 리턴합니다
	 * 
	 * @param doc
	 * @param dataSetName
	 * @return HashMap<String, String>
	 * @throws FrameworkErrorSignal, NotFoundSignal, Exception
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap getDataSetXml(Document doc, String dataSetName)
	{
		HashMap hashMapXml = new HashMap();
		
		Element root = doc.getDocument().getRootElement();
		Element element = root.getChild("body");
		
		for ( Iterator iterator = element.getChildren().iterator(); iterator.hasNext(); )
		{
			Element elementMember = (Element) iterator.next();
			
			if ( elementMember.getName().indexOf("DATALIST") > -1 && elementMember.getAttributeValue("ID").equalsIgnoreCase(dataSetName) )
			{
				List<HashMap> dataList = new ArrayList<HashMap>();
				// DATA 횟수 만큼
				for ( Iterator iteratorList = elementMember.getChildren().iterator(); iteratorList.hasNext(); )
				{
					Element elementList = (Element) iteratorList.next();
					// dataListMap에 담기위한 SubMap
					HashMap<String, String> unitData = new HashMap<String, String>();
					
					for ( Iterator iteratorData = elementList.getChildren().iterator(); iteratorData.hasNext(); )
					{
						// DATA Element
						Element elementData = (Element) iteratorData.next();
						
						unitData.put(elementData.getName().toUpperCase(), elementData.getText());
							
					}
					
					dataList.add(unitData);
				}
				
				hashMapXml.put(dataSetName, dataList);
				break;
			}
		}
		
		return hashMapXml;
	}
	
	/**
	 * 
	 * Document형을 Argument로 받아 HashMap형으로 Parse 후 Return
	 * 1차 HashMap 구조로 body 의 1단계 Child만 갖은 경우 HashMap으로 Parse하고
	 * 2단계 Child가 AttributeValue List 구조로 구성되어 있을 경우 ','를 구분자로 하여 한 String으로 구성
	 * 3단계 Child는 Parse 안됨
	 * 
	 * @param doc
	 * @return HashMap<String, String>
	 * @throws FrameworkErrorSignal, NotFoundSignal, Exception
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<String, String> getHashXmlParse(Document doc)
	{
		HashMap<String, String> hashMapXml = new HashMap<String, String>();
		
		Element root = doc.getDocument().getRootElement();
		Element element = root.getChild("body");
		
		for ( Iterator iterator = element.getChildren().iterator(); iterator.hasNext(); )
		{
			Element elementMember = (Element) iterator.next();
			
			if (elementMember.getName().indexOf("LIST") == -1)
			{
				hashMapXml.put(elementMember.getName().toUpperCase(), elementMember.getText());
			}
			else
			{
				String elementName = elementMember.getName();
				Element listElement = element.getChild(elementName);
				String member = null;
				
				if (listElement.getContentSize() > 0)
				{
					for ( Iterator iteratorSub = listElement.getChildren().iterator(); iteratorSub.hasNext(); )
					{
						Element listMember = (Element) iteratorSub.next();
						String tempValue = listMember.getAttributeValue(elementName.substring(0, elementName.length()-4));
						if (member != null)
						{
							member += ("," + tempValue);
						}
						else
						{
							member = tempValue;
						}
					}
				}
				
				hashMapXml.put(elementName.toUpperCase(), member);
			}
		}
		return hashMapXml;
	}
	
	/**
	 * 
	 * 이중 List 구조로 이루어진 XML에 대한 Parsing
	 * 
	 * @param doc
	 * @return HashMap<String, Object>
	 * @throws FrameworkErrorSignal, NotFoundSignal, Exception
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> getDoubleXmlParse(Document doc)
	{
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		
		Element root = doc.getDocument().getRootElement();
		Element element = root.getChild("body");
		
		// Body 의 Depth No.1 - Body 하위 Element 
		for ( Iterator iterator = element.getChildren().iterator(); iterator.hasNext(); )
		{
			// Body 하위 Element
			Element oneMember = (Element) iterator.next();
			
			if ( oneMember.getName().indexOf("LIST") > -1 )
			{
				// List 구조의 경우
				List<HashMap> oneList = new ArrayList<HashMap>();
				
				// List 의 DataInfo 횟수 만큼
				for ( Iterator iteratorOneList = oneMember.getChildren().iterator(); iteratorOneList.hasNext(); )
				{
					Element twoMember = (Element) iteratorOneList.next();
					
					// DataInfo 의 ElementMap
					HashMap<String, Object> oneDataMap = new HashMap<String, Object>();
					
					for ( Iterator iteratorOneData = twoMember.getChildren().iterator(); iteratorOneData.hasNext(); )
					{
						// DATA Element
						Element threeMember = (Element) iteratorOneData.next();
						
						
						// 이중 List 구조
						if ( threeMember.getName().indexOf("LIST") > -1 )
						{
							// List 구조의 경우
							List<HashMap> twoList = new ArrayList<HashMap>();
							
							// List 의 DataInfo 횟수 만큼
							for ( Iterator iteratorTwoList = threeMember.getChildren().iterator(); iteratorTwoList.hasNext(); )
							{
								Element fourMember = (Element) iteratorTwoList.next();
								
								// DataInfo 의 ElementMap
								HashMap<String, String> twoDataMap = new HashMap<String, String>();
								
								for ( Iterator iteratorTwoData = fourMember.getChildren().iterator(); iteratorTwoData.hasNext(); )
								{
									// DATA Element
									Element fiveMember = (Element) iteratorTwoData.next();
									
									twoDataMap.put(fiveMember.getName().toUpperCase(), fiveMember.getText());
								}
								
								twoList.add(twoDataMap);
							}
							
							oneDataMap.put(threeMember.getName().toUpperCase(), twoList);
						}
						else
						{
							// Map Value 값인 경우
							oneDataMap.put(threeMember.getName().toUpperCase(), threeMember.getText());
						}
					}
					
					oneList.add(oneDataMap);
				}
				
				resultHashMap.put(oneMember.getName().toUpperCase(), oneList);
			}
			else
			{
				// Map Value 값인 경우
				resultHashMap.put(oneMember.getName().toUpperCase(), oneMember.getText());
			}
		}
		
		return resultHashMap;
	}
	
	/**
	 * 
	 * Document형을 Argument로 받아 HashMap형으로 Parse 후 Return
	 * 
	 * @param doc
	 * @return HashMap<String, HashMap<String, String>>
	 * @throws FrameworkErrorSignal, NotFoundSignal, Exception
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> getXmlParse(Document doc)
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
				HashMap<String, Object> twoHashMap = new HashMap<String, Object>();
	
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
								HashMap<String, Object> fourHashMap = new HashMap<String, Object>();
								
								// Body 의 Depth No.4 - ResultList
								for ( Iterator iteratorFour = threeMember.getChildren().iterator(); iteratorFour.hasNext(); )
								{
									// ResultList 하위 Element
									Element fourMember = (Element) iteratorFour.next();
									
									if ( fourMember.getChildren().size() > 0 )
									{
										HashMap<String, Object> fiveHashMap = new HashMap<String, Object>();
										
										// Body 의 Depth No.5 - ResultInfo
										for ( Iterator iteratorFive = fourMember.getChildren().iterator(); iteratorFive.hasNext(); )
										{
											// ResultInfo 하위 Element
											Element fiveMember = (Element) iteratorFour.next();
											
											if ( fiveMember.getChildren().size() > 0 )
											{
												// Depth 추가시 Logic 부분
											}
											else
											{
												fiveHashMap.put(fiveMember.getName(), fiveMember.getText());
											}
										}
										fourHashMap.put(fourMember.getName().toUpperCase(), fiveHashMap);
									}
									else
									{
										fourHashMap.put(fourMember.getName(), fourMember.getText());
									}
								}
								threeHashMap.put(threeMember.getName().toUpperCase(), fourHashMap);
								
							}
							else
							{
								threeHashMap.put(threeMember.getName(), threeMember.getText());
							}
						}
						twoHashMap.put(twoMember.getName().toUpperCase(), threeHashMap);
						
					}
					else
					{
						twoHashMap.put(twoMember.getName().toUpperCase(), twoMember.getText());
					}
				}
				resultHashMap.put(oneMember.getName().toUpperCase(), twoHashMap);
				
			}
			else
			{
				resultHashMap.put(oneMember.getName().toUpperCase(), oneMember.getText());
			}
		}
		
		return resultHashMap;
	}
	
	/**
	 * 
	 * Element형을 Argument로 받아 HashMap형으로 Parse 후 Return
     * <String,String> Base의 가장 기본단위가 되는 HashMap을 반환
     * 
	 * @param doc
	 * @return HashMap<String, HashMap<String, String>>
	 * @throws FrameworkErrorSignal, NotFoundSignal, Exception
	 * 
     */
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> getDataSetXmlDefault(Document doc)
	{
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		
		HashMap<String, String> paraMap = new HashMap<String, String>();
		HashMap<String, String> bindParam = new HashMap<String, String>();
		
		Element root = doc.getDocument().getRootElement();
		Element element = root.getChild("body");
		
		for ( Iterator iterator = element.getChildren().iterator(); iterator.hasNext(); )
		{
			Element listMember = (Element) iterator.next();
			
			if ( listMember.getName().indexOf("DATALIST") > -1 )
			{	
				ArrayList<Object> dataList = new ArrayList<Object>();
				String listKey = listMember.getAttributeValue("ID");
				// DATA 횟수 만큼
				for ( Iterator iterList = listMember.getChildren().iterator(); iterList.hasNext(); )
				{
					Element dataElement = (Element) iterList.next();
					
					// dataListMap에 담기위한 SubList
					HashMap<String, String> unitData = new HashMap<String, String>();
					
					for ( Iterator iteratorList = dataElement.getChildren().iterator(); iteratorList.hasNext(); )
					{
						// DATA Element
						Element dataMember = (Element) iteratorList.next();
						
						unitData.put(dataMember.getName().toUpperCase(), dataMember.getText());
							
					}
					
					dataList.add(unitData);
				}
				
				// Key - Object[]로 저장
				resultHashMap.put(listKey, dataList);
				
			}
			else if ( listMember.getName().indexOf("BINDV") > -1 )
			{
				Element listElement = element.getChild("BINDV");
				
				for ( Iterator iteratorList = listElement.getChildren().iterator(); iteratorList.hasNext(); )
				{
					// DATA Element
					Element dataMember = (Element) iteratorList.next();
					
					bindParam.put(dataMember.getName().toUpperCase(), dataMember.getText());
				}
				
				resultHashMap.put("BINDV",  bindParam);
				
			}
			else
			{
				paraMap.put(listMember.getName(), listMember.getText());				
			}
		}
		
		if (paraMap.size() > 0 )
			resultHashMap.put("PARAMS", paraMap);
		
		return resultHashMap;
	}
	
	/**
	 * 
	 * Document형을 Argument로 받아 HashMap형으로 Parse 후 Return
	 * 1차 HashMap 구조로 body 의 1단계 Child만 갖은 경우 HashMap으로 Parse
	 * 
	 * @param doc
	 * @return List<HashMap<String, Object>>
	 * @throws FrameworkErrorSignal, NotFoundSignal, Exception
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static List<HashMap<String, String>> getDefaultXmlParse(Document doc)
	{
		Element root = doc.getDocument().getRootElement();
		Element element = root.getChild("body");
		
		// List 구조의 경우
		List<HashMap<String, String>> oneList = new ArrayList<HashMap<String, String>>();
		
		for ( Iterator iterator = element.getChildren().iterator(); iterator.hasNext(); )
		{
			Element oneMember = (Element) iterator.next();
			
			if ( oneMember.getName().indexOf("LIST") > -1 )
			{
				// List 의 DataInfo 횟수 만큼
				for ( Iterator iteratorOneList = oneMember.getChildren().iterator(); iteratorOneList.hasNext(); )
				{
					Element twoMember = (Element) iteratorOneList.next();
					
					// DataInfo 의 ElementMap
					HashMap<String, String> oneDataMap = new HashMap<String, String>();
					
					for ( Iterator iteratorOneData = twoMember.getChildren().iterator(); iteratorOneData.hasNext(); )
					{
						// DATA Element
						Element threeMember = (Element) iteratorOneData.next();
						oneDataMap.put(threeMember.getName().toUpperCase(), threeMember.getText());
					}
					
					oneList.add(oneDataMap);
				}
				
				break;
			}
		}
		
		return oneList;
	}
}

