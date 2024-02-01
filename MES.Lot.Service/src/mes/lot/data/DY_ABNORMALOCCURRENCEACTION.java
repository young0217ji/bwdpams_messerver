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
public class DY_ABNORMALOCCURRENCEACTION
{
    // Key Info
    private String PLANTID;
    private String DATAKEY;

    // Data Info
    private String ACTIONCODE;
    private String ACTIONCOMMENT;
    private Integer LINESTOPCODE;
    private String DEFECTREPORTID;
    private String DESCRIPTION;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;

    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_ABNORMALOCCURRENCEACTION() { }

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

    // DATAKEY
    public String getKeyDATAKEY()
    {
        return DATAKEY;
    }
    public void setKeyDATAKEY(String DATAKEY)
    {
        this.DATAKEY = DATAKEY;
        this.KEYMAP.put("DATAKEY", DATAKEY);
    }


    // Data Methods
    // ACTIONCODE
    public String getACTIONCODE()
    {
        return ACTIONCODE;
    }
    public void setACTIONCODE(String ACTIONCODE)
    {
        this.ACTIONCODE = ACTIONCODE;
        this.DATAMAP.put("ACTIONCODE", ACTIONCODE);
    }
    
    // ACTIONCOMMENT
    public String getACTIONCOMMENT()
    {
        return ACTIONCOMMENT;
    }
    public void setACTIONCOMMENT(String ACTIONCOMMENT)
    {
        this.ACTIONCOMMENT = ACTIONCOMMENT;
        this.DATAMAP.put("ACTIONCOMMENT", ACTIONCOMMENT);
    }

    // LINESTOPCODE
    public Integer getLINESTOPCODE()
    {
        return LINESTOPCODE;
    }
    public void setLINESTOPCODE(Integer LINESTOPCODE)
    {
        this.LINESTOPCODE = LINESTOPCODE;
        this.DATAMAP.put("LINESTOPCODE", LINESTOPCODE);
    }

    // DEFECTREPORTID
    public String getDEFECTREPORTID()
    {
        return DEFECTREPORTID;
    }
    public void setDEFECTREPORTID(String DEFECTREPORTID)
    {
        this.DEFECTREPORTID = DEFECTREPORTID;
        this.DATAMAP.put("DEFECTREPORTID", DEFECTREPORTID);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.DATAKEY == null )
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, DATAKEY "});
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
