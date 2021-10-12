package ceylon.linux.asynctask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.db.Dbworker;
import ceylon.linux.url.URLS;
import ceylon.linux.utility.Utility;

public class UploadDataToServer extends AsyncTask<String, String, String> {

	public static int salesOrdercount;
	public static int ucallcount = 0;
	Dbworker dbworker;
	SharedPreferences userdata;
	JSONArray purchase_order_wrappper;
	private Context context;
	private ProgressDialog dialog;
	private ceylon.linux.controller.Jsonhelper jh;

	public UploadDataToServer(Context cxt) {
		context = cxt;
		dbworker = new Dbworker(cxt);
		dialog = new ProgressDialog(context);
		jh = new Jsonhelper();
		userdata = cxt.getSharedPreferences("USERDATA", Context.MODE_PRIVATE);
	}

	@Override
	protected String doInBackground(String... params) {

	/*	get_all_newly_added_line_item_wise_targets();
		get_all_not_synchronized_purchase_orders();
		get_all_non_synchronized_dealers_locations();
		getAllNonSynchronizedTourItenary();
		get_all_newly_added_competitor_parts();		*/
		
		get_all_newly_added_competitor_parts();	
		get_all_non_synchronized_dealers_locations();
		get_all_not_synchronized_purchase_orders();
		getAllBdepositPayments();
		getAllgarageLoyaltyPayments();
		getAllChequePayments();
		getAllPaymentReturns();
		return null;

	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		dialog = ProgressDialog.show(context, "",
				"Please wait Upload Data to server... ", false);
		dialog.show();

	}

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);

		if (null != dialog && dialog.isShowing()) {

			try {
				dialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		dbworker.close();
	}

	public JSONObject upload_purchase_orders() {
		get_all_newly_added_customers__with_vehicale();

		Cursor purchase_order = dbworker.get_purchase_orders_NOT_SYNCED();
		purchase_order_wrappper = new JSONArray();
		JSONObject final_Wrapper = new JSONObject();
		JSONObject purchase_order_json_object = null;

		if (purchase_order != null) {
			purchase_order.moveToFirst();
			while (!purchase_order.isAfterLast()) {

				try {
					purchase_order_json_object = new JSONObject();
					JSONArray items = create_purchase_items(purchase_order
							.getString(0));

					purchase_order_json_object.put("ID",
							purchase_order.getString(0));
					purchase_order_json_object.put("BillAmount",
							purchase_order.getString(1));
					purchase_order_json_object.put("BillAmountwithvat",
							purchase_order.getString(10));
					purchase_order_json_object.put("dealer_id",
							purchase_order.getString(2));
					purchase_order_json_object.put("date_of_bill",
							purchase_order.getString(4));
					purchase_order_json_object.put("lon",
							purchase_order.getString(5));
					purchase_order_json_object.put("lat",
							purchase_order.getString(6));
					purchase_order_json_object.put("b_level",
							purchase_order.getString(7));
					purchase_order_json_object.put("completed",
							purchase_order.getString(8));
					purchase_order_json_object.put("SYNC",
							purchase_order.getString(9));
					purchase_order_json_object.put("iterner_status",
							purchase_order.getString(12));

					purchase_order_json_object.put("items", items);

					purchase_order_wrappper.put(purchase_order_json_object);

					/*
					 * Log.i("ID", purchase_order.getString(0) + "");
					 * Log.i("Bill Amount", purchase_order.getString(1));
					 * Log.i("dealer_id", purchase_order.getString(2));
					 * Log.i("date_of_bill", purchase_order.getString(3));
					 */
					purchase_order.moveToNext();
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
			purchase_order.close();
			try {
				final_Wrapper.put("user_id", userdata.getString("ID", ""));
				final_Wrapper.put("order", purchase_order_wrappper);
				/*
				 * final_Wrapper.put("new_dealers", getNewlyAddedDealers());
				 * final_Wrapper.put("new_garages",
				 * get_all_newly_added_garages());
				 * final_Wrapper.put("customer_details",
				 * get_all_newly_added_customers__with_vehicale());
				 */

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return final_Wrapper;
	}

	public JSONArray create_purchase_items(String bill_id) {
		Cursor purchase_order_items = dbworker.get_purchase_items(bill_id);
		JSONArray j = new JSONArray();

		if (purchase_order_items != null) {
			purchase_order_items.moveToFirst();
			while (!purchase_order_items.isAfterLast()) {

				JSONObject jobj = new JSONObject();

				try {
					jobj.put("item_id", purchase_order_items.getString(3)
							.toString());
					jobj.put("qty", purchase_order_items.getString(4)
							.toString());
					jobj.put("price", purchase_order_items.getString(5)
							.toString());
					jobj.put("item_comment", purchase_order_items.getString(7)
							.toString());

					j.put(jobj);
					purchase_order_items.moveToNext();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			purchase_order_items.close();
		}

		return j;

	}

	public JSONArray getNewlyAddedDealers() {

		Cursor newly_added_dealers = dbworker.get_all_newly_added_dealers();
		JSONArray j = new JSONArray();

		if (newly_added_dealers != null) {
			newly_added_dealers.moveToFirst();
			while (!newly_added_dealers.isAfterLast()) {

				JSONObject jobj = new JSONObject();

				try {
					jobj.put("delar_name", newly_added_dealers.getString(1)
							.toString());
					jobj.put("delar_address", newly_added_dealers.getString(2)
							.toString());
					jobj.put("shop_name", newly_added_dealers.getString(3)
							.toString());

					j.put(jobj);
					newly_added_dealers.moveToNext();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			newly_added_dealers.close();
		}

		return j;

	}

	public JSONArray get_all_newly_added_garages() {
		Cursor get_all_newly_added_garages = dbworker
				.get_all_newly_added_garages();
		JSONArray j = new JSONArray();
		if (get_all_newly_added_garages != null) {
			get_all_newly_added_garages.moveToFirst();
			while (!get_all_newly_added_garages.isAfterLast()) {

				JSONObject jobj = new JSONObject();

				try {
					jobj.put("g_name", get_all_newly_added_garages.getString(1)
							.toString());
					jobj.put("g_address", get_all_newly_added_garages
							.getString(2).toString());
					jobj.put("g_contact_no", get_all_newly_added_garages
							.getString(3).toString());
					jobj.put("nearest_dealer", get_all_newly_added_garages
							.getString(4).toString());
					jobj.put("remarks", get_all_newly_added_garages
							.getString(5).toString());

					j.put(jobj);
					get_all_newly_added_garages.moveToNext();

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
			get_all_newly_added_garages.close();
		}

		return j;

	}

	public JSONArray get_all_newly_added_customers__with_vehicale() {

		// Log.i("Method work 2", "metod work 2");
		Cursor get_all_newly_added_customers = dbworker
				.get_all_newly_customer();

		JSONArray j = new JSONArray();
		if (get_all_newly_added_customers != null) {
			get_all_newly_added_customers.moveToFirst();
			while (!get_all_newly_added_customers.isAfterLast()) {

				JSONObject jobj = new JSONObject();

				try {

					Log.i("customer name", get_all_newly_added_customers
							.getString(1).toString());
					jobj.put("customer_name", get_all_newly_added_customers
							.getString(1).toString());
					jobj.put("customer_address", get_all_newly_added_customers
							.getString(2).toString());
					jobj.put("contact_no", get_all_newly_added_customers
							.getString(3).toString());
					jobj.put("vehicales",
							get_vehicales(get_all_newly_added_customers
									.getString(0).toString()));

					j.put(jobj);

					get_all_newly_added_customers.moveToNext();
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		} else {

		}

		return j;

	}

	public JSONArray get_vehicales(String user_id) {

		Cursor get_all_newly_added_vehicales = dbworker.get_vehicales(user_id);
		JSONArray j = new JSONArray();

		if (get_all_newly_added_vehicales != null) {
			get_all_newly_added_vehicales.moveToFirst();
			while (!get_all_newly_added_vehicales.isAfterLast()) {

				JSONObject jobj = new JSONObject();
				try {
					jobj.put("vehicale_no", get_all_newly_added_vehicales
							.getString(1).toString());
					j.put(jobj);
					get_all_newly_added_vehicales.moveToNext();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			get_all_newly_added_vehicales.close();
		}
		return j;

	}

	// GET payment CASH DETAILS

	public JSONArray cash_details() {
		Cursor cash_details = dbworker.get_cash_payment();

		JSONArray jj = new JSONArray();
		JSONObject cash_final = new JSONObject();

		try {
			if (cash_details != null) {
				cash_details.moveToFirst();

				while (!cash_details.isAfterLast()) {
					JSONObject cash = new JSONObject();
					cash.put("deliver_order_id", cash_details.getString(0)
							.toString());
					cash.put("cash_amount", cash_details.getString(1)
							.toString());
					cash.put("iternary_status", cash_details.getString(2)
							.toString());
					jj.put(cash);

					cash_details.moveToNext();

				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jj;

	}

	// GET PAYMENT CHEQUE DETAILS

	public JSONArray cheque_details() {
		Cursor cheque_details = dbworker.get_cheque_payment_details();

		JSONArray jj = new JSONArray();
		JSONObject cheque_final = new JSONObject();

		try {
			if (cheque_details != null) {
				cheque_details.moveToFirst();

				while (!cheque_details.isAfterLast()) {
					JSONObject cheque = new JSONObject();
					cheque.put("deliver_order_id", cheque_details.getString(1)
							.toString());
					cheque.put("cheque_no", cheque_details.getString(2)
							.toString());
					cheque.put("amount", cheque_details.getString(3).toString());
					cheque.put("bankid", cheque_details.getString(4).toString());
					cheque.put("realised_date", cheque_details.getString(5)
							.toString());
					cheque.put("image_path", cheque_details.getString(6)
							.toString());
					cheque.put("iternary_status", cheque_details.getString(7)
							.toString());
					jj.put(cheque);

					cheque_details.moveToNext();

				}
				// cheque_final.put("cheque_details", jj);
				// cheque_final.put("officer_id", userdata.getString("ID", ""));
				// cheque_final.put("time_stamp", Utility.timestamp_creater());
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jj;

	}

	// GET DEPOSIT DETAILS

	public JSONArray deposit_details() {
		Cursor cheque_details = dbworker.get_deposit_payment_details();

		JSONArray jj = new JSONArray();
		JSONObject cheque_final = new JSONObject();

		try {
			if (cheque_details != null) {
				cheque_details.moveToFirst();

				while (!cheque_details.isAfterLast()) {
					JSONObject cheque = new JSONObject();

					cheque.put("deliver_order_id", cheque_details.getString(1)
							.toString());
					// Log.i("deliver_order_id",
					// cheque_details.getString(1).toString());
					cheque.put("slip_no", cheque_details.getString(2)
							.toString());
					cheque.put("amount", cheque_details.getString(3).toString());
					cheque.put("bankid", cheque_details.getString(4).toString());
					cheque.put("deposit_date", cheque_details.getString(5)
							.toString());
					cheque.put("path", cheque_details.getString(6).toString());
					cheque.put("iternary_status", cheque_details.getString(7)
							.toString());

					jj.put(cheque);
					cheque_details.moveToNext();

				}

				// cheque_final.put("deposit_data", jj);
				// cheque_final.put("officer_id", userdata.getString("ID", ""));
				// cheque_final.put("time_stamp", Utility.timestamp_creater());

				// Log.i("cheque", cheque_final.toString());
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jj;

	}

	// Send Marketing activity to the server

	public JSONObject upload_marketing_Activity() {
		Cursor c = dbworker.get_marketing_activity_details();
		Cursor c2;
		Cursor c3;
		JSONObject parent = new JSONObject();
		JSONArray parent_array = new JSONArray();
		if (c != null) {
			c.moveToFirst();
			while (!c.isAfterLast()) {

				try {
					JSONObject child = new JSONObject();
					String ID = c.getString(0);

					child.put("campaign_type", c.getString(1));
					child.put("campaign_date", c.getString(2));
					child.put("objective", c.getString(3));
					child.put("material_required_for_ho", c.getString(4));
					child.put("other_requirment_for_branch", c.getString(5));
					child.put("location", c.getString(6));
					child.put("invitees", c.getString(7));
					child.put("dimo_employees", c.getString(8));
					child.put("no_of_employees", c.getString(9));
					child.put("quotation", c.getString(10));
					child.put("budget", c.getString(11));
					child.put("priority", c.getString(12));

					c2 = dbworker.get_target_dealers(ID);
					JSONArray targetdealers = new JSONArray();
					c2.moveToFirst();
					while (!c2.isAfterLast()) {
						JSONObject targetdealer = new JSONObject();
						targetdealer.put("account_no", c2.getString(2));
						targetdealer.put("current_to", c2.getString(3));
						targetdealer.put("expected_increase_after_three_month",
								c2.getString(4));
						targetdealer.put("expected_increase_after_three_month",
								c2.getString(5));
						targetdealers.put(targetdealer);
						c2.moveToNext();

					}

					c3 = dbworker.get_description_table_marketing_activity(ID);
					JSONArray description_marketing_activities = new JSONArray();
					c3.moveToFirst();
					while (!c3.isAfterLast()) {
						JSONObject description_marketing_activity = new JSONObject();
						description_marketing_activity.put("description",
								c3.getString(1));
						description_marketing_activity.put("ecpu",
								c3.getString(2));
						description_marketing_activity.put("qty",
								c3.getString(3));
						description_marketing_activity.put("total",
								c3.getString(4));
						description_marketing_activities
								.put(description_marketing_activity);
						c3.moveToNext();

					}

					child.put("targetdealers", targetdealers);
					child.put("marketingactivities",
							description_marketing_activities);
					parent_array.put(child);
					c.moveToNext();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		try {
			parent.put("marketingActivity", parent_array);
			parent.put("officer_id", userdata.getString("ID", ""));
			parent.put("time_stamp", Utility.timestamp_creater());
			// Log.i("parent", parent.toString());
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return parent;
	}

	// Send tour Plan To the Server
	public JSONObject upload_tour_plan() {

		Cursor c = dbworker.get_tour_plan();
		JSONObject parent = new JSONObject();
		JSONArray parent_array = new JSONArray();
		if (c != null) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				JSONObject child = new JSONObject();
				try {

					child.put("town", c.getString(1));
					child.put("dealer", c.getString(2));
					child.put("date", c.getString(3));
					child.put("dealer_ID", c.getString(4));
					child.put("status", c.getString(5));
					child.put("status", c.getString(6));
					parent_array.put(child);
					c.moveToNext();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		try {
			parent.put("tour_plan", parent_array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parent;

	}

	public JSONObject upload_Brands() {
		Cursor c = dbworker.get_brands();
		JSONArray parent_array = new JSONArray();
		JSONObject final_json = new JSONObject();

		if (c != null) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				JSONObject child = new JSONObject();
				try {

					child.put("other_details", c.getString(1));
					child.put("type", c.getString(2));
					child.put("outlet_id", c.getString(3));
					child.put("image_path", c.getString(4));
					child.put("iternery_status", c.getString(5));

					parent_array.put(child);
					c.moveToNext();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		try {
			final_json.put("officer_id", userdata.getString("ID", ""));
			final_json.put("brands", parent_array);
			final_json.put("time_stamp", Utility.timestamp_creater());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return final_json;

	}

	// upload failure items

	public JSONObject upload_failures() {
		Cursor c = dbworker.get_failures();
		JSONArray parent_array = new JSONArray();
		JSONObject final_json = new JSONObject();

		if (c != null) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				JSONObject child = new JSONObject();
				try {

					child.put("type", c.getString(1));
					child.put("outlet_id", c.getString(2));
					child.put("part_no", c.getString(3));
					child.put("failure", c.getString(4));
					child.put("image_path", c.getString(5));

					parent_array.put(child);
					c.moveToNext();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		try {

			final_json.put("officer_id", userdata.getString("ID", ""));
			final_json.put("failures", parent_array);
			final_json.put("time_stamp", Utility.timestamp_creater());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return final_json;

	}

	public void get_all_newly_added_line_item_wise_targets() {
		Cursor cursor = dbworker.get_non_sync_target();

		if (cursor.moveToFirst()) {
			do {

				JSONObject target = new JSONObject();
				try {
					target.put("dealer_id", cursor.getString(1));
					target.put("year", cursor.getString(2));
					target.put("month", cursor.getString(3));
					target.put("added_date", cursor.getString(4));
					target.put("added_time", cursor.getString(5));
					target.put("current_discount_percentage",
							cursor.getString(6));

					JSONArray target_items = new JSONArray();
					Cursor cursor2 = dbworker.get_items_by_target_id(cursor
							.getInt(0));
					if (cursor2.moveToFirst()) {
						do {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("item_id", cursor2.getInt(2));
							jsonObject.put("minimum_qty", cursor2.getString(3));
							jsonObject.put("additional_qty",
									cursor2.getString(4));
							jsonObject.put("current_selling_price",
									cursor2.getString(5));
							target_items.put(jsonObject);

						} while (cursor2.moveToNext());

					}

					cursor2.close();

					target.put("items", target_items);
					JSONObject json_tree = new JSONObject();

					json_tree.put("target_json", target);

					String[] parameters = { "sales_rep_id", "json_string" };
					String[] data = { userdata.getString("ID", ""),
							json_tree.toString() };
					HttpResponse respons = jh.callServer(
							URLS.send_line_item_wise_target, parameters, data);

					String string_response = jh.responsDataConvertor(respons);

					Log.w("response", string_response);

					JSONObject json_response = new JSONObject(string_response);
					if (json_response.getInt("result") == 1) {
						dbworker.update_target_sync_status_by_target_id(cursor
								.getInt(0));
						
						
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	public void get_all_newly_added_competitor_parts() {

		Cursor cursor = dbworker.get_non_sync_competitorParts();
		if (cursor.moveToFirst()) {

			do {

				JSONObject comp = new JSONObject();

				try {

					comp.put("outlet_cat_id", cursor.getInt(1));
					comp.put("outlet_id", cursor.getInt(2));
					comp.put("added_date", cursor.getString(4));
					comp.put("added_time", cursor.getString(3));

					JSONArray item_js_arr = new JSONArray();

					Cursor cursor_item = dbworker
							.get_competitor_items_by_comp_id(cursor.getInt(0));
					if (cursor_item.moveToFirst()) {
						do {

							JSONObject comp_item = new JSONObject();
							comp_item.put("item_id", cursor_item.getInt(2));
							comp_item.put("part_number",
									cursor_item.getString(3));
							comp_item.put("brand", cursor_item.getString(4));
							comp_item.put("importer", cursor_item.getString(5));
							comp_item.put("cost_price_to_the_dealer",
									cursor_item.getString(6));
							comp_item.put("selling_price_to_the_customer",
									cursor_item.getString(7));
							comp_item.put("average_monthly_movement",
									cursor_item.getString(8));
							comp_item.put("overall_movement_at_the_dealer",
									cursor_item.getString(9));

							if (cursor_item.getString(10).equals("") == false) {
								String file_path = cursor_item.getString(10);
								File file = new File(file_path);

								try {
									// Reading a Image file from file system
									FileInputStream imageInFile = new FileInputStream(
											file);
									byte imageData[] = new byte[(int) file
											.length()];
									imageInFile.read(imageData);

									// Converting Image byte array into Base64
									// String

									String imageDataString = Base64
											.encodeToString(imageData,
													Base64.DEFAULT);

									comp_item.put("image_of_comp_part",
											imageDataString);

									imageInFile.close();

									Log.i("success",
											"Image Successfully Manipulated!");
								} catch (FileNotFoundException e) {
									Log.e("error", "Image not found" + e);
								} catch (IOException ioe) {
									Log.e("error",
											"Exception while reading the Image "
													+ ioe);
								}
							}
							Log.i("jsn comp item", comp_item.toString());
							item_js_arr.put(comp_item);
						} while (cursor_item.moveToNext());

					}

					cursor_item.close();

					comp.put("comp_items", item_js_arr);

					String[] parameters = { "sales_rep_id", "json_string" };
					String[] data = { userdata.getString("ID", ""),
							comp.toString() };
					HttpResponse respons = jh.callServer(
							URLS.send_competitor_parts, parameters, data);

					String string_response = jh.responsDataConvertor(respons);

					JSONObject json_response = new JSONObject(string_response);
					if (json_response.getString("result").equals("1")) {
						dbworker.update_compparts_sync_status_by_target_id(cursor
								.getInt(0));
						Log.e("response", string_response);
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	public void get_all_non_synchronized_dealers_locations() {
		Cursor cursor = dbworker.get_all_non_synchronized_dealers_locations();
		if (cursor.moveToFirst()) {
			do {
				JSONObject jsonObject = new JSONObject();

				try {
					jsonObject.put("lat", cursor.getString(1));
					jsonObject.put("long", cursor.getString(2));
					jsonObject.put("dealer_password", cursor.getString(3));

					JSONObject json_tree = new JSONObject();

					json_tree.put("data", jsonObject);

					String[] parameters = { "dealer_id", "json_string" };
					String[] data = { cursor.getString(0), json_tree.toString() };
					HttpResponse respons = jh.callServer(
							URLS.update_dealer_location, parameters, data);

					String string_response = jh.responsDataConvertor(respons);

					Log.e("response", string_response);

					JSONObject json_response = new JSONObject(string_response);
					if (json_response.getString("result").equals("1")) {
						dbworker.update_sync_status_by_dealer_id(cursor
								.getString(0));

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	public static String encodeImage(byte[] imageByteArray) {
		// String encodedString = new
		// String(Base64.encodeBase64(imageByteArray));

		String encodedString = imageByteArray.toString();
		return encodedString;
	}

	public void getAllNonSynchronizedTourItenary() {

		Cursor cursor = dbworker.getallunsynchronizedtouritenary();
		if (cursor.moveToFirst()) {
			do {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("outlet_id", cursor.getString(1));
					jsonObject.put("outlet_name", cursor.getString(2));
					jsonObject.put("visit_category", cursor.getString(3));
					jsonObject.put("visit_purpose", cursor.getString(4));
					jsonObject.put("visit_time", cursor.getString(5));
					jsonObject.put("visit_date", cursor.getString(6));
					jsonObject.put("description", cursor.getString(7));

					String[] parameters = { "sales_rep_id", "json_string" };
					String[] data = { userdata.getString("ID", ""),
							jsonObject.toString() };
					HttpResponse respons = jh.callServer(
							URLS.send_tour_itenary, parameters, data);

					String string_response = jh.responsDataConvertor(respons);
					Log.e("check this", string_response);
					JSONObject jobj = new JSONObject(string_response);
					if (jobj.getString("result").trim().equals("true")) {
						dbworker.upadate_tour_itanery_sync_status_by_ID(cursor
								.getString(0));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (cursor.moveToNext());

		}
		cursor.close();
	}

	public void getAllgarageLoyaltyPayments() {
		Cursor cursor = dbworker
				.getAllUnsynchronizedtbl_garage_loyaltyPayments();
		if (cursor.moveToFirst()) {
			do {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("deliver_order_id", cursor.getString(1));
					jsonObject.put("voucher_no", cursor.getString(2));
					jsonObject.put("amount", cursor.getString(3));
					jsonObject.put("remarks", cursor.getString(4));
					jsonObject.put("path", imagetobase64(cursor.getString(5)));
					Log.e("img", cursor.getString(5));
					jsonObject.put("date", cursor.getString(6));
					jsonObject.put("time", cursor.getString(7));

					/*
					 * voucher_no TEXT, amount TEXT,remarks TEXT,path TEXT, date
					 * TEXT,time TEXT,
					 */

					String[] parameters = { "sales_rep_id", "json_string" };
					String[] data = { userdata.getString("ID", ""),
							jsonObject.toString() };
					HttpResponse respons = jh.callServer(
							URLS.insert_cash_payments, parameters, data);

					String string_response = jh.responsDataConvertor(respons);
					Log.e("check this", string_response);

					JSONObject jobj = new JSONObject(string_response);
					if (jobj.getString("result").trim().equals("true")) {
						dbworker.upadate_tbl_garage_loyalty_sync_status_by_ID(cursor
								.getString(0));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	public void getAllChequePayments() {
		Cursor cursor = dbworker.getAllUnsynchronizedChequePayments();
		if (cursor.moveToFirst()) {
			do {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("deliver_order_id", cursor.getString(1));
					jsonObject.put("cheque_no", cursor.getString(2));
					jsonObject.put("amount", cursor.getString(3));
					jsonObject.put("bankname", cursor.getString(4));
					jsonObject.put("realised_date", cursor.getString(5));
					jsonObject.put("date", cursor.getString(7));
					jsonObject.put("time", cursor.getString(8));

					if (cursor.getString(6).equals("") == false) {
						String file_path = cursor.getString(6);
						jsonObject.put("image",imagetobase64(file_path));
					} else {
						jsonObject.put("image", "");
					}

					String[] parameters = { "sales_rep_id", "json_string" };
					String[] data = { userdata.getString("ID", ""),
							jsonObject.toString() };
					HttpResponse respons = jh.callServer(
							URLS.insert_cheque_payments, parameters, data);

					String string_response = jh.responsDataConvertor(respons);
					Log.e("check this", string_response);
					JSONObject jobj = new JSONObject(string_response);
					if (jobj.getString("result").trim().equals("true")) {
						dbworker.upadate_cheque_payment_sync_status_by_ID(cursor
								.getString(0));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	public void getAllBdepositPayments() {
		Cursor cursor = dbworker.getAllUnsynchronizedBdepPayments();
		if (cursor.moveToFirst()) {
			do {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("deliver_order_id", cursor.getString(1));
					jsonObject.put("slip_no", cursor.getString(2));
					jsonObject.put("amount", cursor.getString(3));
					jsonObject.put("bankname", cursor.getString(4));
					jsonObject.put("deposit_date", cursor.getString(5));

					if (!cursor.getString(6).equals("")) {
						jsonObject.put("image",imagetobase64(cursor.getString(6)));
					} else {
						jsonObject.put("image", "");
					}

					jsonObject.put("date", cursor.getString(7));
					jsonObject.put("time", cursor.getString(8));
					jsonObject.put("account_no", cursor.getString(9));

					String[] parameters = { "sales_rep_id", "json_string" };
					String[] data = { userdata.getString("ID", ""),
							jsonObject.toString() };
					HttpResponse respons = jh.callServer(
							URLS.insert_bank_dep_payments, parameters, data);

					String string_response = jh.responsDataConvertor(respons);
					Log.e("check this", string_response);
					JSONObject jobj = new JSONObject(string_response);
					if (jobj.getString("result").trim().equals("true")) {
						dbworker.upadate_bank_dep_sync_status_by_ID(cursor
								.getString(0));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	public void get_all_not_synchronized_purchase_orders() {

		JSONObject jsonObject = new JSONObject();

		Cursor cursor = dbworker.get_purchase_orders_NOT_SYNCED();
		if (cursor.moveToFirst()) {

			try {
				JSONArray order_Array = new JSONArray();
				do {

					JSONObject order_Object = new JSONObject();
					order_Object.put("ID", cursor.getString(0));
					order_Object.put("BillAmount", cursor.getString(10));
					order_Object.put("dealer_id", cursor.getString(2));
					order_Object.put("date_of_bill", cursor.getString(4));
					order_Object.put("lon", cursor.getString(5));
					order_Object.put("lat", cursor.getString(6));
					order_Object.put("b_level", cursor.getString(7));
					order_Object.put("complete", cursor.getString(8));
					order_Object.put("SYNC", cursor.getString(9));
					order_Object.put("iterner_status", cursor.getString(12));
					order_Object.put("remark_1", cursor.getString(15));
					order_Object.put("remark_2", cursor.getString(16));
					order_Object.put("order_type", cursor.getString(17));

					double y = 0;
					Cursor cursor_dis = dbworker.get_dealer_by_id(cursor
							.getString(2));
					if (cursor_dis.moveToFirst()) {
						y = cursor_dis.getDouble(6);
					}
					double b = Double.parseDouble(cursor.getString(10));

					double discount = (b * y) / (100 - y);

					order_Object.put("discount", discount);
					cursor_dis.close();

					JSONArray itemsArray = create_purchase_items(cursor
							.getString(0));

					order_Object.put("items", itemsArray);
					// Log.i("abc",order_Object.toString());
					order_Array.put(order_Object);
				} while (cursor.moveToNext());
				jsonObject.put("order", order_Array);
				Log.d("order", jsonObject.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cursor.close();

		String a = jh.send_Json(URLS.insert_order, jsonObject,
				userdata.getString("ID", ""));

		Log.wtf("response xxx", a);

		try {
			JSONObject response = new JSONObject(a);

			if (response.getString("result").equals("true")) {

				Cursor cursor2 = dbworker.get_purchase_orders_NOT_SYNCED();
				if (cursor2.moveToFirst()) {
					do {
						dbworker.update_sync_status(cursor2.getString(0));
					} while (cursor2.moveToNext());
				}
				cursor2.close();

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String imagetobase64(String path) {
		String img_string = "";
		String file_path = path;
		File file = new File(file_path);

		try {
			// Reading a Image file from file system
			FileInputStream imageInFile = new FileInputStream(file);
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);

			// Converting Image byte array into Base64
			// String

			img_string = Base64.encodeToString(imageData, Base64.DEFAULT);

			imageInFile.close();

			Log.i("success", "Image Successfully Manipulated!");
		} catch (FileNotFoundException e) {
			Log.e("error", "Image not found" + e);
		} catch (IOException ioe) {
			Log.e("error", "Exception while reading the Image " + ioe);
		}

		return img_string;
	}
	
	
	public void getAllPaymentReturns(){
		Cursor cursor = dbworker.getAllUnsynchronizedPaymentsReturnItems();
		if (cursor.moveToFirst()) {
			do {
				JSONObject jsonObject = new JSONObject();
				try {
							
					jsonObject.put("part_no", cursor.getString(2));
					jsonObject.put("qty", cursor.getString(3));
					jsonObject.put("value", cursor.getString(4));
					jsonObject.put("reason", cursor.getString(7));
					jsonObject.put("deliver_order_id", cursor.getString(1));
					jsonObject.put("remarks", cursor.getString(5));
					
					if (!cursor.getString(6).equals("")) {
						

							jsonObject.put("image", imagetobase64(cursor.getString(6)));

					} else {
						jsonObject.put("image","");
					}
					
					
					
					jsonObject.put("date", cursor.getString(8));
					jsonObject.put("time", cursor.getString(9));
					

					String[] parameters = { "sales_rep_id", "json_string" };
					String[] data = { userdata.getString("ID", ""),
							jsonObject.toString() };
					Log.e("dcdcdc", jsonObject.toString());
					HttpResponse respons = jh.callServer(
							URLS.insert_returns_payments, parameters, data);

					String string_response = jh.responsDataConvertor(respons);
					Log.e("check this", string_response);
					JSONObject jobj = new JSONObject(string_response);
					if (jobj.getString("result").trim().equals("true")) {
						dbworker.upadate_payment_return_sync_status_by_ID(cursor
								.getString(0));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} while (cursor.moveToNext());
		}
		cursor.close();
	};
}