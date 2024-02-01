package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.16
 * 
 * @see
 */
public class DY_PLANWEEKRESULT {

	// Key Info
	private int DATAKEY;
	
	// Data Info
	private String PLANTID;
	private String WORKCENTER;
	private String PLANVERSION;
	private String PRODUCTORDERTYPE;
	private String PRODUCTORDERID;
	private String PARENTORDERID;
	private String GROUPPRODUCTORDERID;
	private String PRODUCTID;
	private String PLANOPERID;
	private int PLANQUANTITY;
	private String PLANUNIT;
	private Timestamp DUEDATE;
	private Timestamp PLANSTARTTIME;
	private String PRODUCTITEM;
	private Timestamp PLANENDTIME;
	private String WORKORDERID;
	private String PLANSTATUS;
	private String DESCRIPTION;
	private Double CYCLETIME;
	private String PEGGING;
	private String ORDERSOURCE;
	private Timestamp CREATETIME;
	private String CREATEUSERID;
	private Timestamp LASTUPDATETIME;
	private String LASTUPDATEUSERID;
	
	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    
	public DY_PLANWEEKRESULT() {}
	
	// Key Methods
	// DATAKEY
	public int getKeyDATAKEY() {
		return DATAKEY;
	}
	public void setKeyDATAKEY(int DATAKEY) {
		this.DATAKEY = DATAKEY;
		this.KEYMAP.put("DATAKEY", DATAKEY);
	}
	
