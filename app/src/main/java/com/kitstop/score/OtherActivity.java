package com.kitstop.score;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OtherActivity extends Activity {

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        myDb = new DBAdapter(this);
        myDb.open();
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }
    // ***********************************

    public void onClick_Print(View view) {


        int x = 5/0;


//        myDb.K_Log("Other - Print");
//        Intent s_intent = new Intent(OtherActivity.this, PrintActivity.class);
//        startActivity(s_intent);
        finish();
    }

    // ***********************************

    public void onClick_setGround(View view) {
        myDb.K_Log("Other - Ground");
        Intent s_intent = new Intent(OtherActivity.this, SetGroundActivity.class);
        startActivity(s_intent);
        finish();
    }

    // ***********************************

    public void onClick_Test(View view) {
        myDb.K_Log("Other - Test");
        Intent s_intent = new Intent(OtherActivity.this, TestActivity.class);
        startActivity(s_intent);
        finish();
    }

    // ***********************************

    public void onClick_D_T(View view) {
        myDb.K_Log("Other - Date Time");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings",
                "com.android.settings.DateTimeSettingsSetupWizard"));
        startActivity(intent);
        finish();
    }

    // ***********************************

    public void onClick_disclaimer(View view) {
        myDb.K_Log("Other - Disclaimer");
        Intent s_intent = new Intent(OtherActivity.this, DisclaimerActivity.class);
        startActivity(s_intent);
        finish();
    }

    // ***********************************

    public void onClick_about(View view) {
        myDb.K_Log("Other - About SCaT");
        Intent s_intent = new Intent(OtherActivity.this, AboutActivity.class);
        startActivity(s_intent);
        finish();
    }

    // ***********************************

    public void onClick_about_Scorer(View view) {
        myDb.K_Log("Other - About Scorer");
        Intent s_intent = new Intent(OtherActivity.this, AboutScorerActivity.class);
        startActivity(s_intent);
        finish();
    }

    // ***********************************

    public void onClick_Params(View view) {
        myDb.K_Log("Other - Parameters");
        Intent s_intent = new Intent(OtherActivity.this, PasswordActivity.class);
        startActivity(s_intent);
        finish();
    }
}
