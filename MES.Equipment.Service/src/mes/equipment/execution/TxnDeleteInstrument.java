package mes.equipment.execution;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.exception.NoDataFoundException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.orm.sql.util.SqlQueryUtil;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.equipment.data.DY_INSTRUMENT;
import mes.equipment.data.DY_INSTRUMENTDETAIL;
import mes.equipment.data.DY_PMCODEDETAIL;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 20223.09.05
 * 
 * @see
 */
public class TxnDeleteInstrument implements ObjectExecuteService {
	/**
	 * 계측기 상태를 수정하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		
		// History 기록이 필요한 경우 사용
		AddHistory history = new AddHistory();
		
		for (int i = 0; i < masterData.size(); i++)
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) masterData.get(i);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

			DY_INSTRUMENT dy_instrument = new DY_INSTRUMENT();
			DY_INSTRUMENTDETAIL dy_instrumentDetail = new DY_INSTRUMENTDETAIL();
			
			// Key Value Setting
			dy_instrument.setKeyPLANTID(dataMap.get("PLANTID"));
			dy_instrument.setKeyITEMNO(dataMap.get("ITEMNO"));
			dy_instrument.excuteDML(SqlConstant.DML_DELETE);
			
			history.addHistory(dy_instrument, txnInfo, dataMap.get("_ROWSTATUS"));
			
			// Key Value Setting
			dy_instrumentDetail.setKeyPLANTID(dataMap.get("PLANTID"));
			dy_instrumentDetail.setKeyITEMNO(dataMap.get("ITEMNO"));
			
			List<DY_INSTRUMENTDETAIL> instrumentDetailList = (List<DY_INSTRUMENTDETAIL>) dy_instrumentDetail.excuteDML(SqlConstant.DML_SELECTLIST);
			
			for ( int j = 0; j < instrumentDetailList.size(); j++ ) 
			{
				dy_instrumentDetail.setKeyPLANTID(dataMap.get("PLANTID"));
				dy_instrumentDetail.setKeyITEMNO(dataMap.get("ITEMNO"));
				
//				dy_instrumentDetail = instrumentDetailList.get(j);
				dy_instrumentDetail.setKeyRELATIONTIMEKEY(instrumentDetailList.get(j).getKeyRELATIONTIMEKEY());
				//dy_instrumentDetail.excuteDML(SqlConstant.DML_DELETE);
				//history.addHistory(dy_instrumentDetail, txnInfo, dataMap.get("_ROWSTATUS"));
				
				if ( instrumentDetailList.size() > 0 ) {
					dy_instrumentDetail.excuteDML(SqlConstant.DML_DELETE);
				}
    		}
			
			history.addHistory(dy_instrumentDetail, txnInfo, dataMap.get("_ROWSTATUS"));
		}

		return recvDoc;
	}

}
