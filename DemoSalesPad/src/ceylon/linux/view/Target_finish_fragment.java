package ceylon.linux.view;

import java.util.ArrayList;

import com.example.dimosales.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.db.DbHandler;
import ceylon.linux.db.DbHelper;
import ceylon.linux.db.Dbworker;
import ceylon.linux.model.TargetItemModel;
import ceylon.linux.utility.NumbeFormater;

public class Target_finish_fragment extends Fragment {

	private LinearLayout target_item_rows;

	DbHelper dbHelper;
	Button btn_finish_target, btn_mv_itm;

	TextView text_min_total, text_add_total, text_both_total;

	static double min_total = 0;
	static double add_total = 0;
	static int ii = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.target_finish_layout,
				container, false);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		target_item_rows = (LinearLayout) rootView
				.findViewById(R.id.target_item_rows);
		btn_finish_target = (Button) rootView
				.findViewById(R.id.btn_finish_target);
		btn_mv_itm = (Button) rootView.findViewById(R.id.btn_mv_itm);

		text_min_total = (TextView) rootView.findViewById(R.id.text_min_total);
		text_add_total = (TextView) rootView.findViewById(R.id.text_add_total);
		text_both_total = (TextView) rootView
				.findViewById(R.id.text_both_total);

		load_target_item();

		btn_finish_target.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				insert_target_data();
				insert_target_item_data();

				Fragment home = new HomeFragment();
				jump_to_another("Home", home);
			}
		});

		btn_mv_itm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment home = new Fragment_line_wise_item_target();
				jump_to_another("Line Item Wise Target ("
						+ LineItemWiseTarget.dealer_name + ")", home);
			}
		});

		return rootView;
	}

	void load_target_item() {
		ArrayList<TargetItemModel> a = Fragment_line_wise_item_target.target_items;

		ii = 0;
		for (TargetItemModel targetItem : a) {
			LinearLayout row = new LinearLayout(getActivity());
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			row.setLayoutParams(param);
			row.setWeightSum(12);

			LinearLayout.LayoutParams paramA = new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1);
			LinearLayout.LayoutParams paramB = new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 2);
			LinearLayout.LayoutParams paramC = new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 3);

			TextView a1 = new TextView(getActivity());
			a1.setText(targetItem.getItem_part_no());
			a1.setLayoutParams(paramB);
			a1.setGravity(Gravity.CENTER);

			TextView a2 = new TextView(getActivity());
			a2.setText(targetItem.getItem_name());
			a2.setLayoutParams(paramC);
			a2.setGravity(Gravity.CENTER);

			TextView a3 = new TextView(getActivity());
			Dbworker dbworker = new Dbworker(getActivity());
			Cursor cursor = dbworker.get_item_by_item_id(targetItem
					.getItem_id());

			String item_price = "0.00";
			if (cursor.moveToFirst()) {
				a3.setText(cursor.getString(3));
				item_price = cursor.getString(3);
			}
			cal_min_add_total(item_price, targetItem.getMinimum_qty(),
					targetItem.getAdditional_qty());

			a3.setLayoutParams(paramB);
			a3.setGravity(Gravity.CENTER);

			TextView a4 = new TextView(getActivity());
			a4.setText(targetItem.getMinimum_qty());
			a4.setLayoutParams(paramB);
			a4.setGravity(Gravity.CENTER);

			TextView a5 = new TextView(getActivity());
			a5.setText(targetItem.getAdditional_qty());
			a5.setLayoutParams(paramB);
			a5.setGravity(Gravity.CENTER);

			Button btn_remove = new Button(getActivity());

			btn_remove.setText("-");
			btn_remove.setLayoutParams(paramA);
			btn_remove.setGravity(Gravity.CENTER);
			btn_remove.setId(ii * 17);
			btn_remove.setOnClickListener(new OnClickListener() {

				int j = ii;

				@Override
				public void onClick(View arg0) {
					Toast.makeText(getActivity(), "fdFfZFf", Toast.LENGTH_SHORT)
							.show();

					Fragment_line_wise_item_target.target_items.remove(j);
					Log.i("index is", "" + j);
					target_item_rows.removeAllViews();
					add_total = 0;
					min_total = 0;
					load_target_item();
					NumbeFormater nf = new NumbeFormater();
					text_add_total.setText(nf
							.format_double_val_to_decimal_Strng(add_total));
					text_min_total.setText(nf
							.format_double_val_to_decimal_Strng(min_total));
					text_both_total.setText(nf
							.format_double_val_to_decimal_Strng(min_total
									+ add_total));

				}
			});

			row.addView(a1);
			row.addView(a2);
			row.addView(a3);
			row.addView(a4);
			row.addView(a5);
			row.addView(btn_remove);

			target_item_rows.addView(row);
			ii++;

		}

		NumbeFormater nf = new NumbeFormater();
		text_add_total
				.setText(nf.format_double_val_to_decimal_Strng(add_total));
		text_min_total
				.setText(nf.format_double_val_to_decimal_Strng(min_total));
		text_both_total.setText(nf.format_double_val_to_decimal_Strng(min_total
				+ add_total));
	}

	public void insert_target_data() {

		dbHelper = new DbHelper(getActivity());

		SQLiteDatabase database = dbHelper.getWritableDatabase();

		String sql = "insert into target (dealer_id,year,month,added_date,added_time,current_discount_percentage) values(?,?,?,?,?,?)";
		SQLiteStatement statement = database.compileStatement(sql);

		java.util.Date date = new java.util.Date();
		java.text.SimpleDateFormat dateFormatter1 = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.text.SimpleDateFormat dateFormatter2 = new java.text.SimpleDateFormat(
				"HH:mm:ss");

		Object[] parameters = { LineItemWiseTarget.dealer_id,
				LineItemWiseTarget.selected_year_d,
				LineItemWiseTarget.selected_month_d,
				dateFormatter1.format(date), dateFormatter2.format(date), "0"

		};
		DbHandler.performExecuteInsert(statement, parameters);
		database.close();
	}

	public void insert_target_item_data() {
		SQLiteDatabase database = dbHelper.getWritableDatabase();

		String sql = "insert into target_item (target_id,item_id,minimum_qty,additional_qty,current_selling_price) values(?,?,?,?,?)";
		SQLiteStatement statement = database.compileStatement(sql);

		Dbworker dbworker = new Dbworker(getActivity());

		ArrayList<TargetItemModel> a = Fragment_line_wise_item_target.target_items;

		for (TargetItemModel targetItem : a) {

			Object[] parameters = { dbworker.get_Max_id_target_table(),
					targetItem.getItem_id(), targetItem.getMinimum_qty(),
					targetItem.getAdditional_qty(), "0"

			};
			DbHandler.performExecuteInsert(statement, parameters);
		}

		Fragment_line_wise_item_target.target_items.clear();
		database.close();
		dbworker.close();
	}

	public void jump_to_another(String title, Fragment f) {
		// Fragment fragment = new Target_finish_fragment();
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle("Line Item Wise Target ");
		fragmentManager.beginTransaction().replace(R.id.frame_container, f)
				.addToBackStack(null).commit();
	}

	public void cal_min_add_total(String item_price, String min_qty,
			String add_qty) {
		double a_total = 0;
		double m_total = 0;

		if (!item_price.equals("")) {
			a_total = Double.parseDouble(item_price)
					* Double.parseDouble(add_qty);
			m_total = Double.parseDouble(item_price)
					* Double.parseDouble(min_qty);
		}

		add_total = add_total + a_total;
		min_total = min_total + m_total;

	}
}
