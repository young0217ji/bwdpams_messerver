package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.12.05 
 * 
 * @see
 */
public class DY_BOARDOPTIONSET
{
    // Key Info
    private String PLANTID;
    private String WORKCENTER;
    private String MANAGEMENTID;
    
    // Data Info
    private String BEFOREBACKCOLOR;
    private String BEFOREFONTCOLOR;
    private String WORKINGBACKCOLOR;
    private String WORKINGFONTCOLOR;
    private String AFTERBACKCOLOR;
    private String AFTERFONTCOLOR;
    private String PAUSEBACKCOLOR;
    private String PAUSEFONTCOLOR;
    private String STOPBACKCOLOR;
    private String STOPFONTCOLOR;
    private String NEXTBACKCOLOR;
    private String NEXTFONTCOLOR;
    private String MESSAGEFLAG;
    private Long VISUALRATE;
    private Long NEXTCOUNT;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_BOARDOPTIONSET() { }

    // Key Methods
    // PLANT_ID
    public String getKeyPLANTID() {
        return PLANTID;
    }
    public void setKeyPLANTID(String PLANTID) {
        this.PLANTID = PLANTID;
        this.KEYMAP.put("PLANTID", PLANTID);
    }
    
    // WORKCENTER
    public String getKeyWORKCENTER() {
        return WORKCENTER;
    }
    public void setKeyWORKCENTER(String WORKCENTER) {
        this.WORKCENTER = WORKCENTER;
        this.KEYMAP.put("WORKCENTER", WORKCENTER);
    }
    
    // MANAGEMENTID
    public String getKeyMANAGEMENTID() {
        return MANAGEMENTID;
    }
    public void setKeyMANAGEMENTID(String MANAGEMENTID) {
        this.MANAGEMENTID = MANAGEMENTID;
        this.KEYMAP.put("MANAGEMENTID", MANAGEMENTID);
    }
    
    // Data Methods
    // BEFOREBACKCOLOR
    public String getBEFOREBACKCOLOR() {
        return BEFOREBACKCOLOR;
    }
    public void setBEFOREBACKCOLOR(String BEFOREBACKCOLOR) {
        this.BEFOREBACKCOLOR = BEFOREBACKCOLOR;
        this.DATAMAP.put("BEFOREBACKCOLOR", BEFOREBACKCOLOR);
    }
    
    // BEFOREFONTCOLOR
    public String getBEFOREFONTCOLOR() {
        return BEFOREFONTCOLOR;
    }
    public void setBEFOREFONTCOLOR(String BEFOREFONTCOLOR) {
        this.BEFOREFONTCOLOR = BEFOREFONTCOLOR;
        this.DATAMAP.put("BEFOREFONTCOLOR", BEFOREFONTCOLOR);
    }
    
    // WORKINGBACKCOLOR
    public String getWORKINGBACKCOLOR() {
        return WORKINGBACKCOLOR;
    }
    public void setWORKINGBACKCOLOR(String WORKINGBACKCOLOR) {
        this.WORKINGBACKCOLOR = WORKINGBACKCOLOR;
        this.DATAMAP.put("WORKINGBACKCOLOR", WORKINGBACKCOLOR);
    }
    
    // WORKINGFONTCOLOR
    public String getWORKINGFONTCOLOR() {
        return WORKINGFONTCOLOR;
    }
    public void setWORKINGFONTCOLOR(String WORKINGFONTCOLOR) {
        this.WORKINGFONTCOLOR = WORKINGFONTCOLOR;
        this.DATAMAP.put("WORKINGFONTCOLOR", WORKINGFONTCOLOR);
    }
    
    // AFTERBACKCOLOR
    public String getAFTERBACKCOLOR() {
        return AFTERBACKCOLOR;
    }
    public void setAFTERBACKCOLOR(String AFTERBACKCOLOR) {
        this.AFTERBACKCOLOR = AFTERBACKCOLOR;
        this.DATAMAP.put("AFTERBACKCOLOR", AFTERBACKCOLOR);
    }
    
