package mes.master.biz;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;

public class TxnGetPlanningData implements ObjectExecuteService{

	@Override
	public Object execute(Document recvDoc) {
		SqlMesTemplate.executeProcedure("SP_INTERFACE_IF_ERPLAN_R");
		return recvDoc;
	}

}
