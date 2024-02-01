package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.DY_MASTERPRODUCTIONSCHEDULE;
import mes.util.EventInfoUtil;
import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.19 
 * 
 * @see
 */
public class TxnMasterProductSchedule implements ObjectExecuteService
{
	/**
	 * 용접기준정보의 기본 속성을 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
	@Override
    public Object execute(Document recvDoc){
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++){
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
            
            DY_MASTERPRODUCTIONSCHEDULE dataInfo = new DY_MASTERPRODUCTIONSCHEDULE();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPRODUCTID(dataMap.get("PRODUCTID"));

            // Key 에 대한 DataInfo 조회
            if ( !"C".equalsIgnoreCase(dataMap.get("_ROWSTATUS")) ){
                dataInfo = (DY_MASTERPRODUCTIONSCHEDULE) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            // Data Value Setting
            dataInfo.setWELDINGPASS(ConvertUtil.Object2Double(dataMap.get("WELDINGPASS")));
            dataInfo.setGROOVE(dataMap.get("GROOVE"));
            dataInfo.setWELDINGMATERIAL(dataMap.get("WELDINGMATERIAL"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setRODTHICK(ConvertUtil.Object2Double(dataMap.get("RODTHICK")));
            dataInfo.setWIREDIA(ConvertUtil.Object2Double(dataMap.get("WIREDIA")));
            dataInfo.setLASTUPDATETIME( txnInfo.getEventTime() );
            dataInfo.setLASTUPDATEUSERID( txnInfo.getTxnUser() );

            // DataInfo에  대한 CUD 실행
            if ("D".equalsIgnoreCase(dataMap.get("_ROWSTATUS"))){
            	//삭제
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ("C".equalsIgnoreCase(dataMap.get("_ROWSTATUS"))){  	
                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo.setCREATETIME(txnInfo.getEventTime());  
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ("U".equalsIgnoreCase(dataMap.get("_ROWSTATUS"))){    
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
            
         // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }
        return recvDoc;
    }
}
