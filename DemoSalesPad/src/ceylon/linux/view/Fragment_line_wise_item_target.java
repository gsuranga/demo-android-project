package ceylon.linux.view;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dimosales.R;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.controller.StaticValues;
import ceylon.linux.model.TargetItemModel;
import ceylon.linux.url.URLS;
import ceylon.linux.utility.Utility;

public class Fragment_line_wise_item_target extends Fragment {

	LinearLayout target_history_Layout, fastMovingItemsLayout,
					targetHistoryList, part_number_layout;
	Utility utility;
	Button btn_mv_to_target, btn_item_fragment;

	static ArrayList<TargetItemModel> target_items = new ArrayList<TargetItemModel>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_line_item_wise_target_operation, container,false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		target_history_Layout = (LinearLayout) rootView.findViewById(R.id.target_history_Layout);
		fastMovingItemsLayout = (LinearLayout) rootView.findViewById(R.id.fastMovingItemsLayout);
		utility = new Utility(getActivity());
		targetHistoryList = (LinearLayout) rootView.findViewById(R.id.targetHistoryList);
		part_number_layout = (LinearLayout) rootView.findViewById(R.id.part_number_layout);

		final ToggleButton btn_target_histry = (ToggleButton) rootView.findViewById(R.id.btn_target_histry);

