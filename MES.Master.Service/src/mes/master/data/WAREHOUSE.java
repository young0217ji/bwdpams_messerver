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
public class WAREHOUSE
{
    // Key Info
    private String PLANTID;
    private String WAREHOUSEID;

    // Data Info
    private String WAREHOUSENAME;
    private String WAREHOUSETYPE;
    private String STOCKCHECKFLAG;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public WAREHOUSE() { }

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

    // WAREHOUSEID
    public String getKeyWAREHOUSEID()
    {
        return WAREHOUSEID;
    }
    public void setKeyWAREHOUSEID(String WAREHOUSEID)
    {
        this.WAREHOUSEID = WAREHOUSEID;
        this.KEYMAP.put("WAREHOUSEID", WAREHOUSEID);
    }


    // Data Methods
    // WAREHOUSENAME
    public String getWAREHOUSENAME()
    {
        return WAREHOUSENAME;
    }
    public void setWAREHOUSENAME(String WAREHOUSENAME)
    {
        this.WAREHOUSENAME = WAREHOUSENAME;
        this.DATAMAP.put("WAREHOUSENAME", WAREHOUSENAME);
    }

    // WAREHOUSETYPE
    public String getWAREHOUSETYPE()
    {
        return WAREHOUSETYPE;
    }
    public void setWAREHOUSETYPE(String WAREHOUSETYPE)
    {
        this.WAREHOUSETYPE = WAREHOUSETYPE;
        this.DATAMAP.put("WAREHOUSETYPE", WAREHOUSETYPE);
    }

    // STOCKCHECKFLAG
    public String getSTOCKCHECKFLAG()
    {
        return STOCKCHECKFLAG;
    }
    public void setSTOCKCHECKFLAG(String STOCKCHECKFLAG)
    {
        this.STOCKCHECKFLAG = STOCKCHECKFLAG;
        this.DATAMAP.put("STOCKCHECKFLAG", STOCKCHECKFLAG);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.WAREHOUSEID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, WAREHOUSEID"});
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
