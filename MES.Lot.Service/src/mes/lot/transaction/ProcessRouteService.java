package mes.lot.transaction;

import kr.co.mesframe.orm.sql.constant.SqlConstant;
import mes.master.data.PROCESSROUTE;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class ProcessRouteService
{
	/**
	 * 공정 라우트 ID를 가지고 해당 공정 라우트 정보를 가져옴
	 * 
	 * @param plantID
	 * @param processRouteID
	 * @return PROCESSROUTE
	 * 
	 */
	public PROCESSROUTE getProcessRouteInfo(String plantID, String processRouteID)
	{
		PROCESSROUTE processRoute = new PROCESSROUTE();
		processRoute.setKeyPLANTID( plantID );
		processRoute.setKeyPROCESSROUTEID( processRouteID );
		
		processRoute = (PROCESSROUTE) processRoute.excuteDML(SqlConstant.DML_SELECTROW);

		return processRoute;
	}
	
//	@SuppressWarnings("unchecked")
//	public List<ROUTECOMPOSITION> getRouteCompositionList(String plantID, String repRouteID)
//	{
//		List<ROUTECOMPOSITION> listProcessRouteComposition = new ArrayList<ROUTECOMPOSITION>();
//		
//		ROUTECOMPOSITION routeComposition = new ROUTECOMPOSITION();
//		routeComposition.setKeyPLANTID( plantID );
//		routeComposition.setKeyREPROUTEID( repRouteID );
//		
//		String tName = routeComposition.getClass().getName().substring(routeComposition.getClass().getName().lastIndexOf(".")+1);
//		String sql = SqlQueryUtil.createSelectString(SqlConstant.DML_SELECTLIST, tName, routeComposition.getKEYMAP(), null);
//		sql = sql + " ORDER BY relationsequence ";
//		List<LinkedCaseInsensitiveMap> listRouteComposition = SqlMesTemplate.queryForList(sql, routeComposition.getKEYMAP());
//
//		if ( listRouteComposition != null )
//		{
//			for ( int i = 0; i < listRouteComposition.size(); i++ )
//			{
//				LinkedCaseInsensitiveMap mapRouteComposition = listRouteComposition.get(i);
//				ROUTECOMPOSITION routeCompositionElement = (ROUTECOMPOSITION) SqlQueryUtil.returnRowToData(routeComposition, mapRouteComposition);
//				
//				listProcessRouteComposition.add(routeCompositionElement);
//			}
//			
//			return listProcessRouteComposition;
//		}
//		else
//		{
//			throw new CustomException("OM-021", new Object[] {"ProcessFlowComposition"});
//		}
//	}
}
