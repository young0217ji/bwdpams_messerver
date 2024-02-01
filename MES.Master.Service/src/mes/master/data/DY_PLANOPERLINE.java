package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.10.11
 * 
 * @see
 */
public class DY_PLANOPERLINE
{
    // Key Info
    private String PLANTID;
    private String PLANOPERID; 
    private String WORKCENTER;
    
    // Data Info
    private Double MAXOPERATINGTIME;
    private Double STDOPERATINGTIME;
    private Double CYCLETIME;
    private Double LEADTIME;
    private int EQUIPMENTCOUNT;
    private String USEFLAG;
    private String DESCRIPTION;
    private Timestamp CREATETIME;    
    private String CREATEUSERID;     
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID; 
    
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_PLANOPERLINE() { }

    // Key Methods
	// PLANTID
    public String getKeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}

	// PLANOPERID
	public String getKeyPLANOPERID() {
		return PLANOPERID;
	}
	public void setKeyPLANOPERID(String PLANOPERID) {
		this.PLANOPERID = PLANOPERID;
		this.KEYMAP.put("PLANOPERID", PLANOPERID);
	}
	
	// WORKCENTER
	public String getKeyWORKCENTER() {
		return WORKCENTER;
	}
	public void setKeyWORKCENTER(String WORKCENTER) {
		this.WORKCENTER = WORKCENTER;
		this.KEYMAP.put("WORKCENTER", WORKCENTER);
	}
	
	
	
	// Data Methods
	// MAXOPERATINGTIME
	public Double getMAXOPERATINGTIME() {
		return MAXOPERATINGTIME;
	}
	public void setMAXOPERATINGTIME(Double MAXOPERATINGTIME) {
		this.MAXOPERATINGTIME = MAXOPERATINGTIME;
		this.DATAMAP.put("MAXOPERATINGTIME", MAXOPERATINGTIME);
	}

	// STDOPERATINGTIME
	public Double getSTDOPERATINGTIME() {
		return STDOPERATINGTIME;
	}
	public void setSTDOPERATINGTIME(Double STDOPERATINGTIME) {
		this.STDOPERATINGTIME = STDOPERATINGTIME;
		this.DATAMAP.put("STDOPERATINGTIME", STDOPERATINGTIME);
	}

	// CYCLETIME
	public Double getCYCLETIME() {
		return CYCLETIME;
	}
	public void setCYCLETIME(Double CYCLETIME) {
		this.CYCLETIME = CYCLETIME;
		this.DATAMAP.put("CYCLETIME", CYCLETIME);
	}

	// LEADTIME
	public Double getLEADTIME() {
		return LEADTIME;
	}
	public void setLEADTIME(Double LEADTIME) {
		this.LEADTIME = LEADTIME;
		this.DATAMAP.put("LEADTIME", LEADTIME);
	}

	// EQUIPMENTCOUNT
	public int getEQUIPMENTCOUNT() {
		return EQUIPMENTCOUNT;
	}
	public void setEQUIPMENTCOUNT(int EQUIPMENTCOUNT) {
		this.EQUIPMENTCOUNT = EQUIPMENTCOUNT;
		this.DATAMAP.put("EQUIPMENTCOUNT", EQUIPMENTCOUNT);
	}

	// USEFLAG
	public String getUSEFLAG() {
		return USEFLAG;
	}
	public void setUSEFLAG(String USEFLAG) {
		this.USEFLAG = USEFLAG;
		this.DATAMAP.put("USEFLAG", USEFLAG);
	}

	// DESCRIPTION
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String DESCRIPTION) {
		this.DESCRIPTION = DESCRIPTION;
		this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
	}

	// CREATETIME
	public Timestamp getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(Timestamp CREATETIME) {
		this.CREATETIME = CREATETIME;
		this.DATAMAP.put("CREATETIME", CREATETIME);
	}

	// CREATEUSERID
	public String getCREATEUSERID() {
		return CREATEUSERID;
	}
	public void setCREATEUSERID(String CREATEUSERID) {
		this.CREATEUSERID = CREATEUSERID;
		this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
	}

	// LASTUPDATETIME
	public Timestamp getLASTUPDATETIME() {
		return LASTUPDATETIME;
	}
	public void setLASTUPDATETIME(Timestamp LASTUPDATETIME) {
		this.LASTUPDATETIME = LASTUPDATETIME;
		this.DATAMAP.put("LASTUPDATETIME", LASTUPDATETIME);
	}

	// LASTUPDATEUSERID
	public String getLASTUPDATEUSERID() {
		return LASTUPDATEUSERID;
	}
	public void setLASTUPDATEUSERID(String LASTUPDATEUSERID) {
		this.LASTUPDATEUSERID = LASTUPDATEUSERID;
		this.DATAMAP.put("LASTUPDATEUSERID", LASTUPDATEUSERID);
	}
	

    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PLANOPERID == null || this.WORKCENTER == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID,PLANOPERID,WORKCENTER"});
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
