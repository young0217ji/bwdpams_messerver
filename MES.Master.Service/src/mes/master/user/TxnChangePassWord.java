package mes.master.user;

import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.exception.NoDataFoundException;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.SF_USER;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class TxnChangePassWord implements ObjectExecuteService
{
	private static final transient Logger logger = LoggerFactory.getLogger(TxnChangePassWord.class);
	
	/**
	 * 비밀번호 변경하는 트랜잭션 실행
	 * 
	 * @param recvDoc
	 * @return Object
	 * @throws CustomException
	 * 
	 */
    @Override
    public Object execute(Document recvDoc)
    {
//      String plantID = message.getParam(recvDoc, "PLANTID");
    	String userID = MessageParse.getParam(recvDoc, "USERID");
    	String userPassWord = MessageParse.getParam(recvDoc, "USERPASSWORD");
    	String changePassWord = MessageParse.getParam(recvDoc, "CHANGEPASSWORD");

    	logger.info("Input USERID : " + userID);
//    	logger.info("Input USERPASSWORD : " + userPassWord);
//    	logger.info("Input CHANGEPASSWORD : " + changePassWord);

    	SF_USER userInfo = new SF_USER();
    	userInfo.setKeyUSERID(userID);
    	
    	try
    	{
    		userInfo = (SF_USER) userInfo.excuteDML(SqlConstant.DML_SELECTFORUPDATE);
    		
    		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    		
    		if ( encoder.matches(userPassWord, userInfo.getPASSWORD()) )
    		{
    			// 비밀번호 일치
    			userInfo.setPASSWORD(encoder.encode(changePassWord));
    			userInfo.excuteDML(SqlConstant.DML_UPDATE);
    		}
    		else
    		{
    			// ({0}) 사용자의 비밀번호가 일치하지 않습니다.
    			throw new CustomException("USER-002", userID);
    		}
    	}
    	catch ( NoDataFoundException e )
    	{
    		// ({0}) 등록되지 않은 사용자 입니다.
			throw new CustomException("USER-001", userID);
    	}
    	
        return recvDoc;
    }
}
