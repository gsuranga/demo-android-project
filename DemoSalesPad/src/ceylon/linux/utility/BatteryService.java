/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 10:43:25 AM
 */
package ceylon.linux.utility;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class BatteryService {

	public static int getBatteryLevel(Context context) {
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, ifilter);
		return batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	}
}
