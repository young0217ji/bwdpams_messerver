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
public class ENUMDEFINITION
{
    // Key Info
	private String PLANTID;
    private String ENUMID;

    // Data Info
    private String ENUMNAME;
    private String ACCESSTYPE;
    private String ENUMGROUP;
    private String CONSTANTFLAG;
    private String KOREAN;
    private String ENGLISH;
    private String NATIVE1;
    private String NATIVE2;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public ENUMDEFINITION() { }

    // Key Methods
    // PLANTID
    public String getKeyPLANTID() {
        return PLANTID;
    }
    public void setKeyPLANTID(String PLANTID) {
        this.PLANTID = PLANTID;
        this.KEYMAP.put("PLANTID", PLANTID);
    }
    
    // ENUMID
    public String getKeyENUMID() {
        return ENUMID;
    }
    public void setKeyENUMID(String ENUMID) {
        this.ENUMID = ENUMID;
        this.KEYMAP.put("ENUMID", ENUMID);
    }


    // Data Methods
    // ENUMNAME
    public String getENUMNAME() {
        return ENUMNAME;
    }
    public void setENUMNAME(String ENUMNAME) {
        this.ENUMNAME = ENUMNAME;
        this.DATAMAP.put("ENUMNAME", ENUMNAME);
    }

    // ACCESSTYPE
    public String getACCESSTYPE() {
        return ACCESSTYPE;
    }
    public void setACCESSTYPE(String ACCESSTYPE) {
        this.ACCESSTYPE = ACCESSTYPE;
        this.DATAMAP.put("ACCESSTYPE", ACCESSTYPE);
    }

    // ENUMGROUP
    public String getENUMGROUP() {
        return ENUMGROUP;
    }
    public void setENUMGROUP(String ENUMGROUP) {
        this.ENUMGROUP = ENUMGROUP;
        this.DATAMAP.put("ENUMGROUP", ENUMGROUP);
    }

    // CONSTANTFLAG
    public String getCONSTANTFLAG() {
        return CONSTANTFLAG;
    }
    public void setCONSTANTFLAG(String CONSTANTFLAG) {
        this.CONSTANTFLAG = CONSTANTFLAG;
        this.DATAMAP.put("CONSTANTFLAG", CONSTANTFLAG);
    }
    
    // KOREAN
    public String getKOREAN() {
        return KOREAN;
    }
    public void setKOREAN(String KOREAN) {
        this.KOREAN = KOREAN;
        this.DATAMAP.put("KOREAN", KOREAN);
    }
    
    // ENGLISH
    public String getENGLISH() {
        return ENGLISH;
    }
    public void setENGLISH(String ENGLISH) {
        this.ENGLISH = ENGLISH;
        this.DATAMAP.put("ENGLISH", ENGLISH);
    }
    
    // NATIVE1
    public String getNATIVE1() {
        return NATIVE1;
    }
    public void setNATIVE1(String NATIVE1) {
        this.NATIVE1 = NATIVE1;
        this.DATAMAP.put("NATIVE1", NATIVE1);
    }
    
    // NATIVE2
    public String getNATIVE2() {
        return NATIVE2;
    }
    public void setNATIVE2(String NATIVE2) {
        this.NATIVE2 = NATIVE2;
        this.DATAMAP.put("NATIVE2", NATIVE2);
    }
    

    // Key Validation
    public void checkKeyNotNull() 
    {
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null || this.ENUMID == null )
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, ENUMID"});
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
