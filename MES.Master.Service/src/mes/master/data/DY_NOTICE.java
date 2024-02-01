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
public class DY_NOTICE
{
    // Key Info
    private String PLANTID;
    private String NOTICEID;

    
    
    // Data Info
    private String TITLE;
    private String CONTENTS;
    private String TYPE;
    private int SEARCHCOUNT;
    private String DISPLAYFLAG;
    private String DESCRIPTION;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;
    

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_NOTICE() { }

    // Key Methods
    // PLANT_ID
    public String getKeyPLANTID() {
        return PLANTID;
    }
    public void setKeyPLANTID(String PLANTID) {
        this.PLANTID = PLANTID;
        this.KEYMAP.put("PLANTID", PLANTID);
    }
    
    // NOTICEID
    public String getKeyNOTICEID() {
        return NOTICEID;
    }
    public void setKeyNOTICEID(String NOTICEID) {
        this.NOTICEID = NOTICEID;
        this.KEYMAP.put("NOTICEID", NOTICEID);
    }
    
    // TITLE
    public String getTITLE() {
        return TITLE;
    }
    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
        this.DATAMAP.put("TITLE", TITLE);
    }
    
    // CONTENTS
    public String getCONTENTS() {
    	return CONTENTS;
    }
    public void setCONTENTS(String CONTENTS) {
    	this.CONTENTS = CONTENTS;
    	this.DATAMAP.put("CONTENTS", CONTENTS);
    }
    
    // Data Methods
    // TYPE
    public String getTYPE() {
        return TYPE;
    }
    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
        this.DATAMAP.put("TYPE", TYPE);
    }
    
    // SEARCHCOUNT
    public int getSEARCHCOUNT() {
        return SEARCHCOUNT;
    }
    public void setSEARCHCOUNT(int SEARCHCOUNT) {
        this.SEARCHCOUNT = SEARCHCOUNT;
        this.DATAMAP.put("SEARCHCOUNT", SEARCHCOUNT);
    }
    
    // DISPLAYFLAG
    public String getDISPLAYFLAG() {
        return DISPLAYFLAG;
    }
    public void setDISPLAYFLAG(String DISPLAYFLAG) {
        this.DISPLAYFLAG = DISPLAYFLAG;
        this.DATAMAP.put("DISPLAYFLAG", DISPLAYFLAG);
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
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.NOTICEID == null )
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, NOTICEID"});
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
