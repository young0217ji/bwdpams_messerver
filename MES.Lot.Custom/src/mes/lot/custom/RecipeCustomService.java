package mes.lot.custom;

import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import mes.lot.data.LOTINFORMATION;
import mes.lot.transaction.RecipeService;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class RecipeCustomService
{
	/**
	 * processID의  recipeID 스텝 시작
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 */
	public void recipeStart(String lotID, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		RecipeService recipeService = new RecipeService();
		
		// LotInformation
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		recipeService.recipeStart(lotInfo, processID, processSequence, routeRelationSequence, recipeID, recipeSequence, recipeRelationCode, recipeTypeCode, txnInfo, pastMode);
	}
	
	/**
	 * processID의  recipeID 스텝 종료
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 */
	public void recipeEnd(String lotID, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		RecipeService recipeService = new RecipeService();
		
		// LotInformation
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		recipeService.recipeEnd(lotInfo, processID, processSequence, routeRelationSequence, recipeID, recipeSequence, recipeRelationCode, recipeTypeCode, txnInfo, pastMode);
	}
	
	/**
	 * processID의  recipeID 스텝 변경
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 */
	public void recipeChange(String lotID, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		RecipeService recipeService = new RecipeService();
		
		// LotInformation
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		recipeService.recipeChange(lotInfo, processID, processSequence, routeRelationSequence, recipeID, recipeSequence, recipeRelationCode, recipeTypeCode, txnInfo, pastMode);
	}
	
	/**
	 * processID의  recipeID 스텝 확인
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 */
	public void recipeCheck(String lotID, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		RecipeService recipeService = new RecipeService();
		
		// LotInformation
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		recipeService.recipeCheck(lotInfo, processID, processSequence, routeRelationSequence, recipeID, recipeSequence, recipeRelationCode, recipeTypeCode, txnInfo, pastMode);
	}
	
	/**
	 * processID의  recipeID 스텝 취소
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 */
	public void recipeProcessCancel(String lotID, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		RecipeService recipeService = new RecipeService();
		
		// LotInformation
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		recipeService.recipeProcessCancel(lotInfo, processID, processSequence, routeRelationSequence, recipeID, recipeSequence, recipeRelationCode, recipeTypeCode, txnInfo, pastMode);
	}
	
	/**
	 * processID의  recipeID 스텝 진행
	 * 
	 * @param lotID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param recipeID
	 * @param recipeSequence
	 * @param recipeRelationCode
	 * @param recipeTypeCode
	 * @param txnInfo
	 * @param pastMode
	 * @return
	 */
	public void recipeProcess(String lotID, String processID, String processSequence, Long routeRelationSequence, String recipeID, 
			String recipeSequence, String recipeRelationCode, String recipeTypeCode, TxnInfo txnInfo, String pastMode)
	{
		RecipeService recipeService = new RecipeService();
		
		// LotInformation
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID( lotID );
		
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		
		recipeService.recipeProcess(lotInfo, processID, processSequence, routeRelationSequence, recipeID, recipeSequence, recipeRelationCode, recipeTypeCode, txnInfo, pastMode);
	}
}
