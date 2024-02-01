package mes.master.data;

import java.sql.Timestamp;
import java.util.HashMap;

import com.mongodb.internal.connection.Time;

import kr.co.mesframe.exception.KeyRequiredException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.29
 * 
 * @see
 */
public class DY_PAINTINGINSPECTION {
	
	// Key Info
	private String PLANTID;
	private int DATAKEY;
	
	// Data Info
	private Timestamp INSPECTIONDATE;
	private String INSPECTIONUSERID;
	private String PRODUCTID;
	private int LOTCOUNT;
	private String EQUIPMENTID;
	private String ITEM001;
	private String ITEM002;
	private String ITEM003;
	private String ITEM004;
	private String ITEM005;
	private String ITEM006;
	private String ITEM007;
	private String ITEM008;
	private String ITEM009;
	private String ITEM010;
	private String ITEM011;
	private String ITEM012;
	private String ITEM013;
	private String ITEM014;
	private String ITEM015;
	private String ITEM016;
	private String ITEM017;
	private String ITEM018;
	private String ITEM019;
	private String ITEM020;
	private String ITEM021;
	private String ITEM022;
	private String ITEM023;
	private String ITEM024;
	private String ITEM025;
	private String ITEM026;
	private String ITEM027;
	private String ITEM028;
	private String ITEM029;
	private String ITEM030;
	private String ITEM031;
	private String ITEM032;
	private String ITEM033;
	private String ITEM034;
	private String ITEM035;
	private String ITEM036;
	private String ITEM037;
	private String ITEM038;
	private String ITEM039;
	private String ITEM040;
	private String ITEM041;
	private String ITEM042;
	private String ITEM043;
	private String ITEM044;
	private String ITEM045;
	private String ITEM046;
	private String ITEM047;
	private String ITEM048;
	private String ITEM049;
	private String ITEM050;
	private String ITEM051;
	private String ITEM052;
	private String ITEM053;
	private String ITEM054;
	private String ITEM055;
	private String ITEM056;
	private String ITEM057;
	private String ITEM058;
	private String ITEM059;
	private String ITEM060;
	private String ITEM061;
	private String ITEM062;
	private String ITEM063;
	private String ITEM064;
	private String ITEM065;
	private String ITEM066;
	private String ITEM067;
	private String ITEM068;
	private String ITEM069;
	private String ITEM070;
	private String ITEM071;
	private String ITEM072;
	private String ITEM073;
	private String ITEM074;
	private String ITEM075;
	private String ITEM076;
	private String ITEM077;
	private String ITEM078;
	private String ITEM079;
	private String ITEM080;
	private String ITEM081;
	private String ITEM082;
	private String ITEM083;
	private String ITEM084;
	private String ITEM085;
	private String ITEM086;
	private String ITEM087;
	private String ITEM088;
	private String ITEM089;
	private String ITEM090;
	private String ITEM091;
	private String ITEM092;
	private String ITEM093;
	private String ITEM094;
	private String ITEM095;
	private String ITEM096;
	private String ITEM097;
	private String ITEM098;
	private String ITEM099;
	private String ITEM100;
	private String DESCRIPTION;
	private String CREATEUSERID;
	private Timestamp CREATETIME;
	private String LASTUPDATEUSERID;
	private Timestamp LASTUPDATETIME;                      
	
	
	private HashMap<String, Object> KEYMAP = new HashMap<String, Object>();
    private HashMap<String, Object> DATAMAP = new HashMap<String, Object>();
    
    public DY_PAINTINGINSPECTION() {}
    
    // Key Methods
    // PLANTID
    public String getKeyPLANTID() {
		return PLANTID;
	}
	public void setKeyPLANTID(String PLANTID) {
		this.PLANTID = PLANTID;
		this.KEYMAP.put("PLANTID", PLANTID);
	}
	
	// DATAKEY
	public int getKeyDATAKEY() {
		return DATAKEY;
	}
	public void setKeyDATAKEY(int DATAKEY) {
		this.DATAKEY = DATAKEY;
		this.KEYMAP.put("DATAKEY", DATAKEY);
	}
	
	
	// Data Methods
	// INSPECTIONDATE
	public Timestamp getINSPECTIONDATE() {
		return INSPECTIONDATE;
	}
	public void setINSPECTIONDATE(Timestamp INSPECTIONDATE) {
		this.INSPECTIONDATE = INSPECTIONDATE;
		this.DATAMAP.put("INSPECTIONDATE", INSPECTIONDATE);
	}

