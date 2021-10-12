package ceylon.linux.view;

import ceylon.linux.emailgenerator.GMailSender;

import com.example.dimosales.R;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentEmail extends Fragment {

	SharedPreferences user_data;
	private EditText txt_email, txt_password;
	private Button button_save, button_clear, button_test;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.email_fragment, container,
				false);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		user_data = getActivity().getSharedPreferences("USERDATA",
				Context.MODE_PRIVATE);

		getActivity().getActionBar().setTitle("E mail");

		txt_email = (EditText) rootView.findViewById(R.id.txt_email);
		txt_password = (EditText) rootView.findViewById(R.id.txt_password);

		button_save = (Button) rootView.findViewById(R.id.button_save);
		button_clear = (Button) rootView.findViewById(R.id.button_clear);
		button_test = (Button) rootView.findViewById(R.id.button_test);

		if (user_data.contains("email_address")
				&& user_data.contains("email_password")) {
			txt_email.setText(user_data.getString("email_address", ""));
			txt_password.setText(user_data.getString("email_password", ""));

			button_save.setText("UPDATE");
		}

		button_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_email.setText("");
				txt_password.setText("");
			}
		});

		button_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = user_data.edit();
				editor.putString("email_address", txt_email.getText()
						.toString());
				editor.putString("email_password", txt_password.getText()
						.toString());
				editor.apply();
				editor.commit();

			}
		});

		button_test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (user_data.contains("email_address")
						&& user_data.contains("email_password")) {
					mail_send(user_data.getString("email_address", ""),
							user_data.getString("email_password", ""));
				}

			}
		});

		return rootView;
	}

	public void mail_send(final String sendermail, final String password) {
		String[] name = { "aaa" };

		new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {

				GMailSender sender = new GMailSender(sendermail.trim(),
						password);
				try {
					sender.sendMail(
							"Test mail",
							"xxxxxxxxxx\nTEST mail\nxxxxxxxxxxx\n", sendermail.trim(),
							"prasanna@ceylonlinux.lk");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
		}.execute(name);
	}
}
