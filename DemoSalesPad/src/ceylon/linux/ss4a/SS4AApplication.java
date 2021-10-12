package ceylon.linux.ss4a;

import android.app.Application;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Thedath Oudarya on 11/17/2015.
 */
public abstract class SS4AApplication extends Application {

    private static final String TAG = SS4AApplication.class.getSimpleName();

    private boolean flag = true;

    private ServerSocket serverSocket;

    private Socket socket;

    private BufferedReader br;

    private PrintWriter pw;

    private SQLiteOpenHelper sqliteOpenHelper;

    public abstract SQLiteOpenHelper getDataBase();

    @Override
    public void onCreate() {
        super.onCreate();

        sqliteOpenHelper = getDataBase();

        if (sqliteOpenHelper != null) {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        l1:
                        while (flag) {
                            serverSocket = new ServerSocket(1993);
                            socket = serverSocket.accept();
                            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            pw = new PrintWriter(socket.getOutputStream(), true);
                            l2:
                            while (true) {
                                String requestJSONString = br.readLine();
                                Log.wtf(TAG, "REQ : " + requestJSONString);
                                if (requestJSONString == null) {
                                    closeConnection();
                                    continue l1;
                                } else {
                                    Data data;
                                    try {
                                        data = new Data(new JSONObject(requestJSONString));
                                        int status = data.getStatus();
                                        if (status == Data.CONNECTION_REQUEST) {
                                            Log.wtf(TAG, "connection_request");
                                            data.setStatus(Data.CONNECTION_ACCEPTED);
                                        } else if (status == Data.LIVE_CONNECTION) {
                                            Log.wtf(TAG, "live_connection");
                                        } else if (status == Data.QUERY) {
                                            Log.wtf(TAG, "query");
                                            String result = "No result";
                                            try {
                                                Cursor cursor = sqliteOpenHelper.getWritableDatabase().rawQuery(data.getQuery(), null);
                                                boolean firstTime = true;
                                                int columnCount = 0;
                                                JSONArray jsonArray = new JSONArray();
                                                while (cursor.moveToNext()) {
                                                    if (firstTime) {
                                                        columnCount = cursor.getColumnCount();
                                                        firstTime = false;
                                                    }
                                                    JSONObject jsonObject = new JSONObject();
                                                    for (int i = 0; i < columnCount; i++) {
                                                        int columnType = cursor.getType(i);
                                                        String columnName = cursor.getColumnName(i);
                                                        if (columnType == Cursor.FIELD_TYPE_STRING) {
                                                            jsonObject.put(columnName, cursor.getString(i));
                                                        } else if (columnType == Cursor.FIELD_TYPE_BLOB) {
                                                            jsonObject.put(columnName, cursor.getBlob(i).toString());
                                                        } else if (columnType == Cursor.FIELD_TYPE_FLOAT) {
                                                            jsonObject.put(columnName, cursor.getFloat(i));
                                                        } else if (columnType == Cursor.FIELD_TYPE_INTEGER) {
                                                            jsonObject.put(columnName, cursor.getInt(i));
                                                        } else if (columnType == Cursor.FIELD_TYPE_NULL) {
                                                            jsonObject.put(columnName, "NULL");
                                                        } else {
                                                            jsonObject.put(columnName, "invalid type");
                                                        }
                                                    }
                                                    jsonArray.put(jsonObject);
                                                }
//                                                result = DatabaseUtils.dumpCursorToString(cursor);
                                                result = jsonArray.toString(2);
                                                cursor.close();
                                            } catch (Exception e) {
                                                StringWriter sw = new StringWriter();
                                                PrintWriter epw = new PrintWriter(sw);
                                                e.printStackTrace(epw);
                                                result = sw.toString();
                                                epw.close();
                                                sw.close();
                                            } finally {
                                                data.setResult(result);
                                            }
                                        } else if (status == Data.DEVICE_NAME) {
                                            data.setResult(Build.MODEL);
                                            Log.wtf(TAG, "device_name");
                                        } else if (status == Data.APPLICATION_ID) {
                                            Log.wtf(TAG, "application_id");
                                            data.setResult(getPackageName());
                                        } else {
                                            Log.wtf(TAG, "some_thing_else");
                                            closeConnection();
                                            continue l1;
                                        }
                                        String responseJSONString = data.toJSON().toString();
                                        Log.wtf(TAG, "RES : " + responseJSONString);
                                        pw.println(responseJSONString);
                                    } catch (JSONException e) {
                                        closeConnection();
                                        continue l1;
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        Log.wtf(TAG, "SocketError : " + e.getLocalizedMessage());
                    }
                    return null;
                }
            }.execute();
        }
    }

    private void closeConnection() {
        try {
            pw.close();
            pw = null;
            br.close();
            socket.close();
            socket = null;
            serverSocket.close();
            serverSocket = null;
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
