package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import ceylon.linux.db.Dbworker;

public class NewTourItenaryFragment extends Fragment {

	private Spinner spinner_category, spinner_purpose;
	ArrayList<HashMap<String, String>> categories, purposes, dealers;
	HashMap<String, String> purpose, category;
	private Button btn_reset, btn_start;
	private AutoCompleteTextView autoCompleteTextView1;
	private EditText editText_otherDtl;
	private Dbworker dbworker;
	private static String dealer_name = "";
	public static String dealer_id = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_new_tour_itenary,
				container, false);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		dbworker = new Dbworker(getActivity());

		spinner_category = (Spinner) rootView
				.findViewById(R.id.spinner_category);
		spinner_purpose = (Spinner) rootView.findViewById(R.id.spinner_purpose);

		categories = new ArrayList<HashMap<String, String>>();
		purposes = new ArrayList<HashMap<String, String>>();
		dealers = new ArrayList<HashMap<String, String>>();

		btn_reset = (Button) rootView.findViewById(R.id.btn_reset);
		btn_start = (Button) rootView.findViewById(R.id.btn_start);

		editText_otherDtl = (EditText) rootView
				.findViewById(R.id.editText_otherDtl);

		autoCompleteTextView1 = (AutoCompleteTextView) rootView
				.findViewById(R.id.autoCompleteTextView1);

		fill_array_lists();

		ArrayAdapter<HashMap<String, String>> adapter_category = new ArrayAdapter<HashMap<String, String>>(
				getActivity(), android.R.layout.simple_spinner_item, categories);
		adapter_category
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<HashMap<String, String>> adapter_purpose = new ArrayAdapter<HashMap<String, String>>(
				getActivity(), android.R.layout.simple_spinner_item, purposes);
		adapter_purpose
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_category.setAdapter(adapter_category);
		spinner_purpose.setAdapter(adapter_purpose);

		spinner_category
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						load_outlet_autocomplete(arg2);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		spinner_purpose.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		btn_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editText_otherDtl.setText("");

				autoCompleteTextView1.setText("");
				spinner_category.setSelection(0);
				spinner_purpose.setSelection(0);
			}
		});

		btn_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (spinner_category.getSelectedItemPosition() == 0
						|| spinner_purpose.getSelectedItemPosition() == 0
						|| autoCompleteTextView1.getText().equals("")) {
					Toast.makeText(getActivity(), "Fill Mandatory Fields",
							Toast.LENGTH_SHORT).show();
				} else {
					java.util.Date date = new java.util.Date();
					java.text.SimpleDateFormat dateFormatter = new java.text.SimpleDateFormat(
							"yyyy-MM-dd");
					java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(
							"HH:mm:ss");

					if (spinner_purpose.getSelectedItemPosition() == 1) {
						moveToFragment(new ManagePurchaseOrders());
					} else if (spinner_purpose.getSelectedItemPosition() == 2) {
						moveToFragment(new FailurePartFragment());
					} else if (spinner_purpose.getSelectedItemPosition() == 3) {
						moveToFragment(new PaymentFragment());
					} else if (spinner_purpose.getSelectedItemPosition() == 4) {
					//	moveToFragment(new ManageOrderFragment());
					} else if (spinner_purpose.getSelectedItemPosition() == 5) {
						
					} else if (spinner_purpose.getSelectedItemPosition() == 6) {
						moveToFragment(new CompetitorPartsFragment());
					}

					dbworker.insert_tour_itenary(Integer.parseInt(dealer_id),
							dealer_name, spinner_category.getSelectedItem()
									.toString(), spinner_purpose
									.getSelectedItem().toString(),
							timeFormatter.format(date), dateFormatter
									.format(date), editText_otherDtl.getText()
									.toString());
				}

			}
		});
		return rootView;
	}

	@SuppressWarnings("serial")
	private void fill_array_lists() {
		purpose = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		purpose.put("value", "0");
		purpose.put("name", "--------select--------");
		purposes.add(purpose);
		purpose = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		purpose.put("value", "1");
		purpose.put("name", "Order");
		purposes.add(purpose);
		purpose = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		purpose.put("value", "2");
		purpose.put("name", "Return");
		purposes.add(purpose);
		purpose = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		purpose.put("value", "3");
		purpose.put("name", "Collection");
		purposes.add(purpose);
		purpose = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		purpose.put("value", "4");
		purpose.put("name", "Branding");
		purposes.add(purpose);
		purpose = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		purpose.put("value", "5");
		purpose.put("name", "Visit Only");
		purposes.add(purpose);
		purpose = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		purpose.put("value", "6");
		purpose.put("name", "Market Info");
		purposes.add(purpose);

		category = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		category.put("value", "0");
		category.put("name", "--------select--------");
		categories.add(category);
		category = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		category.put("value", "1");
		category.put("name", "Dealer");
		categories.add(category);
		category = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		category.put("value", "2");
		category.put("name", "Garage");
		categories.add(category);
		category = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		category.put("value", "3");
		category.put("name", "Fleet Owner");
		categories.add(category);
		category = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		category.put("value", "4");
		category.put("name", "New Shop");
		categories.add(category);
		category = new HashMap<String, String>() {
			@Override
			public String toString() {

				return super.get("name");
			}
		};
		category.put("value", "5");
		category.put("name", "Vehicle Owner");
		categories.add(category);

	}

	private void load_outlet_autocomplete(int index) {
		Cursor cur_outlets;
		switch (index) {
		case 0:
			cur_outlets = null;
			break;

		case 1:
			cur_outlets = dbworker.get_all_dealers();
			break;
		case 2:
			cur_outlets = dbworker.get_all_garages();
			break;
		case 3:
			cur_outlets = dbworker.get_all_fleet_vehicales();
			break;
		default:
			cur_outlets = dbworker.get_all_dealers();
			break;
		}

		if (cur_outlets != null) {
			if (cur_outlets.moveToFirst()) {
				do {
					@SuppressWarnings("serial")
					HashMap<String, String> dealer = new HashMap<String, String>() {
						@Override
						public String toString() {

							return super.get("dealer_name");
						}
					};

					if (index == 1) {
						dealer.put("id", cur_outlets.getString(1));
						dealer.put("dealer_name", cur_outlets.getString(3));
						HomeFragment.dealer_account_no = cur_outlets
								.getString(2);
						HomeFragment.discount_percentage = cur_outlets
								.getString(6);
						HomeFragment.dealer_name = cur_outlets.getString(3);
						dealer_name = cur_outlets.getString(3);

						HomeFragment.overdue_amount = cur_outlets.getString(13);
						HomeFragment.outstanding_amount = cur_outlets
								.getString(12);
						HomeFragment.credit_limit = cur_outlets.getString(11);

					} else if (index == 2) {
						dealer.put("id", cur_outlets.getString(0));
						dealer.put("dealer_name", cur_outlets.getString(1));
					} else {
						dealer.put("id", cur_outlets.getString(1));
						dealer.put("dealer_name", cur_outlets.getString(3));
					}
					dealers.add(dealer);
					Log.i("xxx", cur_outlets.getString(3));

				} while (cur_outlets.moveToNext());
			}

			cur_outlets.close();
		} else {
			if (!dealers.isEmpty()) {
				dealers.clear();
			}
		}
		SimpleAdapter autocomplete = new SimpleAdapter(getActivity(), dealers,
				R.layout.list_view_1uto, new String[] { "dealer_name" },
				new int[] { R.id.Name });

		autoCompleteTextView1.setThreshold(1);

		autoCompleteTextView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> a = (HashMap<String, String>) arg0
						.getAdapter().getItem(arg2);
				HomeFragment.dealer_id = a.get("id");
				dealer_id = a.get("id");
				dealer_name = a.get("dealer_name");
			}

		});
		autoCompleteTextView1.setAdapter(autocomplete);
	}

	private void moveToFragment(Fragment fragment) {

		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(
				"Invoice Information(" + dealer_name + ")");
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack(null)
				.commit();

		ViewSavedOrdersFragment.purchase_order_edit = 0;

	}
}
