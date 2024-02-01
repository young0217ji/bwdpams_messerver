package mes.master.data;

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
public class PORTDEFINITION
{
    // Key Info
    private String PLANTID;
    private String EQUIPMENTID;
    private String PORTID;

    // Data Info
    private String PORTTYPE;
    private String VENDOR;
    private String MODEL;
    private String SERIALNUMBER;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public PORTDEFINITION() { }

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

    // EQUIPMENTID
    public String getKeyEQUIPMENTID()
    {
        return EQUIPMENTID;
    }
    public void setKeyEQUIPMENTID(String EQUIPMENTID)
    {
        this.EQUIPMENTID = EQUIPMENTID;
        this.KEYMAP.put("EQUIPMENTID", EQUIPMENTID);
    }

    // PORTID
    public String getKeyPORTID()
    {
        return PORTID;
    }
    public void setKeyPORTID(String PORTID)
    {
        this.PORTID = PORTID;
        this.KEYMAP.put("PORTID", PORTID);
    }


    // Data Methods
    // PORTTYPE
    public String getPORTTYPE()
    {
        return PORTTYPE;
    }
    public void setPORTTYPE(String PORTTYPE)
    {
        this.PORTTYPE = PORTTYPE;
        this.DATAMAP.put("PORTTYPE", PORTTYPE);
    }

    // VENDOR
    public String getVENDOR()
    {
        return VENDOR;
    }
    public void setVENDOR(String VENDOR)
    {
        this.VENDOR = VENDOR;
        this.DATAMAP.put("VENDOR", VENDOR);
    }

    // MODEL
    public String getMODEL()
    {
        return MODEL;
    }
    public void setMODEL(String MODEL)
    {
        this.MODEL = MODEL;
        this.DATAMAP.put("MODEL", MODEL);
    }

    // SERIALNUMBER
    public String getSERIALNUMBER()
    {
        return SERIALNUMBER;
    }
    public void setSERIALNUMBER(String SERIALNUMBER)
    {
        this.SERIALNUMBER = SERIALNUMBER;
        this.DATAMAP.put("SERIALNUMBER", SERIALNUMBER);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.EQUIPMENTID == null || this.PORTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, EQUIPMENTID, PORTID"});
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
