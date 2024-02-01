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
public class EQUIPMENTDEFINITION
{
    // Key Info
    private String PLANTID;
    private String EQUIPMENTID;

    // Data Info
    private String EQUIPMENTNAME;
    private String EQUIPMENTTYPE;
    private String EQUIPMENTDETAILTYPE;
    private String ACTIVESTATE;
    private String AREAID;
    private String SUPEREQUIPMENTID;
    private String EQUIPMENTGROUPID;
    private String PROCESSUNIT;
    private String PROCESSCAPACITY;
    private Long PROCESSGROUPSIZEMIN;
    private Long PROCESSGROUPSIZEMAX;
    private String DEFUALTRECIPENAMESPACENAME;
    private String STATEMODELID;
    private String COSTCENTER;
    private Long VOLUME;
    private String PRODUCTIONTYPE;
    private String AUTOFLAG;
    private String LOCATIONID;
    private String MODELNUMBER;
    private String SERIALNUMBER;
    private String PURCHASEDATE;
    private String VENDOR;
    private String VENDORDESCRIPTION;
    private String VENDORADDRESS;
    private String MAKEDATE;
    private String MAKER;
    private String MAKERADDRESS;
    private String SUBCONTRACTOR;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public EQUIPMENTDEFINITION() { }

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
    // EQUIPMENTNAME
    public String getEQUIPMENTNAME()
    {
        return EQUIPMENTNAME;
    }
    public void setEQUIPMENTNAME(String EQUIPMENTNAME)
    {
        this.EQUIPMENTNAME = EQUIPMENTNAME;
        this.DATAMAP.put("EQUIPMENTNAME", EQUIPMENTNAME);
    }

    // EQUIPMENTTYPE
    public String getEQUIPMENTTYPE()
    {
        return EQUIPMENTTYPE;
    }
    public void setEQUIPMENTTYPE(String EQUIPMENTTYPE)
    {
        this.EQUIPMENTTYPE = EQUIPMENTTYPE;
        this.DATAMAP.put("EQUIPMENTTYPE", EQUIPMENTTYPE);
    }

    // EQUIPMENTDETAILTYPE
    public String getEQUIPMENTDETAILTYPE()
    {
        return EQUIPMENTDETAILTYPE;
    }
    public void setEQUIPMENTDETAILTYPE(String EQUIPMENTDETAILTYPE)
    {
        this.EQUIPMENTDETAILTYPE = EQUIPMENTDETAILTYPE;
        this.DATAMAP.put("EQUIPMENTDETAILTYPE", EQUIPMENTDETAILTYPE);
    }

