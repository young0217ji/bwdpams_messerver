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
public class DY_PAINTINGPROCESS
{
    // Key Info
    private String PLANTID;
    private String SCANID;

    
    // Data Info
    private String PROCESSID;
    private String EQUIPMENTID;
    private String INPUTPRODUCTID;
    private String INPUTTYPE;
    private String STATE;
    private Timestamp PROCESSSTARTTIME;
    private Timestamp PROCESSENDTIME;
    private String DESCRIPTION;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;

    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_PAINTINGPROCESS() { }

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

    // SCANID
    public String getKeySCANID()
    {
        return SCANID;
    }
    public void setKeySCANID(String SCANID)
    {
        this.SCANID = SCANID;
        this.KEYMAP.put("SCANID", SCANID);
    }


    // Data Methods
    // PROCESSID
    public String getPROCESSID()
    {
        return PROCESSID;
    }
    public void setPROCESSID(String PROCESSID)
    {
        this.PROCESSID = PROCESSID;
        this.DATAMAP.put("PROCESSID", PROCESSID);
    }
    
    // EQUIPMENTID
    public String getEQUIPMENTID()
    {
        return EQUIPMENTID;
    }
    public void setEQUIPMENTID(String EQUIPMENTID)
    {
        this.EQUIPMENTID = EQUIPMENTID;
        this.DATAMAP.put("EQUIPMENTID", EQUIPMENTID);
    }
    
    // INPUTPRODUCTID
    public String getINPUTPRODUCTID()
    {
        return INPUTPRODUCTID;
    }
    public void setINPUTPRODUCTID(String INPUTPRODUCTID)
    {
        this.INPUTPRODUCTID = INPUTPRODUCTID;
        this.DATAMAP.put("INPUTPRODUCTID", INPUTPRODUCTID);
    }
    
    // INPUTTYPE
    public String getINPUTTYPE()
    {
        return INPUTTYPE;
    }
    public void setINPUTTYPE(String INPUTTYPE)
    {
        this.INPUTTYPE = INPUTTYPE;
        this.DATAMAP.put("INPUTTYPE", INPUTTYPE);
    }

    // STATE
    public String getSTATE()
    {
        return STATE;
    }
    public void setSTATE(String STATE)
    {
        this.STATE = STATE;
        this.DATAMAP.put("STATE", STATE);
    }

    // PROCESSSTARTTIME
    public Timestamp getPROCESSSTARTTIME()
    {
        return PROCESSSTARTTIME;
    }
    public void setPROCESSSTARTTIME(Timestamp PROCESSSTARTTIME)
    {
        this.PROCESSSTARTTIME = PROCESSSTARTTIME;
        this.DATAMAP.put("PROCESSSTARTTIME", PROCESSSTARTTIME);
    }
    
    // PROCESSENDTIME
    public Timestamp getPROCESSENDTIME()
    {
        return PROCESSENDTIME;
    }
    public void setPROCESSENDTIME(Timestamp PROCESSENDTIME)
    {
        this.PROCESSENDTIME = PROCESSENDTIME;
        this.DATAMAP.put("PROCESSENDTIME", PROCESSENDTIME);
    }
    
    // DESCRIPTION
    public String getDESCRIPTION() 
    {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String DESCRIPTION) {
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.SCANID == null )
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, SCANID"});
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
