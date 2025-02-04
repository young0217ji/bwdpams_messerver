package mes.material.execution;

import java.util.HashMap;
import java.util.List;
import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.material.data.MATERIALSTOCK;
import mes.material.transaction.MaterialService;
import mes.material.validation.MaterialValidation;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnStockTransfer implements ObjectExecuteService
{			
    @Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

        // UI Size
        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            // From 창고 트랜잭션 처리
            fromWarehouseProcess(dataMap, txnInfo);
                      
            // To 창고 트랜잭션 처리
            toWarehouseProcess(dataMap, txnInfo);
        }
        
        return recvDoc;
    }
    
	public void fromWarehouseProcess(HashMap<String, String> dataMap, TxnInfo txnInfo)
	{
        MaterialService service = new MaterialService();
        MATERIALSTOCK fromWarehouseInfo = new MATERIALSTOCK();

        // Data Value Setting
        String fromWarehouse = dataMap.get("WAREHOUSEID");
        Double moveQty = ConvertUtil.String2Double(dataMap.get("MOVEQTY"));
        
        // Key Value Setting
        fromWarehouseInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        fromWarehouseInfo.setKeyYYYYMM(dataMap.get("YYYYMM"));
        fromWarehouseInfo.setKeyMATERIALTYPE(dataMap.get("MATERIALTYPE"));
        fromWarehouseInfo.setKeyMATERIALID(dataMap.get("MATERIALID"));
        fromWarehouseInfo.setKeyMATERIALLOTID(dataMap.get("MATERIALLOTID"));
        fromWarehouseInfo.setKeyWAREHOUSEID(fromWarehouse);
		
		// DB의 현재 데이터 Bind
        fromWarehouseInfo = (MATERIALSTOCK) fromWarehouseInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
        Double currentOUTQTY = fromWarehouseInfo.getOUTQTY();
        Double currentSTOCKQTY = fromWarehouseInfo.getSTOCKQTY();

        // DML_SELECTFORUPDATE 수행 후 eventTimeKey Setting
		fromWarehouseInfo.setLASTEVENTTIMEKEY(txnInfo.getEventTimeKey());

		// Double 연산
        Double outQtyCalc = ConvertUtil.doubleAdd(currentOUTQTY, moveQty);
        Double stockQtyCalc = ConvertUtil.doubleSubtract(currentSTOCKQTY, moveQty);

        fromWarehouseInfo.setOUTQTY(outQtyCalc);
        fromWarehouseInfo.setSTOCKQTY(stockQtyCalc);
		
        // 마이너스 재고 validation
    	if (MaterialValidation.checkStockPolicy( fromWarehouseInfo, MaterialValidation.CHECK_MINUSSTOCK | MaterialValidation.CHECK_MONTHSTATE ))
    	{
            fromWarehouseInfo.excuteDML(SqlConstant.DML_UPDATE);
            
            // From창고 history 기록
            service.addMaterialHistory(fromWarehouseInfo, txnInfo, "TxnOutTransfer", "", moveQty);
    	}        	
        
	}
	
	@SuppressWarnings("unchecked")
	public void toWarehouseProcess(HashMap<String, String> dataMap, TxnInfo txnInfo)
	{
        MaterialService service = new MaterialService();    
        MATERIALSTOCK toWarehouseInfo = new MATERIALSTOCK();
        
        // Data Value Setting
        String toWarehouse = dataMap.get("TOWAREHOUSENAME");
        Double moveQty = ConvertUtil.String2Double(dataMap.get("MOVEQTY"));
        
        // Key Value Setting
        toWarehouseInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        toWarehouseInfo.setKeyYYYYMM(dataMap.get("YYYYMM"));
        toWarehouseInfo.setKeyMATERIALTYPE(dataMap.get("MATERIALTYPE"));
        toWarehouseInfo.setKeyMATERIALID(dataMap.get("MATERIALID"));
        toWarehouseInfo.setKeyMATERIALLOTID(dataMap.get("MATERIALLOTID"));
        toWarehouseInfo.setKeyWAREHOUSEID(toWarehouse);

        // To창고의 해당  PLANTID, WAREHOUSEID, MATERIALLOTID 존재하는지 Validation 수행
        MATERIALSTOCK checkInfo = new MATERIALSTOCK();
        checkInfo.setKeyPLANTID(toWarehouseInfo.getKeyPLANTID());
        checkInfo.setKeyWAREHOUSEID(toWarehouse);
        checkInfo.setKeyMATERIALLOTID(toWarehouseInfo.getKeyMATERIALLOTID());
        
        // To창고에 해당  Material Lot 존재하는지 확인
		List<MATERIALSTOCK> listMaterialStock = (List<MATERIALSTOCK>) checkInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		for( int i = 0; i < listMaterialStock.size(); i++ )
		{
			// 해당 LotID 있으면 Update
    		if (listMaterialStock.get(i).getKeyMATERIALLOTID().equals(toWarehouseInfo.getKeyMATERIALLOTID()))
    		{
    			// DB의 현재 데이터 Bind
    			toWarehouseInfo = (MATERIALSTOCK) toWarehouseInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
    			
    	        // Data Value Setting
    			// DML_SELECTFORUPDATE 수행 후 eventTimeKey Setting
    	        toWarehouseInfo.setLASTEVENTTIMEKEY(txnInfo.getEventTimeKey());
    	        Double currentINQTY = toWarehouseInfo.getINQTY();
    	        Double currentSTOCKQTY = toWarehouseInfo.getSTOCKQTY();
    	        
    	        // Double 연산
    	        Double inQtyCalc = ConvertUtil.doubleAdd(currentINQTY, moveQty);
    	        Double stockQtyCalc = ConvertUtil.doubleAdd(currentSTOCKQTY, moveQty);
    	        
    	        toWarehouseInfo.setINQTY(inQtyCalc);
    	        toWarehouseInfo.setSTOCKQTY(stockQtyCalc);
    	        toWarehouseInfo.excuteDML(SqlConstant.DML_UPDATE);
                break;
    		}
		}
		// 신규면 Insert
		if( listMaterialStock.size() == 0 )
		{
	        // Data Value Setting
			String receiptDate			= dataMap.get("RECEIPTDATE");
			String vendor				= dataMap.get("VENDOR");
			Double openingQty			= (double) 0;
			Double bonusQty				= (double) 0;
			Double consumeQty			= (double) 0;
			Double scrapQty				= (double) 0;
			Double outQty				= (double) 0;
			Double holdQty				= (double) 0;

			// To창고 Insert
			toWarehouseInfo.setRECEIPTDATE(receiptDate);
			toWarehouseInfo.setVENDOR(vendor);
	        toWarehouseInfo.setLASTEVENTTIMEKEY(txnInfo.getEventTimeKey());
			toWarehouseInfo.setOPENINGQTY(openingQty);
			toWarehouseInfo.setBONUSQTY(bonusQty);
			toWarehouseInfo.setCONSUMEQTY(consumeQty);
			toWarehouseInfo.setSCRAPQTY(scrapQty);
			toWarehouseInfo.setOUTQTY(outQty);
			toWarehouseInfo.setHOLDQTY(holdQty);
	        toWarehouseInfo.setINQTY(moveQty);
	        toWarehouseInfo.setSTOCKQTY(moveQty);
	        toWarehouseInfo.excuteDML(SqlConstant.DML_INSERT);
		}

		// To창고 history 기록
        service.addMaterialHistory(toWarehouseInfo, txnInfo, "TxnInTransfer", "", moveQty);
	}


}
