package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dimosales.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.controller.StaticValues;
import ceylon.linux.db.Dbworker;
import ceylon.linux.url.URLS;
import ceylon.linux.utility.NumbeFormater;

public class Call_order_fragment extends Fragment {
	public static String updated_bill_id;
	public static String update_status;
	Dbworker dbworker;
	SharedPreferences userdata;
	BaseExpandableListAdapter adapter;
	HashMap<String, String> purchase_order_details;
	private ArrayList<CallOrder> callOrders;


	public Call_order_fragment() {
		update_status = "0";

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		dbworker = new Dbworker(getActivity());
		get_call_orders();
		View rootView = inflater.inflate(R.layout.call_order_fragment,
			container, false);
		userdata = getActivity().getSharedPreferences("USERDATA",
			Context.MODE_PRIVATE);
		StaticValues.order_item_models.clear();
		ItemFragment.clicked_items.clear();
		ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.orderListView);

		adapter = new BaseExpandableListAdapter() {

			@Override
			public boolean isChildSelectable(int arg0, int arg1) {
				return true;
			}

			@Override
			public boolean hasStableIds() {
				return false;
			}

			@Override
			public View getGroupView(int arg0, boolean arg1, View arg2,
			                         ViewGroup arg3) {
				if (arg2 == null) {
					LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					arg2 = inflater.inflate(R.layout.manage_order_list_item,null);
					
					
				}
				TextView dealerName = (TextView) arg2.findViewById(R.id.txtDealerName);
				TextView date = (TextView) arg2.findViewById(R.id.txtDateOfBill);
				CallOrder callOrder = getGroup(arg0);
				dealerName.setText(callOrder.dealerName);
				date.setText(callOrder.date);
				return arg2;
			}

			@Override
			public long getGroupId(int arg0) {
				return arg0;
			}

			@Override
			public int getGroupCount() {
				return callOrders.size();
			}

			@Override
			public CallOrder getGroup(int arg0) {
				return callOrders.get(arg0);
			}

			@Override
			public int getChildrenCount(int arg0) {
				return 1;
			}

			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
			}

			@Override
			public View getChildView(final int arg0, int arg1, boolean arg2,
			                         View arg3, ViewGroup arg4) {
				if (arg3 == null) {
					LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					arg3 = inflater.inflate(R.layout.order_detail_page, null);
				}

				CallOrder callOrder = getGroup(arg0);
				ArrayList<Item> items = callOrder.items;

				TextView dealerName = (TextView) arg3.findViewById(R.id.dealerName);
				TextView dealerAccountNo = (TextView) arg3.findViewById(R.id.dealerAccountNo);
				TextView dateOfBill = (TextView) arg3.findViewById(R.id.dateOfBill);
				TextView billAmount = (TextView) arg3.findViewById(R.id.billAmount);

				TextView billAmount_vat = (TextView) arg3.findViewById(R.id.billAmount_vat);

				dealerName.setText(callOrder.dealerName);
				dealerAccountNo.setText(callOrder.account_no);
				dateOfBill.setText(callOrder.date);
				billAmount.setText(NumbeFormater.round(callOrder.amount, 2));

				//Double newvalue = Double.valueOf(billAmount.getText()
				//	.toString())
				//	+ (Double.valueOf(billAmount.getText().toString())
				//	* Double.valueOf(userdata.getString("vat", "")) / 100);

				billAmount_vat.setText(NumbeFormater.round(callOrder.amount_with_vat, 2));

				LinearLayout selectedItemsListView = (LinearLayout) arg3.findViewById(R.id.selectedItemsListView);
				selectedItemsListView.removeAllViews();
				for (int i = 0; i < callOrder.items.size(); i++) {
					View itemView = inflater.inflate(R.layout.suggetion_order_detail, null);
					TextView txtDescription = (TextView) itemView.findViewById(R.id.txtItemName);
					TextView txtQty = (TextView) itemView.findViewById(R.id.txtQuantity);
					TextView txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);
					TextView txtprice = (TextView) itemView.findViewById(R.id.txtprice);
					Item item = callOrder.items.get(i);
					txtDescription.setText(item.itemName);
					txtQty.setText(item.quantity);
					txtprice.setText(NumbeFormater.round(item.price != null && !item.price.isEmpty() ? item.price : "0", 2));
					txtAmount.setText(NumbeFormater.round(item.amount != null && !item.amount.isEmpty() ? item.amount : "0", 2));
					itemView.setBackgroundColor((i % 2 == 0) ? Color.parseColor("#E6E6E6") : Color.parseColor("#ffffff"));
					selectedItemsListView.addView(itemView);
				}

