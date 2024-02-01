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
public class REASONCODE
{
    // Key Info
    private String PLANTID;
    private String REASONCODETYPE;
    private String REASONCODE;

    // Data Info
    private String DESCRIPTION;
    private String SUPERREASONCODE;
    private Long LEVELNO;
    private String AVAILABILITY;
    private Long POSITION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public REASONCODE() { }

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

    // REASONCODETYPE
    public String getKeyREASONCODETYPE()
    {
        return REASONCODETYPE;
    }
    public void setKeyREASONCODETYPE(String REASONCODETYPE)
    {
        this.REASONCODETYPE = REASONCODETYPE;
        this.KEYMAP.put("REASONCODETYPE", REASONCODETYPE);
    }

    // REASONCODE
    public String getKeyREASONCODE()
    {
        return REASONCODE;
    }
    public void setKeyREASONCODE(String REASONCODE)
    {
        this.REASONCODE = REASONCODE;
        this.KEYMAP.put("REASONCODE", REASONCODE);
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

    // SUPERREASONCODE
    public String getSUPERREASONCODE()
    {
        return SUPERREASONCODE;
    }
    public void setSUPERREASONCODE(String SUPERREASONCODE)
    {
        this.SUPERREASONCODE = SUPERREASONCODE;
        this.DATAMAP.put("SUPERREASONCODE", SUPERREASONCODE);
    }

    // LEVELNO
    public Long getLEVELNO()
    {
        return LEVELNO;
    }
    public void setLEVELNO(Long LEVELNO)
    {
        this.LEVELNO = LEVELNO;
        this.DATAMAP.put("LEVELNO", LEVELNO);
    }

    // AVAILABILITY
    public String getAVAILABILITY()
    {
        return AVAILABILITY;
    }
    public void setAVAILABILITY(String AVAILABILITY)
    {
        this.AVAILABILITY = AVAILABILITY;
        this.DATAMAP.put("AVAILABILITY", AVAILABILITY);
    }

    // POSITION
    public Long getPOSITION()
    {
        return POSITION;
    }
    public void setPOSITION(Long POSITION)
    {
        this.POSITION = POSITION;
        this.DATAMAP.put("POSITION", POSITION);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.REASONCODETYPE == null || this.REASONCODE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, REASONCODETYPE, REASONCODE"});
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
