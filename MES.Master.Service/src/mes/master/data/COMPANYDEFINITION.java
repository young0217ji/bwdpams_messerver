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
public class COMPANYDEFINITION
{
    // Key Info
    private String PLANTID;
    private String COMPANYID;

    // Data Info
    private String COMPANYNAME;
    private String COMPANYTYPE;
    private String ADDRESS;
    private String DEALER;
    private String DEALERMOBILE;
    private String DEALEREMAIL;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public COMPANYDEFINITION() { }

    // Key Methods
    // PLANTID
    public String getKeyPLANTID()
    {
        return PLANTID;
    }
    public void setKeyPLANTID(String PLANTID)
    {
        this.PLANTID = PLANTID;
        this.KEYMAP.put("PLANTID", PLANTID);
    }

    // COMPANYID
    public String getKeyCOMPANYID()
    {
        return COMPANYID;
    }
    public void setKeyCOMPANYID(String COMPANYID)
    {
        this.COMPANYID = COMPANYID;
        this.KEYMAP.put("COMPANYID", COMPANYID);
    }


    // Data Methods
    // COMPANYNAME
    public String getCOMPANYNAME()
    {
        return COMPANYNAME;
    }
    public void setCOMPANYNAME(String COMPANYNAME)
    {
        this.COMPANYNAME = COMPANYNAME;
        this.DATAMAP.put("COMPANYNAME", COMPANYNAME);
    }

    // COMPANYTYPE
    public String getCOMPANYTYPE()
    {
        return COMPANYTYPE;
    }
    public void setCOMPANYTYPE(String COMPANYTYPE)
    {
        this.COMPANYTYPE = COMPANYTYPE;
        this.DATAMAP.put("COMPANYTYPE", COMPANYTYPE);
    }

    // ADDRESS
    public String getADDRESS()
    {
        return ADDRESS;
    }
    public void setADDRESS(String ADDRESS)
    {
        this.ADDRESS = ADDRESS;
        this.DATAMAP.put("ADDRESS", ADDRESS);
    }

    // DEALER
    public String getDEALER()
    {
        return DEALER;
    }
    public void setDEALER(String DEALER)
    {
        this.DEALER = DEALER;
        this.DATAMAP.put("DEALER", DEALER);
    }

    // DEALERMOBILE
    public String getDEALERMOBILE()
    {
        return DEALERMOBILE;
    }
    public void setDEALERMOBILE(String DEALERMOBILE)
    {
        this.DEALERMOBILE = DEALERMOBILE;
        this.DATAMAP.put("DEALERMOBILE", DEALERMOBILE);
    }

    // DEALEREMAIL
    public String getDEALEREMAIL()
    {
        return DEALEREMAIL;
    }
    public void setDEALEREMAIL(String DEALEREMAIL)
    {
        this.DEALEREMAIL = DEALEREMAIL;
        this.DATAMAP.put("DEALEREMAIL", DEALEREMAIL);
    }

    // DESCRIPTION
    public String getDESCRIPTION()
    {
        return DESCRIPTION;
    }
    public void setDESCRIPTION(String DESCRIPTION)
    {
        this.DESCRIPTION = DESCRIPTION;
        this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.COMPANYID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, COMPANYID"});
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
