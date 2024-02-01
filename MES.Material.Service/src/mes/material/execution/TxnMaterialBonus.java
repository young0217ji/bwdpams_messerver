package mes.material.execution;

import java.util.HashMap;
import java.util.List;

import mes.event.MessageParse;
import mes.material.data.MATERIALSTOCK;
import mes.material.transaction.MaterialService;
import mes.material.validation.MaterialValidation;
import mes.util.EventInfoUtil;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnMaterialBonus implements ObjectExecuteService 
{
	@Override
	public Object execute(Document recvDoc) 
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);		
		MaterialService service = new MaterialService();
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			MATERIALSTOCK dataInfo = new MATERIALSTOCK();
			
			// Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyYYYYMM(dataMap.get("YYYYMM"));
            dataInfo.setKeyWAREHOUSEID(dataMap.get("WAREHOUSEID"));
            dataInfo.setKeyMATERIALTYPE(dataMap.get("MATERIALTYPE"));
            dataInfo.setKeyMATERIALID(dataMap.get("MATERIALID"));
            dataInfo.setKeyMATERIALLOTID(dataMap.get("MATERIALLOTID"));
            
            // Data Value Setting
            dataInfo = (MATERIALSTOCK) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            dataInfo.setLASTEVENTTIMEKEY(txnInfo.getEventTimeKey());
            dataInfo.setBONUSQTY(ConvertUtil.String2Double(dataMap.get("BONUSQTY")));
            dataInfo.setSCRAPQTY(ConvertUtil.String2Double(dataMap.get("SCRAPQTY")));
            dataInfo.setSTOCKQTY(ConvertUtil.String2Double(dataMap.get("STOCKQTY")));
            dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            
            // 마이너스 재고 validation           
            if( MaterialValidation.checkStockPolicy( dataInfo, MaterialValidation.CHECK_MINUSSTOCK | MaterialValidation.CHECK_MONTHSTATE ) )
			{
            	dataInfo.excuteDML( SqlConstant.DML_UPDATE );
            	// History 기록
            	String reasonCode = dataMap.get("REASONCODE");
                Double qty = ConvertUtil.String2Double(dataMap.get("QTY"));
                service.addMaterialHistory(dataInfo, txnInfo, txnInfo.getTxnId(), reasonCode, qty);   
			}           
		}
		return recvDoc;
	}
}
