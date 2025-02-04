package mes.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.MessageUtil;

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
public class MessageAdaptor
{
	private static final transient Logger logger = LoggerFactory.getLogger(MessageAdaptor.class);
	
    /**
     * 
     * Document의 내용의 Body를 다시 Setting하는 Method
     * 
     * @param Document
     * @param Element
     * @return Document
     * @throws Exception
     * 
     */
    public Document createSendBody(Document doc, Element element) throws Exception
    {
    	MessageUtil.setNodeText(doc, "//message/header/messagename", MessageUtil.selectText(doc, "//message/header/messagename", "") + "Reply");
    	
        if( doc.getDocument().getRootElement().removeChild("body") )
        {
        	Element body = new Element("body");
            body.addContent(element);

            doc.getDocument().getRootElement().addContent(body);
            
            Element returnElement = new Element("return");
            MessageUtil.addElement(returnElement, "returncode", "0");
            MessageUtil.addElement(returnElement, "returntype", "");
            MessageUtil.addElement(returnElement, "returnmessage", "");
            MessageUtil.addElement(returnElement, "returndetailmessage", "");

            doc.getDocument().getRootElement().addContent(returnElement);
        }

        return doc;
    }

    /**
     * 
     * createReplyDocument가 Error Message일 경우의 Send
     * 
     * @param Document
     * @param errorMessage
     * @param errorType
     * @return
     * @throws Exception
     */
    public void sendReplyError(Document recvDoc, String errorMessage, String errorType)
    {
        try
        {
        	String thisSubject = "" + System.getProperty("mode") + ".KR." + System.getProperty("group") + ".MES." + System.getProperty("svr");
        	
        	logger.debug(" > SendReply Ready Step 1 ) ");
        	String replySubjectName = MessageUtil.selectText(recvDoc, "//message/header/replysubject", "");
        	String replyTopicName = MessageUtil.selectText(recvDoc, "//message/header/replytopic", "");
        	logger.info("-------------------------------- Send Topic " + replyTopicName);
        	if ( replySubjectName == null || replySubjectName.isEmpty() )
        	{
        		// 메시지 전송 중지!
        		return;
        	}
        	
        	String sourceSubjectName = MessageUtil.selectText(recvDoc, "//message/header/sourcesubject", "");
        	String targetSubjectName = MessageUtil.selectText(recvDoc, "//message/header/targetsubject", "");

            MessageUtil.setChildText(recvDoc, "//message/header/", "targetsubject", replySubjectName);
            
            logger.debug(" > SendReply Ready Step 2 ) ");
            String replyMsg = null;

            logger.debug(" > SendReply Ready Step 3 ) ");
            logger.debug("Create Message ( Document => String ) ");
            
            // Send
            if( !replySubjectName.equalsIgnoreCase(sourceSubjectName) )
            {
            	if( targetSubjectName != null && !targetSubjectName.isEmpty() )
            	{
            		logger.info("-------------------------------- Send " + replySubjectName);
                	Document replyDoc = createReplyDocument(recvDoc, errorMessage, errorType);
                	replyMsg = MessageUtil.toString(replyDoc);
                	
                	MESFrameProxy.getMessageService().send(replySubjectName, replyMsg);
                	logger.info("-------------------------------- Send Complete " + replySubjectName);
            	}
            	else
            	{
            		logger.info("-------------------------------- Send " + sourceSubjectName);
                    Document replyDoc = createCloneReplyDocument(recvDoc);
                    if ( MessageUtil.selectNode(replyDoc, "//message/return") == null )
            		{
                    	replyDoc = createReplyDocument(replyDoc, errorMessage, errorType);
            		}
                    replyMsg = MessageUtil.toString(replyDoc);
            		
                    MESFrameProxy.getMessageService().reply(sourceSubjectName, replyTopicName, replyMsg);
                    logger.info("-------------------------------- Send Complete " + sourceSubjectName);
            	}
            }
            // Reply
            else if ( !replySubjectName.equalsIgnoreCase(thisSubject) )
            {
            	logger.info("-------------------------------- Send " + replySubjectName);
                Document replyDoc = createCloneReplyDocument(recvDoc);
                if ( MessageUtil.selectNode(replyDoc, "//message/return") == null )
        		{
                	replyDoc = createReplyDocument(replyDoc, errorMessage, errorType);
        		}
                replyMsg = MessageUtil.toString(replyDoc);
                MESFrameProxy.getMessageService().reply(replySubjectName, replyTopicName, replyMsg);
                logger.info("-------------------------------- Send Complete " + replySubjectName);
            }
            else
            {
            	// 자신이 자신에게 오류 메시지를 전송할 경우 반복 메시지 전송을 제한
            	logger.info("-------------------------------- Send Message Fail! ");
            }
            logger.debug("SERP :: " + replyMsg);
        }
        catch( Exception e )
        {
            logger.error("Reply Send Error", e);
        }
    }

