package mes.util;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.mesframe.util.ConvertUtil;
import mes.errorHandler.CustomException;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class ValidationUtil
{
	private static final transient Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

	/**
	 * 필수 입력 항목이 null일 때 validation
	 * 
	 * @param hashMap
	 * @param arg
	 * @return
	 * @throws CustomException
	 *
	 */
	@SuppressWarnings("rawtypes")
	public void checkKeyNull(HashMap hashMap , Object... arg)
	{
		for ( int i = 0; i < arg.length; i++ )
		{
			if ( hashMap.get(arg[i]) == null || hashMap.get(arg[i]) == "" )
			{
				// 필수 입력 항목 ({0}) 의 입력된 값이 없습니다.
				throw new CustomException("CHECK-001", arg[i]);
			}
		}
	}
	
	/**
	 * checkListNull 함수 호출
	 * 
	 * @param list
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public void checkListNull(List list)
	{
		checkListNull(list, "");
	}
	
	/**
	 * list가 null일 때 validation
	 * 
	 * @param list
	 * @param listName
	 * @return
	 * @throws CustomException
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public void checkListNull(List list, String listName)
	{
		if ( list == null || list.isEmpty() )
		{
			// 필수 입력 항목에 대한 정의가 약속 되지 않았습니다. DATALIST ID = "{0}" 
			throw new CustomException("CHECK-002", new Object[] {listName});
		}
	}
	
	/**
	 * number가 null일 때 validation
	 * 
	 * @param number
	 * @return Object[]
	 * 
	 */
	public Object[] checkNumber(Object... number)
	{
		for ( int i = 0; i < number.length; i++ )
		{
			if ( number[i] == null || number[i] == "" )
			{
				number[i] = 0;
			}
		}
		
		return number;
	}
	
	/**
	 * 받아온 데이터가 범위에 적합하지 않을 때 validation
	 * 
	 * @param argName
	 * @param lowerQty
	 * @param upperQty
	 * @param currentQty
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void validationNumber(String argName, Object lowerQty, Object upperQty, String currentQty)
	{
		Double qty = ConvertUtil.String2Double(currentQty, 0.0);
		
		if ( lowerQty != null && upperQty != null )
		{
			double lower = ConvertUtil.Object2Double4Zero(lowerQty);
			double upper = ConvertUtil.Object2Double4Zero(upperQty);
			
			if ( qty < lower || upper < qty )
			{
				// ({0}) 입력된 값이 최소값 : ({1}), 최대값 : ({2}) 의 범위에 적합하지 않습니다.
				throw new CustomException("CHECK-003", new Object[] {argName, lower, upper});
			}
		}
		else if ( lowerQty == null && upperQty != null )
		{
			double upper = ConvertUtil.Object2Double4Zero(upperQty);
			
			if ( upper < qty )
			{
				// ({0}) 입력된 값이 최대값 : ({1}) 의 범위에 적합하지 않습니다.
				throw new CustomException("CHECK-004", new Object[] {argName, upper});
			}
		}
		else if ( upperQty == null && lowerQty != null )
		{
			double lower = ConvertUtil.Object2Double4Zero(lowerQty);
			
			if ( qty < lower )
			{
				// ({0}) 입력된 값이 최소값 : ({1}) 의 범위에 적합하지 않습니다.
				throw new CustomException("CHECK-005", new Object[] {argName, lower});
			}
		}
	}
	
	/**
	 * 받아온 이벤트가 다음 상태에서 처리할 수 없을 때 validation
	 * 
	 * @param eventName
	 * @param checkState
	 * @param currentState
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void validationState(String eventName, String checkState, String currentState)
	{
		if ( !checkState.equalsIgnoreCase(currentState) )
		{
			// 현재 진행 이벤트 ({0}) 는 다음 상태 ({1}) 에서 처리 될 수 없습니다.
			throw new CustomException("LOT-002", new Object[] {eventName, currentState});
		}
	}
	
	/**
	 * 받아온 이벤트가 다음 상태에서 처리할 수 없을 때 validation
	 * 
	 * @param eventName
	 * @param checkState
	 * @param currentState
	 * @return
	 * @throws CustomException
	 * 
	 */
	public void validationNotState(String eventName, String checkState, String currentState)
	{
		if ( checkState.equalsIgnoreCase(currentState) )
		{
			// 현재 진행 이벤트 ({0}) 는 다음 상태 ({1}) 에서 처리 될 수 없습니다.
			throw new CustomException("LOT-002", new Object[] {eventName, currentState});
		}
	}
}
