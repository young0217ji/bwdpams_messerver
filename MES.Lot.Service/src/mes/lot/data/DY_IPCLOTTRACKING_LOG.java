package mes.lot.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class DY_IPCLOTTRACKING_LOG
{
    // Key Info
    private String PLANTID;
    private String USERID;
    private String SCANID;
    private String TIMEKEY;

    // Data Info
    private String PRODUCTID;
    private String WORKORDER;
    private String LOTID;
    private String PROCESSID;
    private String PROCESSSEQUENCE;
    private String EQUIPMENTID;
    private String ACTIONTYPE;
    private String EVENTNAME;
    private String EVENTRESULT;
    private String EVENTCOMMENT;
    private String LOCALPCNAME;
    private String LOCALIP;
    private Timestamp EVENTTIME;
    private String LOTTRACKINGID;
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_IPCLOTTRACKING_LOG() { }

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

    // USERID
    public String getKeyUSERID()
    {
        return USERID;
    }
    public void setKeyUSERID(String USERID)
    {
        this.USERID = USERID;
        this.KEYMAP.put("USERID", USERID);
    }

    // SCANID
    public String getKeySCANID()
    {
        return SCANID;
    }
    public void setKeySCANID(String SCANID)
    {
        this.SCANID = SCANID;
        this.KEYMAP.put("SCANID", SCANID);
    }

    // TIMEKEY
    public String getKeyTIMEKEY()
    {
        return TIMEKEY;
    }
    public void setKeyTIMEKEY(String TIMEKEY)
    {
        this.TIMEKEY = TIMEKEY;
        this.KEYMAP.put("TIMEKEY", TIMEKEY);
    }

    // Data Methods

    // PRODUCTID
    public String getPRODUCTID()
    {
        return PRODUCTID;
    }
    public void setPRODUCTID(String PRODUCTID)
    {
        this.PRODUCTID = PRODUCTID;
        this.DATAMAP.put("PRODUCTID", PRODUCTID);
    }
    
    // WORKORDER
    public String getWORKORDER()
    {
        return WORKORDER;
    }
    public void setWORKORDER(String WORKORDER)
    {
        this.WORKORDER = WORKORDER;
        this.DATAMAP.put("WORKORDER", WORKORDER);
    }
    
    // LOTID
    public String getLOTID()
    {
        return LOTID;
    }
    public void setLOTID(String LOTID)
    {
        this.LOTID = LOTID;
        this.DATAMAP.put("LOTID", LOTID);
    }

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
    
    // PROCESSSEQUENCE
    public String getPROCESSSEQUENCE()
    {
        return PROCESSSEQUENCE;
    }
    public void setPROCESSSEQUENCE(String PROCESSSEQUENCE)
    {
        this.PROCESSSEQUENCE = PROCESSSEQUENCE;
        this.DATAMAP.put("PROCESSSEQUENCE", PROCESSSEQUENCE);
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
    
    // EVENTNAME
    public String getEVENTNAME()
    {
        return EVENTNAME;
    }
    public void setEVENTNAME(String EVENTNAME)
    {
        this.EVENTNAME = EVENTNAME;
        this.DATAMAP.put("EVENTNAME", EVENTNAME);
    }
    
    // EVENTRESULT
    public String getEVENTRESULT()
    {
        return EVENTRESULT;
    }
    public void setEVENTRESULT(String EVENTRESULT)
    {
        this.EVENTRESULT = EVENTRESULT;
        this.DATAMAP.put("EVENTRESULT", EVENTRESULT);
    }
    
    // EVENTCOMMENT
    public String getEVENTCOMMENT()
    {
        return EVENTCOMMENT;
    }
    public void setEVENTCOMMENT(String EVENTCOMMENT)
    {
        this.EVENTCOMMENT = EVENTCOMMENT;
        this.DATAMAP.put("EVENTCOMMENT", EVENTCOMMENT);
    }
    
    // LOCALPCNAME
    public String getLOCALPCNAME()
    {
        return LOCALPCNAME;
    }
    public void setLOCALPCNAME(String LOCALPCNAME)
    {
        this.LOCALPCNAME = LOCALPCNAME;
        this.DATAMAP.put("LOCALPCNAME", LOCALPCNAME);
    }
    
    // LOCALIP
    public String getLOCALIP()
    {
        return LOCALIP;
    }
    public void setLOCALIP(String LOCALIP)
    {
        this.LOCALIP = LOCALIP;
        this.DATAMAP.put("LOCALIP", LOCALIP);
    }
    
    // LOTTRACKINGID
    public String getLOTTRACKINGID()
    {
        return LOTTRACKINGID;
    }
    public void setLOTTRACKINGID(String LOTTRACKINGID)
    {
        this.LOTTRACKINGID = LOTTRACKINGID;
        this.DATAMAP.put("LOTTRACKINGID", LOTTRACKINGID);
    }

    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.USERID == null || this.SCANID == null || this.TIMEKEY == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, USERID, SCANID, TIMEKEY"});
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