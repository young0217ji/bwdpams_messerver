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
public class BOMDETAIL
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;
    private String BOMID;
    private String BOMVERSION;
    private Long BOMINDEX;

    // Data Info
    private String CONSUMABLEID;
    private Double CONSUMABLEVALUE;
    private String CONSUMABLEUNIT;
    private String CONSUMABLEACCOUNT;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public BOMDETAIL() { }

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

    // PRODUCTID
    public String getKeyPRODUCTID()
    {
        return PRODUCTID;
    }
    public void setKeyPRODUCTID(String PRODUCTID)
    {
        this.PRODUCTID = PRODUCTID;
        this.KEYMAP.put("PRODUCTID", PRODUCTID);
    }

    // BOMID
    public String getKeyBOMID()
    {
        return BOMID;
    }
    public void setKeyBOMID(String BOMID)
    {
        this.BOMID = BOMID;
        this.KEYMAP.put("BOMID", BOMID);
    }

    // BOMVERSION
    public String getKeyBOMVERSION()
    {
        return BOMVERSION;
    }
    public void setKeyBOMVERSION(String BOMVERSION)
    {
        this.BOMVERSION = BOMVERSION;
        this.KEYMAP.put("BOMVERSION", BOMVERSION);
    }

    // BOMINDEX
    public Long getKeyBOMINDEX()
    {
        return BOMINDEX;
    }
    public void setKeyBOMINDEX(Long BOMINDEX)
    {
        this.BOMINDEX = BOMINDEX;
        this.KEYMAP.put("BOMINDEX", BOMINDEX);
    }


    // Data Methods
    // CONSUMABLEID
    public String getCONSUMABLEID()
    {
        return CONSUMABLEID;
    }
    public void setCONSUMABLEID(String CONSUMABLEID)
    {
        this.CONSUMABLEID = CONSUMABLEID;
        this.DATAMAP.put("CONSUMABLEID", CONSUMABLEID);
    }

    // CONSUMABLEVALUE
    public Double getCONSUMABLEVALUE()
    {
        return CONSUMABLEVALUE;
    }
    public void setCONSUMABLEVALUE(Double CONSUMABLEVALUE)
    {
        this.CONSUMABLEVALUE = CONSUMABLEVALUE;
        this.DATAMAP.put("CONSUMABLEVALUE", CONSUMABLEVALUE);
    }

    // CONSUMABLEUNIT
    public String getCONSUMABLEUNIT()
    {
        return CONSUMABLEUNIT;
    }
    public void setCONSUMABLEUNIT(String CONSUMABLEUNIT)
    {
        this.CONSUMABLEUNIT = CONSUMABLEUNIT;
        this.DATAMAP.put("CONSUMABLEUNIT", CONSUMABLEUNIT);
    }

    // CONSUMABLEACCOUNT
    public String getCONSUMABLEACCOUNT()
    {
        return CONSUMABLEACCOUNT;
    }
    public void setCONSUMABLEACCOUNT(String CONSUMABLEACCOUNT)
    {
        this.CONSUMABLEACCOUNT = CONSUMABLEACCOUNT;
        this.DATAMAP.put("CONSUMABLEACCOUNT", CONSUMABLEACCOUNT);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTID == null || this.BOMID == null || this.BOMVERSION == null || this.BOMINDEX == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTID, BOMID, BOMVERSION, BOMINDEX"});
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
