package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.12.21 
 * 
 * @see
 */
public class DY_PARTINFORMATION
{
    // Key Info
    private String PLANTID;
    private String WORKCENTER;
    private String PRODUCTID;
    
    // Data Info
    private String FIRSTPARTINSPECTION;
    private String SECONDPARTINSPECTION;
    private String JIGNUMBER;
    private String DESCRIPTION;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_PARTINFORMATION() { }

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
    
    // PRODUCTID
    public String getKeyPRODUCTID() {
        return PRODUCTID;
    }
    public void setKeyPRODUCTID(String PRODUCTID) {
        this.PRODUCTID = PRODUCTID;
        this.KEYMAP.put("PRODUCTID", PRODUCTID);
    }
    
    // Data Methods
    // FIRSTPARTINSPECTION
    public String getFIRSTPARTINSPECTION() {
        return FIRSTPARTINSPECTION;
    }
    public void setFIRSTPARTINSPECTION(String FIRSTPARTINSPECTION) {
        this.FIRSTPARTINSPECTION = FIRSTPARTINSPECTION;
        this.DATAMAP.put("FIRSTPARTINSPECTION", FIRSTPARTINSPECTION);
    }
    
    // SECONDPARTINSPECTION
    public String getSECONDPARTINSPECTION() {
        return SECONDPARTINSPECTION;
    }
    public void setSECONDPARTINSPECTION(String SECONDPARTINSPECTION) {
        this.SECONDPARTINSPECTION = SECONDPARTINSPECTION;
        this.DATAMAP.put("SECONDPARTINSPECTION", SECONDPARTINSPECTION);
    }
    
    // JIGNUMBER
    public String getJIGNUMBER() {
        return JIGNUMBER;
    }
    public void setJIGNUMBER(String JIGNUMBER) {
        this.JIGNUMBER = JIGNUMBER;
        this.DATAMAP.put("JIGNUMBER", JIGNUMBER);
    }
    
    // DESCRIPTION
    public String getDESCRIPTION() {
        return DESCRIPTION;
    }
    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
        this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
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
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.WORKCENTER == null || this.PRODUCTID == null )
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, WORKCENTER, PRODUCTID"});
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
