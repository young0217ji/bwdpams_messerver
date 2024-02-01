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
public class DY_WORKCALLENDERDETAIL {

	// Key Info
	private String PLANTID;
	private String WORKDATE;
	private String PROCESSID;
	private String EQUIPMENTID;
	private String SHIFTTYPE;
	
	// Data Info
	private String WORKERID;
	private String DESCRIPTION;
	private String CREATEUSERID;
	private Timestamp CREATETIME;
	private String LASTUPDATEUSERID;
	private Timestamp LASTUPDATETIME;
	
	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public DY_WORKCALLENDERDETAIL() {}

    
    // Key Methods
    // PLANTID
	public String getPLANTID() {
		return PLANTID;
	}
	public void setPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}

	// WORKDATE
	public String getWORKDATE() {
		return WORKDATE;
	}
	public void setWORKDATE(String WORKDATE) {
		this.WORKDATE = WORKDATE;
		this.KEYMAP.put("WORKDATE", WORKDATE);
	}

	// PROCESSID
	public String getPROCESSID() {
		return PROCESSID;
	}
	public void setPROCESSID(String PROCESSID) {
		this.PROCESSID = PROCESSID;
		this.KEYMAP.put("PROCESSID", PROCESSID);
	}

	// EQUIPMENTID
	public String getEQUIPMENTID() {
		return EQUIPMENTID;
	}
	public void setEQUIPMENTID(String EQUIPMENTID) {
		this.EQUIPMENTID = EQUIPMENTID;
		this.KEYMAP.put("EQUIPMENTID", EQUIPMENTID);
	}

	// SHIFTTYPE
	public String getSHIFTTYPE() {
		return SHIFTTYPE;
	}
	public void setSHIFTTYPE(String SHIFTTYPE) {
		this.SHIFTTYPE = SHIFTTYPE;
		this.KEYMAP.put("SHIFTTYPE", SHIFTTYPE);
	}

	// Data Methods
	// WORKERID
	public String getWORKERID() {
		return WORKERID;
	}

	public void setWORKERID(String WORKERID) {
		this.WORKERID = WORKERID;
		this.DATAMAP.put("WORKERID", WORKERID);
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
    public void checkKeyNotNull() 
    {
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.WORKDATE == null || this.PROCESSID == null || this.EQUIPMENTID == null || this.SHIFTTYPE== null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, WORKDATE, PROCESSID, EQUIPMENTID, SHIFTTYPE"});
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
