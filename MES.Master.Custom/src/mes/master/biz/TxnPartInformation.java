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
import mes.master.data.DY_PARTINFORMATION;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.12.21 
 * 
 * @see
 */
public class TxnPartInformation implements ObjectExecuteService
{
	/**
	 * PART INFORMATION의 기본 속성을 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws 
	 * 
	 */
	@Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo oTxnInfo = EventInfoUtil.setTxnInfo(recvDoc);

        for (int i = 0 ; i < masterData.size(); i ++) {
            HashMap<String, String> mDataMap = (HashMap<String, String>)masterData.get(i);
            oTxnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_PARTINFORMATION oDataInfo = new DY_PARTINFORMATION();
            // Key Value Setting
            oDataInfo.setKeyPLANTID(mDataMap.get("PLANTID"));
            oDataInfo.setKeyWORKCENTER(mDataMap.get("WORKCENTER"));
            oDataInfo.setKeyPRODUCTID(mDataMap.get("PRODUCTID"));

            // Key 에 대한 DataInfo 조회
            if ( "C".equals(mDataMap.get("_ROWSTATUS")) == false ) {
                oDataInfo = (DY_PARTINFORMATION) oDataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            // Data Value Setting
            oDataInfo.setFIRSTPARTINSPECTION(mDataMap.get("FIRSTPARTINSPECTION"));
            oDataInfo.setSECONDPARTINSPECTION(mDataMap.get("SECONDPARTINSPECTION"));
            oDataInfo.setJIGNUMBER(mDataMap.get("JIGNUMBER"));
            oDataInfo.setDESCRIPTION(mDataMap.get("DESCRIPTION"));
            oDataInfo.setLASTUPDATETIME(oTxnInfo.getEventTime());
            oDataInfo.setLASTUPDATEUSERID(oTxnInfo.getTxnUser());

            // DataInfo에  대한 CUD 실행
            if ( "D".equals(mDataMap.get("_ROWSTATUS")) ) {
                oDataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( "C".equals(mDataMap.get("_ROWSTATUS")) ) { 
            	
                oDataInfo.setCREATEUSERID(oTxnInfo.getTxnUser());
                oDataInfo.setCREATETIME(oTxnInfo.getEventTime());
                oDataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( "U".equals(mDataMap.get("_ROWSTATUS")) ) {
                oDataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(oDataInfo, oTxnInfo, mDataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }
}
