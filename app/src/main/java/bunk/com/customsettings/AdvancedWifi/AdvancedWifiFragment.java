package bunk.com.customsettings.AdvancedWifi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import bunk.com.customsettings.Helpers.Utils;
import bunk.com.customsettings.R;


public class AdvancedWifiFragment extends Fragment implements ConfiguredWifiAdapter.ConfiguredWifiAdapterListener{


    private OnFragmentInteractionListener mListener;
    private View mView;
    private FragmentActivity activity;
    private DBHelper mDbHelper;
    List<String> availableWifi;
    List<WifiDetailsModel> mConfiguredWifi;

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

        availableWifi = Utils.getAvailableWifi(activity);
        if (availableWifi == null) {
            Snackbar snackbar = Snackbar.make(mView, "Please enable wifi to Edit settings", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }

        mDbHelper = new DBHelper(activity);
        mConfiguredWifi =  WifiDetailsModel.getAllConfiguredWifi(mDbHelper.getReadableDatabase());

        if (mConfiguredWifi == null) {
            mConfiguredWifi = new ArrayList<WifiDetailsModel>();
        }

        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.configured_wifi);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(new ConfiguredWifiAdapter(activity, mConfiguredWifi, this));

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WifiDetailsActivity.class);
                intent.putStringArrayListExtra(WifiDetailsActivity.WIFI_AVAILABILITY_ARG, (ArrayList<String>) availableWifi);
                startActivity(intent);
            }
        });

        return mView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onWifiClick(int position) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
