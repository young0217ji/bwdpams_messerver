package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import mes.userdefine.data.PRODUCTDEFINITION_UDF;
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
public class DY_USERPRODUCTDEFINITION extends PRODUCTDEFINITION_UDF
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;

    // Data Info
    private String ENDPRODUCTID;
    private String ENDPRODUCTNAME;
    private String USERPRODUCTID;
    private String LABELTYPE;
    private String CUSTOMERNAME;
    private String DESCRIPTION;
    private String DOOSANCLASS;
    private String CUSTOMERNAME2;
    private String USERPRODUCTID2;
    private String CUSTOMERNAME3;
    private String USERPRODUCTID3;
    private String CUSTOMERNAME4;
    private String USERPRODUCTID4;
    private String LOCALGROUP;
    private String HYUNDAICODE;
    private String HYUNDAIMODEL;
    private String USEFLAG;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_USERPRODUCTDEFINITION() { }

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

    // PRODUCTID
    public String getKeyPRODUCTID()
    {
        return PRODUCTID;
    }
    public void setKeyPRODUCTID(String PRODUCTID)
    {
        this.PRODUCTID = PRODUCTID;
        this.KEYMAP.put("PRODUCTID", PRODUCTID);
    }


    // Data Methods
    // ENDPRODUCTID
    public String getENDPRODUCTID()
    {
        return ENDPRODUCTID;
    }
    public void setENDPRODUCTID(String ENDPRODUCTID)
    {
        this.ENDPRODUCTID = ENDPRODUCTID;
        this.DATAMAP.put("ENDPRODUCTID", ENDPRODUCTID);
    }

    // ENDPRODUCTNAME
    public String getENDPRODUCTNAME()
    {
        return ENDPRODUCTNAME;
    }
    public void setENDPRODUCTNAME(String ENDPRODUCTNAME)
    {
        this.ENDPRODUCTNAME = ENDPRODUCTNAME;
        this.DATAMAP.put("ENDPRODUCTNAME", ENDPRODUCTNAME);
    }

    // USERPRODUCTID
    public String getUSERPRODUCTID()
    {
        return USERPRODUCTID;
    }
    public void setUSERPRODUCTID(String USERPRODUCTID)
    {
        this.USERPRODUCTID = USERPRODUCTID;
        this.DATAMAP.put("USERPRODUCTID", USERPRODUCTID);
    }

    // LABELTYPE
    public String getLABELTYPE()
    {
        return LABELTYPE;
    }
    public void setLABELTYPE(String LABELTYPE)
    {
        this.LABELTYPE = LABELTYPE;
        this.DATAMAP.put("LABELTYPE", LABELTYPE);
    }

    // CUSTOMER
    public String getCUSTOMERNAME()
    {
        return CUSTOMERNAME;
    }
    public void setCUSTOMERNAME(String CUSTOMERNAME)
    {
        this.CUSTOMERNAME = CUSTOMERNAME;
        this.DATAMAP.put("CUSTOMERNAME", CUSTOMERNAME);
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
    
    // DOOSANCLASS
    public String getDOOSANCLASS() 
    {
		return DOOSANCLASS;
	}

	public void setDOOSANCLASS(String DOOSANCLASS) {
        this.DOOSANCLASS = DOOSANCLASS;
        this.DATAMAP.put("DOOSANCLASS", DOOSANCLASS);
	}
	
	// CUSTOMER2
	public String getCUSTOMERNAME2() {
			return CUSTOMERNAME2;
	}

	public void setCUSTOMERNAME2(String CUSTOMERNAME2) {
	    this.CUSTOMERNAME2 = CUSTOMERNAME2;
	    this.DATAMAP.put("CUSTOMERNAME2", CUSTOMERNAME2);
	}
	
    // USERPRODUCTID2
    public String getUSERPRODUCTID2() 
    {
    	return USERPRODUCTID2;
    }
    
    public void setUSERPRODUCTID2(String USERPRODUCTID2) {
    	this.USERPRODUCTID2 = USERPRODUCTID2;
    	this.DATAMAP.put("USERPRODUCTID2", USERPRODUCTID2);
    }
    
    // CUSTOMER3
    public String getCUSTOMERNAME3() 
    {
    	return CUSTOMERNAME3;
    }
    
    public void setCUSTOMERNAME3(String CUSTOMERNAME3) {
    	this.CUSTOMERNAME3 = CUSTOMERNAME3;
    	this.DATAMAP.put("CUSTOMERNAME3", CUSTOMERNAME3);
    }
    
    // USERPRODUCTID3
    public String getUSERPRODUCTID3() 
    {
    	return USERPRODUCTID3;
    }
    
    public void setUSERPRODUCTID3(String USERPRODUCTID3) {
    	this.USERPRODUCTID3 = USERPRODUCTID3;
    	this.DATAMAP.put("USERPRODUCTID3", USERPRODUCTID3);
    }
    
    // CUSTOMER4
    public String getCUSTOMERNAME4() 
    {
    	return CUSTOMERNAME4;
    }
    
    public void setCUSTOMERNAME4(String CUSTOMERNAME4) {
    	this.CUSTOMERNAME4 = CUSTOMERNAME4;
    	this.DATAMAP.put("CUSTOMERNAME4", CUSTOMERNAME4);
    }
    
    // USERPRODUCTID4
    public String getUSERPRODUCTID4() 
    {
    	return USERPRODUCTID4;
    }
    
    public void setUSERPRODUCTID4(String USERPRODUCTID4) {
    	this.USERPRODUCTID4 = USERPRODUCTID4;
    	this.DATAMAP.put("USERPRODUCTID4", USERPRODUCTID4);
    }

    // LOCALGROUP
 	public String getLOCALGROUP() 
 	{
 		return LOCALGROUP;
 	}

 	public void setLOCALGROUP(String LOCALGROUP) {
         this.LOCALGROUP = LOCALGROUP;
         this.DATAMAP.put("LOCALGROUP", LOCALGROUP);
 	}
 	
 	// HYUNDAICODE
 	public String getHYUNDAICODE() 
 	{
 		return HYUNDAICODE;
 	}

 	public void setHYUNDAICODE(String HYUNDAICODE) {
         this.HYUNDAICODE = HYUNDAICODE;
         this.DATAMAP.put("HYUNDAICODE", HYUNDAICODE);
 	}

     // HYUNDAIMODEL
 	public String getHYUNDAIMODEL() {
 		return HYUNDAIMODEL;
 	}

 	public void setHYUNDAIMODEL(String HYUNDAIMODEL) {
         this.HYUNDAIMODEL = HYUNDAIMODEL;
         this.DATAMAP.put("HYUNDAIMODEL", HYUNDAIMODEL);
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

    // CREATEUSERID
    public String  getCREATEUSERID()
    {
        return CREATEUSERID;
    }
    public void setCREATEUSERID(String  CREATEUSERID)
    {
        this.CREATEUSERID = CREATEUSERID;
        this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
    }

    // CREATETIME
    public Timestamp getCREATETIME()
    {
        return CREATETIME;
    }
    public void setCREATETIME(Timestamp CREATETIME)
    {
        this.CREATETIME = CREATETIME;
        this.DATAMAP.put("CREATETIME", CREATETIME);
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

	// Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.PLANTID == null || this.PRODUCTID == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTID"});
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
