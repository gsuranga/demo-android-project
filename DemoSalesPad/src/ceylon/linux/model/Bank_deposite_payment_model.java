package ceylon.linux.model;

public class Bank_deposite_payment_model {
	private String account_no , bankname,bank_id, deposit_date, path,slip_no;

	private String amount="0.00";

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getDeposit_date() {
		return deposit_date;
	}

	public void setDeposit_date(String deposit_date) {
		this.deposit_date = deposit_date;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getSlip_no() {
		return slip_no;
	}

	public void setSlip_no(String slip_no) {
		this.slip_no = slip_no;
	}
}
