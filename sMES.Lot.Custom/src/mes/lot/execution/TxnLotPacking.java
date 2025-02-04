package mes.lot.execution;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.event.MessageParse;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.PACKINGLIST;
import mes.lot.validation.LotValidation;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnLotPacking implements ObjectExecuteService
{
	/**
	 * Lot을 포장처리하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc)
	{
		List<HashMap<String, String>> dataList = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
		
		LotValidation validation = new LotValidation();
		NameGenerator generator = new NameGenerator();
		AddHistory addHistory = new AddHistory();
		
		validation.checkListNull(dataList);
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataMap = (HashMap<String, String>) dataList.get(i);

			validation.checkKeyNull(dataMap , new Object[] {"PLANTID", "LOTID"});
			
			String plantID 				= dataMap.get("PLANTID");
			String lotID 				= dataMap.get("LOTID");
			
			
			// 로트 정보 변경
			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID(lotID);
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			validation.validationLotState( lotInfo.getKeyLOTID(), lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED} );
			
			
			if ( "D".equals(dataMap.get("_ROWSTATUS")) )
			{
				validation.checkKeyNull(dataMap, new Object[] {"PACKINGID"});

				txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

				PACKINGLIST packingInfo = new PACKINGLIST();
				packingInfo.setKeyPLANTID(plantID);
				packingInfo.setKeyLOTID(lotID);
				packingInfo.setKeyPACKINGID(dataMap.get("PACKINGID"));
				
				packingInfo = (PACKINGLIST) packingInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
				
				packingInfo.excuteDML(SqlConstant.DML_DELETE);
				
				addHistory.addHistory(packingInfo, txnInfo, dataMap.get("_ROWSTATUS"));
			}
			else if ( "C".equals(dataMap.get("_ROWSTATUS")) || "U".equals(dataMap.get("_ROWSTATUS")) )
			{
				String packingID = null;
				if ( "C".equals(dataMap.get("_ROWSTATUS")) )
				{
					packingID = generator.nameGenerate(plantID, "PackingID", new Object[] {"PN"});
				}
				else
				{
					packingID = dataMap.get("PACKINGID");
				}

				PACKINGLIST packingInfo = new PACKINGLIST();
				packingInfo.setKeyPLANTID(plantID);
				packingInfo.setKeyLOTID(lotID);
				packingInfo.setKeyPACKINGID(packingID);
				
				Timestamp tPackingTime = txnInfo.getEventTime();
				String packingTime = dataMap.get("PACKINGTIME");
				
				if ( packingTime != null && !packingTime.isEmpty() )
				{
					tPackingTime = DateUtil.getTimestamp(packingTime);
				}
				// Key 에 대한 DataInfo 조회
				if ( "U".equals(dataMap.get("_ROWSTATUS")) )
				{
					packingInfo = (PACKINGLIST) packingInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
					
					packingInfo.setPACKINGSTATUS(Constant.SHIPPINGSTATE_WAIT);
					packingInfo.setPACKINGQUANTITY(ConvertUtil.Object2Double(dataMap.get("PACKINGQUANTITY")));
					packingInfo.setPACKINGTIME(tPackingTime);
					packingInfo.setPACKINGUSERID(txnInfo.getTxnUser());
					packingInfo.setMATERIALID(dataMap.get("MATERIALID"));
					packingInfo.setMATERIALQUANTITY(ConvertUtil.Object2Double(dataMap.get("MATERIALQUANTITY")));
					packingInfo.setMATERIALTYPE(dataMap.get("MATERIALTYPE"));
					packingInfo.excuteDML(SqlConstant.DML_UPDATE);
				}
				else
				{
					packingInfo.setPACKINGSTATUS(Constant.SHIPPINGSTATE_WAIT);
					packingInfo.setPACKINGQUANTITY(ConvertUtil.Object2Double(dataMap.get("PACKINGQUANTITY")));
					packingInfo.setPACKINGTIME(tPackingTime);
					packingInfo.setPACKINGUSERID(txnInfo.getTxnUser());
					packingInfo.setMATERIALID(dataMap.get("MATERIALID"));
					packingInfo.setMATERIALQUANTITY(ConvertUtil.Object2Double(dataMap.get("MATERIALQUANTITY")));
					packingInfo.setMATERIALTYPE(dataMap.get("MATERIALTYPE"));
					packingInfo.excuteDML(SqlConstant.DML_INSERT);
				}
				
				addHistory.addHistory(packingInfo, txnInfo, dataMap.get("_ROWSTATUS"));
			}
		}

		return recvDoc;
	}
}
