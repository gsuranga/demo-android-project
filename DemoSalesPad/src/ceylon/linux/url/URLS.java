package ceylon.linux.url;

public class URLS {
	
//Live
	public static String prefix = "http://123.231.13.186/dimo_lanka";
//	public static String prefixxx= "http://192.168.0.157/dimo_lanka";
	
	//web
//	public static String prefix = "http://192.168.0.152/dimo_lanka";
//	public static String prefixxx= "http://192.168.0.157/dimo_lanka";
	
	/////
	
	// public static String prefix1 = "http://192.168.0.164/dimo_lanka_live";
	//public static String prefix = "http://gateway.ceylonlinux.com/dimo_lanka";
	

	// public static String send_json =
	// "http://192.168.1.168/dimo_lanka_live/android_service/insert_sales_order";
	public static String json_save="http://192.168.0.155/JSON%20save/json_save.php";
	
	
	public static String server_authorization = prefix
			+ "/android_service/getPassword";

	public static String get_item = prefix + "/android_service/item";

	public static String get_dealers = prefix + "/android_service/dealer";

	public static String get_invoice_info = prefix
			+ "/android_service/autoCreatePurchaseOrder";

	public static String insert_order = prefix
			+ "/android_service/insert_sales_order";

	public static String pending_payments = prefix
			+ "/android_service/getAllPendingPayments";

	public static String getDealerStock = prefix+ "/android_service/getDealerStock";
	
	public static String get_vehicle = prefix + "/android_service/get_vehicle";
	
	public static String get_garages = prefix + "/android_service/get_garages";
	
	public static String visit_purposes = prefix
			+ "/android_service/get_visit_purposes";
	
	public static String visit_catogery = prefix
			+ "/android_service/get_visit_categories";
	
	public static String visit_history = prefix
			+ "/android_service/getAllVisitHistory";
	
	public static String fleet_owners = prefix
			+ "/android_service/getAllFleetOwners";
	
	public static String new_shops = prefix + "/android_service/get_new_shops";
	
	public static String fast_moving = prefix
			+ "/android_service/getFastMoovingItemInArea";
	
	public static String nt_achived_targets = prefix
			+ "/android_service/getNotAchivedTargets";
	
	public static String send_payment_details = prefix
			+ "/android_service/getNotAchivedTargets";
	
	public static String upload_failures = prefix
			+ "/android_service/insert_product_failures";
	
	public static String upload_brands = prefix + "/android_service/branding";
	public static String send_cheque = prefix
			+ "/android_service/payment_details";
	public static String get_all_banks = prefix
			+ "/android_service/getAllBanks";
	public static String send_marketing_activity = prefix
			+ "/android_service/getAllBanks";

	/*
	 * public static String manage_order_webview = prefix +
	 * "/anroid_login/checkAuthentication";
	 */

	public static String manage_order_webview = prefix
			+ "/anroid_login/checkAuthentication";

	public static String android_web_auth = prefix
			+ "/anroid_login/checkAuthentication";

	public static String update_user_name = "http://192.168.1.200/TATA/TATA/anroid_login/checkAuthentication";

	public static String item_wise_web_view = prefix
			+ "/s_o_monthly_target/drawIndexAddTarget";

	public static String manage_garage_webview = prefix
			+ "/anroid_login/checkAuthentication";

	

	public static String invoice_web_view = prefix
			+ "/android_service/drawIndexAddTarget";
	
	public static String get_target_history = prefix
			+ "/android_service/getDealerTargetHistory";
	
	public static String send_line_item_wise_target = prefix+ "/android_service/MonthlyTarget";
	
	public static String send_competitor_parts = prefix
			+ "/android_service/comperiterPart";
	
	public static String update_dealer_location = prefix
			+ "/android_service/update_dealer_location";
	
	public static String send_tour_itenary = prefix
			+ "/android_service/insert_new_tour_itentary";
	
	public static String insert_cash_payments = prefix
			+ "/android_service/insert_cash_payment";
	
	public static String insert_cheque_payments = prefix
			+ "/android_service/insert_cheque_payment";
	
	
	public static String insert_bank_dep_payments = prefix
			+ "/android_service/insert_bank_deposit_payment";
	
	public static String insert_returns_payments=prefix
			+ "/android_service/insert_return_from_tab";
	
	public static String UPDATER = prefix + "/android_service/updateservice";

	public static String get_dealer_avg_mvmt = prefix
			+ "/android_service/avg_movement_item_dealer";
	
	public static String dealerwise_lost_sales = prefix
			+ "/anroid_login/checkAuthentication";
	
	public static String stock_movement_report = prefix
			+"/anroid_login/checkAuthentication";
	
	

}
