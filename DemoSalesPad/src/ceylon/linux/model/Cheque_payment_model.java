package ceylon.linux.model;

public class Cheque_payment_model {

	private String cheque_no, bankname, realised_date, path, bank_id;
	private String amount = "0.00";

	public String getCheque_no() {
		return cheque_no;
	}

	public void setCheque_no(String cheque_no) {
		this.cheque_no = cheque_no;
	}

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

	public String getRealised_date() {
		return realised_date;
	}

	public void setRealised_date(String realised_date) {
		this.realised_date = realised_date;
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

}
