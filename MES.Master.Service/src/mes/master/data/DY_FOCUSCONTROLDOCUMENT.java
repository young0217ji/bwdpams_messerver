package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.25
 * 
 * @see
 */
public class DY_FOCUSCONTROLDOCUMENT
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;

    // Data Info
    private String P_FILELOCATION;
    private String P_FILENAME;
    private String Q_FILELOCATION;
    private String Q_FILENAME;
    private String USEFLAG;
    private String DESCRIPTION;
    private Timestamp CREATETIME;      
    private String CREATEUSERID;    
    private Timestamp LASTUPDATETIME;  
    private String LASTUPDATEUSERID;

    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_FOCUSCONTROLDOCUMENT() { }

    // Key Methods
    // PLANTID
    public String getKeyPLANTID() {
		return PLANTID;
	}
    
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}
    
    // PROUDUCTID
    public String getKeyPRODUCTID() {
    	return PRODUCTID;
    }
    
    public void setKeyPRODUCTID(String PRODUCTID) {
    	this.PRODUCTID = PRODUCTID;
    	this.KEYMAP.put("PRODUCTID", PRODUCTID);
    }
	
    
	// Data Methods
	// P_FILELOCATION
    public String getP_FILELOCATION() {
		return P_FILELOCATION;
	}
    
	public void setP_FILELOCATION(String P_FILELOCATION) {
		this.P_FILELOCATION = P_FILELOCATION;
		this.DATAMAP.put("P_FILELOCATION", P_FILELOCATION);
	}

	// P_FILENAME
	public String getP_FILENAME() {
		return P_FILENAME;
	}
	
	public void setP_FILENAME(String P_FILENAME) {
		this.P_FILENAME = P_FILENAME;
		this.DATAMAP.put("P_FILENAME", P_FILENAME);
	}

	// Q_FILELOCATION
	public String getQ_FILELOCATION() {
		return Q_FILELOCATION;
	}

	public void setQ_FILELOCATION(String Q_FILELOCATION) {
		this.Q_FILELOCATION = Q_FILELOCATION;
		this.DATAMAP.put("Q_FILELOCATION", Q_FILELOCATION);
	}

	// Q_FILENAME
	public String getQ_FILENAME() {
		return Q_FILENAME;
	}

	public void setQ_FILENAME(String Q_FILENAME) {
		this.Q_FILENAME = Q_FILENAME;
		this.DATAMAP.put("Q_FILENAME", Q_FILENAME);
	}

	// USEFLAG
	public String getUSEFLAG() {
		return USEFLAG;
	}

	public void setUSEFLAG(String USEFLAG) {
		this.USEFLAG = USEFLAG;
		this.DATAMAP.put("USEFLAG", USEFLAG);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null|| this.PRODUCTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID,PRODUCTID"});
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
