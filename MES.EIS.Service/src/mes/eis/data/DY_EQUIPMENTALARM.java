package mes.eis.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class DY_EQUIPMENTALARM
{
    // Key Info
	private String PLANTID;
	private String DATAKEY;

    // Data Info
    private String EQUIPMENTID;
    private String ALARMID;
    private String ALARMCOMMENT;
    private Timestamp EVENTTIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_EQUIPMENTALARM() { }

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
    
    // DATAKEY
    public String getKeyDATAKEY()
    {
        return DATAKEY;
    }
    public void setKeyDATAKEY(String DATAKEY)
    {
        this.DATAKEY = DATAKEY;
        this.KEYMAP.put("DATAKEY", DATAKEY);
    }
    
    // Data Methods
    // EQUIPMENTID
    public String getEQUIPMENTID()
    {
        return EQUIPMENTID;
    }
    public void setEQUIPMENTID(String EQUIPMENTID)
    {
        this.EQUIPMENTID = EQUIPMENTID;
        this.KEYMAP.put("EQUIPMENTID", EQUIPMENTID);
    }

    // ALARMID
    public String getALARMID()
    {
        return ALARMID;
    }
    public void setALARMID(String ALARMID)
    {
        this.ALARMID = ALARMID;
        this.KEYMAP.put("ALARMID", ALARMID);
    }    

    // ALARMCOMMENT
    public String getALARMCOMMENT()
    {
        return ALARMCOMMENT;
    }
    public void setALARMCOMMENT(String ALARMCOMMENT)
    {
        this.ALARMCOMMENT = ALARMCOMMENT;
        this.KEYMAP.put("ALARMCOMMENT", ALARMCOMMENT);
    }

   
    // EVENTTIME
    public Timestamp getEVENTTIME()
    {
        return EVENTTIME;
    }
    public void setEVENTTIME(Timestamp EVENTTIME)
    {
        this.EVENTTIME = EVENTTIME;
        this.DATAMAP.put("EVENTTIME", EVENTTIME);
    }

    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.EQUIPMENTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, DATAKEY"});
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