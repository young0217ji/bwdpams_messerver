package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.WAREHOUSE;
import mes.util.CustomQueryUtil;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnWarehouse implements ObjectExecuteService
{
	/**
	 * 창고 정보를 등록, 수정, 삭제 하는 트랜잭션 실행
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

            WAREHOUSE dataInfo = new WAREHOUSE();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyWAREHOUSEID(dataMap.get("WAREHOUSEID"));

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (WAREHOUSE) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            dataInfo.setWAREHOUSENAME(dataMap.get("WAREHOUSENAME"));
            dataInfo.setWAREHOUSETYPE(dataMap.get("WAREHOUSETYPE"));
            dataInfo.setSTOCKCHECKFLAG(dataMap.get("STOCKCHECKFLAG"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));


            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                // WAREHOUSEMAP 테이블의 데이터 삭제.
                deleteWarehouseMap(dataInfo);
                
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
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));


        }

        return recvDoc;
    }
    
    /***************************************************************************
     * WAREHOUSEMAP 데이터 삭제 (PLANTID, WAREHOUSEID 기준 삭제)
     * @param productInfo
     */
	public void deleteWarehouseMap(WAREHOUSE warehouseInfo)
	{
		String sPlantID = warehouseInfo.getKeyPLANTID();
		String sWarehouseID = warehouseInfo.getKeyWAREHOUSEID();
		
		CustomQueryUtil.deleteWarehouseMapTable("WAREHOUSEMAP", sPlantID, sWarehouseID); 
	}

}
