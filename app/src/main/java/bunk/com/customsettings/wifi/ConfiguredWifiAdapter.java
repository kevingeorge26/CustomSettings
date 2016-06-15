package bunk.com.customsettings.wifi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import bunk.com.customsettings.R;

/**
 * Created by kevin varkey on 3/1/2016.
 */
public class ConfiguredWifiAdapter extends RecyclerView.Adapter<ConfiguredWifiAdapter.ViewHolder> {

    List<ConfiguredWifiModel> mlist;
    WifiDeleteListener listener;

    public ConfiguredWifiAdapter(List<ConfiguredWifiModel> mlist, WifiDeleteListener listener) {
        this.mlist = mlist;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.configured_wifi, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ConfiguredWifiModel configuredWifi = mlist.get(position);
        holder.wifiNameView.setText(configuredWifi.getWifiName());
        holder.profileNameView.setText(configuredWifi.getProfileName());


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView wifiNameView, profileNameView;
        ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            wifiNameView = (TextView) itemView.findViewById(R.id.configured_wifi_name);
            profileNameView = (TextView) itemView.findViewById(R.id.configured_profile_name);
            deleteButton = (ImageButton) itemView.findViewById(R.id.wifi_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.deleteWifiConfiguration(getAdapterPosition());
                }

            });


        }
    }

    public interface WifiDeleteListener {
        public void deleteWifiConfiguration(int position);
    }


    public static class ConfiguredWifiModel {
        private String wifiName, profileName;
        public ConfiguredWifiModel(String wifiName, String profileName) {
            this.wifiName = wifiName;
            this.profileName = profileName;
        }

        public String getWifiName() {
            return wifiName;
        }
        public String getProfileName() {
            return profileName;
        }
    }

}
