package ceylon.linux.db;

public class DataBaseStatements {

	public static String INSERT_ITEMS = "INSERT INTO  item (`item_id`, description,`item_part_no`,`selling_price`,total_stock_qty, status,time_stamp,avg_movement_in_area) VALUES (? , ? , ?, ?,?,? ,?,?)";

	public static String INSERT_DEALERS = "INSERT INTO  Dealer (`delar_id`, delar_account_no,`delar_name`,`delar_address`,status,discount_percentage,username,dealer_password ,shop_name,so_update_status,current_to,credit_limit,outstanding_amount,overdue_amount) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String INSERT_BILL = "INSERT INTO  Purchase_bill (`BillAmount`, dealer_id,  dealer_name,date_of_bill,lon,lat,b_level,status,discount_percentage,BillAmount_with_vat,tour_iternery_status,account_no,finish_status,remark_one,remark_two,is_call_order) VALUES (? ,?,?, ? ,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String INSERT_BILL_ITEMS = "REPLACE INTO  purchase_items (BillID, description,  item_id,qty,price,part_no,comment) VALUES (?,?,?,?,?,?,?)";

	public static String INSERT_DEALER = "INSERT INTO  NewDealer (`delar_name`, delar_address,  shop_name ) VALUES (? ,?, ?)";

	public static String DELETE_NEWLY_ADDED_DEALER = "DELETE  FROM NewDealer WHERE ID = ? ";

	public static String DELETE_PURCHASE_BILL = "DELETE  FROM Purchase_bill ";

	public static String DELETE_NEWLY_ADDED_GARAGE = "DELETE  FROM garages WHERE ID = ? ";

	public static String INSERT_CUSTOMER = "INSERT INTO  customer (`customer_name`, customer_address,  contact_no) VALUES (? ,?, ?)";

	public static String INSERT_VEHICALE = "INSERT OR IGNORE INTO  vehicale (`vehicale_no`, user_id,type ) VALUES (?,?,?)";

	public static String INSERT_GARAGE = "INSERT INTO  garages (`garage_name`, g_address,g_contact_no,nearest_dealer,remarks ) VALUES (?,?,?,?,?)";

	public static String INSERT_INVOICE_INFO = "REPLACE INTO sales_info (Dealer_ID,ItemID,Part_Number, Available_Stocks_at_the_Dealer,avg_monthly_sale,Total_Sales_for_last_30days, Stocklostsales,Valuelostsales,AverageDailyDemand,Daysbetweenorders,SuggestedQty,Available_Stocks_at_VSD,UnsuppliedOrderQtyfor90day,movement_in_area_per_month,Days_since_Last_Invoice_Date,Days_since_Last_PO_Date,Avg_monthly_requirement,Number_of_Items_invoice_for_past_01_month ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String INSERT_TARGET_DEALER = "INSERT INTO  target_dealer (account_no, current_to,  expected_increase_after_three_month,  campaign_ID ) VALUES (?,?,?,?)";

	public static String INSERT_DESCRIPTION_MARKETING_ACTIVITY = "INSERT INTO  description_table_marketing_activity (description,ecpu,qty,total ,campaign_ID) VALUES (?,?,?,?,?)";

	public static String INSERT_MARKETING_ACTIVITY = "INSERT INTO  marketing_activity (campaign_type,campaign_date,objective,material_required_for_ho,other_requirment_for_branch,location ,invitees,dimo_employees,no_of_employees,quotation) VALUES (?,?,?,?,?,?,?,?,?,?)";

	public static String INSERT_PENDING_PAYMENT = "REPLACE INTO  deliver_order (deliver_order_id,purchase_order_id,dealer_id,invoice_no,wip_no,total_amount,added_date,added_time ,due_date,status,accepted_by,cash_payment,cheque_payment,bank_dep_payment,total_payment,pending_amount,delar_account_no,total_paid_amount_with_unrealized_cheques,total_paid_amount_without_unrealized_cheques,total_pending_amount_with_unrealized_cheques,total_pending_amount_without_unrealized_cheques,realized_cheque_amount,unrealized_cheque_amount,return_amount,number_of_days,target_collection_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String SEND_PAYMENT = "INSERT INTO  PaymentSending(deliver_order_id,payment_mode,cash_payment,unrealized_cheque_payment,bank_dep_payment) VALUES (?,?,?,?,?)";

	public static String INSERT_CHEQUE_DETAILS = "INSERT INTO cheque_details(deliver_order_id,cheque_no,amount,bankname,realised_date,path) VALUES(?,?,?,?,?,?)";

	public static String INSERT_DEPOSIT_PAYMENT = "INSERT INTO deposit_payment(deliver_order_id,slip_no,amount,bankname,deposit_date,path) VALUES(?,?,?,?,?,?)";

	public static String INSERT_DEALER_STOCK = "REPLACE INTO dealer_stock(item_part_no,description,remaining_qty,last_stock_date,delar_id,delar_name,delar_account_no,avg_movement_at_dealer,avg_movement_in_area) VALUES(?,?,?,?,?,?,?,?,?)";

	public static String INSERT_TOUR_PLAN = "REPLACE INTO tour_plan(town,dealer,date,dealer_ID,status,reason) VALUES(?,?,?,?,?,?)";

	public static String INSERT_EXISTING_GARAGES = "INSERT INTO garages(g_id,`garage_name`, g_address,g_contact_no,nearest_dealer,remarks ) VALUES(?,?,?,?,?,?)";

	public static String INSERT_VISIT_PURPOSES = "REPLACE INTO visit_purposes_details(purpose_name,visit_purpose_id) VALUES(?,?)";

	public static String INSERT_VISIT_CATOGERIES = "REPLACE INTO visit_categories_details(category_name,visit_category_id) VALUES(?,?)";

	public static String INSERT_VISIT_HISTORY = "INSERT INTO visit_history(visit_by,visit_date,visit_purpose,description,select_id,visit_category) VALUES(?,?,?,?,?,?)";

	public static String INSERT_NEW_SHOP = "INSERT INTO new_shops_details(shop_id,shop_name) VALUES(?,?)";

	public static String INSERT_TOUR_ITERNERY = "INSERT INTO tour_iternary(catogery,outlet,purpose,district,town,route) VALUES(?,?,?,?,?,?)";

	public static String INSERT_BRANDS = "INSERT INTO branding(other_details,type,outlet_id,image_path, iternery_status) VALUES(?,?,?,?,?)";

	public static String INSERT_FAILURE = "INSERT INTO failure(type,outlet_id,part_no,failure,image_path) VALUES(?,?,?,?,?)";
	public static String INSERT_BANK = "REPLACE INTO banks(bank_id,bank_name) VALUES(?,?)";
//	public static String UPDATE_ORDER = "UPDATE   Purchase_bill SET BillAmount=?,status=?,BillAmount_with_vat=?,finish_status=? WHERE ID=?";


	//public static String INSERT_FAST_MOVING_ITEMS="INSERT INTO new_shops_details(item_id,item_part_no,description,quantity) VALUES(?,?)";
} 
 
 
 