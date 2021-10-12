package ceylon.linux.controller;

import java.util.ArrayList;

import org.json.JSONObject;

import ceylon.linux.model.Purchase_order_item_model;

public class StaticValues {

	public static boolean line_wise_item_target_data_load_status = true;
	public static JSONObject jsonobject_tatrget_history=null;
	public static JSONObject jsonobject_fast_moving=null;
	public static String file_path_payments="";
	public static int capture_type=0;
	
	public static ArrayList<Purchase_order_item_model> order_item_models = new ArrayList<Purchase_order_item_model>();
	public static JSONObject jasonPast = new JSONObject();
	public static JSONObject jasonNot = new JSONObject();
}
