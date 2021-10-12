package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.dimosales.R;
import com.example.dimosales.R.color;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import ceylon.linux.asynctask.UploadDataToServer;
import ceylon.linux.controller.StaticValues;
import ceylon.linux.db.DbHandler;
import ceylon.linux.db.DbHelper;
import ceylon.linux.db.Dbworker;
import ceylon.linux.model.Bank_deposite_payment_model;
import ceylon.linux.model.Cheque_payment_model;
import ceylon.linux.model.Garage_loyalty_item_model;
import ceylon.linux.model.Payment_return_item_model;
import ceylon.linux.utility.NumbeFormater;

public class SelectedPaymentFragment extends Fragment {
	private Dbworker dbworker;
	private TextView txt_inv_no, txt_wip_no, txt_paid_amount,
			txt_invoice_amount, txt_pending_amount, text_crdt_amount,
			txt_due_date;
	static double credit_amount;
	private Button button_cash_payment, button_cheque_payment,
			button_bank_dep_payment, button_save_pay, button_remarks,
			button_sync_pay;

	private LinearLayout b_dep_payment_layout, cheque_payment_layout;

	private EditText edit_txt_target_collection_date;
	NumbeFormater nf = new NumbeFormater();

	private int i;
	private int j;
	private int k;
	private int l;

	private SimpleAdapter spinner_adapter;

