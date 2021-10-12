package ceylon.linux.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.dimosales.R;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

public class ManageOrderFragment extends Fragment {
	SharedPreferences userdata;
	private Dbworker dbworker;
	private ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setRequestedOrientation(
			ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		dbworker = new Dbworker(getActivity());
		Cursor purchaseOrdersCursor = dbworker.get_pending_orders();
		userdata = getActivity().getSharedPreferences("USERDATA",
			Context.MODE_PRIVATE);
		int dealerNameIndex = purchaseOrdersCursor
			.getColumnIndex("dealer_name");
		int dealerAccountIndex = purchaseOrdersCursor
			.getColumnIndex("account_no");
		int billAmountIndex = purchaseOrdersCursor.getColumnIndex("BillAmount");
		int dateOdBillIndex = purchaseOrdersCursor
			.getColumnIndex("date_of_bill");
		int billIdIndex = purchaseOrdersCursor.getColumnIndex("ID");
		int BillAmount_with_vat = purchaseOrdersCursor.getColumnIndex("BillAmount_with_vat");


		for (purchaseOrdersCursor.moveToFirst(); !purchaseOrdersCursor
			.isAfterLast(); purchaseOrdersCursor.moveToNext()) {
			PurchaseOrder purchaseOrder = new PurchaseOrder(
				purchaseOrdersCursor.getString(billIdIndex),
				purchaseOrdersCursor.getString(dealerNameIndex),
				purchaseOrdersCursor.getString(dealerAccountIndex),
				purchaseOrdersCursor.getString(billAmountIndex),
				purchaseOrdersCursor.getString(dateOdBillIndex),
				purchaseOrdersCursor.getString(BillAmount_with_vat)


			);
			purchaseOrders.add(purchaseOrder);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.manage_order_fragment, null);
		ExpandableListView tbl_details = (ExpandableListView) view.findViewById(R.id.orderListView);
		tbl_details.setAdapter(new BaseExpandableListAdapter() {

			@Override
			public int getGroupCount() {
				return purchaseOrders.size();
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				return 1;
			}

			@Override
			public Object getGroup(int groupPosition) {
				return purchaseOrders.get(groupPosition);
			}

			@Override
			public Object getChild(int groupPosition, int childPosition) {
				return null;
			}

			@Override
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return childPosition;
			}

			@Override
			public boolean hasStableIds() {
				return false;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
			                         View convertView, ViewGroup parent) {
				TableRowViewHolder tableRowViewHolder;
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.manage_order_list_item, null);
					tableRowViewHolder = new TableRowViewHolder();
					tableRowViewHolder.txtDealerName = (TextView) convertView.findViewById(R.id.txtDealerName);
					tableRowViewHolder.txtDateOfBill = (TextView) convertView.findViewById(R.id.txtDateOfBill);
					convertView.setTag(tableRowViewHolder);
				} else {
					tableRowViewHolder = (TableRowViewHolder) convertView.getTag();
				}
				PurchaseOrder purchaseOrder = purchaseOrders.get(groupPosition);
				tableRowViewHolder.txtDealerName.setText(purchaseOrder.dealerName + " (Pending)");
				tableRowViewHolder.txtDateOfBill
					.setText(purchaseOrder.dateOfBill);
				convertView.setBackgroundColor((groupPosition % 2 == 0) ? Color.rgb(171, 217, 231) : Color.rgb(181, 207, 231));
				return convertView;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,  boolean isLastChild, View convertView, ViewGroup parent) {
				PurchaseOrder purchaseOrder = purchaseOrders.get(groupPosition);
				Cursor purchased_items = dbworker.get_purchase_items(purchaseOrder.billId);
				
				int descriptionIndex = purchased_items.getColumnIndex("description");
				int qtyIndex = purchased_items.getColumnIndex("qty");
				final ArrayList<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<PurchaseOrderDetail>();
				
				for (purchased_items.moveToFirst(); !purchased_items.isAfterLast(); purchased_items.moveToNext()) {
					PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail(purchased_items.getString(descriptionIndex),purchased_items.getString(qtyIndex));purchaseOrderDetails.add(purchaseOrderDetail);
				}

				View view = inflater.inflate(R.layout.order_detail_page, null);
				TextView dealerName = (TextView) view.findViewById(R.id.dealerName);
				TextView dealerAccountNo = (TextView) view.findViewById(R.id.dealerAccountNo);
				TextView dateOfBill = (TextView) view.findViewById(R.id.dateOfBill);
				TextView billAmount = (TextView) view.findViewById(R.id.billAmount);
				TextView billAmount_vat = (TextView) view.findViewById(R.id.billAmount_vat);

				dealerName.setText(purchaseOrder.dealerName);
				dealerAccountNo.setText(purchaseOrder.dealerAccount);
				dateOfBill.setText(purchaseOrder.dateOfBill);
				billAmount.setText(purchaseOrder.billAmount);

				Double newvalue = Double.valueOf(billAmount.getText().toString())+ (Double.valueOf(userdata.getString("vat", "")) / 100)* Double.valueOf(billAmount.getText().toString());

				billAmount_vat.setText(purchaseOrder.bill_with_vat);

				LinearLayout selectedItemsListView = (LinearLayout) view.findViewById(R.id.selectedItemsListView);
				
				
				for (int i = 0; i < purchaseOrderDetails.size(); i++) {
					View itemView = inflater.inflate(R.layout.item_detail_page,null);
					TextView txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
					TextView txtQty = (TextView) itemView.findViewById(R.id.txtQty);
					PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetails.get(i);
					txtDescription.setText(purchaseOrderDetail.description);
					txtQty.setText(purchaseOrderDetail.qty);
					itemView.setBackgroundColor((i % 2 == 0) ? Color.parseColor("#E6E6E6") : Color.parseColor("#ffffff"));
					selectedItemsListView.addView(itemView);
				}
				
				
				view.setBackgroundColor((groupPosition % 2 == 0) ? Color.rgb(171, 217, 231) : Color.rgb(181, 207, 231));
				return view;
			}

			@Override
			public boolean isChildSelectable(int groupPosition,
			                                 int childPosition) {
				return false;
			}
		});
		return view;
	}

	private static class PurchaseOrderDetail {
		String description;
		String qty;

		PurchaseOrderDetail(String description, String qty) {
			this.description = description;
			this.qty = qty;
		}
	}

	private static class PurchaseOrderDetailViewHolder {
		TextView txtDescription;
		TextView txtQty;
	}

	public static class PurchaseOrder implements Serializable {
		String billId;
		String dealerName;
		String dealerAccount;
		String billAmount;

		String dateOfBill;
		String bill_with_vat;

		PurchaseOrder(String billId, String dealerName, String dealerAccount,
		              String billAmount, String dateOfBill, String bill_with_vat) {
			this.billId = billId;
			this.dealerName = dealerName;
			this.dealerAccount = dealerAccount;
			this.billAmount = billAmount;
			this.dateOfBill = dateOfBill;
			this.bill_with_vat = bill_with_vat;
		}

		public ArrayList<PurchaseOrderDetail> getPurchaseOrderDetails() {
			return null;
		}
	}

	private static class TableRowViewHolder {
		TextView txtDealerName;
		TextView txtDateOfBill;
	}

}
