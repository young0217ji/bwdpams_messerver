package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.CONSUMABLECONDITIONSET;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnConsumableConditionSet  implements ObjectExecuteService
{
	/**
	 * 라우팅의 원료투입정보를 등록, 수정, 삭제 하는 트랜잭션 실행
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

	        
			String sPlantID = "";
			String sProductID = "";
			
			if (masterData != null)
			{
				sPlantID = masterData.get(0).get("PLANTID");
				sProductID = masterData.get(0).get("PRODUCTID");
			}
			
			// PROCESSROUTEID가 같은 것들을 전부 지웁니다.
			deleteConsumableConditionSetTable("CONSUMABLECONDITIONSET",sPlantID,sProductID);
			

	        for (int i = 0 ; i < masterData.size(); i ++)
	        {
	            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
	            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

	            if(!dataMap.get("ROCCOMPOSITIONID").isEmpty())
	            {
		            CONSUMABLECONDITIONSET dataInfo = new CONSUMABLECONDITIONSET();
		            // Key Value Setting
		            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
		            dataInfo.setKeyPRODUCTID(dataMap.get("PRODUCTID"));
		            dataInfo.setKeyROCCOMPOSITIONID(dataMap.get("ROCCOMPOSITIONID"));
		            dataInfo.setKeyBOMID(dataMap.get("BOMID"));
		            dataInfo.setKeyBOMVERSION(dataMap.get("BOMVERSION"));
		            dataInfo.setKeyBOMINDEX(ConvertUtil.String2Long(dataMap.get("BOMINDEX")));
		            dataInfo.setKeyCONSUMABLEID(dataMap.get("CONSUMABLEID"));
		            
		            
		            // Data Value Setting
		            dataInfo.setORDERINDEX(ConvertUtil.String2Long(dataMap.get("ORDERINDEX")));
		            dataInfo.setFEEDINGMODE(dataMap.get("FEEDINGMODE"));
		            dataInfo.setFEEDINGDESCRIPTION(dataMap.get("FEEDINGDESCRIPTION"));
		            dataInfo.setFEEDINGRATE(ConvertUtil.Object2Double(dataMap.get("FEEDINGRATE")));
		            dataInfo.setRECYCLEFLAG(dataMap.get("RECYCLEFLAG"));
		            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
		            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
		            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
	
		            dataInfo.excuteDML(SqlConstant.DML_INSERT);
	
		            
		            // History 기록이 필요한 경우 사용
		            //AddHistory history = new AddHistory();
		            //history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
	            }

	        }

	        return recvDoc;
	    }
	 
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		private void deleteConsumableConditionSetTable (String sTableName, String sPlantID, String sProductID)
		{
			String sDeleteSql = " " +
			" DELETE FROM " + sTableName + 
			" WHERE " +
			" PLANTID = :PLANTID AND " +
			" PRODUCTID = :PRODUCTID ";
			
			HashMap bindMap = new HashMap();
			bindMap.put("PLANTID", sPlantID);
			bindMap.put("PRODUCTID", sProductID);
			SqlMesTemplate.update(sDeleteSql, bindMap);
		}
}
