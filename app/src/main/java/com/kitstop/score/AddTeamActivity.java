package com.kitstop.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddTeamActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addteam);
	}

	public void onClick(View view) {
		finish();
	}

	@Override
	public void finish() {

		EditText text = (EditText) findViewById(R.id.input1);

		String string = text.getText().toString();

		Intent data = new Intent();
		data.putExtra("returnKey1", string);
		setResult(RESULT_OK, data);
		super.finish();
	}
}
