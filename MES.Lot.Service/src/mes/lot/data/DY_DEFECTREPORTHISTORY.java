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
public class DY_DEFECTREPORTHISTORY
{
    // Key Info
    private String PLANTID;
    private String DEFECTREPORTID;
    private String TIMEKEY;

    // Data Info
    private String PRODUCTID;
    private String WORKORDERID;
    private String PROCESSID;
    private String ERPPROCESSID;
    private String LOTID;
    private String EQUIPMENTID;
    private String DEFECTTYPE;
    private String DEFECTSTARTDATE;
    private String DEFECTENDDATE;
    private Timestamp REPORTDATE;
    private Integer DEFECTQTY;
    private String DEFECTDESCRIPTION;
    private String QUALITYREPORTTARGET;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;
    private String EVENTNAME;
	private String EVENTTYPE;
    private Timestamp EVENTTIME;
    private String EVENTUSERID;
    private String EVENTCOMMENT;

    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_DEFECTREPORTHISTORY() { }

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

    // DEFECTREPORTID
    public String getKeyDEFECTREPORTID()
    {
        return DEFECTREPORTID;
    }
    public void setKeyDEFECTREPORTID(String DEFECTREPORTID)
    {
        this.DEFECTREPORTID = DEFECTREPORTID;
        this.KEYMAP.put("DEFECTREPORTID", DEFECTREPORTID);
    }
    
    // TIMEKEY
 	public String getKeyTIMEKEY()
     {
         return TIMEKEY;
     }
     public void setKeyTIMEKEY(String TIMEKEY)
     {
         this.TIMEKEY = TIMEKEY;
         this.KEYMAP.put("TIMEKEY", TIMEKEY);
     }


    // Data Methods
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

    // PROCESSID
    public String gePROCESSID()
    {
        return PROCESSID;
    }
    public void setPROCESSID(String PROCESSID)
    {
        this.PROCESSID = PROCESSID;
        this.DATAMAP.put("PROCESSID", PROCESSID);
    }

    // ERPPROCESSID
    public String getERPPROCESSID()
    {
        return ERPPROCESSID;
    }
    public void setERPPROCESSID(String ERPPROCESSID)
    {
        this.ERPPROCESSID = ERPPROCESSID;
        this.DATAMAP.put("ERPPROCESSID", ERPPROCESSID);
    }

    // LOTID
    public String getLOTID()
    {
        return LOTID;
    }
    public void setLOTID(String LOTID)
    {
        this.LOTID = LOTID;
        this.DATAMAP.put("LOTID", LOTID);
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
    
    // DEFECTTYPE
    public String getDEFECTTYPE() 
    {
		return DEFECTTYPE;
	}

	public void setDEFECTTYPE(String DEFECTTYPE) {
        this.DEFECTTYPE = DEFECTTYPE;
        this.DATAMAP.put("DEFECTTYPE", DEFECTTYPE);
	}
	
    // DEFECTSTARTDATE
	public String getDEFECTSTARTDATE() 
	{
		return DEFECTSTARTDATE;
	}

	public void setDEFECTSTARTDATE(String DEFECTSTARTDATE) {
        this.DEFECTSTARTDATE = DEFECTSTARTDATE;
        this.DATAMAP.put("DEFECTSTARTDATE", DEFECTSTARTDATE);
	}

    // DEFECTENDDATE
	public String getDEFECTENDDATE() 
	{
		return DEFECTENDDATE;
	}

	public void setDEFECTENDDATE(String DEFECTENDDATE) {
        this.DEFECTENDDATE = DEFECTENDDATE;
        this.DATAMAP.put("DEFECTENDDATE", DEFECTENDDATE);
	}

    // REPORTDATE
	public Timestamp getREPORTDATE() 
	{
		return REPORTDATE;
	}

	public void setREPORTDATE(Timestamp REPORTDATE) {
        this.REPORTDATE = REPORTDATE;
        this.DATAMAP.put("REPORTDATE", REPORTDATE);
	}

    // DEFECTQTY
	public Integer getDEFECTQTY() 
	{
		return DEFECTQTY;
	}

	public void setDEFECTQTY(Integer DEFECTQTY) {
        this.DEFECTQTY = DEFECTQTY;
        this.DATAMAP.put("DEFECTQTY", DEFECTQTY);
	}

    // DEFECTDESCRIPTION
	public String getDEFECTDESCRIPTION() 
	{
		return DEFECTDESCRIPTION;
	}

	public void setDEFECTDESCRIPTION(String DEFECTDESCRIPTION) {
        this.DEFECTDESCRIPTION = DEFECTDESCRIPTION;
        this.DATAMAP.put("DEFECTDESCRIPTION", DEFECTDESCRIPTION);
	}

    // QUALITYREPORTTARGET
	public String getQUALITYREPORTTARGET() 
	{
		return QUALITYREPORTTARGET;
	}

	public void setQUALITYREPORTTARGET(String QUALITYREPORTTARGET) {
        this.QUALITYREPORTTARGET = QUALITYREPORTTARGET;
        this.DATAMAP.put("QUALITYREPORTTARGET", QUALITYREPORTTARGET);
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
    
    // EVENTNAME
    public String getEVENTNAME()
    {
        return EVENTNAME;
    }
    public void setEVENTNAME(String EVENTNAME)
    {
        this.EVENTNAME = EVENTNAME;
        this.DATAMAP.put("EVENTNAME", EVENTNAME);
    }
    
    // EVENTTYPE
    public String getEVENTTYPE()
    {
        return EVENTTYPE;
    }
    public void setEVENTTYPE(String EVENTTYPE)
    {
        this.EVENTTYPE = EVENTTYPE;
        this.DATAMAP.put("EVENTTYPE", EVENTTYPE);
    }

    // EVENTTIME
    public Timestamp getEVENTTIME()
    {
        return EVENTTIME;
    }
    public void setEVENTTIME(Timestamp EVENTTIME)
    {
        this.EVENTTIME = EVENTTIME;
        this.DATAMAP.put("EVENTTIME", EVENTTIME);
    }

    // EVENTUSERID
    public String getEVENTUSERID()
    {
        return EVENTUSERID;
    }
    public void setEVENTUSERID(String EVENTUSERID)
    {
        this.EVENTUSERID = EVENTUSERID;
        this.DATAMAP.put("EVENTUSERID", EVENTUSERID);
    }

    // EVENTCOMMENT
    public String getEVENTCOMMENT()
    {
        return EVENTCOMMENT;
    }
    public void setEVENTCOMMENT(String EVENTCOMMENT)
    {
        this.EVENTCOMMENT = EVENTCOMMENT;
        this.DATAMAP.put("EVENTCOMMENT", EVENTCOMMENT);
    }

    
	// Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.DEFECTREPORTID == null || this.TIMEKEY == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, DEFECTREPORTID, TIMEKEY"});
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
