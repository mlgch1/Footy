
package com.kitstop.score;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.kitstop.score.GlobalClass.*;

public class AddHomePlayerActivity extends Activity {

	private DBAdapter	myDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addhomeplayer);

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

		EditText text1 = (EditText) findViewById(R.id.editNo);
		String strString1 = text1.getText().toString();

		EditText text2 = (EditText) findViewById(R.id.editName);
		String strString2 = text2.getText().toString();

		strString1 = strString1.trim();

		strString2 = strString2.trim();

		if ((strString1.length() != 0) && (strString2.length() != 0)) {
			myDb.insertPlayerRow(getHomeTeam(), strString1, strString2, 0, 0, 0);
		}

		myDb.K_Log("Add Home Player " + strString1 + " " + strString2);

		super.finish();
	}
}
