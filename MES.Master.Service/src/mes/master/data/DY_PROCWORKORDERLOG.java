package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.22
 * 
 * @see
 */
public class DY_PROCWORKORDERLOG {
	
	// Key Info
	private String PLANTID;
	private String DATAKEY;
	private String TIMEKEY;
	
	// Data Info
	private String COLUMNNAME;
	private String CONTENT;
	private String EVENTNAME;
	private String EVENTTYPE;
	private Timestamp EVENTTIME;
	private String EVENTUSERID;
	private String EVENTCOMMENT;

	
	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public DY_PROCWORKORDERLOG() {}
    
    // Key Methods
    // PLANTID
    public String getKeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}
	
	//DATAKEY
	public String getKeyDATAKEY() {
		return DATAKEY;
	}
	public void setKeyDATAKEY(String DATAKEY) {
		this.DATAKEY = DATAKEY;
		this.KEYMAP.put("DATAKEY", DATAKEY);
	}
	
	//TIMEKEY
	public String getKeyTIMEKEY() {
		return TIMEKEY;
	}
	public void setKeyTIMEKEY(String TIMEKEY) {
		this.TIMEKEY = TIMEKEY;
		this.KEYMAP.put("TIMEKEY", TIMEKEY);
	}
	
	// Data Methods
	public String getCOLUMNNAME() {
		return COLUMNNAME;
	}

	public void setCOLUMNNAME(String COLUMNNAME) {
		this.COLUMNNAME = COLUMNNAME;
		this.DATAMAP.put("COLUMNNAME", COLUMNNAME);
	}

	public String getCONTENT() {
		return CONTENT;
	}

	public void setCONTENT(String CONTENT) {
		this.CONTENT = CONTENT;
		this.DATAMAP.put("CONTENT", CONTENT);
	}

	public String getEVENTNAME() {
		return EVENTNAME;
	}

	public void setEVENTNAME(String EVENTNAME) {
		this.EVENTNAME = EVENTNAME;
		this.DATAMAP.put("EVENTNAME", EVENTNAME);
	}

	public String getEVENTTYPE() {
		return EVENTTYPE;
	}

	public void setEVENTTYPE(String EVENTTYPE) {
		this.EVENTTYPE = EVENTTYPE;
		this.DATAMAP.put("EVENTTYPE", EVENTTYPE);
	}

	public Timestamp getEVENTTIME() {
		return EVENTTIME;
	}

	public void setEVENTTIME(Timestamp EVENTTIME) {
		this.EVENTTIME = EVENTTIME;
		this.DATAMAP.put("EVENTTIME", EVENTTIME);
	}

	public String getEVENTUSERID() {
		return EVENTUSERID;
	}

	public void setEVENTUSERID(String EVENTUSERID) {
		this.EVENTUSERID = EVENTUSERID;
		this.DATAMAP.put("EVENTUSERID", EVENTUSERID);
	}

	public String getEVENTCOMMENT() {
		return EVENTCOMMENT;
	}

	public void setEVENTCOMMENT(String EVENTCOMMENT) {
		this.EVENTCOMMENT = EVENTCOMMENT;
		this.DATAMAP.put("EVENTCOMMENT", EVENTCOMMENT);
	}

	// Key Validation
    public void checkKeyNotNull() 
    {
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.DATAKEY == null || this.TIMEKEY == null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, DATAKEY, TIMEKEY"});
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
