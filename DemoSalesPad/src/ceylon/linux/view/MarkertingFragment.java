package ceylon.linux.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.db.Dbworker;
import ceylon.linux.fileexplorer.FileChooser;
import ceylon.linux.utility.Utility;

@SuppressLint("NewApi")
public class MarkertingFragment extends Fragment {
	public int id;
	public Double budget1 = 0.0;
	public Double final_budget = 0.0;
	OnClickListener remove2 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ViewGroup layout = (ViewGroup) v.getParent();
			if (null != layout) // for safety only as you are doing onClick

				for (int i = 0; i < layout.getChildCount(); i++) {
					if (layout.getChildAt(i).getTag().equals("total")) {
						EditText total = (EditText) layout.getChildAt(i);
						final_budget = final_budget
							- Double.parseDouble(total.getText().toString());
						show_budget.setText(String.valueOf(final_budget));

					}

				}
			ly1.removeView(layout);

		}
	};
	LinearLayout ly;
	LinearLayout ly1;
	LinearLayout ll;
	LinearLayout ll2;
	Button b1;
	Button b2;
	Button btnQuotations, save;
	Spinner show_markerting_items;
	EditText date_choose;
	EditText other;
	Dbworker dbworker;
	EditText name;
	ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();
	ScrollView scroll;
	SimpleAdapter adapter;
	AutoCompleteTextView cat;
	Double temp;
	TextWatcher textwatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
		                          int count) {
			// TODO Auto-generated method stub
			show_budget.setText(s);

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
		                              int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			temp = 0.0;

			if (s.toString().equals("")) {
				temp = 0.0;

			} else {
				if (ecpy.getText().toString().equals("")) {

					Toast.makeText(getActivity(), "Add Estimated cost first", Toast.LENGTH_LONG).show();

				} else {

					temp = Double.parseDouble(ecpy.getText().toString())
						* Double.parseDouble(s.toString());
					total_ecpu.setText(String.valueOf(temp));
					show_budget.setText(String.valueOf(find_budget()));

				}


			}

		}
	};
	;
	String quotation;
	EditText name_auto;
	EditText invitees, dimo_employees, no_of_employees, ecpy, quantity, total,
		tot, description, total_ecpu, c_to, ince, minimalrequired,
		objective, other_required, location;
	TextView show_budget;
	CustomAutoCompleteTextView2 a_no;
	TextWatcher t = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
		                          int count) {

			int no_of_invitees = 0;
			int no_of_dimo_employees = 0;

			if (invitees.getText().toString().trim().equals("") || invitees.getText() == null) {
				no_of_invitees = 0;

			} else {

				no_of_invitees = Integer.parseInt(invitees.getText().toString());
			}

			if (dimo_employees.getText().toString().trim().equals("") || dimo_employees.getText() == null) {

				no_of_dimo_employees = 0;

			} else {
				no_of_dimo_employees = Integer.parseInt(dimo_employees.getText().toString());

			}

			int total = no_of_invitees + no_of_dimo_employees;
			no_of_employees.setText(String.valueOf(total));

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
		                              int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};
	OnTouchListener ot = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			View focussedView = getActivity().getCurrentFocus();
			if (focussedView != null)
				focussedView.clearFocus();
			return false;
		}
	};
	OnClickListener yy = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ViewGroup viewgroup = (ViewGroup) v.getParent();
			Log.i("1111 st", viewgroup.getChildCount() + " ");
		}
	};
	OnClickListener save_marketing_activities = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Save_Marketing_Activities();

		}
	};
	OnClickListener add = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Add_Item();

		}
	};
	OnClickListener add2 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Add_Item2();

		}
	};
	Calendar myCalendar = Calendar.getInstance();
	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
		                      int dayOfMonth) {
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		}

	};
	OnClickListener choosedate = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new DatePickerDialog(getActivity(), date,
				myCalendar.get(Calendar.YEAR),
				myCalendar.get(Calendar.MONTH),
				myCalendar.get(Calendar.DAY_OF_MONTH)).show();
		}
	};
	OnClickListener remove = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ViewGroup layout = (ViewGroup) v.getParent();
			// Log.i("count", layout.getChildCount() + "");

			if (null != layout) // for safety only as you are doing onClick
				ly.removeView(layout);

		}
	};
	private OnItemClickListener autoItemSelectedListner = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                        long arg3) {
			HashMap<String, String> hm = (HashMap<String, String>) arg0
				.getAdapter().getItem(arg2);

			int id = getActivity().getCurrentFocus().getNextFocusDownId();
			if (id != View.NO_ID) {

				EditText a = (EditText) getActivity().findViewById(id);
				a.setText(hm.get("NAME"));
				EditText ek = (EditText) getActivity().findViewById(id + 1);
				ek.setText(hm.get("current_to"));

			}
		}

	};
	private OnItemClickListener autoItemSelectedListner2 = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                        long arg3) {

			int id = getActivity().getCurrentFocus().getNextFocusDownId();
			if (id != View.NO_ID) {
				Log.i("value", id + "");
			}
		}

	};
	private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
		                           long arg3) {
			Object item = arg0.getItemAtPosition(arg2);
			if (item.toString().equals("other")) {

				other.setVisibility(View.VISIBLE);

			} else {

				other.setVisibility(View.GONE);

			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.markerting_fragment,
			container, false);
		getActivity().setRequestedOrientation(
			ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		dbworker = new Dbworker(getActivity());

		populate();
		// Initializs No of employees
		invitees = (EditText) rootView.findViewById(R.id.invitees);
		invitees.setInputType(InputType.TYPE_CLASS_NUMBER);
		invitees.addTextChangedListener(t);
		dimo_employees = (EditText) rootView.findViewById(R.id.dimo_employees);
		dimo_employees.setInputType(InputType.TYPE_CLASS_NUMBER);
		dimo_employees.addTextChangedListener(t);
		no_of_employees = (EditText) rootView
			.findViewById(R.id.total_employees);
		// end of the initialize

		// Initializing the target Dealer

		String[] from = {"NAME", "ID"};

		int[] to = {R.id.auto_id, R.id.auto_name};
		adapter = new SimpleAdapter(getActivity(), values, R.layout.row, from, to);
		cat = (CustomAutoCompleteTextView2) rootView.findViewById(R.id.a_no);
		cat.setNextFocusDownId(R.id.name);
		cat.setNextFocusUpId(R.id.name);
		cat.setThreshold(1);
		cat.setOnItemClickListener(autoItemSelectedListner);
		cat.setAdapter(adapter);

		name = (EditText) rootView.findViewById(R.id.name);
		c_to = (EditText) rootView.findViewById(R.id.c_to);
		ince = (EditText) rootView.findViewById(R.id.ince);

		b1 = (Button) rootView.findViewById(R.id.add_button);
		b1.setOnClickListener(add);

		// End of initializing the target Dealer

		// Initialize Edit text in Description for marketing activities

		description = (EditText) rootView.findViewById(R.id.description);
		ecpy = (EditText) rootView.findViewById(R.id.ecpu);
		ecpy.setId(200 + (++id));
		ecpy.setInputType(InputType.TYPE_CLASS_NUMBER);

		quantity = (EditText) rootView.findViewById(R.id.qty);
		quantity.setId(200 + (++id));
		quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
		quantity.addTextChangedListener(textwatcher);

		total_ecpu = (EditText) rootView.findViewById(R.id.total);
		total_ecpu.setId(200 + (++id));
		total_ecpu.setInputType(InputType.TYPE_CLASS_NUMBER);

		b2 = (Button) rootView.findViewById(R.id.add_button2);
		b2.setOnClickListener(add2);

		// End of the initializing Description for marketing activities

		// initializing the Spinner with data

		ArrayList<String> spinnerValues = new ArrayList<String>();

		spinnerValues.add("Dealer Dinner");
		spinnerValues.add("Store Arrangement");
		spinnerValues.add("Garage Mechanic Meet");
		spinnerValues.add("Street Promotion");
		spinnerValues.add("Fleet owner get-together");
		spinnerValues.add("Store Arrangement");
		spinnerValues.add("Garage Branding");

		spinnerValues.add("other");
		show_markerting_items = (Spinner) rootView
			.findViewById(R.id.markerting_items);
		show_markerting_items.setOnItemSelectedListener(itemSelectedListener);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
			getActivity(), android.R.layout.simple_spinner_item,
			spinnerValues);
		dataAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		show_markerting_items.setAdapter(dataAdapter);

		// end of the initializing

		// initialize save button
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////
		save = (Button) rootView.findViewById(R.id.save);
		save.setOnClickListener(save_marketing_activities);
		// end of the initializing

		// Initializing layout
		ly = (LinearLayout) rootView.findViewById(R.id.linearMain2);
		ly1 = (LinearLayout) rootView.findViewById(R.id.linearMain3);

		scroll = (ScrollView) rootView.findViewById(R.id.scrollView1);
		scroll.setOnTouchListener(ot);
		// end of the initializing

		other = (EditText) rootView.findViewById(R.id.other);
		other.setVisibility(View.GONE);

		// initialize marketing activity edit texts
		minimalrequired = (EditText) rootView
			.findViewById(R.id.minimalrequired);
		objective = (EditText) rootView.findViewById(R.id.objective);
		other_required = (EditText) rootView.findViewById(R.id.other_required);
		location = (EditText) rootView.findViewById(R.id.location);
		date_choose = (EditText) rootView.findViewById(R.id.date_chooser);
		date_choose.setInputType(InputType.TYPE_NULL);
		date_choose.setOnClickListener(choosedate);

		show_budget = (TextView) rootView.findViewById(R.id.budget);

		// end of the initialize

		btnQuotations = (Button) rootView.findViewById(R.id.quotations);
		btnQuotations.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnQuotationsClicked(v);
			}
		});
		return rootView;
	}

	private void btnQuotationsClicked(View v) {
		Intent intent = new Intent(this.getActivity(), FileChooser.class);
		startActivityForResult(intent, 0);
	}

	public void Add_Item() {
		int next_id;
		int previous_id;
		int currnt_id;

		ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		Button product = new Button(getActivity());
		product.setText("-");
		product.setPadding(10, 10, 10, 10);
		product.setWidth(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		product.setTag("ID");
		product.setOnClickListener(remove);
		ll.addView(product);

		a_no = new CustomAutoCompleteTextView2(getActivity(), null);
		a_no.setAdapter(adapter);
		a_no.setHint("Account No");
		a_no.setTag("account_no");
		a_no.setEms(10);
		a_no.setId(100 + (++id));
		currnt_id = a_no.getId();
		a_no.setNextFocusDownId(++currnt_id);
		a_no.setNextFocusUpId(++currnt_id);
		a_no.setThreshold(1);

		a_no.setPadding(10, 10, 10, 10);
		a_no.setOnItemClickListener(autoItemSelectedListner);
		ll.addView(a_no);

		name = new EditText(getActivity());
		name.setId(100 + (++id));
		name.setHint("Name");
		currnt_id = name.getId();
		name.setPadding(10, 10, 10, 10);
		name.setEms(10);
		name.setTag("name");
		ll.addView(name);

		EditText c_to = new EditText(getActivity());
		c_to.setId(100 + (++id));
		c_to.setTag("current_to");
		c_to.setHint("Current T/o");
		c_to.setPadding(10, 10, 10, 10);
		c_to.setEms(10);
		ll.addView(c_to);

		EditText ince = new EditText(getActivity());
		ince.setId(3);

		ince.setTag("ince");
		ince.setHint("Expected increase after three months");
		ince.setPadding(10, 10, 10, 10);
		ince.setEms(18);

		ll.addView(ince);
		ly.addView(ll);

	}

	public void Add_Item2() {

		ll2 = new LinearLayout(getActivity());
		ll2.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		Button product = new Button(getActivity());
		product.setText("-");
		product.setPadding(10, 10, 10, 10);
		product.setWidth(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		product.setOnClickListener(remove2);
		product.setTag("b2");
		ll2.addView(product);

		EditText description = new EditText(getActivity());

		description.setHint("Description");
		description.setEms(10);
		description.setTag("description");
		description.setPadding(10, 10, 10, 10);
		ll2.addView(description);

		TextView ecpu = new EditText(getActivity());
		ecpu.setId(200 + (++id));
		ecpu.setHint("Estimated Cost Per Unit");
		ecpu.setInputType(InputType.TYPE_CLASS_NUMBER);
		ecpu.setPadding(10, 10, 10, 10);
		ecpu.setTag("ecpy");
		ecpu.setEms(10);
		ll2.addView(ecpu);

		EditText qty = new EditText(getActivity());
		qty.setId(200 + (++id));
		qty.setHint("Quantity");
		qty.setTag("qty");
		qty.setInputType(InputType.TYPE_CLASS_NUMBER);
		qty.addTextChangedListener(new CustomTextWatcher(qty));
		qty.setPadding(10, 10, 10, 10);
		qty.setEms(10);
		ll2.addView(qty);

		tot = new EditText(getActivity());
		tot.setId(200 + (++id));
		tot.setHint("Total");
		tot.setTag("total");
		tot.setPadding(10, 10, 10, 10);
		tot.setEms(18);
		ll2.addView(tot);
		ly1.addView(ll2);

	}

	public void populate() {

		Cursor cursor1 = dbworker.get_all_dealers();

		if (cursor1 != null) {
			cursor1.moveToFirst();
			while (!cursor1.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("NAME", cursor1.getString(3));
				temp.put("ID", String.valueOf(cursor1.getString(2)));
				temp.put("current_to", String.valueOf(cursor1.getString(11)));
				values.add(temp);
				cursor1.moveToNext();
			}

		}
		cursor1.close();

	}

	void updateLabel() {
		String myFormat = "MM-dd-yy"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		date_choose.setText(sdf.format(myCalendar.getTime()));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			getActivity();
			if (resultCode == Activity.RESULT_OK) {
				String curFileName = data.getStringExtra("absolutePath");
				Utility.createDirectoryIfNeeded("marketing_activity");
				Utility.copy(curFileName, "marketing_activity", Utility.timestamp_creater());
				quotation = curFileName;
				//System.out.println(curFileName);
			}
		}
		Toast.makeText(getActivity(), "It is working", Toast.LENGTH_LONG).show();
	}

	public void Save_Marketing_Activities() {

		String campaigntype = null;

		if (show_markerting_items.getSelectedItem().equals("other")) {
			campaigntype = other.getText().toString();
		} else {
			campaigntype = show_markerting_items.getSelectedItem().toString();

		}

		Long ID = dbworker.insert_marketing_activities(campaigntype, date_choose.getText().toString(), objective.getText().toString(), minimalrequired.getText().toString(), other_required.getText().toString(), location.getText().toString(), invitees.getText().toString(), dimo_employees.getText().toString(), no_of_employees.getText().toString(), quotation);

		dbworker.insert_targeet_dealer_for_marketing_activities(cat.getText().toString(), c_to.getText().toString(), ince.getText().toString(), String.valueOf(ID));
		//	dbworker.insert_description_for_marketing_activities(description.getTag().toString(),ecpy.getText().toString(), quantity.getText().toString(), total_ecpu.getText().toString(), String.valueOf(ID));

		ViewGroup view_group = ly;
		for (int i = 0; i < view_group.getChildCount(); i++) {
			ViewGroup vv = (ViewGroup) view_group.getChildAt(i);
			String a_no = null;
			String current_to = null;
			String ince = null;
			for (int j = 0; j < vv.getChildCount(); j++) {

				if (vv.getChildAt(j).getTag().toString().equals("account_no")) {
					CustomAutoCompleteTextView2 a = (CustomAutoCompleteTextView2) vv
						.getChildAt(j);

					a_no = a.getText().toString();
					Log.i("account_no", "Description is" + a_no);
				}
				if (vv.getChildAt(j).getTag().toString().equals("current_to")) {
					EditText e = (EditText) vv.getChildAt(j);
					current_to = e.getText().toString();
					Log.i("current_to", "current_to is" + current_to);
				}

				if (vv.getChildAt(j).getTag().toString().equals("ince")) {
					EditText e = (EditText) vv.getChildAt(j);
					ince = e.getText().toString();
					Log.i("ince", "current_to is" + ince);
				}

			}


			dbworker.insert_targeet_dealer_for_marketing_activities(a_no, current_to, ince, String.valueOf(ID));
		}

		ViewGroup view_group2 = ly1;
		for (int k = 0; k < view_group2.getChildCount(); k++) {
			ViewGroup vv3 = (ViewGroup) view_group2.getChildAt(k);
			String description = null;
			String ecpu = null;
			String qty = null;
			String total = null;

			for (int l = 0; l < vv3.getChildCount(); l++) {
				if (vv3.getChildAt(l).getTag().toString().equals("description")) {
					EditText e = (EditText) vv3.getChildAt(l);
					description = e.getText().toString();

				}
				if (vv3.getChildAt(l).getTag().toString().equals("ecpy")) {
					EditText e1 = (EditText) vv3.getChildAt(l);
					ecpu = e1.getText().toString();
				}

				if (vv3.getChildAt(l).getTag().toString().equals("qty")) {
					EditText e2 = (EditText) vv3.getChildAt(l);

					qty = e2.getText().toString();

				}
				if (vv3.getChildAt(l).getTag().toString().equals("total")) {

					EditText e3 = (EditText) vv3.getChildAt(l);
					total = e3.getText().toString();
				}

			}

			dbworker.insert_description_for_marketing_activities(description, ecpu, qty, total, String.valueOf(ID));

		}

		Toast.makeText(getActivity(), "Data Saved Sucessfully", Toast.LENGTH_LONG).show();

	}

	public Double find_budget() {
		final_budget = 0.0;
		return final_budget = budget1 + temp;

	}

	private class CustomTextWatcher implements TextWatcher {
		private EditText mEditText;

		public CustomTextWatcher(EditText e) {
			mEditText = e;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
		                              int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
		                          int count) {
			int id = mEditText.getId();
			budget1 = 0.0;

			EditText e1 = (EditText) getActivity().findViewById(--id);
			EditText e2 = (EditText) getActivity().findViewById(++id);
			EditText e3 = (EditText) getActivity().findViewById(++id);

			int quantity = 0;
			Integer ecpu = 0;

			if (e1.getText().toString().trim().equals("")
				|| e1.getText() == null) {
				Toast.makeText(getActivity(), "Add Estimated Quantity first",
					Toast.LENGTH_LONG).show();
				ecpu = 0;
			} else {

				ecpu = Integer.parseInt(e1.getText().toString());
			}
			if (e2.getText().toString().trim().equals("")
				|| e2.getText() == null) {
				quantity = 0;
			} else

				quantity = Integer.parseInt(e2.getText().toString());
			e3.setText(String.valueOf(ecpu * quantity));

			for (int i = 0; i < ly1.getChildCount(); i++) {
				LinearLayout ll = (LinearLayout) ly1.getChildAt(i);
				for (int j = 0; j < ll.getChildCount(); j++) {
					if (ll.getChildAt(j).getTag().toString().equals("total")) {

						EditText e = (EditText) ll.getChildAt(j);

						if (e.getText().equals("")) {

						} else {

							budget1 = budget1
								+ Integer.parseInt(e.getText().toString());
						}

					}

				}

			}

			show_budget.setText(String.valueOf(find_budget()));

		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	}

}