    // ACTIVESTATE
    public String getACTIVESTATE()
    {
        return ACTIVESTATE;
    }
    public void setACTIVESTATE(String ACTIVESTATE)
    {
        this.ACTIVESTATE = ACTIVESTATE;
        this.DATAMAP.put("ACTIVESTATE", ACTIVESTATE);
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

    // SUPEREQUIPMENTID
    public String getSUPEREQUIPMENTID()
    {
        return SUPEREQUIPMENTID;
    }
    public void setSUPEREQUIPMENTID(String SUPEREQUIPMENTID)
    {
        this.SUPEREQUIPMENTID = SUPEREQUIPMENTID;
        this.DATAMAP.put("SUPEREQUIPMENTID", SUPEREQUIPMENTID);
    }

    // EQUIPMENTGROUPID
    public String getEQUIPMENTGROUPID()
    {
        return EQUIPMENTGROUPID;
    }
    public void setEQUIPMENTGROUPID(String EQUIPMENTGROUPID)
    {
        this.EQUIPMENTGROUPID = EQUIPMENTGROUPID;
        this.DATAMAP.put("EQUIPMENTGROUPID", EQUIPMENTGROUPID);
    }

    // PROCESSUNIT
    public String getPROCESSUNIT()
    {
        return PROCESSUNIT;
    }
    public void setPROCESSUNIT(String PROCESSUNIT)
    {
        this.PROCESSUNIT = PROCESSUNIT;
        this.DATAMAP.put("PROCESSUNIT", PROCESSUNIT);
    }

    // PROCESSCAPACITY
    public String getPROCESSCAPACITY()
    {
        return PROCESSCAPACITY;
    }
    public void setPROCESSCAPACITY(String PROCESSCAPACITY)
    {
        this.PROCESSCAPACITY = PROCESSCAPACITY;
        this.DATAMAP.put("PROCESSCAPACITY", PROCESSCAPACITY);
    }

    // PROCESSGROUPSIZEMIN
    public Long getPROCESSGROUPSIZEMIN()
    {
        return PROCESSGROUPSIZEMIN;
    }
    public void setPROCESSGROUPSIZEMIN(Long PROCESSGROUPSIZEMIN)
    {
        this.PROCESSGROUPSIZEMIN = PROCESSGROUPSIZEMIN;
        this.DATAMAP.put("PROCESSGROUPSIZEMIN", PROCESSGROUPSIZEMIN);
    }

    // PROCESSGROUPSIZEMAX
    public Long getPROCESSGROUPSIZEMAX()
    {
        return PROCESSGROUPSIZEMAX;
    }
    public void setPROCESSGROUPSIZEMAX(Long PROCESSGROUPSIZEMAX)
    {
        this.PROCESSGROUPSIZEMAX = PROCESSGROUPSIZEMAX;
        this.DATAMAP.put("PROCESSGROUPSIZEMAX", PROCESSGROUPSIZEMAX);
    }

    // DEFUALTRECIPENAMESPACENAME
    public String getDEFUALTRECIPENAMESPACENAME()
    {
        return DEFUALTRECIPENAMESPACENAME;
    }
    public void setDEFUALTRECIPENAMESPACENAME(String DEFUALTRECIPENAMESPACENAME)
    {
        this.DEFUALTRECIPENAMESPACENAME = DEFUALTRECIPENAMESPACENAME;
        this.DATAMAP.put("DEFUALTRECIPENAMESPACENAME", DEFUALTRECIPENAMESPACENAME);
    }

    // STATEMODELID
    public String getSTATEMODELID()
    {
        return STATEMODELID;
    }
    public void setSTATEMODELID(String STATEMODELID)
    {
        this.STATEMODELID = STATEMODELID;
        this.DATAMAP.put("STATEMODELID", STATEMODELID);
    }

    // COSTCENTER
    public String getCOSTCENTER()
    {
        return COSTCENTER;
    }
    public void setCOSTCENTER(String COSTCENTER)
    {
        this.COSTCENTER = COSTCENTER;
        this.DATAMAP.put("COSTCENTER", COSTCENTER);
    }

    // VOLUME
    public Long getVOLUME()
    {
        return VOLUME;
    }
    public void setVOLUME(Long VOLUME)
    {
        this.VOLUME = VOLUME;
        this.DATAMAP.put("VOLUME", VOLUME);
    }

    // PRODUCTIONTYPE
    public String getPRODUCTIONTYPE()
    {
        return PRODUCTIONTYPE;
    }
    public void setPRODUCTIONTYPE(String PRODUCTIONTYPE)
    {
        this.PRODUCTIONTYPE = PRODUCTIONTYPE;
        this.DATAMAP.put("PRODUCTIONTYPE", PRODUCTIONTYPE);
    }

    // AUTOFLAG
    public String getAUTOFLAG()
    {
        return AUTOFLAG;
    }
    public void setAUTOFLAG(String AUTOFLAG)
    {
        this.AUTOFLAG = AUTOFLAG;
        this.DATAMAP.put("AUTOFLAG", AUTOFLAG);
    }

    // LOCATIONID
    public String getLOCATIONID()
    {
        return LOCATIONID;
    }
    public void setLOCATIONID(String LOCATIONID)
    {
        this.LOCATIONID = LOCATIONID;
        this.DATAMAP.put("LOCATIONID", LOCATIONID);
    }

    // MODELNUMBER
    public String getMODELNUMBER()
    {
        return MODELNUMBER;
    }
    public void setMODELNUMBER(String MODELNUMBER)
    {
        this.MODELNUMBER = MODELNUMBER;
        this.DATAMAP.put("MODELNUMBER", MODELNUMBER);
    }

    // SERIALNUMBER
    public String getSERIALNUMBER()
    {
        return SERIALNUMBER;
    }
    public void setSERIALNUMBER(String SERIALNUMBER)
    {
        this.SERIALNUMBER = SERIALNUMBER;
        this.DATAMAP.put("SERIALNUMBER", SERIALNUMBER);
    }

    // PURCHASEDATE
    public String getPURCHASEDATE()
    {
        return PURCHASEDATE;
    }
    public void setPURCHASEDATE(String PURCHASEDATE)
    {
        this.PURCHASEDATE = PURCHASEDATE;
        this.DATAMAP.put("PURCHASEDATE", PURCHASEDATE);
    }

    // VENDOR
    public String getVENDOR()
    {
        return VENDOR;
    }
    public void setVENDOR(String VENDOR)
    {
        this.VENDOR = VENDOR;
        this.DATAMAP.put("VENDOR", VENDOR);
    }

    // VENDORDESCRIPTION
    public String getVENDORDESCRIPTION()
    {
        return VENDORDESCRIPTION;
    }
    public void setVENDORDESCRIPTION(String VENDORDESCRIPTION)
    {
        this.VENDORDESCRIPTION = VENDORDESCRIPTION;
        this.DATAMAP.put("VENDORDESCRIPTION", VENDORDESCRIPTION);
    }

    // VENDORADDRESS
    public String getVENDORADDRESS()
    {
        return VENDORADDRESS;
    }
    public void setVENDORADDRESS(String VENDORADDRESS)
    {
        this.VENDORADDRESS = VENDORADDRESS;
        this.DATAMAP.put("VENDORADDRESS", VENDORADDRESS);
    }

    // MAKEDATE
    public String getMAKEDATE()
    {
        return MAKEDATE;
    }
    public void setMAKEDATE(String MAKEDATE)
    {
        this.MAKEDATE = MAKEDATE;
        this.DATAMAP.put("MAKEDATE", MAKEDATE);
    }

    // MAKER
    public String getMAKER()
    {
        return MAKER;
    }
    public void setMAKER(String MAKER)
    {
        this.MAKER = MAKER;
        this.DATAMAP.put("MAKER", MAKER);
    }

    // MAKERADDRESS
    public String getMAKERADDRESS()
    {
        return MAKERADDRESS;
    }
    public void setMAKERADDRESS(String MAKERADDRESS)
    {
        this.MAKERADDRESS = MAKERADDRESS;
        this.DATAMAP.put("MAKERADDRESS", MAKERADDRESS);
    }

    // SUBCONTRACTOR
    public String getSUBCONTRACTOR()
    {
        return SUBCONTRACTOR;
    }
    public void setSUBCONTRACTOR(String SUBCONTRACTOR)
    {
        this.SUBCONTRACTOR = SUBCONTRACTOR;
        this.DATAMAP.put("SUBCONTRACTOR", SUBCONTRACTOR);
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
