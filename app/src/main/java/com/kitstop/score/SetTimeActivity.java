
package com.kitstop.score;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import static com.kitstop.score.GlobalClass.getClock_Up_Dn;
import static com.kitstop.score.GlobalClass.getQuarterTime;
import static com.kitstop.score.GlobalClass.setClock_Up_Dn;
import static com.kitstop.score.GlobalClass.setQuarterTime;

public class SetTimeActivity extends Activity {

    DBAdapter myDb;

    Context context = SetTimeActivity.this;

    TextView t;
    EditText e;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settime);

        myDb = new DBAdapter(this);
        myDb.open();

        int clock = getClock_Up_Dn();

        RadioButton b_up = (RadioButton) findViewById(R.id.radio_up);
        RadioButton b_dn = (RadioButton) findViewById(R.id.radio_down);

        t = (TextView) findViewById(R.id.l_of_q);
        e = (EditText) findViewById(R.id.input1);

        if (clock == 0) {
            b_up.setChecked(true);

            t.setVisibility(View.INVISIBLE);
            e.setVisibility(View.INVISIBLE);
        } else {

            b_dn.setChecked(true);

            t.setVisibility(View.VISIBLE);
            e.setVisibility(View.VISIBLE);
            e.setText("" + getQuarterTime());
            e.selectAll();
        }
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }

    // ***********************************

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_up:
                if (checked) {
                    t.setVisibility(View.INVISIBLE);
                    e.setVisibility(View.INVISIBLE);

                    e.setInputType(InputType.TYPE_CLASS_NUMBER);

                    setClock_Up_Dn(0);

                    myDb.updateSystem(DBAdapter.KEY_SYSTEM_CLOCK_UP_DN, 0);
                }

                break;

            case R.id.radio_down:
                if (checked) {
                    t.setVisibility(View.VISIBLE);
                    e.setVisibility(View.VISIBLE);

                    setClock_Up_Dn(1);

                    myDb.updateSystem(DBAdapter.KEY_SYSTEM_CLOCK_UP_DN, 1);

                    e.setText("" + getQuarterTime());
                    e.selectAll();

                    if (imm == null) {
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    }

                    imm.showSoftInput(e, 0);
                }

                break;
        }
    }

    // ***********************************

    public void onClick_Set_Time(View view) {

        finish();
    }

    @Override
    public void finish() {

        if (getClock_Up_Dn() != 0) {

            if (e.getText().toString() != "") {
                Integer intValue = Integer.parseInt(e.getText().toString());

                if (intValue != 0) {
                    myDb.updateSystem(DBAdapter.KEY_SYSTEM_QUARTER_LENGTH, intValue);

                    setQuarterTime(intValue);
                }
            }
        }

        super.finish();
    }
}