	// INSPECTIONUSERID
	public String getINSPECTIONUSERID() {
		return INSPECTIONUSERID;
	}
	public void setINSPECTIONUSERID(String INSPECTIONUSERID) {
		this.INSPECTIONUSERID = INSPECTIONUSERID;
		this.DATAMAP.put("INSPECTIONUSERID", INSPECTIONUSERID);
	}

	// PRODUCTID
	public String getPRODUCTID() {
		return PRODUCTID;
	}
	public void setPRODUCTID(String PRODUCTID) {
		this.PRODUCTID = PRODUCTID;
		this.DATAMAP.put("PRODUCTID", PRODUCTID);
	}

	// LOTCOUNNT
	public int getLOTCOUNT() {
		return LOTCOUNT;
	}
	public void setLOTCOUNT(int LOTCOUNT) {
		this.LOTCOUNT = LOTCOUNT;
		this.DATAMAP.put("LOTCOUNT", LOTCOUNT);
	}

	// EQUIPMENTID
	public String getEQUIPMENTID() {
		return EQUIPMENTID;
	}
	public void setEQUIPMENTID(String EQUIPMENTID) {
		this.EQUIPMENTID = EQUIPMENTID;
		this.DATAMAP.put("EQUIPMENTID", EQUIPMENTID);
	}

	// ITEM
	public String getITEM001() {
		return ITEM001;
	}
	public void setITEM001(String ITEM001) {
		this.ITEM001 = ITEM001;
		this.DATAMAP.put("ITEM001", ITEM001);
	}

	public String getITEM002() {
		return ITEM002;
	}
	public void setITEM002(String ITEM002) {
		this.ITEM002 = ITEM002;
		this.DATAMAP.put("ITEM002", ITEM002);
	}

	public String getITEM003() {
		return ITEM003;
	}
	public void setITEM003(String ITEM003) {
		this.ITEM003 = ITEM003;
		this.DATAMAP.put("ITEM003", ITEM003);
	}

	public String getITEM004() {
		return ITEM004;
	}

	public void setITEM004(String ITEM004) {
		this.ITEM004 = ITEM004;
		this.DATAMAP.put("ITEM004", ITEM004);
	}

	public String getITEM005() {
		return ITEM005;
	}

	public void setITEM005(String ITEM005) {
		this.ITEM005 = ITEM005;
		this.DATAMAP.put("ITEM005", ITEM005);
	}

	public String getITEM006() {
		return ITEM006;
	}

	public void setITEM006(String ITEM006) {
		this.ITEM006 = ITEM006;
		this.DATAMAP.put("ITEM006", ITEM006);
	}

	public String getITEM007() {
		return ITEM007;
	}

	public void setITEM007(String ITEM007) {
		this.ITEM007 = ITEM007;
		this.DATAMAP.put("ITEM007", ITEM007);
	}

	public String getITEM008() {
		return ITEM008;
	}

	public void setITEM008(String ITEM008) {
		this.ITEM008 = ITEM008;
		this.DATAMAP.put("ITEM008", ITEM008);
	}

	public String getITEM009() {
		return ITEM009;
	}

	public void setITEM009(String ITEM009) {
		this.ITEM009 = ITEM009;
		this.DATAMAP.put("ITEM009", ITEM009);
	}

	public String getITEM010() {
		return ITEM010;
	}

	public void setITEM010(String ITEM010) {
		this.ITEM010 = ITEM010;
		this.DATAMAP.put("ITEM010", ITEM010);
	}

	public String getITEM011() {
		return ITEM011;
	}

	public void setITEM011(String ITEM011) {
		this.ITEM011 = ITEM011;
		this.DATAMAP.put("ITEM011", ITEM011);
	}

	public String getITEM012() {
		return ITEM012;
	}

	public void setITEM012(String ITEM012) {
		this.ITEM012 = ITEM012;
		this.DATAMAP.put("ITEM012", ITEM012);
	}

