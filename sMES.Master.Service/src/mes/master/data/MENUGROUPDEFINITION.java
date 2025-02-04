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
public class MENUGROUPDEFINITION
{
    // Key Info
    private String PLANTID;
    private String MENUGROUPID;

    // Data Info
    private String MENUGROUPNAME;
    private String MENUGROUPTYPE;
    private Long POSITION;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public MENUGROUPDEFINITION() { }

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

    // MENUGROUPID
    public String getKeyMENUGROUPID()
    {
        return MENUGROUPID;
    }
    public void setKeyMENUGROUPID(String MENUGROUPID)
    {
        this.MENUGROUPID = MENUGROUPID;
        this.KEYMAP.put("MENUGROUPID", MENUGROUPID);
    }


    // Data Methods
    // MENUGROUPNAME
    public String getMENUGROUPNAME()
    {
        return MENUGROUPNAME;
    }
    public void setMENUGROUPNAME(String MENUGROUPNAME)
    {
        this.MENUGROUPNAME = MENUGROUPNAME;
        this.DATAMAP.put("MENUGROUPNAME", MENUGROUPNAME);
    }

    // MENUGROUPTYPE
    public String getMENUGROUPTYPE()
    {
        return MENUGROUPTYPE;
    }
    public void setMENUGROUPTYPE(String MENUGROUPTYPE)
    {
        this.MENUGROUPTYPE = MENUGROUPTYPE;
        this.DATAMAP.put("MENUGROUPTYPE", MENUGROUPTYPE);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.MENUGROUPID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, MENUGROUPID"});
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
