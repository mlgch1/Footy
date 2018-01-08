
package com.kitstop.score;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.kitstop.score.GlobalClass.*;

public class WiFiActivity extends Activity {

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        myDb = new DBAdapter(this);
        myDb.open();

        EditText text = (EditText) findViewById(R.id.inputSSID);
        text.setText("" + getSSID());
        text = (EditText) findViewById(R.id.inputChannel);
        text.setText("" + getChannel());

        myDb.K_Log("WiFi Setup");
    }
    // ******************************************************************************

    @Override
    protected void onDestroy() {

        super.onDestroy();
        myDb.close();
    }
    // ******************************************************************************

     public void onClick(View view) {

        EditText text = (EditText) findViewById(R.id.inputSSID);
        if (!text.getText().toString().equals("")) {
            Integer intValue = Integer.parseInt(text.getText().toString());
            if (intValue != 0) {
                myDb.K_Log("SSID " + intValue);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SSID, intValue);
                setSSID(intValue);
            }
        }
        text = (EditText) findViewById(R.id.inputChannel);
        if (!text.getText().toString().equals("")) {
            Integer intValue = Integer.parseInt(text.getText().toString());
            if (intValue != 0) {
                myDb.K_Log("Channel " + intValue);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_CHANNEL, intValue);
                setChannel(intValue);
            }
        }

        if (getUsb()) {
            new Handler().postDelayed(new Runnable() {
                // Using handler with postDelayed called runnable run method

                @Override
                public void run() {
                    Usb_Service.stringCntr = -1;
                    Usb_Service.readString = "";
                    setWiFiSetup(true);
                }
            }, 1000); // wait for 1000  mseconds

        }else{
            myDb.K_Log("WiFi Box not connected");
            Toast toast = Toast.makeText(getApplicationContext(), "WiFi Box not connected.", Toast.LENGTH_LONG);
            toast.show();
        }
        finish();
    }
}

