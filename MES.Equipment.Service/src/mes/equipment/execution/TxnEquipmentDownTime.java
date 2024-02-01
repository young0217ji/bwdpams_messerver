package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.equipment.data.EQUIPMENTDOWNTIME;
import mes.event.MessageParse;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.02.22 
 * 
 * @see
 */
public class TxnEquipmentDownTime implements ObjectExecuteService 
{
	/**
	 * 비가동 정보 관리
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		for (int i = 0 ; i < masterData.size(); i ++)
		{
		    HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
		    txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

		    EQUIPMENTDOWNTIME dataInfo = new EQUIPMENTDOWNTIME();
		    // Key Value Setting
		    dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
		    dataInfo.setKeyLOTID(dataMap.get("LOTID"));
		    dataInfo.setKeyRELATIONTIMEKEY(dataMap.get("TIMEKEY"));
		    dataInfo.setKeyPROCESSID(dataMap.get("PROCESSID"));
		    dataInfo.setKeyPROCESSSEQUENCE( ConvertUtil.String2Long(dataMap.get("PROCESSSEQUENCE")) );
		    dataInfo.setKeyREASONCODE(dataMap.get("REASONCODE"));
		    
		    // Key 에 대한 DataInfo 조회
		    if ( !dataMap.get("_ROWSTATUS").equals("C") )
		    {
		        dataInfo = (EQUIPMENTDOWNTIME) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		    }
		    
		    // Data Value Setting
		    dataInfo.setDOWNTIME( ConvertUtil.String2Double(dataMap.get("DOWNTIME")) );
		    dataInfo.setEQUIPMENTID(dataMap.get("EQUIPMENTID"));
		    dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
		    
		    // DataInfo에  대한 CUD 실행
		    if ( dataMap.get("_ROWSTATUS").equals("D") )
		    {
		        dataInfo.excuteDML(SqlConstant.DML_DELETE);
		    }
		    else if ( dataMap.get("_ROWSTATUS").equals("C") )
		    { 
		        dataInfo.excuteDML(SqlConstant.DML_INSERT);
		    }
		    else if ( dataMap.get("_ROWSTATUS").equals("U") )
		    {
		        dataInfo.excuteDML(SqlConstant.DML_UPDATE);
		    }

		    // History 기록이 필요한 경우 사용
		    AddHistory history = new AddHistory();
		    history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
		}
		return recvDoc;
	}
}
