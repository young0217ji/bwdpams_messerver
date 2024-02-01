package mes.lot.data;

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
public class REWORKINFORMATION
{
    // Key Info
    private String LOTID;
    private String PROCESSROUTEID;
    private Long RELATIONSEQUENCE;

    // Data Info
    private Timestamp STARTTIME;
    private Timestamp ENDTIME;
    private String REWORKSTATE;
    private String REWORKEQUIPMENTID;
    private String REASONCODETYPE;
    private String REASONCODE;
    private String REASONPROCESSID;
    private Long REASONPROCESSSEQUENCE;
    private Long DEPTHLEVEL;
    private Long FROMID;
    private Long TOID;
    private String EVENTUSERID;
    private String REWORKPROCESSID;
    private Long REWORKPROCESSSEQUENCE;
    private String RELATIONTIMEKEY;
    private String STARTPROCESSID;
    private Long STARTPROCESSSEQUENCE;
    private String ENDPROCESSID;
    private Long ENDPROCESSSEQUENCE;
    private String RETURNPROCESSROUTEID;
    private String RETURNPROCESSID;
    private Long RETURNPROCESSSEQUENCE;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public REWORKINFORMATION() { }

    // Key Methods
    // LOTID
    public String getKeyLOTID()
    {
        return LOTID;
    }
    public void setKeyLOTID(String LOTID)
    {
        this.LOTID = LOTID;
        this.KEYMAP.put("LOTID", LOTID);
    }

    // PROCESSROUTEID
    public String getKeyPROCESSROUTEID()
    {
        return PROCESSROUTEID;
    }
    public void setKeyPROCESSROUTEID(String PROCESSROUTEID)
    {
        this.PROCESSROUTEID = PROCESSROUTEID;
        this.KEYMAP.put("PROCESSROUTEID", PROCESSROUTEID);
    }

    // RELATIONSEQUENCE
    public Long getKeyRELATIONSEQUENCE()
    {
        return RELATIONSEQUENCE;
    }
    public void setKeyRELATIONSEQUENCE(Long RELATIONSEQUENCE)
    {
        this.RELATIONSEQUENCE = RELATIONSEQUENCE;
        this.KEYMAP.put("RELATIONSEQUENCE", RELATIONSEQUENCE);
    }


    // Data Methods
    // STARTTIME
    public Timestamp getSTARTTIME()
    {
        return STARTTIME;
    }
    public void setSTARTTIME(Timestamp STARTTIME)
    {
        this.STARTTIME = STARTTIME;
        this.DATAMAP.put("STARTTIME", STARTTIME);
    }

    // ENDTIME
    public Timestamp getENDTIME()
    {
        return ENDTIME;
    }
    public void setENDTIME(Timestamp ENDTIME)
    {
        this.ENDTIME = ENDTIME;
        this.DATAMAP.put("ENDTIME", ENDTIME);
    }

    // REWORKSTATE
    public String getREWORKSTATE()
    {
        return REWORKSTATE;
    }
    public void setREWORKSTATE(String REWORKSTATE)
    {
        this.REWORKSTATE = REWORKSTATE;
        this.DATAMAP.put("REWORKSTATE", REWORKSTATE);
    }

    // REWORKEQUIPMENTID
    public String getREWORKEQUIPMENTID()
    {
        return REWORKEQUIPMENTID;
    }
    public void setREWORKEQUIPMENTID(String REWORKEQUIPMENTID)
    {
        this.REWORKEQUIPMENTID = REWORKEQUIPMENTID;
        this.DATAMAP.put("REWORKEQUIPMENTID", REWORKEQUIPMENTID);
    }

    // REASONCODETYPE
    public String getREASONCODETYPE()
    {
        return REASONCODETYPE;
    }
    public void setREASONCODETYPE(String REASONCODETYPE)
    {
        this.REASONCODETYPE = REASONCODETYPE;
        this.DATAMAP.put("REASONCODETYPE", REASONCODETYPE);
    }

    // REASONCODE
    public String getREASONCODE()
    {
        return REASONCODE;
    }
    public void setREASONCODE(String REASONCODE)
    {
        this.REASONCODE = REASONCODE;
        this.DATAMAP.put("REASONCODE", REASONCODE);
    }
    
    // REASONPROCESSID
    public String getREASONPROCESSID()
    {
        return REASONPROCESSID;
    }
    public void setREASONPROCESSID(String REASONPROCESSID)
    {
        this.REASONPROCESSID = REASONPROCESSID;
        this.DATAMAP.put("REASONPROCESSID", REASONPROCESSID);
    }
    
    // REASONPROCESSSEQUENCE
    public Long getREASONPROCESSSEQUENCE()
    {
        return REASONPROCESSSEQUENCE;
    }
    public void setREASONPROCESSSEQUENCE(Long REASONPROCESSSEQUENCE)
    {
        this.REASONPROCESSSEQUENCE = REASONPROCESSSEQUENCE;
        this.DATAMAP.put("REASONPROCESSSEQUENCE", REASONPROCESSSEQUENCE);
    }
    
    // DEPTHLEVEL
    public Long getDEPTHLEVEL()
    {
        return DEPTHLEVEL;
    }
    public void setDEPTHLEVEL(Long DEPTHLEVEL)
    {
        this.DEPTHLEVEL = DEPTHLEVEL;
        this.DATAMAP.put("DEPTHLEVEL", DEPTHLEVEL);
    }
    
