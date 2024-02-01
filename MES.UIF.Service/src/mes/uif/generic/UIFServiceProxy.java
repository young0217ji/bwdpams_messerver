package mes.uif.generic;

import mes.uif.biz.TargetSubjectMap;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class UIFServiceProxy {
	
	private static TargetSubjectMap targetSubjectMap;
	
	public static TargetSubjectMap getTargetSubjectMap()
	{
		return UIFServiceProxy.targetSubjectMap;
	}

	public void setTargetSubjectMap(TargetSubjectMap targetSubjectMap)
	{
		UIFServiceProxy.targetSubjectMap = targetSubjectMap;
	}
}