    // AFTERFONTCOLOR
    public String getAFTERFONTCOLOR() {
        return AFTERFONTCOLOR;
    }
    public void setAFTERFONTCOLOR(String AFTERFONTCOLOR) {
        this.AFTERFONTCOLOR = AFTERFONTCOLOR;
        this.DATAMAP.put("AFTERFONTCOLOR", AFTERFONTCOLOR);
    }
    
    // PAUSEBACKCOLOR
    public String getPAUSEBACKCOLOR() {
        return PAUSEBACKCOLOR;
    }
    public void setPAUSEBACKCOLOR(String PAUSEBACKCOLOR) {
        this.PAUSEBACKCOLOR = PAUSEBACKCOLOR;
        this.DATAMAP.put("PAUSEBACKCOLOR", PAUSEBACKCOLOR);
    }
    
    // PAUSEFONTCOLOR
    public String getPAUSEFONTCOLOR() {
        return PAUSEFONTCOLOR;
    }
    public void setPAUSEFONTCOLOR(String PAUSEFONTCOLOR) {
        this.PAUSEFONTCOLOR = PAUSEFONTCOLOR;
        this.DATAMAP.put("PAUSEFONTCOLOR", PAUSEFONTCOLOR);
    }
    
    // STOPBACKCOLOR
    public String getSTOPBACKCOLOR() {
        return STOPBACKCOLOR;
    }
    public void setSTOPBACKCOLOR(String STOPBACKCOLOR) {
        this.STOPBACKCOLOR = STOPBACKCOLOR;
        this.DATAMAP.put("STOPBACKCOLOR", STOPBACKCOLOR);
    }
    
    // STOPFONTCOLOR
    public String getSTOPFONTCOLOR() {
        return STOPFONTCOLOR;
    }
    public void setSTOPFONTCOLOR(String STOPFONTCOLOR) {
        this.STOPFONTCOLOR = STOPFONTCOLOR;
        this.DATAMAP.put("STOPFONTCOLOR", STOPFONTCOLOR);
    }
    
    // NEXTBACKCOLOR
    public String getNEXTBACKCOLOR() {
        return NEXTBACKCOLOR;
    }
    public void setNEXTBACKCOLOR(String NEXTBACKCOLOR) {
        this.NEXTBACKCOLOR = NEXTBACKCOLOR;
        this.DATAMAP.put("NEXTBACKCOLOR", NEXTBACKCOLOR);
    }
    
    // NEXTFONTCOLOR
    public String getNEXTFONTCOLOR() {
        return NEXTFONTCOLOR;
    }
    public void setNEXTFONTCOLOR(String NEXTFONTCOLOR) {
        this.NEXTFONTCOLOR = NEXTFONTCOLOR;
        this.DATAMAP.put("NEXTFONTCOLOR", NEXTFONTCOLOR);
    }
    
    // MESSAGEFLAG
    public String getMESSAGEFLAG() {
        return MESSAGEFLAG;
    }
    public void setMESSAGEFLAG(String MESSAGEFLAG) {
        this.MESSAGEFLAG = MESSAGEFLAG;
        this.DATAMAP.put("MESSAGEFLAG", MESSAGEFLAG);
    }
    
    // VISUALRATE
    public Long getVISUALRATE() {
        return VISUALRATE;
    }
    public void setVISUALRATE(Long VISUALRATE) {
        this.VISUALRATE = VISUALRATE;
        this.DATAMAP.put("VISUALRATE", VISUALRATE);
    }
    
    // NEXTCOUNT
    public Long getNEXTCOUNT() {
        return NEXTCOUNT;
    }
    public void setNEXTCOUNT(Long NEXTCOUNT) {
        this.NEXTCOUNT = NEXTCOUNT;
        this.DATAMAP.put("NEXTCOUNT", NEXTCOUNT);
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
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.WORKCENTER == null || this.MANAGEMENTID == null )
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, WORKCENTER, MANAGEMENTID"});
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