    /**
     * 
     * createReplyDocument가 Error Message일 경우의 Send
     * 
     * @param recvDoc
     * @param throwable
     * @return
     *  
     */
    public void sendReplyError(Document recvDoc, Throwable throwable)
    {
        try
        {
        	String thisSubject = "" + System.getProperty("mode") + ".KR." + System.getProperty("group") + ".MES." + System.getProperty("svr");
        	
        	logger.debug(" > SendReply Ready Step 1 ) ");
        	String replySubjectName = MessageUtil.selectText(recvDoc, "//message/header/replysubject", "");
        	String replyTopicName = MessageUtil.selectText(recvDoc, "//message/header/replytopic", "");
        	logger.info("-------------------------------- Send Topic " + replyTopicName);
        	if ( replySubjectName == null || replySubjectName.isEmpty() )
        	{
        		// 메시지 전송 중지!
        		return;
        	}
        	
        	String sourceSubjectName = MessageUtil.selectText(recvDoc, "//message/header/sourcesubject", "");
        	String targetSubjectName = MessageUtil.selectText(recvDoc, "//message/header/targetsubject", "");

            MessageUtil.setChildText(recvDoc, "//message/header/", "targetsubject", replySubjectName);
            
            logger.debug(" > SendReply Ready Step 2 ) ");
            String replyMsg = null;

            logger.debug(" > SendReply Ready Step 3 ) ");
            logger.debug("Create Message ( Document => String ) ");
            
            // Send
            if( !replySubjectName.equalsIgnoreCase(sourceSubjectName) )
            {
            	if( targetSubjectName != null && !targetSubjectName.isEmpty() )
            	{
            		logger.info("-------------------------------- Send " + replySubjectName);
            		Document replyDoc = createReplyDocument(recvDoc, throwable);
                	replyMsg = MessageUtil.toString(replyDoc);
                	
            		MESFrameProxy.getMessageService().send(replySubjectName, replyMsg);
            		logger.info("-------------------------------- Send Complete " + replySubjectName);
            	}
            	else
            	{
            		logger.info("-------------------------------- Send " + sourceSubjectName);
                    Document replyDoc = createCloneReplyDocument(recvDoc);
                    if ( MessageUtil.selectNode(replyDoc, "//message/return") == null )
            		{
                    	replyDoc = createReplyDocument(recvDoc, throwable);
            		}
                    replyMsg = MessageUtil.toString(replyDoc);
            		
                    MESFrameProxy.getMessageService().reply(sourceSubjectName, replyTopicName, replyMsg);
                    logger.info("-------------------------------- Send Complete " + sourceSubjectName);
            	}
            }
            // Reply
            else if ( !replySubjectName.equalsIgnoreCase(thisSubject) )
            {
            	logger.info("-------------------------------- Send " + replySubjectName);
                Document replyDoc = createCloneReplyDocument(recvDoc);
                if ( MessageUtil.selectNode(replyDoc, "//message/return") == null )
        		{
                	replyDoc = createReplyDocument(recvDoc, throwable);
        		}
                replyMsg = MessageUtil.toString(replyDoc);
                MESFrameProxy.getMessageService().reply(replySubjectName, replyTopicName, replyMsg);
                logger.info("-------------------------------- Send Complete " + replySubjectName);
            }
            else
            {
            	// 자신이 자신에게 오류 메시지를 전송할 경우 반복 메시지 전송을 제한
            	logger.info("-------------------------------- Send Message Fail! ");
            }
            logger.debug("SERP :: " + replyMsg);
        }
        catch( Exception e )
        {
            logger.error("Reply Send Error", e);
        }
    }
    
