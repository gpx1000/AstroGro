package btle.astrogrow.test.btletest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import static java.lang.Math.min;


public class MainActivity extends ActionBarActivity {

    public Intent btleResult;
    public Context applicationContext;
    public Button bConnect, bDisconnect, bSend;
    public TextView tvUART;
    public EditText etTxData;
    private BtleModuleAPI mBtleService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bConnect = (Button) findViewById(R.id.bConnect);
        bDisconnect = (Button) findViewById(R.id.bDisconnect);
        bSend = (Button) findViewById(R.id.bSend);
        etTxData = (EditText) findViewById(R.id.etTxData);
        tvUART = (TextView) findViewById(R.id.bDisconnect);

        btleResult = new Intent();
        applicationContext = this;

        service_init();

        bConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBtleService.connect();
            }
        });

        bSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String data = etTxData.getText().toString();
                int remaining = data.length();
                while(remaining > 0)
                {
                    String dataToSend;
                    int bytesToSend = min(remaining, 20);
                    dataToSend = data.substring(0, bytesToSend);
                    mBtleService.sendData(dataToSend);
                    data = data.substring(bytesToSend, remaining);
                    remaining -= bytesToSend;
                }

                etTxData.setText("");
            }
        });
    }

    private void service_init() {
        Intent bindIntent = new Intent(this, BtleModuleAPI.class);
        boolean didItWork = getApplicationContext().bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(BtleStatusChangeReceiver, makeBtleUpdateIntentFilter());
    }

    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mBtleService = ((BtleModuleAPI.LocalBinder) rawBinder).getService();
            if (!mBtleService.init(applicationContext, btleResult)) {
                return;
            }

        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            mBtleService = null;
        }
    };

    private static IntentFilter makeBtleUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BtleModuleAPI.ACTION_DATA_RECEIVED);

        return intentFilter;
    }

    private final BroadcastReceiver BtleStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
            //*********************//
            if (action.equals(BtleModuleAPI.ACTION_DATA_RECEIVED)) {

            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mBtleService.onActivityResult(requestCode, resultCode, data);
    }
}
