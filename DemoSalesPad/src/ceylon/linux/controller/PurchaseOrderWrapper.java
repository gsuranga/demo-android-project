package ceylon.linux.controller;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class PurchaseOrderWrapper extends ArrayList<HashMap<String, String>> {

	@Override
	public boolean contains(Object o) {
		boolean exist = false;
		HashMap<String, String> ob = (HashMap<String, String>) o;
		for (HashMap<String, String> map : this) {
		//	Log.i("kasun",
			//	ob.get("part_no")
				//	+ " "
					//+ map.get("part_no")
					//+ " = "
					//+ Boolean.toString(ob.get("part_no")
					//.equalsIgnoreCase(map.get("part_no"))));
			if (ob.get("part_no").equalsIgnoreCase(map.get("part_no"))) {
				exist = true;
			}
		}
		return exist;
	}

	public int removeDuplicates(Object o) {
		HashMap<String, String> ob = (HashMap<String, String>) o;
		for (int index = 0; index < this.size(); index++) {
			HashMap<String, String> map = this.get(index);
			Log.i("kasun",
				ob.get("part_no")
					+ " "
					+ map.get("part_no")
					+ " = "
					+ Boolean.toString(ob.get("part_no")
					.equalsIgnoreCase(map.get("part_no"))));
			if (ob.get("part_no").equalsIgnoreCase(map.get("part_no"))) {
				return index;
			}
		}
		return -1;
	}
}