package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.AREA;
import mes.util.EventInfoUtil;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnArea implements ObjectExecuteService
{	
	List<AREA> listArea;
	
	/**
	 * 공장의 Area 를 등록, 수정, 삭제 하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
    @SuppressWarnings("unchecked" )
	@Override
    public Object execute(Document recvDoc)
    {
        List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
        TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            AREA dataInfo = new AREA();
            
            // Key Value Setting
            dataInfo.setKeyPLANTID(dataMap.get("PLANTID"));
            dataInfo.setKeyAREAID(dataMap.get("AREAID"));


            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
                dataInfo = (AREA) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }

            // Data Value Setting
            dataInfo.setAREANAME(dataMap.get("AREANAME"));
            dataInfo.setAREATYPE(dataMap.get("AREATYPE"));
            dataInfo.setSUPERAREAID(dataMap.get("SUPERAREAID"));
            dataInfo.setPOSITION(ConvertUtil.String2Long(dataMap.get("POSITION")));
            dataInfo.setGOINORDERREQUIRED(dataMap.get("GOINORDERREQUIRED"));
            dataInfo.setKOREAN(dataMap.get("KOREAN"));
            dataInfo.setENGLISH(dataMap.get("ENGLISH"));
            dataInfo.setNATIVE1(dataMap.get("NATIVE1"));
            dataInfo.setNATIVE2(dataMap.get("NATIVE2"));
            dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
            // [2023.08.16-구자윤] AREA 컬럼 추가
            dataInfo.setREPRESENTATIVECHAR(dataMap.get("REPRESENTATIVECHAR"));
            dataInfo.setWORKCENTERTYPE(dataMap.get("WORKCENTERTYPE"));
            dataInfo.setWORKCENTERGROUPID(dataMap.get("WORKCENTERGROUPID"));
            dataInfo.setUNITCOUNT(ConvertUtil.Object2Long(dataMap.get("UNITCOUNT")));
            dataInfo.setSTANDARDVALUEKEY(dataMap.get("STANDARDVALUEKEY"));
            dataInfo.setSTANDARDLABOR(ConvertUtil.Object2Long(dataMap.get("STANDARDLABOR")));
            dataInfo.setREPRESENTATIVEPROCESSID(dataMap.get("REPRESENTATIVEPROCESSID"));
            dataInfo.setUSEFLAG(dataMap.get("USEFLAG"));
            
            
            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
        		AREA checkInfo = new AREA();
        		checkInfo.setKeyPLANTID(dataInfo.getKeyPLANTID());
        		listArea = (List<AREA>) checkInfo.excuteDML(SqlConstant.DML_SELECTLIST);
        		
            	RecursiveDelete( dataInfo );
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else if ( dataMap.get("_ROWSTATUS").equals("C") )
            {
            	checkAreaExist(dataInfo.getKeyPLANTID(), dataInfo.getKeyAREAID());
            	
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
    
    // 하위노드 삭제 Recursive 함수
	private void RecursiveDelete( AREA dataInfo )
	{
		for (int j=listArea.size()-1; j>=0; j--)
		{
			if (listArea.get(j).getSUPERAREAID().equals(dataInfo.getKeyAREAID()))
			{
				AREA subDataInfo = new AREA();
				subDataInfo.setKeyPLANTID(dataInfo.getKeyPLANTID());
				subDataInfo.setKeyAREAID(listArea.get(j).getKeyAREAID());
				subDataInfo.excuteDML(SqlConstant.DML_DELETE);
				RecursiveDelete(subDataInfo);
			}
		}
	}

    // 기존 Area 등록 여부 체크
	@SuppressWarnings("unchecked")
	private void checkAreaExist(String plantID, String areaID)
	{
		AREA dataInfo = new AREA();
		dataInfo.setKeyPLANTID(plantID);
		dataInfo.setKeyAREAID(areaID);
		
		List<Object> listDataInfo = (List<Object>) dataInfo.excuteDML(SqlConstant.DML_SELECTLIST);
		
		if ( listDataInfo.size() > 0 ) {
			// 해당 정보 ({0}) 가 이미 존재합니다. 확인해주시기 바랍니다.
			throw new CustomException("MD-015", new Object[] {((AREA) listDataInfo.get(0)).getKeyAREAID()});
		}
	}
}
