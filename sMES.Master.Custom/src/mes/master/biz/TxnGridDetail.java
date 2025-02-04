package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.GRIDDETAIL;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnGridDetail implements ObjectExecuteService
{
	/**
	 * Grid 상세 정보를 등록, 수정, 삭제 하는 트랜잭션 실행
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

            GRIDDETAIL dataInfo = new GRIDDETAIL();
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyGRIDID(dataMap.get("GRIDID"));
            dataInfo.setKeyGRIDCOLUMNID(dataMap.get("GRIDCOLUMNID"));


            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (GRIDDETAIL) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            dataInfo.setGRIDCOLUMNNAME(dataMap.get("GRIDCOLUMNNAME"));
            dataInfo.setPRIMARYKEYFLAG(dataMap.get("PRIMARYKEYFLAG"));
            dataInfo.setGRIDCOLUMNSIZE(ConvertUtil.String2Long(dataMap.get("GRIDCOLUMNSIZE")));
            dataInfo.setVISIBLEFLAG(dataMap.get("VISIBLEFLAG"));
            dataInfo.setEDITFLAG(dataMap.get("EDITFLAG"));
            dataInfo.setCELLTYPE(dataMap.get("CELLTYPE"));
            dataInfo.setDEFAULTVALUE(dataMap.get("DEFAULTVALUE"));
            dataInfo.setCOMBOENUMID(dataMap.get("COMBOENUMID"));
            dataInfo.setCOMBOLIST(dataMap.get("COMBOLIST"));
            dataInfo.setPOSITION(ConvertUtil.String2Long(dataMap.get("POSITION")));
            dataInfo.setALIGN(dataMap.get("ALIGN"));
            dataInfo.setCHANGECELLTYPE(dataMap.get("CHANGECELLTYPE"));
            dataInfo.setCLASSNAME(dataMap.get("CLASSNAME"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setCHILDGRIDID(dataMap.get("CHILDGRIDID"));
            dataInfo.setHEADERMERGENAME(dataMap.get("HEADERMERGENAME"));
            dataInfo.setHEADERLOCKFLAG(dataMap.get("HEADERLOCKFLAG"));
            dataInfo.setPARENTCOLUMNID(dataMap.get("PARENTCOLUMNID"));


            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
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
            //AddHistory history = new AddHistory();
            //history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }

}
