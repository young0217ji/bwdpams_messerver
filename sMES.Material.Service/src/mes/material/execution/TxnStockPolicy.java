package mes.material.execution;

import java.util.HashMap;
import java.util.List;

import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.STOCKMONTH;
import mes.master.data.STOCKPOLICY;
import mes.util.EventInfoUtil;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnStockPolicy implements ObjectExecuteService 
{
	@Override
	public Object execute(Document recvDoc) 
	{
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            STOCKPOLICY dataInfo = new STOCKPOLICY();
            STOCKMONTH stockMonth = new STOCKMONTH();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPOLICYID(dataMap.get("POLICYID"));
        	dataInfo.setKeyYYYYMM(dataMap.get("YYYYMM"));
        	
            stockMonth.setKeyPLANTID(dataMap.get("PLANTID"));
            stockMonth.setKeyYYYYMM(dataMap.get("YYYYMM"));
            
            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (STOCKPOLICY) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else
            {            	            	
            	dataInfo.setPOLICYVALUE(dataMap.get("POLICYVALUE"));
            	dataInfo.setACTIVESTATE(dataMap.get("ACTIVESTATE"));
            	dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            	
            	// DataInfo에  대한 CUD 실행
            	if ( dataMap.get("_ROWSTATUS").equals("C") )
            	{
            		dataInfo.excuteDML(SqlConstant.DML_INSERT);
            	}
            	else if ( dataMap.get("_ROWSTATUS").equals("U") )
            	{
            		dataInfo.excuteDML(SqlConstant.DML_UPDATE);             		
            	}
            	else
            	{
            		// Data Value Setting
                	stockMonth.setSTATE(dataMap.get("STATE"));
                	stockMonth.excuteDML(SqlConstant.DML_UPDATE);
                	AddHistory history = new AddHistory();
                	history.addHistory(stockMonth, txnInfo, "U");
            	}
            }                        
            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));        
        }
		return recvDoc;
	}
	
	public static class MaterialValidation
	{
		public static final int CHECK_MONTHSTATE = 4;
		public static boolean checkMonthState( STOCKPOLICY stockInfo )
		{
			STOCKMONTH stockMonth = new STOCKMONTH();
			stockMonth.setKeyPLANTID( Constant.PLANTID );
			stockMonth = ( STOCKMONTH )stockMonth.excuteDML( SqlConstant.DML_SELECTROW );

			if( !stockMonth.getKeyYYYYMM().equals( stockInfo.getKeyYYYYMM() ) )
			{
				throw new CustomException( "MM-005", new Object[] { stockMonth.getKeyYYYYMM(), stockInfo.getKeyYYYYMM() } );
			}
			else
			{
				if( stockMonth.getSTATE().equals( "Closing" ) )
				{
					throw new CustomException( "MM-006", new Object[] { stockMonth.getSTATE() } );
				}
				else
				{
					return true;
				}
			}
		}
	}
}
