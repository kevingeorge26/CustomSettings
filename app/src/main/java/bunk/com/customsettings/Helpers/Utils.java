package bunk.com.customsettings.Helpers;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

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
}
