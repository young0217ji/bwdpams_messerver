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
public class SAMPLINGPOLICY
{
    // Key Info
	private String PLANTID;
    private String POLICYCODE;
    private String PROCESSID;
    private String EQUIPMENTID;

    // Data Info
    private String POLICYTYPE;
    private String SAMPLINGID;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public SAMPLINGPOLICY() { }

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
    
    // POLICYCODE
    public String getKeyPOLICYCODE()
    {
        return POLICYCODE;
    }
    public void setKeyPOLICYCODE(String POLICYCODE)
    {
        this.POLICYCODE = POLICYCODE;
        this.KEYMAP.put("POLICYCODE", POLICYCODE);
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

    // SAMPLINGID
    public String getSAMPLINGID()
    {
        return SAMPLINGID;
    }
    public void setSAMPLINGID(String SAMPLINGID)
    {
        this.SAMPLINGID = SAMPLINGID;
        this.DATAMAP.put("SAMPLINGID", SAMPLINGID);
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
        if (this.KEYMAP.isEmpty() || this.POLICYCODE == null || this.PROCESSID == null || this.EQUIPMENTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"POLICYCODE, PROCESSID, EQUIPMENTID"});
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
