package mes.lot.data;

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
public class SUBLOTDATA
{
    // Key Info
	private String SUBLOTID;

    // Data Info
	private String LOTID;
    private Long LOCATIONID;
    private String STATUS;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public SUBLOTDATA() { }

    // Key Methods
    // SUBLOTID
    public String getKeySUBLOTID()
    {
        return SUBLOTID;
    }
    public void setKeySUBLOTID(String SUBLOTID)
    {
        this.SUBLOTID = SUBLOTID;
        this.KEYMAP.put("SUBLOTID", SUBLOTID);
    }


    // Data Methods
    // LOTID
    public String getLOTID()
    {
        return LOTID;
    }
    public void setLOTID(String LOTID)
    {
        this.LOTID = LOTID;
        this.DATAMAP.put("LOTID", LOTID);
    }

    // LOCATIONID
    public Long getLOCATIONID()
    {
        return LOCATIONID;
    }
    public void setLOCATIONID(Long LOCATIONID)
    {
        this.LOCATIONID = LOCATIONID;
        this.DATAMAP.put("LOCATIONID", LOCATIONID);
    }

    // STATUS
    public String getSTATUS()
    {
        return STATUS;
    }
    public void setSTATUS(String STATUS)
    {
        this.STATUS = STATUS;
        this.DATAMAP.put("STATUS", STATUS);
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
        if (this.KEYMAP.isEmpty() || this.LOTID == null || this.LOCATIONID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"LOTID, LOCATIONID"});
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
