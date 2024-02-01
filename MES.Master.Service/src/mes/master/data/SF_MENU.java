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
public class SF_MENU
{
    // Key Info
	private String PLANTID;
    private String MENUID;
    
    // Data Info
    private String MENUNAME;
    private String PARENTSID;
    private String MENUTYPE;
    private String URL;
    private Long POSITION;
    private String MENUNAME_EN;
    private String ICON_CLASS;
    private String USEFLAG;
    private String CREATEUSERID;
    private String LASTUPDATEUSERID;
    private Timestamp CREATETIME;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public SF_MENU() { }

    // Key Methods
    // PLANTID
    public String getKeyPLANTID() {
        return PLANTID;
    }
    public void setKeyPLANTID(String PLANTID) {
        this.PLANTID = PLANTID;
        this.KEYMAP.put("PLANTID", PLANTID);
    }
    
    // MENUID
    public String getKeyMENUID() {
        return MENUID;
    }
    public void setKeyMENUID(String MENUID) {
        this.MENUID = MENUID;
        this.KEYMAP.put("MENUID", MENUID);
    }

    // Data Methods
    // MENUNAME
    public String getMENUNAME() {
        return MENUNAME;
    }
    public void setMENUNAME(String MENUNAME) {
        this.MENUNAME = MENUNAME;
        this.DATAMAP.put("MENUNAME", MENUNAME);
    }
    
    // PARENTSID
    public String getPARENTSID() {
        return PARENTSID;
    }
    public void setPARENTSID(String PARENTSID) {
        this.PARENTSID = PARENTSID;
        this.DATAMAP.put("PARENTSID", PARENTSID);
    }
    
    // MENUTYPE
    public String getMENUTYPE() {
        return MENUTYPE;
    }
    public void setMENUTYPE(String MENUTYPE) {
        this.MENUTYPE = MENUTYPE;
        this.DATAMAP.put("MENUTYPE", MENUTYPE);
    }
    
    // URL
    public String getURL() {
        return URL;
    }
    public void setURL(String URL) {
        this.URL = URL;
        this.DATAMAP.put("URL", URL);
    }
    
    // POSITION
    public Long getPOSITION() {
        return POSITION;
    }
    public void setPOSITION(Long POSITION) {
        this.POSITION = POSITION;
        this.DATAMAP.put("POSITION", POSITION);
    }
    
    // MENUNAME_EN
    public String getMENUNAME_EN() {
        return MENUNAME_EN;
    }
    public void setMENUNAME_EN(String MENUNAME_EN) {
        this.MENUNAME_EN = MENUNAME_EN;
        this.DATAMAP.put("MENUNAME_EN", MENUNAME_EN);
    }
    
    // ICON_CLASS
    public String getICON_CLASS() {
        return ICON_CLASS;
    }
    public void setICON_CLASS(String ICON_CLASS) {
        this.ICON_CLASS = ICON_CLASS;
        this.DATAMAP.put("ICON_CLASS", ICON_CLASS);
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
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.MENUID == null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, MENUID"});
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
