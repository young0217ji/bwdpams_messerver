package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
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
public class TxnTableTruncate implements ObjectExecuteService
{
	/**
	 * 테이블 자르는 트랜잭션 실행
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

			TABLELIST dataInfo = new TABLELIST();
			dataInfo.setKeyTABLEID( dataMap.get( "TABLEID" ) );
			dataInfo.excuteTRUNCATE();
		}

		return recvDoc;
	}
}
