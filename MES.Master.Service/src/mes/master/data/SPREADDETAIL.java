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
public class SPREADDETAIL
{
    // Key Info
    private String PLANTID;
    private String SPREADID;
    private String SPREADCOLUMNID;

    // Data Info
    private String SPREADCOLUMNNAME;
    private Long SPREADCOLUMNSIZE;
    private String VISIBLEFLAG;
    private String EDITFLAG;
    private String CELLTYPE;
    private String COMBOENUMID;
    private Long POSITION;
    private String ALIGN;
    private String CLASSNAME;
    private String DESCRIPTION;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public SPREADDETAIL() { }

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

    // SPREADID
    public String getKeySPREADID()
    {
        return SPREADID;
    }
    public void setKeySPREADID(String SPREADID)
    {
        this.SPREADID = SPREADID;
        this.KEYMAP.put("SPREADID", SPREADID);
    }

    // SPREADCOLUMNID
    public String getKeySPREADCOLUMNID()
    {
        return SPREADCOLUMNID;
    }
    public void setKeySPREADCOLUMNID(String SPREADCOLUMNID)
    {
        this.SPREADCOLUMNID = SPREADCOLUMNID;
        this.KEYMAP.put("SPREADCOLUMNID", SPREADCOLUMNID);
    }


    // Data Methods
    // SPREADCOLUMNNAME
    public String getSPREADCOLUMNNAME()
    {
        return SPREADCOLUMNNAME;
    }
    public void setSPREADCOLUMNNAME(String SPREADCOLUMNNAME)
    {
        this.SPREADCOLUMNNAME = SPREADCOLUMNNAME;
        this.DATAMAP.put("SPREADCOLUMNNAME", SPREADCOLUMNNAME);
    }

    // SPREADCOLUMNSIZE
    public Long getSPREADCOLUMNSIZE()
    {
        return SPREADCOLUMNSIZE;
    }
    public void setSPREADCOLUMNSIZE(Long SPREADCOLUMNSIZE)
    {
        this.SPREADCOLUMNSIZE = SPREADCOLUMNSIZE;
        this.DATAMAP.put("SPREADCOLUMNSIZE", SPREADCOLUMNSIZE);
    }

    // VISIBLEFLAG
    public String getVISIBLEFLAG()
    {
        return VISIBLEFLAG;
    }
    public void setVISIBLEFLAG(String VISIBLEFLAG)
    {
        this.VISIBLEFLAG = VISIBLEFLAG;
        this.DATAMAP.put("VISIBLEFLAG", VISIBLEFLAG);
    }

    // EDITFLAG
    public String getEDITFLAG()
    {
        return EDITFLAG;
    }
    public void setEDITFLAG(String EDITFLAG)
    {
        this.EDITFLAG = EDITFLAG;
        this.DATAMAP.put("EDITFLAG", EDITFLAG);
    }

    // CELLTYPE
    public String getCELLTYPE()
    {
        return CELLTYPE;
    }
    public void setCELLTYPE(String CELLTYPE)
    {
        this.CELLTYPE = CELLTYPE;
        this.DATAMAP.put("CELLTYPE", CELLTYPE);
    }

    // COMBOENUMID
    public String getCOMBOENUMID()
    {
        return COMBOENUMID;
    }
    public void setCOMBOENUMID(String COMBOENUMID)
    {
        this.COMBOENUMID = COMBOENUMID;
        this.DATAMAP.put("COMBOENUMID", COMBOENUMID);
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

    // ALIGN
    public String getALIGN()
    {
        return ALIGN;
    }
    public void setALIGN(String ALIGN)
    {
        this.ALIGN = ALIGN;
        this.DATAMAP.put("ALIGN", ALIGN);
    }

    // CLASSNAME
    public String getCLASSNAME()
    {
        return CLASSNAME;
    }
    public void setCLASSNAME(String CLASSNAME)
    {
        this.CLASSNAME = CLASSNAME;
        this.DATAMAP.put("CLASSNAME", CLASSNAME);
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
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.SPREADID == null || this.SPREADCOLUMNID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, SPREADID, SPREADCOLUMNID"});
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
