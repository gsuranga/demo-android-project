package ceylon.linux.model;

import com.google.gson.annotations.SerializedName;


public class Item {

	@SerializedName("item_id")
	private String item_id;

	@SerializedName("item_part_no")
	private String item_part_no;

	@SerializedName("description")
	private String description;

	@SerializedName("selling_price")
	private String selling_price;

	@SerializedName("total_stock_qty")
	private String total_stock_qty;

	@SerializedName("status")
	private String status;

	@SerializedName("time_stamp")
	private String time_stamp;

	@SerializedName("avg_movement_in_area")
	private String avg_movement_in_area;

	public String getItem_id() {
		return item_id;
	}

	public String getItem_part_no() {
		return item_part_no;
	}

	public String getDescription() {
		return description;
	}

	public String getSelling_price() {
		return selling_price;
	}

	public String getTotal_stock_qty() {
		return total_stock_qty;
	}

	public String getStatus() {
		return status;
	}

	public String getTime_stamp() {
		return time_stamp;
	}

	public String getAvg_movement_in_area() {
		return avg_movement_in_area;
	}

}