	// Data Methods
	// PLANTID
	public String getPLANTID() {
		return PLANTID;
	}
	public void setPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.DATAMAP.put("PLANTID", PLANTID);
	}
	
	// WORKCENTER
	public String getWORKCENTER() {
		return WORKCENTER;
	}
	public void setWORKCENTER(String WORKCENTER) {
		this.WORKCENTER = WORKCENTER;
		this.DATAMAP.put("WORKCENTER", WORKCENTER);
	}
	
	// PLANVERSION 
	public String getPLANVERSION() {
		return PLANVERSION;
	}
	public void setPLANVERSION(String PLANVERSION) {
		this.PLANVERSION = PLANVERSION;
		this.DATAMAP.put("PLANVERSION", PLANVERSION);
	}
	
	// PRODUCTORDERTYPE
	public String getPRODUCTORDERTYPE() {
		return PRODUCTORDERTYPE;
	}
	public void setPRODUCTORDERTYPE(String PRODUCTORDERTYPE) {
		this.PRODUCTORDERTYPE = PRODUCTORDERTYPE;
		this.DATAMAP.put("PRODUCTORDERTYPE", PRODUCTORDERTYPE);
	}
	
	// PRODUCTORDERID
	public String getPRODUCTORDERID() {
		return PRODUCTORDERID;
	}
	public void setPRODUCTORDERID(String PRODUCTORDERID) {
		this.PRODUCTORDERID = PRODUCTORDERID;
		this.DATAMAP.put("PRODUCTORDERID", PRODUCTORDERID);
	}
	
	// PARENTORDERID
	public String getPARENTORDERID() {
		return PARENTORDERID;
	}
	public void setPARENTORDERID(String PARENTORDERID) {
		this.PARENTORDERID = PARENTORDERID;
		this.DATAMAP.put("PARENTORDERID", PARENTORDERID);
	}
	
	// GROUPPRODUCTORDERID
	public String getGROUPPRODUCTORDERID() {
		return GROUPPRODUCTORDERID;
	}
	public void setGROUPPRODUCTORDERID(String GROUPPRODUCTORDERID) {
		this.GROUPPRODUCTORDERID = GROUPPRODUCTORDERID;
		this.DATAMAP.put("GROUPPRODUCTORDERID", GROUPPRODUCTORDERID);
	}
	
	// PRODUCTID
	public String getPRODUCTID() {
		return PRODUCTID;
	}
	public void setPRODUCTID(String PRODUCTID) {
		this.PRODUCTID = PRODUCTID;
		this.DATAMAP.put("PRODUCTID", PRODUCTID);
	}
	
	// PLANOPERID
	public String getPLANOPERID() {
		return PLANOPERID;
	}
	public void setPLANOPERID(String PLANOPERID) {
		this.PLANOPERID = PLANOPERID;
		this.DATAMAP.put("PLANOPERID", PLANOPERID);
	}
	
	// PLANUNIT
	public String getPLANUNIT() {
		return PLANUNIT;
	}
	public void setPLANUNIT(String PLANUNIT) {
		this.PLANUNIT = PLANUNIT;
		this.DATAMAP.put("PLANUNIT", PLANUNIT);
	}
	
	// PLANQUANTITY
	public int getPLANQUANTITY() {
		return PLANQUANTITY;
	}
	public void setPLANQUANTITY(int PLANQUANTITY) {
		this.PLANQUANTITY = PLANQUANTITY;
		this.DATAMAP.put("PLANQUANTITY", PLANQUANTITY);
	}
	
	// DUEDATE
	public Timestamp getDUEDATE() {
		return DUEDATE;
	}
	public void setDUEDATE(Timestamp DUEDATE) {
		this.DUEDATE = DUEDATE;
		this.DATAMAP.put("DUEDATE", DUEDATE);
	}
	
	// PLANSTARTTIME
	public Timestamp getPLANSTARTTIME() {
		return PLANSTARTTIME;
	}
	public void setPLANSTARTTIME(Timestamp PLANSTARTTIME) {
		this.PLANSTARTTIME = PLANSTARTTIME;
		this.DATAMAP.put("PLANSTARTTIME", PLANSTARTTIME);
	}
	
	// PRODUCTITEM
	public String getPRODUCTITEM() {
		return PRODUCTITEM;
	}
	public void setPRODUCTITEM(String PRODUCTITEM) {
		this.PRODUCTITEM = PRODUCTITEM;
		this.DATAMAP.put("PRODUCTITEM", PRODUCTITEM);
	}
	
	// PLANENDTIME
	public Timestamp getPLANENDTIME() {
		return PLANENDTIME;
	}
	public void setPLANENDTIME(Timestamp PLANENDTIME) {
		this.PLANENDTIME = PLANENDTIME;
		this.DATAMAP.put("PLANENDTIME", PLANENDTIME);
	}

	// WORKORDERID
	public String getWORKORDERID() {
		return WORKORDERID;
	}
	public void setWORKORDERID(String WORKORDERID) {
		this.WORKORDERID = WORKORDERID;
		this.DATAMAP.put("WORKORDERID", WORKORDERID);
	}
	
	// PLANSTATUS
	public String getPLANSTATUS() {
		return PLANSTATUS;
	}
	public void setPLANSTATUS(String PLANSTATUS) {
		this.PLANSTATUS = PLANSTATUS;
		this.DATAMAP.put("PLANSTATUS", PLANSTATUS);
	}
	
	// DESCRIPTION
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String DESCRIPTION) {
		this.DESCRIPTION = DESCRIPTION;
		this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
	}
	
	// CYCLETIME
	public Double getCYCLETIME() {
		return CYCLETIME;
	}
	public void setCYCLETIME(Double CYCLETIME) {
		this.CYCLETIME = CYCLETIME;
		this.DATAMAP.put("CYCLETIME", CYCLETIME);
	}
	
	// PEGGING
	public String getPEGGING() {
		return PEGGING;
	}
	public void setPEGGING(String PEGGING) {
		this.PEGGING = PEGGING;
		this.DATAMAP.put("PEGGING", PEGGING);
	}
	
	// ORDERSOURCE
	public String getORDERSOURCE() {
		return ORDERSOURCE;
	}
	public void setORDERSOURCE(String ORDERSOURCE) {
		this.ORDERSOURCE = ORDERSOURCE;
		this.DATAMAP.put("ORDERSOURCE", ORDERSOURCE);
	}
	
	//
	public Timestamp getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(Timestamp CREATETIME) {
		this.CREATETIME = CREATETIME;
		this.DATAMAP.put("CREATETIME", CREATETIME);
	}
	
	// CREATEUSERID
	public String getCREATEUSERID() {
		return CREATEUSERID;
	}
	public void setCREATEUSERID(String CREATEUSERID) {
		this.CREATEUSERID = CREATEUSERID;
		this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
	}
	
	// LASTUPDATETIME
	public Timestamp getLASTUPDATETIME() {
		return LASTUPDATETIME;
	}
	public void setLASTUPDATETIME(Timestamp LASTUPDATETIME) {
		this.LASTUPDATETIME = LASTUPDATETIME;
		this.DATAMAP.put("LASTUPDATETIME", LASTUPDATETIME);
	}
	
	// LASTUPDATEUSERID
	public String getLASTUPDATEUSERID() {
		return LASTUPDATEUSERID;
	}
	public void setLASTUPDATEUSERID(String LASTUPDATEUSERID) {
		this.LASTUPDATEUSERID = LASTUPDATEUSERID;
		this.DATAMAP.put("LASTUPDATEUSERID", LASTUPDATEUSERID);
	}
	
	// Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.DATAKEY == 0)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"DATAKEY"});
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
