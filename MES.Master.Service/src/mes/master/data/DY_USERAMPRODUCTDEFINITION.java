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
public class DY_USERAMPRODUCTDEFINITION extends PRODUCTDEFINITION_UDF
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;
    private String CUSTOMER;
    private String USERPRODUCTID;
    private String USAGE;
    private String DISTRIBUTIONCHANNEL;

    // Data Info
    private String SELECTITEM;
    private String PRODUCTNAME;
    private String CUSTOMERNAME;
    private String USAGENAME;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_USERAMPRODUCTDEFINITION() { }

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

    // CUSTOMER
    public String getKeyCUSTOMER()
    {
        return CUSTOMER;
    }
    public void setKeyCUSTOMER(String CUSTOMER)
    {
        this.CUSTOMER = CUSTOMER;
        this.KEYMAP.put("CUSTOMER", CUSTOMER);
    }
    
    // USERPRODUCTID
    public String getKeyUSERPRODUCTID()
    {
        return USERPRODUCTID;
    }
    public void setKeyUSERPRODUCTID(String USERPRODUCTID)
    {
        this.USERPRODUCTID = USERPRODUCTID;
        this.KEYMAP.put("USERPRODUCTID", USERPRODUCTID);
    }
    
    // USAGE
    public String getKeyUSAGE()
    {
        return USAGE;
    }
    public void setKeyUSAGE(String USAGE)
    {
        this.USAGE = USAGE;
        this.KEYMAP.put("USAGE", USAGE);
    }
    
    // DISTRIBUTIONCHANNEL
    public String getKeyDISTRIBUTIONCHANNEL()
    {
        return DISTRIBUTIONCHANNEL;
    }
    public void setKeyDISTRIBUTIONCHANNEL(String DISTRIBUTIONCHANNEL)
    {
        this.DISTRIBUTIONCHANNEL = DISTRIBUTIONCHANNEL;
        this.KEYMAP.put("DISTRIBUTIONCHANNEL", DISTRIBUTIONCHANNEL);
    }
    
    // Data Methods
    // SELECTITEM
    public String getSELECTITEM()
    {
        return SELECTITEM;
    }
    public void setSELECTITEM(String SELECTITEM)
    {
        this.SELECTITEM = SELECTITEM;
        this.DATAMAP.put("SELECTITEM", SELECTITEM);
    }

    // PRODUCTNAME
    public String getPRODUCTNAME()
    {
        return PRODUCTNAME;
    }
    public void setPRODUCTNAME(String PRODUCTNAME)
    {
        this.PRODUCTNAME = PRODUCTNAME;
        this.DATAMAP.put("PRODUCTNAME", PRODUCTNAME);
    }


    // CUSTOMERNAME
    public String getCUSTOMERNAME()
    {
        return CUSTOMERNAME;
    }
    public void setCUSTOMERNAME(String CUSTOMERNAME)
    {
        this.CUSTOMERNAME = CUSTOMERNAME;
        this.DATAMAP.put("CUSTOMERNAME", CUSTOMERNAME);
    }
    
    // USAGENAME
    public String getUSAGENAME()
    {
    	return USAGENAME;
    }
    public void setUSAGENAME(String USAGENAME)
    {
    	this.USAGENAME = USAGENAME;
    	this.DATAMAP.put("USAGENAME", USAGENAME);
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
            throw new KeyRequiredException(new Object [] {"PLANTID, PRODUCTID", "CUSTOMER", "USERPRODUCTID", "USAGE", "DISTRIBUTIONCHANNEL"});
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
