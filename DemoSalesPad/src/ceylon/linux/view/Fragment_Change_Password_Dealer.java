package ceylon.linux.view;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.db.Dbworker;
import ceylon.linux.url.URLS;

import com.example.dimosales.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Fragment_Change_Password_Dealer extends Fragment {
	private EditText edit_txt_old_passWord, edit_txt_new_password,
			edit_txt_confirm_new_password;

	private Button button_submit, button_sync;

	String oldPassword;

	Dbworker dbworker;
	private Jsonhelper jh;

	private static boolean iSvalid = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater
				.inflate(R.layout.change_password, container, false);

		edit_txt_old_passWord = (EditText) root
				.findViewById(R.id.edit_txt_old_passWord);
		edit_txt_new_password = (EditText) root
				.findViewById(R.id.edit_txt_new_password);
		edit_txt_confirm_new_password = (EditText) root
				.findViewById(R.id.edit_txt_confirm_new_password);

		button_submit = (Button) root.findViewById(R.id.button_submit);
		button_sync = (Button) root.findViewById(R.id.button_sync);

		oldPassword = "";

		dbworker = new Dbworker(getActivity());
		jh= new Jsonhelper();

		Cursor cursor = dbworker
				.get_dealer_by_id(Fragment_Dealer_Select_change_password.dealer_id);

		if (cursor.moveToFirst()) {
			oldPassword = cursor.getString(8);
		}
		cursor.close();

		edit_txt_old_passWord.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.toString().equals(oldPassword)) {
					edit_txt_old_passWord.setBackgroundColor(Color
							.parseColor("#99ff99"));
				}
				else
				{
					edit_txt_old_passWord.setBackgroundColor(Color
							.parseColor("#ff6666"));
				}
			}
		});

		edit_txt_confirm_new_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.toString().equals(
						edit_txt_new_password.getText().toString())) {
					edit_txt_confirm_new_password.setBackgroundColor(Color
							.parseColor("#99ff99"));
				}
				else
				{
					edit_txt_confirm_new_password.setBackgroundColor(Color
							.parseColor("#ff6666"));
				}
			}
		});

		button_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				iSvalid = true;
				if (!edit_txt_old_passWord.getText().toString()
						.equals(oldPassword)) {
					edit_txt_old_passWord.setBackgroundColor(Color
							.parseColor("#ff6666"));
					iSvalid = false;

					Toast.makeText(getActivity(), "Password is wrong",
							Toast.LENGTH_SHORT).show();
				}

				if (!edit_txt_confirm_new_password.getText().toString()
						.equals(edit_txt_new_password.getText().toString())) {
					edit_txt_confirm_new_password.setBackgroundColor(Color
							.parseColor("#ff6666"));
					iSvalid = false;
					Toast.makeText(getActivity(), "does not match,type again",
							Toast.LENGTH_SHORT).show();
				}

				if (iSvalid) {
					dbworker.update_dealer_password(
							Fragment_Dealer_Select_change_password.dealer_id,
							edit_txt_confirm_new_password.getText().toString()
									.trim());
				}
			}
		});

		button_sync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				sync_details();

			}
		});

		return root;
	}
	
	private void sync_details()
	{
		AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>()
		{
			private ProgressDialog dialog= new ProgressDialog(getActivity());

			@Override
			protected void onPreExecute() {
				
				super.onPreExecute();
				dialog = ProgressDialog.show(getActivity(), "",
						"Please wait Upload Data to server... ", false);
				dialog.show();
			}
			@Override
			protected String doInBackground(String... params) {
				get_all_non_synchronized_dealers_locations();
				return null;
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (null != dialog && dialog.isShowing()) {

					try {
						dialog.dismiss();
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			
				button_submit.setEnabled(false);
				move_to_another_fragment(new Fragment_Dealer_Select_change_password(), "Change Passwords");
			}
			
		};
		String params[]={""};
		
		asyncTask.execute(params);
		
		
		
	}
	
	
	public void get_all_non_synchronized_dealers_locations() {
		Cursor cursor = dbworker.get_all_non_synchronized_dealers_locations();
		if (cursor.moveToFirst()) {
			do {
				JSONObject jsonObject = new JSONObject();

				try {
					jsonObject.put("lat", cursor.getString(1));
					jsonObject.put("long", cursor.getString(2));
					jsonObject.put("dealer_password", cursor.getString(3));

					JSONObject json_tree = new JSONObject();

					json_tree.put("data", jsonObject);

					String[] parameters = { "dealer_id", "json_string" };
					String[] data = { cursor.getString(0), json_tree.toString() };
					HttpResponse respons = jh.callServer(
							URLS.update_dealer_location, parameters, data);

					String string_response = jh.responsDataConvertor(respons);

					Log.e("response", string_response);

					JSONObject json_response = new JSONObject(string_response);
					if (json_response.getString("result").equals("1")) {
						dbworker.update_sync_status_by_dealer_id(cursor
								.getString(0));

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	
	public void move_to_another_fragment(Fragment f, String name) {

		
		
		
		Fragment fragment = f;
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(name);
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack("item")
				.commit();
	}
}
