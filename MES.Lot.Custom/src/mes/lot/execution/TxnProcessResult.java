package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.lot.data.DY_PROCESSRESULT;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 20223.09.05 
 * 
 * @see
 */
public class TxnProcessResult implements ObjectExecuteService
{
	/**
	 * 검사성적서 Report Serial을  수정하는 트랜잭션 실행
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

        for (int i = 0 ; i < masterData.size(); i ++) {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_PROCESSRESULT dataInfo = new DY_PROCESSRESULT();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyLOTID(dataMap.get("LOTID"));
            dataInfo.setKeyPROCESSID(dataMap.get("PROCESSID"));
            dataInfo.setKeyPROCESSSEQUENCE(ConvertUtil.String2Long(dataMap.get("PROCESSSEQUENCE")));
            dataInfo.setKeyREWORKCOUNT(ConvertUtil.String2Long(dataMap.get("REWORKCOUNT")));

			// Key 에 대한 DataInfo 조회
			if( "C".equals(dataMap.get("_ROWSTATUS")) == false ) {
				dataInfo = ( DY_PROCESSRESULT )dataInfo.excuteDML( SqlConstant.DML_SELECTFORUPDATE );
			}

			// Data Value Setting
			dataInfo.setWORKORDERID(dataMap.get("WORKORDERID"));
			dataInfo.setERPPROCESSCODE(dataMap.get("ERPPROCESSCODE"));
			dataInfo.setPRODUCTORDERTYPE(dataMap.get("PRODUCTORDERTYPE"));
			dataInfo.setPRODUCTQUANTITY(ConvertUtil.String2Double4Zero(dataMap.get("PRODUCTQUANTITY")));
			dataInfo.setPRODUCTUNIT(dataMap.get("PRODUCTUNIT"));
			dataInfo.setSCRAPQUANTITY(ConvertUtil.String2Double4Zero(dataMap.get("SCRAPQUANTITY")));
			dataInfo.setSTARTTIME(DateUtil.getTimestamp(dataMap.get("STARTTIME")) );
			dataInfo.setENDTIME(DateUtil.getTimestamp(dataMap.get("ENDTIME")) );
			dataInfo.setPROCESSTIME(ConvertUtil.String2Long(dataMap.get("PROCESSTIME")));
			dataInfo.setWORKTIME(ConvertUtil.String2Long(dataMap.get("WORKTIME")));
			dataInfo.setMACHINETIME(ConvertUtil.String2Long(dataMap.get("MACHINETIME")));
			dataInfo.setINTERFACEKEY(dataMap.get("INTERFACEKEY"));
			dataInfo.setEQUIPMENTID(dataMap.get("ROD_SURFACE"));
			dataInfo.setPROCESSDOWNTIME(ConvertUtil.String2Long(dataMap.get("PROCESSDOWNTIME")));
			dataInfo.setDEFECTQUANTITY(ConvertUtil.String2Double4Zero(dataMap.get("DEFECTQUANTITY")));
			dataInfo.setPRODUCTORDERID(dataMap.get("PRODUCTORDERID"));
			dataInfo.setLASTUPDATEUSERID( txnInfo.getTxnUser() );
			dataInfo.setLASTUPDATETIME( txnInfo.getEventTime() );

			// DataInfo에 대한 CUD 실행
			if( "U".equals(dataMap.get("_ROWSTATUS")) ) {
				dataInfo.excuteDML( SqlConstant.DML_UPDATE );
				
				// History 기록이 필요한 경우 사용
//				AddHistory history = new AddHistory();
//				history.addHistory( dataInfo, txnInfo, dataMap.get("_ROWSTATUS") );
			}
        }

        return recvDoc;
    }
}
