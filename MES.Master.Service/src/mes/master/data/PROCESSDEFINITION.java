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
public class PROCESSDEFINITION
{
    // Key Info
    private String PLANTID;
    private String PROCESSID;

    // Data Info
    private String PROCESSNAME;
    private String ACTIVESTATE;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private String PROCESSTYPE;
    private String DETAILPROCESSTYPE;
    private String BASICPROCESSID;
    private String CLASSIFICATION;
    private String PACKINGFLAG;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String DESCRIPTION;
    private String WORKCENTER;
    private String REPRESENTATIVECHAR;
    private Double CYCLETIME;
    private Double STANDARDTIME;
    private Double MAXTIME;
    private String ERPPROCESSCODE;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public PROCESSDEFINITION() { }

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

    // PROCESSID
    public String getKeyPROCESSID()
    {
        return PROCESSID;
    }
    public void setKeyPROCESSID(String PROCESSID)
    {
        this.PROCESSID = PROCESSID;
        this.KEYMAP.put("PROCESSID", PROCESSID);
    }


    // Data Methods
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

    // PROCESSTYPE
    public String getPROCESSTYPE()
    {
        return PROCESSTYPE;
    }
    public void setPROCESSTYPE(String PROCESSTYPE)
    {
        this.PROCESSTYPE = PROCESSTYPE;
        this.DATAMAP.put("PROCESSTYPE", PROCESSTYPE);
    }

    // DETAILPROCESSTYPE
    public String getDETAILPROCESSTYPE()
    {
        return DETAILPROCESSTYPE;
    }
    public void setDETAILPROCESSTYPE(String DETAILPROCESSTYPE)
    {
        this.DETAILPROCESSTYPE = DETAILPROCESSTYPE;
        this.DATAMAP.put("DETAILPROCESSTYPE", DETAILPROCESSTYPE);
    }

    // BASICPROCESSID
    public String getBASICPROCESSID()
    {
        return BASICPROCESSID;
    }
    public void setBASICPROCESSID(String BASICPROCESSID)
    {
        this.BASICPROCESSID = BASICPROCESSID;
        this.DATAMAP.put("BASICPROCESSID", BASICPROCESSID);
    }

    // CLASSIFICATION
    public String getCLASSIFICATION()
    {
        return CLASSIFICATION;
    }
    public void setCLASSIFICATION(String CLASSIFICATION)
    {
        this.CLASSIFICATION = CLASSIFICATION;
        this.DATAMAP.put("CLASSIFICATION", CLASSIFICATION);
    }

    // PACKINGFLAG
    public String getPACKINGFLAG()
    {
        return PACKINGFLAG;
    }
    public void setPACKINGFLAG(String PACKINGFLAG)
    {
        this.PACKINGFLAG = PACKINGFLAG;
        this.DATAMAP.put("PACKINGFLAG", PACKINGFLAG);
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
    
    // REPRESENTATIVECHAR
    public String getREPRESENTATIVECHAR()
    {
        return REPRESENTATIVECHAR;
    }
    public void setREPRESENTATIVECHAR(String REPRESENTATIVECHAR)
    {
        this.REPRESENTATIVECHAR = REPRESENTATIVECHAR;
        this.DATAMAP.put("REPRESENTATIVECHAR", REPRESENTATIVECHAR);
    }
    
    // CYCLETIME
    public Double getCYCLETIME()
    {
        return CYCLETIME;
    }
    public void setCYCLETIME(Double CYCLETIME)
    {
        this.CYCLETIME = CYCLETIME;
        this.DATAMAP.put("CYCLETIME", CYCLETIME);
    }
    
    // STANDARDTIME
    public Double getSTANDARDTIME()
    {
        return STANDARDTIME;
    }
    public void setSTANDARDTIME(Double STANDARDTIME)
    {
        this.STANDARDTIME = STANDARDTIME;
        this.DATAMAP.put("STANDARDTIME", STANDARDTIME);
    }
    
    // MAXTIME
    public Double getMAXTIME()
    {
        return MAXTIME;
    }
    public void setMAXTIME(Double MAXTIME)
    {
        this.MAXTIME = MAXTIME;
        this.DATAMAP.put("MAXTIME", MAXTIME);
    }
    
    // ERPPROCESSCODE
    public String getERPPROCESSCODE()
    {
        return ERPPROCESSCODE;
    }
    public void setERPPROCESSCODE(String ERPPROCESSCODE)
    {
        this.ERPPROCESSCODE = ERPPROCESSCODE;
        this.DATAMAP.put("ERPPROCESSCODE", ERPPROCESSCODE);
    }
    

    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PROCESSID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PROCESSID"});
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
