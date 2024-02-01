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
public class DY_INSPECTIONREPORT
{
    // Key Info
    private String PLANTID;
    private String WORKORDERID;
    private Integer INSPECTIONSEQUENCE;

    // Data Info
    private String INSPECTIONDATE;
    private String PRODUCTID;
    private String WORKCENTER;
    private String INSPECTIONSERIALTYPE;
    private String ENDPRODUCTID;
    private String USERPRODUCTID;
    private Integer INSPECTIONQUANTITY;
    private Integer GOODQUANTITY;
    private String INSPECTIONMETHOD;
    private String HC_INSIDE_DIA;
    private String HC_SIDTH;
    private String KC_INSIDE_DIA;
    private String KC_SIDTH;
    private String CLOSED_LENTH;
    private String LENGTH_RC_TO_KC;
    private String DESCRIPTION;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_INSPECTIONREPORT() { }

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

    // Data Methods
    // INSPECTIONDATE
    public String getINSPECTIONDATE()
    {
        return INSPECTIONDATE;
    }
    public void setINSPECTIONDATE(String INSPECTIONDATE)
    {
        this.INSPECTIONDATE = INSPECTIONDATE;
        this.DATAMAP.put("INSPECTIONDATE", INSPECTIONDATE);
    }

    // PRODUCTID
    public String getPRODUCTID()
    {
        return PRODUCTID;
    }
    public void setPRODUCTID(String PRODUCTID)
    {
        this.PRODUCTID = PRODUCTID;
        this.DATAMAP.put("PRODUCTID", PRODUCTID);
    }

    // WORKCENTER
    public String geWORKCENTER()
    {
        return WORKCENTER;
    }
    public void setWORKCENTER(String WORKCENTER)
    {
        this.WORKCENTER = WORKCENTER;
        this.DATAMAP.put("WORKCENTER", WORKCENTER);
    }

    // INSPECTIONSERIALTYPE
    public String getINSPECTIONSERIALTYPE()
    {
        return INSPECTIONSERIALTYPE;
    }
    public void setINSPECTIONSERIALTYPE(String INSPECTIONSERIALTYPE)
    {
        this.INSPECTIONSERIALTYPE = INSPECTIONSERIALTYPE;
        this.DATAMAP.put("INSPECTIONSERIALTYPE", INSPECTIONSERIALTYPE);
    }

    // ENDPRODUCTID
    public String getENDPRODUCTID()
    {
        return ENDPRODUCTID;
    }
    public void setENDPRODUCTID(String ENDPRODUCTID)
    {
        this.ENDPRODUCTID = ENDPRODUCTID;
        this.DATAMAP.put("ENDPRODUCTID", ENDPRODUCTID);
    }
    
    // USERPRODUCTID
    public String getUSERPRODUCTID()
    {
        return USERPRODUCTID;
    }
    public void setUSERPRODUCTID(String USERPRODUCTID)
    {
        this.USERPRODUCTID = USERPRODUCTID;
        this.DATAMAP.put("USERPRODUCTID", USERPRODUCTID);
    }
    
    // INSPECTIONQUANTITY
    public Integer getINSPECTIONQUANTITY() 
    {
		return INSPECTIONQUANTITY;
	}

	public void setINSPECTIONQUANTITY(Integer INSPECTIONQUANTITY) {
        this.INSPECTIONQUANTITY = INSPECTIONQUANTITY;
        this.DATAMAP.put("INSPECTIONQUANTITY", INSPECTIONQUANTITY);
	}
	
    // GOODQUANTITY
	public Integer getGOODQUANTITY() 
	{
		return GOODQUANTITY;
	}

	public void setGOODQUANTITY(Integer GOODQUANTITY) {
        this.GOODQUANTITY = GOODQUANTITY;
        this.DATAMAP.put("GOODQUANTITY", GOODQUANTITY);
	}

    // INSPECTIONMETHOD
	public String getINSPECTIONMETHOD() 
	{
		return INSPECTIONMETHOD;
	}

	public void setINSPECTIONMETHOD(String INSPECTIONMETHOD) {
        this.INSPECTIONMETHOD = INSPECTIONMETHOD;
        this.DATAMAP.put("INSPECTIONMETHOD", INSPECTIONMETHOD);
	}

    // HC_INSIDE_DIA
	public String getHC_INSIDE_DIA() 
	{
		return HC_INSIDE_DIA;
	}

	public void setHC_INSIDE_DIA(String HC_INSIDE_DIA) {
        this.HC_INSIDE_DIA = HC_INSIDE_DIA;
        this.DATAMAP.put("HC_INSIDE_DIA", HC_INSIDE_DIA);
	}

    // HC_SIDTH
	public String getHC_SIDTH() 
	{
		return HC_SIDTH;
	}

	public void setHC_SIDTH(String HC_SIDTH) {
        this.HC_SIDTH = HC_SIDTH;
        this.DATAMAP.put("HC_SIDTH", HC_SIDTH);
	}

    // KC_INSIDE_DIA
	public String getKC_INSIDE_DIA() 
	{
		return KC_INSIDE_DIA;
	}

	public void setKC_INSIDE_DIA(String KC_INSIDE_DIA) {
        this.KC_INSIDE_DIA = KC_INSIDE_DIA;
        this.DATAMAP.put("KC_INSIDE_DIA", KC_INSIDE_DIA);
	}

    // KC_SIDTH
	public String getKC_SIDTH() 
	{
		return KC_SIDTH;
	}

	public void setKC_SIDTH(String KC_SIDTH) {
        this.KC_SIDTH = KC_SIDTH;
        this.DATAMAP.put("KC_SIDTH", KC_SIDTH);
	}

    // CLOSED_LENTH
	public String getCLOSED_LENTH() 
	{
		return CLOSED_LENTH;
	}

	public void setCLOSED_LENTH(String CLOSED_LENTH) {
        this.CLOSED_LENTH = CLOSED_LENTH;
        this.DATAMAP.put("CLOSED_LENTH", CLOSED_LENTH);
	}

    // LENGTH_RC_TO_KC
	public String getLENGTH_RC_TO_KC() {
		return LENGTH_RC_TO_KC;
	}

	public void setLENGTH_RC_TO_KC(String LENGTH_RC_TO_KC) {
        this.LENGTH_RC_TO_KC = LENGTH_RC_TO_KC;
        this.DATAMAP.put("LENGTH_RC_TO_KC", LENGTH_RC_TO_KC);
	}
    
    
    // DESCRIPTION
    public String getDESCRIPTION()
    {
        return DESCRIPTION;
    }
    public void setDESCRIPTION(String DESCRIPTION)
    {
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.WORKORDERID == null || this.INSPECTIONSEQUENCE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, WORKORDERID, INSPECTIONSEQUENCE"});
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
