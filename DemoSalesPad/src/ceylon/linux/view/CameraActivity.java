package ceylon.linux.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONException;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.controller.StaticValues;

public class CameraActivity extends Activity {

	final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	private static final String TAG = "CaptureImage.java";
	public static ArrayList<HashMap<String, String>> outlets = new ArrayList<HashMap<String, String>>();
	public static ImageView showImg = null;
	static String uploadFilePath = "";
	
	static String Path;
	static int delete_image_id = 0;
	static TextView imageDetails = null;
	// FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(CaptureImage.this);
	static Cursor cursor = null;
	public ProgressDialog dialog_upload = null;
	public String s_name;
	public String s_Adress;
	int serverResponseCode = 0;
	TextView shift_display;
	public static String img_path = "";
	Uri imageUri = null;
	OnClickListener oclBtnOk = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			Log.i("VALUES", "It is Working");
			String fileName = "Camera_Example.jpg";
			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.TITLE, fileName);
			values.put(MediaStore.Images.Media.DESCRIPTION,
					"Image capture by camera");

			imageUri = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

			/*************************** Camera Intent End ************************/

		}
	};
	CameraActivity cameraActivity = null;

	Button outlet_names_show;
	double latitude;
	double longitude;

	private Button buttonback;

	/**
	 * ********* Convert Image Uri path to physical path *************
	 */

	public static String convertImageUriToFile(Uri imageUri, Activity activity) {

		int imageID = 0;

		try {

			/*********** Which columns values want to get *******/
			String[] proj = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID,
					MediaStore.Images.Thumbnails._ID,
					MediaStore.Images.ImageColumns.ORIENTATION };

			cursor = activity.getContentResolver().query(imageUri, proj, null,
					null, null);

			// Get Query Data

			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
			int file_ColumnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			// int orientation_ColumnIndex = cursor.
			// getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

			int size = cursor.getCount();

			/******* If size is 0, there are no images on the SD Card. *****/

			if (size == 0) {

				// imageDetails.setText("No Image");
			} else {

				if (cursor.moveToFirst()) {

					imageID = cursor.getInt(columnIndex);
					delete_image_id = imageID;

					Path = cursor.getString(file_ColumnIndex);

				}

			}

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		// Return Captured Image ImageID ( By this ImageID Image will load from
		// sdcard )

		return "" + imageID;
	}

	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		StaticValues.file_path_payments="";
		cameraActivity = this;
		showImg = (ImageView) findViewById(R.id.imageView1);
		// upload = new Upload(CaptureImage.this);
		final Button photo = (Button) findViewById(R.id.btn_take_photo);
		final Button upload = (Button) findViewById(R.id.btn_save);

		photo.setOnClickListener(oclBtnOk);
		// upload.setOnClickListener(oclBtnOk3);

		buttonback = (Button) findViewById(R.id.btn_back);
		buttonback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				back_press();

			}
		});
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					copy();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (imageUri != null) {
			outState.putString("cameraImageUri", imageUri.toString());
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey("cameraImageUri")) {
			imageUri = Uri
					.parse(savedInstanceState.getString("cameraImageUri"));
		}
	}

	public void upload_prescription() {
		dialog_upload = ProgressDialog.show(CameraActivity.this, "",
				"Uploading file...", true);

		latitude = 5;
		longitude = 8;

		Toast.makeText(
				getApplicationContext(),
				"Your Location is - \nLat: " + latitude + "\nLong: "
						+ longitude, Toast.LENGTH_LONG).show();

		new Thread(new Runnable() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {

					}
				});

				createDirectoryIfNeeded();

				try {
					copy();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// deleteImageFromGallery(delete_image_id + "");
				// upload.uploadFile(uploadFilePath);

			}
		}).start();

	}

	private void deleteImageFromGallery(String captureimageid) {

		Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		cursor = null;

		getContentResolver().delete(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				BaseColumns._ID + "=?", new String[] { captureimageid });

		String[] projection = { MediaStore.Images.ImageColumns.SIZE,
				MediaStore.Images.ImageColumns.DISPLAY_NAME,
				MediaStore.Images.ImageColumns.DATA, BaseColumns._ID, };

		Log.i("InfoLog", "on activityresult Uri u " + u.toString());

		try {
			if (u != null) {
				cursor = CameraActivity.this.getContentResolver().query(
						imageUri, projection, null, null, null);
			}
			if ((cursor != null) && (cursor.moveToLast())) {

				int i = getContentResolver().delete(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						BaseColumns._ID + "=" + cursor.getString(3), null);
				Log.v(TAG, "Number of column deleted : " + i);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	@SuppressLint("SimpleDateFormat")
	private String getDate() {

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate1 = df1.format(c.getTime());

		return formattedDate1;

	}

	private void createDirectoryIfNeeded() {
		File direct = new File(Environment.getExternalStorageDirectory()
				+ "/Capturetemp");
		
		if(StaticValues.capture_type==422){
			 direct = new File(Environment.getExternalStorageDirectory()
						+ "/garage_loyality");
		}else if(StaticValues.capture_type==572)
		{
	 direct = new File(Environment.getExternalStorageDirectory()
				+ "/cheque_payments");
		}else if(StaticValues.capture_type == 983)
		{
	 direct = new File(Environment.getExternalStorageDirectory()
				+ "/bank_deposits");
		}else if(StaticValues.capture_type == 105)
		{
	 direct = new File(Environment.getExternalStorageDirectory()
				+ "/pay_returns");
		}else
		{
	 direct = new File(Environment.getExternalStorageDirectory()
				+ "/Capturetemp");
		}

		if (!direct.exists()) {
			if (direct.mkdir()) {
				// directory is created;
			}

		}

	}

	private void copy() throws JSONException {

		java.util.Date date = new java.util.Date();
		java.text.SimpleDateFormat dateFormatter1 = new java.text.SimpleDateFormat(
				"yyyy_MM_dd_HH_mm_ss");

		Log.e("date", dateFormatter1.format(date) + "");

		String filename = dateFormatter1.format(date);
		
		if(StaticValues.capture_type==422){

			uploadFilePath="/sdcard/garage_loyality/" + filename.toString() + ".jpg";
		}else if(StaticValues.capture_type==572)
		{
			uploadFilePath = "/sdcard/cheque_payments/" + filename.toString() + ".jpg";
	 }else if(StaticValues.capture_type==983)
	 {
	 uploadFilePath = "/sdcard/bank_deposits/" + filename.toString() + ".jpg";//pay_returns
	 
	 }else if(StaticValues.capture_type==105)
	 {
	 uploadFilePath = "/sdcard/pay_returns/" + filename.toString() + ".jpg";//pay_returns
	 
	 }else
		{
			uploadFilePath = "/sdcard/comp_parts/" + filename.toString() + ".jpg";
			
		}

		StaticValues.file_path_payments = uploadFilePath;

		File sourceLocation = new File(Path);
		File targetLocation = new File(uploadFilePath);
		Log.v(TAG, "sourceLocation: " + sourceLocation);
		Log.v(TAG, "targetLocation: " + targetLocation);

		try {

			// 1 = move the file, 2 = copy the file
			int actionChoice = 2;

			// moving the file to another directory
			if (actionChoice == 1) {

				if (sourceLocation.renameTo(targetLocation)) {
					Log.v(TAG, "Move file successful.");
				} else {
					Log.v(TAG, "Move file failed.");
				}

			}

			// we will copy the file
			else {

				// make sure the target file exists
				// TODO
				if (sourceLocation.exists()) {
					
					

					File f = new File(Environment.getExternalStorageDirectory()
							+ "/comp_parts");
					
					if(StaticValues.capture_type==422)
					{
						
						f = new File(Environment.getExternalStorageDirectory()
								+ "/garage_loyality");
					}else if(StaticValues.capture_type==572)
					{
						
						f = new File(Environment.getExternalStorageDirectory()
								+ "/cheque_payments");
					}else if(StaticValues.capture_type==983)
					{
						f = new File(Environment.getExternalStorageDirectory()
								+ "/bank_deposits");//pay_returns
					}else if(StaticValues.capture_type==105)
					{
						f = new File(Environment.getExternalStorageDirectory()
								+ "/pay_returns");//pay_returns
					}

					if (f.isDirectory()) {

						InputStream in = new FileInputStream(sourceLocation);
						OutputStream out = new FileOutputStream(targetLocation);

						// Copy the bits from in stream to out stream
						byte[] buf = new byte[1024];
						int len;

						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						in.close();
						out.close();
					} else {
						f.mkdir();
						Log.e("errr", "no folder");

						InputStream in = new FileInputStream(sourceLocation);
						OutputStream out = new FileOutputStream(targetLocation);

						// Copy the bits from instream to outstream
						byte[] buf = new byte[1024];
						int len;

						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						in.close();
						out.close();
					}

					Log.v(TAG, "Copy file successful.");

				} else {
					Log.v(TAG, "Copy file failed. Source file missing.");
				}

			}

		} catch (NullPointerException e) {
			// e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

			if (resultCode == RESULT_OK) {

				/*********** Load Captured Image And Data Start ***************/

				String imageId = convertImageUriToFile(imageUri, cameraActivity);

				// Create and excecute AsyncTask to load capture image

				new LoadImagesFromSDCard(CameraActivity.this).execute(""
						+ imageId);

				/*********** Load Captured Image And Data End ****************/

			} else if (resultCode == RESULT_CANCELED) {

				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();

			} else {

				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();

			}
		}

	}

	@Override
	public void onBackPressed() {
		// SinhalaToast.makeSinhalaToast(getApplicationContext(),
		// "fnd;a;u tnqjd", Toast.LENGTH_LONG).show();
	}

	public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {

		Context context;
		Bitmap mBitmap;
		private ProgressDialog loadingdailog;

		public LoadImagesFromSDCard(CameraActivity context) {
			this.context = context;

		}

		@Override
		protected void onPreExecute() {
			loadingdailog = new ProgressDialog(context);
			loadingdailog.setMessage("Loading Image.....");
			loadingdailog.show();

		}

		// Call after onPreExecute method
		@Override
		protected Void doInBackground(String... urls) {

			Bitmap bitmap = null;
			Bitmap newBitmap = null;
			Uri uri = null;

			try {

				uri = Uri.withAppendedPath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ""
								+ urls[0]);

				/************** Decode an input stream into a bitmap. *********/
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uri));

				if (bitmap != null) {

					/********* Creates a new bitmap, scaled from an existing bitmap. ***********/

					newBitmap = Bitmap.createScaledBitmap(bitmap, 350, 350,
							true);
					// SaveIamge(newBitmap);
					bitmap.recycle();
					if (newBitmap != null) {

						mBitmap = newBitmap;
					}
				}
			} catch (IOException e) {
				// Error fetching image, try to recover

				/********* Cancel execution of this task. **********/
				cancel(true);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {

			if (loadingdailog.isShowing() && loadingdailog != null) {

				loadingdailog.dismiss();
				loadingdailog = null;
			}

			if (mBitmap != null) {
				showImg.setImageBitmap(mBitmap);

			}
		}
	}

	private void open() {

		String fileName = "Camera_Example.jpg";
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION,
				"Image capture by camera");

		imageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		startActivityForResult(intent, 0);
	}

	

	private void back_press() {
		super.onBackPressed();
	}
}
