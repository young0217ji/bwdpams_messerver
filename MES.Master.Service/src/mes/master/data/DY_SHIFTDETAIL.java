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
public class DY_SHIFTDETAIL {
	
	// Key Info
	private String PLANTID;
	private String SHIFTID;
	private int SEQUENCE;
	
	// Data Info
	private String WORKTYPE;
	private String STARTTIME;
	private String ENDTIME;
	private int WORKTIME;
	private String ALLDAY;
	private String MONDAY;
	private String TUESDAY;
	private String WEDNESDAY;
	private String THURSDAY;
	private String FRIDAY;
	private String SATURDAY;
	private String SUNDAY;
	private String CREATEUSERID;
	private Timestamp CREATETIME;
	private String LASTUPDATEUSERID;
	private Timestamp LASTUPDATETIME;
	
	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public DY_SHIFTDETAIL() { }

    // Key Methods
    // PLANTID
	public String getKeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}

	// SHIFTID
	public String getKeySHIFTID() {
		return SHIFTID;
	}
	public void setKeySHIFTID(String SHIFTID) {
		this.SHIFTID = SHIFTID;
		this.KEYMAP.put("SHIFTID", SHIFTID);
	}

	// SEQ
	public int getKeySEQUENCE() {
		return SEQUENCE;
	}
	public void setKeySEQUENCE(int SEQUENCE) {
		this.SEQUENCE = SEQUENCE;
		this.KEYMAP.put("SEQUENCE", SEQUENCE);
	}

	// Data Methods
	// WORKTYPE
	public String getWORKTYPE() {
		return WORKTYPE;
	}
	public void setWORKTYPE(String WORKTYPE) {
		this.WORKTYPE = WORKTYPE;
		this.DATAMAP.put("WORKTYPE", WORKTYPE);
	}

	// STARTTIME
	public String getSTARTTIME() {
		return STARTTIME;
	}
	public void setSTARTTIME(String STARTTIME) {
		this.STARTTIME = STARTTIME;
		this.DATAMAP.put("STARTTIME", STARTTIME);
	}

	// ENDTIME
	public String getENDTIME() {
		return ENDTIME;
	}
	public void setENDTIME(String ENDTIME) {
		this.ENDTIME = ENDTIME;
		this.DATAMAP.put("ENDTIME", ENDTIME);
	}

	// WORKTIME
	public int getWORKTIME() {
		return WORKTIME;
	}
	public void setWORKTIME(int WORKTIME) {
		this.WORKTIME = WORKTIME;
		this.DATAMAP.put("WORKTIME", WORKTIME);
	}

	// ALLDAY
	public String getALLDAY() {
		return ALLDAY;
	}
	public void setALLDAY(String ALLDAY) {
		this.ALLDAY = ALLDAY;
		this.DATAMAP.put("ALLDAY", ALLDAY);
	}

	// MONDAY
	public String getMONDAY() {
		return MONDAY;
	}
	public void setMONDAY(String MONDAY) {
		this.MONDAY = MONDAY;
		this.DATAMAP.put("MONDAY", MONDAY);
	}

	// TUESDAY
	public String getTUESDAY() {
		return TUESDAY;
	}
	public void setTUESDAY(String TUESDAY) {
		this.TUESDAY = TUESDAY;
		this.DATAMAP.put("TUESDAY", TUESDAY);
	}

	// WEDNESDAY
	public String getWEDNESDAY() {
		return WEDNESDAY;
	}

	public void setWEDNESDAY(String WEDNESDAY) {
		this.WEDNESDAY = WEDNESDAY;
		this.DATAMAP.put("WEDNESDAY", WEDNESDAY);
	}

	// THURSDAY
	public String getTHURSDAY() {
		return THURSDAY;
	}
	public void setTHURSDAY(String THURSDAY) {
		this.THURSDAY = THURSDAY;
		this.DATAMAP.put("THURSDAY", THURSDAY);
	}
	
	// FRIDAY
	public String getFRIDAY() {
		return FRIDAY;
	}
	public void setFRIDAY(String FRIDAY) {
		this.FRIDAY = FRIDAY;
		this.DATAMAP.put("FRIDAY", FRIDAY);
	}

	// SATURDAY
	public String getSATURDAY() {
		return SATURDAY;
	}
	public void setSATURDAY(String SATURDAY) {
		this.SATURDAY = SATURDAY;
		this.DATAMAP.put("SATURDAY", SATURDAY);
	}

	// SUNDAY
	public String getSUNDAY() {
		return SUNDAY;
	}
	public void setSUNDAY(String SUNDAY) {
		this.SUNDAY = SUNDAY;
		this.DATAMAP.put("SUNDAY", SUNDAY);
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
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.SHIFTID == null || this.SEQUENCE == 0)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, SHIFTID, SEQUENCE"});
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
