package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.event.MessageParse;
import mes.master.data.AREA;
import mes.master.data.DY_BOARDOPTIONSET;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.12.05 
 * 
 * @see
 */
public class TxnBoardOptionSet implements ObjectExecuteService
{	
	List<AREA> listArea;
	
	/**
	 * 옵션관리의 색상 및 기준정보를 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo oTxnInfo = EventInfoUtil.setTxnInfo(recvDoc);

        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> mDataMap = (HashMap<String, String>)masterData.get(i);
            oTxnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            DY_BOARDOPTIONSET oDataInfo = new DY_BOARDOPTIONSET();
            
            // Key Value Setting
            oDataInfo.setKeyPLANTID(mDataMap.get("PLANTID"));
            oDataInfo.setKeyWORKCENTER(mDataMap.get("WORKCENTER"));
            oDataInfo.setKeyMANAGEMENTID(mDataMap.get("MANAGEMENTID"));

            // Key 에 대한 DataInfo 조회
            if ( "C".equals(mDataMap.get("_ROWSTATUS")) == false )
            {
                oDataInfo = (DY_BOARDOPTIONSET) oDataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            oDataInfo.setBEFOREBACKCOLOR(mDataMap.get("BEFOREBACKCOLOR"));
            oDataInfo.setBEFOREFONTCOLOR(mDataMap.get("BEFOREFONTCOLOR"));
            oDataInfo.setWORKINGBACKCOLOR(mDataMap.get("WORKINGBACKCOLOR"));
            oDataInfo.setWORKINGFONTCOLOR(mDataMap.get("WORKINGFONTCOLOR"));
            oDataInfo.setAFTERBACKCOLOR(mDataMap.get("AFTERBACKCOLOR"));
            oDataInfo.setAFTERFONTCOLOR(mDataMap.get("AFTERFONTCOLOR"));
            oDataInfo.setPAUSEBACKCOLOR(mDataMap.get("PAUSEBACKCOLOR"));
            oDataInfo.setPAUSEFONTCOLOR(mDataMap.get("PAUSEFONTCOLOR"));
            oDataInfo.setSTOPBACKCOLOR(mDataMap.get("STOPBACKCOLOR"));
            oDataInfo.setSTOPFONTCOLOR(mDataMap.get("STOPFONTCOLOR"));
            oDataInfo.setNEXTBACKCOLOR(mDataMap.get("NEXTBACKCOLOR"));
            oDataInfo.setNEXTFONTCOLOR(mDataMap.get("NEXTFONTCOLOR"));
            oDataInfo.setMESSAGEFLAG(mDataMap.get("MESSAGEFLAG"));
            oDataInfo.setVISUALRATE( ConvertUtil.Object2Long(mDataMap.get("VISUALRATE")) );
            oDataInfo.setNEXTCOUNT( ConvertUtil.Object2Long4Zero(mDataMap.get("NEXTCOUNT")) );
            oDataInfo.setLASTUPDATETIME(oTxnInfo.getEventTime());
            oDataInfo.setLASTUPDATEUSERID(oTxnInfo.getTxnUser());            
            
            // DataInfo에  대한 CUD 실행
            if ( "D".equals(mDataMap.get("_ROWSTATUS")) ) {
                oDataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( "C".equals(mDataMap.get("_ROWSTATUS")) ) {
                oDataInfo.setCREATETIME(oTxnInfo.getEventTime());
                oDataInfo.setCREATEUSERID(oTxnInfo.getTxnUser());
                
                oDataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( "U".equals(mDataMap.get("_ROWSTATUS")) ) {
                oDataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
        }

        return recvDoc;
    }
}
