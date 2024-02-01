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
public class EQUIPMENTBOM
{
    // Key Info
    private String PLANTID;
    private String EQUIPMENTID;
    private Long BOMINDEX;

    // Data Info
    private String BOMINDEXTYPE;
    private String PARTID;
    private String PARTTYPE;
    private Long QUANTITY;
    private String UNIT;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public EQUIPMENTBOM() { }

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
    // BOMINDEXTYPE
    public String getBOMINDEXTYPE()
    {
        return BOMINDEXTYPE;
    }
    public void setBOMINDEXTYPE(String BOMINDEXTYPE)
    {
        this.BOMINDEXTYPE = BOMINDEXTYPE;
        this.DATAMAP.put("BOMINDEXTYPE", BOMINDEXTYPE);
    }

    // PARTID
    public String getPARTID()
    {
        return PARTID;
    }
    public void setPARTID(String PARTID)
    {
        this.PARTID = PARTID;
        this.DATAMAP.put("PARTID", PARTID);
    }

    // PARTTYPE
    public String getPARTTYPE()
    {
        return PARTTYPE;
    }
    public void setPARTTYPE(String PARTTYPE)
    {
        this.PARTTYPE = PARTTYPE;
        this.DATAMAP.put("PARTTYPE", PARTTYPE);
    }

    // QUANTITY
    public Long getQUANTITY()
    {
        return QUANTITY;
    }
    public void setQUANTITY(Long QUANTITY)
    {
        this.QUANTITY = QUANTITY;
        this.DATAMAP.put("QUANTITY", QUANTITY);
    }

    // UNIT
    public String getUNIT()
    {
        return UNIT;
    }
    public void setUNIT(String UNIT)
    {
        this.UNIT = UNIT;
        this.DATAMAP.put("UNIT", UNIT);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.EQUIPMENTID == null || this.BOMINDEX == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, EQUIPMENTID, BOMINDEX"});
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
