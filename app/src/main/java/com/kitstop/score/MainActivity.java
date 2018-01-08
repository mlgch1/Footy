
package com.kitstop.score;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.acra.ACRA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static com.kitstop.score.GlobalClass.getAwayGoals;
import static com.kitstop.score.GlobalClass.getAwayPoints;
import static com.kitstop.score.GlobalClass.getAwayTeam;
import static com.kitstop.score.GlobalClass.getClock;
import static com.kitstop.score.GlobalClass.getClock_Running;
import static com.kitstop.score.GlobalClass.getClock_Up_Dn;
import static com.kitstop.score.GlobalClass.getClub;
import static com.kitstop.score.GlobalClass.getGameDate;
import static com.kitstop.score.GlobalClass.getHomeGoals;
import static com.kitstop.score.GlobalClass.getHomePoints;
import static com.kitstop.score.GlobalClass.getHomeTeam;
import static com.kitstop.score.GlobalClass.getInc;
import static com.kitstop.score.GlobalClass.getMode;
import static com.kitstop.score.GlobalClass.getQuarter;
import static com.kitstop.score.GlobalClass.getQuarterTime;
import static com.kitstop.score.GlobalClass.getThresh;
import static com.kitstop.score.GlobalClass.setAwayGoals;
import static com.kitstop.score.GlobalClass.setAwayPoints;
import static com.kitstop.score.GlobalClass.setAwayTeam;
import static com.kitstop.score.GlobalClass.setChannel;
import static com.kitstop.score.GlobalClass.setClock;
import static com.kitstop.score.GlobalClass.setClock_Running;
import static com.kitstop.score.GlobalClass.setClock_Up_Dn;
import static com.kitstop.score.GlobalClass.setClub;
import static com.kitstop.score.GlobalClass.setGameDate;
import static com.kitstop.score.GlobalClass.setGround;
import static com.kitstop.score.GlobalClass.setHomeGoals;
import static com.kitstop.score.GlobalClass.setHomePoints;
import static com.kitstop.score.GlobalClass.setHomeTeam;
import static com.kitstop.score.GlobalClass.setInc;
import static com.kitstop.score.GlobalClass.setMode;
import static com.kitstop.score.GlobalClass.setQuarter;
import static com.kitstop.score.GlobalClass.setQuarterTime;
import static com.kitstop.score.GlobalClass.setSSID;
import static com.kitstop.score.GlobalClass.setThresh;
import static com.kitstop.score.GlobalClass.setTimeToGo;
import static com.kitstop.score.GlobalClass.setUsb;
import static com.kitstop.score.GlobalClass.setError;
import static com.kitstop.score.GlobalClass.getError;

public class MainActivity extends Activity implements OnMenuItemClickListener {

    // Mode 1 Everything
    // Mode 2 Scoring and Timing and driving the display.
    // Mode 3 Team Manager - Players - Score totals visually from scoreboard or the game.
    // Mode 4 Team Manager - Players - Score totals wifi-ly from Mode 2 tablet.

    private int mode;

    private DBAdapter myDb;

    private TextView t;
    private View v;

    // Members Lists

    private ListView list1;

    private ListView list2;

    private ListView list3;

    private ListView list4;

    private static final int ROWS_IN_LIST = 13;

    private long lngIdTempDB;

    private int intTotal_ht = 0;
    private int intTotal_at = 0;
    private int intGoalsDiff = 0;
    private int intPointsDiff = 0;

    private int intSum;
    private int intSum1;
    private int intSum2;

    // Timers

    private quarterTimer q_counter;
    private boolean q_timerActive = false;
    private boolean q_timerStarted = false;
    private long q_millis;
    private boolean q_timerLockout = false;

    private storeTimer s_counter;
    private boolean s_timerActive = false;
    private resumeTimer res_counter;
    private boolean res_timerActive = false;
    private batteryTimer battery_counter;
    private int batteryTestLevel;
    private int batteryMessageNo = 5;

    AlertDialog ad = null;

    // *************************************************************************************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(intent);
        Config.context = this;
        myDb = new DBAdapter(this);
        myDb.open();

        if (!getClubName()) {
            System.exit(0);
        }
        setGlobals();
        mode = getMode();
        batteryTestLevel = getThresh();
        switch (mode) {
            case 1:
                setContentView(R.layout.activity_main_1);
                break;
            case 2:
                setContentView(R.layout.activity_main_2);
                break;
            case 3:
                setContentView(R.layout.activity_main_3);
                break;
            case 4:
                setContentView(R.layout.activity_main_4);
                break;
            default:
                setContentView(R.layout.activity_main_1);
                break;
        }
        myDb.K_Log("Start App");
        if (mode != 2) {
            registerListClickCallback();
        }

//        Toast toast = Toast.makeText(getApplicationContext(), "onCreate " + (getClock_Running() == 1), Toast.LENGTH_LONG);
//        toast.show();




//        if (getClock_Running() == 0) {
            t = (TextView) findViewById(R.id.q_timer);
            if (getClock_Up_Dn() == 0) {
                t.setText("" + "00:00");
                setTimeToGo("00:00");
            } else {
                t.setText("" + getQuarterTime() + ":00");
                setTimeToGo("" + padLeft("" + getQuarterTime(), 2) + ":00");
            }

//        } else {
//
//
//
//            q_millis = 600 * 60000 - getClock();
//
//            toast = Toast.makeText(getApplicationContext(), "millis " + q_millis, Toast.LENGTH_LONG);
//            toast.show();
//
//            String time = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(q_millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(q_millis)), TimeUnit.MILLISECONDS.toSeconds(q_millis)
//                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(q_millis)));
//            t = (TextView) findViewById(R.id.q_timer);
//            t.setText(time);
//            setTimeToGo(time);
//
//            toast = Toast.makeText(getApplicationContext(), "time " + time, Toast.LENGTH_LONG);
//            toast.show();
//
//            q_timerStarted = true;
//
//            startQuarterTimer(v);
//        }

        buttons(true, false, false, true);

        start_ResumeTimer();    // Delay onResume() for things to settle
        start_batteryTimer();


//        Toast toast = Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_LONG);
//        toast.show();


    }




/*
    @Override
    public void onStart() {
        super.onStart();

        // Store our shared preference
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", true);
        ed.apply();
    }

    @Override
    public void onStop() {
        super.onStop();

        // Store our shared preference
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);;
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.apply();
    }
*/

    // ******************************************************************************

    @Override
    public void onBackPressed() {
    }

    // ******************************************************************************

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(new Intent(this, Usb_Service.class));

        myDb.K_Log("Close App");
        myDb.close();
    }

