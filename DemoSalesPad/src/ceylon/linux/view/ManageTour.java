package ceylon.linux.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dimosales.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.db.Dbworker;

public class ManageTour extends Fragment_Competitior_parts {
	public static MyBaseAdapter adapter;
	View rootView;
	Dbworker dbworker;
	ListView show_tours;
	SimpleAdapter dealer_adapter;
	SimpleAdapter city_adapter;
	ArrayList<HashMap<String, String>> values_for_dealers = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> cities_array = new ArrayList<HashMap<String, String>>();
	EditText select;
	Date time_constant;
	ArrayList<Tour_plans> tour_plans = new ArrayList<Tour_plans>();
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
	private ArrayList<Tour_plans> tour_plans_array;

	public ManageTour() {
		SimpleDateFormat tc = new SimpleDateFormat("HH:mm");
		try {
			time_constant = tc.parse("15:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		rootView = inflater.inflate(R.layout.manage_tour_plan, container, false);
		dbworker = new Dbworker(getActivity());

		populate_tour_plans();
		show_tours = (ListView) rootView.findViewById(R.id.show_tour);
		select = (EditText) rootView.findViewById(R.id.select);

		adapter = new MyBaseAdapter();
		select.addTextChangedListener(t);
		show_tours.setAdapter(adapter);

		show_tours.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(final AdapterView<?> parent,
			                               View view, final int position, long id) {

				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
					getActivity());
				alertBuilder.setTitle("Delete Tour");
				alertBuilder.setMessage("Are you Sure want to delete Tour");
				alertBuilder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
						                    int which) {
							Tour_plans t = (Tour_plans) parent
								.getItemAtPosition(position);
							tour_plans.remove(position);
							adapter.notifyDataSetChanged();
							dbworker.DeleteTourPlan(t.ID);
							dbworker.close();
							return;
						}
					});
				alertBuilder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
						                    int which) {

						}
					});

				alertBuilder.show();

				return true;
			}
		});

		populate_Dealers();
		populate_cities();

		String[] from = {"NAME", "ID"};
		int[] to = {R.id.auto_id, R.id.auto_name};
		dealer_adapter = new SimpleAdapter(getActivity(), values_for_dealers,
			R.layout.row, from, to);
		city_adapter = new SimpleAdapter(getActivity(), cities_array,
			R.layout.row, from, to);
		show_tours.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			                        final int position, long id) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				Tour_plans t = tour_plans.get(position);

				try {
					Date tour_date = sdf.parse(t.date);
					Date today = new Date();
					sdf.parse(sdf.format(today));

					if (tour_date.after(sdf.parse(sdf.format(today)))) {
						name(position);
					} else if (tour_date.before((sdf.parse(sdf.format(today))))) {

						Toast.makeText(getActivity(),
							"Cannot update the Tour plan",
							Toast.LENGTH_LONG).show();
					} else if (tour_date.equals(sdf.parse(sdf.format(today)))) {

						SimpleDateFormat formatter = new SimpleDateFormat(
							"HH:mm");
						Log.i("time", formatter.format((today)));
						Date time = formatter.parse(formatter.format((today)));

						if (time.compareTo(time_constant) > 0) {
							Toast.makeText(getActivity(),
								"Cannot update the Tour plan",
								Toast.LENGTH_SHORT).show();

						} else {

							name(position);

						}

					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// name(position);
			}

		});

		return rootView;
	}

	public void populate_tour_plans() {
		Cursor cursor1 = dbworker.get_tour_plan();
		if (cursor1 != null) {
			cursor1.moveToFirst();
			while (!cursor1.isAfterLast()) {

				Tour_plans t = new Tour_plans();

				t.ID = String.valueOf(cursor1.getString(0));
				t.town = String.valueOf(cursor1.getString(1));
				t.dealer = String.valueOf(cursor1.getString(2));
				t.date = String.valueOf(cursor1.getString(3));
				tour_plans.add(t);
				cursor1.moveToNext();
			}
			tour_plans_array = (ArrayList<Tour_plans>) tour_plans.clone();

		}
		cursor1.close();
	}

	public void name(final int p_id) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("Values");
		final CustomAutoCompleteTextView edit_town = new CustomAutoCompleteTextView(
			getActivity(), null);
		final CustomAutoCompleteTextView dealer = new CustomAutoCompleteTextView(
			getActivity(), null);
		edit_town.setAdapter(city_adapter);
		dealer.setAdapter(dealer_adapter);

		LinearLayout ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.addView(edit_town);
		ll.addView(dealer);

		alertDialog.setView(ll);
		alertDialog.setCancelable(false);
		alertDialog.setPositiveButton("Update",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					Tour_plans t = tour_plans.get(p_id);
					t.town = edit_town.getText().toString();
					t.dealer = dealer.getText().toString();
					tour_plans.set(p_id, t);
					adapter.notifyDataSetChanged();
					dbworker.update_tour_plan(t.town, t.dealer, t.date,
						"0", t.ID);
					dbworker.close();

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

				values_for_dealers.add(temp);
				cursor1.moveToNext();
			}

		}
		cursor1.close();
	}

	public void populate_cities() {

		// /Get the JSON object from the data
		Jsonhelper jsonhelper = new Jsonhelper();
		JSONObject parent = jsonhelper.parseJSONData(getActivity(),
			"tbl_city.json");

		JSONArray cities;
		try {
			cities = parent.getJSONArray("cities");
			for (int i = 0; i < cities.length(); i++) {
				JSONObject j = cities.getJSONObject(i);
				j.getString("city_name");
				j.getString("city_id");
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("NAME", j.getString("city_name"));
				temp.put("ID", j.getString("city_id"));
				cities_array.add(temp);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static class ViewHolder {

		TextView ID;
		TextView town;
		TextView dealer;
		TextView date;
	}

	static class Tour_plans {
		String ID;
		String town;
		String dealer;
		String date;

	}

	private class MyBaseAdapter extends BaseAdapter implements Filterable {

		public MyBaseAdapter() {

		}

		@Override
		public int getCount() {
			return tour_plans.size();
		}

		@Override
		public Object getItem(int position) {
			return tour_plans.get(position);
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
				convertView = layoutInflater.inflate(R.layout.mange_tour_row,
					null);
				viewHolder = new ViewHolder();
				viewHolder.ID = (TextView) convertView.findViewById(R.id.id);
				viewHolder.town = (TextView) convertView
					.findViewById(R.id.town);
				viewHolder.dealer = (TextView) convertView
					.findViewById(R.id.dealer);
				viewHolder.date = (TextView) convertView
					.findViewById(R.id.added_date);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Tour_plans t = (Tour_plans) getItem(position);

			viewHolder.ID.setText(t.ID);
			viewHolder.town.setText(t.town);
			viewHolder.dealer.setText(t.dealer);
			viewHolder.date.setText(t.date);

			return convertView;
		}

		@Override
		public Filter getFilter() {
			return filter;
		}

		Filter filter = new Filter() {

			ArrayList<Tour_plans> filtered_item_list;

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				filtered_item_list = new ArrayList<Tour_plans>();
				for (Tour_plans Tour_plan : tour_plans_array) {
					String term = constraint.toString().trim().toLowerCase();

					if (Tour_plan.date.toLowerCase().startsWith(term)
						|| Tour_plan.dealer.toLowerCase().startsWith(term)
						|| Tour_plan.town.toLowerCase().startsWith(term)) {
						filtered_item_list.add(Tour_plan);
					}

				}
				results.values = filtered_item_list;
				results.count = filtered_item_list.size();
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
			                              FilterResults results) {
				tour_plans = (ArrayList<Tour_plans>) results.values;
				notifyDataSetChanged();
			}
		};


	}

}
