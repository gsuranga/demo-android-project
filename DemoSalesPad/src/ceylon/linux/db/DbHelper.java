package ceylon.linux.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressLint("NewApi")
public class DbHelper extends SQLiteOpenHelper {

	private final AssetManager assets;
	public SQLiteDatabase db;

	public DbHelper(Context context) {
		super(context, "dimo", null, 5);
		assets = context.getAssets();
		db = this.getWritableDatabase();

	 
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		getWritableDatabase().close();
		getReadableDatabase().close();
		db.close();
		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			InputStream databaseStream = assets.open("database.sql");
			BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(databaseStream));
			String databaseDeclaration = "";
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				databaseDeclaration = databaseDeclaration
					+ line.replace("\t", "");
			}
			for (String sql : databaseDeclaration.split(";")) {
				if (!sql.trim().isEmpty()) {
					db.execSQL(sql.trim());
				}
			}


		} catch (IOException ex) {
			Logger.getLogger(DbHelper.class.getName()).log(Level.SEVERE, null,
				ex);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int verold, int vernew) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS tbltest");
		onCreate(db);

	}


	public Cursor getQuery(String tablename, String[] columns,String selection, String[] SelectionArg) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(tablename, columns, selection, SelectionArg,null, null, null);
		return cursor;
	}

	public Cursor getrawQuery(String query, String args[]) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery(query, args);
		return cur;
	}


	public int insetrawQuery(String query) {

		db.beginTransaction();

		db.execSQL(query);
		db.setTransactionSuccessful();
		db.endTransaction();
		return 1;
	}


}
