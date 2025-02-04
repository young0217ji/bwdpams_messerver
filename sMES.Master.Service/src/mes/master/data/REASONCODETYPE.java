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
public class REASONCODETYPE
{
    // Key Info
    private String PLANTID;
    private String REASONCODETYPEID;

    // Data Info
    private String REASONCODETYPENAME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public REASONCODETYPE() { }

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

    // REASONCODETYPEID
    public String getKeyREASONCODETYPEID()
    {
        return REASONCODETYPEID;
    }
    public void setKeyREASONCODETYPEID(String REASONCODETYPEID)
    {
        this.REASONCODETYPEID = REASONCODETYPEID;
        this.KEYMAP.put("REASONCODETYPEID", REASONCODETYPEID);
    }


    // Data Methods
    // REASONCODETYPENAME
    public String getREASONCODETYPENAME()
    {
        return REASONCODETYPENAME;
    }
    public void setREASONCODETYPENAME(String REASONCODETYPENAME)
    {
        this.REASONCODETYPENAME = REASONCODETYPENAME;
        this.DATAMAP.put("REASONCODETYPENAME", REASONCODETYPENAME);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.REASONCODETYPEID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, REASONCODETYPEID"});
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
