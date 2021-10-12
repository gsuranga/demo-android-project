package ceylon.linux.ss4a;

import android.database.sqlite.SQLiteOpenHelper;
import ceylon.linux.db.DbHelper;

public class DimoApplication extends SS4AApplication{

	@Override
	public SQLiteOpenHelper getDataBase() {
		// TODO Auto-generated method stub
		return new DbHelper(getApplicationContext());
	}
	
	@Override
	public void onCreate() {		
		super.onCreate();
	}

}
