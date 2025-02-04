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
public class PRODUCTDEFINITION extends PRODUCTDEFINITION_UDF
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;

    // Data Info
    private String PRODUCTNAME;
    private String PRODUCTGROUPID;
    private String ACTIVESTATE;
    private Timestamp CREATETIME;
    private String CREATEUSERID;
    private String PRODUCTIONTYPE;
    private String PRODUCTTYPE;
    private Long PRODUCTQUANTITY;
    private String PRODUCTUNIT;
    private Long ESTIMATEDCYCLETIME;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;
    private String DESCRIPTION;
    private String PROQTYPE;
    private String OLDPRODUCTID;
    private String PRODUCTREV;
    private String ECNNO;
    private String ISNO;
    private String MRPMANAGER;
    private String TOPCOATING;
    private String PRIMERCOATING;
    private String INSPECTIONFLAG;
    private String FIRSTPRODUCT;
    private String FERTH;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public PRODUCTDEFINITION() { }

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

    // PRODUCTGROUPID
    public String getPRODUCTGROUPID()
    {
        return PRODUCTGROUPID;
    }
    public void setPRODUCTGROUPID(String PRODUCTGROUPID)
    {
        this.PRODUCTGROUPID = PRODUCTGROUPID;
        this.DATAMAP.put("PRODUCTGROUPID", PRODUCTGROUPID);
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

    // CREATEUSERID
    public String getCREATEUSERID()
    {
        return CREATEUSERID;
    }
    public void setCREATEUSERID(String CREATEUSERID)
    {
        this.CREATEUSERID = CREATEUSERID;
        this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
    }

    // PRODUCTIONTYPE
    public String getPRODUCTIONTYPE()
    {
        return PRODUCTIONTYPE;
    }
    public void setPRODUCTIONTYPE(String PRODUCTIONTYPE)
    {
        this.PRODUCTIONTYPE = PRODUCTIONTYPE;
        this.DATAMAP.put("PRODUCTIONTYPE", PRODUCTIONTYPE);
    }

    // PRODUCTTYPE
    public String getPRODUCTTYPE()
    {
        return PRODUCTTYPE;
    }
    public void setPRODUCTTYPE(String PRODUCTTYPE)
    {
        this.PRODUCTTYPE = PRODUCTTYPE;
        this.DATAMAP.put("PRODUCTTYPE", PRODUCTTYPE);
    }

    // PRODUCTQUANTITY
    public Long getPRODUCTQUANTITY()
    {
        return PRODUCTQUANTITY;
    }
    public void setPRODUCTQUANTITY(Long PRODUCTQUANTITY)
    {
        this.PRODUCTQUANTITY = PRODUCTQUANTITY;
        this.DATAMAP.put("PRODUCTQUANTITY", PRODUCTQUANTITY);
    }

    // PRODUCTUNIT
    public String getPRODUCTUNIT()
    {
        return PRODUCTUNIT;
    }
    public void setPRODUCTUNIT(String PRODUCTUNIT)
    {
        this.PRODUCTUNIT = PRODUCTUNIT;
        this.DATAMAP.put("PRODUCTUNIT", PRODUCTUNIT);
    }

    // ESTIMATEDCYCLETIME
    public Long getESTIMATEDCYCLETIME()
    {
        return ESTIMATEDCYCLETIME;
    }
    public void setESTIMATEDCYCLETIME(Long ESTIMATEDCYCLETIME)
    {
        this.ESTIMATEDCYCLETIME = ESTIMATEDCYCLETIME;
        this.DATAMAP.put("ESTIMATEDCYCLETIME", ESTIMATEDCYCLETIME);
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
    
    // PROQTYPE
    public String getPROQTYPE() 
    {
		return PROQTYPE;
	}

	public void setPROQTYPE(String PROQTYPE) {
        this.PROQTYPE = PROQTYPE;
        this.DATAMAP.put("PROQTYPE", PROQTYPE);
	}
	
    // OLDPRODUCTID
	public String getOLDPRODUCTID() 
	{
		return OLDPRODUCTID;
	}

	public void setOLDPRODUCTID(String OLDPRODUCTID) {
        this.OLDPRODUCTID = OLDPRODUCTID;
        this.DATAMAP.put("OLDPRODUCTID", OLDPRODUCTID);
	}

    // PRODUCTREV
	public String getPRODUCTREV() 
	{
		return PRODUCTREV;
	}

	public void setPRODUCTREV(String PRODUCTREV) {
        this.PRODUCTREV = PRODUCTREV;
        this.DATAMAP.put("PRODUCTREV", PRODUCTREV);
	}

    // ECNNO
	public String getECNNO() 
	{
		return ECNNO;
	}

	public void setECNNO(String ECNNO) {
        this.ECNNO = ECNNO;
        this.DATAMAP.put("ECNNO", ECNNO);
	}

    // ISNO
	public String getISNO() 
	{
		return ISNO;
	}

	public void setISNO(String ISNO) {
        this.ISNO = ISNO;
        this.DATAMAP.put("ISNO", ISNO);
	}

    // MRPMANAGER
	public String getMRPMANAGER() 
	{
		return MRPMANAGER;
	}

	public void setMRPMANAGER(String MRPMANAGER) {
        this.MRPMANAGER = MRPMANAGER;
        this.DATAMAP.put("MRPMANAGER", MRPMANAGER);
	}

    // TOPCOATING
	public String getTOPCOATING() 
	{
		return TOPCOATING;
	}

	public void setTOPCOATING(String TOPCOATING) {
        this.TOPCOATING = TOPCOATING;
        this.DATAMAP.put("TOPCOATING", TOPCOATING);
	}

    // PRIMERCOATING
	public String getPRIMERCOATING() 
	{
		return PRIMERCOATING;
	}

	public void setPRIMERCOATING(String PRIMERCOATING) {
        this.PRIMERCOATING = PRIMERCOATING;
        this.DATAMAP.put("PRIMERCOATING", PRIMERCOATING);
	}

    // INSPECTIONFLAG
	public String getINSPECTIONFLAG() {
		return INSPECTIONFLAG;
	}

	public void setINSPECTIONFLAG(String INSPECTIONFLAG) {
        this.INSPECTIONFLAG = INSPECTIONFLAG;
        this.DATAMAP.put("INSPECTIONFLAG", INSPECTIONFLAG);
	}

    // FIRSTPRODUCT
	public String getFIRSTPRODUCT() {
		return FIRSTPRODUCT;
	}

	public void setFIRSTPRODUCT(String FIRSTPRODUCT) {
        this.FIRSTPRODUCT = FIRSTPRODUCT;
        this.DATAMAP.put("FIRSTPRODUCT", FIRSTPRODUCT);
	}

    // FERTH
	public String getFERTH() {
		return FERTH;
	}

	public void setFERTH(String FERTH) {
        this.FERTH = FERTH;
        this.DATAMAP.put("FERTH", FERTH);
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
