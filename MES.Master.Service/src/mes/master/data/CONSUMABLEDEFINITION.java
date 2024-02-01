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
public class CONSUMABLEDEFINITION
{
    // Key Info
    private String PLANTID;
    private String CONSUMABLEID;

    // Data Info
    private String CONSUMABLENAME;
    private String CONSUMABLETYPE;
    private String UNIT;
    private String ACCOUNTTYPE;
    private String ACCOUNTCODE;
    private Double SAFESTOCK;
    private String MAINVENDOR;
    private String MAKER;
    private String ENGINEER;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public CONSUMABLEDEFINITION() { }

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
    // CONSUMABLENAME
    public String getCONSUMABLENAME()
    {
        return CONSUMABLENAME;
    }
    public void setCONSUMABLENAME(String CONSUMABLENAME)
    {
        this.CONSUMABLENAME = CONSUMABLENAME;
        this.DATAMAP.put("CONSUMABLENAME", CONSUMABLENAME);
    }

    // CONSUMABLETYPE
    public String getCONSUMABLETYPE()
    {
        return CONSUMABLETYPE;
    }
    public void setCONSUMABLETYPE(String CONSUMABLETYPE)
    {
        this.CONSUMABLETYPE = CONSUMABLETYPE;
        this.DATAMAP.put("CONSUMABLETYPE", CONSUMABLETYPE);
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

    // ACCOUNTTYPE
    public String getACCOUNTTYPE()
    {
        return ACCOUNTTYPE;
    }
    public void setACCOUNTTYPE(String ACCOUNTTYPE)
    {
        this.ACCOUNTTYPE = ACCOUNTTYPE;
        this.DATAMAP.put("ACCOUNTTYPE", ACCOUNTTYPE);
    }

    // ACCOUNTCODE
    public String getACCOUNTCODE()
    {
    	return ACCOUNTCODE;
    }
    public void setACCOUNTCODE(String ACCOUNTCODE)
    {
    	this.ACCOUNTCODE = ACCOUNTCODE;
    	this.DATAMAP.put("ACCOUNTCODE", ACCOUNTCODE);
    }
    
    // SAFESTOCK
    public Double getSAFESTOCK()
    {
    	return SAFESTOCK;
    }
    public void setSAFESTOCK(Double SAFESTOCK)
    {
    	this.SAFESTOCK = SAFESTOCK;
    	this.DATAMAP.put("SAFESTOCK", SAFESTOCK);
    }
    
    // MAINVENDOR
    public String getMAINVENDOR()
    {
        return MAINVENDOR;
    }
    public void setMAINVENDOR(String MAINVENDOR)
    {
        this.MAINVENDOR = MAINVENDOR;
        this.DATAMAP.put("MAINVENDOR", MAINVENDOR);
    }
    
    // MAKER
    public String getMAKER()
    {
        return MAKER;
    }
    public void setMAKER(String MAKER)
    {
        this.MAKER = MAKER;
        this.DATAMAP.put("MAKER", MAKER);
    }
    
    // ENGINEER
    public String getENGINEER()
    {
        return ENGINEER;
    }
    public void setENGINEER(String ENGINEER)
    {
        this.ENGINEER = ENGINEER;
        this.DATAMAP.put("ENGINEER", ENGINEER);
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
    public String getCREATEUSERID()
    {
        return CREATEUSERID;
    }
    public void setCREATEUSERID(String CREATEUSERID)
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.CONSUMABLEID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, CONSUMABLEID"});
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
