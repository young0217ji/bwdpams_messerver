package mes.util;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mes.generic.GenericServiceProxy;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class MomentsUtil
{
	public String message = null;
	
	private static final transient Logger logger = LoggerFactory.getLogger(MomentsUtil.class);

	/**
	 * 
	 * code 안의 Element를 검색하여 message출력
	 *
	 * @param code
	 * @param args
	 * @return String
	 *
	 */
	public String getMessage(String code, Object... args)
	{
		String tempMsg = GenericServiceProxy.getCustomExceptionMap().getExceptionMessageDef(code).getExceptionMessage();
		message = MessageFormat.format(tempMsg, args);
		logger.info(message);
		
		return message;
	}
}

