package ceylon.linux.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ceylon.linux.asynctask.AuthoriseFromServer;
import ceylon.linux.asynctask.DownloadDataFromServer;
import ceylon.linux.utility.Utility;

@SuppressLint("NewApi")
public class Login extends Activity {

	public static boolean isfirsttime = true;
	public static boolean islogged = false;
	public static int USER_ID;
	String username, password;
	Utility utility;
	SharedPreferences userdata;
	private EditText editText_username, editText_password;
	private Button button_login, button_exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		editText_username = (EditText) findViewById(R.id.editText_username);
		editText_password = (EditText) findViewById(R.id.editText_password);
		button_login = (Button) findViewById(R.id.button_login);
		button_exit = (Button) findViewById(R.id.button_exit);
		// new Dbworker(this);

		userdata = getSharedPreferences("USERDATA", Context.MODE_PRIVATE);

		// bypass login step if user has sign in to the sales pad previously
		if (userdata.getString("last_login_date", "").equalsIgnoreCase(
				new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
			Intent intent_home = new Intent(Login.this, HomeActivity.class);
			startActivity(intent_home);
			finish();
		}
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		utility = new Utility(Login.this);

		button_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
	
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Login.this);
				// set title
				alertDialogBuilder.setTitle("Exit");
				// set dialog message
				alertDialogBuilder
						.setMessage("Click yes to exit!")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										System.exit(0);
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});
				// show alert dialog
				alertDialogBuilder.show();
			}
		});

		button_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = editText_username.getText().toString();
				password = editText_password.getText().toString();

				// check for empty fields
				if (username.isEmpty()) {
					Toast.makeText(Login.this, "Please Enter User Name",
							Toast.LENGTH_LONG).show();
					editText_username.requestFocus();
					return;
				}
				if (password.isEmpty()) {
					Toast.makeText(Login.this, "Please Enter Password",
							Toast.LENGTH_LONG).show();
					editText_password.requestFocus();
					return;
				}
				if (utility.isNetworkAvailable()) {
					String[] login_data = { username.trim(), password.trim() };

					AsyncTask<String, Void, Void> logindetails = new AuthoriseFromServer(
							Login.this) {
						@Override
						protected void onPostExecute(Void result) {

							super.onPostExecute(result);
							JSONObject jsonobject = AuthoriseFromServer.login_json;
							JSONObject subJsonObject;
							JSONObject branch_name;

							Log.i("JSON VALUE", jsonobject.toString());
							try {
								if (jsonobject.getString("result").equals("1")) {

									JSONArray subJsonArrayuser = jsonobject
											.getJSONArray("data");

									subJsonObject = subJsonArrayuser
											.getJSONObject(0);

									subJsonObject = subJsonArrayuser
											.getJSONObject(0);

									Editor editor = userdata.edit();
									editor.putString("ID", subJsonObject
											.getString("employee_id"));
									editor.putString("name",
											subJsonObject.getString("name"));
									editor.putString("u_name", username);
									editor.putString("pw", password);

									editor.putString("timestamp", subJsonObject
											.getString("employee_id"));
									editor.putString("last_login_date",
											new SimpleDateFormat("yyyy-MM-dd")
													.format(new Date()));
									editor.putString("branch_name",
											subJsonObject
													.getString("branch_name"));

									editor.putString("timestamp", subJsonObject
											.getString("employee_id"));
									editor.putString("last_login_date",
											new SimpleDateFormat("yyyy-MM-dd")
													.format(new Date()));
									editor.putString("vat",
											subJsonObject.getString("vat"));

									editor.putString(
											"sales_officer_account_no",
											subJsonObject
													.getString("sales_officer_account_no"));
									editor.putString("area_name", subJsonObject
											.getString("area_name"));
									editor.putString("area_code", subJsonObject
											.getString("area_code"));
									editor.putString(
											"area_account_no",
											subJsonObject
													.getString("area_account_no"));

									Log.i("VAT is",
											subJsonObject.getString("vat"));

									editor.commit();

									DownloadDataFromServer download = new DownloadDataFromServer(
											Login.this, false) {
										@Override
										protected void onPostExecute(
												String result) {
											super.onPostExecute(result);
											if (dialog != null
													&& dialog.isShowing()) {
												dialog.dismiss();
											}
											try {
												Intent intent_home = new Intent(
														Login.this,
														HomeActivity.class);
												startActivity(intent_home);
											} catch (Exception e) {

											}

											finish();
										}
									};

									String[] params = { "fdfdf" };
									download.execute(params);

									Log.i("Employee ID",
											"" + userdata.getString("ID", ""));

								} else {
									AlertDialog.Builder builder = new AlertDialog.Builder(
											Login.this);
									builder.setTitle(R.string.login_fail_dialog_title);
									builder.setMessage(R.string.login_fail_dialog_message);
									builder.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													editText_username
															.requestFocus();
												}
											});
									builder.show();
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					logindetails.execute(login_data);
				} else {
					Toast.makeText(getApplicationContext(),
							"No Active Internet Connection Found",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

}
