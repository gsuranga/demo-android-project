package ceylon.linux.view;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.example.dimosales.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ceylon.linux.controller.CusAlertDialog;
import ceylon.linux.url.URLS;


public class Update_activity extends Activity {
	
	ProgressDialog dlDialog;
	String path = Environment.getExternalStorageDirectory()+ "/"; // Path to where you want to save the file
	String inet = URLS.prefix+"/apk/DemoSalesPad.apk"; // Internet path to the file
	String cachedir = "";                                       
	String filename = "TMC.apk";    
	CusAlertDialog dialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_activity);
		Update_activity.this
		.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		
		// ///////////////////These codes for action bar view//////////////////
			//	ActionBar bar = this.getActionBar();
		//		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

				this.getActionBar().setDisplayShowCustomEnabled(true);
				this.getActionBar().setDisplayShowTitleEnabled(false);

				LayoutInflater inflator = LayoutInflater.from(this);
				View v = inflator.inflate(R.layout.titleview, null);

				// if you need to customize anything else about the text, do it here.
				// I'm using a custom TextView with a custom font in my layout xml so
				// all I need to do is set title
				TextView title = (TextView) v.findViewById(R.id.title);

				title.setText("UPDATE");

				
				// assign the view to the actionbar
				this.getActionBar().setCustomView(v);

				// /////////////////////////////End of Action bar view//////////////////
		
				dialog = new CusAlertDialog(Update_activity.this);
				dialog.setTitle("Update");
				dialog.setMessage("Do you want to update this app?\n (Unsaved data may lost)");
				dialog.setPositiveButtonName("Update");
				dialog.getBtn_ok().setOnClickListener(back);
				dialog.setNegitiveButtonName("Cancel");
				dialog.getBtn_cancel().setOnClickListener(cancel);
				dialog.show();
		

	   
	}

	public void downloader()
	{
		 File getcache = this.getCacheDir();
		    cachedir = getcache.getAbsolutePath();

		    dlDialog = new ProgressDialog(Update_activity.this);
		    dlDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    dlDialog.setTitle("Downloading................");
		    dlDialog.setMessage("Connecting.........");
		    dlDialog.setCanceledOnTouchOutside(false);
		    dlDialog.show();
		    

		    new Thread(new Runnable() {

		        public void run() {

		            String filePath = path;

		            InputStream is = null;
		            OutputStream os = null;
		            URLConnection URLConn = null;

		            try {
		                URL fileUrl;
		                byte[] buf;
		                int ByteRead = 0;
		                int ByteWritten = 0;
		                fileUrl = new URL(inet);

		                URLConn = fileUrl.openConnection();

		                is = URLConn.getInputStream();

		                String fileName = inet.substring(inet.lastIndexOf("/") + 1);

		                File f = new File(filePath);
		                f.mkdirs();
		                String abs = filePath + fileName;
		                f = new File(abs);                      


		                os = new BufferedOutputStream(new FileOutputStream(abs));

		                buf = new byte[1024];

		                /*
		                 * This loop reads the bytes and updates a progressdialog
		                 */
		    
		    
		                while ((ByteRead = is.read(buf)) != -1) {

		                    os.write(buf, 0, ByteRead);
		                    ByteWritten += ByteRead;

		                    final int tmpWritten = ByteWritten/1024;
		                    runOnUiThread(new Runnable() {

		                        public void run() {
		                            dlDialog.setMessage(""+tmpWritten+" KB");
		                        }

		                    });
		                }

		                runOnUiThread(new Runnable() {

		                    public void run() {
		                        dlDialog.setTitle("Startar");
		                    }

		                });
		                is.close();
		                os.flush();
		                os.close();


		                Thread.sleep(200);

		                dlDialog.dismiss();

		                Intent intent = new Intent(Intent.ACTION_VIEW);
		                intent.setDataAndType(Uri.fromFile(new File(abs)),
		                        "application/vnd.android.package-archive");
		                startActivity(intent);
		                finish();

		            } catch (Exception e) {
		                e.printStackTrace();

		            }

		        }
		    }).start(); 
	}
	OnClickListener back = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			downloader();
			dialog.dismiss();

		}
	};

	OnClickListener cancel = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			dialog.dismiss();
			backpress();
		}
	};
	
	public void onBackPressed() {
	   
	};

	public void backpress()
	{
		finish();
		this.onBackPressed();
	}
}
