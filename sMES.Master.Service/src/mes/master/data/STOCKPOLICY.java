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
public class STOCKPOLICY 
{
	 	// Key Info
		private String PLANTID;
	    private String YYYYMM;
	    private String POLICYID;

	    // Data Info
	    private String POLICYVALUE;
	    private String ACTIVESTATE;
	    private String DESCRIPTION;

	    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
	    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

	    public STOCKPOLICY() { }

	    //// Key Methods	    
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
	    
	    // YYYYMM
	    public String getKeyYYYYMM()
	    {
	        return YYYYMM;
	    }
	    public void setKeyYYYYMM(String YYYYMM)
	    {
	        this.YYYYMM = YYYYMM;
	        this.KEYMAP.put("YYYYMM", YYYYMM);
	    }
	    
	    // POLICYID
	    public String getKeyPOLICYID()
	    {
	        return POLICYID;
	    }
	    public void setKeyPOLICYID(String POLICYID)
	    {
	        this.POLICYID = POLICYID;
	        this.KEYMAP.put("POLICYID", POLICYID);
	    }
	    
	    //// Data Methods
	    // POLICYVALUE
	    public String getPOLICYVALUE()
	    {
	        return POLICYVALUE;
	    }
	    public void setPOLICYVALUE(String POLICYVALUE)
	    {
	        this.POLICYVALUE = POLICYVALUE;
	        this.DATAMAP.put("POLICYVALUE", POLICYVALUE);
	    }

	    // ACTIVESTATE
	    public String getACTIVESTATE()
	    {
	        return ACTIVESTATE;
	    }
	    public void setACTIVESTATE(String ACTIVESTATE)
	    {
	        this.ACTIVESTATE = ACTIVESTATE;
	        this.DATAMAP.put("ACTIVESTATE", ACTIVESTATE);
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
	        if (this.KEYMAP.isEmpty() || this.YYYYMM == null || this.POLICYID == null)
	        {
	            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
	            throw new KeyRequiredException(new Object [] {"YYYYMM, POLICYID"});
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
