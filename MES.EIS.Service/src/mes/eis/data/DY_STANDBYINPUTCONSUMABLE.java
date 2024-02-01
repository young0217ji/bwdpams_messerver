package mes.eis.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class DY_STANDBYINPUTCONSUMABLE
{
    // Key Info
    private String PLANTID;
    private String EQUIPMENTID;
    private String CONSUMABLEID;

    // Data Info
    private String EISEVENTTIME;
    private String CREATEUSERID;
    private Timestamp CREATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_STANDBYINPUTCONSUMABLE() { }

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

    // CONSUMABLEID
    public String getKeyCONSUMABLEID()
    {
        return CONSUMABLEID;
    }
    public void setKeyCONSUMABLEID(String CONSUMABLEID)
    {
        this.CONSUMABLEID = CONSUMABLEID;
        this.DATAMAP.put("CONSUMABLEID", CONSUMABLEID);
    }


    // Data Methods

    // EISEVENTTIME
    public String getEISEVENTTIME()
    {
        return EISEVENTTIME;
    }
    public void setEISEVENTTIME(String EISEVENTTIME)
    {
        this.EISEVENTTIME = EISEVENTTIME;
        this.DATAMAP.put("EISEVENTTIME", EISEVENTTIME);
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


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.EQUIPMENTID == null || this.CONSUMABLEID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, EQUIPMENTID, CONSUMABLEID"});
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