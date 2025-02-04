package mes.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.util.MessageUtil;
import kr.co.mesframe.util.StringUtil;
import kr.co.mesframe.util.object.ObjectCloner;
import mes.event.TargetPlantMap;
import mes.generic.GenericServiceProxy;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class SendMessageUtil {

	private static final transient Logger logger = LoggerFactory.getLogger(SendMessageUtil.class);
	
	public static final String 							MESSAGE_TAG 				= "message";
	public static final String 							MESSAGENAME_TAG 			= "messagename";
	public static final String 							HEADER_TAG 					= "header";
	public static final String 							BODY_TAG 					= "body";
	public static final String 							RESULT_NAME_TAG				= "return";	        
	public static final String							RESULT_RETURNCODE			= "returncode";
	public static final String							RESULT_ERRORMESSAGE			= "returnmessage";
	public static final String							RESULT_ERRORDETAILMESSAGE	= "returndetailmessage";
	
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
	 * LinkedCaseInsensitiveMap를 받아 Element를 만드는 Method
	 * 
	 * @param resultList
	 * @return Element
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Element createElementFromList(List resultList)
	{
		Element elementList = new Element("DATALIST");
		
        if( resultList == null || resultList.size() == 0 )
        {
            logger.warn("RESULT DATA IS EMPTY. RETURN STRING[0][0]");
        }
        else
        {
            for( int i = 0; i < resultList.size(); i++ )
            {
                LinkedCaseInsensitiveMap orderMap = (LinkedCaseInsensitiveMap) resultList.get(i);

                Element elementData = new Element("DATA");

                for ( Iterator iterator = orderMap.entrySet().iterator(); iterator.hasNext(); )
    			{
    				Map.Entry<String, Object> iterMap =  (Map.Entry<String, Object>) iterator.next();
    				
    				String Key = iterMap.getKey();
                    String value = null;

                    if( iterMap.getValue() == null )
                    {
                        value = "";
                    }
                    else if( iterMap.getValue() instanceof String )
                    {
                    	value = (String) iterMap.getValue();
                    }
                    else
                    {
                        value = String.valueOf(iterMap.getValue());
                    }

                    Element Vnode = null;

                    Vnode = new Element(Key);
                    Vnode.setText(value);

                    elementData.addContent(Vnode);
    			}

                elementList.addContent(elementData);
            }
        }
        
        return elementList;
	}
	
	/**
	 * 
	 * hashMap을 받아 Element를 만드는 Method
	 * 
	 * @param hashMap
	 * @return Element
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Element createElementFromHashMap(HashMap hashMap)
	{
		Element dataElement = new Element("DATA");
		
		for ( Iterator iterator = hashMap.entrySet().iterator(); iterator.hasNext(); )
		{
			Map.Entry<String, String> iterMap =  (Map.Entry<String, String>) iterator.next();
			
			String key = iterMap.getKey();
			String value = iterMap.getValue();

			Element node = new Element(key);
			node.setText(value);

			dataElement.addContent(node);
		}
		
        return dataElement;
	}
	
	/**
	 * 
	 * hashMap을 받아 DataSet Element를 만드는 Method
	 * 
	 * @param hashMap
	 * @param dataSetId
	 * @return Element
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Element createDataListElementFromHashMap(HashMap hashMap, String dataSetId)
	{
		Element dataListElement = new Element("DATALIST");
		
		dataListElement.setAttribute("ID", dataSetId);
		
		for ( Iterator iterator = new TreeMap(hashMap).entrySet().iterator(); iterator.hasNext(); )
		{
			Map.Entry<String, HashMap<String, Object>> iterMap =
								(Map.Entry<String, HashMap<String, Object>>) iterator.next();
			
			HashMap<String, Object> tempMap = iterMap.getValue();
			
			Element dataElement = new Element("DATA");
			
			for ( Iterator subIterator = tempMap.entrySet().iterator(); subIterator.hasNext(); )
			{
				Map.Entry<String, Object> subIterMap =  (Map.Entry<String, Object>) subIterator.next();
			
				String key = subIterMap.getKey();
				String value = "";
				
				Object tmpObject = subIterMap.getValue();
				
				if (tmpObject != null)
				{
					value = tmpObject.toString();
				}

				Element node = new Element(key);
				node.setText(value);

				dataElement.addContent(node);
			}
			
			dataListElement.addContent(dataElement);
		}
		
        return dataListElement;
	}
	
	/**
	 * 
	 * hashMap을 받아 DataSet Element를 만드는 Method
	 * Data list Node Name Data Node Name을 Argument로 입력받음
	 * 
	 * @param hashMap
	 * @param listId
	 * @param dataId
	 * @return Element
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Element createDataListElementFromHashMap_UserDefine(HashMap hashMap, String listId, String dataId)
	{
		Element dataListElement = new Element(listId);
		
		//dataListElement.setAttribute("ID", dataId);
		
		for ( Iterator iterator = new TreeMap(hashMap).entrySet().iterator(); iterator.hasNext(); )
		{
			Map.Entry<String, HashMap<String, Object>> iterMap =
								(Map.Entry<String, HashMap<String, Object>>) iterator.next();
			
			HashMap<String, Object> tempMap = iterMap.getValue();
			
			Element dataElement = new Element(dataId);
			
			for ( Iterator subIterator = tempMap.entrySet().iterator(); subIterator.hasNext(); )
			{
				Map.Entry<String, Object> subIterMap =  (Map.Entry<String, Object>) subIterator.next();
			
				String key = subIterMap.getKey();
				String value = "";
				
				Object tmpObject = subIterMap.getValue();
				
				if (tmpObject != null)
				{
					value = tmpObject.toString();
				}

				Element node = new Element(key);
				node.setText(value);

				dataElement.addContent(node);
			}
			
			dataListElement.addContent(dataElement);
		}
		
        return dataListElement;
	}
	/**
	 * 
	 * String형태의 receivedData를 xmlDocument로 변환하여 리턴합니다
	 *
	 * @param receivedData
	 * @return Document
	 *
	 */
	@SuppressWarnings("unchecked")
	public static Document createXmlDocument(String receivedData)
	{
		Element message = new Element( MESSAGE_TAG );                                                                                                            
		Document document = new Document( message );    

		
		int idx = receivedData.indexOf(" ");
		String commandName = receivedData.substring(0, idx).trim();
		HashMap<String, String> messageMap = null;
		messageMap = parsingStringMessage(receivedData);
		
		Element header = new Element( HEADER_TAG );       	
		
//		Element subElement = new Element( BPELName_Tag );    
//		subElement.setText(commandName);
//		header.addContent(subElement);
		
		Element subElement = new Element( MESSAGENAME_TAG );    
		subElement.setText(commandName);
		header.addContent(subElement);
		message.addContent(header);
		Element body = new Element( BODY_TAG );		                                                                                                               
		Element ele = null;
		while (messageMap.keySet().iterator().hasNext())
		{
			String keyName = messageMap.keySet().iterator().next();
			String keyValue = messageMap.remove(keyName);
			ele = new Element( keyName );                                                                                                               
			ele.setText( keyValue );                                                                                                                            
			body.addContent( ele );       			
		}
		message.addContent(body);
		messageMap.clear();
//		logger.debug(JdomUtils.toString(document));
		return document;
	}
	
	/**
	 * 
	 * Hash Table을 1차 Depth의 Xml Message만들어 주는 함수
	 *
	 * @param eventName
	 * @param sourceSubject
	 * @param targetSubject
	 * @param resultList
	 * @return Document
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Document createHashMapXmlDepthOne(String eventName, String sourceSubject, String targetSubject,
													HashMap<String,String> resultList)
	{
		Document resultDocument = null;
		resultDocument = SendMessageUtil.createXmlDocument(eventName + " ");
		
		Element sourceE = null;
		sourceE = new Element("sourcesubject");
		sourceE.setText(sourceSubject);
		
		Element targetE = null;
		targetE = new Element("targetsubject");
		targetE.setText(targetSubject);
		
		//추가
		Element replyE = null;
		replyE = new Element("replysubject");
		replyE.setText("");
		
		resultDocument.getRootElement().getChild("header").addContent((Content)sourceE.clone());
		resultDocument.getRootElement().getChild("header").addContent((Content)targetE.clone());
		//추가
		resultDocument.getRootElement().getChild("header").addContent((Content)replyE.clone());

//		String XPathBody = "//" + SMessageUtil.MESSAGE_TAG + "/"
//				+ SMessageUtil.BODY_TAG;

		if (resultList == null || resultList.size() == 0)
		{
			//logger.warn("RESULT DATA IS EMPTY. RETURN STRING[0][0]");
		}
		else
		{
			//JdomUtils.addElement(resultDocument, XPathBody, "DATALIST", "");
			
			for ( Iterator iterator = resultList.entrySet().iterator(); iterator.hasNext(); )
			{
				Map.Entry<String, String> iterMap =  (Map.Entry<String, String>) iterator.next();
				
				String Key = iterMap.getKey();
				String value = iterMap.getValue();
				
				Element Snode = null;
				Snode = new Element(Key);
				Snode.setText(value);

				resultDocument.getRootElement().getChild(SendMessageUtil.BODY_TAG).addContent(Snode);
			}
		}
		
		return resultDocument;
	}
	
	/**
	 * 
	 * 입력받은 listInHashMap을 파싱하여 Element를 생성합니다
	 *
	 * @param elementName
	 * @param listInHashMap
	 * @return Element
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Element createElementToList(String elementName, List listInHashMap)
	{
		Element listElement = new Element("List");
		
		for( int i = 0; i < listInHashMap.size(); i++ )
        {
            Element nodeElement = new Element(elementName);
			LinkedCaseInsensitiveMap orderMap = (LinkedCaseInsensitiveMap) listInHashMap.get(i);

            for ( Iterator iterator = orderMap.entrySet().iterator(); iterator.hasNext(); )
			{
				Map.Entry<String, Object> iterMap =  (Map.Entry<String, Object>) iterator.next();
				
				String Key = iterMap.getKey();
                String value = null;

                if( iterMap.getValue() == null )
                {
                    value = "";
                }
                else if( iterMap.getValue() instanceof String )
                {
                	value = (String) iterMap.getValue();
                }
                else
                {
                    value = String.valueOf(iterMap.getValue());
                }

                Element element = new Element(Key.toUpperCase());
                element.setText(value);
				
				nodeElement.addContent(element);
			}
            
			listElement.addContent(nodeElement);
		}
        
        return listElement;
	}
	
	public static Document createDocument(String messageName, Element bodyElement)
	{
		return createDocument(messageName, bodyElement, "");
	}
	
	public static Document createDocument(String messageName, Element bodyElement, String transactionID)
	{
		Element message = new Element( MESSAGE_TAG );                                                                                                            
		Document document = new Document( message );    

		Element header = new Element( HEADER_TAG );       	
		
		Element messageElement = new Element( MESSAGENAME_TAG );
		messageElement.setText(messageName);
		Element sourceSubject = new Element( "sourcesubject" );
		sourceSubject.setText("" + System.getProperty("mode") + ".KR." + System.getProperty("group") + ".MES." + System.getProperty("svr"));
		Element replySubject = new Element( "replysubject" );
		
		Element transactionElement = new Element( "replysubject" );
		if ( transactionID != null && transactionID != "" )
		{
			transactionElement.setText(transactionID);
		}
		
		header.addContent(messageElement);
		header.addContent(sourceSubject);
		header.addContent(replySubject);
		header.addContent(transactionID);
		
		message.addContent(header);
		message.addContent(bodyElement);

		return document;
	}
	
	public static Document createXmlDocument(String messageName, Element bodyElement, String toSubject)
	{
		Element message = new Element( MESSAGE_TAG );                                                                                                            
		Document document = new Document( message );    

		Element header = new Element( HEADER_TAG );       	
		
		Element messageElement = new Element( MESSAGENAME_TAG );
		messageElement.setText(messageName);
		Element sourceSubject = new Element( "sourcesubject" );
		sourceSubject.setText("" + System.getProperty("mode") + ".KR." + System.getProperty("group") + ".MES." + System.getProperty("svr"));
		Element targetSubject = new Element( "targetsubject" );
		targetSubject.setText(toSubject);
		
		header.addContent(messageElement);
		header.addContent(sourceSubject);
		header.addContent(targetSubject);
		
		message.addContent(header);
		message.addContent(bodyElement);

		return document;
	}
	
	/**
	 * 
	 * Document의 내용을 subject로 ByPass하여 전달하는 Method
	 * 
	 * @param Document
	 * @param String - subject
	 * 
	 */
	public static void send(String destination, Document recvDoc)
	{
		try
		{
			TargetPlantMap map = GenericServiceProxy.getTargetPlantMap();
			
			Document sendDoc = (Document) ObjectCloner.deepCopy(recvDoc);
			
			String sourceSubject = sendDoc.getDocument().getRootElement().getChild("header").getChildText("sourcesubject");
			
			// Subject 구성
			// ${mode}.KR.${group}.MES.${svr}
			if ( destination != null && destination.contains("EIS") )
			{
				destination = "" + System.getProperty("mode") + ".KR." + System.getProperty("group") + "." + destination;
			}
			else if ( destination != null && ( destination.contains("FSsvr") || destination.contains("EMsvr") || destination.contains("MDLsvr") ) )
			{
				String eventName = sendDoc.getDocument().getRootElement().getChild("header").getChildText("messagename");
				String plantID = map.getSubjectName(eventName);
				
				if ( plantID != null && !plantID.isEmpty() )
				{
					destination = "" + System.getProperty("mode") + ".KR." + plantID + ".MES." + destination;
				}
				else
				{
					destination = "" + System.getProperty("mode") + ".KR." + System.getProperty("group") + ".MES." + destination;
				}
			}
			else
			{
				destination = "" + System.getProperty("mode") + ".KR." + System.getProperty("group") + ".MES." + destination;
			}
			
        	logger.debug(" > SendReply Ready Step 1 : " + destination);
        	MessageUtil.setChildText(sendDoc, "//message/header", "targetsubject", destination);
        	
        	logger.debug(" > SendReply Ready Step 2 ");
        	String replyMsg = null;

        	logger.debug(" > SendReply Ready Step 3 ");

        	logger.info("-------------------------------- Send " + destination);
        	replyMsg = MessageUtil.toString(sendDoc);
        	logger.debug("Create Message ( Document => String ) ");
        	if ( destination != null && destination.contains("EIS") )
			{
        		MESFrameProxy.getMessageService().send(destination, replyMsg, 30.0);
			}
        	else
        	{
            	// MESFrameProxy.getMessageService().send(destination, replyMsg);
            	// ActiveMQ의 경우 단일 Topic 사용으로 로직 변경
            	MESFrameProxy.getMessageService().send(destination, destination, sourceSubject, replyMsg);
        	}
        	logger.info("-------------------------------- Send Complete " + destination);
        	logger.debug("SERP :: " + replyMsg);
		}
	    catch( Exception e )
	    {
	        logger.error("Reply Send Error", e);
	    }
	}
	
	@SuppressWarnings("rawtypes")
	private static HashMap parsingStringMessage(String msg)
	{
		String delimeter = "[{X*X}]";  //  
		
		String msg2 = msg;
		while(true)
		{
			int idx1 = msg2.indexOf("=[");
			int idx2 = msg2.indexOf("]", idx1);
			if (idx1 > 0 && idx2 > 0)
			{
				String a = msg2.substring(idx1+2, idx2);
				if (a.length() == 0)
				{
					msg2 = msg2.substring(idx2+1, msg2.length()); continue;
				}
				String b = org.springframework.util.StringUtils.replace(a, "=", "($%^)");
				msg = org.springframework.util.StringUtils.replace(msg, a, b);				
				msg2 = msg2.substring(idx2, msg2.length());
			}
			else break;
		}
		
		String [] messageSplit = msg.split("=");
		HashMap<String, String> params = new HashMap<String, String>();
		StringBuffer keyNameBuffer = new StringBuffer();
		keyNameBuffer.append("Command").append(delimeter);
		StringBuffer valueBuffer = new StringBuffer();
		for (int i=0; i<messageSplit.length; i++)
		{
			if (getKeyName(messageSplit[i]).length() > 0) {
				keyNameBuffer.append(getKeyName(messageSplit[i])).append(delimeter);
				valueBuffer.append(getValue(messageSplit[i])).append(delimeter);
			}
			else {
				valueBuffer.append(messageSplit[i]).append(delimeter);
			}
		}

		String[] keyNames = org.springframework.util.StringUtils.delimitedListToStringArray(keyNameBuffer.toString(), delimeter);
		String[] keyvalues = org.springframework.util.StringUtils.delimitedListToStringArray(valueBuffer.toString(), delimeter);
		
		//if (keyNames.length == (keyvalues.length+1))
			
			
		for (int i=1; i<keyNames.length; i++)
		{
			if (keyNames[i] == null || keyNames[i].trim().length() == 0) continue;
			try {
				keyvalues[i] = keyvalues[i].trim();
				keyvalues[i] = org.springframework.util.StringUtils.replace(keyvalues[i], "($%^)", "=");
				if (keyvalues[i].startsWith("[") && keyvalues[i].endsWith("]"))
					keyvalues[i] = keyvalues[i].substring(1, keyvalues[i].length()-1);
				params.put(keyNames[i], keyvalues[i].trim());
			} catch (Exception e) {
				params.put(keyNames[i], "");
				logger.debug("Value of last data is empty");
			}
		}
		return params;
	}
	
	private static String getKeyName(String partialMsg)
	{
		if (partialMsg.startsWith("[") && partialMsg.endsWith("]"))
			return "";
		String character = "";
		String value = "";
		for (int i=partialMsg.length(); i>0; i--)
		{
			character = partialMsg. substring(i-1, i);
			if (character.equalsIgnoreCase(" "))
			{
				return StringUtil.reverse(value);
			}
			else {
				value += character;
			}
		}
		return "";
	}

	private static String getValue(String partialMsg)
	{
		partialMsg = StringUtil.removeEnd(partialMsg, getKeyName(partialMsg));
//		try {
//			if (partialMsg.trim().length() == 0)
//				return "";
//		} catch (Exception e) {}
		return partialMsg;
	}
	
}
