package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

public class EQUIPMENTIMAGE
{
    // Key Info
    private String PLANTID;
    private String EQUIPMENTIMAGEID;

    // Data Info
    private String PMSCHEDULEID;
    private String EQUIPMENTID;
    private String EQUIPMENTIMAGETYPE;
    private Timestamp IMAGECREATETIME;
    private String EQUIPMENTIMAGE;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public EQUIPMENTIMAGE() { }

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

    public String getKeyEQUIPMENTIMAGEID()
    {
        return EQUIPMENTIMAGEID;
    }
    public void setKeyEQUIPMENTIMAGEID(String EQUIPMENTIMAGEID)
    {
        this.EQUIPMENTIMAGEID = EQUIPMENTIMAGEID;
        this.KEYMAP.put("EQUIPMENTIMAGEID", EQUIPMENTIMAGEID);
    }

    // PMSCHEDULEID
    public String getPMSCHEDULEID()
    {
        return PMSCHEDULEID;
    }
    public void setPMSCHEDULEID(String PMSCHEDULEID)
    {
        this.PMSCHEDULEID = PMSCHEDULEID;
        this.DATAMAP.put("PMSCHEDULEID", PMSCHEDULEID);
    }
    
    public String getEQUIPMENTID()
    {
        return EQUIPMENTID;
    }
    public void setEQUIPMENTID(String EQUIPMENTID)
    {
        this.EQUIPMENTID = EQUIPMENTID;
        this.DATAMAP.put("EQUIPMENTID", EQUIPMENTID);
    }
    
    // EQUIPMENTIMAGETYPE
    public String getEQUIPMENTIMAGETYPE()
    {
        return EQUIPMENTIMAGETYPE;
    }
    public void setEQUIPMENTIMAGETYPE(String EQUIPMENTIMAGETYPE)
    {
        this.EQUIPMENTIMAGETYPE = EQUIPMENTIMAGETYPE;
        this.DATAMAP.put("EQUIPMENTIMAGETYPE", EQUIPMENTIMAGETYPE);
    }
    
    // IMAGECREATETIME
    public Timestamp getIMAGECREATETIME()
    {
    	return this.IMAGECREATETIME;
    }
    public void setIMAGECREATETIME(Timestamp IMAGECREATETIME)
    {
    	this.IMAGECREATETIME = IMAGECREATETIME;
    	this.DATAMAP.put("IMAGECREATETIME", IMAGECREATETIME);
    }
    
    public String getEQUIPMENTIMAGE()
    {
    	return this.EQUIPMENTIMAGE;
    }
    public void setEQUIPMENTIMAGE(String EQUIPMENTIMAGE)
    {
    	this.EQUIPMENTIMAGE = EQUIPMENTIMAGE;
    	this.DATAMAP.put("EQUIPMENTIMAGE", EQUIPMENTIMAGE);
    }

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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.EQUIPMENTIMAGEID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, EQUIPMENTIMAGEID"});
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
