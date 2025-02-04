package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.WAREHOUSE;
import mes.master.data.WAREHOUSEMAP;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnWarehouseMAP implements ObjectExecuteService
{
	/**
	 * WarehouseMap 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
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

            WAREHOUSEMAP dataInfo = new WAREHOUSEMAP();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyWAREHOUSEID(dataMap.get("WAREHOUSEID"));
            dataInfo.setKeyAREAID(dataMap.get("AREAID"));


            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (WAREHOUSEMAP) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
            	// Warehouse 기등록 여부 체크
            	checkExistWarehouse(dataMap.get("PLANTID"), dataMap.get("WAREHOUSEID"));
            	
            	// AREAID가 이미 다른 WAREHOUSEID에 설정된 경우인지 Check
            	checkDuplicateAreaID(dataMap.get("PLANTID"), dataMap.get("AREAID"));
            	
                dataInfo.setCREATETIME(txnInfo.getEventTime());
                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            }
            else
            {
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
            }
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));


            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") )
            {
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            //AddHistory history = new AddHistory();
            //history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }
    
	private void checkDuplicateAreaID(String sPlantID, String sAreaID) 
	{
		WAREHOUSEMAP CheckInfo = new WAREHOUSEMAP();
		CheckInfo.setKeyPLANTID(sPlantID);
		CheckInfo.setKeyAREAID(sAreaID);
		
		List<WAREHOUSEMAP> CheckList = (List<WAREHOUSEMAP>) CheckInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( CheckList.size() > 0 )
		{
			String sExistData = sPlantID + "," + sAreaID + "("+ CheckList.get(0).getKeyWAREHOUSEID()+")";
			// 해당 정보 ({0}) 가 이미 존재합니다. 확인해주시기 바랍니다.
			throw new CustomException("MD-015", new Object[] {sExistData});
		}
	}
	
	private void checkExistWarehouse(String sPlantID, String sWarehouseID) 
	{
		WAREHOUSE oWarehouse = new WAREHOUSE();
		oWarehouse.setKeyPLANTID(sPlantID);
		oWarehouse.setKeyWAREHOUSEID(sWarehouseID);
		
		List<WAREHOUSE> liWarehouse = (List<WAREHOUSE>) oWarehouse.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( liWarehouse.size() < 1 ) {
			// ({0}) 창고는 존재하지 않습니다.
			throw new CustomException("MM-002", new Object[] {sWarehouseID});
		}
	} 
}
