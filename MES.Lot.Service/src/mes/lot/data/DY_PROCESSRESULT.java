package mes.lot.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.07 
 * 
 * @see
 */
public class DY_PROCESSRESULT
{
    // Key Info
    private String PLANTID;
    private String LOTID;
    private String PROCESSID;
    private Long PROCESSSEQUENCE;
    private Long REWORKCOUNT;

    // Data Info
    private String WORKORDERID;
    private String ERPPROCESSCODE;
    private String PRODUCTORDERTYPE;
    private Double PRODUCTQUANTITY;
    private String PRODUCTUNIT;
    private Double SCRAPQUANTITY;
    private Timestamp STARTTIME;
    private Timestamp ENDTIME;
    private Long PROCESSTIME;
    private Long WORKTIME;
    private Long MACHINETIME;
    private String INTERFACEKEY;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String EQUIPMENTID;
    private Long PROCESSDOWNTIME;
    private Double DEFECTQUANTITY;
    private String PRODUCTORDERID;
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_PROCESSRESULT() { }

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

    // LOTID
    public String getKeyLOTID()
    {
        return LOTID;
    }
    public void setKeyLOTID(String LOTID)
    {
        this.LOTID = LOTID;
        this.KEYMAP.put("LOTID", LOTID);
    }
    
    // PROCESSID
    public String getKeyPROCESSID()
    {
        return PROCESSID;
    }
    public void setKeyPROCESSID(String PROCESSID)
    {
        this.PROCESSID = PROCESSID;
        this.KEYMAP.put("PROCESSID", PROCESSID);
    }
    
    // PROCESSSEQUENCE
    public Long getKeyPROCESSSEQUENCE()
    {
        return PROCESSSEQUENCE;
    }
    public void setKeyPROCESSSEQUENCE(Long PROCESSSEQUENCE)
    {
        this.PROCESSSEQUENCE = PROCESSSEQUENCE;
        this.KEYMAP.put("PROCESSSEQUENCE", PROCESSSEQUENCE);
    }
    
    // REWORKCOUNT
    public Long getKeyREWORKCOUNT()
    {
        return REWORKCOUNT;
    }
    public void setKeyREWORKCOUNT(Long REWORKCOUNT)
    {
        this.REWORKCOUNT = REWORKCOUNT;
        this.KEYMAP.put("REWORKCOUNT", REWORKCOUNT);
    }

    // Data Methods
    // WORKORDERID
    public String getWORKORDERID()
    {
        return WORKORDERID;
    }
    public void setWORKORDERID(String WORKORDERID)
    {
        this.WORKORDERID = WORKORDERID;
        this.DATAMAP.put("WORKORDERID", WORKORDERID);
    }

    // ERPPROCESSCODE
    public String getERPPROCESSCODE()
    {
        return ERPPROCESSCODE;
    }
    public void setERPPROCESSCODE(String ERPPROCESSCODE)
    {
        this.ERPPROCESSCODE = ERPPROCESSCODE;
        this.DATAMAP.put("ERPPROCESSCODE", ERPPROCESSCODE);
    }

    // PRODUCTORDERTYPE
    public String gePRODUCTORDERTYPE()
    {
        return PRODUCTORDERTYPE;
    }
    public void setPRODUCTORDERTYPE(String PRODUCTORDERTYPE)
    {
        this.PRODUCTORDERTYPE = PRODUCTORDERTYPE;
        this.DATAMAP.put("PRODUCTORDERTYPE", PRODUCTORDERTYPE);
    }

    // PRODUCTQUANTITY
    public Double getPRODUCTQUANTITY()
    {
        return PRODUCTQUANTITY;
    }
    public void setPRODUCTQUANTITY(Double PRODUCTQUANTITY)
    {
        this.PRODUCTQUANTITY = PRODUCTQUANTITY;
        this.DATAMAP.put("PRODUCTQUANTITY", PRODUCTQUANTITY);
    }

    // PRODUCTUNIT
    public String getPRODUCTUNIT()
    {
        return PRODUCTUNIT;
    }
    public void setPRODUCTUNIT(String PRODUCTUNIT)
    {
        this.PRODUCTUNIT = PRODUCTUNIT;
        this.DATAMAP.put("PRODUCTUNIT", PRODUCTUNIT);
    }
    
