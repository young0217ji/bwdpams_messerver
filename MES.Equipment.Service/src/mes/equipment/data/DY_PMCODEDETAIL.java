package mes.equipment.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class DY_PMCODEDETAIL
{
    // Key Info
    private String PLANTID;
    private String PMCODEID;
    private String CHECKSEQUENCE;

    // Data Info
    private String CHECKITEM;
    private Long CHECKTIME;
    private String MEASURETOOL;
    private String UPPERLIMIT;
    private String TARGETVALUE;
    private String LOWERLIMIT;
    private String DESCRIPTION;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_PMCODEDETAIL() { }

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

    // PMCODEID
    public String getKeyPMCODEID()
    {
        return PMCODEID;
    }
    public void setKeyPMCODEID(String PMCODEID)
    {
        this.PMCODEID = PMCODEID;
        this.KEYMAP.put("PMCODEID", PMCODEID);
    }
    
    // CHECKSEQUENCE
    public String getKeyCHECKSEQUENCE()
    {
        return CHECKSEQUENCE;
    }
    public void setKeyCHECKSEQUENCE(String CHECKSEQUENCE)
    {
        this.CHECKSEQUENCE = CHECKSEQUENCE;
        this.KEYMAP.put("CHECKSEQUENCE", CHECKSEQUENCE);
    }

    
    // Data Methods
    // CHECKITEM
    public String getCHECKITEM()
    {
        return CHECKITEM;
    }
    public void setCHECKITEM(String CHECKITEM)
    {
        this.CHECKITEM = CHECKITEM;
        this.DATAMAP.put("CHECKITEM", CHECKITEM);
    }
    
    // CHECKTIME
    public Long getCHECKTIME()
    {
        return CHECKTIME;
    }
    public void setCHECKTIME(Long CHECKTIME)
    {
        this.CHECKTIME = CHECKTIME;
        this.DATAMAP.put("CHECKTIME", CHECKTIME);
    }
    
    // MEASURETOOL
    public String getMEASURETOOL()
    {
        return MEASURETOOL;
    }
    public void setMEASURETOOL(String MEASURETOOL)
    {
        this.MEASURETOOL = MEASURETOOL;
        this.DATAMAP.put("MEASURETOOL", MEASURETOOL);
    }
    
    // UPPERLIMIT
    public String getUPPERLIMIT()
    {
        return UPPERLIMIT;
    }
    public void setUPPERLIMIT(String UPPERLIMIT)
    {
        this.UPPERLIMIT = UPPERLIMIT;
        this.DATAMAP.put("UPPERLIMIT", UPPERLIMIT);
    }
    
    // TARGETVALUE
    public String getTARGETVALUE()
    {
        return TARGETVALUE;
    }
    public void setTARGETVALUE(String TARGETVALUE)
    {
        this.TARGETVALUE = TARGETVALUE;
        this.DATAMAP.put("TARGETVALUE", TARGETVALUE);
    }
    
    // LOWERLIMIT
    public String getLOWERLIMIT()
    {
        return LOWERLIMIT;
    }
    public void setLOWERLIMIT(String LOWERLIMIT)
    {
        this.LOWERLIMIT = LOWERLIMIT;
        this.DATAMAP.put("LOWERLIMIT", LOWERLIMIT);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PMCODEID == null || this.CHECKSEQUENCE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PMCODEID, CHECKSEQUENCE"});
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