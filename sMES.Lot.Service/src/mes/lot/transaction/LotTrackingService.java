package mes.lot.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import kr.co.mesframe.exception.NoDataFoundException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.lot.data.LOTINFORMATION;
import mes.lot.data.REWORKINFORMATION;
import mes.lot.data.SAMPLINGINFORMATION;
import mes.lot.data.VIEWLOTTRACKING;
import mes.master.data.PRODUCTDEFINITION;
import mes.master.data.SAMPLINGDEFINITION;
import mes.master.data.SAMPLINGPOLICY;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class LotTrackingService
{
//	private static Log	log	= LogFactory.getLog(LotTrackingService.class);
	
	/**
	 * lotID의 다음 공정 반환
	 * 
	 * @param lotID
	 * @param processRouteID
	 * @param routeRelationSequence
	 * @param actionType
	 * @return VIEWLOTTRACKING
	 * 
	 */
	public VIEWLOTTRACKING getNextProcess(String lotID, String processRouteID, Long routeRelationSequence, String actionType)
	{
		return getNextProcess(lotID, processRouteID, routeRelationSequence, actionType, Long.valueOf(0));
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 다음 공정 반환
	 * 
	 * @param lotID
	 * @param processRouteID
	 * @param routeRelationSequence
	 * @param actionType
	 * @param processSequence
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING getNextProcess(String lotID, String processRouteID, Long routeRelationSequence, String actionType, Long processSequence)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( processRouteID );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( actionType );
//		viewLotInfo.setSTATUS( Constant.OPERATIONSTATE_WAITING );
		
		String suffix = " AND processSequence > " + String.valueOf(processSequence) + " ORDER BY processSequence ";
		
		// 현재 처리 공정의 다음공정을 검색하는 로직
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
		
		if ( listViewLotInfo.size() > 0 )
		{
			viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(0);
		}
		else
		{
			if ( viewLotInfo.getROUTERELATIONTYPE() != null && 
					!Constant.PROCESSROUTERELATION_REWORK.equalsIgnoreCase(viewLotInfo.getROUTERELATIONTYPE()) && 
					viewLotInfo.getROUTERELATIONSEQUENCE() != null )
			{
				viewLotInfo = getNextProcess(lotID, actionType, viewLotInfo.getROUTERELATIONSEQUENCE());
			}
			else
			{
				// 다음 진행 가능 공정이 존재하지 않습니다.
				return null;
			}
		}
		
		if ( viewLotInfo.getPROCESSSEQUENCE() == null )
		{
			if ( viewLotInfo.getROUTERELATIONTYPE() != null && 
					!Constant.PROCESSROUTERELATION_REWORK.equalsIgnoreCase(viewLotInfo.getROUTERELATIONTYPE()) && 
					viewLotInfo.getROUTERELATIONSEQUENCE() != null )
			{
				viewLotInfo = getNextProcess(lotID, actionType, viewLotInfo.getROUTERELATIONSEQUENCE());
			}
			else
			{
				// 다음 진행 가능 공정이 존재하지 않습니다.
				return null;
			}
		}
		
		// 샘플링 검사 여부 체크 ( 다음공정이 있는 경우에만 해당 )
		if ( viewLotInfo != null && viewLotInfo.getPROCESSSEQUENCE() != null && 
				Constant.RECIPE_ID_MEASURE.equalsIgnoreCase(viewLotInfo.getPROCESSTYPE()) )
		{
			viewLotInfo = checkSamplingProcess(lotID, viewLotInfo);
		}
		
		return viewLotInfo;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 다음 공정 반환
	 * 
	 * @param lotID
	 * @param actionType
	 * @param routeRelationSequence
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING getNextProcess(String lotID, String actionType, Long routeRelationSequence)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( actionType );
		viewLotInfo.setSTATUS( Constant.OPERATIONSTATE_WAITING );
		
		String suffix = " AND routerelationSequence > " + String.valueOf(routeRelationSequence) +
				" ORDER BY routerelationSequence, processPrintIndex, recipePrintIndex, orderIndex, timeKey ";
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
		
		if ( listViewLotInfo.size() > 0 )
		{
			viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(0);
		}
		else
		{
			// 다음 진행 가능 공정이 존재하지 않습니다.
			return null;
		}
		
		if ( viewLotInfo.getPROCESSSEQUENCE() == null )
		{
			// 다음 진행 가능 공정이 존재하지 않습니다.
			return null;
		}
		
		return viewLotInfo;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 이전 공정 반환 
	 * 
	 * @param lotID
	 * @param processRouteID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING getPreviewProcess(String lotID, String processRouteID, Long processSequence, Long routeRelationSequence)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( processRouteID );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_END );
		
		String suffix = " AND processSequence < " + String.valueOf(processSequence) + " ORDER BY processSequence DESC ";
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
		
		if ( listViewLotInfo.size() > 0 )
		{
			viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(0);
		}
		else
		{
			// 진행 가능 공정이 존재하지 않습니다.
			return null;
		}
		
		return viewLotInfo;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 이전 공정이 존재하는지 확인
	 * 
	 * @param lotID
	 * @param processRouteID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @return boolean
	 * 
	 */
	@SuppressWarnings("unchecked")
	public boolean checkPreviewProcess(String lotID, String processRouteID, Long processSequence, Long routeRelationSequence)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( processRouteID );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_END );
		viewLotInfo.setSTATUS(Constant.OPERATIONSTATE_WAITING);
		
		String suffix = " AND processSequence < " + String.valueOf(processSequence) + " ORDER BY processSequence DESC ";
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
		
		if ( listViewLotInfo.size() > 0 )
		{
			return false;
		}
		else
		{
			// 진행 가능 공정이 존재하지 않습니다.
			return true;
		}
	}
	
	/** 
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 공정 정보 반환 
	 * 
	 * @param lotID
	 * @param processRouteID
	 * @param processID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @param actionType
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING getProcessInfo(String lotID, String processRouteID, String processID, Long processSequence, Long routeRelationSequence, String actionType)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( processRouteID );
		viewLotInfo.setPROCESSID( processID );
		viewLotInfo.setPROCESSSEQUENCE( processSequence );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( actionType );
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( listViewLotInfo.size() > 0 )
		{
			viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(0);
		}
		else
		{
			// 다음 진행 가능 공정이 존재하지 않습니다.
			return null;
		}
		
		if ( viewLotInfo.getPROCESSSEQUENCE() == null )
		{
			// 다음 진행 가능 공정이 존재하지 않습니다.
			return null;
		}
		
		return viewLotInfo;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 시작할 공정 리스트 반환
	 * 
	 * @param lotID
	 * @param processRouteID
	 * @param routeRelationSequence
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VIEWLOTTRACKING> getStartProcessList(String lotID, String processRouteID, Long routeRelationSequence)
	{
		List<VIEWLOTTRACKING> listProcess = new ArrayList<VIEWLOTTRACKING>();
		
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( processRouteID );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_START );
		
		String suffix = " ORDER BY routerelationSequence, processPrintIndex, recipePrintIndex, orderIndex, timeKey ";
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
		
		if ( listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				listProcess.add(viewLotElement);
			}
		}
		else
		{
			// 공정이 존재하지 않습니다.
		}
		
		return listProcess;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 공정 리스트 반환
	 * 
	 * @param lotID
	 * @param processRouteID
	 * @param routeRelationSequence
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VIEWLOTTRACKING> getProcessList(String lotID, String processRouteID, Long routeRelationSequence)
	{
		List<VIEWLOTTRACKING> listProcess = new ArrayList<VIEWLOTTRACKING>();
		
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( processRouteID );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_END );
		viewLotInfo.setSTATUS( Constant.OPERATIONSTATE_WAITING );
		
		String suffix = " ORDER BY routerelationSequence, processPrintIndex, recipePrintIndex, orderIndex, timeKey ";
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
		
		if ( listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				listProcess.add(viewLotElement);
			}
		}
		else
		{
			// 공정이 존재하지 않습니다.
		}
		
		return listProcess;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 파라미터 정보 반환 (InputMode : OperationParameter)
	 *  
	 * @param processLotInfo
	 * @param recipeParameterID
	 * @return VIEWLOTTRACKING
	 * 
	 */
	public VIEWLOTTRACKING getProcessParameterInfo(VIEWLOTTRACKING processLotInfo, String recipeParameterID)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( processLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( processLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( processLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( processLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setRECIPEPARAMETERID( recipeParameterID );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATIONPARAMETER );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		return viewLotInfo;
	}
	
	/**
	 * getProcessParameterList 함수 호출해 공정 Parameter 리스트 반환
	 * 
	 * @param processLotInfo
	 * @return List
	 * 
	 */
	public List<VIEWLOTTRACKING> getProcessParameterList(VIEWLOTTRACKING processLotInfo)
	{
		return getProcessParameterList(processLotInfo, null);
	}

	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 공정 Parameter 리스트 반환 (InputMode : OperationParameter)
	 * 
	 * @param processLotInfo
	 * @param cancelEvent
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VIEWLOTTRACKING> getProcessParameterList(VIEWLOTTRACKING processLotInfo, String cancelEvent)
	{
		List<VIEWLOTTRACKING> listProcessParameter = new ArrayList<VIEWLOTTRACKING>();
		
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( processLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( processLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( processLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( processLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATIONPARAMETER );
		
		if ( Constant.RECIPE_ID_MEASURE.equalsIgnoreCase(processLotInfo.getPROCESSTYPE()) || 
				Constant.RECIPE_ID_PACKING.equalsIgnoreCase(processLotInfo.getPROCESSTYPE()) )
		{
//			viewLotInfo.setSTATUS( Constant.RECIPEPARAMETERSTATE_PROCESSING );
		}
		else
		{
			if ( Constant.FLAG_YESNO_YES.equalsIgnoreCase(cancelEvent) )
			{
				viewLotInfo.setSTATUS( Constant.RECIPEPARAMETERSTATE_PROCESSING );
			}
			else
			{
				viewLotInfo.setSTATUS( Constant.RECIPEPARAMETERSTATE_WAITING );
			}
		}
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				listProcessParameter.add(viewLotElement);
			}
		}
		
		return listProcessParameter;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 검사 Parameter 반환
	 * 
	 * @param processLotInfo
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING getInspectionParameter(VIEWLOTTRACKING processLotInfo)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( processLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( processLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( processLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( processLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setPROCESSTYPE( Constant.RECIPE_ID_MEASURE );
		viewLotInfo.setRECIPEPARAMETERID( Constant.RECIPE_ID_MEASURE );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATIONPARAMETER );
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		if ( listViewLotInfo.size() > 0 )
		{
			viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(0);
		}
		else
		{
			viewLotInfo = null;
		}
		
		return viewLotInfo;
	}
	
	/**
	 * getNextRecipe 함수 호출해 다음 스텝 반환
	 * 
	 * @param processLotInfo
	 * @return VIEWLOTTRACKING
	 * 
	 */
	public VIEWLOTTRACKING getNextRecipe(VIEWLOTTRACKING processLotInfo)
	{
		return getNextRecipe(processLotInfo, Long.valueOf(0));
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 다음 스텝 반환 (InputMode : Recipe)
	 * 
	 * @param processLotInfo
	 * @param recipeSeq
	 * @return VIEWLOTTRACKING
	 * 
	 */ 
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING getNextRecipe(VIEWLOTTRACKING processLotInfo, Long recipeSeq)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( processLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( processLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( processLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( processLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		
		String suffix = " AND recipeSequence >= " + String.valueOf(recipeSeq) + " ORDER BY recipeSequence, orderIndex ";
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
		
		if ( listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				if ( viewLotInfo.getRECIPESEQUENCE().equals(recipeSeq) )
				{
					// 스텝 순번이 같은 경우
					if ( !Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
					{
						// 진행중 혹은 완료인 경우
						viewLotInfo = new VIEWLOTTRACKING();
						continue;
					}
					else if ( Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) && Constant.FLAG_YN_NO.equalsIgnoreCase(viewLotInfo.getCURRENTFLAG()) )
					{
						// 선택 스텝에 대한 정보 변경
						break;
					}
					else
					{
						viewLotInfo = new VIEWLOTTRACKING();
						break;
					}
				}
				else
				{
					// 스텝 순번이 이후 스텝인 경우
					if ( Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(viewLotInfo.getSTATUS()) && Constant.FLAG_YN_NO.equalsIgnoreCase(viewLotInfo.getCURRENTFLAG()) )
					{
						if ( viewLotInfo.getRECIPEID().equalsIgnoreCase(Constant.RECIPE_ID_ADDMATERIAL) )
						{
							viewLotInfo.setSTATUS( Constant.RECIPESTATE_PROCESSING );
							viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
							viewLotInfo.excuteDML(SqlConstant.DML_UPDATE);
						}
						else
						{
							// 선택 스텝에 대한 정보 변경
							break;
						}
					}
					else
					{
						if ( viewLotInfo.getRECIPEID().equalsIgnoreCase(Constant.RECIPE_ID_ADDMATERIAL) )
						{
							viewLotInfo = new VIEWLOTTRACKING();
							continue;
						}
						else
						{
							viewLotInfo = new VIEWLOTTRACKING();
							break;
						}
					}
				}
			}
		}
		else
		{
			// 현재 공정의 공정종료 Open
			VIEWLOTTRACKING endOperationLotInfo = 
					getProcessInfo(processLotInfo.getKeyLOTID(), processLotInfo.getPROCESSROUTEID(), processLotInfo.getPROCESSID(), processLotInfo.getPROCESSSEQUENCE(), processLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_END);
			
			if ( Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(endOperationLotInfo.getSTATUS()) && Constant.FLAG_YN_NO.equalsIgnoreCase(endOperationLotInfo.getCURRENTFLAG()) )
			{
				endOperationLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
				endOperationLotInfo.excuteDML(SqlConstant.DML_UPDATE);
			}

			// 다음 진행 가능 스텝이 존재하지 않습니다.
			return null;
		}
		
		if ( viewLotInfo.getRECIPESEQUENCE() == null )
		{
			viewLotInfo = new VIEWLOTTRACKING();
			viewLotInfo.setKeyLOTID( processLotInfo.getKeyLOTID() );
			viewLotInfo.setPROCESSROUTEID( processLotInfo.getPROCESSROUTEID() );
			viewLotInfo.setPROCESSID( processLotInfo.getPROCESSID() );
			viewLotInfo.setPROCESSSEQUENCE( processLotInfo.getPROCESSSEQUENCE() );
			viewLotInfo.setROUTERELATIONSEQUENCE( processLotInfo.getROUTERELATIONSEQUENCE() );
			viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
			viewLotInfo.setSTATUS( Constant.RECIPESTATE_WAITING );
			listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
			if ( listViewLotInfo.size() < 1 )
			{
				// 현재 공정의 공정종료 Open
				VIEWLOTTRACKING endOperationLotInfo = 
						getProcessInfo(processLotInfo.getKeyLOTID(), processLotInfo.getPROCESSROUTEID(), processLotInfo.getPROCESSID(), processLotInfo.getPROCESSSEQUENCE(), processLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_END);
				
				if ( Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(endOperationLotInfo.getSTATUS()) && Constant.FLAG_YN_NO.equalsIgnoreCase(endOperationLotInfo.getCURRENTFLAG()) )
				{
					endOperationLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
					endOperationLotInfo.excuteDML(SqlConstant.DML_UPDATE);
				}
			}
			
			// 다음 진행 가능 스텝이 존재하지 않습니다.
			return null;
		}
		
		return viewLotInfo;
	}
	
	/**
	 * 받아온 정보로 VIEWLOTTRACKING 값 설정 후 조회하여  스텝 정보를 반환
	 *  
	 * @param processLotInfo
	 * @param recipeID
	 * @param recipeSeq
	 * @param recipeType
	 * @param recipeRelationCode
	 * @return VIEWLOTTRACKING
	 *  
	 */
	public VIEWLOTTRACKING getRecipeInfo(VIEWLOTTRACKING processLotInfo, String recipeID, Long recipeSeq, String recipeType, String recipeRelationCode)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( processLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( processLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( processLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( processLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setRECIPEID( recipeID );
		viewLotInfo.setRECIPESEQUENCE( recipeSeq );
		viewLotInfo.setRECIPETYPE( recipeType );
		viewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		return viewLotInfo;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 자재 정보 반환 (InputMode : Recipe)
	 * 
	 * @param consumableLotInfo
	 * @param recipeID
	 * @param recipeSeq
	 * @param recipeType
	 * @param recipeRelationCode
	 * @return VIEWLOTTRACKING
	 * 
	 */
	public VIEWLOTTRACKING getConsumableInfo(VIEWLOTTRACKING consumableLotInfo, String recipeID, Long recipeSeq, String recipeType, String recipeRelationCode)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( consumableLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( consumableLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( consumableLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( consumableLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( consumableLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setRECIPEID( recipeID );
		viewLotInfo.setRECIPESEQUENCE( recipeSeq );
		viewLotInfo.setRECIPETYPE( recipeType );
		viewLotInfo.setRECIPERELATIONCODE( recipeRelationCode );
		viewLotInfo.setRECIPEPARAMETERID( consumableLotInfo.getRECIPEPARAMETERID() );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		return viewLotInfo;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 스텝 리스트 반환 (InputMode : Recipe)
	 * 
	 * @param processLotInfo
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VIEWLOTTRACKING> getRecipeList(VIEWLOTTRACKING processLotInfo)
	{
		List<VIEWLOTTRACKING> listRecipe = new ArrayList<VIEWLOTTRACKING>();
		
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( processLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( processLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( processLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( processLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				listRecipe.add(viewLotElement);
			}
		}
		else
		{
			// 스텝이 존재하지 않습니다.
		}
		
		return listRecipe;
	}
	
	/**
	 * 받아온 정보로 값 설정 후 조회하여 자재 리스트 반환
	 * 
	 * @param recipeLotInfo
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VIEWLOTTRACKING> getConsumableList(VIEWLOTTRACKING recipeLotInfo)
	{
		List<VIEWLOTTRACKING> listRecipe = new ArrayList<VIEWLOTTRACKING>();
		
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( recipeLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( recipeLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( recipeLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( recipeLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( recipeLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setRECIPEID( recipeLotInfo.getRECIPEID() );
		viewLotInfo.setRECIPESEQUENCE( recipeLotInfo.getRECIPESEQUENCE() );
		viewLotInfo.setRECIPERELATIONCODE( recipeLotInfo.getRECIPERELATIONCODE() );
		viewLotInfo.setRECIPETYPECODE( recipeLotInfo.getRECIPETYPECODE() );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPE );
		viewLotInfo.setACTIONTYPE( Constant.RECIPE_ID_CONSUMABLE );
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				listRecipe.add(viewLotElement);
			}
		}
		else
		{
			// 스텝이 존재하지 않습니다.
		}
		
		return listRecipe;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 스텝 파라미터 반환 (InputMode : RecipeParameter)
	 * 
	 * @param recipeLotInfo
	 * @param recipeParameterID
	 * @return VIEWLOTTRACKING
	 * 
	 */
	public VIEWLOTTRACKING getRecipeParameterInfo(VIEWLOTTRACKING recipeLotInfo, String recipeParameterID)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( recipeLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( recipeLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( recipeLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( recipeLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( recipeLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setRECIPEID( recipeLotInfo.getRECIPEID() );
		viewLotInfo.setRECIPESEQUENCE( recipeLotInfo.getRECIPESEQUENCE() );
		viewLotInfo.setRECIPERELATIONCODE( recipeLotInfo.getRECIPERELATIONCODE() );
		viewLotInfo.setRECIPETYPECODE( recipeLotInfo.getRECIPETYPECODE() );
		viewLotInfo.setRECIPEPARAMETERID( recipeParameterID );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPEPARAMETER );
		
		viewLotInfo = (VIEWLOTTRACKING) viewLotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		return viewLotInfo;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 스텝 파라미터 리스트 반환 (InputMode : RecipeParameter)
	 * 
	 * @param recipeLotInfo
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VIEWLOTTRACKING> getRecipeParameterList(VIEWLOTTRACKING recipeLotInfo)
	{
		List<VIEWLOTTRACKING> listRecipeParameter = new ArrayList<VIEWLOTTRACKING>();
		
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( recipeLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( recipeLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( recipeLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( recipeLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( recipeLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setRECIPEID( recipeLotInfo.getRECIPEID() );
		viewLotInfo.setRECIPESEQUENCE( recipeLotInfo.getRECIPESEQUENCE() );
		viewLotInfo.setRECIPERELATIONCODE( recipeLotInfo.getRECIPERELATIONCODE() );
		viewLotInfo.setRECIPETYPECODE( recipeLotInfo.getRECIPETYPECODE() );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPEPARAMETER );
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				listRecipeParameter.add(viewLotElement);
			}
		}
		else
		{
			// 스텝 조건이 존재하지 않습니다.
		}
		
		return listRecipeParameter;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 상태 Completed로 수정(InputMode : RecipeParameter)
	 * 
	 * @param recipeLotInfo
	 * @param startViewLotInfo
	 * @param txnInfo
	 * @return 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void recipeParameterComplete(VIEWLOTTRACKING recipeLotInfo, VIEWLOTTRACKING startViewLotInfo, TxnInfo txnInfo)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( recipeLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( recipeLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setROUTERELATIONSEQUENCE( recipeLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setRECIPEID( recipeLotInfo.getRECIPEID() );
		viewLotInfo.setRECIPERELATIONCODE( recipeLotInfo.getRECIPERELATIONCODE() );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_RECIPEPARAMETER );
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING recipeParameterInfo = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				recipeParameterInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
				recipeParameterInfo.setSTATUS( Constant.RECIPEPARAMETERSTATE_COMPLETE );
				recipeParameterInfo.excuteDML(SqlConstant.DML_UPDATE);
				
				// Time 공정조건에 의한 QueueTime 관리
				if ( Constant.RECIPEPARAMETER_ID_TIME.equalsIgnoreCase(recipeParameterInfo.getRECIPEPARAMETERID()) )
				{
					LotQueueTimeManager.endLotQueueTime(startViewLotInfo, txnInfo);
				}
			}
		}
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 상태 Completed로 수정(InputMode : OpertationParameter)
	 * 
	 * @param processLotInfo
	 * @param startViewLotInfo
	 * @param txnInfo
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void processParameterComplete(VIEWLOTTRACKING processLotInfo, VIEWLOTTRACKING startViewLotInfo, TxnInfo txnInfo)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( processLotInfo.getKeyLOTID() );
		viewLotInfo.setPROCESSROUTEID( processLotInfo.getPROCESSROUTEID() );
		viewLotInfo.setPROCESSID( processLotInfo.getPROCESSID() );
		viewLotInfo.setPROCESSSEQUENCE( processLotInfo.getPROCESSSEQUENCE() );
		viewLotInfo.setROUTERELATIONSEQUENCE( processLotInfo.getROUTERELATIONSEQUENCE() );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATIONPARAMETER );
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING recipeParameterInfo = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				recipeParameterInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
				recipeParameterInfo.setSTATUS( Constant.RECIPEPARAMETERSTATE_COMPLETE );
				recipeParameterInfo.excuteDML(SqlConstant.DML_UPDATE);
				
				// Time 공정조건에 의한 QueueTime 관리
				if ( Constant.RECIPEPARAMETER_ID_TIME.equalsIgnoreCase(recipeParameterInfo.getRECIPEPARAMETERID()) )
				{
					LotQueueTimeManager.endLotQueueTime(startViewLotInfo, txnInfo);
				}
			}
		}
	}
	
	/**
	 * 받아온 parameterType을  VIEWLOTTRACKING의 InputMode에 값 설정 후 조회하여 파라미터 리스트 반환
	 * 
	 * @param lotID
	 * @param processRouteID
	 * @param routeRelationSequence
	 * @param parameterType
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VIEWLOTTRACKING> getParameterList(String lotID, String processRouteID, Long routeRelationSequence, String parameterType)
	{
		List<VIEWLOTTRACKING> listParamter = new ArrayList<VIEWLOTTRACKING>();
		
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( processRouteID );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( parameterType );
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( listViewLotInfo.size() > 0 )
		{
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				listParamter.add(viewLotElement);
			}
		}
		else
		{
			// 스텝이 존재하지 않습니다.
		}
		
		return listParamter;
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 CurrentFlag가 Yes인  Lot 정보 반환
	 * 
	 * @param lotID
	 * @param processRouteType
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public VIEWLOTTRACKING getCurrentViewLotInfo(String lotID, String processRouteType)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTETYPE( processRouteType );
		viewLotInfo.setROUTERELATIONSEQUENCE( null );
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		if ( listViewLotInfo.size() > 0 )
		{
			HashMap<String, VIEWLOTTRACKING> listViewLotElement = new HashMap<String, VIEWLOTTRACKING>();

			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);

				listViewLotElement.put(viewLotElement.getKeyTIMEKEY(), viewLotElement);
			}
			
			TreeMap<String, VIEWLOTTRACKING> orderMap = new TreeMap(listViewLotElement);
			viewLotInfo = orderMap.get(orderMap.lastKey());
			
			return viewLotInfo;
		}
		else
		{
			viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
			viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
			viewLotInfo.setACTIONTYPE( Constant.EVENT_START );
			viewLotInfo.setSTATUS( Constant.OPERATIONSTATE_WAITING );
			
			listViewLotInfo = (List) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
			if ( listViewLotInfo.size() > 0 )
			{
				HashMap<Long, VIEWLOTTRACKING> listViewLotElement = new HashMap<Long, VIEWLOTTRACKING>();
				
				for ( int i = 0; i < listViewLotInfo.size(); i++ )
				{
					VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);
					
					listViewLotElement.put(viewLotElement.getPROCESSSEQUENCE(), viewLotElement);
				}
				
				for ( Iterator iterator = new TreeMap(listViewLotElement).entrySet().iterator(); iterator.hasNext(); )
				{
					Map.Entry<Long, VIEWLOTTRACKING> iteratorMap = (Map.Entry<Long, VIEWLOTTRACKING>) iterator.next();
					viewLotInfo = iteratorMap.getValue();
					break;
				}
			}
			else
			{
				// 다음 진행 가능 공정이 존재하지 않습니다.
				return null;
			}
			
			return viewLotInfo;
		}
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 CurrentFlag가 Yes인  Lot 정보 반환
	 * 
	 * @param lotInfo
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public VIEWLOTTRACKING getCurrentViewLotInfo(LOTINFORMATION lotInfo)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID(lotInfo.getKeyLOTID());
		viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_YES );
		
		if ( Constant.LOTREWORKSTATE_INREWORK.equalsIgnoreCase(lotInfo.getREWORKSTATE()) )
		{
			REWORKINFORMATION reworkInfo = new REWORKINFORMATION();
			reworkInfo.setKeyLOTID(lotInfo.getKeyLOTID());
			reworkInfo.setREWORKSTATE(Constant.REWORKSTATE_PROCESSING);
			
			String Surfix = " ORDER BY relationSequence DESC ";
			List<Object> listReworkInfo = (List<Object>) reworkInfo.excuteDML(SqlConstant.DML_SELECTLIST, Surfix);
			
			reworkInfo = (REWORKINFORMATION) listReworkInfo.get(0);
			
			
			viewLotInfo.setPROCESSROUTEID(reworkInfo.getKeyPROCESSROUTEID());
			viewLotInfo.setPROCESSROUTETYPE(Constant.PROCESSROUTETYPE_REWORK);
			viewLotInfo.setROUTERELATIONTYPE(Constant.PROCESSROUTETYPE_REWORK);
			viewLotInfo.setROUTERELATIONSEQUENCE(reworkInfo.getKeyRELATIONSEQUENCE());
		}
		else
		{
			viewLotInfo.setPROCESSROUTEID(lotInfo.getPROCESSROUTEID());
			viewLotInfo.setROUTERELATIONTYPE(null);
			viewLotInfo.setROUTERELATIONSEQUENCE(null);
		}
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		if ( listViewLotInfo.size() > 0 )
		{
			HashMap<String, VIEWLOTTRACKING> listViewLotElement = new HashMap<String, VIEWLOTTRACKING>();

			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);

				listViewLotElement.put(viewLotElement.getKeyTIMEKEY(), viewLotElement);
			}
			
			TreeMap<String, VIEWLOTTRACKING> orderMap = new TreeMap(listViewLotElement);
			viewLotInfo = orderMap.get(orderMap.lastKey());
			
			return viewLotInfo;
		}
		else
		{
			viewLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
			viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
			viewLotInfo.setACTIONTYPE( Constant.EVENT_START );
			viewLotInfo.setSTATUS( Constant.OPERATIONSTATE_WAITING );
			
			listViewLotInfo = (List) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
			if ( listViewLotInfo.size() > 0 )
			{
				HashMap<Long, VIEWLOTTRACKING> listViewLotElement = new HashMap<Long, VIEWLOTTRACKING>();
				
				for ( int i = 0; i < listViewLotInfo.size(); i++ )
				{
					VIEWLOTTRACKING viewLotElement = (VIEWLOTTRACKING) listViewLotInfo.get(i);
					
					listViewLotElement.put(viewLotElement.getPROCESSSEQUENCE(), viewLotElement);
				}
				
				for ( Iterator iterator = new TreeMap(listViewLotElement).entrySet().iterator(); iterator.hasNext(); )
				{
					Map.Entry<Long, VIEWLOTTRACKING> iteratorMap = (Map.Entry<Long, VIEWLOTTRACKING>) iterator.next();
					viewLotInfo = iteratorMap.getValue();
					break;
				}
			}
			else
			{
				// 다음 진행 가능 공정이 존재하지 않습니다.
				return null;
			}
		}
		
		return viewLotInfo;
	}

	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 순선대로 확인
	 * 
	 * @param viewLotInfo
	 * @param txnInfo
	 * @return
	 * @throws CustomException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void checkGoInOrder(VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
		if ( Constant.OBJECTTYPE_OPERATION.equalsIgnoreCase(viewLotInfo.getINPUTMODE()) )
		{
//			if ( !Constant.CONSUMABLE_USER_EIS.equalsIgnoreCase(txnInfo.getWorkType()) && 
//					txnInfo.getWorkType() != null && !txnInfo.getWorkType().contains("ROUTING") )
//			{
//				checkHistoryTime(viewLotInfo, txnInfo);
//			}
			
			if ( Constant.EVENT_START.equalsIgnoreCase(viewLotInfo.getACTIONTYPE()) )
			{
				if ( Constant.FLAG_YN_NO.equalsIgnoreCase(viewLotInfo.getCURRENTFLAG()) )
				{
					// 요청된 공정({0})은 현재 진행순서가 아니기 때문에 공정 시작을 할 수 없습니다.
					throw new CustomException("LOT-004", new Object[] {viewLotInfo.getPROCESSNAME()});
				}
			}
			else
			{
				VIEWLOTTRACKING operationInfo = new VIEWLOTTRACKING();
				operationInfo.setKeyLOTID(viewLotInfo.getKeyLOTID());
				operationInfo.setPROCESSROUTEID(viewLotInfo.getPROCESSROUTEID());
				operationInfo.setPROCESSSEQUENCE(viewLotInfo.getPROCESSSEQUENCE());
				operationInfo.setROUTERELATIONSEQUENCE(viewLotInfo.getROUTERELATIONSEQUENCE());
				operationInfo.setSTATUS(Constant.OPERATIONSTATE_WAITING);

				List<Object> listViewLotInfo = (List<Object>) operationInfo.excuteDML(SqlConstant.DML_SELECTLIST);

				if ( listViewLotInfo.size() > 0 )
				{
					// 요청된 공정({0})에서 미진행 항목이 존재하여 종료 처리가 불가능 합니다.
					throw new CustomException("LOT-005", new Object[] {viewLotInfo.getPROCESSNAME()});
				}
			}
		}
		else if ( Constant.OBJECTTYPE_RECIPE.equalsIgnoreCase(viewLotInfo.getINPUTMODE()) )
		{
			VIEWLOTTRACKING recipeInfo = new VIEWLOTTRACKING();
			recipeInfo.setKeyLOTID(viewLotInfo.getKeyLOTID());
			recipeInfo.setPROCESSROUTEID(viewLotInfo.getPROCESSROUTEID());
			recipeInfo.setPROCESSSEQUENCE(viewLotInfo.getPROCESSSEQUENCE());
			recipeInfo.setROUTERELATIONSEQUENCE(viewLotInfo.getROUTERELATIONSEQUENCE());
			recipeInfo.setINPUTMODE(Constant.OBJECTTYPE_RECIPE);
			recipeInfo.setSTATUS(Constant.OPERATIONSTATE_WAITING);
			
			List<Object> listViewLotInfo = (List<Object>) recipeInfo.excuteDML(SqlConstant.DML_SELECTLIST);
			
			for ( int i = 0; i < listViewLotInfo.size(); i++ )
			{
				recipeInfo = (VIEWLOTTRACKING) listViewLotInfo.get(i);
				
				if ( !Constant.RECIPE_ID_ADDMATERIAL.equalsIgnoreCase(recipeInfo.getRECIPEID()) && 
						!Constant.RECIPE_ID_ADDMATERIAL.equalsIgnoreCase(viewLotInfo.getRECIPEID()) )
				{
					if ( Constant.RECIPE_ID_CONSUMABLE.equalsIgnoreCase(viewLotInfo.getRECIPETYPE()) )
					{
						if ( (recipeInfo.getORDERINDEX() != null) && (recipeInfo.getORDERINDEX() > 0) && 
							  viewLotInfo.getORDERINDEX() != null && 
						     (recipeInfo.getORDERINDEX() < viewLotInfo.getORDERINDEX()) )
						{
//							if ( !Constant.CONSUMABLE_USER_EIS.equalsIgnoreCase(viewLotInfo.getWORKTYPE()) )
//							{
//								// 이전 원료({0})에 대한 투입이 이루어지지 않아 처리가 불가능 합니다.
//								throw new CustomException("OM-087", new Object[] {recipeInfo.getRECIPEPARAMETERID()});
//							}
						}
					}
					else
					{
//						if ( !Constant.CONSUMABLE_USER_EIS.equalsIgnoreCase(txnInfo.getWorkType()) )
//						{
//							checkHistoryTime(viewLotInfo, txnInfo);
//						}
						
						// 공정이 시작중인지 여부 체크
						VIEWLOTTRACKING processInfo = 
								getProcessInfo(viewLotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getPROCESSID(), viewLotInfo.getPROCESSSEQUENCE(), viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_START);
						
						if ( Constant.OPERATIONSTATE_WAITING.equalsIgnoreCase(processInfo.getSTATUS()) )
						{
							// 요청된 공정({0}) 시작이 처리되지 않았습니다. 스텝에 대한 진행은 불가능합니다. 
							throw new CustomException("LOT-006", new Object[] {processInfo.getPROCESSNAME()});
						}
						
						if ( recipeInfo.getRECIPESEQUENCE() < viewLotInfo.getRECIPESEQUENCE() )
						{
							// 이전  스텝({0})에 대한 진행이 이루어지지 않아 처리가 불가능 합니다.
							throw new CustomException("LOT-007", new Object[] {recipeInfo.getRECIPENAME()});
						}
					}
				}
			}
		}
	}
	
	public void checkHistoryTime(VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
//		LOTPROCESSHISTORY history = new LOTPROCESSHISTORY();
//		history.setKeyLOTID(viewLotInfo.getKeyLOTID());
//		history.setCANCELFLAG(Constant.FLAG_YESNO_YES);
//		history.setEVENTNAME(Constant.EVENT_LOTSTART);
//		history.setEVENTTIME(txnInfo.getEventTime());
//		
//		String tName = history.getClass().getName().substring(history.getClass().getName().lastIndexOf(".")+1);
//		String sql = SqlQueryUtil.createSelectString(SqlConstant.DML_SELECTLIST, tName, history.getKEYMAP(), null);
//		sql = sql + " AND eventname != :EVENTNAME " +
//				"AND ( eventname LIKE 'Track%' OR eventname LIKE '%Start' OR eventname LIKE '%Change' OR eventname LIKE '%End' ) " +
//				"AND cancelflag != :CANCELFLAG AND eventtime > :EVENTTIME ";
//		List<LinkedCaseInsensitiveMap> listViewLotInfo = 
//			SqlMesTemplate.queryForList(sql, SqlQueryUtil.concatHashMap(history.getKEYMAP(), history.getDATAMAP()));
//
//		log.info("시간이 역전된 이력의 발생 개수 : " + listViewLotInfo.size());
//		
//		if ( listViewLotInfo.size() > 0 )
//		{
//			// 입력된 시간보다 늦은 시간에 이미 진행된 이벤트가 있습니다. 입력된 진행시간을 확인하세요!
//			throw new CustomException("OM-126", new Object[] {});
//		}
	}
	
	/**
	 * 받아온 정보를 VIEWLOTTRACKING 값 설정 후 조회하여 상태 Complete로 수정
	 * 
	 * @param lotInfo
	 * @param viewLotInfo
	 * @param txnInfo
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING repeatProcess(LOTINFORMATION lotInfo, VIEWLOTTRACKING viewLotInfo, TxnInfo txnInfo)
	{
		// Repeat 공정의 완료 처리
		VIEWLOTTRACKING repeatLotInfo = new VIEWLOTTRACKING();
		repeatLotInfo.setKeyLOTID( viewLotInfo.getKeyLOTID() );
		repeatLotInfo.setPROCESSROUTEID( viewLotInfo.getPROCESSROUTEID() );
		repeatLotInfo.setPROCESSID( viewLotInfo.getPROCESSID() );
		repeatLotInfo.setPROCESSSEQUENCE( viewLotInfo.getPROCESSSEQUENCE() );
		repeatLotInfo.setROUTERELATIONSEQUENCE( viewLotInfo.getROUTERELATIONSEQUENCE() );

		String suffix = " ORDER BY processPrintIndex, recipePrintIndex, orderIndex, timeKey ";
		
		List<Object> listRepeatLotInfo = (List<Object>) repeatLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);
		
		for ( int i = 0; i < listRepeatLotInfo.size(); i++ )
		{
			repeatLotInfo = (VIEWLOTTRACKING) listRepeatLotInfo.get(i);

			// 원료투입인 경우 미완료인 상태 유지 그외에 모든 공정진행에 대한 완료 처리 
			if ( !Constant.RECIPE_ID_FEEDINGMATERIAL.equalsIgnoreCase(repeatLotInfo.getRECIPEID()) )
			{
				repeatLotInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
				repeatLotInfo.setSTATUS( Constant.RECIPESTATE_COMPLETE );
				repeatLotInfo.excuteDML(SqlConstant.DML_UPDATE);
			}
		}

		// Measure 공정 조회
		VIEWLOTTRACKING measureViewLotInfo = getBeforeInspectionProcess(viewLotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getPROCESSSEQUENCE(), viewLotInfo.getROUTERELATIONSEQUENCE());

		if ( measureViewLotInfo != null )
		{
			// 중간검사 공정의 복사 추가
			measureViewLotInfo = copyProcess(viewLotInfo, measureViewLotInfo.getPROCESSID(), measureViewLotInfo.getPROCESSSEQUENCE(), 
					viewLotInfo.getPROCESSID(), viewLotInfo.getPROCESSSEQUENCE(), txnInfo);
			
			// 반복 공정의 복사 추가
			copyProcess(viewLotInfo, viewLotInfo.getPROCESSID(), viewLotInfo.getPROCESSSEQUENCE(), 
					measureViewLotInfo.getPROCESSID(), measureViewLotInfo.getPROCESSSEQUENCE(), txnInfo);
		}
		
		return null;
	}
	
	/**
	 * 받아온 데이터로 값 설정 후 조회하여 검사 전 공정 받아와 반환 
	 * 
	 * @param lotID
	 * @param processRouteID
	 * @param processSequence
	 * @param routeRelationSequence
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING getBeforeInspectionProcess(String lotID, String processRouteID, Long processSequence, Long routeRelationSequence)
	{
		VIEWLOTTRACKING viewLotInfo = new VIEWLOTTRACKING();
		viewLotInfo.setKeyLOTID( lotID );
		viewLotInfo.setPROCESSROUTEID( processRouteID );
		viewLotInfo.setROUTERELATIONSEQUENCE( routeRelationSequence );
		viewLotInfo.setINPUTMODE( Constant.OBJECTTYPE_OPERATION );
		viewLotInfo.setACTIONTYPE( Constant.EVENT_START );
		
		String suffix = " AND processSequence < " + String.valueOf(processSequence) + " ORDER BY processSequence DESC ";
		
		List<Object> listViewLotInfo = (List<Object>) viewLotInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);		
		
		if ( listViewLotInfo.size() > 0 )
		{
			viewLotInfo = (VIEWLOTTRACKING) listViewLotInfo.get(0);
			
			if ( !Constant.RECIPE_ID_MEASURE.equalsIgnoreCase(viewLotInfo.getPROCESSTYPE()) && 
					!Constant.OPERATIONSTATE_COMPLETE.equalsIgnoreCase(viewLotInfo.getSTATUS()) )
			{
				viewLotInfo = null;
			}
		}
		else
		{
			// 진행 가능 공정이 존재하지 않습니다.
			return null;
		}
		
		return viewLotInfo;
	}
	
	/**
	 * copyProcess 함수 호출해 공정 복사 처리
	 * 
	 * @param viewLotInfo
	 * @param originProcessID
	 * @param originProcessSeq
	 * @param txnInfo
	 * @return VIEWLOTTRACKING
	 * 
	 */
	public VIEWLOTTRACKING copyProcess(VIEWLOTTRACKING viewLotInfo, String originProcessID, Long originProcessSeq, TxnInfo txnInfo)
	{
		return copyProcess(viewLotInfo, originProcessID, originProcessSeq, originProcessID, originProcessSeq, txnInfo);
	}
	
	/**
	 * originProcessID의 공정을 조회하여 targetProcessID 공정으로 복사
	 * 
	 * @param viewLotInfo
	 * @param originProcessID
	 * @param originProcessSeq
	 * @param targetProcessID
	 * @param targetProcessSeq
	 * @param txnInfo
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING copyProcess(VIEWLOTTRACKING viewLotInfo, String originProcessID, Long originProcessSeq, 
			String targetProcessID, Long targetProcessSeq, TxnInfo txnInfo)
	{
		// 스텝 진행에 대한 RelationCode 재발번 관리
		HashMap<String, String> stepRelationCode = new HashMap<String, String>();

		// 해당 공정의 다음공정에 추가
		VIEWLOTTRACKING targetProcessInfo = 
				getProcessInfo(viewLotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), targetProcessID, targetProcessSeq, viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_END);
		
		// 복사할 공정의 조회
		VIEWLOTTRACKING originProcessInfo = new VIEWLOTTRACKING();
		originProcessInfo.setKeyLOTID( viewLotInfo.getKeyLOTID() );
		originProcessInfo.setPROCESSROUTEID( viewLotInfo.getPROCESSROUTEID() );
		originProcessInfo.setPROCESSID( originProcessID );
		originProcessInfo.setPROCESSSEQUENCE( originProcessSeq );
		originProcessInfo.setROUTERELATIONSEQUENCE( viewLotInfo.getROUTERELATIONSEQUENCE() );

		String suffix = " ORDER BY processPrintIndex, recipePrintIndex, orderIndex, timeKey ";

		List<Object> listOriginProcessInfo = (List<Object>) originProcessInfo.excuteDML(SqlConstant.DML_SELECTLIST, suffix);

		for ( int i = 0; i < listOriginProcessInfo.size(); i++ )
		{
			originProcessInfo = (VIEWLOTTRACKING) listOriginProcessInfo.get(i);

			// 원료투입인 경우 제외
			if ( Constant.RECIPE_ID_ABNORMALCONSUMABLE.equalsIgnoreCase(originProcessInfo.getRECIPEID()) || 
					Constant.RECIPE_ID_FEEDINGMATERIAL.equalsIgnoreCase(originProcessInfo.getRECIPEID()) )
			{
				continue;
			}
			
			// 추가 투입인 경우 재투입 가능하도록 설정
			if ( Constant.RECIPE_ID_ADDMATERIAL.equalsIgnoreCase(originProcessInfo.getRECIPEID()) && 
					originProcessInfo.getORDERINDEX().compareTo( (long)900 ) != 0 )
			{
				originProcessInfo.setORDERINDEX( (long)900 );
			}

			// 스텝 혹은 스텝 공정조건인 경우 연결코드
			if ( Constant.OBJECTTYPE_RECIPE.equalsIgnoreCase(originProcessInfo.getINPUTMODE()) || 
					Constant.OBJECTTYPE_RECIPEPARAMETER.equalsIgnoreCase(originProcessInfo.getINPUTMODE()) )
			{
				if ( Constant.OBJECTTYPE_RECIPE.equalsIgnoreCase(originProcessInfo.getINPUTMODE()) )
				{
					if ( Constant.EVENT_START.equalsIgnoreCase(originProcessInfo.getACTIONTYPE()) )
					{
						// Start 만 존재하는 경우 체크
						VIEWLOTTRACKING checkEndStep = new VIEWLOTTRACKING();
						checkEndStep.setKeyLOTID( originProcessInfo.getKeyLOTID() );
						checkEndStep.setPROCESSROUTEID( originProcessInfo.getPROCESSROUTEID() );
						checkEndStep.setPROCESSID( originProcessInfo.getPROCESSID() );
						checkEndStep.setPROCESSSEQUENCE( originProcessInfo.getPROCESSSEQUENCE() );
						checkEndStep.setROUTERELATIONSEQUENCE( originProcessInfo.getROUTERELATIONSEQUENCE() );
						checkEndStep.setRECIPEID( originProcessInfo.getRECIPEID() );
						checkEndStep.setRECIPERELATIONCODE( originProcessInfo.getRECIPERELATIONCODE() );
						checkEndStep.setACTIONTYPE( Constant.EVENT_END );
						List<Object> checkEndStepList = (List<Object>) checkEndStep.excuteDML(SqlConstant.DML_SELECTLIST);
						if ( checkEndStepList == null || checkEndStepList.size() < 1 )
						{
							// 종료가 공정내 존재하지 않는 경우 공정진행에 추가하지 않음.
							continue;
						}


						String addString = "01";
						if ( originProcessInfo.getRECIPERELATIONCODE().length() > 5 )
						{
							addString = originProcessInfo.getRECIPERELATIONCODE().substring(5);
							addString = ConvertUtil.Object2String(ConvertUtil.Object2Int(addString) + 1);

							if ( addString.length() == 1 )
							{
								addString = "0" + addString;
							}
						}
						else
						{
							// 기본값 "01" 사용
						}

						// 신규코드 추가
						stepRelationCode.put(originProcessInfo.getRECIPERELATIONCODE(), originProcessInfo.getRECIPERELATIONCODE().substring(0, 5) + addString);

						originProcessInfo.setRECIPERELATIONCODE(stepRelationCode.get(originProcessInfo.getRECIPERELATIONCODE()));
					}
					else if ( Constant.EVENT_CHANGE.equalsIgnoreCase(originProcessInfo.getACTIONTYPE()) || 
							Constant.EVENT_END.equalsIgnoreCase(originProcessInfo.getACTIONTYPE()) )
					{
						// 추가된 신규코드를 조회
						if ( stepRelationCode.get(originProcessInfo.getRECIPERELATIONCODE()) != null && 
								!stepRelationCode.get(originProcessInfo.getRECIPERELATIONCODE()).isEmpty() )
						{
							originProcessInfo.setRECIPERELATIONCODE(stepRelationCode.get(originProcessInfo.getRECIPERELATIONCODE()));
						}
						else
						{
							// 공정진행에 추가하지 않음.
							continue;
						}
					}
				}
				else if ( Constant.OBJECTTYPE_RECIPEPARAMETER.equalsIgnoreCase(originProcessInfo.getINPUTMODE()) )
				{
					// 추가된 신규코드를 조회
					if ( stepRelationCode.get(originProcessInfo.getRECIPERELATIONCODE()) != null && 
							!stepRelationCode.get(originProcessInfo.getRECIPERELATIONCODE()).isEmpty() )
					{
						originProcessInfo.setRECIPERELATIONCODE(stepRelationCode.get(originProcessInfo.getRECIPERELATIONCODE()));
					}
					else
					{
						// 공정진행에 추가하지 않음.
						continue;
					}
				}
			}

			// 공정 순번 및 스텝 순번 재구성
			if ( Constant.OBJECTTYPE_OPERATION.equalsIgnoreCase(originProcessInfo.getINPUTMODE()) 
					|| Constant.OBJECTTYPE_OPERATIONPARAMETER.equalsIgnoreCase(originProcessInfo.getINPUTMODE()) )
			{
				originProcessInfo.setPROCESSSEQUENCE(targetProcessSeq + 1);
			}
			else
			{
				originProcessInfo.setPROCESSSEQUENCE(targetProcessSeq + 1);
				originProcessInfo.setRECIPESEQUENCE(originProcessInfo.getRECIPESEQUENCE() + 1);
			}

			originProcessInfo.setKeyTIMEKEY(DateUtil.getCurrentEventTimeKey());
			originProcessInfo.setRELATIONTIMEKEY( "" );
			originProcessInfo.setRELATIONTIME( null );
			originProcessInfo.setPROCESSPRINTINDEX( ConvertUtil.doubleAdd(targetProcessInfo.getPROCESSPRINTINDEX(), 0.01) );
			originProcessInfo.setCURRENTFLAG( Constant.FLAG_YN_NO );
			originProcessInfo.setSTATUS( Constant.RECIPESTATE_WAITING );
			originProcessInfo.excuteDML(SqlConstant.DML_INSERT);
		}
		
		targetProcessInfo = 
				getProcessInfo(viewLotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), originProcessID, targetProcessSeq + 1, viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_END);
		
		return targetProcessInfo;
	}
	
	/**
	 * lotID로 LOTINFORMATION을 가져와 샘플링 공정인지 확인
	 * 
	 * @param lotID
	 * @param viewLotInfo
	 * @return VIEWLOTTRACKING
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VIEWLOTTRACKING checkSamplingProcess(String lotID, VIEWLOTTRACKING viewLotInfo)
	{
		boolean judgement = false;
		
		LOTINFORMATION lotInfo = new LOTINFORMATION();
		lotInfo.setKeyLOTID(lotID);
		lotInfo = (LOTINFORMATION) lotInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		PRODUCTDEFINITION productInfo = new PRODUCTDEFINITION();
		productInfo.setKeyPLANTID(lotInfo.getPLANTID());
		productInfo.setKeyPRODUCTID(lotInfo.getPRODUCTID());
		productInfo = (PRODUCTDEFINITION) productInfo.excuteDML(SqlConstant.DML_SELECTROW);
		
		SAMPLINGPOLICY samplingPolicy = getSamplingPolicy(productInfo.getKeyPLANTID(), productInfo.getKeyPRODUCTID(), Constant.SAMPLING_POLICYTYPE_PRODUCT, viewLotInfo);
		if ( samplingPolicy == null )
		{
			samplingPolicy = getSamplingPolicy(productInfo.getKeyPLANTID(), productInfo.getPRODUCTGROUPID(), Constant.SAMPLING_POLICYTYPE_PRODUCTGROUP, viewLotInfo);
		}
		
		if ( samplingPolicy != null )
		{
			SAMPLINGDEFINITION samplingRule = new SAMPLINGDEFINITION();
			samplingRule.setKeyPLANTID(lotInfo.getPLANTID());
			samplingRule.setKeySAMPLINGID(samplingPolicy.getSAMPLINGID());
			
			try
			{
				samplingRule = (SAMPLINGDEFINITION) samplingRule.excuteDML(SqlConstant.DML_SELECTROW);
			}
			catch (NoDataFoundException e)
			{
				// 기준정보 미등록 상태 - 기본상태 검사 진행
				judgement = true;
			}
			
			if ( !judgement )
			{
				if ( Constant.SAMPLING_ANYTHING.equalsIgnoreCase(samplingRule.getSAMPLINGCHECK()) )
				{
					SAMPLINGINFORMATION samplingInfo = getSamplingInfo(lotInfo, samplingPolicy, samplingRule, (long) 0);

					judgement = checkSamplingJudgement(samplingInfo, viewLotInfo);
				}
				else
				{
					String[] checkString = samplingRule.getSAMPLINGCHECK().split("\\,");
					for ( int i = 0; i < checkString.length; i++ )
					{
						Long checkValue = ConvertUtil.Object2Long(checkString[i]);

						SAMPLINGINFORMATION samplingInfo = getSamplingInfo(lotInfo, samplingPolicy, samplingRule, checkValue);

						if( judgement )
						{
							checkSamplingJudgement(samplingInfo, viewLotInfo);
						}
						else
						{
							judgement = checkSamplingJudgement(samplingInfo, viewLotInfo);
						}
					}
				}
			}
		}
		else
		{
			// 기준정보 미등록 상태 - 기본상태 검사 진행
			judgement = true;
		}
	
		if ( judgement )
		{
			// 샘플링 검사를 진행한다.
			return viewLotInfo;
		}
		else
		{
			// 공정을 Skip 하는 경우
			// Repeat 공정의 Complete 처리
			VIEWLOTTRACKING samplingLotInfo = new VIEWLOTTRACKING();
			samplingLotInfo.setKeyLOTID( viewLotInfo.getKeyLOTID() );
			samplingLotInfo.setPROCESSROUTEID( viewLotInfo.getPROCESSROUTEID() );
			samplingLotInfo.setPROCESSID( viewLotInfo.getPROCESSID() );
			samplingLotInfo.setPROCESSSEQUENCE( viewLotInfo.getPROCESSSEQUENCE() );
			samplingLotInfo.setROUTERELATIONSEQUENCE( viewLotInfo.getROUTERELATIONSEQUENCE() );

			List<Object> listSamplingLotInfo = (List<Object>) samplingLotInfo.excuteDML(SqlConstant.DML_SELECTLIST);
			for ( int i = 0; i < listSamplingLotInfo.size(); i++ )
			{
				samplingLotInfo = (VIEWLOTTRACKING) listSamplingLotInfo.get(i);

				samplingLotInfo.setSTATUS( Constant.RECIPESTATE_COMPLETE );
				samplingLotInfo.setEVENTUSERID( "SAMPLINGPROCESS" );
				
				// 2019.06.05 Sampling Skip 처리시 CurrentFlag = 'Y' 로 남는 버그 수정
				samplingLotInfo.setCURRENTFLAG(Constant.FLAG_YN_NO);
				
				samplingLotInfo.excuteDML(SqlConstant.DML_UPDATE);
			}

			VIEWLOTTRACKING nextViewLotInfo = 
					getNextProcess(lotInfo.getKeyLOTID(), viewLotInfo.getPROCESSROUTEID(), viewLotInfo.getROUTERELATIONSEQUENCE(), Constant.EVENT_START, viewLotInfo.getPROCESSSEQUENCE());
			
			return nextViewLotInfo;
		}
	}
	
	/**
	 * samplingInfo의 샘플링 방법에 따라 샘플링 판단해 반환 
	 * 
	 * @param samplingInfo
	 * @param viewLotInfo
	 * @return boolean
	 * 
	 */
	public boolean checkSamplingJudgement(SAMPLINGINFORMATION samplingInfo, VIEWLOTTRACKING viewLotInfo)
	{
		boolean judgement = false;
		
		if ( Constant.SAMPLING_METHOD_ODD.equalsIgnoreCase(samplingInfo.getSAMPLINGMETHOD()) )
		{
			String lastNumber = viewLotInfo.getKeyLOTID().substring(viewLotInfo.getKeyLOTID().length()-1);
			
			if ( ConvertUtil.Object2Long(lastNumber) == 0 )
			{
				// 짝수
			}
			else if( ConvertUtil.Object2Long(lastNumber)%2 == 0 )
			{
				// 짝수
			}
			else
			{
				// 홀수
				judgement = true;
			}
		}
		else if ( Constant.SAMPLING_METHOD_EVEN.equalsIgnoreCase(samplingInfo.getSAMPLINGMETHOD()) )
		{
			String lastNumber = viewLotInfo.getKeyLOTID().substring(viewLotInfo.getKeyLOTID().length()-1);
			
			if ( ConvertUtil.Object2Long(lastNumber) == 0 )
			{
				// 짝수
				judgement = true;
			}
			else if( ConvertUtil.Object2Long(lastNumber)%2 == 0 )
			{
				// 짝수
				judgement = true;
			}
			else
			{
				// 홀수
			}
		}
		else if ( Constant.SAMPLING_METHOD_LOTNUMBER.equalsIgnoreCase(samplingInfo.getSAMPLINGMETHOD()) )
		{
			String lastNumber = viewLotInfo.getKeyLOTID().substring(viewLotInfo.getKeyLOTID().length()-1);
			
			if( ConvertUtil.Object2Long(lastNumber).compareTo(samplingInfo.getKeyCHECKVALUE()) == 0 )
			{
				// 동일수
				judgement = true;
			}
		}
		else
		{
			// Anything 일 경우
			if ( samplingInfo.getKeyCHECKVALUE().compareTo(samplingInfo.getCHECKEDCOUNT()+1) <= 0 )
			{
				judgement = true;
				
				samplingInfo.setCHECKEDCOUNT((long) 0);
			}
			else
			{
				samplingInfo.setCHECKEDCOUNT( samplingInfo.getCHECKEDCOUNT()+1 );
			}
		}
		
		samplingInfo.setLASTCHECKLOTID(viewLotInfo.getKeyLOTID());
		samplingInfo.setLASTCHECKTIME(DateUtil.getCurrentTimestamp());
		samplingInfo.excuteDML(SqlConstant.DML_UPDATE);
		
		return judgement; 
	}
	
	/**
	 * 받아온 정보로 samplingPolicy 값 설정 후 검색된 Policy 에 대한 기준정보 반환
	 * 
	 * @param plantID
	 * @param policyCode
	 * @param policyType
	 * @param viewLotInfo
	 * @return SAMPLINGPOLICY
	 * 
	 */
	@SuppressWarnings("unchecked")
	public SAMPLINGPOLICY getSamplingPolicy(String plantID, String policyCode, String policyType, VIEWLOTTRACKING viewLotInfo)
	{
		SAMPLINGPOLICY samplingPolicy = new SAMPLINGPOLICY();
		samplingPolicy.setKeyPLANTID(plantID);
		samplingPolicy.setKeyPOLICYCODE(policyCode);
		samplingPolicy.setPOLICYTYPE(policyType);
		List<Object> listPolicy = (List<Object>) samplingPolicy.excuteDML(SqlConstant.DML_SELECTLIST);
		
		if ( listPolicy.size() > 0 )
		{
			TreeMap<Integer, SAMPLINGPOLICY> mapSamplingInfo = new TreeMap<Integer, SAMPLINGPOLICY>();
			
			for ( int i = 0; i < listPolicy.size(); i++ )
			{
				SAMPLINGPOLICY instantSamplingPolicy = (SAMPLINGPOLICY) listPolicy.get(i);
				
				if ( instantSamplingPolicy.getKeyPROCESSID().equalsIgnoreCase(viewLotInfo.getPROCESSID()) && 
						instantSamplingPolicy.getKeyEQUIPMENTID().equalsIgnoreCase(viewLotInfo.getEQUIPMENTID()) )
				{
					// 공정 코드와 설비 코드로 등록된 기준정보 1순위
					mapSamplingInfo.put(1, instantSamplingPolicy);
					break;
				}
				else if ( instantSamplingPolicy.getKeyPROCESSID().equalsIgnoreCase(viewLotInfo.getPROCESSID()) && 
						instantSamplingPolicy.getKeyEQUIPMENTID().equalsIgnoreCase(Constant.SAMPLING_ANYTHING) )
				{
					// 2순위
					mapSamplingInfo.put(2, instantSamplingPolicy);
				}
				else if ( instantSamplingPolicy.getKeyPROCESSID().equalsIgnoreCase(Constant.SAMPLING_ANYTHING) && 
						instantSamplingPolicy.getKeyEQUIPMENTID().equalsIgnoreCase(viewLotInfo.getEQUIPMENTID()) )
				{
					// 3순위
					mapSamplingInfo.put(3, instantSamplingPolicy);
				} 
				else if ( instantSamplingPolicy.getKeyPROCESSID().equalsIgnoreCase(Constant.SAMPLING_ANYTHING) && 
						instantSamplingPolicy.getKeyEQUIPMENTID().equalsIgnoreCase(Constant.SAMPLING_ANYTHING) )
				{
					// 4순위
					mapSamplingInfo.put(4, instantSamplingPolicy);
				}
			}
			
			// 검색된 Policy 에 대한 기준정보
			if ( mapSamplingInfo != null && !mapSamplingInfo.isEmpty() )
			{
				samplingPolicy = mapSamplingInfo.get(mapSamplingInfo.firstKey());
				
				return samplingPolicy;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * 샘플링 정보 수정/추가
	 * 
	 * @param lotInfo
	 * @param samplingPolicy
	 * @param samplingRule
	 * @param checkValue
	 * @return SAMPLINGINFORMATION
	 * @throws NoDataFoundException
	 * 
	 */
	public SAMPLINGINFORMATION getSamplingInfo(LOTINFORMATION lotInfo, SAMPLINGPOLICY samplingPolicy, SAMPLINGDEFINITION samplingRule, Long checkValue)
	{
		SAMPLINGINFORMATION samplingInfo = new SAMPLINGINFORMATION();
		samplingInfo.setKeySAMPLINGID(samplingPolicy.getSAMPLINGID());
		samplingInfo.setKeyCHECKVALUE(checkValue);
		
		try
		{
			samplingInfo = (SAMPLINGINFORMATION) samplingInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
		}
		catch ( NoDataFoundException e )
		{
			samplingInfo.setPOLICYTYPE(samplingPolicy.getPOLICYTYPE());
			samplingInfo.setPOLICYCODE(samplingPolicy.getKeyPOLICYCODE());
			samplingInfo.setPROCESSID(samplingPolicy.getKeyPROCESSID());
			samplingInfo.setEQUIPMENTID(samplingPolicy.getKeyEQUIPMENTID());
			samplingInfo.setSAMPLINGTYPE(samplingRule.getSAMPLINGTYPE());
			samplingInfo.setSAMPLINGMETHOD(samplingRule.getSAMPLINGMETHOD());
			
			samplingInfo.setCHECKEDCOUNT((long) 0);
			samplingInfo.setLASTCHECKLOTID(lotInfo.getKeyLOTID());
			samplingInfo.setLASTCHECKTIME(DateUtil.getCurrentTimestamp());

			samplingInfo.excuteDML(SqlConstant.DML_INSERT);
		}
		
		return samplingInfo;
	}
}