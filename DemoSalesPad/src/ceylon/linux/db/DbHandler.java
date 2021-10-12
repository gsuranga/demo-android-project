package ceylon.linux.db;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class DbHandler {

	public static void performExecute(SQLiteDatabase database, String sql, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = getCompiledStatement(database, sql, parameters);
		compiledStatement.execute();
	}

	public static long performExecuteInsert(SQLiteDatabase database, String sql, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = getCompiledStatement(database, sql, parameters);
		return compiledStatement.executeInsert();
	}

	public static boolean performExecuteUpdateDelete(SQLiteDatabase database, String sql, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = getCompiledStatement(database, sql, parameters);
		return compiledStatement.executeUpdateDelete() > 0;
	}

	public static void performExecute(SQLiteStatement sqLiteStatement, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = bindParameters(sqLiteStatement, parameters);
		compiledStatement.execute();
	}

	public static long performExecuteInsert(SQLiteStatement sqLiteStatement, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = bindParameters(sqLiteStatement, parameters);
		return compiledStatement.executeInsert();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static boolean performExecuteUpdateDelete(SQLiteStatement sqLiteStatement, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = bindParameters(sqLiteStatement, parameters);
		return compiledStatement.executeUpdateDelete() > 0;
	}

	public static Cursor performRawQuery(SQLiteDatabase database, String sql, Object[] parameters) {
		return database.rawQuery(sql, convertToStringArray(parameters));
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static SQLiteStatement getCompiledStatement(SQLiteDatabase database, String sql, Object[] parameters) throws SQLException {
		SQLiteStatement sqLiteStatement = database.compileStatement(sql);
		String[] stringParameters = convertToStringArray(parameters);
		if (stringParameters != null) {
		//	sqLiteStatement.bindAllArgsAsStrings(stringParameters);
			for(int i=1;i<stringParameters.length+1;i++)
			{
				sqLiteStatement.bindString(i, stringParameters[i]);
			}
			
		}
		return sqLiteStatement;
	}

	private static SQLiteStatement bindParameters(SQLiteStatement sqLiteStatement, Object[] parameters) {
		String[] stringParameters = convertToStringArray(parameters);
		if (stringParameters != null) {
			for(int i=0;i<stringParameters.length;i++)
			{
				int j=i;
				sqLiteStatement.bindString(j+1, stringParameters[i]);
			}
			
		}
		return sqLiteStatement;
	}

	private static String[] convertToStringArray(Object[] parameters) {
		if (parameters == null) {
			return null;
		}
		final int PARAMETERS_LENGTH = parameters.length;
		if (PARAMETERS_LENGTH == 0) {
			return null;
		}
		String[] stringParameters = new String[PARAMETERS_LENGTH];
		for (int i = 0; i < PARAMETERS_LENGTH; i++) {
			stringParameters[i] = String.valueOf(parameters[i]);
		}
		return stringParameters;
	}
}
