
package com.kitstop.score;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import static com.kitstop.score.GlobalClass.setGameDate;

public class SetGameDateActivity extends Activity {

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setgamedate);

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

    public void onClick(View view) {
        finish();
    }

    // ***********************************

    @Override
    public void finish() {

        DatePicker dp = (DatePicker) findViewById(R.id.datePicker1);

        int day = dp.getDayOfMonth();
        int month = dp.getMonth() + 1;
        int year = dp.getYear();

        String strDate = padLeft0("" + day, 2) + "/" + padLeft0("" + month, 2) + "/" + ("" + year);

        if (strDate.length() != 0) {
            myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_GAME_DATE, strDate);

            setGameDate(strDate);
        }
        super.finish();
    }

    // ***********************************

    public static String padLeft0(String s, int n) {
        return String.format("%" + n + "s", s).replace(' ', '0');
    }
}
