package mes.lot.data;

import java.sql.Timestamp;
import java.util.HashMap;

import mes.userdefine.data.LOTINFORMATION_UDF;
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
public class LOTINFORMATION extends LOTINFORMATION_UDF
{
    // Key Info
    private String LOTID;

    // Data Info
    private String PRODUCTID;
    private String PRODUCTTYPE;
    private String REPROUTEID;
    private String PROCESSROUTEID;
    private String PROCESSROUTETYPE;
    private String BOMID;
    private String BOMVERSION;
    private String POLICYNAME;
    private String POLICYVALUE;
    private String CONDITIONTYPE;
    private String CONDITIONID;
    private String CONDITIONVALUE;
    private String LOTTYPE;
    private String WORKORDER;
    private String ROOTLOTID;
    private String SOURCELOTID;
    private String PRODUCTUNITQUANTITY;
    private Double CURRENTQUANTITY;
    private Timestamp STARTDUEDATE;
    private Timestamp ENDDUEDATE;
    private String PRIORITY;
    private String PLANTID;
    private String AREAID;
    private String DEPARTMENT;
    private String WORKCENTER;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp RELEASETIME;
    private String RELEASEUSERID;
    private Timestamp COMPLETETIME;
    private String COMPLETEUSERID;
    private Timestamp FIRSTPROCESSSTARTTIME;
    private String FIRSTPROCESSSTARTUSERID;
    private Double RATIOCUTTINGPERCENT;
    private String LOTGRADE;
    private String HOLDSTATE;
    private String REWORKSTATE;
    private String LOTSTATE;
    private String SHIPSTATE;
    private String SHIPPINGUSERID;
    private String LASTJUDGECODE;
    private String LOCATION;
    private String CARRIERID;
    private String ASSIGNSLOT;
    private Long REWORKCOUNT;
    private Long REPEATCOUNT;
    private String GOINORDERREQUIRED;
    private String MERGEFLAG;
    private String PARENTLOTID;
    private String MERGERATIO;
    private String DESCRIPTION;
    private String MATERIALLOTID;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public LOTINFORMATION() { }

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
    
    // PRODUCTTYPE
    public String getPRODUCTTYPE()
    {
        return PRODUCTTYPE;
    }
    public void setPRODUCTTYPE(String PRODUCTTYPE)
    {
        this.PRODUCTTYPE = PRODUCTTYPE;
        this.DATAMAP.put("PRODUCTTYPE", PRODUCTTYPE);
    }

    // REPROUTEID
    public String getREPROUTEID()
    {
        return REPROUTEID;
    }
    public void setREPROUTEID(String REPROUTEID)
    {
        this.REPROUTEID = REPROUTEID;
        this.DATAMAP.put("REPROUTEID", REPROUTEID);
    }

    // PROCESSROUTEID
    public String getPROCESSROUTEID()
    {
        return PROCESSROUTEID;
    }
    public void setPROCESSROUTEID(String PROCESSROUTEID)
    {
        this.PROCESSROUTEID = PROCESSROUTEID;
        this.DATAMAP.put("PROCESSROUTEID", PROCESSROUTEID);
    }

    // PROCESSROUTETYPE
    public String getPROCESSROUTETYPE()
    {
        return PROCESSROUTETYPE;
    }
    public void setPROCESSROUTETYPE(String PROCESSROUTETYPE)
    {
        this.PROCESSROUTETYPE = PROCESSROUTETYPE;
        this.DATAMAP.put("PROCESSROUTETYPE", PROCESSROUTETYPE);
    }
    
	// BOMID
    public String getBOMID()
    {
        return BOMID;
    }
    public void setBOMID(String BOMID)
    {
        this.BOMID = BOMID;
        this.DATAMAP.put("BOMID", BOMID);
    }

    // BOMVERSION
    public String getBOMVERSION()
    {
        return BOMVERSION;
    }
    public void setBOMVERSION(String BOMVERSION)
    {
        this.BOMVERSION = BOMVERSION;
        this.DATAMAP.put("BOMVERSION", BOMVERSION);
    }

    // POLICYNAME
    public String getPOLICYNAME()
    {
        return POLICYNAME;
    }
    public void setPOLICYNAME(String POLICYNAME)
    {
        this.POLICYNAME = POLICYNAME;
        this.DATAMAP.put("POLICYNAME", POLICYNAME);
    }

