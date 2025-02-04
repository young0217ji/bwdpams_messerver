package mes.query.biz;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.util.SqlLogUtil;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.MessageUtil;
import mes.errorHandler.CustomException;
import mes.generic.GenericServiceProxy;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class GetQueryResult implements ObjectExecuteService
{
	private static final transient Logger logger = LoggerFactory.getLogger(GetQueryResult.class);

    /**
     * @param Document
     * @return Document
     * @throws Exception
     * @description Document형을 Argument로 받아 HashMap형으로 Parse 후 QUERYID 와 VERSION, QueryString의 Bind변수를 Argument 로 구성하여
     *              Query 실행 실행 후의 결과값을 받아 XML로 구성하여 Return
    */
    @SuppressWarnings("rawtypes")
	@Override
    public Object execute(Document recvDoc)
    {
    	long startTime = System.currentTimeMillis();
    	
    	logger.debug("{}", MessageUtil.convertDocumentToXmlString(recvDoc));
    	
    	
    	// Get Query String
    	String plantID = recvDoc.getDocument().getRootElement().getChild("body").getChildText("PLANTID");
    	String queryID = recvDoc.getDocument().getRootElement().getChild("body").getChildText("QUERYID");
    	String version = recvDoc.getDocument().getRootElement().getChild("body").getChildText("QUERYVERSION");
    	String queryType = recvDoc.getDocument().getRootElement().getChild("body").getChildText("QUERYTYPE");
    	String sourceSubjectName = recvDoc.getDocument().getRootElement().getChild("header").getChildText("sourcesubject");
    	String targetSubjectName = recvDoc.getDocument().getRootElement().getChild("header").getChildText("targetsubject");
    	String replySubjectName = recvDoc.getDocument().getRootElement().getChild("header").getChildText("replysubject");
    	String replyTopicName = recvDoc.getDocument().getRootElement().getChild("header").getChildText("replytopic");
    	String transactionID = recvDoc.getDocument().getRootElement().getChild("header").getChildText("transactionid");

    	Object[] args = new Object[] { plantID, queryID, version };

    	String usrSql = "SELECT queryString FROM customQuery WHERE plantID = ? AND queryID = ? AND queryVersion = ? ";

    	List resultList = null;
    	resultList = SqlMesTemplate.queryForList(usrSql, args);
    	
    	//updateQueryCount(plantID, queryID, version);

    	if( resultList.size() == 0 )
    	{
    		logger.error("-----------------------------------------------------------------------------");
    		logger.error("PlantID - " + plantID + ", GetQueryResult : " + queryID + " QueryID Not Found Error");
    		logger.error("-----------------------------------------------------------------------------");
    		throw new CustomException("CM-002", new Object[] { queryID, version });
    	}

    	LinkedCaseInsensitiveMap queryMap = (LinkedCaseInsensitiveMap) resultList.get(0);
    	String dbSql = (String) queryMap.get("queryString");
    	
    	// Paging Query 적용을 위한 로직
    	if ( queryType != null && "Paging".equalsIgnoreCase(queryType) ) {
    		dbSql = dbSql.replaceFirst("(?i)select", "SELECT COUNT(*) OVER () AS TOTAL_COUNT, ");   		

    		// [2023.08.24-구자윤] MSSQL DB에서의 Paging 쿼리 대응을 위한 로직 추가 *SQL Server 2012부터 지원*
    		if ( "MSSQL".equalsIgnoreCase(MESFrameProxy.getDriverType()) ) {
    			
    			// PAGING_NO - 조회하려는 페이지 번호
    			// PAGING_SIZE - 한번 조회할때 가져오는 데이터 Row 수
    			dbSql = dbSql + " OFFSET (:PAGING_NO-1)*:PAGING_SIZE ROW FETCH NEXT :PAGING_SIZE ROW ONLY ";
    		}
    		else {
    			// PAGING_LIMIT - 조회 Row COUNT
    			// PAGING_OFFSET - 시작 ROW COUNT
    			dbSql = dbSql + " LIMIT :PAGING_LIMIT OFFSET :PAGING_OFFSET ";
    		}
    	}

    	HashMap<String, Object> bindMap = new HashMap<String, Object>();

    	if( recvDoc.getDocument().getRootElement().getChild("body").getChild("BINDV") != null )
    	{
    		Element elementMember = recvDoc.getDocument().getRootElement().getChild("body").getChild("BINDV");
    		for( Iterator iterator = elementMember.getChildren().iterator(); iterator.hasNext(); )
    		{
    			Element listMember = (Element) iterator.next();
    			bindMap.put(listMember.getName(), listMember.getText());
    			
    			if ( "PAGING_LIMIT".equals(listMember.getName()) || "PAGING_OFFSET".equals(listMember.getName()) || 
    					 "PAGING_NO".equals(listMember.getName()) || "PAGING_SIZE".equals(listMember.getName()) ) {
    				bindMap.put(listMember.getName(), ConvertUtil.String2Int(listMember.getText()));
    			}
    		}
    	}

    	if( bindMap.isEmpty() || bindMap == null )
    	{
    		if( dbSql.indexOf(":") > -1 )
    		{
				throw new CustomException("CM-003", new Object[] {});
    		}
    		logger.info(">> SQL = [" + dbSql + "]");
    	}
    	else
    	{
    		logger.info(">> SQL = [" + SqlLogUtil.getLogFormatSqlStatement(dbSql, bindMap, logger) + "]");
    	}
    	
    	// Bind 변수없이 이벤트가 호출되는 경우 NULL로 처리되도록 설정
    	ArrayList<String> bindParamList = GenericServiceProxy.getMessageAdaptor().getBindParamList(dbSql);
    	for ( int j = 0; j < bindParamList.size(); j++ )
    	{
    		String stringBind = bindParamList.get(j);
    		if ( !bindMap.containsKey(stringBind) )
    		{
    			bindMap.put(stringBind, "");
    		}
    	}

    	resultList.clear();
    	resultList = SqlMesTemplate.queryForList(dbSql, bindMap);

    	/*************************************************************************
         * SQL RUNTIME CHECK
         *************************************************************************/
        long sqlEndTime = System.currentTimeMillis();
        double runTime = (double) (sqlEndTime - startTime) / (double) 1000;
        String sqlRunTime = runTime + "";
        if( sqlRunTime.length() > 5 )
        {
            sqlRunTime = sqlRunTime.substring(0, 5);
        }
    	
        // 처리결과를 담을 XML
    	Element elmMessage = new Element("message");
    	Element elmHeader = new Element("header");
    	Element elmBody = new Element("body");
    	Element elmReturn = new Element("return");

    	// 해더설정
    	elmHeader.addContent(new Element("messagename").addContent(queryID + "Reply"));
    	elmHeader.addContent(new Element("sourcesubject").addContent(sourceSubjectName));
    	elmHeader.addContent(new Element("targetsubject").addContent(targetSubjectName));
    	elmHeader.addContent(new Element("replysubject").addContent(replySubjectName));
    	elmHeader.addContent(new Element("replytopic").addContent(replyTopicName));
    	elmHeader.addContent(new Element("transactionid").addContent(transactionID));

    	// QUERY결과 DATASET설정
    	int rowCount = resultList.size();
    	if( rowCount != 0 )
    	{
    		Element elmDataList = new Element("DATALIST");
    		for( int i = 0; i < rowCount; i++ )
    		{
    			// RowCount 1000건당 처리 시간 체크 후 과부하 쿼리에 대한 TimeOut 설정
    			if ( ConvertUtil.doubleRemainder( (double)(i + 1), 1000.0 ).compareTo(0.0) == 0 )
    			{
    				// 이벤트 발생 후 10분이 초과하는 경우 강제 종료
    				if ( (System.currentTimeMillis() - startTime) > 600000 )
    				{
    					// 총 조회건수 ({0})중 처리 시간 10분을 초과하여 ({1})번째 처리중 처리를 중지합니다.
    					throw new CustomException("CM-004", new Object[] { rowCount, i + 1 });
    				}
    			}
    			
    			LinkedCaseInsensitiveMap orderMap = (LinkedCaseInsensitiveMap) resultList.get(i);
    			Iterator map = orderMap.entrySet().iterator();

    			Element elmRowData = new Element("DATA");
    			while (map.hasNext())
    			{
    				Entry entry = (Entry) map.next();
    				String Key = (String) entry.getKey();
    				String value = null;

    				if( entry.getValue() == null )
    				{
    					value = "";
    				}
    				else if( entry.getValue() instanceof String )
    				{
    					value = (String) entry.getValue();
    				}
    				else
    				{
    					value = String.valueOf(entry.getValue());
    				}
    				
    				elmRowData.addContent(new Element(Key).addContent(value));
    			}
    			
    			elmDataList.addContent(elmRowData);
    		}
    		
    		elmBody.addContent(elmDataList);
    	}

    	// Return코드설정
    	elmReturn.addContent(new Element("returncode").addContent("0"));
    	elmReturn.addContent(new Element("returnmessage").addContent(""));
    	elmReturn.addContent(new Element("returndetailmessage").addContent(""));

    	elmMessage.addContent(elmHeader);
    	elmMessage.addContent(elmBody);
    	elmMessage.addContent(elmReturn);

    	XMLOutputter xmlOut = new XMLOutputter();
    	xmlOut.setFormat(Format.getPrettyFormat());
    	String sGetQueryResultMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + xmlOut.outputString(elmMessage);

    	/*************************************************************************
         * PARSING RUNTIME CHECK
         *************************************************************************/
        long parseEndTime = System.currentTimeMillis();
        double totalParseEndTime = (double) (parseEndTime - sqlEndTime) / (double) 1000;
        String sTotalParseTime = totalParseEndTime + "";
        if( sTotalParseTime.length() > 5 )
        {
            sTotalParseTime = sTotalParseTime.substring(0, 5);
        }
    	
//    	NCrossRequester sender = (NCrossRequester) BundleUtil.getServiceByBeanName("jmsSender");

    	if ( sourceSubjectName != null && !sourceSubjectName.trim().isEmpty() )
		{
			logger.info("-------------------------------- Send " + sourceSubjectName);
			MESFrameProxy.getMessageService().reply(sourceSubjectName, replyTopicName, sGetQueryResultMsg);			
			logger.info("-------------------------------- Send Complete " + sourceSubjectName);
		}
		else
		{
			logger.info("SERP :: " + sGetQueryResultMsg);
			logger.info("Message 전송에 실패했습니다.");
		}

		/*************************************************************************
         * SEND RUNTIME CHECK
         *************************************************************************/
        long sendEndTime = System.currentTimeMillis();
        double totalSendEndTime = (double) (sendEndTime - parseEndTime) / (double) 1000;
        double totalRunTime = (double) (sendEndTime - startTime) / (double) 1000;
        String sTotalRunTime = totalRunTime + "";
        String sTotalSendEndTime = totalSendEndTime + "";
        if( sTotalRunTime.length() > 5 )
        {
            sTotalRunTime = sTotalRunTime.substring(0, 5);
        }
        if( sTotalSendEndTime.length() > 5 )
        {
            sTotalSendEndTime = sTotalSendEndTime.substring(0, 5);
        }

//        logger.info("(" + queryID + ":" + version + ") Success " + sTotalRunTime + " (SQL:" + sqlRunTime + " + XML:" + sTotalParseTime + " + SEND:" + sTotalSendEndTime + ") Sec\n");
        MemoryMXBean membean = (MemoryMXBean) ManagementFactory.getMemoryMXBean();
		MemoryUsage heap = membean.getHeapMemoryUsage();
		MemoryUsage nonheap = membean.getNonHeapMemoryUsage();
		long heapInit = heap.getInit();
		long heapUsed = heap.getUsed();
		long heapCommit = heap.getCommitted();
		long heapMax = heap.getMax();
        String memInfo = " HEAPINIT : " + ConvertUtil.Object2String(heapInit);
        memInfo = memInfo + ", HEAPUSED : " + ConvertUtil.Object2String(heapUsed);
        memInfo = memInfo + ", HEAPCOMMIT : " + ConvertUtil.Object2String(heapCommit);
        memInfo = memInfo + ", HEAPMAX : " + ConvertUtil.Object2String(heapMax);
		
        logger.info("(" + queryID + ":" + version + ") Success " + sTotalRunTime + " (SQL:" + sqlRunTime + " + XML:" + sTotalParseTime + " + SEND:" + sTotalSendEndTime + ") Sec\n"
        		 + memInfo);
        
    	return null;
    }
    
    public void updateQueryCount(String plantID, String queryID, String queryVersion)
    {
    	String updateSql = "UPDATE CUSTOMQUERY SET QUERYCOUNT = NVL(QUERYCOUNT,0) + 1 " + 
                           "WHERE PLANTID='" + plantID + "' " +
				           "AND QUERYID='" + queryID + "' " + 
                           "AND QUERYVERSION='"+queryVersion+"' ";
		SqlMesTemplate.update(updateSql);
    }
    
    
    
}