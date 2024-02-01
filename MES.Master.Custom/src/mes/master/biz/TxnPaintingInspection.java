package mes.master.biz;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_PAINTINGINSPECTION;
import mes.master.data.DY_PAINTINGINSPECTIONDETAIL;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.30
 * 
 * @see
 */

public class TxnPaintingInspection implements ObjectExecuteService {

	/**
	 * 도장 완성검사 성적서를 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++) {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
            
            
            if("INSPECTION".equals(dataMap.get("TYPE"))) {
            	
            	DY_PAINTINGINSPECTION dataInfo = new DY_PAINTINGINSPECTION();
                
                // Key Value Setting
                dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
                dataInfo.setKeyDATAKEY(Integer.parseInt(dataMap.get("DATAKEY")));
                
                
                // Key에 대한 DataInfo 조회
                if(!dataMap.get("_ROWSTATUS").equals("C")) {
                	
                	dataInfo = (DY_PAINTINGINSPECTION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
                	
                }
                
                dataInfo.setINSPECTIONDATE(new Timestamp(Long.parseLong(dataMap.get("INSPECTIONDATE"))));
                dataInfo.setINSPECTIONUSERID(dataMap.get("INSPECTIONUSERID"));
                dataInfo.setPRODUCTID(dataMap.get("PRODUCTID"));
                dataInfo.setLOTCOUNT(Integer.parseInt(dataMap.get("LOTCOUNT")));
                dataInfo.setEQUIPMENTID(dataMap.get("EQUIPMENTID"));
                dataInfo.setITEM001(dataMap.get("ITEM001"));
                dataInfo.setITEM002(dataMap.get("ITEM002"));
                dataInfo.setITEM003(dataMap.get("ITEM003"));
                dataInfo.setITEM004(dataMap.get("ITEM004"));
                dataInfo.setITEM005(dataMap.get("ITEM005"));
                dataInfo.setITEM006(dataMap.get("ITEM006"));
                dataInfo.setITEM007(dataMap.get("ITEM007"));
                dataInfo.setITEM008(dataMap.get("ITEM008"));
                dataInfo.setITEM009(dataMap.get("ITEM009"));
                dataInfo.setITEM010(dataMap.get("ITEM010"));
                dataInfo.setITEM011(dataMap.get("ITEM011"));
                dataInfo.setITEM012(dataMap.get("ITEM012"));
                dataInfo.setITEM013(dataMap.get("ITEM013"));
                dataInfo.setITEM014(dataMap.get("ITEM014"));
                dataInfo.setITEM015(dataMap.get("ITEM015"));
                dataInfo.setITEM016(dataMap.get("ITEM016"));
                dataInfo.setITEM017(dataMap.get("ITEM017"));
                dataInfo.setITEM018(dataMap.get("ITEM018"));
                dataInfo.setITEM019(dataMap.get("ITEM019"));
                dataInfo.setITEM020(dataMap.get("ITEM020"));
                dataInfo.setITEM021(dataMap.get("ITEM021"));
                dataInfo.setITEM022(dataMap.get("ITEM022"));
                dataInfo.setITEM023(dataMap.get("ITEM023"));
                dataInfo.setITEM024(dataMap.get("ITEM024"));
                dataInfo.setITEM025(dataMap.get("ITEM025"));
                dataInfo.setITEM026(dataMap.get("ITEM026"));
                dataInfo.setITEM027(dataMap.get("ITEM027"));
                dataInfo.setITEM028(dataMap.get("ITEM028"));
                dataInfo.setITEM029(dataMap.get("ITEM029"));
                dataInfo.setITEM030(dataMap.get("ITEM030"));
                dataInfo.setITEM031(dataMap.get("ITEM031"));
                dataInfo.setITEM032(dataMap.get("ITEM032"));
                dataInfo.setITEM033(dataMap.get("ITEM033"));
                dataInfo.setITEM034(dataMap.get("ITEM034"));
                dataInfo.setITEM035(dataMap.get("ITEM035"));
                dataInfo.setITEM036(dataMap.get("ITEM036"));
                dataInfo.setITEM037(dataMap.get("ITEM037"));
                dataInfo.setITEM038(dataMap.get("ITEM038"));
                dataInfo.setITEM039(dataMap.get("ITEM039"));
                dataInfo.setITEM040(dataMap.get("ITEM040"));
                dataInfo.setITEM041(dataMap.get("ITEM041"));
                dataInfo.setITEM042(dataMap.get("ITEM042"));
                dataInfo.setITEM043(dataMap.get("ITEM043"));
                dataInfo.setITEM044(dataMap.get("ITEM044"));
                dataInfo.setITEM045(dataMap.get("ITEM045"));
                dataInfo.setITEM046(dataMap.get("ITEM046"));
                dataInfo.setITEM047(dataMap.get("ITEM047"));
                dataInfo.setITEM048(dataMap.get("ITEM048"));
                dataInfo.setITEM049(dataMap.get("ITEM049"));
                dataInfo.setITEM050(dataMap.get("ITEM050"));
                dataInfo.setITEM051(dataMap.get("ITEM051"));
                dataInfo.setITEM052(dataMap.get("ITEM052"));
                dataInfo.setITEM053(dataMap.get("ITEM053"));
                dataInfo.setITEM054(dataMap.get("ITEM054"));
                dataInfo.setITEM055(dataMap.get("ITEM055"));
                dataInfo.setITEM056(dataMap.get("ITEM056"));
                dataInfo.setITEM057(dataMap.get("ITEM057"));
                dataInfo.setITEM058(dataMap.get("ITEM058"));
                dataInfo.setITEM059(dataMap.get("ITEM059"));
                dataInfo.setITEM060(dataMap.get("ITEM060"));
                dataInfo.setITEM061(dataMap.get("ITEM061"));
                dataInfo.setITEM062(dataMap.get("ITEM062"));
                dataInfo.setITEM063(dataMap.get("ITEM063"));
                dataInfo.setITEM064(dataMap.get("ITEM064"));
                dataInfo.setITEM065(dataMap.get("ITEM065"));
                dataInfo.setITEM066(dataMap.get("ITEM066"));
                dataInfo.setITEM067(dataMap.get("ITEM067"));
                dataInfo.setITEM068(dataMap.get("ITEM068"));
                dataInfo.setITEM069(dataMap.get("ITEM069"));
                dataInfo.setITEM070(dataMap.get("ITEM070"));
                dataInfo.setITEM071(dataMap.get("ITEM071"));
                dataInfo.setITEM072(dataMap.get("ITEM072"));
                dataInfo.setITEM073(dataMap.get("ITEM073"));
                dataInfo.setITEM074(dataMap.get("ITEM074"));
                dataInfo.setITEM075(dataMap.get("ITEM075"));
                dataInfo.setITEM076(dataMap.get("ITEM076"));
                dataInfo.setITEM077(dataMap.get("ITEM077"));
                dataInfo.setITEM078(dataMap.get("ITEM078"));
                dataInfo.setITEM079(dataMap.get("ITEM079"));
                dataInfo.setITEM080(dataMap.get("ITEM080"));
                dataInfo.setITEM081(dataMap.get("ITEM081"));
                dataInfo.setITEM082(dataMap.get("ITEM082"));
                dataInfo.setITEM083(dataMap.get("ITEM083"));
                dataInfo.setITEM084(dataMap.get("ITEM084"));
                dataInfo.setITEM085(dataMap.get("ITEM085"));
                dataInfo.setITEM086(dataMap.get("ITEM086"));
                dataInfo.setITEM087(dataMap.get("ITEM087"));
                dataInfo.setITEM088(dataMap.get("ITEM088"));
                dataInfo.setITEM089(dataMap.get("ITEM089"));
                dataInfo.setITEM090(dataMap.get("ITEM090"));
                dataInfo.setITEM091(dataMap.get("ITEM091"));
                dataInfo.setITEM092(dataMap.get("ITEM092"));
                dataInfo.setITEM093(dataMap.get("ITEM093"));
                dataInfo.setITEM094(dataMap.get("ITEM094"));
                dataInfo.setITEM095(dataMap.get("ITEM095"));
                dataInfo.setITEM096(dataMap.get("ITEM096"));
                dataInfo.setITEM097(dataMap.get("ITEM097"));
                dataInfo.setITEM098(dataMap.get("ITEM098"));
                dataInfo.setITEM099(dataMap.get("ITEM099"));
                dataInfo.setITEM100(dataMap.get("ITEM100"));
                dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
                dataInfo.setCREATETIME(txnInfo.getEventTime());
                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                
                
                // DataInfo에  대한 CUD 실행
                if(dataMap.get("_ROWSTATUS").equals("D")) {
                	dataInfo.excuteDML(SqlConstant.DML_DELETE);
                }else if(dataMap.get("_ROWSTATUS").equals("C")) {
                	dataInfo.excuteDML(SqlConstant.DML_INSERT);
                }else if(dataMap.get("_ROWSTATUS").equals("U")) {
                	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
                }
                
            }else {
            	
            	DY_PAINTINGINSPECTIONDETAIL dataInfo2 = new DY_PAINTINGINSPECTIONDETAIL();
            	
            	// Key Value Setting
                dataInfo2.setKeyPLANTID(dataMap.get("PLANTID"));
                dataInfo2.setKeyDATAKEY(Integer.parseInt(dataMap.get("DATAKEY")));
                dataInfo2.setKeyDATACOUNT(Integer.parseInt(dataMap.get("DATACOUNT")));
                
                
                // Key에 대한 DataInfo 조회
                if(!dataMap.get("_ROWSTATUS").equals("C")) {
                	
                	dataInfo2 = (DY_PAINTINGINSPECTIONDETAIL) dataInfo2.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
                	
                }
                
                dataInfo2.setDATATYPE(dataMap.get("DATATYPE"));
                dataInfo2.setCHANGEDATE(new Timestamp(Long.parseLong(dataMap.get("CHANGEDATE"))));
                dataInfo2.setCHANGECOMMENT(dataMap.get("CHANGECOMMENT"));
                dataInfo2.setCONFIRMUSERID(dataMap.get("CONFIRMUSERID"));
                dataInfo2.setDESCRIPTION(dataMap.get("DESCRIPTION"));
                dataInfo2.setCREATETIME(txnInfo.getEventTime());
                dataInfo2.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo2.setLASTUPDATETIME(txnInfo.getEventTime());
                dataInfo2.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                
                
                // DataInfo에  대한 CUD 실행
                if(dataMap.get("_ROWSTATUS").equals("D")) {
                	dataInfo2.excuteDML(SqlConstant.DML_DELETE);
                }else if(dataMap.get("_ROWSTATUS").equals("C")) {
                	dataInfo2.excuteDML(SqlConstant.DML_INSERT);
                }else if(dataMap.get("_ROWSTATUS").equals("U")) {
                	dataInfo2.excuteDML(SqlConstant.DML_UPDATE);
                }
                
            }
             
		}

			return recvDoc;
	}

}
