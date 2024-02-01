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
public class DY_SENDPUSH_LOG
{
    // Key Info
    private String PLANTID;
    private String DATAKEY;

    // Data Info
    private String USERID;
    private String MESSAGE;
    private String IFFLG;
    private Timestamp INSERTDT;
    private Timestamp UPDATEDT;
    private String SYSTEM_GUBUN;
    private String STATUS;
    private String EVENTNAME;
    private String RESULTVALUE;
    private String ERRORCOMMENT;
    
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_SENDPUSH_LOG() { }

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

    // USERID
    public String getKeyUSERID()
    {
        return USERID;
    }
    public void setKeyUSERID(String USERID)
    {
        this.USERID = USERID;
        this.KEYMAP.put("USERID", USERID);
    }


    // Data Methods
    // USERID
    public String getUSERID()
    {
        return USERID;
    }
    public void setUSERID(String USERID)
    {
        this.USERID = USERID;
        this.KEYMAP.put("USERID", USERID);
    }
    
    // MESSAGE
    public String getMESSAGE()
    {
        return MESSAGE;
    }
    public void setMESSAGE(String MESSAGE)
    {
        this.MESSAGE = MESSAGE;
        this.DATAMAP.put("MESSAGE", MESSAGE);
    }
    
    // IFFLG
    public String getIFFLG()
    {
        return IFFLG;
    }
    public void setIFFLG(String IFFLG)
    {
        this.IFFLG = IFFLG;
        this.DATAMAP.put("IFFLG", IFFLG);
    }


    // INSERTDT
	public Timestamp getINSERTDT() 
	{
		return INSERTDT;
	}

	public void setINSERTDT(Timestamp INSERTDT) {
        this.INSERTDT = INSERTDT;
        this.DATAMAP.put("INSERTDT", INSERTDT);
	}

    // INSERTDT
	public Timestamp getUPDATEDT() 
	{
		return UPDATEDT;
	}

	public void setUPDATEDT(Timestamp UPDATEDT) {
        this.UPDATEDT = UPDATEDT;
        this.DATAMAP.put("UPDATEDT", UPDATEDT);
	}

    // SYSTEM_GUBUN
	public String getSYSTEM_GUBUN() 
	{
		return SYSTEM_GUBUN;
	}

	public void setSYSTEM_GUBUN(String SYSTEM_GUBUN) {
        this.SYSTEM_GUBUN = SYSTEM_GUBUN;
        this.DATAMAP.put("SYSTEM_GUBUN", SYSTEM_GUBUN);
	}

    // STATUS
	public String getSTATUS() 
	{
		return STATUS;
	}

	public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
        this.DATAMAP.put("STATUS", STATUS);
	}

    // EVENTNAME
	public String getEVENTNAME() 
	{
		return EVENTNAME;
	}

	public void setEVENTNAME(String EVENTNAME) {
        this.EVENTNAME = EVENTNAME;
        this.DATAMAP.put("EVENTNAME", EVENTNAME);
	}

    // RESULTVALUE
    public String  getRESULTVALUE()
    {
        return RESULTVALUE;
    }
    public void setRESULTVALUE(String RESULTVALUE)
    {
        this.RESULTVALUE = RESULTVALUE;
        this.DATAMAP.put("RESULTVALUE", RESULTVALUE);
    }
    
    // ERRORCOMMENT
    public String  getERRORCOMMENT()
    {
        return ERRORCOMMENT;
    }
    public void setERRORCOMMENT(String ERRORCOMMENT)
    {
        this.ERRORCOMMENT = ERRORCOMMENT;
        this.DATAMAP.put("ERRORCOMMENT", ERRORCOMMENT);
    }

    
	// Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.DATAKEY == null )
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, DATAKEY"});
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
