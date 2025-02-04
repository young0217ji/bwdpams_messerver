package mes.master.data;

import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TABLELIST
{
    // Key Info
    private String TABLEID;
    private String TABLENAME;

    // Data Info
    private String TABLETYPE;
    private String COMMENTS;
    private String MADEVERSION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public TABLELIST() { }

    // Key Methods
    // TABLEID
    public String getKeyTABLEID()
    {
        return TABLEID;
    }
    public void setKeyTABLEID(String TABLEID)
    {
        this.TABLEID = TABLEID;
        this.KEYMAP.put("TABLEID", TABLEID);
    }

    // TABLENAME
    public String getKeyTABLENAME()
    {
        return TABLENAME;
    }
    public void setKeyTABLENAME(String TABLENAME)
    {
        this.TABLENAME = TABLENAME;
        this.KEYMAP.put("TABLENAME", TABLENAME);
    }


    // Data Methods
    // TABLETYPE
    public String getTABLETYPE()
    {
        return TABLETYPE;
    }
    public void setTABLETYPE(String TABLETYPE)
    {
        this.TABLETYPE = TABLETYPE;
        this.DATAMAP.put("TABLETYPE", TABLETYPE);
    }

    // COMMENTS
    public String getCOMMENTS()
    {
        return COMMENTS;
    }
    public void setCOMMENTS(String COMMENTS)
    {
        this.COMMENTS = COMMENTS;
        this.DATAMAP.put("COMMENTS", COMMENTS);
    }

    // MADEVERSION
    public String getMADEVERSION()
    {
        return MADEVERSION;
    }
    public void setMADEVERSION(String MADEVERSION)
    {
        this.MADEVERSION = MADEVERSION;
        this.DATAMAP.put("MADEVERSION", MADEVERSION);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.TABLEID == null || this.TABLENAME == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"TABLEID, TABLENAME"});
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
    
	public Object excuteTRUNCATE()
	{
		String sSql = " TRUNCATE TABLE " + TABLEID;
		int iReturn = SqlMesTemplate.update( sSql );

		return iReturn;
	}
	
	public Object excuteBACKUP()
	{
		String sSql = " CREATE TABLE " + TABLEID + "_BACKUP AS\n"
					+ " SELECT * FROM " + TABLEID;
		int iReturn = SqlMesTemplate.update( sSql );
		
		return iReturn;
	}
	
	public Object excuteDROP()
	{
		String sSql = " DROP TABLE " + TABLEID;
		int iReturn = SqlMesTemplate.update( sSql );

		return iReturn;
	}
	
	public Object excuteSetComments()
	{
		String sSql = " COMMENT ON TABLE " + TABLEID + " IS '" + COMMENTS + "' ";
		int iReturn = SqlMesTemplate.update( sSql );
		
		return iReturn;
	}
}
