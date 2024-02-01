package mes.equipment.data;

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
public class SPAREPARTINFORMATION
{
    // Key Info
    private String PLANTID;
    private String SPAREPARTID;
    private String SPAREPARTLOTID;

    // Data Info
    private String SPAREPARTLOTSTATE;
    private Long QUANTITY;
    private String PRODUCTNUMBER;
    private String SERIALNUMBER;
    private String GRADE;
    private Timestamp RECEIPTDATE;
    private String SUPPLYVENDOR;
    private Timestamp RESERVEDATE;
    private String RESERVEUSERID;
    private String RESERVECOMMENT;
    private String RESERVEEQUIPMENTID;
    private String RESERVELOCATION;
    private String LOCATION;
    private String REFERENCETYPE;
    private String REFERENCEVALUE;
    private String REASONCODETYPE;
    private String REASONCODE;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public SPAREPARTINFORMATION() { }

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

    // SPAREPARTID
    public String getKeySPAREPARTID()
    {
        return SPAREPARTID;
    }
    public void setKeySPAREPARTID(String SPAREPARTID)
    {
        this.SPAREPARTID = SPAREPARTID;
        this.KEYMAP.put("SPAREPARTID", SPAREPARTID);
    }

    // SPAREPARTLOTID
    public String getKeySPAREPARTLOTID()
    {
        return SPAREPARTLOTID;
    }
    public void setKeySPAREPARTLOTID(String SPAREPARTLOTID)
    {
        this.SPAREPARTLOTID = SPAREPARTLOTID;
        this.KEYMAP.put("SPAREPARTLOTID", SPAREPARTLOTID);
    }


    // Data Methods
    // SPAREPARTLOTSTATE
    public String getSPAREPARTLOTSTATE()
    {
        return SPAREPARTLOTSTATE;
    }
    public void setSPAREPARTLOTSTATE(String SPAREPARTLOTSTATE)
    {
        this.SPAREPARTLOTSTATE = SPAREPARTLOTSTATE;
        this.DATAMAP.put("SPAREPARTLOTSTATE", SPAREPARTLOTSTATE);
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

    // PRODUCTNUMBER
    public String getPRODUCTNUMBER()
    {
        return PRODUCTNUMBER;
    }
    public void setPRODUCTNUMBER(String PRODUCTNUMBER)
    {
        this.PRODUCTNUMBER = PRODUCTNUMBER;
        this.DATAMAP.put("PRODUCTNUMBER", PRODUCTNUMBER);
    }

    // SERIALNUMBER
    public String getSERIALNUMBER()
    {
        return SERIALNUMBER;
    }
    public void setSERIALNUMBER(String SERIALNUMBER)
    {
        this.SERIALNUMBER = SERIALNUMBER;
        this.DATAMAP.put("SERIALNUMBER", SERIALNUMBER);
    }

    // GRADE
    public String getGRADE()
    {
        return GRADE;
    }
    public void setGRADE(String GRADE)
    {
        this.GRADE = GRADE;
        this.DATAMAP.put("GRADE", GRADE);
    }

    // RECEIPTDATE
    public Timestamp getRECEIPTDATE()
    {
        return RECEIPTDATE;
    }
    public void setRECEIPTDATE(Timestamp RECEIPTDATE)
    {
        this.RECEIPTDATE = RECEIPTDATE;
        this.DATAMAP.put("RECEIPTDATE", RECEIPTDATE);
    }

    // SUPPLYVENDOR
    public String getSUPPLYVENDOR()
    {
        return SUPPLYVENDOR;
    }
    public void setSUPPLYVENDOR(String SUPPLYVENDOR)
    {
        this.SUPPLYVENDOR = SUPPLYVENDOR;
        this.DATAMAP.put("SUPPLYVENDOR", SUPPLYVENDOR);
    }

    // RESERVEDATE
    public Timestamp getRESERVEDATE()
    {
        return RESERVEDATE;
    }
    public void setRESERVEDATE(Timestamp RESERVEDATE)
    {
        this.RESERVEDATE = RESERVEDATE;
        this.DATAMAP.put("RESERVEDATE", RESERVEDATE);
    }

    // RESERVEUSERID
    public String getRESERVEUSERID()
    {
        return RESERVEUSERID;
    }
    public void setRESERVEUSERID(String RESERVEUSERID)
    {
        this.RESERVEUSERID = RESERVEUSERID;
        this.DATAMAP.put("RESERVEUSERID", RESERVEUSERID);
    }

    // RESERVECOMMENT
    public String getRESERVECOMMENT()
    {
        return RESERVECOMMENT;
    }
    public void setRESERVECOMMENT(String RESERVECOMMENT)
    {
        this.RESERVECOMMENT = RESERVECOMMENT;
        this.DATAMAP.put("RESERVECOMMENT", RESERVECOMMENT);
    }

    // RESERVEEQUIPMENTID
    public String getRESERVEEQUIPMENTID()
    {
        return RESERVEEQUIPMENTID;
    }
    public void setRESERVEEQUIPMENTID(String RESERVEEQUIPMENTID)
    {
        this.RESERVEEQUIPMENTID = RESERVEEQUIPMENTID;
        this.DATAMAP.put("RESERVEEQUIPMENTID", RESERVEEQUIPMENTID);
    }

    // RESERVELOCATION
    public String getRESERVELOCATION()
    {
        return RESERVELOCATION;
    }
    public void setRESERVELOCATION(String RESERVELOCATION)
    {
        this.RESERVELOCATION = RESERVELOCATION;
        this.DATAMAP.put("RESERVELOCATION", RESERVELOCATION);
    }

    // LOCATION
    public String getLOCATION()
    {
        return LOCATION;
    }
    public void setLOCATION(String LOCATION)
    {
        this.LOCATION = LOCATION;
        this.DATAMAP.put("LOCATION", LOCATION);
    }
    
    // REFERENCETYPE
    public String getREFERENCETYPE()
    {
        return REFERENCETYPE;
    }
    public void setREFERENCETYPE(String REFERENCETYPE)
    {
        this.REFERENCETYPE = REFERENCETYPE;
        this.DATAMAP.put("REFERENCETYPE", REFERENCETYPE);
    }
    
    // REFERENCETYPE
    public String getREFERENCEVALUE()
    {
        return REFERENCEVALUE;
    }
    public void setREFERENCEVALUE(String REFERENCEVALUE)
    {
        this.REFERENCEVALUE = REFERENCEVALUE;
        this.DATAMAP.put("REFERENCEVALUE", REFERENCEVALUE);
    }
    
    // REASONCODETYPE
    public String getREASONCODETYPE()
    {
        return REASONCODETYPE;
    }
    public void setREASONCODETYPE(String REASONCODETYPE)
    {
        this.REASONCODETYPE = REASONCODETYPE;
        this.DATAMAP.put("REASONCODETYPE", REASONCODETYPE);
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
    
    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.SPAREPARTID == null || this.SPAREPARTLOTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, SPAREPARTID, SPAREPARTLOTID"});
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
