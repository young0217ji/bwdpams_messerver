package mes.equipment.data;

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
public class EQUIPMENTPMSCHEDULE
{
    // Key Info
    private String PLANTID;
    private String PMSCHEDULEID;
    private String EQUIPMENTID;

    // Data Info
    private String PMSCHEDULETYPE;
    private Timestamp PMPLANNEDTIME;
    private Timestamp PMSTARTTIME;
    private Timestamp PMENDTIME;
    private String WORKERUSERID;
    private String MANAGERUSERID;
    private String WORKCOMMENT;
    private String WORKRESULT;
    private String REFERENCETYPE;
    private String REFERENCEVALUE;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public EQUIPMENTPMSCHEDULE() { }

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

    // PMSCHEDULEID
    public String getKeyPMSCHEDULEID()
    {
        return PMSCHEDULEID;
    }
    public void setKeyPMSCHEDULEID(String PMSCHEDULEID)
    {
        this.PMSCHEDULEID = PMSCHEDULEID;
        this.KEYMAP.put("PMSCHEDULEID", PMSCHEDULEID);
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

    // Data Methods
    // PMSCHEDULETYPE
    public String getPMSCHEDULETYPE()
    {
        return PMSCHEDULETYPE;
    }
    public void setPMSCHEDULETYPE(String PMSCHEDULETYPE)
    {
        this.PMSCHEDULETYPE = PMSCHEDULETYPE;
        this.DATAMAP.put("PMSCHEDULETYPE", PMSCHEDULETYPE);
    }
    
    // PMPLANNEDTIME
    public Timestamp getPMPLANNEDTIME()
    {
        return PMPLANNEDTIME;
    }
    public void setPMPLANNEDTIME(Timestamp PMPLANNEDTIME)
    {
        this.PMPLANNEDTIME = PMPLANNEDTIME;
        this.DATAMAP.put("PMPLANNEDTIME", PMPLANNEDTIME);
    }
    
    // PMSTARTTIME
    public Timestamp getPMSTARTTIME()
    {
        return PMSTARTTIME;
    }
    public void setPMSTARTTIME(Timestamp PMSTARTTIME)
    {
        this.PMSTARTTIME = PMSTARTTIME;
        this.DATAMAP.put("PMSTARTTIME", PMSTARTTIME);
    }
    
    // PMENDTIME
    public Timestamp getPMENDTIME()
    {
        return PMENDTIME;
    }
    public void setPMENDTIME(Timestamp PMENDTIME)
    {
        this.PMENDTIME = PMENDTIME;
        this.DATAMAP.put("PMENDTIME", PMENDTIME);
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
    
    // MANAGERUSERID
    public String getMANAGERUSERID()
    {
        return MANAGERUSERID;
    }
    public void setMANAGERUSERID(String MANAGERUSERID)
    {
        this.MANAGERUSERID = MANAGERUSERID;
        this.DATAMAP.put("MANAGERUSERID", MANAGERUSERID);
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
    
    // WORKRESULT
    public String getWORKRESULT()
    {
        return WORKRESULT;
    }
    public void setWORKRESULT(String WORKRESULT)
    {
        this.WORKRESULT = WORKRESULT;
        this.DATAMAP.put("WORKRESULT", WORKRESULT);
    }
    
    // REFERENCETYPE
    public String getREFERENCETYPE()
    {
        return REFERENCETYPE;
    }
    public void setREFERENCETYPE(String REFERENCETYPE)
    {
        this.REFERENCETYPE = REFERENCETYPE;
        this.DATAMAP.put("REFERENCETYPE", REFERENCETYPE);
    }
    
    // REFERENCEVALUE
    public String getREFERENCEVALUE()
    {
        return REFERENCEVALUE;
    }
    public void setREFERENCEVALUE(String REFERENCEVALUE)
    {
        this.REFERENCEVALUE = REFERENCEVALUE;
        this.DATAMAP.put("REFERENCEVALUE", REFERENCEVALUE);
    }

    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PMSCHEDULEID == null || this.EQUIPMENTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PMSCHEDULEID, EQUIPMENTID"});
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
