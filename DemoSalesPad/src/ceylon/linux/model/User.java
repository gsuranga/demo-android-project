package ceylon.linux.model;

import android.util.Log;

public class User {

	private int userId;
	private String name;
	private String userType;
	public String uname;
	public String pwd;
	
	public int getUserId() {
		Log.wtf("asdfg", "" + userId);
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public User() {
	}

	public User(int userId, String name) {
		this.userId = userId;
		this.name = name;
	}
	
	public User(int userId, String userName, String userType, String uname,String pwd) {
		this.userId = userId;
		this.name = name;
		this.userType = userType;
		this.uname = uname;
		this.pwd = pwd;
	}

	
	
}
