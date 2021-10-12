package ceylon.linux.view;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.dimosales.DealerStockMovementWebViewActivity;
import com.example.dimosales.DealerwiseLostSalesWv;
import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.asynctask.DownloadDataFromServer;
import ceylon.linux.asynctask.UploadDataToServer;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.controller.NavDrawerListAdapter;
import ceylon.linux.model.NavDrawerItem;
import ceylon.linux.model.NavDrawerSubItem;
import ceylon.linux.service.Update_manger;
import ceylon.linux.url.URLS;
import ceylon.linux.utility.Utility;

@SuppressLint("NewApi")
public class HomeActivity extends Activity {

	public ActionBarDrawerToggle mDrawerToggle;
	SharedPreferences user_data;
	Jsonhelper json;
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	// nav drawer title
	private CharSequence mDrawerTitle;
	// used to store app title
	private CharSequence mTitle;
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	public static int lastOdr = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// Intent intentx = new Intent(HomeActivity.this, Update_manger.class);
		// startService(intentx);

		mTitle = mDrawerTitle = getTitle();
		json = new Jsonhelper();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();
		user_data = getSharedPreferences("USERDATA", Context.MODE_PRIVATE);

		// adding nav drawer items to array
		// Purchase Order
		NavDrawerItem purchaseOrderNavDrawerItem = new NavDrawerItem(
				navMenuTitles[0], navMenuIcons.getResourceId(0, -1));
		ArrayList<NavDrawerSubItem> purchaseOrderNavDrawerSubItems = new ArrayList<NavDrawerSubItem>();
		purchaseOrderNavDrawerSubItems.add(new NavDrawerSubItem("Create New",
				R.drawable.create_new_order));

		purchaseOrderNavDrawerSubItems.add(new NavDrawerSubItem(
				"Manage-Orders", R.drawable.manage_order));
		purchaseOrderNavDrawerItem
				.setNavDrawerSubItems(purchaseOrderNavDrawerSubItems);
		navDrawerItems.add(purchaseOrderNavDrawerItem);
		purchaseOrderNavDrawerSubItems.add(new NavDrawerSubItem(
				"View All Orders", R.drawable.manage_order));
		purchaseOrderNavDrawerSubItems.add(new NavDrawerSubItem(
				"View Saved oders", R.drawable.manage_order));
		purchaseOrderNavDrawerSubItems.add(new NavDrawerSubItem("Last Order",
				R.drawable.manage_order));
		purchaseOrderNavDrawerSubItems.add(new NavDrawerSubItem(
				"Call Order Accepting", R.drawable.manage_order));		
		purchaseOrderNavDrawerSubItems.add(new NavDrawerSubItem(
				"Update stock", R.drawable.manage_order));

		// Marketing Activity
		NavDrawerItem marketingActivutyNavDrawerItem = new NavDrawerItem(
				navMenuTitles[1], navMenuIcons.getResourceId(1, -1));
		ArrayList<NavDrawerSubItem> marketingActivutyNavDrawerSubItems = new ArrayList<NavDrawerSubItem>();
		marketingActivutyNavDrawerSubItems.add(new NavDrawerSubItem(
				"Notification", R.drawable.notification));
		marketingActivutyNavDrawerSubItems.add(new NavDrawerSubItem(
				"New Campaign", R.drawable.campaign));
		marketingActivutyNavDrawerSubItems.add(new NavDrawerSubItem("Branding",
				R.drawable.campaign));
		marketingActivutyNavDrawerSubItems.add(new NavDrawerSubItem(
				"Marketing-Calender", R.drawable.campaign));
		marketingActivutyNavDrawerItem
				.setNavDrawerSubItems(marketingActivutyNavDrawerSubItems);
		navDrawerItems.add(marketingActivutyNavDrawerItem);

