package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.lot.data.PRODUCTORDER;
import mes.lot.validation.LotValidation;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

/**
 * @author LSMESSolution
 * 
 * @since 2023.02.20 
 * 
 * @see
 */
public class TxnProductOrder implements ObjectExecuteService
{
	/**
	 * 생산오더 정보 관리(P/O)
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
    public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> liDataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo oTxnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		NameGenerator oNameGenerator = new NameGenerator();
		LotValidation oValidation = new LotValidation();
		
		for ( int i = 0; i < liDataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) liDataList.get(i);

			oValidation.checkKeyNull( dataMap, new Object[] {"PLANTID", "PRODUCTID", "WORKORDERQTY"} );

			oTxnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

			String sPlantID				= dataMap.get("PLANTID");
			String sProductOrderID 		= dataMap.get("PRODUCTORDERID");
			
			String sProductID 			= dataMap.get("PRODUCTID");
			String sPlanStartDate		= dataMap.get("PLANSTARTDATE");
			String sPlanEndDate			= dataMap.get("PLANENDDATE");
			String sPlanOrderQty		= dataMap.get("PLANORDERQTY");
			String sProductUnit			= dataMap.get("PRODUCTUNIT");
			String sWorkorderQty		= dataMap.get("WORKORDERQTY");
			String sVendor				= dataMap.get("VENDOR");
			String sDueDate				= dataMap.get("DUEDATE");
			String sDescription			= dataMap.get("DESCRIPTION");
			
			PRODUCTORDER oDataInfo = new PRODUCTORDER();
            // Key Value Setting
			oDataInfo.setKeyPLANTID(sPlantID);
			oDataInfo.setKeyPRODUCTORDERID(sProductOrderID);
			
		    // Key 에 대한 DataInfo 조회
		    if ( !dataMap.get("_ROWSTATUS").equals("C") )
		    {
		    	oDataInfo = (PRODUCTORDER) oDataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		    }
		    else {
		    	sProductOrderID 		= oNameGenerator.nameGenerate(sPlantID, "POID", "PO");
		    	oDataInfo.setKeyPRODUCTORDERID(sProductOrderID);
		    }
		    
            // Data Value Setting
		    oDataInfo.setPRODUCTID(sProductID);
		    oDataInfo.setPLANSTARTDATE(sPlanStartDate);
		    oDataInfo.setPLANENDDATE(sPlanEndDate);
		    oDataInfo.setPLANORDERQTY(sPlanOrderQty);
		    oDataInfo.setPRODUCTUNIT(sProductUnit);
		    oDataInfo.setWORKORDERQTY(sWorkorderQty);
		    oDataInfo.setVENDOR(sVendor);
		    oDataInfo.setDUEDATE(sDueDate);
		    oDataInfo.setDESCRIPTION(sDescription);
		    
		    // DataInfo에  대한 CUD 실행
		    if ( dataMap.get("_ROWSTATUS").equals("D") )
		    {
		    	oDataInfo.excuteDML(SqlConstant.DML_DELETE);
		    }
		    else if ( dataMap.get("_ROWSTATUS").equals("C") )
		    { 
		    	oDataInfo.excuteDML(SqlConstant.DML_INSERT);
		    }
		    else if ( dataMap.get("_ROWSTATUS").equals("U") )
		    {
		    	oDataInfo.excuteDML(SqlConstant.DML_UPDATE);
		    }

		    // History 기록이 필요한 경우 사용
		    AddHistory oHistory = new AddHistory();
		    oHistory.addHistory(oDataInfo, oTxnInfo, dataMap.get("_ROWSTATUS"));
		}

		return recvDoc;
	}
}
