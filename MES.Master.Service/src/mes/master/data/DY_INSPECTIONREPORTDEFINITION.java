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
public class DY_INSPECTIONREPORTDEFINITION extends PRODUCTDEFINITION_UDF
{
    // Key Info
    private String PLANTID;
    private String PRODUCTID;

    // Data Info
    private String ENDPRODUCTID;
    private String ENDPRODUCTNAME;
    private String USERPRODUCTID;
    private String APPEARN;
    private String WORKABILITY;
    private String PROOF_TEST;
    private String INTERNAL_LEAKAGE;
    private String EXTERNAL_LEAKAGE;
    private String MIN_OPER_PRESS;
    private String WELDING;
    private String ROD_SURFACE;
    private String THREAD;
    private String STROKE_BASE;
    private String STROKE_UPPER;
    private String STROKE_LOWER;
    private String MARK_POINT;
    private String LOT_NO;
    private String HC_INSIDE_DIA_BASE;
    private String HC_INSIDE_DIA_UPPPER;
    private String HC_INSIDE_DIA_LOWER;
    private String HC_SIDTH_BASE;
    private String HC_SIDTH_UPPER;
    private String HC_SIDTH_LOWER;
    private String KC_INSIDE_DIA_BASE;
    private String KC_INSIDE_DIA_UPPPER;
    private String KC_INSIDE_DIA_LOWER;
    private String KC_SIDTH_BASE;
    private String KC_SIDTH_UPPER;
    private String KC_SIDTH_LOWER;
    private String CLOSED_LENTH_BASE;
    private String CLOSED_LENTH_UPPER;
    private String CLOSED_LENTH_LOWER;
    private String LENGTH_RC_TO_KC_BASE;
    private String LENGTH_RC_TO_KC_UPPER;
    private String LENGTH_RC_TO_KC_LOWER;
    private String DESCRIPTION;
    private String CREATEUSERID;
    private Timestamp CREATETIME;
    private String LASTUPDATEUSERID;
    private Timestamp LASTUPDATETIME;
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DY_INSPECTIONREPORTDEFINITION() { }

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

    // APPEARN
    public String getAPPEARN()
    {
        return APPEARN;
    }
    public void setAPPEARN(String APPEARN)
    {
        this.APPEARN = APPEARN;
        this.DATAMAP.put("APPEARN", APPEARN);
    }

    // WORKABILITY
    public String getWORKABILITY()
    {
        return WORKABILITY;
    }
    public void setWORKABILITY(String WORKABILITY)
    {
        this.WORKABILITY = WORKABILITY;
        this.DATAMAP.put("WORKABILITY", WORKABILITY);
    }
    
    // PROOF_TEST
    public String getPROOF_TEST()
    {
        return PROOF_TEST;
    }
    public void setPROOF_TEST(String PROOF_TEST)
    {
        this.PROOF_TEST = PROOF_TEST;
        this.DATAMAP.put("PROOF_TEST", PROOF_TEST);
    }
    
    // INTERNAL_LEAKAGE
    public String getINTERNAL_LEAKAGE() 
    {
		return INTERNAL_LEAKAGE;
	}

	public void setINTERNAL_LEAKAGE(String INTERNAL_LEAKAGE) {
        this.INTERNAL_LEAKAGE = INTERNAL_LEAKAGE;
        this.DATAMAP.put("INTERNAL_LEAKAGE", INTERNAL_LEAKAGE);
	}
	
    // EXTERNAL_LEAKAGE
	public String getEXTERNAL_LEAKAGE() 
	{
		return EXTERNAL_LEAKAGE;
	}

	public void setEXTERNAL_LEAKAGE(String EXTERNAL_LEAKAGE) {
        this.EXTERNAL_LEAKAGE = EXTERNAL_LEAKAGE;
        this.DATAMAP.put("EXTERNAL_LEAKAGE", EXTERNAL_LEAKAGE);
	}

    // MIN_OPER_PRESS
	public String getMIN_OPER_PRESS() 
	{
		return MIN_OPER_PRESS;
	}

	public void setMIN_OPER_PRESS(String MIN_OPER_PRESS) {
        this.MIN_OPER_PRESS = MIN_OPER_PRESS;
        this.DATAMAP.put("MIN_OPER_PRESS", MIN_OPER_PRESS);
	}

    // WELDING
	public String getWELDING() 
	{
		return WELDING;
	}

	public void setWELDING(String WELDING) {
        this.WELDING = WELDING;
        this.DATAMAP.put("WELDING", WELDING);
	}

    // ROD_SURFACE
	public String getROD_SURFACE() 
	{
		return ROD_SURFACE;
	}

	public void setROD_SURFACE(String ROD_SURFACE) {
        this.ROD_SURFACE = ROD_SURFACE;
        this.DATAMAP.put("ROD_SURFACE", ROD_SURFACE);
	}

    // THREAD
	public String getTHREAD() 
	{
		return THREAD;
	}

	public void setTHREAD(String THREAD) {
        this.THREAD = THREAD;
        this.DATAMAP.put("THREAD", THREAD);
	}

