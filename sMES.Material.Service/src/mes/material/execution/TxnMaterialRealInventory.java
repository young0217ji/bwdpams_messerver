package mes.material.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageAdaptor;
import mes.event.MessageParse;
import mes.material.data.MATERIALSTOCK;
import mes.util.EventInfoUtil;
import mes.material.transaction.MaterialService;
import mes.material.validation.MaterialValidation;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnMaterialRealInventory implements ObjectExecuteService
{
    @SuppressWarnings("unchecked")
	@Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        MaterialService services = new MaterialService();

    	// dataMap : UI 데이터
    	// dataInfo : DB 데이터
        HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(0);
        txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

        MATERIALSTOCK dataInfo = new MATERIALSTOCK();
        
        // Key Value Setting (UI 데이터 매핑)
        dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        dataInfo.setKeyYYYYMM(dataMap.get("YYYYMM"));
        dataInfo.setKeyWAREHOUSEID(dataMap.get("WAREHOUSEID"));
        dataInfo.setKeyMATERIALTYPE(dataMap.get("MATERIALTYPE"));
        dataInfo.setKeyMATERIALID(dataMap.get("MATERIALID"));
        dataInfo.setKeyMATERIALLOTID(dataMap.get("MATERIALLOTID"));
        
        // DML_SELECTLIST 키 값 바인딩
        // DB데이터의 LotID와 UI데이터의 LotID가 같을 때, 현 재고수량(stockQTY)이 같은지 비교
        List<MATERIALSTOCK> listMaterialStock = (List<MATERIALSTOCK>) dataInfo.excuteDML(SqlConstant.DML_SELECTLIST);
        
        // Data Value Setting (UI 데이터 매핑)
        dataInfo.setRECEIPTDATE(MakeReciptdate(dataMap.get("RECEIPTDATE")));
        dataInfo.setVENDOR(dataMap.get("VENDOR"));
        dataInfo.setLASTEVENTTIMEKEY(dataMap.get("LASTEVENTTIMEKEY"));
        dataInfo.setOPENINGQTY(ConvertUtil.String2Double(dataMap.get("OPENINGQTY")));
        dataInfo.setINQTY(ConvertUtil.String2Double(dataMap.get("INQTY")));
        dataInfo.setBONUSQTY(ConvertUtil.String2Double(dataMap.get("BONUSQTY")));
        dataInfo.setCONSUMEQTY(ConvertUtil.String2Double(dataMap.get("CONSUMEQTY")));
        dataInfo.setSCRAPQTY(ConvertUtil.String2Double(dataMap.get("SCRAPQTY")));
        dataInfo.setOUTQTY(ConvertUtil.String2Double(dataMap.get("OUTQTY")));
        dataInfo.setHOLDQTY(ConvertUtil.String2Double(dataMap.get("HOLDQTY")));
        dataInfo.setSTOCKQTY(ConvertUtil.String2Double(dataMap.get("REALQTY")));
       
        // Key 데이터 있으면 Update
        if (listMaterialStock.size() != 0)
        {
            // 현재 DB의 stockQty 값
            Double currentSTOCKQTY = listMaterialStock.get(0).getSTOCKQTY();

            // Update 값 할당 및 계산
            Double newStockQty = ConvertUtil.String2Double(dataMap.get("REALQTY")); // 바뀐 현 재고수량 (UI 데이터)
            Double stockQtyGap = ConvertUtil.doubleSubtract(newStockQty, currentSTOCKQTY); // 현 재고수량의 차이 값

            Double bonusQtyCalc = ConvertUtil.doubleAdd(dataInfo.getBONUSQTY(), stockQtyGap); // Update 될  Bonus 수량
            Double scrapQtyCalc = ConvertUtil.doubleSubtract(dataInfo.getSCRAPQTY(), stockQtyGap); // Update 될 Scrap 수량

            // eventComment
            txnInfo.setTxnComment("재고실사 변경현황");

    		// DB stockQty < UI stockQty
    		if (listMaterialStock.get(0).getSTOCKQTY().compareTo(dataInfo.getSTOCKQTY()) == -1)
    		{
    			dataInfo.setBONUSQTY(bonusQtyCalc);
    			dataInfo.setSTOCKQTY(newStockQty);

    			if( MaterialValidation.checkStockPolicy(dataInfo, MaterialValidation.CHECK_MINUSSTOCK | MaterialValidation.CHECK_MONTHSTATE) )
    			{
        			dataInfo.excuteDML(SqlConstant.DML_UPDATE);
        			
                    // History 기록이 필요한 경우 사용
                    services.addMaterialHistory(dataInfo, txnInfo, "TxnMaterialBonusByReal", dataInfo.getSTOCKQTY());
    			}
                
    		}
    		// DB stockQty > UI stockQty
    		else if (listMaterialStock.get(0).getSTOCKQTY().compareTo(dataInfo.getSTOCKQTY()) == 1)
    		{
    			dataInfo.setSCRAPQTY(scrapQtyCalc);
    			dataInfo.setSTOCKQTY(newStockQty);

    			if( MaterialValidation.checkStockPolicy(dataInfo, MaterialValidation.CHECK_MINUSSTOCK | MaterialValidation.CHECK_MONTHSTATE) )
    			{
        			dataInfo.excuteDML(SqlConstant.DML_UPDATE);
        			
                    // History 기록이 필요한 경우 사용
                    services.addMaterialHistory(dataInfo, txnInfo, "TxnMaterialScrapByReal", dataInfo.getSTOCKQTY());
    			}
    		}
        }
        // Key 데이터 없으면 Insert
        else if (listMaterialStock.size() == 0)
        {
        	// Key 값 비어있으면 error
        	if (dataInfo.getKeyPLANTID().isEmpty() || dataInfo.getKeyPLANTID() == null 
        			|| dataInfo.getKeyYYYYMM().isEmpty() || dataInfo.getKeyYYYYMM() == null 
        			|| dataInfo.getKeyWAREHOUSEID().isEmpty() || dataInfo.getKeyWAREHOUSEID() == null 
        			|| dataInfo.getKeyMATERIALTYPE().isEmpty() || dataInfo.getKeyMATERIALTYPE() == null 
        			|| dataInfo.getKeyMATERIALID().isEmpty() || dataInfo.getKeyMATERIALID() == null 
        			|| dataInfo.getKeyMATERIALLOTID().isEmpty() || dataInfo.getKeyMATERIALLOTID() == null)
        	{
            	MessageAdaptor msg = new MessageAdaptor();
            	try 
            	{
    				recvDoc = msg.createReplyDocument(recvDoc, "9999;Error;WrongValue;", "ErrType");
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        	else
        	{
    	        // Data Value Setting
        		String receiptDate			= MakeReciptdate(dataMap.get("RECEIPTDATE"));
    			String vendor				= dataMap.get("VENDOR");
    			Double openingQty			= (double) 0;
    			Double bonusQty				= ConvertUtil.String2Double(dataMap.get("REALQTY"));
    			Double consumeQty			= (double) 0;
    			Double scrapQty				= (double) 0;
    			Double inQty				= (double) 0;
    			Double outQty				= (double) 0;
    			Double holdQty				= (double) 0;
    			Double stockQty				= bonusQty;

    			// To창고 Insert
    			dataInfo.setRECEIPTDATE(receiptDate);
    			dataInfo.setVENDOR(vendor);
    			dataInfo.setLASTEVENTTIMEKEY(txnInfo.getEventTimeKey());
    			dataInfo.setOPENINGQTY(openingQty);
    			dataInfo.setBONUSQTY(bonusQty);
    			dataInfo.setCONSUMEQTY(consumeQty);
    			dataInfo.setSCRAPQTY(scrapQty);
    			dataInfo.setOUTQTY(outQty);
    			dataInfo.setHOLDQTY(holdQty);
    			dataInfo.setINQTY(inQty);
    			dataInfo.setSTOCKQTY(stockQty);

    			if( MaterialValidation.checkStockPolicy(dataInfo, MaterialValidation.CHECK_MINUSSTOCK | MaterialValidation.CHECK_MONTHSTATE) )
    			{
        			dataInfo.excuteDML(SqlConstant.DML_INSERT);
        			
                    // eventComment
                    txnInfo.setTxnComment("재고실사 변경현황");
                    
                    // History 기록이 필요한 경우 사용
                    services.addMaterialHistory(dataInfo, txnInfo, "TxnMaterialCreateByReal", dataInfo.getSTOCKQTY());
    			}
        	}
        }
   
        return recvDoc;       
    }
    
    private String MakeReciptdate(String receiptDate)
    {
        String[] arrReceiptdate = receiptDate.split("-");
        String ResultReceiptdate = "";
        
        for (int i=0; i<arrReceiptdate.length; i++)
        {
        	ResultReceiptdate +=  arrReceiptdate[i];
        }
        
        return ResultReceiptdate;
    }
}
