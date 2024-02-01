package mes.equipment.data;

import java.sql.Timestamp;
import java.util.HashMap;

import mes.userdefine.data.PRODUCTDEFINITION_UDF;
import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12
 * 
 * @see
 */
public class DY_INSTRUMENT {
	// Key Info
	private String PLANTID;
	private String ITEMNO;

	// Data Info
	private String ITEMNAME;
	private String ITEMCATEGORY;
	private String ITEMTYPE;
	private String DEPARTMENT;
	private String ASSETFLAG;
	private String ASSETNO;
	private String ITEMGRADE;
	private String ITEMSTATE;
	private String INSTRUMENTREVISION;
	private Integer ACQUISITIONAMOUNT;
	private Integer RESIDUALAMOUNT;
	private Timestamp ACQUISITIONDATE;
	private String ACQUISITIONREASONE;
	private String MAKER;
	private String MODELNO;
	private String SERIALNO;
	private String VENDOR;
	private String STANDARD;
	private String RESOLUTION;
	private String CHECKINTERVAL;
	private Timestamp NEXTCHECKDATE;
	private String DESCRIPTION;
	private String CREATEUSERID;
	private Timestamp CREATETIME;
	private String LASTUPDATEUSERID;
	private Timestamp LASTUPDATETIME;

	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
	private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

	public DY_INSTRUMENT() {
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

	// ITEMNO
	public String getKeyITEMNO() {
		return ITEMNO;
	}

	public void setKeyITEMNO(String ITEMNO) {
		this.ITEMNO = ITEMNO;
		this.KEYMAP.put("ITEMNO", ITEMNO);
	}

	// Data Methods
	// ITEMNAME
	public String getITEMNAME() {
		return ITEMNAME;
	}

	public void setITEMNAME(String ITEMNAME) {
		this.ITEMNAME = ITEMNAME;
		this.DATAMAP.put("ITEMNAME", ITEMNAME);
	}

	// ITEMCATEGORY
	public String getITEMCATEGORY() {
		return ITEMCATEGORY;
	}

	public void setITEMCATEGORY(String ITEMCATEGORY) {
		this.ITEMCATEGORY = ITEMCATEGORY;
		this.DATAMAP.put("ITEMCATEGORY", ITEMCATEGORY);
	}

	// ITEMTYPE
	public String geWITEMTYPE() {
		return ITEMTYPE;
	}

	public void setITEMTYPE(String ITEMTYPE) {
		this.ITEMTYPE = ITEMTYPE;
		this.DATAMAP.put("ITEMTYPE", ITEMTYPE);
	}

	// DEPARTMENT
	public String getDEPARTMENT() {
		return DEPARTMENT;
	}

	public void setDEPARTMENT(String DEPARTMENT) {
		this.DEPARTMENT = DEPARTMENT;
		this.DATAMAP.put("DEPARTMENT", DEPARTMENT);
	}

	// ASSETFLAG
	public String getASSETFLAG() {
		return ASSETFLAG;
	}

	public void setASSETFLAG(String ASSETFLAG) {
		this.ASSETFLAG = ASSETFLAG;
		this.DATAMAP.put("ASSETFLAG", ASSETFLAG);
	}

	// ASSETNO
	public String getASSETNO() {
		return ASSETNO;
	}

	public void setASSETNO(String ASSETNO) {
		this.ASSETNO = ASSETNO;
		this.DATAMAP.put("ASSETNO", ASSETNO);
	}

	// ITEMGRADE
	public String getITEMGRADE() {
		return ITEMGRADE;
	}

	public void setITEMGRADE(String ITEMGRADE) {
		this.ITEMGRADE = ITEMGRADE;
		this.DATAMAP.put("ITEMGRADE", ITEMGRADE);
	}

	// ITEMSTATE
	public String getITEMSTATE() {
		return ITEMSTATE;
	}

	public void setITEMSTATE(String ITEMSTATE) {
		this.ITEMSTATE = ITEMSTATE;
		this.DATAMAP.put("ITEMSTATE", ITEMSTATE);
	}
	
	// INSTRUMENTREVISION
	public String getINSTRUMENTREVISION() {
		return INSTRUMENTREVISION;
	}
	
	public void setINSTRUMENTREVISION(String INSTRUMENTREVISION) {
		this.INSTRUMENTREVISION = INSTRUMENTREVISION;
		this.DATAMAP.put("INSTRUMENTREVISION", INSTRUMENTREVISION);
	}

	// ACQUISITIONAMOUNT
	public Integer getACQUISITIONAMOUNT() {
		return ACQUISITIONAMOUNT;
	}

	public void setACQUISITIONAMOUNT(Integer ACQUISITIONAMOUNT) {
		this.ACQUISITIONAMOUNT = ACQUISITIONAMOUNT;
		this.DATAMAP.put("ACQUISITIONAMOUNT", ACQUISITIONAMOUNT);
	}

	// RESIDUALAMOUNT
	public Integer geRESIDUALAMOUNT() {
		return RESIDUALAMOUNT;
	}

	public void setRESIDUALAMOUNT(Integer RESIDUALAMOUNT) {
		this.RESIDUALAMOUNT = RESIDUALAMOUNT;
		this.DATAMAP.put("RESIDUALAMOUNT", RESIDUALAMOUNT);
	}

	// ACQUISITIONDATE
	public Timestamp getACQUISITIONDATE() {
		return ACQUISITIONDATE;
	}

	public void setACQUISITIONDATE(Timestamp ACQUISITIONDATE) {
		this.ACQUISITIONDATE = ACQUISITIONDATE;
		this.DATAMAP.put("ACQUISITIONDATE", ACQUISITIONDATE);
	}

	// ACQUISITIONREASONE
	public String getACQUISITIONREASONE() {
		return ACQUISITIONREASONE;
	}

	public void setACQUISITIONREASONE(String ACQUISITIONREASONE) {
		this.ACQUISITIONREASONE = ACQUISITIONREASONE;
		this.DATAMAP.put("ACQUISITIONREASONE", ACQUISITIONREASONE);
	}

	// MAKER
	public String getMAKER() {
		return MAKER;
	}

	public void setMAKER(String MAKER) {
		this.MAKER = MAKER;
		this.DATAMAP.put("MAKER", MAKER);
	}

	// MODELNO
	public String getMODELNO() {
		return MODELNO;
	}

	public void setMODELNO(String MODELNO) {
		this.MODELNO = MODELNO;
		this.DATAMAP.put("MODELNO", MODELNO);
	}

	// SERIALNO
	public String getSERIALNO() {
		return SERIALNO;
	}

	public void setSERIALNO(String SERIALNO) {
		this.SERIALNO = SERIALNO;
		this.DATAMAP.put("SERIALNO", SERIALNO);
	}

	// VENDOR
	public String getVENDOR() {
		return VENDOR;
	}

	public void setVENDOR(String VENDOR) {
		this.VENDOR = VENDOR;
		this.DATAMAP.put("VENDOR", VENDOR);
	}
	
	// STANDARD
	public String getSTANDARD() {
		return STANDARD;
	}
	
	public void setSTANDARD(String STANDARD) {
		this.STANDARD = STANDARD;
		this.DATAMAP.put("STANDARD", STANDARD);
	}

	// RESOLUTION
	public String getRESOLUTION() {
		return RESOLUTION;
	}

	public void setRESOLUTION(String RESOLUTION) {
		this.RESOLUTION = RESOLUTION;
		this.DATAMAP.put("RESOLUTION", RESOLUTION);
	}
	
	// CHECKINTERVAL
	public String getCHECKINTERVAL() {
		return CHECKINTERVAL;
	}

	public void setCHECKINTERVAL(String CHECKINTERVAL) {
		this.CHECKINTERVAL = CHECKINTERVAL;
		this.DATAMAP.put("CHECKINTERVAL", CHECKINTERVAL);
	}
	
	// NEXTCHECKDATE
	public Timestamp getNEXTCHECKDATE() {
		return NEXTCHECKDATE;
	}

	public void setNEXTCHECKDATE(Timestamp NEXTCHECKDATE) {
		this.NEXTCHECKDATE = NEXTCHECKDATE;
		this.DATAMAP.put("NEXTCHECKDATE", NEXTCHECKDATE);
	}
	
	// DESCRIPTION
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String DESCRIPTION) {
		this.DESCRIPTION = DESCRIPTION;
		this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
	}
	