    // STROKE_BASE
	public String getSTROKE_BASE() 
	{
		return STROKE_BASE;
	}

	public void setSTROKE_BASE(String STROKE_BASE) {
        this.STROKE_BASE = STROKE_BASE;
        this.DATAMAP.put("STROKE_BASE", STROKE_BASE);
	}

    // STROKE_UPPER
	public String getSTROKE_UPPER() 
	{
		return STROKE_UPPER;
	}

	public void setSTROKE_UPPER(String STROKE_UPPER) {
        this.STROKE_UPPER = STROKE_UPPER;
        this.DATAMAP.put("STROKE_UPPER", STROKE_UPPER);
	}

    // STROKE_LOWER
	public String getSTROKE_LOWER() {
		return STROKE_LOWER;
	}

	public void setSTROKE_LOWER(String STROKE_LOWER) {
        this.STROKE_LOWER = STROKE_LOWER;
        this.DATAMAP.put("STROKE_LOWER", STROKE_LOWER);
	}
    
    // MARK_POINT
	public String getMARK_POINT() {
		return MARK_POINT;
	}

	public void setMARK_POINT(String MARK_POINT) {
        this.MARK_POINT = MARK_POINT;
        this.DATAMAP.put("MARK_POINT", MARK_POINT);
	}
	
    // LOT_NO
	public String getLOT_NO() {
		return LOT_NO;
	}

	public void setLOT_NO(String LOT_NO) {
        this.LOT_NO = LOT_NO;
        this.DATAMAP.put("LOT_NO", LOT_NO);
	}
	
    // HC_INSIDE_DIA_BASE
	public String getHC_INSIDE_DIA_BASE() {
		return HC_INSIDE_DIA_BASE;
	}

	public void setHC_INSIDE_DIA_BASE(String HC_INSIDE_DIA_BASE) {
        this.HC_INSIDE_DIA_BASE = HC_INSIDE_DIA_BASE;
        this.DATAMAP.put("HC_INSIDE_DIA_BASE", HC_INSIDE_DIA_BASE);
	}
	
    // HC_INSIDE_DIA_UPPPER
	public String getHC_INSIDE_DIA_UPPPER() {
		return HC_INSIDE_DIA_UPPPER;
	}

	public void setHC_INSIDE_DIA_UPPPER(String HC_INSIDE_DIA_UPPPER) {
        this.HC_INSIDE_DIA_UPPPER = HC_INSIDE_DIA_UPPPER;
        this.DATAMAP.put("HC_INSIDE_DIA_UPPPER", HC_INSIDE_DIA_UPPPER);
	}
	
    // HC_INSIDE_DIA_LOWER
	public String getHC_INSIDE_DIA_LOWER() {
		return HC_INSIDE_DIA_LOWER;
	}

	public void setHC_INSIDE_DIA_LOWER(String HC_INSIDE_DIA_LOWER) {
        this.HC_INSIDE_DIA_LOWER = HC_INSIDE_DIA_LOWER;
        this.DATAMAP.put("HC_INSIDE_DIA_LOWER", HC_INSIDE_DIA_LOWER);
	}
	
    // HC_SIDTH_BASE
	public String getHC_SIDTH_BASE() {
		return HC_SIDTH_BASE;
	}

	public void setHC_SIDTH_BASE(String HC_SIDTH_BASE) {
        this.HC_SIDTH_BASE = HC_SIDTH_BASE;
        this.DATAMAP.put("HC_SIDTH_BASE", HC_SIDTH_BASE);
	}
	
    // HC_SIDTH_UPPER
	public String getHC_SIDTH_UPPER() {
		return HC_SIDTH_UPPER;
	}

	public void setHC_SIDTH_UPPER(String HC_SIDTH_UPPER) {
        this.HC_SIDTH_UPPER = HC_SIDTH_UPPER;
        this.DATAMAP.put("HC_SIDTH_UPPER", HC_SIDTH_UPPER);
	}
	
    // HC_SIDTH_LOWER
	public String getHC_SIDTH_LOWER() {
		return HC_SIDTH_LOWER;
	}

	public void setHC_SIDTH_LOWER(String HC_SIDTH_LOWER) {
        this.HC_SIDTH_LOWER = HC_SIDTH_LOWER;
        this.DATAMAP.put("HC_SIDTH_LOWER", HC_SIDTH_LOWER);
	}
	
    // KC_INSIDE_DIA_BASE
	public String getKC_INSIDE_DIA_BASE() {
		return KC_INSIDE_DIA_BASE;
	}

	public void setKC_INSIDE_DIA_BASE(String KC_INSIDE_DIA_BASE) {
        this.KC_INSIDE_DIA_BASE = KC_INSIDE_DIA_BASE;
        this.DATAMAP.put("KC_INSIDE_DIA_BASE", KC_INSIDE_DIA_BASE);
	}
	
    // KC_INSIDE_DIA_UPPPER
	public String getKC_INSIDE_DIA_UPPPER() {
		return KC_INSIDE_DIA_UPPPER;
	}

