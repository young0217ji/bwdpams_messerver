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
public class DSPCH_EVENT
{
    // Key Info
	private String FACTORY_ID;
    private String SVR_NM;
    private String EVENT_NM;

    // Data Info
    private String CLASS_NM;
    private String TGT_SBJT_NM;
    private String TGT_FACTORY;
    private String LOG_SAVE_FLAG;

    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public DSPCH_EVENT() { }

    // Key Methods
    // FACTORY_ID
    public String getKeyFACTORY_ID() {
        return FACTORY_ID;
    }
    public void setKeyFACTORY_ID(String FACTORY_ID) {
        this.FACTORY_ID = FACTORY_ID;
        this.KEYMAP.put("FACTORY_ID", FACTORY_ID);
    }
    
    // SVR_NM
    public String getKeySVR_NM() {
        return SVR_NM;
    }
    public void setKeySVR_NM(String SVR_NM) {
        this.SVR_NM = SVR_NM;
        this.KEYMAP.put("SVR_NM", SVR_NM);
    }

    // EVENT_NM
    public String getKeyEVENT_NM() {
        return EVENT_NM;
    }
    public void setKeyEVENT_NM(String EVENT_NM) {
        this.EVENT_NM = EVENT_NM;
        this.KEYMAP.put("EVENT_NM", EVENT_NM);
    }


    // Data Methods
    // CLASS_NM
    public String getCLASS_NM() {
        return CLASS_NM;
    }
    public void setCLASS_NM(String CLASS_NM) {
        this.CLASS_NM = CLASS_NM;
        this.DATAMAP.put("CLASS_NM", CLASS_NM);
    }

    // TGT_SBJT_NM
    public String getTGT_SBJT_NM() {
        return TGT_SBJT_NM;
    }
    public void setTGT_SBJT_NM(String TGT_SBJT_NM) {
        this.TGT_SBJT_NM = TGT_SBJT_NM;
        this.DATAMAP.put("TGT_SBJT_NM", TGT_SBJT_NM);
    }
    
    // TGT_FACTORY
    public String getTGT_FACTORY() {
        return TGT_FACTORY;
    }
    public void setTGT_FACTORY(String TGT_FACTORY) {
        this.TGT_FACTORY = TGT_FACTORY;
        this.DATAMAP.put("TGT_FACTORY", TGT_FACTORY);
    }
    
    // LOG_SAVE_FLAG
    public String getLOG_SAVE_FLAG() {
        return LOG_SAVE_FLAG;
    }
    public void setLOG_SAVE_FLAG(String LOG_SAVE_FLAG) {
        this.LOG_SAVE_FLAG = LOG_SAVE_FLAG;
        this.DATAMAP.put("LOG_SAVE_FLAG", LOG_SAVE_FLAG);
    }
    

    // Key Validation
    public void checkKeyNotNull() 
    {
        if ( this.KEYMAP.isEmpty() || this.FACTORY_ID == null || this.SVR_NM == null || this.EVENT_NM == null )
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"FACTORY_ID, SVR_NM, EVENT_NM"});
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
