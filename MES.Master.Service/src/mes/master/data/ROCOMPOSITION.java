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
public class ROCOMPOSITION
{
    // Key Info
    private String PLANTID;
    private String PROCESSROUTEID;
    private String PROCESSID;
    private Long PROCESSSEQUENCE;

    // Data Info
    private String ROCOMPOSITIONID;
    private String CONCURRENCYPROCESSID;
    private Long CONCURRENCYSEQUENCE;
    private String PROCESSNAME;
    private String PROCESSMODE;
    private String CREATELOTFLAG;
    private String CREATELOTRULE;
    private String COSTCENTER;
    private String ENDOFROUTE;
    private String AUTOTRACKIN;
    private String AUTOTRACKOUT;
    private String DESCRIPTION;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String ERPPROCESSCODE;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public ROCOMPOSITION() { }

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


    // Data Methods
    // ROCOMPOSITIONID
    public String getROCOMPOSITIONID()
    {
        return ROCOMPOSITIONID;
    }
    public void setROCOMPOSITIONID(String ROCOMPOSITIONID)
    {
        this.ROCOMPOSITIONID = ROCOMPOSITIONID;
        this.DATAMAP.put("ROCOMPOSITIONID", ROCOMPOSITIONID);
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

    // PROCESSNAME
    public String getPROCESSNAME()
    {
        return PROCESSNAME;
    }
    public void setPROCESSNAME(String PROCESSNAME)
    {
        this.PROCESSNAME = PROCESSNAME;
        this.DATAMAP.put("PROCESSNAME", PROCESSNAME);
    }

    // PROCESSMODE
    public String getPROCESSMODE()
    {
        return PROCESSMODE;
    }
    public void setPROCESSMODE(String PROCESSMODE)
    {
        this.PROCESSMODE = PROCESSMODE;
        this.DATAMAP.put("PROCESSMODE", PROCESSMODE);
    }

    // CREATELOTFLAG
    public String getCREATELOTFLAG()
    {
        return CREATELOTFLAG;
    }
    public void setCREATELOTFLAG(String CREATELOTFLAG)
    {
        this.CREATELOTFLAG = CREATELOTFLAG;
        this.DATAMAP.put("CREATELOTFLAG", CREATELOTFLAG);
    }

    // CREATELOTRULE
    public String getCREATELOTRULE()
    {
        return CREATELOTRULE;
    }
    public void setCREATELOTRULE(String CREATELOTRULE)
    {
        this.CREATELOTRULE = CREATELOTRULE;
        this.DATAMAP.put("CREATELOTRULE", CREATELOTRULE);
    }

    // COSTCENTER
    public String getCOSTCENTER()
    {
        return COSTCENTER;
    }
    public void setCOSTCENTER(String COSTCENTER)
    {
        this.COSTCENTER = COSTCENTER;
        this.DATAMAP.put("COSTCENTER", COSTCENTER);
    }

    // ENDOFROUTE
    public String getENDOFROUTE()
    {
        return ENDOFROUTE;
    }
    public void setENDOFROUTE(String ENDOFROUTE)
    {
        this.ENDOFROUTE = ENDOFROUTE;
        this.DATAMAP.put("ENDOFROUTE", ENDOFROUTE);
    }

    // AUTOTRACKIN
    public String getAUTOTRACKIN()
    {
        return AUTOTRACKIN;
    }
    public void setAUTOTRACKIN(String AUTOTRACKIN)
    {
        this.AUTOTRACKIN = AUTOTRACKIN;
        this.DATAMAP.put("AUTOTRACKIN", AUTOTRACKIN);
    }

    // AUTOTRACKOUT
    public String getAUTOTRACKOUT()
    {
        return AUTOTRACKOUT;
    }
    public void setAUTOTRACKOUT(String AUTOTRACKOUT)
    {
        this.AUTOTRACKOUT = AUTOTRACKOUT;
        this.DATAMAP.put("AUTOTRACKOUT", AUTOTRACKOUT);
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


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PROCESSROUTEID == null || this.PROCESSID == null || this.PROCESSSEQUENCE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PROCESSROUTEID, PROCESSID, PROCESSSEQUENCE"});
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
