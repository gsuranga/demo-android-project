package ceylon.linux.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.controller.StaticValues;
import ceylon.linux.db.Dbworker;
import ceylon.linux.model.Purchase_order_item_model;
import ceylon.linux.url.URLS;
import ceylon.linux.utility.Utility;

@SuppressLint("NewApi")
public class InvoiceInfoFragment extends Fragment {

	public static ArrayList<String> invoice_parts = new ArrayList<String>();
	public static ArrayList<String> fast_moingitems = new ArrayList<String>();
	public static ArrayList<String> not_achieved = new ArrayList<String>();

	// public static ArrayList<Purchase_order_item_model>
	// purchase_order_item_models = new ArrayList<Purchase_order_item_model>();
	public static String status;

	HashMap<String, String> purchase_order_details;
	private static Cursor cursor;
	private static int selectedItems = 0;
	LinearLayout tl;
	LinearLayout part_number_layout;
	LinearLayout layout_not_achived;
	Dbworker db;
	Button next_item_fragment, moveButton;
	ToggleButton sitems, fastMoving, notAchieved;
	Jsonhelper j;
	Utility utility;
	SharedPreferences userdata;
	View rootView;
	public static int status_fragment = 0;

	private ArrayList<NameValuePair> nameValuePairs;

	public InvoiceInfoFragment() {
		status = "1";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_invoice_info,
				container, false);

		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		db = new Dbworker(getActivity());
		userdata = getActivity().getSharedPreferences("USERDATA",
				Context.MODE_PRIVATE);

		part_number_layout = (LinearLayout) rootView
				.findViewById(R.id.part_number_layout);
		layout_not_achived = (LinearLayout) rootView
				.findViewById(R.id.layout_not_achived);
		next_item_fragment = (Button) rootView.findViewById(R.id.next);
		sitems = (ToggleButton) rootView.findViewById(R.id.s_items);
		fastMoving = (ToggleButton) rootView.findViewById(R.id.fastMoving);
		notAchieved = (ToggleButton) rootView.findViewById(R.id.notAchieved);

		sitems.setChecked(true);
		fastMoving.setChecked(true);
		notAchieved.setChecked(true);
		moveButton = (Button) rootView
				.findViewById(R.id.move_to_purchase_order);
		moveButton.setOnClickListener(move_to_purchase_order);
		tl = (LinearLayout) rootView.findViewById(R.id.suggestedOrdersList);
		utility = new Utility(getActivity());
		invoice_parts.clear();
		j = new Jsonhelper();

	///	populate_invoice_info();
	//	getFastMovingItemInArea();
		getNotAchivedTargets();

		next_item_fragment.setOnClickListener(next);
		switch (selectedItems) {
		case 1:
			fastMoving.performClick();
			notAchieved.performClick();
			break;
		case 2:
			sitems.performClick();
			notAchieved.performClick();
			break;
		case 3:
			sitems.performClick();
			fastMoving.performClick();
			break;
		}

		sitems.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View view = getActivity().findViewById(R.id.suggestionLayout);

