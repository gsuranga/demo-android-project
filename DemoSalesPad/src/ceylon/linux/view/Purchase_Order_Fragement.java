package ceylon.linux.view;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dimosales.R;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ceylon.linux.controller.InternetObserver;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.controller.StaticValues;
import ceylon.linux.db.Dbworker;
import ceylon.linux.emailgenerator.GMailSender;
import ceylon.linux.model.Purchase_order_item_model;
import ceylon.linux.url.URLS;
import ceylon.linux.utility.BatteryService;
import ceylon.linux.utility.GpsReceiver;
import ceylon.linux.utility.Utility;

@SuppressLint("NewApi")
public class Purchase_Order_Fragement extends Fragment {
	TextView display_total_amount, display_total_amount_with_vat_and_discount,
			show_disount, credit_limit, over_due, outstnding;
	public String iternery_status = "0";
	SharedPreferences userdata;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dbworker = new Dbworker(getActivity());
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		View rootView = inflater.inflate(R.layout.purchase_order, container,
				false);
		display_total_amount = (TextView) rootView
				.findViewById(R.id.fullAmount);
		item_add = (Button) rootView.findViewById(R.id.add_item);
		item_add.setOnClickListener(add_item);
		finish_bill = (Button) rootView.findViewById(R.id.finish_bill);
		btn_save_order = (Button) rootView.findViewById(R.id.btn_save_order_po);

		if (ViewSavedOrdersFragment.purchase_order_edit == 0) {

			finish_bill.setOnClickListener(finish_bill_event);
			btn_save_order.setOnClickListener(hold_order);
		} else if (ViewSavedOrdersFragment.purchase_order_edit == 1) {
			finish_bill.setOnClickListener(finish_bill_event_edit);
			btn_save_order.setOnClickListener(hold_order2);
		} else if (ViewSavedOrdersFragment.purchase_order_edit == 2) {
			finish_bill.setOnClickListener(finish_bill_event);
			btn_save_order.setOnClickListener(hold_order);
		}

		display_total_amount_with_vat_and_discount = (TextView) rootView
				.findViewById(R.id.fullAmount_with_vat);
		userdata = getActivity().getSharedPreferences("USERDATA",
				Context.MODE_PRIVATE);

		show_disount = (TextView) rootView.findViewById(R.id.discount);
		show_disount.setText(HomeFragment.discount_percentage + "%");
		move_to_item = (Button) rootView.findViewById(R.id.move_item);
		move_to_item.setOnClickListener(move_to_item_fragment);
		credit_limit = (TextView) rootView.findViewById(R.id.credit);
		outstnding = (TextView) rootView.findViewById(R.id.outstanding);
		over_due = (TextView) rootView.findViewById(R.id.OverDue);
		credit_limit.setText(HomeFragment.credit_limit);
		outstnding.setText(HomeFragment.outstanding_amount);
		over_due.setText(HomeFragment.overdue_amount);

		tl = (TableLayout) rootView.findViewById(R.id.show_purchase_order);

		if (ViewSavedOrdersFragment.purchase_order_edit == 0) {
			try {
				populate_purchase_order();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

		} else if (ViewSavedOrdersFragment.purchase_order_edit == 1) {

			try {
				populate_saved_order_details();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}

		return rootView;
	}

	OnClickListener finish_bill_event = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (StaticValues.order_item_models.size() == 0) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setTitle("Message");
				alertDialogBuilder
						.setMessage("Add items before finish the bill")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			} else {

				final Dialog dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.user_pass_custom_dialog);
				final RadioButton rBtnSuggestionOrder = (RadioButton) dialog
						.findViewById(R.id.rBtnSuggestionOrder);
				final RadioButton rBtnAcceptedOrder = (RadioButton) dialog
						.findViewById(R.id.rBtnAcceptedOrder);
				final RadioButton rBtnCallOrder = (RadioButton) dialog
						.findViewById(R.id.rBtnCallOrder);
				final EditText eTUserName = (EditText) dialog
						.findViewById(R.id.eTUserName);
				final EditText eTPassword = (EditText) dialog
						.findViewById(R.id.eTPassword);
				final TextView error = (TextView) dialog
						.findViewById(R.id.error);
				String s = "";

