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
public class DY_ABNORMALOCCURRENCE
{
    // Key Info
	private String PLANTID;
	private String DATAKEY;

    // Data Info
	private String WORKORDERID;
	private String PRODUCTID;
	private String PROCESSID;
	private String EQUIPMENTID;
	private String ABNORMALTYPE;
	private String ABNORMALSTATE;
	private String OCCURRENCECODE;
	private String OCCURRENCECOMMENT;
	private Timestamp OCCURRENCETIME;
	private String OCCURRENCEUSERID;
	private Timestamp ARRIVALTIME;
	private String ARRIVALUSERID;
	private String ARRIVALRESULT;
	private Timestamp FINISHTIME;
	private String FINISHUSERID;
	private String FINISHRESULT;
	private String FINISHCOMMENT;
	private String DESCRIPTION;
	private Timestamp CREATETIME;
	private String CREATEUSERID;
	private Timestamp LASTUPDATETIME;
	private String LASTUPDATEUSERID;
    
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_ABNORMALOCCURRENCE() { }


    
    // Key Methods
    //PLANTID
    public String getKeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}

    //DATAKEY
	public String getKeyDATAKEY() {
		return DATAKEY;
	}
	public void setKeyDATAKEY(String DATAKEY) {
		this.DATAKEY = DATAKEY;
		this.KEYMAP.put("DATAKEY", DATAKEY);
	}

	//Data Method
    //WORKORDERID
	public String getWORKORDERID() {
		return WORKORDERID;
	}
	public void setWORKORDERID(String WORKORDERID) {
		this.WORKORDERID = WORKORDERID;
		this.DATAMAP.put("WORKORDERID", WORKORDERID);
	}

    //PRODUCTID
	public String getPRODUCTID() {
		return PRODUCTID;
	}
	public void setPRODUCTID(String PRODUCTID) {
		this.PRODUCTID = PRODUCTID;
		this.DATAMAP.put("PRODUCTID", PRODUCTID);
	}

    //PROCESSID
	public String getPROCESSID() {
		return PROCESSID;
	}
	public void setPROCESSID(String PROCESSID) {
		this.PROCESSID = PROCESSID;
		this.DATAMAP.put("PROCESSID", PROCESSID);
	}

    //EQUIPMENTID
	public String getEQUIPMENTID() {
		return EQUIPMENTID;
	}
	public void setEQUIPMENTID(String EQUIPMENTID) {
		this.EQUIPMENTID = EQUIPMENTID;
		this.DATAMAP.put("EQUIPMENTID", EQUIPMENTID);
	}
	
    //ABNORMALTYPE
	public String getABNORMALTYPE() {
		return ABNORMALTYPE;
	}
	public void setABNORMALTYPE(String ABNORMALTYPE) {
		this.ABNORMALTYPE = ABNORMALTYPE;
		this.DATAMAP.put("ABNORMALTYPE", ABNORMALTYPE);
	}
	
	//ABNORMALSTATE
	public String getABNORMALSTATE() {
		return ABNORMALSTATE;
	}
	public void setABNORMALSTATE(String ABNORMALSTATE) {
		this.ABNORMALSTATE = ABNORMALSTATE;
		this.DATAMAP.put("ABNORMALSTATE", ABNORMALSTATE);
	}
	
    //OCCURRENCECODE
	public String getOCCURRENCECODE() {
		return OCCURRENCECODE;
	}
	public void setOCCURRENCECODE(String OCCURRENCECODE) {
		this.OCCURRENCECODE = OCCURRENCECODE;
		this.DATAMAP.put("OCCURRENCECODE", OCCURRENCECODE);
	}
	
    //OCCURRENCECOMMENT
	public String getOCCURRENCECOMMENT() {
		return OCCURRENCECOMMENT;
	}
	public void setOCCURRENCECOMMENT(String OCCURRENCECOMMENT) {
		this.OCCURRENCECOMMENT = OCCURRENCECOMMENT;
		this.DATAMAP.put("OCCURRENCECOMMENT", OCCURRENCECOMMENT);
	}
	
    //OCCURRENCETIME
	public Timestamp getOCCURRENCETIME() {
		return OCCURRENCETIME;
	}
	public void setOCCURRENCETIME(Timestamp OCCURRENCETIME) {
		this.OCCURRENCETIME = OCCURRENCETIME;
		this.DATAMAP.put("OCCURRENCETIME", OCCURRENCETIME);
	}

    //OCCURRENCEUSERID
	public String getOCCURRENCEUSERID() {
		return OCCURRENCEUSERID;
	}
	public void setOCCURRENCEUSERID(String OCCURRENCEUSERID) {
		this.OCCURRENCEUSERID = OCCURRENCEUSERID;
		this.DATAMAP.put("OCCURRENCEUSERID", OCCURRENCEUSERID);
	}

    //ARRIVALTIME
	public Timestamp getARRIVALTIME() {
		return ARRIVALTIME;
	}
	public void setARRIVALTIME(Timestamp ARRIVALTIME) {
		this.ARRIVALTIME = ARRIVALTIME;
		this.DATAMAP.put("ARRIVALTIME", ARRIVALTIME);
	}

    //ARRIVALUSERID
	public String getARRIVALUSERID() {
		return ARRIVALUSERID;
	}
	public void setARRIVALUSERID(String ARRIVALUSERID) {
		this.ARRIVALUSERID = ARRIVALUSERID;
		this.DATAMAP.put("ARRIVALUSERID", ARRIVALUSERID);
	}

    //ARRIVALRESULT
	public String getARRIVALRESULT() {
		return ARRIVALRESULT;
	}
	public void setARRIVALRESULT(String ARRIVALRESULT) {
		this.ARRIVALRESULT = ARRIVALRESULT;
		this.DATAMAP.put("ARRIVALRESULT", ARRIVALRESULT);
	}

    //FINISHTIME
	public Timestamp getFINISHTIME() {
		return FINISHTIME;
	}
	public void setFINISHTIME(Timestamp FINISHTIME) {
		this.FINISHTIME = FINISHTIME;
		this.DATAMAP.put("FINISHTIME", FINISHTIME);
	}

    //FINISHUSERID
	public String getFINISHUSERID() {
		return FINISHUSERID;
	}
	public void setFINISHUSERID(String FINISHUSERID) {
		this.FINISHUSERID = FINISHUSERID;
		this.DATAMAP.put("FINISHUSERID", FINISHUSERID);
	}

    //FINISHRESULT
	public String getFINISHRESULT() {
		return FINISHRESULT;
	}
	public void setFINISHRESULT(String FINISHRESULT) {
		this.FINISHRESULT = FINISHRESULT;
		this.DATAMAP.put("FINISHRESULT", FINISHRESULT);
	}

    //FINISHCOMMENT
	public String getFINISHCOMMENT() {
		return FINISHCOMMENT;
	}
	public void setFINISHCOMMENT(String FINISHCOMMENT) {
		this.FINISHCOMMENT = FINISHCOMMENT;
		this.DATAMAP.put("FINISHCOMMENT", FINISHCOMMENT);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.DATAKEY == null)
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
