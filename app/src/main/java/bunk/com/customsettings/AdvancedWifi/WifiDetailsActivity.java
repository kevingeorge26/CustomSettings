package bunk.com.customsettings.AdvancedWifi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import bunk.com.customsettings.R;

public class WifiDetailsActivity extends AppCompatActivity {

    public static final String WIFI_DETAILS_ARG = "wifi_details";
    public static final String WIFI_AVAILABILITY_ARG = "available_wifi_list";
    WifiDetailsModel mWifiDetails;
    ArrayList<String> availableWifi;

    LinearLayout mSoundOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_details);

        Intent intent = getIntent();
        mWifiDetails = (WifiDetailsModel) intent.getSerializableExtra(WIFI_DETAILS_ARG);
        availableWifi = intent.getStringArrayListExtra(WIFI_AVAILABILITY_ARG);

        // populate the spinner
        Spinner wifiSpinner = (Spinner) findViewById(R.id.available_wifi_spinner);
        ArrayAdapter wifiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, availableWifi);
        wifiSpinner.setAdapter(wifiAdapter);


        mSoundOptions  = (LinearLayout) findViewById(R.id.sound_options);

        Switch sound_switch = (Switch) findViewById(R.id.sound_switch);
        sound_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSoundOptions.setVisibility(View.VISIBLE);
                    mSoundOptions.setAlpha(0.0f);
                    mSoundOptions.animate()
                            .translationY(mSoundOptions.getHeight())

                } else {

                    mSoundOptions.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
//                                    mSoundOptions.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });



    }
}
