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
public class FUTUREACTION
{
    // Key Info
    private String LOTID;
    private String RELATIONTIMEKEY;
    private String SEQUENCE;

    // Data Info
    private String ACTIONTYPE;
    private String ACTIONSTATE;
    private String REASONCODETYPE;
    private String REASONCODE;
    private String ACTIONUSERID;
    private Timestamp ACTIONTIME;
    private String CHANGEUSERID;
    private Timestamp CHANGETIME;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public FUTUREACTION() { }

    // Key Methods
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

    // RELATIONTIMEKEY
    public String getKeyRELATIONTIMEKEY()
    {
        return RELATIONTIMEKEY;
    }
    public void setKeyRELATIONTIMEKEY(String RELATIONTIMEKEY)
    {
        this.RELATIONTIMEKEY = RELATIONTIMEKEY;
        this.KEYMAP.put("RELATIONTIMEKEY", RELATIONTIMEKEY);
    }

    // SEQUENCE
    public String getKeySEQUENCE()
    {
        return SEQUENCE;
    }
    public void setKeySEQUENCE(String SEQUENCE)
    {
        this.SEQUENCE = SEQUENCE;
        this.KEYMAP.put("SEQUENCE", SEQUENCE);
    }


    // Data Methods
    // ACTIONTYPE
    public String getACTIONTYPE()
    {
        return ACTIONTYPE;
    }
    public void setACTIONTYPE(String ACTIONTYPE)
    {
        this.ACTIONTYPE = ACTIONTYPE;
        this.DATAMAP.put("ACTIONTYPE", ACTIONTYPE);
    }

    // ACTIONSTATE
    public String getACTIONSTATE()
    {
        return ACTIONSTATE;
    }
    public void setACTIONSTATE(String ACTIONSTATE)
    {
        this.ACTIONSTATE = ACTIONSTATE;
        this.DATAMAP.put("ACTIONSTATE", ACTIONSTATE);
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

    // ACTIONUSERID
    public String getACTIONUSERID()
    {
        return ACTIONUSERID;
    }
    public void setACTIONUSERID(String ACTIONUSERID)
    {
        this.ACTIONUSERID = ACTIONUSERID;
        this.DATAMAP.put("ACTIONUSERID", ACTIONUSERID);
    }

    // ACTIONTIME
    public Timestamp getACTIONTIME()
    {
        return ACTIONTIME;
    }
    public void setACTIONTIME(Timestamp ACTIONTIME)
    {
        this.ACTIONTIME = ACTIONTIME;
        this.DATAMAP.put("ACTIONTIME", ACTIONTIME);
    }

    // CHANGEUSERID
    public String getCHANGEUSERID()
    {
        return CHANGEUSERID;
    }
    public void setCHANGEUSERID(String CHANGEUSERID)
    {
        this.CHANGEUSERID = CHANGEUSERID;
        this.DATAMAP.put("CHANGEUSERID", CHANGEUSERID);
    }

    // CHANGETIME
    public Timestamp getCHANGETIME()
    {
        return CHANGETIME;
    }
    public void setCHANGETIME(Timestamp CHANGETIME)
    {
        this.CHANGETIME = CHANGETIME;
        this.DATAMAP.put("CHANGETIME", CHANGETIME);
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
        if (this.KEYMAP.isEmpty() || this.LOTID == null || this.RELATIONTIMEKEY == null || this.SEQUENCE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"LOTID, RELATIONTIMEKEY, SEQUENCE"});
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