package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.05
 * 
 * @see
 */
public class DY_WORKCALLENDER
{
    // Key Info
    private String PLANTID;
    private String WORKDATE;
    private String WORKCENTER;
    private String EQUIPMENTID;

    // Data Info
    private String WORKTYPE;
    private String SHIFTID;
    private int WORKTIME;
    private int CONFIRMFLG;
    private String DESCRIPTION;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_WORKCALLENDER() { }

    // Key Methods
	// PLANTID
    public String getKeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}

	// WORKDATE
	public String getKeyWORKDATE() {
		return WORKDATE;
	}
	public void setKeyWORKDATE(String WORKDATE) {
		this.WORKDATE = WORKDATE;
		this.KEYMAP.put("WORKDATE", WORKDATE);
	}

	// WORKCENTER
	public String getKeyWORKCENTER() {
		return WORKCENTER;
	}
	public void setKeyWORKCENTER(String WORKCENTER) {
		this.WORKCENTER = WORKCENTER;
		this.KEYMAP.put("WORKCENTER", WORKCENTER);
	}

	// EQUIPMENTID
	public String getKeyEQUIPMENTID() {
		return EQUIPMENTID;
	}
	public void setKeyEQUIPMENTID(String EQUIPMENTID) {
		this.EQUIPMENTID = EQUIPMENTID;
		this.KEYMAP.put("EQUIPMENTID", EQUIPMENTID);
	}
	
	
	// Data Methods
	// WRKTYPE
	public String getWORKTYPE() {
		return WORKTYPE;
	}
	public void setWORKTYPE(String WORKTYPE) {
		this.WORKTYPE = WORKTYPE;
		this.DATAMAP.put("WORKTYPE", WORKTYPE);
	}

	// SHIFTID
	public String getSHIFTID() {
		return SHIFTID;
	}
	public void setSHIFTID(String SHIFTID) {
		this.SHIFTID = SHIFTID;
		this.DATAMAP.put("SHIFTID", SHIFTID);
	}

	// WORKTIME
	public int getWORKTIME() {
		return WORKTIME;
	}
	public void setWORKTIME(int WORKTIME) {
		this.WORKTIME = WORKTIME;
		this.DATAMAP.put("WORKTIME", WORKTIME);
	}

	// CONFIRMFLG
	public int getCONFIRMFLG() {
		return CONFIRMFLG;
	}
	public void setCONFIRMFLG(int CONFIRMFLG) {
		this.CONFIRMFLG = CONFIRMFLG;
		this.DATAMAP.put("CONFIRMFLG", CONFIRMFLG);
	}

	// DESCRIPTION
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String DESCRIPTION) {
		this.DESCRIPTION = DESCRIPTION;
		this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
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
    

    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.WORKDATE == null || this.WORKCENTER == null|| this.EQUIPMENTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID,WORKDATE,WORKCENTER,EQUIPMENTID"});
        }
    }


	public void setKEYMAP(HashMap<String, Object> KEYMAP)
    {
        this.KEYMAP = KEYMAP;
    }
    public HashMap<String, Object> getKEYMAP()
    {
        return KEYMAP;
    }
    public void setDATAMAP(HashMap<String, Object> DATAMAP)
    {
        this.DATAMAP = DATAMAP;
    }
    public HashMap<String, Object> getDATAMAP()
    {
        return DATAMAP;
    }

    public Object excuteDML(String type)
    {
        return excuteDML(type, "");
    }

    public Object excuteDML(String type, String suffix)
    {
        if ( type.equalsIgnoreCase(SqlConstant.DML_INSERT) || type.equalsIgnoreCase(SqlConstant.DML_UPDATE) || type.equalsIgnoreCase(SqlConstant.DML_DELETE) )
        {
            checkKeyNotNull();
        }
        Object returnValue = SqlQueryUtil.excuteDML(this.getClass(), type, suffix, KEYMAP, DATAMAP);
        return returnValue;
    }
}
