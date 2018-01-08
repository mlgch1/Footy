
package com.kitstop.score;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import static com.kitstop.score.GlobalClass.getHomeTeam;

public class DeleteHomePlayerActivity extends Activity {

    private DBAdapter myDb;
    private ListView list1;

    private long lngIdTempDB;

    // private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletehomeplayer);

        myDb = new DBAdapter(this);
        myDb.open();

        registerListClickCallback();
        populateListViewFromDB_Home();
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();

        myDb.close();
    }

    // ***********************************

    public void onClick_Done(View view) {
        finish();
    }

    // ***********************************

    public void populateListViewFromDB_Home() {
        Cursor cursor;

        list1 = (ListView) findViewById(R.id.home_player_list_1_d);

        if (getHomeTeam() != 0) {
            cursor = myDb.getAllPlayerRowsForTeam(getHomeTeam(), "0", "100");
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

        cursor = myDb.getAllPlayerRowsForTeam(getHomeTeam(), "0", "100");
        startManagingCursor(cursor);

        // Setup mapping from cursor to view fields:
        String[] fromFieldNames = new String[]{DBAdapter.KEY_PLAYER_NO, DBAdapter.KEY_PLAYER_NAME};

        int[] toViewIDs = new int[]{R.id.player_no, R.id.player_name};

        SimpleCursorAdapter myCursorAdapter;

        // Create adapter to many columns of the DB onto elements in the UI.
        myCursorAdapter = new SimpleCursorAdapter(this,    // Context
                R.layout.player_item_layout1,                            // Row layout template
                cursor,                                            // cursor (set of DB records to map)
                fromFieldNames,                                    // DB Column names
                toViewIDs                                        // View IDs to put information in
        );

        // Set the adapter for the list view
        list1.setAdapter(myCursorAdapter);

    }

    // ***********************************

    private void registerListClickCallback() {
        ListView list1 = (ListView) findViewById(R.id.home_player_list_1_d);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) {
                lngIdTempDB = idInDB;

                deleteAlertDialog(lngIdTempDB);
            }
        });
    }

    // ***********************************

    private void deleteAlertDialog(long Player_Id) {
        Context context = this;
        String name = "";

        Cursor cursor = myDb.getPlayerRow(lngIdTempDB);
        if (cursor.moveToFirst()) {

            name = cursor.getString(cursor.getColumnIndex("DBAdapter.player_name"));

        }

        String message = "You REALLY want to delete '" + name + "'?";
        String button1String = "Delete";
        String button2String = "Cancel";

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);

        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                myDb.deletePlayerRow(lngIdTempDB);

                myDb.K_Log("Delete Yes");

                populateListViewFromDB_Home();
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // do nothing

                myDb.K_Log("Delete No");
            }
        });
        ad.show();
    }

}
