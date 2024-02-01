package mes.time.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.orm.sql.SqlMesTemplate;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

/*
 ****************************************************************************
 *  PACKAGE : mes.time.biz;
 *  NAME    : EmLoadingQISDataTimer.java
 *  TYPE    : JAVA
 *  DESCRIPTION : 2013-02-18. EMC관련 QIS 데이터 Loading
 *	MODIFY	: 2013-03-25. KJH
 ****************************************************************************
 */

public class CronTestTimer implements Job 
{
	private static final transient Logger logger = LoggerFactory.getLogger(CronTestTimer.class);
	
	public void executeCronTestTimer() throws Exception
	{		
		//Log
		logger.info("========= CronTestTimer Execute ===========");
		
	}
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException 
	{	
		try
		{
			MESFrameProxy.getTxManager().begin();
			executeCronTestTimer();
			MESFrameProxy.getTxManager().lastCommitForce();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			MESFrameProxy.getTxManager().lastRollbackForce();
		}
	}
}