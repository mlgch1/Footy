
package com.kitstop.score;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.kitstop.score.GlobalClass.*;

public class SetGroundActivity extends Activity {

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setground);

        myDb = new DBAdapter(this);
        myDb.open();

        EditText text = (EditText) findViewById(R.id.inputGnd);
        text.setText(getGround());
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
        EditText text = (EditText) findViewById(R.id.inputGnd);
        String strString = text.getText().toString();
        myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_GROUND, strString);
        setGround(strString);
        super.finish();
    }
}