	public String getITEM013() {
		return ITEM013;
	}

	public void setITEM013(String ITEM013) {
		this.ITEM013 = ITEM013;
		this.DATAMAP.put("ITEM013", ITEM013);
	}

	public String getITEM014() {
		return ITEM014;
	}

	public void setITEM014(String ITEM014) {
		this.ITEM014 = ITEM014;
		this.DATAMAP.put("ITEM014", ITEM014);
	}

	public String getITEM015() {
		return ITEM015;
	}

	public void setITEM015(String ITEM015) {
		this.ITEM015 = ITEM015;
		this.DATAMAP.put("ITEM015", ITEM015);
	}

	public String getITEM016() {
		return ITEM016;
	}

	public void setITEM016(String ITEM016) {
		this.ITEM016 = ITEM016;
		this.DATAMAP.put("ITEM016", ITEM016);
	}

	public String getITEM017() {
		return ITEM017;
	}

	public void setITEM017(String ITEM017) {
		this.ITEM017 = ITEM017;
		this.DATAMAP.put("ITEM017", ITEM017);
	}

	public String getITEM018() {
		return ITEM018;
	}

	public void setITEM018(String ITEM018) {
		this.ITEM018 = ITEM018;
		this.DATAMAP.put("ITEM018", ITEM018);
	}

	public String getITEM019() {
		return ITEM019;
	}

	public void setITEM019(String ITEM019) {
		this.ITEM019 = ITEM019;
		this.DATAMAP.put("ITEM019", ITEM019);
	}

	public String getITEM020() {
		return ITEM020;
	}

	public void setITEM020(String ITEM020) {
		this.ITEM020 = ITEM020;
		this.DATAMAP.put("ITEM020", ITEM020);
	}

	public String getITEM021() {
		return ITEM021;
	}

	public void setITEM021(String ITEM021) {
		this.ITEM021 = ITEM021;
		this.DATAMAP.put("ITEM021", ITEM021);
	}

	public String getITEM022() {
		return ITEM022;
	}

	public void setITEM022(String ITEM022) {
		this.ITEM022 = ITEM022;
		this.DATAMAP.put("ITEM022", ITEM022);
	}

	public String getITEM023() {
		return ITEM023;
	}

	public void setITEM023(String ITEM023) {
		this.ITEM023 = ITEM023;
		this.DATAMAP.put("ITEM023", ITEM023);
	}

	public String getITEM024() {
		return ITEM024;
	}

	public void setITEM024(String ITEM024) {
		this.ITEM024 = ITEM024;
		this.DATAMAP.put("ITEM024", ITEM024);
	}

	public String getITEM025() {
		return ITEM025;
	}

	public void setITEM025(String ITEM025) {
		this.ITEM025 = ITEM025;
		this.DATAMAP.put("ITEM025", ITEM025);
	}

	public String getITEM026() {
		return ITEM026;
	}

	public void setITEM026(String ITEM026) {
		this.ITEM026 = ITEM026;
		this.DATAMAP.put("ITEM026", ITEM026);
	}

	public String getITEM027() {
		return ITEM027;
	}

	public void setITEM027(String ITEM027) {
		this.ITEM027 = ITEM027;
		this.DATAMAP.put("ITEM027", ITEM027);
	}

	public String getITEM028() {
		return ITEM028;
	}

	public void setITEM028(String ITEM028) {
		this.ITEM028 = ITEM028;
		this.DATAMAP.put("ITEM028", ITEM028);
	}

	public String getITEM029() {
		return ITEM029;
	}

	public void setITEM029(String ITEM029) {
		this.ITEM029 = ITEM029;
		this.DATAMAP.put("ITEM029", ITEM029);
	}

	public String getITEM030() {
		return ITEM030;
	}

	public void setITEM030(String ITEM030) {
		this.ITEM030 = ITEM030;
		this.DATAMAP.put("ITEM030", ITEM030);
	}

	public String getITEM031() {
		return ITEM031;
	}

	public void setITEM031(String ITEM031) {
		this.ITEM031 = ITEM031;
		this.DATAMAP.put("ITEM031", ITEM031);
	}

	public String getITEM032() {
		return ITEM032;
	}