    /**
     * 
     * createReplyDocument로 구성된 Message를 Send
     * 
     * @param replyDocument
     * @return
     * @throws Exception
     * 
     */
    public void sendReply(Document replyDocument) throws Exception
    {
    	sendReply(replyDocument, null);
    }

    /**
     * 
     * createReplyDocument로 구성된 Message를 Send
     * 
     * @param replyDocument
     * @param element
     * @return
     * @throws Exception
     * 
     */
    public void sendReply(Document replyDocument, Element element) throws Exception
    {
    	logger.debug(" > SendReply Ready Step 1 ) ");
    	String replySubjectName = MessageUtil.selectText(replyDocument, "//message/header/replysubject", "");
    	String replyTopicName = MessageUtil.selectText(replyDocument, "//message/header/replytopic", "");
    	logger.info("-------------------------------- Send Topic " + replyTopicName);
    	if ( replySubjectName == null || replySubjectName.isEmpty() )
    	{
    		// 메시지 전송 중지!
    		return;
    	}
    	
        String sourceSubjectName = MessageUtil.selectText(replyDocument, "//message/header/sourcesubject", "");
        String targetSubjectName = MessageUtil.selectText(replyDocument, "//message/header/targetsubject", "");
        
        MessageUtil.setChildText(replyDocument, "//message/header/", "targetsubject", replySubjectName);
        
        logger.debug(" > SendReply Ready Step 2 ) ");
        String replyMsg = null;

        logger.debug(" > SendReply Ready Step 3 ) ");
        logger.debug("Create Message ( Document => String ) ");
        
        // Send
        if( !replySubjectName.equalsIgnoreCase(sourceSubjectName) )
        {
        	if( targetSubjectName != null && !targetSubjectName.isEmpty() )
        	{
        		logger.info("-------------------------------- Send " + replySubjectName);
        		Document replyDoc = null;
        		if ( element != null )
        		{
        			replyDoc = createSendBody(replyDocument, element);
        		}
        		else
        		{
        			replyDoc = createReplyDocument(replyDocument);
        		}
            	replyMsg = MessageUtil.toString(replyDoc);
            	
        		MESFrameProxy.getMessageService().send(replySubjectName, replyMsg);
        		logger.info("-------------------------------- Send Complete " + replySubjectName);
        	}
        	else
        	{
        		logger.info("-------------------------------- Send " + sourceSubjectName);
        		Document replyDoc = null;
        		if ( element != null )
        		{
        			replyDoc = createSendBody(replyDocument, element);
        		}
        		else
        		{
        			replyDoc = createCloneReplyDocument(replyDocument);
        		}
        		
        		if ( MessageUtil.selectNode(replyDoc, "//message/return") == null )
        		{
                	replyDoc = createReplyDocument(replyDocument);
        		}
                replyMsg = MessageUtil.toString(replyDoc);
                
                MESFrameProxy.getMessageService().reply(sourceSubjectName, replyTopicName, replyMsg);
                logger.info("-------------------------------- Send Complete " + sourceSubjectName);
        	}
        }
        // Reply
        else
        {
        	logger.info("-------------------------------- Send " + replySubjectName);
            Document replyDoc = createCloneReplyDocument(replyDocument);
    		if ( MessageUtil.selectNode(replyDoc, "//message/return") == null )
    		{
    			if ( element != null )
        		{
        			replyDoc = createSendBody(replyDocument, element);
        		}
        		else
        		{
        			replyDoc = createReplyDocument(replyDocument);
        		}
    		}
            replyMsg = MessageUtil.toString(replyDoc);
            MESFrameProxy.getMessageService().reply(replySubjectName, replyTopicName, replyMsg);
            logger.info("-------------------------------- Send Complete " + replySubjectName);
        }
        logger.debug("SERP :: " + replyMsg);
    }