		// Garage
		NavDrawerItem garageNavDrawerItem = new NavDrawerItem(navMenuTitles[2],
				navMenuIcons.getResourceId(2, -1));
		ArrayList<NavDrawerSubItem> garageNavDrawerSubItems = new ArrayList<NavDrawerSubItem>();
		garageNavDrawerSubItems.add(new NavDrawerSubItem("View Profile",
				R.drawable.garage));
		garageNavDrawerSubItems.add(new NavDrawerSubItem("Register",
				R.drawable.add_garage));
		garageNavDrawerSubItems.add(new NavDrawerSubItem("Manage Garages",
				R.drawable.add_garage));
		garageNavDrawerItem.setNavDrawerSubItems(garageNavDrawerSubItems);
		navDrawerItems.add(garageNavDrawerItem);

		// Register
		NavDrawerItem registerNavDrawerItem = new NavDrawerItem(
				navMenuTitles[3], navMenuIcons.getResourceId(3, -1));
		ArrayList<NavDrawerSubItem> registerNavDrawerSubItems = new ArrayList<NavDrawerSubItem>();

		registerNavDrawerSubItems.add(new NavDrawerSubItem(
				"Update Dealer Location", R.drawable.add_dealer));
		registerNavDrawerSubItems.add(new NavDrawerSubItem(
				"View Dealer Location", R.drawable.add_dealer));
		registerNavDrawerSubItems.add(new NavDrawerSubItem("Register Dealer",
				R.drawable.add_dealer));
		registerNavDrawerSubItems.add(new NavDrawerSubItem(
				"Change Dealer Passwords", R.drawable.add_dealer));

		registerNavDrawerItem.setNavDrawerSubItems(registerNavDrawerSubItems);
		navDrawerItems.add(registerNavDrawerItem);

		// stock report
		NavDrawerItem stockNavDrawerItem = new NavDrawerItem(navMenuTitles[4],
				navMenuIcons.getResourceId(3, -1));
		ArrayList<NavDrawerSubItem> stockSubItems = new ArrayList<NavDrawerSubItem>();
		stockSubItems.add(new NavDrawerSubItem("Dealer Stock Report"));
		stockSubItems.add(new NavDrawerSubItem("VSD Report"));
		stockSubItems.add(new NavDrawerSubItem("Invoice Summary"));
		stockSubItems.add(new NavDrawerSubItem(
				"Marketing Campaign effectiveness"));
		stockSubItems.add(new NavDrawerSubItem("Line Item wise target"));
		stockSubItems.add(new NavDrawerSubItem("Dealer rank"));
		stockSubItems.add(new NavDrawerSubItem("Dealers performance"));
		stockSubItems.add(new NavDrawerSubItem(
				"Competitor parts & Product failures"));
		stockSubItems.add(new NavDrawerSubItem("Expenses summary"));
		stockSubItems.add(new NavDrawerSubItem("Visit history"));
		stockSubItems
				.add(new NavDrawerSubItem("Dealer,garage & vehicle owners"));
		stockSubItems.add(new NavDrawerSubItem("Payment summary"));
		stockSubItems.add(new NavDrawerSubItem("Dealer wise lost sales"));
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		stockSubItems.add(new NavDrawerSubItem("Dealer stock movement Report"));
		
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@	
		stockNavDrawerItem.setNavDrawerSubItems(stockSubItems);
		navDrawerItems.add(stockNavDrawerItem);

		NavDrawerItem paymentNavDrawerItem = new NavDrawerItem(
				navMenuTitles[5], navMenuIcons.getResourceId(3, -1));
		ArrayList<NavDrawerSubItem> paymentNavDrawerSubItems = new ArrayList<NavDrawerSubItem>();
		paymentNavDrawerSubItems.add(new NavDrawerSubItem("Dealer Payment",
				R.drawable.pay));
		paymentNavDrawerItem.setNavDrawerSubItems(paymentNavDrawerSubItems);
		navDrawerItems.add(paymentNavDrawerItem);

