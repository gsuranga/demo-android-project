package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.db.Dbworker;
import ceylon.linux.url.URLS;

import com.example.dimosales.R;

public class ViewOrdersFragment extends Fragment {

	Dbworker dbworker;
	ArrayList<HashMap<String, String>> itemlist;
	private ListView listview_purchaseorder;
	private SimpleAdapter adapter;
	public static int purchase_order_edit = 0;
	public static String pur_id="0";
	public static String dealer_id="0";

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.view_purchase_order,
				container, false);
		dbworker = new Dbworker(getActivity());

		listview_purchaseorder = (ListView) rootView
				.findViewById(R.id.listview_purchaseorder);

		String params[] = {};
		xxx.execute(params);

		// dbworker.close();
		return rootView;
	}

	AsyncTask<String, String, String> xxx = new AsyncTask<String, String, String>() {

		protected void onPreExecute() {

		};

		@Override
		protected String doInBackground(String... params) {
		
			itemlist = dbworker.get_all_saved_purchaseOrders();

			adapter = new SimpleAdapter(getActivity(), itemlist,
					R.layout.view_order_listview_item, new String[] {
							"dealer_name", "dealer_id", "BillAmount",
							"BillAmount_with_vat", "date_of_bill" }, new int[] {
							R.id.dealer_name, R.id.dealer_id, R.id.BillAmount,
							R.id.BillAmount_with_vat, R.id.date_of_bill }) {
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View r = super.getView(position, convertView, parent);

					HashMap<String, String> hashMap = itemlist.get(position);
					final String dealer_name = hashMap.get("dealer_name");
					 dealer_id = hashMap.get("dealer_id");
					 pur_id = hashMap.get("ID");
					
					final String condition = hashMap.get("syncstatus");
					final String condition_fnish = hashMap.get("finish_status")
							.trim();

					if (condition_fnish.equals("0")) {
						r.setBackgroundColor(Color.parseColor("#4D4DFF"));
						
					} else {
						if (condition.equals("NOT SYNC")) {
							r.setBackgroundColor(Color.parseColor("#FF8080"));
						} else {
							r.setBackgroundColor(Color.parseColor("#338533"));
						}
					}

					r.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {

							// Toast.makeText(getActivity(), "lol",
							// Toast.LENGTH_SHORT).show();
							final Dialog dialog = new Dialog(getActivity());
							dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
							dialog.setContentView(R.layout.edit_purchase_dialog2);

							dialog.getWindow().setFeatureInt(
									Window.FEATURE_CUSTOM_TITLE,
									R.layout.titleview);
							View vv = dialog.getWindow().getDecorView();
							TextView txt_title = (TextView) vv
									.findViewById(R.id.title);
							txt_title.setText("Purchase Order");
							dialog.setCanceledOnTouchOutside(false);

							Button button_edit = (Button) dialog
									.findViewById(R.id.button_edit);
							Button button_finish = (Button) dialog
									.findViewById(R.id.button_finish);
							Button button_sync = (Button) dialog
									.findViewById(R.id.button_sync);
							Button button_cancel = (Button) dialog
									.findViewById(R.id.button_cancel);

							button_cancel
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											dialog.dismiss();
										}
									});

							button_sync
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											
											if (condition.equals("NOT SYNC")) {
											
											
											
											AsyncTask<String, String, String> upload = new AsyncTask<String, String, String>() {
												private ProgressDialog dialogx = new ProgressDialog(
														getActivity());

												@Override
												protected void onPreExecute() {
													super.onPreExecute();
													dialogx = ProgressDialog
															.show(getActivity(),
																	"",
																	"Please wait Upload Data to server... ",
																	false);
													dialogx.show();
												}

												@Override
												protected void onPostExecute(
														String result) {
													super.onPostExecute(result);

													itemlist = dbworker
															.get_all_saved_purchaseOrders();
													adapter.notifyDataSetChanged();

													dialogx.cancel();
												};

												@Override
												protected String doInBackground(
														String... params) {
													Cursor purchase_order = dbworker
															.get_purchase_order_by_purchase_order_id(pur_id);
													
													
													
													
													JSONArray purchase_orders = new JSONArray();
													JSONObject final_Wrapper = new JSONObject();
													JSONObject purchase_order_json_object = new JSONObject();

													if (purchase_order
															.moveToFirst()) {
														try {
															purchase_order_json_object
																	.put("ID",
																			purchase_order
																					.getString(0));
															purchase_order_json_object
																	.put("BillAmount",
																			purchase_order
																					.getString(1));
															purchase_order_json_object
																	.put("BillAmountwithvat",
																			purchase_order
																					.getString(10));
															purchase_order_json_object
																	.put("dealer_id",
																			purchase_order
																					.getString(2));
															purchase_order_json_object
																	.put("date_of_bill",
																			purchase_order
																					.getString(4));
															purchase_order_json_object
																	.put("lon",
																			purchase_order
																					.getString(5));
															purchase_order_json_object
																	.put("lat",
																			purchase_order
																					.getString(6));
															purchase_order_json_object
																	.put("b_level",
																			purchase_order
																					.getString(7));
															purchase_order_json_object
																	.put("completed",
																			purchase_order
																					.getString(8));
															purchase_order_json_object
																	.put("SYNC",
																			purchase_order
																					.getString(9));
															purchase_order_json_object
																	.put("iterner_status",
																			purchase_order
																					.getString(12));
															
															JSONArray items = new JSONArray();
															
															Cursor purchase_order_items = dbworker.get_purchase_items(purchase_order
																	.getString(0));
															if(purchase_order_items.moveToFirst())
															{
																do{
																	JSONObject item = new JSONObject();
																	
																	item.put("item_id", purchase_order_items.getString(3)
																			.toString());
																	item.put("qty", purchase_order_items.getString(4)
																			.toString());
																	item.put("price", purchase_order_items.getString(5)
																			.toString());
																	item.put("item_comment", purchase_order_items.getString(7)
																			.toString());
																	items.put(item);
																}
																while(purchase_order_items.moveToNext());
															}
															
															purchase_order_items.close();
															purchase_order_json_object.put("items", items);

															purchase_orders
																	.put(purchase_order_json_object);
															
															Jsonhelper jh = new Jsonhelper();
															
														SharedPreferences	userdata = getActivity().getSharedPreferences("USERDATA", Context.MODE_PRIVATE);
														get_all_newly_added_customers__with_vehicale();
														
														
														final_Wrapper.put("user_id", userdata.getString("ID", ""));
														final_Wrapper.put("order", purchase_orders);
														final_Wrapper.put("new_dealers", getNewlyAddedDealers());
														final_Wrapper.put("new_garages", get_all_newly_added_garages());
														final_Wrapper.put("customer_details",
																get_all_newly_added_customers__with_vehicale());
														
															jh.send_Json(URLS.insert_order, final_Wrapper, userdata.getString("ID", ""));
															
															
															for (int i = 0; i < purchase_orders.length(); i++) { // /

																JSONObject j;
																try {
																	j = purchase_orders.getJSONObject(i); //
																	dbworker.update_sync_status(j.getString("ID"));

																} catch (JSONException e) {

																	e.printStackTrace();
																}
															}

														} catch (JSONException e) {
															
															e.printStackTrace();
														}

													}
													
													purchase_order.close();
													

													return null;
												}
											};
											String xx[] = {};

											upload.execute(xx);
											
										}
										else
										{
										Toast.makeText(getActivity(), "Already Synchronized", Toast.LENGTH_SHORT).show();
										}
											dialog.dismiss();

										}
									});

							button_edit
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {

											if (condition_fnish.equals("0")){
												move_to_another_fragment(dealer_name);
											HomeFragment.dealer_id = dealer_id;
											purchase_order_edit = 1;
											dialog.dismiss();}
											else
											{
												Toast.makeText(getActivity(), "Could not edit", Toast.LENGTH_SHORT).show();
											}
										}
									});
							button_finish
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											if (condition_fnish.equals("0")){
											dbworker.update_finish_status_by_purch_id(pur_id);
											dialog.dismiss();
											itemlist = dbworker
													.get_all_saved_purchaseOrders();
											adapter.notifyDataSetChanged();
											}
											else
											{
												Toast.makeText(getActivity(), "Already finished", Toast.LENGTH_SHORT).show();
											}
										}
									});
							dialog.show();

							return false;
						}
					});

					return r;

				};

			};

			return null;
		}

		protected void onPostExecute(String result) {

			listview_purchaseorder.setAdapter(adapter);

		};
	};

	public void move_to_another_fragment(String dealer_name) {

		Fragment fragment = new InvoiceInfoFragment();
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(
				"Invoice Information(" + dealer_name + ")");
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack(null)
				.commit();

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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			get_all_newly_added_garages.close();
		}

		return j;

	}

}
