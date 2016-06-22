package bunk.com.customsettings.AdvancedWifi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bunk.com.customsettings.Helpers.DividerItemDecoration;
import bunk.com.customsettings.Helpers.Utils;
import bunk.com.customsettings.R;


public class AdvancedWifiFragment extends Fragment implements ConfiguredWifiAdapter.ConfiguredWifiAdapterListener{


    private View mView;
    FragmentActivity activity;
    private DBHelper mDbHelper;

    List<String> mAvailableWifi;
    List<WifiDetailsModel> mConfiguredWifi;
    ConfiguredWifiAdapter mConfiguredWifiAdapter;


    public AdvancedWifiFragment() {
        // Required empty public constructor
    }

    public static AdvancedWifiFragment newInstance() {
        AdvancedWifiFragment fragment = new AdvancedWifiFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_advanced_wifi, container, false);
        activity = getActivity();

        mDbHelper = new DBHelper(activity);
        mConfiguredWifi =  WifiDetailsModel.getAllConfiguredWifi(mDbHelper.getReadableDatabase());

        if (mConfiguredWifi == null) {
            mConfiguredWifi = new ArrayList<WifiDetailsModel>();
        }

        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.configured_wifi);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
        mConfiguredWifiAdapter = new ConfiguredWifiAdapter(activity, mConfiguredWifi, this);
        recyclerView.setAdapter(mConfiguredWifiAdapter);

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAvailableWifi = Utils.getAvailableWifi(activity);
                if (mAvailableWifi == null) {
                    Snackbar snackbar = Snackbar.make(mView, "Please enable Wi-Fi first", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.RED);
                    snackbar.show();
                } else {
                    Intent intent = new Intent(activity, WifiDetailsActivity.class);
                    intent.putStringArrayListExtra(WifiDetailsActivity.WIFI_AVAILABILITY_ARG, (ArrayList<String>) mAvailableWifi);
                    startActivity(intent);
                }

            }
        });

        return mView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        List<WifiDetailsModel> temp = WifiDetailsModel.getAllConfiguredWifi(mDbHelper.getReadableDatabase());
        if (temp != null) {
            mConfiguredWifi.clear();
            mConfiguredWifi.addAll(0, temp);
            mConfiguredWifiAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onWifiClick(int position) {
        Intent intent = new Intent(activity, WifiDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(WifiDetailsActivity.WIFI_DETAILS_ARG, mConfiguredWifi.get(position));
        startActivity(intent);

    }

}
