package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dimosales.R;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.controller.StaticValues;
import ceylon.linux.db.Dbworker;
import ceylon.linux.model.Purchase_order_item_model;
import ceylon.linux.utility.Utility;

public class OrdersFragment extends Fragment {

	Dbworker dbworker;
	ArrayList<HashMap<String, String>> itemlist;
	private ListView listview_purchaseorder;
	private SimpleAdapter adapter;
	public static int purchase_order_edit = 0;
	public static String pur_id = "0";
	public static String dealer_id = "0";
	HashMap<String, String> hm;
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.view_purchase_order,
				container, false);
		dbworker = new Dbworker(getActivity());
		listview_purchaseorder = (ListView) rootView
				.findViewById(R.id.listview_purchaseorder);
	/*	if(HomeActivity.lastOdr == 0){
			load_data();
		}
		if(HomeActivity.lastOdr == 1){
			hm = Utility.readFromFile();
			if(hm != null){
				
				load_data2();
			}
		}*/
		load_data();
		 dbworker.close();
		return rootView;
	}

	public void load_data() {
		String params[] = {};

		new AsyncTask<String, String, String>() {

			protected void onPreExecute() {

			};

			@Override
			protected String doInBackground(String... params) {
				itemlist = dbworker.get_all_saved_purchaseOrders();
				
				adapter = new SimpleAdapter(getActivity(), itemlist,
						R.layout.view_order_listview_item, new String[] {
								"dealer_name", "delar_account_no",
								"BillAmount", "BillAmount_with_vat",
								"date_of_bill" }, new int[] { R.id.dealer_name,
								R.id.dealer_id, R.id.BillAmount,
								R.id.BillAmount_with_vat, R.id.date_of_bill }) {
					public View getView(final int position, View convertView,
							ViewGroup parent) {
						View r = super.getView(position, convertView, parent);

						HashMap<String, String> hashMap = itemlist.get(position);
						final String dealer_name = hashMap.get("dealer_name");
						dealer_id = hashMap.get("dealer_id");
						pur_id = hashMap.get("ID");
						HomeFragment.discount_percentage=hashMap.get("dealer_discount");
		
						HomeFragment.dealer_id = dealer_id;
						HomeFragment.dealer_name = dealer_name;
						HomeFragment.dealer_account_no = hashMap.get("delar_account_no");
						HomeFragment.overdue_amount= hashMap.get("overdue_amount");
						HomeFragment.outstanding_amount= hashMap.get("outstanding_amount");
						HomeFragment.credit_limit= hashMap.get("credit_limit");
						
						final String condition = hashMap.get("syncstatus");
						final String condition_fnish = hashMap.get("finish_status").trim();

						
						if (condition_fnish.equals("0")) {
							r.setBackgroundColor(Color.parseColor("#4D4DFF"));
						} else {
							if (!condition.equals("SYNC")) {
								r.setBackgroundColor(Color.parseColor("#FF8080"));
							} else {
								r.setBackgroundColor(Color.parseColor("#338533"));
							}
						}


						r.setOnLongClickListener(new OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {	
								final Dialog dialog = new Dialog(getActivity());
								dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
								dialog.setContentView(R.layout.edit_purchase_dialog);

								dialog.getWindow().setFeatureInt(
										Window.FEATURE_CUSTOM_TITLE,
										R.layout.titleview);
								View vv = dialog.getWindow().getDecorView();
								TextView txt_title = (TextView) vv.findViewById(R.id.title);
								txt_title.setText("Purchase Order");
								dialog.setCanceledOnTouchOutside(false);
								
								Button button_edit = (Button) dialog
										.findViewById(R.id.button_edit);
								Button button_delete = (Button) dialog
										.findViewById(R.id.button_delete);
								Button button_cancel = (Button) dialog
										.findViewById(R.id.button_cancel);
								button_cancel
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();
											}
										});
								button_delete
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dbworker.delete_purchase_order_by_id(pur_id);
												move_to_this_fragment();
												dialog.dismiss();
											}
										});
								button_edit
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												StaticValues.order_item_models.clear();
												HashMap<String, String> hashMap = itemlist
														.get(position);
												dealer_id = hashMap.get("dealer_id");
												pur_id = hashMap.get("ID");												
												if (condition_fnish.equals("5")
														&& condition.equals("NOT SYNC")) {
													move_to_another_fragment(new InvoiceInfoFragment(),"Purchase Order");
													HomeFragment.dealer_id = dealer_id;
													purchase_order_edit = 1;
													dialog.dismiss();
													StaticValues.order_item_models.clear();
													StaticValues.jasonNot = null;
													StaticValues.jasonPast = null;

													Cursor cursor_items = dbworker
															.get_purchase_order_items_by_id(pur_id);

										if (cursor_items.moveToFirst()) {

													do {
														Purchase_order_item_model model = new Purchase_order_item_model();
								
														model.setBill_id(cursor_items.getString(1));
														model.setComment(cursor_items.getString(7));
														model.setDescription(cursor_items.getString(2));
														model.setItem_id(cursor_items.getString(3));
														model.setAvg_movement("0");
														model.setPart_no(cursor_items.getString(6));
														model.setQty(cursor_items.getString(4));
														model.setSelling_price(cursor_items.getString(5));
						
														StaticValues.order_item_models.add(model);
	
														} while (cursor_items.moveToNext());
													}
													cursor_items.close();
												} else {
													Toast.makeText(
															getActivity(),
															"Could not edit",
															Toast.LENGTH_SHORT)
															.show();
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
		}.execute(params);
	}
	
	public void load_data2() {
		//purchase_order_edit = 2;
		String params[] = {};

		new AsyncTask<String, String, String>() {

			protected void onPreExecute() {

			};

			@Override
			protected String doInBackground(String... params) {

				final ArrayList<HashMap<String, String>> purchase_order_list = new ArrayList<HashMap<String, String>>();
				purchase_order_list.add(hm);

				adapter = new SimpleAdapter(getActivity(), purchase_order_list,
						R.layout.view_order_listview_item, new String[] {
								"dealer_name", "delar_account_no",
								"BillAmount", "BillAmount_with_vat",
								"date_of_bill" }, new int[] { R.id.dealer_name,
								R.id.dealer_id, R.id.BillAmount,
								R.id.BillAmount_with_vat, R.id.date_of_bill }) {
					public View getView(final int position, View convertView,
							ViewGroup parent) {
						View r = super.getView(position, convertView, parent);

						HashMap<String, String> hashMap = purchase_order_list
								.get(position);
						final String dealer_name = hashMap.get("dealer_name");
						dealer_id = hashMap.get("dealer_id");
						pur_id = hashMap.get("ID");
						HomeFragment.discount_percentage=hashMap.get("dealer_discount");
		
						HomeFragment.dealer_id = dealer_id;
						HomeFragment.dealer_name = dealer_name;
						HomeFragment.dealer_account_no = hashMap.get("delar_account_no");
						HomeFragment.overdue_amount= hashMap.get("overdue_amount");
						HomeFragment.outstanding_amount= hashMap.get("outstanding_amount");
						HomeFragment.credit_limit= hashMap.get("credit_limit");
						
						final String condition = hashMap.get("syncstatus");
						final String condition_fnish = hashMap.get("finish_status").trim();

						r.setBackgroundColor(Color.parseColor("#00AAAA"));
	
						r.setOnLongClickListener(new OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {	
								final Dialog dialog = new Dialog(getActivity());
								dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
								dialog.setContentView(R.layout.edit_purchase_dialog);

								dialog.getWindow().setFeatureInt(
										Window.FEATURE_CUSTOM_TITLE,
										R.layout.titleview);
								View vv = dialog.getWindow().getDecorView();
								TextView txt_title = (TextView) vv.findViewById(R.id.title);
								txt_title.setText("Purchase Order");
								dialog.setCanceledOnTouchOutside(false);
								
								Button button_edit = (Button) dialog
										.findViewById(R.id.button_edit);
								Button button_delete = (Button) dialog
										.findViewById(R.id.button_delete);
								Button button_cancel = (Button) dialog
										.findViewById(R.id.button_cancel);
								button_cancel
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();
											}
										});
								button_delete
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dbworker.delete_purchase_order_by_id(pur_id);
												move_to_this_fragment();
												dialog.dismiss();
											}
										});
								button_edit
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												StaticValues.order_item_models.clear();
											
													move_to_another_fragment(new InvoiceInfoFragment(),"Purchase Order");
													HomeFragment.dealer_id = dealer_id;
													purchase_order_edit = 2;
													dialog.dismiss();
													StaticValues.order_item_models.clear();
													StaticValues.jasonNot = null;
													StaticValues.jasonPast = null;


													int i = 1;

													 while (Utility.itmDet.length-1 > i) {
														Purchase_order_item_model model = new Purchase_order_item_model();
								
														model.setItem_id(Utility.itmDet[i]);
														i++;
														model.setPart_no(Utility.itmDet[i]);
														i++;
														model.setDescription(Utility.itmDet[i]);
														i++;
														model.setAvg_movement(Utility.itmDet[i]);
														i++;
														model.setQty(Utility.itmDet[i]);
														i++;
														model.setComment(Utility.itmDet[i]);
														i++;
														model.setSelling_price(Utility.itmDet[i]);
														i++;
														StaticValues.order_item_models.add(model);
	
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
		}.execute(params);
	}


	
	
	
public void move_to_another_fragment(Fragment f, String name) {

		Fragment fragment = f;
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(name);
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack("item")
				.commit();
	}

	public void move_to_this_fragment() {

		Fragment fragment = new OrdersFragment();
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle("Dealers");
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
