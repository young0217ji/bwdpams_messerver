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
import mes.equipment.data.DY_INSTRUMENTDETAIL;
import mes.event.MessageParse;
import mes.lot.data.DY_INSPECTIONREPORT;
import mes.lot.data.DY_INSPECTIONREPORTSERIAL;
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
public class TxnEditInstrumentDetail implements ObjectExecuteService {
	/**
	 *  계측기 상태를 수정하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		for (int i = 0; i < masterData.size(); i++) {
			HashMap<String, String> dataMap = (HashMap<String, String>) masterData.get(i);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

			DY_INSTRUMENTDETAIL dy_instrumentDetail = new DY_INSTRUMENTDETAIL();
			// Key Value Setting
			dy_instrumentDetail.setKeyPLANTID(dataMap.get("PLANTID"));
			dy_instrumentDetail.setKeyRELATIONTIMEKEY(dataMap.get("RELATIONTIMEKEY"));
			dy_instrumentDetail.setKeyITEMNO(dataMap.get("ITEMNO"));

			// Key 에 대한 DataInfo 조회
			if ("C".equals(dataMap.get("_ROWSTATUS")) == false) {
				dy_instrumentDetail = (DY_INSTRUMENTDETAIL) dy_instrumentDetail.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			}

			// Data Value Setting
			dy_instrumentDetail.setACTIONTYPE(dataMap.get("ACTIONTYPE"));
			dy_instrumentDetail.setACTIONTYPEDETAIL(dataMap.get("ACTIONTYPEDETAIL"));
			dy_instrumentDetail.setACTIONRESULT(dataMap.get("ACTIONRESULT"));
			dy_instrumentDetail.setACTIONTIME(DateUtil.getTimestamp(dataMap.get("ACTIONTIME")));
			dy_instrumentDetail.setACTIONCOMPANY(dataMap.get("ACTIONCOMPANY"));
			dy_instrumentDetail.setACTIONDEPARTMENT(dataMap.get("ACTIONDEPARTMENT"));
			dy_instrumentDetail.setACTIONCOMPANYUSERID(dataMap.get("ACTIONCOMPANYUSERID"));
			dy_instrumentDetail.setACTIONCOST(ConvertUtil.String2Int(dataMap.get("ACTIONCOST")));
			dy_instrumentDetail.setDESCRIPTION(dataMap.get("DESCRIPTION"));
			dy_instrumentDetail.setLASTUPDATEUSERID(txnInfo.getTxnUser());
			dy_instrumentDetail.setLASTUPDATETIME(txnInfo.getEventTime());

			// DataInfo에 대한 CUD 실행
			if ("U".equals(dataMap.get("_ROWSTATUS"))) {
				dy_instrumentDetail.excuteDML(SqlConstant.DML_UPDATE);

				// History 기록이 필요한 경우 사용
				AddHistory history = new AddHistory();
				history.addHistory(dy_instrumentDetail, txnInfo, dataMap.get("_ROWSTATUS"));
			}

		}

		return recvDoc;
	}

}
