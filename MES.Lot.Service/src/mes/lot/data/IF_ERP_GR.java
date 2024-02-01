package mes.lot.data;

import java.sql.Timestamp;
import java.util.HashMap;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.22 
 * 
 * @see
 */
public class IF_ERP_GR
{
    // Key Info
    private Long IFID; // Interface ID
    private Long SEQ; // Sequence : 기본 실적 1 고정값, 부적합 2 고정값

    // Data Info
    private String MANDT; // Client : 100 고정값
    private String WERKS; // Plant ID
    private String AUFNR; // 작지 번호
    private String BUDAT; // Posting Date : 실적일자 YYYYMMDD
    private String LGORT; // Storage Location
    private Double ERFMG; // 수량 : 입고수량
    private String MEINS; // Unit
    private String INSMK; // Stock Type : 부적합='S', 양품=NULL 고정값
    private Timestamp ADD_DTTM;
    private String ADD_USERID;
    private String ERP_STATUS;
    private String ERP_MESSAGE;
    private Timestamp ERP_DTTM;
    
//    컬럼명	#	설명	Type	Length	Scale	정밀도	Not Null	Identity	디폴트	Collation
//    IFID	1	Sequence	int	4	[NULL]	10	true	false	[NULL]	[NULL]
//    SEQ	2	MENU ID	int	4	[NULL]	10	true	false	[NULL]	[NULL]
//    MANDT	3	Client	nvarchar	3	[NULL]	[NULL]	false	false	[NULL]	Korean_Wansung_CI_AS
//    WERKS	4	Plant ID	nvarchar	4	[NULL]	[NULL]	false	false	[NULL]	Korean_Wansung_CI_AS
//    AUFNR	5	작지 번호	nvarchar	12	[NULL]	[NULL]	false	false	[NULL]	Korean_Wansung_CI_AS
//    BUDAT	6	Posting Date	nvarchar	8	[NULL]	[NULL]	false	false	[NULL]	Korean_Wansung_CI_AS
//    LGORT	7	Storage Location	nvarchar	4	[NULL]	[NULL]	false	false	[NULL]	Korean_Wansung_CI_AS
//    ERFMG	8	수량	decimal	9	3	13	false	false	[NULL]	[NULL]
//    MEINS	9	Unit	nvarchar	3	[NULL]	[NULL]	false	false	[NULL]	Korean_Wansung_CI_AS
//    INSMK	10	Stock Type	nvarchar	1	[NULL]	[NULL]	false	false	[NULL]	Korean_Wansung_CI_AS
//    ADD_DTTM	11	생성일시	datetime	8	3	23	false	false	[NULL]	[NULL]
//    ADD_USERID	12	생성자	nvarchar	50	[NULL]	[NULL]	false	false	[NULL]	Korean_Wansung_CI_AS
//    ERP_STATUS	13	ERP 처리 Status	nvarchar	1	[NULL]	[NULL]	false	false	[NULL]	Korean_Wansung_CI_AS
//    ERP_MESSAGE	14	ERP 처리 Message	nvarchar	200	[NULL]	[NULL]	false	false	[NULL]	Korean_Wansung_CI_AS
//    ERP_DTTM	15	ERP 처리 DateTime	datetime	8	3	23	false	false	[NULL]	[NULL]
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public IF_ERP_GR() { }

    // Key Methods
    // IFID
    public Long getKeyIFID()
    {
        return IFID;
    }
    public void setKeyIFID(Long IFID)
    {
        this.IFID = IFID;
        this.KEYMAP.put("IFID", IFID);
    }
    
    // SEQ
    public Long getKeySEQ()
    {
        return SEQ;
    }
    public void setKeySEQ(Long SEQ)
    {
        this.SEQ = SEQ;
        this.KEYMAP.put("SEQ", SEQ);
    }

    // Data Methods
    // MANDT
    public String getMANDT()
    {
        return MANDT;
    }
    public void setMANDT(String MANDT)
    {
        this.MANDT = MANDT;
        this.DATAMAP.put("MANDT", MANDT);
    }

    // WERKS
    public String getWERKS()
    {
        return WERKS;
    }
    public void setWERKS(String WERKS)
    {
        this.WERKS = WERKS;
        this.DATAMAP.put("WERKS", WERKS);
    }

    // AUFNR
    public String geAUFNR()
    {
        return AUFNR;
    }
    public void setAUFNR(String AUFNR)
    {
        this.AUFNR = AUFNR;
        this.DATAMAP.put("AUFNR", AUFNR);
    }
    
    // BUDAT
    public String geBUDAT()
    {
        return BUDAT;
    }
    public void setBUDAT(String BUDAT)
    {
        this.BUDAT = BUDAT;
        this.DATAMAP.put("BUDAT", BUDAT);
    }
    
    // LGORT
    public String geLGORT()
    {
        return LGORT;
    }
    public void setLGORT(String LGORT)
    {
        this.LGORT = LGORT;
        this.DATAMAP.put("LGORT", LGORT);
    }
    
    // ERFMG
    public Double getERFMG()
    {
        return ERFMG;
    }
    public void setERFMG(Double ERFMG)
    {
        this.ERFMG = ERFMG;
        this.DATAMAP.put("ERFMG", ERFMG);
    }
    
    // MEINS
    public String geMEINS()
    {
        return MEINS;
    }
    public void setMEINS(String MEINS)
    {
        this.MEINS = MEINS;
        this.DATAMAP.put("MEINS", MEINS);
    }
    
    // INSMK
    public String geINSMK()
    {
        return INSMK;
    }
    public void setINSMK(String INSMK)
    {
        this.INSMK = INSMK;
        this.DATAMAP.put("INSMK", INSMK);
    }
    
    // ADD_DTTM
    public Timestamp getADD_DTTM()
    {
        return ADD_DTTM;
    }
    public void setADD_DTTM(Timestamp ADD_DTTM)
    {
        this.ADD_DTTM = ADD_DTTM;
        this.DATAMAP.put("ADD_DTTM", ADD_DTTM);
    }
    
    // ADD_USERID
    public String geADD_USERID()
    {
        return ADD_USERID;
    }
    public void setADD_USERID(String ADD_USERID)
    {
        this.ADD_USERID = ADD_USERID;
        this.DATAMAP.put("ADD_USERID", ADD_USERID);
    }
    
    // ERP_STATUS
    public String geERP_STATUS()
    {
        return ERP_STATUS;
    }
    public void setERP_STATUS(String ERP_STATUS)
    {
        this.ERP_STATUS = ERP_STATUS;
        this.DATAMAP.put("ERP_STATUS", ERP_STATUS);
    }
    
    // ERP_MESSAGE
    public String geERP_MESSAGE()
    {
        return ERP_MESSAGE;
    }
    public void setERP_MESSAGE(String ERP_MESSAGE)
    {
        this.ERP_MESSAGE = ERP_MESSAGE;
        this.DATAMAP.put("ERP_MESSAGE", ERP_MESSAGE);
    }
    
    // ERP_DTTM
    public Timestamp getERP_DTTM()
    {
        return ERP_DTTM;
    }
    public void setERP_DTTM(Timestamp ERP_DTTM)
    {
        this.ERP_DTTM = ERP_DTTM;
        this.DATAMAP.put("ERP_DTTM", ERP_DTTM);
    }
    
	
	// Key Validation
    public void checkKeyNotNull()
    {
        if (this.KEYMAP.isEmpty() || this.IFID == null || this.SEQ == null)
        {
            // [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"IFID, SEQ"});
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