		NavDrawerItem sales_visit = new NavDrawerItem(navMenuTitles[6],
				navMenuIcons.getResourceId(3, -1));
		ArrayList<NavDrawerSubItem> sales_visitSubItems = new ArrayList<NavDrawerSubItem>();
		sales_visitSubItems.add(new NavDrawerSubItem("Add Tour Plan"));
		sales_visitSubItems.add(new NavDrawerSubItem("Manage Tour Plan"));
		sales_visitSubItems.add(new NavDrawerSubItem("New Iternary"));
		sales_visitSubItems.add(new NavDrawerSubItem("Sales Tour Expenses"));

		sales_visit.setNavDrawerSubItems(sales_visitSubItems);
		navDrawerItems.add(sales_visit);

		// parts
		NavDrawerItem parts = new NavDrawerItem(navMenuTitles[7],
				navMenuIcons.getResourceId(3, -1));
		ArrayList<NavDrawerSubItem> parts_SubItems = new ArrayList<NavDrawerSubItem>();
		parts_SubItems.add(new NavDrawerSubItem("Product Failures"));
		parts_SubItems.add(new NavDrawerSubItem("Competitor Parts"));
		parts.setNavDrawerSubItems(parts_SubItems);
		navDrawerItems.add(parts);

		NavDrawerItem target = new NavDrawerItem(navMenuTitles[8],
				navMenuIcons.getResourceId(3, -1));
		ArrayList<NavDrawerSubItem> sub_target = new ArrayList<NavDrawerSubItem>();
		sub_target.add(new NavDrawerSubItem("Line-ItemWise-Target"));
		target.setNavDrawerSubItems(sub_target);
		navDrawerItems.add(target);

