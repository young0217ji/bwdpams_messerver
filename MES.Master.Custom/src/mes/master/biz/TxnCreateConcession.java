package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_CONCESSION;
import mes.master.data.DY_CONCESSIONFILE;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.09
 * 
 * @see
 */

public class TxnCreateConcession implements ObjectExecuteService {

	/**
	 * 특채 신청서를 등록하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        NameGenerator nameGenerator = new NameGenerator();
        
        String CONCESSIONID = "";
        
        for (HashMap<String, String> hashMap : masterData) {
			if("CONCESSION".equals(hashMap.get("TYPE")) && "C".equals(hashMap.get("_ROWSTATUS"))) {
				CONCESSIONID = nameGenerator.nameGenerate(hashMap.get("PLANTID"), "ConcessionID", new Object[] {});
			}
		}
        if(CONCESSIONID.equals("")) {
        	CONCESSIONID = masterData.get(0).get("CONCESSIONID");
        }
        
        
        for (int i = 0 ; i < masterData.size(); i ++) {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

      
            if("CONCESSION".equals(dataMap.get("TYPE"))) {
            	// CONCESSION INSERT
            	DY_CONCESSION dataInfo = new DY_CONCESSION();
            	
            	// Key Value Setting
                dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
                dataInfo.setKeyCONCESSIONID(CONCESSIONID);                	
                
                // Key에 대한 DataInfo 조회
                if(!dataMap.get("_ROWSTATUS").equals("C")) {
                	dataInfo = (DY_CONCESSION)  dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
                }
                
                // Data
                dataInfo.setCONCESSIONSTATE(dataMap.get("CONCESSIONSTATE"));
                dataInfo.setRECEIVERUSERID(dataMap.get("RECEIVERUSERID"));
                dataInfo.setREQUESTDEPARTMENT(dataMap.get("REQUESTDEPARTMENT"));
                dataInfo.setREQUESTUSERID(dataMap.get("REQUESTUSERID"));
                dataInfo.setREQUESTITEM(dataMap.get("REQUESTITEM"));        
                dataInfo.setWORKCENTER(dataMap.get("WORKCENTER"));     
                dataInfo.setREQUESTQUANTITY(ConvertUtil.String2Double(dataMap.get("REQUESTQUANTITY")));          
                dataInfo.setREQUESTCOMPANY(dataMap.get("REQUESTCOMPANY"));      
                dataInfo.setREQUESTCOST(ConvertUtil.String2Double(dataMap.get("REQUESTCOST")));         
                dataInfo.setDEFECTREASON(dataMap.get("DEFECTREASON"));        
                dataInfo.setCONCESSIONREASON(dataMap.get("CONCESSIONREASON"));    
                dataInfo.setIMPROVEMENTPLAN(dataMap.get("IMPROVEMENTPLAN"));     
                dataInfo.setCONCESSIONGRADE(dataMap.get("CONCESSIONGRADE"));     
                dataInfo.setVERIFICATIONRESULT(dataMap.get("VERIFICATIONRESULT"));           
                dataInfo.setPRODUCTDEVELOPMENTUSERID(dataMap.get("PRODUCTDEVELOPMENTUSERID"));     
                dataInfo.setPRODUCTDEVELOPMENTREASON(dataMap.get("PRODUCTDEVELOPMENTREASON"));     
                dataInfo.setPRODUCTDEVELOPMENTRESULT(dataMap.get("PRODUCTDEVELOPMENTRESULT"));  
                dataInfo.setPRODUCTIONENGINEERINGUSERID(dataMap.get("PRODUCTIONENGINEERINGUSERID"));  
                dataInfo.setPRODUCTIONENGINEERINGREASON(dataMap.get("PRODUCTIONENGINEERINGREASON"));  
                dataInfo.setPRODUCTIONENGINEERINGRESULT(dataMap.get("PRODUCTIONENGINEERINGRESULT"));  
                dataInfo.setPRODUCTIONUSERID(dataMap.get("PRODUCTIONUSERID"));             
                dataInfo.setPRODUCTIONREASON(dataMap.get("PRODUCTIONREASON"));             
                dataInfo.setPRODUCTIONRESULT(dataMap.get("PRODUCTIONRESULT"));  
                dataInfo.setQUALITYUSERID(dataMap.get("QUALITYUSERID"));               
                dataInfo.setQUALITYREASON(dataMap.get("QUALITYREASON"));                
                dataInfo.setQUALITYRESULT(dataMap.get("QUALITYRESULT"));                
                dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
                dataInfo.setCREATETIME(txnInfo.getEventTime());
                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            	
                // 제품개발 / 생산기술 / 생산 / 품질
                
                // APPROVALTYPE = PD / PE / P / Q 
                
                dataInfo.setPRODUCTDEVELOPMENTDATE("PD".equals(dataMap.get("APPROVALTYPE"))?txnInfo.getEventTime():dataInfo.getPRODUCTDEVELOPMENTDATE());
                dataInfo.setPRODUCTIONENGINEERINGDATE("PE".equals(dataMap.get("APPROVALTYPE"))?txnInfo.getEventTime():dataInfo.getPRODUCTIONENGINEERINGDATE());
                dataInfo.setPRODUCTIONDATE("P".equals(dataMap.get("APPROVALTYPE"))?txnInfo.getEventTime():dataInfo.getPRODUCTIONDATE());
                dataInfo.setCONCESSIONJUDGMENTDATE("Q".equals(dataMap.get("APPROVALTYPE"))?txnInfo.getEventTime():dataInfo.getCONCESSIONJUDGMENTDATE());
             
                
                // DataInfo에 대한 CUD 실행
                if(dataMap.get("_ROWSTATUS").equals("D")) {
                	dataInfo.excuteDML(SqlConstant.DML_DELETE);
                }else if(dataMap.get("_ROWSTATUS").equals("C")) {
                	dataInfo.excuteDML(SqlConstant.DML_INSERT);
                }else if(dataMap.get("_ROWSTATUS").equals("U")) {
                	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
                };
            	
            }else {
            	// FILE INSERT
            	DY_CONCESSIONFILE dataInfo2 = new DY_CONCESSIONFILE();
      	
            	
            	//파일 Key Value Setting
                dataInfo2.setKeyPLANTID(dataMap.get("PLANTID"));
                dataInfo2.setKeyCONCESSIONID(CONCESSIONID);   
                
                dataInfo2.setKeyFILETYPE(dataMap.get("FILETYPE"));
            	
                List<Object> orgFile = (List<Object>) dataInfo2.excuteDML(SqlConstant.DML_SELECTLIST);
                
                // 파일 Data
                dataInfo2.setFILENAME(dataMap.get("FILENAME"));
                dataInfo2.setFILELOCATION(dataMap.get("FILELOCATION"));
                dataInfo2.setDESCRIPTION(dataMap.get("DESCRIPTION"));
                dataInfo2.setCREATETIME(txnInfo.getEventTime());
                dataInfo2.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo2.setLASTUPDATETIME(txnInfo.getEventTime());
                dataInfo2.setLASTUPDATEUSERID(txnInfo.getTxnUser());

                if(orgFile.size()>0) {
                	dataInfo2.excuteDML(SqlConstant.DML_UPDATE);	
                }else {
                	dataInfo2.excuteDML(SqlConstant.DML_INSERT);
                }
                
                  

            }
            
		}

			return recvDoc;
	}

}