    /**
     * 
     * onMessage Method 구동시 Document를 받아 XML 구성
     * 
     * @param recvDoc
     * @return Document
     * @throws Exception
     * 
     */
    public Document createReplyDocument(Document recvDoc) throws Exception
    {
        Document replyDoc = MessageUtil.loadText("<message/>");

        Element header = MessageUtil.selectNode(recvDoc, "//message/header");
        replyDoc.getRootElement().addContent((Content) header.clone());
        MessageUtil.setNodeText(replyDoc, "//message/header/messagename", MessageUtil.selectText(recvDoc, "//message/header/messagename", "") + "Reply");

        // Element body = JdomUtils.getNode(recvDoc, "//message/body");
        // replyDoc.getRootElement().addContent((Content)body.clone());
        Element body = new Element("body");
        replyDoc.getRootElement().addContent(body);

        Element returnElement = new Element("return");
        MessageUtil.addElement(returnElement, "returncode", "0");
        MessageUtil.addElement(returnElement, "returntype", "");
        MessageUtil.addElement(returnElement, "returnmessage", "");
        MessageUtil.addElement(returnElement, "returndetailmessage", "");

        replyDoc.getRootElement().addContent(returnElement);

        return replyDoc;
    }

    /**
     * 
     * onMessage Method 구동시 Document를 받아 XML 구성
     * 
     * @param recvDoc
     * @return Document
     * @throws Exception
     * 
     */
    public Document createBodyReplyDocument(Document recvDoc) throws Exception
    {
        Document replyDoc = MessageUtil.loadText("<message/>");

        Element header = MessageUtil.selectNode(recvDoc, "//message/header");
        replyDoc.getRootElement().addContent((Content) header.clone());
        MessageUtil.setNodeText(replyDoc, "//message/header/messagename", MessageUtil.selectText(recvDoc, "//message/header/messagename", "") + "Reply");

        Element body = MessageUtil.selectNode(recvDoc, "//message/body");
        replyDoc.getRootElement().addContent((Content) body.clone());

        Element returnElement = new Element("return");
        MessageUtil.addElement(returnElement, "returncode", "0");
        MessageUtil.addElement(returnElement, "returntype", "");
        MessageUtil.addElement(returnElement, "returnmessage", "");
        MessageUtil.addElement(returnElement, "returndetailmessage", "");

        replyDoc.getRootElement().addContent(returnElement);

        return replyDoc;
    }

    /**
     * 
     * onMessage Method 구동시 Document를 받아 XML 구성
     * 
     * @param recvDoc
     * @return Document
     * @throws Exception
     * 
     */
    public Document createCloneReplyDocument(Document recvDoc) throws Exception
    {
        Document replyDoc = MessageUtil.loadText("<message/>");

        Element header = MessageUtil.selectNode(recvDoc, "//message/header");
        replyDoc.getRootElement().addContent((Content) header.clone());

        Element body = MessageUtil.selectNode(recvDoc, "//message/body");
        replyDoc.getRootElement().addContent((Content) body.clone());

        if ( MessageUtil.selectNode(recvDoc, "//message/return") != null )
		{
        	Element returnElement = MessageUtil.selectNode(recvDoc, "//message/return");
        	replyDoc.getRootElement().addContent((Content) returnElement.clone());
		}
        
        return replyDoc;
    }

    /**
     * 
     * onMessage Method 구동시 Document를 받아 XML 구성
     * 
     * @param recvDoc
     * @param throwable
     * @return Document
     * @throws Exception
     * 
     */
    public Document createReplyDocument(Document recvDoc, Throwable throwable) throws Exception
    {
        Document replyDoc = MessageUtil.loadText("<message/>");

        Element header = MessageUtil.selectNode(recvDoc, "//message/header");
        replyDoc.getRootElement().addContent((Content) header.clone());
        MessageUtil.setNodeText(replyDoc, "//message/header/messagename", MessageUtil.selectText(recvDoc, "//message/header/messagename", "") + "Reply");

        // Element body = JdomUtils.getNode(recvDoc, "//message/body");
        // replyDoc.getRootElement().addContent((Content)body.clone());
        Element body = new Element("body");
        replyDoc.getRootElement().addContent(body);

        Element returnElement = new Element("return");
        if( throwable == null )
        {
        	MessageUtil.addElement(returnElement, "returncode", "0");
        	MessageUtil.addElement(returnElement, "returntype", "");
        	MessageUtil.addElement(returnElement, "returnmessage", "");
        	MessageUtil.addElement(returnElement, "returndetailmessage", "");
        }
        else
        {
        	MessageUtil.addElement(returnElement, "returncode", "9999");
        	MessageUtil.addElement(returnElement, "returntype", "Error");
        	MessageUtil.addElement(returnElement, "returnmessage", throwable.toString());
        	MessageUtil.addElement(returnElement, "returndetailmessage", ConvertUtil.Object2String(throwable));
        }

        replyDoc.getRootElement().addContent(returnElement);

        return replyDoc;
    }