		NavDrawerItem e_mail = new NavDrawerItem("E mail",
				navMenuIcons.getResourceId(3, -1));
		ArrayList<NavDrawerSubItem> sub_e_mail = new ArrayList<NavDrawerSubItem>();
		sub_e_mail.add(new NavDrawerSubItem("E mail details"));
		e_mail.setNavDrawerSubItems(sub_e_mail);
		navDrawerItems.add(e_mail);

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View view, int groupPosition, int childPosition,
							long id) {

						return displayView(groupPosition, childPosition);

					}
				});

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
				// accessibility
				R.string.app_name // nav drawer close - description for
		// accessibility
		) {
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0, 0);
		}

		Intent intent = new Intent(HomeActivity.this, Update_manger.class);// TODO
		startService(intent);

		if (InvoiceInfoFragment.status_fragment == 1) {
			displayView(0, 4);
		} else if (InvoiceInfoFragment.status_fragment == 2) {
			displayView(0, 5);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			UploadDataToServer u = new UploadDataToServer(HomeActivity.this) {
				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					/*
					 * DownloadDataFromServer d = new
					 * DownloadDataFromServer(HomeActivity.this); String[]
					 * params = { "cvcv" }; d.execute(params);
					 */

				}
			};
			String[] params = { "cvcv" };
			u.execute(params);
			return true;
		case R.id.action_settings_download:
			DownloadDataFromServer d = new DownloadDataFromServer(
					HomeActivity.this, false);
			String[] paramsx = { "cvcv" };
			d.execute(paramsx);
			return true;
		case R.id.logout:
			SharedPreferences userData = getSharedPreferences("USERDATA",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = userData.edit().clear();
			editor.commit();
			// android.os.Process.killProcess(android.os.Process.myPid());
			Intent intent = new Intent(this, Login.class);
			startActivity(intent);
			finish();
			return true;

		case R.id.changepassword:
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					HomeActivity.this);
			Builder setTitle = alertDialog.setTitle(user_data.getString(
					"u_name", ""));
			final EditText pw = new EditText(HomeActivity.this);
			final EditText confirm = new EditText(HomeActivity.this);
			confirm.setHint("Confirm Password");
			final TextView error = new TextView(HomeActivity.this);

			// final EditText lot = new EditText(getActivity());
			pw.setHint("User Name");
			LinearLayout ll = new LinearLayout(HomeActivity.this);
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.addView(pw);
			ll.addView(confirm);
			ll.addView(error);

			alertDialog.setView(ll);
			alertDialog.setCancelable(false);

			alertDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							if (pw.getText()
									.toString()
									.trim()
									.equals(confirm.getText().toString().trim())) {

							} else {

							}

						}
					}).setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// dialog.cancel();
						}
					});

			final AlertDialog alert = alertDialog.create();

			alert.show();

			alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							Boolean wantToCloseDialog = false;
							if (pw.getText()
									.toString()
									.trim()
									.equals(confirm.getText().toString().trim())) {

								AsyncTask<String, Void, JSONObject> update_data = new AsyncTask<String, Void, JSONObject>() {

									protected void onPreExecute() {
										super.onPreExecute();

									}

									@Override
									protected JSONObject doInBackground(
											String... params) {
										ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
												2);
										nameValuePairs
												.add(new BasicNameValuePair(
														"username", params[0]));
										nameValuePairs
												.add(new BasicNameValuePair(
														"password", params[1]));

										Utility u = new Utility(
												HomeActivity.this);
										JSONObject j = null;
										if (u.isNetworkAvailable()) {
											j = json.JsonObjectSendToServerPostWithNameValuePare(
													URLS.update_user_name,
													nameValuePairs);

										} else {

											Toast.makeText(
													HomeActivity.this,
													"Internet is not Available",
													Toast.LENGTH_LONG).show();

										}
										return j;
									}

									@Override
									protected void onPostExecute(
											JSONObject result) {
										super.onPostExecute(result);
										SharedPreferences userData = getSharedPreferences(
												"USERDATA",
												Context.MODE_PRIVATE);
										SharedPreferences.Editor editor = userData
												.edit().clear();
										editor.commit();
										android.os.Process
												.killProcess(android.os.Process
														.myPid());
										Intent intent = new Intent(
												HomeActivity.this, Login.class);
										startActivity(intent);
										finish();
									}

								};
								String[] params = {
										user_data.getString("u_name", ""),
										pw.getText().toString() };

								if (pw.getText().toString().equals("")
										|| confirm.getText().toString()
												.equals("")) {

								} else

								{
									update_data.execute(params);
								}

							} else {

								error.setTextColor(Color.RED);
								error.setText("Password mismatch");
							}

							if (wantToCloseDialog)
								alert.dismiss();

						}
					});

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 */
	private boolean displayView(int groupPosition, int childPosition) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (groupPosition) {
		case 0:
			switch (childPosition) {
			case 0:
				fragment = new HomeFragment();
				ViewSavedOrdersFragment.purchase_order_edit = 0;// create new
																// order
																// fragment
				break;
			/*
			 * case 1: fragment = new Sugesstion_order_fragment();// Suggestion
			 * Order // fragment break; case 2: fragment = new
			 * ManageOrderFragment();// manage order fragment break; case 3:
			 * fragment = new Show_Rejected_Orders();// Rejected order fragment
			 * break;
			 */
			case 1:
				fragment = new ManagePurchaseOrders();// Manage Purchase order
				break;

			case 2:
				fragment = new Call_order_fragment();// Call Order

				break;

			case 3:
				fragment = new ViewSavedOrdersFragment();// saved order

				break;

			case 4:
				File root = new File(Environment.getExternalStorageDirectory(),
						"Dimo_Current_order");
				if (!root.exists()) {
					root.mkdirs();
				}
				File gpxfile = new File(root, "LastOrder.txt");
				if (gpxfile.exists()) {
					fragment = new ReadFromFileOrdersFragment();// read from
																// file
					lastOdr = 1;
				} else {
					Toast.makeText(HomeActivity.this,
							"There is no last saved order", Toast.LENGTH_SHORT)
							.show();
				}
				break;

			case 5:
				fragment = new Call_Order_Accepting();// saved order
				break;
			case 6:
				new DownloadDataFromServer(HomeActivity.this, true).execute(""); // update stock
			}
			break;
		case 1:
			switch (childPosition) {
			case 0:
				fragment = new NotificationFragment();// notification fragment
				break;
			case 1:
				fragment = new MarkertingFragment();// new campaign fragment
				break;
			case 2:
				// fragment = new BrandFragment();// new campaign fragment
				break;

			case 3:
				fragment = new MarketingActivityCalender();// new campaign
															// fragment
				break;

			}
			break;
		case 2:
			switch (childPosition) {
			case 0:
				// fragment = new PaymentShow();// view garage profile fragment
				break;
			case 1:
				fragment = new RegisterGarageFragement();// register fragment
				// fragment
				break;
			case 2:
				fragment = new ManageGarageDetails();// register fragment
				// fragment
				break;
			}
			break;
		case 3:
			switch (childPosition) {
			/*
			 * case 0: fragment = new RegisterCustomerFragment();// add user
			 * fragment break; case 1: fragment = new RegisterDealer();//
			 * register dealer fragment break;
			 */

			case 0:
				fragment = new UpdateDealerLocation();// update location
														// fragment
				// fragment
				break;
				//////////////////////
			case 1:
				Intent i = new Intent(HomeActivity.this, MapViewActivty.class);
				startActivity(i);
				break;
////////////////////////////////////////////
			case 2:
				fragment = new FragmentSelectDealerRegister();
				break;
				//////////////////////////
			case 3:
				fragment = new Fragment_Dealer_Select_change_password();
				break;
			}
			break;
///////////////////////////////////////////////
		case 4:
			switch (childPosition) {
			case 0:
				fragment = new StockReport();// add Stock Fragment
				break;
			case 1:
				fragment = new VSDfragment();// add Stock Fragment
				break;
			case 2:
				fragment = new InvoiceSummery();// add Stock Fragment
				break;
			case 12:
				startActivity(new Intent(HomeActivity.this,
						DealerwiseLostSalesWv.class));
				break;
			case 13:
				startActivity(new Intent(HomeActivity.this,
						DealerStockMovementWebViewActivity.class));
				break;
			}
			break;
//////////////////////////////////////////
		case 5:
			switch (childPosition) {
			case 0:
				fragment = new PaymentFragment();// payment fragment
				break;
			}
			break;
//////////////////////////////////////////////////////
		case 6:
			switch (childPosition) {
			case 0:
				fragment = new NewTourPlanFragment();// payment fragment
				break;
			case 1:
				fragment = new ManageTour();// payment fragment
				break;
			case 2:
				fragment = new NewTourItenaryFragment();// payment fragment
				break;
			case 3:
				fragment = new SalesTourExpensesFragment();
				break;
			}
			break;

		case 7:
			switch (childPosition) {
			case 0:
				fragment = new FailurePartFragment();
				break;
			case 1:
				fragment = new CompetitorPartsFragment();
				break;

			}
			break;

		case 8:
			switch (childPosition) {
			case 0:
				fragment = new LineItemWiseTarget();// LineItemWise fragment
				break;

			}
			break;
		case 9:
			switch (childPosition) {
			case 0:
				fragment = new FragmentEmail();// email fragment
				break;

			}
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.frame_container, fragment);
			fragmentTransaction.addToBackStack(null).commit();

			// update selected item and title, then close the drawer
			if (groupPosition == 0 && childPosition == 4) {

				adapter.setSelection(groupPosition, 0);
			} else if (groupPosition == 0 && childPosition == 5) {
				adapter.setSelection(groupPosition, 0);
			} else {
				adapter.setSelection(groupPosition, childPosition);

			}
			// collapse and expand for colour changes
			mDrawerList.collapseGroup(groupPosition);
			mDrawerList.expandGroup(groupPosition);

			setTitle(navMenuTitles[groupPosition]);
			mDrawerLayout.closeDrawer(mDrawerList);
			return true;
		}
		return false;
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);

	}

	@Override
	public void onBackPressed() {

	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
