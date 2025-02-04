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
public class USERDEFINITION
{
    // Key Info
    private String USERID;

    // Data Info
    private String USERPASSWORD;
    private String USERNAME;
    private String PLANTID;
    private String USERGROUPID;
    private String AREAID;
    private String DEPARTMENT;
    private String WORKPOSITION;
    private String EMAIL;
    private Timestamp LASTUPDATETIME;
    private String LASTUPDATEUSERID;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public USERDEFINITION() { }

    // Key Methods
    // USERID
    public String getKeyUSERID()
    {
        return USERID;
    }
    public void setKeyUSERID(String USERID)
    {
        this.USERID = USERID;
        this.KEYMAP.put("USERID", USERID);
    }


    // Data Methods
    // USERPASSWORD
    public String getUSERPASSWORD()
    {
        return USERPASSWORD;
    }
    public void setUSERPASSWORD(String USERPASSWORD)
    {
        this.USERPASSWORD = USERPASSWORD;
        this.DATAMAP.put("USERPASSWORD", USERPASSWORD);
    }

    // USERNAME
    public String getUSERNAME()
    {
        return USERNAME;
    }
    public void setUSERNAME(String USERNAME)
    {
        this.USERNAME = USERNAME;
        this.DATAMAP.put("USERNAME", USERNAME);
    }

    // PLANTID
    public String getPLANTID()
    {
        return PLANTID;
    }
    public void setPLANTID(String PLANTID)
    {
        this.PLANTID = PLANTID;
        this.DATAMAP.put("PLANTID", PLANTID);
    }

    // USERGROUPID
    public String getUSERGROUPID()
    {
        return USERGROUPID;
    }
    public void setUSERGROUPID(String USERGROUPID)
    {
        this.USERGROUPID = USERGROUPID;
        this.DATAMAP.put("USERGROUPID", USERGROUPID);
    }

    // AREAID
    public String getAREAID()
    {
        return AREAID;
    }
    public void setAREAID(String AREAID)
    {
        this.AREAID = AREAID;
        this.DATAMAP.put("AREAID", AREAID);
    }

    // DEPARTMENT
    public String getDEPARTMENT()
    {
        return DEPARTMENT;
    }
    public void setDEPARTMENT(String DEPARTMENT)
    {
        this.DEPARTMENT = DEPARTMENT;
        this.DATAMAP.put("DEPARTMENT", DEPARTMENT);
    }

    // WORKPOSITION
    public String getWORKPOSITION()
    {
        return WORKPOSITION;
    }
    public void setWORKPOSITION(String WORKPOSITION)
    {
        this.WORKPOSITION = WORKPOSITION;
        this.DATAMAP.put("WORKPOSITION", WORKPOSITION);
    }

    // EMAIL
    public String getEMAIL()
    {
        return EMAIL;
    }
    public void setEMAIL(String EMAIL)
    {
        this.EMAIL = EMAIL;
        this.DATAMAP.put("EMAIL", EMAIL);
    }

    // LASTUPDATETIME
    public Timestamp getLASTUPDATETIME()
    {
        return LASTUPDATETIME;
    }
    public void setLASTUPDATETIME(Timestamp LASTUPDATETIME)
    {
        this.LASTUPDATETIME = LASTUPDATETIME;
        this.DATAMAP.put("LASTUPDATETIME", LASTUPDATETIME);
    }

    // LASTUPDATEUSERID
    public String getLASTUPDATEUSERID()
    {
        return LASTUPDATEUSERID;
    }
    public void setLASTUPDATEUSERID(String LASTUPDATEUSERID)
    {
        this.LASTUPDATEUSERID = LASTUPDATEUSERID;
        this.DATAMAP.put("LASTUPDATEUSERID", LASTUPDATEUSERID);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.USERID == null)
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