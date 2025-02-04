package mes.errorHandler;

import java.text.MessageFormat;

import kr.co.mesframe.exception.MESFrameException;
import kr.co.mesframe.txninfo.TxnInfo;
import mes.generic.GenericServiceProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
@SuppressWarnings("serial")
public class CustomException extends MESFrameException
{
	private static final transient Logger logger = LoggerFactory.getLogger(CustomException.class);
	
	public CustomExceptionDef	customExceptionDef;
	public String exceptionMessage = null;

	public CustomException()
	{
	}

	/**
	 * 
	 * args 를 배열로 받아 Message를 구성할 때 배열 순서대로 삽입하여 구성
	 * 
	 * @param exceptionID
	 * @param args
	 * @return
	 * 
	 */
	public CustomException(String exceptionID, Object... args)
	{
		String exceptionMsg = null;
		customExceptionDef = GenericServiceProxy.getCustomExceptionMap().getExceptionMessageDef(exceptionID);
		exceptionMsg = this.customExceptionDef.getExceptionMessage();
		exceptionMessage = MessageFormat.format(exceptionMsg, args);
		logger.info(exceptionMessage);
	}
	
	/**
	 * 
	 * args 를 배열로 받아 Message를 구성할 때 배열 순서대로 삽입하여 구성
	 * 
	 * @param errorCode
	 * @param TxnInfo txnInfo
	 * @param args
	 * @return 
	 * 
	 */
	public CustomException(String exceptionID, TxnInfo txnInfo, Object... args)
	{
		String exceptionMsg = null;
		customExceptionDef = GenericServiceProxy.getCustomExceptionMap().getExceptionMessageDef(exceptionID, txnInfo.getLanguage());
		exceptionMsg = this.customExceptionDef.getExceptionMessage();
		exceptionMessage = MessageFormat.format(exceptionMsg, args);
		logger.info(exceptionMessage);
	}
	
	/**
	 * 
	 * args 를 배열로 받아 Message를 구성할 때 배열 순서대로 삽입하여 구성
	 * 
	 * @param errorCode
	 * @param Location Language
	 * @param args
	 * @return
	 * 
	 */
//	public CustomException(String exceptionID, String localeID, Object... args)
//	{
//		String exceptionMsg = null;
//		customExceptionDef = GenericServiceProxy.getCustomExceptionMap().getCustomExceptionDef(exceptionID, localeID);
//		exceptionMsg = GenericServiceProxy.getCustomExceptionMap().getExceptionMessage(exceptionID, localeID);
//		exceptionMessage = MessageFormat.format(exceptionMsg, args);
//		logger.info(exceptionMessage);
//	}

	/**
	 * 
	 * message를 출력하는 CustomException을 발생시킵니다
	 * 
	 * @param message
	 * @return
	 * 
	 */
	public CustomException(String message)
	{
		super(message);
		exceptionMessage = message;
	}

	/**
	 * 
	 * Throwable변수 cause를 받아 CumstomException을 발생시킵니다
	 * 
	 * @param cause
	 * @return
	 * 
	 */
	public CustomException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * 
	 * Throwable변수 cause를 받아 message를 출력하는 CustomException을 발생시킵니다
	 * 
	 * @param message
	 * @param cause
	 * @return
	 * 
	 */
	public CustomException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * 
	 * exceptionMessage를 출력합니다
	 * 
	 * @param
	 * @return String
	 *
	 */
	public String getCustomExceptionMessage()
	{
		if ( this.customExceptionDef == null || this.customExceptionDef.getExceptionID() == null ) {
			return "9999;" + this.exceptionMessage;
		}
		else {
			return this.customExceptionDef.getExceptionID() + ";" + this.exceptionMessage;
		}
	}
	
	/**
	 * 
	 * exceptionType을 리턴합니다
	 *
	 * @param parameter
	 * @return String
	 *
	 */
	public String getExceptionType()
	{
		if ( this.customExceptionDef == null || this.customExceptionDef.getExceptionType() == null ) {
			return "Info";
		}
		else {
			return this.customExceptionDef.getExceptionType();			
		}
	}

	/**
	 * exceptionMessage를 리턴합니다
	 * 
	 * @param
	 * @return String
	 */
	@Override
	public String getMessage()
	{
		return exceptionMessage;
	}
}

