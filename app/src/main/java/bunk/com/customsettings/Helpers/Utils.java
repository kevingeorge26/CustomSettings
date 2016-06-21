package bunk.com.customsettings.Helpers;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bunk.com.customsettings.AdvancedWifi.WifiDetailsModel;

/**
 * Created by kevin varkey on 6/12/2016.
 */
public class Utils {

    public static List<String> getAvailableWifi(FragmentActivity activity) {
        List<String> availableWifiSSID;
        WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            return null;
        } else {
            List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
            availableWifiSSID = new ArrayList<String>();
            for (WifiConfiguration wifi : configuredNetworks) {
                availableWifiSSID.add(wifi.SSID.replaceAll("\"", ""));
            }
            return  availableWifiSSID;
        }
    }

    /**
     *
     * @param context
     * @return Name of the connected wifi or null if not connected
     */
    public static String getCurrentWifiName(Context context) {

        String wifiName = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            wifiName = wifiInfo.getSSID().replaceAll("\"","");
        }

        return wifiName;
    }

    public static void changeNotificationSettings(Context context, int notificationSetting) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        switch (notificationSetting) {
            case WifiDetailsModel.SOUND_LOUD:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            case WifiDetailsModel.SOUND_SILENT:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            case WifiDetailsModel.SOUND_DND:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
        }
    }

    public static void changeBluetoothSettings(Context context, int bluetoothSetting) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        switch (bluetoothSetting) {
            case WifiDetailsModel.ENABLE:
                mBluetoothAdapter.enable();
                break;
            case WifiDetailsModel.DISABLE:
                mBluetoothAdapter.disable();
                break;
        }
    }
}
