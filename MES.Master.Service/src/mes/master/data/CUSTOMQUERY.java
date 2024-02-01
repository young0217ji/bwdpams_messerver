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
public class CUSTOMQUERY
{
    // Key Info
	private String PLANTID;
    private String QUERYID;
    private String QUERYVERSION;

    // Data Info
    private String QUERYSTRING;
    private String QUERYTYPE;
    private String DESCRIPTION;
    private String PROGRAMID;
    private String FORMID;
    private String MENUID;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private Long QUERYCOUNT;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public CUSTOMQUERY() { }

    // Key Methods
    // PLANTID
    public String getKeyPLANTID() {
        return PLANTID;
    }
    public void setKeyPLANTID(String PLANTID) {
        this.PLANTID = PLANTID;
        this.KEYMAP.put("PLANTID", PLANTID);
    }
    
    // QUERYID
    public String getKeyQUERYID() {
        return QUERYID;
    }
    public void setKeyQUERYID(String QUERYID) {
        this.QUERYID = QUERYID;
        this.KEYMAP.put("QUERYID", QUERYID);
    }

    // QUERYVERSION
    public String getKeyQUERYVERSION() {
        return QUERYVERSION;
    }
    public void setKeyQUERYVERSION(String QUERYVERSION) {
        this.QUERYVERSION = QUERYVERSION;
        this.KEYMAP.put("QUERYVERSION", QUERYVERSION);
    }


    // Data Methods
    // QUERYSTRING
    public String getQUERYSTRING() {
        return QUERYSTRING;
    }
    public void setQUERYSTRING(String QUERYSTRING) {
        this.QUERYSTRING = QUERYSTRING;
        this.DATAMAP.put("QUERYSTRING", QUERYSTRING);
    }
    
    // QUERYTYPE
    public String getQUERYTYPE() {
        return QUERYTYPE;
    }
    public void setQUERYTYPE(String QUERYTYPE) {
        this.QUERYTYPE = QUERYTYPE;
        this.DATAMAP.put("QUERYTYPE", QUERYTYPE);
    }

    // DESCRIPTION
    public String getDESCRIPTION() {
        return DESCRIPTION;
    }
    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
        this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
    }
    
    // PROGRAMID
    public String getPROGRAMID() {
        return PROGRAMID;
    }
    public void setPROGRAMID(String PROGRAMID) {
        this.PROGRAMID = PROGRAMID;
        this.DATAMAP.put("PROGRAMID", PROGRAMID);
    }

    // FORMID
    public String getFORMID() {
        return FORMID;
    }
    public void setFORMID(String FORMID) {
        this.FORMID = FORMID;
        this.DATAMAP.put("FORMID", FORMID);
    }

    // MENUID
    public String getMENUID() {
        return MENUID;
    }
    public void setMENUID(String MENUID) {
        this.MENUID = MENUID;
        this.DATAMAP.put("MENUID", MENUID);
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
    
    // QUERYCOUNT
    public Long getQUERYCOUNT()
    {
        return QUERYCOUNT;
    }
    public void setQUERYCOUNT(Long QUERYCOUNT)
    {
        this.QUERYCOUNT = QUERYCOUNT;
        this.DATAMAP.put("QUERYCOUNT", QUERYCOUNT);
    }

    // Key Validation
    public void checkKeyNotNull() 
    {
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.QUERYID == null || this.QUERYVERSION == null )
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, QUERYID, QUERYVERSION"});
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
