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
public class DY_LINESTOP
{
    // Key Info
	private Integer LINESTOPID;

    // Data Info
	private String PLANTID;
	private String WORKORDER;
	private String EQUIPMENTID;
    private String PROCESSID;
    private Integer LINESTOPCODE;
    private Timestamp LINESTOPSTARTTIME;
    private Timestamp LINESTOPENDTIME;
    private String DESCRIPTION;
    private String RESULT_FLAG;
    private Integer PORESULT_ID;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_LINESTOP() { }

    // Key Methods
    // LINESTOPID
    public Integer getKeyLINESTOPID() {
    	return LINESTOPID;
    }
    public void setKeyLINESTOPID(Integer LINESTOPID) {
    	this.LINESTOPID = LINESTOPID;
    	this.KEYMAP.put("LINESTOPID", LINESTOPID);
    }
    
    
    // Data Methods
    // PLANTID
    public String getPLANTID() {
        return PLANTID;
    }
    public void setPLANTID(String PLANTID) {
        this.PLANTID = PLANTID;
        this.KEYMAP.put("PLANTID", PLANTID);
    }
    

    // WORKORDER
    public String getWORKORDER() {
        return WORKORDER;
    }
    public void setWORKORDER(String WORKORDER) {
        this.WORKORDER = WORKORDER;
        this.KEYMAP.put("WORKORDER", WORKORDER);
    }
    
    // EQUIPMENTID
    public String getEQUIPMENTID() {
        return EQUIPMENTID;
    }
    public void setEQUIPMENTID(String EQUIPMENTID) {
        this.EQUIPMENTID= EQUIPMENTID;
        this.KEYMAP.put("EQUIPMENTID", EQUIPMENTID);
    }

    // PROCESSID
    public String getPROCESSID() {
        return PROCESSID;
    }
    public void setPROCESSID(String PROCESSID) {
        this.PROCESSID = PROCESSID;
        this.DATAMAP.put("PROCESSID", PROCESSID);
    }
    
    // LINESTOPCODE
    public Integer getLINESTOPCODEE() {
        return LINESTOPCODE;
    }
    public void setLINESTOPCODE(Integer LINESTOPCODE) {
        this.LINESTOPCODE = LINESTOPCODE;
        this.DATAMAP.put("LINESTOPCODE", LINESTOPCODE);
    }
    
    // LINESTOPSTARTTIME
    public Timestamp getLINESTOPSTARTTIME() {
        return CREATETIME;
    }
    public void setLINESTOPSTARTTIME(Timestamp LINESTOPSTARTTIME) {
        this.LINESTOPSTARTTIME = LINESTOPSTARTTIME;
        this.DATAMAP.put("LINESTOPSTARTTIME", LINESTOPSTARTTIME);
    }
    
    // LINESTOPENDTIME
    public Timestamp getLINESTOPENDTIME() {
        return LINESTOPENDTIME;
    }
    public void setLINESTOPENDTIME(Timestamp LINESTOPENDTIME) {
        this.LINESTOPENDTIME = LINESTOPENDTIME;
        this.DATAMAP.put("LINESTOPENDTIME", LINESTOPENDTIME);
    }

    // DESCRIPTION
    public String getDESCRIPTION() {
        return DESCRIPTION;
    }
    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
        this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
    }
    
    // RESULT_FLAG
    public String getRESULT_FLAG() {
        return RESULT_FLAG;
    }
    public void setRESULT_FLAG(String RESULT_FLAG) {
        this.RESULT_FLAG = RESULT_FLAG;
        this.DATAMAP.put("RESULT_FLAG", RESULT_FLAG);
    }
    
    //PORESULT_ID
    public Integer getPORESULT_ID() {
        return PORESULT_ID;
    }
    public void setPORESULT_ID(Integer PORESULT_ID) {
        this.PORESULT_ID = PORESULT_ID;
        this.DATAMAP.put("PORESULT_ID", PORESULT_ID);
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
        if ( this.KEYMAP.isEmpty() || this.LINESTOPID == null )
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"LINESTOPID "});
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
