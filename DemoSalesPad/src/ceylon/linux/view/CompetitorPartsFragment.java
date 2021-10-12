package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.dimosales.R;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.db.DbHandler;
import ceylon.linux.db.DbHelper;
import ceylon.linux.db.Dbworker;
import ceylon.linux.model.CompetitorPartModel;

public class CompetitorPartsFragment extends Fragment {

	DbHelper dbHelper;
	View rootView;
	Dbworker dbworker;
	Button btn_item, btn_submit;
	Spinner spin_category;
	AutoCompleteTextView text_outlet;
	public static int outlet_catogory;
	public static int outlet_id;
	public static String outlet_name = "";
	public static ArrayList<CompetitorPartModel> coArrayList = new ArrayList<CompetitorPartModel>();
	LinearLayout item_layout;
	static int i = 0;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.competitor_parts_layout,
				container, false);

		dbworker = new Dbworker(getActivity());
		btn_item = (Button) rootView.findViewById(R.id.btn_item);
		btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
		spin_category = (Spinner) rootView.findViewById(R.id.spin_category);
		text_outlet = (AutoCompleteTextView) rootView
				.findViewById(R.id.text_outlet);

		text_outlet.setText(outlet_name);
		String[] categories = { "Dealer", "Garage", "Fleet Owner", "New Shop",
				"Vehicle Owner" };

		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, categories);

		spin_category.setAdapter(spinnerArrayAdapter);
		spin_category
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						load_outlets(arg2);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		
		btn_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				move_to_another_fragment(new ItemFragmentCompetitorParts(),
						"  Item  ");
			}
		});

		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (text_outlet.getText().toString().equals("")) {
					Toast.makeText(getActivity(), "Select Outlet",
							Toast.LENGTH_SHORT).show();

				} else {
					if (coArrayList.isEmpty()) {
						Toast.makeText(getActivity(), "Enter competitor parts",
								Toast.LENGTH_SHORT).show();
					} else {

						insert_competitor_data();
						insert_competitor_item_data();
						move_to_another_fragment(new HomeFragment(), " Home ");
						outlet_id = 0;
						outlet_name = "";
					}
				}
			}
		});
		load_cmpetitor_parts_list();
		return rootView;

	}

	public void load_outlets(int position) {
		switch (position) {
		case 0:
			load_dealers();

			break;
		case 1:
			Log.i("msg", "normal");
			outlet_catogory = 1;
			break;
		case 2:
			Log.i("msg", "good");
			break;
		case 3:
			Log.i("msg", "better");
			break;
		case 4:
			Log.i("msg", "best");
			break;
		default:
			load_dealers();
			outlet_catogory = 1;
			break;
		}
	}

	@SuppressWarnings("serial")
	public void load_dealers() {
		Cursor cur = dbworker.get_all_dealers();

		ArrayList<HashMap<String, String>> dealerslist = new ArrayList<HashMap<String, String>>();
		if (cur.moveToFirst()) {
			do {
				HashMap<String, String> dealer = new HashMap<String, String>() {
					@Override
					public String toString() {

						return super.get("outlet_name");
					}
				};
				dealer.put("outlet_id", cur.getString(1));
				dealer.put("outlet_name", cur.getString(3));
				dealerslist.add(dealer);

			} while (cur.moveToNext());
		}
		cur.close();

		SimpleAdapter autocomplete = new SimpleAdapter(getActivity(),
				dealerslist, R.layout.list_view_1uto,
				new String[] { "outlet_name" }, new int[] { R.id.Name });
		text_outlet.setThreshold(1);
		text_outlet.setAdapter(autocomplete);
		text_outlet
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						@SuppressWarnings("unchecked")
						HashMap<String, String> a = (HashMap<String, String>) arg0
								.getAdapter().getItem(arg2);
						Log.i("dealer", "" + a.get("outlet_name"));
						outlet_id = Integer.parseInt(a.get("outlet_id"));
						outlet_name = a.get("outlet_name");

					}
				});
	}

	@Override
	public void onDestroy() {

		dbworker.close();
		super.onDestroy();
	}

	public void move_to_another_fragment(Fragment f, String name) {
		Fragment fragment = f;
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(name);
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack("item")
				.commit();
	}

	public void load_cmpetitor_parts_list() {

		item_layout = (LinearLayout) rootView.findViewById(R.id.item_layout);

		i = 0;

		for (CompetitorPartModel partModel : coArrayList) {

			LinearLayout item_row = new LinearLayout(getActivity());
			item_row.setWeightSum(8);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			item_row.setLayoutParams(params);
			item_row.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout.LayoutParams paramsA = new LinearLayout.LayoutParams(
					0, LayoutParams.MATCH_PARENT, 1);
			LinearLayout.LayoutParams paramsB = new LinearLayout.LayoutParams(
					0, LayoutParams.MATCH_PARENT, 2);
			LinearLayout.LayoutParams paramsC = new LinearLayout.LayoutParams(
					0, LayoutParams.MATCH_PARENT, 3);

			TextView a = new TextView(getActivity());
			a.setLayoutParams(paramsB);
			a.setText(partModel.getTgp_number());
			a.setGravity(Gravity.CENTER);

			TextView b = new TextView(getActivity());
			b.setLayoutParams(paramsC);
			b.setText(partModel.getDescription());
			b.setGravity(Gravity.CENTER);

			TextView c = new TextView(getActivity());
			c.setLayoutParams(paramsB);
			c.setText(partModel.getBrand());
			c.setGravity(Gravity.CENTER);

			Button btn = new Button(getActivity());
			btn.setLayoutParams(paramsA);
			btn.setId((i + 1) * 17);
			btn.setText("-");

			item_row.addView(a);
			item_row.addView(b);
			item_row.addView(c);
			item_row.addView(btn);

			btn.setOnClickListener(new OnClickListener() {
				int j = i;

				@Override
				public void onClick(View arg0) {

					Log.i("index is", "" + j);
					item_layout.removeAllViews();
					coArrayList.remove(j);

					load_cmpetitor_parts_list();
				}
			});

			item_layout.addView(item_row);
			i++;
		}

	}

	public void insert_competitor_data() {
		dbHelper = new DbHelper(getActivity());

		SQLiteDatabase database = dbHelper.getWritableDatabase();

		String sql = "insert into competitor_part (outlet_cat_id,outlet_id,added_time,added_date) values(?,?,?,?)";
		SQLiteStatement statement = database.compileStatement(sql);

		java.util.Date date = new java.util.Date();
		java.text.SimpleDateFormat dateFormatter1 = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.text.SimpleDateFormat dateFormatter2 = new java.text.SimpleDateFormat(
				"HH:mm:ss");
		Log.e("date",dateFormatter1.format(date)+"");
		Log.e("time",dateFormatter2.format(date)+"");

		Object[] parameters = { outlet_catogory, outlet_id,
				dateFormatter2.format(date)+"", dateFormatter1.format(date)+"" };
		DbHandler.performExecuteInsert(statement, parameters);
		database.close();
	}

	public void insert_competitor_item_data() {
		dbHelper = new DbHelper(getActivity());

		SQLiteDatabase database = dbHelper.getWritableDatabase();

		String sql = "insert into competitor_part_item (cp_id,item_id,part_number,brand,importer,cost_price_to_the_dealer,selling_price_to_the_customer,average_monthly_movement,overall_movement_at_the_dealer,upload_image_path) values(?,?,?,?,?,?,?,?,?,?)";
		SQLiteStatement statement = database.compileStatement(sql);

		for (CompetitorPartModel a : coArrayList) {
			Object[] parameters = { dbworker.get_Max_id_competitor_table(),
					a.getPart_id(), a.getPart_number(), a.getBrand(),
					a.getImporter(), a.getCost_price_to_the_dealer(),
					a.getSelling_price_to_the_customer(),
					a.getAverage_monthly_movement(),
					a.getOverall_movement_at_the_dealer(),
					a.getUpload_image_path() };

			Log.i("part_id", a.getPart_id());
			DbHandler.performExecuteInsert(statement, parameters);

			Log.e("db id", dbworker.get_Max_id_competitor_table() + "");
		}

		database.close();
		coArrayList.clear();

	}
}
