package ceylon.linux.model;

import com.google.gson.annotations.SerializedName;

public class Payment {

	@SerializedName("deliver_order_id")
	private String deliver_order_id;

	@SerializedName("purchase_order_id")
	private String purchase_order_id;

	@SerializedName("dealer_id")
	private String dealer_id;

	@SerializedName("invoice_no")
	private String invoice_no;

	@SerializedName("wip_no")
	private String wip_no;

	@SerializedName("total_amount")
	private String total_amount;

	@SerializedName("added_date")
	private String added_date;

	@SerializedName("accepted_by")
	private String accepted_by;
	
	@SerializedName("added_time")
	private String added_time;
	
	@SerializedName("due_date")
	private String due_date;

	
	@SerializedName("time_stamp")
	private String time_stamp;
	
	@SerializedName("status")
	private String status;
	
	@SerializedName("return_amount")
	private String return_amount;
	
	@SerializedName("delar_account_no")
	private String delar_account_no;
	
	@SerializedName("delar_shop_name")
	private String delar_shop_name;
	
	@SerializedName("business_address")
	private String business_address;
	
	
	@SerializedName("branch_name")
	private String branch_name;
	
	
	@SerializedName("cash_payment")
	private String cash_payment;
	
	@SerializedName("realized_cheque_amount")
	private String realized_cheque_amount;
	
	@SerializedName("unrealized_cheque_amount")
	private String unrealized_cheque_amount;
	
	@SerializedName("bank_dep_payment")
	private String bank_dep_payment;
	
	
	@SerializedName("total_paid_amount_with_unrealized_cheques")
	private String total_paid_amount_with_unrealized_cheques;
	
	@SerializedName("total_paid_amount_without_unrealized_cheques")
	private String total_paid_amount_without_unrealized_cheques;
	
	@SerializedName("total_pending_amount_without_unrealized_cheques")
	private String total_pending_amount_without_unrealized_cheques;
	
	@SerializedName("total_pending_amount_with_unrealized_cheques")
	private String total_pending_amount_with_unrealized_cheques;
	
	
	@SerializedName("number_of_days")
	private String number_of_days;


	public String getDeliver_order_id() {
		return deliver_order_id;
	}


	public String getPurchase_order_id() {
		return purchase_order_id;
	}


	public String getDealer_id() {
		return dealer_id;
	}


	public String getInvoice_no() {
		return invoice_no;
	}


	public String getWip_no() {
		return wip_no;
	}


	public String getTotal_amount() {
		return total_amount;
	}


	public String getAdded_date() {
		return added_date;
	}


	public String getAccepted_by() {
		return accepted_by;
	}


	public String getAdded_time() {
		return added_time;
	}


	public String getDue_date() {
		return due_date;
	}


	public String getTime_stamp() {
		return time_stamp;
	}


	public String getStatus() {
		return status;
	}


	public String getReturn_amount() {
		return return_amount;
	}


	public String getDelar_account_no() {
		return delar_account_no;
	}


	public String getDelar_shop_name() {
		return delar_shop_name;
	}


	public String getBusiness_address() {
		return business_address;
	}


	public String getBranch_name() {
		return branch_name;
	}


	public String getCash_payment() {
		return cash_payment;
	}


	public String getRealized_cheque_amount() {
		return realized_cheque_amount;
	}


	public String getUnrealized_cheque_amount() {
		return unrealized_cheque_amount;
	}


	public String getBank_dep_payment() {
		return bank_dep_payment;
	}


	public String getTotal_paid_amount_with_unrealized_cheques() {
		return total_paid_amount_with_unrealized_cheques;
	}


	public String getTotal_paid_amount_without_unrealized_cheques() {
		return total_paid_amount_without_unrealized_cheques;
	}


	public String getTotal_pending_amount_without_unrealized_cheques() {
		return total_pending_amount_without_unrealized_cheques;
	}

	public String getTotal_pending_amount_with_unrealized_cheques()
	{
		return total_pending_amount_with_unrealized_cheques;
	}

	public String getNumber_of_days() {
		return number_of_days;
	}
	
	
	
	
}
