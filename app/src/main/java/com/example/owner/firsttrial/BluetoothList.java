package com.example.owner.firsttrial;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothList extends AppCompatActivity {
    Button enableBluetooth_btn, showPairedDevices_btn;
    ListView pairedDevices_lv;
    public static String EXTRA_ADDRESS = null;
    public static final int ENABLE_REQUEST = 1;
    private Context context;
    private BluetoothAdapter mBluetooth = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> pairedDevices_set;
    private ArrayList pairedDevices_array = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);
        enableBluetooth_btn = (Button) findViewById(R.id.btn_enable_bluetooth);
        showPairedDevices_btn = (Button) findViewById(R.id.btn_list_devices);
        pairedDevices_lv = (ListView) findViewById(R.id.lv_paired_devices);
        context = this;
        enableBluetooth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetooth == null) {

                    Toast.makeText(getApplicationContext(), "Bluetooth on supporetd on device!", Toast.LENGTH_LONG).show();
                    finish();
                }
                if (!mBluetooth.isEnabled()) {
                    Intent enableIntent = new Intent(mBluetooth.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, ENABLE_REQUEST);
                    Toast.makeText(getApplicationContext(), "Enabling bluetooth", Toast.LENGTH_LONG).show();
                }
            }
        });
        showPairedDevices_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });
    }


    void pairedDevicesList() {
        pairedDevices_set = mBluetooth.getBondedDevices();
        if (pairedDevices_set.size() > 0) {
            for (BluetoothDevice device : pairedDevices_set) {
                pairedDevices_array.add(device.getName() + "\n" + device.getAddress());
            }

        } else {
            Toast.makeText(getApplicationContext(), "No paired devices!", Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<BluetoothDevice> adapter = new ArrayAdapter<BluetoothDevice>(context, android.R.layout.simple_list_item_1, pairedDevices_array);
        pairedDevices_lv.setAdapter(adapter);
        pairedDevices_lv.setOnItemClickListener(mListner);

    }

    //moving to the next page when a device is clicked.
    private AdapterView.OnItemClickListener mListner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent i = new Intent(BluetoothList.this, ControlCar.class);
            i.putExtra(EXTRA_ADDRESS, address);
            startActivity(i);
        }
    };

    //checking the result of Bluetooth ENABLE_REQUEST.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Can't open bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
    }


}





