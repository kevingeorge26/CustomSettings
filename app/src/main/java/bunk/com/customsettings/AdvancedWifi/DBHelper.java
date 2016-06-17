package bunk.com.customsettings.AdvancedWifi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kevin varkey on 6/13/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "custom_settings";
    public static final int DATABASE_VERSION = 3;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DBTablesContract.WifiProfile.TABLE_NAME + "(" +
        DBTablesContract.WifiProfile.COLUMN_NAME_WIFI_NAME + " TEXT PRIMARY KEY," +
        DBTablesContract.WifiProfile.COLUMN_NAME_BLUETOOTH_SETTING + " INTEGER, " +
        DBTablesContract.WifiProfile.COLUMN_NAME_SOUND_SETTING + " INTEGER, " +
        DBTablesContract.WifiProfile.COLUMN_NAME_UNLOCK_SETTING + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBTablesContract.WifiProfile.TABLE_NAME);
        onCreate(db);
    }

    public SQLiteDatabase getWriteDB() {
        return getWritableDatabase();
    }

    public SQLiteDatabase getReadDB() {
        return getReadableDatabase();
    }
}
