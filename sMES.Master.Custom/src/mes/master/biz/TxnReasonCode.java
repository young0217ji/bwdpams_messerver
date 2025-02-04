package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.REASONCODE;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnReasonCode implements ObjectExecuteService
{
	List<REASONCODE> listReasonCode;
	
	/**
	 * 사유코드를 등록, 수정, 삭제 하는 트랜잭션 실행
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

            REASONCODE dataInfo = new REASONCODE();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyREASONCODETYPE(dataMap.get("REASONCODETYPE"));
            dataInfo.setKeyREASONCODE(dataMap.get("REASONCODE"));

            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (REASONCODE) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }


            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            dataInfo.setSUPERREASONCODE(dataMap.get("SUPERREASONCODE"));
            dataInfo.setLEVELNO(ConvertUtil.Object2Long(dataMap.get("LEVELNO")));
            dataInfo.setAVAILABILITY(dataMap.get("AVAILABILITY"));
            dataInfo.setPOSITION(ConvertUtil.Object2Long(dataMap.get("POSITION")));
			
            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") ) {
            	REASONCODE checkInfo = new REASONCODE();
        		checkInfo.setKeyPLANTID(dataInfo.getKeyPLANTID());
        		checkInfo.setKeyREASONCODETYPE(dataInfo.getKeyREASONCODETYPE());
        		listReasonCode = (List<REASONCODE>) checkInfo.excuteDML(SqlConstant.DML_SELECTLIST);
        		
            	RecursiveDelete( dataInfo );
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") ) {
            	checkReasonCodeExist(dataInfo.getKeyPLANTID(), dataInfo.getKeyREASONCODETYPE(), dataInfo.getKeyREASONCODE());
            	
                dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("U") ) {
                dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
        }

        return recvDoc;
    }
    
    // 하위노드 삭제 Recursive 함수
	private void RecursiveDelete( REASONCODE dataInfo )
	{
		for ( int j = listReasonCode.size()-1; j >= 0; j-- ) {
			if ( listReasonCode.get(j).getSUPERREASONCODE().equals(dataInfo.getKeyREASONCODE()) ) {
				REASONCODE subDataInfo = new REASONCODE();
				subDataInfo.setKeyPLANTID(dataInfo.getKeyPLANTID());
				subDataInfo.setKeyREASONCODETYPE(dataInfo.getKeyREASONCODETYPE());
				subDataInfo.setKeyREASONCODE(listReasonCode.get(j).getKeyREASONCODE());
				subDataInfo.excuteDML(SqlConstant.DML_DELETE);
				RecursiveDelete(subDataInfo);
			}
		}
	}
	
    // 기존 ReasonCode 등록 여부 체크
	@SuppressWarnings("unchecked")
	private void checkReasonCodeExist(String sPlantID, String sReasonCodeType, String sReasonCode) {
		REASONCODE dataInfo = new REASONCODE();
		dataInfo.setKeyPLANTID(sPlantID);
		dataInfo.setKeyREASONCODETYPE(sReasonCodeType);
		dataInfo.setKeyREASONCODE(sReasonCode);
		
		List<Object> listDataInfo = (List<Object>) dataInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		if ( listDataInfo.size() > 0 ) {
			// 해당 정보 ({0}) 가 이미 존재합니다. 확인해주시기 바랍니다.
			throw new CustomException("MD-015", new Object[] {((REASONCODE) listDataInfo.get(0)).getKeyREASONCODE()});
		}
	}
}
