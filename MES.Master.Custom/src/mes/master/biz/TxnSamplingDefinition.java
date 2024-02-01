package mes.master.biz;

import java.util.HashMap;
import java.util.List;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.DateUtil;
import mes.constant.Constant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.SAMPLINGDEFINITION;
import mes.master.data.SAMPLINGPOLICY;
import mes.util.EventInfoUtil;
import mes.util.NameGenerator;

import org.jdom.Document;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnSamplingDefinition implements ObjectExecuteService
{
	/**
	 * 공정진행을 선택적으로 해야 하는 경우 샘플링규칙을 등록, 수정, 삭제 하는 트랜잭션 실행
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
        
        NameGenerator nameGenerator = new NameGenerator();

        for (int i = 0 ; i < masterData.size(); i ++)
        {
            HashMap<String, String> dataMap = (HashMap<String, String>)masterData.get(i);
            txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

            String plantID = dataMap.get("PLANTID");
            String samplingID = dataMap.get("SAMPLINGID");
            
            SAMPLINGDEFINITION dataInfo = new SAMPLINGDEFINITION();
            
            // Key 에 대한 DataInfo 조회
            if ( !dataMap.get("_ROWSTATUS").equals("C") )
            {
            	dataInfo.setKeyPLANTID(plantID);
            	dataInfo.setKeySAMPLINGID(samplingID);
                dataInfo = (SAMPLINGDEFINITION) dataInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
            }
            else
            {
            	samplingID = nameGenerator.nameGenerate(plantID, "SamplingID", new Object[] {"S"});
            	dataInfo.setKeyPLANTID(plantID);
            	dataInfo.setKeySAMPLINGID(samplingID);
            }

            // DataInfo에  대한 CUD 실행
            if ( dataMap.get("_ROWSTATUS").equals("D") )
            {
            	// 샘플링 기준정보가 사용중인지 여부 체크
            	SAMPLINGPOLICY policyInfo = new SAMPLINGPOLICY();
            	policyInfo.setKeyPLANTID(dataInfo.getKeyPLANTID());
            	policyInfo.setSAMPLINGID(dataInfo.getKeySAMPLINGID());
            	List<Object> listPolicyInfo = (List<Object>) policyInfo.excuteDML(SqlConstant.DML_SELECTLIST);
            	if ( listPolicyInfo.size() > 0 )
            	{
            		// 해당 기준정보 ({0}) 가 사용중입니다.
            		throw new CustomException("MD-013", new Object[] {dataInfo.getKeySAMPLINGID()});
            	}
            	
                dataInfo.excuteDML(SqlConstant.DML_DELETE);
            }
            else
            {
            	// Data Value Setting
                String samplingType = dataMap.get("SAMPLINGTYPE");
                dataInfo.setSAMPLINGTYPE(samplingType); // 샘플링 구분 [ Count | Target ]
                
                String samplingMethod = dataMap.get("SAMPLINGMETHOD");
                if ( Constant.SAMPLING_TYPE_COUNT.equalsIgnoreCase(samplingType) )
                {
                	dataInfo.setSAMPLINGMETHOD(Constant.SAMPLING_ANYTHING);
                }
                else
                {
                	dataInfo.setSAMPLINGMETHOD(samplingMethod); // 샘플링 방법 [ LotNumber | Odd | Even ] ( Count 인 경우 없음 )
                }
                
                if ( Constant.SAMPLING_METHOD_ODD.equalsIgnoreCase(samplingMethod) || Constant.SAMPLING_METHOD_EVEN.equalsIgnoreCase(samplingMethod) )
                {
                	dataInfo.setSAMPLINGCHECK(Constant.SAMPLING_ANYTHING);
                }
                else
                {
                	dataInfo.setSAMPLINGCHECK(dataMap.get("SAMPLINGCHECK")); // Count Check 횟수 or Target LotNumber 끝번호 - 구분자 , 로 복수 등록 가능 ( Target 의 홀수, 짝수인 경우 없음 )
                }
                dataInfo.setDESCRIPTION(dataMap.get("DESCRIPTION"));
                
                // DataInfo에  대한 CUD 실행
                if ( dataMap.get("_ROWSTATUS").equals("C") )
                {
                    dataInfo.setCREATETIME(txnInfo.getEventTime());
                    dataInfo.setCREATEUSERID(txnInfo.getTxnUser());
                    
                    dataInfo.excuteDML(SqlConstant.DML_INSERT);
                }
                else if ( dataMap.get("_ROWSTATUS").equals("U") )
                {
                    dataInfo.excuteDML(SqlConstant.DML_UPDATE);
                }
            }

            // History 기록이 필요한 경우 사용
            AddHistory history = new AddHistory();
            history.addHistory(dataInfo, txnInfo, dataMap.get("_ROWSTATUS"));
        }

        return recvDoc;
    }

}
