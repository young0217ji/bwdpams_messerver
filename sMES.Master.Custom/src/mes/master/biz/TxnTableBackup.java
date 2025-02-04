package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.TABLELIST;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnTableBackup implements ObjectExecuteService
{
	/**
	 * 테이블 백업하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute( Document recvDoc )
	{
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse( recvDoc );
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo( recvDoc );

		for( int i = 0; i < masterData.size(); i++ )
		{
			HashMap<String, String> dataMap = ( HashMap<String, String> )masterData.get( i );
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );

			TABLELIST backupInfo = new TABLELIST();
			backupInfo.setKeyTABLEID( dataMap.get( "TABLEID" ) );
			backupInfo.excuteBACKUP();

			TABLELIST TableAddedInfo = new TABLELIST();
			TableAddedInfo.setKeyTABLEID( dataMap.get( "TABLEID" ) + "_BACKUP" );
			TableAddedInfo.setKeyTABLENAME( dataMap.get( "TABLENAME" ) + "_BACKUP" );
			TableAddedInfo.setTABLETYPE( "Backup" );
			TableAddedInfo.setCOMMENTS( dataMap.get( "TABLEID" ) + "의 Backup테이블" );
			TableAddedInfo.excuteDML( SqlConstant.DML_INSERT );
			TableAddedInfo.excuteSetComments();
		}

		return recvDoc;
	}
}
