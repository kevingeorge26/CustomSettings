package bunk.com.customsettings.Default;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import bunk.com.customsettings.AdvancedWifi.WifiDetailsModel;
import bunk.com.customsettings.Helpers.Utils;
import bunk.com.customsettings.R;
import bunk.com.customsettings.wifi.WifiStateReceiver;

public class DefaultSettingsFragment extends Fragment {

    public static final String SHARED_PREFERENCE_ARG = "default_settings";
    public static final String SOUND_OPTIONS_ARG = "sound_options";
    public static final String BLUETOOTH_OPTIONS_ARG = "bluetooth_options";
    public static final String LOCK_OPTIONS_ARG = "lock_options_arg";

    View mView;
    FragmentActivity mActivity;
    SharedPreferences sharedPreferences;
    boolean isEdited = false;
    int soundSetting, bluetoothSetting, lockSetting;

    LinearLayout mSoundOptions, mBluetoothOptions, mLockOptions;

    public DefaultSettingsFragment() {
        // Required empty public constructor
    }

    public static DefaultSettingsFragment newInstance() {
        DefaultSettingsFragment fragment = new DefaultSettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.default_settings_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_settings) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SOUND_OPTIONS_ARG, soundSetting);
            editor.putInt(BLUETOOTH_OPTIONS_ARG, bluetoothSetting);
            editor.putInt(LOCK_OPTIONS_ARG, lockSetting);
            editor.commit();
            Toast toast =Toast.makeText(mActivity, "Settings Saved", Toast.LENGTH_SHORT);
            toast.show();

            String wifiName = Utils.getCurrentWifiName(mActivity.getBaseContext());
            if (wifiName == null) {
                Utils.changeNotificationSettings(mActivity.getBaseContext(), soundSetting);
                Utils.changeBluetoothSettings(mActivity.getBaseContext(), bluetoothSetting);
            }

        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_default_settings, container, false);
        mActivity = getActivity();

        setHasOptionsMenu(true);

        sharedPreferences = mActivity.getSharedPreferences(SHARED_PREFERENCE_ARG, Context.MODE_PRIVATE);
        soundSetting = sharedPreferences.getInt(SOUND_OPTIONS_ARG, WifiDetailsModel.NOT_CONFIGURED);
        bluetoothSetting = sharedPreferences.getInt(BLUETOOTH_OPTIONS_ARG, WifiDetailsModel.NOT_CONFIGURED);
        lockSetting = sharedPreferences.getInt(LOCK_OPTIONS_ARG, WifiDetailsModel.NOT_CONFIGURED);


        mSoundOptions  = (LinearLayout) mView.findViewById(R.id.sound_options);
        mBluetoothOptions  = (LinearLayout) mView.findViewById(R.id.bluetooth_options);
        mLockOptions = (LinearLayout) mView.    findViewById(R.id.lock_options);

        final Switch soundSwitch = (Switch) mView.findViewById(R.id.sound_switch);
        final Switch bluetoothSwitch = (Switch) mView.findViewById(R.id.bluetooth_switch);
        final Switch lockSwitch = (Switch) mView.findViewById(R.id.lock_switch);

        RadioGroup soundRadioGroup = (RadioGroup)  mView.findViewById(R.id.sound_radio_group);
        RadioGroup bluetoothRadioGroup = (RadioGroup)  mView.findViewById(R.id.bluetooth_radio_group);
        RadioGroup lockRadioGroup = (RadioGroup)  mView.findViewById(R.id.lock_radio_group);

        final RadioButton soundLoudRadioButton = (RadioButton)  mView.findViewById(R.id.sound_loud);
        final RadioButton bluetoothOnRadioButton = (RadioButton)  mView.findViewById(R.id.bluetooth_enable);
        final RadioButton lockOnRadioButton = (RadioButton)  mView.findViewById(R.id.lock_enable);

        // set up the UI for sound settings
        if (soundSetting != WifiDetailsModel.NOT_CONFIGURED) {
            soundSwitch.setChecked(true);
            mSoundOptions.setVisibility(View.VISIBLE);

            switch (soundSetting) {
                case WifiDetailsModel.SOUND_DND:
                    ((RadioButton) mView.findViewById(R.id.sound_alarms)).setChecked(true);
                    break;
                case WifiDetailsModel.SOUND_SILENT:
                    ((RadioButton) mView.findViewById(R.id.sound_vibrate)).setChecked(true);
                    break;
                case WifiDetailsModel.SOUND_LOUD:
                    ((RadioButton) mView.findViewById(R.id.sound_loud)).setChecked(true);
                    break;
            }
        }

        if (bluetoothSetting != WifiDetailsModel.NOT_CONFIGURED) {
            bluetoothSwitch.setChecked(true);
            mBluetoothOptions.setVisibility(View.VISIBLE);
            switch (bluetoothSetting) {
                case WifiDetailsModel.ENABLE:
                    ((RadioButton) mView.findViewById(R.id.bluetooth_enable)).setChecked(true);
                    break;
                case WifiDetailsModel.DISABLE:
                    ((RadioButton) mView.findViewById(R.id.bluetooth_disable)).setChecked(true);
                break;
            }
        }

        if (lockSetting != WifiDetailsModel.NOT_CONFIGURED) {
            lockSwitch.setChecked(true);
            mLockOptions.setVisibility(View.VISIBLE);
            switch (lockSetting) {
                case WifiDetailsModel.ENABLE:
                    ((RadioButton) mView.findViewById(R.id.lock_enable)).setChecked(true);
                    break;
                case WifiDetailsModel.DISABLE:
                    ((RadioButton) mView.findViewById(R.id.lock_disable)).setChecked(true);
                    break;
            }
        }

        // listener for switch
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEdited = true;
                if (isChecked) {
                    mSoundOptions.setVisibility(View.VISIBLE);
                    soundSetting = WifiDetailsModel.SOUND_LOUD;
                    soundLoudRadioButton.setChecked(true);

                } else {
                    mSoundOptions.setVisibility(View.GONE);
                    soundSetting = WifiDetailsModel.NOT_CONFIGURED;
                }
            }
        });

        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEdited = true;
                if (isChecked) {
                    mBluetoothOptions.setVisibility(View.VISIBLE);
                    bluetoothOnRadioButton.setChecked(true);
                    bluetoothSetting = WifiDetailsModel.ENABLE;

                } else {
                    mBluetoothOptions.setVisibility(View.GONE);
                    bluetoothSetting = WifiDetailsModel.NOT_CONFIGURED;
                }
            }
        });

        lockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEdited = true;
                if (isChecked) {
                    mLockOptions.setVisibility(View.VISIBLE);
                    lockSetting = WifiDetailsModel.ENABLE;
                    lockOnRadioButton.setChecked(true);

                } else {
                    mLockOptions.setVisibility(View.GONE);
                    lockSetting = WifiDetailsModel.NOT_CONFIGURED;
                }
            }
        });

        soundRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isEdited = true;
                switch (checkedId) {
                    case R.id.sound_loud:
                        soundSetting = WifiDetailsModel.SOUND_LOUD;
                        break;
                    case R.id.sound_vibrate:
                        soundSetting = WifiDetailsModel.SOUND_SILENT;
                        break;
                    case R.id.sound_alarms:
                        soundSetting = WifiDetailsModel.SOUND_DND;
                        break;
                }
            }
        });

        bluetoothRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isEdited = true;
                switch (checkedId) {
                    case R.id.bluetooth_enable:
                        bluetoothSetting = WifiDetailsModel.ENABLE;
                        break;
                    case R.id.bluetooth_disable:
                        bluetoothSetting = WifiDetailsModel.DISABLE;
                        break;
                }
            }
        });

        lockRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isEdited = true;
                switch (checkedId) {
                    case R.id.lock_enable:
                        lockSetting = WifiDetailsModel.ENABLE;
                        break;
                    case R.id.lock_disable:
                        lockSetting = WifiDetailsModel.DISABLE;
                        break;
                }
            }
        });


        // handle on click on Switch text with ripple effect
        mView.findViewById(R.id.sound_switch_holder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundSwitch.setChecked(!soundSwitch.isChecked());
            }
        });


        mView.findViewById(R.id.bluetooth_switch_holder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothSwitch.setChecked(!bluetoothSwitch.isChecked());
            }
        });

        mView.findViewById(R.id.lock_switch_holder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockSwitch.setChecked(!lockSwitch.isChecked());
            }
        });


        return mView;
    }

    // handle switch when clicked on the label
    public void triggerSwitchOperation(View v) {
        Switch temp =(Switch) v.findViewWithTag("switch");
        temp.setChecked(!temp.isChecked());
    }

}