	public void setITEM032(String ITEM032) {
		this.ITEM032 = ITEM032;
		this.DATAMAP.put("ITEM032", ITEM032);
	}

	public String getITEM033() {
		return ITEM033;
	}

	public void setITEM033(String ITEM033) {
		this.ITEM033 = ITEM033;
		this.DATAMAP.put("ITEM033", ITEM033);
	}

	public String getITEM034() {
		return ITEM034;
	}

	public void setITEM034(String ITEM034) {
		this.ITEM034 = ITEM034;
		this.DATAMAP.put("ITEM034", ITEM034);
	}

	public String getITEM035() {
		return ITEM035;
	}

	public void setITEM035(String ITEM035) {
		this.ITEM035 = ITEM035;
		this.DATAMAP.put("ITEM035", ITEM035);
	}

	public String getITEM036() {
		return ITEM036;
	}

	public void setITEM036(String ITEM036) {
		this.ITEM036 = ITEM036;
		this.DATAMAP.put("ITEM036", ITEM036);
	}

	public String getITEM037() {
		return ITEM037;
	}

	public void setITEM037(String ITEM037) {
		this.ITEM037 = ITEM037;
		this.DATAMAP.put("ITEM037", ITEM037);
	}

	public String getITEM038() {
		return ITEM038;
	}

	public void setITEM038(String ITEM038) {
		this.ITEM038 = ITEM038;
		this.DATAMAP.put("ITEM038", ITEM038);
	}

	public String getITEM039() {
		return ITEM039;
	}

	public void setITEM039(String ITEM039) {
		this.ITEM039 = ITEM039;
		this.DATAMAP.put("ITEM039", ITEM039);
	}

	public String getITEM040() {
		return ITEM040;
	}

	public void setITEM040(String ITEM040) {
		this.ITEM040 = ITEM040;
		this.DATAMAP.put("ITEM040", ITEM040);
	}

	public String getITEM041() {
		return ITEM041;
	}

	public void setITEM041(String ITEM041) {
		this.ITEM041 = ITEM041;
		this.DATAMAP.put("ITEM041", ITEM041);
	}

	public String getITEM042() {
		return ITEM042;
	}

	public void setITEM042(String ITEM042) {
		this.ITEM042 = ITEM042;
		this.DATAMAP.put("ITEM042", ITEM042);
	}

	public String getITEM043() {
		return ITEM043;
	}

	public void setITEM043(String ITEM043) {
		this.ITEM043 = ITEM043;
		this.DATAMAP.put("ITEM043", ITEM043);
	}

	public String getITEM044() {
		return ITEM044;
	}

	public void setITEM044(String ITEM044) {
		this.ITEM044 = ITEM044;
		this.DATAMAP.put("ITEM044", ITEM044);
	}

	public String getITEM045() {
		return ITEM045;
	}

	public void setITEM045(String ITEM045) {
		this.ITEM045 = ITEM045;
		this.DATAMAP.put("ITEM045", ITEM045);
	}

	public String getITEM046() {
		return ITEM046;
	}

	public void setITEM046(String ITEM046) {
		this.ITEM046 = ITEM046;
		this.DATAMAP.put("ITEM046", ITEM046);
	}

	public String getITEM047() {
		return ITEM047;
	}

	public void setITEM047(String ITEM047) {
		this.ITEM047 = ITEM047;
		this.DATAMAP.put("ITEM047", ITEM047);
	}

	public String getITEM048() {
		return ITEM048;
	}

	public void setITEM048(String ITEM048) {
		this.ITEM048 = ITEM048;
		this.DATAMAP.put("ITEM048", ITEM048);
	}

	public String getITEM049() {
		return ITEM049;
	}

	public void setITEM049(String ITEM049) {
		this.ITEM049 = ITEM049;
		this.DATAMAP.put("ITEM049", ITEM049);
	}

	public String getITEM050() {
		return ITEM050;
	}

	public void setITEM050(String ITEM050) {
		this.ITEM050 = ITEM050;
		this.DATAMAP.put("ITEM050", ITEM050);
	}

	public String getITEM051() {
		return ITEM051;
	}

