
package com.kitstop.score;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class NotPlayingHomePlayerActivity extends Activity {

	private DBAdapter	myDb;
	private ListView	list1;

	private long		lngIdTempDB;

	// private Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notplayinghomeplayer);

		openDB();

		registerListClickCallback();
		populateListViewFromDB_Home();
	}

	// ***********************************

	@Override
	protected void onDestroy() {
		super.onDestroy();

		closeDB();
	}

	// ***********************************

	private void openDB() {
		myDb = new DBAdapter(this);
		myDb.open();
	}

	// ***********************************

	private void closeDB() {
		myDb.close();
	}

	// ***********************************

	public void onClick_Done(View view) {
		finish();
	}

	// ***********************************

	public void populateListViewFromDB_Home() {
		Cursor cursor;

		list1 = (ListView) findViewById(R.id.home_player_list_1_n);

		if (GlobalClass.getHomeTeam() != 0) {
			cursor = myDb.getAllPlayerRowsForTeam(GlobalClass.getHomeTeam(), "0", "100");
		} else {

			// Set the adapter for the list view
			list1.setAdapter(null);

			return;
		}

		if (cursor.getCount() == 0) {
			list1.setVisibility(View.INVISIBLE);

			cursor.close();

			return;
		}

		list1.setVisibility(View.VISIBLE);

		cursor = myDb.getAllPlayerRowsForTeam(GlobalClass.getHomeTeam(), "0", "100");
		startManagingCursor(cursor);

		// Setup mapping from cursor to view fields:
		String[] fromFieldNames = new String[] { DBAdapter.KEY_PLAYER_NO, DBAdapter.KEY_PLAYER_NAME };

		int[] toViewIDs = new int[] { R.id.player_no, R.id.player_name };

		SimpleCursorAdapter myCursorAdapter;

		// Create adapter to many columns of the DB onto elements in the UI.
		myCursorAdapter = new SimpleCursorAdapter(this, 	// Context
		R.layout.player_item_layout1, 							// Row layout template
		cursor, 											// cursor (set of DB records to map)
		fromFieldNames, 									// DB Column names
		toViewIDs 										// View IDs to put information in
		);

		// Set the adapter for the list view
		list1.setAdapter(myCursorAdapter);

	}

	// ***********************************

	private void registerListClickCallback() {
		ListView list1 = (ListView) findViewById(R.id.home_player_list_1_n);
		list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) {
				lngIdTempDB = idInDB;


				Cursor cursor = myDb.getPlayerRow(lngIdTempDB);
				if (cursor.moveToFirst()) {
					myDb.updatePlayerRow(lngIdTempDB, DBAdapter.KEY_PLAYER_STATUS, 1);
				}

				cursor.close();

				myDb.K_Log("Not Playing");

				populateListViewFromDB_Home();
			}
		});
	}

}
