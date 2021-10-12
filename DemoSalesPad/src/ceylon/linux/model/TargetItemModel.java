package ceylon.linux.model;

public class TargetItemModel {
	


	private int item_id;

	private String minimum_qty;

	private String additional_qty;

	private String current_selling_price;
	
	private String item_name;
	
	private String item_part_no;

	

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public String getMinimum_qty() {
		return minimum_qty;
	}

	public void setMinimum_qty(String minimum_qty) {
		this.minimum_qty = minimum_qty;
	}

	public String getAdditional_qty() {
		return additional_qty;
	}

	public void setAdditional_qty(String additional_qty) {
		this.additional_qty = additional_qty;
	}

	public String getCurrent_selling_price() {
		return current_selling_price;
	}

	public void setCurrent_selling_price(String current_selling_price) {
		this.current_selling_price = current_selling_price;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_part_no() {
		return item_part_no;
	}

	public void setItem_part_no(String item_part_no) {
		this.item_part_no = item_part_no;
	}
	
	

}
