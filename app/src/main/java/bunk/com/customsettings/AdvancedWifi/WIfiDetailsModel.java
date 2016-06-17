package bunk.com.customsettings.AdvancedWifi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin varkey on 6/13/2016.
 */
public class WifiDetailsModel implements Serializable{

    public static final int NOT_CONFIGURED = 0, ENABLE = 1, DISABLE = 2;
    public static final int SOUND_LOUD = 3, SOUND_SILENT = 4, SOUND_DND = 5;

    public String wifiName;
    public int bluetoothStatus, soundProfileStatus, unlockStatus;

    public WifiDetailsModel() {
        this.bluetoothStatus = this.soundProfileStatus = this.unlockStatus = WifiDetailsModel.NOT_CONFIGURED;
    }


    public static List<WifiDetailsModel> getAllConfiguredWifi(SQLiteDatabase readDB) {
        List<WifiDetailsModel> availableWifi = new ArrayList<>();
        Cursor cursor = readDB.rawQuery("select * from " + DBTablesContract.WifiProfile.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                WifiDetailsModel model = new WifiDetailsModel();

                model.wifiName = cursor.getString(cursor.getColumnIndex(DBTablesContract.WifiProfile.COLUMN_NAME_WIFI_NAME));
                model.soundProfileStatus = cursor.getInt(cursor.getColumnIndex(DBTablesContract.WifiProfile.COLUMN_NAME_SOUND_SETTING));
                model.bluetoothStatus = cursor.getInt(cursor.getColumnIndex(DBTablesContract.WifiProfile.COLUMN_NAME_BLUETOOTH_SETTING));
                model.unlockStatus = cursor.getInt(cursor.getColumnIndex(DBTablesContract.WifiProfile.COLUMN_NAME_UNLOCK_SETTING));
                availableWifi.add(model);

            } while(cursor.moveToNext());
        }

        return availableWifi;
    }

    public static void addOrUpdateWifi(WifiDetailsModel model, SQLiteDatabase writeDB){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBTablesContract.WifiProfile.COLUMN_NAME_WIFI_NAME, model.wifiName);
        contentValues.put(DBTablesContract.WifiProfile.COLUMN_NAME_BLUETOOTH_SETTING, model.bluetoothStatus);
        contentValues.put(DBTablesContract.WifiProfile.COLUMN_NAME_SOUND_SETTING, model.soundProfileStatus);
        contentValues.put(DBTablesContract.WifiProfile.COLUMN_NAME_UNLOCK_SETTING, model.unlockStatus);

        writeDB.insertWithOnConflict(DBTablesContract.WifiProfile.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

}
