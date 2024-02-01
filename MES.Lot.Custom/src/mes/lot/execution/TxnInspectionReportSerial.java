package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.exception.NoDataFoundException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.lot.data.DY_INSPECTIONREPORT;
import mes.lot.data.DY_INSPECTIONREPORTSERIAL;
import mes.master.data.DY_ALARMDEFINITION;
import mes.master.data.DY_INSPECTIONREPORTDEFINITION;
import mes.master.data.DY_USERPRODUCTDEFINITION;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 20223.09.05 
 * 
 * @see
 */
public class TxnInspectionReportSerial implements ObjectExecuteService
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

            DY_INSPECTIONREPORTSERIAL dataInfo = new DY_INSPECTIONREPORTSERIAL();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyWORKORDERID(dataMap.get("WORKORDERID"));
            dataInfo.setKeyINSPECTIONSEQUENCE(ConvertUtil.String2Int(dataMap.get("INSPECTIONSEQUENCE")));
            dataInfo.setKeySERIALNUMBER(dataMap.get("SERIALNUMBER"));

			// Key 에 대한 DataInfo 조회
			if( "C".equals(dataMap.get("_ROWSTATUS")) == false ) {
				dataInfo = ( DY_INSPECTIONREPORTSERIAL )dataInfo.excuteDML( SqlConstant.DML_SELECTFORUPDATE );
			}

			// Data Value Setting
			dataInfo.setAPPEARN(dataMap.get("APPEARN"));
			dataInfo.setWORKABILITY(dataMap.get("WORKABILITY"));
			dataInfo.setPROOF_TEST(dataMap.get("PROOF_TEST"));
			dataInfo.setINTERNAL_LEAKAGE(dataMap.get("INTERNAL_LEAKAGE"));
			dataInfo.setEXTERNAL_LEAKAGE(dataMap.get("EXTERNAL_LEAKAGE"));
			dataInfo.setMIN_OPER_PRESS(dataMap.get("MIN_OPER_PRESS"));
			dataInfo.setWELDING(dataMap.get("WELDING"));
			dataInfo.setROD_SURFACE(dataMap.get("ROD_SURFACE"));
			dataInfo.setTHREAD(dataMap.get("THREAD"));
			dataInfo.setSTROKE(dataMap.get("STROKE"));
			dataInfo.setMARK_POINT(dataMap.get("MARK_POINT"));
			dataInfo.setLOTID(dataMap.get("LOTID"));
			dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
			dataInfo.setLASTUPDATEUSERID( txnInfo.getTxnUser() );
			dataInfo.setLASTUPDATETIME( txnInfo.getEventTime() );

			// DataInfo에 대한 CUD 실행
			if( "U".equals(dataMap.get("_ROWSTATUS")) ) {
				dataInfo.excuteDML( SqlConstant.DML_UPDATE );
				
				// History 기록이 필요한 경우 사용
				AddHistory history = new AddHistory();
				history.addHistory( dataInfo, txnInfo, dataMap.get("_ROWSTATUS") );
			}
        }

        return recvDoc;
    }
}
