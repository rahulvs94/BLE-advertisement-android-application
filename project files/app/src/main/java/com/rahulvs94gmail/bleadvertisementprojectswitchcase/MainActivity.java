package com.rahulvs94gmail.bleadvertisementprojectswitchcase;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;

import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public BluetoothAdapter bluetoothAdapter;
    private AdvertiseData mAdvData;
    private AdvertiseSettings mAdvSettings;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private static final UUID CLIENT_CHARACTERISTIC_CONFIGURATION_UUID = UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");


    private final AdvertiseCallback advertisingCallback = new AdvertiseCallback()
    {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect)
        {
            super.onStartSuccess(settingsInEffect);
        }

        @Override
        public void onStartFailure(int errorCode)
        {
            Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
            super.onStartFailure(errorCode);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Button advertise = (Button) findViewById(R.id.button4);
        Button turn_on = (Button) findViewById(R.id.button2);
        Button turn_off = (Button) findViewById(R.id.button3);
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        advertise.setOnClickListener(this);
        turn_on.setOnClickListener(this);
        turn_off.setOnClickListener(this);
        if( !BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported() )
        {
            Toast.makeText( this, "Multiple advertisement not supported", Toast.LENGTH_SHORT ).show();
        }

       // mBluetoothGattService = mCurrentServiceFragment.getBluetoothGattService();

        mAdvSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setConnectable(true)
                .build();
        mAdvData = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(true)
                .addServiceUuid(new ParcelUuid( UUID.fromString( getString( R.string.ble_uuid ) ) ))
                .build();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.button2:
                if (bluetoothAdapter == null)
                {
                    Toast.makeText(getApplicationContext(), "No Bluetooth Detected", Toast.LENGTH_LONG).show();
                } else
                {
                    if (!bluetoothAdapter.isEnabled())
                    {
                        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBluetoothIntent, 1);
                    }
                }
                break;
            case R.id.button3:
                bluetoothAdapter.disable();
                break;
            case R.id.button4:
                advertise();
                break;
        }
    }

    public void advertise()
        {
            BluetoothLeAdvertiser advertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
            /*AdvertiseSettings settings = new AdvertiseSettings.Builder()
                    .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                    .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                    .setTimeout(0)
                    .setConnectable(true)
                    .build();
            ParcelUuid pUuid = new ParcelUuid( UUID.fromString( getString( R.string.ble_uuid ) ) );
            AdvertiseData data = new AdvertiseData.Builder()
                    .setIncludeDeviceName(true)
                    .setIncludeTxPowerLevel(true)
                    .addServiceUuid(pUuid)
                    .build();*/
            advertiser.startAdvertising(mAdvSettings, mAdvData, advertisingCallback);
        }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(getApplicationContext(), "Bluetooth has been enabled.",Toast.LENGTH_SHORT).show();
            } else
            {
                Toast.makeText(getApplicationContext(), "Bluetooth is not enabled.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
