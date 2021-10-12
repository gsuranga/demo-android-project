package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import ceylon.linux.db.Dbworker;

public class StockReport extends Fragment {
	Dbworker dbworker;
	ArrayList<HashMap<String, String>> values_for_dealers = new ArrayList<HashMap<String, String>>();
	CustomAutoCompleteTextView dealer_search;
	ArrayList<StockReport.ItemDetails> iItemDetails = new ArrayList<StockReport.ItemDetails>();
	ArrayList<StockReport.ItemDetails> fixitemDetails;
	private OnItemClickListener autoItemSelectedListner_dealer = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                        long arg3) {
			iItemDetails.clear();
			HashMap<String, String> s = (HashMap<String, String>) arg0
				.getAdapter().getItem(arg2);
			Cursor cursor = dbworker.getDealerStockByDealerAccountNumber(s
				.get("ID"));

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				ItemDetails details = new ItemDetails();
				details.item_part_no = cursor.getString(0);
				details.description = cursor.getString(1);
				details.remaining_qty = cursor.getString(2);
				details.last_stock_date = cursor.getString(3);

				/*
				 * Log.i("details.item_part_no", cursor.getString(0));
				 * Log.i("details.description", cursor.getString(1));
				 * Log.i("details.remaining_qty", cursor.getString(2));
				 * Log.i("details.last_stock_date ", cursor.getString(3));
				 */
				iItemDetails.add(details);

				cursor.moveToNext();

			}
			fixitemDetails = (ArrayList<ItemDetails>) iItemDetails.clone();
			arrayAdapter.notifyDataSetChanged();
		}
	};
	EditText item_search;
	TextWatcher t = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
		                          int count) {
			arrayAdapter.getFilter().filter(s);

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
		                              int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	private ListView partList;
	private ArrayAdapter<StockReport.ItemDetails> arrayAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbworker = new Dbworker(getActivity());
		if (iItemDetails.isEmpty()) {
			populate_Dealers();
		}
		dbworker.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		getActivity().setRequestedOrientation(
			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		View rootView = inflater.inflate(R.layout.stock_report_view, container,
			false);

		String[] from = {"NAME", "ID"};
		int[] to = {R.id.auto_id, R.id.auto_name};
		SimpleAdapter adapter = new SimpleAdapter(getActivity(),
			values_for_dealers, R.layout.row, from, to);

		dealer_search = (CustomAutoCompleteTextView) rootView
			.findViewById(R.id.select_dealer);
		item_search = (EditText) rootView.findViewById(R.id.select_part);
		item_search.addTextChangedListener(t);
		dealer_search.setThreshold(1);
		dealer_search.setOnItemClickListener(autoItemSelectedListner_dealer);
		partList = (ListView) rootView.findViewById(R.id.part_list);

		arrayAdapter = new ArrayAdapter<StockReport.ItemDetails>(getActivity()
			.getApplicationContext(), R.layout.stock_report_2, iItemDetails) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ItemDetailsViewHolder itemDetailsViewHolder;
				if (convertView == null) {
					LayoutInflater layoutInflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = layoutInflater.inflate(
						R.layout.stock_report_2, null);
					itemDetailsViewHolder = new ItemDetailsViewHolder();
					itemDetailsViewHolder.itemPartView = (TextView) convertView
						.findViewById(R.id.part_no);
					itemDetailsViewHolder.descriptionView = (TextView) convertView
						.findViewById(R.id.description);
					itemDetailsViewHolder.remainingView = (TextView) convertView
						.findViewById(R.id.remaining_qty);
					itemDetailsViewHolder.lastStockDateView = (TextView) convertView
						.findViewById(R.id.last_stock_date);
					convertView.setTag(itemDetailsViewHolder);
				} else {
					itemDetailsViewHolder = (ItemDetailsViewHolder) convertView
						.getTag();
				}
				ItemDetails item = getItem(position);
				itemDetailsViewHolder.itemPartView.setText(item.item_part_no);
				itemDetailsViewHolder.descriptionView.setText(item.description);
				itemDetailsViewHolder.remainingView.setText(item.remaining_qty);
				itemDetailsViewHolder.lastStockDateView
					.setText(item.last_stock_date);
				return convertView;
			}

			@Override
			public Filter getFilter() {
				return filter;
			}

			Filter filter = new Filter() {

				ArrayList<ItemDetails> filtered_item_list;

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {

					FilterResults results = new FilterResults();
					filtered_item_list = new ArrayList<ItemDetails>();
					for (ItemDetails item : fixitemDetails) {
						String term = constraint.toString().trim()
							.toLowerCase();
						if (item.item_part_no.toLowerCase().startsWith(term)
							|| item.description.toLowerCase().startsWith(
							term)) {

							filtered_item_list.add(item);
						}
					}
					Log.i("SIZE-1", arrayAdapter.getCount() + "");
					results.values = filtered_item_list;
					results.count = filtered_item_list.size();
					return results;
				}

				@Override
				protected void publishResults(CharSequence constraint,
				                              FilterResults results) {

					iItemDetails = (ArrayList<ItemDetails>) results.values;
					results.count = filtered_item_list.size();
					notifyDataSetChanged();
					clear();
					// Log.i("size after clear",iItemDetails.size()+"");
					for (ItemDetails i : iItemDetails)
						add(i);
					notifyDataSetInvalidated();

				}
			};


		};
		partList.setAdapter(arrayAdapter);
		dealer_search.setAdapter(adapter);
		return rootView;
	}

	public void populate_Dealers() {
		Cursor cursor1 = dbworker.get_all_dealers();
		if (cursor1 != null) {
			cursor1.moveToFirst();
			while (!cursor1.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("NAME", cursor1.getString(3));
				temp.put("ID", String.valueOf(cursor1.getString(2)));
				temp.put("shop_name", String.valueOf(cursor1.getString(9)));
				temp.put("address", String.valueOf(cursor1.getString(4)));
				Log.i("String.valueOf(cursor1.getString(2))",
					String.valueOf(cursor1.getString(2)));
				values_for_dealers.add(temp);
				cursor1.moveToNext();
			}

		}
		cursor1.close();
	}

	private static class ItemDetails {

		String item_part_no;
		String description;
		String remaining_qty;
		String last_stock_date;
	}

	private static class ItemDetailsViewHolder {
		TextView itemPartView;
		TextView descriptionView;
		TextView remainingView;
		TextView lastStockDateView;
	}

}
