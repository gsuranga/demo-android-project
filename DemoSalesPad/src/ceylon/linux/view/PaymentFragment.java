package ceylon.linux.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.net.ParseException;
import android.os.AsyncTask;
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
import ceylon.linux.db.Dbworker;
import ceylon.linux.utility.NumbeFormater;

public class PaymentFragment extends Fragment {

	private EditText editText_dealername;
	private ListView list_payment_pending;
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String, String>> list_payments = new ArrayList<HashMap<String, String>>();
	public static String deliver_order_id = "0";
	NumbeFormater nf = new NumbeFormater();
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.payment_list_outstan,
				container, false);
		list_payment_pending = (ListView) rootView
				.findViewById(R.id.list_payment_pending);

		editText_dealername = (EditText) rootView
				.findViewById(R.id.editText_dealername);

		editText_dealername.addTextChangedListener(tWatcher);

		add_table_rows();

		list_payment_pending.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> ss = (HashMap<String, String>) adapter
						.getItem(arg2);

				move_to_another_fragment(ss.get("delar_name"));
				deliver_order_id = ss.get("D_ID");
			}
		});
		return rootView;
	}

	private void add_table_rows() {

		AsyncTask<String, String, String> a = new AsyncTask<String, String, String>() {
			Dbworker dbworker;

			@SuppressLint("SimpleDateFormat")
			@Override
			protected String doInBackground(String... params) {

				list_payments.clear();
				dbworker = new Dbworker(getActivity());
				Cursor cursor;
				if (NewTourItenaryFragment.dealer_id.equals("")) {
					cursor = dbworker.get_all_pending_payments();
				} else {
					cursor = dbworker
							.get_all_pending_payments_by_dealer_id_selected(NewTourItenaryFragment.dealer_id);
				}
				if (cursor.moveToFirst()) {
					do {
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("invoice_no",cursor.getString(8));
						hashMap.put("dealer_id", cursor.getString(0));
						hashMap.put("added_date", cursor.getString(1));
						hashMap.put("added_time", cursor.getString(2));
						hashMap.put("total_amount", nf
								.format_double_val_to_decimal_Strng(Double
										.parseDouble(cursor.getString(3))));
						hashMap.put("delar_name", cursor.getString(4));
						hashMap.put("pending_amount", nf
								.format_double_val_to_decimal_Strng(Double
										.parseDouble(cursor.getString(5))));
						hashMap.put("due_date", cursor.getString(6));
						hashMap.put("D_ID", cursor.getString(7));
						
						Calendar today = Calendar.getInstance();
						
						String strThatDay=cursor.getString(1);
						  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						  Date d = null;
						  try {
							  d = formatter.parse(strThatDay);//catch exception
						  } catch (ParseException e) {
							  Log.e("error", e+"");
						   e.printStackTrace();
						  } catch (java.text.ParseException e) {
							Log.e("error", e+"");
							e.printStackTrace();
						} 
						  Calendar that_day = Calendar.getInstance();
						  that_day.setTime(d);
						  
						  long diff = today.getTimeInMillis() - that_day.getTimeInMillis(); //result in millis
						  
						  long days = diff / (24 * 60 * 60 * 1000);
						  
						  int value = 0;
						  value=  (int)days;
						  Log.e("error", value+"");
						hashMap.put("no_of_days_over_due", value+"");

						list_payments.add(hashMap);

					} while (cursor.moveToNext());
				}
				Log.e("gg cursor size", cursor.getCount() + "");
				cursor.close();
				dbworker.close();

				adapter = new SimpleAdapter(getActivity(), list_payments,
						R.layout.payment_row_layout, new String[] {"invoice_no",
								"added_date", "added_time", "delar_name",
								"total_amount", "pending_amount", "due_date","no_of_days_over_due" },
						new int[] {R.id.text_invoice_no, R.id.text_date, R.id.text_time,
								R.id.text_dealer_name, R.id.text_total_amount,
								R.id.text_pending_amount, R.id.text_due_date ,R.id.text_no_days_overdue}) {

				};

				return null;
			}

			@Override
			protected void onPostExecute(String string) {
				// TODO Auto-generated method stub

				dbworker.close();

				list_payment_pending.setAdapter(adapter);
				Log.e("gg list view size size", list_payment_pending.getCount()
						+ "");
				super.onPostExecute(string);
			}
		};
		String params[] = {};
		a.execute(params);

	}

	public void move_to_another_fragment(String dealer_name) {

		Fragment fragment = new SelectedPaymentFragment();
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(
				"Deliver Order Detail(" + dealer_name + ")");
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack(null)
				.commit();

	}

}
