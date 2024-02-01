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
public class STATEDEFINITION
{
    // Key Info
	private String PLANTID;
    private String STATEOBJECT;
    private String STATE;

    // Data Info
    private String STATENAME;
    private String TABLENAME;
    private String COLUMNNAME;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public STATEDEFINITION() { }

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
    
    // STATEOBJECT
    public String getKeySTATEOBJECT()
    {
        return STATEOBJECT;
    }
    public void setKeySTATEOBJECT(String STATEOBJECT)
    {
        this.STATEOBJECT = STATEOBJECT;
        this.KEYMAP.put("STATEOBJECT", STATEOBJECT);
    }

    // STATE
    public String getKeySTATE()
    {
        return STATE;
    }
    public void setKeySTATE(String STATE)
    {
        this.STATE = STATE;
        this.KEYMAP.put("STATE", STATE);
    }


    // Data Methods
    // STATENAME
    public String getSTATENAME()
    {
        return STATENAME;
    }
    public void setSTATENAME(String STATENAME)
    {
        this.STATENAME = STATENAME;
        this.DATAMAP.put("STATENAME", STATENAME);
    }
    
    // TABLENAME
    public String getTABLENAME()
    {
        return TABLENAME;
    }
    public void setTABLENAME(String TABLENAME)
    {
        this.TABLENAME = TABLENAME;
        this.DATAMAP.put("TABLENAME", TABLENAME);
    }

    // COLUMNNAME
    public String getCOLUMNNAME()
    {
        return COLUMNNAME;
    }
    public void setCOLUMNNAME(String COLUMNNAME)
    {
        this.COLUMNNAME = COLUMNNAME;
        this.DATAMAP.put("COLUMNNAME", COLUMNNAME);
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
        if (this.KEYMAP.isEmpty() || this.STATEOBJECT == null || this.STATE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"STATEOBJECT, STATE"});
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
