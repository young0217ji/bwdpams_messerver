package mes.time.biz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.orm.sql.SqlMesTemplate;

/*
 ****************************************************************************
 *  PACKAGE : mes.time.biz;
 *  NAME    : DatabaseSyncQA.java
 *  TYPE    : JAVA
 *  DESCRIPTION : Server Start With DEV DB to QA DB Synchronization
 *	MODIFY	: 2023.10.06
 ****************************************************************************
 */

public class DatabaseSyncQA implements Job 
{
	private static final transient Logger logger = LoggerFactory.getLogger(DatabaseSyncQA.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException 
	{	
		try {
			logger.info("########## DatabaseSyncQA Execute Start ##########");
			MESFrameProxy.getTxManager().begin();
			executeRepeatTimer();
			MESFrameProxy.getTxManager().lastCommitForce();
			logger.info("########## DatabaseSyncQA Execute End ##########");
		} 
		catch (Exception e) {
			e.printStackTrace();
			MESFrameProxy.getTxManager().lastRollbackForce();
			logger.info("########## DatabaseSyncQA Execute Error ##########");
		}
	}
	
	public void executeRepeatTimer() throws Exception
	{
		if ( "QA".equalsIgnoreCase(System.getProperty("mode")) == false ) {
			return;
		}
		
		String sDeleteSql = "DELETE FROM DY_MES_QA.dbo.CUSTOMQUERY ";
		SqlMesTemplate.update(sDeleteSql);
		String sInsertSql = "INSERT INTO DY_MES_QA.dbo.CUSTOMQUERY SELECT * FROM DY_MES.dbo.CUSTOMQUERY ";
		SqlMesTemplate.update(sInsertSql);
		
		logger.info("########## DatabaseSyncQA Execute Complete --- CUSTOMQUERY ##########");
		
		sDeleteSql = "DELETE FROM DY_MES_QA.dbo.GRIDDEFINITION ";
		SqlMesTemplate.update(sDeleteSql);
		sInsertSql = "INSERT INTO DY_MES_QA.dbo.GRIDDEFINITION SELECT * FROM DY_MES.dbo.GRIDDEFINITION ";
		SqlMesTemplate.update(sInsertSql);
		
		logger.info("########## DatabaseSyncQA Execute Complete --- GRIDDEFINITION ##########");
		
		sDeleteSql = "DELETE FROM DY_MES_QA.dbo.GRIDDETAIL ";
		SqlMesTemplate.update(sDeleteSql);
		sInsertSql = "INSERT INTO DY_MES_QA.dbo.GRIDDETAIL SELECT * FROM DY_MES.dbo.GRIDDETAIL ";
		SqlMesTemplate.update(sInsertSql);
		
		logger.info("########## DatabaseSyncQA Execute Complete --- GRIDDETAIL ##########");
		
		sDeleteSql = "DELETE FROM DY_MES_QA.dbo.ENUMDEFINITION ";
		SqlMesTemplate.update(sDeleteSql);
		sInsertSql = "INSERT INTO DY_MES_QA.dbo.ENUMDEFINITION SELECT * FROM DY_MES.dbo.ENUMDEFINITION ";
		SqlMesTemplate.update(sInsertSql);
		
		logger.info("########## DatabaseSyncQA Execute Complete --- ENUMDEFINITION ##########");
		
		sDeleteSql = "DELETE FROM DY_MES_QA.dbo.ENUMVALUE ";
		SqlMesTemplate.update(sDeleteSql);
		sInsertSql = "INSERT INTO DY_MES_QA.dbo.ENUMVALUE SELECT * FROM DY_MES.dbo.ENUMVALUE ";
		SqlMesTemplate.update(sInsertSql);
		
		logger.info("########## DatabaseSyncQA Execute Complete --- ENUMVALUE ##########");
	}
}