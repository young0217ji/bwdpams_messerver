package mes.master.data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.06
 * 
 * @see
 */
public class DY_PROCWORKORDER {
	// Key Info
	private String PLANTID;
	private String DATAKEY;

	// Data Info
	private String WORKORDERID;
	private Integer PLANSTARTPRIOR;
	private String PRODUCTID;
	private String PLANSTARTDATE;
	private String PLANSTARTTIME;
	private String PLANENDDATE;
	private String PLANENDTIME;
	private String EXECUTESTARTDATE;
	private String EXECUTESTARTTIME;
	private String EXECUTEENDDATE;
	private String EXECUTEENDTIME;
	private String ERPOPERID;
	private String WORKCENTER;
	private String EQUIPMENTID;
	private Integer QUANTITY;
	private Integer QUANTITYPRODUCT;
	private Integer QUANTITYNOTOK;
	private Integer QUANTITYSEPARATION;
	private BigDecimal CYCLETIME;
	private BigDecimal NEEDTIME;
	private Integer PROCESSSTATE;
	private String ERPSTARTDATE;
	private String SCHEDULEALTERCODE;
	private String SCHEDULEALTERDESCRIPTION;
	private String ISSUE;
	private String ROD2MATERIAL;
	private String ROD2MANUFACTURE;
	private String ROD2PLATING;
	private String TUBEREMARK;
	private String ROD2MOVEMENT;
	private String RODASSYREMARK;
	private String HCSCHEDULE;
	private String CLSCHEDULE;
	private String KLSCHEDULE;
	private String RCSCHEDULE;
	private String PTSCHEDULE;
	private String PIPESCHEDULE;
	private String ASSEMBLYDATE;
	private String ROD2MOVEMENTTOOLTIP;
	private String PARTSCRREMARK;
	private String BUSHSCHEDULE;
	private String VALVESCHEDULE;
	private String NEEDTIMEONASSEMBLY;
	private String CHECK20PRODUCT;
	private String PARTSREMARK;
	private String DESCRIPTION;
	private Timestamp CREATETIME;
	private String CREATEUSERID;
	private Timestamp LASTUPDATETIME;
	private String LASTUPDATEUSERID;
	private String PLANPRODUCTDATE;
	private String PARTSGRREMARK;

	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
	private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

	public DY_PROCWORKORDER() {
	}