	// CREATEUSERID
	public String getCREATEUSERID() {
		return CREATEUSERID;
	}

	public void setCREATEUSERID(String CREATEUSERID) {
		this.CREATEUSERID = CREATEUSERID;
		this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
	}

	// CREATETIME
	public Timestamp getCREATETIME() {
		return CREATETIME;
	}

	public void setCREATETIME(Timestamp CREATETIME) {
		this.CREATETIME = CREATETIME;
		this.DATAMAP.put("CREATETIME", CREATETIME);
	}

	// LASTUPDATEUSERID
	public String getLASTUPDATEUSERID() {
		return LASTUPDATEUSERID;
	}

	public void setLASTUPDATEUSERID(String LASTUPDATEUSERID) {
		this.LASTUPDATEUSERID = LASTUPDATEUSERID;
		this.DATAMAP.put("LASTUPDATEUSERID", LASTUPDATEUSERID);
	}

	// LASTUPDATETIME
	public Timestamp getLASTUPDATETIME() {
		return LASTUPDATETIME;
	}

	public void setLASTUPDATETIME(Timestamp LASTUPDATETIME) {
		this.LASTUPDATETIME = LASTUPDATETIME;
		this.DATAMAP.put("LASTUPDATETIME", LASTUPDATETIME);
	}


	// Key Validation
	public void checkKeyNotNull() {
		if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.ITEMNO == null) {
			// [{1}] 다음의 필수 Key 값이 누락되었습니다.
			throw new KeyRequiredException(new Object[] { "PLANTID, ITEMNO" });
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
