package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_ALARMDEFINITION;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.10.13 
 * 
 * @see
 */
public class TxnAlarmDefinition implements ObjectExecuteService
{
	/**
	 * 이상관리기준정보
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

		for( int i = 0; i < masterData.size(); i++ ) {
			HashMap<String, String> dataMap = ( HashMap<String, String> )masterData.get( i );
			txnInfo.setEventTimeKey( DateUtil.getCurrentEventTimeKey() );

			DY_ALARMDEFINITION oDataInfo = new DY_ALARMDEFINITION();

			// Key Value Setting
			oDataInfo.setKeyPLANTID( dataMap.get("PLANTID") );
			oDataInfo.setKeyALARMID( dataMap.get("ALARMID") );
			oDataInfo.setKeyWORKCENTER( dataMap.get("WORKCENTER") );

			// Key 에 대한 DataInfo 조회
			if( "C".equals(dataMap.get("_ROWSTATUS")) == false ) {
				oDataInfo = ( DY_ALARMDEFINITION )oDataInfo.excuteDML( SqlConstant.DML_SELECTFORUPDATE );
			}

			// Data Value Setting
			oDataInfo.setROLEID(dataMap.get("ROLEID"));
			oDataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
			oDataInfo.setLASTUPDATEUSERID( txnInfo.getTxnUser() );
			oDataInfo.setLASTUPDATETIME( txnInfo.getEventTime() );

			// DataInfo에 대한 CUD 실행
			if( "D".equals(dataMap.get("_ROWSTATUS")) ) {
				oDataInfo.excuteDML( SqlConstant.DML_DELETE );

			}
			else if( "C".equals(dataMap.get("_ROWSTATUS")) ) {
				oDataInfo.setCREATEUSERID( txnInfo.getTxnUser() );
				oDataInfo.setCREATETIME( txnInfo.getEventTime() );
				oDataInfo.excuteDML( SqlConstant.DML_INSERT );

			}
			else if( "U".equals(dataMap.get("_ROWSTATUS")) ) {
				oDataInfo.excuteDML( SqlConstant.DML_UPDATE );
			}

			// History 기록이 필요한 경우 사용
			AddHistory history = new AddHistory();
			history.addHistory( oDataInfo, txnInfo, dataMap.get("_ROWSTATUS") );
		}

		return recvDoc;
	}
}
