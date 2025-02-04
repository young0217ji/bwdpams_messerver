package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.PORT;
import mes.master.data.PORTDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnPort implements ObjectExecuteService
{
	/**
	 * 설비의 Port를 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 */
    @Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++)
        {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
        	txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

        	String plantID = dataMap.get("PLANTID");
        	String equipmentID = dataMap.get("EQUIPMENTID");
        	String portID = dataMap.get("PORTID");

        	PORTDEFINITION portDef = new PORTDEFINITION();
        	portDef.setKeyPLANTID(plantID);
        	portDef.setKeyEQUIPMENTID(equipmentID);
        	portDef.setKeyPORTID(portID);

        	PORT portInfo = new PORT();
        	portInfo.setKeyPLANTID(plantID);
        	portInfo.setKeyEQUIPMENTID(equipmentID);
        	portInfo.setKeyPORTID(portID);

        	// DataInfo에  대한 CUD 실행
        	if ( dataMap.get("_ROWSTATUS").equals("C") )
        	{
        		// 생성
        		portDef.setPORTTYPE(dataMap.get("PORTTYPE"));
        		portDef.setVENDOR(dataMap.get("VENDOR"));
        		portDef.setMODEL(dataMap.get("MODEL"));
        		portDef.setSERIALNUMBER(dataMap.get("SERIALNUMBER"));
        		portDef.setDESCRIPTION(dataMap.get("DESCRIPTION"));
        		portDef.excuteDML(SqlConstant.DML_INSERT);

        		portInfo.setPORTTYPE(dataMap.get("PORTTYPE"));
        		portInfo.setCONTROLMODE(dataMap.get("CONTROLMODE"));
        		portInfo.setTRANSFERSTATE(Constant.PORT_STATE_READYTOLOAD); // ReadyToLoad | ReservedToLoad | PreparedToProcess | Processing | ReadyToUnload | ReservedToUnload
        		portInfo.setSTATECHANGEPOLICY(dataMap.get("STATECHANGEPOLICY"));
        		portInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
        		portInfo.excuteDML(SqlConstant.DML_INSERT);
        	}
        	else
        	{
        		// Key 에 대한 DataInfo 조회
        		portDef = (PORTDEFINITION) portDef.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

        		portInfo = (PORT) portInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

        		// Port의 사용중 여부 체크
        		if ( !Constant.PORT_STATE_READYTOLOAD.equalsIgnoreCase(portInfo.getTRANSFERSTATE()) )
        		{
        			// Port ({0}) 의 상태 ({1}) 가 사용중이므로 정보변경이 불가능합니다.
        			throw new CustomException("MD-014", new Object[] {portInfo.getKeyPORTID(), portInfo.getTRANSFERSTATE()});
        		}

        		// 삭제와 수정
        		if ( dataMap.get("_ROWSTATUS").equals("D") )
        		{
        			portDef.excuteDML(SqlConstant.DML_DELETE);

        			portInfo.excuteDML(SqlConstant.DML_DELETE);
        		}
        		else
        		{
        			// Data Value Setting
        			portDef.setPORTTYPE(dataMap.get("PORTTYPE"));
            		portDef.setVENDOR(dataMap.get("VENDOR"));
            		portDef.setMODEL(dataMap.get("MODEL"));
            		portDef.setSERIALNUMBER(dataMap.get("SERIALNUMBER"));
            		portDef.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            		
            		portInfo.setPORTTYPE(dataMap.get("PORTTYPE"));
            		portInfo.setCONTROLMODE(dataMap.get("CONTROLMODE"));
//            		portInfo.setTRANSFERSTATE(Constant.PORT_STATE_READYTOLOAD); // ReadyToLoad | ReservedToLoad | PreparedToProcess | Processing | ReadyToUnload | ReservedToUnload
            		portInfo.setSTATECHANGEPOLICY(dataMap.get("STATECHANGEPOLICY"));
            		portInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));

        			if ( dataMap.get("_ROWSTATUS").equals("U") )
        			{
        				portDef.excuteDML(SqlConstant.DML_UPDATE);
        				
        				portInfo.excuteDML(SqlConstant.DML_UPDATE);
        			}
        		}
        	}

        	// History 기록이 필요한 경우 사용
        	AddHistory history = new AddHistory();
        	history.addHistory(portDef, txnInfo, dataMap.get("_ROWSTATUS"));
        	history.addHistory(portInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }
}