	public void setKC_INSIDE_DIA_UPPPER(String KC_INSIDE_DIA_UPPPER) {
        this.KC_INSIDE_DIA_UPPPER = KC_INSIDE_DIA_UPPPER;
        this.DATAMAP.put("KC_INSIDE_DIA_UPPPER", KC_INSIDE_DIA_UPPPER);
	}
	
    // KC_INSIDE_DIA_LOWER
	public String getKC_INSIDE_DIA_LOWER() {
		return KC_INSIDE_DIA_LOWER;
	}

	public void setKC_INSIDE_DIA_LOWER(String KC_INSIDE_DIA_LOWER) {
        this.KC_INSIDE_DIA_LOWER = KC_INSIDE_DIA_LOWER;
        this.DATAMAP.put("KC_INSIDE_DIA_LOWER", KC_INSIDE_DIA_LOWER);
	}
	
    // KC_SIDTH_BASE
	public String getKC_SIDTH_BASE() {
		return KC_SIDTH_BASE;
	}

	public void setKC_SIDTH_BASE(String KC_SIDTH_BASE) {
        this.KC_SIDTH_BASE = KC_SIDTH_BASE;
        this.DATAMAP.put("KC_SIDTH_BASE", KC_SIDTH_BASE);
	}
	
    // KC_SIDTH_UPPER
	public String getKC_SIDTH_UPPER() {
		return KC_SIDTH_UPPER;
	}

	public void setKC_SIDTH_UPPER(String KC_SIDTH_UPPER) {
        this.KC_SIDTH_UPPER = KC_SIDTH_UPPER;
        this.DATAMAP.put("KC_SIDTH_UPPER", KC_SIDTH_UPPER);
	}
	
    // KC_SIDTH_LOWER
	public String getKC_SIDTH_LOWER() {
		return KC_SIDTH_LOWER;
	}

	public void setKC_SIDTH_LOWER(String KC_SIDTH_LOWER) {
        this.KC_SIDTH_LOWER = KC_SIDTH_LOWER;
        this.DATAMAP.put("KC_SIDTH_LOWER", KC_SIDTH_LOWER);
	}
	
    // CLOSED_LENTH_BASE
	public String getCLOSED_LENTH_BASE() {
		return CLOSED_LENTH_BASE;
	}

	public void setCLOSED_LENTH_BASE(String CLOSED_LENTH_BASE) {
        this.CLOSED_LENTH_BASE = CLOSED_LENTH_BASE;
        this.DATAMAP.put("CLOSED_LENTH_BASE", CLOSED_LENTH_BASE);
	}
	
    // CLOSED_LENTH_UPPER
	public String getCLOSED_LENTH_UPPER() {
		return CLOSED_LENTH_UPPER;
	}

	public void setCLOSED_LENTH_UPPER(String CLOSED_LENTH_UPPER) {
        this.CLOSED_LENTH_UPPER = CLOSED_LENTH_UPPER;
        this.DATAMAP.put("CLOSED_LENTH_UPPER", CLOSED_LENTH_UPPER);
	}
	
    // CLOSED_LENTH_LOWER
	public String getCLOSED_LENTH_LOWER() {
		return CLOSED_LENTH_LOWER;
	}

	public void setCLOSED_LENTH_LOWER(String CLOSED_LENTH_LOWER) {
        this.CLOSED_LENTH_LOWER = CLOSED_LENTH_LOWER;
        this.DATAMAP.put("CLOSED_LENTH_LOWER", CLOSED_LENTH_LOWER);
	}
	
    // LENGTH_RC_TO_KC_BASE
	public String getLENGTH_RC_TO_KC_BASE() {
		return LENGTH_RC_TO_KC_BASE;
	}

	public void setLENGTH_RC_TO_KC_BASE(String LENGTH_RC_TO_KC_BASE) {
        this.LENGTH_RC_TO_KC_BASE = LENGTH_RC_TO_KC_BASE;
        this.DATAMAP.put("LENGTH_RC_TO_KC_BASE", LENGTH_RC_TO_KC_BASE);
	}
	
    // LENGTH_RC_TO_KC_UPPER
	public String getLENGTH_RC_TO_KC_UPPER() {
		return LENGTH_RC_TO_KC_UPPER;
	}

	public void setLENGTH_RC_TO_KC_UPPER(String LENGTH_RC_TO_KC_UPPER) {
        this.LENGTH_RC_TO_KC_UPPER = LENGTH_RC_TO_KC_UPPER;
        this.DATAMAP.put("LENGTH_RC_TO_KC_UPPER", LENGTH_RC_TO_KC_UPPER);
	}
	
    // LENGTH_RC_TO_KC_LOWER
	public String getLENGTH_RC_TO_KC_LOWER() {
		return LENGTH_RC_TO_KC_LOWER;
	}

	public void setLENGTH_RC_TO_KC_LOWER(String LENGTH_RC_TO_KC_LOWER) {
        this.LENGTH_RC_TO_KC_LOWER = LENGTH_RC_TO_KC_LOWER;
        this.DATAMAP.put("LENGTH_RC_TO_KC_LOWER", LENGTH_RC_TO_KC_LOWER);
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
