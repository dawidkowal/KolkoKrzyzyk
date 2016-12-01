package com.example.dkkow.kolkokrzyzyk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.*;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    public static WifiP2pInfo info;
    private WifiP2pManager mManager;
    private Channel mChannel;
    private MyWiFiActivity mActivity;
    private Context contextConnection;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, MyWiFiActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        contextConnection = context;

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                mActivity.setIsWifiP2p(true);
            } else {
                mActivity.setIsWifiP2p(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (mManager != null) {
                mManager.requestPeers(mChannel, (PeerListListener) mActivity.getFragmentManager().
                        findFragmentById(R.id.listUser));
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (mManager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // We are connected with the other device, request connection
                // info to find group owner IP

                mManager.requestConnectionInfo(mChannel,
                        new ConnectionInfoListener() {

                            @Override
                            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                                if (info != null) {
                                    WiFiDirectBroadcastReceiver.info = info;

                                    Intent i = new Intent(contextConnection, KolkoKrzyzyk.class);
                                    contextConnection.startActivity(i);
                                }
                            }
                        }
                );
            }
            else {
                mActivity.resetData();
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            mActivity.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        }
    }
}