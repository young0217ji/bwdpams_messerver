package mes.master.data;

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
public class MODELINGCONFIRMDETAIL
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;
    private String BOMID;
    private String BOMVERSION;
    private String POLICYNAME;
    private String POLICYVALUE;
    private String CONDITIONTYPE;
    private String CONDITIONID;
    private String TIMEKEY;

    // Data Info
    private String REPPROCESSROUTEID;
    private String PROCESSROUTEID;
    private String PROCESSROUTETYPE;
    private String ROUTERELATIONTYPE;
    private Long ROUTERELATIONSEQUENCE;
    private String COMPOSITIONID;
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
    private String PROCESSTYPE;
    private String DETAILPROCESSTYPE;
    private String AUTOTRACKIN;
    private String AUTOTRACKOUT;
    private String CREATELOTFLAG;
    private String CREATELOTRULE;
    private String BASICPROCESSID;
    private String PACKINGFLAG;
    private String ENDOFROUTE;
    private String FIRSTPROCESSFLAG;
    private String CHANGEROUTEFLAG;
    private String PROCESSTIMEFLAG;
    private String AUTOMODE;
    private Double PROCESSPRINTINDEX;
    private String EQUIPMENTID;
    private String CONCURRENCYPROCESSID;
    private Long CONCURRENCYSEQUENCE;
    private String CONSUMETYPE;
    private String CONSUMEID;
    private Long RECIPEPRINTINDEX;
    private String INPUTMODE;
    private String ACTIONTYPE;
    private String TARGET;
    private String LOWERSPECLIMIT;
    private String UPPERSPECLIMIT;
    private String LOWERCONTROLLIMIT;
    private String UPPERCONTROLLIMIT;
    private String LOWERSCREENLIMIT;
    private String UPPERSCREENLIMIT;
    private String SPECTYPE;
    private String UNITOFMEASURE;
    private Long ORDERINDEX;
    private Double FEEDINGRATE;
    private String CTPFLAG;
    private String AUTOTRACKINGFLAG;
    private String DESCRIPTION;
    private Long STANDARDVALUE;
    private Double CONSUMABLEVALUE;
    private String ERPPROCESSCODE;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public MODELINGCONFIRMDETAIL() { }

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

    // PRODUCTID
    public String getKeyPRODUCTID()
    {
        return PRODUCTID;
    }
    public void setKeyPRODUCTID(String PRODUCTID)
    {
        this.PRODUCTID = PRODUCTID;
        this.KEYMAP.put("PRODUCTID", PRODUCTID);
    }

    // BOMID
    public String getKeyBOMID()
    {
        return BOMID;
    }
    public void setKeyBOMID(String BOMID)
    {
        this.BOMID = BOMID;
        this.KEYMAP.put("BOMID", BOMID);
    }

    // BOMVERSION
    public String getKeyBOMVERSION()
    {
        return BOMVERSION;
    }
    public void setKeyBOMVERSION(String BOMVERSION)
    {
        this.BOMVERSION = BOMVERSION;
        this.KEYMAP.put("BOMVERSION", BOMVERSION);
    }

    // POLICYNAME
    public String getKeyPOLICYNAME()
    {
        return POLICYNAME;
    }
    public void setKeyPOLICYNAME(String POLICYNAME)
    {
        this.POLICYNAME = POLICYNAME;
        this.KEYMAP.put("POLICYNAME", POLICYNAME);
    }

    // POLICYVALUE
    public String getKeyPOLICYVALUE()
    {
        return POLICYVALUE;
    }
    public void setKeyPOLICYVALUE(String POLICYVALUE)
    {
        this.POLICYVALUE = POLICYVALUE;
        this.KEYMAP.put("POLICYVALUE", POLICYVALUE);
    }

    // CONDITIONTYPE
    public String getKeyCONDITIONTYPE()
    {
        return CONDITIONTYPE;
    }
    public void setKeyCONDITIONTYPE(String CONDITIONTYPE)
    {
        this.CONDITIONTYPE = CONDITIONTYPE;
        this.KEYMAP.put("CONDITIONTYPE", CONDITIONTYPE);
    }

    // CONDITIONID
    public String getKeyCONDITIONID()
    {
        return CONDITIONID;
    }
    public void setKeyCONDITIONID(String CONDITIONID)
    {
        this.CONDITIONID = CONDITIONID;
        this.KEYMAP.put("CONDITIONID", CONDITIONID);
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
    // REPPROCESSROUTEID
    public String getREPPROCESSROUTEID()
    {
        return REPPROCESSROUTEID;
    }
    public void setREPPROCESSROUTEID(String REPPROCESSROUTEID)
    {
        this.REPPROCESSROUTEID = REPPROCESSROUTEID;
        this.DATAMAP.put("REPPROCESSROUTEID", REPPROCESSROUTEID);
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

    // ROUTERELATIONTYPE
    public String getROUTERELATIONTYPE()
    {
        return ROUTERELATIONTYPE;
    }
    public void setROUTERELATIONTYPE(String ROUTERELATIONTYPE)
    {
        this.ROUTERELATIONTYPE = ROUTERELATIONTYPE;
        this.DATAMAP.put("ROUTERELATIONTYPE", ROUTERELATIONTYPE);
    }

    // ROUTERELATIONSEQUENCE
    public Long getROUTERELATIONSEQUENCE()
    {
        return ROUTERELATIONSEQUENCE;
    }
    public void setROUTERELATIONSEQUENCE(Long ROUTERELATIONSEQUENCE)
    {
        this.ROUTERELATIONSEQUENCE = ROUTERELATIONSEQUENCE;
        this.DATAMAP.put("ROUTERELATIONSEQUENCE", ROUTERELATIONSEQUENCE);
    }

    // COMPOSITIONID
    public String getCOMPOSITIONID()
    {
        return COMPOSITIONID;
    }
    public void setCOMPOSITIONID(String COMPOSITIONID)
    {
        this.COMPOSITIONID = COMPOSITIONID;
        this.DATAMAP.put("COMPOSITIONID", COMPOSITIONID);
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

    // AUTOTRACKIN
    public String getAUTOTRACKIN()
    {
        return AUTOTRACKIN;
    }
    public void setAUTOTRACKIN(String AUTOTRACKIN)
    {
        this.AUTOTRACKIN = AUTOTRACKIN;
        this.DATAMAP.put("AUTOTRACKIN", AUTOTRACKIN);
    }

    // AUTOTRACKOUT
    public String getAUTOTRACKOUT()
    {
        return AUTOTRACKOUT;
    }
    public void setAUTOTRACKOUT(String AUTOTRACKOUT)
    {
        this.AUTOTRACKOUT = AUTOTRACKOUT;
        this.DATAMAP.put("AUTOTRACKOUT", AUTOTRACKOUT);
    }

    // CREATELOTFLAG
    public String getCREATELOTFLAG()
    {
        return CREATELOTFLAG;
    }
    public void setCREATELOTFLAG(String CREATELOTFLAG)
    {
        this.CREATELOTFLAG = CREATELOTFLAG;
        this.DATAMAP.put("CREATELOTFLAG", CREATELOTFLAG);
    }

    // CREATELOTRULE
    public String getCREATELOTRULE()
    {
        return CREATELOTRULE;
    }
    public void setCREATELOTRULE(String CREATELOTRULE)
    {
        this.CREATELOTRULE = CREATELOTRULE;
        this.DATAMAP.put("CREATELOTRULE", CREATELOTRULE);
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

    // ENDOFROUTE
    public String getENDOFROUTE()
    {
        return ENDOFROUTE;
    }
    public void setENDOFROUTE(String ENDOFROUTE)
    {
        this.ENDOFROUTE = ENDOFROUTE;
        this.DATAMAP.put("ENDOFROUTE", ENDOFROUTE);
    }

    // FIRSTPROCESSFLAG
    public String getFIRSTPROCESSFLAG()
    {
        return FIRSTPROCESSFLAG;
    }
    public void setFIRSTPROCESSFLAG(String FIRSTPROCESSFLAG)
    {
        this.FIRSTPROCESSFLAG = FIRSTPROCESSFLAG;
        this.DATAMAP.put("FIRSTPROCESSFLAG", FIRSTPROCESSFLAG);
    }

    // CHANGEROUTEFLAG
    public String getCHANGEROUTEFLAG()
    {
        return CHANGEROUTEFLAG;
    }
    public void setCHANGEROUTEFLAG(String CHANGEROUTEFLAG)
    {
        this.CHANGEROUTEFLAG = CHANGEROUTEFLAG;
        this.DATAMAP.put("CHANGEROUTEFLAG", CHANGEROUTEFLAG);
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

    // AUTOMODE
    public String getAUTOMODE()
    {
        return AUTOMODE;
    }
    public void setAUTOMODE(String AUTOMODE)
    {
        this.AUTOMODE = AUTOMODE;
        this.DATAMAP.put("AUTOMODE", AUTOMODE);
    }

    // PROCESSPRINTINDEX
    public Double getPROCESSPRINTINDEX()
    {
        return PROCESSPRINTINDEX;
    }
    public void setPROCESSPRINTINDEX(Double PROCESSPRINTINDEX)
    {
        this.PROCESSPRINTINDEX = PROCESSPRINTINDEX;
        this.DATAMAP.put("PROCESSPRINTINDEX", PROCESSPRINTINDEX);
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

    // CONCURRENCYPROCESSID
    public String getCONCURRENCYPROCESSID()
    {
        return CONCURRENCYPROCESSID;
    }
    public void setCONCURRENCYPROCESSID(String CONCURRENCYPROCESSID)
    {
        this.CONCURRENCYPROCESSID = CONCURRENCYPROCESSID;
        this.DATAMAP.put("CONCURRENCYPROCESSID", CONCURRENCYPROCESSID);
    }

    // CONCURRENCYSEQUENCE
    public Long getCONCURRENCYSEQUENCE()
    {
        return CONCURRENCYSEQUENCE;
    }
    public void setCONCURRENCYSEQUENCE(Long CONCURRENCYSEQUENCE)
    {
        this.CONCURRENCYSEQUENCE = CONCURRENCYSEQUENCE;
        this.DATAMAP.put("CONCURRENCYSEQUENCE", CONCURRENCYSEQUENCE);
    }

    // CONSUMETYPE
    public String getCONSUMETYPE()
    {
        return CONSUMETYPE;
    }
    public void setCONSUMETYPE(String CONSUMETYPE)
    {
        this.CONSUMETYPE = CONSUMETYPE;
        this.DATAMAP.put("CONSUMETYPE", CONSUMETYPE);
    }

    // CONSUMEID
    public String getCONSUMEID()
    {
        return CONSUMEID;
    }
    public void setCONSUMEID(String CONSUMEID)
    {
        this.CONSUMEID = CONSUMEID;
        this.DATAMAP.put("CONSUMEID", CONSUMEID);
    }

    // RECIPEPRINTINDEX
    public Long getRECIPEPRINTINDEX()
    {
        return RECIPEPRINTINDEX;
    }
    public void setRECIPEPRINTINDEX(Long RECIPEPRINTINDEX)
    {
        this.RECIPEPRINTINDEX = RECIPEPRINTINDEX;
        this.DATAMAP.put("RECIPEPRINTINDEX", RECIPEPRINTINDEX);
    }

    // INPUTMODE
    public String getINPUTMODE()
    {
        return INPUTMODE;
    }
    public void setINPUTMODE(String INPUTMODE)
    {
        this.INPUTMODE = INPUTMODE;
        this.DATAMAP.put("INPUTMODE", INPUTMODE);
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

    // TARGET
    public String getTARGET()
    {
        return TARGET;
    }
    public void setTARGET(String TARGET)
    {
        this.TARGET = TARGET;
        this.DATAMAP.put("TARGET", TARGET);
    }

    // LOWERSPECLIMIT
    public String getLOWERSPECLIMIT()
    {
        return LOWERSPECLIMIT;
    }
    public void setLOWERSPECLIMIT(String LOWERSPECLIMIT)
    {
        this.LOWERSPECLIMIT = LOWERSPECLIMIT;
        this.DATAMAP.put("LOWERSPECLIMIT", LOWERSPECLIMIT);
    }

    // UPPERSPECLIMIT
    public String getUPPERSPECLIMIT()
    {
        return UPPERSPECLIMIT;
    }
    public void setUPPERSPECLIMIT(String UPPERSPECLIMIT)
    {
        this.UPPERSPECLIMIT = UPPERSPECLIMIT;
        this.DATAMAP.put("UPPERSPECLIMIT", UPPERSPECLIMIT);
    }

    // LOWERCONTROLLIMIT
    public String getLOWERCONTROLLIMIT()
    {
        return LOWERCONTROLLIMIT;
    }
    public void setLOWERCONTROLLIMIT(String LOWERCONTROLLIMIT)
    {
        this.LOWERCONTROLLIMIT = LOWERCONTROLLIMIT;
        this.DATAMAP.put("LOWERCONTROLLIMIT", LOWERCONTROLLIMIT);
    }

    // UPPERCONTROLLIMIT
    public String getUPPERCONTROLLIMIT()
    {
        return UPPERCONTROLLIMIT;
    }
    public void setUPPERCONTROLLIMIT(String UPPERCONTROLLIMIT)
    {
        this.UPPERCONTROLLIMIT = UPPERCONTROLLIMIT;
        this.DATAMAP.put("UPPERCONTROLLIMIT", UPPERCONTROLLIMIT);
    }

    // LOWERSCREENLIMIT
    public String getLOWERSCREENLIMIT()
    {
        return LOWERSCREENLIMIT;
    }
    public void setLOWERSCREENLIMIT(String LOWERSCREENLIMIT)
    {
        this.LOWERSCREENLIMIT = LOWERSCREENLIMIT;
        this.DATAMAP.put("LOWERSCREENLIMIT", LOWERSCREENLIMIT);
    }

    // UPPERSCREENLIMIT
    public String getUPPERSCREENLIMIT()
    {
        return UPPERSCREENLIMIT;
    }
    public void setUPPERSCREENLIMIT(String UPPERSCREENLIMIT)
    {
        this.UPPERSCREENLIMIT = UPPERSCREENLIMIT;
        this.DATAMAP.put("UPPERSCREENLIMIT", UPPERSCREENLIMIT);
    }

    // SPECTYPE
    public String getSPECTYPE()
    {
        return SPECTYPE;
    }
    public void setSPECTYPE(String SPECTYPE)
    {
        this.SPECTYPE = SPECTYPE;
        this.DATAMAP.put("SPECTYPE", SPECTYPE);
    }

    // UNITOFMEASURE
    public String getUNITOFMEASURE()
    {
        return UNITOFMEASURE;
    }
    public void setUNITOFMEASURE(String UNITOFMEASURE)
    {
        this.UNITOFMEASURE = UNITOFMEASURE;
        this.DATAMAP.put("UNITOFMEASURE", UNITOFMEASURE);
    }

    // ORDERINDEX
    public Long getORDERINDEX()
    {
        return ORDERINDEX;
    }
    public void setORDERINDEX(Long ORDERINDEX)
    {
        this.ORDERINDEX = ORDERINDEX;
        this.DATAMAP.put("ORDERINDEX", ORDERINDEX);
    }

    // FEEDINGRATE
    public Double getFEEDINGRATE()
    {
        return FEEDINGRATE;
    }
    public void setFEEDINGRATE(Double FEEDINGRATE)
    {
        this.FEEDINGRATE = FEEDINGRATE;
        this.DATAMAP.put("FEEDINGRATE", FEEDINGRATE);
    }

    // CTPFLAG
    public String getCTPFLAG()
    {
        return CTPFLAG;
    }
    public void setCTPFLAG(String CTPFLAG)
    {
        this.CTPFLAG = CTPFLAG;
        this.DATAMAP.put("CTPFLAG", CTPFLAG);
    }

    // AUTOTRACKINGFLAG
    public String getAUTOTRACKINGFLAG()
    {
        return AUTOTRACKINGFLAG;
    }
    public void setAUTOTRACKINGFLAG(String AUTOTRACKINGFLAG)
    {
        this.AUTOTRACKINGFLAG = AUTOTRACKINGFLAG;
        this.DATAMAP.put("AUTOTRACKINGFLAG", AUTOTRACKINGFLAG);
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

    // STANDARDVALUE
    public Long getSTANDARDVALUE()
    {
        return STANDARDVALUE;
    }
    public void setSTANDARDVALUE(Long STANDARDVALUE)
    {
        this.STANDARDVALUE = STANDARDVALUE;
        this.DATAMAP.put("STANDARDVALUE", STANDARDVALUE);
    }

    // CONSUMABLEVALUE
    public Double getCONSUMABLEVALUE()
    {
        return CONSUMABLEVALUE;
    }
    public void setCONSUMABLEVALUE(Double CONSUMABLEVALUE)
    {
        this.CONSUMABLEVALUE = CONSUMABLEVALUE;
        this.DATAMAP.put("CONSUMABLEVALUE", CONSUMABLEVALUE);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTID == null || this.BOMID == null || this.BOMVERSION == null || this.POLICYNAME == null || this.POLICYVALUE == null || this.CONDITIONTYPE == null || this.CONDITIONID == null || this.TIMEKEY == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTID, BOMID, BOMVERSION, POLICYNAME, POLICYVALUE, CONDITIONTYPE, CONDITIONID, TIMEKEY"});
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
