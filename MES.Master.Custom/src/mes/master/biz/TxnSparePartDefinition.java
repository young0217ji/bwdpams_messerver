package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.EQUIPMENTBOM;
import mes.master.data.SPAREPARTDEFINITION;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnSparePartDefinition implements ObjectExecuteService
{
	/**
	 * 설비 부품 기준정보를 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
    @Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            SPAREPARTDEFINITION dataInfo = new SPAREPARTDEFINITION();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeySPAREPARTID(dataMap.get("SPAREPARTID"));


            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (SPAREPARTDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            dataInfo.setSPAREPARTNAME(dataMap.get("SPAREPARTNAME"));
            dataInfo.setSPAREPARTTYPE(dataMap.get("SPAREPARTTYPE"));
            dataInfo.setSAFESTOCK(ConvertUtil.String2Long(dataMap.get("SAFESTOCK")));
            dataInfo.setUNIT(dataMap.get("UNIT"));
            dataInfo.setMAINVENDOR(dataMap.get("MAINVENDOR"));
            dataInfo.setMAKER(dataMap.get("MAKER"));
            dataInfo.setENGINEER(dataMap.get("ENGINEER"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));


            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
            	// EQUIPMENTBOM 정보가 존재하는지 여부 체크 삭제 불가능
            	EQUIPMENTBOM oBOMDataInfo = new EQUIPMENTBOM();
            	oBOMDataInfo.setKeyPLANTID(dataInfo.getKeyPLANTID());
            	oBOMDataInfo.setPARTID(dataInfo.getKeySPAREPARTID());
            	List<EQUIPMENTBOM> listBOMDataInfo = (List<EQUIPMENTBOM>) oBOMDataInfo.excuteDML(SqlConstant.DML_SELECTLIST);
            	
            	if ( listBOMDataInfo.size() > 0 ) {
            		String sEquipmentList = "";
            		
            		for ( int j = 0; j < listBOMDataInfo.size(); j++ ) {
            			oBOMDataInfo = listBOMDataInfo.get(j);
            			
            			if ( j == 0 ) {
            				sEquipmentList = sEquipmentList + oBOMDataInfo.getKeyEQUIPMENTID();	
            			}
            			else {
            				sEquipmentList = sEquipmentList + ", " + oBOMDataInfo.getKeyEQUIPMENTID();
            			}
            		}
            		
            		// 다음 ({0}) 예비품이 설비에서 사용중 입니다. 삭제를 진행 할 수 없습니다. ( 사용 설비 : ({1}) )
        			throw new CustomException("MD-018", new Object[] {dataInfo.getKeySPAREPARTID(), sEquipmentList});
            	}
            	
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") )
            {
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }

            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }

}