		btn_mv_to_target = (Button) rootView.findViewById(R.id.btn_mv_to_target);
		btn_mv_to_target.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jump_to_another();
			}
		});

		btn_item_fragment = (Button) rootView.findViewById(R.id.btn_item_fragment);
		btn_item_fragment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				jump_to_item_fragment();
			}
		});

		btn_target_histry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (btn_target_histry.isChecked()) {
					target_history_Layout.setVisibility(View.GONE);
				} else {
					target_history_Layout.setVisibility(View.VISIBLE);
				}
			}
		});
		
		final ToggleButton btn_fastMoving_itm = (ToggleButton) rootView.findViewById(R.id.btn_fastMoving_itm);
		
		btn_fastMoving_itm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btn_fastMoving_itm.isChecked()) {
					fastMovingItemsLayout.setVisibility(View.GONE);
				} else {
					fastMovingItemsLayout.setVisibility(View.VISIBLE);
				}
			}
		});

	
	//	getPastMovingItems();
		getTargetHistory();
		return rootView;
	}

	public void getTargetHistory() {

		AsyncTask<String, String, String> target_async = new DownLoadTarget(
				getActivity()) {
		};

		String[] a = { LineItemWiseTarget.dealer_id,
				LineItemWiseTarget.selected_month };

		if (utility.isNetworkAvailable()) {
			target_async.execute(a);
		} else {
			Toast.makeText(getActivity(),
					"There is no active internet connection",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void getPastMovingItems() {
		AsyncTask<String, String, String> async = new DownloadPastMovingItems(
				getActivity()) {
		};

		String[] a = { LineItemWiseTarget.dealer_id };

		if (utility.isNetworkAvailable()) {
			async.execute(a);
		} else {
			Toast.makeText(getActivity(),
					"There is no active internet connection",
					Toast.LENGTH_SHORT).show();
		}

	}

	private class DownLoadTarget extends AsyncTask<String, String, String> {

		ProgressDialog progressBar;
		Context context;
		private ArrayList<NameValuePair> nameValuePairs;
		Jsonhelper jhelper;

		// JSONObject jsonobject;

		public DownLoadTarget(Context context) {
			this.context = context;
			nameValuePairs = new ArrayList<NameValuePair>(2);
			jhelper = new Jsonhelper();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressBar = new ProgressDialog(context);
			progressBar.setMessage("Data downloading ...");
			progressBar.setCanceledOnTouchOutside(false);
			progressBar.setIndeterminate(false);

			progressBar.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (StaticValues.line_wise_item_target_data_load_status) {
				nameValuePairs.add(new BasicNameValuePair("dealer_id",
						params[0]));
				nameValuePairs.add(new BasicNameValuePair("target_month",
						params[1]));
				StaticValues.jsonobject_tatrget_history = jhelper
						.JsonObjectSendToServerPostWithNameValuePare(
								URLS.get_target_history, nameValuePairs);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressBar.dismiss();
			if (StaticValues.jsonobject_tatrget_history != null) {

				TextView a = new TextView(getActivity());
				a.setText("asdfghjkl");

				/*
				 * targetHistoryRow.addView(a);
				 * targetHistoryList.addView(targetHistoryRow);
				 */

				try {
					JSONArray jarr = StaticValues.jsonobject_tatrget_history
							.getJSONArray("history_data");

					for (int i = 0; i < jarr.length(); i++) {

						LinearLayout targetHistoryRow = new LinearLayout(
								getActivity());

						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);

						targetHistoryRow.setWeightSum(26);

						LinearLayout.LayoutParams paramA = new LinearLayout.LayoutParams(
								0, LayoutParams.WRAP_CONTENT, 1);
						LinearLayout.LayoutParams paramB = new LinearLayout.LayoutParams(
								0, LayoutParams.WRAP_CONTENT, 2);
						LinearLayout.LayoutParams paramC = new LinearLayout.LayoutParams(
								0, LayoutParams.WRAP_CONTENT, 3);

						targetHistoryRow.setLayoutParams(param);
						JSONObject jo = jarr.getJSONObject(i);

						final int item_id = jo.getInt("item_id");
						final String item_name = jo.getString("description");
						final String item_part_no = jo
								.getString("item_part_no");

						TextView a1 = new TextView(getActivity());
						a1.setText(jo.getString("item_part_no"));
						a1.setLayoutParams(paramB);
						a1.setGravity(Gravity.CENTER);

						TextView a2 = new TextView(getActivity());
						a2.setText(jo.getString("description"));
						a2.setLayoutParams(paramC);
						a2.setGravity(Gravity.CENTER);

						TextView a3 = new TextView(getActivity());
						a3.setText(jo.getString("bbf"));
						a3.setLayoutParams(paramA);
						a3.setGravity(Gravity.CENTER);

						TextView a4 = new TextView(getActivity());
						a4.setText(jo.getString("re_order_qty"));
						a4.setLayoutParams(paramB);
						a4.setGravity(Gravity.CENTER);

						TextView a5 = new TextView(getActivity());
						a5.setText(jo.getString("current_stock"));
						a5.setLayoutParams(paramB);
						a5.setGravity(Gravity.CENTER);

						TextView a6 = new TextView(getActivity());
						a6.setText(jo.getString("movement"));
						a6.setLayoutParams(paramB);
						a6.setGravity(Gravity.CENTER);

						TextView a7 = new TextView(getActivity());
						a7.setText(jo.getString("month1_actual"));
						a7.setLayoutParams(paramA);
						a7.setGravity(Gravity.CENTER);

						TextView a8 = new TextView(getActivity());
						a8.setText(jo.getString("month1_target"));
						a8.setLayoutParams(paramA);
						a8.setGravity(Gravity.CENTER);

						TextView a9 = new TextView(getActivity());
						a9.setText(jo.getString("month2_actual"));
						a9.setLayoutParams(paramA);
						a9.setGravity(Gravity.CENTER);

						TextView a10 = new TextView(getActivity());
						a10.setText(jo.getString("month2_target"));
						a10.setLayoutParams(paramA);
						a10.setGravity(Gravity.CENTER);

						TextView a11 = new TextView(getActivity());
						a11.setText(jo.getString("month3_actual"));
						a11.setLayoutParams(paramA);
						a11.setGravity(Gravity.CENTER);

						TextView a12 = new TextView(getActivity());
						a12.setText(jo.getString("month3_target"));
						a12.setLayoutParams(paramA);
						a12.setGravity(Gravity.CENTER);

						Button addvButton = new Button(getActivity());
						addvButton.setText("Add Item");
						addvButton.setLayoutParams(paramA);
						addvButton.setId(i * 11);

						ArrayList<TargetItemModel> axxx = Fragment_line_wise_item_target.target_items;

						for (TargetItemModel targetItem : axxx) {
							if (targetItem.getItem_id() == item_id) {
								addvButton.setText("Already Added");
								addvButton.setEnabled(false);
							}
							Log.i("item id sdfaff", targetItem.getItem_id()
									+ "");
						}

						addvButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(final View arg0) {
								// TODO Auto-generated method stub
								final Dialog dialog = new Dialog(getActivity());
								dialog.setTitle("Add Item");
								dialog.setContentView(R.layout.dialog_add_target_item);
								final EditText editMinQuanty = (EditText) dialog.findViewById(R.id.editMinQuanty);
								final EditText editAddQuanty = (EditText) dialog.findViewById(R.id.editAddQuanty);
								Button button_add = (Button) dialog.findViewById(R.id.button_add);
								Button button_cancel = (Button) dialog.findViewById(R.id.button_cancel);

								button_cancel
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												dialog.dismiss();

											}
										});

								button_add
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {

												if(editMinQuanty.getText().toString().equals("")||editAddQuanty.getText().toString().equals(""))
												{
													Toast.makeText(getActivity(), "empty values are not allowed", Toast.LENGTH_SHORT).show();
												}
												else
												{
												TargetItemModel tim = new TargetItemModel();
												tim.setItem_id(item_id);
												tim.setItem_name(item_name);
												tim.setItem_part_no(item_part_no);
												tim.setMinimum_qty(editMinQuanty.getText().toString());
												tim.setAdditional_qty(editAddQuanty.getText().toString());
												target_items.add(tim);
												// jump_to_another();
												dialog.dismiss();
												Button addvButton=(Button)arg0;
												
												addvButton.setText("Already Added");
												addvButton.setEnabled(false);
												}
											}
										});

								dialog.setCanceledOnTouchOutside(false);
								dialog.show();

							}
						});

						TextView a13 = new TextView(getActivity());
						a13.setText(jo.getString("item_part_no"));
						a13.setLayoutParams(paramB);
						a13.setGravity(Gravity.CENTER);

						TextView a14 = new TextView(getActivity());
						a14.setText(jo.getString("description"));
						a14.setLayoutParams(paramC);
						a14.setGravity(Gravity.CENTER);

						targetHistoryRow.addView(a1);
						targetHistoryRow.addView(a2);
						targetHistoryRow.addView(a3);
						targetHistoryRow.addView(a4);
						targetHistoryRow.addView(a5);
						targetHistoryRow.addView(a6);
						targetHistoryRow.addView(a7);
						targetHistoryRow.addView(a8);
						targetHistoryRow.addView(a9);
						targetHistoryRow.addView(a10);
						targetHistoryRow.addView(a11);
						targetHistoryRow.addView(a12);
						targetHistoryRow.addView(addvButton);
						targetHistoryRow.addView(a13);
						targetHistoryRow.addView(a14);

						targetHistoryList.addView(targetHistoryRow);
					}
					/*
					 * JSONObject jaobj = jarr.getJSONObject(0);
					 * 
					 * a.setText(jaobj.getString("bank_name"));
					 */
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class DownloadPastMovingItems extends AsyncTask<String, String, String> {
		ProgressDialog progressBar;
		Context context;
		private ArrayList<NameValuePair> nameValuePairs;
		Jsonhelper jhelper;

		// JSONObject jsonobject;

		public DownloadPastMovingItems(Context context) {
			this.context = context;
			nameValuePairs = new ArrayList<NameValuePair>(2);
			jhelper = new Jsonhelper();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressBar = new ProgressDialog(context);
			progressBar.setMessage("Data downloading ...");
			progressBar.setCanceledOnTouchOutside(false);
			progressBar.setIndeterminate(false);
			progressBar.setCancelable(true);
			progressBar.show();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressBar.dismiss();

			if (StaticValues.jsonobject_fast_moving != null) {
				JSONArray jarr = new JSONArray();
				try {
					jarr = StaticValues.jsonobject_fast_moving
							.getJSONArray("fast_mooving_items");

					for (int i = 0; i < jarr.length(); i++) {
						JSONObject jobj = jarr.getJSONObject(i);
						Log.i("json obj", jobj.toString());

						final int item_id = jobj.getInt("item_id");
						final String item_name = jobj.getString("description");
						final String item_part_no = jobj
								.getString("item_part_no");

						LinearLayout layout_past_move_row = new LinearLayout(
								getActivity());
						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);

						LinearLayout.LayoutParams paramA = new LinearLayout.LayoutParams(
								0, LayoutParams.WRAP_CONTENT, 100);

						layout_past_move_row.setLayoutParams(param);

						layout_past_move_row.setWeightSum(400);

						TextView a1 = new TextView(getActivity());
						a1.setText(jobj.getString("item_part_no"));
						a1.setLayoutParams(paramA);
						a1.setGravity(Gravity.CENTER);

						TextView a2 = new TextView(getActivity());
						a2.setText(jobj.getString("description"));
						a2.setLayoutParams(paramA);
						a2.setGravity(Gravity.CENTER);

						TextView a3 = new TextView(getActivity());
						a3.setText(jobj.getString("quantity"));
						a3.setLayoutParams(paramA);
						a3.setGravity(Gravity.CENTER);

						Button addvButton = new Button(getActivity());
						addvButton.setText("Add Item");
						addvButton.setLayoutParams(paramA);
						addvButton.setId(i * 23);

						ArrayList<TargetItemModel> axxx = Fragment_line_wise_item_target.target_items;

						for (TargetItemModel targetItem : axxx) {
							if (targetItem.getItem_id() == item_id) {
								addvButton.setText("Already Added");
								addvButton.setEnabled(false);
								Log.e("it is working", "dsvsvz");
							}
							Log.i("item id sdfaff", targetItem.getItem_id()
									+ "");
						}

						addvButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								final Dialog dialog = new Dialog(getActivity());
								dialog.setTitle("Add Item");
								dialog.setContentView(R.layout.dialog_add_target_item);
								final EditText editMinQuanty = (EditText) dialog.findViewById(R.id.editMinQuanty);
								final EditText editAddQuanty = (EditText) dialog.findViewById(R.id.editAddQuanty);
								Button button_add = (Button) dialog.findViewById(R.id.button_add);
								Button button_cancel = (Button) dialog.findViewById(R.id.button_cancel);

								button_cancel.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();
											}
										});

								button_add.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												TargetItemModel tim = new TargetItemModel();
												tim.setItem_id(item_id);
												tim.setItem_name(item_name);
												tim.setItem_part_no(item_part_no);
												tim.setMinimum_qty(editMinQuanty.getText().toString());
												tim.setAdditional_qty(editAddQuanty.getText().toString());
												target_items.add(tim);
												jump_to_another();
												dialog.dismiss();
											}
										});
								dialog.setCanceledOnTouchOutside(false);
								dialog.show();
							}
						});

						layout_past_move_row.addView(a1);
						layout_past_move_row.addView(a2);
						layout_past_move_row.addView(a3);
						layout_past_move_row.addView(addvButton);
						part_number_layout.addView(layout_past_move_row);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (StaticValues.line_wise_item_target_data_load_status) {
				
				nameValuePairs.add(new BasicNameValuePair("dealer_id",params[0]));
				StaticValues.jsonobject_fast_moving = jhelper
						.JsonObjectSendToServerPostWithNameValuePare(URLS.fast_moving, nameValuePairs);
			}
			return null;
		}

	}

	public void jump_to_another() {
		Fragment fragment = new Target_finish_fragment();
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle("Line Item Wise Target ");
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
		StaticValues.line_wise_item_target_data_load_status = false;
	}

	public void jump_to_item_fragment() {
		Fragment fragment = new ItemFragment_target();
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle("Items");
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
		StaticValues.line_wise_item_target_data_load_status = false;
	}
}
