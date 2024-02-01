package mes.master.data;

import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.02.07 
 * 
 * @see
 */
public class EQUIPMENTWORKGROUPMAP
{
    // Key Info
    private String PLANTID;
    private String WORKINGDATE;
    private String WORKINGGROUPID;
    private String EQUIPMENTID;
    private String WORKGROUPID;

    // Data Info
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public EQUIPMENTWORKGROUPMAP() { }

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

    // WORKINGDATE
    public String getKeyWORKINGDATE()
    {
        return WORKINGDATE;
    }
    public void setKeyWORKINGDATE(String WORKINGDATE)
    {
        this.WORKINGDATE = WORKINGDATE;
        this.KEYMAP.put("WORKINGDATE", WORKINGDATE);
    }

    // WORKINGGROUPID
    public String getKeyWORKINGGROUPID()
    {
        return WORKINGGROUPID;
    }
    public void setKeyWORKINGGROUPID(String WORKINGGROUPID)
    {
        this.WORKINGGROUPID = WORKINGGROUPID;
        this.KEYMAP.put("WORKINGGROUPID", WORKINGGROUPID);
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

    // WORKGROUPID
    public String getKeyWORKGROUPID()
    {
        return WORKGROUPID;
    }
    public void setKeyWORKGROUPID(String WORKGROUPID)
    {
        this.WORKGROUPID = WORKGROUPID;
        this.KEYMAP.put("WORKGROUPID", WORKGROUPID);
    }


    // Data Methods
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.WORKINGDATE == null || this.WORKINGGROUPID == null || this.EQUIPMENTID == null || this.WORKGROUPID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, WORKINGDATE, WORKINGGROUPID, EQUIPMENTID, WORKGROUPID"});
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