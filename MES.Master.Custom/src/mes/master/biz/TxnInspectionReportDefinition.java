package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.exception.NoDataFoundException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.DY_INSPECTIONREPORTDEFINITION;
import mes.master.data.DY_USERPRODUCTDEFINITION;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 20223.09.05 
 * 
 * @see
 */
public class TxnInspectionReportDefinition implements ObjectExecuteService
{
	/**
	 * 검사성적서 기준정보를  등록, 수정, 삭제 하는 트랜잭션 실행
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

            DY_INSPECTIONREPORTDEFINITION dataInfo = new DY_INSPECTIONREPORTDEFINITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPRODUCTID(dataMap.get("PRODUCTID"));

            // Key 에 대한 DataInfo 조회
            try {
                dataInfo = (DY_INSPECTIONREPORTDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

                // 데이터가 존재하는 경우 Update
                // Data Value Setting
                dataInfo = ( DY_INSPECTIONREPORTDEFINITION )SqlQueryUtil.returnRowToData( dataInfo, dataMap );
                
            	dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
                
                // History 기록이 필요한 경우 사용
                AddHistory history = new AddHistory();
                history.addHistory(dataInfo, txnInfo, SqlConstant.DML_UPDATE);
                
                checkUserProductDefinition(dataInfo, txnInfo);
            }
            catch (NoDataFoundException e) {
            	// 데이터가 존재하지 않는 경우 Insert
            	// Data Value Setting
                dataInfo = ( DY_INSPECTIONREPORTDEFINITION )SqlQueryUtil.returnRowToData( dataInfo, dataMap );
                
            	dataInfo.setCREATETIME(txnInfo.getEventTime());
                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
                dataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
                
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
                
                // History 기록이 필요한 경우 사용
                AddHistory history = new AddHistory();
                history.addHistory(dataInfo, txnInfo, SqlConstant.DML_INSERT);
                
                checkUserProductDefinition(dataInfo, txnInfo);
            }
        }

        return recvDoc;
    }

    // DY_USERPRODUCTDEFINITION 정보를 확인하고 Insert 및 Update 처리 로직 수행
	private void checkUserProductDefinition( DY_INSPECTIONREPORTDEFINITION dataInfo, TxnInfo txnInfo )
	{
        DY_USERPRODUCTDEFINITION oUserDataInfo = new DY_USERPRODUCTDEFINITION();
        oUserDataInfo.setKeyPLANTID(dataInfo.getKeyPLANTID());
        oUserDataInfo.setKeyPRODUCTID(dataInfo.getKeyPRODUCTID());
        
        // Key 에 대한 DataInfo 조회
        try {
        	oUserDataInfo = (DY_USERPRODUCTDEFINITION) oUserDataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

            // 데이터가 존재하는 경우 Update
            // Data Value Setting
        	oUserDataInfo.setENDPRODUCTID(dataInfo.getENDPRODUCTID());
        	oUserDataInfo.setENDPRODUCTNAME(dataInfo.getENDPRODUCTNAME());
        	oUserDataInfo.setUSERPRODUCTID(dataInfo.getUSERPRODUCTID());
        	oUserDataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
        	oUserDataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            
        	oUserDataInfo.excuteDML(SqlConstant.DML_UPDATE);
            
            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(oUserDataInfo, txnInfo, SqlConstant.DML_UPDATE);
        }
        catch (NoDataFoundException e) {
        	// 데이터가 존재하지 않는 경우 Insert
        	// Data Value Setting
        	oUserDataInfo.setENDPRODUCTID(dataInfo.getENDPRODUCTID());
        	oUserDataInfo.setENDPRODUCTNAME(dataInfo.getENDPRODUCTNAME());
        	oUserDataInfo.setUSERPRODUCTID(dataInfo.getUSERPRODUCTID());
        	oUserDataInfo.setCREATETIME(txnInfo.getEventTime());
        	oUserDataInfo.setCREATEUSERID(txnInfo.getTxnUser());
        	oUserDataInfo.setLASTUPDATEUSERID(txnInfo.getTxnUser());
        	oUserDataInfo.setLASTUPDATETIME(txnInfo.getEventTime());
            
        	oUserDataInfo.excuteDML(SqlConstant.DML_INSERT);
            
            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(oUserDataInfo, txnInfo, SqlConstant.DML_INSERT);
        }
	}
}
