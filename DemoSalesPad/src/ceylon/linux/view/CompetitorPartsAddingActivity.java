package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.db.Dbworker;
import ceylon.linux.model.CompetitorPartModel;
import ceylon.linux.url.URLS;

import com.example.dimosales.R;

@SuppressWarnings("deprecation")
public class CompetitorPartsAddingActivity extends Activity {

	Button btn_cancel, btn_add_item, btn_take_photo;
	EditText part_no, brand, edittext_importer, edittext_costPriceDealer,
			edittext_sellpricecustomer, edittext_avg_month_mvmnt,
			edittext_overl_mvment_dealer;

	TextView text_partnumber, text_description, text_sell_price_vat;

	Dbworker dbworker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competitor_parts_adding);
		dbworker = new Dbworker(CompetitorPartsAddingActivity.this);

		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				back_press();
			}
		});

		// Button
		btn_add_item = (Button) findViewById(R.id.btn_add_item);
		btn_take_photo = (Button) findViewById(R.id.btn_take_photo);
		btn_take_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(CompetitorPartsAddingActivity.this,
						CameraActivity.class);
				startActivity(i);

			}
		});
		// Edittext
		part_no = (EditText) findViewById(R.id.part_no);
		brand = (EditText) findViewById(R.id.brand);
		edittext_costPriceDealer = (EditText) findViewById(R.id.edittext_costPriceDealer);
		edittext_sellpricecustomer = (EditText) findViewById(R.id.edittext_sellpricecustomer);
		edittext_avg_month_mvmnt = (EditText) findViewById(R.id.edittext_avg_month_mvmnt);
		edittext_overl_mvment_dealer = (EditText) findViewById(R.id.edittext_overl_mvment_dealer);

		edittext_importer = (EditText) findViewById(R.id.edittext_importer);

		edittext_avg_month_mvmnt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				calculation_a();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		edittext_overl_mvment_dealer.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				calculation_a();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		// TextVIew
		text_partnumber = (TextView) findViewById(R.id.text_partnumber);
		text_description = (TextView) findViewById(R.id.text_description);
		text_sell_price_vat = (TextView) findViewById(R.id.text_sell_price_vat);

		

		int position = ItemFragmentCompetitorParts.position_id;

		// dbworker.item_sales_details(dealer_id, item_id)//TODO

		ArrayList<HashMap<String, String>> item_list = ItemFragmentCompetitorParts.item_list;

		text_partnumber.setText(item_list.get(position).get("part_no"));
		text_description.setText(item_list.get(position).get("description"));
		text_sell_price_vat.setText(item_list.get(position)
				.get("selling_price"));

		String[] params = { item_list.get(position).get("item_id"),
				CompetitorPartsFragment.outlet_id + "" };

		new AsyncTask<String, String, String>() {

			@SuppressWarnings("deprecation")
			@Override
			protected String doInBackground(String... params) {

				Jsonhelper jh = new Jsonhelper();

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs
						.add(new BasicNameValuePair("item_id", params[0]));
				nameValuePairs.add(new BasicNameValuePair("dealer_id",
						params[1]));

				JSONObject jsonObject = jh
						.JsonObjectSendToServerPostWithNameValuePare(
								URLS.get_dealer_avg_mvmt, nameValuePairs);

				String a = "0";

				try {
					if(jsonObject!=null)
					{
					a = jsonObject.getString("avg_movement");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// TODO Auto-generated method stub
				return a;
			}

			protected void onPostExecute(String result) {
				
			};

		}.execute(params);

		

		btn_add_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (brand.getText().toString().equals("")
						|| edittext_sellpricecustomer.getText().toString()
								.equals("")
						|| edittext_avg_month_mvmnt.getText().toString()
								.equals("")
						|| edittext_overl_mvment_dealer.getText().toString()
								.equals("")) {
					if (brand.getText().toString().equals("")) {
						brand.setBackgroundColor(Color.parseColor("#FF6666"));
					} else {
						brand.setBackgroundColor(Color.parseColor("#FFFFFF"));
					}
					if (edittext_sellpricecustomer.getText().toString()
							.equals("")) {
						edittext_sellpricecustomer.setBackgroundColor(Color
								.parseColor("#FF6666"));
					} else {
						edittext_sellpricecustomer.setBackgroundColor(Color
								.parseColor("#FFFFFF"));
					}

					if (edittext_avg_month_mvmnt.getText().toString()
							.equals("")) {
						edittext_avg_month_mvmnt.setBackgroundColor(Color
								.parseColor("#FF6666"));
					} else {
						edittext_avg_month_mvmnt.setBackgroundColor(Color
								.parseColor("#FFFFFF"));
					}

					if (edittext_overl_mvment_dealer.getText().toString()
							.equals("")) {
						edittext_overl_mvment_dealer.setBackgroundColor(Color
								.parseColor("#FF6666"));
					} else {
						edittext_overl_mvment_dealer.setBackgroundColor(Color
								.parseColor("#FFFFFF"));
					}

					Toast.makeText(CompetitorPartsAddingActivity.this,
							"Enter values to Compulsary fields",
							Toast.LENGTH_SHORT).show();
				} else {

					CompetitorPartModel partModel = new CompetitorPartModel();
					int position_O = ItemFragmentCompetitorParts.position_id;
					ArrayList<HashMap<String, String>> item_list_O = ItemFragmentCompetitorParts.item_list;

					partModel.setPart_id(item_list_O.get(position_O).get(
							"item_id"));

					partModel.setTgp_number(item_list_O.get(position_O).get(
							"part_no"));

					partModel.setDescription(item_list_O.get(position_O).get(
							"description"));
					partModel.setSelling_price_with_vat(item_list_O.get(
							position_O).get("selling_price"));
					partModel.setPart_number(part_no.getText().toString());
					partModel.setBrand(brand.getText().toString());
					partModel
							.setCost_price_to_the_dealer(edittext_costPriceDealer
									.getText().toString());
					partModel
							.setSelling_price_to_the_customer(edittext_sellpricecustomer
									.getText().toString());
					partModel
							.setAverage_monthly_movement(edittext_overl_mvment_dealer
									.getText().toString());
					partModel
							.setOverall_movement_at_the_dealer(edittext_overl_mvment_dealer
									.getText().toString());
				
					partModel.setImporter(edittext_importer.getText()
							.toString());
					partModel
							.setMovement_of_tgp_at_the_dealer("0");
					partModel
							.setUpload_image_path(CameraActivity.uploadFilePath);

					CompetitorPartsFragment.coArrayList.add(partModel);
					back_press();
				}

			}
		});

	}

	@Override
	public void onBackPressed() {

	}

	public void back_press() {
		super.onBackPressed();
		dbworker.close();
	}

	public void calculation_a() {

		double market_share_with_brand = 0;
		double mvment_tgp_dealer = 0;
		double avg_mnthl_mvmnt = 0;
		double overall_mvment_dealer = 0;
		double market_share_overall = 0;

		
			mvment_tgp_dealer = 0.00;
		

		if (edittext_avg_month_mvmnt.getText().toString().equals("")) {

		} else {
			avg_mnthl_mvmnt = Double.parseDouble(edittext_avg_month_mvmnt
					.getText().toString());
		}

		if (edittext_overl_mvment_dealer.getText().toString().equals("")) {

		} else {
			overall_mvment_dealer = Double
					.parseDouble(edittext_overl_mvment_dealer.getText()
							.toString());
		}

		market_share_with_brand = mvment_tgp_dealer
				/ (mvment_tgp_dealer + avg_mnthl_mvmnt) * 100;

		market_share_overall = mvment_tgp_dealer / overall_mvment_dealer * 100;

		

	}

}
