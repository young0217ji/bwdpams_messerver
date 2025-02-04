package mes.lot.execution;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import mes.constant.Constant;
import mes.event.MessageParse;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.REWORKINFORMATION;
import mes.lot.transaction.LotService;
import mes.lot.validation.LotValidation;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnLotReworkCancel implements ObjectExecuteService
{
	/**
	 * Rework Lot을 취소하는 트랜잭션 실행
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
		LotService lotService = new LotService();
		
		validation.checkListNull(dataList);
		
		for ( int i = 0; i < dataList.size(); i++ ) 
		{
			HashMap<String, String> dataLotMap = (HashMap<String, String>) dataList.get(i);

			validation.checkKeyNull(dataLotMap , new Object[] {"LOTID"});

			String lotID 						= dataLotMap.get("LOTID");
			String processRouteID				= dataLotMap.get("PROCESSROUTEID");
			String relationSequence				= dataLotMap.get("REWORKCOUNT");
			String description			 		= dataLotMap.get("DESCRIPTION");
			
			
			txnInfo.setTxnComment( description );

			
			// LotInformation
			LOTINFORMATION lotInfo = new LOTINFORMATION();
			lotInfo.setKeyLOTID(lotID);
			lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);

			validation.validationLotState(lotInfo.getKeyLOTID(), lotInfo.getLOTSTATE(), new Object[] {Constant.LOTSTATE_RELEASED});

			
			REWORKINFORMATION reworkInfo = new REWORKINFORMATION();
			reworkInfo.setKeyLOTID(lotInfo.getKeyLOTID());
			reworkInfo.setKeyPROCESSROUTEID(processRouteID);
			reworkInfo.setKeyRELATIONSEQUENCE( ConvertUtil.Object2Long(relationSequence) );
			reworkInfo.setREWORKSTATE(Constant.REWORKSTATE_PROCESSING);
			reworkInfo = (REWORKINFORMATION) reworkInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
			
			reworkInfo.setENDTIME( txnInfo.getEventTime() );
			reworkInfo.setREWORKSTATE(Constant.REWORKSTATE_COMPLETE);
			reworkInfo.excuteDML(SqlConstant.DML_UPDATE);

			// 재작업 진행 취소
			lotService.makeLotNotInRework(lotInfo, reworkInfo, txnInfo);
		}
		
		return recvDoc;
	}
}
