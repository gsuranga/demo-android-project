package ceylon.linux.asynctask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.db.DbHandler;
import ceylon.linux.db.DbHelper;
import ceylon.linux.db.Dbworker;
import ceylon.linux.model.Item;
import ceylon.linux.model.Payment;
import ceylon.linux.model.SearchResponse;
import ceylon.linux.model.SearchResponsePayment;
import ceylon.linux.url.URLS;

public class DownloadDataFromServer extends AsyncTask<String, String, String> {

	protected ProgressDialog dialog;
	SharedPreferences userdata;
	private Context context;
	private ceylon.linux.controller.Jsonhelper jh;
	private String array[];
	private Dbworker dbworker;
	private DbHelper dbhelper;
	private ArrayList<NameValuePair> nameValuePairs;
	SharedPreferences sharedPreferences;
	private boolean stockUpdatingOnly;

	public DownloadDataFromServer(Context cxt, boolean stockUpdatingOnly) {
		context = cxt;
		dbhelper = new DbHelper(cxt);
		dbworker = new Dbworker(cxt);
		userdata = cxt.getSharedPreferences("USERDATA", Context.MODE_PRIVATE);
		sharedPreferences = cxt.getSharedPreferences("ITEM_TIMESTAMP",
				Context.MODE_PRIVATE);
		dialog = new ProgressDialog(context);
		this.stockUpdatingOnly = stockUpdatingOnly;

	}

	@Override
	protected String doInBackground(String... params) {
		array = params;
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("sales_officerID", userdata
				.getString("ID", "")));
		Log.d("sales_officerID",userdata
				.getString("ID", ""));
		jh = new Jsonhelper();
		if (!stockUpdatingOnly){
						
			insert_item_data();

			Save_Dealers(); 
		}
		Log.d("3/////////////////////////////////////////","////////////////////////////////////");
		insert_dealer_stock();
		Log.d("4/////////////////////////////////////////","////////////////////////////////////");
		/*garages_details();
		get_vehicle();
		save_purposes();
		save_vist_catogery();
		save_visit_history();
		FastMovingItems();
		save_shop();*/
		if (!stockUpdatingOnly){
			save_banks(); 
			// save_invoice_info();
		//	insert_pending_payment_data();		
			getAllPendingPayments();
		}
		
		
		
