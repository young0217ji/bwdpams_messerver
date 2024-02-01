package mes.material.data;

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
public class MATERIALSTOCK
{
    // Key Info
    private String PLANTID;
    private String YYYYMM;
    private String WAREHOUSEID;
    private String MATERIALTYPE;
    private String MATERIALID;
    private String MATERIALLOTID;

    // Data Info
    private String RECEIPTDATE;
    private String VENDOR;
    private String LASTEVENTTIMEKEY;
    private Double OPENINGQTY;
    private Double INQTY;
    private Double BONUSQTY;
    private Double CONSUMEQTY;
    private Double SCRAPQTY;
    private Double OUTQTY;
    private Double HOLDQTY;
    private Double STOCKQTY;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public MATERIALSTOCK() { }

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

    // YYYYMM
    public String getKeyYYYYMM()
    {
        return YYYYMM;
    }
    public void setKeyYYYYMM(String YYYYMM)
    {
        this.YYYYMM = YYYYMM;
        this.KEYMAP.put("YYYYMM", YYYYMM);
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

    // MATERIALTYPE
    public String getKeyMATERIALTYPE()
    {
        return MATERIALTYPE;
    }
    public void setKeyMATERIALTYPE(String MATERIALTYPE)
    {
        this.MATERIALTYPE = MATERIALTYPE;
        this.KEYMAP.put("MATERIALTYPE", MATERIALTYPE);
    }

    // MATERIALID
    public String getKeyMATERIALID()
    {
        return MATERIALID;
    }
    public void setKeyMATERIALID(String MATERIALID)
    {
        this.MATERIALID = MATERIALID;
        this.KEYMAP.put("MATERIALID", MATERIALID);
    }

    // MATERIALLOTID
    public String getKeyMATERIALLOTID()
    {
        return MATERIALLOTID;
    }
    public void setKeyMATERIALLOTID(String MATERIALLOTID)
    {
        this.MATERIALLOTID = MATERIALLOTID;
        this.KEYMAP.put("MATERIALLOTID", MATERIALLOTID);
    }


    // Data Methods
    // RECEIPTDATE
    public String getRECEIPTDATE()
    {
        return RECEIPTDATE;
    }
    public void setRECEIPTDATE(String RECEIPTDATE)
    {
        this.RECEIPTDATE = RECEIPTDATE;
        this.DATAMAP.put("RECEIPTDATE", RECEIPTDATE);
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

    // LASTEVENTTIMEKEY
    public String getLASTEVENTTIMEKEY()
    {
        return LASTEVENTTIMEKEY;
    }
    public void setLASTEVENTTIMEKEY(String LASTEVENTTIMEKEY)
    {
        this.LASTEVENTTIMEKEY = LASTEVENTTIMEKEY;
        this.DATAMAP.put("LASTEVENTTIMEKEY", LASTEVENTTIMEKEY);
    }

    // OPENINGQTY
    public Double getOPENINGQTY()
    {
        return OPENINGQTY;
    }
    public void setOPENINGQTY(Double openingQty)
    {
        this.OPENINGQTY = openingQty;
        this.DATAMAP.put("OPENINGQTY", openingQty);
    }

    // INQTY
    public Double getINQTY()
    {
        return INQTY;
    }
    public void setINQTY(Double INQTY)
    {
        this.INQTY = INQTY;
        this.DATAMAP.put("INQTY", INQTY);
    }

    // BONUSQTY
    public Double getBONUSQTY()
    {
        return BONUSQTY;
    }
    public void setBONUSQTY(Double BONUSQTY)
    {
        this.BONUSQTY = BONUSQTY;
        this.DATAMAP.put("BONUSQTY", BONUSQTY);
    }

    // CONSUMEQTY
    public Double getCONSUMEQTY()
    {
        return CONSUMEQTY;
    }
    public void setCONSUMEQTY(Double CONSUMEQTY)
    {
        this.CONSUMEQTY = CONSUMEQTY;
        this.DATAMAP.put("CONSUMEQTY", CONSUMEQTY);
    }

    // SCRAPQTY
    public Double getSCRAPQTY()
    {
        return SCRAPQTY;
    }
    public void setSCRAPQTY(Double SCRAPQTY)
    {
        this.SCRAPQTY = SCRAPQTY;
        this.DATAMAP.put("SCRAPQTY", SCRAPQTY);
    }

    // OUTQTY
    public Double getOUTQTY()
    {
        return OUTQTY;
    }
    public void setOUTQTY(Double OUTQTY)
    {
        this.OUTQTY = OUTQTY;
        this.DATAMAP.put("OUTQTY", OUTQTY);
    }

    // HOLDQTY
    public Double getHOLDQTY()
    {
        return HOLDQTY;
    }
    public void setHOLDQTY(Double HOLDQTY)
    {
        this.HOLDQTY = HOLDQTY;
        this.DATAMAP.put("HOLDQTY", HOLDQTY);
    }

    // STOCKQTY
    public Double getSTOCKQTY()
    {
        return STOCKQTY;
    }
    public void setSTOCKQTY(Double STOCKQTY)
    {
        this.STOCKQTY = STOCKQTY;
        this.DATAMAP.put("STOCKQTY", STOCKQTY);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.YYYYMM == null || this.WAREHOUSEID == null || this.MATERIALTYPE == null || this.MATERIALID == null || this.MATERIALLOTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, YYYYMM, WAREHOUSEID, MATERIALTYPE, MATERIALID, MATERIALLOTID"});
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
