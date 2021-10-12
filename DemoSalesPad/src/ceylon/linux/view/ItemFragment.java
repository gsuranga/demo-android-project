package ceylon.linux.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import com.example.dimosales.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Editable.Factory;
import android.text.InputType;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.controller.StaticValues;
import ceylon.linux.db.Dbworker;
import ceylon.linux.model.Purchase_order_item_model;
import ceylon.linux.utility.NumbeFormater;
import ceylon.linux.utility.Utility;

public class ItemFragment extends Fragment {

	public static final ArrayList<String> clicked_items = new ArrayList<String>();
	public volatile static MyBaseAdapter adapter;
	public static String description;
	public static String selling_price;
	public static String item_id;
	public static String comment;
	public static String showAvgMovementAtDealer;
	public static String part_no;
	public static String qty;
	public static volatile ArrayList<HashMap<String, String>> item_list = new ArrayList<HashMap<String, String>>();
	public volatile static ArrayList<HashMap<String, String>> itemCollection = new ArrayList<HashMap<String, String>>();
	public static String item_temp_no = "";
	SharedPreferences userdata;

	ListView show_items;
	EditText part_select1;
	Button back, back2;
	Dbworker db;
	int j = 0;
	
	OnClickListener back_fragment = new OnClickListener() {

		@Override
		public void onClick(View v) {

			item_id = "-1";
			move_to_another_fragment(new Purchase_Order_Fragement(),
					"Order Fragment(" + HomeFragment.dealer_name + ")");

		}
	};

	TextWatcher t = new TextWatcher() {
		Editable newa;
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(s.length()>0){
			item_temp_no = s.toString();
			}
			adapter.getFilter().filter(s);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {//			
		}

		@Override
		public void afterTextChanged(Editable s) {
			
		}
	};