		return null;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		Log.d("1/////////////////////////////////////////","////////////////////////////////////");
		dialog = new ProgressDialog(context);
		dialog.setMessage("Downloading Data From Server");
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		Log.d("2/////////////////////////////////////////","////////////////////////////////////");
	}
	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);
		Log.d("5/////////////////////////////////////////","////////////////////////////////////");
		dbhelper.close();
		dbworker.close();
		try {
			dialog.dismiss();
		} catch (Exception e) {
			
		}
		Log.d("6/////////////////////////////////////////","////////////////////////////////////");
	}

	private void save_invoice_info() {
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("sales_officerID", userdata
				.getString("ID", "")));
		jh = new Jsonhelper();

		try {

			JSONObject jsonObject = jh
					.JsonObjectSendToServerPostWithNameValuePare(
							URLS.get_invoice_info, nameValuePairs);
			JSONArray jarray = jsonObject.getJSONArray("data");

			Log.e("invoice info", jarray.toString());

			for (int i = 0; i < jarray.length(); i++) {
				// JSONArray jsonsubArray = jarray.getJSONArray(i);

				// for (int j = 0; j < jsonsubArray.length(); j++) {
				JSONObject jsonobject = jarray.getJSONObject(i);
				String delar_id = jsonobject.getString("delar_id");
				String ItemID = jsonobject.getString("ItemID");
				String Part_Number = jsonobject.getString("Part_Number");
				String Available_Stocks_at_the_Dealer = jsonobject
						.getString("Available_Stocks_at_the_Dealer");
				String avg_monthly_sale = jsonobject
						.getString("avg_monthly_sale");
				String Total_Sales_for_last_30days = jsonobject
						.getString("Total_Sales_for_last_30days");
				String Stocklostsales = jsonobject.getString("Stocklostsales");
				String Valuelostsales = jsonobject.getString("Valuelostsales");
				String AverageDailyDemand = jsonobject
						.getString("AverageDailyDemand");
				String Daysbetweenorders = jsonobject
						.getString("Daysbetweenorders");
				String SuggestedQty = jsonobject.getString("SuggestedQty");
				String Available_Stocks_at_VSD = jsonobject
						.getString("Available_Stocks_at_VSD");
				String UnsuppliedOrderQtyfor90day = jsonobject
						.getString("UnsuppliedOrderQtyfor90day");
				String movement_in_area_per_month = jsonobject
						.getString("movement_in_area_per_month");
				String Days_since_Last_Invoice_Date = jsonobject
						.getString("Days_since_Last_Invoice_Date");
				String Days_since_Last_PO_Date = jsonobject
						.getString("Days_since_Last_PO_Date");
				String Avg_monthly_requirement = jsonobject
						.getString("Avg_monthly_requirement");
				String Number_of_Items_invoice_for_past_01_month = jsonobject
						.getString("Number_of_Items_invoice_for_past_01_month");

				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("delar_id", delar_id);
				parameters.put("ItemID", ItemID);
				parameters.put("Part_Number", Part_Number);
				parameters.put("Available_Stocks_at_the_Dealer",
						Available_Stocks_at_the_Dealer);
				parameters.put("avg_monthly_sale", avg_monthly_sale);
				parameters.put("Total_Sales_for_last_30days",
						Total_Sales_for_last_30days);
				parameters.put("Stocklostsales", Stocklostsales);
				parameters.put("Valuelostsales", Valuelostsales);
				parameters.put("AverageDailyDemand", AverageDailyDemand);
				parameters.put("Daysbetweenorders", Daysbetweenorders);
				parameters.put("SuggestedQty", SuggestedQty);
				parameters.put("Available_Stocks_at_VSD",
						Available_Stocks_at_VSD);
				parameters.put("UnsuppliedOrderQtyfor90day",
						UnsuppliedOrderQtyfor90day);
				parameters.put("movement_in_area_per_month",
						movement_in_area_per_month);
				parameters.put("Days_since_Last_Invoice_Date",
						Days_since_Last_Invoice_Date);
				parameters.put("Days_since_Last_PO_Date",
						Days_since_Last_PO_Date);
				parameters.put("Avg_monthly_requirement",
						Avg_monthly_requirement);
				parameters.put("Number_of_Items_invoice_for_past_01_month",
						Number_of_Items_invoice_for_past_01_month);
				dbworker.save_invoice_info(parameters);

				Log.i("ADA", "LLL");
			}
			

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private void Save_Dealers() {

		nameValuePairs = new ArrayList<NameValuePair>();
		String timestamp = "0";

		nameValuePairs.add(new BasicNameValuePair("timestamp", timestamp));
		nameValuePairs.add(new BasicNameValuePair("officer_id", userdata
				.getString("ID", "")));

		JSONObject jsonObject = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.get_dealers, nameValuePairs);

		JSONArray jarray;
		try {
			jarray = jsonObject.getJSONArray("data");
			Log.i("jsonArray is", jarray.toString());
			DbHelper dbHelper = new DbHelper(this.context);
			SQLiteDatabase database = dbHelper.getWritableDatabase();

			database.compileStatement("delete from Dealer")
					.executeUpdateDelete();
			String sql = "REPLACE INTO  Dealer (`delar_id`, delar_account_no,`delar_name`,`delar_address`,status,discount_percentage,username,dealer_password ,shop_name,so_update_status,current_to,credit_limit,outstanding_amount,overdue_amount,lat,long) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			SQLiteStatement statement = database.compileStatement(sql);

			for (int i = 0; i < jarray.length(); i++) {
				JSONObject jsonobject = jarray.getJSONObject(i);
				String delar_id = jsonobject.getString("delar_id");
				String delar_account_no = jsonobject
						.getString("delar_account_no");
				String delar_name = jsonobject.getString("delar_name");
				String delar_address = jsonobject.getString("delar_address");
				String status = jsonobject.getString("status");
				String discount_pescantage = jsonobject
						.getString("discount_percentage");
				String username = jsonobject.getString("username");
				String password = jsonobject.getString("password");
				String delar_shop_name = jsonobject
						.getString("delar_shop_name");
				String so_update_status = jsonobject
						.getString("so_update_status");
				String current_to = jsonobject.getString("current_to");
				String credit_limit = jsonobject.getString("credit_limit");
				String outstanding_amount = jsonobject
						.getString("outstanding_amount");
				String overdue_amount = jsonobject.getString("overdue_amount");
				String lat = jsonobject.getString("latitude");
				String longt = jsonobject.getString("longitude");

				Object[] parameters = { delar_id, delar_account_no, delar_name,
						delar_address, status, discount_pescantage, username,
						password, delar_shop_name, so_update_status,
						current_to, credit_limit, outstanding_amount,
						overdue_amount, lat, longt };

				DbHandler.performExecuteInsert(statement, parameters);

			}
		
		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void insert_dealer_stock() {
		Log.d("A/////////////////////////////////////////","////////////////////////////////////");
		nameValuePairs.add(new BasicNameValuePair("officer_id", userdata
				.getString("ID", "")));
		Log.d("officer_id",userdata.getString("ID", ""));
		Log.d("B/////////////////////////////////////////","////////////////////////////////////");
		Log.d("URLS.getDealerStock",URLS.getDealerStock);
		JSONObject jsonObject = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.getDealerStock, nameValuePairs);
		Log.d("C/////////////////////////////////////////","////////////////////////////////////");
		JSONArray jsonArray;
		try {
			jsonArray = jsonObject.getJSONArray("dealer_stock");
			Log.d("D/////////////////////////////////////////","////////////////////////////////////");
			Log.d(DownloadDataFromServer.class.getName(), "count : "+jsonArray.length());
			Log.d(DownloadDataFromServer.class.getName(), "json : "+jsonObject.toString(2));
			Log.d("E/////////////////////////////////////////","////////////////////////////////////");
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				dbworker.dealer_stock(jsonObject.getString("item_part_no"),
						jsonObject.getString("description"),
						jsonObject.getString("remaining_qty"),
						jsonObject.getString("last_stock_date"),
						jsonObject.getString("delar_id"),
						jsonObject.getString("delar_name"),
						jsonObject.getString("delar_account_no"),
						jsonObject.getString("avg_movement_at_dealer"),
						jsonObject.getString("avg_movement_in_area"));
			}
		
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void item_set(JSONArray jArray) {
		try {
			JSONObject jsonob0 = jArray.getJSONObject(0);
			StringBuffer sbquery = new StringBuffer();
			sbquery.append("REPLACE INTO 'item' SELECT '")
					.append(jsonob0.getString("item_id"))
					.append("' AS 'item_id', '")
					.append(jsonob0.getString("description"))
					.append("' AS 'description' ,'")
					.append(jsonob0.getString("item_part_no"))
					.append("' AS 'item_part_no' ,'")
					.append(jsonob0.getString("selling_price"))
					.append("' AS 'selling_price' ,'")
					.append(jsonob0.getString("total_stock_qty"))
					.append("' AS 'total_stock_qty' ,'")
					.append(jsonob0.getString("status"))
					.append("' AS 'status' ,'")
					.append(jsonob0.getString("time_stamp"))
					.append("' AS 'time_stamp' ,'")
					.append(jsonob0.getString("avg_movement_in_area"))
					.append("' AS 'avg_movement_in_area'");

			for (int i = 1; i < jArray.length(); i++) {
				JSONObject ITEM_DETAILS = jArray.getJSONObject(i);
				String item_part_no = ITEM_DETAILS.get("item_part_no")
						.toString();
				String item_id = ITEM_DETAILS.get("item_id").toString();
				String selling_price = ITEM_DETAILS.get("selling_price")
						.toString();

				String total_stock_qty = ITEM_DETAILS.get("total_stock_qty")
						.toString();
				String status = ITEM_DETAILS.get("status").toString();
				String time_stamp = ITEM_DETAILS.get("time_stamp").toString();
				String description = ITEM_DETAILS.get("description").toString();
				String avg_movement_in_area = ITEM_DETAILS.get(
						"avg_movement_in_area").toString();

				sbquery.append(" UNION SELECT '").append(item_id)
						.append("', '").append(description).append("', '")
						.append(item_part_no).append("', '")
						.append(selling_price).append("', '")
						.append(total_stock_qty).append("', '").append(status)
						.append("', '").append(time_stamp).append("', '")
						.append(avg_movement_in_area).append("'");
			}

			dbhelper.insetrawQuery(sbquery.toString());

		

		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// save garages details
	private void garages_details() {
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("time_stamp", "0"));
		jh = new Jsonhelper();
		JSONObject jsonObject = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.get_garages, nameValuePairs);

		try {
			JSONArray garages_details = jsonObject
					.getJSONArray("garages_details");

			for (int i = 0; i < garages_details.length(); i++) {
				JSONObject j = garages_details.getJSONObject(i);
				dbworker.save_existing_garages(j.getString("garage_id"),
						j.getString("garage_name"),
						j.getString("garage_address"),
						j.getString("garage_contact_no"),
						j.getString("delar_name"), j.getString("remarks"));

			}
			
		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	private void get_vehicle() {
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("time_stamp", "0"));
		jh = new Jsonhelper();
		JSONObject jsonObject = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.get_vehicle, nameValuePairs);

		try {
			JSONArray garages_details = jsonObject
					.getJSONArray("vehicle_details");

			for (int i = 0; i < garages_details.length(); i++) {
				JSONObject j = garages_details.getJSONObject(i);
				dbworker.save_vehicale(j.getString("vehicle_reg_no"),
						j.getString("customer_id"),
						j.getString("customer_type"));

			}
		
		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void save_purposes() {
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("time_stamp", "0"));
		jh = new Jsonhelper();
		JSONObject c = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.visit_purposes, nameValuePairs);
		try {
			JSONArray jarray = c.getJSONArray("visit_purposes_details");
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject j = jarray.getJSONObject(i);
				dbworker.save_vist_purposes(j.getString("purpose_id_name"),
						j.getString("visit_purpose_id"));

			}
		

		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void save_vist_catogery() {
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("time_stamp", "0"));
		jh = new Jsonhelper();
		JSONObject c = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.visit_catogery, nameValuePairs);
		try {
			JSONArray jarray = c.getJSONArray("visit_categories_details");
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject j = jarray.getJSONObject(i);
				dbworker.save_visit_categories_details(
						j.getString("category_name"),
						j.getString("visit_category_id"));
			}
			

		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void save_visit_history() {
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("time_stamp", "0"));
		jh = new Jsonhelper();
		JSONObject c = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.visit_history, nameValuePairs);
		try {
			JSONArray jarray = c.getJSONArray("visit_history");
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject j = jarray.getJSONObject(i);
				dbworker.save_visit_history(j.getString("sales_officer_name"),
						j.getString("visit_date"),
						j.getString("visit_purpose"),
						j.getString("description"), j.getString("selected_id"),
						j.getString("visit_category"));
			}
			

		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void FastMovingItems() {
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("dealer_id", "0"));
		jh = new Jsonhelper();
		JSONObject c = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.fast_moving, nameValuePairs);
		try {
			if (c.has("visit_history")) {

				JSONArray jarray = c.getJSONArray("visit_history");
				for (int i = 0; i < jarray.length(); i++) {
					JSONObject j = jarray.getJSONObject(i);
					dbworker.save_fast_oving_items(j.getString("item_id"),
							j.getString("item_part_no"),
							j.getString("description"), j.getString("quantity"));

				}
			
			} else {
				Log.e("error", "there is no array called visit_history");
			}
		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// save banks to database
	@SuppressWarnings("deprecation")
	private void save_banks() {
		jh = new Jsonhelper();
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("time_stamp", "0"));
		JSONObject c = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.get_all_banks, nameValuePairs);

		try {
			JSONArray jarray = c.getJSONArray("all_banks");
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject j = jarray.getJSONObject(i);
				dbworker.save_banks(j.getString("bank_id"),
						j.getString("bank_name"));
				Log.i("TAG", j.getString("bank_name"));

			}
		

		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void save_shop() {

		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("time_stamp", "0"));
		jh = new Jsonhelper();
		JSONObject c = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.new_shops, nameValuePairs);
		try {
			JSONArray jarray = c.getJSONArray("new_shops_details");
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject j = jarray.getJSONObject(i);
				dbworker.save_new_shops(j.getString("shop_id"),
						j.getString("shop_name"));
			}
		

		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	

	@Override
	protected void onProgressUpdate(String... values) {

		super.onProgressUpdate(values);
	}

	private void insert_item_data() {
		String previous_timestamp = "0";

		if (sharedPreferences.contains("timestamp")) {
	//		previous_timestamp = sharedPreferences.getString("timestamp", "");
		}

		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("timestamp",
				previous_timestamp));
		nameValuePairs.add(new BasicNameValuePair("rep_id", userdata.getString(
				"ID", "")));

		InputStream source = jh.retrieveStream_post(URLS.get_item,
				nameValuePairs);

		Gson gson = new Gson();

		Reader reader = new InputStreamReader(source);

		SearchResponse response = gson.fromJson(reader, SearchResponse.class);

		ArrayList<Item> items = response.data;

		String time_stamp = response.time_stamp;

		Editor editor = sharedPreferences.edit();
		editor.putString("timestamp", time_stamp);
		editor.commit();

		ArrayList<Item> items_temp = new ArrayList<Item>();

		int last = 0;
		// send 100*
		for (int i = 0; i < items.size(); i++) {
			items_temp.add(items.get(i));
			if (i % 250 == 0) {
				item_set_new(items_temp);
				last = i; // last 500*
				items_temp = new ArrayList<Item>();
			}
		}
		// send rest
		if (items.size() > last) {
			item_set_new(items_temp);
		}

	}

	private void item_set_new(ArrayList<Item> items) {
		if (items.isEmpty() != true) {

			Item itemob0 = items.get(0);
			StringBuffer sbquery = new StringBuffer();

			String item_id = "";
			String item_part_no = "";
			String description = "";
			String selling_price = "";
			String total_stock_qty = "";
			String status = "";
			String time_stamp = "";
			String avg_movement_in_area = "";

			item_id = itemob0.getItem_id();

			if (itemob0.getItem_part_no() != null) {
				item_part_no = itemob0.getItem_part_no();
			}
			if (itemob0.getDescription() != null) {
				description = itemob0.getDescription();
			}
			if (itemob0.getSelling_price() != null) {
				selling_price = itemob0.getSelling_price();
			}
			if (itemob0.getTotal_stock_qty() != null) {
				total_stock_qty = itemob0.getTotal_stock_qty();
			}
			if (itemob0.getStatus() != null) {
				status = itemob0.getStatus();
			}
			if (itemob0.getTime_stamp() != null) {
				time_stamp = itemob0.getTime_stamp();
			}
			if (itemob0.getAvg_movement_in_area() != null) {
				avg_movement_in_area = itemob0.getAvg_movement_in_area();
			}

			sbquery.append("REPLACE INTO 'item' SELECT '").append(item_id)
					.append("' AS 'item_id', '").append(description)
					.append("' AS 'description' ,'").append(item_part_no)
					.append("' AS 'item_part_no' ,'").append(selling_price)
					.append("' AS 'selling_price' ,'").append(total_stock_qty)
					.append("' AS 'total_stock_qty' ,'").append(status)
					.append("' AS 'status' ,'").append(time_stamp)
					.append("' AS 'time_stamp' ,'")
					.append(avg_movement_in_area)
					.append("' AS 'avg_movement_in_area'");

			for (int i = 1; i < items.size(); i++) {

				Item item = items.get(i);

				String item_idx = "";
				String item_part_nox = "";
				String descriptionx = "";
				String selling_pricex = "";
				String total_stock_qtyx = "";
				String statusx = "";
				String time_stampx = "";
				String avg_movement_in_areax = "";

				item_idx = item.getItem_id();

				Log.w("item_id", item_idx);

				if (item.getItem_part_no() != null) {
					item_part_nox = item.getItem_part_no();
				}
				if (item.getDescription() != null) {
					descriptionx = item.getDescription();
				}
				if (item.getSelling_price() != null) {
					selling_pricex = item.getSelling_price();
				}
				if (item.getTotal_stock_qty() != null) {
					total_stock_qtyx = item.getTotal_stock_qty();
				}
				if (item.getStatus() != null) {
					statusx = item.getStatus();
				}
				if (item.getTime_stamp() != null) {
					time_stampx = item.getTime_stamp();
				}
				if (item.getAvg_movement_in_area() != null) {
					avg_movement_in_areax = item.getAvg_movement_in_area();
				}

				sbquery.append(" UNION SELECT '").append(item_idx)
						.append("', '").append(descriptionx).append("', '")
						.append(item_part_nox).append("', '")
						.append(selling_pricex).append("', '")
						.append(total_stock_qtyx).append("', '")
						.append(statusx).append("', '").append(time_stampx)
						.append("', '").append(avg_movement_in_areax)
						.append("'");
			}

			dbhelper.insetrawQuery(sbquery.toString());

			dbworker.close();

		}
	}

	private void insert_pending_payment_data() {

		dbworker.delete_pending_payment_data();

		jh = new Jsonhelper();
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("officer_id", userdata
				.getString("ID", "")));

		InputStream source = jh.retrieveStream_post(URLS.pending_payments,
				nameValuePairs);

		Gson gson = new Gson();

		Reader reader = new InputStreamReader(source);

		SearchResponsePayment response = gson.fromJson(reader,
				SearchResponsePayment.class);

		ArrayList<Payment> payments = response.pending_payments;

		ArrayList<Payment> payments_temp = new ArrayList<Payment>();

		int last = 0;
		// send 100*
		for (int i = 0; i < payments.size(); i++) {
			payments_temp.add(payments.get(i));
			if (i % 250 == 0) {
				payment_set_new(payments_temp);
				last = i; // last 500*
				payments_temp = new ArrayList<Payment>();
			}
		}
		// send rest
		if (payments.size() > last) {
			payment_set_new(payments_temp);
		}

	}

	void payment_set_new(ArrayList<Payment> payments) {

		if (payments.isEmpty() != true) {
			Log.e("size of payment arr", payments.size() + "");

			Payment paymentob0 = payments.get(0);
			StringBuffer sbquery = new StringBuffer();

			String deliver_order_id = "";//

			String dealer_id = "";//

			String invoice_no = "";//

			String wip_no = "";//

			String total_amount = "";//

			String added_date = "";//

			String accepted_by = "";//

			String added_time = "";//

			String due_date = "";//

			String time_stamp = "";//

			String status = "";//

			String return_amount = "";//

			String delar_account_no = "";//

			String delar_shop_name = "";//

			String business_address = "";//

			String branch_name = "";//

			String cash_payment = "";//

			String realized_cheque_amount = "";//

			String unrealized_cheque_amount = "";//

			String bank_dep_payment = "";//

			String total_paid_amount_with_unrealized_cheques = "";//

			String total_paid_amount_without_unrealized_cheques = "";//

			String total_pending_amount_without_unrealized_cheques = "";//

			String number_of_days = "";//

			String total_pending_amount_with_unrealized_cheques = "";//

			if (paymentob0.getDeliver_order_id() != null) {
				deliver_order_id = paymentob0.getDeliver_order_id();
			}

			if (paymentob0.getDealer_id() != null) {
				dealer_id = paymentob0.getDealer_id();
			}

			if (paymentob0.getInvoice_no() != null) {
				invoice_no = paymentob0.getInvoice_no();
			}

			if (paymentob0.getWip_no() != null) {
				wip_no = paymentob0.getWip_no();
			}

			if (paymentob0.getTotal_amount() != null) {
				total_amount = paymentob0.getTotal_amount();
			}

			if (paymentob0.getAdded_date() != null) {
				added_date = paymentob0.getAdded_date();
			}

			if (paymentob0.getAccepted_by() != null) {
				accepted_by = paymentob0.getAccepted_by();
			}

			if (paymentob0.getDue_date() != null) {
				due_date = paymentob0.getDue_date();
			}

			if (paymentob0.getStatus() != null) {
				status = paymentob0.getStatus();
			}

			if (paymentob0.getReturn_amount() != null) {
				return_amount = paymentob0.getReturn_amount();
			}

			if (paymentob0.getDelar_account_no() != null) {
				delar_account_no = paymentob0.getDelar_account_no();
			}

			if (paymentob0.getDelar_shop_name() != null) {
				delar_shop_name = paymentob0.getDelar_shop_name();
			}

			if (paymentob0.getBranch_name() != null) {
				branch_name = paymentob0.getBranch_name();
			}

			if (paymentob0.getCash_payment() != null) {
				cash_payment = paymentob0.getCash_payment();
			}

			if (paymentob0.getRealized_cheque_amount() != null) {
				realized_cheque_amount = paymentob0.getRealized_cheque_amount();
			}

			if (paymentob0.getUnrealized_cheque_amount() != null) {
				unrealized_cheque_amount = paymentob0
						.getUnrealized_cheque_amount();
			}

			if (paymentob0.getTotal_paid_amount_with_unrealized_cheques() != null) {
				total_paid_amount_with_unrealized_cheques = paymentob0
						.getTotal_paid_amount_with_unrealized_cheques();
			}

			if (paymentob0.getTotal_paid_amount_without_unrealized_cheques() != null) {
				total_paid_amount_without_unrealized_cheques = paymentob0
						.getTotal_paid_amount_without_unrealized_cheques();
			}

			if (paymentob0.getTotal_pending_amount_without_unrealized_cheques() != null) {
				total_pending_amount_without_unrealized_cheques = paymentob0
						.getTotal_pending_amount_without_unrealized_cheques();
			}

			if (paymentob0.getNumber_of_days() != null) {
				number_of_days = paymentob0.getNumber_of_days();
			}

			if (paymentob0.getTotal_pending_amount_with_unrealized_cheques() != null) {
				total_pending_amount_with_unrealized_cheques = paymentob0
						.getTotal_pending_amount_with_unrealized_cheques();
			}

			sbquery.append("REPLACE INTO 'deliver_order' SELECT '")
					.append(deliver_order_id)
					.append("' AS 'deliver_order_id', '")
					.append(58)
					.append("' AS 'purchase_order_id' ,'")
					.append(dealer_id)
					.append("' AS 'dealer_id' ,'")
					.append(invoice_no)
					.append("' AS 'invoice_no' ,'")
					.append(wip_no)
					.append("' AS 'wip_no' ,'")
					.append(total_amount)
					.append("' AS 'total_amount' ,'")
					.append(added_date)
					.append("' AS 'added_date' ,'")
					.append(added_time)
					.append("' AS 'added_time','")
					.append(due_date)
					.append("' AS 'due_date','")
					.append(status)
					.append("' AS 'status','")
					.append(20)
					.append("' AS 'accepted_by','")
					.append(cash_payment)
					.append("' AS 'cash_payment','")
					.append(realized_cheque_amount)
					.append("' AS 'cheque_payment','")
					.append(bank_dep_payment)
					.append("' AS 'bank_dep_payment','")
					.append(total_paid_amount_with_unrealized_cheques)
					.append("' AS 'total_payment','")
					.append(total_pending_amount_with_unrealized_cheques)
					.append("' AS 'pending_amount','")
					.append(0)
					.append("' AS 'unrealized_cheque','")
					// unrealized_cheque
					.append(delar_account_no)
					.append("' AS 'delar_account_no','")
					.append(total_paid_amount_with_unrealized_cheques)
					.append("' AS 'total_paid_amount_with_unrealized_cheques','")
					.append(total_paid_amount_without_unrealized_cheques)
					.append("' AS 'total_paid_amount_without_unrealized_cheques','")
					.append(total_pending_amount_with_unrealized_cheques)
					.append("' AS 'total_pending_amount_with_unrealized_cheques','")
					.append(total_pending_amount_without_unrealized_cheques)
					.append("' AS 'total_pending_amount_without_unrealized_cheques','")
					.append(realized_cheque_amount)
					.append("' AS 'realized_cheque_amount','")
					.append(unrealized_cheque_amount)
					.append("' AS 'unrealized_cheque_amount','")
					.append(return_amount).append("' AS 'return_amount','")
					.append(return_amount).append("' AS 'number_of_days','")
					.append(0).append("' AS 'iternery_status'");

			for (int i = 1; i < payments.size(); i++) {

				Payment payment = payments.get(i);

				String deliver_order_idx = "";//

				String dealer_idx = "";//

				String invoice_nox = "";//

				String wip_nox = "";//

				String total_amountx = "";//

				String added_datex = "";//

				String accepted_byx = "";//

				String added_timex = "";//

				String due_datex = "";//

				String time_stampx = "";//

				String statusx = "";//

				String return_amountx = "";//

				String delar_account_nox = "";//

				String delar_shop_namex = "";//

				String business_addressx = "";//

				String branch_namex = "";//

				String cash_paymentx = "";//

				String realized_cheque_amountx = "";//

				String unrealized_cheque_amountx = "";//

				String bank_dep_paymentx = "";//

				String total_paid_amount_with_unrealized_chequesx = "";//

				String total_paid_amount_without_unrealized_chequesx = "";//

				String total_pending_amount_without_unrealized_chequesx = "";//

				String number_of_daysx = "";//

				String total_pending_amount_with_unrealized_chequesx = "";//

				if (paymentob0.getDeliver_order_id() != null) {
					deliver_order_idx = paymentob0.getDeliver_order_id();
				}

				if (paymentob0.getDealer_id() != null) {
					dealer_idx = paymentob0.getDealer_id();
				}

				if (paymentob0.getInvoice_no() != null) {
					invoice_nox = paymentob0.getInvoice_no();
				}

				if (paymentob0.getWip_no() != null) {
					wip_nox = paymentob0.getWip_no();
				}

				if (paymentob0.getTotal_amount() != null) {
					total_amountx = paymentob0.getTotal_amount();
				}

				if (paymentob0.getAdded_date() != null) {
					added_datex = paymentob0.getAdded_date();
				}

				if (paymentob0.getAccepted_by() != null) {
					accepted_byx = paymentob0.getAccepted_by();
				}

				if (paymentob0.getDue_date() != null) {
					due_datex = paymentob0.getDue_date();
				}

				if (paymentob0.getStatus() != null) {
					statusx = paymentob0.getStatus();
				}

				if (paymentob0.getReturn_amount() != null) {
					return_amountx = paymentob0.getReturn_amount();
				}

				if (paymentob0.getDelar_account_no() != null) {
					delar_account_nox = paymentob0.getDelar_account_no();
				}

				if (paymentob0.getDelar_shop_name() != null) {
					delar_shop_namex = paymentob0.getDelar_shop_name();
				}

				if (paymentob0.getBranch_name() != null) {
					branch_namex = paymentob0.getBranch_name();
				}

				if (paymentob0.getCash_payment() != null) {
					cash_paymentx = paymentob0.getCash_payment();
				}

				if (paymentob0.getRealized_cheque_amount() != null) {
					realized_cheque_amountx = paymentob0
							.getRealized_cheque_amount();
				}

				if (paymentob0.getUnrealized_cheque_amount() != null) {
					unrealized_cheque_amountx = paymentob0
							.getUnrealized_cheque_amount();
				}

				if (paymentob0.getTotal_paid_amount_with_unrealized_cheques() != null) {
					total_paid_amount_with_unrealized_chequesx = paymentob0
							.getTotal_paid_amount_with_unrealized_cheques();
				}

				if (paymentob0
						.getTotal_paid_amount_without_unrealized_cheques() != null) {
					total_paid_amount_without_unrealized_chequesx = paymentob0
							.getTotal_paid_amount_without_unrealized_cheques();
				}

				if (paymentob0
						.getTotal_pending_amount_without_unrealized_cheques() != null) {
					total_pending_amount_without_unrealized_chequesx = paymentob0
							.getTotal_pending_amount_without_unrealized_cheques();
				}

				if (paymentob0.getNumber_of_days() != null) {
					number_of_daysx = paymentob0.getNumber_of_days();
				}

				if (paymentob0
						.getTotal_pending_amount_with_unrealized_cheques() != null) {
					total_pending_amount_with_unrealized_chequesx = paymentob0
							.getTotal_pending_amount_with_unrealized_cheques();
				}

				sbquery.append(" UNION SELECT '")
						.append(deliver_order_idx)
						.append("', '")
						.append(58)
						.append("', '")
						.append(dealer_idx)
						.append("', '")
						.append(invoice_nox)
						.append("', '")
						.append(wip_nox)
						.append("', '")
						.append(total_amountx)
						.append("', '")
						.append(added_datex)
						.append("', '")
						.append(added_timex)
						.append("', '")
						.append(due_datex)
						.append("', '")
						.append(statusx)
						.append("', '")
						.append(20)
						.append("', '")
						.append(cash_paymentx)
						.append("', '")
						.append(realized_cheque_amountx)
						.append("', '")
						.append(bank_dep_payment)
						.append("', '")
						.append(total_paid_amount_with_unrealized_chequesx)
						.append("', '")
						.append(total_pending_amount_with_unrealized_chequesx)
						.append("', '")
						.append(0)
						.append("', '")
						.append(delar_account_nox)
						.append("', '")
						.append(total_paid_amount_with_unrealized_chequesx)
						.append("', '")
						.append(total_paid_amount_without_unrealized_chequesx)
						.append("', '")
						.append(total_pending_amount_with_unrealized_chequesx)
						.append("', '")
						.append(total_pending_amount_without_unrealized_chequesx)
						.append("', '").append(realized_cheque_amountx)
						.append("', '").append(unrealized_cheque_amountx)
						.append("', '").append(return_amountx).append("', '")
						.append(number_of_daysx).append("', '").append(0)
						.append("'");

			}

			Log.e("query is ", sbquery.toString());

			dbhelper.insetrawQuery(sbquery.toString());

			

		}
	}

	private void getAllPendingPayments() {

		jh = new Jsonhelper();
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("officer_id", userdata
				.getString("ID", "")));

		JSONObject jsonObject = jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.pending_payments, nameValuePairs);

		dbworker.delete_pending_payment_data();

		try {
			JSONArray jarray = jsonObject.getJSONArray("pending_payments");
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject j = jarray.getJSONObject(i);

				dbworker.insert_pending_payment(
						j.getString("deliver_order_id"),
						"58",
						j.getString("dealer_id").toString(),
						j.getString("invoice_no").toString(),
						j.getString("wip_no").toString(),
						j.getString("total_amount").toString(),
						j.getString("added_date").toString(),
						j.getString("added_time").toString(),
						j.getString("due_date").toString(),
						j.getString("status"),
						"20",
						j.getString("cash_payment"),
						j.getString("realized_cheque_amount"),
						j.getString("bank_dep_payment"),
						j.getString("total_paid_amount_with_unrealized_cheques"),
						j.getString("total_pending_amount_with_unrealized_cheques"),
						j.getString("delar_account_no"),
						j.getString("total_paid_amount_with_unrealized_cheques"),
						j.getString("total_paid_amount_without_unrealized_cheques"),
						j.getString("total_pending_amount_with_unrealized_cheques"),//
						j.getString("total_pending_amount_without_unrealized_cheques"),//
						j.getString("realized_cheque_amount"),//
						j.getString("unrealized_cheque_amount"), j
								.getString("return_amount"), j
								.getString("number_of_days"),
								j.getString("tgt_collection_dt")

				);
				dbworker.close();

			}
		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
