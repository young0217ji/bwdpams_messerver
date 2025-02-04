package mes.lot.transaction;

import java.util.List;

import kr.co.mesframe.orm.sql.constant.SqlConstant;
import mes.errorHandler.CustomException;
import mes.master.data.PRODUCTDEFINITION;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class ProductService
{
	/**
	 * 제품 ID를 가지고 제품 기준정보 리스트를 가져옴
	 * 
	 * @param plantID
	 * @param productID
	 * @return PRODUCTDEFINITION
	 * @throws CustomException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public PRODUCTDEFINITION getProductInfo(String plantID, String productID)
	{
		// Get Product Spec Info
		PRODUCTDEFINITION productDefinition = new PRODUCTDEFINITION();
		productDefinition.setKeyPLANTID( plantID );
		productDefinition.setKeyPRODUCTID( productID );

		List<Object> listProductDefinition = (List<Object>) productDefinition.excuteDML(SqlConstant.DML_SELECTLIST);
		
		if ( listProductDefinition.size() > 0 )
		{
			productDefinition = (PRODUCTDEFINITION) listProductDefinition.get(0);
			
			return productDefinition;
		}
		else
		{
			// 필요한 기준정보가 존재하지 않아 진행이 불가능합니다. (기준정보테이블 : {0})
			throw new CustomException("CHECK-006", new Object[] {"ProductDefinition"});
		}
	}
}
