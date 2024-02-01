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
public class DURABLEINFORMATION
{
    // Key Info
    private String DURABLEID;

    // Data Info
    private String STANDARDDURABLEID;
    private String DURABLETYPE;
    private Long EXPIRYTIME;
    private Long USAGELIMIT;
    private Double CAPACITY;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private String DURABLESTATE;
    private String PROCESSSTATE;
    private String EQUIPMENTID;
    private String PORTID;
    private Long LOTCOUNT;
    private Long USAGETIME;
    private Long USAGECOUNT;
    private String REASONCODETYPE;
    private String REASONCODE;
    private String DESCRIPTION;
    private String LOTID;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DURABLEINFORMATION() { }

    // Key Methods
    // DURABLEID
    public String getKeyDURABLEID()
    {
        return DURABLEID;
    }
    public void setKeyDURABLEID(String DURABLEID)
    {
        this.DURABLEID = DURABLEID;
        this.KEYMAP.put("DURABLEID", DURABLEID);
    }


    // Data Methods
    // STANDARDDURABLEID
    public String getSTANDARDDURABLEID()
    {
        return STANDARDDURABLEID;
    }
    public void setSTANDARDDURABLEID(String STANDARDDURABLEID)
    {
        this.STANDARDDURABLEID = STANDARDDURABLEID;
        this.DATAMAP.put("STANDARDDURABLEID", STANDARDDURABLEID);
    }

    // DURABLETYPE
    public String getDURABLETYPE()
    {
        return DURABLETYPE;
    }
    public void setDURABLETYPE(String DURABLETYPE)
    {
        this.DURABLETYPE = DURABLETYPE;
        this.DATAMAP.put("DURABLETYPE", DURABLETYPE);
    }

    // EXPIRYTIME
    public Long getEXPIRYTIME()
    {
        return EXPIRYTIME;
    }
    public void setEXPIRYTIME(Long EXPIRYTIME)
    {
        this.EXPIRYTIME = EXPIRYTIME;
        this.DATAMAP.put("EXPIRYTIME", EXPIRYTIME);
    }

    // USAGELIMIT
    public Long getUSAGELIMIT()
    {
        return USAGELIMIT;
    }
    public void setUSAGELIMIT(Long USAGELIMIT)
    {
        this.USAGELIMIT = USAGELIMIT;
        this.DATAMAP.put("USAGELIMIT", USAGELIMIT);
    }

    // CAPACITY
    public Double getCAPACITY()
    {
        return CAPACITY;
    }
    public void setCAPACITY(Double CAPACITY)
    {
        this.CAPACITY = CAPACITY;
        this.DATAMAP.put("CAPACITY", CAPACITY);
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
    public String getCREATEUSERID()
    {
        return CREATEUSERID;
    }
    public void setCREATEUSERID(String CREATEUSERID)
    {
        this.CREATEUSERID = CREATEUSERID;
        this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
    }

    // DURABLESTATE
    public String getDURABLESTATE()
    {
        return DURABLESTATE;
    }
    public void setDURABLESTATE(String DURABLESTATE)
    {
        this.DURABLESTATE = DURABLESTATE;
        this.DATAMAP.put("DURABLESTATE", DURABLESTATE);
    }

    // PROCESSSTATE
    public String getPROCESSSTATE()
    {
        return PROCESSSTATE;
    }
    public void setPROCESSSTATE(String PROCESSSTATE)
    {
        this.PROCESSSTATE = PROCESSSTATE;
        this.DATAMAP.put("PROCESSSTATE", PROCESSSTATE);
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
    
    // PORTID
    public String getPORTID()
    {
        return PORTID;
    }
    public void setPORTID(String PORTID)
    {
        this.PORTID = PORTID;
        this.DATAMAP.put("PORTID", PORTID);
    }

    // LOTCOUNT
    public Long getLOTCOUNT()
    {
        return LOTCOUNT;
    }
    public void setLOTCOUNT(Long LOTCOUNT)
    {
        this.LOTCOUNT = LOTCOUNT;
        this.DATAMAP.put("LOTCOUNT", LOTCOUNT);
    }

    // USAGETIME
    public Long getUSAGETIME()
    {
        return USAGETIME;
    }
    public void setUSAGETIME(Long USAGETIME)
    {
        this.USAGETIME = USAGETIME;
        this.DATAMAP.put("USAGETIME", USAGETIME);
    }

    // USAGECOUNT
    public Long getUSAGECOUNT()
    {
        return USAGECOUNT;
    }
    public void setUSAGECOUNT(Long USAGECOUNT)
    {
        this.USAGECOUNT = USAGECOUNT;
        this.DATAMAP.put("USAGECOUNT", USAGECOUNT);
    }

    // REASONCODETYPE
    public String getREASONCODETYPE()
    {
        return REASONCODETYPE;
    }
    public void setREASONCODETYPE(String REASONCODETYPE)
    {
        this.REASONCODETYPE = REASONCODETYPE;
        this.DATAMAP.put("REASONCODETYPE", REASONCODETYPE);
    }

    // REASONCODE
    public String getREASONCODE()
    {
        return REASONCODE;
    }
    public void setREASONCODE(String REASONCODE)
    {
        this.REASONCODE = REASONCODE;
        this.DATAMAP.put("REASONCODE", REASONCODE);
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


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.DURABLEID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"DURABLEID"});
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
