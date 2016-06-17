package bunk.com.customsettings.AdvancedWifi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import bunk.com.customsettings.R;

public class WifiDetailsActivity extends AppCompatActivity {

    public static final String WIFI_DETAILS_ARG = "wifi_details";
    public static final String WIFI_AVAILABILITY_ARG = "available_wifi_list";
    WifiDetailsModel mWifiDetails;
    DBHelper mDBHelper;
    ArrayList<String> availableWifi;
    Spinner mWifiSpinner;

    View mRootView;
    LinearLayout mSoundOptions, mBluetoothOptions, mLockOptions;
    boolean isEdited = false;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wifi_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_settings) {
            mWifiDetails.wifiName = mWifiSpinner.getSelectedItem().toString();
            WifiDetailsModel.addOrUpdateWifi(mWifiDetails, mDBHelper.getWritableDatabase());
            isEdited = false;
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isEdited) {
            showNotSaved();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_details);
        mRootView = findViewById(R.id.root_view);
        mDBHelper = new DBHelper(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSoundOptions  = (LinearLayout) findViewById(R.id.sound_options);
        mBluetoothOptions  = (LinearLayout) findViewById(R.id.bluetooth_options);
        mLockOptions = (LinearLayout) findViewById(R.id.lock_options);

        final Switch soundSwitch = (Switch) findViewById(R.id.sound_switch);
        Switch bluetoothSwitch = (Switch) findViewById(R.id.bluetooth_switch);
        Switch lockSwitch = (Switch) findViewById(R.id.lock_switch);

        RadioGroup soundRadioGroup = (RadioGroup) findViewById(R.id.sound_radio_group);
        RadioGroup bluetoothRadioGroup = (RadioGroup) findViewById(R.id.bluetooth_radio_group);
        RadioGroup lockRadioGroup = (RadioGroup) findViewById(R.id.lock_radio_group);

        final RadioButton soundLoudRadioButton = (RadioButton) findViewById(R.id.sound_loud);
        final RadioButton bluetoothOnRadioButton = (RadioButton) findViewById(R.id.bluetooth_enable);
        final RadioButton lockOnRadioButton = (RadioButton) findViewById(R.id.lock_enable);

        Intent intent = getIntent();
        mWifiDetails = (WifiDetailsModel) intent.getSerializableExtra(WIFI_DETAILS_ARG);
        availableWifi = intent.getStringArrayListExtra(WIFI_AVAILABILITY_ARG);

        // if this is edit prepare the UI
        if (mWifiDetails != null) {
            actionBar.setTitle("Edit");
            availableWifi = new ArrayList<>();
            availableWifi.add(mWifiDetails.wifiName);

            // check if sound profile is enabled
            if (mWifiDetails.soundProfileStatus != WifiDetailsModel.NOT_CONFIGURED) {
                soundSwitch.setChecked(true);
                mSoundOptions.setVisibility(View.VISIBLE);

                switch (mWifiDetails.soundProfileStatus) {
                    case WifiDetailsModel.SOUND_DND:
                        ((RadioButton) findViewById(R.id.sound_alarms)).setChecked(true);
                        break;
                    case WifiDetailsModel.SOUND_SILENT:
                        ((RadioButton) findViewById(R.id.sound_vibrate)).setChecked(true);
                        break;
                    case WifiDetailsModel.SOUND_LOUD:
                        ((RadioButton) findViewById(R.id.sound_loud)).setChecked(true);
                        break;
                }
            }

            if (mWifiDetails.bluetoothStatus != WifiDetailsModel.NOT_CONFIGURED) {
                bluetoothSwitch.setChecked(true);
                mBluetoothOptions.setVisibility(View.VISIBLE);
                switch (mWifiDetails.bluetoothStatus) {
                    case WifiDetailsModel.ENABLE:
                        ((RadioButton) findViewById(R.id.bluetooth_enable)).setChecked(true);
                        break;
                    case WifiDetailsModel.DISABLE:
                        ((RadioButton) findViewById(R.id.bluetooth_disable)).setChecked(true);
                        break;
                }
            }

            if (mWifiDetails.unlockStatus != WifiDetailsModel.NOT_CONFIGURED) {
                lockSwitch.setChecked(true);
                mLockOptions.setVisibility(View.VISIBLE);
                switch (mWifiDetails.unlockStatus) {
                    case WifiDetailsModel.ENABLE:
                        ((RadioButton) findViewById(R.id.lock_enable)).setChecked(true);
                        break;
                    case WifiDetailsModel.DISABLE:
                        ((RadioButton) findViewById(R.id.lock_disable)).setChecked(true);
                        break;
                }
            }
        } else {
            // if this is a not a edit we need to create a obj
            mWifiDetails = new WifiDetailsModel();
        }

        // populate the spinner
        mWifiSpinner = (Spinner) findViewById(R.id.available_wifi_spinner);
        ArrayAdapter wifiAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_icon, R.id.text, availableWifi);
        mWifiSpinner.setAdapter(wifiAdapter);

        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEdited = true;
                if (isChecked) {
                    mSoundOptions.setVisibility(View.VISIBLE);
                    mWifiDetails.soundProfileStatus = WifiDetailsModel.SOUND_LOUD;
                    soundLoudRadioButton.setChecked(true);

                } else {
                    mSoundOptions.setVisibility(View.GONE);
                    mWifiDetails.soundProfileStatus = WifiDetailsModel.NOT_CONFIGURED;
                }
            }
        });

        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEdited = true;
                if (isChecked) {
                    mBluetoothOptions.setVisibility(View.VISIBLE);
                    mWifiDetails.bluetoothStatus = WifiDetailsModel.ENABLE;
                    bluetoothOnRadioButton.setChecked(true);

                } else {
                    mBluetoothOptions.setVisibility(View.GONE);
                    mWifiDetails.bluetoothStatus = WifiDetailsModel.NOT_CONFIGURED;
                }
            }
        });

        lockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEdited = true;
                if (isChecked) {
                    mLockOptions.setVisibility(View.VISIBLE);
                    mWifiDetails.unlockStatus = WifiDetailsModel.ENABLE;
                    lockOnRadioButton.setChecked(true);

                } else {
                    mLockOptions.setVisibility(View.GONE);
                    mWifiDetails.unlockStatus = WifiDetailsModel.NOT_CONFIGURED;
                }
            }
        });

        soundRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isEdited = true;
                switch (checkedId) {
                    case R.id.sound_loud:
                        mWifiDetails.soundProfileStatus = WifiDetailsModel.SOUND_LOUD;
                        break;
                    case R.id.sound_vibrate:
                        mWifiDetails.soundProfileStatus = WifiDetailsModel.SOUND_SILENT;
                        break;
                    case R.id.sound_alarms:
                        mWifiDetails.soundProfileStatus = WifiDetailsModel.SOUND_DND;
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
                        mWifiDetails.bluetoothStatus = WifiDetailsModel.ENABLE;
                        break;
                    case R.id.bluetooth_disable:
                        mWifiDetails.bluetoothStatus = WifiDetailsModel.DISABLE;
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
                        mWifiDetails.unlockStatus = WifiDetailsModel.ENABLE;
                        break;
                    case R.id.lock_disable:
                        mWifiDetails.unlockStatus = WifiDetailsModel.DISABLE;
                        break;
                }
            }
        });
    }

    // show snackbar
    private void showNotSaved() {

        final Snackbar snackbar = Snackbar
                .make(mRootView, "Unsaved Changes", Snackbar.LENGTH_INDEFINITE);


        snackbar.setAction("Don't care", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdited = false;
                onBackPressed();
            }
        });

        snackbar.show();
    }

    // handle switch when clicked on the label
    public void triggerSwitchOperation(View v) {
        Switch temp =(Switch) v.findViewWithTag("switch");
        temp.setChecked(!temp.isChecked());
    }

}
