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
public class TGPOLICY
{
    // Key Info
    private String PLANTID;
    private String PRODUCTGROUPID;
    private String POLICYNAME;
    private String POLICYVALUE;
    private String CONDITIONTYPE;
    private String CONDITIONID;

    // Data Info
    private String PROCESSROUTEID;
    private String PROCESSROUTETYPE;
    private String DESCRIPTION;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public TGPOLICY() { }

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

    // PRODUCTGROUPID
    public String getKeyPRODUCTGROUPID()
    {
        return PRODUCTGROUPID;
    }
    public void setKeyPRODUCTGROUPID(String PRODUCTGROUPID)
    {
        this.PRODUCTGROUPID = PRODUCTGROUPID;
        this.KEYMAP.put("PRODUCTGROUPID", PRODUCTGROUPID);
    }

    // POLICYNAME
    public String getKeyPOLICYNAME()
    {
        return POLICYNAME;
    }
    public void setKeyPOLICYNAME(String POLICYNAME)
    {
        this.POLICYNAME = POLICYNAME;
        this.KEYMAP.put("POLICYNAME", POLICYNAME);
    }

    // POLICYVALUE
    public String getKeyPOLICYVALUE()
    {
        return POLICYVALUE;
    }
    public void setKeyPOLICYVALUE(String POLICYVALUE)
    {
        this.POLICYVALUE = POLICYVALUE;
        this.KEYMAP.put("POLICYVALUE", POLICYVALUE);
    }

    // CONDITIONTYPE
    public String getKeyCONDITIONTYPE()
    {
        return CONDITIONTYPE;
    }
    public void setKeyCONDITIONTYPE(String CONDITIONTYPE)
    {
        this.CONDITIONTYPE = CONDITIONTYPE;
        this.KEYMAP.put("CONDITIONTYPE", CONDITIONTYPE);
    }

    // CONDITIONID
    public String getKeyCONDITIONID()
    {
        return CONDITIONID;
    }
    public void setKeyCONDITIONID(String CONDITIONID)
    {
        this.CONDITIONID = CONDITIONID;
        this.KEYMAP.put("CONDITIONID", CONDITIONID);
    }


    // Data Methods
    // PROCESSROUTEID
    public String getPROCESSROUTEID()
    {
        return PROCESSROUTEID;
    }
    public void setPROCESSROUTEID(String PROCESSROUTEID)
    {
        this.PROCESSROUTEID = PROCESSROUTEID;
        this.DATAMAP.put("PROCESSROUTEID", PROCESSROUTEID);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTGROUPID == null || this.POLICYNAME == null || this.POLICYVALUE == null || this.CONDITIONTYPE == null || this.CONDITIONID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTGROUPID, POLICYNAME, POLICYVALUE, CONDITIONTYPE, CONDITIONID"});
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
