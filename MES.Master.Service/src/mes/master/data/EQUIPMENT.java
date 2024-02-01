package mes.master.data;

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
public class EQUIPMENT
{
    // Key Info
    private String PLANTID;
    private String EQUIPMENTID;

    // Data Info
    private String AREAID;
    private String COMMUNICATIONSTATE;
    private String EQUIPMENTSTATE;
    private String REASONCODETYPE;
    private String REASONCODE;
    private Long BATCHCOUNT;
    private String LOTID;
    private String WORKORDER;
    private String ALARMID;
    private String ALARMTIMEKEY;
    private String PMCYCLETYPE;
    private Long PMCYCLEVALUE;
    private Timestamp LASTMAINTENANCETIME;
    private String LASTMAINTENANCEUSERID;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;
    private String REFERENCETYPE;
    private String REFERENCEVALUE;
    private String CURRENTWORKORDER;
    private String CURRENTLOTID;
    private String CURRENTUSERID;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public EQUIPMENT() { }

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


    // Data Methods
    // AREAID
    public String getAREAID()
    {
        return AREAID;
    }
    public void setAREAID(String AREAID)
    {
        this.AREAID = AREAID;
        this.DATAMAP.put("AREAID", AREAID);
    }

    // COMMUNICATIONSTATE
    public String getCOMMUNICATIONSTATE()
    {
        return COMMUNICATIONSTATE;
    }
    public void setCOMMUNICATIONSTATE(String COMMUNICATIONSTATE)
    {
        this.COMMUNICATIONSTATE = COMMUNICATIONSTATE;
        this.DATAMAP.put("COMMUNICATIONSTATE", COMMUNICATIONSTATE);
    }

    // EQUIPMENTSTATE
    public String getEQUIPMENTSTATE()
    {
        return EQUIPMENTSTATE;
    }
    public void setEQUIPMENTSTATE(String EQUIPMENTSTATE)
    {
        this.EQUIPMENTSTATE = EQUIPMENTSTATE;
        this.DATAMAP.put("EQUIPMENTSTATE", EQUIPMENTSTATE);
    }

    // REASONCODETYPE
    public String getREASONCODETYPE()
    {
        return REASONCODETYPE;
    }
    public void setREASONCODETYPE(String REASONCODETYPE)
    {
        this.REASONCODETYPE = REASONCODETYPE;
        this.DATAMAP.put("REASONCODETYPE", REASONCODETYPE);
    }

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

    // BATCHCOUNT
    public Long getBATCHCOUNT()
    {
        return BATCHCOUNT;
    }
    public void setBATCHCOUNT(Long BATCHCOUNT)
    {
        this.BATCHCOUNT = BATCHCOUNT;
        this.DATAMAP.put("BATCHCOUNT", BATCHCOUNT);
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

    // ALARMID
    public String getALARMID()
    {
        return ALARMID;
    }
    public void setALARMID(String ALARMID)
    {
        this.ALARMID = ALARMID;
        this.DATAMAP.put("ALARMID", ALARMID);
    }

    // ALARMTIMEKEY
    public String getALARMTIMEKEY()
    {
        return ALARMTIMEKEY;
    }
    public void setALARMTIMEKEY(String ALARMTIMEKEY)
    {
        this.ALARMTIMEKEY = ALARMTIMEKEY;
        this.DATAMAP.put("ALARMTIMEKEY", ALARMTIMEKEY);
    }

    // PMCYCLETYPE
    public String getPMCYCLETYPE()
    {
        return PMCYCLETYPE;
    }
    public void setPMCYCLETYPE(String PMCYCLETYPE)
    {
        this.PMCYCLETYPE = PMCYCLETYPE;
        this.DATAMAP.put("PMCYCLETYPE", PMCYCLETYPE);
    }

    // PMCYCLEVALUE
    public Long getPMCYCLEVALUE()
    {
        return PMCYCLEVALUE;
    }
    public void setPMCYCLEVALUE(Long PMCYCLEVALUE)
    {
        this.PMCYCLEVALUE = PMCYCLEVALUE;
        this.DATAMAP.put("PMCYCLEVALUE", PMCYCLEVALUE);
    }

    // LASTMAINTENANCETIME
    public Timestamp getLASTMAINTENANCETIME()
    {
        return LASTMAINTENANCETIME;
    }
    public void setLASTMAINTENANCETIME(Timestamp LASTMAINTENANCETIME)
    {
        this.LASTMAINTENANCETIME = LASTMAINTENANCETIME;
        this.DATAMAP.put("LASTMAINTENANCETIME", LASTMAINTENANCETIME);
    }

    // LASTMAINTENANCEUSERID
    public String getLASTMAINTENANCEUSERID()
    {
        return LASTMAINTENANCEUSERID;
    }
    public void setLASTMAINTENANCEUSERID(String LASTMAINTENANCEUSERID)
    {
        this.LASTMAINTENANCEUSERID = LASTMAINTENANCEUSERID;
        this.DATAMAP.put("LASTMAINTENANCEUSERID", LASTMAINTENANCEUSERID);
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
    
    // CURRENTWORKORDER
    public String getCURRENTWORKORDER()
    {
    	return CURRENTWORKORDER;
    }
    public void setCURRENTWORKORDER(String CURRENTWORKORDER)
    {
    	this.CURRENTWORKORDER = CURRENTWORKORDER;
    	this.DATAMAP.put("CURRENTWORKORDER", CURRENTWORKORDER);
    }
    
    // CURRENTLOTID
    public String getCURRENTLOTID()
    {
    	return CURRENTLOTID;
    }
    public void setCURRENTLOTID(String CURRENTLOTID)
    {
    	this.CURRENTLOTID = CURRENTLOTID;
    	this.DATAMAP.put("CURRENTLOTID", CURRENTLOTID);
    }
    
    //CURRENTUSERID
    public String getCURRENTUSERID()
    {
    	return CURRENTUSERID;
    }
    public void setCURRENTUSERID(String CURRENTUSERID)
    {
    	this.CURRENTUSERID = CURRENTUSERID;
    	this.DATAMAP.put("CURRENTUSERID", CURRENTUSERID);
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
