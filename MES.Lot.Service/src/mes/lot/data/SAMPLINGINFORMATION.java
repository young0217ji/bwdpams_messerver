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
public class SAMPLINGINFORMATION
{
    // Key Info
    private String SAMPLINGID;
    private Long CHECKVALUE;

    // Data Info
    private String SAMPLINGTYPE;
    private String SAMPLINGMETHOD;
    private String POLICYTYPE;
    private String POLICYCODE;
    private String PROCESSID;
    private String EQUIPMENTID;
    private Long CHECKEDCOUNT;
    private String LASTCHECKLOTID;
    private Timestamp LASTCHECKTIME;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public SAMPLINGINFORMATION() { }

    // Key Methods
    // SAMPLINGID
    public String getKeySAMPLINGID()
    {
        return SAMPLINGID;
    }
    public void setKeySAMPLINGID(String SAMPLINGID)
    {
        this.SAMPLINGID = SAMPLINGID;
        this.KEYMAP.put("SAMPLINGID", SAMPLINGID);
    }

    // CHECKVALUE
    public Long getKeyCHECKVALUE()
    {
        return CHECKVALUE;
    }
    public void setKeyCHECKVALUE(Long CHECKVALUE)
    {
        this.CHECKVALUE = CHECKVALUE;
        this.KEYMAP.put("CHECKVALUE", CHECKVALUE);
    }


    // Data Methods
    // SAMPLINGTYPE
    public String getSAMPLINGTYPE()
    {
        return SAMPLINGTYPE;
    }
    public void setSAMPLINGTYPE(String SAMPLINGTYPE)
    {
        this.SAMPLINGTYPE = SAMPLINGTYPE;
        this.DATAMAP.put("SAMPLINGTYPE", SAMPLINGTYPE);
    }

    // SAMPLINGMETHOD
    public String getSAMPLINGMETHOD()
    {
        return SAMPLINGMETHOD;
    }
    public void setSAMPLINGMETHOD(String SAMPLINGMETHOD)
    {
        this.SAMPLINGMETHOD = SAMPLINGMETHOD;
        this.DATAMAP.put("SAMPLINGMETHOD", SAMPLINGMETHOD);
    }

    // POLICYTYPE
    public String getPOLICYTYPE()
    {
        return POLICYTYPE;
    }
    public void setPOLICYTYPE(String POLICYTYPE)
    {
        this.POLICYTYPE = POLICYTYPE;
        this.DATAMAP.put("POLICYTYPE", POLICYTYPE);
    }

    // POLICYCODE
    public String getPOLICYCODE()
    {
        return POLICYCODE;
    }
    public void setPOLICYCODE(String POLICYCODE)
    {
        this.POLICYCODE = POLICYCODE;
        this.DATAMAP.put("POLICYCODE", POLICYCODE);
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

    // CHECKEDCOUNT
    public Long getCHECKEDCOUNT()
    {
        return CHECKEDCOUNT;
    }
    public void setCHECKEDCOUNT(Long CHECKEDCOUNT)
    {
        this.CHECKEDCOUNT = CHECKEDCOUNT;
        this.DATAMAP.put("CHECKEDCOUNT", CHECKEDCOUNT);
    }

    // LASTCHECKLOTID
    public String getLASTCHECKLOTID()
    {
        return LASTCHECKLOTID;
    }
    public void setLASTCHECKLOTID(String LASTCHECKLOTID)
    {
        this.LASTCHECKLOTID = LASTCHECKLOTID;
        this.DATAMAP.put("LASTCHECKLOTID", LASTCHECKLOTID);
    }

    // LASTCHECKTIME
    public Timestamp getLASTCHECKTIME()
    {
        return LASTCHECKTIME;
    }
    public void setLASTCHECKTIME(Timestamp LASTCHECKTIME)
    {
        this.LASTCHECKTIME = LASTCHECKTIME;
        this.DATAMAP.put("LASTCHECKTIME", LASTCHECKTIME);
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
        if (this.KEYMAP.isEmpty() || this.SAMPLINGID == null || this.CHECKVALUE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"SAMPLINGID, CHECKVALUE"});
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