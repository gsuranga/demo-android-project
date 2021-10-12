package ceylon.linux.view;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint({"NewApi", "CutPasteId"})
public class Fragment_Competitior_parts extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_competitor_parts,container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getActivity().onConfigurationChanged(null);

		final TableLayout tl = (TableLayout) rootView.findViewById(R.id.show_competitor_product);
		// /Table Header

		TableRow tr_head = new TableRow(getActivity());
		tr_head.setId(10);
		tr_head.setBackgroundColor(Color.rgb(153, 153, 153));
		tr_head.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView add = new TextView(getActivity());
		add.setId(000000);
		add.setText(" ");

		add.setPadding(5, 5, 5, 5);
		tr_head.addView(add);

		TextView label_date = new TextView(getActivity());
		// label_date.setId(111111);
		label_date.setText("TGP Number");

		label_date.setPadding(5, 5, 5, 5);
		tr_head.addView(label_date);// add the column to the table row here

		TextView header_previous_stock = new TextView(getActivity());
		header_previous_stock.setId(222222);// define id that must be unique
		header_previous_stock.setText("Description"); // set the text for the
		// set the color
		header_previous_stock.setPadding(5, 5, 5, 5); // set the padding (if
		tr_head.addView(header_previous_stock); // add the column to the table
		// row

		TextView header_current_stock = new TextView(getActivity());
		// header_current_stock.setId(333333);// define id that must be unique
		header_current_stock.setText("current stock"); // set the text for the
		// set the color
		header_current_stock.setPadding(5, 5, 5, 5); // set the padding (if
		tr_head.addView(header_current_stock);

		TextView header_date = new TextView(getActivity());
		// header_date.setId(21);// define id that must be unique
		header_date.setText("SP of TGP \n Number"); // set the text for the
		// header
		// set the color
		header_date.setPadding(5, 5, 5, 5); // set the padding (if required)
		tr_head.addView(header_date);

		TextView header_total_off_take = new TextView(getActivity());
		// header_total_off_take.setId(444444);// define id that must be unique
		header_total_off_take.setText("Competitor Part \n number"); // set the
		// text for
		// the
		// header
		// set the color
		header_total_off_take.setPadding(5, 5, 5, 5); // set the padding (if
		// required)
		tr_head.addView(header_total_off_take);

		TextView header_weekly_off_take = new TextView(getActivity());
		// header_weekly_off_take.setId(555555);// define id that must be unique
		header_weekly_off_take.setText("Brand"); // set the text for
		// the header
		// set the color
		header_weekly_off_take.setPadding(5, 5, 5, 5); // set the padding (if
		// required)
		tr_head.addView(header_weekly_off_take);

		TextView importer = new TextView(getActivity());
		// importer.setId(666666);// define id that must be unique
		importer.setText("Importer"); // set the text for
		// the header
		// set the color
		importer.setPadding(5, 5, 5, 5); // set the padding (if
		// required)
		tr_head.addView(importer);

		TextView selling_Price_of_non_genuine = new TextView(getActivity());
		// selling_Price_of_non_genuine.setId(777777);// define id that must be
		// unique
		selling_Price_of_non_genuine.setText("Selling Price of \n non genuine"); // set
		// the
		// text
		// for
		// the header

		selling_Price_of_non_genuine.setPadding(5, 5, 5, 5); // set the padding
		// (if
		// required)
		tr_head.addView(selling_Price_of_non_genuine);

		// Movement of non TGP

		TextView movement_of_non_TGP = new TextView(getActivity());
		// movement_of_non_TGP.setId(888888);// define id that must be unique
		movement_of_non_TGP.setText("Movement of \nnon TGP"); // set the text
		// for
		// the header

		movement_of_non_TGP.setPadding(5, 5, 5, 5); // set the padding (if
		// required)
		tr_head.addView(movement_of_non_TGP);

		// Remark

		TextView remark = new TextView(getActivity());
		// remark.setId(999999);// define id that must be unique
		remark.setText("Remark"); // set the text for
		// the header

		remark.setPadding(5, 5, 5, 5); // set the padding (if
		// required)
		tr_head.addView(remark);

		TextView remove = new TextView(getActivity());
		// remove.setId(454545);// define id that must be unique
		remove.setText(" "); // set the text for
		// the header

		remove.setPadding(5, 5, 5, 5); // set the padding (if
		// required)
		tr_head.addView(remove);

		tl.addView(tr_head, new TableLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		// /END OF Table header
		// start of initial row
		TableRow tr_intial_row = new TableRow(getActivity());
		tr_intial_row.setId(1);
		tr_intial_row.setBackgroundColor(Color.rgb(250, 250, 250));
		tr_intial_row.setLayoutParams(new LayoutParams(
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		// View inflatedView =
		// getLayoutInflater().inflate(R.layout.other_layout, null); TextView
		// text = (TextView) inflatedView.findViewById(R.id.text_view);
		View inflatedView = getActivity().getLayoutInflater().inflate(
			R.layout.button_add, null);

		Button add_btn = (Button) inflatedView.findViewById(R.id.btn_add);
		add_btn.setId(454545);// define id that must be unique
		add_btn.setText("+"); // set the text for
		// add_btn.setBackgroundResource(R.drawable.button_shape); // the header

		// add_btn.setPadding(5, 5, 5, 5);

		// add_btn.setBackgroundColor(Color.rgb(0, 153, 153));
		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				add_tbl_row(tl);
			}
		});// set the padding (if
		// required)
		tr_intial_row.addView(add_btn);

		AutoCompleteTextView auto_tgp_num = new AutoCompleteTextView(
			getActivity());
		auto_tgp_num.setId(454545);
		auto_tgp_num.setHint("enter tgp number");
		auto_tgp_num.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(auto_tgp_num);

		TextView text_description = new TextView(getActivity());
		text_description.setId(3);

		text_description.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(text_description);

		EditText edit_cur_stock = new EditText(getActivity());
		edit_cur_stock.setId(3);
		edit_cur_stock.setHint("enter current stock");
		edit_cur_stock.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_cur_stock);

		EditText edit_sp_of_tgp_no = new EditText(getActivity());
		edit_sp_of_tgp_no.setId(3);
		edit_sp_of_tgp_no.setHint("enter number");
		edit_sp_of_tgp_no.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_sp_of_tgp_no);

		EditText edit_com_petitor = new EditText(getActivity());
		edit_com_petitor.setId(3);
		edit_com_petitor.setHint("enter number");
		edit_com_petitor.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_com_petitor);

		EditText edit_brand = new EditText(getActivity());
		edit_brand.setId(3);
		edit_brand.setHint("enter number");
		edit_brand.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_brand);

		EditText edit_impoter = new EditText(getActivity());
		edit_impoter.setId(3);
		edit_impoter.setHint("enter number");
		edit_impoter.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_impoter);

		EditText edit_selling_price_ngp = new EditText(getActivity());
		edit_selling_price_ngp.setId(3);
		edit_selling_price_ngp.setHint("enter number");
		edit_selling_price_ngp.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_selling_price_ngp);

		EditText edit_movement_ngp = new EditText(getActivity());
		edit_movement_ngp.setId(3);
		edit_movement_ngp.setHint("enter number");
		edit_movement_ngp.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_movement_ngp);

		EditText edit_remarks = new EditText(getActivity());
		edit_remarks.setId(3);
		edit_remarks.setHint("enter number");
		edit_remarks.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_remarks);

		View inflatedView_1 = getActivity().getLayoutInflater().inflate(
			R.layout.button_remove, null);

		Button remove_btn = (Button) inflatedView_1
			.findViewById(R.id.btn_remove);
		remove_btn.setId(454545);// define id that must be unique
		remove_btn.setText("-"); // set the text for
		tr_intial_row.addView(remove_btn);

		// End of initial row

		tl.addView(tr_intial_row, new TableLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		return rootView;
	}

	@SuppressLint("CutPasteId")
	public void add_tbl_row(final TableLayout tl)// to add a table row
	{
		TableRow tr_intial_row = new TableRow(getActivity());
		tr_intial_row.setId(1);
		tr_intial_row.setBackgroundColor(Color.rgb(250, 250, 250));
		tr_intial_row.setLayoutParams(new LayoutParams(
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		View inflatedView = getActivity().getLayoutInflater().inflate(
			R.layout.button_add, null);

		Button add_btn = (Button) inflatedView.findViewById(R.id.btn_add);
		add_btn.setId(454545);// define id that must be unique
		add_btn.setText("+");

		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				add_tbl_row(tl);
			}
		});// // the header

		add_btn.setPadding(5, 5, 5, 5); // set the padding (if
		// required)
		tr_intial_row.addView(add_btn);

		AutoCompleteTextView auto_tgp_num = new AutoCompleteTextView(
			getActivity());
		auto_tgp_num.setId(454545);
		auto_tgp_num.setHint("enter tgp number");
		auto_tgp_num.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(auto_tgp_num);

		TextView text_description = new TextView(getActivity());
		text_description.setId(3);

		text_description.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(text_description);

		EditText edit_cur_stock = new EditText(getActivity());
		edit_cur_stock.setId(3);
		edit_cur_stock.setHint("enter current stock");
		edit_cur_stock.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_cur_stock);

		EditText edit_sp_of_tgp_no = new EditText(getActivity());
		edit_sp_of_tgp_no.setId(3);
		edit_sp_of_tgp_no.setHint("enter number");
		edit_sp_of_tgp_no.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_sp_of_tgp_no);

		EditText edit_com_petitor = new EditText(getActivity());
		edit_com_petitor.setId(3);
		edit_com_petitor.setHint("enter number");
		edit_com_petitor.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_com_petitor);

		EditText edit_brand = new EditText(getActivity());
		edit_brand.setId(3);
		edit_brand.setHint("enter number");
		edit_brand.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_brand);

		EditText edit_impoter = new EditText(getActivity());
		edit_impoter.setId(3);
		edit_impoter.setHint("enter number");
		edit_impoter.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_impoter);

		EditText edit_selling_price_ngp = new EditText(getActivity());
		edit_selling_price_ngp.setId(3);
		edit_selling_price_ngp.setHint("enter number");
		edit_selling_price_ngp.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_selling_price_ngp);

		EditText edit_movement_ngp = new EditText(getActivity());
		edit_movement_ngp.setId(3);
		edit_movement_ngp.setHint("enter number");
		edit_movement_ngp.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_movement_ngp);

		EditText edit_remarks = new EditText(getActivity());
		edit_remarks.setId(3);
		edit_remarks.setHint("enter number");
		edit_remarks.setPadding(5, 5, 5, 5);
		tr_intial_row.addView(edit_remarks);

		View inflatedView_1 = getActivity().getLayoutInflater().inflate(
			R.layout.button_remove, null);

		Button remove_btn = (Button) inflatedView_1
			.findViewById(R.id.btn_remove);
		remove_btn.setId(454545);// define id that must be unique
		remove_btn.setText("-"); // set the text for
		tr_intial_row.addView(remove_btn);

		tl.addView(tr_intial_row, new TableLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
	                           long arg3) {
		// TODO Auto-generated method stub

	}

}
