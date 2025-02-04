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
public class GRIDDEFINITION
{
    // Key Info
    private String PLANTID;
    private String GRIDID;

    // Data Info
    private String GRIDNAME;
    private String CHECKBOX;
    private String ROWSTATUS;
    private Long HEADERROWSIZE;
    private Long HEADERCOLUMNSIZE;
    private Long POSITION;
    private String CLASSNAME;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public GRIDDEFINITION() { }

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

    // GRIDID
    public String getKeyGRIDID()
    {
        return GRIDID;
    }
    public void setKeyGRIDID(String GRIDID)
    {
        this.GRIDID = GRIDID;
        this.KEYMAP.put("GRIDID", GRIDID);
    }


    // Data Methods
    // GRIDNAME
    public String getGRIDNAME()
    {
        return GRIDNAME;
    }
    public void setGRIDNAME(String GRIDNAME)
    {
        this.GRIDNAME = GRIDNAME;
        this.DATAMAP.put("GRIDNAME", GRIDNAME);
    }

    // CHECKBOX
    public String getCHECKBOX()
    {
        return CHECKBOX;
    }
    public void setCHECKBOX(String CHECKBOX)
    {
        this.CHECKBOX = CHECKBOX;
        this.DATAMAP.put("CHECKBOX", CHECKBOX);
    }

    // ROWSTATUS
    public String getROWSTATUS()
    {
        return ROWSTATUS;
    }
    public void setROWSTATUS(String ROWSTATUS)
    {
        this.ROWSTATUS = ROWSTATUS;
        this.DATAMAP.put("ROWSTATUS", ROWSTATUS);
    }

    // HEADERROWSIZE
    public Long getHEADERROWSIZE()
    {
        return HEADERROWSIZE;
    }
    public void setHEADERROWSIZE(Long HEADERROWSIZE)
    {
        this.HEADERROWSIZE = HEADERROWSIZE;
        this.DATAMAP.put("HEADERROWSIZE", HEADERROWSIZE);
    }

    // HEADERCOLUMNSIZE
    public Long getHEADERCOLUMNSIZE()
    {
        return HEADERCOLUMNSIZE;
    }
    public void setHEADERCOLUMNSIZE(Long HEADERCOLUMNSIZE)
    {
        this.HEADERCOLUMNSIZE = HEADERCOLUMNSIZE;
        this.DATAMAP.put("HEADERCOLUMNSIZE", HEADERCOLUMNSIZE);
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

    // CLASSNAME
    public String getCLASSNAME()
    {
        return CLASSNAME;
    }
    public void setCLASSNAME(String CLASSNAME)
    {
        this.CLASSNAME = CLASSNAME;
        this.DATAMAP.put("CLASSNAME", CLASSNAME);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.GRIDID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, GRIDID"});
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
