package mes.lot.data;

import java.sql.Timestamp;
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
public class PACKINGLIST
{
    // Key Info
    private String PLANTID;
    private String LOTID;
    private String PACKINGID;

    // Data Info
    private String PACKINGSTATUS;
    private Double PACKINGQUANTITY;
    private Timestamp PACKINGTIME;
    private String PACKINGUSERID;
    private String MATERIALID;
    private String MATERIALTYPE;
    private Double MATERIALQUANTITY;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public PACKINGLIST() { }

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

    // PACKINGID
    public String getKeyPACKINGID()
    {
        return PACKINGID;
    }
    public void setKeyPACKINGID(String PACKINGID)
    {
        this.PACKINGID = PACKINGID;
        this.KEYMAP.put("PACKINGID", PACKINGID);
    }


    // Data Methods
    // PACKINGSTATUS
    public String getPACKINGSTATUS()
    {
        return PACKINGSTATUS;
    }
    public void setPACKINGSTATUS(String PACKINGSTATUS)
    {
        this.PACKINGSTATUS = PACKINGSTATUS;
        this.DATAMAP.put("PACKINGSTATUS", PACKINGSTATUS);
    }

    // PACKINGQUANTITY
    public Double getPACKINGQUANTITY()
    {
        return PACKINGQUANTITY;
    }
    public void setPACKINGQUANTITY(Double PACKINGQUANTITY)
    {
        this.PACKINGQUANTITY = PACKINGQUANTITY;
        this.DATAMAP.put("PACKINGQUANTITY", PACKINGQUANTITY);
    }

    // PACKINGTIME
    public Timestamp getPACKINGTIME()
    {
        return PACKINGTIME;
    }
    public void setPACKINGTIME(Timestamp PACKINGTIME)
    {
        this.PACKINGTIME = PACKINGTIME;
        this.DATAMAP.put("PACKINGTIME", PACKINGTIME);
    }

    // PACKINGUSERID
    public String getPACKINGUSERID()
    {
        return PACKINGUSERID;
    }
    public void setPACKINGUSERID(String PACKINGUSERID)
    {
        this.PACKINGUSERID = PACKINGUSERID;
        this.DATAMAP.put("PACKINGUSERID", PACKINGUSERID);
    }

    // MATERIALID
    public String getMATERIALID()
    {
        return MATERIALID;
    }
    public void setMATERIALID(String MATERIALID)
    {
        this.MATERIALID = MATERIALID;
        this.DATAMAP.put("MATERIALID", MATERIALID);
    }

    // MATERIALTYPE
    public String getMATERIALTYPE()
    {
        return MATERIALTYPE;
    }
    public void setMATERIALTYPE(String MATERIALTYPE)
    {
        this.MATERIALTYPE = MATERIALTYPE;
        this.DATAMAP.put("MATERIALTYPE", MATERIALTYPE);
    }

    // MATERIALQUANTITY
    public Double getMATERIALQUANTITY()
    {
        return MATERIALQUANTITY;
    }
    public void setMATERIALQUANTITY(Double MATERIALQUANTITY)
    {
        this.MATERIALQUANTITY = MATERIALQUANTITY;
        this.DATAMAP.put("MATERIALQUANTITY", MATERIALQUANTITY);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.LOTID == null || this.PACKINGID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, LOTID, PACKINGID"});
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