// ******************************************************************************

    private void setGlobals() {

        setHomeTeam(myDb.readSystem(DBAdapter.KEY_SYSTEM_HOME_TEAM));
        setAwayTeam(myDb.readSystem(DBAdapter.KEY_SYSTEM_AWAY_TEAM));
        setHomeGoals(myDb.readSystem(DBAdapter.KEY_SYSTEM_HOME_GOALS));
        setHomePoints(myDb.readSystem(DBAdapter.KEY_SYSTEM_HOME_POINTS));
        setAwayGoals(myDb.readSystem(DBAdapter.KEY_SYSTEM_AWAY_GOALS));
        setAwayPoints(myDb.readSystem(DBAdapter.KEY_SYSTEM_AWAY_POINTS));
        setClock_Up_Dn(myDb.readSystem(DBAdapter.KEY_SYSTEM_CLOCK_UP_DN));
        setQuarterTime(myDb.readSystem(DBAdapter.KEY_SYSTEM_QUARTER_LENGTH));
        setQuarter(myDb.readSystem(DBAdapter.KEY_SYSTEM_QUARTER));
        if (getQuarter() == 0) {
            setQuarter(1);
            myDb.updateSystem(DBAdapter.KEY_SYSTEM_QUARTER, 1);
        }
        setGameDate(myDb.readSystemStr(DBAdapter.KEY_SYSTEM_GAME_DATE));
        setGround(myDb.readSystemStr(DBAdapter.KEY_SYSTEM_GROUND));
        setMode(myDb.readSystem(DBAdapter.KEY_SYSTEM_MODE));
        setThresh(myDb.readSystem(DBAdapter.KEY_SYSTEM_BATT_THRESH));
        setInc(myDb.readSystem(DBAdapter.KEY_SYSTEM_BATT_INC));
        setSSID(myDb.readSystem(DBAdapter.KEY_SYSTEM_SSID));
        setChannel(myDb.readSystem(DBAdapter.KEY_SYSTEM_CHANNEL));

        setError(myDb.readSystem(DBAdapter.KEY_SYSTEM_ERROR));

//        setClock_Running(myDb.readSystem(DBAdapter.KEY_SYSTEM_CLOCK_RUNNING));
//        setClock(myDb.readSystem(DBAdapter.KEY_SYSTEM_CLOCK));

        // public static final String KEY_SYSTEM_QUARTER_START_TIME = "system_quarter_start_time";

        setUsb(false);
    }