	private static String invoice_amount = "0";
	private static ArrayList<Cheque_payment_model> cheque_payment_models = new ArrayList<Cheque_payment_model>();
	private static ArrayList<Bank_deposite_payment_model> bank_deposite_payment_models = new ArrayList<Bank_deposite_payment_model>();
	private static ArrayList<Payment_return_item_model> payment_return_item_models = new ArrayList<Payment_return_item_model>();
	private static ArrayList<Garage_loyalty_item_model> garage_loyalty_item_models = new ArrayList<Garage_loyalty_item_model>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.payment, container, false);

		txt_inv_no = (TextView) rootView.findViewById(R.id.txt_inv_no);
		txt_wip_no = (TextView) rootView.findViewById(R.id.txt_wip_no);
		txt_paid_amount = (TextView) rootView
				.findViewById(R.id.txt_paid_amount);
		txt_invoice_amount = (TextView) rootView
				.findViewById(R.id.txt_invoice_amount);
		txt_pending_amount = (TextView) rootView
				.findViewById(R.id.txt_pending_amount);
		text_crdt_amount = (TextView) rootView
				.findViewById(R.id.text_crdt_amount);
		txt_due_date = (TextView) rootView.findViewById(R.id.txt_due_date);

		edit_txt_target_collection_date = (EditText) rootView
				.findViewById(R.id.edit_txt_target_collection_date);

		button_cash_payment = (Button) rootView
				.findViewById(R.id.button_cash_payment);
		button_cheque_payment = (Button) rootView
				.findViewById(R.id.button_cheque_payment);
		button_bank_dep_payment = (Button) rootView
				.findViewById(R.id.button_bank_dep_payment);
		button_save_pay = (Button) rootView.findViewById(R.id.button_save_pay);
		button_remarks = (Button) rootView.findViewById(R.id.button_remarks);
		button_sync_pay = (Button) rootView.findViewById(R.id.button_sync_pay);

		b_dep_payment_layout = (LinearLayout) rootView
				.findViewById(R.id.b_dep_payment_layout);
		b_dep_payment_layout.setVisibility(View.GONE);
		cheque_payment_layout = (LinearLayout) rootView
				.findViewById(R.id.cheque_payment_layout);
		cheque_payment_layout.setVisibility(View.GONE);

		load_data();
		load_bank_data();

		button_cash_payment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				generate_dialog_cash();

			}
		});
		button_cheque_payment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				generate_dialog_cheque();

			}
		});
		button_bank_dep_payment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				generate_dialog_bank_deposit();

			}
		});

		button_remarks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				instantiate_dialog_remarks();

			}
		});

		button_save_pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DbHelper dbHelper = new DbHelper(getActivity());
				SQLiteDatabase database = dbHelper.getWritableDatabase();

				String sql_cheque = "replace into tbl_cheque_payment(deliver_order_id,cheque_no,amount,bankname,realised_date,path,date,time) values(?,?,?,?,?,?,?,?)";
				SQLiteStatement statement_cheque = database
						.compileStatement(sql_cheque);

				String sql_bank_deposit = "replace into tbl_bank_deposit_payment(deliver_order_id,slip_no,amount,bankname, deposit_date,path,date,time,account_no) values(?,?,?,?,?,?,?,?,?)";
				SQLiteStatement statement_bank_deposit = database
						.compileStatement(sql_bank_deposit);

				String sql_return_item = "replace into payment_return_item (deliver_order_id,item_id,qty,amount,reason_id,remarks,path,date,time) values(?,?,?,?,?,?,?,?,?)";

				SQLiteStatement statement_sql_return_item = database
						.compileStatement(sql_return_item);

				java.util.Date date = new java.util.Date();
				java.text.SimpleDateFormat dateFormatter = new java.text.SimpleDateFormat(
						"yyyy-MM-dd");
				java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(
						"HH:mm:ss");

				for (Cheque_payment_model model : cheque_payment_models) {
					Object parameters_cheque[] = {
							PaymentFragment.deliver_order_id,
							model.getCheque_no(), model.getAmount(),
							model.getBank_id(), model.getRealised_date(),
							model.getPath(), dateFormatter.format(date),
							timeFormatter.format(date) };
					DbHandler.performExecuteInsert(statement_cheque,
							parameters_cheque);

				}

				for (Bank_deposite_payment_model model : bank_deposite_payment_models) {
					Log.i("added", "added");

					Object parameters_bank_deposit[] = {
							PaymentFragment.deliver_order_id,
							model.getSlip_no(), model.getAmount(),
							model.getBank_id(), model.getDeposit_date(),
							model.getPath(), dateFormatter.format(date),
							timeFormatter.format(date), model.getAccount_no() };
					DbHandler.performExecuteInsert(statement_bank_deposit,
							parameters_bank_deposit);

				}

				for (Payment_return_item_model model : payment_return_item_models) {

					Object parameters_sql_return_item[] = {
							PaymentFragment.deliver_order_id,
							model.getItem_id(), model.getQty(),
							model.getAmount(), model.getReason_id(),
							model.getRemarks(), model.getFile_path(),
							dateFormatter.format(date),
							timeFormatter.format(date) };
					DbHandler.performExecuteInsert(statement_sql_return_item,
							parameters_sql_return_item);

				}

				String sql_cash = "insert into tbl_garage_loyalty( deliver_order_id,voucher_no,amount,remarks,path,date,time) values(?,?,?,?,?,?,?)";

				SQLiteStatement statement_cash = database
						.compileStatement(sql_cash);

				for (Garage_loyalty_item_model object : garage_loyalty_item_models) {

					Object parameters_cash[] = {
							PaymentFragment.deliver_order_id,
							object.getVoucher_no(), object.getAmount(),
							object.getRemarks(), object.getFile_path(),
							dateFormatter.format(date),
							timeFormatter.format(date) };

					DbHandler.performExecuteInsert(statement_cash,
							parameters_cash);
				}

				cheque_payment_models.clear();
				bank_deposite_payment_models.clear();
				payment_return_item_models.clear();
				garage_loyalty_item_models.clear();
				database.close();
				dbHelper.close();
				Button a = (Button) v;
				a.setEnabled(false);
			}
		});

		button_sync_pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AsyncTask<String, String, String> a = new UploadDataToServer(
						getActivity()) {
					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						super.onPostExecute(result);
						move_to_another_fragment();
					}
				};

				String params[] = {};
				a.execute(params);
			}
		});

		
		edit_txt_target_collection_date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				date_pick_dialog_gen();
				
			}
		});
		
		return rootView;
	}

	public void load_data() {
		dbworker = new Dbworker(getActivity());
		Cursor cursor = dbworker
				.get_all_pending_payment_by_id(PaymentFragment.deliver_order_id);

		if (cursor.moveToFirst()) {
			txt_inv_no.setText(cursor.getString(0));
			txt_wip_no.setText(cursor.getString(1));
			txt_paid_amount.setText(nf
					.format_double_val_to_decimal_Strng(Double
							.parseDouble(cursor.getString(3))));
			txt_invoice_amount.setText(nf
					.format_double_val_to_decimal_Strng(Double
							.parseDouble(cursor.getString(2))));
			invoice_amount = cursor.getString(2);

			txt_pending_amount.setText(nf
					.format_double_val_to_decimal_Strng(Double
							.parseDouble(cursor.getString(4))));
			credit_amount = Double.parseDouble(cursor.getString(4));
			text_crdt_amount.setText(credit_amount + "");
			txt_due_date.setText(cursor.getString(5));
			if(cursor.getString(6).trim().equals("0000-00-00")==false)
			edit_txt_target_collection_date.setText(cursor.getString(6));
		}

		cursor.close();
	}

	public void generate_dialog_cash() {

		Button btn_ok, btn_cancel, btn_photo_capture;
		TextView textmsg, txt_title;
		final EditText edittxt_voucher_no, edittxt_amount, edittxt_voucher_remark;
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		dialog.setContentView(R.layout.mydialoglayout_cash);
		dialog.setCanceledOnTouchOutside(false);

		btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
		btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		btn_photo_capture = (Button) dialog
				.findViewById(R.id.btn_photo_capture);
		textmsg = (TextView) dialog.findViewById(R.id.textmsg);
		dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titleview);
		View vv = dialog.getWindow().getDecorView();
		txt_title = (TextView) vv.findViewById(R.id.title);
		edittxt_voucher_no = (EditText) dialog
				.findViewById(R.id.edittxt_voucher_no);
		edittxt_amount = (EditText) dialog.findViewById(R.id.edittxt_amount);
		edittxt_voucher_remark = (EditText) dialog
				.findViewById(R.id.edittxt_voucher_remark);

		textmsg.setText("Garage Loyalty");
		txt_title.setText("Garage Loyalty");
		btn_ok.setText("Enter");
		btn_cancel.setText("Cancel");

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Garage_loyalty_item_model model = new Garage_loyalty_item_model();
				model.setAmount(edittxt_amount.getText().toString());
				model.setRemarks(edittxt_voucher_remark.getText().toString());
				model.setVoucher_no(edittxt_voucher_no.getText().toString());
				model.setFile_path(CameraActivity.uploadFilePath);
				garage_loyalty_item_models.add(model);
				add_garage_loyalty_row();
				dialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		btn_photo_capture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(getActivity(), CameraActivity.class);
				startActivity(i);
				StaticValues.capture_type = 422;
			}
		});
		dialog.show();

	}

	public void generate_dialog_cheque() {

		Button btn_ok, btn_cancel, btn_cap_photo;
		TextView textmsg, txt_title;
		final Spinner spin_bank;
		final DatePicker date_date_pic;

		final EditText edittxt_amount_cheque, edittxt_cheque_no;
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		dialog.setContentView(R.layout.mydialoglayout_cheque);
		dialog.setCanceledOnTouchOutside(false);

		btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
		btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		btn_cap_photo = (Button) dialog.findViewById(R.id.btn_cap_photo);
		textmsg = (TextView) dialog.findViewById(R.id.textmsg);
		dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titleview);
		View vv = dialog.getWindow().getDecorView();
		txt_title = (TextView) vv.findViewById(R.id.title);
		edittxt_amount_cheque = (EditText) dialog
				.findViewById(R.id.edittxt_amount_cheque);
		edittxt_cheque_no = (EditText) dialog
				.findViewById(R.id.edittxt_cheque_no);
		textmsg.setText("Enter Cheque details");
		txt_title.setText("Cheque");
		btn_ok.setText("Enter");
		btn_cancel.setText("Cancel");

		spin_bank = (Spinner) dialog.findViewById(R.id.spin_bank);
		spin_bank.setAdapter(spinner_adapter);

		date_date_pic = (DatePicker) dialog.findViewById(R.id.date_date_pic);

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> selected_bank = (HashMap<String, String>) spin_bank
						.getSelectedItem();

				int day = date_date_pic.getDayOfMonth();
				int month = date_date_pic.getMonth() + 1;
				int year = date_date_pic.getYear();

				Cheque_payment_model model = new Cheque_payment_model();

				model.setAmount(edittxt_amount_cheque.getText().toString());
				model.setCheque_no(edittxt_cheque_no.getText().toString());
				model.setRealised_date(year + "-" + month + "-" + day);
				model.setBankname(selected_bank.get("bank_name"));
				model.setBank_id(selected_bank.get("bank_id"));
				model.setPath(CameraActivity.uploadFilePath);

				cheque_payment_models.add(model);
				add_cheque_paymants();
				dialog.dismiss();

				set_updated_values();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		btn_cap_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(getActivity(), CameraActivity.class);
				startActivity(i);
				StaticValues.capture_type = 572;

			}
		});

		dialog.show();

		// Bank Name Account No Amount Deposit Date Deposit Slip Image
	}

	public void generate_dialog_bank_deposit() {

		final Spinner spin_bank;
		Button btn_ok, btn_cancel, btn_cap_photo;
		TextView textmsg, txt_title;
		final DatePicker date_date_pic;
		final EditText edittxt_amount, edittxt_account_no, edittxt_slip_no;
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		dialog.setContentView(R.layout.mydialoglayout_bank_dep);
		dialog.setCanceledOnTouchOutside(false);

		btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
		btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		btn_cap_photo = (Button) dialog.findViewById(R.id.btn_cap_photo);
		textmsg = (TextView) dialog.findViewById(R.id.textmsg);
		dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titleview);
		View vv = dialog.getWindow().getDecorView();
		txt_title = (TextView) vv.findViewById(R.id.title);
		edittxt_amount = (EditText) dialog.findViewById(R.id.edittxt_amount);
		edittxt_account_no = (EditText) dialog
				.findViewById(R.id.edittxt_account_no);
		edittxt_slip_no = (EditText) dialog.findViewById(R.id.edittxt_slip_no);
		spin_bank = (Spinner) dialog.findViewById(R.id.spin_bank);
		date_date_pic = (DatePicker) dialog.findViewById(R.id.date_date_pic);

		textmsg.setText("Enter Cheque amount");
		txt_title.setText("Bank Deposit");
		btn_ok.setText("Enter");
		btn_cancel.setText("Cancel");

		spin_bank.setAdapter(spinner_adapter);

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> selected_bank = (HashMap<String, String>) spin_bank
						.getSelectedItem();
				int day = date_date_pic.getDayOfMonth();
				int month = date_date_pic.getMonth() + 1;
				int year = date_date_pic.getYear();
				Bank_deposite_payment_model model = new Bank_deposite_payment_model();
				model.setAmount(edittxt_amount.getText().toString());
				model.setDeposit_date(year + "-" + month + "-" + day);
				model.setBankname(selected_bank.get("bank_name"));
				model.setBank_id(selected_bank.get("bank_id"));
				model.setAccount_no(edittxt_account_no.getText().toString());
				model.setSlip_no(edittxt_slip_no.getText().toString());
				model.setPath(CameraActivity.uploadFilePath);

				bank_deposite_payment_models.add(model);
				add_bank_dep_paymants();
				set_updated_values();
				dialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		btn_cap_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CameraActivity.class);
				startActivity(i);
				StaticValues.capture_type = 983;
			}
		});
		dialog.show();

		// Bank Name Account No Amount Deposit Date Deposit Slip Image
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

	}

	private void load_bank_data() {
		ArrayList<HashMap<String, String>> banks = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> object_default = new HashMap<String, String>();
		object_default.put("bank_id", "0");
		object_default.put("bank_name", "SELECT BANK");
		banks.add(object_default);

		Cursor cur_bank = dbworker.get_banks();
		if (cur_bank.moveToFirst()) {
			do {

				HashMap<String, String> object = new HashMap<String, String>();
				object.put("bank_id", cur_bank.getString(0));
				object.put("bank_name", cur_bank.getString(1));
				banks.add(object);
			} while (cur_bank.moveToNext());
		}

		cur_bank.close();

		spinner_adapter = new SimpleAdapter(getActivity(), banks,
				R.layout.list_view_1uto, new String[] { "bank_name" },
				new int[] { R.id.Name });

	}

	private void add_cheque_paymants() {
		cheque_payment_layout.removeAllViews();
		if (!cheque_payment_models.isEmpty()) {

			cheque_payment_layout.setVisibility(View.VISIBLE);
			LinearLayout linearLayoutChequerow = new LinearLayout(getActivity());
			linearLayoutChequerow
					.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
			linearLayoutChequerow.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout.LayoutParams params = new LayoutParams(0,
					LayoutParams.MATCH_PARENT, 10f);
			params.setMargins(5, 0, 10, 5);

			// /Bank Name Cheque No Amount Realised Date

			TextView aaa = new TextView(getActivity());
			aaa.setLayoutParams(params);
			aaa.setText("Bank Name");
			aaa.setTextSize(18f);
			aaa.setTextColor(color.blacky);
			aaa.setBackgroundColor(Color.parseColor("#FFB56C"));
			aaa.setGravity(Gravity.CENTER);

			TextView bbb = new TextView(getActivity());
			bbb.setLayoutParams(params);
			bbb.setText("Cheque No");
			bbb.setTextSize(18f);
			bbb.setTextColor(color.blacky);
			bbb.setBackgroundColor(Color.parseColor("#FFB56C"));
			bbb.setGravity(Gravity.CENTER);

			TextView ccc = new TextView(getActivity());
			ccc.setLayoutParams(params);
			ccc.setText("Amount");
			ccc.setTextSize(18f);
			ccc.setTextColor(color.blacky);
			ccc.setBackgroundColor(Color.parseColor("#FFB56C"));
			ccc.setGravity(Gravity.CENTER);

			TextView ddd = new TextView(getActivity());
			ddd.setLayoutParams(params);
			ddd.setText("Realised Date");
			ddd.setTextSize(18f);
			ddd.setTextColor(color.blacky);
			ddd.setBackgroundColor(Color.parseColor("#FFB56C"));
			ddd.setGravity(Gravity.CENTER);

			TextView eee = new TextView(getActivity());
			eee.setLayoutParams(params);
			eee.setTextSize(18f);
			eee.setGravity(Gravity.CENTER);

			linearLayoutChequerow.addView(aaa);
			linearLayoutChequerow.addView(bbb);
			linearLayoutChequerow.addView(ccc);
			linearLayoutChequerow.addView(ddd);
			linearLayoutChequerow.addView(eee);
			cheque_payment_layout.addView(linearLayoutChequerow);

			i = 0;
			for (Cheque_payment_model model : cheque_payment_models) {

				LinearLayout linearLayoutChequerowll = new LinearLayout(
						getActivity());
				linearLayoutChequerowll
						.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
				linearLayoutChequerowll.setOrientation(LinearLayout.HORIZONTAL);

				TextView aaaa = new TextView(getActivity());
				aaaa.setLayoutParams(params);
				aaaa.setText(model.getBankname());
				aaaa.setTextSize(18f);
				aaaa.setTextColor(color.blacky);
				aaaa.setBackgroundColor(Color.parseColor("#ECECEC"));
				aaaa.setGravity(Gravity.CENTER);

				TextView bbbb = new TextView(getActivity());
				bbbb.setLayoutParams(params);
				bbbb.setText(model.getCheque_no());
				bbbb.setTextSize(18f);
				bbbb.setTextColor(color.blacky);
				bbbb.setBackgroundColor(Color.parseColor("#ECECEC"));
				bbbb.setGravity(Gravity.CENTER);

				TextView cccc = new TextView(getActivity());
				cccc.setLayoutParams(params);
				cccc.setText(nf.format_double_val_to_decimal_Strng(Double
						.parseDouble(model.getAmount())));
				cccc.setTextSize(18f);
				cccc.setTextColor(color.blacky);
				cccc.setBackgroundColor(Color.parseColor("#ECECEC"));
				cccc.setGravity(Gravity.CENTER);

				TextView dddd = new TextView(getActivity());
				dddd.setLayoutParams(params);
				dddd.setText(model.getRealised_date());
				dddd.setTextSize(18f);
				dddd.setTextColor(color.blacky);
				dddd.setBackgroundColor(Color.parseColor("#ECECEC"));
				dddd.setGravity(Gravity.CENTER);

				int btn_id = i * 13;
				Button eeee = new Button(getActivity());
				eeee.setId(btn_id);
				eeee.setLayoutParams(params);
				eeee.setTextSize(18f);
				eeee.setText("Remove");
				eeee.setGravity(Gravity.CENTER);
				eeee.setBackgroundResource(R.drawable.greenbutton);
				eeee.setOnClickListener(new OnClickListener() {

					int arr_index = i;

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						cheque_payment_models.remove(arr_index);

						add_cheque_paymants();
					}
				});

				linearLayoutChequerowll.addView(aaaa);
				linearLayoutChequerowll.addView(bbbb);
				linearLayoutChequerowll.addView(cccc);
				linearLayoutChequerowll.addView(dddd);
				linearLayoutChequerowll.addView(eeee);
				cheque_payment_layout.addView(linearLayoutChequerowll);
				i++;
			}

		}
	}

	private void add_bank_dep_paymants() {
		b_dep_payment_layout.removeAllViews();
		if (!bank_deposite_payment_models.isEmpty()) {

			b_dep_payment_layout.setVisibility(View.VISIBLE);
			LinearLayout linearLayoutChequerow = new LinearLayout(getActivity());
			linearLayoutChequerow
					.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
			linearLayoutChequerow.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout.LayoutParams params = new LayoutParams(0,
					LayoutParams.MATCH_PARENT, 10f);
			params.setMargins(5, 0, 10, 5);

			// /Account No Amount Deposit Date

			TextView aaa = new TextView(getActivity());
			aaa.setLayoutParams(params);
			aaa.setText("Bank Name");
			aaa.setTextSize(18f);
			aaa.setTextColor(color.blacky);
			aaa.setBackgroundColor(Color.parseColor("#FFB56C"));
			aaa.setGravity(Gravity.CENTER);

			TextView bbb = new TextView(getActivity());
			bbb.setLayoutParams(params);
			bbb.setText("Account No");
			bbb.setTextSize(18f);
			bbb.setTextColor(color.blacky);
			bbb.setBackgroundColor(Color.parseColor("#FFB56C"));
			bbb.setGravity(Gravity.CENTER);

			TextView ccc = new TextView(getActivity());
			ccc.setLayoutParams(params);
			ccc.setText("Amount");
			ccc.setTextSize(18f);
			ccc.setTextColor(color.blacky);
			ccc.setBackgroundColor(Color.parseColor("#FFB56C"));
			ccc.setGravity(Gravity.CENTER);

			TextView ddd = new TextView(getActivity());
			ddd.setLayoutParams(params);
			ddd.setText("Deposit Date");
			ddd.setTextSize(18f);
			ddd.setTextColor(color.blacky);
			ddd.setBackgroundColor(Color.parseColor("#FFB56C"));
			ddd.setGravity(Gravity.CENTER);

			TextView eee = new TextView(getActivity());
			eee.setLayoutParams(params);
			eee.setTextSize(18f);
			eee.setGravity(Gravity.CENTER);

			linearLayoutChequerow.addView(aaa);
			linearLayoutChequerow.addView(bbb);
			linearLayoutChequerow.addView(ccc);
			linearLayoutChequerow.addView(ddd);
			linearLayoutChequerow.addView(eee);
			b_dep_payment_layout.addView(linearLayoutChequerow);

			for (Bank_deposite_payment_model model : bank_deposite_payment_models) {
				LinearLayout linearLayoutChequerowll = new LinearLayout(
						getActivity());
				linearLayoutChequerowll
						.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
				linearLayoutChequerowll.setOrientation(LinearLayout.HORIZONTAL);

				TextView aaaa = new TextView(getActivity());
				aaaa.setLayoutParams(params);
				aaaa.setText(model.getBankname());
				aaaa.setTextSize(18f);
				aaaa.setTextColor(color.blacky);
				aaaa.setBackgroundColor(Color.parseColor("#ECECEC"));
				aaaa.setGravity(Gravity.CENTER);

				TextView bbbb = new TextView(getActivity());
				bbbb.setLayoutParams(params);
				bbbb.setText(model.getAccount_no());
				bbbb.setTextSize(18f);
				bbbb.setTextColor(color.blacky);
				bbbb.setBackgroundColor(Color.parseColor("#ECECEC"));
				bbbb.setGravity(Gravity.CENTER);

				TextView cccc = new TextView(getActivity());
				cccc.setLayoutParams(params);
				cccc.setText(nf.format_double_val_to_decimal_Strng(Double
						.parseDouble(model.getAmount())));
				cccc.setTextSize(18f);
				cccc.setTextColor(color.blacky);
				cccc.setBackgroundColor(Color.parseColor("#ECECEC"));
				cccc.setGravity(Gravity.CENTER);

				TextView dddd = new TextView(getActivity());
				dddd.setLayoutParams(params);
				dddd.setText(model.getDeposit_date());
				dddd.setTextSize(18f);
				dddd.setTextColor(color.blacky);
				dddd.setBackgroundColor(Color.parseColor("#ECECEC"));
				dddd.setGravity(Gravity.CENTER);

				int btn_id = j * 17;
				Button eeee = new Button(getActivity());
				eeee.setId(btn_id);
				eeee.setLayoutParams(params);
				eeee.setTextSize(18f);
				eeee.setText("Remove");
				eeee.setGravity(Gravity.CENTER);
				eeee.setBackgroundResource(R.drawable.greenbutton);
				eeee.setOnClickListener(new OnClickListener() {
					int arr_index = j - 1;

					@Override
					public void onClick(View v) {

						bank_deposite_payment_models.remove(arr_index);
						add_bank_dep_paymants();
						set_updated_values();
					}
				});

				linearLayoutChequerowll.addView(aaaa);
				linearLayoutChequerowll.addView(bbbb);
				linearLayoutChequerowll.addView(cccc);
				linearLayoutChequerowll.addView(dddd);
				linearLayoutChequerowll.addView(eeee);
				b_dep_payment_layout.addView(linearLayoutChequerowll);
				j++;
			}

		}
	}

	public void move_to_another_fragment() {

		Fragment fragment = new HomeFragment();
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle("Home");
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack(null)
				.commit();

	}

	public void set_updated_values() {
		double credit_amount = 0;
		double total_paid_amount = 0;

		// total_paid_amount = total_paid_amount +
		// Double.parseDouble(cash_amount);

		for (Bank_deposite_payment_model bank_deposite_payment_model : bank_deposite_payment_models) {
			if (!bank_deposite_payment_model.getAmount().equals(""))
				total_paid_amount = total_paid_amount
						+ Double.parseDouble(bank_deposite_payment_model
								.getAmount());
		}

		txt_paid_amount.setText(nf
				.format_double_val_to_decimal_Strng(total_paid_amount));

		credit_amount = Double.parseDouble(invoice_amount) - total_paid_amount;
		text_crdt_amount.setText(nf
				.format_double_val_to_decimal_Strng(credit_amount));
		txt_pending_amount.setText(nf
				.format_double_val_to_decimal_Strng(credit_amount));
	}

	public void instantiate_dialog_remarks() {
		AsyncTask<String, String, ArrayList<HashMap<String, String>>> a = new AsyncTask<String, String, ArrayList<HashMap<String, String>>>() {
			public ProgressDialog dialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog = new ProgressDialog(getActivity());
				dialog.setMessage("Loading Item data..............");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}

			@Override
			protected void onPostExecute(
					ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				generate_dialog_remarks(result);

				try {
					dialog.dismiss();
				} catch (Exception e) {

				}
			}

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(
					String... params) {
				Cursor items_cur = dbworker.get_all_items();
				ArrayList<HashMap<String, String>> arrayListitems = new ArrayList<HashMap<String, String>>();

				if (items_cur.moveToFirst()) {
					do {
						HashMap<String, String> item = new HashMap<String, String>();

						item.put("item_id", items_cur.getString(0));
						item.put("item_part_no", items_cur.getString(1));
						item.put("description", items_cur.getString(2));
						arrayListitems.add(item);

					} while (items_cur.moveToNext());
				}
				items_cur.close();
				return arrayListitems;
			}
		};
		String arr[] = { "" };
		a.execute(arr);
	}

	public void generate_dialog_remarks(
			ArrayList<HashMap<String, String>> arrayListitems) {

		final EditText editText_remarks;
		final EditText EditText_qty;
		final EditText EditText_value;
		final Spinner spinner_reason;
		final AutoCompleteTextView editText_partno;
		Button btn_ok, btn_cancel, btn_cap_photo;
		TextView txt_title;
		final TextView text_part_desc;

		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		dialog.setContentView(R.layout.mydialoglayout_remarks);
		dialog.setCanceledOnTouchOutside(false);

		dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titleview);
		View vv = dialog.getWindow().getDecorView();
		txt_title = (TextView) vv.findViewById(R.id.title);

		text_part_desc = (TextView) dialog.findViewById(R.id.text_part_desc);

		editText_remarks = (EditText) dialog
				.findViewById(R.id.editText_remarks);
		EditText_qty = (EditText) dialog.findViewById(R.id.EditText_qty);
		EditText_value = (EditText) dialog.findViewById(R.id.EditText_value);
		editText_partno = (AutoCompleteTextView) dialog
				.findViewById(R.id.editText_partno);

		btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
		btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		btn_cap_photo = (Button) dialog.findViewById(R.id.btn_cap_photo);

		ArrayList<HashMap<String, String>> items_spin = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> a = new HashMap<String, String>();
		a.put("id", "0");
		a.put("reason", "Reason (Drop down to select)");
		HashMap<String, String> b = new HashMap<String, String>();
		b.put("id", "1");
		b.put("reason", "Short & Wrong Supply");
		HashMap<String, String> c = new HashMap<String, String>();
		c.put("id", "2");
		c.put("reason", "Damage / Corroded");
		HashMap<String, String> d = new HashMap<String, String>();
		d.put("id", "3");
		d.put("reason", "Different with Sample");
		HashMap<String, String> e = new HashMap<String, String>();
		e.put("id", "4");
		e.put("reason", "Order Not Complete");
		HashMap<String, String> f = new HashMap<String, String>();
		f.put("id", "5");
		f.put("reason", "Stores Supply Issues");
		HashMap<String, String> g = new HashMap<String, String>();
		g.put("id", "6");
		g.put("reason", "Salesman Errors");
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("id", "7");
		h.put("reason", "Invoicing Errors");

		items_spin.add(a);
		items_spin.add(b);
		items_spin.add(c);
		items_spin.add(d);
		items_spin.add(e);
		items_spin.add(f);
		items_spin.add(g);
		items_spin.add(h);

		spinner_reason = (Spinner) dialog.findViewById(R.id.spinner_reason);
		SimpleAdapter spin = new SimpleAdapter(getActivity(), items_spin,
				R.layout.list_view_1uto, new String[] { "reason" },
				new int[] { R.id.Name });

		spinner_reason.setAdapter(spin);

		txt_title.setText("RETURN ITEM");
		btn_ok.setText("Enter");
		btn_cancel.setText("Cancel");

		SimpleAdapter auto = new SimpleAdapter(getActivity(), arrayListitems,
				R.layout.list_view_1uto, new String[] { "description" },
				new int[] { R.id.Name });

		editText_partno.setThreshold(1);

		editText_partno.setAdapter(auto);

		editText_partno.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> hashMap = (HashMap<String, String>) parent
						.getAdapter().getItem(position);

				editText_partno.setText(hashMap.get("description"));
				editText_partno.setTag(hashMap.get("item_id"));
				text_part_desc.setText(hashMap.get("item_part_no"));

			}
		});

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Payment_return_item_model payment_return_item_model = new Payment_return_item_model();
				payment_return_item_model.setItem_id(editText_partno.getTag()
						.toString());
				payment_return_item_model.setItem_description(text_part_desc
						.getText().toString());
				payment_return_item_model.setItem_no(editText_partno.getText()
						.toString());
				payment_return_item_model.setQty(EditText_qty.getText()
						.toString());
				payment_return_item_model.setAmount(EditText_value.getText()
						.toString());
				payment_return_item_model.setRemarks(editText_remarks.getText()
						.toString());
				payment_return_item_model
						.setFile_path(CameraActivity.uploadFilePath);

				@SuppressWarnings("unchecked")
				HashMap<String, String> a = (HashMap<String, String>) spinner_reason
						.getSelectedItem();

				payment_return_item_model.setReason(a.get("reason"));
				payment_return_item_model.setReason_id(a.get("id"));

				payment_return_item_models.add(payment_return_item_model);
				add_remarks_row();
				dialog.dismiss();
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		btn_cap_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CameraActivity.class);
				startActivity(i);
				StaticValues.capture_type = 105;

			}
		});

		dialog.show();
	}

	void add_remarks_row() {
		LinearLayout b_returns_layout = (LinearLayout) getActivity()
				.findViewById(R.id.b_returns_layout);

		b_returns_layout.removeAllViews();
		if (!payment_return_item_models.isEmpty()) {
			b_returns_layout.setVisibility(View.VISIBLE);
			LinearLayout linearLayoutChequerow = new LinearLayout(getActivity());
			linearLayoutChequerow
					.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
			linearLayoutChequerow.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout.LayoutParams params = new LayoutParams(0,
					LayoutParams.MATCH_PARENT, 10f);
			params.setMargins(5, 0, 10, 5);

			// /Account No Amount Deposit Date

			TextView aaa = new TextView(getActivity());
			aaa.setLayoutParams(params);
			aaa.setText("Item Number");
			aaa.setTextSize(18f);
			aaa.setTextColor(color.blacky);
			aaa.setBackgroundColor(Color.parseColor("#FFB56C"));
			aaa.setGravity(Gravity.CENTER);

			TextView bbb = new TextView(getActivity());
			bbb.setLayoutParams(params);
			bbb.setText("Quantity");
			bbb.setTextSize(18f);
			bbb.setTextColor(color.blacky);
			bbb.setBackgroundColor(Color.parseColor("#FFB56C"));
			bbb.setGravity(Gravity.CENTER);

			TextView ccc = new TextView(getActivity());
			ccc.setLayoutParams(params);
			ccc.setText("Amount");
			ccc.setTextSize(18f);
			ccc.setTextColor(color.blacky);
			ccc.setBackgroundColor(Color.parseColor("#FFB56C"));
			ccc.setGravity(Gravity.CENTER);

			TextView ddd = new TextView(getActivity());
			ddd.setLayoutParams(params);
			ddd.setText("Remarks");
			ddd.setTextSize(18f);
			ddd.setTextColor(color.blacky);
			ddd.setBackgroundColor(Color.parseColor("#FFB56C"));
			ddd.setGravity(Gravity.CENTER);

			TextView eee = new TextView(getActivity());
			eee.setLayoutParams(params);
			eee.setTextSize(18f);
			eee.setGravity(Gravity.CENTER);

			linearLayoutChequerow.addView(aaa);
			linearLayoutChequerow.addView(bbb);
			linearLayoutChequerow.addView(ccc);
			linearLayoutChequerow.addView(ddd);
			linearLayoutChequerow.addView(eee);
			b_returns_layout.addView(linearLayoutChequerow);

			for (Payment_return_item_model model : payment_return_item_models) {
				LinearLayout linearLayoutChequerowll = new LinearLayout(
						getActivity());
				linearLayoutChequerowll
						.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
				linearLayoutChequerowll.setOrientation(LinearLayout.HORIZONTAL);

				TextView aaaa = new TextView(getActivity());
				aaaa.setLayoutParams(params);
				aaaa.setText(model.getItem_no());
				aaaa.setTextSize(18f);
				aaaa.setTextColor(color.blacky);
				aaaa.setBackgroundColor(Color.parseColor("#ECECEC"));
				aaaa.setGravity(Gravity.CENTER);

				TextView bbbb = new TextView(getActivity());
				bbbb.setLayoutParams(params);
				bbbb.setText(model.getQty());
				bbbb.setTextSize(18f);
				bbbb.setTextColor(color.blacky);
				bbbb.setBackgroundColor(Color.parseColor("#ECECEC"));
				bbbb.setGravity(Gravity.CENTER);

				TextView cccc = new TextView(getActivity());
				cccc.setLayoutParams(params);
				cccc.setText(nf.format_double_val_to_decimal_Strng(Double
						.parseDouble(model.getAmount())));
				cccc.setTextSize(18f);
				cccc.setTextColor(color.blacky);
				cccc.setBackgroundColor(Color.parseColor("#ECECEC"));
				cccc.setGravity(Gravity.CENTER);

				TextView dddd = new TextView(getActivity());
				dddd.setLayoutParams(params);
				dddd.setText(model.getRemarks());
				dddd.setTextSize(18f);
				dddd.setTextColor(color.blacky);
				dddd.setBackgroundColor(Color.parseColor("#ECECEC"));
				dddd.setGravity(Gravity.CENTER);

				int btn_id = k * 23;
				Button eeee = new Button(getActivity());
				eeee.setId(btn_id);
				eeee.setLayoutParams(params);
				eeee.setTextSize(18f);
				eeee.setText("Remove");
				eeee.setGravity(Gravity.CENTER);
				eeee.setBackgroundResource(R.drawable.greenbutton);
				eeee.setOnClickListener(new OnClickListener() {
					int arr_index = k - 1;

					@Override
					public void onClick(View v) {

						payment_return_item_models.remove(arr_index);
						add_remarks_row();
						set_updated_values();
					}
				});

				linearLayoutChequerowll.addView(aaaa);
				linearLayoutChequerowll.addView(bbbb);
				linearLayoutChequerowll.addView(cccc);
				linearLayoutChequerowll.addView(dddd);
				linearLayoutChequerowll.addView(eeee);
				b_returns_layout.addView(linearLayoutChequerowll);
				k++;
			}

		}
	}

	void add_garage_loyalty_row() {
		LinearLayout garage_loyalty_view_layout = (LinearLayout) getActivity()
				.findViewById(R.id.garage_loyalty_view_layout);

		garage_loyalty_view_layout.removeAllViews();
		if (!garage_loyalty_item_models.isEmpty()) {
			garage_loyalty_view_layout.setVisibility(View.VISIBLE);
			LinearLayout linearLayoutChequerow = new LinearLayout(getActivity());
			linearLayoutChequerow
					.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
			linearLayoutChequerow.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout.LayoutParams params = new LayoutParams(0,
					LayoutParams.MATCH_PARENT, 10f);
			params.setMargins(5, 0, 10, 5);

			// /Account No Amount Deposit Date

			TextView aaa = new TextView(getActivity());
			aaa.setLayoutParams(params);
			aaa.setText("Voucher number");
			aaa.setTextSize(18f);
			aaa.setTextColor(color.blacky);
			aaa.setBackgroundColor(Color.parseColor("#FFB56C"));
			aaa.setGravity(Gravity.CENTER);

			TextView bbb = new TextView(getActivity());
			bbb.setLayoutParams(params);
			bbb.setText("Amount");
			bbb.setTextSize(18f);
			bbb.setTextColor(color.blacky);
			bbb.setBackgroundColor(Color.parseColor("#FFB56C"));
			bbb.setGravity(Gravity.CENTER);

			TextView ccc = new TextView(getActivity());
			ccc.setLayoutParams(params);
			ccc.setText("Remarks");
			ccc.setTextSize(18f);
			ccc.setTextColor(color.blacky);
			ccc.setBackgroundColor(Color.parseColor("#FFB56C"));
			ccc.setGravity(Gravity.CENTER);

			TextView eee = new TextView(getActivity());
			eee.setLayoutParams(params);
			eee.setTextSize(18f);
			eee.setGravity(Gravity.CENTER);

			linearLayoutChequerow.addView(aaa);
			linearLayoutChequerow.addView(bbb);
			linearLayoutChequerow.addView(ccc);

			linearLayoutChequerow.addView(eee);
			garage_loyalty_view_layout.addView(linearLayoutChequerow);

			for (Garage_loyalty_item_model model : garage_loyalty_item_models) {
				LinearLayout linearLayoutChequerowll = new LinearLayout(
						getActivity());
				linearLayoutChequerowll
						.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
				linearLayoutChequerowll.setOrientation(LinearLayout.HORIZONTAL);

				TextView aaaa = new TextView(getActivity());
				aaaa.setLayoutParams(params);
				aaaa.setText(model.getVoucher_no());
				aaaa.setTextSize(18f);
				aaaa.setTextColor(color.blacky);
				aaaa.setBackgroundColor(Color.parseColor("#ECECEC"));
				aaaa.setGravity(Gravity.CENTER);

				TextView cccc = new TextView(getActivity());
				cccc.setLayoutParams(params);
				cccc.setText(nf.format_double_val_to_decimal_Strng(Double
						.parseDouble(model.getAmount())));
				cccc.setTextSize(18f);
				cccc.setTextColor(color.blacky);
				cccc.setBackgroundColor(Color.parseColor("#ECECEC"));
				cccc.setGravity(Gravity.CENTER);

				TextView dddd = new TextView(getActivity());
				dddd.setLayoutParams(params);
				dddd.setText(model.getRemarks());
				dddd.setTextSize(18f);
				dddd.setTextColor(color.blacky);
				dddd.setBackgroundColor(Color.parseColor("#ECECEC"));
				dddd.setGravity(Gravity.CENTER);

				int btn_id = k * 29;
				Button eeee = new Button(getActivity());
				eeee.setId(btn_id);
				eeee.setLayoutParams(params);
				eeee.setTextSize(18f);
				eeee.setText("Remove");
				eeee.setGravity(Gravity.CENTER);
				eeee.setBackgroundResource(R.drawable.greenbutton);
				eeee.setOnClickListener(new OnClickListener() {
					int arr_index = l - 1;

					@Override
					public void onClick(View v) {

						garage_loyalty_item_models.remove(arr_index);
						add_garage_loyalty_row();
						set_updated_values();
					}
				});

				linearLayoutChequerowll.addView(aaaa);
				linearLayoutChequerowll.addView(cccc);
				linearLayoutChequerowll.addView(dddd);
				linearLayoutChequerowll.addView(eeee);
				garage_loyalty_view_layout.addView(linearLayoutChequerowll);
				l++;
			}

		}
	}
	
	
	private void date_pick_dialog_gen()
	{
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.alert_month_picker);
		dialog.setTitle("Enter Month");
		dialog.setCanceledOnTouchOutside(false);
		Button btn_add = (Button) dialog.findViewById(R.id.btn_add);
		Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		
		final DatePicker date_picker_select_month = (DatePicker) dialog
				.findViewById(R.id.date_picker_select_month);
		
		

		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				
				
				dialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});
		dialog.show();
	}
}
