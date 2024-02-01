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
import mes.master.data.PROCESSROUTE;
import mes.master.data.ROCOMPOSITION;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnROComposition implements ObjectExecuteService
{
	/**
	 * 라우팅의 공정 수순을 등록, 수정, 삭제 하는 트랜잭션 실행
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
		String sProcessRouteID = "";
		
		if ( (masterData != null) && (masterData.size() > 0) )
		{
			sPlantID = masterData.get(0).get("PLANTID");
			sProcessRouteID = masterData.get(0).get("PROCESSROUTEID");
		}
		else
		{
			sPlantID = MessageParse.getParam(recvDoc, "PLANTID");
			sProcessRouteID = MessageParse.getParam(recvDoc, "PROCESSROUTEID");
		}
		
		// PROCESSROUTEID가 같은 것들을 전부 지웁니다.
        deleteCompositionTable("ROCOMPOSITION",sPlantID,sProcessRouteID);
		
		
        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            ROCOMPOSITION dataInfo = new ROCOMPOSITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPROCESSROUTEID(dataMap.get("PROCESSROUTEID"));
            dataInfo.setKeyPROCESSID(dataMap.get("PROCESSID"));
            dataInfo.setKeyPROCESSSEQUENCE(ConvertUtil.String2Long(dataMap.get("PROCESSSEQUENCE")));
            
            

	        NameGenerator nameGenerator = new NameGenerator();
	        // Key Value Setting
	        PROCESSROUTE processRouteInfo = new PROCESSROUTE();
	        processRouteInfo.setKeyPLANTID(dataMap.get("PLANTID"));
	        processRouteInfo.setKeyPROCESSROUTEID(dataMap.get("PROCESSROUTEID"));
	        processRouteInfo = (PROCESSROUTE) processRouteInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
	
	        // 신규로 등록된 항목은 RocompositionID를 새로 발번합니다.
	        if(dataMap.get("ROCOMPOSITIONID").isEmpty())
	        {
	        	String ROCOMPOSITIONID = nameGenerator.nameGenerateROCompositionID(dataInfo.getKeyPLANTID(), processRouteInfo.getPROCESSROUTECODE(), dataInfo.getKeyPROCESSID());
	        	dataInfo.setROCOMPOSITIONID(ROCOMPOSITIONID);
	        }
	        else
	        {
	        	dataInfo.setROCOMPOSITIONID(dataMap.get("ROCOMPOSITIONID"));
	        }
            
            dataInfo.setCONCURRENCYPROCESSID(dataMap.get("CONCURRENCYPROCESSID"));
            dataInfo.setCONCURRENCYSEQUENCE(ConvertUtil.String2Long(dataMap.get("CONCURRENCYSEQUENCE")));
            dataInfo.setPROCESSNAME(dataMap.get("PROCESSNAME"));
            dataInfo.setPROCESSMODE(dataMap.get("PROCESSMODE"));
            dataInfo.setCREATELOTFLAG(dataMap.get("CREATELOTFLAG"));
            dataInfo.setCREATELOTRULE(dataMap.get("CREATELOTRULE"));
            dataInfo.setCOSTCENTER(dataMap.get("COSTCENTER"));
            dataInfo.setENDOFROUTE(dataMap.get("ENDOFROUTE"));
            dataInfo.setAUTOTRACKIN(dataMap.get("AUTOTRACKIN"));
            dataInfo.setAUTOTRACKOUT(dataMap.get("AUTOTRACKOUT"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            dataInfo.setERPPROCESSCODE(dataMap.get("ERPPROCESSCODE"));
            
            dataInfo.excuteDML(SqlConstant.DML_INSERT);

            // History 기록이 필요한 경우 사용
            //AddHistory history = new AddHistory();
            //history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));

        }

        return recvDoc;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void deleteCompositionTable (String sTableName, String sPlantID, String sProcessRouteID)
	{
		String sDeleteSql = " " +
		" DELETE FROM " + sTableName + 
		" WHERE " +
		" PLANTID = :PLANTID AND " +
		" PROCESSROUTEID = :PROCESSROUTEID ";
		
		HashMap bindMap = new HashMap();
		bindMap.put("PLANTID", sPlantID);
		bindMap.put("PROCESSROUTEID", sProcessRouteID);
		SqlMesTemplate.update(sDeleteSql, bindMap);
	}

}
