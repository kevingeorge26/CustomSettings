package bunk.com.customsettings.AdvancedWifi;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import bunk.com.customsettings.R;

/**
 * Created by kevin varkey on 6/13/2016.
 */
public class ConfiguredWifiAdapter extends RecyclerView.Adapter<ConfiguredWifiAdapter.ViewHolder> {

    List<WifiDetailsModel> mList;
    ConfiguredWifiAdapterListener mListener;
    Context mContext;


    public ConfiguredWifiAdapter(Context context, List content, ConfiguredWifiAdapterListener listener) {
        this.mList = content;
        this.mListener = listener;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.advanced_configured_wifi, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WifiDetailsModel wifiDetails = mList.get(position);

        holder.position = position;
        holder.wifiName.setText(wifiDetails.wifiName);
        // set the activated icons

        int imageType = 0;
        if (wifiDetails.soundProfileStatus !=  WifiDetailsModel.NOT_CONFIGURED) {

            switch (wifiDetails.soundProfileStatus) {
                case WifiDetailsModel.SOUND_SILIENT:
                    imageType = android.R.drawable.ic_lock_silent_mode;
                    break;
                case WifiDetailsModel.SOUND_LOUD:
                    imageType = android.R.drawable.ic_lock_silent_mode_off;
                    break;
                case WifiDetailsModel.SOUND_DND:
                    imageType = android.R.drawable.ic_lock_idle_alarm;
                    break;
            }

            if (imageType > 0) {
                addSettingsIcon(imageType, holder.settingsIndicatorLayout);
            }

        }
        if (wifiDetails.bluetoothStatus !=  WifiDetailsModel.NOT_CONFIGURED) {
            imageType = 0;
            switch (wifiDetails.bluetoothStatus) {
                case WifiDetailsModel.DISABLE:
                    imageType = R.drawable.ic_bluetooth_disabled_black_24dp;
                    break;
                case WifiDetailsModel.ENABLE:
                    imageType = R.drawable.ic_bluetooth_black_24dp;
                    break;
            }

            if (imageType > 0) {
                addSettingsIcon(imageType, holder.settingsIndicatorLayout);
            }
        }
        if (wifiDetails.unlockStatus !=  WifiDetailsModel.NOT_CONFIGURED) {

            imageType = 0;
            switch (wifiDetails.unlockStatus) {
                case WifiDetailsModel.DISABLE:
                    imageType = R.drawable.ic_lock_open_black_24dp;
                    break;
                case WifiDetailsModel.ENABLE:
                    imageType = R.drawable.ic_lock_outline_black_24dp;
                    break;
            }

            if (imageType > 0) {
                addSettingsIcon(imageType, holder.settingsIndicatorLayout);
            }
        }
    }

    private void addSettingsIcon(int imageType, LinearLayout holder) {
        ImageView icon = new ImageView(mContext);
        icon.setImageResource(imageType);
        icon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        holder.addView(icon);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView wifiName;
        LinearLayout settingsIndicatorLayout;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            wifiName = (TextView) itemView.findViewById(R.id.wifi_name);
            settingsIndicatorLayout = (LinearLayout) itemView.findViewById(R.id.setting_indicator_layout);
        }

        @Override
        public void onClick(View v) {
            mListener.onWifiClick(position);
        }
    }

    public interface ConfiguredWifiAdapterListener {
        public void onWifiClick(int position);
    }
}
