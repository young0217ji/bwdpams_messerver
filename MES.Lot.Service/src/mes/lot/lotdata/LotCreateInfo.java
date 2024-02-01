package mes.lot.lotdata;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class LotCreateInfo
{
    // Key Info
	private String LOTID;

    // Data Info
	private String PLANTID;
    private String WORKORDER;
    private String PRODUCTID;
    private String DEPARTMENT;
    private String AREAID;
    private String EQUIPMENTID;
    private Double STARTQUANTITY;
    
    private String BOMID;
    private String BOMVERSION;
    private String POLICYNAME;
    private String POLICYVALUE;
    private String CONDITIONTYPE;
    private String CONDITIONID;
    private String LOTTYPE;
    private String PRIORITY;

    public LotCreateInfo() { }

	public void setLOTID(String LOTID)
	{
		this.LOTID = LOTID;
	}

	public String getLOTID()
	{
		return LOTID;
	}

	public void setPLANTID(String PLANTID)
	{
		this.PLANTID = PLANTID;
	}

	public String getPLANTID()
	{
		return PLANTID;
	}

	public void setWORKORDER(String WORKORDER)
	{
		this.WORKORDER = WORKORDER;
	}

	public String getWORKORDER()
	{
		return WORKORDER;
	}

	public void setPRODUCTID(String PRODUCTID)
	{
		this.PRODUCTID = PRODUCTID;
	}

	public String getPRODUCTID()
	{
		return PRODUCTID;
	}

	public void setDEPARTMENT(String DEPARTMENT)
	{
		this.DEPARTMENT = DEPARTMENT;
	}

	public String getDEPARTMENT()
	{
		return DEPARTMENT;
	}

	public void setAREAID(String AREAID)
	{
		this.AREAID = AREAID;
	}

	public String getAREAID()
	{
		return AREAID;
	}
	
	public void setEQUIPMENTID(String EQUIPMENTID)
	{
		this.EQUIPMENTID = EQUIPMENTID;
	}

	public String getEQUIPMENTID()
	{
		return EQUIPMENTID;
	}
	
	public void setSTARTQUANTITY(Double STARTQUANTITY)
	{
		this.STARTQUANTITY = STARTQUANTITY;
	}

	public Double getSTARTQUANTITY()
	{
		return STARTQUANTITY;
	}
	
	// 모델링 기준정보
	public void setBOMID(String BOMID)
	{
		this.BOMID = BOMID;
	}

	public String getBOMID()
	{
		return BOMID;
	}

	public void setBOMVERSION(String BOMVERSION)
	{
		this.BOMVERSION = BOMVERSION;
	}

	public String getBOMVERSION()
	{
		return BOMVERSION;
	}
	
	public void setPOLICYNAME(String POLICYNAME)
	{
		this.POLICYNAME = POLICYNAME;
	}

	public String getPOLICYNAME()
	{
		return POLICYNAME;
	}
	
	public void setPOLICYVALUE(String POLICYVALUE)
	{
		this.POLICYVALUE = POLICYVALUE;
	}

	public String getPOLICYVALUE()
	{
		return POLICYVALUE;
	}
	
	public void setCONDITIONTYPE(String CONDITIONTYPE)
	{
		this.CONDITIONTYPE = CONDITIONTYPE;
	}

	public String getCONDITIONTYPE()
	{
		return CONDITIONTYPE;
	}
	
	public void setCONDITIONID(String CONDITIONID)
	{
		this.CONDITIONID = CONDITIONID;
	}

	public String getCONDITIONID()
	{
		return CONDITIONID;
	}
	
	public void setLOTTYPE(String LOTTYPE)
	{
		this.LOTTYPE = LOTTYPE;
	}

	public String getLOTTYPE()
	{
		return LOTTYPE;
	}
	
	public void setPRIORITY(String PRIORITY)
	{
		this.PRIORITY = PRIORITY;
	}

	public String getPRIORITY()
	{
		return PRIORITY;
	}
}