    /**
     * 
     * onMessage Method 구동시 Document 를 받아 XML 구성
     * 
     * @param recvDoc
     * @param errorMessage
     * @param errorType
     * @return Document
     * @throws Exception
     * 
     */
    public Document createReplyDocument(Document recvDoc, String errorMessage, String errorType) throws Exception
    {
        Document replyDoc = MessageUtil.loadText("<message/>");

        Element header = MessageUtil.selectNode(recvDoc, "//message/header");
        replyDoc.getRootElement().addContent((Content) header.clone());
        MessageUtil.setNodeText(replyDoc, "//message/header/messagename", MessageUtil.selectText(recvDoc, "//message/header/messagename") + "Reply");

        Element body = new Element("body");
        replyDoc.getRootElement().addContent(body);

        Element returnElement = new Element("return");

        String[] error = errorMessage.split(";");

        MessageUtil.addElement(returnElement, "returncode", error[0]);
        MessageUtil.addElement(returnElement, "returntype", errorType);
        MessageUtil.addElement(returnElement, "returnmessage", error[1]);
        if( error.length > 2 )
        {
        	MessageUtil.addElement(returnElement, "returndetailmessage", error[2]);
        }
        else
        {
        	MessageUtil.addElement(returnElement, "returndetailmessage", "");
        }

        replyDoc.getRootElement().addContent(returnElement);

        return replyDoc;
    }

    /**
     * SQL을 실행하고 그 결과를 List 로 리턴을 한다.
     * 
     * @param queryString - sql
     * @param inputParamsMap - SQL바인딩처리할 Paramters(Map)
     * @return List<LinkedCaseInsensivieMap>
     * @author 
     * @date 2011.04.20
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<LinkedCaseInsensitiveMap> executeQueryForList(String queryString, Object inputParamsMap)
    {
        long startTime = System.currentTimeMillis();

        Map inParams = (Map) inputParamsMap;

        String query = queryString;
        query = query.replaceAll("(?i)#if", "#if");
        query = query.replaceAll("(?i)#end", "#end");
        query = parseDynamicSQL(query, inParams);

        // GetQueryResult 실행 SQL 실행 및 BIND변수처리
        query = query.replaceAll("(?i):MI", "#MI");
        query = query.replaceAll("(?i):SS", "#SS");
        ArrayList<String> bindParamList = getBindParamList(query);
        query = query.replaceAll("(?i)#MI", ":MI");
        query = query.replaceAll("(?i)#SS", ":SS");

        // CONSOLE에 뿌려줄 SQL의 로그를 생성한다.
        ArrayList<String> sqllogBindParamList = new ArrayList<String>();
        for( int i = 0; i < bindParamList.size(); i++ )
        {
        	sqllogBindParamList.add(bindParamList.get(i));
        }
        Collections.sort(sqllogBindParamList);
        String querylog = query;
        for( int i = sqllogBindParamList.size() - 1; i > -1; i-- )
        {
        	String paramValue = inParams.get(sqllogBindParamList.get(i)) + "";
        	if( "null".equalsIgnoreCase(paramValue) )
        		paramValue = "";

        	if( paramValue.length() == 0 )
        	{
        		querylog = querylog.replaceAll(":" + sqllogBindParamList.get(i).toString(), "NULL");
        	}
        	else
        	{
        		querylog = querylog.replaceAll(":" + sqllogBindParamList.get(i).toString(), "'" + inParams.get(sqllogBindParamList.get(i)) + "'");
        	}
        }

        logger.info("-------------------------------------------------------------------------");
        logger.info(querylog.trim());
        logger.info("-------------------------------------------------------------------------");

        List<LinkedCaseInsensitiveMap> listResult = SqlMesTemplate.queryForList(query, inParams);

        long endTime = System.currentTimeMillis();
        double runTime = (double) (endTime - startTime) / (double) 1000;
        String processRunTime = (double) (runTime) + "";
        if( processRunTime.length() > 5 )
        {
        	processRunTime = processRunTime.substring(0, 5);
        }

        logger.info(" Query Excute Complete : " + listResult.size() + " Records " + runTime + " Sec\n");

        return listResult;
    }

    /**
     * SQL 문장에 들어있는 BIND변수를 찾아서 BIND변수를 ArrayList로 리턴한다.
     * 
     * @param query SQL문장
     * @return ArrayList - Bind변수처리할 변수의 목록
     * @author 
     * @date 2011.02.09
     */
    public ArrayList<String> getBindParamList(String query)
    {
        ArrayList<String> bindParamList = new ArrayList<String>();

        String[] buff = query.split("[:]");
        for( int i = 1; i < buff.length; i++ )
        {
            String[] bindParam = buff[i].split("[ ,)|+-/*%\r\n\t]");
            bindParamList.add(bindParam[0].trim().toUpperCase());
        }

        return bindParamList;
    }

