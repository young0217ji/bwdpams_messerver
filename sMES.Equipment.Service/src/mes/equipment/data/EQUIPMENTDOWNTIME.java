package mes.equipment.data;

import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class EQUIPMENTDOWNTIME
{
    // Key Info
    private String PLANTID;
    private String LOTID;
    private String RELATIONTIMEKEY;
    private String PROCESSID;
    private Long PROCESSSEQUENCE;
    private String REASONCODE;

    // Data Info
    private Double DOWNTIME;
    private String EQUIPMENTID;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public EQUIPMENTDOWNTIME() { }

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

    // REASONCODE
    public String getKeyREASONCODE()
    {
        return REASONCODE;
    }
    public void setKeyREASONCODE(String REASONCODE)
    {
        this.REASONCODE = REASONCODE;
        this.KEYMAP.put("REASONCODE", REASONCODE);
    }


    // Data Methods
    // DOWNTIME
    public Double getDOWNTIME()
    {
        return DOWNTIME;
    }
    public void setDOWNTIME(Double DOWNTIME)
    {
        this.DOWNTIME = DOWNTIME;
        this.DATAMAP.put("DOWNTIME", DOWNTIME);
    }

    // EQUIPMENTID
    public String getEQUIPMENTID()
    {
        return EQUIPMENTID;
    }
    public void setEQUIPMENTID(String EQUIPMENTID)
    {
        this.EQUIPMENTID = EQUIPMENTID;
        this.DATAMAP.put("EQUIPMENTID", EQUIPMENTID);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.LOTID == null || this.RELATIONTIMEKEY == null || this.PROCESSID == null || this.PROCESSSEQUENCE == null || this.REASONCODE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, LOTID, RELATIONTIMEKEY, PROCESSID, PROCESSSEQUENCE, REASONCODE"});
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