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
public class LOTCONSUMABLERATIOHISTORY
{
    // Key Info
    private String LOTID;
    private String TIMEKEY;
    private String CONSUMABLESEQUENCE;

    // Data Info
    private String CONSUMABLEID;
    private String CONSUMABLELOTID;
    private Double CONSUMABLEQUANTITY;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public LOTCONSUMABLERATIOHISTORY() { }

    // Key Methods
    // LOTID
    public String getKeyLOTID()
    {
        return LOTID;
    }
    public void setKeyLOTID(String LOTID)
    {
        this.LOTID = LOTID;
        this.KEYMAP.put("LOTID", LOTID);
    }

    // TIMEKEY
    public String getKeyTIMEKEY()
    {
        return TIMEKEY;
    }
    public void setKeyTIMEKEY(String TIMEKEY)
    {
        this.TIMEKEY = TIMEKEY;
        this.KEYMAP.put("TIMEKEY", TIMEKEY);
    }

    // CONSUMABLESEQUENCE
    public String getKeyCONSUMABLESEQUENCE()
    {
        return CONSUMABLESEQUENCE;
    }
    public void setKeyCONSUMABLESEQUENCE(String CONSUMABLESEQUENCE)
    {
        this.CONSUMABLESEQUENCE = CONSUMABLESEQUENCE;
        this.KEYMAP.put("CONSUMABLESEQUENCE", CONSUMABLESEQUENCE);
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

    // CONSUMABLELOTID
    public String getCONSUMABLELOTID()
    {
        return CONSUMABLELOTID;
    }
    public void setCONSUMABLELOTID(String CONSUMABLELOTID)
    {
        this.CONSUMABLELOTID = CONSUMABLELOTID;
        this.DATAMAP.put("CONSUMABLELOTID", CONSUMABLELOTID);
    }

    // CONSUMABLEQUANTITY
    public Double getCONSUMABLEQUANTITY()
    {
        return CONSUMABLEQUANTITY;
    }
    public void setCONSUMABLEQUANTITY(Double CONSUMABLEQUANTITY)
    {
        this.CONSUMABLEQUANTITY = CONSUMABLEQUANTITY;
        this.DATAMAP.put("CONSUMABLEQUANTITY", CONSUMABLEQUANTITY);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.LOTID == null || this.TIMEKEY == null || this.CONSUMABLESEQUENCE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"LOTID, TIMEKEY, CONSUMABLESEQUENCE"});
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
