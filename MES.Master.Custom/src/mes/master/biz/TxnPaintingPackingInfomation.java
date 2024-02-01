package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.DY_PAINTINGPACKINGINFOMATION;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.09.18
 * 
 * @see
 */
public class TxnPaintingPackingInfomation implements ObjectExecuteService {
	
	/**
	 * 제품 포장사양 등록, 수정, 삭제 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */

	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);
        
        for (int i = 0 ; i < masterData.size(); i ++) {
        	HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());
        	
        	DY_PAINTINGPACKINGINFOMATION dataInfo = new DY_PAINTINGPACKINGINFOMATION();
        	
        	// Key Value Setting
        	dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
        	dataInfo.setKeyPRODUCTID(dataMap.get("PRODUCTID"));
        	dataInfo.setKeyCOMPANYID(dataMap.get("COMPANYID"));
        	
        	// Key에 대한 DataInfo 조회
        	if(!dataMap.get("_ROWSTATUS").equals("C")) {
        		dataInfo = (DY_PAINTINGPACKINGINFOMATION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
        	}
        	
        	dataInfo.setPAINTINGCOLOR(dataMap.get("PAINTINGCOLOR"));
        	dataInfo.setPACKINGTYPE(dataMap.get("PACKINGTYPE"));
        	dataInfo.setPACKINGQUANTITY(Integer.parseInt(dataMap.get("PACKINGQUANTITY")));
        	dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));

        	// DataInfo에 대한 CUD 실행
            if(dataMap.get("_ROWSTATUS").equals("D")) {
            	dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }else if(dataMap.get("_ROWSTATUS").equals("C")) {
            	checkPK(dataInfo.getKeyPLANTID(), dataInfo.getKeyPRODUCTID(), dataInfo.getKeyCOMPANYID());
            	dataInfo.excuteDML(SqlConstant.DML_INSERT);
            }else if(dataMap.get("_ROWSTATUS").equals("U")) {
            	dataInfo.excuteDML(SqlConstant.DML_UPDATE);
            }
        }
        
        return recvDoc;
        
	}
	
	// 기존 데이터 PK 체크
	@SuppressWarnings("unchecked")
	private void checkPK(String plantId, String productId, String companyId) {
		DY_PAINTINGPACKINGINFOMATION dataInfo = new DY_PAINTINGPACKINGINFOMATION();
    	
    	// Key Value Setting
    	dataInfo.setKeyPLANTID(plantId);
    	dataInfo.setKeyPRODUCTID(productId);
    	dataInfo.setKeyCOMPANYID(companyId);
		
		List<Object> listDataInfo = (List<Object>) dataInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		if ( listDataInfo.size() > 0 ) {
			// 해당 정보가 이미 존재합니다. 확인해주시기 바랍니다.
			throw new CustomException("MD-015", new Object[] {"PLANTID: "+plantId+" / PRODUCTID: "+productId+" / COMPANYID: "+companyId});
		}
	}

}
