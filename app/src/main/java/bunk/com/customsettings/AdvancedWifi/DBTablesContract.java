package bunk.com.customsettings.AdvancedWifi;

import android.provider.BaseColumns;

/**
 * Created by kevin varkey on 6/13/2016.
 */
public class DBTablesContract {

    public DBTablesContract() {}


    public static abstract class WifiProfile implements BaseColumns {
        public static final String TABLE_NAME = "wifi_settings";
        public static final String COLUMN_NAME_WIFI_NAME = "wifi_name";
        public static final String COLUMN_NAME_SOUND_SETTING = "sound_settings";
        public static final String COLUMN_NAME_BLUETOOTH_SETTING = "bluetooth_settings";
        public static final String COLUMN_NAME_UNLOCK_SETTING = "unlock_settings";

    }
}
