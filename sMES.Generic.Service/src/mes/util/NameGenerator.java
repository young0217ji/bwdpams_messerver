package mes.util;

import java.util.ArrayList;
import java.util.List;

import kr.co.mesframe.MESFrameProxy;
import kr.co.mesframe.util.ConvertUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class NameGenerator
{
	/**
	 * 
	 * ID가 plantID인 공장의 이름생성 규칙을 불러옵니다
	 *
	 * @param plantID
	 * @param ruleName
	 * @Object args
	 * @return String
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String nameGenerate(String plantID, String ruleName, Object... args)
	{
		List nameArgs = new ArrayList();
		for ( int i = 0; i < args.length; i++ )
		{
			nameArgs.add(args[i]);
		}

		List nameList = 
				MESFrameProxy.getIdClassService().createID(plantID, ruleName, 1, nameArgs);

		return (String) nameList.get(0);
	}
	/**
	 * 
	 * plantID 공장의 warehouseID 창고에 materialType을 구분하여
	 * ID를 규칙에 맞게 생성합니다
	 *
	 * @param plantID
	 * @param materialType
	 * @param warehouseID
	 * @return String
	 *
	 */
	public String MaterialLotIDGenerate(String plantID, String materialType, String warehouseID )
	{
		// LotID 생성을 위한 argument 설정.
		String materialTypeCode;
		if( materialType.equals("Consumable"))
		{
			materialTypeCode = "C";
		}
		else if( materialType.equals("SparePart"))
		{
			materialTypeCode = "S";
		}
		else if( materialType.equals("Durable"))
		{
			materialTypeCode = "D";
		}
		else if( materialType.equals("Product"))
		{
			materialTypeCode = "P";
		}
		else
		{
			materialTypeCode = "X";
		}
		String warehouseCode = warehouseID.substring(0,2);
		warehouseCode += warehouseID.substring(warehouseID.length() - 1, warehouseID.length());
		
		return nameGenerate( plantID, "MaterialLotID", new Object[] { materialTypeCode, warehouseCode });
	}
	/**
	 * 
	 * plantID 공장의 areaID 구역에 productionType에 따라
	 * 규칙에 맞게 lotID를 생성합니다
	 *
	 * @param parameter
	 * @return returnType
	 * @throws Exception
	 *
	 */
	public String lotIDGenerate(String plantID, String areaID, String productionType)
	{
//		String preFix = productionType + areaID;
		
		return nameGenerate( plantID, "LotID", new Object[] {productionType, areaID.substring(3, 7)} );
	}
	
	/**
	 * 
	 * plantID 공장에 ruleName, nameCount, args에 따라
	 * 규칙에 맞게 이름을 생성하여 리스트로 리턴합니다
	 *
	 * @param plantID
	 * @param ruleName
	 * @param args
	 * @return List
	 *
	 */	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List dozenNameGenerate(String plantID, String ruleName, int nameCount, Object... args)
	{
		List nameArgs = new ArrayList();
		for ( int i = 0; i < args.length; i++ )
		{
			nameArgs.add(args[i]);
		}

		List nameList = 
				MESFrameProxy.getIdClassService().createID(plantID, ruleName, nameCount, nameArgs);

		return nameList;
	}
	/**
	 * 
	 * plantID 공장에 ruleName, arg에 따라 규칙에 맞게 Long타입의 시퀀스를 생성합니다
	 *
	 * @param plantID
	 * @param ruleName
	 * @param arg
	 * @return Long
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long seqGenerate(String plantID, String ruleName, String arg)
	{
		List nameArgs = new ArrayList();
		nameArgs.add(arg);
		
		List nameList = 
				MESFrameProxy.getIdClassService().createID(plantID, ruleName, 1, nameArgs);
		
		String seq = (String) nameList.get(0);

		return Long.valueOf(seq.replace(arg, ""));
	}
	/**
	 * 
	 * plantID 공장에 ruleName, arg에 따라 규칙에 맞게 String타입의 시퀀스를 생성합니다
	 *
	 * @param plantID
	 * @param ruleName
	 * @param arg
	 * @return String
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String stringSeqGenerate(String plantID, String ruleName, String arg)
	{
		List nameArgs = new ArrayList();
		nameArgs.add(arg);
		
		List nameList = 
				MESFrameProxy.getIdClassService().createID(plantID, ruleName, 1, nameArgs);
		
		String seq = (String) nameList.get(0);

		return seq.replace(arg, "");
	}
	/**
	 * 
	 * plantID 공장에 processRouteCode, processID에 따라
	 * 규칙에 맞게 ROComposition의 ID를 생성합니다
	 *
	 * @param plantID
	 * @param processRouteCode
	 * @param processID
	 * @return String
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String nameGenerateROCompositionID(String plantID, String processRouteCode, String processID)
	{
//		processFlowCode = ConvertUtil.toStringEndWithZero(processFlowCode, 22);
		processID = ConvertUtil.toStringStartWithZero(processID, 5);
		
		List nameArgs = new ArrayList();
		nameArgs.add(processRouteCode);
		nameArgs.add(processID);

		List nameList = 
				MESFrameProxy.getIdClassService().createID(plantID, "ROCompositionID", 1, nameArgs);

		return (String) nameList.get(0);
	}
	/**
	 * 
	 * plantID공장에 processRouteCode에 따라
	 * 규칙에 맞게 RelationCode의 ID를 생성합니다
	 *
	 * @param plantID
	 * @param processRouteCode
	 * @return String
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String nameGenerateRelationCode(String plantID, String processRouteCode)
	{
//		processFlowName = ConvertUtil.toStringEndWithZero(processFlowName, 22);
		
		List nameArgs = new ArrayList();
		nameArgs.add(processRouteCode);

		List nameList = 
				MESFrameProxy.getIdClassService().createID(plantID, "RecipeRelationCode", 1, nameArgs);

		String name = (String) nameList.get(0);
		String relationCode = "R" + name.replace(processRouteCode, "") + "0";
		
		return relationCode;
	}
	/**
	 * 
	 * plantID공장에 processRouteCode, recipeRelationCode에 따라
	 * 규칙에 맞게 TypeCode의 ID를 생성합니다
	 *
	 * @param plantID
	 * @param processRouteCode
	 * @param recipeRelationCode
	 * @return String
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String nameGenerateTypeCode(String plantID, String processRouteCode, String recipeRelationCode)
	{
//		processFlowName = ConvertUtil.toStringEndWithZero(processFlowCode, 22);
//		processFlowCode = processFlowCode + recipeRelationCode;
		
		List nameArgs = new ArrayList();
		nameArgs.add(processRouteCode);
		nameArgs.add(recipeRelationCode);

		List nameList = 
				MESFrameProxy.getIdClassService().createID(plantID, "TypeCode", 1, nameArgs);

		String processFlow = processRouteCode + recipeRelationCode;
		
		String name = (String) nameList.get(0);
		String typeCode = name.replace(processFlow, "") + "0";
		
		return typeCode;
	}
}
