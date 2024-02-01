package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.06
 * 
 * @see
 */
public class DY_PRODUCTCYCLETIME
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;
    private String PROCESSID;
    private String EQUIPMENTID;

    // Data Info
    private Double CYCLETIME;       
    private String DESCRIPTION;     
    private Timestamp CREATETIME;      
    private String CREATEUSERID;    
    private Timestamp LASTUPDATETIME;  
    private String LASTUPDATEUSERID;

    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_PRODUCTCYCLETIME() { }

    // Key Methods
	// PLANTID
    public String getKeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}

	// PRODUCTID
	public String getKeyPRODUCTID() {
		return PRODUCTID;
	}
	public void setKeyPRODUCTID(String PRODUCTID) {
		this.PRODUCTID = PRODUCTID;
		this.KEYMAP.put("PRODUCTID", PRODUCTID);
	}

	// PROCESSID
	public String getKeyPROCESSID() {
		return PROCESSID;
	}
	public void setKeyPROCESSID(String PROCESSID) {
		this.PROCESSID = PROCESSID;
		this.KEYMAP.put("PROCESSID", PROCESSID);
	}

	// EQUIPMENTID
	public String getKeyEQUIPMENTID() {
		return EQUIPMENTID;
	}
	public void setKeyEQUIPMENTID(String EQUIPMENTID) {
		this.EQUIPMENTID = EQUIPMENTID;
		this.KEYMAP.put("EQUIPMENTID", EQUIPMENTID);
	}
	
	
	// Data Methods
	// CYCLETIME
	public Double getCYCLETIME() {
		return CYCLETIME;
	}

	public void setCYCLETIME(Double CYCLETIME) {
		this.CYCLETIME = CYCLETIME;
		this.DATAMAP.put("CYCLETIME", CYCLETIME);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTID == null || this.PROCESSID == null|| this.EQUIPMENTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID,PRODUCTID,PROCESSID,EQUIPMENTID"});
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
