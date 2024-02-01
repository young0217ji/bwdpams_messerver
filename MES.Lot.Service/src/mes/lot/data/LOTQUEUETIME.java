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
public class LOTQUEUETIME
{
    // Key Info
    private String LOTID;
    private String RELATIONTIMEKEY;
    private Long SEQUENCE;

    // Data Info
    private String PROCESSID;
    private String PROCESSNAME;
    private Long PROCESSSEQUENCE;
    private String RECIPEID;
    private String RECIPENAME;
    private Long RECIPESEQUENCE;
    private String EQUIPMENTID;
    private String TARGET;
    private String SPECTYPE;
    private String EVENTTYPE;
    private Timestamp STARTTIME;
    private Timestamp ENDTIME;
    private String STATUS;
    private Timestamp ALARMTIME;
    private String ALARMSTATUS;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public LOTQUEUETIME() { }

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
    public Long getKeySEQUENCE()
    {
        return SEQUENCE;
    }
    public void setKeySEQUENCE(Long SEQUENCE)
    {
        this.SEQUENCE = SEQUENCE;
        this.KEYMAP.put("SEQUENCE", SEQUENCE);
    }


    // Data Methods
    // PROCESSID
    public String getPROCESSID()
    {
        return PROCESSID;
    }
    public void setPROCESSID(String PROCESSID)
    {
        this.PROCESSID = PROCESSID;
        this.DATAMAP.put("PROCESSID", PROCESSID);
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

    // PROCESSSEQUENCE
    public Long getPROCESSSEQUENCE()
    {
        return PROCESSSEQUENCE;
    }
    public void setPROCESSSEQUENCE(Long PROCESSSEQUENCE)
    {
        this.PROCESSSEQUENCE = PROCESSSEQUENCE;
        this.DATAMAP.put("PROCESSSEQUENCE", PROCESSSEQUENCE);
    }

    // RECIPEID
    public String getRECIPEID()
    {
        return RECIPEID;
    }
    public void setRECIPEID(String RECIPEID)
    {
        this.RECIPEID = RECIPEID;
        this.DATAMAP.put("RECIPEID", RECIPEID);
    }

    // RECIPENAME
    public String getRECIPENAME()
    {
        return RECIPENAME;
    }
    public void setRECIPENAME(String RECIPENAME)
    {
        this.RECIPENAME = RECIPENAME;
        this.DATAMAP.put("RECIPENAME", RECIPENAME);
    }

    // RECIPESEQUENCE
    public Long getRECIPESEQUENCE()
    {
        return RECIPESEQUENCE;
    }
    public void setRECIPESEQUENCE(Long RECIPESEQUENCE)
    {
        this.RECIPESEQUENCE = RECIPESEQUENCE;
        this.DATAMAP.put("RECIPESEQUENCE", RECIPESEQUENCE);
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

    // TARGET
    public String getTARGET()
    {
        return TARGET;
    }
    public void setTARGET(String TARGET)
    {
        this.TARGET = TARGET;
        this.DATAMAP.put("TARGET", TARGET);
    }

    // SPECTYPE
    public String getSPECTYPE()
    {
        return SPECTYPE;
    }
    public void setSPECTYPE(String SPECTYPE)
    {
        this.SPECTYPE = SPECTYPE;
        this.DATAMAP.put("SPECTYPE", SPECTYPE);
    }

    // EVENTTYPE
    public String getEVENTTYPE()
    {
        return EVENTTYPE;
    }
    public void setEVENTTYPE(String EVENTTYPE)
    {
        this.EVENTTYPE = EVENTTYPE;
        this.DATAMAP.put("EVENTTYPE", EVENTTYPE);
    }

    // STARTTIME
    public Timestamp getSTARTTIME()
    {
        return STARTTIME;
    }
    public void setSTARTTIME(Timestamp STARTTIME)
    {
        this.STARTTIME = STARTTIME;
        this.DATAMAP.put("STARTTIME", STARTTIME);
    }

    // ENDTIME
    public Timestamp getENDTIME()
    {
        return ENDTIME;
    }
    public void setENDTIME(Timestamp ENDTIME)
    {
        this.ENDTIME = ENDTIME;
        this.DATAMAP.put("ENDTIME", ENDTIME);
    }

    // STATUS
    public String getSTATUS()
    {
        return STATUS;
    }
    public void setSTATUS(String STATUS)
    {
        this.STATUS = STATUS;
        this.DATAMAP.put("STATUS", STATUS);
    }

    // ALARMTIME
    public Timestamp getALARMTIME()
    {
        return ALARMTIME;
    }
    public void setALARMTIME(Timestamp ALARMTIME)
    {
        this.ALARMTIME = ALARMTIME;
        this.DATAMAP.put("ALARMTIME", ALARMTIME);
    }

    // ALARMSTATUS
    public String getALARMSTATUS()
    {
        return ALARMSTATUS;
    }
    public void setALARMSTATUS(String ALARMSTATUS)
    {
        this.ALARMSTATUS = ALARMSTATUS;
        this.DATAMAP.put("ALARMSTATUS", ALARMSTATUS);
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
