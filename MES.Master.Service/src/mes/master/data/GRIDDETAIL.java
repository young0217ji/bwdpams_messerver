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
public class GRIDDETAIL
{
    // Key Info
    private String PLANTID;
    private String GRIDID;
    private String GRIDCOLUMNID;

    // Data Info
    private String GRIDCOLUMNNAME;
    private String PRIMARYKEYFLAG;
    private Long GRIDCOLUMNSIZE;
    private String VISIBLEFLAG;
    private String EDITFLAG;
    private String CELLTYPE;
    private String DEFAULTVALUE;
    private String COMBOENUMID;
    private String COMBOLIST;
    private Long POSITION;
    private String ALIGN;
    private String CHANGECELLTYPE;
    private String CLASSNAME;
    private String DESCRIPTION;
    private String CHILDGRIDID;
    private String HEADERMERGENAME;
    private String HEADERLOCKFLAG;
    private String PARENTCOLUMNID;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public GRIDDETAIL() { }

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

    // GRIDID
    public String getKeyGRIDID()
    {
        return GRIDID;
    }
    public void setKeyGRIDID(String GRIDID)
    {
        this.GRIDID = GRIDID;
        this.KEYMAP.put("GRIDID", GRIDID);
    }

    // GRIDCOLUMNID
    public String getKeyGRIDCOLUMNID()
    {
        return GRIDCOLUMNID;
    }
    public void setKeyGRIDCOLUMNID(String GRIDCOLUMNID)
    {
        this.GRIDCOLUMNID = GRIDCOLUMNID;
        this.KEYMAP.put("GRIDCOLUMNID", GRIDCOLUMNID);
    }


    // Data Methods
    // GRIDCOLUMNNAME
    public String getGRIDCOLUMNNAME()
    {
        return GRIDCOLUMNNAME;
    }
    public void setGRIDCOLUMNNAME(String GRIDCOLUMNNAME)
    {
        this.GRIDCOLUMNNAME = GRIDCOLUMNNAME;
        this.DATAMAP.put("GRIDCOLUMNNAME", GRIDCOLUMNNAME);
    }

    // PRIMARYKEYFLAG
    public String getPRIMARYKEYFLAG()
    {
        return PRIMARYKEYFLAG;
    }
    public void setPRIMARYKEYFLAG(String PRIMARYKEYFLAG)
    {
        this.PRIMARYKEYFLAG = PRIMARYKEYFLAG;
        this.DATAMAP.put("PRIMARYKEYFLAG", PRIMARYKEYFLAG);
    }

    // GRIDCOLUMNSIZE
    public Long getGRIDCOLUMNSIZE()
    {
        return GRIDCOLUMNSIZE;
    }
    public void setGRIDCOLUMNSIZE(Long GRIDCOLUMNSIZE)
    {
        this.GRIDCOLUMNSIZE = GRIDCOLUMNSIZE;
        this.DATAMAP.put("GRIDCOLUMNSIZE", GRIDCOLUMNSIZE);
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

    // DEFAULTVALUE
    public String getDEFAULTVALUE()
    {
        return DEFAULTVALUE;
    }
    public void setDEFAULTVALUE(String DEFAULTVALUE)
    {
        this.DEFAULTVALUE = DEFAULTVALUE;
        this.DATAMAP.put("DEFAULTVALUE", DEFAULTVALUE);
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

    // COMBOLIST
    public String getCOMBOLIST()
    {
        return COMBOLIST;
    }
    public void setCOMBOLIST(String COMBOLIST)
    {
        this.COMBOLIST = COMBOLIST;
        this.DATAMAP.put("COMBOLIST", COMBOLIST);
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
    
    // CHANGECELLTYPE
    public String getCHANGECELLTYPE()
    {
        return CHANGECELLTYPE;
    }
    public void setCHANGECELLTYPE(String CHANGECELLTYPE)
    {
        this.CHANGECELLTYPE = CHANGECELLTYPE;
        this.DATAMAP.put("CHANGECELLTYPE", CHANGECELLTYPE);
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
    
    // CHILDGRIDID
    public String getCHILDGRIDID()
    {
        return CHILDGRIDID;
    }
    public void setCHILDGRIDID(String CHILDGRIDID)
    {
        this.CHILDGRIDID = CHILDGRIDID;
        this.DATAMAP.put("CHILDGRIDID", CHILDGRIDID);
    }
    
    // HEADERMERGENAME
    public String getHEADERMERGENAME()
    {
        return HEADERMERGENAME;
    }
    public void setHEADERMERGENAME(String HEADERMERGENAME)
    {
        this.HEADERMERGENAME = HEADERMERGENAME;
        this.DATAMAP.put("HEADERMERGENAME", HEADERMERGENAME);
    }
    
    // HEADERLOCKFLAG
    public String getHEADERLOCKFLAG()
    {
        return HEADERLOCKFLAG;
    }
    public void setHEADERLOCKFLAG(String HEADERLOCKFLAG)
    {
        this.HEADERLOCKFLAG = HEADERLOCKFLAG;
        this.DATAMAP.put("HEADERLOCKFLAG", HEADERLOCKFLAG);
    }
    
    // PARENTCOLUMNID
    public String getPARENTCOLUMNID()
    {
        return PARENTCOLUMNID;
    }
    public void setPARENTCOLUMNID(String PARENTCOLUMNID)
    {
        this.PARENTCOLUMNID = PARENTCOLUMNID;
        this.DATAMAP.put("PARENTCOLUMNID", PARENTCOLUMNID);
    }


    // Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.GRIDID == null || this.GRIDCOLUMNID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, GRIDID, GRIDCOLUMNID"});
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
