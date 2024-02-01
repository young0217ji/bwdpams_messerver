package mes.material.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.exception.NoDataFoundException;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.VIEWLOTTRACKING;
import mes.master.data.AREA;
import mes.master.data.STOCKMONTH;
import mes.master.data.STOCKPOLICY;
import mes.master.data.WAREHOUSE;
import mes.master.data.WAREHOUSEMAP;
import mes.material.data.MATERIALSTOCK;
import mes.material.data.MATERIALSTOCKHISTORY;
import mes.util.QueryResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class StockService
{
	
	private static final transient Logger log = LoggerFactory.getLogger( MaterialService.class );

	public boolean checkStockPolicy(String sPlantID, String sPolicyID)
	{
		STOCKPOLICY policyInfo = new STOCKPOLICY();
		policyInfo.setKeyPLANTID(sPlantID);
		String YYYYMM = GetStockMonth(sPlantID, false).getKeyYYYYMM();
		policyInfo.setKeyYYYYMM(YYYYMM);
		policyInfo.setKeyPOLICYID(sPolicyID);

    	try
    	{
    		policyInfo = (STOCKPOLICY) policyInfo.excuteDML(SqlConstant.DML_SELECTROW);

    		// 사용설정이 안되어 있으면
    		if ( Constant.ACTIVESTATE_ACTIVE.equals(policyInfo.getACTIVESTATE()) )
    		{
				if ( Constant.FLAG_YESNO_YES.equals(policyInfo.getPOLICYVALUE()) )
				{
					return true;
				}
				else
				{
					return false;
				}
    		}
    	}
    	catch ( NoDataFoundException e )
    	{
			String sMsg = "재고정책 이 존재하지 않습니다.\r\n" + 
    	                  "PLANTID  : " + sPlantID + ", YYYYMM  : " + YYYYMM + "\r\n" +
                          "POLICYID : " + sPolicyID;
			//throw new CustomException("CM-999",new Object[] {sMsg});
			log.info(sMsg);
			return false;
    	}
		return false;
	}
	
	
	public void ConsumableProcess(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
		log.info("ConsumableProcess Start");
		// 원료투입시 재고관리 여부 체크 
		if (checkStockPolicy(lotInfo.getPLANTID(), "ConsumableMaterialStockProcess"))
		{
			// 해당 월요코드의 투입량 만큼 현 재고에서 처리할 ConsumableLot List완 투입량을 가져온다.
			ConsumableStockProcess(lotInfo, viewLotInfo, txnInfo);
		}
		
		log.info("ConsumableProcess End");
	}
	
	// 해당 Lot의 원료투입처리할 ConsumableLot List를 가져온다.
	/**
	 * 원료 투입 처리
	 * 해당 Lot의 원료투입처리할 ConsumableLot List를 가져온다.
	 * 
	 * @param lotInfo
	 * @param viewLotInfo
	 * @param txnInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void ConsumableStockProcess(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
		String sConsumableID = viewLotInfo.getRECIPEPARAMETERID();
		Double dConsumableQty = ConvertUtil.Object2Double(viewLotInfo.getRESULTVALUE(), 0.0);
		String YYYYMM = GetStockMonth(lotInfo.getPLANTID(), true).getKeyYYYYMM();
		// AREAID로 WAREHOUSEMAP에 설정된 창고코드를 찾는다. 
		WAREHOUSE wareHouseInfo = GetWarehouseInfoByAreaID(lotInfo.getPLANTID(), lotInfo.getAREAID());
		// 2. 해당 창고의 ConsumableID 로 투입된 얄만큼 Consumable LotID와 투입얄을 산출한다. (이때 선입선출로 처리한다. STOCKPOLIY에 설정)
		//  - Consumable Lot ID가 존재하지 않으면 Dummy Lot 재고 0으로 생성후 처리한다. (마이너스 재고 허용시)
		MATERIALSTOCK stockInfo = new MATERIALSTOCK();
		stockInfo.setKeyPLANTID(wareHouseInfo.getKeyPLANTID());
		stockInfo.setKeyYYYYMM(YYYYMM);
		stockInfo.setKeyWAREHOUSEID(wareHouseInfo.getKeyWAREHOUSEID());
		stockInfo.setKeyMATERIALTYPE("Consumable");  // Constant 에 추가할것... 일단 Table Key에서도 빼야할듯...
		stockInfo.setKeyMATERIALID(sConsumableID);
		List<MATERIALSTOCK> listConsumableLotList = (List<MATERIALSTOCK>) stockInfo.excuteDML(SqlConstant.DML_SELECTLIST, "ORDER BY RECEIPTDATE, MATERIALLOTID");
		
		BigDecimal remainQty = new BigDecimal(dConsumableQty);

		// 재고가 없는경우 Dummy Lot 생성
		if (listConsumableLotList != null)
		{
			log.info("listConsumableLotList.size(); : " +listConsumableLotList.size());
			// Lot 목록에서 투입량 만큰 정보를 만든다.
			for (int i=0; i<listConsumableLotList.size(); i++)
			{
				log.info("before remainQty : " + remainQty.toString());
				MATERIALSTOCK materialStockInfo = listConsumableLotList.get(i);
				BigDecimal currentStockQty = new BigDecimal(materialStockInfo.getSTOCKQTY());
				BigDecimal currentHoldQty = new BigDecimal(materialStockInfo.getHOLDQTY());
				BigDecimal availableStockQty = currentStockQty.subtract(currentHoldQty);
				
				if ( availableStockQty.compareTo(new BigDecimal("0.0")) != 1 ) {

					// availableStockQty <= 0 재고 없음!
					continue;
				}
				
				BigDecimal currentConsumeQty = new BigDecimal(materialStockInfo.getCONSUMEQTY());
				BigDecimal inputQty = new BigDecimal("0.0");
				if (availableStockQty.compareTo(remainQty) == 1)  // availableStockQty > remainQty
				{
					inputQty = remainQty;
					// 잔여 투입량을 차감한다.
					remainQty = new BigDecimal("0.0");
				}
				else    // availableStockQty <= remainQty
				{
					inputQty = availableStockQty;
					// 잔여 투입량을 차감한다.
					remainQty = remainQty.subtract(availableStockQty);
				}
				log.info("inputQty : " + inputQty.toString());
	
				// inputQty 처리
				if (inputQty.compareTo(new BigDecimal("0.0")) == 1) // inputQty > 0
				{
					// 재고처리
					// CONSUMEQTY 증가,  STOCKQTY 감소
					materialStockInfo.setCONSUMEQTY(currentConsumeQty.add(inputQty).doubleValue());
					materialStockInfo.setSTOCKQTY(currentStockQty.subtract(inputQty).doubleValue());
					materialStockInfo.excuteDML(SqlConstant.DML_UPDATE);
		
					MakeMaterialStockHistory(materialStockInfo, viewLotInfo.getRELATIONTIMEKEY(), txnInfo, inputQty);
				}

				log.info("after remainQty : " + remainQty.toString());
	
				// 잔여 투입량이 0보다 작아지면 그만 처리한다.
				if (remainQty.compareTo(new BigDecimal("0.0")) != 1)  // remainQty <= 0
				{
					 break;
				}
			}
		}

		// remainQty가 남아 있으면
		// Dummy Lot 생성후 마이너스 재고 생성
		if (remainQty.compareTo(new BigDecimal("0.0")) == 1)  // remainQty > 0
		{
			// 재고체크여부 확인하여 처리
			if (Constant.FLAG_YESNO_YES.equals(wareHouseInfo.getSTOCKCHECKFLAG()))
			{
				// ({0}) 창고에 마이너스재고를 입력할 수 없습니다.
				throw new CustomException("MM-001",new Object[] {wareHouseInfo.getKeyWAREHOUSEID()});
			}
			stockInfo = new MATERIALSTOCK();
			stockInfo.setKeyPLANTID(wareHouseInfo.getKeyPLANTID());
			stockInfo.setKeyWAREHOUSEID(wareHouseInfo.getKeyWAREHOUSEID());
			stockInfo.setKeyMATERIALTYPE("Consumable");  // Constant 에 추가할것... 일단 Table Key에서도 빼야할듯...
			stockInfo.setKeyMATERIALID(sConsumableID);
			stockInfo.setKeyYYYYMM(YYYYMM);
			stockInfo.setKeyMATERIALLOTID("Z99999999");
			BigDecimal zeroQty = new BigDecimal("0.0");

			List<MATERIALSTOCK> listCheck = (List<MATERIALSTOCK>) stockInfo.excuteDML(SqlConstant.DML_SELECTLIST);
			if (listCheck.size() > 0)
			{
				// 존재하면 Update
				stockInfo = (MATERIALSTOCK) stockInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

				BigDecimal currentConsumeQty = new BigDecimal(stockInfo.getCONSUMEQTY());
				BigDecimal currentStockQty = new BigDecimal(stockInfo.getSTOCKQTY());
				stockInfo.setCONSUMEQTY(currentConsumeQty.add(remainQty).doubleValue());
				stockInfo.setSTOCKQTY(currentStockQty.subtract(remainQty).doubleValue());
				
				stockInfo.excuteDML(SqlConstant.DML_UPDATE);
			}
			else
			{
				// 존재하지 않으면 Insert
				String receiptDate = DateUtil.getCurrentTime(DateUtil.FORMAT_SIMPLE_DAY);
				stockInfo.setRECEIPTDATE(receiptDate);
				stockInfo.setVENDOR("");
				stockInfo.setLASTEVENTTIMEKEY(txnInfo.getEventTimeKey());
				stockInfo.setOPENINGQTY(zeroQty.doubleValue());
				stockInfo.setINQTY(zeroQty.doubleValue());
				stockInfo.setBONUSQTY(zeroQty.doubleValue());
				stockInfo.setSCRAPQTY(zeroQty.doubleValue());
				stockInfo.setOUTQTY(zeroQty.doubleValue());
				stockInfo.setHOLDQTY(zeroQty.doubleValue());
				
				stockInfo.setCONSUMEQTY(remainQty.doubleValue());
				stockInfo.setSTOCKQTY(zeroQty.subtract(remainQty).doubleValue());    // StockQty = 0 - remainQty
				
				stockInfo.excuteDML(SqlConstant.DML_INSERT);
			}
			
			MakeMaterialStockHistory(stockInfo, viewLotInfo.getRELATIONTIMEKEY(), txnInfo, remainQty);
			
		}
		
	}
	
	public void ConsumableCancelProcess(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
		log.info("ConsumableCancelProcess Start");
		
		if (checkStockPolicy(lotInfo.getPLANTID(), "ConsumableMaterialStockProcess"))
		{
			// 해당 월요코드의 투입량 만큼 현 재고에서 처리할 ConsumableLot List완 투입량을 가져온다.
			ConsumableCancelStockProcess(lotInfo, viewLotInfo, txnInfo);
		}
		
		log.info("ConsumableCancelProcess End");
	}
	

	/**
	 * 원료 투입 취소 처리
	 * 
	 * @param lotInfo
	 * @param viewLotInfo
	 * @param txnInfo
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void ConsumableCancelStockProcess(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
		log.info("ConsumableCancelStockProcess Start");
		log.info("LOTID : " + lotInfo.getKeyLOTID());
		log.info("CONSUMABLETIMEKEY : " + viewLotInfo.getRELATIONTIMEKEY());

		String YYYYMM = GetStockMonth(lotInfo.getPLANTID(), true).getKeyYYYYMM();
		MATERIALSTOCKHISTORY history = new MATERIALSTOCKHISTORY();
		history.setKeyPLANTID(lotInfo.getPLANTID());
		//history.setKeyYYYYMM(YYYYMM);
		history.setCONSUMABLETIMEKEY(viewLotInfo.getRELATIONTIMEKEY());
    	List<MATERIALSTOCKHISTORY> listConsumableLotList = (List<MATERIALSTOCKHISTORY>) history.excuteDML(SqlConstant.DML_SELECTLIST);
    	if (listConsumableLotList.size()>0)
		{
        	for (int i=0; i<listConsumableLotList.size(); i++)
        	{
        		history = listConsumableLotList.get(i);
        		if (history.getKeyYYYYMM().equals(YYYYMM))
        		{
            		// MATERIALSTOCKHISTORY 정보를 가지고 MATERIALSTOCK을 원복처리한다.
            		// 재고 처리후 MATERIALSTOCKHISOTRY 저장한다.
            		// 처리 QTY는 MATERIALSTOCKHISTORY의 -QTY로 처리한다.
            		MATERIALSTOCK cancelStock = new MATERIALSTOCK();
            		cancelStock.setKeyPLANTID(history.getKeyPLANTID());
            		cancelStock.setKeyYYYYMM(history.getKeyYYYYMM());
            		cancelStock.setKeyWAREHOUSEID(history.getKeyWAREHOUSEID());
            		cancelStock.setKeyMATERIALID(history.getKeyMATERIALID());
            		cancelStock.setKeyMATERIALLOTID(history.getKeyMATERIALLOTID());
            		cancelStock.setKeyMATERIALTYPE(history.getKeyMATERIALTYPE());
            		
            		cancelStock = (MATERIALSTOCK) cancelStock.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            		BigDecimal cancelQty = new BigDecimal(history.getQTY());
            		
        			BigDecimal currentStockQty = new BigDecimal(cancelStock.getSTOCKQTY());
        			BigDecimal currentConsumeQty = new BigDecimal(cancelStock.getCONSUMEQTY());
            		cancelStock.setCONSUMEQTY(currentConsumeQty.subtract(cancelQty).doubleValue());
            		cancelStock.setSTOCKQTY(currentStockQty.add(cancelQty).doubleValue());
            		
            		cancelStock.excuteDML(SqlConstant.DML_UPDATE);
            		
            		BigDecimal zeroQty = new BigDecimal("0.0");
            		cancelQty = zeroQty.subtract(cancelQty);
            		MakeMaterialStockHistory(cancelStock, viewLotInfo.getRELATIONTIMEKEY(), txnInfo, cancelQty);
        			
        		}
        		else
        		{
        			// 회계년도가 달라지면 처리 할수 없다.
            		log.info("ConsumableCancelStockProcess : Different YYYYMM");
            		log.info("STOCKPOLICY YYYYMM : " + YYYYMM);
            		log.info("CONSUMABLEHISTORY YYYYMM : " + history.getKeyYYYYMM());
					// 해당 원료투입의 결산월과 현재 결산월이 다릅니다. 담당자에게 문의하시기 바랍니다.
					String sMsg = "해당 원료투입의 결산월("+history.getKeyYYYYMM()+")과 현재 결산월("+YYYYMM+")이 다릅니다.\n";
					sMsg += "담당자에게 문의하시기 바랍니다.";
					throw new CustomException("CM-999",new Object[] {sMsg});
        		}
        			
        	}
		}
    	else
    	{
    		log.info("ConsumableCancelStockProcess : There is no ConsumableHistory");
    	}
    	
    	
    	
	}	
	
	public MATERIALSTOCKHISTORY MakeMaterialStockHistory(MATERIALSTOCK stockInfo, String consumableTimekey, TxnInfo txnInfo, BigDecimal inputQty) 
	{
    	MATERIALSTOCKHISTORY materialHistory = new MATERIALSTOCKHISTORY();

    	// input Key
    	materialHistory.setKeyPLANTID(stockInfo.getKeyPLANTID());
    	materialHistory.setKeyYYYYMM(stockInfo.getKeyYYYYMM());
    	materialHistory.setKeyWAREHOUSEID(stockInfo.getKeyWAREHOUSEID());
    	materialHistory.setKeyMATERIALTYPE(stockInfo.getKeyMATERIALTYPE());
    	materialHistory.setKeyMATERIALID(stockInfo.getKeyMATERIALID());
    	materialHistory.setKeyMATERIALLOTID(stockInfo.getKeyMATERIALLOTID());
    	materialHistory.setKeyTIMEKEY(txnInfo.getEventTimeKey());

    	// input Data
    	// MATERIALSTOCK Data
    	materialHistory.setRECEIPTDATE(stockInfo.getRECEIPTDATE());
    	materialHistory.setVENDOR(stockInfo.getVENDOR());
    	materialHistory.setOPENINGQTY(stockInfo.getOPENINGQTY());
    	materialHistory.setINQTY(stockInfo.getINQTY());
    	materialHistory.setBONUSQTY(stockInfo.getBONUSQTY());
    	materialHistory.setCONSUMEQTY(stockInfo.getCONSUMEQTY());
    	materialHistory.setSCRAPQTY(stockInfo.getSCRAPQTY());
    	materialHistory.setOUTQTY(stockInfo.getOUTQTY());
    	materialHistory.setHOLDQTY(stockInfo.getHOLDQTY());
    	materialHistory.setSTOCKQTY(stockInfo.getSTOCKQTY());

    	// Data created new
    	materialHistory.setREASONCODE("");
    	materialHistory.setQTY(inputQty.doubleValue());
    	materialHistory.setREFERENCETIMEKEY("");
    	materialHistory.setCONSUMABLETIMEKEY(consumableTimekey);
    	materialHistory.setEVENTNAME(txnInfo.getTxnId());
    	materialHistory.setEVENTTIME(txnInfo.getEventTime());
    	materialHistory.setEVENTUSERID(txnInfo.getTxnUser());
    	materialHistory.setEVENTCOMMENT(txnInfo.getTxnComment());
    	
    	// insert into MaterialHistory
    	materialHistory.excuteDML(SqlConstant.DML_INSERT);
    	
    	return materialHistory;
	}
	

	@SuppressWarnings("unchecked")
	public STOCKMONTH GetStockMonth(String sPlantID, boolean bCheckClosing)
	{
		STOCKMONTH stockMonthInfo = new STOCKMONTH();
		stockMonthInfo.setKeyPLANTID(sPlantID);
		
		List<STOCKMONTH> listStockMonthInfo = (List<STOCKMONTH>) stockMonthInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if (listStockMonthInfo.size() > 0)
		{
			stockMonthInfo = listStockMonthInfo.get(0);
		}
		if (bCheckClosing)
		{
			if ("Closing".equals(stockMonthInfo.getSTATE()))
			{
				// 재고 마감중입니다. 현재 처리할수 없습니다.
				String sMsg = "재고 마감중입니다. 현재 처리할수 없습니다.";
				throw new CustomException("CM-999",new Object[] {sMsg});
			}
		}
		return stockMonthInfo;
	}
	
	
	public BigDecimal GetStockQuantity(List<MATERIALSTOCK> listMaterialStock)
	{
		BigDecimal bigDecimalTotal = new BigDecimal("0");
		for (int i=0; i<listMaterialStock.size(); i++)
		{
			MATERIALSTOCK materialStockInfo = listMaterialStock.get(i);
			BigDecimal stockQty = new BigDecimal(materialStockInfo.getSTOCKQTY().doubleValue());
			bigDecimalTotal = bigDecimalTotal.add(stockQty);
		}
		log.info("-----------> TotalStockQty : " + bigDecimalTotal.toString());
		return bigDecimalTotal;
	}
	
	@SuppressWarnings("unchecked")
	public WAREHOUSE GetWarehouseInfoByAreaID(String sPlantID, String sAreaID)
	{
		WAREHOUSEMAP wareHouseMapInfo = new WAREHOUSEMAP();
		wareHouseMapInfo.setKeyPLANTID(sPlantID);
		wareHouseMapInfo.setKeyAREAID(sAreaID);
		List<WAREHOUSEMAP> listWarehouseMap = (List<WAREHOUSEMAP>) wareHouseMapInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		WAREHOUSE wareHouseInfo = new WAREHOUSE();
		if (listWarehouseMap.size() > 0)
		{
			wareHouseMapInfo = listWarehouseMap.get(0);

			// Key Value Setting
			wareHouseInfo.setKeyPLANTID(sPlantID);
			wareHouseInfo.setKeyWAREHOUSEID(wareHouseMapInfo.getKeyWAREHOUSEID());
			wareHouseInfo = (WAREHOUSE) wareHouseInfo.excuteDML(SqlConstant.DML_SELECTROW);
		}
		else
		{
			AREA areaInfo = new AREA();
			areaInfo.setKeyPLANTID(sPlantID);
			areaInfo.setKeyAREAID(sAreaID);
			areaInfo = (AREA) areaInfo.excuteDML(SqlConstant.DML_SELECTROW);
			if (areaInfo != null)
			{
				if (areaInfo.getSUPERAREAID().isEmpty())
				{
					// 창고정보가 존재하지 않습니다. 기준정보를 확인해 주세요
					String sMsg = "창고정보가 존재하지 않습니다. 창고기준정보를 확인해 주세요";
					throw new CustomException("CM-999",new Object[] {sMsg});
				}
				else
				{
					return GetWarehouseInfoByAreaID(sPlantID, areaInfo.getSUPERAREAID());
				}
			}
		}
		return wareHouseInfo;
	}	


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String GetNextMonth(String currentMonth)
	{
		String sReturn = "";
/*
		// Oracle
		//String sSql = "SELECT to_char(ADD_MONTHS(to_date(:CURRENTMONTH,'yyyymm'),1),'yyyymm') as NEXTMONTH FROM dual";
		
		// MariaDB
		//String sSql = "SELECT DATE_FORMAT(DATE_ADD(CONVERT(:CURRENTMONTH||'01',datetime), INTERVAL + 1 MONTH), '%Y%m') as NEXTMONTH FROM DUAL ";

		HashMap bindMap = new HashMap();
		bindMap.put("CURRENTMONTH", currentMonth);
		List<Object> DataList = SqlMesTemplate.queryForList(sSql, bindMap);
		if (DataList.size()>0)
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(0);
			sReturn = dataMap.get("NEXTMONTH");
		}
*/
		HashMap bindMap = new HashMap();
		bindMap.put("CURRENTMONTH", currentMonth);
		QueryResult qr = new QueryResult();
		List<LinkedCaseInsensitiveMap> DataList = qr.getQueryResult("GetNextMonth", bindMap);
		if (DataList.size()>0)
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) DataList.get(0);
			sReturn = dataMap.get("NEXTMONTH");
		}
		
		log.info("CURRENT MONTH : " + currentMonth);
		log.info("NEXT    MONTH : " + sReturn);
		return sReturn;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int insertMonthDataMaterialStockHistory(String plantID, String YYYYMM, String timeKey)
	{
		String sSql = "";
		
		/*
		// Oracle
		sSql = ""
	         + " INSERT INTO materialstockhistory \n"
             + "        ( plantid, yyyymm, warehouseid, materialtype, materialid, materiallotid, \n"
	         + "          timekey, receiptdate, vendor, \n"
	         + "          openingqty, inqty, bonusqty, consumeqty, scrapqty, outqty, holdqty, stockqty, qty, \n"
	         + "          reasoncode, referencetimekey, consumabletimekey,  \n"
	         + "          eventname, eventtime, eventuserid, eventcomment \n"
	         + "         ) \n"  
	         + " SELECT plantid, yyyymm, warehouseid, materialtype, materialid, materiallotid, \n"
	         + "        :TIMEKEY, receiptdate, vendor, \n"
	         + "        openingqty, inqty, bonusqty, consumeqty, scrapqty, outqty, holdqty, stockqty, 0, \n"
	         + "        '', '', '', \n"
	         + "        'CreateStock', sysdate, 'CloseMonth', '' \n"
	         + " FROM   materialstock \n"
	         + " WHERE 1 = 1 \n"
	         + " AND   plantid = :PLANTID \n"
	         + " AND   yyyymm = :YYYYMM \n";
		*/
		
		// MariaDB   sysdate -> now()
		sSql = ""
	         + " INSERT INTO materialstockhistory \n"
             + "        ( plantid, yyyymm, warehouseid, materialtype, materialid, materiallotid, \n"
	         + "          timekey, receiptdate, vendor, \n"
	         + "          openingqty, inqty, bonusqty, consumeqty, scrapqty, outqty, holdqty, stockqty, qty, \n"
	         + "          reasoncode, referencetimekey, consumabletimekey,  \n"
	         + "          eventname, eventtime, eventuserid, eventcomment \n"
	         + "         ) \n"  
	         + " SELECT plantid, yyyymm, warehouseid, materialtype, materialid, materiallotid, \n"
	         + "        :TIMEKEY, receiptdate, vendor, \n"
	         + "        openingqty, inqty, bonusqty, consumeqty, scrapqty, outqty, holdqty, stockqty, 0, \n"
	         + "        '', '', '', \n"
	         + "        'CreateStock', now(), 'CloseMonth', '' \n"
	         + " FROM   materialstock \n"
	         + " WHERE 1 = 1 \n"
	         + " AND   plantid = :PLANTID \n"
	         + " AND   yyyymm = :YYYYMM \n";
		
		HashMap bindMap = new HashMap();
		bindMap.put("PLANTID", plantID);
		bindMap.put("TIMEKEY", timeKey);
		bindMap.put("YYYYMM", YYYYMM);
		int iReturn = SqlMesTemplate.update(sSql, bindMap);
		return iReturn;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void MaterialStockCloseProcess(String plantID, String currentMonth)
	{
		// 1. 다음 회계년월로 재고 생성 처리
		String sNextMonth = GetNextMonth(currentMonth);
		String sTimekey = DateUtil.getCurrentEventTimeKey();
		
		String sSql = " INSERT INTO materialstock \n"
		            + "        ( plantid, yyyymm, warehouseid, materialtype, materialid, materiallotid, \n"
		            + "          receiptdate, vendor, lasteventtimekey, \n"
		            + "          openingqty, inqty, bonusqty, consumeqty, scrapqty, outqty, holdqty, stockqty \n"
		            + "         ) \n"  
		            + " SELECT plantid, :NEXTMONTH, warehouseid, materialtype, materialid, materiallotid, \n"
		            + "        receiptdate, vendor, :TIMEKEY, \n"
		            + "        stockqty, 0, 0, 0, 0, 0, holdqty, stockqty \n"
		            + " FROM   materialstock \n"
		            + " WHERE 1 = 1 \n"
		            + " AND   plantid = :PLANTID \n"
		            + " AND   yyyymm = :YYYYMM \n"
		            + " AND   stockqty <> 0 \n";

		// 최근한달이상 재고이동 이력이 없고 재고가 0 인것은 제외시키는 로직 필요함.
		// 현재는 재고량이 0 인 경우는 다음달로 이월시키지 않음.
		
		HashMap bindMap = new HashMap();
		bindMap.put("PLANTID", plantID);
		bindMap.put("YYYYMM", currentMonth);
		bindMap.put("NEXTMONTH", sNextMonth);
		bindMap.put("TIMEKEY", sTimekey);
		int iReturn = SqlMesTemplate.update(sSql, bindMap);
		log.info("1. INSERT materialstock NEXTMONTH DATA : " + iReturn);
		
		// 2. 다음회계년월 이력 생성 (MATERIALSTOCK 기준으로 MATERIALSTOCKHISTORY 생성함.)
		iReturn = insertMonthDataMaterialStockHistory(plantID, sNextMonth, sTimekey);
		log.info("2. INSERT materialstockhistory NEXTMONTH DATA : " + iReturn);
		
		// 3. 현재 회계년월 이력 생성 (MATERIALSTOCK 기준으로 MATERIALSTOCKHISTORY 생성함.)
		iReturn = insertMonthDataMaterialStockHistory(plantID, currentMonth, sTimekey);
		log.info("3. INSERT materialstockhistory CURRENTMONTH DATA : " + iReturn);

		// 4. 현재 회계년월 삭제 (MATERIALSTOCK 삭제)
		String sDeleteSql = " DELETE FROM MATERIALSTOCK "  
                          + " WHERE 1 = 1 \n"
                          + " AND   plantid = :PLANTID \n"
                          + " AND   yyyymm = :YYYYMM \n";
		bindMap = new HashMap();
		bindMap.put("PLANTID", plantID);
		bindMap.put("YYYYMM", currentMonth);
		iReturn = SqlMesTemplate.update(sDeleteSql, bindMap);
		log.info("4. DELETE materialstock CURRENTMONTH DATA : " + iReturn);
		
	}
	
	
}
