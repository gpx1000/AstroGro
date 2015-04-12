package com.astrogro.astrogro;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import btle.astrogrow.test.btletest.BtleModuleAPI;

/**
 * Created by swinston1 on 4/12/15.
 */
public class PodInfo extends Activity {
    public Intent btleResult;
    public Context applicationContext;
    private BtleModuleAPI mBtleService = null;
    TextView notificationArea;
    String notification = "Notifications:";

    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mBtleService = ((BtleModuleAPI.LocalBinder) rawBinder).getService();
            if (!mBtleService.init(applicationContext, btleResult)) {
                return;
            }
            mBtleService.connect();
        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            mBtleService = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pod_info_display);
        notificationArea = (TextView) findViewById(R.id.notification);

        applicationContext = this;
        btleResult = new Intent();
        service_init();
    }

    private static IntentFilter makeBtleUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BtleModuleAPI.ACTION_DATA_RECEIVED);
        intentFilter.addAction(BtleModuleAPI.ACTION_RX_DATA_VAlID);

        return intentFilter;
    }

    private final BroadcastReceiver BtleStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
            //*********************//
            if (action.equals(BtleModuleAPI.ACTION_DATA_RECEIVED)) {

            }
            if (action.equals(BtleModuleAPI.ACTION_RX_DATA_VAlID)) {
                notification = "Notification:";
                notification += "\nId: " + mBtleService.configData.id;
//                notification += "\nHeat: " + mBtleService.configData.heat;
//                notification += "\nLight: " + mBtleService.configData.light;
                notification += "\nMoisture: " + mBtleService.configData.moisture;
//                notification += "\nPump: " + mBtleService.configData.pump;
                notification += "\nSpectrum: " + mBtleService.configData.spectrum;
                notification += "\nTemperature: " + mBtleService.configData.temperature;
                notification += "\nTime: " + mBtleService.configData.timer;
                notificationArea.setText(notification);
                //do something with data
                mBtleService.resetRead();
            }
        }
    };

    private void service_init() {
        Intent bindIntent = new Intent(this, BtleModuleAPI.class);
        boolean didItWork = getApplicationContext().bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(BtleStatusChangeReceiver, makeBtleUpdateIntentFilter());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mBtleService.onActivityResult(requestCode, resultCode, data);
    }
}
