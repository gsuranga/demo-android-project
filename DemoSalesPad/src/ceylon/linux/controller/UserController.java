package ceylon.linux.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import ceylon.linux.model.User;

import android.content.Context;
import android.content.SharedPreferences;



public class UserController extends AbstractController {

	
	public static User getAuthorizedUser(Context context) {
		SharedPreferences userData = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
		
		Integer userId;
		String userName;
		String userType;
		String uname;
		String pwd;
		long loginTime;
		
		if ((loginTime = userData.getLong("loginTime", -1)) == -1) {
			return null;
		}
		Date lastLoginDate = new Date(loginTime);
		Date currentDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y-M-d");
		if (!simpleDateFormat.format(lastLoginDate).equalsIgnoreCase(simpleDateFormat.format(currentDate))) {
			return null;
		}
		if ((userId = userData.getInt("userId", -1)) == -1) {
			return null;
		}
		if ((userName = userData.getString("userName", "")).isEmpty()) {
			return null;
		}
		if ((userType = userData.getString("type", "")).isEmpty()) {
			return null;
		}
		if ((uname = userData.getString("uname", "")).isEmpty()) {
			return null;
		}
		if ((pwd = userData.getString("pwd", "")).isEmpty()) {
			return null;
		}
		return new User(userId, userName, userType, uname, pwd);
	}
	
}