	public void setITEM051(String ITEM051) {
		this.ITEM051 = ITEM051;
		this.DATAMAP.put("ITEM051", ITEM051);
	}

	public String getITEM052() {
		return ITEM052;
	}

	public void setITEM052(String ITEM052) {
		this.ITEM052 = ITEM052;
		this.DATAMAP.put("ITEM052", ITEM052);
	}

	public String getITEM053() {
		return ITEM053;
	}

	public void setITEM053(String ITEM053) {
		this.ITEM053 = ITEM053;
		this.DATAMAP.put("ITEM053", ITEM053);
	}

	public String getITEM054() {
		return ITEM054;
	}

	public void setITEM054(String ITEM054) {
		this.ITEM054 = ITEM054;
		this.DATAMAP.put("ITEM054", ITEM054);
	}

	public String getITEM055() {
		return ITEM055;
	}

	public void setITEM055(String ITEM055) {
		this.ITEM055 = ITEM055;
		this.DATAMAP.put("ITEM055", ITEM055);
	}

	public String getITEM056() {
		return ITEM056;
	}

	public void setITEM056(String ITEM056) {
		this.ITEM056 = ITEM056;
		this.DATAMAP.put("ITEM056", ITEM056);
	}

	public String getITEM057() {
		return ITEM057;
	}

	public void setITEM057(String ITEM057) {
		this.ITEM057 = ITEM057;
		this.DATAMAP.put("ITEM057", ITEM057);
	}

	public String getITEM058() {
		return ITEM058;
	}

	public void setITEM058(String ITEM058) {
		this.ITEM058 = ITEM058;
		this.DATAMAP.put("ITEM058", ITEM058);
	}

	public String getITEM059() {
		return ITEM059;
	}

	public void setITEM059(String ITEM059) {
		this.ITEM059 = ITEM059;
		this.DATAMAP.put("ITEM059", ITEM059);
	}

	public String getITEM060() {
		return ITEM060;
	}

	public void setITEM060(String ITEM060) {
		this.ITEM060 = ITEM060;
		this.DATAMAP.put("ITEM060", ITEM060);
	}

	public String getITEM061() {
		return ITEM061;
	}

	public void setITEM061(String ITEM061) {
		this.ITEM061 = ITEM061;
		this.DATAMAP.put("ITEM061", ITEM061);
	}

	public String getITEM062() {
		return ITEM062;
	}

	public void setITEM062(String ITEM062) {
		this.ITEM062 = ITEM062;
		this.DATAMAP.put("ITEM062", ITEM062);
	}

	public String getITEM063() {
		return ITEM063;
	}

	public void setITEM063(String ITEM063) {
		this.ITEM063 = ITEM063;
		this.DATAMAP.put("ITEM063", ITEM063);
	}

	
	public String getITEM064() {
		return ITEM064;
	}

	public void setITEM064(String ITEM064) {
		this.ITEM064 = ITEM064;
		this.DATAMAP.put("ITEM064", ITEM064);
	}

	public String getITEM065() {
		return ITEM065;
	}

	public void setITEM065(String ITEM065) {
		this.ITEM065 = ITEM065;
		this.DATAMAP.put("ITEM065", ITEM065);
	}

	public String getITEM066() {
		return ITEM066;
	}

	public void setITEM066(String ITEM066) {
		this.ITEM066 = ITEM066;
		this.DATAMAP.put("ITEM066", ITEM066);
	}

	public String getITEM067() {
		return ITEM067;
	}

	public void setITEM067(String ITEM067) {
		this.ITEM067 = ITEM067;
		this.DATAMAP.put("ITEM067", ITEM067);
	}

	public String getITEM068() {
		return ITEM068;
	}

	public void setITEM068(String ITEM068) {
		this.ITEM068 = ITEM068;
		this.DATAMAP.put("ITEM068", ITEM068);
	}

	public String getITEM069() {
		return ITEM069;
	}

	public void setITEM069(String ITEM069) {
		this.ITEM069 = ITEM069;
		this.DATAMAP.put("ITEM069", ITEM069);
	}

	public String getITEM070() {
		return ITEM070;
	}

	public void setITEM070(String ITEM070) {
		this.ITEM070 = ITEM070;
		this.DATAMAP.put("ITEM070", ITEM070);
	}

