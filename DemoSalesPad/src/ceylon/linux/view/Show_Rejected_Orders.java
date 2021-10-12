package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ceylon.linux.db.Dbworker;
import ceylon.linux.utility.NumbeFormater;

public class Show_Rejected_Orders extends Fragment {
	Dbworker dbworker;
	SharedPreferences userdata;
	BaseExpandableListAdapter adapter;
	TextView title;
	HashMap<String, String> purchase_order_details;
	private ArrayList<SuggestionOrder> suggestionOrders;

	@Override
	public View onCreateView(final LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		dbworker = new Dbworker(getActivity());
		get_suggestion_orders();
		View rootView = inflater.inflate(R.layout.sugesstion_order_fragment,
			container, false);
		userdata = getActivity().getSharedPreferences("USERDATA",
			Context.MODE_PRIVATE);
		title = (TextView) rootView.findViewById(R.id.title);
		title.setText("Reject Orders");
		ExpandableListView expandableListView = (ExpandableListView) rootView
			.findViewById(R.id.orderListView);

		adapter = new BaseExpandableListAdapter() {

			@Override
			public boolean isChildSelectable(int arg0, int arg1) {
				return false;
			}

			@Override
			public boolean hasStableIds() {
				return false;
			}

			@Override
			public View getGroupView(int arg0, boolean arg1, View arg2,
			                         ViewGroup arg3) {
				if (arg2 == null) {
					LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					arg2 = inflater.inflate(R.layout.manage_order_list_item,
						null);
				}
				TextView dealerName = (TextView) arg2
					.findViewById(R.id.txtDealerName);
				TextView date = (TextView) arg2
					.findViewById(R.id.txtDateOfBill);
				SuggestionOrder suggestionOrder = getGroup(arg0);
				dealerName.setText(suggestionOrder.dealerName);
				date.setText(suggestionOrder.date);
				return arg2;
			}

			@Override
			public long getGroupId(int arg0) {
				return arg0;
			}

			@Override
			public int getGroupCount() {
				return suggestionOrders.size();
			}

			@Override
			public SuggestionOrder getGroup(int arg0) {
				return suggestionOrders.get(arg0);
			}

			@Override
			public int getChildrenCount(int arg0) {
				return 1;
			}

			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
			}

			@Override
			public View getChildView(final int arg0, int arg1, boolean arg2,
			                         View arg3, ViewGroup arg4) {
				if (arg3 == null) {
					LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					arg3 = inflater.inflate(R.layout.order_detail_page, null);
				}

				SuggestionOrder suggestionOrder = getGroup(arg0);
				ArrayList<Item> items = suggestionOrder.items;

				TextView dealerName = (TextView) arg3
					.findViewById(R.id.dealerName);
				TextView dealerAccountNo = (TextView) arg3
					.findViewById(R.id.dealerAccountNo);
				TextView dateOfBill = (TextView) arg3
					.findViewById(R.id.dateOfBill);
				TextView billAmount = (TextView) arg3
					.findViewById(R.id.billAmount);

				TextView billAmount_vat = (TextView) arg3
					.findViewById(R.id.billAmount_vat);

				dealerName.setText(suggestionOrder.dealerName);
				dealerAccountNo.setText(suggestionOrder.accountNo);
				dateOfBill.setText(suggestionOrder.date);
				billAmount.setText(NumbeFormater.round(suggestionOrder.amount, 2));

				Double newvalue = Double.valueOf(billAmount.getText().toString()) + (Double.valueOf(billAmount.getText().toString()) * Double.valueOf(userdata.getString("vat", "")) / 100);


				billAmount_vat.setText(NumbeFormater.round(suggestionOrder.amount_with_vat, 2));

				LinearLayout selectedItemsListView = (LinearLayout) arg3
					.findViewById(R.id.selectedItemsListView);
				selectedItemsListView.removeAllViews();
				for (int i = 0; i < suggestionOrder.items.size(); i++) {
					View itemView = inflater.inflate(
						R.layout.suggetion_order_detail, null);
					TextView txtDescription = (TextView) itemView
						.findViewById(R.id.txtItemName);
					TextView txtQty = (TextView) itemView
						.findViewById(R.id.txtQuantity);
					TextView txtAmount = (TextView) itemView
						.findViewById(R.id.txtAmount);
					TextView txtprice = (TextView) itemView.findViewById(R.id.txtprice);
					Item item = suggestionOrder.items.get(i);
					txtDescription.setText(item.itemName);
					txtQty.setText(item.quantity);
					txtprice.setText(NumbeFormater.round(item.price, 2));
					txtAmount.setText(NumbeFormater.round(item.amount, 2));
					itemView.setBackgroundColor((i % 2 == 0) ? Color
						.parseColor("#E6E6E6") : Color
						.parseColor("#ffffff"));
					selectedItemsListView.addView(itemView);
				}

				return arg3;
			}

			@Override
			public long getChildId(int arg0, int arg1) {
				return arg1;
			}

			@Override
			public SuggestionOrder getChild(int arg0, int arg1) {
				return suggestionOrders.get(arg0);
			}
		};
		expandableListView.setAdapter(adapter);


		return rootView;
	}

	public void get_suggestion_orders() {
		suggestionOrders = new ArrayList<SuggestionOrder>();
		Cursor cursor = dbworker.get_all_suggestion_orders("-1");
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
				.moveToNext()) {
				SuggestionOrder suggestionOrder = new SuggestionOrder();
				// dealername
				suggestionOrder.dealerName = cursor.getString(0);
				// username
				suggestionOrder.userName = cursor.getString(1);
				// password
				suggestionOrder.password = cursor.getString(2);
				// account_no
				suggestionOrder.accountNo = cursor.getString(3);

				// bill_id
				suggestionOrder.billId = cursor.getString(4);
				// bill date
				suggestionOrder.date = cursor.getString(5);
				// BillAmount
				suggestionOrder.amount = cursor.getString(6);
				suggestionOrder.amount_with_vat = cursor.getString(7);
				Cursor purchase_items = dbworker.get_purchase_items(suggestionOrder.billId);
				int itemNameIndex = purchase_items
					.getColumnIndex("description");
				int price = purchase_items.getColumnIndex("price");
				int quantity = purchase_items.getColumnIndex("qty");

				for (purchase_items.moveToFirst(); !purchase_items
					.isAfterLast(); purchase_items.moveToNext()) {
					Item item = new Item();
					item.itemName = purchase_items.getString(itemNameIndex);
					item.price = purchase_items.getString(price);
					item.quantity = purchase_items.getString(quantity);
					item.amount = Double.toString(Double
						.parseDouble(item.price)
						* Double.parseDouble(item.quantity));
					suggestionOrder.items.add(item);
				}

				suggestionOrders.add(suggestionOrder);
			}
			cursor.close();
		}
	}

	private static class SuggestionOrder {
		String billId;
		String userName;
		String password;
		String amount;
		ArrayList<Item> items = new ArrayList<Item>();
		String accountNo;
		String date;
		String dealerName;
		String amount_with_vat;
	}

	private static class Item {
		String itemName;
		String price;
		String quantity;
		String amount;
		String discount_percntage;
	}

}