    // POLICYVALUE
    public String getPOLICYVALUE()
    {
        return POLICYVALUE;
    }
    public void setPOLICYVALUE(String POLICYVALUE)
    {
        this.POLICYVALUE = POLICYVALUE;
        this.DATAMAP.put("POLICYVALUE", POLICYVALUE);
    }

    // CONDITIONTYPE
    public String getCONDITIONTYPE()
    {
        return CONDITIONTYPE;
    }
    public void setCONDITIONTYPE(String CONDITIONTYPE)
    {
        this.CONDITIONTYPE = CONDITIONTYPE;
        this.DATAMAP.put("CONDITIONTYPE", CONDITIONTYPE);
    }

    // CONDITIONID
    public String getCONDITIONID()
    {
        return CONDITIONID;
    }
    public void setCONDITIONID(String CONDITIONID)
    {
        this.CONDITIONID = CONDITIONID;
        this.DATAMAP.put("CONDITIONID", CONDITIONID);
    }

    // CONDITIONVALUE
    public String getCONDITIONVALUE()
    {
        return CONDITIONVALUE;
    }
    public void setCONDITIONVALUE(String CONDITIONVALUE)
    {
        this.CONDITIONVALUE = CONDITIONVALUE;
        this.DATAMAP.put("CONDITIONVALUE", CONDITIONVALUE);
    }

