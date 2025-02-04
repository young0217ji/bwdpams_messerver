package mes.material.data;

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
public class MATERIALSTOCKHISTORY
{
    // Key Info
    private String PLANTID;
    private String YYYYMM;
    private String WAREHOUSEID;
    private String MATERIALTYPE;
    private String MATERIALID;
    private String MATERIALLOTID;
    private String TIMEKEY;

    // Data Info
    private String RECEIPTDATE;
    private String VENDOR;
    private Double OPENINGQTY;
    private Double INQTY;
    private Double BONUSQTY;
    private Double CONSUMEQTY;
    private Double SCRAPQTY;
    private Double OUTQTY;
    private Double HOLDQTY;
    private Double STOCKQTY;
    private Double QTY;
    private String REASONCODE;
    private String REFERENCETIMEKEY;
    private String CONSUMABLETIMEKEY;
    private String EVENTNAME;
    private Timestamp EVENTTIME;
    private String EVENTUSERID;
    private String EVENTCOMMENT;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public MATERIALSTOCKHISTORY() { }

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

    // OPENINGQTY
    public Double getOPENINGQTY()
    {
        return OPENINGQTY;
    }
    public void setOPENINGQTY(Double OPENINGQTY)
    {
        this.OPENINGQTY = OPENINGQTY;
        this.DATAMAP.put("OPENINGQTY", OPENINGQTY);
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

    // QTY
    public Double getQTY()
    {
        return QTY;
    }
    public void setQTY(Double QTY)
    {
        this.QTY = QTY;
        this.DATAMAP.put("QTY", QTY);
    }

    // REASONCODE
    public String getREASONCODE()
    {
        return REASONCODE;
    }
    public void setREASONCODE(String REASONCODE)
    {
        this.REASONCODE = REASONCODE;
        this.DATAMAP.put("REASONCODE", REASONCODE);
    }

    // REFERENCETIMEKEY
    public String getREFERENCETIMEKEY()
    {
        return REFERENCETIMEKEY;
    }
    public void setREFERENCETIMEKEY(String REFERENCETIMEKEY)
    {
        this.REFERENCETIMEKEY = REFERENCETIMEKEY;
        this.DATAMAP.put("REFERENCETIMEKEY", REFERENCETIMEKEY);
    }

    // CONSUMABLETIMEKEY
    public String getCONSUMABLETIMEKEY()
    {
        return CONSUMABLETIMEKEY;
    }
    public void setCONSUMABLETIMEKEY(String CONSUMABLETIMEKEY)
    {
        this.CONSUMABLETIMEKEY = CONSUMABLETIMEKEY;
        this.DATAMAP.put("CONSUMABLETIMEKEY", CONSUMABLETIMEKEY);
    }

    // EVENTNAME
    public String getEVENTNAME()
    {
        return EVENTNAME;
    }
    public void setEVENTNAME(String EVENTNAME)
    {
        this.EVENTNAME = EVENTNAME;
        this.DATAMAP.put("EVENTNAME", EVENTNAME);
    }

    // EVENTTIME
    public Timestamp getEVENTTIME()
    {
        return EVENTTIME;
    }
    public void setEVENTTIME(Timestamp EVENTTIME)
    {
        this.EVENTTIME = EVENTTIME;
        this.DATAMAP.put("EVENTTIME", EVENTTIME);
    }

    // EVENTUSERID
    public String getEVENTUSERID()
    {
        return EVENTUSERID;
    }
    public void setEVENTUSERID(String EVENTUSERID)
    {
        this.EVENTUSERID = EVENTUSERID;
        this.DATAMAP.put("EVENTUSERID", EVENTUSERID);
    }

    // EVENTCOMMENT
    public String getEVENTCOMMENT()
    {
        return EVENTCOMMENT;
    }
    public void setEVENTCOMMENT(String EVENTCOMMENT)
    {
        this.EVENTCOMMENT = EVENTCOMMENT;
        this.DATAMAP.put("EVENTCOMMENT", EVENTCOMMENT);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.YYYYMM == null || this.WAREHOUSEID == null || this.MATERIALTYPE == null || this.MATERIALID == null || this.MATERIALLOTID == null || this.TIMEKEY == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, YYYYMM, WAREHOUSEID, MATERIALTYPE, MATERIALID, MATERIALLOTID, TIMEKEY"});
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
