package bunk.com.customsettings.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import bunk.com.customsettings.R;

public class WifiStateReceiver extends BroadcastReceiver {

    private static final String LOG_PREFIX = "WIFI_RECEIVER";
    public WifiStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                if (wifiInfo.getBSSID() != null) {
                    String connectedWifiName = wifiInfo.getSSID().replaceAll("\"","");
                    File file = new File(context.getFilesDir(), WifiFragment.WIFI_CONFIG);
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line;
                        while((line = reader.readLine()) != null) {
                            String[] configuredWifiDetails = line.split(WifiFragment.TERM_SEPRATOR);
                            if (configuredWifiDetails[0].equals(connectedWifiName)) {
                                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                if (configuredWifiDetails[1].equals(context.getString(R.string.dnd))) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                } else if (configuredWifiDetails[1].equals(context.getString(R.string.vibrate))) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                } else {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                }
                                break;
                            }
                        }
                        reader.close();

                    } catch (IOException e) {
                        Log.e(LOG_PREFIX, e.getStackTrace().toString());
                    }
                }
            }
        }
    }
}
