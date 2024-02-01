package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.05
 * 
 * @see
 */
public class DY_WORKCALLENDERBASIC
{
    // Key Info
    private String PLANTID;
    private String PROCESSID;
    private String EQUIPMENTID;

    // Data Info
    private int STDLABOR;
    private int EQUIPMENTCOUNT;
    private int WORKBENCH;
    private int DISPLAYSEQUENCE;
    private String HOLDFLAG;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_WORKCALLENDERBASIC() { }

    // Key Methods
    // PLANTID
    public String getPLANTID() {
		return PLANTID;
	}
	public void setPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}

	// PROCESSID
	public String getPROCESSID() {
		return PROCESSID;
	}
	public void setPROCESSID(String PROCESSID) {
		this.PROCESSID = PROCESSID;
		this.KEYMAP.put("PROCESSID", PROCESSID);
	}

	// EQUIPMENTID
	public String getEQUIPMENTID() {
		return EQUIPMENTID;
	}
	public void setEQUIPMENTID(String EQUIPMENTID) {
		this.EQUIPMENTID = EQUIPMENTID;
		this.KEYMAP.put("EQUIPMENTID", EQUIPMENTID);
	}

	
    // Data Methods
	// STDLABOR
	public int getSTDLABOR() {
		return STDLABOR;
	}
	public void setSTDLABOR(int STDLABOR) {
		this.STDLABOR = STDLABOR;
		this.DATAMAP.put("STDLABOR", STDLABOR);
	}

	// EQUIPMENTCNT
	public int getEQUIPMENTCOUNT() {
		return EQUIPMENTCOUNT;
	}
	public void setEQUIPMENTCOUNT(int EQUIPMENTCOUNT) {
		this.EQUIPMENTCOUNT = EQUIPMENTCOUNT;
		this.DATAMAP.put("EQUIPMENTCOUNT", EQUIPMENTCOUNT);
	}

	// WORKBENCH
	public int getWORKBENCH() {
		return WORKBENCH;
	}
	public void setWORKBENCH(int WORKBENCH) {
		this.WORKBENCH = WORKBENCH;
		this.DATAMAP.put("WORKBENCH", WORKBENCH);
	}

	// DISPLAYSEQ
	public int getDISPLAYSEQUENCE() {
		return DISPLAYSEQUENCE;
	}
	public void setDISPLAYSEQUENCE(int DISPLAYSEQUENCE) {
		this.DISPLAYSEQUENCE = DISPLAYSEQUENCE;
		this.DATAMAP.put("DISPLAYSEQUENCE", DISPLAYSEQUENCE);
	}

	// HOLDFLAG
	public String getHOLDFLAG() {
		return HOLDFLAG;
	}
	public void setHOLDFLAG(String HOLDFLAG) {
		this.HOLDFLAG = HOLDFLAG;
		this.DATAMAP.put("HOLDFLAG", HOLDFLAG);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PROCESSID == null || this.EQUIPMENTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PROCESSID,EQUIPMENTID"});
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
