package mes.durable.transaction;

import java.util.List;

import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.LOTPROCESSHISTORY;
import mes.lot.data.VIEWLOTTRACKING;
import mes.lot.transaction.LotHistory;
import mes.lot.transaction.LotTrackingService;
import mes.master.data.DURABLEDEFINITION;
import mes.master.data.DURABLEINFORMATION;
import mes.util.NameGenerator;


/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class DurableService
{
	/**
     * durableDef의 정보를 받아 새로운 Durable을 생성하고
     * NameGenerator의 규칙에 따라 만들어진 ID를 생성한 Durable에 부여한 뒤
     * insert합니다
     * 
     * TxnInfo를 기반으로 history에 기록합니다
	 *
	 * @param durableDef
	 * @param txnInfo
	 * @return
	 *
	*/
	public void create(DURABLEDEFINITION durableDef, TxnInfo txnInfo)
	{
		NameGenerator nameGenerator = new NameGenerator();
		String durableID = nameGenerator.nameGenerate(durableDef.getKeyPLANTID(), "DurableID", new Object[] {durableDef.getKeyPLANTID()});

		DURABLEINFORMATION durableInfo = new DURABLEINFORMATION();
		durableInfo.setKeyDURABLEID(durableID);
		
		durableInfo.setSTANDARDDURABLEID(durableDef.getKeySTANDARDDURABLEID());
		durableInfo.setDURABLETYPE(durableDef.getDURABLETYPE());
		durableInfo.setEXPIRYTIME(durableDef.getEXPIRYTIME());
		durableInfo.setUSAGELIMIT(durableDef.getUSAGELIMIT());
		durableInfo.setCAPACITY(durableDef.getCAPACITY());
		durableInfo.setLOTCOUNT((long) 0);
		durableInfo.setDURABLESTATE(Constant.DURABLE_STATE_CREATED);
		durableInfo.setPROCESSSTATE(Constant.DURABLE_PROCESSSTATE_WAIT);
//		durableInfo.setEQUIPMENTID("");
//		durableInfo.setPORTID("");
		durableInfo.setUSAGETIME((long) 0);
		durableInfo.setUSAGECOUNT((long) 0);
        durableInfo.setCREATEUSERID(txnInfo.getTxnUser());
        durableInfo.setCREATETIME(txnInfo.getEventTime());

        durableInfo.excuteDML(SqlConstant.DML_INSERT);

	    // History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "C");
	}
	
	/**
     * durableInfo의 상태를 changeState로 변경하고 update합니다
     * 
     * TxnInfo를 기반으로 history에 기록합니다
	 *
	 * @param durableInfo
	 * @param changeState
	 * @param txnInfo
	 * @return
	 *
	*/
	public void stateChange(DURABLEINFORMATION durableInfo, String changeState, TxnInfo txnInfo)
	{
		stateChange(durableInfo, changeState, "", "", txnInfo);
	}
	
    /**
     * durableInfo의 상태를 chagneState로 변경하고
     * reasonCode, reasonCodeType를 set한 뒤
     * update합니다
     * 
     * TxnInfo를 기반으로 history에 기록합니다
     * 
     * @param durableInfo
     * @param changeState
     * @param reasonCode
     * @param reasonCodeType
     * @param txnInfo
	 * @return
     * 
    */
	public void stateChange(DURABLEINFORMATION durableInfo, String changeState, String reasonCode, String reasonCodeType, TxnInfo txnInfo)
	{
		durableInfo.setDURABLESTATE(changeState);
		durableInfo.setREASONCODE(reasonCode);
		durableInfo.setREASONCODETYPE(reasonCodeType);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
	}
	
    /**
     * durableInfo에 할당된 모든 Lot의 CARRIERID와 ASSIGNLOT을 제거하고 lotInfo를 update한 뒤 history에 기록합니다
     * durableInfo의 LOTCOUNT를 0으로 하고 durableInfo를 update한 뒤 history에 기록합니다
     * 
     * @param durableInfo
     * @param txnInfo
	 * @return
     * 
    */
	public void removeAll(DURABLEINFORMATION durableInfo, TxnInfo txnInfo)
	{
		// Durable 이 Processing 상태인 경우는 처리되지 못하도록 체크
		checkProcessing(durableInfo);
		
		LotHistory addHistory = new LotHistory();
		
		List<Object> listLotInfo = getLotList(durableInfo);
		
		// Lot 이 Assign 된 수량과 현재 Carrier 에 담긴 수량 체크
		checkCountCompare(durableInfo, listLotInfo.size());
		
		for ( int i = 0; i < listLotInfo.size(); i++ )
		{
			LOTINFORMATION lotInfo = (LOTINFORMATION) listLotInfo.get(i);
			lotInfo.setCARRIERID("");
			lotInfo.setASSIGNSLOT("");
			lotInfo.excuteDML(SqlConstant.DML_UPDATE);
			
			// History Insert
			LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
			addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
		}
		
//		durableInfo.setEQUIPMENTID("");
//		durableInfo.setPORTID("");
		durableInfo.setLOTCOUNT((long) 0);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
	}
	
    /**
     * lofInfo의 CARRIERID에 durableInfo의 ID를, ASSIGNLOT에 durableInfo의 LOTCOUNT+1을 set하고 update한 뒤 history에 기록합니다
     * durableInfo의 LOTCOUNT를 1만큼 증가시키고 update한 뒤 history에 기록합니다
     * 
     * @param lotInfo
     * @param durableInfo
     * @param txnInfo
	 * @return
     * 
    */
	public void assign(LOTINFORMATION lotInfo, DURABLEINFORMATION durableInfo, TxnInfo txnInfo)
	{
		LotTrackingService trackingService = new LotTrackingService();
		
		// Durable 이 Processing 상태인 경우는 처리되지 못하도록 체크
		checkProcessing(durableInfo);
		
		// Lot 이 Capacity(한계용량) 까지 담기지 못하도록 체크
		checkCountOver(durableInfo);
		
		LotHistory addHistory = new LotHistory();

		lotInfo.setCARRIERID(durableInfo.getKeyDURABLEID());
		lotInfo.setASSIGNSLOT( ConvertUtil.Object2String(durableInfo.getLOTCOUNT() + 1) );
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// History Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
		
		VIEWLOTTRACKING viewLotInfo = trackingService.getCurrentViewLotInfo(lotInfo);
		
		if ( viewLotInfo != null )
		{
			durableInfo.setEQUIPMENTID(viewLotInfo.getEQUIPMENTID());
			durableInfo.setPORTID("");
		}
		else
		{
			durableInfo.setEQUIPMENTID("");
			durableInfo.setPORTID("");
		}
		durableInfo.setLOTCOUNT(durableInfo.getLOTCOUNT() + 1);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
	}
	
    /**
     * durableInfo의 특정 slot에 lotInfo를 할당합니다
     * 
     * @param LOTINFORMATION
     * @param DUABLEINFORMATION
     * @param TxnInfo
	 * @return
     * 
    */
	public void assignToSlot(LOTINFORMATION lotInfo, DURABLEINFORMATION durableInfo, String slotNumber, TxnInfo txnInfo)
	{
		LotTrackingService trackingService = new LotTrackingService();
		
		// Durable 이 Processing 상태인 경우는 처리되지 못하도록 체크
		checkProcessing(durableInfo);
		
		// Lot 이 Capacity(한계용량) 까지 담기지 못하도록 체크
		checkCountOver(durableInfo);
		
		// Slot 에 이미 Lot 이 담겨있는지 여부 체크
		checkSlotUse(durableInfo, slotNumber);
		
		LotHistory addHistory = new LotHistory();

		lotInfo.setCARRIERID(durableInfo.getKeyDURABLEID());
		lotInfo.setASSIGNSLOT(slotNumber);
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// History Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
		
		VIEWLOTTRACKING viewLotInfo = trackingService.getCurrentViewLotInfo(lotInfo);
		
		if ( viewLotInfo != null )
		{
			durableInfo.setEQUIPMENTID(viewLotInfo.getEQUIPMENTID());
			durableInfo.setPORTID("");
		}
		else
		{
			durableInfo.setEQUIPMENTID("");
			durableInfo.setPORTID("");
		}
		durableInfo.setLOTCOUNT(durableInfo.getLOTCOUNT() + 1);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
	}
	
    /**
     * durableInfo에 할당된 lotInfo를 해제합니다
     * 
     * @param lotInfo
     * @param durableInfo
     * @param txnInfo
	 * @return
     * 
    */
	public void deassign(LOTINFORMATION lotInfo, DURABLEINFORMATION durableInfo, TxnInfo txnInfo)
	{
		// Durable 이 Processing 상태인 경우는 처리되지 못하도록 체크
		checkProcessing(durableInfo);
		
		// CarrierID 와 DurableID 의 동일여부 체크
		checkIDCompare(durableInfo, lotInfo.getCARRIERID());
		
		LotHistory addHistory = new LotHistory();

		lotInfo.setCARRIERID("");
		lotInfo.setASSIGNSLOT("");
		lotInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// History Insert
		LOTPROCESSHISTORY lotProcessHistory = new LOTPROCESSHISTORY();
		addHistory.addLotProcessHistory(lotProcessHistory, lotInfo, null, txnInfo);
		
		durableInfo.setLOTCOUNT(durableInfo.getLOTCOUNT() - 1);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
	}
	
    /**
     * durableInfo를 세척합니다
     * 
     * @param durableInfo
     * @param txnInfo
	 * @return
     * 
    */
	public void clean(DURABLEINFORMATION durableInfo, TxnInfo txnInfo)
	{
		// Durable 이 Processing 상태인 경우는 처리되지 못하도록 체크
		checkProcessing(durableInfo);
		
		// 세척상태를 체크하여 Clean 상태인 경우 처리되지 않도록 체크
		checkState(durableInfo.getDURABLESTATE(), Constant.DURABLE_STATE_DIRTY);
		
		durableInfo.setDURABLESTATE(Constant.DURABLE_STATE_RELEASED);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
	}
	
    /**
     * durableInfo의 상태를 dirty로 변경합니다
     * 
     * @param durableInfo
     * @param txnInfo
	 * @return
     * 
    */
	public void dirty(DURABLEINFORMATION durableInfo, TxnInfo txnInfo)
	{
		// Durable 이 Processing 상태인 경우는 처리되지 못하도록 체크
		checkProcessing(durableInfo);
		
		// 세척상태를 체크하여 Dirty 상태인 경우 처리되지 않도록 체크
		checkState(durableInfo.getDURABLESTATE(), Constant.DURABLE_STATE_RELEASED);
		
		durableInfo.setDURABLESTATE(Constant.DURABLE_STATE_DIRTY);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
	}
	
    /**
     * 파손된 durableInfo를 수리합니다
     * 
     * @param durableInfo
     * @param txnInfo
	 * @return
     * 
    */
	public void repair(DURABLEINFORMATION durableInfo, TxnInfo txnInfo)
	{
		// Durable 이 Processing 상태인 경우는 처리되지 못하도록 체크
		checkProcessing(durableInfo);
		
		checkState(durableInfo.getDURABLESTATE(), Constant.DURABLE_STATE_BROKEN);
		
		durableInfo.setDURABLESTATE(Constant.DURABLE_STATE_RELEASED);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
		
	}
	
    /**
     * durableInfo를 폐기합니다
     * 
     * @param durableInfo
     * @param txnInfo
	 * @return
     * 
    */
	public void terminate(DURABLEINFORMATION durableInfo, TxnInfo txnInfo)
	{
		// Durable 이 사용중인 경우 폐기하지 못하도록 체크 ( LotCount 체크 )
		
		durableInfo.setDURABLESTATE(Constant.DURABLE_STATE_TERMINATE);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
	}
	
	/**************************************** Tracking ****************************************/
    /**
     * durableInfo의 상태를 Processing으로 변경하고 update합니다
     * 
     * @param durableInfo
     * @param txnInfo
     * @return
     * 
    */
	public void moveIn(DURABLEINFORMATION durableInfo, TxnInfo txnInfo)
	{
		// Durable 이 Processing 상태인 경우는 처리되지 못하도록 체크
		checkProcessing(durableInfo);
		
		checkState(durableInfo.getPROCESSSTATE(), Constant.DURABLE_PROCESSSTATE_WAIT);
		
//		durableInfo.setEQUIPMENTID("");
//		durableInfo.setPORTID("");
		durableInfo.setPROCESSSTATE(Constant.DURABLE_PROCESSSTATE_PROCESSING);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
	}
	
    /**
     * durableInfo의 상태를 Wait으로 변경하고 update합니다
     * 
     * @param durableInfo
     * @param txnInfo
     * @return 
     * 
    */
	public void moveOut(DURABLEINFORMATION durableInfo, TxnInfo txnInfo)
	{
		// Durable 이 Processing 상태인 경우에만 처리되도록 체크
		checkState(durableInfo.getPROCESSSTATE(), Constant.DURABLE_PROCESSSTATE_PROCESSING);
		
		durableInfo.setEQUIPMENTID("");
		durableInfo.setPORTID("");
		durableInfo.setPROCESSSTATE(Constant.DURABLE_PROCESSSTATE_WAIT);
		durableInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		// History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(durableInfo, txnInfo, "U");
	}
	
	/**************************************** 정보 조회 ****************************************/
    /**
     * durableInfo에 할당된 Lot을 list로 불러옵니다
     * 
     * @param durableInfo
     * @return List<Object>
     * 
    */
	@SuppressWarnings("unchecked")
	public List<Object> getLotList(DURABLEINFORMATION durableInfo)
	{
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setCARRIERID(durableInfo.getKeyDURABLEID());
		List<Object> listLotInfo = (List<Object>) lotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		return listLotInfo;
	}
	
	/**************************************** 체크 로직 ****************************************/
    /**
     * durableInfo의 상태가 Processing인지 체크합니다
     * 
     * @param DURABLEINFORMATION
     * @return 
     * @throws CustomException
     * 
    */
	public void checkProcessing(DURABLEINFORMATION durableInfo)
	{
		if ( Constant.DURABLE_PROCESSSTATE_PROCESSING.equalsIgnoreCase(durableInfo.getPROCESSSTATE()) )
		{
			// ({0}) 은 진행중인 상태에서 처리가 불가능합니다.
			throw new CustomException("DUR-001", new Object[] {durableInfo.getKeyDURABLEID()});
		}
	}
	
    /**
     * durableInfo에 할당된 Lot의 실제수량과 전산상의 수량을 비교합니다
     * 
     * @param durableInfo
     * @param lotCount
     * @return 
     * @throws CustomExceiption
     * 
    */
	public void checkCountCompare(DURABLEINFORMATION durableInfo, int lotCount)
	{
		if ( ConvertUtil.Object2Long4Zero(durableInfo.getLOTCOUNT()) != lotCount )
		{
			// ({0}) Carrier 에 담긴 Lot 의 수량({1})과 전산상 Lot 의 수량({2})이 일치하지 않습니다.
			throw new CustomException("DUR-002", new Object[] {durableInfo.getKeyDURABLEID(), durableInfo.getLOTCOUNT(), lotCount});
		}
	}
	
    /**
     * durableInfo의 한계용량을 체크합니다
     * 
     * @param durableInfo
     * @return 
     * @throws CustomException
     * 
    */
	public void checkCountOver(DURABLEINFORMATION durableInfo)
	{
		if ( durableInfo.getCAPACITY().compareTo( ConvertUtil.Object2Double(durableInfo.getLOTCOUNT()) ) <= 0 )
		{
			// ({0}) Carrier 의 한계용량({1})을 초과하여 담을 수 없습니다.
			throw new CustomException("DUR-003", new Object[] {durableInfo.getKeyDURABLEID(), durableInfo.getCAPACITY()});
		}
	}
	
    /**
     * durableInfo의 실제ID와 전산상의 ID를 비교합니다
     * 
     * @param durableInfo
     * @param carrierID
     * @return 
     * @throws CustomException
     * 
    */
	public void checkIDCompare(DURABLEINFORMATION durableInfo, String carrierID)
	{
		if ( !durableInfo.getKeyDURABLEID().equalsIgnoreCase(carrierID) )
		{
			// ({0}) Carrier 와 Lot 의 Carrier ({1})의 데이터가 일치하지 않습니다.
			throw new CustomException("DUR-004", new Object[] {durableInfo.getKeyDURABLEID()});
		}
	}
	
    /**
     * durableInfo의 실제상태와 전산상의 상태를 비교합니다
     * 
     * @param durableState
     * @param requireState
     * @return 
     * @throws CustomException
     * 
    */
	public void checkState(String durableState, String requireState)
	{
		if ( !requireState.equalsIgnoreCase(durableState) )
		{
			// 이미 처리가 완료되었거나 처리 가능한 상태가 아닙니다. 현재 상태를 다시 한번 확인 후 진행해 주시기 바랍니다.
			throw new CustomException("LOT-008", new Object[] {});
		}
	}
	
    /**
     * durableInfo의 slotNumber가 비어있는지 체크합니다
     * 
     * @param durableInfo
     * @param slotNumber
     * @return 
     * @throws CustomException
     * 
    */
	public void checkSlotUse(DURABLEINFORMATION durableInfo, String slotNumber)
	{
		List<Object> listLotInfo = getLotList(durableInfo);
		
		for ( int i = 0; i < listLotInfo.size(); i++ )
		{
			LOTINFORMATION lotInfo = (LOTINFORMATION) listLotInfo.get(i);
			
			if ( lotInfo.getASSIGNSLOT().equalsIgnoreCase(slotNumber) )
			{
				// ({0}) Carrier 의 Slot ({1})을 이미 사용중인 Lot ({2})이 존재합니다.
				throw new CustomException("DUR-005", new Object[] {durableInfo.getKeyDURABLEID(), slotNumber, lotInfo.getKeyLOTID()});
			}
		}
	}
}

