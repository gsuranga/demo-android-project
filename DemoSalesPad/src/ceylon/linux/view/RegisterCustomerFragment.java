package ceylon.linux.view;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ceylon.linux.db.Dbworker;

@SuppressLint("NewApi")
public class RegisterCustomerFragment extends Fragment {
	EditText customer_name;
	EditText c_address;
	EditText officer;
	EditText contact_no;
	Button save_customer;
	TableLayout tl;
	SharedPreferences userdata;
	Dbworker dbworker;
	long customer_id;
	OnClickListener save = new OnClickListener() {

		@Override
		public void onClick(View v) {

			customer_id = dbworker.save_customer(customer_name.getText()
				.toString(), c_address.getText().toString(), contact_no
				.getText().toString());

			ViewGroup vv = tl;

			for (int i = 0; i < tl.getChildCount(); i++) {

				ViewGroup vvv = (ViewGroup) tl.getChildAt(i);
				for (int j = 0; j < vvv.getChildCount(); j++) {
					View vvvv = vvv.getChildAt(j);

					if (vvvv instanceof EditText) {

						Log.i("edit text", "Edit text");
						if (vvvv.getTag().toString().equals("v_no")) {

							EditText e = (EditText) vvvv;

							dbworker.save_vehicale(e.getText().toString(),
								String.valueOf(customer_id), "jhihg");

						}

					}

				}

				while (tl.getChildCount() > 1)
					tl.removeView(tl.getChildAt(tl.getChildCount() - 1));
			}

			ViewGroup vvvvv = (ViewGroup) tl.getChildAt(0);
			Log.i("count is", "" + vvvvv.getChildCount());

			for (int m = 0; m < vvvvv.getChildCount(); m++) {

				if (vvvvv.getChildAt(m) instanceof EditText) {
					if (vvvvv.getChildAt(m).getTag().equals("v_no")) {

						EditText ee = (EditText) vvvvv.getChildAt(m);
						ee.setText("");

					}

				}
			}

		}
	};
	int count;
	OnClickListener add = new OnClickListener() {

		@Override
		public void onClick(View v) {
			populate();
		}
	};
	OnClickListener remove = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ViewGroup vv = (ViewGroup) v.getParent();
			vv.removeAllViews();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.customer_fragement,
			container, false);
		dbworker = new Dbworker(getActivity());
		tl = (TableLayout) rootView.findViewById(R.id.newly_added_customers);
		customer_name = (EditText) rootView.findViewById(R.id.c_name);
		c_address = (EditText) rootView.findViewById(R.id.c_address);
		officer = (EditText) rootView.findViewById(R.id.officer);
		contact_no = (EditText) rootView.findViewById(R.id.contact_no);
		save_customer = (Button) rootView.findViewById(R.id.save_customer);
		userdata = getActivity().getSharedPreferences("USERDATA",
			getActivity().MODE_PRIVATE);
		TableRow tr_head = new TableRow(getActivity());
		tr_head.setId(10);
		tr_head.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
			LayoutParams.WRAP_CONTENT));

		officer.setText(userdata.getString("name", ""));

		Button b_add = new Button(getActivity());
		b_add.setId(200);
		b_add.setText("+");
		b_add.setTextColor(Color.RED);

		b_add.setGravity(Gravity.CENTER);
		b_add.setPadding(2, 2, 2, 2);
		b_add.setOnClickListener(add);
		tr_head.addView(b_add);

		EditText v_no = new EditText(getActivity());
		v_no.setId(20);
		v_no.setTextColor(Color.RED);
		v_no.setGravity(Gravity.CENTER);
		v_no.setHint("Vehicale NO");
		v_no.setPadding(2, 2, 2, 2);
		v_no.setTag("v_no");
		tr_head.addView(v_no);

		tl.addView(tr_head);
		save_customer.setOnClickListener(save);

		return rootView;

	}

	public void populate() {

		TableRow tr = new TableRow(getActivity());

		tr.setId(100 + count);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
			LayoutParams.WRAP_CONTENT));

		Button b_add = new Button(getActivity());
		b_add.setId(20);
		b_add.setText("+");
		b_add.setTextColor(Color.RED);
		b_add.setGravity(Gravity.CENTER);
		b_add.setPadding(2, 2, 2, 2);
		b_add.setOnClickListener(add);

		tr.addView(b_add);

		TextView v_no = new EditText(getActivity());
		v_no.setId(200 + count);
		v_no.setHint("Vehicale NO");
		v_no.setTag("adress");
		v_no.setTag("v_no");
		v_no.setTextColor(Color.BLACK);
		v_no.setGravity(Gravity.CENTER);
		tr.addView(v_no);

		Button b_minus = new Button(getActivity());
		b_minus.setId(20);
		b_minus.setText("-");
		b_minus.setTextColor(Color.RED);
		b_minus.setGravity(Gravity.CENTER);
		b_minus.setPadding(2, 2, 2, 2);
		b_minus.setOnClickListener(remove);
		tr.addView(b_minus);
		// finally add this to the table row
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
			LayoutParams.WRAP_CONTENT));
		count++;
	}

}
