package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.SF_ROLEUSERMAP;
import mes.master.data.SF_USER;
import mes.util.EventInfoUtil;

/**
 * 
 * @author LSMESSolution
 * 
 * @since 2023.04.04
 * 
 * @see
 *
 */

public class TxnSFUser implements ObjectExecuteService
{
	
	/**
	 * SF_User를 등록, 수정, 삭제 하는 트랜젝션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 */
    @Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

        String plantID = MessageParse.getParam(recvDoc, "PLANTID");
        
        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            SF_USER dataInfo = new SF_USER();
            // Key Value Setting
            dataInfo.setKeyUSERID(dataMap.get("USERID"));
            

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (SF_USER) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            dataInfo.setUSERNAME(dataMap.get("USERNAME"));
            dataInfo.setEMAIL(dataMap.get("EMAIL"));
            dataInfo.setPHONE_NUMBER(dataMap.get("PHONE_NUMBER"));
            dataInfo.setUSEFLAG(dataMap.get("USEFLAG")); // 미사용

            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") ) {
            	// 전체 Map 삭제
            	executeRoleUserMapDelete(plantID, dataInfo, txnInfo);
            	
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") ) {
                // 비밀번호 생성 및 수정
                if ( dataMap.get("NEWPASSWORD") != null && dataMap.get("NEWPASSWORD").length() > 0 ) {
                	dataInfo.setPASSWORD(dataMap.get("NEWPASSWORD"));
                }
                else {
                	// 필수값 체크 오류
                	// 비밀번호가 입력되지 않았습니다.
        			throw new CustomException("USER-003", new Object[] {});
                }
                
                // 권한 Map 설정
                if ( dataMap.get("ROLEID") != null && dataMap.get("ROLEID").length() > 0 ) {
                	
                	String sRoleList = dataMap.get("ROLEID");
                	String[] arRoleArray = sRoleList.split(",");
                	
                	// 전체 Map 삭제
                	executeRoleUserMapDelete(plantID, dataInfo, txnInfo);
                	
                	txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
                	for ( int ii = 0; ii < arRoleArray.length; ii++ ) {
                		SF_ROLEUSERMAP oRoleUserMap = new SF_ROLEUSERMAP();
                		oRoleUserMap.setKeyPLANTID(plantID);
                		oRoleUserMap.setKeyROLEID(arRoleArray[ii]);
                		oRoleUserMap.setKeyUSERID(dataInfo.getKeyUSERID());
                		oRoleUserMap.setUSEFLAG(Constant.FLAG_YESNO_YES);
                		oRoleUserMap.setCREATETIME(txnInfo.getEventTime());
                		oRoleUserMap.setLASTUPDATETIME(txnInfo.getEventTime());
                		oRoleUserMap.setCREATEUSERID(txnInfo.getTxnUser());
                		oRoleUserMap.setLASTUPDATEUSERID(txnInfo.getTxnUser());

                		oRoleUserMap.excuteDML(SqlConstant.DML_INSERT);
                		
                		// History 기록이 필요한 경우 사용
                		AddHistory history = new AddHistory();
                		history.addHistory(oRoleUserMap, txnInfo, SqlConstant.DML_INSERT);
                	}
                }
                
            	dataInfo.setCREATETIME(txnInfo.getEventTime());
            	dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            	dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
            	dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") ) {
                // 비밀번호 확인
                if ( dataMap.get("CHECKPASSWORD") != null && dataMap.get("CHECKPASSWORD").length() > 0 ) {
	        		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        		if ( !encoder.matches(dataMap.get("CHECKPASSWORD").toString(), dataInfo.getPASSWORD()) ) {
	        			// ({0}) 사용자의 비밀번호가 일치하지 않습니다.
	        			throw new CustomException("USER-002", dataInfo.getKeyUSERID());
	        		}
                }
                
                // 비밀번호 생성 및 수정
                if ( dataMap.get("NEWPASSWORD") != null && dataMap.get("NEWPASSWORD").length() > 0 ) {
                	dataInfo.setPASSWORD(dataMap.get("NEWPASSWORD"));
                }
                
                // 권한 Map 설정
                if ( dataMap.get("ROLEID") != null && dataMap.get("ROLEID").length() > 0 ) {
                	
                	String sRoleList = dataMap.get("ROLEID");
                	String[] arRoleArray = sRoleList.split(",");
                	
                	if ( sRoleList.equalsIgnoreCase(dataMap.get("OLDROLEID")) == false ) {

                		// 전체 Map 삭제
                		executeRoleUserMapDelete(plantID, dataInfo, txnInfo);

                		txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
                		for ( int ii = 0; ii < arRoleArray.length; ii++ ) {
                			SF_ROLEUSERMAP oRoleUserMap = new SF_ROLEUSERMAP();
                			oRoleUserMap.setKeyPLANTID(plantID);
                			oRoleUserMap.setKeyROLEID(arRoleArray[ii]);
                			oRoleUserMap.setKeyUSERID(dataInfo.getKeyUSERID());
                			oRoleUserMap.setUSEFLAG(Constant.FLAG_YESNO_YES);
                			oRoleUserMap.setCREATETIME(txnInfo.getEventTime());
                			oRoleUserMap.setLASTUPDATETIME(txnInfo.getEventTime());
                			oRoleUserMap.setCREATEUSERID(txnInfo.getTxnUser());
                			oRoleUserMap.setLASTUPDATEUSERID(txnInfo.getTxnUser());

                			oRoleUserMap.excuteDML(SqlConstant.DML_INSERT);

                			// History 기록이 필요한 경우 사용
	                		AddHistory history = new AddHistory();
	                		history.addHistory(oRoleUserMap, txnInfo, SqlConstant.DML_INSERT);
                		}
                	}
                }
                
                
            	dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            	dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }

	private void executeRoleUserMapDelete( String plantID, SF_USER dataInfo, TxnInfo txnInfo ) {
		SF_ROLEUSERMAP oRoleUserMap = new SF_ROLEUSERMAP();
		oRoleUserMap.setKeyPLANTID(plantID);
		oRoleUserMap.setKeyUSERID(dataInfo.getKeyUSERID());
		
		List<SF_ROLEUSERMAP> oRoleUserMapList = (List<SF_ROLEUSERMAP>) oRoleUserMap.excuteDML(SqlConstant.DML_SELECTLIST);
		
		for ( int i = 0; i < oRoleUserMapList.size(); i++ ) {
			oRoleUserMap = oRoleUserMapList.get(i);
			oRoleUserMap.excuteDML(SqlConstant.DML_DELETE);

			// History 기록이 필요한 경우 사용
			AddHistory history = new AddHistory();
			history.addHistory(oRoleUserMap, txnInfo, SqlConstant.DML_DELETE);
		}
	}
}
