package mes.master.biz;

import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import mes.master.data.TABLELIST;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnTableDrop implements ObjectExecuteService
{
	/**
	 * 테이블 삭제하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public Object execute( Document recvDoc )
	{
		TABLELIST dropInfo = new TABLELIST();
		List<TABLELIST> tableList = ( List<TABLELIST> )dropInfo.excuteDML( SqlConstant.DML_SELECTLIST );

		for( int i = 0; i < tableList.size(); ++i )
		{
			if( tableList.get( i ).getTABLETYPE().equals( "Backup" ) )
			{
				tableList.get( i ).excuteDML( SqlConstant.DML_DELETE );
				tableList.get( i ).excuteDROP();
			}
		}

		return recvDoc;
	}
}
