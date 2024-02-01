package mes.userdefine.data;

import java.util.HashMap;

import mes.lot.data.LOTINFORMATION;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class LOTINFORMATION_UDF
{
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public LOTINFORMATION_UDF() { }
    
    public LOTINFORMATION_UDF(LOTINFORMATION lotInformation)
    {
    	this.DATAMAP = lotInformation.getDATAMAP();
    }
    
    // Data Info
    private String ATTR;

    // Data Methods
    // ATTR
    public String getATTR()
    {
        return ATTR;
    }
    public void setATTR(String ATTR)
    {
        this.ATTR = ATTR;
        this.DATAMAP.put("ATTR", ATTR);
    }
}
