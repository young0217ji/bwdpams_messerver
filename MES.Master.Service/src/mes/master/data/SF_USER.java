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
public class SF_USER
{
    // Key Info
    private String USERID;
    
    // Data Info
    private String PASSWORD;
    private String USERNAME;
    private String EMAIL;
    private String PHONE_NUMBER;
    private String ACCESS_TOKEN;
    private String REFRESH_TOKEN;
    private String USEFLAG;
    private String CREATEUSERID;
    private String LASTUPDATEUSERID;
    private Timestamp CREATETIME;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public SF_USER() { }

    // Key Methods
    // USER_ID
    public String getKeyUSERID() {
        return USERID;
    }
    public void setKeyUSERID(String USERID) {
        this.USERID = USERID;
        this.KEYMAP.put("USERID", USERID);
    }
    
    // Data Methods
    // PASSWROD
    public String getPASSWORD() {
        return PASSWORD;
    }
    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
        this.DATAMAP.put("PASSWORD", PASSWORD);
    }
    
    // USER_NAME
    public String getUSERNAME() {
        return USERNAME;
    }
    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
        this.DATAMAP.put("USERNAME", USERNAME);
    }
    
    // EMAIL
    public String getEMAIL() {
        return EMAIL;
    }
    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
        this.DATAMAP.put("EMAIL", EMAIL);
    }
    
    // PHONE_NUMBER
    public String getPHONE_NUMBER() {
    	return PHONE_NUMBER;
    }
    public void setPHONE_NUMBER(String PHONE_NUMBER) {
    	this.PHONE_NUMBER = PHONE_NUMBER;
    	this.DATAMAP.put("PHONE_NUMBER", PHONE_NUMBER);
    }
    
    // ACCESS_TOKEN
    public String getACCESS_TOKEN() {
    	return ACCESS_TOKEN;
    }
    public void setACCESS_TOKEN(String ACCESS_TOKEN) {
    	this.ACCESS_TOKEN = ACCESS_TOKEN;
    	this.DATAMAP.put("ACCESS_TOKEN", ACCESS_TOKEN);
    }
    
    // PHONE_NUMBER
    public String getREFRESH_TOKEN() {
    	return REFRESH_TOKEN;
    }
    public void setREFRESH_TOKEN(String REFRESH_TOKEN) {
    	this.REFRESH_TOKEN = REFRESH_TOKEN;
    	this.DATAMAP.put("REFRESH_TOKEN", REFRESH_TOKEN);
    }
    
    // USEFLAG
    public String getUSEFLAG() {
        return USEFLAG;
    }
    public void setUSEFLAG(String USEFLAG) {
        this.USEFLAG = USEFLAG;
        this.DATAMAP.put("USEFLAG", USEFLAG);
    }
    
    // CREATEUSERID
    public String getCREATEUSERID() {
        return CREATEUSERID;
    }
    public void setCREATEUSERID(String CREATEUSERID) {
        this.CREATEUSERID = CREATEUSERID;
        this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
    }
    
    // LASTUPDATEUSERID
    public String getLASTUPDATEUSERID() {
        return LASTUPDATEUSERID;
    }
    public void setLASTUPDATEUSERID(String LASTUPDATEUSERID) {
        this.LASTUPDATEUSERID = LASTUPDATEUSERID;
        this.DATAMAP.put("LASTUPDATEUSERID", LASTUPDATEUSERID);
    }
    
    // CREATETIME
    public Timestamp getCREATETIME() {
        return CREATETIME;
    }
    public void setCREATETIME(Timestamp CREATETIME) {
        this.CREATETIME = CREATETIME;
        this.DATAMAP.put("CREATETIME", CREATETIME);
    }
    
    // LASTUPDATETIME
    public Timestamp getLASTUPDATETIME() {
        return LASTUPDATETIME;
    }
    public void setLASTUPDATETIME(Timestamp LASTUPDATETIME) {
        this.LASTUPDATETIME = LASTUPDATETIME;
        this.DATAMAP.put("LASTUPDATETIME", LASTUPDATETIME);
    }
    

    // Key Validation
    public void checkKeyNotNull() 
    {
        if ( this.KEYMAP.isEmpty() || this.USERID == null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"USERID"});
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