				if (Double.valueOf(display_total_amount_with_vat_and_discount
						.getText().toString()) < Double.valueOf(outstnding
						.getText().toString())) {

					s += "Outstanding,";

				}
				if (Double.valueOf(display_total_amount_with_vat_and_discount
						.getText().toString()) < Double.valueOf(credit_limit
						.getText().toString())) {
					s += "Credit,";

				}
				if (Double.valueOf(display_total_amount_with_vat_and_discount
						.getText().toString()) < Double.valueOf(over_due
						.getText().toString())) {
					s += "Over due ";
				}
				s += "is Greater than Total amount";
				dialog.setTitle(s);

				Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
				Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
				rBtnSuggestionOrder.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						rBtnAcceptedOrder.setChecked(false);
						rBtnCallOrder.setChecked(false);
						eTUserName.setEnabled(false);
						eTPassword.setEnabled(false);
						eTUserName.setText("");
					}
				});
				rBtnAcceptedOrder.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						rBtnSuggestionOrder.setChecked(false);
						rBtnCallOrder.setChecked(false);

						eTPassword.setEnabled(true);

						Cursor cursor = dbworker
								.get_username_password_of_dealer(HomeFragment.dealer_id);
						if (cursor.moveToFirst()) {
							int username = cursor.getColumnIndex("username");

							String userName = cursor.getString(username);
							eTUserName.setText(userName);
							eTUserName.setEnabled(false);
						}
						cursor.close();

					}
				});

				rBtnCallOrder.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						rBtnAcceptedOrder.setChecked(false);
						rBtnSuggestionOrder.setChecked(false);
						eTUserName.setText("");
						eTUserName.setEnabled(false);
						eTPassword.setEnabled(false);
					}
				});

				eTUserName.setEnabled(false);
				eTPassword.setEnabled(false);
				btnYes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						if (rBtnSuggestionOrder.isChecked()) {

							permissionGranted(v, 3,0);
							dialog.cancel();

						}

						else if (rBtnCallOrder.isChecked()) {

							permissionGranted(v, 0,1);

							dialog.dismiss();

						}

						else {

							Cursor cursor = dbworker
									.get_username_password_of_dealer(HomeFragment.dealer_id);
							if (cursor.moveToFirst()) {
								int username = cursor
										.getColumnIndex("username");
								int dealer_password = cursor
										.getColumnIndex("dealer_password");
								String userName = cursor.getString(username);
								String password = cursor
										.getString(dealer_password);
								// Log.i("username",
								// cursor.getString(username));
								// Log.i("username", password);

								if (userName.trim().equalsIgnoreCase(
										eTUserName.getText().toString().trim())
										&& password.trim().equalsIgnoreCase(
												eTPassword.getText().toString()
														.trim())) {

									permissionGranted(v, 0,0);

								} else {
									error.setText("UserName and Password Incorrect");
								}

							}

							cursor.close();
							dialog.dismiss();
						}
					}

				});

				btnNo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
				dialog.show();
			}
		}
	};


	Dbworker dbworker;
	TableLayout tl;

	Double total = 0.0;
	Button finish_bill;
	Button item_add, btn_save_order, move_to_item;
	DecimalFormat df = new DecimalFormat("#.##");
	HashMap<String, String> purchase_order_details;

	OnClickListener remove_item = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Button a = (Button) v;

			StaticValues.order_item_models.remove(a.getId() - 250);

			ViewGroup viewgroup = (ViewGroup) v.getParent().getParent();
			viewgroup.removeAllViews();
			populate_purchase_order();

		}

	};

	android.view.View.OnClickListener remove_itemx = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ViewGroup viewgroup = (ViewGroup) v.getParent();
			// HashMap<String, String> removitems = new HashMap<String,
			// String>();
			Purchase_order_item_model modelx = new Purchase_order_item_model();
			for (int i = 0; i < viewgroup.getChildCount(); i++) {
				View vv = viewgroup.getChildAt(i);

				if (vv instanceof TextView) {
					TextView t = (TextView) vv;
					if (t.getTag().equals("description")) {

						modelx.setDescription(((TextView) vv).getText()
								.toString());
					}
					if (t.getTag().equals("part_no")) {

						modelx.setPart_no(((TextView) vv).getText().toString());
						ItemFragment.clicked_items.remove(((TextView) vv)
								.getText().toString());

					} else if (t.getTag().equals("quantity")) {

						modelx.setQty(((TextView) vv).getText().toString());

					} else if (vv.getTag().equals("selling_price")) {

						modelx.setSelling_price(((TextView) vv).getText()
								.toString());

					} else if (vv.getTag().equals("discount_percentag" + "e")) {
						/*
						 * modelx.put("discount_percentage", ((TextView) vv)
						 * .getText().toString());
						 */
					}

				}
			}
			Double r_sellingprice = 0.0;
			Double r_quantity = 0.0;
			Double r_discount = 0.0;

			r_sellingprice = Double.valueOf(modelx.getSelling_price());
			r_quantity = Double.valueOf(modelx.getQty());

			Double display_total = Double.valueOf(display_total_amount
					.getText().toString());

			Double lastamount = display_total - r_sellingprice * r_quantity;
			Double lastamount_with_vat = lastamount
					+ Double.valueOf(userdata.getString("vat", "")) / 100;
			display_total_amount.setText(df.format(lastamount));

			if (lastamount < Double.valueOf(userdata.getString("vat", "")) / 100) {
				display_total_amount_with_vat_and_discount.setText("");

			} else {
				display_total_amount_with_vat_and_discount.setText(String
						.valueOf(lastamount_with_vat));
			}

			// TODO
			// TODO
			int index;
			if ((StaticValues.order_item_models.remove(modelx))) {
				// wrapper.remove(index);

				if (StaticValues.order_item_models.isEmpty()) {
					getFragmentManager().popBackStack();

				}

			}

			viewgroup.removeAllViews();

		}
	};

	OnClickListener remove_item_edit = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// TODO
			TableRow tr = (TableRow) v.getParent();
			tr.setVisibility(View.GONE);
			TextView id = (TextView) tr.getChildAt(8);
			dbworker.delete_purchase_item_by_id(id.getText().toString());

		}
	};

	OnClickListener add_item = new OnClickListener() {

		@Override
		public void onClick(View v) {

			move_to_another_fragment(new ItemFragment(), "Items");

		}
	};
	OnClickListener hold_order = new OnClickListener() {

		@Override
		public void onClick(View v) {

			save_order_fist_time();

		}
	};

	OnClickListener hold_order2 = new OnClickListener() {

		@Override
		public void onClick(View v) {

			save_order_repeat();

		}
	};
	android.view.View.OnClickListener move_to_item_fragment = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// Intent intent = new Intent(getActivity(), ItemFragment.class);
			// startActivity(intent);
			move_to_another_fragment(new ItemFragment(), "Items ");
			InvoiceInfoFragment.status = "0";

		}
	};
	// Used to upadte the
	OnClickListener update = new OnClickListener() {
		HashMap<String, String> update_hashmap = new HashMap<String, String>();
		String part_no;
		String updated_quantity;
		String exist_quantity;

		String updated_comment;

		String selling_price;

		@Override
		public void onClick(final View arg0) {

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					getActivity());
			alertDialog.setTitle("Values");
			final EditText edited_quantity = new EditText(getActivity());
			final EditText edited_comment = new EditText(getActivity());

			edited_quantity.setHint("Quantity");
			edited_comment.setHint("Edit Comment");
			edited_quantity.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			TextView t = new TextView(getActivity());

			Double vat = Double.valueOf(userdata.getString("vat", ""));
			t.setBackgroundColor(Color.CYAN);
			ViewGroup parentview = (ViewGroup) arg0.getParent();
			for (int i = 0; i < parentview.getChildCount(); i++) {

				View ChildView = parentview.getChildAt(i);

				if (ChildView.getTag().equals("part_no")) {
					TextView part_no = (TextView) ChildView;
					Cursor cursor = dbworker.get_item_details(part_no.getText()
							.toString());

					if (cursor != null) {
						cursor.moveToFirst();
						while (!cursor.isAfterLast()) {

							String vsdstock = cursor.getString(4);
							selling_price = cursor.getString(3);
							Double selling_price_with_vat = Double
									.valueOf(selling_price)
									+ (Double.valueOf(selling_price) * vat / 100);
							t.setText("Selling Price With VAT:-"
									+ Utility
											.formatStringAmounts(selling_price_with_vat
													.toString()) + "   "
									+ "VSD stock is:-" + vsdstock);
							cursor.moveToNext();

						}
					}

				} else if (ChildView.getTag().equals("description")) {
					TextView des = (TextView) ChildView;
					alertDialog.setTitle(des.getText().toString());

				} else if (ChildView.getTag().equals("quantity")) {
					TextView qty = (TextView) ChildView;
					edited_quantity.setText(qty.getText().toString());

				}

				else if (ChildView.getTag().equals("comment")) {
					TextView comment = (TextView) ChildView;
					edited_quantity
							.setText(edited_comment.getText().toString());

				}
			}

			LinearLayout ll = new LinearLayout(getActivity());
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.addView(edited_quantity);
			ll.addView(t);
			ll.addView(edited_comment);

			alertDialog.setView(ll);
			alertDialog.setCancelable(false);
			alertDialog.setPositiveButton("Add Quantity",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							ViewGroup parentview = (ViewGroup) arg0.getParent();
							for (int i = 0; i < parentview.getChildCount(); i++) {

								View ChildView = parentview.getChildAt(i);
								if (ChildView.getTag().equals("part_no")) {
									TextView part = (TextView) ChildView;
									part_no = part.getText().toString();

								} else if (ChildView.getTag()
										.equals("quantity")) {
									TextView qty = (TextView) ChildView;
									exist_quantity = qty.getText().toString();
									qty.setText(edited_quantity.getText()
											.toString());
									updated_quantity = qty.getText().toString();

								}

								else if (ChildView.getTag().equals("comment")) {
									TextView comment = (TextView) ChildView;
									comment.setText(edited_comment.getText()
											.toString());
									updated_comment = edited_comment.getText()
											.toString();
								}

							}
							update_hashmap.put("part_no", part_no);
							update_hashmap.put("quantity", updated_quantity);
							update_hashmap.put("comment", updated_comment);

							/*
							 * wrapper.get(
							 * wrapper.removeDuplicates(update_hashmap))
							 * .put("quantity", updated_quantity); wrapper.get(
							 * wrapper.removeDuplicates(update_hashmap))
							 * .put("comment", updated_comment);
							 */
							// TODO
							find_new_total(updated_quantity, selling_price,
									exist_quantity);

						}
					}).setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

			AlertDialog alert = alertDialog.create();
			alert.show();

		}
	};

	OnClickListener update_edit = new OnClickListener() {

		@Override
		public void onClick(View v) {

			TableRow tr = (TableRow) v.getParent();

			TextView discrp = (TextView) tr.getChildAt(0);
			final TextView comment = (TextView) tr.getChildAt(5);
			final TextView qty = (TextView) tr.getChildAt(2);
			TextView part_no = (TextView) tr.getChildAt(1);
			TextView id = (TextView) tr.getChildAt(8);

			final Dialog dialog = new Dialog(getActivity());

			dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			dialog.setContentView(R.layout.edit_purchase_item_dialog);

			dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.titleview);
			View vv = dialog.getWindow().getDecorView();
			TextView txt_title = (TextView) vv.findViewById(R.id.title);

			Button button_update = (Button) dialog
					.findViewById(R.id.button_update);

			button_update.setTag(id.getText());

			Button button_cancel = (Button) dialog
					.findViewById(R.id.button_cancel);

			button_update.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LinearLayout l = (LinearLayout) v.getParent();

					EditText edit_text_comment = (EditText) l.getChildAt(0);
					EditText edit_text_qty = (EditText) l.getChildAt(1);
					Button btn_up = (Button) l.getChildAt(2);

					if (btn_up.getTag().toString().equals("xxx")) {
						comment.setText(edit_text_comment.getText().toString());
						qty.setText(edit_text_qty.getText().toString());

					} else {
						dbworker.update_purchase_item_by_id(btn_up.getTag()
								.toString(), edit_text_comment.getText()
								.toString(), edit_text_qty.getText().toString());
						comment.setText(edit_text_comment.getText().toString());
						qty.setText(edit_text_qty.getText().toString());

					}

					dialog.dismiss();

				}
			});

			button_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});

			txt_title.setText(discrp.getText() + "(" + part_no.getText() + ")");

			EditText edit_text_comment = (EditText) dialog
					.findViewById(R.id.edit_text_comment);
			edit_text_comment.setText(comment.getText());

			EditText edit_text_qty = (EditText) dialog
					.findViewById(R.id.edit_text_qty);
			edit_text_qty.setText(qty.getText());

			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		}
	};

	private Location location;

	public Purchase_Order_Fragement() {
	}

	// this code for update existing purchase order

	public void populate_purchase_order() {

		total = 0.0;

		if (ViewSavedOrdersFragment.purchase_order_edit != 0) {

		}

		display_total_amount_with_vat_and_discount.setText(df.format(0.00));
		display_total_amount.setText(df.format((0.00)));

		purchase_order_details = new HashMap<String, String>();
		if (ItemFragment.item_id.equals("-1")) {
			TableRow tr_head = new TableRow(getActivity());
			tr_head.setId(10);
			tr_head.setBackgroundColor(Color.GRAY);
			tr_head.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

			TextView label_description = new TextView(getActivity());
			label_description.setId(20);
			label_description.setText("Description");
			label_description.setTextColor(Color.RED);
			label_description.setGravity(Gravity.CENTER);
			label_description.setPadding(5, 5, 5, 5);
			tr_head.addView(label_description);// add the column to the table
			// row
			// here

			TextView label_part_no = new TextView(getActivity());
			label_part_no.setId(21);// define id that must be unique
			label_part_no.setText("Part Number"); // set the text for the
			label_part_no.setTextColor(Color.RED); // set the color
			label_part_no.setGravity(Gravity.CENTER);
			label_part_no.setPadding(5, 5, 5, 5);
			tr_head.addView(label_part_no); // add the column to the table

			TextView label_quantity = new TextView(getActivity());
			label_quantity.setId(21);// define id that must be unique
			label_quantity.setText("Quantity"); // set the text for the
			label_quantity.setTextColor(Color.RED); // set the color
			label_quantity.setPadding(5, 5, 5, 5);
			label_quantity.setGravity(Gravity.CENTER);
			tr_head.addView(label_quantity);

			TextView label_selling_price = new TextView(getActivity());
			label_selling_price.setId(21);// define id that must be unique
			label_selling_price.setText("Selling Price"); // set the text for
			label_selling_price.setTextColor(Color.RED); // set the color
			label_selling_price.setPadding(5, 5, 5, 5);
			label_selling_price.setGravity(Gravity.CENTER);
			tr_head.addView(label_selling_price);

			TextView label_discount_percentage = new TextView(getActivity());
			label_discount_percentage.setId(21);// define id that must be unique
			label_discount_percentage.setText("Average Movement At Dealer"); // set
																				// the
			label_discount_percentage.setGravity(Gravity.CENTER); // text for
			label_discount_percentage.setTextColor(Color.RED); // set the color
			label_discount_percentage.setPadding(5, 5, 5, 5);
			tr_head.addView(label_discount_percentage);

			TextView label_comment = new TextView(getActivity());
			label_comment.setId(21);// define id that must be unique
			label_comment.setText("Comment"); // set the
			label_comment.setGravity(Gravity.CENTER); // text for
			label_comment.setTextColor(Color.RED); // set the color
			label_comment.setPadding(5, 5, 5, 5);
			tr_head.addView(label_comment);

			tl.addView(tr_head, new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			Integer count = 0;

			Double qty = 0.0;
			Double discount = 0.0;
			Double selling_price = 0.0;

			for (Purchase_order_item_model model : StaticValues.order_item_models) {

				TableRow tr = new TableRow(getActivity());

				if (count % 2 != 0) {
					tr.setBackgroundColor(Color.parseColor("#1A5876"));
				}
				tr.setId(100 + count);
				tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.MATCH_PARENT));

				TextView description = new TextView(getActivity());
				description.setTag("description");
				description.setId(200 + count);
				description.setText(model.getDescription());
				description.setTextColor(Color.BLACK);
				description.setGravity(Gravity.CENTER);
				tr.addView(description);

				TextView part_no = new TextView(getActivity());
				part_no.setId(200 + count);
				part_no.setText(model.getPart_no());
				part_no.setTag("part_no");
				part_no.setTextColor(Color.BLACK);
				part_no.setGravity(Gravity.CENTER);
				tr.addView(part_no);

				//
				// Log.wtf("purchase oreder line 970",
				// "**"+model.getPart_no()+"**");

				TextView quantity = new TextView(getActivity());
				quantity.setId(200 + count);
				quantity.setText(model.getQty());
				quantity.setTag("quantity");
				quantity.setTextColor(Color.BLACK);
				quantity.setGravity(Gravity.CENTER);
				tr.addView(quantity);

				TextView price = new TextView(getActivity());
				price.setId(200 + count);
				price.setText(model.getSelling_price());
				price.setTag("selling_price");
				price.setTextColor(Color.BLACK);
				price.setGravity(Gravity.CENTER);
				tr.addView(price);

				TextView discount_percentage = new TextView(getActivity());
				discount_percentage.setId(200 + count);
				discount_percentage.setText(model.getAvg_movement());
				discount_percentage.setTag("discount_percentage");
				discount_percentage.setTextColor(Color.BLACK);
				discount_percentage.setGravity(Gravity.CENTER);
				tr.addView(discount_percentage);

				TextView comment = new TextView(getActivity());
				comment.setId(200 + count);
				comment.setText(model.getComment());
				comment.setTag("comment");
				// comment.setInputType(InputType.TYPE_NULL);
				comment.setTextColor(Color.BLACK);
				comment.setGravity(Gravity.CENTER);
				tr.addView(comment);

				Button remove = new Button(getActivity());
				remove.setOnClickListener(remove_item);
				remove.setId(250 + count);
				remove.setTag("remove");
				remove.setText("Remove Part");
				remove.setGravity(Gravity.CENTER);
				tr.addView(remove);

				Button update_qty = new Button(getActivity());
				update_qty.setOnClickListener(update);
				update_qty.setId(200 + count);
				update_qty.setTag("Update");
				update_qty.setText("Update");
				update_qty.setGravity(Gravity.CENTER);
				tr.addView(update_qty);

				// finally add this to the table row
				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				count++;

				qty = Double.valueOf(model.getQty());
				selling_price = Double.valueOf(model.getSelling_price());

				total = total + qty * selling_price;

			}

			Double last_amount = total
					- (total * Double.valueOf(HomeFragment.discount_percentage) / 100);
			Double vat = Double.valueOf(userdata.getString("vat", ""));
			Double lastamount_with_vat = last_amount + last_amount * vat / 100;
			display_total_amount_with_vat_and_discount.setText(df
					.format(lastamount_with_vat));
			display_total_amount
					.setText(df.format((total * (100 + vat) / 100)));

		} else {

			purchase_order_details.put("discount_percentage",
					HomeFragment.discount_percentage);
			purchase_order_details.put("description", ItemFragment.description);
			purchase_order_details.put("selling_price",
					ItemFragment.selling_price);
			purchase_order_details.put("part_no", ItemFragment.part_no);
			purchase_order_details.put("item_id", ItemFragment.item_id);
			purchase_order_details.put("quantity", ItemFragment.qty);
			purchase_order_details.put("avg_movement_at_dealer",
					ItemFragment.showAvgMovementAtDealer);
			purchase_order_details.put("comment", ItemFragment.comment);
			// wrapper.add(purchase_order_details);

			TableRow tr_head = new TableRow(getActivity());
			tr_head.setId(10);
			tr_head.setBackgroundColor(Color.GRAY);
			tr_head.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

			TextView label_description = new TextView(getActivity());
			label_description.setId(20);
			label_description.setText("Description");
			label_description.setTextColor(Color.RED);
			label_description.setGravity(Gravity.CENTER);
			label_description.setPadding(5, 5, 5, 5);
			tr_head.addView(label_description);// add the column to the table

			TextView label_part_no = new TextView(getActivity());
			label_part_no.setId(21);// define id that must be unique
			label_part_no.setText("Part Number"); // set the text for the
			label_part_no.setTextColor(Color.RED); // set the color
			label_part_no.setGravity(Gravity.CENTER);
			label_part_no.setPadding(5, 5, 5, 5);

			// set the padding (if
			tr_head.addView(label_part_no); // add the column to the table
			// row

			TextView label_quantity = new TextView(getActivity());
			label_quantity.setId(21);// define id that must be unique
			label_quantity.setText("Quantity"); // set the text for the
			label_quantity.setTextColor(Color.RED); // set the color
			label_quantity.setPadding(5, 5, 5, 5);
			label_quantity.setGravity(Gravity.CENTER);
			tr_head.addView(label_quantity);

			TextView label_selling_price = new TextView(getActivity());
			label_selling_price.setId(21);// define id that must be unique
			label_selling_price.setText("Selling Price"); // set the text for
			label_selling_price.setTextColor(Color.RED); // set the color
			label_selling_price.setPadding(5, 5, 5, 5);
			label_selling_price.setGravity(Gravity.CENTER);
			tr_head.addView(label_selling_price);

			TextView label_discount_percentage = new TextView(getActivity());
			label_discount_percentage.setId(21);// define id that must be unique
			label_discount_percentage.setText("Average Movement At Dealer"); // set
			label_discount_percentage.setGravity(Gravity.CENTER); // text for
			label_discount_percentage.setTextColor(Color.RED); // set the color
			label_discount_percentage.setPadding(5, 5, 5, 5);
			tr_head.addView(label_discount_percentage);

			TextView label_comment = new TextView(getActivity());
			label_comment.setId(21);// define id that must be unique
			label_comment.setText("Comment"); // set the
			label_comment.setGravity(Gravity.CENTER); // text for
			label_comment.setTextColor(Color.RED); // set the color
			label_comment.setPadding(5, 5, 5, 5);
			tr_head.addView(label_comment);

			tl.addView(tr_head, new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			Integer count = 0;

			Double qty = 0.0;
			Double discount = 0.0;
			Double selling_price = 0.0;

			for (Purchase_order_item_model model : StaticValues.order_item_models) {

				TableRow tr = new TableRow(getActivity());

				if (count % 2 != 0) {
					tr.setBackgroundColor(Color.parseColor("#1A5876"));
				}
				tr.setId(100 + count);
				tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));

				TextView description = new TextView(getActivity());
				description.setTag("description");
				description.setId(200 + count);
				description.setText(model.getDescription());
				description.setTextColor(Color.BLACK);
				description.setGravity(Gravity.CENTER);
				tr.addView(description);

				TextView part_no = new TextView(getActivity());
				part_no.setId(200 + count);
				part_no.setText(model.getPart_no());
				part_no.setTag("part_no");
				part_no.setTextColor(Color.BLACK);
				part_no.setGravity(Gravity.CENTER);
				tr.addView(part_no);

				TextView quantity = new TextView(getActivity());
				quantity.setId(200 + count);
				quantity.setText(model.getQty());
				quantity.setTag("quantity");
				quantity.setTextColor(Color.BLACK);
				quantity.setGravity(Gravity.CENTER);
				tr.addView(quantity);

				TextView price = new TextView(getActivity());
				price.setId(200 + count);
				price.setText(model.getSelling_price());
				price.setTag("selling_price");
				price.setTextColor(Color.BLACK);
				price.setGravity(Gravity.CENTER);
				tr.addView(price);

				TextView discount_percentage = new TextView(getActivity());
				discount_percentage.setId(200 + count);
				discount_percentage.setText(model.getAvg_movement());
				discount_percentage.setTag("discount_percentage");
				discount_percentage.setTextColor(Color.BLACK);
				discount_percentage.setGravity(Gravity.CENTER);
				tr.addView(discount_percentage);

				TextView comment = new TextView(getActivity());
				comment.setId(200 + count);
				comment.setText(model.getComment());
				comment.setTag("comment");
				comment.setTextColor(Color.BLACK);
				comment.setGravity(Gravity.CENTER);
				tr.addView(comment);

				Button remove = new Button(getActivity());
				remove.setOnClickListener(remove_item);
				remove.setId(250 + count);
				remove.setTag("remove");
				remove.setText("Remove Part");
				remove.setGravity(Gravity.CENTER);
				tr.addView(remove);

				Button update_qty = new Button(getActivity());
				update_qty.setOnClickListener(update);
				update_qty.setId(200 + count);
				update_qty.setTag("Update");
				update_qty.setText("Update");
				update_qty.setGravity(Gravity.CENTER);
				tr.addView(update_qty);
				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				count++;

				qty = Double.valueOf(model.getQty());

				if (!model.getSelling_price().equals("")) {
					selling_price = Double.valueOf(model.getSelling_price());
				}
				total = total + qty * selling_price;
			}

			Double last_amount = total
					- (total * Double.valueOf(HomeFragment.discount_percentage) / 100);
			Double vat = Double.valueOf(userdata.getString("vat", ""));
			Double lastamount_with_vat = last_amount + last_amount * vat / 100;
			display_total_amount_with_vat_and_discount.setText(df
					.format(lastamount_with_vat));
			display_total_amount
					.setText(df.format((total * (100 + vat) / 100)));
		}
	}

	private void permissionGranted(View view, final int orderTypez,final int is_call_order) {

		if (orderTypez != 5) {

			final Dialog dialogx = new Dialog(getActivity());
			dialogx.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			dialogx.setContentView(R.layout.dialoglayout);
			dialogx.setCanceledOnTouchOutside(false);

			dialogx.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.titleview);
			View vv = dialogx.getWindow().getDecorView();
			TextView txt_title = (TextView) vv.findViewById(R.id.title);
			txt_title.setText("Remarks");

			dialogx.show();

			final EditText edit_text_remark_1 = (EditText) dialogx
					.findViewById(R.id.edit_text_remark_1);

			final EditText edit_text_remark_2 = (EditText) dialogx
					.findViewById(R.id.edit_text_remark_2);

			Button btn_ok = (Button) dialogx.findViewById(R.id.btn_ok);

			btn_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AsyncTask<Void, Void, Void>() {
						private ProgressDialog progressDialog;

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							progressDialog = new ProgressDialog(getActivity());
							progressDialog.setMessage("Waiting for GPS...");
							progressDialog.setCanceledOnTouchOutside(false);
							progressDialog.show();
						}

						@Override
						protected Void doInBackground(Void... params) {

							if (orderTypez != 5) {
								GpsReceiver gpsReceiver = GpsReceiver
										.getGpsReceiver(getActivity());

								location = gpsReceiver
										.getHighAccurateLocation();
								location.setLatitude(0.0000000);
								location.setLongitude(0.0000000);
								do {
									location = gpsReceiver
											.getHighAccurateLocation();
								} while (location == null);
							}
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							if (progressDialog != null
									&& progressDialog.isShowing()) {

								try {
									progressDialog.dismiss();
								} catch (Exception e) {
									// TODO: handle exception
								}

							}

							String batteryLevel = Integer
									.toString(BatteryService
											.getBatteryLevel(getActivity()));

							if (InternetObserver
									.isConnectedToInternet(getActivity())
									&& orderTypez == 3) {
								HashMap<String, Object> jsonParams = new HashMap<String, Object>();
								jsonParams.put("user_id ",
										userdata.getString("ID", ""));
								JSONObject order = new JSONObject();
								try {
									order.put("lon", Double.toString(location
											.getLongitude()));
									order.put("lat", Double.toString(location
											.getLatitude()));

									order.put("BillAmount",
											display_total_amount_with_vat_and_discount
													.getText());

									// double discount
									// =Double.parseDouble(display_total_amount_with_vat_and_discount.getText().toString())*100/(100-Double.parseDouble(HomeFragment.discount_percentage));
									double b = Double
											.parseDouble(display_total_amount_with_vat_and_discount
													.getText().toString());
									double y = Double
											.parseDouble(HomeFragment.discount_percentage);
									double discount = (b * y) / (100 - y);
									order.put("dealer_id",
											HomeFragment.dealer_id);
									order.put("ID", -1);
									order.put("complete", "3");
									order.put("date_of_bill",
											Utility.timestamp_creater());
									order.put("b_level", batteryLevel);
									order.put("discount", discount);
									order.put("remark_1", edit_text_remark_1
											.getText().toString());
									order.put("remark_2", edit_text_remark_2
											.getText().toString());

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								JSONArray items = new JSONArray();

								for (Purchase_order_item_model model : StaticValues.order_item_models) {
									JSONObject itemParams = new JSONObject();
									try {
										itemParams.put("qty", model.getQty());
										itemParams.put("item_id",
												model.getItem_id());
										itemParams.put("price",
												model.getSelling_price());
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									items.put(itemParams);
								}

								try {
									order.put("items", items);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								JSONArray orderarr = new JSONArray();
								orderarr.put(order);

								JSONObject jsonx = new JSONObject();// TODO

								try {
									jsonx.put("order", orderarr);

									Log.i("order json", jsonx.toString());
								} catch (JSONException e) {

									e.printStackTrace();
								}

								long bill_id = dbworker.save_purchase_bill(
										display_total_amount.getText()
												.toString(),
										HomeFragment.dealer_id,
										HomeFragment.dealer_name,
										Utility.timestamp_creater(),
										Double.toString(location.getLongitude()),
										Double.toString(location.getLatitude()),
										Integer.toString(BatteryService
												.getBatteryLevel(getActivity())),
										Integer.toString(orderTypez),
										HomeFragment.discount_percentage,
										display_total_amount_with_vat_and_discount
												.getText().toString(),
										iternery_status,
										HomeFragment.dealer_account_no,
										"0",
										edit_text_remark_1.getText().toString(),
										edit_text_remark_2.getText().toString(),is_call_order+"");

								for (Purchase_order_item_model model : StaticValues.order_item_models) {
									dbworker.save_purchase_bill_items(
											String.valueOf(bill_id),
											model.getDescription(),
											model.getItem_id(), model.getQty(),
											model.getSelling_price(),
											model.getPart_no(),
											model.getComment());

								}

								String body = mail_body(
										HomeFragment.dealer_name,
										HomeFragment.dealer_account_no, Utility
												.timestamp_creater(),
										display_total_amount_with_vat_and_discount
												.getText().toString(),
										display_total_amount_with_vat_and_discount
												.getText().toString(),
										StaticValues.order_item_models);

								SharedPreferences user_data = getActivity()
										.getSharedPreferences("USERDATA",
												Context.MODE_PRIVATE);

								if (user_data.contains("email_address")
										&& user_data.contains("email_password")) {
									mail_send(user_data.getString(
											"email_address", ""), user_data
											.getString("email_password", ""),
											"Purchase order---"
													+ HomeFragment.dealer_name,
											body);
								}

								StaticValues.order_item_models.clear();
								new Jsonhelper().send_Json(URLS.insert_order,
										jsonx, userdata.getString("ID", ""));

								dbworker.update_sync_status(dbworker
										.get_purchase_order_max_id() + "");
								move_to_another_fragment(new HomeFragment(),
										"Dealers");

								deleteOldFile();

							} else if (InternetObserver
									.isConnectedToInternet(getActivity())
									&& orderTypez == 0) {
								HashMap<String, Object> jsonParams = new HashMap<String, Object>();
								jsonParams.put("user_id ",
										userdata.getString("ID", ""));
								JSONObject order = new JSONObject();
								try {
									double b = Double
											.parseDouble(display_total_amount_with_vat_and_discount
													.getText().toString());
									double y = Double
											.parseDouble(HomeFragment.discount_percentage);
									double discount = (b * y) / (100 - y);
									order.put("lon", Double.toString(location
											.getLongitude()));
									order.put("lat", Double.toString(location
											.getLatitude()));
									order.put("BillAmount",
											display_total_amount_with_vat_and_discount
													.getText());
									order.put("dealer_id",
											HomeFragment.dealer_id);
									order.put("ID", -1);
									order.put("complete", "0");
									order.put("date_of_bill",
											Utility.timestamp_creater());
									order.put("b_level", batteryLevel);
									order.put("discount", discount);
									order.put("remark_1", edit_text_remark_1
											.getText().toString());
									order.put("remark_2", edit_text_remark_2
											.getText().toString());
									order.put("order_type", is_call_order+"");

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								JSONArray items = new JSONArray();

								for (Purchase_order_item_model model : StaticValues.order_item_models) {
									JSONObject itemParams = new JSONObject();
									try {
										itemParams.put("qty", model.getQty());
										itemParams.put("item_id",
												model.getItem_id());
										itemParams.put("price",
												model.getSelling_price());
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									items.put(itemParams);
								}

								try {
									order.put("items", items);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								JSONArray orderarr = new JSONArray();
								orderarr.put(order);

								JSONObject jsonx = new JSONObject();// TODO

								try {
									jsonx.put("order", orderarr);
									// Log.i("order json", jsonx.toString());
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// Log.i("HomeFragment.dealer_name",
								// HomeFragment.dealer_name);
								long bill_id = dbworker.save_purchase_bill(
										display_total_amount.getText()
												.toString(),
										HomeFragment.dealer_id,
										HomeFragment.dealer_name,
										Utility.timestamp_creater(),
										Double.toString(location.getLongitude()),
										Double.toString(location.getLatitude()),
										Integer.toString(BatteryService
												.getBatteryLevel(getActivity())),
										Integer.toString(orderTypez),
										HomeFragment.discount_percentage,
										display_total_amount_with_vat_and_discount
												.getText().toString(),
										iternery_status,
										HomeFragment.dealer_account_no,
										"0",
										edit_text_remark_1.getText().toString(),
										edit_text_remark_2.getText().toString(),is_call_order+"");

								for (Purchase_order_item_model model : StaticValues.order_item_models) {
									dbworker.save_purchase_bill_items(
											String.valueOf(bill_id),
											model.getDescription(),
											model.getItem_id(), model.getQty(),
											model.getSelling_price(),
											model.getPart_no(),
											model.getComment());

								}

								String body = mail_body(
										HomeFragment.dealer_name,
										HomeFragment.dealer_account_no, Utility
												.timestamp_creater(),
										display_total_amount_with_vat_and_discount
												.getText().toString(),
										display_total_amount_with_vat_and_discount
												.getText().toString(),
										StaticValues.order_item_models);

								SharedPreferences user_data = getActivity()
										.getSharedPreferences("USERDATA",
												Context.MODE_PRIVATE);

								if (user_data.contains("email_address")
										&& user_data.contains("email_password")) {
									mail_send(user_data.getString(
											"email_address", ""), user_data
											.getString("email_password", ""),
											"Purchase order---"
													+ HomeFragment.dealer_name,
											body);
								}

								StaticValues.order_item_models.clear();
								String a = new Jsonhelper().send_Json(
										URLS.insert_order, jsonx,
										userdata.getString("ID", ""));

								dbworker.update_sync_status(dbworker
										.get_purchase_order_max_id() + "");

								move_to_another_fragment(new HomeFragment(),
										"Dealers");
								deleteOldFile();
							} else {
								long bill_id = dbworker.save_purchase_bill(
										display_total_amount.getText()
												.toString(),
										HomeFragment.dealer_id,
										HomeFragment.dealer_name,
										Utility.timestamp_creater(),
										Double.toString(0.0000000),
										Double.toString(0.0000000),
										Integer.toString(BatteryService
												.getBatteryLevel(getActivity())),
										Integer.toString(orderTypez),
										HomeFragment.discount_percentage,
										display_total_amount_with_vat_and_discount
												.getText().toString(),
										iternery_status,
										HomeFragment.dealer_account_no,
										"0",
										edit_text_remark_1.getText().toString(),
										edit_text_remark_2.getText().toString(),is_call_order+"");

								for (Purchase_order_item_model model : StaticValues.order_item_models) {
									dbworker.save_purchase_bill_items(
											String.valueOf(bill_id),
											model.getDescription(),
											model.getItem_id(), model.getQty(),
											model.getSelling_price(),
											model.getPart_no(),
											model.getComment());
								}

							}
							dbworker.close();

							StaticValues.order_item_models.clear();
							iternery_status = "0";
							purchase_order_details.clear();
							ItemFragment.clicked_items.clear();
							move_to_another_fragment(new HomeFragment(),
									"Dealars");

							deleteOldFile();
						}
					}.execute();

					dialogx.dismiss();

				}

			});

			Button btn_cancel = (Button) dialogx.findViewById(R.id.btn_cancel);

			btn_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					dialogx.dismiss();
				}
			});
		}

		else {

			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					return null;
				}

				protected void onPostExecute(Void result) {

					dbworker = new Dbworker(getActivity());

					long bill_id = dbworker.save_purchase_bill(
							display_total_amount.getText().toString(),
							HomeFragment.dealer_id, HomeFragment.dealer_name,
							Utility.timestamp_creater(), "", "", Integer
									.toString(BatteryService
											.getBatteryLevel(getActivity())),
							Integer.toString(orderTypez),
							HomeFragment.discount_percentage,
							display_total_amount_with_vat_and_discount
									.getText().toString(), iternery_status,
							HomeFragment.dealer_account_no, "0", "", "",is_call_order+"");

					for (Purchase_order_item_model model : StaticValues.order_item_models) {
						dbworker.save_purchase_bill_items(
								String.valueOf(bill_id),
								model.getDescription(), model.getItem_id(),
								model.getQty(), model.getSelling_price(),
								model.getPart_no(), model.getComment());

					}

					dbworker.close();

					StaticValues.order_item_models.clear();
					iternery_status = "0";
					purchase_order_details.clear();

					ItemFragment.clicked_items.clear();
					move_to_another_fragment(new HomeFragment(), "Dealars");
					deleteOldFile();
				}
			}.execute();
		}
		// Log.e("xxxxx", "permissionGranted");

	}

	private void permissionGranted2(View view, final int orderType,
			final String bill_id, final String finish_status,final String is_call_order) {
		// Log.e("xxxxx", "permissionGranted2");

		new AsyncTask<Void, Void, Void>() {
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setMessage("Waiting for GPS...");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {

				if (orderType != 5) {
					GpsReceiver gpsReceiver = GpsReceiver
							.getGpsReceiver(getActivity());

					location = gpsReceiver.getHighAccurateLocation();
					location.setLatitude(0.0000000);
					location.setLongitude(0.0000000);
					do {
						location = gpsReceiver.getHighAccurateLocation();
					} while (location == null);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (progressDialog != null && progressDialog.isShowing()) {
					try {
						progressDialog.dismiss();
					} catch (Exception e) {
						// TODO: handle exception
					}

				}

				if (orderType == 5) {
					dbworker.Update_purchase_order(bill_id,
							display_total_amount.getText().toString(),
							orderType + "",
							display_total_amount_with_vat_and_discount
									.getText().toString(), finish_status,
							"0.0", "0.0", "0000-00-00 00:00:00", "", "", "",is_call_order);
				} else {
					dbworker.Update_purchase_order(bill_id,
							display_total_amount.getText().toString(),
							orderType + "",
							display_total_amount_with_vat_and_discount
									.getText().toString(), finish_status,
							Double.toString(location.getLongitude()), Double
									.toString(location.getLatitude()), Utility
									.timestamp_creater(), "", "", "",is_call_order);

					if (new Utility(getActivity()).isNetworkAvailable()) {

						String params[] = { "" };

						new AsyncTask<String, String, String>() {

							@Override
							protected String doInBackground(String... params) {
								get_all_not_synchronized_purchase_orders();
								return null;
							}

						}.execute(params);
					}

				}

				if (ViewSavedOrdersFragment.purchase_order_edit == 0) {
					for (Purchase_order_item_model model : StaticValues.order_item_models) {
						dbworker.save_purchase_bill_items(
								String.valueOf(bill_id),
								model.getDescription(), model.getItem_id(),
								model.getQty(), model.getSelling_price(),
								model.getPart_no(), model.getComment());
					}
					Sugesstion_order_fragment.update_status = "0";
					dbworker.close();

					ItemFragment.clicked_items.clear();
				} else {

					dbworker.delete_purchase_order_items_by_pur_id(ViewSavedOrdersFragment.pur_id);

					for (Purchase_order_item_model item_model : StaticValues.order_item_models) {
						dbworker.save_purchase_bill_items(
								ViewSavedOrdersFragment.pur_id,
								item_model.getDescription(),
								item_model.getItem_id(), item_model.getQty(),
								item_model.getSelling_price(),
								item_model.getPart_no(),
								item_model.getComment());
					}
					StaticValues.order_item_models.clear();
					dbworker.close();

				}
				deleteOldFile();
			}
		}.execute();

	}

	// find new total
	public void find_new_total(String updated_quantity, String price,
			String exist_quantity) {
		Double new_amount = (Double.valueOf(updated_quantity) - Double
				.valueOf(exist_quantity)) * Double.valueOf(price);
		Double new_full_amount = Double.parseDouble(display_total_amount
				.getText().toString()) + new_amount;
		display_total_amount.setText(df.format(new_full_amount));
		Double new_amount_with_vat = new_full_amount - new_amount
				* Double.parseDouble(HomeFragment.discount_percentage) / 100;
		display_total_amount_with_vat_and_discount.setText(df
				.format(new_amount_with_vat));

	}

	private void add_tbl_header() {

		TableRow tr_head = new TableRow(getActivity());
		tr_head.setId(10);
		tr_head.setBackgroundColor(Color.GRAY);
		tr_head.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		TextView label_description = new TextView(getActivity());
		label_description.setId(20);
		label_description.setText("Description");
		label_description.setTextColor(Color.RED);
		label_description.setGravity(Gravity.CENTER);
		label_description.setPadding(5, 5, 5, 5);
		tr_head.addView(label_description);// add the column to the table
		// row
		// here

		TextView label_part_no = new TextView(getActivity());
		label_part_no.setId(21);// define id that must be unique
		label_part_no.setText("Part Number"); // set the text for the
		label_part_no.setTextColor(Color.RED); // set the color
		label_part_no.setGravity(Gravity.CENTER);
		label_part_no.setPadding(5, 5, 5, 5);
		tr_head.addView(label_part_no); // add the column to the table

		TextView label_quantity = new TextView(getActivity());
		label_quantity.setId(21);// define id that must be unique
		label_quantity.setText("Quantity"); // set the text for the
		label_quantity.setTextColor(Color.RED); // set the color
		label_quantity.setPadding(5, 5, 5, 5);
		label_quantity.setGravity(Gravity.CENTER);
		tr_head.addView(label_quantity);

		TextView label_selling_price = new TextView(getActivity());
		label_selling_price.setId(21);// define id that must be unique
		label_selling_price.setText("Selling Price"); // set the text for
		label_selling_price.setTextColor(Color.RED); // set the color
		label_selling_price.setPadding(5, 5, 5, 5);
		label_selling_price.setGravity(Gravity.CENTER);
		tr_head.addView(label_selling_price);

		TextView label_discount_percentage = new TextView(getActivity());
		label_discount_percentage.setId(21);// define id that must be unique
		label_discount_percentage.setText("Average Movement At Dealer"); // set
																			// the
		label_discount_percentage.setGravity(Gravity.CENTER); // text for
		label_discount_percentage.setTextColor(Color.RED); // set the color
		label_discount_percentage.setPadding(5, 5, 5, 5);
		tr_head.addView(label_discount_percentage);

		TextView label_comment = new TextView(getActivity());
		label_comment.setId(21);// define id that must be unique
		label_comment.setText("Comment"); // set the
		label_comment.setGravity(Gravity.CENTER); // text for
		label_comment.setTextColor(Color.RED); // set the color
		label_comment.setPadding(5, 5, 5, 5);
		tr_head.addView(label_comment);

		tl.addView(tr_head, new TableLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}

	@SuppressWarnings("deprecation")
	public void add_table_row(int count, Cursor cursor)// TODO
	{
		TableRow tr = new TableRow(getActivity());

		if (count % 2 != 0) {
			tr.setBackgroundColor(Color.parseColor("#1A5876"));
		}
		tr.setId(100 + count);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.MATCH_PARENT));

		TextView description = new TextView(getActivity());
		description.setTag("description");
		description.setId(200 + count);
		description.setText(cursor.getString(2));
		description.setTextColor(Color.BLACK);
		description.setGravity(Gravity.CENTER);
		tr.addView(description);

		TextView part_no = new TextView(getActivity());
		part_no.setId(200 + count);
		part_no.setText(cursor.getString(6));
		part_no.setTag("part_no");
		part_no.setTextColor(Color.BLACK);
		part_no.setGravity(Gravity.CENTER);
		tr.addView(part_no);

		TextView quantity = new TextView(getActivity());
		quantity.setId(200 + count);
		quantity.setText(cursor.getString(4));
		quantity.setTag("quantity");
		quantity.setTextColor(Color.BLACK);
		quantity.setGravity(Gravity.CENTER);
		tr.addView(quantity);

		TextView price = new TextView(getActivity());
		price.setId(200 + count);
		price.setText(cursor.getString(5));
		price.setTag("selling_price");
		price.setTextColor(Color.BLACK);
		price.setGravity(Gravity.CENTER);
		tr.addView(price);

		TextView discount_percentage = new TextView(getActivity());
		discount_percentage.setId(200 + count);
		discount_percentage.setText(cursor.getString(0));
		discount_percentage.setTag("discount_percentage");
		discount_percentage.setTextColor(Color.BLACK);
		discount_percentage.setGravity(Gravity.CENTER);
		tr.addView(discount_percentage);

		TextView comment = new TextView(getActivity());
		comment.setId(200 + count);
		comment.setText(cursor.getString(7));
		comment.setTag("comment");
		// comment.setInputType(InputType.TYPE_NULL);
		comment.setTextColor(Color.BLACK);
		comment.setGravity(Gravity.CENTER);
		tr.addView(comment);

		Button remove = new Button(getActivity());
		remove.setOnClickListener(remove_item_edit);
		remove.setId(200 + count);
		remove.setTag("remove");
		remove.setText("Remove Part");
		remove.setGravity(Gravity.CENTER);
		tr.addView(remove);

		Button update_qty = new Button(getActivity());
		update_qty.setOnClickListener(update_edit);
		update_qty.setId(200 + count);
		update_qty.setTag("Update");
		update_qty.setText("Update");
		update_qty.setGravity(Gravity.CENTER);
		tr.addView(update_qty);

		TextView id = new TextView(getActivity());
		id.setText(cursor.getString(0));
		id.setId(315 + count);
		id.setVisibility(View.GONE);
		tr.addView(id);

		// finally add this to the table row
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

	}

	public void move_to_another_fragment(Fragment f, String name) {

		Fragment fragment = f;
		FragmentManager fragmentManager = getFragmentManager();
		getActivity().getActionBar().setTitle(name);
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack("item")
				.commit();
	}

	public void mail_send(final String sendermail, final String password,
			final String subject, final String body) {
		String[] name = { "aaa" };

		new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {

				GMailSender sender = new GMailSender(sendermail.trim(),
						password);
				try {
					sender.sendMail(subject, body, sendermail.trim(),
							"prasanna@ceylonlinux.lk");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
		}.execute(name);
	}

	private String mail_body(String dealer_name, String dealer_account_no,
			String date_of_bill, String bill_amount_vat, String bill_amount,
			ArrayList<Purchase_order_item_model> order_item_models) {
		String content = "                Purchase Order\n\n\n";
		content = content + " Dealer Name            :" + dealer_name + "\n";
		content = content + " Dealer Account No      :" + dealer_account_no
				+ "\n";
		content = content + " Date Of Bill           :" + date_of_bill + "\n";
		content = content + " Bill Amount(Vat)       :" + bill_amount_vat
				+ "\n";
		content = content + " Bill Amount(Discount)  :" + bill_amount + "\n";
		content = content + "\n\n\n\n\n\n                ITEMS\n";

		content = content + string_resize("Item Description", 30)
				+ string_resize("Item part no", 20)
				+ string_resize("price", 10) + string_resize("Qty", 5)
				+ string_resize("Total", 10) + "\n";

		for (Purchase_order_item_model item_model : order_item_models) {
			String sellingPrice = item_model.getSelling_price();
			content = content
					+ string_resize(item_model.getDescription(), 30)
					+ string_resize(item_model.getPart_no(), 20)
					+ string_resize(item_model.getSelling_price(), 10)
					+ string_resize(item_model.getQty(), 5)
					+ string_resize(
							(Double.parseDouble(sellingPrice != null && !sellingPrice.isEmpty() ? sellingPrice : "0")
									* Double.parseDouble(item_model.getQty()) + ""),
							10) + "\n";

		}
		content = content
				+ "\n\n\n\n\n       Automatically generated email by sales pad app";
		return content;
	}

	private String string_resize(String word, int max) {
		StringBuilder sb = new StringBuilder(word);
		int n = (word.length() >= max) ? 0 : (max - word.length());
		for (int i = 0; i < n; i++) {
			sb.append(" ");
		}
		return sb.toString().substring(0, max);
	}

	private void deleteOldFile() {

		File root = new File(Environment.getExternalStorageDirectory(),
				"Dimo_Current_order");
		if (!root.exists()) {
			root.mkdirs();
		}
		File gpxfile = new File(root, "LastOrder.txt");
		if (gpxfile.exists()) {
			gpxfile.delete();
			Log.w("sssdel", "deleted");
		}
	}

	private void save_order_fist_time() // very fisrt time order saving
	{
		long bill_id = dbworker
				.save_purchase_bill(display_total_amount.getText().toString(),
						HomeFragment.dealer_id, HomeFragment.dealer_name,
						"0000-00-00 00:00:00", "0.00", "0.00", "0", Integer
								.toString(5), HomeFragment.discount_percentage,
						display_total_amount_with_vat_and_discount.getText()
								.toString(), iternery_status,
						HomeFragment.dealer_account_no, "0", "", "","0");

		for (Purchase_order_item_model model : StaticValues.order_item_models) {
			dbworker.save_purchase_bill_items(String.valueOf(bill_id),
					model.getDescription(), model.getItem_id(), model.getQty(),
					model.getSelling_price(), model.getPart_no(),
					model.getComment());

		}
		StaticValues.order_item_models.clear();
		move_to_another_fragment(new HomeFragment(), "Home");
	}

	private void save_order_repeat()// repeat saving
	{

		dbworker.Update_purchase_order(
				ViewSavedOrdersFragment.pur_id,
				display_total_amount.getText().toString(),
				"5",
				display_total_amount_with_vat_and_discount.getText().toString(),
				"0", "0.0", "0.0", "0000-00-00 00:00:00", "", "", "","0");

		dbworker.delete_purchase_order_items_by_pur_id(ViewSavedOrdersFragment.pur_id);

		dbworker.delete_purchase_order_items_by_pur_id(ViewSavedOrdersFragment.pur_id);

		for (Purchase_order_item_model item_model : StaticValues.order_item_models) {
			dbworker.save_purchase_bill_items(ViewSavedOrdersFragment.pur_id,
					item_model.getDescription(), item_model.getItem_id(),
					item_model.getQty(), item_model.getSelling_price(),
					item_model.getPart_no(), item_model.getComment());
		}
		StaticValues.order_item_models.clear();

		move_to_another_fragment(new HomeFragment(), "Home");
	}

	private void populate_saved_order_details() {

		add_tbl_header();
		total = 0.0;
		display_total_amount_with_vat_and_discount.setText(df.format(0.00));
		display_total_amount.setText(df.format((0.00)));

		Integer count = 0;

		Double qty = 0.0;
		Double discount = 0.0;
		Double selling_price = 0.0;

		for (Purchase_order_item_model model : StaticValues.order_item_models) {

			TableRow tr = new TableRow(getActivity());

			if (count % 2 != 0) {
				tr.setBackgroundColor(Color.parseColor("#1A5876"));
			}
			tr.setId(100 + count);
			tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.MATCH_PARENT));

			TextView description = new TextView(getActivity());
			description.setTag("description");
			description.setId(200 + count);
			description.setText(model.getDescription());
			description.setTextColor(Color.BLACK);
			description.setGravity(Gravity.CENTER);
			tr.addView(description);

			TextView part_no = new TextView(getActivity());
			part_no.setId(200 + count);
			part_no.setText(model.getPart_no());
			part_no.setTag("part_no");
			part_no.setTextColor(Color.BLACK);
			part_no.setGravity(Gravity.CENTER);
			tr.addView(part_no);

			//
			// Log.wtf("purchase oreder line 970",
			// "**"+model.getPart_no()+"**");

			TextView quantity = new TextView(getActivity());
			quantity.setId(200 + count);
			quantity.setText(model.getQty());
			quantity.setTag("quantity");
			quantity.setTextColor(Color.BLACK);
			quantity.setGravity(Gravity.CENTER);
			tr.addView(quantity);

			TextView price = new TextView(getActivity());
			price.setId(200 + count);
			price.setText(model.getSelling_price());
			price.setTag("selling_price");
			price.setTextColor(Color.BLACK);
			price.setGravity(Gravity.CENTER);
			tr.addView(price);

			TextView discount_percentage = new TextView(getActivity());
			discount_percentage.setId(200 + count);
			discount_percentage.setText(model.getAvg_movement());
			discount_percentage.setTag("discount_percentage");
			discount_percentage.setTextColor(Color.BLACK);
			discount_percentage.setGravity(Gravity.CENTER);
			tr.addView(discount_percentage);

			TextView comment = new TextView(getActivity());
			comment.setId(200 + count);
			comment.setText(model.getComment());
			comment.setTag("comment");
			// comment.setInputType(InputType.TYPE_NULL);
			comment.setTextColor(Color.BLACK);
			comment.setGravity(Gravity.CENTER);
			tr.addView(comment);

			Button remove = new Button(getActivity());
			remove.setOnClickListener(remove_item);
			remove.setId(250 + count);
			remove.setTag("remove");
			remove.setText("Remove Part");
			remove.setGravity(Gravity.CENTER);
			tr.addView(remove);

			Button update_qty = new Button(getActivity());
			update_qty.setOnClickListener(update);
			update_qty.setId(200 + count);
			update_qty.setTag("Update");
			update_qty.setText("Update");
			update_qty.setGravity(Gravity.CENTER);
			tr.addView(update_qty);

			// finally add this to the table row
			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			count++;

			qty = Double.valueOf(model.getQty());
			selling_price = Double.valueOf(model.getSelling_price());

			total = total + qty * selling_price;

			Double last_amount = total
					- (total * Double.valueOf(HomeFragment.discount_percentage) / 100);
			Double vat = Double.valueOf(userdata.getString("vat", ""));
			Double lastamount_with_vat = last_amount + last_amount * vat / 100;
			display_total_amount_with_vat_and_discount.setText(df
					.format(lastamount_with_vat));
			display_total_amount
					.setText(df.format((total * (100 + vat) / 100)));

		}

	}

	private void get_all_not_synchronized_purchase_orders() {

		Jsonhelper jh = new Jsonhelper();

		JSONObject jsonObject = new JSONObject();

		Cursor cursor = dbworker.get_purchase_orders_NOT_SYNCED();
		if (cursor.moveToFirst()) {

			try {
				JSONArray order_Array = new JSONArray();
				do {

					JSONObject order_Object = new JSONObject();
					order_Object.put("ID", cursor.getString(0));
					order_Object.put("BillAmount", cursor.getString(10));
					order_Object.put("dealer_id", cursor.getString(2));
					order_Object.put("date_of_bill", cursor.getString(4));
					order_Object.put("lon", cursor.getString(5));
					order_Object.put("lat", cursor.getString(6));
					order_Object.put("b_level", cursor.getString(7));
					order_Object.put("complete", cursor.getString(8));
					order_Object.put("SYNC", cursor.getString(9));
					order_Object.put("iterner_status", cursor.getString(12));
					order_Object.put("remark_1", cursor.getString(15));
					order_Object.put("remark_2", cursor.getString(16));
					order_Object.put("order_type", cursor.getString(17));

					double y = 0;
					Cursor cursor_dis = dbworker.get_dealer_by_id(cursor
							.getString(2));
					if (cursor_dis.moveToFirst()) {
						y = cursor_dis.getDouble(6);
					}
					double b = Double.parseDouble(cursor.getString(10));

					double discount = (b * y) / (100 - y);

					order_Object.put("discount", discount);
					cursor_dis.close();

					JSONArray itemsArray = create_purchase_items(cursor
							.getString(0));

					order_Object.put("items", itemsArray);
					// Log.i("abc",order_Object.toString());
					order_Array.put(order_Object);
				} while (cursor.moveToNext());
				jsonObject.put("order", order_Array);
				Log.d("order", jsonObject.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cursor.close();

		String a = jh.send_Json(URLS.insert_order, jsonObject,
				userdata.getString("ID", ""));

		Log.wtf("response xxx", a);

		try {
			JSONObject response = new JSONObject(a);

			if (response.getString("result").equals("true")) {

				Cursor cursor2 = dbworker.get_purchase_orders_NOT_SYNCED();
				if (cursor2.moveToFirst()) {
					do {
						dbworker.update_sync_status(cursor2.getString(0));
					} while (cursor2.moveToNext());
				}
				cursor2.close();

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public JSONArray create_purchase_items(String bill_id) {
		Cursor purchase_order_items = dbworker.get_purchase_items(bill_id);
		JSONArray j = new JSONArray();

		if (purchase_order_items != null) {
			purchase_order_items.moveToFirst();
			while (!purchase_order_items.isAfterLast()) {

				JSONObject jobj = new JSONObject();

				try {
					jobj.put("item_id", purchase_order_items.getString(3)
							.toString());
					jobj.put("qty", purchase_order_items.getString(4)
							.toString());
					jobj.put("price", purchase_order_items.getString(5)
							.toString());
					jobj.put("item_comment", purchase_order_items.getString(7)
							.toString());

					j.put(jobj);
					purchase_order_items.moveToNext();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			purchase_order_items.close();
		}

		return j;

	}

	OnClickListener finish_bill_event_edit = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (StaticValues.order_item_models.size() == 0) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setTitle("Message");
				alertDialogBuilder
						.setMessage("Add items before finish the bill")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			} else {

				finish_order_after_saving();
			}
		}
	};

	private void finish_order_after_saving() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.user_pass_custom_dialog);
		final RadioButton rBtnSuggestionOrder = (RadioButton) dialog
				.findViewById(R.id.rBtnSuggestionOrder);
		final RadioButton rBtnAcceptedOrder = (RadioButton) dialog
				.findViewById(R.id.rBtnAcceptedOrder);
		final RadioButton rBtnCallOrder = (RadioButton) dialog
				.findViewById(R.id.rBtnCallOrder);
		final EditText eTUserName = (EditText) dialog
				.findViewById(R.id.eTUserName);
		final EditText eTPassword = (EditText) dialog
				.findViewById(R.id.eTPassword);
		final TextView error = (TextView) dialog.findViewById(R.id.error);
		final Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
		final Button btnNo = (Button) dialog.findViewById(R.id.btnNo);

		rBtnSuggestionOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rBtnAcceptedOrder.setChecked(false);
				rBtnCallOrder.setChecked(false);
				eTUserName.setEnabled(false);
				eTPassword.setEnabled(false);
				eTUserName.setText("");
			}
		});
		rBtnAcceptedOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rBtnSuggestionOrder.setChecked(false);
				rBtnCallOrder.setChecked(false);

				eTPassword.setEnabled(true);

				Cursor cursor = dbworker
						.get_username_password_of_dealer(ViewSavedOrdersFragment.dealer_id);
				if (cursor.moveToFirst()) {
					int username = cursor.getColumnIndex("username");

					String userName = cursor.getString(username);
					eTUserName.setText(userName);

				}
				cursor.close();
				eTUserName.setEnabled(false);

			}
		});

		rBtnCallOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				rBtnAcceptedOrder.setChecked(false);
				rBtnSuggestionOrder.setChecked(false);
				eTUserName.setText("");
				eTUserName.setEnabled(false);
				eTPassword.setEnabled(false);
			}
		});

		String s = "";

		if (Double.valueOf(display_total_amount_with_vat_and_discount.getText()
				.toString()) < Double.valueOf(outstnding.getText().toString())) {

			s += "Outstanding,";

		}
		if (Double.valueOf(display_total_amount_with_vat_and_discount.getText()
				.toString()) < Double
				.valueOf(credit_limit.getText().toString())) {
			s += "Credit,";

		}
		if (Double.valueOf(display_total_amount_with_vat_and_discount.getText()
				.toString()) < Double.valueOf(over_due.getText().toString())) {
			s += "Over due ";
		}
		s += "is Greater than Total amount";
		dialog.setTitle(s);
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});

		btnYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (rBtnSuggestionOrder.isChecked()) {

					remark_generator_finish(3,0);
					dialog.cancel();

				}

				else if (rBtnCallOrder.isChecked()) {

					remark_generator_finish(0,1);
					dialog.dismiss();

				}

				else {

					Cursor cursor = dbworker
							.get_username_password_of_dealer(HomeFragment.dealer_id);
					if (cursor.moveToFirst()) {
						int username = cursor.getColumnIndex("username");
						int dealer_password = cursor
								.getColumnIndex("dealer_password");
						String userName = cursor.getString(username);
						String password = cursor.getString(dealer_password);
						// Log.i("username",
						// cursor.getString(username));
						// Log.i("username", password);

						if (userName.trim().equalsIgnoreCase(
								eTUserName.getText().toString().trim())
								&& password.trim().equalsIgnoreCase(
										eTPassword.getText().toString().trim())) {
							remark_generator_finish(0,0);

						} else {
							error.setText("Password Incorrect");
						}

					}

					cursor.close();
					dialog.dismiss();
				}
			}

		});

		dialog.show();
	}

	private void remark_generator_finish(final int order_type,final int is_call_order) {
		final Dialog dialogx = new Dialog(getActivity());
		dialogx.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		dialogx.setContentView(R.layout.dialoglayout);
		dialogx.setCanceledOnTouchOutside(false);

		dialogx.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titleview);
		View vv = dialogx.getWindow().getDecorView();
		TextView txt_title = (TextView) vv.findViewById(R.id.title);
		txt_title.setText("Remarks");

		final EditText edit_text_remark_1 = (EditText) dialogx
				.findViewById(R.id.edit_text_remark_1);

		final EditText edit_text_remark_2 = (EditText) dialogx
				.findViewById(R.id.edit_text_remark_2);

		Button btn_ok = (Button) dialogx.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) dialogx.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialogx.dismiss();
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Integer[] params={order_type};

				new AsyncTask<Integer, Void, Void>() {
					private ProgressDialog progressDialog;

					int type = 0;

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						progressDialog = new ProgressDialog(getActivity());
						progressDialog.setMessage("Waiting for GPS...");
						progressDialog.setCanceledOnTouchOutside(false);
						progressDialog.show();
					}

					@Override
					protected Void doInBackground(Integer... params) {
						type = params[0];

						GpsReceiver gpsReceiver = GpsReceiver
								.getGpsReceiver(getActivity());

						location = gpsReceiver.getHighAccurateLocation();
						location.setLatitude(0.0000000);
						location.setLongitude(0.0000000);
						do {
							location = gpsReceiver.getHighAccurateLocation();
						} while (location == null);

						return null;
					}

					protected void onPostExecute(Void result) {
						if (progressDialog != null
								&& progressDialog.isShowing()) {

							try {
								progressDialog.dismiss();
							} catch (Exception e) {
								// TODO: handle exception
							}

						}

						String batteryLevel = Integer.toString(BatteryService
								.getBatteryLevel(getActivity()));

						dbworker.Update_purchase_order(
								ViewSavedOrdersFragment.pur_id,
								display_total_amount.getText().toString(), "",
								display_total_amount_with_vat_and_discount
										.getText().toString(), "1",
								Double.toString(location.getLongitude()) + "",
								Double.toString(location.getLatitude()) + "",
								Utility.timestamp_creater(), edit_text_remark_1
										.getText().toString(),
								edit_text_remark_2.getText().toString(),
								batteryLevel,is_call_order+"");
						
						
						dbworker.delete_purchase_order_items_by_pur_id(ViewSavedOrdersFragment.pur_id);

						for (Purchase_order_item_model item_model : StaticValues.order_item_models) {
							dbworker.save_purchase_bill_items(ViewSavedOrdersFragment.pur_id,
									item_model.getDescription(), item_model.getItem_id(),
									item_model.getQty(), item_model.getSelling_price(),
									item_model.getPart_no(), item_model.getComment());
						}
						StaticValues.order_item_models.clear();
						
						
						move_to_another_fragment(new HomeFragment(), "HOME");
					};

				}.execute(params);
				dialogx.dismiss();
				
			}
		});
		dialogx.show();
	}

}
