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
public class PLANT
{
    // Key Info
    private String PLANTID;

    // Data Info
    private String PLANTTYPE;
    private String PLANTNAME;
    private String RESOURCESTATE;
    private String SUPERPLANTID;
    private String DESCRIPTION;
    private String REPRESENTATIVECHAR;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public PLANT() { }

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


    // Data Methods
    // PLANTTYPE
    public String getPLANTTYPE()
    {
        return PLANTTYPE;
    }
    public void setPLANTTYPE(String PLANTTYPE)
    {
        this.PLANTTYPE = PLANTTYPE;
        this.DATAMAP.put("PLANTTYPE", PLANTTYPE);
    }

    // PLANTNAME
    public String getPLANTNAME()
    {
        return PLANTNAME;
    }
    public void setPLANTNAME(String PLANTNAME)
    {
        this.PLANTNAME = PLANTNAME;
        this.DATAMAP.put("PLANTNAME", PLANTNAME);
    }

    // RESOURCESTATE
    public String getRESOURCESTATE()
    {
        return RESOURCESTATE;
    }
    public void setRESOURCESTATE(String RESOURCESTATE)
    {
        this.RESOURCESTATE = RESOURCESTATE;
        this.DATAMAP.put("RESOURCESTATE", RESOURCESTATE);
    }

    // SUPERPLANTID
    public String getSUPERPLANTID()
    {
        return SUPERPLANTID;
    }
    public void setSUPERPLANTID(String SUPERPLANTID)
    {
        this.SUPERPLANTID = SUPERPLANTID;
        this.DATAMAP.put("SUPERPLANTID", SUPERPLANTID);
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


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID"});
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
