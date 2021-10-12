package ceylon.linux.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

@SuppressLint("NewApi")
public class Dbworker extends DbHelper {
	SQLiteStatement Insert_ITEM = this.getWritableDatabase().compileStatement(
			DataBaseStatements.INSERT_ITEMS);

	public Dbworker(Context context) {
		super(context);

	}

	public boolean CheckDataAlreadyExist(String tablename, String[] columns,
			String Selection, String args[]) {
		boolean status = false;
		Cursor cur = getQuery(tablename, columns, Selection, args);
		if (cur.moveToFirst()) {
			status = true;
		}
		cur.close();
		return status;
	}

	public boolean getuser_availability() {

		String query = "SELECT SUM(id_user) FROM  tbl_user_details  ";

		Log.i("Query", query);
		Cursor data = this.getrawQuery(query, null);

		if (data.moveToFirst()) {
			int i = data.getInt(0);
			if (i >= 1) {
				data.close();
				return false;
			} else {
				data.close();
				return true;
			}

		} else {

			data.close();
			return true;

		}

	}

	public Cursor getuser_details() {
		String query = "SELECT * FROM  Purchase_bil  ";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public Cursor get_purchase_orders() {
		String query = "SELECT * FROM  Purchase_bill ";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public Cursor get_purchase_orders_NOT_SYNCED() {
		Log.i("ccccccccc", "ffff");
		String query = "SELECT * FROM  Purchase_bill where syncstatus =='NOT SYNC'  and status!= '5'";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public Cursor get_purchase_order_by_id(String id) {
		String query = "SELECT * FROM  Purchase_bill where  ID = " + id;
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public Cursor get_purchase_order_NOT_SYNCED_by_id(String id) {
		String query = "SELECT * FROM  Purchase_bill where syncstatus =='NOT SYNC' AND ID = "
				+ id;
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public Cursor get_purchase_items(String BillID) {

		String query = "SELECT * FROM  purchase_items  WHERE  BillID= ? ";
		String args[] = { BillID };
		Cursor cursor = this.getWritableDatabase().rawQuery(query, args);
		return cursor;
	}

	public Cursor get_all_new_dealers() {

		String query = "SELECT * FROM  Dealer  WHERE  so_update_status= 0 ";

		Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
		return cursor;
	}

	// select marketing activity detaisl

	public Cursor get_marketing_activity_details() {

		String query = "SELECT ma.* FROM marketing_activity AS ma INNER JOIN target_dealer  AS td ON  ma.ID=td.campaign_ID INNER JOIN description_table_marketing_activity as dtm ON ma.ID=dtm.campaign_ID    ";
		Cursor c = this.getWritableDatabase().rawQuery(query, null);
		return c;

	}

	// Select target dealers

	public Cursor get_target_dealers(String ID) {

		String[] param = { ID };
		String query = "SELECT * FROM target_dealer WHERE campaign_ID=?";
		Cursor c = this.getWritableDatabase().rawQuery(query, param);
		return c;

	}

	// Select description_table_marketing_activity
	public Cursor get_description_table_marketing_activity(String ID) {

		String[] param = { ID };
		String query = "SELECT * FROM description_table_marketing_activity WHERE campaign_ID=?";
		Cursor c = this.getWritableDatabase().rawQuery(query, param);
		return c;

	}

	public Cursor get_stcock_quantity_by_part_no(String ID) {

		String[] param = { ID };
		String query = "SELECT total_stock_qty FROM item WHERE item_id=?";
		Cursor c = this.getWritableDatabase().rawQuery(query, param);
		return c;

	}

	public void Save_ITEMS(String item_id, String item_part_no,
			String description, String selling_price, String total_stock_qty,
			String status, String time_stamp, String avg_movement_in_area) {

		Insert_ITEM.bindString(1, item_id);
		Insert_ITEM.bindString(3, item_part_no);
		Insert_ITEM.bindString(2, description);
		Insert_ITEM.bindString(4, selling_price);
		Insert_ITEM.bindString(5, total_stock_qty);
		Insert_ITEM.bindString(6, status);
		Insert_ITEM.bindString(7, time_stamp);
		Insert_ITEM.bindString(8, avg_movement_in_area);
		Insert_ITEM.executeInsert();

		// long Insert_PreviousStock_Id = Insert_ITEM.executeInsert();
		Insert_ITEM.close();
		// Log.i("Insert Item Is", String.valueOf(Insert_PreviousStock_Id));
	}

	public void save_dealers(String delar_id, String delar_account_no,
			String delar_name, String delar_address, String status,
			String discount_percentage, String username, String password,
			String shop_name, String so_update_status, String current_to,
			String credit_limit, String outstanding_amount,
			String overdue_amount) {

		SQLiteStatement Insert_DEALERS = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.INSERT_DEALERS);
		Insert_DEALERS.bindString(1, delar_id);
		Insert_DEALERS.bindString(2, delar_account_no);
		Insert_DEALERS.bindString(3, delar_name);
		Insert_DEALERS.bindString(4, delar_address);
		Insert_DEALERS.bindString(5, status);
		Insert_DEALERS.bindString(6, discount_percentage);
		Insert_DEALERS.bindString(7, username);
		Insert_DEALERS.bindString(8, password);
		Insert_DEALERS.bindString(9, shop_name);
		Insert_DEALERS.bindString(10, so_update_status);
		Insert_DEALERS.bindString(11, current_to);
		Insert_DEALERS.bindString(12, credit_limit);
		Insert_DEALERS.bindString(13, outstanding_amount);
		Insert_DEALERS.bindString(14, overdue_amount);
		Insert_DEALERS.executeInsert();

		Insert_DEALERS.close();

	}

	public Long save_purchase_bill(String BillAmount, String dealer_account_no,
			String dealer_name, String date_of_bill, String lon, String lat,
			String b_level, String status, String discount,
			String amount_with_vat, String tour_iternery_status,
			String account_no, String finish_status, String remark_one,
			String remark_two,String is_call_order) {
		SQLiteStatement Insert_DEALERS = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.INSERT_BILL);
		Insert_DEALERS.bindString(1, BillAmount);
		Insert_DEALERS.bindString(2, dealer_account_no);
		Insert_DEALERS.bindString(3, dealer_name);
		Insert_DEALERS.bindString(4, date_of_bill);
		Insert_DEALERS.bindString(5, lon);
		Insert_DEALERS.bindString(6, lat);
		Insert_DEALERS.bindString(7, b_level);
		Insert_DEALERS.bindString(8, status);
		Insert_DEALERS.bindString(9, discount);
		Insert_DEALERS.bindString(10, amount_with_vat);
		Insert_DEALERS.bindString(11, tour_iternery_status);
		Insert_DEALERS.bindString(12, account_no);
		Insert_DEALERS.bindString(13, finish_status);
		Insert_DEALERS.bindString(14, remark_one);
		Insert_DEALERS.bindString(15, remark_two);
		Insert_DEALERS.bindString(16, is_call_order);

		long INSERT_DEALERS_ID = Insert_DEALERS.executeInsert();
		Insert_DEALERS.close();
		// Log.i("insert bill id is", String.valueOf(INSERT_DEALERS_ID));
		return INSERT_DEALERS_ID;
	}

	public ArrayList<HashMap<String, String>> get_all_saved_purchaseOrders() {
		ArrayList<HashMap<String, String>> purchase_order_list = new ArrayList<HashMap<String, String>>();

		String query = "SELECT pb.ID,pb.BillAmount,pb.dealer_id,pb.dealer_name,pb.date_of_bill,pb.syncstatus,pb.BillAmount_with_vat,pb.status,de.delar_account_no,de.discount_percentage,"
				+ "de.credit_limit,de.outstanding_amount,de.overdue_amount  FROM Purchase_bill pb INNER JOIN Dealer de ON pb.dealer_id=de.delar_id WHERE pb.status= '5' ORDER BY pb.ID DESC";
		Cursor cur = this.getrawQuery(query, null);

		if (cur.moveToFirst()) {
			do {

				HashMap<String, String> purchase_order = new HashMap<String, String>();

				purchase_order.put("ID", cur.getString(0));
				purchase_order.put("BillAmount", cur.getString(1));
				purchase_order.put("dealer_id", cur.getString(2));
				purchase_order.put("dealer_name", cur.getString(3));
				purchase_order.put("date_of_bill", cur.getString(4));
				purchase_order.put("syncstatus", cur.getString(5));
				purchase_order.put("BillAmount_with_vat", cur.getString(6));
				purchase_order.put("finish_status", cur.getString(7));
				purchase_order.put("delar_account_no", cur.getString(8));
				purchase_order.put("dealer_discount", cur.getString(9));
				purchase_order.put("overdue_amount", cur.getString(12));
				purchase_order.put("outstanding_amount", cur.getString(11));
				purchase_order.put("credit_limit", cur.getString(10));

				Log.e("finish status", cur.getString(7));

				purchase_order_list.add(purchase_order);
			} while (cur.moveToNext());
		}
		cur.close();
		return purchase_order_list;
	}

	public void save_purchase_bill_items(String BILL_ID, String description,
			String item_id, String qty, String price, String part_no,
			String comment) {
		SQLiteStatement Insert_bill_items = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.INSERT_BILL_ITEMS);
		Insert_bill_items.bindString(1, BILL_ID);
		Insert_bill_items.bindString(2, description);
		Insert_bill_items.bindString(3, item_id);
		Insert_bill_items.bindString(4, qty);
		Insert_bill_items.bindString(5, price);
		Insert_bill_items.bindString(6, part_no);
		if (comment != null) {
			Insert_bill_items.bindString(7, comment);
		} else {
			Insert_bill_items.bindString(7, "");
		}

		long INSERT_BURCHASE_ITEM_ID = Insert_bill_items.executeInsert();
		Insert_bill_items.close();
		Log.i("InsertBillitemId    Is", String.valueOf(INSERT_BURCHASE_ITEM_ID));
	}

	public void delete_purchase_order_items_by_pur_id(String pur_id) {
		SQLiteStatement delete_bill_items = this
				.getWritableDatabase()
				.compileStatement("DELETE FROM purchase_items WHERE BillID = ?");

		delete_bill_items.bindString(1, pur_id);

		delete_bill_items.executeUpdateDelete();
	}

	public void save_dealer(String delar_name, String delar_address,
			String shop_name) {
		SQLiteStatement Insert_dealer = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.INSERT_DEALER);
		Insert_dealer.bindString(1, delar_name);
		Insert_dealer.bindString(2, delar_address);
		Insert_dealer.bindString(3, shop_name);

		long INSERT_DEALER = Insert_dealer.executeInsert();
		Insert_dealer.close();
		Log.i("InsertDealerId    Is", String.valueOf(INSERT_DEALER));

	}

	public long save_customer(String customer_name, String customer_address,
			String contact_no) {
		SQLiteStatement Insert_Customer = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.INSERT_CUSTOMER);
		Insert_Customer.bindString(1, customer_name);
		Insert_Customer.bindString(2, customer_address);
		Insert_Customer.bindString(3, contact_no);

		long INSERT_CUSTOMER = Insert_Customer.executeInsert();
		Insert_Customer.close();
		Log.i("insert customer id is", String.valueOf(INSERT_CUSTOMER));

		return INSERT_CUSTOMER;

	}