	OnClickListener back_to_finvoice_info = new OnClickListener() {

		@Override
		public void onClick(View v) {

			move_to_another_fragment(new InvoiceInfoFragment(),
					""+HomeFragment.dealer_name);

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
/*		if (!item_temp_no.isEmpty()) {
			try{
			adapter.getFilter().filter(item_temp_no);
			}catch(Exception e){
				e.printStackTrace();
			}
		}*/
		
		db = new Dbworker(getActivity());
		
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		item_list = (ArrayList<HashMap<String, String>>) itemCollection.clone();
		
		userdata = getActivity().getSharedPreferences("USERDATA",Context.MODE_PRIVATE);

		if (itemCollection.isEmpty()) {
			populate_list();
		}
		
		for (int i = 0; i < InvoiceInfoFragment.invoice_parts.size(); i++) {
			int a = getIndexOFValue(InvoiceInfoFragment.invoice_parts.get(i),item_list);
			if (a == -1) {
				continue;
			}
			try{
				Collections.swap(item_list, a, i);
			}catch(Exception e){};
			j = i;
		}
		
		int k = 0;
		int p = 0;
		for (p = j; p < j + InvoiceInfoFragment.fast_moingitems.size(); p++) {
			int d = getIndexOFValue(InvoiceInfoFragment.fast_moingitems.get(k),itemCollection);
			if (d == -1) {
				continue;
			}
			try{
				Collections.swap(itemCollection, d, p);
			}catch(Exception e){};
			k++;
		}
		
		j = p;

		int l = 0;
		for (p = j; p < j + InvoiceInfoFragment.not_achieved.size(); p++) {

			int d = getIndexOFValue(InvoiceInfoFragment.not_achieved.get(l),itemCollection);
			if (d == -1) {
				continue;
			}
			try{
				Collections.swap(itemCollection, d, p);
			}catch(Exception e){};
			l++;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.itemfragment,container, false);
		part_select1 = (EditText) rootView.findViewById(R.id.select_part1);
		show_items = (ListView) rootView.findViewById(R.id.part_list);
		back = (Button) rootView.findViewById(R.id.back);
		back2 = (Button) rootView.findViewById(R.id.back2);
		back.setOnClickListener(back_fragment);
		back2.setOnClickListener(back_to_finvoice_info);

		j = 0;

		if (StaticValues.order_item_models.isEmpty()) {
			back.setVisibility(View.GONE);
		} else {
			back.setVisibility(View.VISIBLE);
		}

		adapter = new MyBaseAdapter();
		show_items.setAdapter(adapter);
		part_select1.addTextChangedListener(t);

	
		
		show_items.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {				
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

						HashMap<String, String> temp = new HashMap<String, String>();
						temp.put("item_id", cursor.getString(0).trim());
						temp.put("part_no", cursor.getString(2).trim());
						temp.put("description", cursor.getString(1).trim());
						temp.put("selling_price", cursor.getString(3).trim());
						double vat = Double.parseDouble(userdata.getString("vat", ""));
						double price_with_vat = cursor.getDouble(3) * (vat + 100) / 100;
						NumbeFormater formater = new NumbeFormater();
						temp.put("selling_price_with_vat",
								(formater.format_double_val_to_decimal_Strng(price_with_vat))+ "");
						temp.put("qty", cursor.getString(4).trim());
						temp.put("avg_movement_at_dealer", "0.00");
						temp.put("avg_movement_in_area", cursor.getString(7).trim());

						try{
							itemCollection.add(temp);
						}catch(ConcurrentModificationException e){						
						}catch(Exception e){};
						
						cursor.moveToNext();
					}

					getActivity().runOnUiThread(new Runnable() {
						@SuppressWarnings("unchecked")
						public void run() {
							item_list = (ArrayList<HashMap<String, String>>) itemCollection.clone();
							if (mDialog != null && mDialog.isShowing()) {
								try {
									mDialog.dismiss();
								} catch (Exception e) {
									// nothing
								}
							}
							adapter.notifyDataSetChanged();
						}
					});

				}
				db.close();
			}
		}.start();

	}

	public void name(final int position) {
		final String avgMovementAtDealer;
		String avgMovementInArea = null;

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle(item_list.get(position).get("description"));
		final TextView Error = new TextView(getActivity());
		final EditText quantity = new EditText(getActivity());
		final EditText commentTxt = new EditText(getActivity());
		final TextView avg_movement_at_dealer = new TextView(getActivity());
		final TextView avg_movement_in_area = new TextView(getActivity());
		// final EditText lot = new EditText(getActivity());
		quantity.setHint("Enter Quantity Here");
		quantity.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);

		// if(item_list.get(position).get("avg_movement_at_dealer")==null ){
		// avgMovementAtDealer="0.0";
		// }
		// else{
		avgMovementAtDealer = item_list.get(position).get("avg_movement_at_dealer");
		// }

		// if(item_list.get(position).get("avg_movement_in_area")== null ){

		// avgMovementInArea="0.0";
		// }
		// else{

		avgMovementInArea = item_list.get(position).get("avg_movement_in_area");

		avg_movement_at_dealer.setText("Avg Movement At Dealer:"
				+ avgMovementAtDealer);
		avg_movement_in_area.setText("Avg Movement in Area:"
				+ avgMovementInArea);
		avg_movement_at_dealer.setTextColor(Color.RED);
		avg_movement_in_area.setTextColor(Color.RED);
		avg_movement_at_dealer.setTextSize(25);
		avg_movement_in_area.setTextSize(25);
		commentTxt.setHint("Add your Comment Here");

		LinearLayout ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.addView(quantity);
		ll.addView(Error);
		ll.addView(avg_movement_at_dealer);
		ll.addView(avg_movement_in_area);
		ll.addView(commentTxt);

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
							try{
								item_list.get(position).get("selling_price");
							try{
								clicked_items.add(item_list.get(position).get("part_no"));
							}catch(IllegalStateException e){		
							}catch(ConcurrentModificationException e){
							};
							
								description = item_list.get(position).get("description");
								selling_price = item_list.get(position).get("selling_price");
								InvoiceInfoFragment.status = "2";
								item_id = item_list.get(position).get("item_id");
								part_no = item_list.get(position).get("part_no");
								
								
								qty = quantity.getText().toString();
								comment = commentTxt.getText().toString();
								showAvgMovementAtDealer = avgMovementAtDealer;
								
								Log.e("qty", qty+"");

								Purchase_order_item_model item_model = new Purchase_order_item_model();

								item_model.setDescription(description);
								item_model.setSelling_price(selling_price);
								item_model.setItem_id(item_id);
								item_model.setPart_no(part_no);
								item_model.setQty(qty);
								item_model.setComment(comment);
								item_model.setAvg_movement(showAvgMovementAtDealer);
								item_temp_no = part_no;
								
								try{	
									StaticValues.order_item_models.add(item_model);
								}catch(IllegalStateException e){	
								}catch(ConcurrentModificationException e){
								};
								
							try{			
							//	Utility.writeToExternalFile(description,selling_price,item_id,part_no,qty,comment,showAvgMovementAtDealer);	
								Utility.writeToExternalFile1(description,selling_price,item_id,part_no,qty,comment,showAvgMovementAtDealer);
							}catch(Exception e){
								e.printStackTrace();
							}							
					move_to_another_fragment(new Purchase_Order_Fragement(),"Purchase Order -" 
							+ HomeFragment.dealer_name);												
							}catch(Exception e){
								e.printStackTrace();
							}
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
			viewHolder.selling_price.setText("Selling price:-"
					+ itemInfo.get("selling_price"));
			viewHolder.selling_price_vat.setText("Selling price with vat:"
					+ itemInfo.get("selling_price_with_vat"));
			viewHolder.stock_qty.setText("Current quantity:-"
					+ itemInfo.get("qty"));

			HashMap<String, String> obj = (HashMap<String, String>) show_items
					.getAdapter().getItem(position);

			for (Purchase_order_item_model model : StaticValues.order_item_models) {

				if (model.getPart_no().trim().equals(obj.get("part_no").trim())) {

					convertView.setBackgroundColor(Color.parseColor("#009933"));
					Log.e(model.getPart_no().trim(), obj.get("part_no").trim());
				}

				else if (InvoiceInfoFragment.invoice_parts.contains(obj
						.get("part_no"))) {
					convertView.setBackgroundColor(Color.RED);

				} else if (InvoiceInfoFragment.fast_moingitems.contains(obj
						.get("part_no"))) {
					convertView.setBackgroundColor(Color.YELLOW);

				} else if (InvoiceInfoFragment.not_achieved.contains(obj
						.get("part_no"))) {
					convertView.setBackgroundColor(Color.MAGENTA);

				} else {
					convertView.setBackgroundColor(Color.WHITE);
				}
			}
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
						try{
							filtered_item_list.add(item);
						}catch(ConcurrentModificationException e){							
						}catch(Exception e){};
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