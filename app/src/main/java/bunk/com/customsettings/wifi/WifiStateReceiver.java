package bunk.com.customsettings.wifi;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import bunk.com.customsettings.AdvancedWifi.DBHelper;
import bunk.com.customsettings.AdvancedWifi.WifiDetailsModel;
import bunk.com.customsettings.Default.DefaultSettingsFragment;
import bunk.com.customsettings.Helpers.Utils;

public class WifiStateReceiver extends BroadcastReceiver {

    private static final String LOG_PREFIX = "WIFI_RECEIVER";
    public WifiStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
                WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                if (wifiInfo.getBSSID() != null) {
                    String connectedWifiName = wifiInfo.getSSID().replaceAll("\"","");
                    DBHelper dbHelper = new DBHelper(context);
                    WifiDetailsModel model = WifiDetailsModel.getWifiDetails(connectedWifiName, dbHelper.getReadDB());

                    if (model != null) {
                        Utils.changeNotificationSettings(context, model.soundProfileStatus);
                        Utils.changeBluetoothSettings(context, model.bluetoothStatus);
                    }

                }
            } else if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(DefaultSettingsFragment.SHARED_PREFERENCE_ARG, Context.MODE_PRIVATE);
                int soundSetting = sharedPreferences.getInt(DefaultSettingsFragment.SOUND_OPTIONS_ARG, WifiDetailsModel.NOT_CONFIGURED);
                int bluetoothSetting = sharedPreferences.getInt(DefaultSettingsFragment.BLUETOOTH_OPTIONS_ARG, WifiDetailsModel.NOT_CONFIGURED);
                Utils.changeBluetoothSettings(context, soundSetting);
                Utils.changeBluetoothSettings(context, bluetoothSetting);
            }
        }
    }


}
