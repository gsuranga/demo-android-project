package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import ceylon.linux.controller.StaticValues;
import ceylon.linux.db.Dbworker;

public class LineItemWiseTarget extends Fragment {
	static final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	public static String discount_percentage;
	public static String dealer_id;
	public static String dealer_account_no;
	public static String dealer_name;
	public static String selected_year_d;
	public static String selected_month_d;
	public static String selected_month;
	public static String credit_limit;
	public static String outstanding_amount;
	public static String overdue_amount;
	ListView show_dealers;
	SimpleAdapter adapter;
	EditText select_dealer;
	Dbworker db;

	TextWatcher t = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			adapter.getFilter().filter(s);

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	public LineItemWiseTarget() {
		Sugesstion_order_fragment.update_status = "0";
		// TODO Change this
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_line_item_wise_target, container, false);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		StaticValues.order_item_models.clear();
		// TODO change this
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		ItemFragment.clicked_items.clear();
		db = new Dbworker(getActivity());

		select_dealer = (EditText) rootView.findViewById(R.id.select);
		populate_list();
		show_dealers = (ListView) rootView.findViewById(R.id.list);

		adapter = new SimpleAdapter(getActivity(), list,
				R.layout.dealers_list_view_item, new String[] { "dealer_name",
						"delar_account_no", "credit_limit",
						"outstanding_amount", "overdue_amount" }, new int[] {
						R.id.dealer_name, R.id.dealer_account,
						R.id.credit_limit, R.id.outstanding_amount,
						R.id.overdue_amount });

		show_dealers.setAdapter(adapter);

		select_dealer.addTextChangedListener(t);

		show_dealers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id)

			{
				HashMap<String, String> ss = (HashMap<String, String>) adapter
						.getItem(position);
				discount_percentage = ss.get("discount_percentage");
				dealer_id = ss.get("dealer_id");
				dealer_name = ss.get("dealer_name");
				dealer_account_no = ss.get("delar_account_no");

				overdue_amount = ss.get("overdue_amount");
				outstanding_amount = ss.get("outstanding_amount");
				credit_limit = ss.get("credit_limit");

				move_to_another_fragment();
			}

		});

		return rootView;

	}

	public void move_to_another_fragment() {

		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.alert_month_picker);
		dialog.setTitle("Enter Month");
		dialog.setCanceledOnTouchOutside(false);
		Button btn_add = (Button) dialog.findViewById(R.id.btn_add);
		Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		
		final DatePicker date_picker_select_month = (DatePicker) dialog
				.findViewById(R.id.date_picker_select_month);
		
		int day = getActivity().getResources().getIdentifier("android:id/day", null, null);
		if(day != 0){
		    View daypicker = date_picker_select_month.findViewById(day);
		    if(daypicker != null){
		    	daypicker.setVisibility(View.GONE);
		    }
		}
		

		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Fragment_line_wise_item_target.target_items!=null)
				{
					Fragment_line_wise_item_target.target_items.clear();
				}
				Target_finish_fragment.add_total=0;
				Target_finish_fragment.min_total=0;
				
			//	int day = date_picker_select_month.getDayOfMonth();
				int month = date_picker_select_month.getMonth() + 1;
				int year = date_picker_select_month.getYear();
				
				selected_month =year+"-"+month;
				selected_month_d=month+"";
				selected_year_d=year+"";
				
				Fragment fragment = new Fragment_line_wise_item_target();
				FragmentManager fragmentManager = getFragmentManager();
				getActivity().getActionBar().setTitle(
						"Line Item Wise Target (" + dealer_name + ")");
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment)
						.addToBackStack(null).commit();
				list.clear();
				// selected_month=
				StaticValues.line_wise_item_target_data_load_status = true;
				
				dialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});
		dialog.show();

	}

	public void populate_list() {
		if (list.isEmpty()) {
			Cursor cursor = db.get_all_dealers();
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					HashMap<String, String> temp = new HashMap<String, String>();
					temp.put("dealer_id", cursor.getString(1));
					temp.put("dealer_name", cursor.getString(3));
					temp.put("discount_percentage", cursor.getString(6));
					temp.put("delar_account_no", cursor.getString(2));
					temp.put("credit_limit", cursor.getString(12));
					temp.put("outstanding_amount", cursor.getString(13));
					temp.put("overdue_amount", cursor.getString(14));

					list.add(temp);

					cursor.moveToNext();
				}
				cursor.close();
			}

			db.close();
		}
	}

}
