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
public class STATECHANGEPOLICY
{
    // Key Info
	private String PLANTID;
    private String STATEOBJECT;
    private String RESOURCESTATE;
    private String CHANGESTATE;
   
    // Data Info
    private String DESCRIPTION;
    private String REASONCODETYPE;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public STATECHANGEPOLICY() { }

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

    // RESOURCESTATE
    public String getKeyRESOURCESTATE()
    {
        return RESOURCESTATE;
    }
    public void setKeyRESOURCESTATE(String RESOURCESTATE)
    {
        this.RESOURCESTATE = RESOURCESTATE;
        this.KEYMAP.put("RESOURCESTATE", RESOURCESTATE);
    }

    // CHANGESTATE
    public String getKeyCHANGESTATE()
    {
        return CHANGESTATE;
    }
    public void setKeyCHANGESTATE(String CHANGESTATE)
    {
        this.CHANGESTATE = CHANGESTATE;
        this.KEYMAP.put("CHANGESTATE", CHANGESTATE);
    }
    
    // Data Methods
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
        if (this.KEYMAP.isEmpty() || this.STATEOBJECT == null || this.RESOURCESTATE == null || this.CHANGESTATE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"STATEOBJECT, RESOURCESTATE, CHANGESTATE"});
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
