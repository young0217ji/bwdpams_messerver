package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.BOMDEFINITION;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnBomDefinition implements ObjectExecuteService
{
	/**
	 * BOM의 기본 속성을 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        String sOldActiveState = "";
        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            BOMDEFINITION dataInfo = new BOMDEFINITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyPRODUCTID(dataMap.get("PRODUCTID"));
            dataInfo.setKeyBOMID(dataMap.get("BOMID"));
            dataInfo.setKeyBOMVERSION(dataMap.get("BOMVERSION"));

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (BOMDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
                sOldActiveState = dataInfo.getACTIVESTATE();
            }
            
            // Data Value Setting
            dataInfo.setBOMTYPE(dataMap.get("BOMTYPE"));
            dataInfo.setCONDITIONTYPE(dataMap.get("CONDITIONTYPE"));
            dataInfo.setCONDITIONID(dataMap.get("CONDITIONID"));
            dataInfo.setSTANDARDVALUE(ConvertUtil.String2Long(dataMap.get("STANDARDVALUE")));
            dataInfo.setSTANDARDUNIT(dataMap.get("STANDARDUNIT"));
            dataInfo.setACTIVESTATE(dataMap.get("ACTIVESTATE"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));

            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            { 
            	
            	// 해당  PLANTID, BOMID, BOMVersion 으로 존재하는지 Validation 한다.
            	BOMDEFINITION checkInfo = new BOMDEFINITION();
	            // Key Value Setting
            	checkInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            	checkInfo.setKeyBOMID(dataMap.get("BOMID"));
            	checkInfo.setKeyBOMVERSION(dataMap.get("BOMVERSION"));
				List<BOMDEFINITION> listBOMDefinition = (List<BOMDEFINITION>) checkInfo.excuteDML(SqlConstant.DML_SELECTLIST);
        		if (listBOMDefinition.size()>0)
    			{
        			// 해당 정보 ({0}) 가 이미 존재합니다. 확인해주시기 바랍니다.
        			String sExistData = checkInfo.getKeyPLANTID()+","+checkInfo.getKeyBOMID()+","+checkInfo.getKeyBOMVERSION();
        			throw new CustomException("MD-015", new Object[] {sExistData});
    			}
            	
            	
                dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                dataInfo.setCREATETIME(txnInfo.getEventTime());

                if (Constant.ACTIVESTATE_ACTIVE.equals(dataMap.get("ACTIVESTATE")))
                {
                	dataInfo.setACTIVEUSERID(txnInfo.getTxnUser());
                	dataInfo.setACTIVETIME(txnInfo.getEventTime());
                }
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") )
            {
            	if ( (Constant.ACTIVESTATE_INACTIVE.equals(sOldActiveState)) && 
	               		 (Constant.ACTIVESTATE_ACTIVE.equals(dataMap.get("ACTIVESTATE"))) )
               	{
                	dataInfo.setACTIVEUSERID(txnInfo.getTxnUser());
                	dataInfo.setACTIVETIME(txnInfo.getEventTime());
               	}
            	if ( (Constant.ACTIVESTATE_ACTIVE.equals(sOldActiveState)) && 
               		 (Constant.ACTIVESTATE_INACTIVE.equals(dataMap.get("ACTIVESTATE"))) )
               	{
                	dataInfo.setINACTIVEUSERID(txnInfo.getTxnUser());
                	dataInfo.setINACTIVETIME(txnInfo.getEventTime());
               	}
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            //AddHistory history = new AddHistory();
            //history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));


        }

        return recvDoc;
    }
}