// ******************************************************************************

    private boolean getClubName() {

        String strString;
        File myFile = new File("/sdcard/f87297.azc");
        try {
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow;
            strString = "";
            while ((aDataRow = myReader.readLine()) != null) {
                strString += aDataRow + "\n";
            }
            myReader.close();
            if (!strString.contains("Pa6gK3")) {
                return false;
            }
            int intOffset = strString.charAt(1) - 48;
            strString = strString.substring(8);
            strString = strString.substring(0, strString.length() - 4);
            strString = new StringBuffer(strString).reverse().toString();
            int i = 0;
            String strTemp = "";
            while (i < strString.length()) {
                char c = strString.charAt(i);
                c = (char) (c - intOffset);
                strTemp = strTemp + c;
                i++;
            }
            strString = strTemp;
            setClub(strString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
// ******************************************************************************

    @Override
    protected void onResume() {
        super.onResume();
        String strName;

        if ((mode == 1) || (mode == 2)) {
            startService(new Intent(this, Usb_Service.class));
        }

//        myDb.K_Log("Resume App");

        // *********** Mode
        Button btn = (Button) findViewById(R.id.a_mode);
        if (btn != null) {
            btn.setText("" + getMode());
        }
        // *********** Date of Game
        String strG_D;
        strG_D = getGameDate();
        strG_D = strG_D.trim();
        t = (TextView) findViewById(R.id.gamedate);
        if (strG_D.length() != 0) {
            t.setTextColor(getResources().getColor(R.color.dk_green));
            t.setText("Game Date " + getGameDate());
        } else {
            t.setTextColor(getResources().getColor(R.color.red));
            t.setText(R.string.game_date_not_yet_set);
        }
        // *********** Club
        t = (TextView) findViewById(R.id.club);
        t.setText(getClub());

        // *********** Home Team
        t = (TextView) findViewById(R.id.HomeTeam);
        if (getHomeTeam() != 0) {
            Cursor cursor = myDb.getTeamRow(getHomeTeam());
            strName = cursor.getString(DBAdapter.COL_TEAM_NAME);
            t.setTextColor(getResources().getColor(R.color.dk_green));
            t.setText(strName);
            cursor.close();
        } else {
            t.setTextColor(getResources().getColor(R.color.red));
            t.setText(R.string.not_yet_selected);
        }
        // *********** Away Team
        t = (TextView) findViewById(R.id.AwayTeam);
        if (getAwayTeam() != 0) {
            Cursor cursor = myDb.getTeamRow(getAwayTeam());
            strName = cursor.getString(DBAdapter.COL_TEAM_NAME);
            t.setTextColor(getResources().getColor(R.color.dk_green));
            t.setText(strName);
            cursor.close();
        } else {
            t.setTextColor(getResources().getColor(R.color.red));
            t.setText(R.string.not_yet_selected);
        }
        // *********** Selected buttons in Action Bar
        btn = (Button) findViewById(R.id.a_home);
        if (btn != null) {
            if (getHomeTeam() != 0) {
                btn.setTextColor(getResources().getColor(R.color.dk_green));
            } else {
                btn.setTextColor(getResources().getColor(R.color.red));
            }
        }
        btn = (Button) findViewById(R.id.a_away);
        if (btn != null) {
            if (getAwayTeam() != 0) {
                btn.setTextColor(getResources().getColor(R.color.dk_green));
            } else {
                btn.setTextColor(getResources().getColor(R.color.red));
            }
        }
        btn = (Button) findViewById(R.id.a_date);
        if (btn != null) {
            if (getGameDate().trim().length() != 0) {
                btn.setTextColor(getResources().getColor(R.color.dk_green));
            } else {
                btn.setTextColor(getResources().getColor(R.color.red));
            }
        }

        v = findViewById(R.id.blink_bug);
        if (getError() == 1) {
            if (v != null) {
                v.setVisibility(TextView.VISIBLE);
            }
        } else {
            if (v != null) {
                v.setVisibility(TextView.INVISIBLE);
            }
        }

        // *********** Quarter Timing
        if ((mode == 1) || (mode == 2)) {
            if (!q_timerStarted) {
                t = (TextView) findViewById(R.id.q_timer);
                TextView tt = (TextView) findViewById(R.id.u_d);
                if (getClock_Up_Dn() == 0) {
                    t.setText("" + "00:00");
                    setTimeToGo("00:00");
                    tt.setText("Up");
                } else {
                    t.setText("" + getQuarterTime() + ":00");
                    setTimeToGo("" + padLeft("" + getQuarterTime(), 2) + ":00");
                    tt.setText("Dn");
                }
                t = (TextView) findViewById(R.id.quarter);
                t.setText("Q" + getQuarter());
            }
        }
        // ***********
        // *********** Goals and Points
        int intGoals = getHomeGoals();
        t = (TextView) findViewById(R.id.d_goals_ht);
        t.setText(padLeft(Integer.toString(intGoals), 3));
        // ***********
        int intPoints = getHomePoints();
        t = (TextView) findViewById(R.id.d_points_ht);
        t.setText(padLeft(Integer.toString(intPoints), 3));
        // ***********
        intGoals = getAwayGoals();
        t = (TextView) findViewById(R.id.d_goals_at);
        t.setText(padLeft(Integer.toString(intGoals), 3));
        // ***********
        intPoints = getAwayPoints();
        t = (TextView) findViewById(R.id.d_points_at);
        t.setText(padLeft(Integer.toString(intPoints), 3));
        // *********** Totals
        intTotal_ht = (getHomeGoals() * 6) + (getHomePoints());
        t = (TextView) findViewById(R.id.d_total_ht);
        t.setText(padLeft(Integer.toString(intTotal_ht), 3));
        intTotal_at = (getAwayGoals() * 6) + (getAwayPoints());
        t = (TextView) findViewById(R.id.d_total_at);
        t.setText(padLeft(Integer.toString(intTotal_at), 3));
        populateListViewFromDB_Home();
        populateListViewFromDB_Away();


//        Toast toast = Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_LONG);
//        toast.show();


    }
// ******************************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
// ******************************************************************************

    private void populateListViewFromDB_Home() {

        Cursor cursor;
        if (mode == 2) {
            return;
        }
        t = (TextView) findViewById(R.id.home_player_no);
        t.setText("");
        diffValues_Home();
        list1 = (ListView) findViewById(R.id.home_player_list_1);
        list2 = (ListView) findViewById(R.id.home_player_list_2);
        if (getHomeTeam() != 0) {
            cursor = myDb.getAllPlayerRowsForTeam(getHomeTeam(), "0", "100");
        } else {
            // Set the adapter for the list view
            list1.setAdapter(null);
            list2.setAdapter(null);
            return;
        }
        if (cursor.getCount() == 0) {
            list1.setVisibility(View.INVISIBLE);
            list2.setVisibility(View.INVISIBLE);
            cursor.close();
            return;
        }
        list1.setVisibility(View.VISIBLE);
        list2.setVisibility(View.VISIBLE);
        cursor = myDb.getAllPlayerRowsForTeam(getHomeTeam(), "0", "" + ROWS_IN_LIST);
        startManagingCursor(cursor);
        // Setup mapping from cursor to view fields:
        String[] fromFieldNames = new String[]{DBAdapter.KEY_PLAYER_NO, DBAdapter.KEY_PLAYER_NAME, DBAdapter.KEY_PLAYER_GOALS, DBAdapter.KEY_PLAYER_POINTS};
        int[] toViewIDs = new int[]{R.id.player_no, R.id.player_name, R.id.player_goals, R.id.player_points};
        SimpleCursorAdapter myCursorAdapter;
        if (mode == 1) {
            // Create adapter to many columns of the DB onto elements in the UI.
            myCursorAdapter = new SimpleCursorAdapter(this,    // Context
                    R.layout.player_item_layout1,                            // Row layout template
                    cursor,                                            // cursor (set of DB records to map)
                    fromFieldNames,                                    // DB Column names
                    toViewIDs                                        // View IDs to put information in
            );
        } else {
            // Create adapter to many columns of the DB onto elements in the UI.
            myCursorAdapter = new SimpleCursorAdapter(this,    // Context
                    R.layout.player_item_layout34,                            // Row layout template
                    cursor,                                            // cursor (set of DB records to map)
                    fromFieldNames,                                    // DB Column names
                    toViewIDs                                        // View IDs to put information in
            );
        }
        // Set the adapter for the list view
        list1.setAdapter(myCursorAdapter);
        cursor = myDb.getAllPlayerRowsForTeam(getHomeTeam(), "0", "100");
        int intPlayer_No = cursor.getCount();
        if (intPlayer_No > ROWS_IN_LIST) {
            cursor = myDb.getAllPlayerRowsForTeam(getHomeTeam(), "" + ROWS_IN_LIST, "" + (ROWS_IN_LIST * 2));
            startManagingCursor(cursor);
            // Setup mapping from cursor to view fields:
            String[] fromFieldNames1 = new String[]{DBAdapter.KEY_PLAYER_NO, DBAdapter.KEY_PLAYER_NAME, DBAdapter.KEY_PLAYER_GOALS, DBAdapter.KEY_PLAYER_POINTS};
            int[] toViewIDs1 = new int[]{R.id.player_no, R.id.player_name, R.id.player_goals, R.id.player_points};
            SimpleCursorAdapter myCursorAdapter1;
            if (mode == 1) {
                // Create adapter to many columns of the DB onto elements in the UI.
                myCursorAdapter1 = new SimpleCursorAdapter(this, // Context
                        R.layout.player_item_layout1, // Row layout template
                        cursor, // cursor (set of DB records to map)
                        fromFieldNames1, // DB Column names
                        toViewIDs1 // View IDs to put information in
                );
            } else {
                // Create adapter to many columns of the DB onto elements in the UI.
                myCursorAdapter1 = new SimpleCursorAdapter(this, // Context
                        R.layout.player_item_layout34, // Row layout template
                        cursor, // cursor (set of DB records to map)
                        fromFieldNames1, // DB Column names
                        toViewIDs1 // View IDs to put information in
                );
            }
            // Set the adapter for the list view
            list2.setAdapter(myCursorAdapter1);
        } else {
            // Set the adapter for the list view
            list2.setAdapter(null);
        }
        t = (TextView) findViewById(R.id.home_player_no);
        t.setText(Integer.toString(intPlayer_No));
    }
// ******************************************************************************

    private void populateListViewFromDB_Away() {

        Cursor cursor;
        if (mode == 2) {
            return;
        }
        t = (TextView) findViewById(R.id.away_player_no);
        t.setText("");
        diffValues_Away();
        list3 = (ListView) findViewById(R.id.away_player_list_1);
        list4 = (ListView) findViewById(R.id.away_player_list_2);
        if (getAwayTeam() != 0) {
            cursor = myDb.getAllPlayerRowsForTeam(getAwayTeam(), "0", "100");
        } else {
            // Set the adapter for the list view
            list3.setAdapter(null);
            list4.setAdapter(null);
            return;
        }
        if (cursor.getCount() == 0) {
            list3.setVisibility(View.INVISIBLE);
            list4.setVisibility(View.INVISIBLE);
            cursor.close();
            return;
        }
        list3.setVisibility(View.VISIBLE);
        list4.setVisibility(View.VISIBLE);
        cursor = myDb.getAllPlayerRowsForTeam(getAwayTeam(), "0", "" + ROWS_IN_LIST);
        startManagingCursor(cursor);
        // Setup mapping from cursor to view fields:
        String[] fromFieldNames = new String[]{DBAdapter.KEY_PLAYER_NO, DBAdapter.KEY_PLAYER_NAME, DBAdapter.KEY_PLAYER_GOALS, DBAdapter.KEY_PLAYER_POINTS};
        int[] toViewIDs = new int[]{R.id.player_no, R.id.player_name, R.id.player_goals, R.id.player_points};
        SimpleCursorAdapter myCursorAdapter;
        if (mode == 1) {
            // Create adapter to many columns of the DB onto elements in the UI.
            myCursorAdapter = new SimpleCursorAdapter(this, // Context
                    R.layout.player_item_layout1, // Row layout template
                    cursor, // cursor (set of DB records to map)
                    fromFieldNames, // DB Column names
                    toViewIDs // View IDs to put information in
            );
        } else {
            // Create adapter to many columns of the DB onto elements in the UI.
            myCursorAdapter = new SimpleCursorAdapter(this, // Context
                    R.layout.player_item_layout34, // Row layout template
                    cursor, // cursor (set of DB records to map)
                    fromFieldNames, // DB Column names
                    toViewIDs // View IDs to put information in
            );
        }
        // Set the adapter for the list view
        list3.setAdapter(myCursorAdapter);
        cursor = myDb.getAllPlayerRowsForTeam(getAwayTeam(), "0", "100");
        int intPlayer_No = cursor.getCount();
        if (cursor.getCount() > ROWS_IN_LIST) {
            cursor = myDb.getAllPlayerRowsForTeam(getAwayTeam(), "" + ROWS_IN_LIST, "" + (ROWS_IN_LIST * 2));
            startManagingCursor(cursor);
            // Setup mapping from cursor to view fields:
            String[] fromFieldNames1 = new String[]{DBAdapter.KEY_PLAYER_NO, DBAdapter.KEY_PLAYER_NAME, DBAdapter.KEY_PLAYER_GOALS, DBAdapter.KEY_PLAYER_POINTS};
            int[] toViewIDs1 = new int[]{R.id.player_no, R.id.player_name, R.id.player_goals, R.id.player_points};
            SimpleCursorAdapter myCursorAdapter1;
            if (mode == 1) {
                // Create adapter to many columns of the DB onto elements in the UI.
                myCursorAdapter1 = new SimpleCursorAdapter(this, // Context
                        R.layout.player_item_layout1, // Row layout template
                        cursor, // cursor (set of DB records to map)
                        fromFieldNames1, // DB Column names
                        toViewIDs1 // View IDs to put information in
                );
            } else {
                // Create adapter to many columns of the DB onto elements in the UI.
                myCursorAdapter1 = new SimpleCursorAdapter(this, // Context
                        R.layout.player_item_layout34, // Row layout template
                        cursor, // cursor (set of DB records to map)
                        fromFieldNames1, // DB Column names
                        toViewIDs1 // View IDs to put information in
                );
            }
            // Set the adapter for the list view
            list4.setAdapter(myCursorAdapter1);
        } else {
            // Set the adapter for the list view
            list4.setAdapter(null);
        }
        t = (TextView) findViewById(R.id.away_player_no);
        t.setText(Integer.toString(intPlayer_No));
    }
// ******************************************************************************

    public void diffValues_Home() {

        if ((mode == 1) || (mode == 4)) {
            // Check player scores against team score
            intSum = myDb.getPlayerGoalsForTeam(getHomeTeam());
            intGoalsDiff = getHomeGoals() - intSum;
            t = (TextView) findViewById(R.id.d_goals_diff_ht);
            t.setText(padLeft(Integer.toString(intGoalsDiff), 3));
            intSum = myDb.getPlayerPointsForTeam(getHomeTeam());
            intPointsDiff = getHomePoints() - intSum;
            t = (TextView) findViewById(R.id.d_points_diff_ht);
            t.setText(padLeft(Integer.toString(intPointsDiff), 3));
            if ((intGoalsDiff == 0) && (intPointsDiff == 0)) {
                v = findViewById(R.id.d_diff_ht);
                v.setVisibility(View.INVISIBLE);
            } else {
                v = findViewById(R.id.d_diff_ht);
                v.setVisibility(View.VISIBLE);
            }
        } else {
            // Get player scores total
            intSum = myDb.getPlayerGoalsForTeam(getHomeTeam());
            t = (TextView) findViewById(R.id.d_goals_ht);
            t.setText(padLeft(Integer.toString(intSum), 3));
            intSum1 = myDb.getPlayerPointsForTeam(getHomeTeam());
            t = (TextView) findViewById(R.id.d_points_ht);
            t.setText(padLeft(Integer.toString(intSum1), 3));
            intSum2 = (intSum * 6) + intSum1;
            t = (TextView) findViewById(R.id.d_total_ht);
            t.setText(padLeft(Integer.toString(intSum2), 3));
        }
    }
// ******************************************************************************

    public void diffValues_Away() {

        if ((mode == 1) || (mode == 4)) {
            // Check player scores against team score
            intSum = myDb.getPlayerGoalsForTeam(getAwayTeam());
            intGoalsDiff = getAwayGoals() - intSum;
            t = (TextView) findViewById(R.id.d_goals_diff_at);
            t.setText(padLeft(Integer.toString(intGoalsDiff), 3));
            intSum = myDb.getPlayerPointsForTeam(getAwayTeam());
            intPointsDiff = getAwayPoints() - intSum;
            t = (TextView) findViewById(R.id.d_points_diff_at);
            t.setText(padLeft(Integer.toString(intPointsDiff), 3));
            if ((intGoalsDiff == 0) && (intPointsDiff == 0)) {
                v = findViewById(R.id.d_diff_at);
                v.setVisibility(View.INVISIBLE);
            } else {
                v = findViewById(R.id.d_diff_at);
                v.setVisibility(View.VISIBLE);
            }
        } else {            // Mode 3
            // Get player scores total
            intSum = myDb.getPlayerGoalsForTeam(getAwayTeam());
            t = (TextView) findViewById(R.id.d_goals_at);
            t.setText(padLeft(Integer.toString(intSum), 3));
            intSum1 = myDb.getPlayerPointsForTeam(getAwayTeam());
            t = (TextView) findViewById(R.id.d_points_at);
            t.setText(padLeft(Integer.toString(intSum1), 3));
            intSum2 = (intSum * 6) + intSum1;
            t = (TextView) findViewById(R.id.d_total_at);
            t.setText(padLeft(Integer.toString(intSum2), 3));
        }
    }
// ******************************************************************************

    public void onClick_HomePlayer(View view) {

        if (!q_timerStarted) {
            if (getHomeTeam() != 0) {
                Intent intent = new Intent(MainActivity.this, HomePlayerActivity.class);
                startActivity(intent);
                myDb.K_Log("Home Players");
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Home Team must be selected", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot change players when play is under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************

    public void onClick_AwayPlayer(View view) {

        if (!q_timerStarted) {
            if (getAwayTeam() != 0) {
                Intent intent = new Intent(MainActivity.this, AwayPlayerActivity.class);
                startActivity(intent);
                myDb.K_Log("Away Players");
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Away Team must be selected", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot change players when play is under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************
// Home Team Scoring
// ******************************************************************************

    public void goalPlus_ht(View view) {

        if (set_to_go_timer_running()) {
            setHomeGoals(getHomeGoals() + 1);
            intTotal_ht = intTotal_ht + 6;
            t = (TextView) findViewById(R.id.d_goals_ht);
            t.setText(padLeft(Integer.toString(getHomeGoals()), 3));
            t = (TextView) findViewById(R.id.d_total_ht);
            t.setText(padLeft(Integer.toString(intTotal_ht), 3));
            // Update System
            myDb.updateSystem(DBAdapter.KEY_SYSTEM_HOME_GOALS, getHomeGoals());
            populateListViewFromDB_Home();
            myDb.K_Log("inc Home Goal " + intTotal_ht);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Home and Away Teams and Game Date must be set and Quarter must be under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************

    public void goalNeg_ht(View view) {

        if (set_to_go_timer_running()) {
            if (getHomeGoals() != 0) {
                setHomeGoals(getHomeGoals() - 1);
                intTotal_ht = intTotal_ht - 6;
            }
            t = (TextView) findViewById(R.id.d_goals_ht);
            t.setText(padLeft(Integer.toString(getHomeGoals()), 3));
            t = (TextView) findViewById(R.id.d_total_ht);
            t.setText(padLeft(Integer.toString(intTotal_ht), 3));
            // Update System
            myDb.updateSystem(DBAdapter.KEY_SYSTEM_HOME_GOALS, getHomeGoals());
            populateListViewFromDB_Home();
            myDb.K_Log("dec Home Goal " + intTotal_ht);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Home and Away Teams and Game Date must be set and Quarter must be under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************

    public void pointsPlus_ht(View view) {

        if (set_to_go_timer_running()) {
            setHomePoints(getHomePoints() + 1);
            intTotal_ht++;
            t = (TextView) findViewById(R.id.d_points_ht);
            t.setText(padLeft(Integer.toString(getHomePoints()), 3));
            t = (TextView) findViewById(R.id.d_total_ht);
            t.setText(padLeft(Integer.toString(intTotal_ht), 3));
            // Update System
            myDb.updateSystem(DBAdapter.KEY_SYSTEM_HOME_POINTS, getHomePoints());
            populateListViewFromDB_Home();
            myDb.K_Log("inc Home Point " + intTotal_ht);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Home and Away Teams and Game Date must be set and Quarter must be under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************

    public void pointsNeg_ht(View view) {

        if (set_to_go_timer_running()) {
            if (getHomePoints() != 0) {
                setHomePoints(getHomePoints() - 1);
                intTotal_ht--;
            }
            t = (TextView) findViewById(R.id.d_points_ht);
            t.setText(padLeft(Integer.toString(getHomePoints()), 3));
            t = (TextView) findViewById(R.id.d_total_ht);
            t.setText(padLeft(Integer.toString(intTotal_ht), 3));
            // Update System
            myDb.updateSystem(DBAdapter.KEY_SYSTEM_HOME_POINTS, getHomePoints());
            populateListViewFromDB_Home();
            myDb.K_Log("dec Home Point " + intTotal_ht);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Home and Away Teams and Game Date must be set and Quarter must be under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************
// Away Team Scoring
// ******************************************************************************

    public void goalPlus_at(View view) {

        if (set_to_go_timer_running()) {
            setAwayGoals(getAwayGoals() + 1);
            intTotal_at = intTotal_at + 6;
            t = (TextView) findViewById(R.id.d_goals_at);
            t.setText(padLeft(Integer.toString(getAwayGoals()), 3));
            t = (TextView) findViewById(R.id.d_total_at);
            t.setText(padLeft(Integer.toString(intTotal_at), 3));
            // Update System
            myDb.updateSystem(DBAdapter.KEY_SYSTEM_AWAY_GOALS, getAwayGoals());
            populateListViewFromDB_Away();
            myDb.K_Log("inc Away Goal " + intTotal_at);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Home and Away Teams and Game Date must be set and Quarter must be under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************

    public void goalNeg_at(View view) {

        if (set_to_go_timer_running()) {
            if (getAwayGoals() != 0) {
                setAwayGoals(getAwayGoals() - 1);
                intTotal_at = intTotal_at - 6;
            }
            t = (TextView) findViewById(R.id.d_goals_at);
            t.setText(padLeft(Integer.toString(getAwayGoals()), 3));
            t = (TextView) findViewById(R.id.d_total_at);
            t.setText(padLeft(Integer.toString(intTotal_at), 3));
            // Update System
            myDb.updateSystem(DBAdapter.KEY_SYSTEM_AWAY_GOALS, getAwayGoals());
            populateListViewFromDB_Away();
            myDb.K_Log("dec Away Goal " + intTotal_at);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Home and Away Teams and Game Date must be set and Quarter must be under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************

    public void pointsPlus_at(View view) {

        if (set_to_go_timer_running()) {
            setAwayPoints(getAwayPoints() + 1);
            intTotal_at++;
            t = (TextView) findViewById(R.id.d_points_at);
            t.setText(padLeft(Integer.toString(getAwayPoints()), 3));
            t = (TextView) findViewById(R.id.d_total_at);
            t.setText(padLeft(Integer.toString(intTotal_at), 3));
            // Update System
            myDb.updateSystem(DBAdapter.KEY_SYSTEM_AWAY_POINTS, getAwayPoints());
            populateListViewFromDB_Away();
            myDb.K_Log("inc Away Point " + intTotal_at);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Home and Away Teams and Game Date must be set and Quarter must be under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************

    public void pointsNeg_at(View view) {

        if (set_to_go_timer_running()) {
            if (getAwayPoints() != 0) {
                setAwayPoints(getAwayPoints() - 1);
                intTotal_at--;
            }
            t = (TextView) findViewById(R.id.d_points_at);
            t.setText(padLeft(Integer.toString(getAwayPoints()), 3));
            t = (TextView) findViewById(R.id.d_total_at);
            t.setText(padLeft(Integer.toString(intTotal_at), 3));
            // Update System
            myDb.updateSystem(DBAdapter.KEY_SYSTEM_AWAY_POINTS, getAwayPoints());
            populateListViewFromDB_Away();
            myDb.K_Log("dec Away Point " + intTotal_at);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Home and Away Teams and Game Date must be set and Quarter must be under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************
// Buttons in Action Bar
// ******************************************************************************

    public void onClick_Reset(View view) {

        myDb.K_Log("Reset");
        resetAlertDialog();
    }
// ******************************************************************************

    public void onClick_selectHomeTeam(View view) {    // From Action Bar
        if (!q_timerStarted) {
            Intent s_intent = new Intent(MainActivity.this, HomeTeamActivity.class);
            startActivity(s_intent);
            myDb.K_Log("Select Home Team");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot change teams when play is under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************

    public void onClick_selectAwayTeam(View view) {    // From Action Bar
        if (!q_timerStarted) {
            Intent s_intent = new Intent(MainActivity.this, AwayTeamActivity.class);
            startActivity(s_intent);
            myDb.K_Log("Select Away Team");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot change teams when play is under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************

    public void onClick_selectGameDate(View view) {    // From Action Bar
        if (!q_timerStarted) {
            Intent s_intent = new Intent(MainActivity.this, SetGameDateActivity.class);
            startActivity(s_intent);
            myDb.K_Log("Select Game Date");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot change date when play is under way", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************

    public void onClick_Set_Time(View view) {
        Intent intent = new Intent(MainActivity.this, SetTimeActivity.class);
        startActivity(intent);
        myDb.K_Log("Set Time Menu");
    }
// ******************************************************************************

    public void onClick_Other(View view) {
        Intent intent = new Intent(MainActivity.this, OtherActivity.class);
        startActivity(intent);
        myDb.K_Log("Open Other Menu");
    }
// ******************************************************************************

    public void onClick_Exit(View view) {

        myDb.K_Log("Quit");
        quitAlertDialog();
    }
// ******************************************************************************

    private void resetAlertDialog() {

        Context context = MainActivity.this;
        String message = "You REALLY want to Reset everything?";
        String button1String = "Reset";
        String button2String = "Cancel";
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // GlobalClass.setQuarterTimer(false);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_HOME_TEAM, 0);
                setHomeTeam(0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_AWAY_TEAM, 0);
                setAwayTeam(0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_HOME_GOALS, 0);
                setHomeGoals(0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_HOME_POINTS, 0);
                setHomePoints(0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_AWAY_GOALS, 0);
                setAwayGoals(0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_AWAY_POINTS, 0);
                setAwayPoints(0);
                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_GAME_DATE, "");
                setGameDate("");
                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_QUARTER, "1");
                setQuarter(0);
                inc_Quarter();
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_G_1, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_G_2, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_G_3, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_G_4, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_B_1, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_B_2, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_B_3, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_B_4, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_G_1, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_G_2, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_G_3, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_G_4, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_B_1, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_B_2, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_B_3, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_B_4, 0);

                myDb.updateSystem(DBAdapter.KEY_SYSTEM_ERROR, 0);
                setError(0);

                q_timerActive = false;
//                myDb.updateSystem(DBAdapter.KEY_SYSTEM_CLOCK_RUNNING, 0);
                q_timerLockout = false;
                q_timerStarted = false;
                if (q_counter != null) {
                    q_counter.cancel();
                    q_counter = null;
                }
                s_timerActive = false;
                if (s_counter != null) {
                    s_counter.cancel();
                    s_counter = null;
                }
                if ((mode == 1) || (mode == 2)) {
                    t = (TextView) findViewById(R.id.q_timer);
                    if (getClock_Up_Dn() == 0) {
                        t.setText("" + "00:00");
                        setTimeToGo("00:00");
                    } else {
                        t.setText("" + getQuarterTime() + ":00");
                        setTimeToGo("" + padLeft("" + getQuarterTime(), 2) + ":00");
                    }
                    t = (TextView) findViewById(R.id.quarter);
                    t.setText("Q1");

                    buttons(true, false, false, true);
                }
                intTotal_ht = 0;
                intTotal_at = 0;
                intGoalsDiff = 0;
                intPointsDiff = 0;
                if (mode != 2) {
                    // Set the adapter for the list view
                    list1 = (ListView) findViewById(R.id.home_player_list_1);
                    list1.setAdapter(null);
                    list2 = (ListView) findViewById(R.id.home_player_list_2);
                    list2.setAdapter(null);
                    list3 = (ListView) findViewById(R.id.away_player_list_1);
                    list3.setAdapter(null);
                    list4 = (ListView) findViewById(R.id.away_player_list_2);
                    list4.setAdapter(null);
                    myDb.zeroPlayerRows();
                    t = (TextView) findViewById(R.id.home_player_no);
                    t.setText(null);
                    t = (TextView) findViewById(R.id.away_player_no);
                    t.setText(null);
                }
                myDb.K_Log("Reset Yes");
                onResume();
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // do nothing
                myDb.K_Log("Reset No");
            }
        });
        ad.show();
    }
// ******************************************************************************

    private void quitAlertDialog() {

        Context context = MainActivity.this;
        String message = "You REALLY want to Quit?";
        String button1String = "Quit";
        String button2String = "Cancel";
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                myDb.K_Log("Quit App");

                L.d("Finish");

                finish();
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // do nothing
                myDb.K_Log("Quit Not");
            }
        });
        ad.show();
    }
// ******************************************************************************

    private void registerListClickCallback() {

        ListView list1 = (ListView) findViewById(R.id.home_player_list_1);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) {

                lngIdTempDB = idInDB;
                CreatePopupMenu(findViewById(R.id.home_player_list_1));
            }
        });
        ListView list2 = (ListView) findViewById(R.id.home_player_list_2);
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) {

                lngIdTempDB = idInDB;
                CreatePopupMenu(findViewById(R.id.home_player_list_2));
            }
        });
        ListView list3 = (ListView) findViewById(R.id.away_player_list_1);
        list3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) {

                lngIdTempDB = idInDB;
                CreatePopupMenu(findViewById(R.id.away_player_list_1));
            }
        });
        ListView list4 = (ListView) findViewById(R.id.away_player_list_2);
        list4.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) {

                lngIdTempDB = idInDB;
                CreatePopupMenu(findViewById(R.id.away_player_list_2));
            }
        });
    }
// ******************************************************************************

    public void CreatePopupMenu(View v) {

        PopupMenu mypopupmenu = new PopupMenu(this, v, 0x00800005);
        mypopupmenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = mypopupmenu.getMenuInflater();
        inflater.inflate(R.menu.popup1, mypopupmenu.getMenu());
        mypopupmenu.show();
    }
// ******************************************************************************

    @Override
    public boolean onMenuItemClick(MenuItem arg0) {

        switch (arg0.getItemId()) {
            case R.id.option2: {    // Add a goal
                Cursor cursor = myDb.getPlayerRow(lngIdTempDB);
                if (cursor.moveToFirst()) {
                    int data = cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_PLAYER_GOALS));
                    data++;
                    myDb.updatePlayerRow(lngIdTempDB, DBAdapter.KEY_PLAYER_GOALS, data);
                }
                cursor.close();
                populateListViewFromDB_Home();
                populateListViewFromDB_Away();
                return true;
            }
            case R.id.option3: {    // Subtract a goal
                Cursor cursor = myDb.getPlayerRow(lngIdTempDB);
                if (cursor.moveToFirst()) {
                    int data = cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_PLAYER_GOALS));
                    if (data != 0) {
                        data--;
                    }
                    myDb.updatePlayerRow(lngIdTempDB, DBAdapter.KEY_PLAYER_GOALS, data);
                }
                cursor.close();
                populateListViewFromDB_Home();
                populateListViewFromDB_Away();
                return true;
            }
            case R.id.option4: {    // Add a point
                Cursor cursor = myDb.getPlayerRow(lngIdTempDB);
                if (cursor.moveToFirst()) {
                    int data = cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_PLAYER_POINTS));
                    data++;
                    myDb.updatePlayerRow(lngIdTempDB, DBAdapter.KEY_PLAYER_POINTS, data);
                }
                cursor.close();
                populateListViewFromDB_Home();
                populateListViewFromDB_Away();
                return true;
            }
            case R.id.option5: {    // Subtract a point
                Cursor cursor = myDb.getPlayerRow(lngIdTempDB);
                if (cursor.moveToFirst()) {
                    int data = cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_PLAYER_POINTS));
                    if (data != 0) {
                        data--;
                    }
                    myDb.updatePlayerRow(lngIdTempDB, DBAdapter.KEY_PLAYER_POINTS, data);
                }
                cursor.close();
                populateListViewFromDB_Home();
                populateListViewFromDB_Away();
                return true;
            }
            default:
                return super.onContextItemSelected(arg0);
        }
    }
// ******************************************************************************
// Buttons in Timing Bar
// ******************************************************************************

    public void buttons(boolean start, boolean stop, boolean finish, boolean set) {

        t = (TextView) findViewById(R.id.b_q_start);
        if (start) {
            t.setVisibility(View.VISIBLE);
            t.setText(R.string.qstart);
        } else {
            t.setVisibility(View.INVISIBLE);
        }
        t = (TextView) findViewById(R.id.b_stop);
        if (stop) {
            t.setVisibility(View.VISIBLE);
            t.setText(R.string.stop_clock);
        } else {
            t.setVisibility(View.INVISIBLE);
        }
        t = (TextView) findViewById(R.id.b_finish);
        if (finish) {
            t.setVisibility(View.VISIBLE);
        } else {
            t.setVisibility(View.INVISIBLE);
        }
        t = (TextView) findViewById(R.id.b_settime);
        if (set) {
            t.setVisibility(View.VISIBLE);
        } else {
            t.setVisibility(View.INVISIBLE);
        }
    }
    // ******************************************************************************

    public void startQuarterTimer(View view) {

        if (set_to_go()) {
            if (!q_timerLockout) {


//                if (getClock_Running() == 0) {


                    if (!q_timerStarted) {
                        q_timerStarted = true;
                        q_timerActive = true;
                        myDb.updateSystem(DBAdapter.KEY_SYSTEM_CLOCK_RUNNING, 1);

                        if (getClock_Up_Dn() == 0) {
                            q_counter = new quarterTimer((600 * 60000), 500);
                        } else {
                            q_counter = new quarterTimer((getQuarterTime() * 60000), 500);
                        }
                        t = (TextView) findViewById(R.id.quarter);
                        t.setText("Q" + getQuarter());
                        q_counter.start();
                        buttons(false, true, false, false);
                        myDb.K_Log("Start Quarter");
                    } else {
                        q_timerLockout = false;
                        q_timerStarted = false;
                        buttons(true, false, false, true);
                        t = (TextView) findViewById(R.id.q_timer);
                        myDb.K_Log("Reset Quarter");
                        if (getClock_Up_Dn() == 0) {
                            t.setText("" + "00:00");
                            setTimeToGo("00:00");
                        } else {
                            t.setText("" + getQuarterTime() + ":00");
                            setTimeToGo("" + padLeft("" + getQuarterTime(), 2) + ":00");
                        }
                    }
//                } else {
//
//                    q_counter = new quarterTimer(600 * 60000 - (getClock()), 500);
//                    q_counter.start();
//                    buttons(false, true, false, false);
//
//
//                }
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Home and Away Teams and Game Date must be set", Toast.LENGTH_LONG);
            toast.show();
        }
    }
// ******************************************************************************
// Quarter Timer
// ******************************************************************************

    class quarterTimer extends CountDownTimer {

        public quarterTimer(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {        // Only happens on Count Down
            t = (TextView) findViewById(R.id.q_timer);
            t.setText("" + getQuarterTime() + ":00");
            setTimeToGo("" + padLeft("" + getQuarterTime(), 2) + ":00");
            q_counter = null;
            q_timerStarted = false;
            startStoreTimer();
            q_timerLockout = true;
            q_millis = 0;
            String ms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(q_millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(q_millis)), TimeUnit.MILLISECONDS.toSeconds(q_millis)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(q_millis)));
            t = (TextView) findViewById(R.id.q_timer);
            t.setText(ms);
            t = (TextView) findViewById(R.id.b_stop);
            t.setVisibility(View.INVISIBLE);
            setTimeToGo(ms);
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_ACK);
            myDb.K_Log("30 Seconds to add Post Siren score!");
            Toast toast = Toast.makeText(getApplicationContext(), "30 Seconds to add Post Siren score!", Toast.LENGTH_LONG);
            toast.show();
        }

        @Override
        public void onTick(long millisUntilFinished) {

            if (getClock_Up_Dn() == 0) {
                q_millis = 600 * 60000 - millisUntilFinished;
            } else {
                q_millis = millisUntilFinished;
            }
            String time = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(q_millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(q_millis)), TimeUnit.MILLISECONDS.toSeconds(q_millis)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(q_millis)));
            t = (TextView) findViewById(R.id.q_timer);
            t.setText(time);
            setTimeToGo(time);


//            myDb.updateSystemLng(DBAdapter.KEY_SYSTEM_CLOCK, q_millis);

//            setClock(q_millis);
        }
    }
    // ******************************************************************************

    public void onClick_Stop(View view) {

        if (set_to_go()) {
            if (q_timerActive) {
                q_timerActive = false;
//                myDb.updateSystem(DBAdapter.KEY_SYSTEM_CLOCK_RUNNING, 0);

                q_counter.cancel();
                q_counter = null;
                buttons(true, true, false, false);
                t = (TextView) findViewById(R.id.b_q_start);
                t.setText(R.string.reset_clock);
                t = (TextView) findViewById(R.id.b_stop);
                t.setText(R.string.resume_clock);
                if (getClock_Up_Dn() == 0) {
                    t = (TextView) findViewById(R.id.b_finish);
                    t.setVisibility(View.VISIBLE);
                }
                myDb.K_Log("Stop Clock");
            } else {                        // Resume
                q_timerActive = true;
//                myDb.updateSystem(DBAdapter.KEY_SYSTEM_CLOCK_RUNNING, 1);

                if (getClock_Up_Dn() == 0) {
                    q_counter = new quarterTimer((600 * 60000) - q_millis, 500);
                } else {
                    q_counter = new quarterTimer(q_millis, 500);
                }
                q_counter.start();
                buttons(false, true, false, false);
                myDb.K_Log("Resume Clock");
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Home and Away Teams and Game Date must be set", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    // ******************************************************************************

    public void onClick_Finish(View view) {            // Only visible on Count Up
        q_timerLockout = false;
        q_timerStarted = false;
        t = (TextView) findViewById(R.id.q_timer);
        t.setText("" + getQuarterTime() + ":00");
        if (getClock_Up_Dn() == 0) {
            q_millis = 0;
        } else {
            q_millis = getQuarterTime() * 60000;
        }
        String ms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(q_millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(q_millis)), TimeUnit.MILLISECONDS.toSeconds(q_millis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(q_millis)));
        setTimeToGo(ms);
        buttons(true, false, false, true);
        updateQuarter();
    }
    // ******************************************************************************

	/*	public void onClick_Hoot(View view) {

		final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
		tg.startTone(ToneGenerator.TONE_PROP_ACK);

		myDb.K_Log("Hooter");
	}
	*/
    // ******************************************************************************

    private void storeAlertDialog() {

        Context context = MainActivity.this;
        String message = "Are you ready to store the quarter's scores?";
        String button1String = "Yes";
        String button2String = "No";
        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        tg.startTone(ToneGenerator.TONE_PROP_ACK);
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                q_timerActive = false;
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_CLOCK_RUNNING, 0);

                updateQuarter();
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                startStoreTimer();
                myDb.K_Log("Not Update Quarter");
            }
        });
        ad.show();
    }
    // ******************************************************************************

    private void updateQuarter() {
        // Update System
        switch (getQuarter()) {
            case 1:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_G_1, getHomeGoals());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_B_1, getHomePoints());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_G_1, getAwayGoals());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_B_1, getAwayPoints());
                break;
            case 2:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_G_2, getHomeGoals());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_B_2, getHomePoints());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_G_2, getAwayGoals());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_B_2, getAwayPoints());
                break;
            case 3:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_G_3, getHomeGoals());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_B_3, getHomePoints());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_G_3, getAwayGoals());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_B_3, getAwayPoints());
                break;
            case 4:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_G_4, getHomeGoals());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_H_B_4, getHomePoints());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_G_4, getAwayGoals());
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_A_B_4, getAwayPoints());
        }
        q_timerLockout = false;
        buttons(true, false, false, true);
        t = (TextView) findViewById(R.id.q_timer);
        if (getClock_Up_Dn() == 0) {
            t.setText("" + "00:00");
            setTimeToGo("00:00");
        } else {
            t.setText("" + getQuarterTime() + ":00");
            setTimeToGo("" + padLeft("" + getQuarterTime(), 2) + ":00");
        }
        myDb.K_Log("Update Quarter " + getQuarter());
        Toast toast = Toast.makeText(getApplicationContext(), "Quarter " + getQuarter() + " scores stored!", Toast.LENGTH_LONG);
        toast.show();
        inc_Quarter();
    }
    // ******************************************************************************
    // Timer for Storing the Quarter's scores
    // ******************************************************************************

    public void startStoreTimer() {

        if (!s_timerActive) {
            s_timerActive = true;
            s_counter = new storeTimer((30000), 1000);
            s_counter.start();
            myDb.K_Log("Start store timer");
        }
    }
