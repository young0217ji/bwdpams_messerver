package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.10.30
 * 
 * @see
 */
public class DY_WORKERCHANGE {
	
	// Key Info
	private String PLANTID;
	private String PROCESSID;
	private String CHANGEDATE;
	private String CHANGEWORKER;

	// Data Info
	private String PRVWORKER;
	private String CHANGETYPE;
	private String SPECIALQUALIFICATIONFLAG;
	private String EDUCATIONFLAG;
	private String STANDARDDOCUMENTFLAG;
	private String DRAWINGFLAG;
	private String INSPECTIONSTANDARDFLAG;
	private String INSPECTIONDOCUMENTFLAG;
	private String MEASURINGINSTRUMENTFLAG;
	private String MONITORINGRESULT;
	private String DESCRIPTION;
	private Timestamp CREATETIME;
	private String CREATEUSERID;
	private Timestamp LASTUPDATETIME;
	private String LASTUPDATEUSERID;
	
	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
	
	public DY_WORKERCHANGE() {}
	
	// Key Methods
	// PLANTID
	public String getkeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}
	
	// PLANTID
	public String getkeyPROCESSID() {
		return PROCESSID;
	}
	public void setKeyPROCESSID(String PROCESSID) {
		this.PROCESSID = PROCESSID;
		this.KEYMAP.put("PROCESSID", PROCESSID);
	}
	
	// PLANTID
	public String getkeyCHANGEDATE() {
		return CHANGEDATE;
	}
	public void setKeyCHANGEDATE(String CHANGEDATE) {
		this.CHANGEDATE = CHANGEDATE;
		this.KEYMAP.put("CHANGEDATE", CHANGEDATE);
	}
	
	// PLANTID
	public String getkeyCHANGEWORKER() {
		return CHANGEWORKER;
	}
	public void setKeyCHANGEWORKER(String CHANGEWORKER) {
		this.CHANGEWORKER =CHANGEWORKER;
		this.KEYMAP.put("CHANGEWORKER", CHANGEWORKER);
	}
	
	// Data Methods
	// PRVWORKER
	public String getPRVWORKER() {
		return PRVWORKER;
	}
	public void setPRVWORKER(String PRVWORKER) {
		this.PRVWORKER = PRVWORKER;
		this.DATAMAP.put("PRVWORKER", PRVWORKER);
	}

	// CHANGETYPE
	public String getCHANGETYPE() {
		return CHANGETYPE;
	}
	public void setCHANGETYPE(String CHANGETYPE) {
		this.CHANGETYPE = CHANGETYPE;
		this.DATAMAP.put("CHANGETYPE", CHANGETYPE);
	}

	// SPECIALQUALIFICATIONFLAG
	public String getSPECIALQUALIFICATIONFLAG() {
		return SPECIALQUALIFICATIONFLAG;
	}
	public void setSPECIALQUALIFICATIONFLAG(String SPECIALQUALIFICATIONFLAG) {
		this.SPECIALQUALIFICATIONFLAG = SPECIALQUALIFICATIONFLAG;
		this.DATAMAP.put("SPECIALQUALIFICATIONFLAG", SPECIALQUALIFICATIONFLAG);
	}

	// EDUCATIONFLAG
	public String getEDUCATIONFLAG() {
		return EDUCATIONFLAG;
	}
	public void setEDUCATIONFLAG(String EDUCATIONFLAG) {
		this.EDUCATIONFLAG = EDUCATIONFLAG;
		this.DATAMAP.put("EDUCATIONFLAG", EDUCATIONFLAG);
	}

	// STANDARDDOCUMENTFLAG
	public String getSTANDARDDOCUMENTFLAG() {
		return STANDARDDOCUMENTFLAG;
	}
	public void setSTANDARDDOCUMENTFLAG(String STANDARDDOCUMENTFLAG) {
		this.STANDARDDOCUMENTFLAG = STANDARDDOCUMENTFLAG;
		this.DATAMAP.put("STANDARDDOCUMENTFLAG", STANDARDDOCUMENTFLAG);
	}

	// DRAWINGFLAG
	public String getDRAWINGFLAG() {
		return DRAWINGFLAG;
	}
	public void setDRAWINGFLAG(String DRAWINGFLAG) {
		this.DRAWINGFLAG = DRAWINGFLAG;
		this.DATAMAP.put("DRAWINGFLAG", DRAWINGFLAG);
	}

	// INSPECTIONSTANDARDFLAG
	public String getINSPECTIONSTANDARDFLAG() {
		return INSPECTIONSTANDARDFLAG;
	}
	public void setINSPECTIONSTANDARDFLAG(String INSPECTIONSTANDARDFLAG) {
		this.INSPECTIONSTANDARDFLAG = INSPECTIONSTANDARDFLAG;
		this.DATAMAP.put("INSPECTIONSTANDARDFLAG", INSPECTIONSTANDARDFLAG);
	}

	// INSPECTIONDOCUMENTFLAG
	public String getINSPECTIONDOCUMENTFLAG() {
		return INSPECTIONDOCUMENTFLAG;
	}
	public void setINSPECTIONDOCUMENTFLAG(String INSPECTIONDOCUMENTFLAG) {
		this.INSPECTIONDOCUMENTFLAG = INSPECTIONDOCUMENTFLAG;
		this.DATAMAP.put("INSPECTIONDOCUMENTFLAG", INSPECTIONDOCUMENTFLAG);
	}

	// MEASURINGINSTRUMENTFLAG
	public String getMEASURINGINSTRUMENTFLAG() {
		return MEASURINGINSTRUMENTFLAG;
	}
	public void setMEASURINGINSTRUMENTFLAG(String MEASURINGINSTRUMENTFLAG) {
		this.MEASURINGINSTRUMENTFLAG = MEASURINGINSTRUMENTFLAG;
		this.DATAMAP.put("MEASURINGINSTRUMENTFLAG", MEASURINGINSTRUMENTFLAG);
	}

	// MONITORINGRESULT
	public String getMONITORINGRESULT() {
		return MONITORINGRESULT;
	}
	public void setMONITORINGRESULT(String MONITORINGRESULT) {
		this.MONITORINGRESULT = MONITORINGRESULT;
		this.DATAMAP.put("MONITORINGRESULT", MONITORINGRESULT);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PROCESSID == null || this.CHANGEDATE == null || this.CHANGEWORKER == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID,PROCESSID,CHANGEDATE,CHANGEWORKER"});
        }
    }
	
	public void setKEYMAP(HashMap<String, Object> KEYMAP){
        this.KEYMAP = KEYMAP;
    }
    public HashMap<String, Object> getKEYMAP(){
        return KEYMAP;
    }
    public void setDATAMAP(HashMap<String, Object> DATAMAP){
        this.DATAMAP = DATAMAP;
    }
    public HashMap<String, Object> getDATAMAP(){
        return DATAMAP;
    }

    public Object excuteDML(String type){
        return excuteDML(type, "");
    }

    public Object excuteDML(String type, String suffix){
        if ( type.equalsIgnoreCase(SqlConstant.DML_INSERT) || type.equalsIgnoreCase(SqlConstant.DML_UPDATE) || type.equalsIgnoreCase(SqlConstant.DML_DELETE) ){
            checkKeyNotNull();
        }
        Object returnValue = SqlQueryUtil.excuteDML(this.getClass(), type, suffix, KEYMAP, DATAMAP);
        return returnValue;
    }
	
}