				return arg3;
			}

			@Override
			public long getChildId(int arg0, int arg1) {
				return arg1;
			}

			@Override
			public CallOrder getChild(int arg0, int arg1) {
				return callOrders.get(arg0);
			}
		};
		expandableListView.setAdapter(adapter);

		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
			                            int arg2, int arg3, long arg4) {
				Toast.makeText(getActivity(), arg2 + "", Toast.LENGTH_LONG)
					.show();

				CallOrder s = callOrders.get(arg2);

				// Log.i("Account Number Is", arg2 + " ");
		//		name(s.userName, s.password, s.billId, arg2, s.dealerName, s);

				return false;
			}

		});

		expandableListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent,
			                               View view, int position, long id) {
				if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
					int groupPosition = ExpandableListView
						.getPackedPositionGroup(id);
					int childPosition = ExpandableListView
						.getPackedPositionChild(id);
					CallOrder s =callOrders.get(groupPosition);
			//		reject_update(s.billId, s.dealerName, s);

//TODO
					
					String[] params={s.billId};
					
					new AsyncTask<String, String, String>() {
						
						private Jsonhelper jh = new Jsonhelper();
						private ProgressDialog dialog= new ProgressDialog(getActivity());
						
						@Override
						protected void onPreExecute() {

							super.onPreExecute();
							dialog = ProgressDialog.show(getActivity(), "",
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
						
						

						@Override
						protected String doInBackground(String... params) {
							Log.wtf("sss", "sss");
							
							get_all_not_synchronized_purchase_orders(params[0]);
							return null;
						}
						
						public void get_all_not_synchronized_purchase_orders(String id) {

							JSONObject jsonObject = new JSONObject();

							Cursor cursor = dbworker.get_purchase_order_by_id(id);
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

							String a=jh.send_Json(URLS.insert_order, jsonObject, userdata.getString("ID", ""));

							Log.wtf("response xxx", a);
							
							try {
								JSONObject response = new JSONObject(a);
								
							if(response.getString("result").equals("true"))
							{
								
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
						
					}.execute(params);
					
					
					
					return true;
				}

				return false;
			}
		});
		return rootView;
	}

	public void namef(final String uname, final String pw, final String bill_id, final int index, String dealer_name, CallOrder s) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle(dealer_name);
		final EditText UserName = new EditText(getActivity());

		final EditText password = new EditText(getActivity());
		password.setHint("Password");
		password.setInputType(InputType.TYPE_CLASS_TEXT
			| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		UserName.setHint("UserName");

		LinearLayout ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.addView(UserName);
		ll.addView(password);

		alertDialog.setView(ll);
		alertDialog.setCancelable(false);
		alertDialog.setPositiveButton("Accept Order",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					if (uname.equals(UserName.getText().toString())
						&& pw.equals(password.getText().toString())) {

						callOrders.remove(index);
						adapter.notifyDataSetChanged();

						// change status to 0
						dbworker.update_to_accepted_order(bill_id, "0");
						dbworker.close();

					} else {
						Toast.makeText(getActivity(), "Wrong UserName Password ", Toast.LENGTH_LONG).show();

					}

				}
			}).setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});

		AlertDialog alert = alertDialog.create();
		alert.show();
	}

	public void reject_update(final String bill_id, String dealer_name, final CallOrder s) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle(dealer_name);
		final Button reject = new Button(getActivity());
		final Button update = new Button(getActivity());
		reject.setText("Reject");
		update.setText("Update");

		LinearLayout ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.addView(reject);
		ll.addView(update);

		alertDialog.setView(ll);
		alertDialog.setCancelable(false);

		alertDialog.setNegativeButton("Close",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.cancel();

				}
			});

		final AlertDialog alert = alertDialog.create();

		reject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dbworker.update_to_accepted_order(bill_id, "-1");
				alert.cancel();

				dbworker.close();
			}
		});
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				update_status = "1";

				HomeFragment.discount_percentage = s.discount;
				HomeFragment.dealer_name = s.dealerName;
				updated_bill_id = s.billId;
				HomeFragment.dealer_id = s.dealer_id;
				Log.i("Bill_id id", bill_id);

				ItemFragment.item_id = "-1";
				for (Item ss : s.items) {
					purchase_order_details = new HashMap<String, String>();
					purchase_order_details.put("description", ss.itemName);
					purchase_order_details.put("selling_price", ss.price);
					purchase_order_details.put("item_id", ss.item_id);
					purchase_order_details.put("quantity", ss.quantity);
					purchase_order_details.put("part_no", ss.item_code);


				//	Purchase_Order_Fragement.wrapper.add(purchase_order_details);
				}

				move_to_another_fragment(new Purchase_Order_Fragement());
				alert.cancel();

			}
		});

		alert.show();
	}

	public void move_to_another_fragment(Fragment f) {

		Fragment fragment = f;
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle("Items");
		fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).addToBackStack(null)
			.commit();

	}

	public void get_call_orders() {
		callOrders = new ArrayList<CallOrder>();
		Cursor cursor = dbworker.get_all_call_orders("4");
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
				.moveToNext()) {
				CallOrder callOrder = new CallOrder();
				// dealername
				callOrder.dealerName = cursor.getString(0);
				// username
				callOrder.userName = cursor.getString(1);
				// password
				callOrder.password = cursor.getString(2);
				// dealer_id
				callOrder.dealer_id = cursor.getString(3);

				// bill_id
				callOrder.billId = cursor.getString(4);
				// bill date
				callOrder.date = cursor.getString(5);
				
				
				// BillAmount
				callOrder.amount = cursor.getString(6);
				// BillAmount with vat
				callOrder.amount_with_vat = cursor.getString(7);
				//   vat
				callOrder.discount = cursor.getString(8);
				callOrder.account_no = cursor.getString(10);

				Log.i("cursor.getString(6)", cursor.getString(6));
				Log.i("cursor.getString(7)", cursor.getString(7));
				Cursor purchase_items = dbworker
					.get_purchase_items(callOrder.billId);
				int itemNameIndex = purchase_items
					.getColumnIndex("description");
				int price = purchase_items.getColumnIndex("price");
				int quantity = purchase_items.getColumnIndex("qty");
				int item_id = purchase_items.getColumnIndex("item_id");
				int item_code = purchase_items.getColumnIndex("part_no");

				for (purchase_items.moveToFirst(); !purchase_items
					.isAfterLast(); purchase_items.moveToNext()) {
					Item item = new Item();
					item.itemName = purchase_items.getString(itemNameIndex);
					item.price = purchase_items.getString(price);
					item.quantity = purchase_items.getString(quantity);
					item.amount = Double.toString(Double.parseDouble(item.price != null && !item.price.isEmpty() ? item.price : "0") * Double.parseDouble(item.quantity != null && !item.quantity.isEmpty() ? item.quantity : "0"));
					item.item_id = purchase_items.getString(item_id);
					item.item_code = purchase_items.getString(item_code);
					callOrder.items.add(item);
				}

				callOrders.add(callOrder);
			}
			cursor.close();
		}
	}

	private static class CallOrder {
		String billId;
		String userName;
		String password;
		String amount;
		ArrayList<Item> items = new ArrayList<Item>();

		String date;
		String dealerName;
		String amount_with_vat;
		String discount;
		String dealer_id;
		String account_no;
	}

	private static class Item {
		String itemName;
		String price;
		String item_id;
		String quantity;
		String amount;
		String item_code;

	}

}
