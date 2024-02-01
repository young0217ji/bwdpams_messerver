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
public class RECIPEPARAMETER
{
    // Key Info
    private String PLANTID;
    private String RECIPEPARAMETERID;

    // Data Info
    private String RECIPEPARAMETERNAME;
    private String RECIPEPARAMETERGROUP;
    private String RECIPEPARAMETERTYPE;
    private String PARAMETERENUMID;
    private String UNITOFMEASURE;
    private String CLASSIFICATION;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String DESCRIPTION;
    private String RECORDCYCLE;
    private String MEASURECYCLE;
    private String INPUTTYPE;
    private String MEASUREGROUP;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public RECIPEPARAMETER() { }

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

    // RECIPEPARAMETERID
    public String getKeyRECIPEPARAMETERID()
    {
        return RECIPEPARAMETERID;
    }
    public void setKeyRECIPEPARAMETERID(String RECIPEPARAMETERID)
    {
        this.RECIPEPARAMETERID = RECIPEPARAMETERID;
        this.KEYMAP.put("RECIPEPARAMETERID", RECIPEPARAMETERID);
    }


    // Data Methods
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

    // RECIPEPARAMETERGROUP
    public String getRECIPEPARAMETERGROUP()
    {
        return RECIPEPARAMETERGROUP;
    }
    public void setRECIPEPARAMETERGROUP(String RECIPEPARAMETERGROUP)
    {
        this.RECIPEPARAMETERGROUP = RECIPEPARAMETERGROUP;
        this.DATAMAP.put("RECIPEPARAMETERGROUP", RECIPEPARAMETERGROUP);
    }

    // RECIPEPARAMETERTYPE
    public String getRECIPEPARAMETERTYPE()
    {
        return RECIPEPARAMETERTYPE;
    }
    public void setRECIPEPARAMETERTYPE(String RECIPEPARAMETERTYPE)
    {
        this.RECIPEPARAMETERTYPE = RECIPEPARAMETERTYPE;
        this.DATAMAP.put("RECIPEPARAMETERTYPE", RECIPEPARAMETERTYPE);
    }

    // PARAMETERENUMID
    public String getPARAMETERENUMID()
    {
        return PARAMETERENUMID;
    }
    public void setPARAMETERENUMID(String PARAMETERENUMID)
    {
        this.PARAMETERENUMID = PARAMETERENUMID;
        this.DATAMAP.put("PARAMETERENUMID", PARAMETERENUMID);
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
    
    // RECORDCYCLE
    public String getRECORDCYCLE()
    {
        return RECORDCYCLE;
    }
    public void setRECORDCYCLE(String RECORDCYCLE)
    {
        this.RECORDCYCLE = RECORDCYCLE;
        this.DATAMAP.put("RECORDCYCLE", RECORDCYCLE);
    }
    
    // MEASURECYCLE
    public String getMEASURECYCLE()
    {
        return MEASURECYCLE;
    }
    public void setMEASURECYCLE(String MEASURECYCLE)
    {
        this.MEASURECYCLE = MEASURECYCLE;
        this.DATAMAP.put("MEASURECYCLE", MEASURECYCLE);
    }
    
    // INPUTTYPE
    public String getINPUTTYPE()
    {
        return INPUTTYPE;
    }
    public void setINPUTTYPE(String INPUTTYPE)
    {
        this.INPUTTYPE = INPUTTYPE;
        this.DATAMAP.put("INPUTTYPE", INPUTTYPE);
    }
    
    // MEASUREGROUP
    public String getMEASUREGROUP()
    {
        return MEASUREGROUP;
    }
    public void setMEASUREGROUP(String MEASUREGROUP)
    {
        this.MEASUREGROUP = MEASUREGROUP;
        this.DATAMAP.put("MEASUREGROUP", MEASUREGROUP);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.RECIPEPARAMETERID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, RECIPEPARAMETERID"});
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