    // LOTTYPE
    public String getLOTTYPE()
    {
        return LOTTYPE;
    }
    public void setLOTTYPE(String LOTTYPE)
    {
        this.LOTTYPE = LOTTYPE;
        this.DATAMAP.put("LOTTYPE", LOTTYPE);
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

    // ROOTLOTID
    public String getROOTLOTID()
    {
        return ROOTLOTID;
    }
    public void setROOTLOTID(String ROOTLOTID)
    {
        this.ROOTLOTID = ROOTLOTID;
        this.DATAMAP.put("ROOTLOTID", ROOTLOTID);
    }

    // SOURCELOTID
    public String getSOURCELOTID()
    {
        return SOURCELOTID;
    }
    public void setSOURCELOTID(String SOURCELOTID)
    {
        this.SOURCELOTID = SOURCELOTID;
        this.DATAMAP.put("SOURCELOTID", SOURCELOTID);
    }

    // PRODUCTUNITQUANTITY
    public String getPRODUCTUNITQUANTITY()
    {
        return PRODUCTUNITQUANTITY;
    }
    public void setPRODUCTUNITQUANTITY(String PRODUCTUNITQUANTITY)
    {
        this.PRODUCTUNITQUANTITY = PRODUCTUNITQUANTITY;
        this.DATAMAP.put("PRODUCTUNITQUANTITY", PRODUCTUNITQUANTITY);
    }

    // CURRENTQUANTITY
    public Double getCURRENTQUANTITY()
    {
        return CURRENTQUANTITY;
    }
    public void setCURRENTQUANTITY(Double CURRENTQUANTITY)
    {
        this.CURRENTQUANTITY = CURRENTQUANTITY;
        this.DATAMAP.put("CURRENTQUANTITY", CURRENTQUANTITY);
    }

    // STARTDUEDATE
    public Timestamp getSTARTDUEDATE()
    {
        return STARTDUEDATE;
    }
    public void setSTARTDUEDATE(Timestamp STARTDUEDATE)
    {
        this.STARTDUEDATE = STARTDUEDATE;
        this.DATAMAP.put("STARTDUEDATE", STARTDUEDATE);
    }

    // ENDDUEDATE
    public Timestamp getENDDUEDATE()
    {
        return ENDDUEDATE;
    }
    public void setENDDUEDATE(Timestamp ENDDUEDATE)
    {
        this.ENDDUEDATE = ENDDUEDATE;
        this.DATAMAP.put("ENDDUEDATE", ENDDUEDATE);
    }

    // PRIORITY
    public String getPRIORITY()
    {
        return PRIORITY;
    }
    public void setPRIORITY(String PRIORITY)
    {
        this.PRIORITY = PRIORITY;
        this.DATAMAP.put("PRIORITY", PRIORITY);
    }

    // PLANTID
    public String getPLANTID()
    {
        return PLANTID;
    }
    public void setPLANTID(String PLANTID)
    {
        this.PLANTID = PLANTID;
        this.DATAMAP.put("PLANTID", PLANTID);
    }

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

    // DEPARTMENT
    public String getDEPARTMENT()
    {
        return DEPARTMENT;
    }
    public void setDEPARTMENT(String DEPARTMENT)
    {
        this.DEPARTMENT = DEPARTMENT;
        this.DATAMAP.put("DEPARTMENT", DEPARTMENT);
    }

    // WORKCENTER
    public String getWORKCENTER()
    {
        return WORKCENTER;
    }
    public void setWORKCENTER(String WORKCENTER)
    {
        this.WORKCENTER = WORKCENTER;
        this.DATAMAP.put("WORKCENTER", WORKCENTER);
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

    // RELEASETIME
    public Timestamp getRELEASETIME()
    {
        return RELEASETIME;
    }
    public void setRELEASETIME(Timestamp RELEASETIME)
    {
        this.RELEASETIME = RELEASETIME;
        this.DATAMAP.put("RELEASETIME", RELEASETIME);
    }

    // RELEASEUSERID
    public String getRELEASEUSERID()
    {
        return RELEASEUSERID;
    }
    public void setRELEASEUSERID(String RELEASEUSERID)
    {
        this.RELEASEUSERID = RELEASEUSERID;
        this.DATAMAP.put("RELEASEUSERID", RELEASEUSERID);
    }

    // COMPLETETIME
    public Timestamp getCOMPLETETIME()
    {
        return COMPLETETIME;
    }
    public void setCOMPLETETIME(Timestamp COMPLETETIME)
    {
        this.COMPLETETIME = COMPLETETIME;
        this.DATAMAP.put("COMPLETETIME", COMPLETETIME);
    }

    // COMPLETEUSERID
    public String getCOMPLETEUSERID()
    {
        return COMPLETEUSERID;
    }
    public void setCOMPLETEUSERID(String COMPLETEUSERID)
    {
        this.COMPLETEUSERID = COMPLETEUSERID;
        this.DATAMAP.put("COMPLETEUSERID", COMPLETEUSERID);
    }

    // FIRSTPROCESSSTARTTIME
    public Timestamp getFIRSTPROCESSSTARTTIME()
    {
        return FIRSTPROCESSSTARTTIME;
    }
    public void setFIRSTPROCESSSTARTTIME(Timestamp FIRSTPROCESSSTARTTIME)
    {
        this.FIRSTPROCESSSTARTTIME = FIRSTPROCESSSTARTTIME;
        this.DATAMAP.put("FIRSTPROCESSSTARTTIME", FIRSTPROCESSSTARTTIME);
    }

    // FIRSTPROCESSSTARTUSERID
    public String getFIRSTPROCESSSTARTUSERID()
    {
        return FIRSTPROCESSSTARTUSERID;
    }
    public void setFIRSTPROCESSSTARTUSERID(String FIRSTPROCESSSTARTUSERID)
    {
        this.FIRSTPROCESSSTARTUSERID = FIRSTPROCESSSTARTUSERID;
        this.DATAMAP.put("FIRSTPROCESSSTARTUSERID", FIRSTPROCESSSTARTUSERID);
    }

    // RATIOCUTTINGPERCENT
    public Double getRATIOCUTTINGPERCENT()
    {
        return RATIOCUTTINGPERCENT;
    }
    public void setRATIOCUTTINGPERCENT(Double RATIOCUTTINGPERCENT)
    {
        this.RATIOCUTTINGPERCENT = RATIOCUTTINGPERCENT;
        this.DATAMAP.put("RATIOCUTTINGPERCENT", RATIOCUTTINGPERCENT);
    }

    // LOTGRADE
    public String getLOTGRADE()
    {
        return LOTGRADE;
    }
    public void setLOTGRADE(String LOTGRADE)
    {
        this.LOTGRADE = LOTGRADE;
        this.DATAMAP.put("LOTGRADE", LOTGRADE);
    }

    // HOLDSTATE
    public String getHOLDSTATE()
    {
        return HOLDSTATE;
    }
    public void setHOLDSTATE(String HOLDSTATE)
    {
        this.HOLDSTATE = HOLDSTATE;
        this.DATAMAP.put("HOLDSTATE", HOLDSTATE);
    }

    // REWORKSTATE
    public String getREWORKSTATE()
    {
        return REWORKSTATE;
    }
    public void setREWORKSTATE(String REWORKSTATE)
    {
        this.REWORKSTATE = REWORKSTATE;
        this.DATAMAP.put("REWORKSTATE", REWORKSTATE);
    }

    // LOTSTATE
    public String getLOTSTATE()
    {
        return LOTSTATE;
    }
    public void setLOTSTATE(String LOTSTATE)
    {
        this.LOTSTATE = LOTSTATE;
        this.DATAMAP.put("LOTSTATE", LOTSTATE);
    }

    // SHIPSTATE
    public String getSHIPSTATE()
    {
        return SHIPSTATE;
    }
    public void setSHIPSTATE(String SHIPSTATE)
    {
        this.SHIPSTATE = SHIPSTATE;
        this.DATAMAP.put("SHIPSTATE", SHIPSTATE);
    }

    // SHIPPINGUSERID
    public String getSHIPPINGUSERID()
    {
        return SHIPPINGUSERID;
    }
    public void setSHIPPINGUSERID(String SHIPPINGUSERID)
    {
        this.SHIPPINGUSERID = SHIPPINGUSERID;
        this.DATAMAP.put("SHIPPINGUSERID", SHIPPINGUSERID);
    }

    // LASTJUDGECODE
    public String getLASTJUDGECODE()
    {
        return LASTJUDGECODE;
    }
    public void setLASTJUDGECODE(String LASTJUDGECODE)
    {
        this.LASTJUDGECODE = LASTJUDGECODE;
        this.DATAMAP.put("LASTJUDGECODE", LASTJUDGECODE);
    }

    // LOCATION
    public String getLOCATION()
    {
        return LOCATION;
    }
    public void setLOCATION(String LOCATION)
    {
        this.LOCATION = LOCATION;
        this.DATAMAP.put("LOCATION", LOCATION);
    }
    
    // CARRIERID
    public String getCARRIERID()
    {
        return CARRIERID;
    }
    public void setCARRIERID(String CARRIERID)
    {
        this.CARRIERID = CARRIERID;
        this.DATAMAP.put("CARRIERID", CARRIERID);
    }
    
    // ASSIGNSLOT
    public String getASSIGNSLOT()
    {
        return ASSIGNSLOT;
    }
    public void setASSIGNSLOT(String ASSIGNSLOT)
    {
        this.ASSIGNSLOT = ASSIGNSLOT;
        this.DATAMAP.put("ASSIGNSLOT", ASSIGNSLOT);
    }

    // REWORKCOUNT
    public Long getREWORKCOUNT()
    {
        return REWORKCOUNT;
    }
    public void setREWORKCOUNT(Long REWORKCOUNT)
    {
        this.REWORKCOUNT = REWORKCOUNT;
        this.DATAMAP.put("REWORKCOUNT", REWORKCOUNT);
    }

    // REPEATCOUNT
    public Long getREPEATCOUNT()
    {
        return REPEATCOUNT;
    }
    public void setREPEATCOUNT(Long REPEATCOUNT)
    {
        this.REPEATCOUNT = REPEATCOUNT;
        this.DATAMAP.put("REPEATCOUNT", REPEATCOUNT);
    }

    // GOINORDERREQUIRED
    public String getGOINORDERREQUIRED()
    {
        return GOINORDERREQUIRED;
    }
    public void setGOINORDERREQUIRED(String GOINORDERREQUIRED)
    {
        this.GOINORDERREQUIRED = GOINORDERREQUIRED;
        this.DATAMAP.put("GOINORDERREQUIRED", GOINORDERREQUIRED);
    }

    // MERGEFLAG
    public String getMERGEFLAG()
    {
        return MERGEFLAG;
    }
    public void setMERGEFLAG(String MERGEFLAG)
    {
        this.MERGEFLAG = MERGEFLAG;
        this.DATAMAP.put("MERGEFLAG", MERGEFLAG);
    }

    // PARENTLOTID
    public String getPARENTLOTID()
    {
        return PARENTLOTID;
    }
    public void setPARENTLOTID(String PARENTLOTID)
    {
        this.PARENTLOTID = PARENTLOTID;
        this.DATAMAP.put("PARENTLOTID", PARENTLOTID);
    }

    // MERGERATIO
    public String getMERGERATIO()
    {
        return MERGERATIO;
    }
    public void setMERGERATIO(String MERGERATIO)
    {
        this.MERGERATIO = MERGERATIO;
        this.DATAMAP.put("MERGERATIO", MERGERATIO);
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

    // MATERIALLOTID
    public String getMATERIALLOTID()
    {
        return MATERIALLOTID;
    }
    public void setMATERIALLOTID(String MATERIALLOTID)
    {
        this.MATERIALLOTID = MATERIALLOTID;
        this.DATAMAP.put("MATERIALLOTID", MATERIALLOTID);
    }

    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.LOTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"LOTID"});
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