	public String getITEM071() {
		return ITEM071;
	}

	public void setITEM071(String ITEM071) {
		this.ITEM071 = ITEM071;
		this.DATAMAP.put("ITEM071", ITEM071);
	}

	public String getITEM072() {
		return ITEM072;
	}

	public void setITEM072(String ITEM072) {
		this.ITEM072 = ITEM072;
		this.DATAMAP.put("ITEM072", ITEM072);
	}

	public String getITEM073() {
		return ITEM073;
	}

	public void setITEM073(String ITEM073) {
		this.ITEM073 = ITEM073;
		this.DATAMAP.put("ITEM073", ITEM073);
	}

	public String getITEM074() {
		return ITEM074;
	}

	public void setITEM074(String ITEM074) {
		this.ITEM074 = ITEM074;
		this.DATAMAP.put("ITEM074", ITEM074);
	}

	public String getITEM075() {
		return ITEM075;
	}

	public void setITEM075(String ITEM075) {
		this.ITEM075 = ITEM075;
		this.DATAMAP.put("ITEM075", ITEM075);
	}

	public String getITEM076() {
		return ITEM076;
	}

	public void setITEM076(String ITEM076) {
		this.ITEM076 = ITEM076;
		this.DATAMAP.put("ITEM076", ITEM076);
	}

	public String getITEM077() {
		return ITEM077;
	}

	public void setITEM077(String ITEM077) {
		this.ITEM077 = ITEM077;
		this.DATAMAP.put("ITEM077", ITEM077);
	}

	public String getITEM078() {
		return ITEM078;
	}

	public void setITEM078(String ITEM078) {
		this.ITEM078 = ITEM078;
		this.DATAMAP.put("ITEM078", ITEM078);
	}

	public String getITEM079() {
		return ITEM079;
	}

	public void setITEM079(String ITEM079) {
		this.ITEM079 = ITEM079;
		this.DATAMAP.put("ITEM079", ITEM079);
	}

	public String getITEM080() {
		return ITEM080;
	}

	public void setITEM080(String ITEM080) {
		this.ITEM080 = ITEM080;
		this.DATAMAP.put("ITEM080", ITEM080);
	}

	public String getITEM081() {
		return ITEM081;
	}

	public void setITEM081(String ITEM081) {
		this.ITEM081 = ITEM081;
		this.DATAMAP.put("ITEM081", ITEM081);
	}

	public String getITEM082() {
		return ITEM082;
	}

	public void setITEM082(String ITEM082) {
		this.ITEM082 = ITEM082;
		this.DATAMAP.put("ITEM082", ITEM082);
	}

	public String getITEM083() {
		return ITEM083;
	}

	public void setITEM083(String ITEM083) {
		this.ITEM083 = ITEM083;
		this.DATAMAP.put("ITEM083", ITEM083);
	}

	public String getITEM084() {
		return ITEM084;
	}

	public void setITEM084(String ITEM084) {
		this.ITEM084 = ITEM084;
		this.DATAMAP.put("ITEM084", ITEM084);
	}

	public String getITEM085() {
		return ITEM085;
	}

	public void setITEM085(String ITEM085) {
		this.ITEM085 = ITEM085;
		this.DATAMAP.put("ITEM085", ITEM085);
	}

	public String getITEM086() {
		return ITEM086;
	}

	public void setITEM086(String ITEM086) {
		this.ITEM086 = ITEM086;
		this.DATAMAP.put("ITEM086", ITEM086);
	}

	public String getITEM087() {
		return ITEM087;
	}

	public void setITEM087(String ITEM087) {
		this.ITEM087 = ITEM087;
		this.DATAMAP.put("ITEM087", ITEM087);
	}

	public String getITEM088() {
		return ITEM088;
	}

	public void setITEM088(String ITEM088) {
		this.ITEM088 = ITEM088;
		this.DATAMAP.put("ITEM088", ITEM088);
	}

	public String getITEM089() {
		return ITEM089;
	}

	public void setITEM089(String ITEM089) {
		this.ITEM089 = ITEM089;
		this.DATAMAP.put("ITEM089", ITEM089);
	}

