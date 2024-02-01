package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.BOMDEFINITION;
import mes.master.data.CONSUMABLEDEFINITION;
import mes.master.data.DY_USERPRODUCTDEFINITION;
import mes.userdefine.data.PRODUCTDEFINITION_UDF;
import mes.util.CustomQueryUtil;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnProductLabelManagement implements ObjectExecuteService
{
	/**
	 * 제품 기준정보를  등록, 수정, 삭제 하는 트랜잭션 실행
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

            DY_USERPRODUCTDEFINITION dataInfo = new DY_USERPRODUCTDEFINITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPRODUCTID(dataMap.get("PRODUCTID"));


            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (DY_USERPRODUCTDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            dataInfo.setENDPRODUCTID(dataMap.get("ENDPRODUCTID"));
            dataInfo.setENDPRODUCTNAME(dataMap.get("ENDPRODUCTNAME"));
            dataInfo.setUSERPRODUCTID(dataMap.get("USERPRODUCTID"));
            dataInfo.setLABELTYPE(dataMap.get("LABELTYPE"));
            dataInfo.setCUSTOMERNAME(dataMap.get("CUSTOMERNAME"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setDOOSANCLASS(dataMap.get("DOOSANCLASS"));
            dataInfo.setCUSTOMERNAME2(dataMap.get("CUSTOMERNAME2"));
            dataInfo.setUSERPRODUCTID2(dataMap.get("USERPRODUCTID2"));
            dataInfo.setCUSTOMERNAME3(dataMap.get("CUSTOMERNAME3"));
            dataInfo.setUSERPRODUCTID3(dataMap.get("USERPRODUCTID3"));
            dataInfo.setCUSTOMERNAME4(dataMap.get("CUSTOMERNAME4"));
            dataInfo.setUSERPRODUCTID4(dataMap.get("USERPRODUCTID4"));
            dataInfo.setLOCALGROUP(dataMap.get("LOCALGROUP"));
            dataInfo.setHYUNDAICODE(dataMap.get("HYUNDAICODE"));
            dataInfo.setHYUNDAIMODEL(dataMap.get("HYUNDAIMODEL"));
            dataInfo.setUSEFLAG(dataMap.get("USEFLAG"));

            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
            	dataInfo.setCREATETIME(txnInfo.getEventTime());
                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") )
            {
            	dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }

}
