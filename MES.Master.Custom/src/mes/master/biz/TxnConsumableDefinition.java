package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.BOMDETAIL;
import mes.master.data.CONSUMABLEDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnConsumableDefinition implements ObjectExecuteService
{
	/**
	 * 소모성 자재 (Consumable) 의 기준정보를  등록, 수정, 삭제 하는 트랜잭션 실행
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

            CONSUMABLEDEFINITION dataInfo = new CONSUMABLEDEFINITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyCONSUMABLEID(dataMap.get("CONSUMABLEID"));


            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (CONSUMABLEDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            dataInfo.setCONSUMABLENAME(dataMap.get("CONSUMABLENAME"));
            dataInfo.setCONSUMABLETYPE(dataMap.get("CONSUMABLETYPE"));
            dataInfo.setUNIT(dataMap.get("UNIT"));
            dataInfo.setSAFESTOCK(ConvertUtil.Object2Double(dataMap.get("SAFESTOCK")));
            dataInfo.setMAINVENDOR(dataMap.get("MAINVENDOR"));
            dataInfo.setMAKER(dataMap.get("MAKER"));
            dataInfo.setENGINEER(dataMap.get("ENGINEER"));
            dataInfo.setACCOUNTTYPE(dataMap.get("ACCOUNTTYPE"));
            dataInfo.setACCOUNTCODE(dataMap.get("ACCOUNTCODE"));


            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                BOMDETAIL oBOMDetail = new BOMDETAIL();
                oBOMDetail.setKeyPLANTID(dataInfo.getKeyPLANTID());
                oBOMDetail.setCONSUMABLEID(dataInfo.getKeyCONSUMABLEID());
                List<BOMDETAIL> liBOMDetail = (List<BOMDETAIL>) oBOMDetail.excuteDML(SqlConstant.DML_SELECTLIST);
	            
                if ( liBOMDetail.size() > 0) {
                	// 해당 기준정보 ({0}) 가 사용중입니다.
                	throw new CustomException("MD-013", new Object[] {"BOM : " + liBOMDetail.get(0).getKeyBOMID()});
                }
            	
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
        		dataInfo.setCREATETIME(DateUtil.getCurrentTimestamp());
            	dataInfo.setCREATEUSERID(txnInfo.getTxnUser());

            	//
        		dataInfo.setLASTUPDATETIME(dataInfo.getCREATETIME());
            	dataInfo.setLASTUPDATEUSERID(dataInfo.getCREATEUSERID());
            	
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") )
            {
        		dataInfo.setLASTUPDATETIME(DateUtil.getCurrentTimestamp());
            	dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());

            	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            //AddHistory history = new AddHistory();
            //history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));


        }

        return recvDoc;
    }

}
