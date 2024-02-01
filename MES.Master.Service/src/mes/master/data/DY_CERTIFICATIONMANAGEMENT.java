package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.01
 * 
 * @see
 */
public class DY_CERTIFICATIONMANAGEMENT {
	
	// Key Info
	private String PLANTID;
	private String CERTIFICATIONID;
	
	// Data Info
	private String CERTIFICATIONDATE;
	private String CERTIFICATIONTYPE;
	private String CERTIFICATIONSUBTYPE;
	private String WORKCENTER;
	private String CERTIFICATIONWORKER;
	private String CERTIFICATIONSTATE;
	private String CHANGEOPERDATE;
	private String DISQUALIFICATIONEXPECTDATE;
	private String DISQUALIFICATIONDATE;
	private String DISQUALIFICATIONCOMMENT;
	private String DESCRIPTION;
	private String CREATEUSERID;
	private Timestamp CREATETIME;
	private String LASTUPDATEUSERID;
	private Timestamp LASTUPDATETIME;
	
	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public DY_CERTIFICATIONMANAGEMENT() {}
    
    // Key Methods
    // PLANTID
    public String getKeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}
	
	//DOCUMENTID
	public String getKeyCERTIFICATIONID() {
		return CERTIFICATIONID;
	}
	public void setKeyCERTIFICATIONID(String CERTIFICATIONID) {
		this.CERTIFICATIONID = CERTIFICATIONID;
		this.KEYMAP.put("CERTIFICATIONID", CERTIFICATIONID);
	}
	
	
	// Data Methods
	// CERTIFICATIONDATE
	public String getCERTIFICATIONDATE() {
		return CERTIFICATIONDATE;
	}

	public void setCERTIFICATIONDATE(String CERTIFICATIONDATE) {
		this.CERTIFICATIONDATE = CERTIFICATIONDATE;
		this.DATAMAP.put("CERTIFICATIONDATE", CERTIFICATIONDATE);
	}

	// CERTIFICATIONTYPE
	public String getCERTIFICATIONTYPE() {
		return CERTIFICATIONTYPE;
	}

	public void setCERTIFICATIONTYPE(String CERTIFICATIONTYPE) {
		this.CERTIFICATIONTYPE = CERTIFICATIONTYPE;
		this.DATAMAP.put("CERTIFICATIONTYPE", CERTIFICATIONTYPE);
	}

	// CERTIFICATIONSUBTYPE
	public String getCERTIFICATIONSUBTYPE() {
		return CERTIFICATIONSUBTYPE;
	}

	public void setCERTIFICATIONSUBTYPE(String CERTIFICATIONSUBTYPE) {
		this.CERTIFICATIONSUBTYPE = CERTIFICATIONSUBTYPE;
		this.DATAMAP.put("CERTIFICATIONSUBTYPE", CERTIFICATIONSUBTYPE);
	}
	
	// WORKCENTER
	public String getWORKCENTER() {
		return WORKCENTER;
	}

	public void setWORKCENTER(String WORKCENTER) {
		this.WORKCENTER = WORKCENTER;
		this.DATAMAP.put("WORKCENTER", WORKCENTER);
	}

	// CERTIFICATIONWORKER
	public String getCERTIFICATIONWORKER() {
		return CERTIFICATIONWORKER;
	}

	public void setCERTIFICATIONWORKER(String CERTIFICATIONWORKER) {
		this.CERTIFICATIONWORKER = CERTIFICATIONWORKER;
		this.DATAMAP.put("CERTIFICATIONWORKER", CERTIFICATIONWORKER);
	}

	// CERTIFICATIONSTATE
	public String getCERTIFICATIONSTATE() {
		return CERTIFICATIONSTATE;
	}

	public void setCERTIFICATIONSTATE(String CERTIFICATIONSTATE) {
		this.CERTIFICATIONSTATE = CERTIFICATIONSTATE;
		this.DATAMAP.put("CERTIFICATIONSTATE", CERTIFICATIONSTATE);
	}

	// CHANGEOPERDATE
	public String getCHANGEOPERDATE() {
		return CHANGEOPERDATE;
	}

	public void setCHANGEOPERDATE(String CHANGEOPERDATE) {
		this.CHANGEOPERDATE = CHANGEOPERDATE;
		this.DATAMAP.put("CHANGEOPERDATE", CHANGEOPERDATE);
	}

	// DISQUALIFICATIONEXPECTDATE
	public String getDISQUALIFICATIONEXPECTDATE() {
		return DISQUALIFICATIONEXPECTDATE;
	}

	public void setDISQUALIFICATIONEXPECTDATE(String DISQUALIFICATIONEXPECTDATE) {
		this.DISQUALIFICATIONEXPECTDATE = DISQUALIFICATIONEXPECTDATE;
		this.DATAMAP.put("DISQUALIFICATIONEXPECTDATE", DISQUALIFICATIONEXPECTDATE);
	}

	// DISQUALIFICATIONDATE
	public String getDISQUALIFICATIONDATE() {
		return DISQUALIFICATIONDATE;
	}

	public void setDISQUALIFICATIONDATE(String DISQUALIFICATIONDATE) {
		this.DISQUALIFICATIONDATE = DISQUALIFICATIONDATE;
		this.DATAMAP.put("DISQUALIFICATIONDATE", DISQUALIFICATIONDATE);
	}

	// DISQUALIFICATIONCOMMENT
	public String getDISQUALIFICATIONCOMMENT() {
		return DISQUALIFICATIONCOMMENT;
	}

	public void setDISQUALIFICATIONCOMMENT(String DISQUALIFICATIONCOMMENT) {
		this.DISQUALIFICATIONCOMMENT = DISQUALIFICATIONCOMMENT;
		this.DATAMAP.put("DISQUALIFICATIONCOMMENT", DISQUALIFICATIONCOMMENT);
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
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.CERTIFICATIONID == null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, CERTIFICATIONID"});
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