	public void save_garages(String g_name, String g_address,
			String g_contact_no, String nearest_dealer, String remarks) {

		SQLiteStatement Insert_Garage = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.INSERT_GARAGE);
		Insert_Garage.bindString(1, g_name);
		Insert_Garage.bindString(2, g_address);
		Insert_Garage.bindString(3, g_contact_no);
		Insert_Garage.bindString(4, nearest_dealer);
		Insert_Garage.bindString(5, remarks);

		long INSERT_GARAGE = Insert_Garage.executeInsert();
		Insert_Garage.close();
		Log.i("insert customer id is", String.valueOf(INSERT_GARAGE));

	}

	public void save_vehicale(String vehicale_no, String user_id, String type) {
		SQLiteStatement Insert_Vehicale = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.INSERT_VEHICALE);
		Insert_Vehicale.bindString(1, vehicale_no);
		Insert_Vehicale.bindString(2, user_id);
		Insert_Vehicale.bindString(3, type);

		Insert_Vehicale.execute();
		Insert_Vehicale.close();

	}

	// save banks
	public void save_banks(String bank_id, String bank_name) {
		SQLiteStatement Insert_banks = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.INSERT_BANK);
		Insert_banks.bindString(1, bank_id);
		Insert_banks.bindString(2, bank_name);

		Insert_banks.execute();
		Insert_banks.close();

	}

	public Cursor get_single_item() {
		Log.d("get_items", "working");
		String strqu = "SELECT  * FROM  item WHERE description = 'TE282967100101'";
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, null);// 2
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Log.i(cursor.getString(1), cursor.getString(2) + " SSSSSS");
		}
		return cursor;

	}

	public void delete_newly_added_dealer(String dealer_id) {

		SQLiteStatement delete_dealer = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.DELETE_NEWLY_ADDED_DEALER);
		delete_dealer.bindString(1, dealer_id);
		delete_dealer.execute();
		delete_dealer.close();
	}

	public void delete_purchase_order_by_id(String id) {
		SQLiteStatement delete_purorder_Statement = this.getWritableDatabase()
				.compileStatement("DELETE FROM Purchase_bill WHERE ID = " + id);
		delete_purorder_Statement.execute();
		delete_purorder_Statement.close();

	}

	public Cursor get_all_dealers() {

		String strqu = "SELECT  * FROM  Dealer";
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, null);
		return cursor;

	}

	public Cursor get_all_garages() {

		String strqu = "SELECT  * FROM  garages";
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, null);
		return cursor;

	}

	public Cursor get_all_items() {
		String strqu = "SELECT  * FROM item ORDER BY description ASC;";
		// String[] arg = { status };
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, null);
		
		return cursor;
	}

	public Cursor get_all_suggestion_orders(String status) {
		String strqu = "SELECT  Dealer.delar_name,Dealer.username,Dealer.dealer_password,Dealer.delar_account_no,Purchase_bill.ID,Purchase_bill.date_of_bill,Purchase_bill.BillAmount,Purchase_bill.BillAmount_with_vat,Purchase_bill.discount_percentage,Purchase_bill.dealer_id,Purchase_bill.account_no FROM  Purchase_bill, Dealer where Purchase_bill.dealer_id = Dealer.delar_id AND Purchase_bill.status=?";

		String[] arg = { status };
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, arg);
		return cursor;
	}

	public Cursor get_all_call_orders(String status) {
		String strqu = "SELECT  Dealer.delar_name,Dealer.username,Dealer.dealer_password,Dealer.delar_account_no,Purchase_bill.ID,Purchase_bill.date_of_bill,Purchase_bill.BillAmount,Purchase_bill.BillAmount_with_vat,Purchase_bill.discount_percentage,Purchase_bill.dealer_id,Purchase_bill.account_no FROM  Purchase_bill, Dealer where Purchase_bill.dealer_id = Dealer.delar_id ";

		String[] arg = null;
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, arg);
		return cursor;
	}

	public Cursor get_all_newly_added_dealers() {
		String strqu = "SELECT  * FROM  NewDealer";
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, null);
		return cursor;
	}

	public Cursor get_all_newly_added_garages() {
		String strqu = "SELECT  * FROM  garages";
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, null);
		return cursor;

	}

	public Cursor get_username_password_of_dealer(String dealer_id) {

		String strqu = "SELECT  * FROM    Dealer" + " WHERE  delar_id= ?";
		String args[] = { dealer_id };
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, args);
		return cursor;
	}

	public Cursor get_all_newly_customer() {
		String strqu = "SELECT  * FROM customer";
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, null);
		return cursor;

	}

	public Cursor get_vehicales(String user_id) {
		String strqu = "SELECT  * FROM    vehicale" + " WHERE  user_id= ?";

		String args[] = { user_id };
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, args);
		return cursor;
	}

	public Cursor get_all_vehicales() {
		String strqu = "SELECT  * FROM    vehicale   where type !=3";

		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, null);
		return cursor;
	}

	public Cursor get_all_fleet_vehicales() {
		String strqu = "SELECT  * FROM    vehicale where type =3";

		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, null);
		return cursor;
	}

	public Cursor get_all_invoice_info(String DealerId) {
		String strqu = "SELECT  * FROM  sales_info WHERE Dealer_ID=? ORDER BY SuggestedQty desc";
		String param[] = { DealerId };
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, param);
		Log.e("Db Worker", "LIne 496");
		return cursor;

	}

	public Cursor get_marketing_activities() {
		String strqu = "SELECT  * FROM   target_dealer ";
		Cursor cursor = this.getWritableDatabase().rawQuery(strqu, null);
		return cursor;

	}

	public Cursor get_pending_orders() {
		String query = "SELECT * FROM  Purchase_bill WHERE status=0";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public Cursor get_visit_categories_details() {
		String query = "SELECT * FROM  visit_categories_details";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public Cursor get_purposes() {
		String query = "SELECT * FROM  visit_purposes_details";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public void delete_newly_added_garage(String g_id) {

		SQLiteStatement delete_dealer = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.DELETE_NEWLY_ADDED_GARAGE);
		delete_dealer.bindString(1, g_id);
		delete_dealer.execute();
		delete_dealer.close();
	}

	public Cursor get_new_shopdetails() {

		String query = "SELECT * FROM  new_shops_details";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	// get All cheque_payment details to json purpose
	public Cursor get_cheque_payment_details() {
		String query1 = "SELECT cd.*,dor.iternery_status FROM deliver_order AS dor,cheque_details AS cd  WHERE dor.deliver_order_id=cd.deliver_order_id ";
		Cursor data = this.getrawQuery(query1, null);
		return data;

	}

	// get All deposit_payment details to json purpose
	public Cursor get_deposit_payment_details() {
		String query1 = "SELECT dp.*,dor.iternery_status FROM deliver_order AS dor,deposit_payment AS dp  WHERE dor.deliver_order_id=dp.deliver_order_id ";
		Cursor data = this.getrawQuery(query1, null);
		return data;

	}

	public Cursor get_cash_payment() {

		String query1 = "SELECT deliver_order_id,cash_payment,iternery_status FROM deliver_order";
		Cursor data = this.getrawQuery(query1, null);
		return data;

	}

	public Cursor get_fast_moving_items() {

		String query = "SELECT * FROM  fast_moving_items";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public Cursor get_brands() {

		String query = "SELECT * FROM  branding";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public Cursor getHistory(String id, String type) {
		String query = null;
		String args[] = { id };
		if (type.equals("1")) {

			query = "SELECT visit_history.*,Dealer.delar_name FROM visit_history,Dealer WHERE visit_history.select_id=Dealer.delar_id AND visit_category=?";

		} else if (type.equals("2")) {

			query = "SELECT visit_history.*, garages.garage_name FROM visit_history,garages WHERE visit_history.select_id=garages.g_id AND visit_category=?";

		} else if (type.equals("5")) {

			query = "SELECT visit_history.*,vehicale.vehicale_no FROM visit_history,vehicale WHERE visit_history.select_id=vehicale.vehicale_no  AND visit_category=? AND type !=3 ";

		} else if (type.equals("3")) {

			query = "SELECT visit_history.*,vehicale.vehicale_no FROM visit_history,vehicale WHERE visit_history.select_id=vehicale.vehicale_no  AND visit_category=? AND type =3 ";
		}
		Cursor data = this.getrawQuery(query, args);

		return data;

	}

	// SELECT ITEM DETAILS FROM iTEMS
	public Cursor get_item_details(String item_part_no) {
		String query = "SELECT * FROM  item WHERE description=?";
		String param[] = { item_part_no.trim() };

		Cursor data = this.getrawQuery(query, param);
		return data;
	}

	// get Failures
	public Cursor get_failures() {
		String query = "SELECT * FROM  failure";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	// get Failures
	public Cursor get_banks() {
		String query = "SELECT * FROM  banks";
		Cursor data = this.getrawQuery(query, null);
		return data;
	}

	public void save_invoice_info(HashMap<String, String> parameters) {

		SQLiteStatement insert_invoice_info_statement = this
				.getWritableDatabase().compileStatement(
						DataBaseStatements.INSERT_INVOICE_INFO);

		insert_invoice_info_statement.bindAllArgsAsStrings(new String[] {
				parameters.get("delar_id"), parameters.get("ItemID"),
				parameters.get("Part_Number"),
				parameters.get("Available_Stocks_at_the_Dealer"),
				parameters.get("avg_monthly_sale"),
				parameters.get("Total_Sales_for_last_30days"),
				parameters.get("Stocklostsales"),
				parameters.get("Valuelostsales"),
				parameters.get("AverageDailyDemand"),
				parameters.get("Daysbetweenorders"),
				parameters.get("SuggestedQty"),
				parameters.get("Available_Stocks_at_VSD"),
				parameters.get("UnsuppliedOrderQtyfor90day"),
				parameters.get("movement_in_area_per_month"),
				parameters.get("Days_since_Last_Invoice_Date"),
				parameters.get("Days_since_Last_PO_Date"),
				parameters.get("Avg_monthly_requirement"),
				parameters.get("Number_of_Items_invoice_for_past_01_month") });

		insert_invoice_info_statement.executeInsert();
		insert_invoice_info_statement.close();

	}

	public void insert_targeet_dealer_for_marketing_activities(
			String account_no, String current_to,
			String expected_increase_after_three_month,
			String marketing_activity_id) {
		SQLiteStatement insert_target_dealer = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.INSERT_TARGET_DEALER);
		insert_target_dealer.bindString(1, account_no);
		insert_target_dealer.bindString(2, current_to);
		insert_target_dealer.bindString(3, expected_increase_after_three_month);
		insert_target_dealer.bindString(4, marketing_activity_id);

		long executeInsert = insert_target_dealer.executeInsert();
		Log.i("ID is", executeInsert + "");
		insert_target_dealer.close();
	}

	public void insert_description_for_marketing_activities(String description,
			String ecpu, String qty, String total, String marketing_activity_id) {
		SQLiteStatement insert_description_for_marketing_activities = this
				.getWritableDatabase()
				.compileStatement(
						DataBaseStatements.INSERT_DESCRIPTION_MARKETING_ACTIVITY);
		insert_description_for_marketing_activities.bindString(1, description);
		insert_description_for_marketing_activities.bindString(2, ecpu);
		insert_description_for_marketing_activities.bindString(3, qty);
		insert_description_for_marketing_activities.bindString(4, total);
		insert_description_for_marketing_activities.bindString(5,
				marketing_activity_id);
		insert_description_for_marketing_activities.executeInsert();
		insert_description_for_marketing_activities.close();
	}

	public long insert_marketing_activities(String campaign_type,
			String campaign_date, String objective,
			String material_required_for_ho,
			String other_requirment_for_branch, String location,
			String invitees, String dimo_employees, String no_of_employees,
			String quotation) {
		SQLiteStatement insert_marketing_activities = this
				.getWritableDatabase().compileStatement(
						DataBaseStatements.INSERT_MARKETING_ACTIVITY);
		insert_marketing_activities.bindString(1, campaign_type);
		insert_marketing_activities.bindString(2, campaign_date);
		insert_marketing_activities.bindString(3, objective);
		insert_marketing_activities.bindString(4, material_required_for_ho);
		insert_marketing_activities.bindString(5, other_requirment_for_branch);
		insert_marketing_activities.bindString(6, location);
		insert_marketing_activities.bindString(7, invitees);
		insert_marketing_activities.bindString(8, dimo_employees);
		insert_marketing_activities.bindString(9, no_of_employees);
		insert_marketing_activities.bindString(10, quotation);

		long executeInsert = insert_marketing_activities.executeInsert();
		Log.i("Marketing actvity id is", executeInsert + "");
		insert_marketing_activities.close();
		return executeInsert;

	}

	public void insert_pending_payment(String deliver_order_id,
			String purchase_order_id, String dealer_id, String invoice_no,
			String wip_no, String total_amount, String added_date,
			String added_time, String due_date, String status,
			String accepted_by, String cash_payment, String cheque_payment,
			String bank_dep_payment, String total_payment,
			String pending_amount, String accountno,
			String total_paid_amount_with_unrealized_cheques,
			String total_paid_amount_without_unrealized_cheques,
			String total_pending_amount_with_unrealized_cheques,
			String total_pending_amount_without_unrealized_cheques,
			String realized_cheque_amount, String unrealized_cheque_amount,
			String return_amount, String number_of_days,String target_collection_date) {

		SQLiteStatement insert_pending_payment = this.getWritableDatabase()
				.compileStatement(DataBaseStatements.INSERT_PENDING_PAYMENT);
		insert_pending_payment.bindString(1, deliver_order_id);
		insert_pending_payment.bindString(2, purchase_order_id);
		insert_pending_payment.bindString(3, dealer_id);
		insert_pending_payment.bindString(4, invoice_no);
		insert_pending_payment.bindString(5, wip_no);
		insert_pending_payment.bindString(6, total_amount);
		insert_pending_payment.bindString(7, added_date);
		insert_pending_payment.bindString(8, added_time);
		insert_pending_payment.bindString(9, due_date);
		insert_pending_payment.bindString(10, status);
		insert_pending_payment.bindString(11, accepted_by);
		insert_pending_payment.bindString(12, cash_payment);
		insert_pending_payment.bindString(13, cheque_payment);
		insert_pending_payment.bindString(14, bank_dep_payment);
		insert_pending_payment.bindString(15, total_amount);
		insert_pending_payment.bindString(16, pending_amount);
		insert_pending_payment.bindString(17, accountno);

		insert_pending_payment.bindString(18,
				total_paid_amount_with_unrealized_cheques);
		insert_pending_payment.bindString(19,
				total_paid_amount_without_unrealized_cheques);
		insert_pending_payment.bindString(20,
				total_pending_amount_with_unrealized_cheques);
		insert_pending_payment.bindString(21,
				total_pending_amount_without_unrealized_cheques);
		insert_pending_payment.bindString(22, realized_cheque_amount);
		insert_pending_payment.bindString(23, unrealized_cheque_amount);
		insert_pending_payment.bindString(24, return_amount);
		insert_pending_payment.bindString(25, number_of_days);
		insert_pending_payment.bindString(26, target_collection_date);
		insert_pending_payment.executeInsert();
		Log.i("unrealized_cheque_amount", unrealized_cheque_amount + "");

	}

	public void delete_pending_payment_data() {
		this.getWritableDatabase()
				.compileStatement("DELETE FROM deliver_order")
				.executeUpdateDelete();
	}

	public void update_sync_status(String bill_id) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement update = writableDatabase
				.compileStatement("update purchase_bill set syncstatus='SYNC' where ID=?");
		update.bindString(1, bill_id);
		update.executeInsert();
	}

	public void update_to_accepted_order(String bill_id, String status) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement update = writableDatabase
				.compileStatement("update purchase_bill set  status=? where ID=?");
		update.bindString(1, status);
		update.bindString(2, bill_id);
		update.executeInsert();
	}

	public void update_set_priority(String ID, String priority) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement update = writableDatabase
				.compileStatement("update marketing_activity set  priority=? where ID=?");
		update.bindString(1, priority);
		update.bindString(2, ID);
		update.executeInsert();
	}

	// pending payment queries
	public Cursor get_pending_orders(String dealer_id, String start, String end) {
		String query = null;
		Cursor cursor = null;

		if (start.equals("") || end.equals("") || start == null || end == null) {

			query = "SELECT Dealer.delar_name, deliver_order.deliver_order_id,  deliver_order.purchase_order_id , deliver_order.invoice_no ,deliver_order.added_date,deliver_order.total_amount,deliver_order.added_time,deliver_order.wip_no, deliver_order.due_date,deliver_order.status,deliver_order.accepted_by,deliver_order.cash_payment,deliver_order.cheque_payment,deliver_order.bank_dep_payment,deliver_order.total_payment,deliver_order.pending_amount,Dealer.shop_name,deliver_order.total_paid_amount_with_unrealized_cheques,deliver_order.total_paid_amount_without_unrealized_cheques,deliver_order.total_pending_amount_with_unrealized_cheques,deliver_order.total_pending_amount_without_unrealized_cheques ,deliver_order.realized_cheque_amount , deliver_order.unrealized_cheque_amount,deliver_order.return_amount ,deliver_order.number_of_days FROM  deliver_order inner join Dealer on Dealer.delar_account_no=deliver_order.delar_account_no where deliver_order.delar_account_no= ?";

			String args[] = { dealer_id };

			cursor = this.getWritableDatabase().rawQuery(query, args);
			Log.i("Size of the cursor is",
					"Size of the cursor is" + cursor.getCount());
		} else {

			query = "SELECT  Dealer.delar_name,deliver_order.deliver_order_id, deliver_order.purchase_order_id , deliver_order.invoice_no ,deliver_order.added_date,deliver_order.total_amount,deliver_order.added_time,deliver_order.wip_no, deliver_order.due_date,deliver_order.status,deliver_order.accepted_by,deliver_order.cash_payment,deliver_order.cheque_payment,deliver_order.bank_dep_payment,deliver_order.total_payment,deliver_order.pending_amount,Dealer.shop_name ,deliver_order.total_paid_amount_with_unrealized_cheques,deliver_order.total_paid_amount_without_unrealized_cheques,deliver_order.total_pending_amount_with_unrealized_cheques,deliver_order.total_pending_amount_without_unrealized_cheques,deliver_order.realized_cheque_amount, deliver_order.realized_cheque_amount ,deliver_order.unrealized_cheque_amount ,deliver_order.return_amount ,deliver_order.number_of_days FROM  deliver_order inner join Dealer on Dealer.delar_account_no=deliver_order.delar_account_no where deliver_order.delar_account_no= ? AND deliver_order.added_date BETWEEN  ? and ?";
			String args[] = { dealer_id, start, end };

			cursor = this.getWritableDatabase().rawQuery(query, args);
		}

		return cursor;
	}

	public void update_pending_orders(String cash_payment,
			String cheque_payment, String bank_dep_payment,
			String pending_amount, String deliver_order_id,
			String iternery_status) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement update = writableDatabase
				.compileStatement("update deliver_order set cash_payment=(cash_payment+?),  cheque_payment=(cheque_payment+?),  bank_dep_payment=(bank_dep_payment+?),pending_amount=?,iternery_status=? where deliver_order_id=?");

		update.bindString(1, cash_payment);
		update.bindString(2, cheque_payment);
		update.bindString(3, bank_dep_payment);
		update.bindString(4, pending_amount);
		update.bindString(5, deliver_order_id);
		update.bindString(6, deliver_order_id);
		update.executeInsert();

	}

	// Insert cheque details

	public void insert_cheque_details(String deliver_order_id,
			String cheque_no, String amount, String bankname,
			String realizeddate, String path) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement update = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_CHEQUE_DETAILS);
		update.bindString(1, deliver_order_id);
		update.bindString(2, cheque_no);
		update.bindString(3, amount);
		update.bindString(4, bankname);
		update.bindString(5, realizeddate);
		update.bindString(6, path);

		update.executeInsert();

	}

	// Insert deposit details
	public void insert_deposit_details(String deliver_order_id, String slip_no,
			String amount, String bankname, String deposit_date, String path) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement update = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_DEPOSIT_PAYMENT);
		update.bindString(1, deliver_order_id);
		update.bindString(2, slip_no);
		update.bindString(3, amount);
		update.bindString(4, bankname);
		update.bindString(5, deposit_date);
		update.bindString(6, path);

		update.executeInsert();

	}

	public Cursor getallunsynchronizedtouritenary() {
		String query = null;
		Cursor cursor = null;

		query = "SELECT * FROM tour_itenary WHERE status=0";

		cursor = this.getReadableDatabase().rawQuery(query, null);

		return cursor;

	}

	public Cursor getAllUnsynchronizedtbl_garage_loyaltyPayments() {
		String query = null;
		Cursor cursor = null;

		query = "SELECT * FROM tbl_garage_loyalty WHERE sync_status=0";

		cursor = this.getReadableDatabase().rawQuery(query, null);

		return cursor;
	}

	public Cursor getAllUnsynchronizedChequePayments() {
		String query = null;
		Cursor cursor = null;

		query = "SELECT * FROM tbl_cheque_payment WHERE sync_status=0";

		cursor = this.getReadableDatabase().rawQuery(query, null);

		return cursor;
	}

	public Cursor getAllUnsynchronizedBdepPayments() {
		String query = null;
		Cursor cursor = null;

		query = "SELECT * FROM tbl_bank_deposit_payment WHERE sync_status=0";

		cursor = this.getReadableDatabase().rawQuery(query, null);

		return cursor;
	}

	
	public Cursor getAllUnsynchronizedPaymentsReturnItems() {
		String query = null;
		Cursor cursor = null;

		query = "SELECT * FROM payment_return_item WHERE sync_status=0";

		cursor = this.getReadableDatabase().rawQuery(query, null);

		return cursor;
	}
	// Insert dealer stock
	public void dealer_stock(String item_part_no, String description,
			String remaining_qty, String last_stock_date, String delar_id,
			String delar_name, String delar_account_no,
			String avg_movement_at_dealer, String avg_movement_in_area) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement update = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_DEALER_STOCK);
		update.bindString(1, item_part_no);
		update.bindString(2, description);
		update.bindString(3, remaining_qty);
		update.bindString(4, last_stock_date);
		update.bindString(5, delar_id);
		update.bindString(6, delar_name);
		update.bindString(7, delar_account_no);
		update.bindString(8, avg_movement_at_dealer);
		update.bindString(9, avg_movement_in_area);

		update.executeInsert();

	}

	// find dealer stock
	public Cursor getDealerStockByDealerAccountNumber(String DealerAccountNumber) {

		String query = "SELECT * FROM dealer_stock where delar_account_no=?";
		String args[] = { DealerAccountNumber };
		Cursor cursor = this.getWritableDatabase().rawQuery(query, args);
		return cursor;

	}

	public Cursor findDealerStockForItem(String DealerAccountNumber,
			String ItemID) {
		String query = "SELECT avg_movement_at_dealer,avg_movement_in_area FROM dealer_stock where delar_account_no=? AND item_part_no=?";
		String args[] = { DealerAccountNumber, ItemID };
		Cursor cursor = this.getWritableDatabase().rawQuery(query, args);
		return cursor;
	}

	public Cursor findAvgMovementInArea(String ItemID) {
		String query = "SELECT avg_movement_in_area FROM item where item_part_no=? ";
		String args[] = { ItemID };
		Cursor cursor = this.getWritableDatabase().rawQuery(query, args);
		return cursor;
	}

	// save tour plan
	public void Save_tour_plan(String town, String dealer, String date,
			String dealer_ID, String status, String reason) {

		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement update = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_TOUR_PLAN);
		update.bindString(1, town);
		update.bindString(2, dealer);
		update.bindString(3, date);
		update.bindString(4, dealer_ID);
		update.bindString(5, status);
		update.bindString(6, reason);

		update.executeInsert();

	}

	// Update purchase order

	public void Update_purchase_order(String orderid, String bill_amount,
			String status, String amount_with_vat, String finish_status,
			String lon, String lat,String timestamp,String remark1,String remark2,String bat_level,String is_call_order) {

		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement update = writableDatabase
				.compileStatement("UPDATE   Purchase_bill SET BillAmount=?,status=?,BillAmount_with_vat=?,finish_status=?,lon=?,lat=?,date_of_bill=?,remark_one=?,remark_two=?,b_level=?,is_call_order=? WHERE ID=?");
		update.bindString(1, bill_amount);
		update.bindString(2, status);

		update.bindString(3, amount_with_vat);

		update.bindString(4, finish_status);
		update.bindString(5, lon);
		update.bindString(6, lat);
		update.bindString(7, timestamp);
		update.bindString(8, remark1);
		update.bindString(9, remark2);
		update.bindString(10, bat_level);
		update.bindString(11, is_call_order);
		update.bindString(12, orderid);
		update.executeUpdateDelete();

	}
	
	

	// update tour plan
	public void update_tour_plan(String town, String dealer, String date,
			String dealer_ID, String ID) {

		SQLiteDatabase writableDatabase = this.getWritableDatabase();

		SQLiteStatement update = writableDatabase
				.compileStatement("update tour_plan set town=?,dealer=?,date=?,dealer_ID=? where ID=?");
		update.bindString(1, town);
		update.bindString(2, dealer);
		update.bindString(3, date);
		update.bindString(4, dealer_ID);
		update.bindString(5, ID);

		update.executeInsert();

	}

	// get all tour plans
	public Cursor get_tour_plan() {

		String query = "SELECT * FROM tour_plan ";

		Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
		return cursor;

	}

	// delete tour plans

	public void DeleteTourPlan(String ID) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement delete_tour_plan = writableDatabase
				.compileStatement("DELETE  FROM tour_plan where ID=?");
		delete_tour_plan.bindString(1, ID);
		delete_tour_plan.execute();

	}

	// save garges

	public void save_existing_garages(String g_id, String g_name,
			String g_address, String g_contact_no, String nearest_dealer,
			String remarks) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement save_existing_garages = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_EXISTING_GARAGES);
		save_existing_garages.bindString(1, g_id);
		save_existing_garages.bindString(2, g_name);
		save_existing_garages.bindString(3, g_address);
		save_existing_garages.bindString(4, g_contact_no);
		save_existing_garages.bindString(5, nearest_dealer);
		save_existing_garages.bindString(6, remarks);
		save_existing_garages.execute();

	}

	// save visit purposes
	public void save_vist_purposes(String purpose_name, String visit_purpose_id) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement save_vist_purposes = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_VISIT_PURPOSES);
		save_vist_purposes.bindString(1, purpose_name);
		save_vist_purposes.bindString(2, visit_purpose_id);

		save_vist_purposes.execute();

	}

	// save visit categories

	public void save_visit_categories_details(String category_name,
			String visit_category_id) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement save_vist_catogeries = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_VISIT_CATOGERIES);
		save_vist_catogeries.bindString(1, category_name);
		save_vist_catogeries.bindString(2, visit_category_id);
		save_vist_catogeries.execute();

	}

	// save visit history
	public void save_visit_history(String visit_by, String visit_date,
			String visit_purpose, String description, String select_id,
			String catogery_id) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement save_vist_catogeries = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_VISIT_HISTORY);
		save_vist_catogeries.bindString(1, visit_by);
		save_vist_catogeries.bindString(2, visit_date);
		save_vist_catogeries.bindString(3, visit_purpose);
		save_vist_catogeries.bindString(4, description);
		save_vist_catogeries.bindString(5, select_id);
		save_vist_catogeries.bindString(6, catogery_id);
		save_vist_catogeries.execute();

	}

	// save new shops

	public void save_new_shops(String shop_id, String shop_name) {
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement save_vist_catogeries = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_NEW_SHOP);
		save_vist_catogeries.bindString(1, shop_id);
		save_vist_catogeries.bindString(2, shop_name);

		save_vist_catogeries.execute();

	}

	// save fast moving items in the database
	public void save_fast_oving_items(String item_id, String item_part_no,
			String description, String quantity) {

		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement save_vist_catogeries = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_NEW_SHOP);
		save_vist_catogeries.bindString(1, item_id);
		save_vist_catogeries.bindString(2, item_part_no);
		save_vist_catogeries.bindString(1, description);
		save_vist_catogeries.bindString(2, quantity);

		save_vist_catogeries.execute();

	}

	// save tour iternary
	public void save_tour_iternary(String catogery, String outlet,
			String purpose, String district, String town, String route) {

		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement save_tour_iternaery = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_TOUR_ITERNERY);
		save_tour_iternaery.bindString(1, catogery);
		save_tour_iternaery.bindString(2, outlet);
		save_tour_iternaery.bindString(3, purpose);
		save_tour_iternaery.bindString(4, district);
		save_tour_iternaery.bindString(5, town);
		save_tour_iternaery.bindString(6, route);

		save_tour_iternaery.execute();
		save_tour_iternaery.close();

	}

	// save Brandings
	public void save_tour_branding(String other_details, String type,
			String outlet_id, String image_path, String iternery_status) {

		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement save_brands = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_BRANDS);
		save_brands.bindString(1, other_details);
		save_brands.bindString(2, type);
		save_brands.bindString(3, outlet_id);
		save_brands.bindString(4, image_path);
		save_brands.bindString(5, iternery_status);

		save_brands.execute();
		save_brands.close();

	}

	// Save Failures

	public void save_failure_parts(String type, String outlet_id,
			String part_no, String failure, String image_name) {

		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		SQLiteStatement insert_failure = writableDatabase
				.compileStatement(DataBaseStatements.INSERT_FAILURE);

		insert_failure.bindString(1, type);
		insert_failure.bindString(2, outlet_id);
		insert_failure.bindString(3, part_no);
		insert_failure.bindString(4, failure);
		insert_failure.bindString(5, image_name);

		insert_failure.execute();
		insert_failure.close();

	}

	public void dropTables() {
		String[] sqlQueries = { "DELETE FROM NewDealer",
				"DELETE FROM customer", "DELETE FROM vehicale",
				"DELETE FROM garages", "DELETE FROM target_dealer", "VACUUM" };
		SQLiteDatabase writableDatabase = this.getWritableDatabase();
		for (String query : sqlQueries) {
			writableDatabase.compileStatement(query).execute();
		}
		writableDatabase.close();
	}

	public int get_Max_id_target_table() {

		int i = 0;
		String query = "SELECT MAX(target_id) FROM target";
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		if (cur.moveToFirst()) {
			i = cur.getInt(0);
		}

		return i;
	}

	public int get_Max_id_competitor_table() {
		int i = 0;
		String query = "SELECT MAX(cp_id) FROM competitor_part";
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		if (cur.moveToFirst()) {
			i = cur.getInt(0);
		}

		return i;
	}

	public Cursor get_non_sync_target() {
		String query = "SELECT * FROM target WHERE `sync_status` = 0 ";
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		return cur;
	}
	
	

	public Cursor get_items_by_target_id(int target_id) {
		String query = "SELECT * FROM target_item WHERE target_id = "
				+ target_id;
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		return cur;
	}

	public Cursor get_item_by_item_id(int item_id) {
		String query = "SELECT * FROM item WHERE item_id = " + item_id;
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		return cur;
	}

	public void update_target_sync_status_by_target_id(int target_id) {
		String query = "UPDATE target SET sync_status = 1 WHERE target_id = ? ";
				
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		Object parameters[] = { target_id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
		statement.close();
	}

	public void update_compparts_sync_status_by_target_id(int target_id) {
		
		String query = "UPDATE competitor_part SET sync_status = 1 WHERE cp_id = ?";
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		Object parameters[] = { target_id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
		statement.close();
		
		
	}

	public Cursor get_non_sync_competitorParts() {
		String query = "SELECT * FROM competitor_part WHERE sync_status=0";
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		return cur;
	}

	public Cursor get_all_non_synchronized_dealers_locations() {
		String query = "SELECT delar_id,lat,long,dealer_password FROM Dealer WHERE sync_status = 0 ";
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		return cur;
	}

	public Cursor get_competitor_items_by_comp_id(int target_id) {
		String query = "SELECT * FROM competitor_part_item WHERE cp_id = "
				+ target_id;
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		return cur;
	}

	public Cursor item_sales_details(String dealer_id, String item_id) {
		String query = "SELECT Total_Sales_for_last_30days,movement_in_area_per_month FROM sales_info WHERE Dealer_ID = "
				+ dealer_id + " AND ItemID = " + item_id;
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		return cur;
	}

	public Cursor get_purchase_order_by_purchase_order_id(String id) {
		String query = "SELECT * FROM Purchase_bill WHERE ID = " + id;
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		return cur;
	}

	public Cursor get_purchase_order_items_by_id(String id) {
		String query = "SELECT * FROM purchase_items WHERE BillID = " + id;
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		return cur;
	}

	public void update_finish_status_by_purch_id(String id) {
		String query = "UPDATE Purchase_bill SET finish_status=1 WHERE ID = "
				+ id;
		SQLiteStatement statement = super.getWritableDatabase()
				.compileStatement(query);
		DbHandler.performExecuteUpdateDelete(statement, null);
	}

	public void update_purchase_item_by_id(String id, String comment, String qty) {
		String query = "UPDATE purchase_items SET qty=?,comment=? WHERE ID = ?";
		SQLiteStatement statement = super.getWritableDatabase()
				.compileStatement(query);
		String parameters[] = { qty, comment, id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
	}

	public void delete_purchase_item_by_id(String id) {
		String query = "DELETE FROM purchase_items  WHERE ID = ?";

		Log.w("sql", query);
		SQLiteStatement statement = super.getWritableDatabase()
				.compileStatement(query);
		String parameters[] = { id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);

	}

	public Cursor get_dealer_by_id(String id) {
		String query = "SELECT * FROM Dealer WHERE delar_id = " + id;
		Cursor cur = super.getReadableDatabase().rawQuery(query, null);
		return cur;
	}

	public void update_dealer_loc(String id, String lat, String longt) {
		String query = "UPDATE Dealer SET lat = ?,long =?,sync_status=0 WHERE delar_id = ?";
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		String parameters[] = { lat, longt, id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
		statement.close();
	}
	
	public void update_dealer_password(String id,String Password) {
		String query = "UPDATE Dealer SET dealer_password= ?,sync_status=0 WHERE delar_id = ?";
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		String parameters[] = { Password, id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
		statement.close();
	}
	

	public void update_sync_status_by_dealer_id(String id) {
		String query = "UPDATE Dealer SET sync_status = 1 WHERE delar_id = ?";
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		String parameters[] = { id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
		statement.close();
	}

	public void upadate_tbl_garage_loyalty_sync_status_by_ID(String id) {
		String query = "UPDATE tbl_garage_loyalty SET sync_status = 1 WHERE ID = ?";
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		String parameters[] = { id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
		statement.close();
		Log.wtf("zfdh", "fhdzh");
	}

	public void upadate_tour_itanery_sync_status_by_ID(String id) {
		String query = "UPDATE tour_itenary SET status = 1 WHERE ti_id = ?";
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		String parameters[] = { id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
		statement.close();
		Log.wtf("zfdh", "fhdzh");
	}

	public void upadate_cheque_payment_sync_status_by_ID(String id) {
		String query = "UPDATE tbl_cheque_payment SET sync_status = 1 WHERE ID = ?";
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		String parameters[] = { id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
		statement.close();
		Log.wtf("zfdh", "fhdzh");
	}

	public void upadate_bank_dep_sync_status_by_ID(String id) {
		String query = "UPDATE tbl_bank_deposit_payment SET sync_status = 1 WHERE ID = ?";
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		String parameters[] = { id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
		statement.close();

	}public void upadate_payment_return_sync_status_by_ID(String id) {
		String query = "UPDATE payment_return_item SET sync_status = 1 WHERE ID = ?";
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		String parameters[] = { id };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
		statement.close();

	}

	public Cursor get_dealer_by_deler_name(String outlet_name) {
		String query = "SELECT * FROM Dealer WHERE delar_name LIKE '"
				+ outlet_name + "%' ";
		Cursor cursor = super.getReadableDatabase().rawQuery(query, null);
		return cursor;

	}

	public void insert_tour_itenary(int outlet_id, String outlet_name,
			String visit_category, String visit_purpose, String visit_time,
			String visit_date, String description) {
		String query = " INSERT INTO tour_itenary (outlet_id,outlet_name,visit_category,visit_purpose,visit_time,visit_date,description)  VALUES (?,?,?,?,?,?,?)";
		SQLiteDatabase database = super.getWritableDatabase();

		SQLiteStatement statement = database.compileStatement(query);
		Object parameters[] = { outlet_id, outlet_name, visit_category,
				visit_purpose, visit_time, visit_date, description };

		DbHandler.performExecuteUpdateDelete(statement, parameters);
		database.close();
	}

	public Cursor get_all_pending_payments() {

		String query = "SELECT  dorder.dealer_id,dorder.added_date,dorder.added_time,dorder.total_amount,outlet.delar_name,dorder.pending_amount,dorder.due_date,dorder.deliver_order_id,dorder.invoice_no "
				+ "FROM deliver_order dorder INNER JOIN Dealer outlet ON dorder.dealer_id=outlet.delar_id ORDER BY dorder.added_date ASC";
		Cursor cursor = super.getReadableDatabase().rawQuery(query, null);
		return cursor;
	}

	public int getCountpendingPayment() {
		int count = 0;
		String query = "SELECT  COUNT(dorder.dealer_id) "
				+ "FROM deliver_order dorder  ";
		Cursor cursor = super.getReadableDatabase().rawQuery(query, null);
		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
		cursor.close();
		return count;

	}

	public Cursor get_all_pending_payments_by_dealer_id_selected(
			String dealer_id) {

		String query = "SELECT  dorder.dealer_id,dorder.added_date,dorder.added_time,dorder.total_amount,outlet.delar_name,dorder.pending_amount,dorder.due_date,dorder.deliver_order_id,dorder.invoice_no "
				+ "FROM deliver_order dorder INNER JOIN Dealer outlet ON dorder.dealer_id=outlet.delar_id  WHERE dorder.dealer_id = "
				+ dealer_id + " ORDER BY dorder.added_date ASC ";
		Cursor cursor = super.getReadableDatabase().rawQuery(query, null);
		return cursor;
	}

	public Cursor get_pending_payments_by_dealer_id(String dealer_id) {
		String query = "SELECT * FROM deliver_order WHERE dealer_id = '"
				+ dealer_id + "' ";
		Cursor cursor = super.getReadableDatabase().rawQuery(query, null);
		return cursor;
	}

	public Cursor get_all_pending_payment_by_id(String do_id) {

		String query = "SELECT  dorder.invoice_no,dorder.wip_no,dorder.total_amount,"
				+ "dorder.total_paid_amount_without_unrealized_cheques,dorder.total_pending_amount_without_unrealized_cheques,"
				+ "dorder.due_date,dorder.target_collection_date FROM deliver_order dorder INNER JOIN Dealer outlet ON dorder.dealer_id=outlet.delar_id WHERE dorder.deliver_order_id = "
				+ do_id;
		Cursor cursor = super.getReadableDatabase().rawQuery(query, null);
		return cursor;
	}

	public int get_purchase_order_max_id() {
		String query = "SELECT MAX(ID) FROM Purchase_bill";
		Cursor cursor = super.getReadableDatabase().rawQuery(query, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		} else {
			return 0;
		}
	}

}
