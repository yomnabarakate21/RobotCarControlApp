package com.example.owner.firsttrial;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;


public class ControlCar extends AppCompatActivity {
    String address = null;
    private ProgressDialog mProgressDialog;
    private BluetoothAdapter mAdapter;
    private BluetoothSocket mSocket;
    private boolean connected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //UUID specified to Bluetooth serial board
    TextView connectionStaus_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        address = newint.getStringExtra(BluetoothList.EXTRA_ADDRESS); //obtain address of the bluetooth device chosen.
        setContentView(R.layout.activity_control_car);
        connectionStaus_tv = (TextView) findViewById(R.id.connection_tv);
        connectionStaus_tv.setText(getStatus(connected));
        BTConnect bluetooth = new BTConnect();   //make an instance of the inner class BTConnect
        bluetooth.execute();
        connectionStaus_tv.setText(getStatus(connected)); //error here the textview doesn't change its content although connected

    }

    //Inner class used for establishing the connection in the background.
    class BTConnect extends AsyncTask<Void, Void, Void> {
        boolean connectionSuccess = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(ControlCar.this, "Connecting", "Please wait!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            if (!(mSocket == null || !connected))
                return null;

            try {
                mAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice mdevice = mAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
                mSocket = mdevice.createInsecureRfcommSocketToServiceRecord(myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                mSocket.connect();//start connection
            } catch (IOException e) {
                connectionSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (!connectionSuccess) {
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                connected = true;
            }
            mProgressDialog.dismiss();
        }

    }

    static private String getStatus(boolean connected) {
        if (connected)
            return "Connected";
        else return "Not Connected";
    }
}



