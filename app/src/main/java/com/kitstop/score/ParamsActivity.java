package com.kitstop.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ParamsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params);

    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // ***********************************

    public void onClick_Mode(View view) {
        Intent s_intent = new Intent(this, ModeActivity.class);
        startActivity(s_intent);
        finish();
    }

    // ***********************************

    public void onClick_Logging(View view) {
        Intent s_intent = new Intent(this, LogActivity.class);
        startActivity(s_intent);
        finish();
    }

    // ***********************************

    public void onClick_Setup(View view) {
        Intent s_intent = new Intent(this, SetupBatteryActivity.class);
        startActivity(s_intent);
        finish();
    }

    // ***********************************

    public void onClick_WiFi(View view) {
        Intent s_intent = new Intent(this, WiFiActivity.class);
        startActivity(s_intent);
        finish();
    }

}
