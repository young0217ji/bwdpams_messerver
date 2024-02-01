package mes.master.biz;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.springframework.util.LinkedCaseInsensitiveMap;

import kr.co.mesframe.esb.ObjectExecuteService;
import kr.co.mesframe.orm.sql.SqlMesTemplate;
import kr.co.mesframe.orm.sql.constant.SqlConstant;
import kr.co.mesframe.orm.sql.util.AddHistory;
import kr.co.mesframe.txninfo.TxnInfo;
import kr.co.mesframe.util.ConvertUtil;
import kr.co.mesframe.util.DateUtil;
import mes.errorHandler.CustomException;
import mes.event.MessageParse;
import mes.master.data.DY_PROCWORKORDER;
import mes.util.EventInfoUtil;

/**
 * @author LSMESSolution
 * 
 * @since 2023.11.27
 * 
 * @see
 */
public class TxnProcWorkOrderReplan implements ObjectExecuteService {

	/**
	 * 생산계획 순서 변경
	 * 
	 * @param recvDoc
	 * @return Object
	 * 
	 */
	@Override
	public Object execute(Document recvDoc) {
		List<HashMap<String, String>> masterData = MessageParse.getDefaultXmlParse(recvDoc);
		TxnInfo txnInfo = EventInfoUtil.setTxnInfo(recvDoc);

		String workcenter = null;
		String equipmentId = null;
		String workDate = null;
		String plantId = null;

		for (int i = 0; i < masterData.size(); i++) {
			HashMap<String, String> dataMap = (HashMap<String, String>) masterData.get(i);
			txnInfo.setEventTimeKey(DateUtil.getCurrentEventTimeKey());

			workcenter = dataMap.get("WORKCENTER");
			equipmentId = dataMap.get("EQUIPMENTID");
			workDate = dataMap.get("WORKDATE");
			plantId = dataMap.get("PLANTID");
			
			LocalDate planDate = LocalDate.parse(workDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
			if(!LocalDate.now().equals(planDate)) {
				throw new CustomException("CM-999","확정 기준 일자(계획)와 실제(당일) 일자가 다릅니다. 확인 후 다시 처리 하시기 바랍니다.");
			}

			DY_PROCWORKORDER procworkorder = new DY_PROCWORKORDER();
			procworkorder.setKeyPLANTID(dataMap.get("PLANTID"));
			procworkorder.setKeyDATAKEY(dataMap.get("DATAKEY"));

			procworkorder.setEQUIPMENTID(dataMap.get("EQUIPMENTID"));
			procworkorder.setPLANSTARTDATE(dataMap.get("PLANSTARTDATE"));
			procworkorder.setPLANSTARTTIME("000000");
			procworkorder.setPLANSTARTPRIOR(Integer.parseInt(dataMap.get("PLANSTARTPRIOR")));

			procworkorder.excuteDML(SqlConstant.DML_UPDATE);

		}

		List<FN_PROCWORKORDER> replanList = selectReplanList(workcenter, equipmentId, workDate);

		String previewDatakey = null;
		for (FN_PROCWORKORDER item : replanList) {
			LocalDateTime expectStartDate = item.getEXPECTSTARTDATE();
			LocalDateTime expectEndDate = item.getEXPECTENDDATE();
			
			DY_PROCWORKORDER procworkorder = new DY_PROCWORKORDER();
			procworkorder.setKeyPLANTID(plantId);
			procworkorder.setKeyDATAKEY(item.getDATAKEY());
			procworkorder.setEQUIPMENTID(equipmentId);
			if(workcenter.startsWith("A")) {
				procworkorder.setASSEMBLYDATE(expectStartDate.plusHours(8).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}


			if (previewDatakey == null) {
				previewDatakey = item.getDATAKEY();
				
				procworkorder.setPLANSTARTPRIOR(item.getEXPECTPLANSTARTPRIOR());
				procworkorder.setPLANSTARTDATE(expectStartDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
				procworkorder.setPLANSTARTTIME(expectStartDate.format(DateTimeFormatter.ofPattern("HHmmss")));
				procworkorder.setPLANENDDATE(expectEndDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
				procworkorder.setPLANENDTIME(expectEndDate.format(DateTimeFormatter.ofPattern("HHmmss")));
				
				procworkorder.excuteDML(SqlConstant.DML_UPDATE);
				// History 기록이 필요한 경우 사용
	            AddHistory history = new AddHistory();
	            history.addHistory(procworkorder, txnInfo, "U");
				continue;
			} 

			if (previewDatakey.equals(item.getDATAKEY())) {

				procworkorder.setPLANENDDATE(expectEndDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
				procworkorder.setPLANENDTIME(expectEndDate.format(DateTimeFormatter.ofPattern("HHmmss")));
				procworkorder.excuteDML(SqlConstant.DML_UPDATE);
				
				// FIXME
				// history 적재

			} else {

				previewDatakey = item.getDATAKEY();

				procworkorder.setPLANSTARTPRIOR(item.getEXPECTPLANSTARTPRIOR());
				procworkorder.setPLANSTARTDATE(expectStartDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
				procworkorder.setPLANSTARTTIME(expectStartDate.format(DateTimeFormatter.ofPattern("HHmmss")));
				procworkorder.setPLANENDDATE(expectEndDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
				procworkorder.setPLANENDTIME(expectEndDate.format(DateTimeFormatter.ofPattern("HHmmss")));
				
				procworkorder.excuteDML(SqlConstant.DML_UPDATE);
				// History 기록이 필요한 경우 사용
	            AddHistory history = new AddHistory();
	            history.addHistory(procworkorder, txnInfo, "U");
				
			}
			
			

		}

		return recvDoc;
	}

	public List<FN_PROCWORKORDER> selectReplanList(String workcenter, String equipmentId, String workDate) {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("WORKCENTER", workcenter);
		paramMap.put("EQUIPMENTID", equipmentId);
		paramMap.put("WORKDATE", workDate);

		String sql = new StringBuffer().append("SELECT EXPECTSEQUNCE").append(", EXPECTPLANSTARTPRIOR").append(", DATAKEY").append(", WORKDATE")
				.append(", PLANSTARTDATE").append(", PLANSTARTPRIOR").append(", EXPECTSTARTDATE")
				.append(", EXPECTENDDATE")
				.append(" FROM FN_PROCWORKORDER(:WORKCENTER, :EQUIPMENTID, :WORKDATE) FNPWO ")
				.append(" ORDER BY ISNULL(FNPWO.WORKDATE, '29981231') ")
				.append(", CASE WHEN FNPWO.SPLIT = '분리앞' THEN 3 ").append("	  WHEN FNPWO.SPLIT = '분리뒤' THEN 1 ")
				.append("	  ELSE 2 END ").append(", FNPWO.EXPECTSTARTDATE, FNPWO.EXPECTSEQUNCE").toString();
		
		List<LinkedCaseInsensitiveMap<?>> replanList = SqlMesTemplate.queryForList(sql, paramMap);
		ArrayList<FN_PROCWORKORDER> result = new ArrayList<FN_PROCWORKORDER>();
		for(LinkedCaseInsensitiveMap<?> v : replanList) {
			result.add(new FN_PROCWORKORDER().ofValue(v));
		}
		return result;
	}

	class FN_PROCWORKORDER {
		private Integer EXPECTSEQUNCE;
		private Integer EXPECTPLANSTARTPRIOR;
		private String WORKDATE;
		private String PLANSTARTDATE;
		private String DATAKEY;
		private String WORKORDER;
		private String PRODUCTID;
		private String PRODUCTSPEC;
		private BigDecimal CYCLETIME;
		private String SHIFTID;
		private BigDecimal SHIFTTOTAL;
		private LocalDateTime EXPECTSTARTDATE;
		private LocalDateTime EXPECTENDDATE;
		private Integer QUANTITY;
		private Integer QUANTITYPRODUCT;
		private Integer QUANTITYNOTOK;
		private Integer QANTITYREMAIN;
		private BigDecimal NEEDTIME;
		private String SPLIT;
		private Integer PLANSTARTPRIOR;
		private String EQUIPMENTID;
		
		public FN_PROCWORKORDER ofValue(LinkedCaseInsensitiveMap<?> map) {
			
			
			if(map.containsKey("EXPECTSEQUNCE")) this.setEXPECTSEQUNCE(ConvertUtil.Object2Int(map.get("EXPECTSEQUNCE")));
			if(map.containsKey("EXPECTPLANSTARTPRIOR")) this.setEXPECTPLANSTARTPRIOR(ConvertUtil.Object2Int(map.get("EXPECTPLANSTARTPRIOR")));
			if(map.containsKey("WORKDATE")) this.setWORKDATE(ConvertUtil.Object2String(map.get("WORKDATE")));
			if(map.containsKey("PLANSTARTDATE")) this.setPLANSTARTDATE(ConvertUtil.Object2String(map.get("PLANSTARTDATE")));
			if(map.containsKey("DATAKEY")) this.setDATAKEY(ConvertUtil.Object2String(map.get("DATAKEY")));
			if(map.containsKey("WORKORDER")) this.setWORKORDER(ConvertUtil.Object2String(map.get("WORKORDER")));
			if(map.containsKey("PRODUCTID")) this.setPRODUCTID(ConvertUtil.Object2String(map.get("PRODUCTID")));
			if(map.containsKey("PRODUCTSPEC")) this.setPRODUCTSPEC(ConvertUtil.Object2String(map.get("PRODUCTSPEC")));
			if(map.containsKey("CYCLETIME")) this.setCYCLETIME(new BigDecimal(ConvertUtil.Object2String(map.get("CYCLETIME"))));
			if(map.containsKey("SHIFTID")) this.setSHIFTID(ConvertUtil.Object2String(map.get("SHIFTID")));
			if(map.containsKey("SHIFTTOTAL")) this.setSHIFTTOTAL(new BigDecimal(ConvertUtil.Object2String(map.get("SHIFTTOTAL"))));
			if(map.containsKey("EXPECTSTARTDATE")) this.setEXPECTSTARTDATE(Timestamp.valueOf(ConvertUtil.Object2String(map.get("EXPECTSTARTDATE"))).toLocalDateTime());
			if(map.containsKey("EXPECTENDDATE")) this.setEXPECTENDDATE(Timestamp.valueOf(ConvertUtil.Object2String(map.get("EXPECTENDDATE"))).toLocalDateTime());
			if(map.containsKey("QUANTITY")) this.setQUANTITY(ConvertUtil.Object2Int(map.get("QUANTITY")));
			if(map.containsKey("QUANTITYPRODUCT")) this.setQUANTITYPRODUCT(ConvertUtil.Object2Int(map.get("QUANTITYPRODUCT")));
			if(map.containsKey("QUANTITYNOTOK")) this.setQANTITYREMAIN(ConvertUtil.Object2Int(map.get("QUANTITYNOTOK")));
			if(map.containsKey("QANTITYREMAIN")) this.setQANTITYREMAIN(ConvertUtil.Object2Int(map.get("QANTITYREMAIN")));
			if(map.containsKey("NEEDTIME")) this.setNEEDTIME(new BigDecimal(ConvertUtil.Object2String(map.get("NEEDTIME"))));
			if(map.containsKey("SPLIT")) this.setSPLIT(ConvertUtil.Object2String(map.get("SPLIT")));
			if(map.containsKey("PLANSTARTPRIOR")) this.setPLANSTARTPRIOR(ConvertUtil.Object2Int(map.get("PLANSTARTPRIOR")));
			if(map.containsKey("EQUIPMENTID")) this.setEQUIPMENTID(ConvertUtil.Object2String(map.get("EQUIPMENTID")));
			return this; 
		}

		public Integer getEXPECTSEQUNCE() {
			return EXPECTSEQUNCE;
		}

		public void setEXPECTSEQUNCE(Integer eXPECTSEQUNCE) {
			EXPECTSEQUNCE = eXPECTSEQUNCE;
		}
		
		public Integer getEXPECTPLANSTARTPRIOR() {
			return EXPECTPLANSTARTPRIOR;
		}

		public void setEXPECTPLANSTARTPRIOR(Integer EXPECTPLANSTARTPRIOR) {
			this.EXPECTPLANSTARTPRIOR = EXPECTPLANSTARTPRIOR;
		}

		public String getWORKDATE() {
			return WORKDATE;
		}

		public void setWORKDATE(String wORKDATE) {
			WORKDATE = wORKDATE;
		}

		public String getPLANSTARTDATE() {
			return PLANSTARTDATE;
		}

		public void setPLANSTARTDATE(String pLANSTARTDATE) {
			PLANSTARTDATE = pLANSTARTDATE;
		}

		public String getDATAKEY() {
			return DATAKEY;
		}

		public void setDATAKEY(String dATAKEY) {
			DATAKEY = dATAKEY;
		}

		public String getWORKORDER() {
			return WORKORDER;
		}

		public void setWORKORDER(String wORKORDER) {
			WORKORDER = wORKORDER;
		}

		public String getPRODUCTID() {
			return PRODUCTID;
		}

		public void setPRODUCTID(String pRODUCTID) {
			PRODUCTID = pRODUCTID;
		}

		public String getPRODUCTSPEC() {
			return PRODUCTSPEC;
		}

		public void setPRODUCTSPEC(String pRODUCTSPEC) {
			PRODUCTSPEC = pRODUCTSPEC;
		}

		public BigDecimal getCYCLETIME() {
			return CYCLETIME;
		}

		public void setCYCLETIME(BigDecimal cYCLETIME) {
			CYCLETIME = cYCLETIME;
		}

		public String getSHIFTID() {
			return SHIFTID;
		}

		public void setSHIFTID(String sHIFTID) {
			SHIFTID = sHIFTID;
		}

		public BigDecimal getSHIFTTOTAL() {
			return SHIFTTOTAL;
		}

		public void setSHIFTTOTAL(BigDecimal sHIFTTOTAL) {
			SHIFTTOTAL = sHIFTTOTAL;
		}

		public LocalDateTime getEXPECTSTARTDATE() {
			return EXPECTSTARTDATE;
		}

		public void setEXPECTSTARTDATE(LocalDateTime eXPECTSTARTDATE) {
			EXPECTSTARTDATE = eXPECTSTARTDATE;
		}

		public LocalDateTime getEXPECTENDDATE() {
			return EXPECTENDDATE;
		}

		public void setEXPECTENDDATE(LocalDateTime eXPECTENDDATE) {
			EXPECTENDDATE = eXPECTENDDATE;
		}

		public Integer getQUANTITY() {
			return QUANTITY;
		}

		public void setQUANTITY(Integer qUANTITY) {
			QUANTITY = qUANTITY;
		}

		public Integer getQUANTITYPRODUCT() {
			return QUANTITYPRODUCT;
		}

		public void setQUANTITYPRODUCT(Integer qUANTITYPRODUCT) {
			QUANTITYPRODUCT = qUANTITYPRODUCT;
		}

		public Integer getQUANTITYNOTOK() {
			return QUANTITYNOTOK;
		}

		public void setQUANTITYNOTOK(Integer qUANTITYNOTOK) {
			QUANTITYNOTOK = qUANTITYNOTOK;
		}

		public Integer getQANTITYREMAIN() {
			return QANTITYREMAIN;
		}

		public void setQANTITYREMAIN(Integer qANTITYREMAIN) {
			QANTITYREMAIN = qANTITYREMAIN;
		}

		public BigDecimal getNEEDTIME() {
			return NEEDTIME;
		}

		public void setNEEDTIME(BigDecimal nEEDTIME) {
			NEEDTIME = nEEDTIME;
		}

		public String getSPLIT() {
			return SPLIT;
		}

		public void setSPLIT(String sPLIT) {
			SPLIT = sPLIT;
		}

		public Integer getPLANSTARTPRIOR() {
			return PLANSTARTPRIOR;
		}

		public void setPLANSTARTPRIOR(Integer pLANSTARTPRIOR) {
			PLANSTARTPRIOR = pLANSTARTPRIOR;
		}

		public String getEQUIPMENTID() {
			return EQUIPMENTID;
		}

		public void setEQUIPMENTID(String eQUIPMENTID) {
			EQUIPMENTID = eQUIPMENTID;
		}
	}
}
