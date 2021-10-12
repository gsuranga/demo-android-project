package ceylon.linux.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ceylon.linux.view.HomeFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.widget.Toast;

public class Utility extends Activity {
	private Context contex;

	public static String[] itmDet = null;
	public Utility(Context contex) {
		// TODO Auto-generated constructor stub
		this.contex = contex;
	}

	// //////////////////////////////////Method to get
	// timestamp///////////////////////////////
	@SuppressLint("SimpleDateFormat")
	public static String timestamp_creater() {

		Date now = new Date();

		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(now);
		// String time = new SimpleDateFormat("HH:MM:ss").format(now);

		return timestamp;
	}

	// /////////////////////////change Network Available END
	// //////////////////////////////

	public static void copy(String path, String target_folder, String image_name) {
		createDirectoryIfNeeded(target_folder);

		String sdCard = Environment.getExternalStorageDirectory().toString();
		String uploadFilePath = sdCard + "/" + target_folder + "/" + image_name;

		File sourceLocation = new File(path);
		File targetLocation1 = new File(uploadFilePath);
		Log.v("sourceLocation: ", "sourceLocation: " + sourceLocation);
		Log.v("targetLocation: ", "targetLocation: " + targetLocation1);

		try {

			// 1 = move the file, 2 = copy the file
			int actionChoice = 1;

			// moving the file to another directory
			if (actionChoice == 1) {

				if (sourceLocation.renameTo(targetLocation1)) {
					Log.v("Move file successful.", "Move file successful.");
				} else {
					Log.v("Move file failed.", "Move file failed.");
				}

			}

			// we will copy the file
			else {

				// make sure the target file exists

				if (sourceLocation.exists()) {

					InputStream in = new FileInputStream(sourceLocation);
					OutputStream out = new FileOutputStream(targetLocation1);

					// Copy the bits from instream to outstream
					byte[] buf = new byte[1024];
					int len;

					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}

					in.close();
					out.close();

					Log.v("Copy file successful.", "Copy file successful.");

				} else {
					Log.v("Copy file failed. Source file missing.",
							"Copy file failed. Source file missing.");
				}

			}

		} catch (NullPointerException e) {
			// e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private static String getLeftAlignedString(String snippet, int length) {
		if (snippet.length() >= length) {
			return snippet.substring(0, length);
		} else {
			while (snippet.length() <= length) {
				snippet = snippet+"_";
			}
			return snippet;
		}
	}
	
