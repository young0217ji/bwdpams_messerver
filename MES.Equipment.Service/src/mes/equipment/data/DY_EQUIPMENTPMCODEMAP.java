package mes.equipment.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class DY_EQUIPMENTPMCODEMAP
{
    // Key Info
    private String PLANTID;
    private String EQUIPMENTID;
    private String PMCODEID;

    // Data Info
    private Timestamp PMPLANSTARTTIME;
    private Timestamp PMPLANENDTIME;
    private String MANAGERUSERID;
    private String WORKERUSERID;
    private String SCHEDULETYPE;
    private String SCHEDULETVALUE;
    private String DESCRIPTION;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_EQUIPMENTPMCODEMAP() { }

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

    // PMCODEID
    public String getKeyPMCODEID()
    {
        return PMCODEID;
    }
    public void setKeyPMCODEID(String PMCODEID)
    {
        this.PMCODEID = PMCODEID;
        this.KEYMAP.put("PMCODEID", PMCODEID);
    }


    // Data Methods
    // PMPLANSTARTTIME
    public Timestamp getPMPLANSTARTTIME()
    {
        return PMPLANSTARTTIME;
    }
    public void setPMPLANSTARTTIME(Timestamp PMPLANSTARTTIME)
    {
        this.PMPLANSTARTTIME = PMPLANSTARTTIME;
        this.DATAMAP.put("PMPLANSTARTTIME", PMPLANSTARTTIME);
    }
    
    // PMPLANENDTIME
    public Timestamp getPMPLANENDTIME()
    {
        return PMPLANENDTIME;
    }
    public void setPMPLANENDTIME(Timestamp PMPLANENDTIME)
    {
        this.PMPLANENDTIME = PMPLANENDTIME;
        this.DATAMAP.put("PMPLANENDTIME", PMPLANENDTIME);
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
    
    // SCHEDULETYPE
    public String getSCHEDULETYPE()
    {
        return SCHEDULETYPE;
    }
    public void setSCHEDULETYPE(String SCHEDULETYPE)
    {
        this.SCHEDULETYPE = SCHEDULETYPE;
        this.DATAMAP.put("SCHEDULETYPE", SCHEDULETYPE);
    }
    
    // SCHEDULETVALUE
    public String getSCHEDULETVALUE()
    {
        return SCHEDULETVALUE;
    }
    public void setSCHEDULETVALUE(String SCHEDULETVALUE)
    {
        this.SCHEDULETVALUE = SCHEDULETVALUE;
        this.DATAMAP.put("SCHEDULETVALUE", SCHEDULETVALUE);
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

    // CREATEUSERID
    public String  getCREATEUSERID()
    {
        return CREATEUSERID;
    }
    public void setCREATEUSERID(String  CREATEUSERID)
    {
        this.CREATEUSERID = CREATEUSERID;
        this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.EQUIPMENTID == null || this.PMCODEID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, EQUIPMENTID, PMCODEID"});
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