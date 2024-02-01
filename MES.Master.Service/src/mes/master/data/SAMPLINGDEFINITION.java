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
public class SAMPLINGDEFINITION
{
    // Key Info
	private String PLANTID;
    private String SAMPLINGID;

    // Data Info
    private String SAMPLINGTYPE;
    private String SAMPLINGMETHOD;
    private String SAMPLINGCHECK;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public SAMPLINGDEFINITION() { }

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
    
    // SAMPLINGID
    public String getKeySAMPLINGID()
    {
        return SAMPLINGID;
    }
    public void setKeySAMPLINGID(String SAMPLINGID)
    {
        this.SAMPLINGID = SAMPLINGID;
        this.KEYMAP.put("SAMPLINGID", SAMPLINGID);
    }


    // Data Methods
    // SAMPLINGTYPE
    public String getSAMPLINGTYPE()
    {
        return SAMPLINGTYPE;
    }
    public void setSAMPLINGTYPE(String SAMPLINGTYPE)
    {
        this.SAMPLINGTYPE = SAMPLINGTYPE;
        this.DATAMAP.put("SAMPLINGTYPE", SAMPLINGTYPE);
    }

    // SAMPLINGMETHOD
    public String getSAMPLINGMETHOD()
    {
        return SAMPLINGMETHOD;
    }
    public void setSAMPLINGMETHOD(String SAMPLINGMETHOD)
    {
        this.SAMPLINGMETHOD = SAMPLINGMETHOD;
        this.DATAMAP.put("SAMPLINGMETHOD", SAMPLINGMETHOD);
    }

    // SAMPLINGCHECK
    public String getSAMPLINGCHECK()
    {
        return SAMPLINGCHECK;
    }
    public void setSAMPLINGCHECK(String SAMPLINGCHECK)
    {
        this.SAMPLINGCHECK = SAMPLINGCHECK;
        this.DATAMAP.put("SAMPLINGCHECK", SAMPLINGCHECK);
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
        if (this.KEYMAP.isEmpty() || this.SAMPLINGID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"SAMPLINGID"});
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
