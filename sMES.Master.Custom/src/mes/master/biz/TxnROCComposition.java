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
import mes.master.data.ROCCOMPOSITION;
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
public class TxnROCComposition implements ObjectExecuteService
{
	/**
	 * 라우팅의 공정내 Step 수순을 등록, 수정, 삭제 하는 트랜잭션 실행
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
        deleteCompositionTable("ROCCOMPOSITION",sPlantID,sProcessRouteID);
		
        
        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            ROCCOMPOSITION dataInfo = new ROCCOMPOSITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPROCESSROUTEID(dataMap.get("PROCESSROUTEID"));
            dataInfo.setKeyPROCESSID(dataMap.get("PROCESSID"));
            dataInfo.setKeyPROCESSSEQUENCE(ConvertUtil.String2Long(dataMap.get("PROCESSSEQUENCE")));
            dataInfo.setKeyRECIPEID(dataMap.get("RECIPEID"));
            dataInfo.setKeyRECIPETYPE(dataMap.get("RECIPETYPE"));
            dataInfo.setKeyRECIPERELATIONCODE(dataMap.get("RECIPERELATIONCODE"));
            dataInfo.setKeyRECIPETYPECODE(dataMap.get("RECIPETYPECODE"));
            dataInfo.setKeyRECIPESEQUENCE(ConvertUtil.String2Long(dataMap.get("RECIPESEQUENCE")));


            // Key 에 대한 DataInfo 조회
            /*
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (ROCCOMPOSITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            */

	        NameGenerator nameGenerator = new NameGenerator();

	        PROCESSROUTE processRouteInfo = new PROCESSROUTE();
	        processRouteInfo.setKeyPLANTID(dataMap.get("PLANTID"));
	        processRouteInfo.setKeyPROCESSROUTEID(dataMap.get("PROCESSROUTEID"));
	        processRouteInfo = (PROCESSROUTE) processRouteInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

            // Data Value Setting
	        // 신규로 등록된 항목은 ROCompositionID를 새로 발번합니다.
	
	       //String ROCCOMPOSITIONID = nameGenerator.nameGenerateROCompositionID(dataInfo.getKeyPLANTID(), processRouteInfo.getPROCESSROUTECODE(), dataInfo.getKeyPROCESSID());
	       	String ROCCOMPOSITIONID = processRouteInfo.getPROCESSROUTECODE() + dataInfo.getKeyRECIPERELATIONCODE() + dataInfo.getKeyRECIPETYPECODE();
	       	dataInfo.setROCCOMPOSITIONID(ROCCOMPOSITIONID);

            dataInfo.setCONCURRENCYPROCESSID(dataMap.get("CONCURRENCYPROCESSID"));
            dataInfo.setCONCURRENCYSEQUENCE(ConvertUtil.String2Long(dataMap.get("CONCURRENCYSEQUENCE")));
            dataInfo.setRECIPENAME(dataMap.get("RECIPENAME"));
            dataInfo.setRECIPEMODE(dataMap.get("RECIPEMODE"));
            dataInfo.setCONSUMETYPE(dataMap.get("CONSUMETYPE"));
            dataInfo.setCONSUMEID(dataMap.get("CONSUMEID"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());


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
