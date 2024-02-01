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
public class DY_PAINTINGPACKINGINFOMATION {
	
	// Key Info
	private String PLANTID;
	private String PRODUCTID;
	private String COMPANYID;
	
	// Data Info
	private String PAINTINGCOLOR;	
	private String PACKINGTYPE;		
	private int PACKINGQUANTITY;	
	private String DESCRIPTION;	
	private String CREATEUSERID;
	private Timestamp CREATETIME;
	private String LASTUPDATEUSERID;
	private Timestamp LASTUPDATETIME;
	
	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public DY_PAINTINGPACKINGINFOMATION() {}
    
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
    public String getKeyPRODUCTID() {
    	return PRODUCTID;
    }
    public void setKeyPRODUCTID(String PRODUCTID) {
    	this.PRODUCTID = PRODUCTID;
    	this.KEYMAP.put("PRODUCTID", PRODUCTID);
    }

    // COMPANYID
    public String getKeyCOMPANYID() {
    	return COMPANYID;
    }
    public void setKeyCOMPANYID(String COMPANYID) {
    	this.COMPANYID = COMPANYID;
    	this.KEYMAP.put("COMPANYID", COMPANYID);
    }
    
    // Data Methods
    // PAINTINGCOLOR
    public String getPAINTINGCOLOR() {
		return PAINTINGCOLOR;
	}

	public void setPAINTINGCOLOR(String PAINTINGCOLOR) {
		this.PAINTINGCOLOR = PAINTINGCOLOR;
		this.DATAMAP.put("PAINTINGCOLOR", PAINTINGCOLOR);
	}

	// PACKINGTYPE
	public String getPACKINGTYPE() {
		return PACKINGTYPE;
	}

	public void setPACKINGTYPE(String PACKINGTYPE) {
		this.PACKINGTYPE = PACKINGTYPE;
		this.DATAMAP.put("PACKINGTYPE", PACKINGTYPE);
	}

	// PACKINGQUANTITY
	public int getPACKINGQUANTITY() {
		return PACKINGQUANTITY;
	}

	public void setPACKINGQUANTITY(int PACKINGQUANTITY) {
		this.PACKINGQUANTITY = PACKINGQUANTITY;
		this.DATAMAP.put("PACKINGQUANTITY", PACKINGQUANTITY);
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
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTID == null || this.COMPANYID == null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTID, COMPANYID"});
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
