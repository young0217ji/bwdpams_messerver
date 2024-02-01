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
public class MENUDEFINITION
{
    // Key Info
    private String PLANTID;
    private String MENUID;

    // Data Info
    private String MENUGROUPID;
    private String MENUNAME;
    private String ACTIVESTATE;
    private String CLASSNAME;
    private String MENUTYPE;
    private String SOURCETYPE;
    private Long POSITION;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public MENUDEFINITION() { }

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

    // MENUID
    public String getKeyMENUID()
    {
        return MENUID;
    }
    public void setKeyMENUID(String MENUID)
    {
        this.MENUID = MENUID;
        this.KEYMAP.put("MENUID", MENUID);
    }


    // Data Methods
    // MENUGROUPID
    public String getMENUGROUPID()
    {
        return MENUGROUPID;
    }
    public void setMENUGROUPID(String MENUGROUPID)
    {
        this.MENUGROUPID = MENUGROUPID;
        this.DATAMAP.put("MENUGROUPID", MENUGROUPID);
    }

    // MENUNAME
    public String getMENUNAME()
    {
        return MENUNAME;
    }
    public void setMENUNAME(String MENUNAME)
    {
        this.MENUNAME = MENUNAME;
        this.DATAMAP.put("MENUNAME", MENUNAME);
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

    // CLASSNAME
    public String getCLASSNAME()
    {
        return CLASSNAME;
    }
    public void setCLASSNAME(String CLASSNAME)
    {
        this.CLASSNAME = CLASSNAME;
        this.DATAMAP.put("CLASSNAME", CLASSNAME);
    }

    // MENUTYPE
    public String getMENUTYPE()
    {
        return MENUTYPE;
    }
    public void setMENUTYPE(String MENUTYPE)
    {
        this.MENUTYPE = MENUTYPE;
        this.DATAMAP.put("MENUTYPE", MENUTYPE);
    }

    // SOURCETYPE
    public String getSOURCETYPE()
    {
        return SOURCETYPE;
    }
    public void setSOURCETYPE(String SOURCETYPE)
    {
        this.SOURCETYPE = SOURCETYPE;
        this.DATAMAP.put("SOURCETYPE", SOURCETYPE);
    }

    // POSITION
    public Long getPOSITION()
    {
        return POSITION;
    }
    public void setPOSITION(Long POSITION)
    {
        this.POSITION = POSITION;
        this.DATAMAP.put("POSITION", POSITION);
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


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.MENUID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, MENUID"});
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