	public String getITEM090() {
		return ITEM090;
	}

	public void setITEM090(String ITEM090) {
		this.ITEM090 = ITEM090;
		this.DATAMAP.put("ITEM090", ITEM090);
	}

	public String getITEM091() {
		return ITEM091;
	}

	public void setITEM091(String ITEM091) {
		this.ITEM091 = ITEM091;
		this.DATAMAP.put("ITEM091", ITEM091);
	}

	public String getITEM092() {
		return ITEM092;
	}

	public void setITEM092(String ITEM092) {
		this.ITEM092 = ITEM092;
		this.DATAMAP.put("ITEM092", ITEM092);
	}

	public String getITEM093() {
		return ITEM093;
	}

	public void setITEM093(String ITEM093) {
		this.ITEM093 = ITEM093;
		this.DATAMAP.put("ITEM093", ITEM093);
	}

	public String getITEM094() {
		return ITEM094;
	}

	public void setITEM094(String ITEM094) {
		this.ITEM094 = ITEM094;
		this.DATAMAP.put("ITEM094", ITEM094);
	}

	public String getITEM095() {
		return ITEM095;
	}

	public void setITEM095(String ITEM095) {
		this.ITEM095 = ITEM095;
		this.DATAMAP.put("ITEM095", ITEM095);
	}

	public String getITEM096() {
		return ITEM096;
	}

	public void setITEM096(String ITEM096) {
		this.ITEM096 = ITEM096;
		this.DATAMAP.put("ITEM096", ITEM096);
	}

	public String getITEM097() {
		return ITEM097;
	}

	public void setITEM097(String ITEM097) {
		this.ITEM097 = ITEM097;
		this.DATAMAP.put("ITEM097", ITEM097);
	}

	public String getITEM098() {
		return ITEM098;
	}

	public void setITEM098(String ITEM098) {
		this.ITEM098 = ITEM098;
		this.DATAMAP.put("ITEM098", ITEM098);
	}

	public String getITEM099() {
		return ITEM099;
	}

	public void setITEM099(String ITEM099) {
		this.ITEM099 = ITEM099;
		this.DATAMAP.put("ITEM099", ITEM099);
	}

	public String getITEM100() {
		return ITEM100;
	}

	public void setITEM100(String ITEM100) {
		this.ITEM100 = ITEM100;
		this.DATAMAP.put("ITEM100", ITEM100);
	}
		
	//DESCRIPTION
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String DESCRIPTION) {
		this.DESCRIPTION = DESCRIPTION;
		this.DATAMAP.put("DESCRIPTION", DESCRIPTION);
	}
	
	//CREATEUSERID
	public String getCREATEUSERID() {
		return CREATEUSERID;
	}
	public void setCREATEUSERID(String CREATEUSERID) {
		this.CREATEUSERID = CREATEUSERID;
		this.DATAMAP.put("CREATEUSERID", CREATEUSERID);
	}
	
	//CREATETIME
	public Timestamp getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(Timestamp CREATETIME) {
		this.CREATETIME = CREATETIME;
		this.DATAMAP.put("CREATETIME", CREATETIME);
	}
	
	//LASTUPDATEUSERID
	public String getLASTUPDATEUSERID() {
		return LASTUPDATEUSERID;
	}
	public void setLASTUPDATEUSERID(String LASTUPDATEUSERID) {
		this.LASTUPDATEUSERID = LASTUPDATEUSERID;
		this.DATAMAP.put("LASTUPDATEUSERID", LASTUPDATEUSERID);
	}
	
	//LASTUPDATETIME
	public Timestamp getLASTUPDATETIME() {
		return LASTUPDATETIME;
	}
	public void setLASTUPDATETIME(Timestamp LASTUPDATETIME) {
		this.LASTUPDATETIME = LASTUPDATETIME;
		this.DATAMAP.put("LASTUPDATETIME", LASTUPDATETIME);
	}
	
	// Key Validation
    public void checkKeyNotNull() 
    {
        if ( this.KEYMAP.isEmpty() || this.PLANTID == null)
        {
        	// [{1}] 다음의 필수 Key 값이 누락되었습니다.
            throw new KeyRequiredException(new Object [] {"PLANTID"});
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
