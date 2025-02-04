package mes.errorHandler;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class CustomExceptionDef
{
	private String exceptionID;
	private String localeID;
	private String exceptionMessage;
	private String exceptionType;
	
	/**
	 * 
	 * exceptionID를 불러옵니다
	 *
	 * @param
	 * @return String
	 *
	 */
	public String getExceptionID()
	{
		return exceptionID;
	}
	/**
	 * 
	 * exceptionID를 세팅합니다
	 *
	 * @param exceptionID
	 * @return
	 *
	 */
	public void setExceptionID(String exceptionID)
	{
		this.exceptionID = exceptionID;
	}
	/**
	 * 
	 * localeID를 불러옵니다
	 *
	 * @param 
	 * @return String
	 *
	 */
	public String getLocaleID()
	{
		return localeID;
	}
	/**
	 * 
	 * localeID를 세팅합니다
	 *
	 * @param localeID
	 * @return
	 *
	 */
	public void setLocaleID(String localeID)
	{
		this.localeID = localeID;
	}
	/**
	 * 
	 * exceptionMessage를 불러옵니다
	 *
	 * @param
	 * @return String
	 *
	 */
	public String getExceptionMessage()
	{
		return exceptionMessage;
	}
	/**
	 * 
	 * exceptionMessage를 세팅합니다
	 *
	 * @param exceptionMessage
	 * @return
	 *
	 */
	public void setExceptionMessage(String exceptionMessage)
	{
		this.exceptionMessage = exceptionMessage;
	}
	/**
	 * 
	 * exceptionType을 불러옵니다
	 *
	 * @param
	 * @return String
	 *
	 */
	public String getExceptionType()
	{
		return exceptionType;
	}
	/**
	 * 
	 * exceptionType을 세팅합니다
	 *
	 * @param exceptionType
	 * @return
	 *
	 */
	public void setExceptionType(String exceptionType)
	{
		this.exceptionType = exceptionType;
	}
}

