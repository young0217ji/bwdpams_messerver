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
public class PROCESSROUTE
{
    // Key Info
    private String PLANTID;
    private String PROCESSROUTEID;

    // Data Info
    private String PROCESSROUTECODE;
    private String PROCESSROUTENAME;
    private String ACTIVESTATE;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private String PROCESSROUTETYPE;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String DESCRIPTION;
    private String COMMONFLAG;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public PROCESSROUTE() { }

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


    // Data Methods
    // PROCESSROUTECODE
    public String getPROCESSROUTECODE()
    {
        return PROCESSROUTECODE;
    }
    public void setPROCESSROUTECODE(String PROCESSROUTECODE)
    {
        this.PROCESSROUTECODE = PROCESSROUTECODE;
        this.DATAMAP.put("PROCESSROUTECODE", PROCESSROUTECODE);
    }

    // PROCESSROUTENAME
    public String getPROCESSROUTENAME()
    {
        return PROCESSROUTENAME;
    }
    public void setPROCESSROUTENAME(String PROCESSROUTENAME)
    {
        this.PROCESSROUTENAME = PROCESSROUTENAME;
        this.DATAMAP.put("PROCESSROUTENAME", PROCESSROUTENAME);
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

    // PROCESSROUTETYPE
    public String getPROCESSROUTETYPE()
    {
        return PROCESSROUTETYPE;
    }
    public void setPROCESSROUTETYPE(String PROCESSROUTETYPE)
    {
        this.PROCESSROUTETYPE = PROCESSROUTETYPE;
        this.DATAMAP.put("PROCESSROUTETYPE", PROCESSROUTETYPE);
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

    // COMMONFLAG
    public String getCOMMONFLAG()
    {
        return COMMONFLAG;
    }
    public void setCOMMONFLAG(String COMMONFLAG)
    {
        this.COMMONFLAG = COMMONFLAG;
        this.DATAMAP.put("COMMONFLAG", COMMONFLAG);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PROCESSROUTEID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PROCESSROUTEID"});
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
