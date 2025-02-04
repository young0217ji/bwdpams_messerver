package mes.lot.transaction;

import kr.co.mesframe.orm.sql.constant.SqlConstant;
import mes.master.data.PROCESSDEFINITION;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class ProcessDefinitionService
{
	/**
	 * 공정 ID를 가지고 해당 공정 기준정보를 가져옴 
	 * 
	 * @param plantID
	 * @param processID
	 * @return PROCESSDEFINITION
	 * 
	 */
	public PROCESSDEFINITION getProcessDefinition(String plantID, String processID)
	{
		// Process Operation Spec Info
		PROCESSDEFINITION processDefinition = new PROCESSDEFINITION();
		processDefinition.setKeyPLANTID( plantID );
		processDefinition.setKeyPROCESSID( processID );

		processDefinition = (PROCESSDEFINITION) processDefinition.excuteDML(SqlConstant.DML_SELECTROW);

		return processDefinition;
	}
}