	public static void writeToExternalFile(String description, String selling_price, String item_id, String part_no, String qty, String comment, String showAvgMovementAtDealer) {
		// wrting price history in a different file
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String datex = df.format(Calendar.getInstance().getTime());
			String sFileName = datex + "-" +HomeFragment.dealer_name+ ".txt";
			//String sFileName =  "LastOrder.txt";
			File sdcard = Environment.getExternalStorageDirectory();
			File file = new File(sdcard+"/Dimo_Current_order",sFileName);
			
			String newOrder = '\n' + getLeftAlignedString(description,22)+ "\t" + "\t"
			+ "\t" + "\t" + selling_price + "\t" + "\t"+ "\t" + "\t"
			+ "\t" + "\t" + item_id + "\t" +"\t"
			+ "\t" + "\t" + part_no + "\t" +"\t" 
			+ "\t" + "\t" + qty + "\t" +"\t" 
			//+ "\t" + "\t" + comment+"_" + "\t" +"\t" 
			+ "\t" + "\t" + showAvgMovementAtDealer + "\t" +"\t" ;
			BufferedReader br = null;
			String allText = null;
			if(file.exists()){
				StringBuilder text = new StringBuilder();
			try {			
				br = new BufferedReader(new FileReader(file)); 

				
			       String line;   
			        while ((line = br.readLine()) != null) {
			                    text.append(line);
			                    text.append(System.getProperty("line.separator"));
			                    }
			        Log.i("After while", "text : "+text+" : end");
				}catch (IOException e) {
			        e.printStackTrace();                    
			    }finally{
			        br.close();
			    }
			text.append(newOrder);
			allText = text.toString();
			}else{
				String str = "itemDescription \t\t\t\t         selling_price \t\t\t item_id \t\t\t\t\t part_no \t\t\t\t qty \t\t\t AvgMovementAtDealers"+"\n";
				
				allText = str + newOrder;
			
			}
			
			try {
				File root = new File(Environment.getExternalStorageDirectory(),"Dimo_Current_order");
				if (!root.exists()) {
					root.mkdirs();
				}
				File gpxfile = new File(root, sFileName);
				FileWriter writer = new FileWriter(gpxfile);
				writer.append(allText);
				writer.flush();
				writer.close();
				//Toast.makeText(, "Saved", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeToExternalFile1(String description, String selling_price, String item_id, String part_no, String qty, String comment, String showAvgMovementAtDealer) {
		// wrting price history in a different file
		try {
			String sFileName =  "LastOrder.txt";
			File sdcard = Environment.getExternalStorageDirectory();
			File file = new File(sdcard+"/Dimo_Current_order",sFileName);
			
			String newOrder = "|"+  item_id
			+ "|"+ part_no + "|" + description + "|" + showAvgMovementAtDealer 
			+ "|"+ qty + "|" + comment+" " +"|" + selling_price;
			
			BufferedReader br = null;
			
			String allText = null;
			
			if(file.exists()){
				StringBuilder text = new StringBuilder();
			try {		
				br = new BufferedReader(new FileReader(file));
			       String line;   
			        while ((line = br.readLine()) != null) {
			                    text.append(line);
			                    }
				}catch (IOException e) {
			        e.printStackTrace();                    
			    }finally{
			        br.close();
			    }
			text.append(newOrder);
			allText = text.toString();
			}else{
				String str = HomeFragment.dealer_id +","+HomeFragment.dealer_name+","
							 +HomeFragment.dealer_account_no+","+HomeFragment.discount_percentage+","+
							 HomeFragment.overdue_amount+","+HomeFragment.outstanding_amount+
							 ","+HomeFragment.credit_limit;
				allText = str + newOrder;
			}
			try {
				File root = new File(Environment.getExternalStorageDirectory(),"Dimo_Current_order");
				if (!root.exists()) {
					root.mkdirs();
				}
				File gpxfile = new File(root, sFileName);
				FileWriter writer = new FileWriter(gpxfile);
				writer.append(allText);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static HashMap<String, String> readFromFile(){
		BufferedReader br = null;
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String datex = df.format(Calendar.getInstance().getTime());
		HashMap<String, String> purchase_order = new HashMap<String, String>();
		String[] ordDet = null;
		
		try {
			String sFileName =  "LastOrder.txt";
			File sdcard = Environment.getExternalStorageDirectory();
			File file = new File(sdcard+"/Dimo_Current_order",sFileName);
			if(file.exists()){
				StringBuilder text = new StringBuilder();
			try {		
				br = new BufferedReader(new FileReader(file));
			       String line;   
			        while ((line = br.readLine()) != null) {	
			        	// Log.i("XXXX",line+"");
			        	itmDet = line.split("\\|");
			        	//Log.w("XXXX",itmDet[0]+"");
			        }
			        // Log.i("WWWW",itmDet[0]+"");
			        ordDet = itmDet[0].split(",");
				}catch (IOException e) {
			        e.printStackTrace();                    
			    }finally{
			        br.close();
			    }
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		purchase_order.put("ID", "");
		purchase_order.put("BillAmount", "0.00");
		purchase_order.put("dealer_id", ordDet[0]);
		purchase_order.put("dealer_name", ordDet[1]);
		purchase_order.put("date_of_bill", datex);
		purchase_order.put("syncstatus", "");
		purchase_order.put("BillAmount_with_vat","0.00");
		purchase_order.put("finish_status", "");
		purchase_order.put("delar_account_no", ordDet[2]);
		purchase_order.put("dealer_discount", ordDet[3]);
		purchase_order.put("overdue_amount", ordDet[4]);
		purchase_order.put("outstanding_amount", ordDet[5]);
		purchase_order.put("credit_limit", ordDet[6]);

		return purchase_order;
	}
	
	public static void zipSubFolder(ZipOutputStream out, File folder,
			int basePathLength) throws IOException {

		final int BUFFER = 2048;

		File[] fileList = folder.listFiles();
		BufferedInputStream origin = null;
		for (File file : fileList) {
			if (file.isDirectory()) {
				zipSubFolder(out, file, basePathLength);
			} else {
				byte data[] = new byte[BUFFER];
				String unmodifiedFilePath = file.getPath();
				String relativePath = unmodifiedFilePath
						.substring(basePathLength);
				Log.i("ZIP SUBFOLDER", "Relative Path : " + relativePath);
				FileInputStream fi = new FileInputStream(unmodifiedFilePath);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(relativePath);
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}
		}
	}

	public static boolean zipFileAtPath(String sourcePath, String toLocation) {
		// ArrayList<String> contentList = new ArrayList<String>();
		final int BUFFER = 2048;

		File sourceFile = new File(sourcePath);
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(toLocation);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			if (sourceFile.isDirectory()) {
				zipSubFolder(out, sourceFile, sourceFile.getParent().length());
			} else {
				byte data[] = new byte[BUFFER];
				FileInputStream fi = new FileInputStream(sourcePath);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * gets the last path component
	 * 
	 * Example: getLastPathComponent("downloads/example/fileToZip"); Result:
	 * "fileToZip"
	 */
	public static String getLastPathComponent(String filePath) {
		String[] segments = filePath.split("/");
		String lastPathComponent = segments[segments.length - 1];
		return lastPathComponent;
	}

	public static void createDirectoryIfNeeded(String folder_name) {
		File direct = new File(Environment.getExternalStorageDirectory() + "/"
				+ folder_name);

		if (!direct.exists()) {
			if (direct.mkdir()) {
				Log.i("Directory Created", " Directory Created");
			}

		}

	}

	public static String formatStringAmounts(String value) {

		try {
			value = value.trim();
			double amount = Double.parseDouble(value);
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			return nf.format(amount);
		} catch (NumberFormatException nfe) {

			return "wrong input";
		}

	}

	// /////////////////////////change Network Available START
	// ///////////////////////////
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) contex
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	// ///////////////////////////////GET BattryLevel
	// ////////////////////////////////////
	// //without Broadcast Receiver (No live Update)
	public float getBatteryLevel() {
		Intent batteryIntent = registerReceiver(null, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		// Error checking that probably isn't needed but I added just in case.
		if (level == -1 || scale == -1) {
			return 50.0f;
		}

		return ((float) level / (float) scale) * 100.0f;
	}

	// upload
	public void batteryLevel() {
		BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);
				int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,
						-1);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				int level = -1;
				if (rawlevel >= 0 && scale > 0) {
					level = (rawlevel * 100) / scale;
				}
				String s = "Battery Level Remaining: " + level + "%";
				// //set your value
			}

		};
		IntentFilter batteryLevelFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri uri) {
		Cursor cursor = contex.getContentResolver().query(uri, null, null,
				null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaColumns.DATA);
		return cursor.getString(idx);
	}

}
