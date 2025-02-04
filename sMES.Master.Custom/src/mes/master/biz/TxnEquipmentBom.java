package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.EQUIPMENTBOM;
import mes.util.EventInfoUtil;
import mes.util.ValidationUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnEquipmentBom implements ObjectExecuteService
{
	/**
	 * 설비 BOM을 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
    @Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        ValidationUtil validation = new ValidationUtil();

        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            validation.checkKeyNull( dataMap, new Object[] {"PLANTID", "EQUIPMENTID", "PARTID"} );
            
            EQUIPMENTBOM dataInfo = new EQUIPMENTBOM();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
            	dataInfo.setKeyBOMINDEX(ConvertUtil.Object2Long4Zero(dataMap.get("BOMINDEX")));
            	
                dataInfo = (EQUIPMENTBOM) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            else {
                String usrSql = "SELECT ISNULL(MAX(BOMINDEX), 0) AS MAXBOMINDEX FROM EQUIPMENTBOM WHERE PLANTID = ? AND EQUIPMENTID = ? ";
//                		+ " GROUP BY PLANTID, EQUIPMENTID ";

            	Integer iMaxBOMIndex = SqlMesTemplate.queryForInt(usrSql, new Object[] { dataMap.get("PLANTID"), dataMap.get("EQUIPMENTID") });
                
                dataInfo.setKeyBOMINDEX(ConvertUtil.Object2Long4Zero(iMaxBOMIndex) + 1);
            }

            // Data Value Setting
            dataInfo.setBOMINDEXTYPE(dataMap.get("BOMINDEXTYPE"));
            dataInfo.setPARTID(dataMap.get("PARTID"));
            dataInfo.setPARTTYPE(dataMap.get("PARTTYPE"));
            dataInfo.setQUANTITY(ConvertUtil.String2Long(dataMap.get("QUANTITY")));
            dataInfo.setUNIT(dataMap.get("UNIT"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));


            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
            	EQUIPMENTBOM oBOMSeqInfo = new EQUIPMENTBOM();
                oBOMSeqInfo.setKeyPLANTID(dataMap.get("PLANTID"));
                oBOMSeqInfo.setKeyEQUIPMENTID(dataMap.get("EQUIPMENTID"));
                oBOMSeqInfo.setPARTID(dataMap.get("PARTID"));
                List<EQUIPMENTBOM> listBOMDataInfo = (List<EQUIPMENTBOM>) oBOMSeqInfo.excuteDML(SqlConstant.DML_SELECTLIST);
                if ( listBOMDataInfo.size() > 0 ) {
                	// 해당 정보 ({0}) 가 이미 존재합니다. 확인해주시기 바랍니다.
                	throw new CustomException("MD-015", new Object[] {oBOMSeqInfo.getPARTID()});
                }
            	
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
