package mes.material.execution;

import java.util.HashMap;
import java.util.List;

import mes.event.MessageParse;
import mes.master.data.STOCKMONTH;
import mes.material.transaction.MaterialService;
import mes.material.transaction.StockService;
import mes.util.EventInfoUtil;

import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnMaterialDeadLine implements ObjectExecuteService 
{
	private static final transient Logger log = LoggerFactory.getLogger( MaterialService.class );
	
	@Override
	public Object execute(Document recvDoc) 
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);		
		StockService stockService = new StockService();		
       
		String sPlantID = "";
		String sYYYYMM = "";
				
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			sPlantID = dataMap.get("PLANTID");
			sYYYYMM = dataMap.get("YYYYMM");
			String sNextMonth = stockService.GetNextMonth(sYYYYMM);
			
			STOCKMONTH stockMonth = new STOCKMONTH();
			STOCKMONTH nextMonth = new STOCKMONTH();
			
			stockMonth.setKeyPLANTID(sPlantID);
            stockMonth.setKeyYYYYMM(sYYYYMM);          
        	stockMonth.excuteDML(SqlConstant.DML_DELETE);       
        	
        	nextMonth.setKeyPLANTID(sPlantID);
        	nextMonth.setKeyYYYYMM(sNextMonth);          
        	nextMonth.setSTATE(dataMap.get("STATE"));
        	nextMonth.excuteDML(SqlConstant.DML_INSERT);  
		}		
        stockService.MaterialStockCloseProcess(sPlantID, sYYYYMM);
        insertSql(sPlantID, sYYYYMM);
        return recvDoc;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertSql(String plantID, String currentMonth)
	{
		StockService stockService = new StockService();		
		String sNextMonth = stockService.GetNextMonth(currentMonth);
		
		 String sql = "INSERT INTO STOCKPOLICY(PLANTID, YYYYMM, POLICYID, POLICYVALUE, ACTIVESTATE, DESCRIPTION) \n"
         		+ "SELECT PLANTID, :NEXTMONTH, POLICYID, POLICYVALUE, ACTIVESTATE, DESCRIPTION \n"
         		+ "FROM STOCKPOLICY \n" 
         		+ "WHERE 1 = 1 \n"
         		+ "AND PLANTID = :PLANTID \n"
         		+ "AND YYYYMM = :YYYYMM \n";
        HashMap bindMap = new HashMap();
 		bindMap.put("PLANTID", plantID);
 		bindMap.put("YYYYMM", currentMonth);
 		bindMap.put("NEXTMONTH", sNextMonth);
 		int iReturn = SqlMesTemplate.update(sql, bindMap);
 		log.info("1. INSERT stockpolicy NEXTMONTH DATA : " + iReturn);
	}
}
