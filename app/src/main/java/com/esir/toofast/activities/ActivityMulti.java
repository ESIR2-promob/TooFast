package com.esir.toofast.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.esir.toofast.R;
import com.esir.toofast.communication.AsyncClientInit;
import com.esir.toofast.controllers.Player;
import com.esir.toofast.communication.AsyncServerInit;
import com.esir.toofast.controllers.WiFiDirectBroadcastReceiver;
import com.esir.toofast.controllers.WifiPeerListAdapter;

public class ActivityMulti extends AppCompatActivity {

    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;
    private WifiPeerListAdapter mAdapter;
    ActivityMulti thisClass = this;
    final String TAG = "ActivityMulti";
    public boolean initIsExecuted ;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_multi);
        initIsExecuted = false;
        ListView peersList = findViewById(R.id.list_peer_devices);
        mAdapter = new WifiPeerListAdapter();
        peersList.setAdapter(mAdapter);
        peersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                connect(position);
            }
        });

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        Log.e("WIFI",receiver.toString());

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        BroadcastReceiver socketConnectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("SOCKET CONNECTION RECEIVER", "onReceive: " + intent.getAction());

                String action = intent.getAction();

                if (!initIsExecuted) {
                    if (action.equals("connection.server")) {
                        new AsyncServerInit(thisClass).execute();
                    } else if (action.equals("connection.client")) {
                        new AsyncClientInit(thisClass).execute(intent.getStringExtra("address"));
                    }
                    initIsExecuted = true;
                }
            }
        };

        IntentFilter filterServer = new IntentFilter("connection.server");
        IntentFilter filterClient = new IntentFilter("connection.client");

        this.getBaseContext().registerReceiver(socketConnectionReceiver,filterServer);
        this.getBaseContext().registerReceiver(socketConnectionReceiver,filterClient);

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank. Code for peer discovery goes in the
                // onReceive method, detailed below.
            }

            @Override
            public void onFailure(int reasonCode) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
                Toast.makeText(getApplicationContext(),"Error while discovering peerToPeer device",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void connect(int position) {
        WifiP2pDevice device = (WifiP2pDevice) mAdapter.getItem(position);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {
                Toast.makeText(getApplicationContext(), "Connect failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager,channel,this);
        registerReceiver(receiver, intentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public WifiPeerListAdapter getAdapter() {
        return mAdapter;
    }

    public void enableWiFi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Le wifi doit être activé pour pouvoir jouer à plusieurs")
                .setTitle("WiFi desactivé")
                .setCancelable(false)
                .setPositiveButton("Activer le Wifi",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.Q)
                            public void onClick(DialogInterface dialog, int id) {
                                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                                startActivityForResult(panelIntent, 17234);
                            }
                        }
                )
                .setNegativeButton("Retour",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getParent().finish();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 17234) {
            startActivity(getIntent());
        }
    }

    public void onAsyncServerInitResult(Boolean areSocketConnected){
        if(areSocketConnected){
            Log.d(TAG, "onAsyncServerInitResult: Socket are connected, launching the preparation");
            Player player = new Player("server", Player.MULTIJOUEUR);
            startActivity(new Intent(getBaseContext(), ActivityAttenteServeur.class).putExtra("Joueur",player));
        }
        else{
            Log.d(TAG, "onAsyncServerInitResult: Socket are not connected, canceling");

        }
    }

    public void onAsyncClientInitResult(Boolean areSocketConnected){
        if(areSocketConnected){
            Log.d(TAG, "onAsyncClientInitResult: Socket are connected, launching the preparation");
            Player player = new Player("client", Player.MULTIJOUEUR);
            startActivity(new Intent(getBaseContext(), ActivityAttenteClient.class).putExtra("Joueur",player));
        }
        else{
            Log.d(TAG, "onAsyncClientInitResult: Socket are not connected, canceling");
       }
    }
}