	// Key Methods
	// PLANTID
	public String getKeyPLANTID() {
		return PLANTID;
	}

	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}

	// DATAKEY
	public String getKeyDATAKEY() {
		return DATAKEY;
	}

	public void setKeyDATAKEY(String DATAKEY) {
		this.DATAKEY = DATAKEY;
		this.KEYMAP.put("DATAKEY", DATAKEY);
	}

	// Data Methods
	public String getWORKORDERID() {
		return WORKORDERID;
	}

	public void setWORKORDERID(String WORKORDERID) {
		this.WORKORDERID = WORKORDERID;
		this.DATAMAP.put("WORKORDERID", WORKORDERID);
	}

	public Integer getPLANSTARTPRIOR() {
		return PLANSTARTPRIOR;
	}

	public void setPLANSTARTPRIOR(Integer PLANSTARTPRIOR) {
		this.PLANSTARTPRIOR = PLANSTARTPRIOR;
		this.DATAMAP.put("PLANSTARTPRIOR", PLANSTARTPRIOR);
	}

	public String getPRODUCTID() {
		return PRODUCTID;
	}

	public void setPRODUCTID(String PRODUCTID) {
		this.PRODUCTID = PRODUCTID;
		this.DATAMAP.put("PRODUCTID", PRODUCTID);
	}

	public String getPLANSTARTDATE() {
		return PLANSTARTDATE;
	}

	public void setPLANSTARTDATE(String PLANSTARTDATE) {
		this.PLANSTARTDATE = PLANSTARTDATE;
		this.DATAMAP.put("PLANSTARTDATE", PLANSTARTDATE);
	}

	public String getPLANSTARTTIME() {
		return PLANSTARTTIME;
	}

	public void setPLANSTARTTIME(String PLANSTARTTIME) {
		this.PLANSTARTTIME = PLANSTARTTIME;
		this.DATAMAP.put("PLANSTARTTIME", PLANSTARTTIME);
	}

	public String getPLANENDDATE() {
		return PLANENDDATE;
	}

	public void setPLANENDDATE(String PLANENDDATE) {
		this.PLANENDDATE = PLANENDDATE;
		this.DATAMAP.put("PLANENDDATE", PLANENDDATE);
	}

	public String getPLANENDTIME() {
		return PLANENDTIME;
	}

	public void setPLANENDTIME(String PLANENDTIME) {
		this.PLANENDTIME = PLANENDTIME;
		this.DATAMAP.put("PLANENDTIME", PLANENDTIME);
	}

	public String getEXECUTESTARTDATE() {
		return EXECUTESTARTDATE;
	}

	public void setEXECUTESTARTDATE(String EXECUTESTARTDATE) {
		this.EXECUTESTARTDATE = EXECUTESTARTDATE;
		this.DATAMAP.put("EXECUTESTARTDATE", EXECUTESTARTDATE);
	}

	public String getEXECUTESTARTTIME() {
		return EXECUTESTARTTIME;
	}

	public void setEXECUTESTARTTIME(String EXECUTESTARTTIME) {
		this.EXECUTESTARTTIME = EXECUTESTARTTIME;
		this.DATAMAP.put("EXECUTESTARTTIME", EXECUTESTARTTIME);
	}

	public String getEXECUTEENDDATE() {
		return EXECUTEENDDATE;
	}

	public void setEXECUTEENDDATE(String EXECUTEENDDATE) {
		this.EXECUTEENDDATE = EXECUTEENDDATE;
		this.DATAMAP.put("EXECUTEENDDATE", EXECUTEENDDATE);
	}

	public String getEXECUTEENDTIME() {
		return EXECUTEENDTIME;
	}

	public void setEXECUTEENDTIME(String EXECUTEENDTIME) {
		this.EXECUTEENDTIME = EXECUTEENDTIME;
		this.DATAMAP.put("EXECUTEENDTIME", EXECUTEENDTIME);
	}

	public String getERPOPERID() {
		return ERPOPERID;
	}

	public void setERPOPERID(String ERPOPERID) {
		this.ERPOPERID = ERPOPERID;
		this.DATAMAP.put("ERPOPERID", ERPOPERID);
	}

	public String getWORKCENTER() {
		return WORKCENTER;
	}

	public void setWORKCENTER(String WORKCENTER) {
		this.WORKCENTER = WORKCENTER;
		this.DATAMAP.put("WORKCENTER", WORKCENTER);
	}

	public String getEQUIPMENTID() {
		return EQUIPMENTID;
	}

	public void setEQUIPMENTID(String EQUIPMENTID) {
		this.EQUIPMENTID = EQUIPMENTID;
		this.DATAMAP.put("EQUIPMENTID", EQUIPMENTID);
	}

	public Integer getQUANTITY() {
		return QUANTITY;
	}

	public void setQUANTITY(Integer QUANTITY) {
		this.QUANTITY = QUANTITY;
		this.DATAMAP.put("QUANTITY", QUANTITY);
	}

	public Integer getQUANTITYPRODUCT() {
		return QUANTITYPRODUCT;
	}

	public void setQUANTITYPRODUCT(Integer QUANTITYPRODUCT) {
		this.QUANTITYPRODUCT = QUANTITYPRODUCT;
		this.DATAMAP.put("QUANTITYPRODUCT", QUANTITYPRODUCT);
	}

	public Integer getQUANTITYNOTOK() {
		return QUANTITYNOTOK;
	}

	public void setQUANTITYNOTOK(Integer QUANTITYNOTOK) {
		this.QUANTITYNOTOK = QUANTITYNOTOK;
		this.DATAMAP.put("QUANTITYNOTOK", QUANTITYNOTOK);
	}

	public Integer getQUANTITYSEPARATION() {
		return QUANTITYSEPARATION;
	}

	public void setQUANTITYSEPARATION(Integer QUANTITYSEPARATION) {
		this.QUANTITYSEPARATION = QUANTITYSEPARATION;
		this.DATAMAP.put("QUANTITYSEPARATION", QUANTITYSEPARATION);
	}

	public BigDecimal getCYCLETIME() {
		return CYCLETIME;
	}

	public void setCYCLETIME(BigDecimal CYCLETIME) {
		this.CYCLETIME = CYCLETIME;
		this.DATAMAP.put("CYCLETIME", CYCLETIME);
	}

	public BigDecimal getNEEDTIME() {
		return NEEDTIME;
	}

	public void setNEEDTIME(BigDecimal NEEDTIME) {
		this.NEEDTIME = NEEDTIME;
		this.DATAMAP.put("NEEDTIME", NEEDTIME);
	}

	public Integer getPROCESSSTATE() {
		return PROCESSSTATE;
	}

	public void setPROCESSSTATE(Integer PROCESSSTATE) {
		this.PROCESSSTATE = PROCESSSTATE;
		this.DATAMAP.put("PROCESSSTATE", PROCESSSTATE);
	}

	public String getERPSTARTDATE() {
		return ERPSTARTDATE;
	}

	public void setERPSTARTDATE(String ERPSTARTDATE) {
		this.ERPSTARTDATE = ERPSTARTDATE;
		this.DATAMAP.put("ERPSTARTDATE", ERPSTARTDATE);
	}

	public String getSCHEDULEALTERCODE() {
		return SCHEDULEALTERCODE;
	}

	public void setSCHEDULEALTERCODE(String SCHEDULEALTERCODE) {
		this.SCHEDULEALTERCODE = SCHEDULEALTERCODE;
		this.DATAMAP.put("SCHEDULEALTERCODE", SCHEDULEALTERCODE);
	}

	public String getSCHEDULEALTERDESCRIPTION() {
		return SCHEDULEALTERDESCRIPTION;
	}

	public void setSCHEDULEALTERDESCRIPTION(String SCHEDULEALTERDESCRIPTION) {
		this.SCHEDULEALTERDESCRIPTION = SCHEDULEALTERDESCRIPTION;
		this.DATAMAP.put("SCHEDULEALTERDESCRIPTION", SCHEDULEALTERDESCRIPTION);
	}

	// --

	public String getISSUE() {
		return ISSUE;
	}

	public void setISSUE(String ISSUE) {
		this.ISSUE = ISSUE;
		this.DATAMAP.put("ISSUE", ISSUE);
	}

	public String getROD2MATERIAL() {
		return ROD2MATERIAL;
	}

	public void setROD2MATERIAL(String ROD2MATERIAL) {
		this.ROD2MATERIAL = ROD2MATERIAL;
		this.DATAMAP.put("ROD2MATERIAL", ROD2MATERIAL);
	}

	public String getROD2MANUFACTURE() {
		return ROD2MANUFACTURE;
	}

	public void setROD2MANUFACTURE(String ROD2MANUFACTURE) {
		this.ROD2MANUFACTURE = ROD2MANUFACTURE;
		this.DATAMAP.put("ROD2MANUFACTURE", ROD2MANUFACTURE);
	}

	public String getROD2PLATING() {
		return ROD2PLATING;
	}

	public void setROD2PLATING(String ROD2PLATING) {
		this.ROD2PLATING = ROD2PLATING;
		this.DATAMAP.put("ROD2PLATING", ROD2PLATING);
	}

	public String getTUBEREMARK() {
		return TUBEREMARK;
	}

	public void setTUBEREMARK(String TUBEREMARK) {
		this.TUBEREMARK = TUBEREMARK;
		this.DATAMAP.put("TUBEREMARK", TUBEREMARK);
	}

	public String getROD2MOVEMENT() {
		return ROD2MOVEMENT;
	}

	public void setROD2MOVEMENT(String ROD2MOVEMENT) {
		this.ROD2MOVEMENT = ROD2MOVEMENT;
		this.DATAMAP.put("ROD2MOVEMENT", ROD2MOVEMENT);
	}

	public String getRODASSYREMARK() {
		return RODASSYREMARK;
	}

	public void setRODASSYREMARK(String RODASSYREMARK) {
		this.RODASSYREMARK = RODASSYREMARK;
		this.DATAMAP.put("RODASSYREMARK", RODASSYREMARK);
	}

	public String getHCSCHEDULE() {
		return HCSCHEDULE;
	}

	public void setHCSCHEDULE(String HCSCHEDULE) {
		this.HCSCHEDULE = HCSCHEDULE;
		this.DATAMAP.put("HCSCHEDULE", HCSCHEDULE);
	}

	public String getCLSCHEDULE() {
		return CLSCHEDULE;
	}

	public void setCLSCHEDULE(String CLSCHEDULE) {
		this.CLSCHEDULE = CLSCHEDULE;
		this.DATAMAP.put("CLSCHEDULE", CLSCHEDULE);
	}

	public String getKLSCHEDULE() {
		return KLSCHEDULE;
	}

	public void setKLSCHEDULE(String KLSCHEDULE) {
		this.KLSCHEDULE = KLSCHEDULE;
		this.DATAMAP.put("KLSCHEDULE", KLSCHEDULE);
	}

	public String getRCSCHEDULE() {
		return RCSCHEDULE;
	}

	public void setRCSCHEDULE(String RCSCHEDULE) {
		this.RCSCHEDULE = RCSCHEDULE;
		this.DATAMAP.put("RCSCHEDULE", RCSCHEDULE);
	}

	public String getPTSCHEDULE() {
		return PTSCHEDULE;
	}

	public void setPTSCHEDULE(String PTSCHEDULE) {
		this.PTSCHEDULE = PTSCHEDULE;
		this.DATAMAP.put("PTSCHEDULE", PTSCHEDULE);
	}

	public String getPIPESCHEDULE() {
		return PIPESCHEDULE;
	}

	public void setPIPESCHEDULE(String PIPESCHEDULE) {
		this.PIPESCHEDULE = PIPESCHEDULE;
		this.DATAMAP.put("PIPESCHEDULE", PIPESCHEDULE);
	}

	public String getASSEMBLYDATE() {
		return ASSEMBLYDATE;
	}

	public void setASSEMBLYDATE(String ASSEMBLYDATE) {
		this.ASSEMBLYDATE = ASSEMBLYDATE;
		this.DATAMAP.put("ASSEMBLYDATE", ASSEMBLYDATE);
	}

	public String getROD2MOVEMENTTOOLTIP() {
		return ROD2MOVEMENTTOOLTIP;
	}

	public void setROD2MOVEMENTTOOLTIP(String ROD2MOVEMENTTOOLTIP) {
		this.ROD2MOVEMENTTOOLTIP = ROD2MOVEMENTTOOLTIP;
		this.DATAMAP.put("ROD2MOVEMENTTOOLTIP", ROD2MOVEMENTTOOLTIP);
	}

	public String getPARTSCRREMARK() {
		return PARTSCRREMARK;
	}

	public void setPARTSCRREMARK(String PARTSCRREMARK) {
		this.PARTSCRREMARK = PARTSCRREMARK;
		this.DATAMAP.put("PARTSCRREMARK", PARTSCRREMARK);
	}

	public String getBUSHSCHEDULE() {
		return BUSHSCHEDULE;
	}

	public void setBUSHSCHEDULE(String BUSHSCHEDULE) {
		this.BUSHSCHEDULE = BUSHSCHEDULE;
		this.DATAMAP.put("BUSHSCHEDULE", BUSHSCHEDULE);
	}

	public String getVALVESCHEDULE() {
		return VALVESCHEDULE;
	}

	public void setVALVESCHEDULE(String VALVESCHEDULE) {
		this.VALVESCHEDULE = VALVESCHEDULE;
		this.DATAMAP.put("VALVESCHEDULE", VALVESCHEDULE);
	}

	public String getNEEDTIMEONASSEMBLY() {
		return NEEDTIMEONASSEMBLY;
	}

	public void setNEEDTIMEONASSEMBLY(String NEEDTIMEONASSEMBLY) {
		this.NEEDTIMEONASSEMBLY = NEEDTIMEONASSEMBLY;
		this.DATAMAP.put("NEEDTIMEONASSEMBLY", NEEDTIMEONASSEMBLY);
	}

	public String getCHECK20PRODUCT() {
		return CHECK20PRODUCT;
	}

	public void setCHECK20PRODUCT(String CHECK20PRODUCT) {
		this.CHECK20PRODUCT = CHECK20PRODUCT;
		this.DATAMAP.put("CHECK20PRODUCT", CHECK20PRODUCT);
	}

	public String getPARTSREMARK() {
		return PARTSREMARK;
	}

	public void setPARTSREMARK(String PARTSREMARK) {
		this.PARTSREMARK = PARTSREMARK;
		this.DATAMAP.put("PARTSREMARK", PARTSREMARK);
	}

	// --

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String DESCRIPTION) {
		this.DESCRIPTION = DESCRIPTION;
		this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
	}

	public String getPLANPRODUCTDATE() {
		return PLANPRODUCTDATE;
	}

	public void setPLANPRODUCTDATE(String PLANPRODUCTDATE) {
		this.PLANPRODUCTDATE = PLANPRODUCTDATE;
		this.DATAMAP.put("PLANPRODUCTDATE", PLANPRODUCTDATE);
	}

	// CREATETIME
	public Timestamp getCREATETIME() {
		return CREATETIME;
	}

	public void setCREATETIME(Timestamp CREATETIME) {
		this.CREATETIME = CREATETIME;
		this.DATAMAP.put("CREATETIME", CREATETIME);
	}

	// CREATEUSERID
	public String getCREATEUSERID() {
		return CREATEUSERID;
	}

	public void setCREATEUSERID(String CREATEUSERID) {
		this.CREATEUSERID = CREATEUSERID;
		this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
	}

	// LASTUPDATETIME
	public Timestamp getLASTUPDATETIME() {
		return LASTUPDATETIME;
	}

	public void setLASTUPDATETIME(Timestamp LASTUPDATETIME) {
		this.LASTUPDATETIME = LASTUPDATETIME;
		this.DATAMAP.put("LASTUPDATETIME", LASTUPDATETIME);
	}

	// LASTUPDATEUSERID
	public String getLASTUPDATEUSERID() {
		return LASTUPDATEUSERID;
	}

	public void setLASTUPDATEUSERID(String LASTUPDATEUSERID) {
		this.LASTUPDATEUSERID = LASTUPDATEUSERID;
		this.DATAMAP.put("LASTUPDATEUSERID", LASTUPDATEUSERID);
	}
	
	public String getPARTSGRREMARK() {
		return PARTSGRREMARK;
	}

	public void setPARTSGRREMARK(String PARTSGRREMARK) {
		this.PARTSGRREMARK = PARTSGRREMARK;
		this.DATAMAP.put("PARTSGRREMARK", PARTSGRREMARK);
	}
	

	// Key Validation
	public void checkKeyNotNull() {
		if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.DATAKEY == null) {
			// [{1}] 다음의 필수 Key 값이 누락되었습니다.
			throw new KeyRequiredException(new Object[] { "PLANTID,DATAKEY" });
		}
	}

	public void setKEYMAP(HashMap<String, Object> KEYMAP) {
		this.KEYMAP = KEYMAP;
	}

	public HashMap<String, Object> getKEYMAP() {
		return KEYMAP;
	}

	public void setDATAMAP(HashMap<String, Object> DATAMAP) {
		this.DATAMAP = DATAMAP;
	}

	public HashMap<String, Object> getDATAMAP() {
		return DATAMAP;
	}

	public Object excuteDML(String type) {
		return excuteDML(type, "");
	}

	public Object excuteDML(String type, String suffix) {
		if (type.equalsIgnoreCase(SqlConstant.DML_INSERT) || type.equalsIgnoreCase(SqlConstant.DML_UPDATE)
				|| type.equalsIgnoreCase(SqlConstant.DML_DELETE)) {
			checkKeyNotNull();
		}
		Object returnValue = SqlQueryUtil.excuteDML(this.getClass(), type, suffix, KEYMAP, DATAMAP);
		return returnValue;
	}

}
