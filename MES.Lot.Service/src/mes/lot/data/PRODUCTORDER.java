package mes.lot.data;

import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class PRODUCTORDER
{
    // Key Info
    private String PLANTID;
    private String PRODUCTORDERID;

    // Data Info
    private String PRODUCTID;
    private String PLANSTARTDATE;
    private String PLANENDDATE;
    private String PLANORDERQTY;
    private String PRODUCTUNIT;
    private String WORKORDERQTY;
    private String VENDOR;
    private String DUEDATE;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public PRODUCTORDER() { }

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

    // PRODUCTORDERID
    public String getKeyPRODUCTORDERID()
    {
        return PRODUCTORDERID;
    }
    public void setKeyPRODUCTORDERID(String PRODUCTORDERID)
    {
        this.PRODUCTORDERID = PRODUCTORDERID;
        this.KEYMAP.put("PRODUCTORDERID", PRODUCTORDERID);
    }


    // Data Methods
    // PRODUCTID
    public String getPRODUCTID()
    {
        return PRODUCTID;
    }
    public void setPRODUCTID(String PRODUCTID)
    {
        this.PRODUCTID = PRODUCTID;
        this.DATAMAP.put("PRODUCTID", PRODUCTID);
    }

    // PLANSTARTDATE
    public String getPLANSTARTDATE()
    {
        return PLANSTARTDATE;
    }
    public void setPLANSTARTDATE(String PLANSTARTDATE)
    {
        this.PLANSTARTDATE = PLANSTARTDATE;
        this.DATAMAP.put("PLANSTARTDATE", PLANSTARTDATE);
    }

    // PLANENDDATE
    public String getPLANENDDATE()
    {
        return PLANENDDATE;
    }
    public void setPLANENDDATE(String PLANENDDATE)
    {
        this.PLANENDDATE = PLANENDDATE;
        this.DATAMAP.put("PLANENDDATE", PLANENDDATE);
    }

    // PLANORDERQTY
    public String getPLANORDERQTY()
    {
        return PLANORDERQTY;
    }
    public void setPLANORDERQTY(String PLANORDERQTY)
    {
        this.PLANORDERQTY = PLANORDERQTY;
        this.DATAMAP.put("PLANORDERQTY", PLANORDERQTY);
    }

    // PRODUCTUNIT
    public String getPRODUCTUNIT()
    {
        return PRODUCTUNIT;
    }
    public void setPRODUCTUNIT(String PRODUCTUNIT)
    {
        this.PRODUCTUNIT = PRODUCTUNIT;
        this.DATAMAP.put("PRODUCTUNIT", PRODUCTUNIT);
    }

    // WORKORDERQTY
    public String getWORKORDERQTY()
    {
        return WORKORDERQTY;
    }
    public void setWORKORDERQTY(String WORKORDERQTY)
    {
        this.WORKORDERQTY = WORKORDERQTY;
        this.DATAMAP.put("WORKORDERQTY", WORKORDERQTY);
    }

    // VENDOR
    public String getVENDOR()
    {
        return VENDOR;
    }
    public void setVENDOR(String VENDOR)
    {
        this.VENDOR = VENDOR;
        this.DATAMAP.put("VENDOR", VENDOR);
    }

    // DUEDATE
    public String getDUEDATE()
    {
        return DUEDATE;
    }
    public void setDUEDATE(String DUEDATE)
    {
        this.DUEDATE = DUEDATE;
        this.DATAMAP.put("DUEDATE", DUEDATE);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTORDERID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTORDERID"});
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