
package com.kitstop.score;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import static com.kitstop.score.GlobalClass.*;

public class ModeActivity extends Activity {

    private DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

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

    public void onClick_Mode_1(View view) {

        setMode(1);

        // Update System
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_MODE, 1);

        myDb.K_Log("Mode 1");

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        this.finish();
    }

    // ***********************************

    public void onClick_Mode_2(View view) {

        setMode(2);

        // Update System
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_MODE, 2);

        myDb.K_Log("Mode 2");

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        this.finish();
    }

    // ***********************************

    public void onClick_Mode_3(View view) {

        setMode(3);

        // Update System
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_MODE, 3);

        myDb.K_Log("Mode 3");

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        this.finish();
    }

    // ***********************************

    public void onClick_Mode_4(View view) {

/*		GlobalClass.setMode(4);

		// Update System
		myDb.updateSystem(DBAdapter.KEY_SYSTEM_MODE, 4);

		myDb.K_Log("Mode 4");

		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
		this.finish();
*/
    }
}
