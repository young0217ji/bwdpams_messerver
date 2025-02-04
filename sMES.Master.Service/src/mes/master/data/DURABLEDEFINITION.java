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
public class DURABLEDEFINITION
{
    // Key Info
    private String PLANTID;
    private String STANDARDDURABLEID;

    // Data Info
    private String STANDARDDURABLENAME;
    private String ACTIVESTATE;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private String DURABLETYPE;
    private Long EXPIRYTIME;
    private Long USAGELIMIT;
    private Double CAPACITY;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DURABLEDEFINITION() { }

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

    // STANDARDDURABLEID
    public String getKeySTANDARDDURABLEID()
    {
        return STANDARDDURABLEID;
    }
    public void setKeySTANDARDDURABLEID(String STANDARDDURABLEID)
    {
        this.STANDARDDURABLEID = STANDARDDURABLEID;
        this.KEYMAP.put("STANDARDDURABLEID", STANDARDDURABLEID);
    }


    // Data Methods
    // STANDARDDURABLENAME
    public String getSTANDARDDURABLENAME()
    {
        return STANDARDDURABLENAME;
    }
    public void setSTANDARDDURABLENAME(String STANDARDDURABLENAME)
    {
        this.STANDARDDURABLENAME = STANDARDDURABLENAME;
        this.DATAMAP.put("STANDARDDURABLENAME", STANDARDDURABLENAME);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.STANDARDDURABLEID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, STANDARDDURABLEID"});
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
