package ceylon.linux.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.url.URLS;

public class AuthoriseFromServer extends AsyncTask<String, Void, Void> {
	public static JSONObject login_json;
	public ProgressDialog dialog;
	private Jsonhelper jh;
	private ArrayList<NameValuePair> nameValuePairs;
	private Context context;
	public AuthoriseFromServer(Context cxt) {
		context = cxt;
	}

	@Override
	protected Void doInBackground(String... params) {

		nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("username", params[0]));
		nameValuePairs.add(new BasicNameValuePair("password", params[1]));

		login_json = checkAuthoFromServer(nameValuePairs);
		System.out.println(login_json.toString());
		return null;

	}

	@Override
	protected void onPostExecute(Void result) {

		super.onPostExecute(result);
		
		try {
			dialog.dismiss();
		} catch (Exception e) {
			
		}
	
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = new ProgressDialog(context);
		dialog.setMessage("Authorizing from server..............");
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public JSONObject checkAuthoFromServer(
			ArrayList<NameValuePair> namevaluePair) {
		jh = new Jsonhelper();
		return jh.JsonObjectSendToServerPostWithNameValuePare(
				URLS.server_authorization, namevaluePair);

	}
}
