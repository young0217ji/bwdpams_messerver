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
public class BOMDEFINITION
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;
    private String BOMID;
    private String BOMVERSION;

    // Data Info
    private String BOMTYPE;
    private String CONDITIONTYPE;
    private String CONDITIONID;
    private Long STANDARDVALUE;
    private String STANDARDUNIT;
    private String ACTIVESTATE;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String ACTIVEUSERID;
    private Timestamp ACTIVETIME;
    private String INACTIVEUSERID;
    private Timestamp INACTIVETIME;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public BOMDEFINITION() { }

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

    // PRODUCTID
    public String getKeyPRODUCTID()
    {
        return PRODUCTID;
    }
    public void setKeyPRODUCTID(String PRODUCTID)
    {
        this.PRODUCTID = PRODUCTID;
        this.KEYMAP.put("PRODUCTID", PRODUCTID);
    }

    // BOMID
    public String getKeyBOMID()
    {
        return BOMID;
    }
    public void setKeyBOMID(String BOMID)
    {
        this.BOMID = BOMID;
        this.KEYMAP.put("BOMID", BOMID);
    }

    // BOMVERSION
    public String getKeyBOMVERSION()
    {
        return BOMVERSION;
    }
    public void setKeyBOMVERSION(String BOMVERSION)
    {
        this.BOMVERSION = BOMVERSION;
        this.KEYMAP.put("BOMVERSION", BOMVERSION);
    }


    // Data Methods
    // BOMTYPE
    public String getBOMTYPE()
    {
        return BOMTYPE;
    }
    public void setBOMTYPE(String BOMTYPE)
    {
        this.BOMTYPE = BOMTYPE;
        this.DATAMAP.put("BOMTYPE", BOMTYPE);
    }

    // CONDITIONTYPE
    public String getCONDITIONTYPE()
    {
        return CONDITIONTYPE;
    }
    public void setCONDITIONTYPE(String CONDITIONTYPE)
    {
        this.CONDITIONTYPE = CONDITIONTYPE;
        this.DATAMAP.put("CONDITIONTYPE", CONDITIONTYPE);
    }

    // CONDITIONVALUE
    public String getCONDITIONID()
    {
        return CONDITIONID;
    }
    public void setCONDITIONID(String CONDITIONID)
    {
        this.CONDITIONID = CONDITIONID;
        this.DATAMAP.put("CONDITIONID", CONDITIONID);
    }

    // STANDARDVALUE
    public Long getSTANDARDVALUE()
    {
        return STANDARDVALUE;
    }
    public void setSTANDARDVALUE(Long STANDARDVALUE)
    {
        this.STANDARDVALUE = STANDARDVALUE;
        this.DATAMAP.put("STANDARDVALUE", STANDARDVALUE);
    }

    // STANDARDUNIT
    public String getSTANDARDUNIT()
    {
        return STANDARDUNIT;
    }
    public void setSTANDARDUNIT(String STANDARDUNIT)
    {
        this.STANDARDUNIT = STANDARDUNIT;
        this.DATAMAP.put("STANDARDUNIT", STANDARDUNIT);
    }

    // ACTIVESTATE
    public String getACTIVESTATE()
    {
        return ACTIVESTATE;
    }
    public void setACTIVESTATE(String ACTIVESTATE)
    {
        this.ACTIVESTATE = ACTIVESTATE;
        this.DATAMAP.put("ACTIVESTATE", ACTIVESTATE);
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

    // ACTIVEUSERID
    public String getACTIVEUSERID()
    {
        return ACTIVEUSERID;
    }
    public void setACTIVEUSERID(String ACTIVEUSERID)
    {
        this.ACTIVEUSERID = ACTIVEUSERID;
        this.DATAMAP.put("ACTIVEUSERID", ACTIVEUSERID);
    }

    // ACTIVETIME
    public Timestamp getACTIVETIME()
    {
        return ACTIVETIME;
    }
    public void setACTIVETIME(Timestamp ACTIVETIME)
    {
        this.ACTIVETIME = ACTIVETIME;
        this.DATAMAP.put("ACTIVETIME", ACTIVETIME);
    }

    // INACTIVEUSERID
    public String getINACTIVEUSERID()
    {
        return INACTIVEUSERID;
    }
    public void setINACTIVEUSERID(String INACTIVEUSERID)
    {
        this.INACTIVEUSERID = INACTIVEUSERID;
        this.DATAMAP.put("INACTIVEUSERID", INACTIVEUSERID);
    }

    // INACTIVETIME
    public Timestamp getINACTIVETIME()
    {
        return INACTIVETIME;
    }
    public void setINACTIVETIME(Timestamp INACTIVETIME)
    {
        this.INACTIVETIME = INACTIVETIME;
        this.DATAMAP.put("INACTIVETIME", INACTIVETIME);
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


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTID == null || this.BOMID == null || this.BOMVERSION == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTID, BOMID, BOMVERSION"});
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
