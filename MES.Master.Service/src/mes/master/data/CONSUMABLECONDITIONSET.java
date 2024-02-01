package mes.master.data;

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
public class CONSUMABLECONDITIONSET
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;
    private String ROCCOMPOSITIONID;
    private String BOMID;
    private String BOMVERSION;
    private Long BOMINDEX;
    private String CONSUMABLEID;

    // Data Info
    private Long ORDERINDEX;
    private String FEEDINGMODE;
    private String FEEDINGDESCRIPTION;
    private Double FEEDINGRATE;
    private String RECYCLEFLAG;
    private String DESCRIPTION;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public CONSUMABLECONDITIONSET() { }

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

    // ROCCOMPOSITIONID
    public String getKeyROCCOMPOSITIONID()
    {
        return ROCCOMPOSITIONID;
    }
    public void setKeyROCCOMPOSITIONID(String ROCCOMPOSITIONID)
    {
        this.ROCCOMPOSITIONID = ROCCOMPOSITIONID;
        this.KEYMAP.put("ROCCOMPOSITIONID", ROCCOMPOSITIONID);
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

    // CONSUMABLEID
    public String getKeyCONSUMABLEID()
    {
        return CONSUMABLEID;
    }
    public void setKeyCONSUMABLEID(String CONSUMABLEID)
    {
        this.CONSUMABLEID = CONSUMABLEID;
        this.KEYMAP.put("CONSUMABLEID", CONSUMABLEID);
    }


    // Data Methods
    // ORDERINDEX
    public Long getORDERINDEX()
    {
        return ORDERINDEX;
    }
    public void setORDERINDEX(Long ORDERINDEX)
    {
        this.ORDERINDEX = ORDERINDEX;
        this.DATAMAP.put("ORDERINDEX", ORDERINDEX);
    }

    // FEEDINGMODE
    public String getFEEDINGMODE()
    {
        return FEEDINGMODE;
    }
    public void setFEEDINGMODE(String FEEDINGMODE)
    {
        this.FEEDINGMODE = FEEDINGMODE;
        this.DATAMAP.put("FEEDINGMODE", FEEDINGMODE);
    }

    // FEEDINGDESCRIPTION
    public String getFEEDINGDESCRIPTION()
    {
        return FEEDINGDESCRIPTION;
    }
    public void setFEEDINGDESCRIPTION(String FEEDINGDESCRIPTION)
    {
        this.FEEDINGDESCRIPTION = FEEDINGDESCRIPTION;
        this.DATAMAP.put("FEEDINGDESCRIPTION", FEEDINGDESCRIPTION);
    }

    // FEEDINGRATE
    public Double getFEEDINGRATE()
    {
        return FEEDINGRATE;
    }
    public void setFEEDINGRATE(Double FEEDINGRATE)
    {
        this.FEEDINGRATE = FEEDINGRATE;
        this.DATAMAP.put("FEEDINGRATE", FEEDINGRATE);
    }

    // RECYCLEFLAG
    public String getRECYCLEFLAG()
    {
        return RECYCLEFLAG;
    }
    public void setRECYCLEFLAG(String RECYCLEFLAG)
    {
        this.RECYCLEFLAG = RECYCLEFLAG;
        this.DATAMAP.put("RECYCLEFLAG", RECYCLEFLAG);
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

    // LASTUPDATEUSERID
    public String getLASTUPDATEUSERID()
    {
        return LASTUPDATEUSERID;
    }
    public void setLASTUPDATEUSERID(String LASTUPDATEUSERID)
    {
        this.LASTUPDATEUSERID = LASTUPDATEUSERID;
        this.DATAMAP.put("LASTUPDATEUSERID", LASTUPDATEUSERID);
    }

    // LASTUPDATETIME
    public Timestamp getLASTUPDATETIME()
    {
        return LASTUPDATETIME;
    }
    public void setLASTUPDATETIME(Timestamp LASTUPDATETIME)
    {
        this.LASTUPDATETIME = LASTUPDATETIME;
        this.DATAMAP.put("LASTUPDATETIME", LASTUPDATETIME);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTID == null || this.ROCCOMPOSITIONID == null || this.BOMID == null || this.BOMVERSION == null || this.BOMINDEX == null || this.CONSUMABLEID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTID, ROCCOMPOSITIONID, BOMID, BOMVERSION, BOMINDEX, CONSUMABLEID"});
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
