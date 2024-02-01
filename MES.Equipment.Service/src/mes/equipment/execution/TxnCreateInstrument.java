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
import mes.equipment.data.DY_INSTRUMENT;
import mes.equipment.data.DY_INSTRUMENTDETAIL;
import mes.event.MessageParse;
import mes.master.data.PLANT;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;


/**
 * @author LSMESSolution
 * 
 * @since 20223.09.05
 * 
 * @see
 */
public class TxnCreateInstrument implements ObjectExecuteService {
	/**
	 * 계측기 정보를 생성하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		AddHistory history = new AddHistory();

		NameGenerator nameGenerator = new NameGenerator();
		
		for (int i = 0; i < masterData.size(); i++) {
			HashMap<String, String> dataMap = (HashMap<String, String>) masterData.get(i);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
			
			//get PLANT.REPRESENTATIVECHAR.
			PLANT plant = new PLANT();
			plant.setKeyPLANTID(dataMap.get("PLANTID"));			
			plant = (PLANT)plant.excuteDML( SqlConstant.DML_SELECTROW );

			DY_INSTRUMENT dy_instrument = new DY_INSTRUMENT();
			// Key Value Setting
			dy_instrument.setKeyPLANTID(dataMap.get("PLANTID"));
//			dy_instrument.setKeyITEMNO(dataMap.get("ITEMNO"));
			
        	String sItemNo = nameGenerator.InstrumentItemIDGenerate(dataMap.get("PLANTID"), plant.getREPRESENTATIVECHAR(), dataMap.get("ITEMTYPE"));
        	
        	dy_instrument.setKeyITEMNO(sItemNo);

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

			dy_instrument.setCREATEUSERID(txnInfo.getTxnUser());
			dy_instrument.setCREATETIME(txnInfo.getEventTime());

			dy_instrument.excuteDML(SqlConstant.DML_INSERT);
			
			

			// History 기록이 필요한 경우 사용
			history = new AddHistory();
			history.addHistory(dy_instrument, txnInfo, SqlConstant.DML_INSERT);
			

		}

		return recvDoc;
	}

}
