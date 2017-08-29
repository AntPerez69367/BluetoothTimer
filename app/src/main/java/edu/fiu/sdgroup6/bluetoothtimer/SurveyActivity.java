package edu.fiu.sdgroup6.bluetoothtimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {

    private String p2pName;
    private WifiP2pManager mManager;
    private Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter = new IntentFilter();
    private List<WifiP2pDevice> peers;

    private ListView mListView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        peers = new ArrayList<>();
        p2pName = setTitleName(savedInstanceState);
        if (p2pName.isEmpty()){
            Log.d("p2pName", "No name specified.");
            Toast.makeText(this, "Please enter a survey name.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Log.d("p2pName", "ok");
            InitializeP2P();
        }
    }

    private String setTitleName(Bundle savedInstanceState){
        String surveyName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                surveyName = null;
            } else {
                surveyName = extras.getString("id");
            }
        } else {
            surveyName = (String) savedInstanceState.getSerializable("id");
        }
        setTitle(surveyName  + "'s survey.");
        return surveyName;
    }

    private void InitializeP2P() {
        // Retrieve information from editText box on Main Activity.

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        if (mManager != null) {
            mChannel = mManager.initialize(this, getMainLooper(), null);
            mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


            mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
//                    Toast.makeText(SurveyActivity.this, "Found devices", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i) {
//                    Toast.makeText(SurveyActivity.this, "Unable to find any devices", Toast.LENGTH_SHORT).show();
                }
            });

            PeerListListener peerListListener = new PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                    //delete old peers in the list
                    peers.clear();
                    peers.addAll(wifiP2pDeviceList.getDeviceList());

                    //To Do - Display Results into a list
//                    mListView = (ListView) findViewById(R.id.peerList);
//                    ArrayAdapter<WifiP2pDevice> adapter = new ArrayAdapter<WifiP2pDevice>(SurveyActivity.this,android.R.layout.simple_list_item_1,peers);
//                    mListView.setAdapter(adapter);


                    if (peers.size() == 0) {
                            Log.d("WiFi-Direct", "No devices found");
                    }
                }
            };

        }
        else
        {
            Toast.makeText(this, "Error Initiating P2P", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
    }