    // FROMID
    public Long getFROMID()
    {
        return FROMID;
    }
    public void setFROMID(Long FROMID)
    {
        this.FROMID = FROMID;
        this.DATAMAP.put("FROMID", FROMID);
    }
    
    // TOID
    public Long getTOID()
    {
        return TOID;
    }
    public void setTOID(Long TOID)
    {
        this.TOID = TOID;
        this.DATAMAP.put("TOID", TOID);
    }

    // EVENTUSERID
    public String getEVENTUSERID()
    {
        return EVENTUSERID;
    }
    public void setEVENTUSERID(String EVENTUSERID)
    {
        this.EVENTUSERID = EVENTUSERID;
        this.DATAMAP.put("EVENTUSERID", EVENTUSERID);
    }

    // REWORKPROCESSID
    public String getREWORKPROCESSID()
    {
        return REWORKPROCESSID;
    }
    public void setREWORKPROCESSID(String REWORKPROCESSID)
    {
        this.REWORKPROCESSID = REWORKPROCESSID;
        this.DATAMAP.put("REWORKPROCESSID", REWORKPROCESSID);
    }

    // REWORKPROCESSSEQUENCE
    public Long getREWORKPROCESSSEQUENCE()
    {
        return REWORKPROCESSSEQUENCE;
    }
    public void setREWORKPROCESSSEQUENCE(Long REWORKPROCESSSEQUENCE)
    {
        this.REWORKPROCESSSEQUENCE = REWORKPROCESSSEQUENCE;
        this.DATAMAP.put("REWORKPROCESSSEQUENCE", REWORKPROCESSSEQUENCE);
    }

    // RELATIONTIMEKEY
    public String getRELATIONTIMEKEY()
    {
        return RELATIONTIMEKEY;
    }
    public void setRELATIONTIMEKEY(String RELATIONTIMEKEY)
    {
        this.RELATIONTIMEKEY = RELATIONTIMEKEY;
        this.DATAMAP.put("RELATIONTIMEKEY", RELATIONTIMEKEY);
    }

    // STARTPROCESSID
    public String getSTARTPROCESSID()
    {
        return STARTPROCESSID;
    }
    public void setSTARTPROCESSID(String STARTPROCESSID)
    {
        this.STARTPROCESSID = STARTPROCESSID;
        this.DATAMAP.put("STARTPROCESSID", STARTPROCESSID);
    }

    // STARTPROCESSSEQUENCE
    public Long getSTARTPROCESSSEQUENCE()
    {
        return STARTPROCESSSEQUENCE;
    }
    public void setSTARTPROCESSSEQUENCE(Long STARTPROCESSSEQUENCE)
    {
        this.STARTPROCESSSEQUENCE = STARTPROCESSSEQUENCE;
        this.DATAMAP.put("STARTPROCESSSEQUENCE", STARTPROCESSSEQUENCE);
    }

    // ENDPROCESSID
    public String getENDPROCESSID()
    {
        return ENDPROCESSID;
    }
    public void setENDPROCESSID(String ENDPROCESSID)
    {
        this.ENDPROCESSID = ENDPROCESSID;
        this.DATAMAP.put("ENDPROCESSID", ENDPROCESSID);
    }

    // ENDPROCESSSEQUENCE
    public Long getENDPROCESSSEQUENCE()
    {
        return ENDPROCESSSEQUENCE;
    }
    public void setENDPROCESSSEQUENCE(Long ENDPROCESSSEQUENCE)
    {
        this.ENDPROCESSSEQUENCE = ENDPROCESSSEQUENCE;
        this.DATAMAP.put("ENDPROCESSSEQUENCE", ENDPROCESSSEQUENCE);
    }

    // RETURNPROCESSROUTEID
    public String getRETURNPROCESSROUTEID()
    {
        return RETURNPROCESSROUTEID;
    }
    public void setRETURNPROCESSROUTEID(String RETURNPROCESSROUTEID)
    {
        this.RETURNPROCESSROUTEID = RETURNPROCESSROUTEID;
        this.DATAMAP.put("RETURNPROCESSROUTEID", RETURNPROCESSROUTEID);
    }

    // RETURNPROCESSID
    public String getRETURNPROCESSID()
    {
        return RETURNPROCESSID;
    }
    public void setRETURNPROCESSID(String RETURNPROCESSID)
    {
        this.RETURNPROCESSID = RETURNPROCESSID;
        this.DATAMAP.put("RETURNPROCESSID", RETURNPROCESSID);
    }

    // RETURNPROCESSSEQUENCE
    public Long getRETURNPROCESSSEQUENCE()
    {
        return RETURNPROCESSSEQUENCE;
    }
    public void setRETURNPROCESSSEQUENCE(Long RETURNPROCESSSEQUENCE)
    {
        this.RETURNPROCESSSEQUENCE = RETURNPROCESSSEQUENCE;
        this.DATAMAP.put("RETURNPROCESSSEQUENCE", RETURNPROCESSSEQUENCE);
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
        if (this.KEYMAP.isEmpty() || this.LOTID == null || this.PROCESSROUTEID == null || this.RELATIONSEQUENCE == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"LOTID, PROCESSROUTEID, REWORKCOUNT"});
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
