package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

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
public class DY_AUTOLOTCREATEERROR
{
    // Key Info
	private String PLANTID;
	private String WORKORDER;
	private String ERRORCODE;

    // Data Info
	private String ERRORCOMMENT;
	private String ERRORTABLE;
	private String RESULTFLAG;
	private Timestamp CREATETIME;
	private String CREATEUSERID;
	private Timestamp LASTUPDATETIME;
	private String LASTUPDATEUSERID;
    
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_AUTOLOTCREATEERROR() { }


    
    // Key Methods
    //PLANTID
    public String getKeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}

    //ERRORCODE
	public String getKeyERRORCODE() {
		return ERRORCODE;
	}
	public void setKeyERRORCODE(String ERRORCODE) {
		this.ERRORCODE = ERRORCODE;
		this.KEYMAP.put("ERRORCODE", ERRORCODE);
	}
	
	//WORKORDER
	public String getKeyWORKORDER() {
		return WORKORDER;
	}
	public void setKeyWORKORDER(String WORKORDER) {
		this.WORKORDER = WORKORDER;
		this.KEYMAP.put("WORKORDER", WORKORDER);
	}

	//Data Method
    //ERRORCOMMENT
	public String getERRORCOMMENT() {
		return ERRORCOMMENT;
	}
	public void setERRORCOMMENT(String ERRORCOMMENT) {
		this.ERRORCOMMENT = ERRORCOMMENT;
		this.DATAMAP.put("ERRORCOMMENT", ERRORCOMMENT);
	}

    //ERRORTABLE
	public String getERRORTABLE() {
		return ERRORTABLE;
	}
	public void setERRORTABLE(String ERRORTABLE) {
		this.ERRORTABLE = ERRORTABLE;
		this.DATAMAP.put("ERRORTABLE", ERRORTABLE);
	}
	
	//RESULTFLAG
	public String getRESULTFLAG() {
		return RESULTFLAG;
	}
	public void setRESULTFLAG(String RESULTFLAG) {
		this.RESULTFLAG = RESULTFLAG;
		this.DATAMAP.put("RESULTFLAG", RESULTFLAG);
	}


    // CREATEUSERID
    public String  getCREATEUSERID()
    {
        return CREATEUSERID;
    }
    public void setCREATEUSERID(String  CREATEUSERID)
    {
        this.CREATEUSERID = CREATEUSERID;
        this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
    }

    // CREATETIME
    public Timestamp getCREATETIME()
    {
        return CREATETIME;
    }
    public void setCREATETIME(Timestamp CREATETIME)
    {
        this.CREATETIME = CREATETIME;
        this.DATAMAP.put("CREATETIME", CREATETIME);
    }

    // LASTUPDATEUSERID
    public String getLASTUPDATEUSERID()
    {
        return LASTUPDATEUSERID;
    }
    public void setLASTUPDATEUSERID(String LASTUPDATEUSERID)
    {
        this.LASTUPDATEUSERID = LASTUPDATEUSERID;
        this.DATAMAP.put("LASTUPDATEUSERID", LASTUPDATEUSERID);
    }

    // LASTUPDATETIME
    public Timestamp getLASTUPDATETIME()
    {
        return LASTUPDATETIME;
    }
    public void setLASTUPDATETIME(Timestamp LASTUPDATETIME)
    {
        this.LASTUPDATETIME = LASTUPDATETIME;
        this.DATAMAP.put("LASTUPDATETIME", LASTUPDATETIME);
    }
    
	
	// Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.WORKORDER == null || this.ERRORCODE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, WORKORDER, ERRORCODE"});
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
