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
public class DY_DISPLAYMANAGEMENT
{
    // Key Info
    private String PLANTID;
    private String PROCESSID;
    private String EQUIPMENTID;
    private String SEQUENCE;
    
    // Data Info
    private String USEFLAG;
    private String DISPLAYTYPE;
    private String FILEPATH;
    private String ORIGINALFILEPATH;
    private String PUBLICFLAG;
    private String DISPLAYPROCESSNAME;
    private String DISPLAYEQUIPMENTNAME;
    private String STARTTIME;
    private String ENDTIME;
    private String DAYFLAG;
    private Double DISPLAYTIME;
    private String DESCRIPTION;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;
    

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_DISPLAYMANAGEMENT() { }

    // Key Methods
    // PLANT_ID
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
    
    // EQUIPMENTID
    public String getKeyEQUIPMENTID() {
        return EQUIPMENTID;
    }
    public void setKeyEQUIPMENTID(String EQUIPMENTID) {
        this.EQUIPMENTID = EQUIPMENTID;
        this.KEYMAP.put("EQUIPMENTID", EQUIPMENTID);
    }
    
    // SEQUENCE
    public String getKeySEQUENCE() {
    	return SEQUENCE;
    }
    public void setKeySEQUENCE(String SEQUENCE) {
    	this.SEQUENCE = SEQUENCE;
    	this.KEYMAP.put("SEQUENCE", SEQUENCE);
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
    
    // DISPLAYTYPE
    public String getDISPLAYTYPE() {
        return DISPLAYTYPE;
    }
    public void setDISPLAYTYPE(String DISPLAYTYPE) {
        this.DISPLAYTYPE = DISPLAYTYPE;
        this.DATAMAP.put("DISPLAYTYPE", DISPLAYTYPE);
    }
    
    // FILEPATH
    public String getFILEPATH() {
        return FILEPATH;
    }
    public void setFILEPATH(String FILEPATH) {
        this.FILEPATH = FILEPATH;
        this.DATAMAP.put("FILEPATH", FILEPATH);
    }
    
    // ORIGINALFILEPATH
    public String getORIGINALFILEPATH() {
        return ORIGINALFILEPATH;
    }
    public void setORIGINALFILEPATH(String ORIGINALFILEPATH) {
        this.ORIGINALFILEPATH = ORIGINALFILEPATH;
        this.DATAMAP.put("ORIGINALFILEPATH", ORIGINALFILEPATH);
    }
    
    // PUBLICFLAG
    public String getPUBLICFLAG() {
    	return PUBLICFLAG;
    }
    public void setPUBLICFLAG(String PUBLICFLAG) {
    	this.PUBLICFLAG = PUBLICFLAG;
    	this.DATAMAP.put("PUBLICFLAG", PUBLICFLAG);
    }
    
    // DISPLAYPROCESSNAME
    public String getDISPLAYPROCESSNAME() {
    	return DISPLAYPROCESSNAME;
    }
    public void setDISPLAYPROCESSNAME(String DISPLAYPROCESSNAME) {
    	this.DISPLAYPROCESSNAME = DISPLAYPROCESSNAME;
    	this.DATAMAP.put("DISPLAYPROCESSNAME", DISPLAYPROCESSNAME);
    }
    
    // DISPLAYEQUIPMENTNAME
    public String getDISPLAYEQUIPMENTNAME() {
    	return DISPLAYEQUIPMENTNAME;
    }
    public void setDISPLAYEQUIPMENTNAME(String DISPLAYEQUIPMENTNAME) {
    	this.DISPLAYEQUIPMENTNAME = DISPLAYEQUIPMENTNAME;
    	this.DATAMAP.put("DISPLAYEQUIPMENTNAME", DISPLAYEQUIPMENTNAME);
    }
    
    // STARTTIME
    public String getSTARTTIME() {
    	return STARTTIME;
    }
    public void setSTARTTIME(String STARTTIME) {
    	this.STARTTIME = STARTTIME;
    	this.DATAMAP.put("STARTTIME", STARTTIME);
    }
    
    // ENDTIME
    public String getENDTIME() {
    	return ENDTIME;
    }
    public void setENDTIME(String ENDTIME) {
    	this.ENDTIME = ENDTIME;
    	this.DATAMAP.put("ENDTIME", ENDTIME);
    }
    
    // DAYFLAG
    public String getDAYFLAG() {
    	return DAYFLAG;
    }
    public void setDAYFLAG(String DAYFLAG) {
    	this.DAYFLAG = DAYFLAG;
    	this.DATAMAP.put("DAYFLAG", DAYFLAG);
    }
    
    // DISPLAYTIME
    public Double getDISPLAYTIME() {
    	return DISPLAYTIME;
    }
    public void setDISPLAYTIME(Double DISPLAYTIME) {
    	this.DISPLAYTIME = DISPLAYTIME;
    	this.DATAMAP.put("DISPLAYTIME", DISPLAYTIME);
    }
    
    // DESCRIPTION
    public String getDESCRIPTION() {
    	return DESCRIPTION;
    }
    public void setDESCRIPTION(String DESCRIPTION) {
    	this.DESCRIPTION = DESCRIPTION;
    	this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
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

    // Key Validation
    public void checkKeyNotNull() 
    {
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.PROCESSID == null || this.EQUIPMENTID == null || this.SEQUENCE == null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PROCESSID, EQUIPMENTID, SEQUENCE"});
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
