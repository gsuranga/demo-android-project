package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import ceylon.linux.controller.StaticValues;
import ceylon.linux.db.Dbworker;

@SuppressLint("NewApi")
public class UpdateDealerLocation extends Fragment {
	static final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	public static String discount_percentage;
	public static String dealer_id;
	public static String dealer_account_no;
    public static String dealer_name;
	public static String credit_limit;
	public static String outstanding_amount;
	public static String overdue_amount;
	ListView show_dealers;
	SimpleAdapter adapter;
	EditText select_dealer;
	Dbworker db;
	TextWatcher tWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			adapter.getFilter().filter(s);

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	public UpdateDealerLocation() {
		Sugesstion_order_fragment.update_status = "0";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		StaticValues.order_item_models.clear();
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		ItemFragment.clicked_items.clear();
		db = new Dbworker(getActivity());

		View rootView = inflater.inflate(R.layout.show_loocation, container,
				false);

		select_dealer = (EditText) rootView.findViewById(R.id.select);
		populate_list();
		show_dealers = (ListView) rootView.findViewById(R.id.list);

		adapter = new SimpleAdapter(getActivity(), list,
				R.layout.dealers_list_location_item, new String[] { "dealer_name",
						"delar_account_no" }, new int[] { R.id.dealer_name1,
						R.id.dealer_account1});

		show_dealers.setAdapter(adapter);

		select_dealer.addTextChangedListener(tWatcher);

		show_dealers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id)

			{
				@SuppressWarnings("unchecked")
				HashMap<String, String> ss = (HashMap<String, String>) adapter
						.getItem(position);
			
				dealer_id = ss.get("dealer_id");
				dealer_name = ss.get("dealer_name");
			
			Intent intent=new Intent(getActivity(),MapActivity.class);
				startActivity(intent);
				getActivity().finish();
				
				
				
		//	move_to_another_fragment();
			}

		});

		return rootView;
	}

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first

		db.close();

	}

	public void move_to_another_fragment() {

		Fragment fragment = new InvoiceInfoFragment();
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(
				"Invoice Information(" + dealer_name + ")");
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack(null)
				.commit();
		list.clear();
		ViewOrdersFragment.purchase_order_edit=0;
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

				
					
					list.add(temp);

					cursor.moveToNext();
				}
				cursor.close();
			}

			db.close();
		}
	}

}
