package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import ceylon.linux.db.Dbworker;
import ceylon.linux.utility.NumbeFormater;

public class VSDfragment extends Fragment {
	public static final ArrayList<String> clicked_items = new ArrayList<String>();
	public static MyBaseAdapter adapter;
	public static String description;
	public static String selling_price;
	public static String item_id;
	public static String part_no;
	public static String qty;
	ListView show_items;
	EditText part_select;
	Button back, back2;
	Dbworker db;
	int j = 0;
	
	SharedPreferences userdata;
	
	TextWatcher t = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
		                          int count) {
			adapter.getFilter().filter(part_select.getText());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
		                              int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	private ArrayList<HashMap<String, String>> itemCollection = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> item_list = new ArrayList<HashMap<String, String>>();

	public static int getIndexOFValue(String value,
	                                  ArrayList<HashMap<String, String>> listMap) {
		int i = 0;
		for (HashMap<String, String> map : listMap) {
			if (map.get("part_no").contains(value)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userdata = getActivity().getSharedPreferences("USERDATA",
				Context.MODE_PRIVATE);
		db = new Dbworker(getActivity());
		if (item_list.isEmpty()) {
			populate_list();
		} else {

			itemCollection = ItemFragment.itemCollection;

		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		getActivity().setRequestedOrientation(
			ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		View rootView = inflater.inflate(R.layout.vsd_fragment, container,
			false);
		part_select = (EditText) rootView.findViewById(R.id.select_part);
		show_items = (ListView) rootView.findViewById(R.id.part_list);
		back = (Button) rootView.findViewById(R.id.back);
		back2 = (Button) rootView.findViewById(R.id.back2);
		part_select.addTextChangedListener(t);
		adapter = new MyBaseAdapter();
		show_items.setAdapter(adapter);

		return rootView;
	}

	public void populate_list() {
		Cursor cursor = db.get_all_items();
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("item_id", cursor.getString(0).trim());
				temp.put("part_no", cursor.getString(2).trim());
				temp.put("description", cursor.getString(1).trim());
				temp.put("selling_price", cursor.getString(3).trim());
				temp.put("qty", cursor.getString(4).trim());
				
				
				double vat = Double.parseDouble(userdata.getString(
						"vat", ""));
				double price_with_vat = cursor.getDouble(3)
						* (vat + 100) / 100;

				NumbeFormater formater = new NumbeFormater();

				temp.put(
						"selling_price_with_vat",
						(formater
								.format_double_val_to_decimal_Strng(price_with_vat))
								+ "");
		

				item_list.add(temp);
				cursor.moveToNext();
			}
			cursor.close();
			itemCollection = (ArrayList<HashMap<String, String>>) item_list.clone();
		}
		db.close();
	}

	static class ViewHolder {

		TextView description;
		TextView part_no;
		TextView stock_qty;
		TextView selling_price;
		TextView selling_price_vat;
	}

	private class MyBaseAdapter extends BaseAdapter implements Filterable {

		public MyBaseAdapter() {

		}

		@Override
		public int getCount() {
			return item_list.size();
		}

		@Override
		public Object getItem(int position) {
			return item_list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				LayoutInflater layoutInflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.stock_report,
					null);
				viewHolder = new ViewHolder();
				viewHolder.description = (TextView) convertView
					.findViewById(R.id.description);
				viewHolder.part_no = (TextView) convertView
					.findViewById(R.id.part_no);
				viewHolder.stock_qty = (TextView) convertView
					.findViewById(R.id.qty);
				viewHolder.selling_price = (TextView) convertView
					.findViewById(R.id.selling_price);
				
				viewHolder.selling_price_vat = (TextView) convertView
						.findViewById(R.id.selling_price_vat);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			HashMap<String, String> itemInfo = (HashMap<String, String>) getItem(position);

			viewHolder.description.setText(itemInfo.get("description"));
			viewHolder.part_no.setText(itemInfo.get("part_no"));
			viewHolder.selling_price.setText("Selling price:-"+ itemInfo.get("selling_price"));
			viewHolder.stock_qty.setText("Current quantity:-"+ itemInfo.get("qty"));
			viewHolder.selling_price_vat.setText("Selling price with vat:"
					+ itemInfo.get("selling_price_with_vat"));
			HashMap<String, String> obj = (HashMap<String, String>) show_items.getAdapter().getItem(position);

			return convertView;
		}

		@Override
		public Filter getFilter() {
			return filter;
		}

		Filter filter = new Filter() {

			ArrayList<HashMap<String, String>> filtered_item_list;

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				filtered_item_list = new ArrayList<HashMap<String, String>>();
				for (HashMap<String, String> item : itemCollection) {
					String term = constraint.toString().trim().toLowerCase();
					if (item.get("part_no").toLowerCase().startsWith(term) || item.get("description").toLowerCase().startsWith(term)) {
						filtered_item_list.add(item);
					}
				}
				results.values = filtered_item_list;
				results.count = filtered_item_list.size();
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
			                              FilterResults results) {
				item_list = (ArrayList<HashMap<String, String>>) results.values;
				Log.i("Item List Size is", item_list.size() + "");
				notifyDataSetChanged();
			}
		};


	}

}
