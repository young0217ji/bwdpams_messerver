package mes.eis.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class DY_EISMESSAGE_LOG
{
    // Key Info
	private int MESSAGEID;

    // Data Info
    private String PLANTID;
    private String MESSAGENAME;
    private String EQUIPMENTID;
    private String SCANID;
    private String ENDSCANID;
    private String STATE;
    private String ARARMID;
    private String ARARMCOMMENT;
    private String TAGID;
    private String TAGVALUE;
    private String EVENTTIME;
    
    private String RESERVEFIELD1;
    private String RESERVEFIELD2;
    private String RESERVEFIELD3;
    private String RESERVEFIELD4;
    private String RESERVEFIELD5;
    
    private String EISEVENTRESULT;
    private String EISEVENTCOMMENT;
    private String EISEVENTUSER;
    private Timestamp EISEVENTTIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_EISMESSAGE_LOG() { }

    // Key Methods
    // MESSAGEID
    public int getKeyMESSAGEID()
    {
        return MESSAGEID;
    }
    public void setKeyMESSAGEID(int MESSAGEID)
    {
        this.MESSAGEID = MESSAGEID;
        this.KEYMAP.put("MESSAGEID", MESSAGEID);
    }
    
    // Data Methods
    
    // PLANTID
    public String getPLANTID()
    {
        return PLANTID;
    }
    public void setPLANTID(String PLANTID)
    {
        this.PLANTID = PLANTID;
        this.KEYMAP.put("PLANTID", PLANTID);
    }

    // MESSAGENAME
    public String getMESSAGENAME()
    {
        return MESSAGENAME;
    }
    public void setMESSAGENAME(String MESSAGENAME)
    {
        this.MESSAGENAME = MESSAGENAME;
        this.KEYMAP.put("MESSAGENAME", MESSAGENAME);
    }

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

    // SCANID
    public String getSCANID()
    {
        return SCANID;
    }
    public void setSCANID(String SCANID)
    {
        this.SCANID = SCANID;
        this.KEYMAP.put("SCANID", SCANID);
    }    

    // ENDSCANID
    public String getENDSCANID()
    {
        return ENDSCANID;
    }
    public void setENDSCANID(String ENDSCANID)
    {
        this.ENDSCANID = ENDSCANID;
        this.KEYMAP.put("ENDSCANID", ENDSCANID);
    }

    // STATE
    public String getSTATE()
    {
        return STATE;
    }
    public void setSTATE(String STATE)
    {
        this.STATE = STATE;
        this.KEYMAP.put("STATE", STATE);
    }

    // ARARMID
    public String getARARMID()
    {
        return ARARMID;
    }
    public void setARARMID(String ARARMID)
    {
        this.ARARMID = ARARMID;
        this.KEYMAP.put("ARARMID", ARARMID);
    }

    // ARARMCOMMENT
    public String getARARMCOMMENT()
    {
        return ARARMCOMMENT;
    }
    public void setARARMCOMMENT(String ARARMCOMMENT)
    {
        this.ARARMCOMMENT = ARARMCOMMENT;
        this.KEYMAP.put("ARARMCOMMENT", ARARMCOMMENT);
    }

    // TAGID
    public String getTAGID()
    {
        return TAGID;
    }
    public void setTAGID(String TAGID)
    {
        this.TAGID = TAGID;
        this.KEYMAP.put("TAGID", TAGID);
    }

    // TAGVALUE
    public String getTAGVALUE()
    {
        return TAGVALUE;
    }
    public void setTAGVALUE(String TAGVALUE)
    {
        this.TAGVALUE = TAGVALUE;
        this.KEYMAP.put("TAGVALUE", TAGVALUE);
    }

    // EVENTTIME
    public String getEVENTTIME()
    {
        return EVENTTIME;
    }
    public void setEVENTTIME(String EVENTTIME)
    {
        this.EVENTTIME = EVENTTIME;
        this.KEYMAP.put("EVENTTIME", EVENTTIME);
    }
    
    // RESERVEFIELD1
    public String getRESERVEFIELD1()
    {
        return RESERVEFIELD1;
    }
    public void setRESERVEFIELD1(String RESERVEFIELD1)
    {
        this.RESERVEFIELD1 = RESERVEFIELD1;
        this.KEYMAP.put("RESERVEFIELD1", RESERVEFIELD1);
    }

    // RESERVEFIELD2
    public String getRESERVEFIELD2()
    {
        return RESERVEFIELD2;
    }
    public void setRESERVEFIELD2(String RESERVEFIELD2)
    {
        this.RESERVEFIELD2 = RESERVEFIELD2;
        this.KEYMAP.put("RESERVEFIELD2", RESERVEFIELD2);
    }

    // RESERVEFIELD3
    public String getRESERVEFIELD3()
    {
        return RESERVEFIELD3;
    }
    public void setRESERVEFIELD3(String RESERVEFIELD3)
    {
        this.RESERVEFIELD3 = RESERVEFIELD3;
        this.KEYMAP.put("RESERVEFIELD3", RESERVEFIELD3);
    }

    // RESERVEFIELD4
    public String getRESERVEFIELD4()
    {
        return RESERVEFIELD4;
    }
    public void setRESERVEFIELD4(String RESERVEFIELD4)
    {
        this.RESERVEFIELD4 = RESERVEFIELD4;
        this.KEYMAP.put("RESERVEFIELD4", RESERVEFIELD4);
    }

    // RESERVEFIELD5
    public String getRESERVEFIELD5()
    {
        return RESERVEFIELD5;
    }
    public void setRESERVEFIELD5(String RESERVEFIELD5)
    {
        this.RESERVEFIELD5 = RESERVEFIELD5;
        this.KEYMAP.put("RESERVEFIELD5", RESERVEFIELD5);
    }

    // EISEVENTRESULT
    public String getEISEVENTRESULT()
    {
        return EISEVENTRESULT;
    }
    public void setEISEVENTRESULT(String EISEVENTRESULT)
    {
        this.EISEVENTRESULT = EISEVENTRESULT;
        this.KEYMAP.put("EISEVENTRESULT", EISEVENTRESULT);
    }

    // EISEVENTCOMMENT
    public String getEISEVENTCOMMENT()
    {
        return EISEVENTCOMMENT;
    }
    public void setEISEVENTCOMMENT(String EISEVENTCOMMENT)
    {
        this.EISEVENTCOMMENT = EISEVENTCOMMENT;
        this.KEYMAP.put("EISEVENTCOMMENT", EISEVENTCOMMENT);
    }

    // EISEVENTUSER
    public String getEISEVENTUSER()
    {
        return EISEVENTUSER;
    }
    public void setEISEVENTUSER(String EISEVENTUSER)
    {
        this.EISEVENTUSER = EISEVENTUSER;
        this.KEYMAP.put("EISEVENTUSER", EISEVENTUSER);
    }

    // EISEVENTTIME
    public Timestamp getEISEVENTTIME()
    {
        return EISEVENTTIME;
    }
    public void setEISEVENTTIME(Timestamp EISEVENTTIME)
    {
        this.EISEVENTTIME = EISEVENTTIME;
        this.DATAMAP.put("EISEVENTTIME", EISEVENTTIME);
    }

    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.EQUIPMENTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, EQUIPMENTID"});
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