package mes.master.data;

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
public class DISPATCHEVENT
{
    // Key Info
	private String PLANTID;
    private String SERVERNAME;
    private String EVENTNAME;

    // Data Info
    private String CLASSNAME;
    private String TARGETSUBJECTNAME;
    private String TARGETPLANT;
    private String LOGSAVEFLAG;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DISPATCHEVENT() { }

    // Key Methods
    // PLANTID
    public String getKeyPLANTID() {
        return PLANTID;
    }
    public void setKeyPLANTID(String PLANTID) {
        this.PLANTID = PLANTID;
        this.KEYMAP.put("PLANTID", PLANTID);
    }
    
    // SERVERNAME
    public String getKeySERVERNAME() {
        return SERVERNAME;
    }
    public void setKeySERVERNAME(String SERVERNAME) {
        this.SERVERNAME = SERVERNAME;
        this.KEYMAP.put("SERVERNAME", SERVERNAME);
    }

    // EVENTNAME
    public String getKeyEVENTNAME() {
        return EVENTNAME;
    }
    public void setKeyEVENTNAME(String EVENTNAME) {
        this.EVENTNAME = EVENTNAME;
        this.KEYMAP.put("EVENTNAME", EVENTNAME);
    }


    // Data Methods
    // CLASSNAME
    public String getCLASSNAME() {
        return CLASSNAME;
    }
    public void setCLASSNAME(String CLASSNAME) {
        this.CLASSNAME = CLASSNAME;
        this.DATAMAP.put("CLASSNAME", CLASSNAME);
    }

    // TARGETSUBJECTNAME
    public String getTARGETSUBJECTNAME() {
        return TARGETSUBJECTNAME;
    }
    public void setTARGETSUBJECTNAME(String TARGETSUBJECTNAME) {
        this.TARGETSUBJECTNAME = TARGETSUBJECTNAME;
        this.DATAMAP.put("TARGETSUBJECTNAME", TARGETSUBJECTNAME);
    }
    
    // TARGETPLANT
    public String getTARGETPLANT() {
        return TARGETPLANT;
    }
    public void setTARGETPLANT(String TARGETPLANT) {
        this.TARGETPLANT = TARGETPLANT;
        this.DATAMAP.put("TARGETPLANT", TARGETPLANT);
    }
    
    // LOGSAVEFLAG
    public String getLOGSAVEFLAG() {
        return LOGSAVEFLAG;
    }
    public void setLOGSAVEFLAG(String LOGSAVEFLAG) {
        this.LOGSAVEFLAG = LOGSAVEFLAG;
        this.DATAMAP.put("LOGSAVEFLAG", LOGSAVEFLAG);
    }
    

    // Key Validation
    public void checkKeyNotNull() 
    {
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.SERVERNAME == null || this.EVENTNAME == null )
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, SERVERNAME, EVENTNAME"});
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
