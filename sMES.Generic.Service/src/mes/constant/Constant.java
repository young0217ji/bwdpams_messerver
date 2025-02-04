package mes.constant;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class Constant
{
	/*********************
	 * PLANT ID Information
	 * PLANT Table 조회
	 */
	public static final String FACTORY_ID = FactoryConstant.getFactoryId();
	

	/*
	 * ***************************** 버전 ************************************
	 */

	/**
	 * 기본 버젼 : 00001
	 */
	public static final String	VERSION_DEFAULT						= "00001";

	/**
	 * ProductSpec 기본 Version : 00001
	 */
	public static final String	PRODUCT_VERSION_DEFAULT				= "00001";

	/*
	 * ***************************** FLAG ************************************
	 */
	/**
	 * 플래그 - 예/아니오 택일 플래그 - 예(Yes) : Y
	 */
	public static final String	FLAG_YN_YES							= "Y";
	public static final String	FLAG_YESNO_YES						= "Yes";
	/**
	 * 플래그 - 예/아니오 택일 플래그 - 아니오(No) : N
	 */
	public static final String	FLAG_YN_NO							= "N";
	public static final String	FLAG_YESNO_NO						= "No";


	/*
	 * ***************************** STATUS ************************************
	 */

	public static final String	PORT_TYPE_INPUT						= "Input";
	public static final String	PORT_TYPE_OUTPUT					= "Output";
	
	public static final String	PORT_ROUTETYPE_FROM					= "From";
	public static final String	PORT_ROUTETYPE_TO					= "To";
	

	/*********************
	 * 7. 알람 상태
	 */

	public static final String	ALARM_STATE_CREATED					= "Created";			// 생성
	public static final String	ALARM_STATE_RECEIVED				= "Received";			// 접수
	public static final String	ALARM_STATE_COMPLETED				= "Completed";			// 완료
	
	
	/*
	 * ***************************** 생산방식 ************************************
	 */
	/**
	 * 개발생산방식 : Lot, 물량생산방식 : Batch
	 */
	public static final String	PRODUCTTYPE_LOT						= "Lot";
	public static final String	PRODUCTTYPE_BATCH					= "Batch";
	
	/*
	 * ****************************** 로트 생성 ************************************
	 */
	public static final String	EVENT_START								= "Start";
	public static final String	EVENT_END								= "End";
	public static final String	EVENT_CHANGE							= "Change";
	
	public static final String	EVENT_CREATELOT							= "CreateLot";
	public static final String	EVENT_CREATELOTCANCEL					= "CreateLotCancel";
	
	public static final String	EVENT_LOTSTART							= "LotStart";
	public static final String	EVENT_LOTSTARTCANCEL					= "LotStartCancel";
	public static final String	EVENT_RECYCLECONSUMABLE					= "RecycleConsumable";
	public static final String	EVENT_CYCLEMERGE						= "CycleMerge";
	public static final String	EVENT_CYCLESPLIT						= "CycleSplit";
	public static final String	EVENT_MEASUREDATARESULT					= "MeasureDataResult";
	public static final String	EVENT_ABNORMALCONSUMABLE				= "비계획투입원료";
	public static final String	EVENT_MAKELOTCOMPLETED					= "MakeLotCompleted";
	public static final String	EVENT_MAKELOTCOMPLETECANCEL				= "MakeLotCompleteCancel";
	public static final String	EVENT_MAKELOTREWORK						= "MakeLotRework";
	public static final String	EVENT_MAKELOTMERGE						= "MakeLotMerge";
	public static final String	EVENT_MAKELOTSHIP						= "MakeLotShip";
	public static final String	EVENT_MAKELOTPACKING					= "MakeLotPacking";
	public static final String	EVENT_MAKELOTPACKINGLOWGRADE			= "MakeLotPackingLowGrade";
	public static final String	EVENT_MAKELOTHOLD						= "MakeLotHold";
	public static final String	EVENT_MAKELOTSCRAP						= "MakeLotScrap";
	public static final String	EVENT_CHANGEEQUIPMENT					= "ChangeEquipment";
	
	public static final String	EVENT_TRACKIN							= "TrackIn";
	public static final String	EVENT_TRACKINCANCEL						= "TrackInCancel";
	public static final String	EVENT_TRACKOUT							= "TrackOut";
	public static final String	EVENT_TRACKOUTCANCEL					= "TrackOutCancel";
	public static final String	EVENT_REPOSITION						= "Reposition";
	public static final String	EVENT_TRANSFER							= "Transfer";
	
	public static final String	EVENT_TYPE_HOLD							= "Hold";
	public static final String	EVENT_TYPE_RELEASE						= "Release";
	public static final String	EVENT_TYPE_SCRAP						= "Scrap";
	public static final String	EVENT_TYPE_UNSCRAP						= "Unscrap";
	
	// [Composed | Run | Completed]
	public static final String	GROUPSTATE_COMPOSED						= "Composed";
	public static final String	GROUPSTATE_RUN							= "Run";
	public static final String	GROUPSTATE_COMPLETED					= "Completed";
	
	// Consumable Stock Status
	public static final String	CONSUMABLE_AVAILABLE					= "Available";
	public static final String	CONSUMABLE_NOTAVAILABLE					= "NotAvailable";
	
	public static final String	LOTSTATE_CREATED						= "Created";
	public static final String	LOTSTATE_RELEASED						= "Released";
	public static final String	LOTSTATE_COMPLETED						= "Completed";
	public static final String	LOTSTATE_EMPTIED						= "Emptied";
	public static final String	LOTSTATE_SHIPPED						= "Shipped";
	public static final String	LOTSTATE_SCRAPPED						= "Scrapped";
	public static final String	LOTSTATE_TERMINATED						= "Terminated";
	public static final String	LOTPROCESSSTATE_TRACKIN					= "TrackIn";
	public static final String	LOTPROCESSSTATE_TRACKOUT				= "TrackOut";
	
	public static final String	SHIPPINGSTATE_WAIT						= "Wait";
	public static final String	SHIPPINGSTATE_PROD_COMPLETE				= "Prod_Complete";
	public static final String	SHIPPINGSTATE_SHIPPING					= "Shipping";
	public static final String	SHIPPINGSTATE_SHIPPED					= "Shipped";
	
	public static final String	TRANSFER_STATUS_REQUEST					= "Request";
	public static final String	TRANSFER_STATUS_COMPLETE				= "Complete";
	public static final String	TRANSFER_STATUS_CANCEL					= "Cancel";
	
	public static final String	LOTHOLDSTATE_NOTONHOLD					= "NotOnHold";
	public static final String	LOTHOLDSTATE_ONHOLD						= "OnHold";
	public static final String	LOTREWORKSTATE_NOTINREWORK				= "NotInRework";
	public static final String	LOTREWORKSTATE_INREWORK					= "InRework";
	
	public static final String	OPERATIONSTATE_WAITING					= "Waiting";
	public static final String	OPERATIONSTATE_COMPLETE					= "Complete";
	
	public static final String	RECIPESTATE_WAITING						= "Waiting";
	public static final String	RECIPESTATE_PROCESSING					= "Processing";
	public static final String	RECIPESTATE_COMPLETE					= "Complete";
	public static final String	RECIPESTATE_NONE						= "None";
	
	public static final String	RECIPEPARAMETERSTATE_WAITING			= "Waiting";
	public static final String	RECIPEPARAMETERSTATE_PROCESSING			= "Processing";
	public static final String	RECIPEPARAMETERSTATE_COMPLETE			= "Complete";

	public static final String	GROUPINGTYPE_EVENT						= "Event";
	public static final String	GROUPINGTYPE_STEP						= "Step";
	public static final String	GROUPINGTYPE_OPERATION					= "Operation";
	
	public static final String	RECIPE_ID_LIMIT							= "Limit";
	public static final String	RECIPE_ID_BOTH							= "Both";
	public static final String	RECIPE_ID_STATUS						= "Status";
	public static final String	RECIPE_ID_MEASURE						= "Measure";
	public static final String	RECIPE_ID_REPEAT						= "Repeat";
	public static final String	RECIPE_ID_CHANGE						= "Change";
	public static final String	RECIPE_ID_PACKING						= "Packing";
	public static final String	RECIPE_ID_ITEM							= "Item";
	public static final String	RECIPE_ID_CHECKITEM						= "CheckItem";
	public static final String	RECIPE_ID_CONSUMABLE					= "Consumable";
	public static final String	RECIPE_ID_ADDCONSUMABLE					= "AddConsumable";				// 원료추가투입
	public static final String	RECIPE_ID_RECYCLECONSUMABLE				= "RecycleConsumable";
	public static final String	RECIPE_ID_ABNORMALCONSUMABLE			= "AbnormalConsumable";
	public static final String	RECIPE_ID_CONSUMABLERESULT				= "ConsumableResult";
	public static final String	RECIPE_ID_CONDITION						= "Condition";
	public static final String	RECIPE_ID_SELECTROUTE					= "SelectRoute";
	public static final String	RECIPE_ID_FEEDINGMATERIAL				= "FeedingMaterial";
	public static final String	RECIPE_ID_ADDMATERIAL					= "AddMaterial";
	
	public static final String	RECIPEPARAMETER_ID_TIME					= "Time";
	
	public static final String	RECIPEPARAMETER_TYPE_RESULT				= "Result";
	public static final String	RECIPEPARAMETER_TYPE_SETTING			= "Setting";
	public static final String	RECIPEPARAMETER_TYPE_CONSUMABLE			= "Consumable";
	public static final String	RECIPEPARAMETER_TYPE_CONDITION			= "Condition";
	public static final String	RECIPEPARAMETER_TYPE_MEASURE			= "Measure";
	public static final String	RECIPEPARAMETER_TYPE_NONE				= "None";
	
	public static final String	RECIPE_PROCESSTYPE_OPERATION			= "Operation";
	public static final String	RECIPE_PROCESSTYPE_STEP					= "Step";
	public static final String	RECIPE_PROCESSTYPE_CONSUMABLE			= "Consumable";
	
	public static final String	LOTGRADE_GOOD							= "Good";
	public static final String	LOTGRADE_NG								= "NG";
	
	public static final String	LOTPROCESS_TYPE_BATCH					= "Batch";
	public static final String	LOTPROCESS_TYPE_LOT						= "Lot";
	public static final String	LOTPROCESS_TYPE_MIXB					= "Mix-B";
	
	public static final String	CONTROL_MODE_AUTO						= "Auto";
	public static final String	CONTROL_MODE_MANUAL						= "Manual";
	
	public static final String	OBJECTTYPE_OPERATION					= "Operation";
	public static final String	OBJECTTYPE_RECIPE						= "Recipe";
	public static final String	OBJECTTYPE_OPERATIONPARAMETER			= "OperationParameter";
	public static final String	OBJECTTYPE_RECIPEPARAMETER				= "RecipeParameter";
	
	
	// ProcessRouteType
	public static final String	PROCESSROUTETYPE_REP					= "Rep";
	public static final String	PROCESSROUTETYPE_MAIN					= "Main";
	public static final String	PROCESSROUTETYPE_PARTIAL				= "Partial";
	public static final String	PROCESSROUTETYPE_REWORK					= "Rework";
	public static final String	PROCESSROUTE_POLICYTYPE_MULTI			= "Multi";
	public static final String	PROCESSROUTE_POLICYTYPE_SINGLE			= "Single";
	
	public static final String	PROCESSROUTERELATION_CONCURRENCY		= "Concurrency";
	public static final String	PROCESSROUTERELATION_CONDITION			= "Condition";
	public static final String	PROCESSROUTERELATION_NORMAL				= "Normal";
	public static final String	PROCESSROUTERELATION_REWORK				= "Rework";
	
	// Rework
	public static final String	REWORKSTATE_PROCESSING					= "Processing";
	public static final String	REWORKSTATE_COMPLETE					= "Complete";
	
	
	/*
	 * ****************************** 영속성 자재 관리 ************************************
	 */
	// Durable State
	public static final String	DURABLE_PROCESSSTATE_WAIT				= "Wait";
	public static final String	DURABLE_PROCESSSTATE_PROCESSING			= "Processing";
	
	public static final String	DURABLE_STATE_CREATED					= "Created";
	public static final String	DURABLE_STATE_RELEASED					= "Released";
	public static final String	DURABLE_STATE_HOLD						= "Hold";
	public static final String	DURABLE_STATE_BROKEN					= "Broken";
	public static final String	DURABLE_STATE_TERMINATE					= "Terminate";
	public static final String	DURABLE_STATE_DIRTY						= "Dirty";
	
	
	/*
	 * ****************************** Sampling 관리 ************************************
	 */
	public static final String	SAMPLING_TYPE_COUNT						= "Count";
	public static final String	SAMPLING_TYPE_TARGET					= "Target";
	public static final String	SAMPLING_METHOD_LOTNUMBER				= "LotNumber";
	public static final String	SAMPLING_METHOD_ODD						= "Odd";
	public static final String	SAMPLING_METHOD_EVEN					= "Even";
	public static final String	SAMPLING_ANYTHING						= "Anything";
	
	public static final String	SAMPLING_POLICYTYPE_PRODUCT				= "Product";
	public static final String	SAMPLING_POLICYTYPE_PRODUCTGROUP		= "ProductGroup";
	
	
	/*
	 * ****************************** 예약 작업 관리 ************************************
	 */
	// STATUS
	public static final String	ACTION_STATE_RESERVE					= "Reserve";
	public static final String	ACTION_STATE_CANCEL						= "Cancel";
	public static final String	ACTION_STATE_COMPLETE					= "Complete";
	
	// TYPE
	public static final String	ACTION_TYPE_SCRAP						= "Scrap";
	public static final String	ACTION_TYPE_HOLD						= "Hold";
	
	
	/*
	 * ****************************** Port 관리 ************************************
	 */
	// ReadyToLoad | ReservedToLoad | PreparedToProcess | Processing | ReadyToUnload | ReservedToUnload
	public static final String	PORT_STATE_READYTOLOAD					= "ReadyToLoad";
	public static final String	PORT_STATE_RESERVEDTOLOAD				= "ReservedToLoad";
	public static final String	PORT_STATE_PREPAREDTOPROCESS			= "PreparedToProcess";
	public static final String	PORT_STATE_PROCESSING					= "Processing";
	public static final String	PORT_STATE_READYTOUNLOAD				= "ReadyToUnload";
	public static final String	PORT_STATE_RESERVEDTOUNLOAD				= "ReservedToUnload";
	
	public static final String	PORT_CONTROL_AUTO						= "Auto";
	public static final String	PORT_CONTROL_MANUAL						= "Manual";
	
	/*
	 * ****************************** 알람 관리 ************************************
	 */
	// 알람 상태 [ None | Process | Complete ]
	public static final String	ALARM_STATUS_NONE						= "None";
	public static final String	ALARM_STATUS_PROCESS					= "Process";
	public static final String	ALARM_STATUS_COMPLETE					= "Complete";
	
	/*
	 * ****************************** 사용관리 ************************************
	 */
	public static final String	ACTIVESTATE_ACTIVE						= "Active";
	public static final String	ACTIVESTATE_INACTIVE					= "Inactive";
	
	/*
	 * ****************************** BOM 관리 ************************************
	 */
	public static final String	BOMTYPE_MAIN							= "Main";
	public static final String	BOMTYPE_ALTERNATIVE						= "Alternative";
		
	/*
	 * ****************************** EQUIPMENT 관리 ************************************
	 */
	public static final String	EQUIPMENTSTATE_IDLE						= "Idle";
	public static final String	EQUIPMENTSTATE_RUN						= "Run";
	public static final String	EQUIPMENTSTATE_DOWN						= "Down";
	public static final String	EQUIPMENTSTATE_HOLD						= "Hold";
	public static final String	EQUIPMENTSTATE_PM						= "PM";
	
	public static final String EQUIPMENT_REFERENCETYPE_PMSCHEDULE		= "PMSchedule";
	
	public static final String	EQUIPMENT_BM_REQUEST					= "Request";
	public static final String	EQUIPMENT_BM_CANCEL						= "Cancel";
	public static final String	EQUIPMENT_BM_HOLD						= "Hold";
	public static final String	EQUIPMENT_BM_REJECT						= "Reject";
	public static final String	EQUIPMENT_BM_COMPLETE					= "Complete";
	
	/*
	 * ****************************** 설비보전자재 관리 ************************************
	 */
	// SparePartLot State
	public static final String	SPAREPART_LOTSTATE_WAITING				= "Waiting";
	public static final String	SPAREPART_LOTSTATE_INUSE     			= "InUse";
	public static final String	SPAREPART_LOTSTATE_BROKEN     			= "Broken";
	public static final String	SPAREPART_LOTSTATE_REPAIRING  			= "Repairing";
	public static final String	SPAREPART_LOTSTATE_TERMINATED  			= "Terminated";

	public static final String	SPAREPART_REFERENCETYPE_PMSCHEDULE		= "PMSchedule";
	
	/*
	 * ****************************** PM작업 관리 ************************************
	 */
	public static final String	PM_REFERENCETYPE_SPAREPARTLOTID 		= "SparePartLotID";
	public static final String 	PM_REFERENCETYPE_PMSTART	 			= "PMStart";
	public static final String 	PM_REFERENCETYPE_PMEND		 			= "PMEnd";
	public static final String 	PM_REFERENCETYPE_PMSTARTCANCEL 			= "PMStartCancel";
	public static final String 	PM_REFERENCETYPE_PMENDCANCEL 			= "PMEndCancel";
	
	/*
	 * ****************************** Input구분 ************************************
	 */
	public static final String 	INPUTTYPE_PC 		= "PC";
	public static final String 	INPUTTYPE_MOBILE	= "Mobile";
	
	/*
	 * ****************************** EIS 장비 ScanIDType구분 ************************************
	 */
	public static final String 	SCANIDTYPE_RFID 	= "RFID";
	public static final String 	SCANIDTYPE_BARCODE	= "BARCODE";
	
	/*
	 * ****************************** Input 결과 ************************************
	 */
	public static final String 	INPUTRESULT_OK 	= "OK";
	public static final String 	INPUTRESULT_NG	= "NG";
}
