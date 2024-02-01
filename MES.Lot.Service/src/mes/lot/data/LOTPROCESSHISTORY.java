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
public class LOTPROCESSHISTORY
{
    // Key Info
    private String LOTID;
    private String TIMEKEY;

    // Data Info
    private String WORKORDER;
    private String ROOTLOTID;
    private String SOURCELOTID;
    private String DESTINATIONLOTID;
    private String PROCESSROUTEID;
    private String PROCESSROUTETYPE;
    private String PRODUCTID;
    private String PROCESSID;
    private String PROCESSNAME;
    private Long PROCESSSEQUENCE;
    private String RECIPEID;
    private String RECIPENAME;
    private Long RECIPESEQUENCE;
    private String RECIPETYPE;
    private String RECIPERELATIONCODE;
    private String RECIPETYPECODE;
    private String RECIPEPARAMETERID;
    private String RECIPEPARAMETERNAME;
    private String EVENTNAME;
    private String EVENTUSERID;
    private String EVENTCOMMENT;
    private Timestamp EVENTTIME;
    private String EVENTFLAG;
    private String EQUIPMENTID;
    private Double OLDCURRENTQUANTITY;
    private Double CURRENTQUANTITY;
    private String DUEDATE;
    private String PRIORITY;
    private String HOLDSTATE;
    private String REWORKSTATE;
    private String LOTSTATE;
    private String SHIPSTATE;
    private String REASONCODETYPE;
    private String REASONCODE;
    private Long REWORKCOUNT;
    private Long REPEATCOUNT;
    private String CONSUMEDLOTID;
    private String CANCELFLAG;
    private String LOCATION;
    private String CARRIERID;
    private String ASSIGNSLOT;
    private String FEEDBACKCOMMENT;
    private String PROCESSTIMEFLAG;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public LOTPROCESSHISTORY() { }

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

    // DESTINATIONLOTID
    public String getDESTINATIONLOTID()
    {
        return DESTINATIONLOTID;
    }
    public void setDESTINATIONLOTID(String DESTINATIONLOTID)
    {
        this.DESTINATIONLOTID = DESTINATIONLOTID;
        this.DATAMAP.put("DESTINATIONLOTID", DESTINATIONLOTID);
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

    // RECIPETYPE
    public String getRECIPETYPE()
    {
        return RECIPETYPE;
    }
    public void setRECIPETYPE(String RECIPETYPE)
    {
        this.RECIPETYPE = RECIPETYPE;
        this.DATAMAP.put("RECIPETYPE", RECIPETYPE);
    }

    // RECIPERELATIONCODE
    public String getRECIPERELATIONCODE()
    {
        return RECIPERELATIONCODE;
    }
    public void setRECIPERELATIONCODE(String RECIPERELATIONCODE)
    {
        this.RECIPERELATIONCODE = RECIPERELATIONCODE;
        this.DATAMAP.put("RECIPERELATIONCODE", RECIPERELATIONCODE);
    }

    // RECIPETYPECODE
    public String getRECIPETYPECODE()
    {
        return RECIPETYPECODE;
    }
    public void setRECIPETYPECODE(String RECIPETYPECODE)
    {
        this.RECIPETYPECODE = RECIPETYPECODE;
        this.DATAMAP.put("RECIPETYPECODE", RECIPETYPECODE);
    }

    // RECIPEPARAMETERID
    public String getRECIPEPARAMETERID()
    {
        return RECIPEPARAMETERID;
    }
    public void setRECIPEPARAMETERID(String RECIPEPARAMETERID)
    {
        this.RECIPEPARAMETERID = RECIPEPARAMETERID;
        this.DATAMAP.put("RECIPEPARAMETERID", RECIPEPARAMETERID);
    }

    // RECIPEPARAMETERNAME
    public String getRECIPEPARAMETERNAME()
    {
        return RECIPEPARAMETERNAME;
    }
    public void setRECIPEPARAMETERNAME(String RECIPEPARAMETERNAME)
    {
        this.RECIPEPARAMETERNAME = RECIPEPARAMETERNAME;
        this.DATAMAP.put("RECIPEPARAMETERNAME", RECIPEPARAMETERNAME);
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

    // EVENTUSERID
    public String getEVENTUSERID()
    {
        return EVENTUSERID;
    }
    public void setEVENTUSERID(String EVENTUSERID)
    {
        this.EVENTUSERID = EVENTUSERID;
        this.DATAMAP.put("EVENTUSERID", EVENTUSERID);
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

    // EVENTFLAG
    public String getEVENTFLAG()
    {
        return EVENTFLAG;
    }
    public void setEVENTFLAG(String EVENTFLAG)
    {
        this.EVENTFLAG = EVENTFLAG;
        this.DATAMAP.put("EVENTFLAG", EVENTFLAG);
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

    // OLDCURRENTQUANTITY
    public Double getOLDCURRENTQUANTITY()
    {
        return OLDCURRENTQUANTITY;
    }
    public void setOLDCURRENTQUANTITY(Double OLDCURRENTQUANTITY)
    {
        this.OLDCURRENTQUANTITY = OLDCURRENTQUANTITY;
        this.DATAMAP.put("OLDCURRENTQUANTITY", OLDCURRENTQUANTITY);
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

    // DUEDATE
    public String getDUEDATE()
    {
        return DUEDATE;
    }
    public void setDUEDATE(String DUEDATE)
    {
        this.DUEDATE = DUEDATE;
        this.DATAMAP.put("DUEDATE", DUEDATE);
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

    // CONSUMEDLOTID
    public String getCONSUMEDLOTID()
    {
        return CONSUMEDLOTID;
    }
    public void setCONSUMEDLOTID(String CONSUMEDLOTID)
    {
        this.CONSUMEDLOTID = CONSUMEDLOTID;
        this.DATAMAP.put("CONSUMEDLOTID", CONSUMEDLOTID);
    }

    // CANCELFLAG
    public String getCANCELFLAG()
    {
        return CANCELFLAG;
    }
    public void setCANCELFLAG(String CANCELFLAG)
    {
        this.CANCELFLAG = CANCELFLAG;
        this.DATAMAP.put("CANCELFLAG", CANCELFLAG);
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

    // FEEDBACKCOMMENT
    public String getFEEDBACKCOMMENT()
    {
        return FEEDBACKCOMMENT;
    }
    public void setFEEDBACKCOMMENT(String FEEDBACKCOMMENT)
    {
        this.FEEDBACKCOMMENT = FEEDBACKCOMMENT;
        this.DATAMAP.put("FEEDBACKCOMMENT", FEEDBACKCOMMENT);
    }

    // PROCESSTIMEFLAG
    public String getPROCESSTIMEFLAG()
    {
        return PROCESSTIMEFLAG;
    }
    public void setPROCESSTIMEFLAG(String PROCESSTIMEFLAG)
    {
        this.PROCESSTIMEFLAG = PROCESSTIMEFLAG;
        this.DATAMAP.put("PROCESSTIMEFLAG", PROCESSTIMEFLAG);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.LOTID == null || this.TIMEKEY == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"LOTID, TIMEKEY"});
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