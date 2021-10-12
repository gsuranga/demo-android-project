package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
public class FragmentSelectDealerRegister extends Fragment {
	static final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	public static String discount_percentage;
	public static String dealer_id;
	public static String dealer_account_no;
    public static String dealer_name;
	public static String credit_limit;
	public static String outstanding_amount;
	public static String overdue_amount;
	public static String dealer_address;
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

	public FragmentSelectDealerRegister() {
		Sugesstion_order_fragment.update_status = "0";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	//	Purchase_Order_Fragement.wrapper.clear();
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		ItemFragment.clicked_items.clear();
		db = new Dbworker(getActivity());

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		select_dealer = (EditText) rootView.findViewById(R.id.select);
		populate_list();
		show_dealers = (ListView) rootView.findViewById(R.id.list);

		adapter = new SimpleAdapter(getActivity(), list,
				R.layout.dealers_list_view_item, new String[] { "dealer_name",
						"delar_account_no" ,"credit_limit","outstanding_amount","overdue_amount"}, new int[] { R.id.dealer_name,
						R.id.dealer_account,R.id.credit_limit,R.id.outstanding_amount,R.id.overdue_amount });

		show_dealers.setAdapter(adapter);

		select_dealer.addTextChangedListener(tWatcher);

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
				dealer_address=ss.get("delar_address");
				overdue_amount= ss.get("overdue_amount");
				outstanding_amount= ss.get("outstanding_amount");
				credit_limit= ss.get("credit_limit");
				
				StaticValues.order_item_models.clear();
				
				StaticValues.jasonNot=null;
				StaticValues.jasonPast = null;
				
				move_to_another_fragment(new FragmentDealerRegister(), "Dealer Register "+dealer_name);
			}

		});

		return rootView;
	}

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first

		db.close();

	}

	public void move_to_another_fragment(Fragment f, String name) {

	
		list.clear();
		ViewSavedOrdersFragment.purchase_order_edit=0;
		
		Fragment fragment = f;
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(name);
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack("item")
				.commit();
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
					temp.put("delar_address", cursor.getString(4));

					temp.put("credit_limit", cursor.getString(12));
					temp.put("outstanding_amount", cursor.getString(13));
					temp.put("overdue_amount", cursor.getString(14));

					Log.i("creditlimit", cursor.getString(12));
					Log.i("outstanding_amount", cursor.getString(13));
					Log.i("overdue_amount", cursor.getString(14));

					list.add(temp);

					cursor.moveToNext();
				}
				cursor.close();
			}

			db.close();
		}
	}

}
