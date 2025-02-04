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
public class TPCPOLICY
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;
    private String COMPOSITIONID;
    private String RECIPEPARAMETERID;
    private String BOMID;
    private String BOMVERSION;

    // Data Info
    private String SPECTYPE;
    private String TARGET;
    private String LOWERSPECLIMIT;
    private String UPPERSPECLIMIT;
    private String LOWERCONTROLLIMIT;
    private String UPPERCONTROLLIMIT;
    private String LOWERSCREENLIMIT;
    private String UPPERSCREENLIMIT;
    private String OBJECTTYPE;
    private String CTPFLAG;
    private String RECIPEPARAMETERMODE;
    private Long ORDERINDEX;
    private String ALARMID;
    private String DESCRIPTION;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public TPCPOLICY() { }

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

    // COMPOSITIONID
    public String getKeyCOMPOSITIONID()
    {
        return COMPOSITIONID;
    }
    public void setKeyCOMPOSITIONID(String COMPOSITIONID)
    {
        this.COMPOSITIONID = COMPOSITIONID;
        this.KEYMAP.put("COMPOSITIONID", COMPOSITIONID);
    }

    // RECIPEPARAMETERID
    public String getKeyRECIPEPARAMETERID()
    {
        return RECIPEPARAMETERID;
    }
    public void setKeyRECIPEPARAMETERID(String RECIPEPARAMETERID)
    {
        this.RECIPEPARAMETERID = RECIPEPARAMETERID;
        this.KEYMAP.put("RECIPEPARAMETERID", RECIPEPARAMETERID);
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
    // SPECTYPE
    public String getSPECTYPE()
    {
        return SPECTYPE;
    }
    public void setSPECTYPE(String SPECTYPE)
    {
        this.SPECTYPE = SPECTYPE;
        this.DATAMAP.put("SPECTYPE", SPECTYPE);
    }

    // TARGET
    public String getTARGET()
    {
        return TARGET;
    }
    public void setTARGET(String TARGET)
    {
        this.TARGET = TARGET;
        this.DATAMAP.put("TARGET", TARGET);
    }

    // LOWERSPECLIMIT
    public String getLOWERSPECLIMIT()
    {
        return LOWERSPECLIMIT;
    }
    public void setLOWERSPECLIMIT(String LOWERSPECLIMIT)
    {
        this.LOWERSPECLIMIT = LOWERSPECLIMIT;
        this.DATAMAP.put("LOWERSPECLIMIT", LOWERSPECLIMIT);
    }

    // UPPERSPECLIMIT
    public String getUPPERSPECLIMIT()
    {
        return UPPERSPECLIMIT;
    }
    public void setUPPERSPECLIMIT(String UPPERSPECLIMIT)
    {
        this.UPPERSPECLIMIT = UPPERSPECLIMIT;
        this.DATAMAP.put("UPPERSPECLIMIT", UPPERSPECLIMIT);
    }

    // LOWERCONTROLLIMIT
    public String getLOWERCONTROLLIMIT()
    {
        return LOWERCONTROLLIMIT;
    }
    public void setLOWERCONTROLLIMIT(String LOWERCONTROLLIMIT)
    {
        this.LOWERCONTROLLIMIT = LOWERCONTROLLIMIT;
        this.DATAMAP.put("LOWERCONTROLLIMIT", LOWERCONTROLLIMIT);
    }

    // UPPERCONTROLLIMIT
    public String getUPPERCONTROLLIMIT()
    {
        return UPPERCONTROLLIMIT;
    }
    public void setUPPERCONTROLLIMIT(String UPPERCONTROLLIMIT)
    {
        this.UPPERCONTROLLIMIT = UPPERCONTROLLIMIT;
        this.DATAMAP.put("UPPERCONTROLLIMIT", UPPERCONTROLLIMIT);
    }

    // LOWERSCREENLIMIT
    public String getLOWERSCREENLIMIT()
    {
        return LOWERSCREENLIMIT;
    }
    public void setLOWERSCREENLIMIT(String LOWERSCREENLIMIT)
    {
        this.LOWERSCREENLIMIT = LOWERSCREENLIMIT;
        this.DATAMAP.put("LOWERSCREENLIMIT", LOWERSCREENLIMIT);
    }

    // UPPERSCREENLIMIT
    public String getUPPERSCREENLIMIT()
    {
        return UPPERSCREENLIMIT;
    }
    public void setUPPERSCREENLIMIT(String UPPERSCREENLIMIT)
    {
        this.UPPERSCREENLIMIT = UPPERSCREENLIMIT;
        this.DATAMAP.put("UPPERSCREENLIMIT", UPPERSCREENLIMIT);
    }

    // OBJECTTYPE
    public String getOBJECTTYPE()
    {
        return OBJECTTYPE;
    }
    public void setOBJECTTYPE(String OBJECTTYPE)
    {
        this.OBJECTTYPE = OBJECTTYPE;
        this.DATAMAP.put("OBJECTTYPE", OBJECTTYPE);
    }

    // CTPFLAG
    public String getCTPFLAG()
    {
        return CTPFLAG;
    }
    public void setCTPFLAG(String CTPFLAG)
    {
        this.CTPFLAG = CTPFLAG;
        this.DATAMAP.put("CTPFLAG", CTPFLAG);
    }

    // RECIPEPARAMETERMODE
    public String getRECIPEPARAMETERMODE()
    {
        return RECIPEPARAMETERMODE;
    }
    public void setRECIPEPARAMETERMODE(String RECIPEPARAMETERMODE)
    {
        this.RECIPEPARAMETERMODE = RECIPEPARAMETERMODE;
        this.DATAMAP.put("RECIPEPARAMETERMODE", RECIPEPARAMETERMODE);
    }

    // ORDERINDEX
    public Long getORDERINDEX()
    {
        return ORDERINDEX;
    }
    public void setORDERINDEX(Long ORDERINDEX)
    {
        this.ORDERINDEX = ORDERINDEX;
        this.DATAMAP.put("ORDERINDEX", ORDERINDEX);
    }

    // ALARMID
    public String getALARMID()
    {
        return ALARMID;
    }
    public void setALARMID(String ALARMID)
    {
        this.ALARMID = ALARMID;
        this.DATAMAP.put("ALARMID", ALARMID);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTID == null || this.COMPOSITIONID == null || this.RECIPEPARAMETERID == null || this.BOMID == null || this.BOMVERSION == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTID, COMPOSITIONID, RECIPEPARAMETERID, BOMID, BOMVERSION"});
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
