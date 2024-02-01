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
public class DURABLEINFORMATION_UDF
{
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public DURABLEINFORMATION_UDF() { }
    
    public DURABLEINFORMATION_UDF(LOTINFORMATION lotInformation)
    {
    	this.DATAMAP = lotInformation.getDATAMAP();
    }
    
    // Data Info

    // Data Methods
}
