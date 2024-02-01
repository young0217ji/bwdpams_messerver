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
public class SF_ROLEUSERMAP
{
    // Key Info
    private String PLANTID;
    private String ROLEID;
    private String USERID;
    
    // Data Info
    private String USEFLAG;
    private String CREATEUSERID;
    private String LASTUPDATEUSERID;
    private Timestamp CREATETIME;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public SF_ROLEUSERMAP() { }

    // Key Methods
    // PLANT_ID
    public String getKeyPLANTID() {
    	return PLANTID;
    }
    public void setKeyPLANTID(String PLANTID) {
    	this.PLANTID = PLANTID;
    	this.KEYMAP.put("PLANTID", PLANTID);
    }
    
    // USER_ID
    public String getKeyUSERID() {
        return USERID;
    }
    public void setKeyUSERID(String USERID) {
        this.USERID = USERID;
        this.KEYMAP.put("USERID", USERID);
    }
    
    // ROLE_ID
    public String getKeyROLEID() {
        return USERID;
    }
    public void setKeyROLEID(String ROLEID) {
        this.ROLEID = ROLEID;
        this.KEYMAP.put("ROLEID", ROLEID);
    }
    
    
    // Data Methods   
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
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.ROLEID == null || this.USERID == null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, ROLEID, USERID"});
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
