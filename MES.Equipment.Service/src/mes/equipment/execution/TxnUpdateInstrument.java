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
public class TxnUpdateInstrument implements ObjectExecuteService {
	/**
	 * 계측기 정보를 수정하는 트랜잭션 실행
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

			DY_INSTRUMENT dy_instrument = new DY_INSTRUMENT();
			// Key Value Setting
			dy_instrument.setKeyPLANTID(dataMap.get("PLANTID"));
			dy_instrument.setKeyITEMNO(dataMap.get("ITEMNO"));

			// Key 에 대한 DataInfo 조회
//			if ("C".equals(dataMap.get("_ROWSTATUS")) == false) {
//				dy_instrument = (DY_INSTRUMENT) dy_instrument.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
//			}

			// Data Value Setting
			dy_instrument.setITEMNAME(dataMap.get("ITEMNAME"));
			dy_instrument.setITEMCATEGORY(dataMap.get("ITEMCATEGORY"));
			dy_instrument.setITEMTYPE(dataMap.get("ITEMTYPE"));
			dy_instrument.setDEPARTMENT(dataMap.get("DEPARTMENT"));
			dy_instrument.setASSETFLAG(dataMap.get("ASSETFLAG"));
			dy_instrument.setASSETNO(dataMap.get("ASSETNO"));
			dy_instrument.setITEMGRADE(dataMap.get("ITEMGRADE"));
			dy_instrument.setITEMSTATE(dataMap.get("ITEMSTATE"));
			dy_instrument.setINSTRUMENTREVISION(dataMap.get("INSTRUMENTREVISION"));
			dy_instrument.setACQUISITIONAMOUNT(ConvertUtil.String2Int(dataMap.get("ACQUISITIONAMOUNT")));
			dy_instrument.setRESIDUALAMOUNT(ConvertUtil.String2Int(dataMap.get("RESIDUALAMOUNT")));
			dy_instrument.setACQUISITIONDATE(DateUtil.getTimestamp(dataMap.get("ACQUISITIONDATE")) );
			dy_instrument.setACQUISITIONREASONE(dataMap.get("ACQUISITIONREASONE"));
			dy_instrument.setMAKER(dataMap.get("MAKER"));
			dy_instrument.setMODELNO(dataMap.get("MODELNO"));
			dy_instrument.setSERIALNO(dataMap.get("SERIALNO"));
			dy_instrument.setVENDOR(dataMap.get("VENDOR"));
			dy_instrument.setSTANDARD(dataMap.get("STANDARD"));
			dy_instrument.setRESOLUTION(dataMap.get("RESOLUTION"));
			dy_instrument.setCHECKINTERVAL(dataMap.get("CHECKINTERVAL"));
			dy_instrument.setNEXTCHECKDATE(DateUtil.getTimestamp(dataMap.get("NEXTCHECKDATE")) );
			dy_instrument.setDESCRIPTION(dataMap.get("DESCRIPTION"));
			dy_instrument.setLASTUPDATEUSERID(txnInfo.getTxnUser());
			dy_instrument.setLASTUPDATETIME(txnInfo.getEventTime());

			// DataInfo에 대한 CUD 실행
			dy_instrument.excuteDML(SqlConstant.DML_UPDATE);

			// History 기록이 필요한 경우 사용
			AddHistory history = new AddHistory();
			history.addHistory(dy_instrument, txnInfo, SqlConstant.DML_UPDATE);

		}

		return recvDoc;
	}

}
