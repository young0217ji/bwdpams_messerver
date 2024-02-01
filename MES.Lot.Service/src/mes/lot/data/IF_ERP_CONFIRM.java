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
public class IF_ERP_CONFIRM
{
    // Key Info
    private Long IFID; // Interface ID
    private Long SEQ; // Sequence : 기본 실적 1 고정값, 부적합 2 고정값

    // Data Info
    private String MANDT; // Client : 100 고정값
    private String WERKS; // Plant ID
    private String AUFNR; // 작지 번호
    private String VORNR; // 공정 번호 : ERPProcessCode
    private String BUDAT; // Posting Date : 실적일자 YYYYMMDD
    private String VGWTS; // Standard Value Key : ProductOrderType
    private String AUERU; // Activate Final Confirmation : WORKORDEROPERATION.LASTOPERFLAG = 'Y' & 계획수량 적합 = 'X', WORKORDEROPERATION.LASTOPERFLAG != 'Y' & 계획수량 적합 = '1', 그외 ''
    private Double LMNGA; // Yield : 양품수량
    private String INSMK; // Stock : 부적합='S', 양품=NULL 고정값
    private Double XMNGA; // Scrap : 부적합이 아닌 Scrap 물량 확인 필요!!!
    private String MEINS; // Unit
    private Long ISM01; // 노무 Activity
    private Long ISM03; // 기계 Activity
    private String ILE01; // Activity 단위 : MIN 고정값
    private String ISDD; // 작업 시작일
    private String IEDD; // 작업 종료일
    private Timestamp ADD_DTTM;
    private String ADD_USERID;
    private String ERP_STATUS;
    private String ERP_MESSAGE;
    private Timestamp ERP_DTTM;
    
    private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();

    public IF_ERP_CONFIRM() { }

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
    
    // VORNR
    public String geVORNR()
    {
        return VORNR;
    }
    public void setVORNR(String VORNR)
    {
        this.VORNR = VORNR;
        this.DATAMAP.put("VORNR", VORNR);
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
    
    // VGWTS
    public String geVGWTS()
    {
        return VGWTS;
    }
    public void setVGWTS(String VGWTS)
    {
        this.VGWTS = VGWTS;
        this.DATAMAP.put("VGWTS", VGWTS);
    }
    
    // AUERU
    public String geAUERU()
    {
        return AUERU;
    }
    public void setAUERU(String AUERU)
    {
        this.AUERU = AUERU;
        this.DATAMAP.put("AUERU", AUERU);
    }
    
    // LMNGA
    public Double getLMNGA()
    {
        return LMNGA;
    }
    public void setLMNGA(Double LMNGA)
    {
        this.LMNGA = LMNGA;
        this.DATAMAP.put("LMNGA", LMNGA);
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
    
    // XMNGA
    public Double getXMNGA()
    {
        return XMNGA;
    }
    public void setXMNGA(Double XMNGA)
    {
        this.XMNGA = XMNGA;
        this.DATAMAP.put("XMNGA", XMNGA);
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
    
    // ISM01
	public Long getISM01() 
	{
		return ISM01;
	}

	public void setISM01(Long ISM01) {
        this.ISM01 = ISM01;
        this.DATAMAP.put("ISM01", ISM01);
	}
	
    // ISM03
	public Long getISM03() 
	{
		return ISM03;
	}

	public void setISM03(Long ISM03) {
        this.ISM03 = ISM03;
        this.DATAMAP.put("ISM03", ISM03);
	}
	
    // ILE01
    public String geILE01()
    {
        return ILE01;
    }
    public void setILE01(String ILE01)
    {
        this.ILE01 = ILE01;
        this.DATAMAP.put("ILE01", ILE01);
    }
    
    // ISDD
    public String geISDD()
    {
        return ISDD;
    }
    public void setISDD(String ISDD)
    {
        this.ISDD = ISDD;
        this.DATAMAP.put("ISDD", ISDD);
    }
    
    // IEDD
    public String geIEDD()
    {
        return IEDD;
    }
    public void setIEDD(String IEDD)
    {
        this.IEDD = IEDD;
        this.DATAMAP.put("IEDD", IEDD);
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
