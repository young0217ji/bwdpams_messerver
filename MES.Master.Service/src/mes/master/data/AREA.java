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
public class AREA implements Cloneable
{
	@Override
	public AREA clone() throws CloneNotSupportedException {
		return this.clone();
	}
	
    // Key Info
    private String PLANTID;
    private String AREAID;

    // Data Info
    private String AREANAME;
    private String AREATYPE;
    private String SUPERAREAID;
    private Long POSITION;
    private String GOINORDERREQUIRED;
    private String KOREAN;
    private String ENGLISH;
    private String NATIVE1;
    private String NATIVE2;
    private String DESCRIPTION;
    private String REPRESENTATIVECHAR;
    private String WORKCENTERTYPE;
    private String WORKCENTERGROUPID;
    private Long UNITCOUNT;
    private String STANDARDVALUEKEY;
    private Long STANDARDLABOR;
    private String REPRESENTATIVEPROCESSID;
    private String USEFLAG;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public AREA() { }

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

    // AREAID
    public String getKeyAREAID()
    {
        return AREAID;
    }
    public void setKeyAREAID(String AREAID)
    {
        this.AREAID = AREAID;
        this.KEYMAP.put("AREAID", AREAID);
    }


    // Data Methods
    // AREANAME
    public String getAREANAME()
    {
        return AREANAME;
    }
    public void setAREANAME(String AREANAME)
    {
        this.AREANAME = AREANAME;
        this.DATAMAP.put("AREANAME", AREANAME);
    }

    // AREATYPE
    public String getAREATYPE()
    {
        return AREATYPE;
    }
    public void setAREATYPE(String AREATYPE)
    {
        this.AREATYPE = AREATYPE;
        this.DATAMAP.put("AREATYPE", AREATYPE);
    }

    // SUPERAREAID
    public String getSUPERAREAID()
    {
        return SUPERAREAID;
    }
    public void setSUPERAREAID(String SUPERAREAID)
    {
        this.SUPERAREAID = SUPERAREAID;
        this.DATAMAP.put("SUPERAREAID", SUPERAREAID);
    }

    // POSITION
    public Long getPOSITION()
    {
        return POSITION;
    }
    public void setPOSITION(Long POSITION)
    {
        this.POSITION = POSITION;
        this.DATAMAP.put("POSITION", POSITION);
    }

    // GOINORDERREQUIRED
    public String getGOINORDERREQUIRED()
    {
        return GOINORDERREQUIRED;
    }
    public void setGOINORDERREQUIRED(String GOINORDERREQUIRED)
    {
        this.GOINORDERREQUIRED = GOINORDERREQUIRED;
        this.DATAMAP.put("GOINORDERREQUIRED", GOINORDERREQUIRED);
    }

    // KOREAN
    public String getKOREAN()
    {
        return KOREAN;
    }
    public void setKOREAN(String KOREAN)
    {
        this.KOREAN = KOREAN;
        this.DATAMAP.put("KOREAN", KOREAN);
    }

    // ENGLISH
    public String getENGLISH()
    {
        return ENGLISH;
    }
    public void setENGLISH(String ENGLISH)
    {
        this.ENGLISH = ENGLISH;
        this.DATAMAP.put("ENGLISH", ENGLISH);
    }

    // NATIVE1
    public String getNATIVE1()
    {
        return NATIVE1;
    }
    public void setNATIVE1(String NATIVE1)
    {
        this.NATIVE1 = NATIVE1;
        this.DATAMAP.put("NATIVE1", NATIVE1);
    }

    // NATIVE2
    public String getNATIVE2()
    {
        return NATIVE2;
    }
    public void setNATIVE2(String NATIVE2)
    {
        this.NATIVE2 = NATIVE2;
        this.DATAMAP.put("NATIVE2", NATIVE2);
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
    
    // REPRESENTATIVECHAR
    public String getREPRESENTATIVECHAR()
    {
        return REPRESENTATIVECHAR;
    }
    public void setREPRESENTATIVECHAR(String REPRESENTATIVECHAR)
    {
        this.REPRESENTATIVECHAR = REPRESENTATIVECHAR;
        this.DATAMAP.put("REPRESENTATIVECHAR", REPRESENTATIVECHAR);
    }
    
    // WORKCENTERTYPE
    public String getWORKCENTERTYPE()
    {
        return WORKCENTERTYPE;
    }
    public void setWORKCENTERTYPE(String WORKCENTERTYPE)
    {
        this.WORKCENTERTYPE = WORKCENTERTYPE;
        this.DATAMAP.put("WORKCENTERTYPE", WORKCENTERTYPE);
    }
    
    // WORKCENTERGROUPID
    public String getWORKCENTERGROUPID()
    {
        return WORKCENTERGROUPID;
    }
    public void setWORKCENTERGROUPID(String WORKCENTERGROUPID)
    {
        this.WORKCENTERGROUPID = WORKCENTERGROUPID;
        this.DATAMAP.put("WORKCENTERGROUPID", WORKCENTERGROUPID);
    }
    
    // UNITCOUNT
    public Long getUNITCOUNT()
    {
        return UNITCOUNT;
    }
    public void setUNITCOUNT(Long UNITCOUNT)
    {
        this.UNITCOUNT = UNITCOUNT;
        this.DATAMAP.put("UNITCOUNT", UNITCOUNT);
    }
    
    // STANDARDVALUEKEY
    public String getSTANDARDVALUEKEY()
    {
        return STANDARDVALUEKEY;
    }
    public void setSTANDARDVALUEKEY(String STANDARDVALUEKEY)
    {
        this.STANDARDVALUEKEY = STANDARDVALUEKEY;
        this.DATAMAP.put("STANDARDVALUEKEY", STANDARDVALUEKEY);
    }
    
    // STANDARDLABOR
    public Long getSTANDARDLABOR()
    {
        return STANDARDLABOR;
    }
    public void setSTANDARDLABOR(Long STANDARDLABOR)
    {
        this.STANDARDLABOR = STANDARDLABOR;
        this.DATAMAP.put("STANDARDLABOR", STANDARDLABOR);
    }
    
    // REPRESENTATIVEPROCESSID
    public String getREPRESENTATIVEPROCESSID()
    {
        return REPRESENTATIVEPROCESSID;
    }
    public void setREPRESENTATIVEPROCESSID(String REPRESENTATIVEPROCESSID)
    {
        this.REPRESENTATIVEPROCESSID = REPRESENTATIVEPROCESSID;
        this.DATAMAP.put("REPRESENTATIVEPROCESSID", REPRESENTATIVEPROCESSID);
    }
    
    // USEFLAG
    public String getUSEFLAG()
    {
    	return USEFLAG;
    }
    public void setUSEFLAG(String USEFLAG)
    {
    	this.USEFLAG = USEFLAG;
    	this.DATAMAP.put("USEFLAG", USEFLAG);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.AREAID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, AREAID"});
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
