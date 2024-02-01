package mes.lot.data;

import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class WORKORDER
{
    // Key Info
    private String PLANTID;
    private String PRODUCTORDERID;
    private String WORKORDERID;

    // Data Info
    private String PRODUCTID;
    private String WORKORDERQTY;
    private String WOSTARTDATE;
    private String WOENDDATE;
    private String DESCRIPTION;
    private String PROCESSSTARTFLAG;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public WORKORDER() { }

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

    // WORKORDERID
    public String getKeyWORKORDERID()
    {
        return WORKORDERID;
    }
    public void setKeyWORKORDERID(String WORKORDERID)
    {
        this.WORKORDERID = WORKORDERID;
        this.KEYMAP.put("WORKORDERID", WORKORDERID);
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

    // WOSTARTDATE
    public String getWOSTARTDATE()
    {
        return WOSTARTDATE;
    }
    public void setWOSTARTDATE(String WOSTARTDATE)
    {
        this.WOSTARTDATE = WOSTARTDATE;
        this.DATAMAP.put("WOSTARTDATE", WOSTARTDATE);
    }

    // WOENDDATE
    public String getWOENDDATE()
    {
        return WOENDDATE;
    }
    public void setWOENDDATE(String WOENDDATE)
    {
        this.WOENDDATE = WOENDDATE;
        this.DATAMAP.put("WOENDDATE", WOENDDATE);
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

    // PROCESSSTARTFLAG
    public String getPROCESSSTARTFLAG()
    {
        return PROCESSSTARTFLAG;
    }
    public void setPROCESSSTARTFLAG(String PROCESSSTARTFLAG)
    {
        this.PROCESSSTARTFLAG = PROCESSSTARTFLAG;
        this.DATAMAP.put("PROCESSSTARTFLAG", PROCESSSTARTFLAG);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTORDERID == null || this.WORKORDERID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTORDERID, WORKORDERID"});
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