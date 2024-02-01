package mes.time.biz;

import java.util.HashMap;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.txninfo.TxnInfo;
import mes.lot.custom.LotCustomService;
import mes.util.NameGenerator;

/*
 ****************************************************************************
 *  PACKAGE : mes.time.biz;
 *  NAME    : DisqualificationDate.java
 *  TYPE    : JAVA
 *  DESCRIPTION : 2023-11-03. 자격인증관리의 자격상실예정일 기준으로 자격상실,자격상실일자를 업데이트한다.
 *	MODIFY	: 2023-11-03. 오유림
 ****************************************************************************
 */

public class DisqualificationDate implements Job {
	private static final transient Logger logger = LoggerFactory.getLogger(DisqualificationDate.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException 
	{	
		try
		{
			MESFrameProxy.getTxManager().begin();
			executeDisqualificationDate();
			MESFrameProxy.getTxManager().lastCommitForce();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			MESFrameProxy.getTxManager().lastRollbackForce();
		}
	}

	private void executeDisqualificationDate() throws Exception{
		//if(System.getProperty("mode").equalsIgnoreCase("DEV"))
		//	return;
		
		logger.info("########## DisqualificationDate Execute Start ##########");
		
		String sSQL = "";
		
		
		sSQL += " UPDATE DY_CERTIFICATIONMANAGEMENT                                                   \n";
		sSQL += " SET CERTIFICATIONSTATE = 'DEL'                                                      \n";
		sSQL += " 	 , DISQUALIFICATIONDATE = CONVERT(CHAR(8),GETDATE(),112)                          \n";
		sSQL += " WHERE DISQUALIFICATIONEXPECTDATE = CONVERT(CHAR(8),GETDATE(),112)                   \n";
	
		
		SqlMesTemplate.update(sSQL);
	}
}
