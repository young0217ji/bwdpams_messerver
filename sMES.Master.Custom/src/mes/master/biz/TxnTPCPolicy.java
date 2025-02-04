package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.TPCPOLICY;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnTPCPolicy implements ObjectExecuteService
{
	/**
	 * 제품으로 설정된 라우팅의 공정조건을 등록, 수정, 삭제 하는 트랜잭션 실행
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

	            TPCPOLICY dataInfo = new TPCPOLICY();
	            // Key Value Setting
	            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
	            dataInfo.setKeyPRODUCTID(dataMap.get("PRODUCTID"));
	            dataInfo.setKeyCOMPOSITIONID(dataMap.get("COMPOSITIONID"));
	            dataInfo.setKeyRECIPEPARAMETERID(dataMap.get("RECIPEPARAMETERID"));
	            dataInfo.setKeyBOMID(dataMap.get("BOMID"));
	            dataInfo.setKeyBOMVERSION(dataMap.get("BOMVERSION"));
	            
	            // Key 에 대한 DataInfo 조회
	            if ( !dataMap.get("_ROWSTATUS").equals("C") )
	            {
	                dataInfo = (TPCPOLICY) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
	            }
	            
	            
	            // Data Value Setting
	            dataInfo.setSPECTYPE(dataMap.get("SPECTYPE"));
	            dataInfo.setTARGET(dataMap.get("TARGET"));
	            dataInfo.setLOWERSPECLIMIT(dataMap.get("LOWERSPECLIMIT"));
	            dataInfo.setUPPERSPECLIMIT(dataMap.get("UPPERSPECLIMIT"));
	            dataInfo.setLOWERCONTROLLIMIT(dataMap.get("LOWERCONTROLLIMIT"));
	            dataInfo.setUPPERCONTROLLIMIT(dataMap.get("UPPERCONTROLLIMIT"));
	            dataInfo.setLOWERSCREENLIMIT(dataMap.get("LOWERSCREENLIMIT"));
	            dataInfo.setUPPERSCREENLIMIT(dataMap.get("UPPERSCREENLIMIT"));
	            dataInfo.setOBJECTTYPE(dataMap.get("OBJECTTYPE"));
	            dataInfo.setCTPFLAG(dataMap.get("CTPFLAG"));
	            dataInfo.setRECIPEPARAMETERMODE(dataMap.get("RECIPEPARAMETERMODE"));
	            dataInfo.setORDERINDEX(ConvertUtil.String2Long(dataMap.get("ORDERINDEX")));
	            dataInfo.setALARMID(dataMap.get("ALARMID"));
	            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
	            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
	            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());

	             
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
	            //AddHistory history = new AddHistory();
	            //history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
	        }

	        return recvDoc;
	    }
}