    /**
     * SQL문장의 #동적처리할 문장을 Parsing처리하여 조건문의 참/거짓을 판단하여 그에 대응하는 SQL문장으로 PreCompile처리한다.
     * 
     * @param query - SQL문장
     * @param inVl - 마이플랫폼 입력파라미터
     * @return String - preCompile처리된 SQL문장
     * @author 최현수
     * @date 2011.02.15
     * 
     */
	@SuppressWarnings("rawtypes")
	public String parseDynamicSQL(String query, Map inVl)
    {
        int dymamicSqlCount = 0;
        String newsql = query;
        String[] buff = split(query, "#end", false);

        for( int i = 0; i < buff.length - 1; i++ )
        {
            boolean notequalsflag = false;
            String[] temp1 = split(buff[i], "#if", false);
            String orgdynamicsql = temp1[1];

            String[] temp2 = split(orgdynamicsql, "#then", false);
            String ifsentens = temp2[0];
            String thensentens = "";
            String elsesentens = "";

            String[] temp3 = split(temp2[1], "#else", false);
            thensentens = temp3[0];
            if( temp3.length == 2 )
            {
                elsesentens = temp3[1];
            }

            boolean truefalse = true;
            String paramName = "";
            String paramValue = "";
            if( ifsentens.toLowerCase().indexOf("isnotnull") != -1 )
            {
                String[] temp4 = split(ifsentens, ".isnotnull", false);
                paramName = temp4[0].trim().toUpperCase();

                paramValue = inVl.get(paramName) + "";
                if( "".equals(paramValue) || "null".equals(paramValue) )
                    truefalse = false;
                else
                    truefalse = true;
            }
            else if( ifsentens.toLowerCase().indexOf("isnull") != -1 )
            {
                String[] temp4 = split(ifsentens, ".isnull", false);
                paramName = temp4[0].trim().toUpperCase();

                paramValue = inVl.get(paramName) + "";
                if( "".equals(paramValue) || "null".equals(paramValue) )
                    truefalse = true;
                else
                    truefalse = false;
            }
            else if( ifsentens.toLowerCase().indexOf("notequals") != -1 )
            {
                notequalsflag = true;

                String[] temp4 = split(ifsentens, ".notequals", false);
                paramName = temp4[0].trim().toUpperCase();
                paramValue = inVl.get(paramName) + "";

                String[] temp5 = temp4[1].split("[\"]");

                if( temp5[1].equalsIgnoreCase(paramValue) )
                    truefalse = false;
                else
                    truefalse = true;
            }
            else if( ifsentens.toLowerCase().indexOf("equals") != -1 )
            {
                if( notequalsflag == false )
                {
                    String[] temp4 = split(ifsentens, ".equals", false);
                    paramName = temp4[0].trim().toUpperCase();
                    paramValue = inVl.get(paramName) + "";

                    String[] temp5 = temp4[1].split("[\"]");

                    if( temp5[1].equalsIgnoreCase(paramValue) )
                        truefalse = true;
                    else
                        truefalse = false;
                }
            }
            else if( ifsentens.indexOf(">=") != -1 )
            {
                String[] temp4 = ifsentens.split(">=");
                paramName = temp4[0].trim().toUpperCase();
                paramValue = inVl.get(paramName) + "";

                String compareValue = "";
                String[] temp5 = temp4[1].split(" ");
                for( int j = 0; j < temp5.length; j++ )
                {
                    String tempValue = temp5[j];
                    if( !"".equals(tempValue) )
                    {
                        compareValue = temp5[j];
                        break;
                    }
                }

                if( Double.parseDouble(compareValue) >= Double.parseDouble((String) inVl.get(paramName)) )
                    truefalse = true;
                else
                    truefalse = false;
            }
            else if( ifsentens.indexOf("<=") != -1 )
            {
                String[] temp4 = ifsentens.split("<=");
                paramName = temp4[0].trim().toUpperCase();
                paramValue = inVl.get(paramName) + "";

                String compareValue = "";
                String[] temp5 = temp4[1].split(" ");
                for( int j = 0; j < temp5.length; j++ )
                {
                    String tempValue = temp5[j];
                    if( !"".equals(tempValue) )
                    {
                        compareValue = temp5[j];
                        break;
                    }
                }

                if( Double.parseDouble(compareValue) <= Double.parseDouble((String) inVl.get(paramName)) )
                    truefalse = true;
                else
                    truefalse = false;
            }
            else if( ifsentens.indexOf(">") != -1 )
            {
                String[] temp4 = ifsentens.split(">");
                paramName = temp4[0].trim().toUpperCase();
                paramValue = inVl.get(paramName) + "";

                String compareValue = "";
                String[] temp5 = temp4[1].split(" ");
                for( int j = 0; j < temp5.length; j++ )
                {
                    String tempValue = temp5[j];
                    if( !"".equals(tempValue) )
                    {
                        compareValue = temp5[j];
                        break;
                    }
                }

                if( Double.parseDouble(compareValue) > Double.parseDouble((String) inVl.get(paramName)) )
                    truefalse = true;
                else
                    truefalse = false;
            }
            else if( ifsentens.indexOf("<") != -1 )
            {
                String[] temp4 = ifsentens.split("<");
                paramName = temp4[0].trim().toUpperCase();
                paramValue = inVl.get(paramName) + "";

                String compareValue = "";
                String[] temp5 = temp4[1].split(" ");
                for( int j = 0; j < temp5.length; j++ )
                {
                    String tempValue = temp5[j];
                    if( !"".equals(tempValue) )
                    {
                        compareValue = temp5[j];
                        break;
                    }
                }

                if( Double.parseDouble(compareValue) < Double.parseDouble((String) inVl.get(paramName)) )
                    truefalse = true;
                else
                    truefalse = false;
            }

            String newsentence = "";
            if( truefalse )
                newsentence = thensentens;
            else
                newsentence = elsesentens;

            // 처리될 SQL 에 .list가 있으면 해당SQL을 IN ('','') 로 처리하도록 변경한다.
            String[] queryincheckbuff = split(newsentence, ".list", false);
            if( queryincheckbuff.length != 1 )
            {
                String inquery = "";
                String inquerysentence = "";
                String[] inbuff2 = queryincheckbuff[0].split(":");

                inquery += inbuff2[0];
                String inParamName = inbuff2[1].trim().toUpperCase();
                String inParamValue = inVl.get(inParamName) + "";

                // LIST의 값이 없을경우
                if( inParamValue.length() == 0 || "null".equalsIgnoreCase(inParamValue) )
                {
                    inquery += "(NULL)";
                    inquery += queryincheckbuff[1];
                    newsentence = inquery;
                }
                else
                {
                    // ('','') 를 만들어준다.
                    String[] inParamValueList = inParamValue.split(",");
                    for( int j = 0; j < inParamValueList.length; j++ )
                    {
                        String listValue = inParamValueList[j] + "";
                        if( listValue.length() == 0 || "null".equalsIgnoreCase(listValue) )
                        {
                            continue;
                        }

                        if( inquerysentence.length() == 0 )
                            inquerysentence = "'" + getQueryString(inParamValueList[j]) + "'";
                        else
                            inquerysentence += ",'" + getQueryString(inParamValueList[j]) + "'";
                    }
                    inquerysentence = "(" + inquerysentence + ")";
                    inquery += inquerysentence;
                    inquery += queryincheckbuff[1];
                    newsentence = inquery;
                }
            }

            newsql = newsql.replace("#if" + orgdynamicsql + "#end", newsentence);
            ++dymamicSqlCount;
        }

        if( dymamicSqlCount != 0 )
        {
            query = newsql;
        }
        
        // #loopstart 1 to PARAMNAME #begin    #loopend 문장처리
        String[] loopbuff = split(query, "#loopend", false);        
        for( int i = 0; i < loopbuff.length - 1; i++ )
        {
            String[] temp1 = split(loopbuff[i], "#loopstart", false);
            String orgdynamicsql = temp1[1];
    
            String[] temp2 = split(orgdynamicsql, "#begin", false);
            String loopcondition = temp2[0];
            String loopcontents  = temp2[1];
            
            //System.out.println("loopcondition "+loopcondition);
            //System.out.println("loopcontents "+loopcontents);
            
            String[] loopCountBuff = split(loopcondition, "to", false);
            String startValue = loopCountBuff[0];
            String endValue   = loopCountBuff[1];
            endValue = inVl.get(loopCountBuff[1].trim())+"";
            
            // 변수 LOOP 의 대소문자에 상관없이 처리를 위해서 #loop 를 모두 소문자처리한다. 
            String newLoopContents = "";
            String[] loopChangeBuff = split(loopcontents, "#loop", false);
            for(int j=0;j<loopChangeBuff.length-1;j++)
            {
                newLoopContents += loopChangeBuff[j]+"#loop";              
            }
            newLoopContents += loopChangeBuff[loopChangeBuff.length-1];              
            
            //System.out.println("startValue ["+startValue+"]");
            //System.out.println("endValue ["+endValue+"]");
            
            String newLoopSQL = "";
            int loopstart = Integer.parseInt(startValue.trim());
            int loopend   = Integer.parseInt(endValue.trim());
            for(int j=loopstart;j<loopend+1;j++)
            {
                String replaceValue = j+"";
                newLoopSQL += newLoopContents.replaceAll("#loop", replaceValue);
            }            
            
            //System.out.println(newLoopSQL);            
            query = query.replace("#loopstart" + orgdynamicsql + "#loopend", newLoopSQL);    
        }
        
        return query;
    }

    
    /**
     * Split 처리를 대소문자구분Flag로 split하게 처리
     * 
     * @param source
     * @param seperator
     * @param matchCase
     * @return String[] - Split처리결과
     * @author http://blog.naver.com/json2811?Redirect=Log&logNo=90094741526
     * @date 2011.02.15
     * 
     */
    public String[] split(String source, String seperator, boolean matchCase)
    {
        ArrayList<String> substrings = new ArrayList<String>();

        if( null == source )
        {
            return new String[0];
        }

        if( null == seperator )
        {
            return new String[0];
        }

        int current_index = 0;
        int delimiter_index = 0;
        String element = null;

        String source_lookup_reference = null;

        // 대소문자를 구별에 관한 판단
        if( !matchCase )
        {
            source_lookup_reference = source.toLowerCase();
            seperator = seperator.toLowerCase();
        }
        else
        {
            source_lookup_reference = source;
        }

        // 문자열 길이보다 작은한 반복해서 split를 시도한다.
        while (current_index <= source_lookup_reference.length())
        {
            // 식별자(seperator)가 존재하는지 검사.
            delimiter_index = source_lookup_reference.indexOf(seperator, current_index);

            // 존재 안 하는 경우
            if( -1 == delimiter_index )
            {
                element = new String(source.substring(current_index, source.length()));
                substrings.add(element);
                current_index = source.length() + 1;
            }
            else
            // 존재하는 경우
            {
                element = new String(source.substring(current_index, delimiter_index));
                substrings.add(element);
                current_index = delimiter_index + seperator.length();
            }
        }

        String[] rtnValue = new String[substrings.size()];
        for( int i = 0; i < substrings.size(); i++ )
        {
            rtnValue[i] = substrings.get(i);
        }

        return rtnValue;
    }

    /**
     * 
     * 입력받은 parameter를 리턴합니다
     *
     * @param parameter
     * @return String
     *
     */
    public String getQueryString(String strValue)
    {
        return strValue;
    }

}

