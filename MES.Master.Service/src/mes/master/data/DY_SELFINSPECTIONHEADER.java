package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class DY_SELFINSPECTIONHEADER {

	// Key Info
	private String PLANTID;
	private String PROCESSID;
    private String HEADERID;
    
    // Data Info
    private String PARENTHEADERID;
    private String HEADERNAME;
    private int HEADERLEVEL;
    private String CELLTYPE;
    private String ENUMID;
    private String CELLWIDTH;
    private int SEQUENCE;
    private String RECIPEPARAMETERID;
    private String DESCRIPTION;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public DY_SELFINSPECTIONHEADER() {}
    
    // Key Methods
    // PLANTID
    public String getKeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}	
	// PROCESSID
    public String getKeyPROCESSID() {
		return PROCESSID;
	}
	public void setKeyPROCESSID(String PROCESSID) {
		this.PROCESSID = PROCESSID;
		this.KEYMAP.put("PROCESSID", PROCESSID);
	}
	// HEADERID
    public String getKeyHEADERID() {
		return HEADERID;
	}
	public void setKeyHEADERID(String HEADERID) {
		this.HEADERID = HEADERID;
		this.KEYMAP.put("HEADERID", HEADERID);
	}
	
	// Data Methods
	// PARENTHEADERID
	public String getPARENTHEADERID() {
		return PARENTHEADERID;
	}
	public void setPARENTHEADERID(String PARENTHEADERID) {
		this.PARENTHEADERID = PARENTHEADERID;
		this.DATAMAP.put("PARENTHEADERID", PARENTHEADERID);
	}
	// HEADERNAME
	public String getHEADERNAME() {
		return HEADERNAME;
	}
	public void setHEADERNAME(String HEADERNAME) {
		this.HEADERNAME = HEADERNAME;
		this.DATAMAP.put("HEADERNAME", HEADERNAME);
	}
	// HEADERLEVEL
	public int getHEADERLEVEL() {
		return HEADERLEVEL;
	}
	public void setHEADERLEVEL(int HEADERLEVEL) {
		this.HEADERLEVEL = HEADERLEVEL;
		this.DATAMAP.put("HEADERLEVEL", HEADERLEVEL);
	}
	// CELLTYPE
	public String getCELLTYPE() {
		return CELLTYPE;
	}
	public void setCELLTYPE(String CELLTYPE) {
		this.CELLTYPE = CELLTYPE;
		this.DATAMAP.put("CELLTYPE", CELLTYPE);
	}
	// ENUMID
	public String getENUMID() {
		return ENUMID;
	}
	public void setENUMID(String ENUMID) {
		this.ENUMID = ENUMID;
		this.DATAMAP.put("ENUMID", ENUMID);
	}
	// CELLWIDTH
	public String getCELLWIDTH() {
		return CELLWIDTH;
	}
	public void setCELLWIDTH(String CELLWIDTH) {
		this.CELLWIDTH = CELLWIDTH;
		this.DATAMAP.put("CELLWIDTH", CELLWIDTH);
	}
	// SEQUENCE
	public int getSEQUENCE() {
		return SEQUENCE;
	}
	public void setSEQUENCE(int SEQUENCE) {
		this.SEQUENCE = SEQUENCE;
		this.DATAMAP.put("SEQUENCE", SEQUENCE);
	}
	// RECIPEPARAMETERID
	public String getRECIPEPARAMETERID() {
		return RECIPEPARAMETERID;
	}
	public void setRECIPEPARAMETERID(String RECIPEPARAMETERID) {
		this.RECIPEPARAMETERID = RECIPEPARAMETERID;
		this.DATAMAP.put("RECIPEPARAMETERID", RECIPEPARAMETERID);
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
	// 
	public Timestamp getLASTUPDATETIME() {
		return LASTUPDATETIME;
	}
	public void setLASTUPDATETIME(Timestamp LASTUPDATETIME) {
		this.LASTUPDATETIME = LASTUPDATETIME;
		this.DATAMAP.put("LASTUPDATETIME", LASTUPDATETIME);
	}
	// 
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null|| this.PROCESSID == null || this.HEADERID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID,PRODUCTID,HEADERID"});
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