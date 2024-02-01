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
public class DY_SHIFT {
	
	// Key Info
	private String PLANTID;
	private String SHIFTID;
	
	// Data Info
	private String SHIFTTIME;
	private String SHIFTNAME;
	private String DESCRIPTIONDAY;
	private String DESCRIPTIONNIGHT;
	private String CREATEUSERID;
	private Timestamp CREATETIME;
	private String LASTUPDATEUSERID;
	private Timestamp LASTUPDATETIME;
	
	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public DY_SHIFT() {}
    
    // Key Methods
    // PALNTID
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
    
    // Data Methods
    // SHIFTTIME
	public String getSHIFTTIME() {
		return SHIFTTIME;
	}
	public void setSHIFTTIME(String SHIFTTIME) {
		this.SHIFTTIME = SHIFTTIME;
		this.DATAMAP.put("SHIFTTIME", SHIFTTIME);
	}
	
	// SHIFTNAME
	public String getSHIFTNAME() {
		return SHIFTNAME;
	}
	public void setSHIFTNAME(String SHIFTNAME) {
		this.SHIFTNAME = SHIFTNAME;
		this.DATAMAP.put("SHIFTNAME", SHIFTNAME);
	}
	
	// DESCRIPTIONDAY
	public String getDESCRIPTIONDAY() {
		return DESCRIPTIONDAY;
	}
	public void setDESCRIPTIONDAY(String DESCRIPTIONDAY) {
		this.DESCRIPTIONDAY = DESCRIPTIONDAY;
		this.DATAMAP.put("DESCRIPTIONDAY", DESCRIPTIONDAY);
	}
	
	// DESCRIPTIONNIGHT
	public String getDESCRIPTIONNIGHT() {
		return DESCRIPTIONNIGHT;
	}
	public void setDESCRIPTIONNIGHT(String DESCRIPTIONNIGHT) {
		this.DESCRIPTIONNIGHT = DESCRIPTIONNIGHT;
		this.DATAMAP.put("DESCRIPTIONNIGHT", DESCRIPTIONNIGHT);
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
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.SHIFTID == null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, SHIFTID"});
        }
    }
	
	// KEYMAP
	public HashMap<String, Object> getKEYMAP() {
		return KEYMAP;
	}
	public void setKEYMAP(HashMap<String, Object> KEYMAP) {
		this.KEYMAP = KEYMAP;
	}
	
	// DATAMAP
	public HashMap<String, Object> getDATAMAP() {
		return DATAMAP;
	}
	public void setDATAMAP(HashMap<String, Object> DATAMAP) {
		this.DATAMAP = DATAMAP;
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
