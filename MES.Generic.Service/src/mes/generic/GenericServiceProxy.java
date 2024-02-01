package mes.generic;

import mes.constant.PlantConstant;
import mes.errorHandler.CustomExceptionMap;
import mes.event.MessageAdaptor;
import mes.event.MessageEventMap;
import mes.event.TargetPlantMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class GenericServiceProxy {
	
	private static MessageAdaptor messageAdaptor;
	private static CustomExceptionMap customExceptionMap;
	private static MessageEventMap messageEventMap;
	private static PlantConstant plantConstant;
	private static TargetPlantMap targetPlantMap;
	
	/**
	 * 
	 * messageAdaptor를 불러옵니다
	 *
	 * @param
	 * @return MessageAdaptor
	 *
	 */
	public static MessageAdaptor getMessageAdaptor()
	{
		return GenericServiceProxy.messageAdaptor;
	}
	/**
	 * 
	 * messageAdaptor를 세팅합니다
	 *
	 * @param messageAdaptor
	 * @return
	 *
	 */
	public void setMessageAdaptor(MessageAdaptor messageAdaptor)
	{
		GenericServiceProxy.messageAdaptor = messageAdaptor;
	}
	/**
	 * 
	 * customExceptionMap을 불러옵니다
	 *
	 * @param 
	 * @return CustomExceptionMap
	 *
	 */
	public static CustomExceptionMap getCustomExceptionMap()
	{
		return customExceptionMap;
	}
	/**
	 * 
	 * customExceptionMap을 세팅합니다
	 *
	 * @param customExceptionMap
	 * @return 
	 *
	 */
	public void setCustomExceptionMap(CustomExceptionMap customExceptionMap)
	{
		GenericServiceProxy.customExceptionMap = customExceptionMap;
	}
	/**
	 * 
	 * messageEventMap을 불러옵니다
	 *
	 * @param 
	 * @return MessageEventMap
	 *
	 */
	public static MessageEventMap getMessageEventMap()
	{
		return messageEventMap;
	}
	/**
	 * 
	 * messageEventMap을 세팅합니다
	 *
	 * @param messageEventMap
	 * @return 
	 *
	 */
	public void setMessageEventMap(MessageEventMap messageEventMap)
	{
		GenericServiceProxy.messageEventMap = messageEventMap;
	}
	/**
	 * 
	 * plantConstant를 불러옵니다
	 *
	 * @param 
	 * @return returnType
	 *
	 */
	public static PlantConstant getPlantConstant()
	{
		return plantConstant;
	}
	/**
	 * 
	 * plantConstant을 세팅합니다
	 *
	 * @param plantConstant
	 * @return 
	 *
	 */
	public void setPlantConstant(PlantConstant plantConstant)
	{
		GenericServiceProxy.plantConstant = plantConstant;
	}
	/**
	 * 
	 * targetPlantMap을 불러옵니다
	 *
	 * @param 
	 * @return TargetPlantMap
	 *
	 */
	public static TargetPlantMap getTargetPlantMap()
	{
		return GenericServiceProxy.targetPlantMap;
	}
	/**
	 * 
	 * targetPlantMap을 세팅합니다
	 *
	 * @param targetPlantMap
	 * @return 
	 *
	 */
	public void setTargetPlantMap(TargetPlantMap targetPlantMap)
	{
		GenericServiceProxy.targetPlantMap = targetPlantMap;
	}
}

