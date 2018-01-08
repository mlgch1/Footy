
package com.kitstop.score;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SimpleCursorAdapter;

public class HomeTeamActivity extends Activity implements OnMenuItemClickListener {
    DBAdapter myDb;
    MainActivity ma;

    private long lngIdTempDB;
    private static final int REQUEST_CODE = 23;

    // ***********************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_team);

        myDb = new DBAdapter(this);
        myDb.open();

        populateListViewFromDB();
        registerListClickCallback();
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }

    // ***********************************

    public void onClick_AddHomeTeam(View view) {
        Intent intent = new Intent(HomeTeamActivity.this, AddTeamActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    // ***********************************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.hasExtra("returnKey1")) {
                String strString = new String(data.getExtras().getString("returnKey1"));
                strString = strString.trim();

                if (strString.length() != 0) {
                    myDb.insertTeamRow(strString);
                }
                populateListViewFromDB();
            }
        }
    }

    // ***********************************

    private void populateListViewFromDB() {

        Cursor cursor = myDb.getAllTeamRows();

        // Allow activity to manage lifetime of the cursor.
        // DEPRECATED! Runs on the UI thread, OK for small/short queries.
        startManagingCursor(cursor);

        // Setup mapping from cursor to view fields:
        String[] fromFieldNames = new String[]{DBAdapter.KEY_TEAM_NAME};

        int[] toViewIDs = new int[]{R.id.item_name};

        // Create adapter to many columns of the DB onto elements in the UI.
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this, // Context
                R.layout.team_item_layout, // Row layout template
                cursor, // cursor (set of DB records to map)
                fromFieldNames, // DB Column names
                toViewIDs // View IDs to put information in
        );

        // Set the adapter for the list view
        ListView myList = (ListView) findViewById(R.id.home_team_list);
        myList.setAdapter(myCursorAdapter);
    }

    // ***********************************

    private void registerListClickCallback() {
        ListView myList = (ListView) findViewById(R.id.home_team_list);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position,
                                    long idInDB) {
                lngIdTempDB = idInDB;
                CreatePopupMenu(findViewById(R.id.home_team_list));
            }
        });
    }

    // ***********************************

    public void CreatePopupMenu(View v) {
        PopupMenu mypopupmenu = new PopupMenu(this, v);
        mypopupmenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = mypopupmenu.getMenuInflater();
        inflater.inflate(R.menu.popup, mypopupmenu.getMenu());

        mypopupmenu.show();
    }

    // ***********************************

    private void displayAlertDialog() {
        Context context = HomeTeamActivity.this;
        String message = "Deleting a team also deletes all the team's players.";
        String button1String = "Delete";
        String button2String = "Cancel";

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);

        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                Cursor cursor = myDb.getTeamRow(lngIdTempDB);
                if (cursor.moveToFirst()) {
                    myDb.deletePlayersOfTeam(lngIdTempDB);
                    myDb.deleteTeamRow(lngIdTempDB);

                    if (GlobalClass.getHomeTeam() == lngIdTempDB) {
                        GlobalClass.setHomeTeam(0);

                        // Update System
                        myDb.updateSystem(DBAdapter.KEY_SYSTEM_HOME_TEAM, 0);
                    }

                    if (GlobalClass.getAwayTeam() == lngIdTempDB) {
                        GlobalClass.setAwayTeam(0);

                        // Update System
                        myDb.updateSystem(DBAdapter.KEY_SYSTEM_AWAY_TEAM, 0);
                    }
                }
                cursor.close();

                populateListViewFromDB();
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // do nothing
            }
        });
        ad.show();
    }

    // ***********************************

    @Override
    public boolean onMenuItemClick(MenuItem arg0) {
        switch (arg0.getItemId()) {
            case R.id.option1: {
                GlobalClass.setHomeTeam(lngIdTempDB);

                // Update System
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_HOME_TEAM, (int) lngIdTempDB);

                myDb.close();
                finish();

                return true;
            }
            case R.id.option2:
                displayAlertDialog();

                return true;

            default:
                return super.onContextItemSelected(arg0);
        }
    }
}
