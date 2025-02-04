package mes.userdefine.data;

import java.util.HashMap;

import mes.master.data.PRODUCTDEFINITION;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class PRODUCTDEFINITION_UDF
{
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public PRODUCTDEFINITION_UDF() { }
    
    public PRODUCTDEFINITION_UDF(PRODUCTDEFINITION productDefinition)
    {
    	this.DATAMAP = productDefinition.getDATAMAP();
    }
    
    // Data Info
    private String PACKINGTYPE;

    // Data Methods
    // ATTR
    public String getPACKINGTYPE()
    {
        return PACKINGTYPE;
    }
    public void setPACKINGTYPE(String PACKINGTYPE)
    {
        this.PACKINGTYPE = PACKINGTYPE;
        this.DATAMAP.put("PACKINGTYPE", PACKINGTYPE);
    }
}
