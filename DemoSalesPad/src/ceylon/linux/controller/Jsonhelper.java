package ceylon.linux.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Jsonhelper {
	private static final int TIMEOUT_MILLISEC = 10000;
	private InputStream content;
	private JSONObject jsonObject;
	private StringBuilder builder;
	private HttpPost httpPost;
	private HttpGet httpGet;
	private HttpResponse response;
	private StatusLine statusLine;
	private DefaultHttpClient httpc;
	private HttpClient httpclient;
	private HttpParams httpParams;

	// Read Remote Json using GET And Return String value
	public String json_Read_URL_ReturnString(String url, String methodType) {

		if ((!methodType.toLowerCase().equals("post"))
				&& (!methodType.toLowerCase().equals("get"))) {
			Log.e("Error ", ":- Invalid Method Use Only GET OR POST");
			return "Error :- Invalid Method Use Only GET OR POST";
		}

		DefaultHttpClient httpc = new DefaultHttpClient();
		HttpUriRequest method = null;

		if (methodType.toLowerCase().equals("post")) {
			httpPost = new HttpPost(url);
			method = httpPost;
		} else if (methodType.toLowerCase().equals("get")) {
			httpGet = new HttpGet(url);
			method = httpGet;
		}

		try {
			response = httpc.execute(method);

			statusLine = response.getStatusLine();

			int statusCode = statusLine.getStatusCode();
			Log.e(Jsonhelper.class.toString(), "MYTest" + statusCode);

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				content = entity.getContent();
				builder = new StringBuilder();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				try {
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					content.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Log.e(Jsonhelper.class.toString(), "Error statusCode Not 200");
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return builder.toString();

	}

	public String send_Json(String url, JSONObject json, String userTken) {
		
		String res="{\"result\":\"false\"}";

		HttpParams httpParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpClient client = new DefaultHttpClient(httpParams);

		HttpPost request = new HttpPost(url);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("officer_id", userTken));
		nameValuePairs.add(new BasicNameValuePair("data", json.toString()));
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(request);
		//	HttpEntity entity = response.getEntity();
			res= responsDataConvertor(response);

		//	res =responsDataConvertor(response);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	public JSONObject json_Read_URL_ReturnJsonObject(String url,
			String methodType) {

		if ((!methodType.toLowerCase().equals("post"))
				&& (!methodType.toLowerCase().equals("get"))) {
			Log.e("Error ", ":- Invalid Method Use Only GET OR POST");
			try {
				return new JSONObject(":- Invalid Method Use Only GET OR POST");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		httpc = new DefaultHttpClient();
		HttpUriRequest method = null;

		if (methodType.toLowerCase().equals("post")) {
			httpPost = new HttpPost(url);
			method = httpPost;
		} else if (methodType.toLowerCase().equals("get")) {
			httpGet = new HttpGet(url);
			method = httpGet;
		}

		try {
			response = httpc.execute(method);
			statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				content = entity.getContent();
				builder = new StringBuilder();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				try {
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					content.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					jsonObject = new JSONObject(builder.toString());
				} catch (JSONException e) {
					Log.e("JSON Parser", "Error parsing data " + e.toString());
				}

			} else {
				Log.e(Jsonhelper.class.toString(), "Error statusCode Not 200");
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;

	}

	public void JsonStringToJsonObject(String jsonString) {
		try {
			JSONArray jsonarray = new JSONArray(jsonString);
			Log.i(Jsonhelper.class.getName() + " JsonStringToJsonObject",
					"Number of entries " + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonObject = jsonarray.getJSONObject(i);
				// Log.i(Jsonhelper.class.getName()+" JsonStringToJsonObject","");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void buildJsonObject() {
		JSONObject object = new JSONObject();
		try {
			object.put("name", "Jack Hack");
			object.put("score", new Integer(200));
			object.put("current", new Double(152.32));
			object.put("nickname", "Hacker");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(object);
	}

	public JSONArray JsonObjectToJsonArray(JSONObject jsonObject) {
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(jsonObject);
		return jsonArray.put(jsonObject);
	}

	/*
	 * can call server with data and get response
	 */

	public JSONObject JsonObjectSendToServerPostWithNameValuePare(String url,
			ArrayList<NameValuePair> data) {

		try {

			int TIMEOUT_MILLISEC = 20000; // = 10 seconds
			httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

			httpclient = new DefaultHttpClient();
			httpPost = new HttpPost(url);
			// httpPost.addHeader("Accept-Encoding", "gzip");
			httpPost.setEntity(new UrlEncodedFormEntity(data));
			HttpResponse response = httpclient.execute(httpPost);
			Log.i("JsonObjectSendToServerWithNameValuePare",
					response.toString());
			// Could do something better with response.
			statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				content = entity.getContent();
				builder = new StringBuilder();
				// BufferedReader reader = new BufferedReader(
				// new InputStreamReader(new GZIPInputStream(content)));
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				try {
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					content.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					jsonObject = new JSONObject(builder.toString());
				} catch (JSONException e) {
					Log.e("JSON Parser", "Error parsing data " + e.toString());
				}

			}
		} catch (Exception e) {
			Log.e("Error", "Error:  " + e.toString());
		}

		return jsonObject;
	}

	public InputStream retrieveStream_post(String url,ArrayList<NameValuePair> data) {

		DefaultHttpClient client = new DefaultHttpClient();

		HttpPost postRequest = new HttpPost(url);
		try {
			postRequest.setEntity(new UrlEncodedFormEntity(data));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			HttpResponse postResponse = client.execute(postRequest);

			final int statusCode = postResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {

				Log.w(getClass().getSimpleName(),

				"Error " + statusCode + " for URL " + url);
				return null;

			}

			HttpEntity getResponseEntity = postResponse.getEntity();

			return getResponseEntity.getContent();

		}

		catch (IOException e) {

			postRequest.abort();

			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);

		}

		return null;

	}

	public HttpResponse callServer(String url, String[] parameters,
			String[] data) {
		httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			for (int i = 0; i < parameters.length; i++) {
				nameValuePairs.add(new BasicNameValuePair(parameters[i],
						data[i]));
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			return response;

		} catch (Exception e) {
			e.printStackTrace();
			Log.v("JadeNetCon-Err:", e.toString());
		}

		return null;

	}

	public String responsDataConvertor(HttpResponse res) {
		String strdata = null;

		try {
			InputStream in = res.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));

			StringBuilder str = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				str.append(line + "\n");
			}

			in.close();
			strdata = str.toString();

		} catch (Exception e) {
			Log.v("AwsSuggestCon:extrData", e.toString());
		}

		return strdata;
	}

	// Read json file
	public JSONObject parseJSONData(Context context, String filename) {
		String JSONString = null;
		JSONObject JSONObject = null;
		try {

			// open the inputStream to the file
			InputStream inputStream = context.getAssets().open(filename);

			int sizeOfJSONFile = inputStream.available();

			// array that will store all the data
			byte[] bytes = new byte[sizeOfJSONFile];

			// reading data into the array from the file
			inputStream.read(bytes);

			// close the input stream
			inputStream.close();

			JSONString = new String(bytes, "UTF-8");
			JSONObject = new JSONObject(JSONString);

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (JSONException x) {
			x.printStackTrace();
			return null;
		}
		return JSONObject;
	}

}
