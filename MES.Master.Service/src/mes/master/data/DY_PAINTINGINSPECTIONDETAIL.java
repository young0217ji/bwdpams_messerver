package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import com.mongodb.internal.connection.Time;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.29
 * 
 * @see
 */
public class DY_PAINTINGINSPECTIONDETAIL {
	
	// Key Info
	private String PLANTID;
	private int DATAKEY;
	private int DATACOUNT;
	
	// Data Info
	private String DATATYPE;
	private Timestamp CHANGEDATE;
	private String CHANGECOMMENT;
	private String CONFIRMUSERID;
	private String DESCRIPTION;
	private String CREATEUSERID;
	private Timestamp CREATETIME;
	private String LASTUPDATEUSERID;
	private Timestamp LASTUPDATETIME;                      
	
	
	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public DY_PAINTINGINSPECTIONDETAIL() {}
    
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
	public int getKeyDATAKEY() {
		return DATAKEY;
	}
	public void setKeyDATAKEY(int DATAKEY) {
		this.DATAKEY = DATAKEY;
		this.KEYMAP.put("DATAKEY", DATAKEY);
	}
	
	// DATACOUNT
	public int getKeyDATACOUNT() {
		return DATACOUNT;
	}
	public void setKeyDATACOUNT(int DATACOUNT) {
		this.DATACOUNT = DATACOUNT;
		this.KEYMAP.put("DATACOUNT", DATACOUNT);
	}
	
	
	// Data Methods
	// DATATYPE
	public String getDATATYPE() {
		return DATATYPE;
	}
	public void setDATATYPE(String DATATYPE) {
		this.DATATYPE = DATATYPE;
		this.DATAMAP.put("DATATYPE", DATATYPE);
	}

	// CHANGEDATE
	public Timestamp getCHANGEDATE() {
		return CHANGEDATE;
	}
	public void setCHANGEDATE(Timestamp CHANGEDATE) {
		this.CHANGEDATE = CHANGEDATE;
		this.DATAMAP.put("CHANGEDATE", CHANGEDATE);
	}

	// CHANGECOMMENT
	public String getCHANGECOMMENT() {
		return CHANGECOMMENT;
	}
	public void setCHANGECOMMENT(String CHANGECOMMENT) {
		this.CHANGECOMMENT = CHANGECOMMENT;
		this.DATAMAP.put("CHANGECOMMENT", CHANGECOMMENT);
	}

	// CONFIRMUSERID
	public String getCONFIRMUSERID() {
		return CONFIRMUSERID;
	}
	public void setCONFIRMUSERID(String CONFIRMUSERID) {
		this.CONFIRMUSERID = CONFIRMUSERID;
		this.DATAMAP.put("CONFIRMUSERID", CONFIRMUSERID);
	}
	
	//DESCRIPTION
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}	
	public void setDESCRIPTION(String DESCRIPTION) {
		this.DESCRIPTION = DESCRIPTION;
		this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
	}
	
	//CREATEUSERID
	public String getCREATEUSERID() {
		return CREATEUSERID;
	}
	public void setCREATEUSERID(String CREATEUSERID) {
		this.CREATEUSERID = CREATEUSERID;
		this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
	}
	
	//CREATETIME
	public Timestamp getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(Timestamp CREATETIME) {
		this.CREATETIME = CREATETIME;
		this.DATAMAP.put("CREATETIME", CREATETIME);
	}
	
	//LASTUPDATEUSERID
	public String getLASTUPDATEUSERID() {
		return LASTUPDATEUSERID;
	}
	public void setLASTUPDATEUSERID(String LASTUPDATEUSERID) {
		this.LASTUPDATEUSERID = LASTUPDATEUSERID;
		this.DATAMAP.put("LASTUPDATEUSERID", LASTUPDATEUSERID);
	}
	
	//LASTUPDATETIME
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
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID"});
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
