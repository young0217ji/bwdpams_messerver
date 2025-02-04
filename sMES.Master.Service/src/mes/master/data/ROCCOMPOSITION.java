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
public class ROCCOMPOSITION
{
    // Key Info
    private String PLANTID;
    private String PROCESSROUTEID;
    private String PROCESSID;
    private Long PROCESSSEQUENCE;
    private String RECIPEID;
    private String RECIPETYPE;
    private String RECIPERELATIONCODE;
    private String RECIPETYPECODE;
    private Long RECIPESEQUENCE;

    // Data Info
    private String ROCCOMPOSITIONID;
    private String CONCURRENCYPROCESSID;
    private Long CONCURRENCYSEQUENCE;
    private String RECIPENAME;
    private String RECIPEMODE;
    private String CONSUMETYPE;
    private String CONSUMEID;
    private String DESCRIPTION;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public ROCCOMPOSITION() { }

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

    // PROCESSROUTEID
    public String getKeyPROCESSROUTEID()
    {
        return PROCESSROUTEID;
    }
    public void setKeyPROCESSROUTEID(String PROCESSROUTEID)
    {
        this.PROCESSROUTEID = PROCESSROUTEID;
        this.KEYMAP.put("PROCESSROUTEID", PROCESSROUTEID);
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

    // RECIPEID
    public String getKeyRECIPEID()
    {
        return RECIPEID;
    }
    public void setKeyRECIPEID(String RECIPEID)
    {
        this.RECIPEID = RECIPEID;
        this.KEYMAP.put("RECIPEID", RECIPEID);
    }

    // RECIPETYPE
    public String getKeyRECIPETYPE()
    {
        return RECIPETYPE;
    }
    public void setKeyRECIPETYPE(String RECIPETYPE)
    {
        this.RECIPETYPE = RECIPETYPE;
        this.KEYMAP.put("RECIPETYPE", RECIPETYPE);
    }

    // RECIPERELATIONCODE
    public String getKeyRECIPERELATIONCODE()
    {
        return RECIPERELATIONCODE;
    }
    public void setKeyRECIPERELATIONCODE(String RECIPERELATIONCODE)
    {
        this.RECIPERELATIONCODE = RECIPERELATIONCODE;
        this.KEYMAP.put("RECIPERELATIONCODE", RECIPERELATIONCODE);
    }

    // RECIPETYPECODE
    public String getKeyRECIPETYPECODE()
    {
        return RECIPETYPECODE;
    }
    public void setKeyRECIPETYPECODE(String RECIPETYPECODE)
    {
        this.RECIPETYPECODE = RECIPETYPECODE;
        this.KEYMAP.put("RECIPETYPECODE", RECIPETYPECODE);
    }

    // RECIPESEQUENCE
    public Long getKeyRECIPESEQUENCE()
    {
        return RECIPESEQUENCE;
    }
    public void setKeyRECIPESEQUENCE(Long RECIPESEQUENCE)
    {
        this.RECIPESEQUENCE = RECIPESEQUENCE;
        this.KEYMAP.put("RECIPESEQUENCE", RECIPESEQUENCE);
    }


    // Data Methods
    // ROCCOMPOSITIONID
    public String getROCCOMPOSITIONID()
    {
        return ROCCOMPOSITIONID;
    }
    public void setROCCOMPOSITIONID(String ROCCOMPOSITIONID)
    {
        this.ROCCOMPOSITIONID = ROCCOMPOSITIONID;
        this.DATAMAP.put("ROCCOMPOSITIONID", ROCCOMPOSITIONID);
    }

    // CONCURRENCYPROCESSID
    public String getCONCURRENCYPROCESSID()
    {
        return CONCURRENCYPROCESSID;
    }
    public void setCONCURRENCYPROCESSID(String CONCURRENCYPROCESSID)
    {
        this.CONCURRENCYPROCESSID = CONCURRENCYPROCESSID;
        this.DATAMAP.put("CONCURRENCYPROCESSID", CONCURRENCYPROCESSID);
    }

    // CONCURRENCYSEQUENCE
    public Long getCONCURRENCYSEQUENCE()
    {
        return CONCURRENCYSEQUENCE;
    }
    public void setCONCURRENCYSEQUENCE(Long CONCURRENCYSEQUENCE)
    {
        this.CONCURRENCYSEQUENCE = CONCURRENCYSEQUENCE;
        this.DATAMAP.put("CONCURRENCYSEQUENCE", CONCURRENCYSEQUENCE);
    }

    // RECIPENAME
    public String getRECIPENAME()
    {
        return RECIPENAME;
    }
    public void setRECIPENAME(String RECIPENAME)
    {
        this.RECIPENAME = RECIPENAME;
        this.DATAMAP.put("RECIPENAME", RECIPENAME);
    }

    // RECIPEMODE
    public String getRECIPEMODE()
    {
        return RECIPEMODE;
    }
    public void setRECIPEMODE(String RECIPEMODE)
    {
        this.RECIPEMODE = RECIPEMODE;
        this.DATAMAP.put("RECIPEMODE", RECIPEMODE);
    }

    // CONSUMETYPE
    public String getCONSUMETYPE()
    {
        return CONSUMETYPE;
    }
    public void setCONSUMETYPE(String CONSUMETYPE)
    {
        this.CONSUMETYPE = CONSUMETYPE;
        this.DATAMAP.put("CONSUMETYPE", CONSUMETYPE);
    }

    // CONSUMEID
    public String getCONSUMEID()
    {
        return CONSUMEID;
    }
    public void setCONSUMEID(String CONSUMEID)
    {
        this.CONSUMEID = CONSUMEID;
        this.DATAMAP.put("CONSUMEID", CONSUMEID);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PROCESSROUTEID == null || this.PROCESSID == null || this.PROCESSSEQUENCE == null || this.RECIPEID == null || this.RECIPETYPE == null || this.RECIPERELATIONCODE == null || this.RECIPETYPECODE == null || this.RECIPESEQUENCE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PROCESSROUTEID, PROCESSID, PROCESSSEQUENCE, RECIPEID, RECIPETYPE, RECIPERELATIONCODE, RECIPETYPECODE, RECIPESEQUENCE"});
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
