package mes.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.util.DateUtil;

import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TimeCalcUtil
{

	/**
	 * 
	 * 현재 시각에 해당하는 공장일(형식:yyyyMMdd) 구하기
	 * 
	 * @param plantID - 공장코드
	 * @return String - yyyymmdd 형식의 공장일
	 * 
	 */
	public static String getCurrentPlantDate(String plantID)
	{

		return TimeCalcUtil.getPlantDate(plantID, DateUtil.getCurrentTimestamp());
	}

	/**
	 * 
	 * 특정 시각에 해당하는 공장일(형식:yyyyMMdd) 구하기
	 * 
	 * @param plantID - 공장코드
	 * @param timestamp - 특정 시각
	 * @return String - yyyymmdd 형식의 공장일
	 * 
	 */
	public static String getPlantDate(String plantID, Timestamp timestamp)
	{

		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_SIMPLE_DAY);
		Timestamp factoryEndTime = TimeCalcUtil.addHourAtTimestamp(timestamp, -8);
		return sdf.format(factoryEndTime);
	}

	/**
	 * 
	 * 해당 공장의 특정일에 해당하는 공장 시작일시를 TimeStamp 형태로 구하기
	 * 
	 * @param plantID - 공장 코드
	 * @param plantDate - yyyyMMdd Type 의 공장 일
	 * @return Timestamp - 공장 시작일시 
	 * 
	 */
	public static Timestamp getFactoryStartTime(String plantID, String plantDate)
	{
		// yyyyMMddHHmmss
		return DateUtil.getTimestamp(plantDate + "080000");
	}

	/**
	 * 
	 * 해당 공장의 특정일에 해당하는 공장 종료일시를 TimeStamp 형태로 구하기
	 * 
	 * @param plantID - 공장 코드
	 * @param plantDate - yyyyMMdd Type 의 공장 일
	 * @return Timestamp - 공장 종료일시 
	 * 
	 */
	public static Timestamp getFactoryEndTime(String plantID, String plantDate)
	{
		// yyyyMMddHHmmss
		return TimeCalcUtil.addHourAtTimestamp(DateUtil.getTimestamp(plantDate + "075959"), 24);
	}

	/**
	 * 
	 * 하루의 종료일시를 TimeStamp 형태로 구하기
	 * 
	 * @param date - yyyyMMdd Type 의 일자
	 * @return 종료일시 Timestamp
	 * 
	 */
	public static Timestamp getEndTime(String date)
	{
		// yyyyMMddHHmmss
		return DateUtil.getTimestamp(date + "235959");
	}

	/**
	 * 
	 * 현재 시각에 해당하는 yyyyMMdd 형식의 일자 구하기
	 * 
	 * @return String
	 */
	public static String getCurrentDate()
	{

		String date = DateUtil.getCurrentTime(DateUtil.FORMAT_SIMPLE_DAY);
		return date;
	}
	
	/**
	 * 
	 * 특정 월의 다음 월 구하기 yyyyMM
	 * 
	 * @param month - 특정 월 YYYYMM
	 * @return String
	 * 
	 */
	public static String getNextMonth(String month)
	{
		if ( month.length() != 6 )
		{
			// 인자값이 잘못되었습니다.
		}
		
		long yyyy = Long.valueOf(month.substring(0, 4));
		long mm = Long.valueOf(month.substring(4, 6));
		
		if ( mm + 1 < 10 )
		{
			return String.valueOf(yyyy) + "0" + String.valueOf(mm + 1);
		}
		else if ( mm + 1 < 13 )
		{
			return String.valueOf(yyyy) + String.valueOf(mm + 1);
		}
		else
		{
			return String.valueOf(yyyy + 1) + "01";
		}
	}
	
	/**
	 * 
	 * 특정 인자의 월차이 구하기 yyyyMM
	 * 
	 * @param fromMonth
	 * @param toMonth
	 * @return long
	 * 
	 */
	public static long getSubtractMonth(String fromMonth, String toMonth)
	{
		long diffMonth = 0;
		
		if ( fromMonth.length() != 6 || toMonth.length() != 6 )
		{
			// 인자값이 잘못되었습니다.
		}
		
		long fromYY = Long.valueOf(fromMonth.substring(0, 4));
		long fromMM = Long.valueOf(fromMonth.substring(4, 6));
		
		long toYY = Long.valueOf(toMonth.substring(0, 4));
		long toMM = Long.valueOf(toMonth.substring(4, 6));
		
		if ( fromYY != toYY )
		{
			diffMonth = (toYY - fromYY) * 12;
		}
		
		diffMonth = diffMonth + (toMM - fromMM);
		
		return diffMonth;
	}

	/**
	 * 
	 * 특정 시각에 해당하는 yyyyMMdd 형식의 일자 구하기
	 * 
	 * @param timestamp - 특정 시각
	 * @return String
	 * 
	 */
	public static String getDate(Timestamp timestamp)
	{

		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_SIMPLE_DAY);
		return sdf.format(timestamp);
	}

	/**
	 * 
	 * 특정 시각에 해당하는 hh24miss(HHmmss) 형식의 시각 구하기
	 * 
	 * @param timestamp - 특정 시각
	 * @return String
	 * 
	 */
	public static String getTime(Timestamp timestamp)
	{

		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		return sdf.format(timestamp);
	}

	/**
	 * 
	 * 시간을 Timestamp의 Time 형태로 변환
	 * 
	 * @param hour - 시간
	 * @return long
	 * 
	 */
	public static long getTimeOfHour(long hour)
	{
		return 1000 * 60 * 60 * hour;
	}

	/**
	 * 
	 * 일자를 Timestamp의 Time 형태로 변환
	 * 
	 * @param date - 시간
	 * @return long
	 * 
	 */
	public static long getTimeOfDate(long date)
	{
		return 1000 * 60 * 60 * 24 * date;
	}

	/**
	 * 
	 * 분을 Timestamp의 Time 형태로 변환
	 * 
	 * @param minute - 분
	 * @return long
	 * 
	 */
	public static long getTimeOfMinute(long minute)
	{
		return 1000 * 60 * minute;
	}

	/**
	 * 
	 * 초을 Timestamp의 Time 형태로 변환
	 * 
	 * @param second - 초
	 * @return long
	 * 
	 */
	public static long getTimeOfSecond(long second)
	{
		return 1000 * second;
	}

	/**
	 * 주어진 시각에 특정 시간을 더하여 반환
	 * 
	 * @param sourceTime
	 *            주어진 시각
	 * @param hour
	 *            더하는 시간
	 * @return 더한 시각
	 */
	public static Timestamp addHourAtTimestamp(Timestamp sourceTime, long hour)
	{
		Timestamp time = (Timestamp) sourceTime.clone();
		time.setTime(time.getTime() + TimeCalcUtil.getTimeOfHour(hour));

		return time;
	}

	/**
	 * 
	 * 주어진 시각에 특정 일자를 더하여 반환
	 * 
	 * @param sourceTime - 주어진 시각
	 * @param date - 더하는 일자
	 * @return Timestamp 더한 시각
	 */
	public static Timestamp addDateAtTimestamp(Timestamp sourceTime, long date)
	{
		Timestamp time = (Timestamp) sourceTime.clone();
		time.setTime(time.getTime() + TimeCalcUtil.getTimeOfDate(date));

		return time;
	}

	/**
	 * 
	 * 주어진 시각에 특정 분을 더하여 반환
	 * 
	 * @param sourceTime - 주어진 시각
	 * @param minute - 더하는 분
	 * @return Timestamp 더한 시각
	 */
	public static Timestamp addMinuteAtTimestamp(Timestamp sourceTime, long minute)
	{
		Timestamp time = (Timestamp) sourceTime.clone();
		time.setTime(time.getTime() + TimeCalcUtil.getTimeOfMinute(minute));

		return time;
	}

	/**
	 * 
	 * 주어진 시각에 특정 초를 더하여 반환
	 * 
	 * @param sourceTime 주어진 시각
	 * @param second 더하는 초
	 * @return Timestamp 더한 시각
	 */
	public static Timestamp addSecondAtTimestamp(Timestamp sourceTime, long second)
	{
		Timestamp time = (Timestamp) sourceTime.clone();
		time.setTime(time.getTime() + TimeCalcUtil.getTimeOfSecond(second));

		return time;
	}

	/**
	 * 
	 * 두 일시의 차이를 일자(date)로 구하기
	 * 
	 * @param minuend - 피감수
	 * @param subtrahend - 감수
	 * @return long - 차이 일수
	 * 
	 */
	public static long subtractTimestampInDate(Timestamp minuend, Timestamp subtrahend)
	{
		long subtractTime = minuend.getTime() - subtrahend.getTime();
		return subtractTime / (1000 * 60 * 60 * 24);
	}

	/**
	 * 
	 * 두 일시의 차이를 시(hour)로 구하기
	 * 
	 * @param minuend - 피감수
	 * @param subtrahend - 감수
	 * @return long - 차이 시
	 * 
	 */
	public static long subtractTimestampInHour(Timestamp minuend, Timestamp subtrahend)
	{
		long subtractTime = minuend.getTime() - subtrahend.getTime();
		return subtractTime / (1000 * 60 * 60);
	}

	/**
	 * 
	 * 두 일시의 차이를 분(min)으로 구하기
	 * 
	 * @param minuend - 피감수
	 * @param subtrahend - 감수
	 * @return long - 차이 분
	 * 
	 */
	public static long subtractTimestampInMinute(Timestamp minuend, Timestamp subtrahend)
	{
		long subtractTime = minuend.getTime() - subtrahend.getTime();
		return subtractTime / (1000 * 60);
	}

	/**
	 * 
	 * 두 일시의 차이를 초(sec)로 구하기
	 * 
	 * @param minuend - 피감수
	 * @param subtrahend - 감수
	 * @return long - 차이 초
	 * 
	 */
	public static long subtractTimestampInSec(Timestamp minuend, Timestamp subtrahend)
	{
		long subtractTime = minuend.getTime() - subtrahend.getTime();
		return subtractTime / 1000;
	}
	/**
	 * 
	 * db에서 현재시간을 불러옵니다
	 *
	 * @param 
	 * @return Object
	 * @throws Exception
	 *
	 */
	@SuppressWarnings("rawtypes")
	public static Object getDBSysdate()
	{
		Object tCurrentTime = DateUtil.getCurrentTimestamp();
		
		try
		{
			String sSql = " SELECT 	SYSDATE AS CURRENTTIME FROM DUAL  "; 
			HashMap bindMap = new HashMap();
			List oSelect = (List) SqlMesTemplate.queryForList(sSql, bindMap);
			if ( (oSelect != null ) && (oSelect.size() > 0) )
			{
				LinkedCaseInsensitiveMap oResult = (LinkedCaseInsensitiveMap) oSelect.get(0);
				tCurrentTime = oResult.get("CURRENTTIME");
			}
		}
		catch ( Exception e )
		{
			tCurrentTime = DateUtil.getCurrentTimestamp();
		}
		return tCurrentTime;
	}
	
	
}

