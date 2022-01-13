package com.esir.toofast.controllers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.esir.toofast.activities.ActivityMulti;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private ActivityMulti activity;
    private List<WifiP2pDevice> peers = new ArrayList<>();

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       ActivityMulti activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        WifiP2pManager.ConnectionInfoListener myConnectionInfoListener = info -> {
            Log.d("WIFIDIRECT LAUNCH", "onConnectionInfoAvailable: in");

            String groupOwnerAddress = info.groupOwnerAddress.getHostAddress();

            Intent intent1;

            // After the group negotiation, we can determine the group owner.
            if (info.groupFormed && info.isGroupOwner) {
                intent1 = new Intent("connection.server");
                activity.sendBroadcast(intent1);
                Log.d("WIFIDIRECT LAUNCH", "onConnectionInfoAvailable: broadcast envoyé");
            } else if (info.groupFormed) {
                intent1 = new Intent("connection.client");
                intent1.putExtra("address",groupOwnerAddress);
                activity.sendBroadcast(intent1);
                Log.d("WIFIDIRECT LAUNCH", "onConnectionInfoAvailable: broadcast envoyé");
            }

        };

        WifiP2pManager.PeerListListener myPeerListListener = peerList -> {

            Collection<WifiP2pDevice> refreshedPeers = peerList.getDeviceList();
            // If an AdapterView is backed by this data, notify it
            // of the change. For instance, if you have a ListView of
            // available peers, trigger an update.
            activity.getAdapter().setData(refreshedPeers);

            if (refreshedPeers.size() == 0) {
                Log.d(""+activity.getLocalClassName(), "No devices found");
            }
        };

        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                Log.e("WIFIPTWOP","WIFI P2P ENABLED");
            } else {
                // Wi-Fi P2P is not enabled
                activity.enableWiFi();
                Log.e("WIFIPTWOP","WIFI P2P NOT ENABLED");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()

            if (manager != null) {
                manager.requestPeers(channel, myPeerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnection

            Log.d("WIFIDIRECT LAUNCH", "State changed, disconnect or new connection");

            if(manager==null)return;

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            Log.d("WIFIDIRECT LAUNCH", "State changed, manager not null");
            if(networkInfo.isConnected()) {
                Log.d("WIFIDIRECT LAUNCH", "State changed, is connected");
                manager.requestConnectionInfo(channel, myConnectionInfoListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }

}