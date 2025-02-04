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
public class SPAREPARTDEFINITION
{
    // Key Info
    private String PLANTID;
    private String SPAREPARTID;

    // Data Info
    private String SPAREPARTNAME;
    private String SPAREPARTTYPE;
    private Long SAFESTOCK;
    private String UNIT;
    private String MAINVENDOR;
    private String MAKER;
    private String ENGINEER;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public SPAREPARTDEFINITION() { }

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

    // SPAREPARTID
    public String getKeySPAREPARTID()
    {
        return SPAREPARTID;
    }
    public void setKeySPAREPARTID(String SPAREPARTID)
    {
        this.SPAREPARTID = SPAREPARTID;
        this.KEYMAP.put("SPAREPARTID", SPAREPARTID);
    }


    // Data Methods
    // SPAREPARTNAME
    public String getSPAREPARTNAME()
    {
        return SPAREPARTNAME;
    }
    public void setSPAREPARTNAME(String SPAREPARTNAME)
    {
        this.SPAREPARTNAME = SPAREPARTNAME;
        this.DATAMAP.put("SPAREPARTNAME", SPAREPARTNAME);
    }

    // SPAREPARTTYPE
    public String getSPAREPARTTYPE()
    {
        return SPAREPARTTYPE;
    }
    public void setSPAREPARTTYPE(String SPAREPARTTYPE)
    {
        this.SPAREPARTTYPE = SPAREPARTTYPE;
        this.DATAMAP.put("SPAREPARTTYPE", SPAREPARTTYPE);
    }

    // SAFESTOCK
    public Long getSAFESTOCK()
    {
        return SAFESTOCK;
    }
    public void setSAFESTOCK(Long SAFESTOCK)
    {
        this.SAFESTOCK = SAFESTOCK;
        this.DATAMAP.put("SAFESTOCK", SAFESTOCK);
    }

    // UNIT
    public String getUNIT()
    {
        return UNIT;
    }
    public void setUNIT(String UNIT)
    {
        this.UNIT = UNIT;
        this.DATAMAP.put("UNIT", UNIT);
    }
    
    // MAINVENDOR
    public String getMAINVENDOR()
    {
        return MAINVENDOR;
    }
    public void setMAINVENDOR(String MAINVENDOR)
    {
        this.MAINVENDOR = MAINVENDOR;
        this.DATAMAP.put("MAINVENDOR", MAINVENDOR);
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

    // ENGINEER
    public String getENGINEER()
    {
        return ENGINEER;
    }
    public void setENGINEER(String ENGINEER)
    {
        this.ENGINEER = ENGINEER;
        this.DATAMAP.put("ENGINEER", ENGINEER);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.SPAREPARTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, SPAREPARTID"});
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
