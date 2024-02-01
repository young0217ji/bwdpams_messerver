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
public class CUSTOMEXCEPTION
{
    // Key Info
    private String EXCEPTIONID;
    private String LOCALEID;

    // Data Info
    private String EXCEPTIONMESSAGE;
    private String EXCEPTIONTYPE;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public CUSTOMEXCEPTION()
    {
    }
    
    // Key Methods
    // EXCEPTIONID
    public String getKeyEXCEPTIONID()
    {
        return EXCEPTIONID;
    }
    public void setKeyEXCEPTIONID(String EXCEPTIONID)
    {
        this.EXCEPTIONID = EXCEPTIONID;
        this.KEYMAP.put("EXCEPTIONID", EXCEPTIONID);
    }

    // LOCALEID
    public String getKeyLOCALEID()
    {
        return LOCALEID;
    }
    public void setKeyLOCALEID(String LOCALEID)
    {
        this.LOCALEID = LOCALEID;
        this.KEYMAP.put("LOCALEID", LOCALEID);
    }


    // Data Methods
    // EXCEPTIONMESSAGE
    public String getEXCEPTIONMESSAGE()
    {
        return EXCEPTIONMESSAGE;
    }
    public void setEXCEPTIONMESSAGE(String EXCEPTIONMESSAGE)
    {
        this.EXCEPTIONMESSAGE = EXCEPTIONMESSAGE;
        this.DATAMAP.put("EXCEPTIONMESSAGE", EXCEPTIONMESSAGE);
    }

    // EXCEPTIONTYPE
    public String getEXCEPTIONTYPE()
    {
        return EXCEPTIONTYPE;
    }
    public void setEXCEPTIONTYPE(String EXCEPTIONTYPE)
    {
        this.EXCEPTIONTYPE = EXCEPTIONTYPE;
        this.DATAMAP.put("EXCEPTIONTYPE", EXCEPTIONTYPE);
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
        if ( this.KEYMAP.isEmpty() || this.EXCEPTIONID == null || this.LOCALEID == null )
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"EXCEPTIONID, LOCALEID"});
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
