package mes.equipment.transaction;

import java.util.HashMap;

import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.equipment.data.EQUIPMENTPMSCHEDULE;
import mes.equipment.data.SPAREPARTINFORMATION;
import mes.util.NameGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class SparePartService
{
	private static final transient Logger log = LoggerFactory.getLogger(SparePartService.class);
	
    /**
     * dataMap을 기반으로 새로운 설비부품을 생성합니다
     * 
     * @param dataMap
     * @param txnInfo
     * @return
     * 
    */
	public void create(HashMap<String, String> dataMap, TxnInfo txnInfo)
	{
		NameGenerator nameGenerator = new NameGenerator();
		String sparePartLotID = nameGenerator.nameGenerate(dataMap.get("PLANTID"), "SparePartLotID", new Object[] {});

		SPAREPARTINFORMATION sparePartInfo = new SPAREPARTINFORMATION();
		sparePartInfo.setKeyPLANTID(dataMap.get("PLANTID"));
		sparePartInfo.setKeySPAREPARTID(dataMap.get("SPAREPARTID"));
		sparePartInfo.setKeySPAREPARTLOTID(sparePartLotID);
		
		sparePartInfo.setSPAREPARTLOTSTATE(Constant.SPAREPART_LOTSTATE_WAITING);
		sparePartInfo.setQUANTITY(ConvertUtil.Object2Long(dataMap.get("QUANTITY")));
		sparePartInfo.setPRODUCTNUMBER(dataMap.get("PRODUCTNUMBER"));
		sparePartInfo.setSERIALNUMBER(dataMap.get("SERIALNUMBER"));
		sparePartInfo.setGRADE(dataMap.get("GRADE"));
		sparePartInfo.setRECEIPTDATE(DateUtil.getTimestamp(dataMap.get("RECEIPTDATE")));
		sparePartInfo.setSUPPLYVENDOR(dataMap.get("SUPPLYVENDOR"));
		//sparePartInfo.setRESERVEDATE(txnInfo.getEventTime());
		//sparePartInfo.setRESERVEUSERID("11111111");
		//sparePartInfo.setRESERVECOMMENT("11111111");
		//sparePartInfo.setRESERVEEQUIPMENTID("11111111");
		//sparePartInfo.setRESERVELOCATION("11111111");
		sparePartInfo.setLOCATION(dataMap.get("LOCATION"));

		sparePartInfo.excuteDML(SqlConstant.DML_INSERT);

	    // History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(sparePartInfo, txnInfo, "C");
	}
		
	public void ReceiveSparePart(SPAREPARTINFORMATION partInfo, TxnInfo txnInfo)
	{
		
	}

	public void ReserveSparePart(SPAREPARTINFORMATION partInfo, TxnInfo txnInfo)
	{
		
	}

    /**
     * partInfo의 상태를 changeState로 변경합니다
     * 
     * @param partInfo
     * @param changeState
     * @param txnInfo
     * 
    */
	public void SparePartStateChange(SPAREPARTINFORMATION partInfo, String changeState, TxnInfo txnInfo)
	{
		SparePartStateChange(partInfo, changeState, "", "", txnInfo);
	}
	
    /**
     * partInfo의 상태를 chageState로 변경하고
     * reasonCode, reasonCodeType을 set합니다
     * 
     * @param partInfo
     * @param changeState
     * @param reasonCode
     * @param reasonCodeType
     * @param txnInfo
     * @return
     * @throws
     * 
    */
	public void SparePartStateChange(SPAREPARTINFORMATION partInfo, String changeState, String reasonCode, String reasonCodeType, TxnInfo txnInfo)
	{
		partInfo.setSPAREPARTLOTSTATE(changeState);
		partInfo.setREASONCODE(reasonCode);
		partInfo.setREASONCODETYPE(reasonCodeType);
		partInfo.excuteDML(SqlConstant.DML_UPDATE);

	    // History 기록
	    AddHistory history = new AddHistory();
	    history.addHistory(partInfo, txnInfo, "U");
	}
	
    /**
     * partInfo의 location(상위설비ID)을 set하고
     * 상태를 "InUse"로 변경합니다
     * 
     * @param partInfo
     * @param location
     * @param txnInfo
     * 
    */
	public void assignSparePart(SPAREPARTINFORMATION partInfo, String location, TxnInfo txnInfo)
	{
		partInfo.setLOCATION(location);
		SparePartStateChange(partInfo, Constant.SPAREPART_LOTSTATE_INUSE, txnInfo);
	}
	
    /**
     * partInfo의 location을 초기화하고
     * 상태를 changeState로 변경합니다
     * 
     * @param partInfo
     * @param changeState
     * @param txnInfo
     * 
    */
	public void deassignSparePart(SPAREPARTINFORMATION partInfo, String changeState, TxnInfo txnInfo)
	{
		partInfo.setLOCATION("");
		SparePartStateChange(partInfo, changeState, txnInfo);
	}
	
    /**
     * PM작업 중에 설비부품을 할당합니다
     * 
     * @param partInfo
     * @param pmInfo
     * @param location
     * @param txnInfo
     * 
    */
	public void pmWork_AssignSparePart(SPAREPARTINFORMATION partInfo, EQUIPMENTPMSCHEDULE pmInfo, String location, TxnInfo txnInfo)
	{
		AddHistory history = new AddHistory();

		if( !pmInfo.getREFERENCETYPE().equals(Constant.PM_REFERENCETYPE_PMSTARTCANCEL) && !pmInfo.getREFERENCETYPE().equals(Constant.PM_REFERENCETYPE_PMENDCANCEL))
		{
			pmInfo.setREFERENCETYPE(Constant.PM_REFERENCETYPE_SPAREPARTLOTID);
			pmInfo.setREFERENCEVALUE(partInfo.getKeySPAREPARTLOTID());
			pmInfo.excuteDML(SqlConstant.DML_UPDATE);
			txnInfo.setTxnId("AssignSparePart");
			history.addHistory(pmInfo, txnInfo, "U");
		}
		else
		{
			txnInfo.setTxnId(pmInfo.getREFERENCETYPE());
		}
		
		partInfo.setREFERENCETYPE(Constant.SPAREPART_REFERENCETYPE_PMSCHEDULE);
		partInfo.setREFERENCEVALUE(pmInfo.getKeyPMSCHEDULEID());
		assignSparePart(partInfo, location, txnInfo);
	}
	
    /**
     * PM작업 중에 설비부품을 해제합니다
     * 
     * @param partInfo
     * @param pmInfo
     * @param changeState
     * @param txnInfo
     * @return
     * 
    */
	public void pmWork_DeassignSparePart(SPAREPARTINFORMATION partInfo, EQUIPMENTPMSCHEDULE pmInfo, String changeState, TxnInfo txnInfo)
	{
		AddHistory history = new AddHistory();
		
		partInfo.setREFERENCETYPE(Constant.SPAREPART_REFERENCETYPE_PMSCHEDULE);
		partInfo.setREFERENCEVALUE(pmInfo.getKeyPMSCHEDULEID());
		deassignSparePart(partInfo, changeState, txnInfo);
		
		if(pmInfo.getREFERENCETYPE().equals(Constant.PM_REFERENCETYPE_PMSTARTCANCEL) || pmInfo.getREFERENCETYPE().equals(Constant.PM_REFERENCETYPE_PMENDCANCEL) )
		{
			txnInfo.setTxnId(pmInfo.getREFERENCETYPE());
		}
		else
		{
			deassignSparePart(partInfo, changeState, txnInfo);
			
			pmInfo.setREFERENCETYPE(Constant.PM_REFERENCETYPE_SPAREPARTLOTID);
			pmInfo.setREFERENCEVALUE(partInfo.getKeySPAREPARTLOTID());
			pmInfo.excuteDML(SqlConstant.DML_UPDATE);
			txnInfo.setTxnId("DeassignSparePart");
			history.addHistory(pmInfo, txnInfo, "U");
		}
	}
}
