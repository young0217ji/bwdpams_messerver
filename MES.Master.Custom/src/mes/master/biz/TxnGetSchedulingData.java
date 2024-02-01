package mes.master.biz;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;

public class TxnGetSchedulingData implements ObjectExecuteService{

	@Override
	public Object execute(Document recvDoc) {
		SqlMesTemplate.executeProcedure("SP_INTERFACE_IF_ERPSCHEDULE_R");
		return recvDoc;
	}
}
