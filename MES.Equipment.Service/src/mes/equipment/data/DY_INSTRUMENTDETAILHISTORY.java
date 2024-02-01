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
public class DY_INSTRUMENTDETAILHISTORY {
	// Key Info
	private String PLANTID;
	private String RELATIONTIMEKEY;
	private String ITEMNO;
	private String TIMEKEY;

	// Data Info
	private String ACTIONTYPE;
	private String ACTIONTYPEDETAIL;
	private String ACTIONRESULT;
	private Timestamp ACTIONTIME;
	private String ACTIONCOMPANY;
	private String ACTIONDEPARTMENT;
	private String ACTIONCOMPANYUSERID;
	private Integer ACTIONCOST;
	private String DESCRIPTION;
	private String CREATEUSERID;
	private Timestamp CREATETIME;
	private String LASTUPDATEUSERID;
	private Timestamp LASTUPDATETIME;
	private String EVENTNAME;
	private String EVENTTYPE;
    private Timestamp EVENTTIME;
    private String EVENTUSERID;
    private String EVENTCOMMENT;

	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
	private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

	public DY_INSTRUMENTDETAILHISTORY() {
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
	
	// RELATIONTIMEKEY
	public String getKeyRELATIONTIMEKEY() {
		return RELATIONTIMEKEY;
	}

	public void setKeyRELATIONTIMEKEY(String RELATIONTIMEKEY) {
		this.RELATIONTIMEKEY = RELATIONTIMEKEY;
		this.KEYMAP.put("RELATIONTIMEKEY", RELATIONTIMEKEY);
	}

	// ITEMNO
	public String getKeyITEMNO() {
		return ITEMNO;
	}

	public void setKeyITEMNO(String ITEMNO) {
		this.ITEMNO = ITEMNO;
		this.KEYMAP.put("ITEMNO", ITEMNO);
	}
	
	// TIMEKEY
	public String getKeyTIMEKEY()
    {
        return TIMEKEY;
    }
    public void setKeyTIMEKEY(String TIMEKEY)
    {
        this.TIMEKEY = TIMEKEY;
        this.KEYMAP.put("TIMEKEY", TIMEKEY);
    }

	// Data Methods
	// ACTIONTYPE
	public String getACTIONTYPE() {
		return ACTIONTYPE;
	}

	public void setACTIONTYPE(String ACTIONTYPE) {
		this.ACTIONTYPE = ACTIONTYPE;
		this.DATAMAP.put("ACTIONTYPE", ACTIONTYPE);
	}

	// ACTIONTYPEDETAIL
	public String getACTIONTYPEDETAIL() {
		return ACTIONTYPEDETAIL;
	}

	public void setACTIONTYPEDETAIL(String ACTIONTYPEDETAIL) {
		this.ACTIONTYPEDETAIL = ACTIONTYPEDETAIL;
		this.DATAMAP.put("ACTIONTYPEDETAIL", ACTIONTYPEDETAIL);
	}

	// ACTIONRESULT
	public String getACTIONRESULT() {
		return ACTIONRESULT;
	}

	public void setACTIONRESULT(String ACTIONRESULT) {
		this.ACTIONRESULT = ACTIONRESULT;
		this.DATAMAP.put("ACTIONRESULT", ACTIONRESULT);
	}

	// ACTIONTIME
	public Timestamp getACTIONTIME() {
		return ACTIONTIME;
	}

	public void setACTIONTIME(Timestamp ACTIONTIME) {
		this.ACTIONTIME = ACTIONTIME;
		this.DATAMAP.put("ACTIONTIME", ACTIONTIME);
	}

	// ACTIONCOMPANY
	public String getACTIONCOMPANY() {
		return ACTIONCOMPANY;
	}

	public void setACTIONCOMPANY(String ACTIONCOMPANY) {
		this.ACTIONCOMPANY = ACTIONCOMPANY;
		this.DATAMAP.put("ACTIONCOMPANY", ACTIONCOMPANY);
	}

	// ACTIONDEPARTMENT
	public String getACTIONDEPARTMENT() {
		return ACTIONDEPARTMENT;
	}

	public void setACTIONDEPARTMENT(String ACTIONDEPARTMENT) {
		this.ACTIONDEPARTMENT = ACTIONDEPARTMENT;
		this.DATAMAP.put("ACTIONDEPARTMENT", ACTIONDEPARTMENT);
	}

	// ACTIONCOMPANYUSERID
	public String getACTIONCOMPANYUSERID() {
		return ACTIONCOMPANYUSERID;
	}

	public void setACTIONCOMPANYUSERID(String ACTIONCOMPANYUSERID) {
		this.ACTIONCOMPANYUSERID = ACTIONCOMPANYUSERID;
		this.DATAMAP.put("ACTIONCOMPANYUSERID", ACTIONCOMPANYUSERID);
	}

	// ACTIONCOST
	public Integer getACTIONCOST() {
		return ACTIONCOST;
	}

	public void setACTIONCOST(Integer ACTIONCOST) {
		this.ACTIONCOST = ACTIONCOST;
		this.DATAMAP.put("ACTIONCOST", ACTIONCOST);
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
	
	// EVENTNAME
    public String getEVENTNAME()
    {
        return EVENTNAME;
    }
    public void setEVENTNAME(String EVENTNAME)
    {
        this.EVENTNAME = EVENTNAME;
        this.DATAMAP.put("EVENTNAME", EVENTNAME);
    }
    
    // EVENTTYPE
    public String getEVENTTYPE()
    {
        return EVENTTYPE;
    }
    public void setEVENTTYPE(String EVENTTYPE)
    {
        this.EVENTTYPE = EVENTTYPE;
        this.DATAMAP.put("EVENTTYPE", EVENTTYPE);
    }

    // EVENTTIME
    public Timestamp getEVENTTIME()
    {
        return EVENTTIME;
    }
    public void setEVENTTIME(Timestamp EVENTTIME)
    {
        this.EVENTTIME = EVENTTIME;
        this.DATAMAP.put("EVENTTIME", EVENTTIME);
    }

    // EVENTUSERID
    public String getEVENTUSERID()
    {
        return EVENTUSERID;
    }
    public void setEVENTUSERID(String EVENTUSERID)
    {
        this.EVENTUSERID = EVENTUSERID;
        this.DATAMAP.put("EVENTUSERID", EVENTUSERID);
    }

    // EVENTCOMMENT
    public String getEVENTCOMMENT()
    {
        return EVENTCOMMENT;
    }
    public void setEVENTCOMMENT(String EVENTCOMMENT)
    {
        this.EVENTCOMMENT = EVENTCOMMENT;
        this.DATAMAP.put("EVENTCOMMENT", EVENTCOMMENT);
    }


	// Key Validation
	public void checkKeyNotNull() {
		if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.RELATIONTIMEKEY == null || this.ITEMNO == null || this.TIMEKEY == null) {
			// [{1}] 다음의 필수 Key 값이 누락되었습니다.
			throw new KeyRequiredException(new Object[] { "PLANTID, RELATIONTIMEKEY, ITEMNO, TIMEKEY" });
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