// ******************************************************************************

    class storeTimer extends CountDownTimer {

        public storeTimer(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            storeAlertDialog();
            s_counter = null;
            s_timerActive = false;
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }
    // ******************************************************************************
    // Timer to delay running onResume on start up
    // ******************************************************************************

    public void start_ResumeTimer() {

        if (!res_timerActive) {
            res_timerActive = true;
            res_counter = new resumeTimer(500, 500);
            res_counter.start();
        }
    }
// ******************************************************************************

    class resumeTimer extends CountDownTimer {

        public resumeTimer(long res_millisInFuture, long countDownInterval) {

            super(res_millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            onResume();
            res_counter = null;
            res_timerActive = false;
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

    // ******************************************************************************
    // Timer for repetitive battery level tests
    // ******************************************************************************

    public void start_batteryTimer() {

        battery_counter = new batteryTimer(30000, 30000);
        battery_counter.start();
    }
// ******************************************************************************

    class batteryTimer extends CountDownTimer {

        batteryTimer(long battery_millisInFuture, long countDownInterval) {

            super(battery_millisInFuture, countDownInterval);
        }

        // ******************************************************************************

        @Override
        public void onFinish() {

            battery_counter = null;
            start_batteryTimer();
            /**
             * Computes the battery level by registering a receiver to the intent triggered
             * by a battery status/level change.
             */
            BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    context.unregisterReceiver(this);
                    int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    int level;
                    if (rawlevel >= 0 && scale > 0) {
                        level = (rawlevel * 100) / scale;
                        checkBattery(level);
                    }
                }
            };
            IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(batteryLevelReceiver, batteryLevelFilter);
        }

        // ******************************************************************************

        @Override
        public void onTick(long millisUntilFinished) {
        }

    }

    // ******************************************************************************

    private void checkBattery(int level) {

        String mess = "";
        if (level <= batteryTestLevel) {
            batteryTestLevel = batteryTestLevel - getInc();
            switch (batteryMessageNo) {
                case 5:
                    mess = "The Battery is getting low.";
                    break;
                case 4:
                    mess = "The Battery is getting lower.";
                    break;
                case 3:
                    mess = "The Battery is getting lower and lower.";
                    break;
                case 2:
                    mess = "The Battery is getting very low.";
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.screenBrightness = 20 / 100.0f;
                    getWindow().setAttributes(lp);
                    break;
                case 1:
                    mess = "Scorer will SHUT DOWN in 30 seconds";
                    batteryTestLevel = batteryTestLevel + getInc();
                    break;
                case 0:
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                    MainActivity.this.finish();
                    break;
            }
            myDb.K_Log("Battery Message - " + mess);
            batteryAlertDialog(mess, Integer.toString(batteryMessageNo));
            batteryMessageNo--;
        }
    }
    // ******************************************************************************

    private void batteryAlertDialog(String message, String no) {

        if (ad != null) {
            ad.cancel();
            Alarm.stopAlarm();
        }
        Alarm.soundAlarm(Config.context);
        myDb.K_Log(message + "  " + no);
        Context context = MainActivity.this;
        String button1String = "Acknowledge";
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setMessage(message);
        adb.setTitle(no);
        adb.setPositiveButton(button1String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                Alarm.stopAlarm();
            }
        });
        ad = adb.create();
        ad.show();
    }
    // ******************************************************************************
    // Odds and Sods
    // ******************************************************************************

    private boolean set_to_go_timer_running() {

        return ((getHomeTeam() != 0) && (getAwayTeam() != 0) && (getGameDate().trim().length() != 0) && q_timerActive);
    }
    // ******************************************************************************

    private boolean set_to_go() {

        return (getHomeTeam() != 0) && (getAwayTeam() != 0) && (getGameDate().trim().length() != 0);
    }
    // ******************************************************************************

    public void inc_Quarter() {

        int intQuarter = getQuarter() + 1;
        if (intQuarter == 5) {
            intQuarter = 1;
        }
        final TextView textViewToChange = (TextView) findViewById(R.id.quarter);
        textViewToChange.setText("Q" + intQuarter);
        setQuarter(intQuarter);
        // Update System
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_QUARTER, intQuarter);
        myDb.K_Log("inc Quarter " + "Q" + intQuarter);
    }
    // ******************************************************************************

    public static String padLeft(String s, int n) {

        return String.format("%1$" + n + "s", s);
    }
}

/* Toast toast = Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT);
   toast.show(); */