    // SCRAPQUANTITY
    public Double getSCRAPQUANTITY()
    {
        return SCRAPQUANTITY;
    }
    public void setSCRAPQUANTITY(Double SCRAPQUANTITY)
    {
        this.SCRAPQUANTITY = SCRAPQUANTITY;
        this.DATAMAP.put("SCRAPQUANTITY", SCRAPQUANTITY);
    }
    
    // STARTTIME
    public Timestamp getSTARTTIME() 
    {
		return STARTTIME;
	}

	public void setSTARTTIME(Timestamp STARTTIME) {
        this.STARTTIME = STARTTIME;
        this.DATAMAP.put("STARTTIME", STARTTIME);
	}
	
    // ENDTIME
    public Timestamp getENDTIME() 
    {
		return ENDTIME;
	}

	public void setENDTIME(Timestamp ENDTIME) {
        this.ENDTIME = ENDTIME;
        this.DATAMAP.put("ENDTIME", ENDTIME);
	}

    // PROCESSTIME
	public Long getPROCESSTIME() 
	{
		return PROCESSTIME;
	}

	public void setPROCESSTIME(Long PROCESSTIME) {
        this.PROCESSTIME = PROCESSTIME;
        this.DATAMAP.put("PROCESSTIME", PROCESSTIME);
	}

    // WORKTIME
	public Long getWORKTIME() 
	{
		return WORKTIME;
	}

	public void setWORKTIME(Long WORKTIME) {
        this.WORKTIME = WORKTIME;
        this.DATAMAP.put("WORKTIME", WORKTIME);
	}

    // MACHINETIME
	public Long getMACHINETIME() 
	{
		return MACHINETIME;
	}

	public void setMACHINETIME(Long MACHINETIME) {
        this.MACHINETIME = MACHINETIME;
        this.DATAMAP.put("MACHINETIME", MACHINETIME);
	}

    // INTERFACEKEY
    public String getINTERFACEKEY()
    {
        return INTERFACEKEY;
    }
    public void setINTERFACEKEY(String INTERFACEKEY)
    {
        this.INTERFACEKEY = INTERFACEKEY;
        this.DATAMAP.put("INTERFACEKEY", INTERFACEKEY);
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
    
    // EQUIPMENTID
    public String getEQUIPMENTID()
    {
        return EQUIPMENTID;
    }
    public void setEQUIPMENTID(String EQUIPMENTID)
    {
        this.EQUIPMENTID = EQUIPMENTID;
        this.DATAMAP.put("EQUIPMENTID", EQUIPMENTID);
    }
    
    // PROCESSDOWNTIME
	public Long getPROCESSDOWNTIME() 
	{
		return PROCESSDOWNTIME;
	}

	public void setPROCESSDOWNTIME(Long PROCESSDOWNTIME) {
        this.PROCESSDOWNTIME = PROCESSDOWNTIME;
        this.DATAMAP.put("PROCESSDOWNTIME", PROCESSDOWNTIME);
	}
	
	// DEFECTQUANTITY
    public Double getDEFECTQUANTITY()
    {
        return DEFECTQUANTITY;
    }
    public void setDEFECTQUANTITY(Double DEFECTQUANTITY)
    {
        this.DEFECTQUANTITY = DEFECTQUANTITY;
        this.DATAMAP.put("DEFECTQUANTITY", DEFECTQUANTITY);
    }
    
    // PRODUCTORDERID
    public String getPRODUCTORDERID()
    {
        return PRODUCTORDERID;
    }
    public void setPRODUCTORDERID(String PRODUCTORDERID)
    {
        this.PRODUCTORDERID = PRODUCTORDERID;
        this.DATAMAP.put("PRODUCTORDERID", PRODUCTORDERID);
    }
    
	
	// Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.LOTID == null || this.PROCESSID == null || this.PROCESSSEQUENCE == null || this.REWORKCOUNT == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, LOTID, PROCESSID, PROCESSSEQUENCE, REWORKCOUNT"});
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
