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
public class DY_DEFECTREPORTITEM
{
    // Key Info
    private String PLANTID;
    private String DEFECTREPORTID;
    private String MATERIALLOTID;

    // Data Info
    private String STOCKLOCATION;
    private Integer PLANQUANTITY;
    private Integer DEFECTQUANTITY;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;

    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_DEFECTREPORTITEM() { }

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

    // DEFECTREPORTID
    public String getKeyDEFECTREPORTID()
    {
        return DEFECTREPORTID;
    }
    public void setKeyDEFECTREPORTID(String DEFECTREPORTID)
    {
        this.DEFECTREPORTID = DEFECTREPORTID;
        this.KEYMAP.put("DEFECTREPORTID", DEFECTREPORTID);
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
    // STOCKLOCATION
    public String getSTOCKLOCATION()
    {
        return STOCKLOCATION;
    }
    public void setSTOCKLOCATION(String STOCKLOCATION)
    {
        this.STOCKLOCATION = STOCKLOCATION;
        this.DATAMAP.put("STOCKLOCATION", STOCKLOCATION);
    }
    
    // PLANQUANTITY
    public Integer getPLANQUANTITY()
    {
        return PLANQUANTITY;
    }
    public void setPLANQUANTITY(Integer PLANQUANTITY)
    {
        this.PLANQUANTITY = PLANQUANTITY;
        this.DATAMAP.put("PLANQUANTITY", PLANQUANTITY);
    }

    // DEFECTQUANTITY
    public Integer getDEFECTQUANTITY()
    {
        return DEFECTQUANTITY;
    }
    public void setDEFECTQUANTITY(Integer DEFECTQUANTITY)
    {
        this.DEFECTQUANTITY = DEFECTQUANTITY;
        this.DATAMAP.put("DEFECTQUANTITY", DEFECTQUANTITY);
    }

	// CREATETIME
    public Timestamp getCREATETIME()
    {
        return CREATETIME;
    }
    public void setCREATETIME(Timestamp CREATETIME)
    {
        this.CREATETIME = CREATETIME;
        this.DATAMAP.put("CREATETIME", CREATETIME);
    }

    // CREATEUSERID
    public String  getCREATEUSERID()
    {
        return CREATEUSERID;
    }
    public void setCREATEUSERID(String  CREATEUSERID)
    {
        this.CREATEUSERID = CREATEUSERID;
        this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
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

    
	// Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.DEFECTREPORTID == null || this.MATERIALLOTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, DEFECTREPORTID, MATERIALLOTID"});
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
