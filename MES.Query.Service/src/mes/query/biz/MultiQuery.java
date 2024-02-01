package mes.query.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.exception.MESFrameException;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.util.SqlLogUtil;
import kr.co.mesframe.util.MessageUtil;
import mes.errorHandler.CustomException;
import mes.util.SendMessageUtil;

import org.jdom.Attribute;
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
public class MultiQuery implements ObjectExecuteService
{
	private static final transient Logger logger = LoggerFactory.getLogger(MultiQuery.class);

	/**
	 * @param Document
	 * 
	 * @return Document
	 * 
	 * @throws Exception
	 * 
	 * @description
	 * Document형을 Argument로 받아 HashMap형으로 Parse 후
	 * QUERYID 와 VERSION, QueryString의 Bind변수를 Argument 로 구성하여 Query 실행
	 * 실행 후의 결과값을 받아 XML로 구성하여 Return 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object execute(Document recvDoc)
	{
		Element body = recvDoc.getDocument().getRootElement().getChild("body");
		Document resultDocument;
		try {
			resultDocument = SendMessageUtil.createXmlDocument("MULTIQUERY" + " ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new MESFrameException(e);
		}
		
		for ( Iterator iterator = body.getChildren().iterator(); iterator.hasNext(); )
		{
			Element queryMember = (Element) iterator.next();
			
			if (queryMember.getName().equalsIgnoreCase("MULTIQUERY"))
			{
				//Get Query String
				String plantID = queryMember.getChildText("PLANTID");
				String queryID = queryMember.getChildText("QUERYID");
				String version = queryMember.getChildText("QUERYVERSION");
				
				Object[] args = new Object[] { plantID, queryID, version };
				
				String usrSql = "SELECT queryString FROM customQuery WHERE plantID = ? AND queryID = ? AND queryVersion = ? ";

				List resultList = null;
				resultList = SqlMesTemplate.queryForList(usrSql, args);

				if (resultList.size() == 0)
				{
					throw new CustomException("CanNotFoundQueryID", queryID, version);
				}

				LinkedCaseInsensitiveMap queryMap = (LinkedCaseInsensitiveMap) resultList.get(0);
				String dbSql = queryMap.get("queryString").toString();
				
				HashMap<String, Object> bindMap = new HashMap<String, Object>();
				
				Element elementMember = queryMember.getChild("BINDV");
				for ( Iterator iteratorSub = elementMember.getChildren().iterator(); iteratorSub.hasNext(); )
				{
					Element listMember = (Element) iteratorSub.next();
					bindMap.put(listMember.getName(), listMember.getText());
				}
				
				if (bindMap.isEmpty())
				{
					logger.info(">> SQL = [" + dbSql + "]");
				}
				else
				{
					logger.info(">> SQL = [" + SqlLogUtil.getLogFormatSqlStatement(dbSql, bindMap, logger) + "]");
				}
				
				resultList.clear();
				resultList = SqlMesTemplate.queryForList(dbSql, bindMap);
				
				String bindString;
				bindString = dbSql.replaceAll("(?i)from ", "FROM ");
				bindString = bindString.replaceAll(" (?i)as ", " AS ");
				bindString = bindString.substring(0, dbSql.indexOf("FROM "));
				bindString = bindString.replaceAll("(?i)select ", "");
				String[] bindValue = bindString.split(",");
				List bindList = new ArrayList();;
				for ( int i = 0; i < bindValue.length; i++ )
				{
					if ( bindValue[i].contains(" AS ") )
					{
						bindList.add( bindValue[i].substring(bindValue[i].indexOf(" AS ")+3, bindValue[i].length()).trim() ); 
					}
					else if ( bindValue[i].contains(".") )
					{
						bindList.add( bindValue[i].substring(bindValue[i].indexOf("."), bindValue[i].length()).trim() );
					}
					else if ( bindValue[i].trim().contains(" ") )
					{
						bindList.add( bindValue[i].substring(bindValue[i].trim().indexOf(" ") + 1, bindValue[i].length()).trim() );
					}
					else
					{
						bindList.add( bindValue[i].trim() );
					}
				}
				
				if (resultList == null || resultList.size() == 0)
				{
					logger.warn("RESULT DATA IS EMPTY. RETURN STRING[0][0]");
				}
				else 
				{
					try {
						resultDocument.getRootElement().getChild("body").addContent(createQueryElement(bindList, resultList));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						throw new MESFrameException(e);
					}
				}
			}
		}

		Element sourceSubjectName = MessageUtil.selectNode(recvDoc, "//message/header/sourcesubject");
		Element replySubjectName = MessageUtil.selectNode(recvDoc, "//message/header/replysubject");
		Element messageID = MessageUtil.selectNode(recvDoc, "//message/header/messageid");
		resultDocument.getRootElement().getChild("header").addContent((Content)sourceSubjectName.clone());
		resultDocument.getRootElement().getChild("header").addContent((Content)replySubjectName.clone());
		resultDocument.getRootElement().getChild("header").addContent((Content)messageID.clone());
		
		resultDocument.getRootElement().addContent(new Element("return"));
		resultDocument.getRootElement().getChild("return").addContent(new Element(SendMessageUtil.RESULT_RETURNCODE).setText("0"));
		resultDocument.getRootElement().getChild("return").addContent(new Element(SendMessageUtil.RESULT_ERRORMESSAGE).setText(""));
		
		logger.info(">> Create Message");
		logger.info("ReturnMessage: "+ MessageUtil.toString(resultDocument));

		return resultDocument;
	}
	
	/**
	 * @param List - resultList
	 * 
	 * @return Element
	 * 
	 * @throws Exception
	 * 
	 * @description Query 결과를 Element의 형식의 구조로 생성
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Element createQueryElement(List bindList, List resultList) throws Exception
	{
		Element element = new Element("DATALIST");

		Element structNode = null;
		structNode = new Element("DATASTRUCTURE");
		
		for (int i = 0; i < bindList.size(); i++)
		{
			String bindMap = (String) bindList.get(i);

			Element Vnode = null;
			Vnode = new Element(bindMap.toUpperCase());
			Vnode.setText("");

			structNode.addContent(Vnode);
		}
		
		element.addContent(structNode);
		
		Element Snode = null;
		
		for (int i = 0; i < resultList.size(); i++)
		{
			LinkedCaseInsensitiveMap orderMap = (LinkedCaseInsensitiveMap) resultList.get(i);
			Iterator map = orderMap.entrySet().iterator();

			Snode = new Element("DATA");
			List attrList = new ArrayList();
			
			while (map.hasNext())
			{
				Entry entry = (Entry) map.next();
				String Key = (String) entry.getKey();
				String value = null;

				if (entry.getValue() == null) {
					value = "";
				} else if (entry.getValue() instanceof String)
					value = (String) entry.getValue();
				else
					value = String.valueOf(entry.getValue());

				Attribute attr = new Attribute(Key, value);
				attrList.add(attr);
			}
			Snode.setAttributes(attrList);
			element.addContent(Snode);
		}

		return element;
	}
}