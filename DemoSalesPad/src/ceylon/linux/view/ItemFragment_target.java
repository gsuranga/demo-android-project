package ceylon.linux.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.example.dimosales.R;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.controller.StaticValues;
import ceylon.linux.db.Dbworker;
import ceylon.linux.model.TargetItemModel;
import ceylon.linux.utility.NumbeFormater;

@SuppressWarnings("unused")
public class ItemFragment_target extends Fragment {

	public static final ArrayList<String> clicked_items = new ArrayList<String>();
	public volatile static MyBaseAdapter adapter;
	public static String description;
	public static String selling_price;
	public static String item_id;
	public static String comment;
	public static String showAvgMovementAtDealer;
	OnClickListener back_fragment = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			item_id = "-1";
			move_to_another_fragment(new Purchase_Order_Fragement(),
					"Order Fragment(" + HomeFragment.dealer_name + ")");

		}
	};
	public static String part_no;
	public static String qty;
	public static volatile ArrayList<HashMap<String, String>> item_list = new ArrayList<HashMap<String, String>>();
	public volatile static ArrayList<HashMap<String, String>> itemCollection = new ArrayList<HashMap<String, String>>();
	ListView show_items;
	EditText part_select;
	Button back, back2;
	Dbworker db;
	int j = 0;
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
	OnClickListener back_to_finvoice_info = new OnClickListener() {

		@Override
		public void onClick(View v) {
			move_to_another_fragment(new Fragment_line_wise_item_target(),
					"Line Item Wise Target(" + LineItemWiseTarget.dealer_name
							+ ")");

		}
	};

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
		db = new Dbworker(getActivity());

		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		item_list = (ArrayList<HashMap<String, String>>) itemCollection.clone();

		
		if (itemCollection.isEmpty()) {
			populate_list();
		}
		
		for (int i = 0; i < InvoiceInfoFragment.invoice_parts.size(); i++) {

			int a = getIndexOFValue(InvoiceInfoFragment.invoice_parts.get(i),
					item_list);
			if (a == -1) {

				continue;

			}
			Collections.swap(item_list, a, i);

			j = i;

		}
		int k = 0;
		int p = 0;
		for (p = j; p < j + InvoiceInfoFragment.fast_moingitems.size(); p++) {

			int d = getIndexOFValue(InvoiceInfoFragment.fast_moingitems.get(k),
					itemCollection);
			if (d == -1) {
				continue;

			}
			Collections.swap(itemCollection, d, p);

			k++;

		}
		j = p;

		int l = 0;
		for (p = j; p < j + InvoiceInfoFragment.not_achieved.size(); p++) {

			int d = getIndexOFValue(InvoiceInfoFragment.not_achieved.get(l),itemCollection);

			if (d == -1) {
				continue;

			}
			
			Collections.swap(itemCollection, d, p);

			l++;

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.itemfragment_target,
				container, false);
		part_select = (EditText) rootView.findViewById(R.id.select_part);
		show_items = (ListView) rootView.findViewById(R.id.part_list);
		back = (Button) rootView.findViewById(R.id.back);
		back2 = (Button) rootView.findViewById(R.id.back2);
		back.setOnClickListener(back_fragment);
		back2.setOnClickListener(back_to_finvoice_info);

		j = 0;

		if (StaticValues.order_item_models.isEmpty()) {

			back.setVisibility(View.INVISIBLE);

		} else {

			back.setVisibility(View.VISIBLE);

		}

		adapter = new MyBaseAdapter();
		show_items.setAdapter(adapter);
		part_select.addTextChangedListener(t);

		show_items.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				name(position);
				
			}

		});
		return rootView;
	}

	public void move_to_another_fragment(Fragment f, String name) {
		Fragment fragment = f;
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(name);
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack("item")
				.commit();
	}

	public void populate_list() {
		final ProgressDialog mDialog = ProgressDialog.show(getActivity(),
				"Please wait...", "Retrieving data ...", true);

		new Thread() {
			public void run() {
				Cursor cursor = db.get_all_items();

				if (cursor != null) {
					cursor.moveToFirst();
					while (!cursor.isAfterLast()) {
						Log.i("cfcfcf", cursor.getString(2));
						HashMap<String, String> temp = new HashMap<String, String>();
						temp.put("item_id", cursor.getString(0).trim());
						temp.put("part_no", cursor.getString(2).trim());
						temp.put("description", cursor.getString(1).trim());
						temp.put("selling_price", cursor.getString(3).trim());
						temp.put("qty", cursor.getString(4).trim());
						temp.put("avg_movement_at_dealer", "0.00");
						temp.put("avg_movement_in_area", cursor.getString(7)
								.trim());
						itemCollection.add(temp);
						cursor.moveToNext();
					}

					getActivity().runOnUiThread(new Runnable() {

						public void run() {
							item_list = (ArrayList<HashMap<String, String>>) itemCollection
									.clone();
							if (mDialog != null && mDialog.isShowing()) {
								
								try {
									mDialog.dismiss();
								} catch (Exception e) {
									// TODO: handle exception
								}
								

							}
							adapter.notifyDataSetChanged();
						}
					});

				}
				// db.close();
			}
		}.start();
		db.close();
	}

	public void name(final int position) {

		boolean status = true;
		ArrayList<TargetItemModel> axxx = Fragment_line_wise_item_target.target_items;

		for (TargetItemModel targetItem : axxx) {
			if (targetItem.getItem_id() == Integer.parseInt(item_list.get(
					position).get("item_id"))) {

				status = false;
			}

		}
		if (status) {

			final Dialog dialog = new Dialog(getActivity());
			dialog.setTitle("Add Item");

			dialog.setContentView(R.layout.dialog_add_target_item);

			final EditText editMinQuanty = (EditText) dialog
					.findViewById(R.id.editMinQuanty);
			final EditText editAddQuanty = (EditText) dialog
					.findViewById(R.id.editAddQuanty);
			Button button_add = (Button) dialog.findViewById(R.id.button_add);
			Button button_cancel = (Button) dialog
					.findViewById(R.id.button_cancel);

			button_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});

			button_add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if(editMinQuanty.getText().toString().equals("")||editAddQuanty.getText().toString().equals(""))
					{
						Toast.makeText(getActivity(), "empty values are not allowed", Toast.LENGTH_SHORT).show();
					}
					else
					{
						
					TargetItemModel tim = new TargetItemModel();
					tim.setItem_id(Integer.parseInt(item_list.get(position)
							.get("item_id")));
					tim.setItem_name(item_list.get(position).get("description"));
					tim.setItem_part_no(item_list.get(position).get("part_no"));
					
					tim.setMinimum_qty(editMinQuanty.getText().toString());
					tim.setAdditional_qty(editAddQuanty.getText().toString());

					Fragment_line_wise_item_target.target_items.add(tim);

					Fragment fragment = new Target_finish_fragment();
					move_to_another_fragment(fragment, "Line Item Wise Target");

					dialog.dismiss();
					
					}
				}
			});

			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		} else {
			Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT)
					.show();
		}

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
				convertView = layoutInflater.inflate(R.layout.stock_report,null);
				viewHolder = new ViewHolder();
				viewHolder.description = (TextView) convertView.findViewById(R.id.description);
				viewHolder.part_no = (TextView) convertView.findViewById(R.id.part_no);
				viewHolder.stock_qty = (TextView) convertView.findViewById(R.id.qty);
				viewHolder.selling_price = (TextView) convertView.findViewById(R.id.selling_price);
				viewHolder.selling_price_vat = (TextView) convertView.findViewById(R.id.selling_price_vat);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			HashMap<String, String> itemInfo = (HashMap<String, String>) getItem(position);
  
			viewHolder.description.setText(itemInfo.get("description"));
			viewHolder.part_no.setText(itemInfo.get("part_no"));
			viewHolder.selling_price.setText("Selling price:-"
					+ itemInfo.get("selling_price"));
			viewHolder.stock_qty.setText("Current quantity:-"
					+ itemInfo.get("qty"));     
			
			double price_vat=0;
			if(!itemInfo.get("selling_price").equals(""))
			{
				price_vat=Double.parseDouble(itemInfo.get("selling_price"))*1.11;
			}
			
			
			viewHolder.selling_price_vat.setText("selling price with vat = "+
					new NumbeFormater().format_double_val_to_decimal_Strng(price_vat));
					//Double.parseDouble(itemInfo.get("selling_price"))*1.11);
			HashMap<String, String> obj = (HashMap<String, String>) show_items     
					.getAdapter().getItem(position);

			if (clicked_items.contains(obj.get("part_no"))) {
				convertView.setBackgroundColor(Color.GREEN);
			} else if (InvoiceInfoFragment.invoice_parts.contains(obj.get("part_no"))) {
				convertView.setBackgroundColor(Color.RED);

			} else if (InvoiceInfoFragment.fast_moingitems.contains(obj.get("part_no"))) {
				convertView.setBackgroundColor(Color.YELLOW);

			} else if (InvoiceInfoFragment.not_achieved.contains(obj.get("part_no"))) {
				convertView.setBackgroundColor(Color.MAGENTA);

			} else {
				convertView.setBackgroundColor(Color.WHITE);
			}
			// convertView.setBackgroundColor(Color.YELLOW);
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

					if (item.get("part_no").toLowerCase().startsWith(term)
							|| item.get("description").toLowerCase()
									.startsWith(term)) {
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
				notifyDataSetChanged();
			}
		};

	}

}