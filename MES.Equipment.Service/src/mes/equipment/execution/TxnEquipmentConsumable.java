package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.equipment.data.DY_EQUIPMENTCONSUMABLE;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.22 
 * 
 * @see
 */
public class TxnEquipmentConsumable implements ObjectExecuteService
{
	/**
	 * 설비의 BM에 소모된 자재를 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
	@Override
    public Object execute(Document recvDoc) {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        AddHistory history = new AddHistory();
        
        for (int i = 0 ; i < masterData.size(); i ++) {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_EQUIPMENTCONSUMABLE oConsumableInfo = new DY_EQUIPMENTCONSUMABLE();
            // Key Value Setting
            oConsumableInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            oConsumableInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
            oConsumableInfo.setKeyREQUESTID(dataMap.get("REQUESTID"));
            oConsumableInfo.setKeyPARTID(dataMap.get("PARTID"));

            // Key 에 대한 DataInfo 조회
            if ( "C".equals(dataMap.get("_ROWSTATUS")) == false ) {
            	oConsumableInfo = (DY_EQUIPMENTCONSUMABLE) oConsumableInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            
            // Data Value Setting
            oConsumableInfo.setQUANTITY( ConvertUtil.Object2Long(dataMap.get("QUANTITY")) );
            oConsumableInfo.setUNIT(dataMap.get("UNIT"));
            oConsumableInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            oConsumableInfo.setLASTUPDATEUSERID( txnInfo.getTxnUser() );
            oConsumableInfo.setLASTUPDATETIME( txnInfo.getEventTime() );

            // DataInfo에  대한 CUD 실행
            if ( "D".equals(dataMap.get("_ROWSTATUS")) ) {
            	oConsumableInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( "C".equals(dataMap.get("_ROWSTATUS")) ) { 
            	oConsumableInfo.setCREATEUSERID( txnInfo.getTxnUser() );
            	oConsumableInfo.setCREATETIME( txnInfo.getEventTime() );
				
            	oConsumableInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( "U".equals(dataMap.get("_ROWSTATUS")) ) {
            	oConsumableInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            history = new AddHistory();
            history.addHistory(oConsumableInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }
}