				if (sitems.isChecked()) {
					view.setVisibility(View.VISIBLE);

				} else {
					view.setVisibility(View.GONE);

				}
			}
		});
		
		fastMoving.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = getActivity().findViewById(
						R.id.fastMovingItemsLayout);

				if (fastMoving.isChecked()) {
					view.setVisibility(View.VISIBLE);

				} else {
					view.setVisibility(View.GONE);

				}
			}
		});
		
		notAchieved.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = getActivity().findViewById(R.id.notAchievedLayout);

				if (notAchieved.isChecked()) {
					view.setVisibility(View.VISIBLE);

				} else {
					view.setVisibility(View.GONE);

				}
			}
		});

		if (ViewSavedOrdersFragment.purchase_order_edit == 0) {
			if (StaticValues.order_item_models == null
					|| StaticValues.order_item_models.isEmpty()) {
				moveButton.setEnabled(false);

			} else {
				moveButton.setEnabled(true);

			}
		} else {
			moveButton.setEnabled(true);
		}
		return rootView;
	}

	OnClickListener suggestion_order_on_click_listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (ViewSavedOrdersFragment.purchase_order_edit == 0) {
				Button b = (Button) arg0;
				b.setText("Already Added");
				name(arg0.getTag().toString());
				fastMoving.setChecked(false);
				notAchieved.setChecked(false);
				b.setOnClickListener(move_to_purchase_order);
				selectedItems = 1;
			} else {
				qty_entering_dialog((Button) arg0, arg0.getTag().toString());
			}
		}
	};
	OnClickListener fast_moving_on_click_listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (ViewSavedOrdersFragment.purchase_order_edit == 0) {
				Button b = (Button) arg0;
				b.setText("Already Added");
				name(arg0.getTag().toString());
				sitems.setChecked(false);
				notAchieved.setChecked(false);
				b.setOnClickListener(move_to_purchase_order);
				selectedItems = 2;
			} else {

				qty_entering_dialog((Button) arg0, arg0.getTag().toString());
			}
		}
	};
	OnClickListener not_achieved_on_click_listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (ViewSavedOrdersFragment.purchase_order_edit == 0) {
				Button b = (Button) arg0;

				b.setText("Already Added");
				name(arg0.getTag().toString());
				b.setOnClickListener(move_to_purchase_order);
				sitems.setChecked(false);
				fastMoving.setChecked(false);
				selectedItems = 3;
			} else {
				qty_entering_dialog((Button) arg0, arg0.getTag().toString());
			}
		}
	};

	OnClickListener next = new OnClickListener() {
		@Override
		public void onClick(View v) {

			move_to_another_fragment(new ItemFragment(),
					HomeFragment.dealer_name);
		}
	};
	OnClickListener move_to_purchase_order = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ItemFragment.item_id = "-1";
			move_to_another_fragment(new Purchase_Order_Fragement(),
					HomeFragment.dealer_name);
		}
	};

	public void move_to_another_fragment(Fragment f, String name) {
		Fragment fragment = f;
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(name);
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack("item")
				.commit();
	}

	public void getFastMovingItemInArea() {
		fast_moingitems.clear();

		// part_number_layout

		if (utility.isNetworkAvailable()) {

			AsyncTask<String, Integer, JSONObject> logindetails = new Download_data2() {
				protected ProgressDialog dialogxx;

				@Override
				protected void onPreExecute() {

					super.onPreExecute();
					dialogxx = new ProgressDialog(getActivity());
					dialogxx.setMessage("Downloading Data From Server");
					dialogxx.setCanceledOnTouchOutside(false);
					dialogxx.show();
				}

				@Override
				protected void onPostExecute(JSONObject result) {

					//Log.i("JSON IS", result.toString());
					super.onPostExecute(result);

					if (dialogxx != null && dialogxx.isShowing()) {
						try {
							dialogxx.dismiss();

						} catch (Exception e) {
							// nothing
						}
					}
					int count = 0;

					JSONArray array;
					try {
						array = result.getJSONArray("fast_mooving_items");
						for (int i = 0; i < array.length(); i++) {
							LinearLayout tr = new LinearLayout(getActivity());

							if (count % 2 != 0) {
								tr.setBackgroundColor(Color
										.parseColor("#1A5876"));
							}
							JSONObject j = (JSONObject) array.get(i);
							tr.setId(100 + count);
							tr.setWeightSum(400f);

							LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT);
							LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
									0, LayoutParams.MATCH_PARENT, 100);

							tr.setOrientation(LinearLayout.HORIZONTAL);
							tr.setLayoutParams(new LayoutParams(param));

							TextView part_no = new TextView(getActivity());
							part_no.setId(200 + count);
							fast_moingitems.add(j.getString("item_part_no"));
							part_no.setText(j.getString("item_part_no"));
							part_no.setTag("part_no");
							part_no.setTextColor(Color.BLACK);
							part_no.setGravity(Gravity.CENTER);
							part_no.setLayoutParams(param2);
							tr.addView(part_no);

							TextView description_show = new TextView(
									getActivity());
							description_show.setTag("Description");
							description_show.setId(200 + count);
							description_show.setText(j.getString("description")
									.toString());
							description_show.setTextColor(Color.BLACK);
							description_show.setGravity(Gravity.CENTER);
							description_show.setLayoutParams(param2);
							tr.addView(description_show);

							TextView quantity = new TextView(getActivity());
							quantity.setId(200 + count);
							quantity.setText(j.getString("quantity").toString());
							quantity.setTag("quantity");
							quantity.setTextColor(Color.BLACK);
							quantity.setGravity(Gravity.CENTER);
							quantity.setLayoutParams(param2);
							tr.addView(quantity);

							if (ItemFragment.clicked_items.contains(part_no
									.getText().toString())) {

								Button AddItem = new Button(getActivity());
								AddItem.setText("Already Added");
								AddItem.setTextColor(Color.CYAN);
								AddItem.setTag(part_no.getText().toString());
								AddItem.setPadding(5, 5, 5, 5);
								AddItem.setGravity(Gravity.CENTER);
								AddItem.setLayoutParams(param2);
								AddItem.setOnClickListener(move_to_purchase_order);
								tr.addView(AddItem);

							} else {

								Button AddItem = new Button(getActivity());
								AddItem.setText("AddItem"); // set the text for
								AddItem.setTextColor(Color.RED); // set the
								AddItem.setTag(part_no.getText().toString());
								AddItem.setPadding(5, 5, 5, 5);
								AddItem.setGravity(Gravity.CENTER);
								AddItem.setOnClickListener(fast_moving_on_click_listener);
								AddItem.setLayoutParams(param2);
								tr.addView(AddItem);
							}

							part_number_layout.addView(tr);
							count++;

						}

					} catch (JSONException e1) {

						e1.printStackTrace();
					}

				}

				@Override
				protected JSONObject doInBackground(String... params) {
					// TODO Auto-generated method stub

					return super.doInBackground(params);
				}
			};
			String[] params = { "dealer_id", URLS.fast_moving,
					HomeFragment.dealer_id };

			Log.d("delerid is", HomeFragment.dealer_id);

			if (utility.isNetworkAvailable()) {

				logindetails.execute(params);
			} else {
				Toast.makeText(getActivity(), "Internet is not available",
						Toast.LENGTH_LONG).show();

			}

		}

	}

	// GET NOT Achived Targets
	public void getNotAchivedTargets() {

		AsyncTask<String, Integer, JSONObject> ntachived = new AsyncTask<String, Integer, JSONObject>() {
			protected ProgressDialog dialog1;

			@Override
			protected void onPreExecute() {

				super.onPreExecute();
				dialog1 = new ProgressDialog(getActivity());
				dialog1.setMessage("Downloading Data From Server");
				dialog1.setCanceledOnTouchOutside(false);
				dialog1.show();
			}

			@SuppressWarnings("deprecation")
			@Override
			protected JSONObject doInBackground(String... params) {

				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("dealer_id",
						params[0]));
				nameValuePairs.add(new BasicNameValuePair("target_month",	
						params[1]));
				nameValuePairs.add(new BasicNameValuePair("officer_id",
						params[2]));
				if (StaticValues.jasonNot == null) {
					StaticValues.jasonNot = new JSONObject();
				}
					StaticValues.jasonNot = j
							.JsonObjectSendToServerPostWithNameValuePare(
									URLS.nt_achived_targets, nameValuePairs);
					Log.i("not achived",StaticValues.jasonNot.toString());
					
					Log.i("XXX", params[0]+"/" +params[1]+"/"+params[2] );
				

				return StaticValues.jasonNot;

			}

			@Override
			protected void onPostExecute(JSONObject result) {

				super.onPostExecute(result);
				not_achieved.clear();

				int count = 0;

				JSONArray array;
				try {

					array = result.getJSONArray("not_achieved");
					LinearLayout show_not_achived_targets;

					show_not_achived_targets = (LinearLayout) getActivity()
							.findViewById(R.id.show_not_achived_targets);

					for (int i = 0; i < array.length(); i++) {

						LinearLayout tr = new LinearLayout(getActivity());

						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT, 0, 500f);
						LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
								0, LinearLayout.LayoutParams.MATCH_PARENT, 100f);
						tr.setLayoutParams(params);

						if (count % 2 != 0) {
							tr.setBackgroundColor(Color.parseColor("#1A5876"));
						}
						JSONObject j = array.getJSONObject(i);
						tr.setId(100 + count);
						Log.i("JSON object is", array.get(i).toString());
						TextView part_no = new TextView(getActivity());
						part_no.setId(200 + count);

						part_no.setText(j.getString("item_part_no"));
						part_no.setTag("part_no");
						part_no.setTextColor(Color.BLACK);
						part_no.setGravity(Gravity.CENTER);
						part_no.setLayoutParams(params2);
						tr.addView(part_no);
						//not_achieved.add(j.getString("item_part_no"));

						TextView description_show = new TextView(getActivity());
						description_show.setTag("Description");
						description_show.setId(200 + count);
						description_show.setText(j.getString("description").toString());
						description_show.setTextColor(Color.BLACK);
						description_show.setGravity(Gravity.LEFT);
						description_show.setLayoutParams(params2);

						tr.addView(description_show);

						TextView m_quantity = new TextView(getActivity());
						m_quantity.setId(200 + count);
						m_quantity.setText(j.getString("tot_minimum_qty").toString());
						m_quantity.setTag("m_quantity");
						m_quantity.setTextColor(Color.BLACK);
						m_quantity.setGravity(Gravity.CENTER);
						m_quantity.setLayoutParams(params2);
						tr.addView(m_quantity);

						TextView a_quantity = new TextView(getActivity());
						a_quantity.setId(200 + count);
						a_quantity.setText(j.getString("tot_additional_qty").toString());
						a_quantity.setTag("a_quantity");
						a_quantity.setTextColor(Color.BLACK);
						a_quantity.setGravity(Gravity.CENTER);
						a_quantity.setLayoutParams(params2);
						tr.addView(a_quantity);

						TextView aa_quantity = new TextView(getActivity());
						aa_quantity.setId(200 + count);
						aa_quantity.setText(j.getString("total_actual").toString());
						aa_quantity.setTag("aa_quantity");
						aa_quantity.setTextColor(Color.BLACK);
						aa_quantity.setGravity(Gravity.CENTER);
						aa_quantity.setLayoutParams(params2);
						tr.addView(aa_quantity);

						if (ItemFragment.clicked_items.contains(part_no
								.getText().toString())) {

							Button AddItem = new Button(getActivity());
							//AddItem.setText("Already Added"); // set the text
							// for the
							AddItem.setTextColor(Color.CYAN); // set the color
							AddItem.setTag(part_no.getText().toString());
							AddItem.setPadding(5, 5, 5, 5);
							AddItem.setGravity(Gravity.CENTER);
							AddItem.setOnClickListener(move_to_purchase_order);
							AddItem.setLayoutParams(params2);
							AddItem.setEnabled(false);
							tr.addView(AddItem);

						} else {

							Button AddItem = new Button(getActivity());
							AddItem.setText("AddItem"); // set the text for the
							AddItem.setTextColor(Color.RED); // set the color
							AddItem.setTag(part_no.getText().toString());
							AddItem.setPadding(5, 5, 5, 5);
							AddItem.setGravity(Gravity.CENTER);
							AddItem.setOnClickListener(not_achieved_on_click_listener);
							AddItem.setLayoutParams(params2);
							tr.addView(AddItem);
						}

						show_not_achived_targets.addView(tr);
						count++;
					}

					if (dialog1 != null && dialog1.isShowing()) {
						try {
							dialog1.dismiss();

						} catch (Exception e) {
							// nothing
						}
					}

				} catch (Exception e1) {

					e1.printStackTrace();
				}

			}

		};
		
		
		
		
		   Calendar c = Calendar.getInstance();
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
	        String formattedDate = df.format(c.getTime());
		

		String params[] = { HomeFragment.dealer_id, formattedDate.trim(),
				userdata.getString("ID", "").toString() };
		if (utility.isNetworkAvailable()) {

			ntachived.execute(params);
		} else {
			Toast.makeText(getActivity(), "Internet is not available",
					Toast.LENGTH_LONG).show();

		}

	}

	public void name(final String item_id) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle(item_id);
		final TextView Error = new TextView(getActivity());
		final TextView show_selling_price_with_vat_and_qty = new TextView(
				getActivity());
		final EditText quantity = new EditText(getActivity());
		show_selling_price_with_vat_and_qty.setBackgroundColor(Color.CYAN);

		quantity.setHint("Enter Quantity Here");
		quantity.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);

		LinearLayout ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.addView(quantity);
		ll.addView(Error);
		ll.addView(show_selling_price_with_vat_and_qty);
		Log.e("111", item_id.toString());

		Cursor cursor = db.get_item_details(item_id.toString());
		Double vat = Double.valueOf(userdata.getString("vat", ""));
		String item_code = null;
		String description = null;
		String qty = null;
		Double selling_price_with_vat = 0.0;

		Double selling_price = 0.0;

		if (cursor.moveToFirst()) {
			selling_price = Double.valueOf(cursor.getString(3));
			item_code = cursor.getString(0);

			Log.e("aaa", cursor.getString(1));
			description = cursor.getString(1);
			qty = cursor.getString(4);
			selling_price_with_vat = selling_price
					+ (selling_price * vat / 100);

			show_selling_price_with_vat_and_qty
					.setText("Selling Price With VAT:-"
							+ Utility
									.formatStringAmounts(selling_price_with_vat
											.toString()) + "   "
							+ "VSD stock is:-" + qty);

		}
		cursor.close();
		final Double selling_price2 = selling_price;
		final String item_code2 = item_code;
		final String description2 = description;
		alertDialog.setView(ll);
		alertDialog.setCancelable(false);
		alertDialog.setPositiveButton("Add Quantity",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						if (quantity.getText().toString().trim().equals("")) {

							Toast.makeText(getActivity(), "Invalid Quantity",
									Toast.LENGTH_LONG).show();
						} else {

							purchase_order_details = new HashMap<String, String>();

							ItemFragment.description = description2;
							ItemFragment.selling_price = selling_price2
									.toString();
							ItemFragment.part_no = item_id.toString();
							ItemFragment.item_id = item_code2;
							ItemFragment.qty = quantity.getText().toString();
							ItemFragment.clicked_items.add(item_id);
							ItemFragment.showAvgMovementAtDealer = "0.0";

							Cursor cursor1 = db.findDealerStockForItem(
									HomeFragment.dealer_account_no, item_id);

							if (cursor1 != null) {
								cursor1.moveToFirst();

								while (!cursor1.isAfterLast()) {
									DecimalFormat df = new DecimalFormat("#.##");
									String formatted = df.format(Double
											.valueOf(cursor1.getString(0)));
									ItemFragment.showAvgMovementAtDealer = formatted;

									cursor1.moveToNext();
								}
								cursor1.close();
							}

							Purchase_order_item_model model = new Purchase_order_item_model();
							model.setAvg_movement(ItemFragment.showAvgMovementAtDealer);
							model.setBill_id("");
							model.setComment("");
							model.setDescription(ItemFragment.description);
							model.setItem_id(ItemFragment.item_id);
							model.setPart_no(ItemFragment.part_no);
							model.setQty(ItemFragment.qty);
							model.setSelling_price(ItemFragment.selling_price);

							StaticValues.order_item_models.add(model);

							moveButton.setEnabled(true);

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

	public void qty_entering_dialog(final Button button, final String item_id) {// TODO

		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		dialog.setContentView(R.layout.enter_purchase_item_qty_dialog);

		dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titleview);
		View vv = dialog.getWindow().getDecorView();
		TextView txt_title = (TextView) vv.findViewById(R.id.title);
		txt_title.setText(item_id);

		final EditText edit_qty = (EditText) dialog.findViewById(R.id.edit_qty);
		final EditText edit_comment = (EditText) dialog
				.findViewById(R.id.edit_comment);
		TextView txt_price_with_vat = (TextView) dialog
				.findViewById(R.id.txt_price_with_vat);
		TextView txt_vsd_stock = (TextView) dialog
				.findViewById(R.id.txt_vsd_stock);

		Cursor cursorx = db.get_item_details(item_id.toString());
		if (cursorx.moveToFirst()) {
			txt_price_with_vat.setText(txt_price_with_vat.getText()
					+ cursorx.getString(3));
			txt_vsd_stock.setText(txt_vsd_stock.getText()
					+ cursorx.getString(5));
		}
		cursorx.close();

		Button button_add_qty = (Button) dialog
				.findViewById(R.id.button_add_qty);
		Button button_cancel = (Button) dialog.findViewById(R.id.button_cancel);

		button_add_qty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				button.setText("Already purchased");
				Purchase_order_item_model item_model = new Purchase_order_item_model();
				Cursor cursorxy = db.get_item_details(item_id.toString());
				if (cursorxy.moveToFirst()) {
					item_model.setItem_id(cursorxy.getString(0));
					item_model.setQty(edit_qty.getText().toString());
					item_model.setComment(edit_comment.getText().toString());
					item_model.setDescription(cursorxy.getString(1));
					item_model.setPart_no(cursorxy.getString(2));
					item_model.setBill_id(ViewSavedOrdersFragment.pur_id);
					item_model.setSelling_price(cursorxy.getString(3));
				}
				cursorxy.close();

				item_model.setAvg_movement("0");

				// item_model.setItem_id(item_id)

				// purchase_order_item_models.add(item_model);
				StaticValues.order_item_models.add(item_model);
				dialog.dismiss();

			}
		});

		button_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		dialog.setCanceledOnTouchOutside(false);

		dialog.show();

	}

	public void populate_invoice_info() {
		TableRow.LayoutParams trparam = new TableRow.LayoutParams(0,
				TableRow.LayoutParams.MATCH_PARENT, 1f);
		TableRow.LayoutParams trparam2 = new TableRow.LayoutParams(0,
				TableRow.LayoutParams.MATCH_PARENT, 1.2f);
		TableRow.LayoutParams trparam3 = new TableRow.LayoutParams(0,
				TableRow.LayoutParams.MATCH_PARENT, 1.4f);
		TableRow.LayoutParams trparam4 = new TableRow.LayoutParams(0,
				TableRow.LayoutParams.MATCH_PARENT, 1.6f);
		TableRow.LayoutParams trparam5 = new TableRow.LayoutParams(0,
				TableRow.LayoutParams.MATCH_PARENT, 2.0f);

		Integer count = 0;

		if (cursor == null || cursor.getCount() == 0) {

			cursor = db.get_all_invoice_info(HomeFragment.dealer_id);
		}

		Log.e("QQQ", cursor.getCount()+" + ");
		
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					LinearLayout tr = new LinearLayout(getActivity());
					tr.setWeightSum(26);

					if (count % 2 != 0) {
						tr.setBackgroundColor(Color.parseColor("#1A5876"));
					}
					tr.setId(100 + count);
					tr.setLayoutParams(new LayoutParams(0,
							LayoutParams.WRAP_CONTENT));

					invoice_parts.add(cursor.getString(2)); // here

					TextView Part_Number = new TextView(getActivity());
					Part_Number.setText(cursor.getString(2)); // set the text
																// for
					Part_Number.setTextColor(Color.RED); // set the color
					Part_Number.setGravity(Gravity.LEFT);
					Part_Number.setPadding(5, 5, 5, 5);
					Part_Number.setLayoutParams(trparam);

					tr.addView(Part_Number); // add the column to the table

					TextView decscription = new TextView(getActivity());

					Cursor cursor1 = db.get_item_details(Part_Number.getText()
							.toString());
					if (cursor1 != null) {
						cursor1.moveToFirst();
						while (!cursor1.isAfterLast()) {
							decscription.setText(cursor1.getString(1)); // set
																		// the
							// text for
							decscription.setTextColor(Color.RED); // set the
																	// color
							decscription.setGravity(Gravity.LEFT);
							decscription.setPadding(5, 5, 5, 5);
							decscription.setLayoutParams(trparam3);
							cursor1.moveToNext();
						}
						cursor1.close();

					}

					tr.addView(decscription); // add the column to the table

					TextView Available_Stocks_at_the_Dealer = new TextView(
							getActivity());
					Available_Stocks_at_the_Dealer.setText(cursor.getString(3)); // set
					Available_Stocks_at_the_Dealer.setTextColor(Color.RED); // set
					Available_Stocks_at_the_Dealer.setPadding(5, 5, 5, 5);
					Available_Stocks_at_the_Dealer.setGravity(Gravity.LEFT);
					Available_Stocks_at_the_Dealer.setLayoutParams(trparam2);
					tr.addView(Available_Stocks_at_the_Dealer);

					TextView avg_monthly_sale = new TextView(getActivity());
					avg_monthly_sale.setText(cursor.getString(5)); // set the
																	// text
					avg_monthly_sale.setTextColor(Color.RED); // set the color
					avg_monthly_sale.setPadding(5, 5, 5, 5);
					avg_monthly_sale.setGravity(Gravity.LEFT);
					avg_monthly_sale.setLayoutParams(trparam);
					tr.addView(avg_monthly_sale);

					TextView Total_Sales_for_last_30days = new TextView(
							getActivity());
					Total_Sales_for_last_30days.setText(cursor.getString(5));
					Total_Sales_for_last_30days.setGravity(Gravity.LEFT);
					Total_Sales_for_last_30days.setTextColor(Color.RED);
					Total_Sales_for_last_30days.setPadding(5, 5, 5, 5);
					Total_Sales_for_last_30days.setLayoutParams(trparam3);

					tr.addView(Total_Sales_for_last_30days);

					TextView Stocklostsales = new TextView(getActivity());
					Stocklostsales.setText(cursor.getString(6)); // set the
					Stocklostsales.setGravity(Gravity.LEFT); // text for
					Stocklostsales.setTextColor(Color.RED); // set the color
					Stocklostsales.setPadding(5, 5, 5, 5);
					Stocklostsales.setLayoutParams(trparam);

					tr.addView(Stocklostsales);

					TextView Valuelostsales = new TextView(getActivity());
					Valuelostsales.setText(cursor.getString(7)); // set the
					Valuelostsales.setGravity(Gravity.LEFT); // text for
					Valuelostsales.setTextColor(Color.RED); // set the color
					Valuelostsales.setPadding(5, 5, 5, 5);
					Valuelostsales.setLayoutParams(trparam);

					tr.addView(Valuelostsales);

					TextView AverageDailyDemand = new TextView(getActivity());
					AverageDailyDemand.setText(cursor.getString(8)); // set the
					AverageDailyDemand.setGravity(Gravity.LEFT); // text for
					AverageDailyDemand.setTextColor(Color.RED); // set the color
					AverageDailyDemand.setPadding(5, 5, 5, 5);
					AverageDailyDemand.setLayoutParams(trparam);

					tr.addView(AverageDailyDemand);

					TextView Daysbetweenorders = new TextView(getActivity());
					Daysbetweenorders.setText(cursor.getString(9)); // set the
					Daysbetweenorders.setGravity(Gravity.LEFT); // text for
					Daysbetweenorders.setTextColor(Color.RED); // set the color
					Daysbetweenorders.setPadding(5, 5, 5, 5);
					Daysbetweenorders.setLayoutParams(trparam);

					tr.addView(Daysbetweenorders);

					TextView SuggestedQty = new TextView(getActivity());
					SuggestedQty.setText(cursor.getString(10)); // set the
					SuggestedQty.setGravity(Gravity.LEFT); // text for
					SuggestedQty.setTextColor(Color.RED); // set the color
					SuggestedQty.setPadding(5, 5, 5, 5);
					SuggestedQty.setLayoutParams(trparam3);
					tr.addView(SuggestedQty);

					TextView Available_Stocks_at_VSD = new TextView(
							getActivity());
					Available_Stocks_at_VSD.setText(cursor.getString(11)); // set
					Available_Stocks_at_VSD.setGravity(Gravity.LEFT); // text
																		// for
					Available_Stocks_at_VSD.setTextColor(Color.RED); // set the
					Available_Stocks_at_VSD.setPadding(5, 5, 5, 5);
					Available_Stocks_at_VSD.setLayoutParams(trparam2);

					tr.addView(Available_Stocks_at_VSD);

					TextView UnsuppliedOrderQtyfor90day = new TextView(
							getActivity());
					UnsuppliedOrderQtyfor90day.setText(cursor.getString(13)); // set
					UnsuppliedOrderQtyfor90day.setGravity(Gravity.LEFT); // text
					UnsuppliedOrderQtyfor90day.setTextColor(Color.RED); // set
																		// the
					UnsuppliedOrderQtyfor90day.setPadding(5, 5, 5, 5);
					UnsuppliedOrderQtyfor90day.setLayoutParams(trparam2);

					tr.addView(UnsuppliedOrderQtyfor90day);

					TextView movement_in_area_per_month = new TextView(
							getActivity());
					movement_in_area_per_month.setText(cursor.getString(14)); // set
					movement_in_area_per_month.setGravity(Gravity.LEFT); // text
					movement_in_area_per_month.setTextColor(Color.RED); // set
																		// the
					movement_in_area_per_month.setPadding(5, 5, 5, 5);
					movement_in_area_per_month.setLayoutParams(trparam3);

					tr.addView(movement_in_area_per_month);

					TextView Days_since_Last_Invoice_Date = new TextView(
							getActivity());
					Days_since_Last_Invoice_Date.setText(cursor.getString(14)); // set
					Days_since_Last_Invoice_Date.setGravity(Gravity.LEFT); // text
					Days_since_Last_Invoice_Date.setTextColor(Color.RED); // set
																			// the
					Days_since_Last_Invoice_Date.setPadding(5, 5, 5, 5);
					Days_since_Last_Invoice_Date.setLayoutParams(trparam3);
					tr.addView(Days_since_Last_Invoice_Date);
                                                                       
					TextView Days_since_Last_PO_Date = new TextView(
							getActivity());
					Days_since_Last_PO_Date.setText(cursor.getString(15));
					Days_since_Last_PO_Date.setGravity(Gravity.LEFT);
					Days_since_Last_PO_Date.setTextColor(Color.RED);
					Days_since_Last_PO_Date.setPadding(5, 5, 5, 5);
					Days_since_Last_PO_Date.setLayoutParams(trparam);
					tr.addView(Days_since_Last_PO_Date);

					TextView Avg_monthly_requirement = new TextView(
							getActivity());
					Avg_monthly_requirement.setText(cursor.getString(16));
					Avg_monthly_requirement.setGravity(Gravity.LEFT);
					Avg_monthly_requirement.setTextColor(Color.RED);
					Avg_monthly_requirement.setPadding(5, 5, 5, 5);
					Avg_monthly_requirement.setLayoutParams(trparam4);
					tr.addView(Avg_monthly_requirement);

					TextView Number_of_Items_invoice_for_past_01_month1 = new TextView(
							getActivity());
					Number_of_Items_invoice_for_past_01_month1.setText(cursor
							.getString(17));
					Number_of_Items_invoice_for_past_01_month1
							.setGravity(Gravity.LEFT); // text for
					Number_of_Items_invoice_for_past_01_month1
							.setTextColor(Color.RED); // set the color
					Number_of_Items_invoice_for_past_01_month1.setPadding(5, 5,
							5, 5);
					Number_of_Items_invoice_for_past_01_month1
							.setLayoutParams(trparam3);
					tr.addView(Number_of_Items_invoice_for_past_01_month1);

					if (ItemFragment.clicked_items.contains(Part_Number
							.getText().toString())) {
						Button AddItem = new Button(getActivity());
						AddItem.setText("Already Added\n("
								+ (cursor.getString(10)) + ")");
						AddItem.setTextColor(Color.CYAN);
						AddItem.setTag(Part_Number.getText().toString());
						AddItem.setPadding(5, 5, 5, 5);

						AddItem.setOnClickListener(move_to_purchase_order);
						AddItem.setGravity(Gravity.LEFT);
						AddItem.setLayoutParams(trparam3);
						tr.addView(AddItem);

					} else {

						Button AddItem = new Button(getActivity());
						AddItem.setText("AddItem\n(" + (cursor.getString(10))
								+ ")");
						AddItem.setTextColor(Color.RED);
						AddItem.setTag(Part_Number.getText().toString());
						AddItem.setOnClickListener(suggestion_order_on_click_listener);

						AddItem.setPadding(5, 5, 5, 5);
						AddItem.setGravity(Gravity.LEFT);
						AddItem.setLayoutParams(trparam3);
						tr.addView(AddItem);
					}

					TextView desc2 = new TextView(getActivity());
					desc2.setText(decscription.getText()); // set the text for
					desc2.setTextColor(Color.RED); // set the color
					desc2.setGravity(Gravity.LEFT);
					desc2.setPadding(5, 5, 5, 5);
					desc2.setLayoutParams(trparam5);
					tr.addView(desc2);

					TextView part2 = new TextView(getActivity());
					part2.setText(Part_Number.getText()); // set the text for
					part2.setTextColor(Color.RED); // set the color
					part2.setGravity(Gravity.LEFT);
					part2.setPadding(5, 5, 5, 5);
					part2.setLayoutParams(trparam);
					tr.addView(part2);

					tl.addView(tr, new TableLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));

					count++;
					cursor.moveToNext();
				}

			} else {

				if (rootView != null) {

					View view = rootView.findViewById(R.id.suggestionLayout);
					view.setVisibility(View.GONE);
					sitems.setVisibility(View.GONE);
				}
			}

			db.close();
		}
	}

	private class Download_data2 extends AsyncTask<String, Integer, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {

			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(params[0], params[2]));

			if (StaticValues.jasonPast == null) {
				StaticValues.jasonPast = new JSONObject();
				StaticValues.jasonPast = j
						.JsonObjectSendToServerPostWithNameValuePare(params[1],
								nameValuePairs);
			}

			return StaticValues.jasonPast;

		}

	}

	public static class ItemQuantityName {
		String quantity;
		String name;

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

}