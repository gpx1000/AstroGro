package btle.astrogrow.test.btletest;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.util.Date;

import static java.lang.Math.min;

/**

 * Created by tpailevanian on 4/11/15.
 */

public class BtleModuleAPI extends Service{

    public static final String TAG = "BtleModuleAPI";
    public final static String ACTION_DATA_RECEIVED =
            "btle.astrogrow.test.btletest.ACTION_DATA_RECEIVED";
    public int startCount = 0;
    public boolean seqFound = false;
    public ConfigData configData = new ConfigData();

    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int UART_PROFILE_READY = 10;
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private static final int STATE_OFF = 10;
    private static final char START_CHAR = 'a';
    private static final int START_NUM = 4;
    private static final int RX_BUFFER_SIZE = 32;

    public byte rxBuffer[] = new byte[RX_BUFFER_SIZE];

    private int mState = UART_PROFILE_DISCONNECTED;
    private UartService mService = null;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;
    private Context context;
    private Intent intent;
    private boolean isConnected;


    public BtleModuleAPI()
    {
        super();
    }


    public boolean init(Context c, Intent i)
    {
        context = c;
        intent = i;
        isConnected = false;
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return false;
        }
        service_init();

        return true;
    }
    private void service_init() {
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((UartService.LocalBinder) rawBinder).getService();
            Log.d(TAG, "onServiceConnected mService= " + mService);
            if (!mService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }

        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            mService = null;
        }
    };

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
            //*********************//
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                mState = UART_PROFILE_CONNECTED;
                isConnected = true;
            }

            //*********************//
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                mState = UART_PROFILE_DISCONNECTED;
                mService.close();
                isConnected = false;
            }


            //*********************//
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mService.enableTXNotification();
            }
            //*********************//
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {

                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                parseData(txValue);
                //figure out buffer stuff

            }
            //*********************//
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
                Toast.makeText(context, "Device doesn't support UART. Disconnecting", Toast.LENGTH_LONG).show();
                mService.disconnect();
            }

        }
    };

    private void parseData(byte[] txVal)
    {
        if(!seqFound)
        {
            for(int i = 0; i< txVal.length; i++ )
            {
                if(txVal[i] == START_CHAR)
                {
                    startCount++;
                    if(startCount == START_NUM)
                    {
                        seqFound = true;
                        startCount = 0;
                        for(int j = i+1; j < txVal.length; j++)
                        {
                            rxBuffer[j-(i+1)]=txVal[j];
                            startCount++;
                        }
                    }
                }
                else
                {
                    startCount = 0;
                }

            }

        }
        else
        {
            for(int j = 0; j < min(txVal.length, RX_BUFFER_SIZE); j++)
            {
                rxBuffer[startCount+j]=txVal[j];
                startCount++;
            }

            // done
            if(startCount >= RX_BUFFER_SIZE) {

                ByteBuffer wrapped = ByteBuffer.wrap(rxBuffer); // big-endian by default

                configData.moisture = wrapped.getInt();
                configData.spectrum = wrapped.getInt();
                configData.temperature = wrapped.getInt();
                configData.id = wrapped.getInt();
                configData.timer = wrapped.getInt();
                configData.light = wrapped.getInt();
                configData.pump = wrapped.getInt();
                configData.heat = wrapped.getInt();

            }
        }

    }

    public void sendData(String data)
    {
        byte[] value;
        try {
            //send data to service
            value = data.getBytes("UTF-8");
            mService.writeRXCharacteristic(value);

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void connect()
    {

            if (!mBtAdapter.isEnabled()) {
                intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((Activity)context).startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
            else {
                if (!isConnected){

                    //Connect button pressed, open btle.astrogrow.test.btletest.DeviceListActivity class, with popup windows that scan for devices

                    intent.setClass(context, DeviceListActivity.class);
                    ((Activity)context).startActivityForResult(intent, REQUEST_SELECT_DEVICE);
                }
            }
    }

    public void disconnect()
    {
        if (mDevice!=null)
        {
            mService.disconnect();

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_SELECT_DEVICE:
                //When the btle.astrogrow.test.btletest.DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
                    mService.connect(deviceAddress);


                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth has turned on ", Toast.LENGTH_SHORT).show();

                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "Problem in BT Turning ON ", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
                Log.e(TAG, "wrong request code");
                break;
        }
    }

    public class LocalBinder extends Binder {
        BtleModuleAPI getService() {return BtleModuleAPI.this;}
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();


    public class ConfigData
    {
        public int moisture;
        public int spectrum;
        public int temperature;
        public int id ;
        public int timer;
        public int light;
        public int pump;
        public int heat;

        public ConfigData()
        {

        }

    }

}


