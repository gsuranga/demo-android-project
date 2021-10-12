package ceylon.linux.model;

public class Payment_return_item_model {

	private String item_id;
	private String item_no;
	private String item_description;
	private String reason_id;
	private String reason;
	private String qty;
	private String remarks;
	private String amount;
	private String file_path;

	public String getReason_id() {
		return reason_id;
	}

	public void setReason_id(String reason_id) {
		this.reason_id = reason_id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getItem_no() {
		return item_no;
	}

	public void setItem_no(String item_no) {
		this.item_no = item_no;
	}

	public String getItem_description() {
		return item_description;
	}

	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

}
