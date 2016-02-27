package bunk.com.customsettings.wifi;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import bunk.com.customsettings.R;

public class WifiFragment extends Fragment {

    private View wifiFragmentView;
    private static List<String> availableWifiSSID;
    private static List<String> profileNameList = Arrays.asList("Silent", "Viberate", "Normal");
    private static ArrayAdapter<String> availableWifiListAdapter, availableSoundProfilesAdapter;
    public static HashMap<String, String> configuredWifiList = new HashMap<>();

    public WifiFragment() {
        // Required empty public constructor
    }

    public static WifiFragment newInstance() {
        WifiFragment fragment = new WifiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        wifiFragmentView = inflater.inflate(R.layout.fragment_wifi, container, false);
        getAvailableWIfi();
        createUITOAddWifi(wifiFragmentView);
        return wifiFragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getAvailableWIfi() {
        WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        availableWifiSSID = new ArrayList<String>();
        for(WifiConfiguration wifi: configuredNetworks) {
            availableWifiSSID.add(wifi.SSID.replaceAll("\"", ""));
        }
    }

    private void createUITOAddWifi(View wifiFragmentView) {
        availableWifiListAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                availableWifiSSID);

        availableSoundProfilesAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                profileNameList);

        final Spinner wifiSpinner = (Spinner) wifiFragmentView.findViewById(R.id.add_wifi_spinner);
        wifiSpinner.setAdapter(availableWifiListAdapter);

        final Spinner profileSpinner = (Spinner) wifiFragmentView.findViewById(R.id.add_profile_spinner);
        profileSpinner.setAdapter(availableSoundProfilesAdapter);

        Button addWifiButton = (Button) wifiFragmentView.findViewById(R.id.save_config_button);
        addWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedWifi;
                selectedWifi = wifiSpinner.getSelectedItem().toString();
                String selectedProfile;
                selectedProfile = profileSpinner.getSelectedItem().toString();
                saveWifiConfiguration(selectedWifi, selectedProfile);
            }
        });
    }



    private void saveWifiConfiguration(String wifiSSID, String profileName) {
        LinearLayout savedWifiEntryContainer  = new LinearLayout(getActivity());
        savedWifiEntryContainer.setOrientation(LinearLayout.HORIZONTAL);
        TextView wifiTextView = new TextView(getActivity());
        wifiTextView.setText(wifiSSID);
        savedWifiEntryContainer.addView(wifiTextView);

        TextView profileTextView = new TextView(getActivity());
        profileTextView.setText(profileName);
        savedWifiEntryContainer.addView(profileTextView);

        Button removeWifiButton = new Button(getActivity());
        removeWifiButton.setText("Delete");
        removeWifiButton.setOnClickListener(new View.OnClickListener(){
            private String deletedWifiSSID;

            @Override
            public void onClick(View v) {
                configuredWifiList.remove(this.deletedWifiSSID);
                LinearLayout deleteLayout = (LinearLayout) v.getParent();
                ((ViewGroup) deleteLayout.getParent()).removeView(deleteLayout);
            }

            public View.OnClickListener init(String wifiSSID) {
                this.deletedWifiSSID = wifiSSID;
                return this;
            }
        }.init(wifiSSID));
        savedWifiEntryContainer.addView(removeWifiButton);

        LinearLayout configuredWIfiContainer = (LinearLayout) wifiFragmentView.findViewById(R.id.saved_wifi);
        configuredWIfiContainer.addView(savedWifiEntryContainer);
    }


}
