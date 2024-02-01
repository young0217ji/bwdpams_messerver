package mes.equipment.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class DY_EQUIPMENTMAINTENANCE
{
    // Key Info
    private String PLANTID;
    private String EQUIPMENTID;
    private String REQUESTID;

    // Data Info
    private String REASONCODE;
    private String REASONCODENAME;
    private String REQUESTDETAIL;
    private String REQUESTUSERID;
    private Timestamp REQUESTTIME;
    private String STATUS;
    private String WORKERUSERID;
    private Timestamp WORKTIME;
    private String WORKCOMMENT;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_EQUIPMENTMAINTENANCE() { }

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

    // REQUESTID
    public String getKeyREQUESTID()
    {
        return REQUESTID;
    }
    public void setKeyREQUESTID(String REQUESTID)
    {
        this.REQUESTID = REQUESTID;
        this.KEYMAP.put("REQUESTID", REQUESTID);
    }


    // Data Methods
    // REASONCODE
    public String getREASONCODE()
    {
        return REASONCODE;
    }
    public void setREASONCODE(String REASONCODE)
    {
        this.REASONCODE = REASONCODE;
        this.DATAMAP.put("REASONCODE", REASONCODE);
    }
    
    // REASONCODENAME
    public String getREASONCODENAME()
    {
        return REASONCODENAME;
    }
    public void setREASONCODENAME(String REASONCODENAME)
    {
        this.REASONCODENAME = REASONCODENAME;
        this.DATAMAP.put("REASONCODENAME", REASONCODENAME);
    }
    
    // REQUESTDETAIL
    public String getREQUESTDETAIL()
    {
        return REQUESTDETAIL;
    }
    public void setREQUESTDETAIL(String REQUESTDETAIL)
    {
        this.REQUESTDETAIL = REQUESTDETAIL;
        this.DATAMAP.put("REQUESTDETAIL", REQUESTDETAIL);
    }
    
    // REQUESTUSERID
    public String getREQUESTUSERID()
    {
        return REQUESTUSERID;
    }
    public void setREQUESTUSERID(String REQUESTUSERID)
    {
        this.REQUESTUSERID = REQUESTUSERID;
        this.DATAMAP.put("REQUESTUSERID", REQUESTUSERID);
    }
    
    // REQUESTTIME
    public Timestamp getREQUESTTIME()
    {
        return REQUESTTIME;
    }
    public void setREQUESTTIME(Timestamp REQUESTTIME)
    {
        this.REQUESTTIME = REQUESTTIME;
        this.DATAMAP.put("REQUESTTIME", REQUESTTIME);
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
    
    // WORKERUSERID
    public String getWORKERUSERID()
    {
        return WORKERUSERID;
    }
    public void setWORKERUSERID(String WORKERUSERID)
    {
        this.WORKERUSERID = WORKERUSERID;
        this.DATAMAP.put("WORKERUSERID", WORKERUSERID);
    }

    // WORKTIME
    public Timestamp getWORKTIME()
    {
        return WORKTIME;
    }
    public void setWORKTIME(Timestamp WORKTIME)
    {
        this.WORKTIME = WORKTIME;
        this.DATAMAP.put("WORKTIME", WORKTIME);
    }
    
    // WORKCOMMENT
    public String getWORKCOMMENT()
    {
        return WORKCOMMENT;
    }
    public void setWORKCOMMENT(String WORKCOMMENT)
    {
        this.WORKCOMMENT = WORKCOMMENT;
        this.DATAMAP.put("WORKCOMMENT", WORKCOMMENT);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.EQUIPMENTID == null || this.REQUESTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, EQUIPMENTID, REQUESTID"});
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