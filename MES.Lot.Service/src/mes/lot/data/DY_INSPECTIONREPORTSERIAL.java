package mes.lot.data;

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
public class DY_INSPECTIONREPORTSERIAL
{
    // Key Info
    private String PLANTID;
    private String WORKORDERID;
    private Integer INSPECTIONSEQUENCE;
    private String SERIALNUMBER;

    // Data Info
    private String APPEARN;
    private String WORKABILITY;
    private String PROOF_TEST;
    private String INTERNAL_LEAKAGE;
    private String EXTERNAL_LEAKAGE;
    private String MIN_OPER_PRESS;
    private String WELDING;
    private String ROD_SURFACE;
    private String THREAD;
    private String STROKE;
    private String MARK_POINT;
    private String LOTID;
    private String DESCRIPTION;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_INSPECTIONREPORTSERIAL() { }

    // Key Methods
    // PLANTID
    public String getKeyPLANTID()
    {
        return PLANTID;
    }
    public void setKeyPLANTID(String PLANTID)
    {
        this.PLANTID = PLANTID;
        this.KEYMAP.put("PLANTID", PLANTID);
    }

    // WORKORDERID
    public String getKeyWORKORDERID()
    {
    	return WORKORDERID;
    }
    public void setKeyWORKORDERID(String WORKORDERID)
    {
    	this.WORKORDERID = WORKORDERID;
    	this.KEYMAP.put("WORKORDERID", WORKORDERID);
    }

    // INSPECTIONSEQUENCE
    public Integer getKeyINSPECTIONSEQUENCE()
    {
        return INSPECTIONSEQUENCE;
    }
    public void setKeyINSPECTIONSEQUENCE(Integer INSPECTIONSEQUENCE)
    {
        this.INSPECTIONSEQUENCE = INSPECTIONSEQUENCE;
        this.KEYMAP.put("INSPECTIONSEQUENCE", INSPECTIONSEQUENCE);
    }

    
    // SERIALNUMBER
    public String getKeySERIALNUMBER()
    {
    	return SERIALNUMBER;
    }
    public void setKeySERIALNUMBER(String SERIALNUMBER)
    {
    	this.SERIALNUMBER= SERIALNUMBER;
    	this.KEYMAP.put("SERIALNUMBER", SERIALNUMBER);
    }
    
    // Data Methods
    // APPEARN
    public String getAPPEARN()
    {
        return APPEARN;
    }
    public void setAPPEARN(String APPEARN)
    {
        this.APPEARN = APPEARN;
        this.DATAMAP.put("APPEARN", APPEARN);
    }

    // WORKABILITY
    public String getWORKABILITY()
    {
        return WORKABILITY;
    }
    public void setWORKABILITY(String WORKABILITY)
    {
        this.WORKABILITY = WORKABILITY;
        this.DATAMAP.put("WORKABILITY", WORKABILITY);
    }

    // PROOF_TEST
    public String geWPROOF_TEST()
    {
        return PROOF_TEST;
    }
    public void setPROOF_TEST(String PROOF_TEST)
    {
        this.PROOF_TEST = PROOF_TEST;
        this.DATAMAP.put("PROOF_TEST", PROOF_TEST);
    }

    // INTERNAL_LEAKAGE
    public String getINTERNAL_LEAKAGE()
    {
        return INTERNAL_LEAKAGE;
    }
    public void setINTERNAL_LEAKAGE(String INTERNAL_LEAKAGE)
    {
        this.INTERNAL_LEAKAGE = INTERNAL_LEAKAGE;
        this.DATAMAP.put("INTERNAL_LEAKAGE", INTERNAL_LEAKAGE);
    }

    // EXTERNAL_LEAKAGE
    public String getEXTERNAL_LEAKAGE()
    {
        return EXTERNAL_LEAKAGE;
    }
    public void setEXTERNAL_LEAKAGE(String EXTERNAL_LEAKAGE)
    {
        this.EXTERNAL_LEAKAGE = EXTERNAL_LEAKAGE;
        this.DATAMAP.put("EXTERNAL_LEAKAGE", EXTERNAL_LEAKAGE);
    }
    
    // MIN_OPER_PRESS
    public String getMIN_OPER_PRESS()
    {
        return MIN_OPER_PRESS;
    }
    public void setMIN_OPER_PRESS(String MIN_OPER_PRESS)
    {
        this.MIN_OPER_PRESS = MIN_OPER_PRESS;
        this.DATAMAP.put("MIN_OPER_PRESS", MIN_OPER_PRESS);
    }
    
    // WELDING
    public String getWELDING() 
    {
		return WELDING;
	}

	public void setWELDING(String WELDING) {
        this.WELDING = WELDING;
        this.DATAMAP.put("WELDING", WELDING);
	}
	
    // ROD_SURFACE
	public String getROD_SURFACE() 
	{
		return ROD_SURFACE;
	}

	public void setROD_SURFACE(String ROD_SURFACE) {
        this.ROD_SURFACE = ROD_SURFACE;
        this.DATAMAP.put("ROD_SURFACE", ROD_SURFACE);
	}

    // THREAD
	public String getTHREAD() 
	{
		return THREAD;
	}

	public void setTHREAD(String THREAD) {
        this.THREAD = THREAD;
        this.DATAMAP.put("THREAD", THREAD);
	}

    // STROKE
	public String geSTROKE() 
	{
		return STROKE;
	}

	public void setSTROKE(String STROKE) {
        this.STROKE = STROKE;
        this.DATAMAP.put("STROKE", STROKE);
	}

    // MARK_POINT
	public String getMARK_POINT() 
	{
		return MARK_POINT;
	}

	public void setMARK_POINT(String MARK_POINT) {
        this.MARK_POINT = MARK_POINT;
        this.DATAMAP.put("MARK_POINT", MARK_POINT);
	}

    // LOTID
	public String getLOTID() 
	{
		return LOTID;
	}

	public void setLOTID(String LOTID) {
        this.LOTID = LOTID;
        this.DATAMAP.put("LOTID", LOTID);
	}
	
    // DESCRIPTION
	public String getDESCRIPTION() 
	{
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
        this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.WORKORDERID == null || this.INSPECTIONSEQUENCE == null || this.SERIALNUMBER == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, WORKORDERID, INSPECTIONSEQUENCE, SERIALNUMBER"});
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
