package bunk.com.customsettings.wifi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class WifiFragment extends Fragment implements ConfiguredWifiAdapter.WifiDeleteListener {

    private View wifiFragmentView;
    private static final String LOG_PREFIX = "CUSTOMSETTINGS_WFIFI";
    private static List<String> availableWifiSSID;
    private static List<String> profileNameList = Arrays.asList("Silent", "Viberate", "Normal");
    private static ArrayAdapter<String> availableWifiListAdapter, availableSoundProfilesAdapter;
    private static List<ConfiguredWifiAdapter.ConfiguredWifiModel> mList = new ArrayList<>();

    ConfiguredWifiAdapter adapterConfiguredWifi;
    public WifiFragment() {
        // Required empty public constructor
    }

    public static WifiFragment newInstance() {
        WifiFragment fragment = new WifiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void deleteWifiConfiguration(int position) {
        mList.remove(position);
        adapterConfiguredWifi.notifyItemRemoved(position);
        notifyWifiAddRemove();
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

        RecyclerView rvWifi = (RecyclerView) wifiFragmentView.findViewById(R.id.configured_wifi_recycle_view);
        adapterConfiguredWifi = new ConfiguredWifiAdapter(mList, this);
        rvWifi.setAdapter(adapterConfiguredWifi);
        rvWifi.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        if (!wifiManager.isWifiEnabled()) {
            Log.e(LOG_PREFIX, wifiFragmentView.toString());
            Snackbar snackbar = Snackbar.make(wifiFragmentView, "Please enable wifi", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        } else {
            List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
            availableWifiSSID = new ArrayList<String>();
            for (WifiConfiguration wifi : configuredNetworks) {
                availableWifiSSID.add(wifi.SSID.replaceAll("\"", ""));
            }
            createUITOAddWifi(wifiFragmentView);
        }
    }

    private void createUITOAddWifi(View wifiFragmentView) {
        availableWifiListAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                availableWifiSSID) {
            @Override
            public boolean isEnabled(int position) {
                String wifiSSID = availableWifiSSID.get(position);
                for(ConfiguredWifiAdapter.ConfiguredWifiModel model: mList) {
                    if(model.getWifiName().equals(wifiSSID)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                String wifiSSID = availableWifiSSID.get(position);
                for(ConfiguredWifiAdapter.ConfiguredWifiModel model: mList) {
                    if(model.getWifiName().equals(wifiSSID)) {
                        view.setTextColor(Color.GRAY);
                    } else {
                        view.setTextColor(Color.BLACK);
                    }
                }

                return view;


            }
        };
        availableWifiListAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

        availableSoundProfilesAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                profileNameList);
        availableSoundProfilesAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

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

    // call this function enable/disable wifi names in the add section
    private static void notifyWifiAddRemove() {
        availableWifiListAdapter.notifyDataSetChanged();
    }

    private void saveWifiConfiguration(String wifiSSID, String profileName) {
        int start = mList.size();
        ConfiguredWifiAdapter.ConfiguredWifiModel model = new ConfiguredWifiAdapter.ConfiguredWifiModel(wifiSSID, profileName);
        mList.add(model);
        adapterConfiguredWifi.notifyItemRangeInserted(start, 1);
        notifyWifiAddRemove();
    }


}
