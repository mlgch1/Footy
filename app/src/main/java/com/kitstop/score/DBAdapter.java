
package com.kitstop.score;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class DBAdapter {

    // Database Name
    private static final String DATABASE_NAME = "Score_Db";
    // ***********************************

    // Database Version
    private static final int DATABASE_VERSION = 7;  // 28/06/2016
    // ***********************************

    // Table Names
    public static final String TABLE_PLAYER = "players";
    public static final String TABLE_TEAM = "teams";
    public static final String TABLE_SYSTEM = "system";
    public static final String TABLE_LOG = "log";
    // ***********************************

    // Players Table - column names
    public static final String KEY_PLAYER_ID = "_id";
    public static final String KEY_PLAYER_TEAM_ID = "player_team_id";
    public static final String KEY_PLAYER_NO = "player_no";
    public static final String KEY_PLAYER_NAME = "player_name";
    public static final String KEY_PLAYER_GOALS = "player_goals";
    public static final String KEY_PLAYER_POINTS = "player_points";
    public static final String KEY_PLAYER_STATUS = "player_status";

    public static final String[] PLAYER_KEYS = new String[]{KEY_PLAYER_ID, KEY_PLAYER_TEAM_ID, KEY_PLAYER_NO, KEY_PLAYER_NAME, KEY_PLAYER_GOALS, KEY_PLAYER_POINTS,
            KEY_PLAYER_STATUS};
    // ***********************************

    // Teams Table - column names
    public static final String KEY_TEAM_ID = "_id";
    public static final String KEY_TEAM_NAME = "team_name";

    public static final int COL_TEAM_NAME = 1;

    public static final String[] TEAM_KEYS = new String[]{KEY_TEAM_ID, KEY_TEAM_NAME};
    // ***********************************

    // System Table - column names
    public static final String KEY_SYSTEM_ID = "_id";
    public static final String KEY_SYSTEM_HOME_TEAM = "system_home_team";
    public static final String KEY_SYSTEM_AWAY_TEAM = "system_away_team";
    public static final String KEY_SYSTEM_HOME_GOALS = "system_home_goals";
    public static final String KEY_SYSTEM_HOME_POINTS = "system_home_points";
    public static final String KEY_SYSTEM_AWAY_GOALS = "system_away_goals";
    public static final String KEY_SYSTEM_AWAY_POINTS = "system_away_points";
    public static final String KEY_SYSTEM_QUARTER_LENGTH = "system_quarter_length";
    public static final String KEY_SYSTEM_QUARTER = "system_quarter";
    public static final String KEY_SYSTEM_QUARTER_START_TIME = "system_quarter_start_time";
    public static final String KEY_SYSTEM_GAME_DATE = "system_game_date";
    public static final String KEY_SYSTEM_GROUND = "system_ground";
    public static final String KEY_SYSTEM_MODE = "system_mode";
    public static final String KEY_SYSTEM_CLOCK_UP_DN = "system_clock_up_dn";

    public static final String KEY_SYSTEM_H_G_1 = "system_h_g_1";
    public static final String KEY_SYSTEM_H_G_2 = "system_h_g_2";
    public static final String KEY_SYSTEM_H_G_3 = "system_h_g_3";
    public static final String KEY_SYSTEM_H_G_4 = "system_h_g_4";

    public static final String KEY_SYSTEM_H_B_1 = "system_h_b_1";
    public static final String KEY_SYSTEM_H_B_2 = "system_h_b_2";
    public static final String KEY_SYSTEM_H_B_3 = "system_h_b_3";
    public static final String KEY_SYSTEM_H_B_4 = "system_h_b_4";

    public static final String KEY_SYSTEM_A_G_1 = "system_a_g_1";
    public static final String KEY_SYSTEM_A_G_2 = "system_a_g_2";
    public static final String KEY_SYSTEM_A_G_3 = "system_a_g_3";
    public static final String KEY_SYSTEM_A_G_4 = "system_a_g_4";

    public static final String KEY_SYSTEM_A_B_1 = "system_a_b_1";
    public static final String KEY_SYSTEM_A_B_2 = "system_a_b_2";
    public static final String KEY_SYSTEM_A_B_3 = "system_a_b_3";
    public static final String KEY_SYSTEM_A_B_4 = "system_a_b_4";

    public static final String KEY_SYSTEM_BATT_THRESH = "system_batt_thresh";
    public static final String KEY_SYSTEM_BATT_INC = "system_batt_inc";

    public static final String KEY_SYSTEM_SSID = "system_ssid";
    public static final String KEY_SYSTEM_CHANNEL = "system_channel";

    public static final String KEY_SYSTEM_ERROR = "system_error";

    public static final String KEY_SYSTEM_CLOCK_RUNNING = "system_clock_running";
    public static final String KEY_SYSTEM_CLOCK = "system_clock";

    public static final String[] SYSTEM_KEYS = new String[]{KEY_SYSTEM_ID, KEY_SYSTEM_HOME_TEAM, KEY_SYSTEM_AWAY_TEAM, KEY_SYSTEM_HOME_GOALS, KEY_SYSTEM_HOME_POINTS,
            KEY_SYSTEM_AWAY_GOALS, KEY_SYSTEM_AWAY_POINTS, KEY_SYSTEM_QUARTER_LENGTH, KEY_SYSTEM_QUARTER, KEY_SYSTEM_QUARTER_START_TIME, KEY_SYSTEM_GAME_DATE, KEY_SYSTEM_GROUND, KEY_SYSTEM_MODE,
            KEY_SYSTEM_CLOCK_UP_DN, KEY_SYSTEM_H_G_1, KEY_SYSTEM_H_G_2, KEY_SYSTEM_H_G_3, KEY_SYSTEM_H_G_4, KEY_SYSTEM_H_B_1, KEY_SYSTEM_H_B_2, KEY_SYSTEM_H_B_3, KEY_SYSTEM_H_B_4,
            KEY_SYSTEM_A_G_1, KEY_SYSTEM_A_G_2, KEY_SYSTEM_A_G_3, KEY_SYSTEM_A_G_4, KEY_SYSTEM_A_B_1, KEY_SYSTEM_A_B_2, KEY_SYSTEM_A_B_3, KEY_SYSTEM_A_B_4, KEY_SYSTEM_BATT_THRESH,
            KEY_SYSTEM_BATT_INC, KEY_SYSTEM_SSID, KEY_SYSTEM_CHANNEL, KEY_SYSTEM_ERROR, KEY_SYSTEM_CLOCK_RUNNING, KEY_SYSTEM_CLOCK};
    // ***********************************

    // Log Table - column names
    public static final String KEY_LOG_ID = "_id";
    public static final String KEY_LOG_DATE = "log_date";
    public static final String KEY_LOG_TIME = "log_time";
    public static final String KEY_LOG_LOG = "log_log";

    public static final String[] LOG_KEYS = new String[]{KEY_LOG_ID, KEY_LOG_DATE, KEY_LOG_TIME, KEY_LOG_LOG};
    // ***********************************
    // Table Create Statements

    // player table create statement
    private static final String CREATE_TABLE_PLAYER = "CREATE TABLE if not exists " + TABLE_PLAYER + "(" + KEY_PLAYER_ID + " INTEGER PRIMARY KEY autoincrement ," + KEY_PLAYER_TEAM_ID
            + " INTEGER," + KEY_PLAYER_NO + " INTEGER," + KEY_PLAYER_NAME + " TEXT," + KEY_PLAYER_GOALS + " INTEGER,"
            + KEY_PLAYER_POINTS + " INTEGER," + KEY_PLAYER_STATUS + " INTEGER" + ")";

    // Team table create statement
    private static final String CREATE_TABLE_TEAM = "CREATE TABLE if not exists " + TABLE_TEAM + "(" + KEY_TEAM_ID + " INTEGER PRIMARY KEY autoincrement ," + KEY_TEAM_NAME + " TEXT"
            + ")";

    // System table create statement
    private static final String CREATE_TABLE_SYSTEM = "CREATE TABLE if not exists " + TABLE_SYSTEM + "(" + KEY_SYSTEM_ID + " INTEGER PRIMARY KEY autoincrement ," + KEY_SYSTEM_HOME_TEAM
            + " INTEGER," + KEY_SYSTEM_AWAY_TEAM + " INTEGER," + KEY_SYSTEM_HOME_GOALS + " INTEGER," + KEY_SYSTEM_HOME_POINTS
            + " INTEGER," + KEY_SYSTEM_AWAY_GOALS + " INTEGER," + KEY_SYSTEM_AWAY_POINTS + " INTEGER," + KEY_SYSTEM_QUARTER_LENGTH
            + " INTEGER," + KEY_SYSTEM_QUARTER + " INTEGER," + KEY_SYSTEM_QUARTER_START_TIME + " INTEGER," + KEY_SYSTEM_GAME_DATE
            + " TEXT," + KEY_SYSTEM_GROUND + " TEXT," + KEY_SYSTEM_MODE + " INTEGER," + KEY_SYSTEM_CLOCK_UP_DN + " INTEGER,"
            + KEY_SYSTEM_H_G_1 + " INTEGER," + KEY_SYSTEM_H_G_2 + " INTEGER," + KEY_SYSTEM_H_G_3 + " INTEGER," + KEY_SYSTEM_H_G_4
            + " INTEGER," + KEY_SYSTEM_H_B_1 + " INTEGER," + KEY_SYSTEM_H_B_2 + " INTEGER," + KEY_SYSTEM_H_B_3 + " INTEGER,"
            + KEY_SYSTEM_H_B_4 + " INTEGER," + KEY_SYSTEM_A_G_1 + " INTEGER," + KEY_SYSTEM_A_G_2 + " INTEGER," + KEY_SYSTEM_A_G_3
            + " INTEGER," + KEY_SYSTEM_A_G_4 + " INTEGER," + KEY_SYSTEM_A_B_1 + " INTEGER," + KEY_SYSTEM_A_B_2 + " INTEGER,"
            + KEY_SYSTEM_A_B_3 + " INTEGER," + KEY_SYSTEM_A_B_4 + " INTEGER," + KEY_SYSTEM_BATT_THRESH + " INTEGER,"
            + KEY_SYSTEM_BATT_INC + " INTEGER," + KEY_SYSTEM_SSID + " INTEGER," + KEY_SYSTEM_CHANNEL + " INTEGER," + KEY_SYSTEM_ERROR + " INTEGER," + KEY_SYSTEM_CLOCK_RUNNING + " INTEGER," + KEY_SYSTEM_CLOCK + " LONG" + ")";

    // Log table create statement
    private static final String CREATE_TABLE_LOG = "CREATE TABLE if not exists " + TABLE_LOG + "(" + KEY_LOG_ID + " INTEGER PRIMARY KEY autoincrement ," + KEY_LOG_DATE + " TEXT,"
            + KEY_LOG_TIME + " TEXT," + KEY_LOG_LOG + " TEXT" + ")";
    // ***********************************
    // Constants & Data
    // ***********************************

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;
    // private Toast toast;
    // ***********************************
    // Public methods:
    // ***********************************

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }
    // ***********************************

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    // ***********************************

    // Close the database connection.
    public void close() {
        L.d("dbClose");

        myDBHelper.close();
    }
    // ***********************************
    // Private Helper Classes:
    // ***********************************

    /**
     * Private class which handles database creation and upgrading. Used to
     * handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // creating required tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_PLAYER);
            db.execSQL(CREATE_TABLE_TEAM);
            db.execSQL(CREATE_TABLE_SYSTEM);
            // Insert a zero row in system table.
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_SYSTEM_HOME_TEAM, 0);
            initialValues.put(KEY_SYSTEM_AWAY_TEAM, 0);
            initialValues.put(KEY_SYSTEM_HOME_GOALS, 0);
            initialValues.put(KEY_SYSTEM_HOME_POINTS, 0);
            initialValues.put(KEY_SYSTEM_AWAY_GOALS, 0);
            initialValues.put(KEY_SYSTEM_AWAY_POINTS, 0);
            initialValues.put(KEY_SYSTEM_QUARTER_LENGTH, 20);
            initialValues.put(KEY_SYSTEM_QUARTER, 0);
            initialValues.put(KEY_SYSTEM_QUARTER_START_TIME, 0);
            initialValues.put(KEY_SYSTEM_GAME_DATE, "");
            initialValues.put(KEY_SYSTEM_GROUND, "");
            initialValues.put(KEY_SYSTEM_MODE, 1);
            initialValues.put(KEY_SYSTEM_CLOCK_UP_DN, 1);
            initialValues.put(KEY_SYSTEM_H_G_1, 0);
            initialValues.put(KEY_SYSTEM_H_G_2, 0);
            initialValues.put(KEY_SYSTEM_H_G_3, 0);
            initialValues.put(KEY_SYSTEM_H_G_4, 0);
            initialValues.put(KEY_SYSTEM_H_B_1, 0);
            initialValues.put(KEY_SYSTEM_H_B_2, 0);
            initialValues.put(KEY_SYSTEM_H_B_3, 0);
            initialValues.put(KEY_SYSTEM_H_B_4, 0);
            initialValues.put(KEY_SYSTEM_A_G_1, 0);
            initialValues.put(KEY_SYSTEM_A_G_2, 0);
            initialValues.put(KEY_SYSTEM_A_G_3, 0);
            initialValues.put(KEY_SYSTEM_A_G_4, 0);
            initialValues.put(KEY_SYSTEM_A_B_1, 0);
            initialValues.put(KEY_SYSTEM_A_B_2, 0);
            initialValues.put(KEY_SYSTEM_A_B_3, 0);
            initialValues.put(KEY_SYSTEM_A_B_4, 0);
            initialValues.put(KEY_SYSTEM_BATT_THRESH, 40);
            initialValues.put(KEY_SYSTEM_BATT_INC, 5);
            initialValues.put(KEY_SYSTEM_SSID, 1);
            initialValues.put(KEY_SYSTEM_CHANNEL, 1);
            initialValues.put(KEY_SYSTEM_ERROR, 0);
            initialValues.put(KEY_SYSTEM_CLOCK_RUNNING, 0);
            initialValues.put(KEY_SYSTEM_CLOCK, 0);

            db.insert(TABLE_SYSTEM, null, initialValues);
            db.execSQL(CREATE_TABLE_LOG);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



    L.d("Upgrade");



            switch (newVersion) {
                case 3:
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_CLOCK_UP_DN + " INTEGER");
                    break;
                case 4:
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_BATT_THRESH + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_BATT_INC + " INTEGER");
                    break;
                case 5:
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SSID + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_CHANNEL + " INTEGER");
                    break;
                case 6:
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_ERROR + " INTEGER");
                    break;
                case 7:
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_CLOCK_RUNNING + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_CLOCK + " LONG");
                    break;
                default:
                    throw new IllegalStateException("onUpgrade() with unknown newVersion" + newVersion);
            }
        }
    }
    // ***********************************
    // Team Table
    // ***********************************

    // Add a new team.
    public long insertTeamRow(String team_name) {
        ContentValues values = new ContentValues();
        values.put(KEY_TEAM_NAME, team_name);
        // Insert it into the database.
        return db.insert(TABLE_TEAM, null, values);
    }
    // ***********************************

    // Return all teams in the database.
    public Cursor getAllTeamRows() {
        String where = null;
        Cursor c = db.query(true, TABLE_TEAM, TEAM_KEYS, where, null, null, null, KEY_TEAM_NAME, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    // ***********************************

    // Get a specific team (by rowId)
    public Cursor getTeamRow(long rowId) {
        String where = KEY_TEAM_ID + "=" + rowId;
        Cursor c = db.query(true, TABLE_TEAM, TEAM_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    // ***********************************

    // Delete a team, by rowId (primary key)
    public boolean deleteTeamRow(long rowId) {
        String where = KEY_TEAM_ID + "=" + rowId;
        return db.delete(TABLE_TEAM, where, null) != 0;
    }
    // ***********************************
    // Players Table
    // ***********************************

    // Add a new player.
    public long insertPlayerRow(long player_team_id, String player_no, String player_name, int player_goals, int player_points, int player_status) {
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_TEAM_ID, player_team_id);
        values.put(KEY_PLAYER_NO, player_no);
        values.put(KEY_PLAYER_NAME, player_name);
        values.put(KEY_PLAYER_GOALS, player_goals);
        values.put(KEY_PLAYER_POINTS, player_points);
        values.put(KEY_PLAYER_STATUS, player_status);
        // Insert it into the database.
        return db.insert(TABLE_PLAYER, null, values);
    }
    // ***********************************

    // Return all players in the database for a given team.
    public Cursor getAllPlayerRowsForTeam(long rowTeamId, String strStart, String strFinish) {
        String where = (KEY_PLAYER_TEAM_ID + "=" + rowTeamId) + " AND " + (KEY_PLAYER_STATUS + " = " + 0);
        Cursor c = db.query(true, TABLE_PLAYER, PLAYER_KEYS, where, null, null, null, KEY_PLAYER_NO, (strStart + "," + strFinish));
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    // ***********************************

    // Return the total goals for players in the database for a given team.
    public int getPlayerGoalsForTeam(long rowTeamId) {
        Integer sum = 0;
        String where = KEY_PLAYER_TEAM_ID + "=" + rowTeamId;
        Cursor c = db.query(true, TABLE_PLAYER, PLAYER_KEYS, where, null, null, null, KEY_PLAYER_NO, null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                int data = c.getInt(c.getColumnIndex(DBAdapter.KEY_PLAYER_GOALS));
                sum = sum + data;
                c.moveToNext();
            }
        }
        c.close();
        return sum;
    }
    // ***********************************

    // Return the total points for players in the database for a given team.
    public int getPlayerPointsForTeam(long rowTeamId) {
        Integer sum = 0;
        String where = KEY_PLAYER_TEAM_ID + "=" + rowTeamId;
        Cursor c = db.query(true, TABLE_PLAYER, PLAYER_KEYS, where, null, null, null, KEY_PLAYER_NO, null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                int data = c.getInt(c.getColumnIndex(DBAdapter.KEY_PLAYER_POINTS));
                sum = sum + data;
                c.moveToNext();
            }
        }
        c.close();
        return sum;
    }
    // ***********************************

    // Get a specific player (by rowId)
    public Cursor getPlayerRow(long rowId) {
        String where = KEY_PLAYER_ID + "=" + rowId;
        Cursor c = db.query(true, TABLE_PLAYER, PLAYER_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    // ***********************************

    // Update a specific player (by rowId)
    public void updatePlayerRow(long rowId, String strCol, int intValue) {
        ContentValues values = new ContentValues();
        values.put(strCol, intValue);
        // Update it in the database.
        db.update(TABLE_PLAYER, values, KEY_PLAYER_ID + "=" + rowId, null);
    }
    // ***********************************

    // Zero the score and status columns in all rows
    public void zeroPlayerRows() {
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_GOALS, 0);
        values.put(KEY_PLAYER_POINTS, 0);
        values.put(KEY_PLAYER_STATUS, 0);
        // Update the database.
        db.update(TABLE_PLAYER, values, null, null);
    }
    // ***********************************

    // Delete a player, by rowId (primary key)
    public boolean deletePlayersOfTeam(long rowId) {
        String where = KEY_PLAYER_TEAM_ID + "=" + rowId;
        return db.delete(TABLE_PLAYER, where, null) != 0;
    }
    // ***********************************

    // Delete a player, by rowId (primary key)
    public boolean deletePlayerRow(long rowId) {
        String where = KEY_PLAYER_ID + "=" + rowId;
        return db.delete(TABLE_PLAYER, where, null) != 0;
    }
    // ***********************************
    // System Table
    // ***********************************

    // Update a value in the system table.
    public void updateSystem(String strValue_Name, int intValue) {
        ContentValues Values = new ContentValues();
        Values.put(strValue_Name, intValue);
        // Update it in the database.
        db.update(TABLE_SYSTEM, Values, KEY_SYSTEM_ID + "=" + 1, null);
    }

    // ***********************************

    // Update a value in the system table.
    public void updateSystemStr(String strValue_Name, String intValue) {
        ContentValues Values = new ContentValues();
        Values.put(strValue_Name, intValue);
        // Update it in the database.
        db.update(TABLE_SYSTEM, Values, KEY_SYSTEM_ID + "=" + 1, null);
    }

    // ***********************************

    // Update a value in the system table.
    public void updateSystemLng(String strValue_Name, Long lngValue) {
        ContentValues Values = new ContentValues();
        Values.put(strValue_Name, lngValue);
        // Update it in the database.
        db.update(TABLE_SYSTEM, Values, KEY_SYSTEM_ID + "=" + 1, null);
    }

    // ***********************************

    // Read a value in the system table.
    public int readSystem(String strValue_Name) {
        int intData = 0;
        Cursor c = db.query(true, TABLE_SYSTEM, SYSTEM_KEYS, KEY_SYSTEM_ID + "=" + 1, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            intData = c.getInt(c.getColumnIndex(strValue_Name));
        }
        return intData;
    }
    // ***********************************

    // Read a value in the system table.
    public String readSystemStr(String strValue_Name) {
        String strData = "";
        Cursor c = db.query(true, TABLE_SYSTEM, SYSTEM_KEYS, KEY_SYSTEM_ID + "=" + 1, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            strData = c.getString(c.getColumnIndex(strValue_Name));
        }
        return strData;
    }
    // ***********************************
    // Log Table
    // ***********************************

    // Insert a value in the Log table.
    public void K_Log(String strLog) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String strDate = padLeft0(("" + day), 2) + "/" + padLeft0(("" + month), 2) + "/" + ("" + year);
        String strTime = padLeft0(("" + hour), 2) + ":" + padLeft0(("" + minute), 2) + ":" + padLeft0(("" + second), 2);
        ContentValues values = new ContentValues();
        values.put(KEY_LOG_DATE, strDate);
        values.put(KEY_LOG_TIME, strTime);
        values.put(KEY_LOG_LOG, strLog);
        // Insert it in the database.
        db.insert(TABLE_LOG, null, values);
    }
    // ***********************************

    public static String padLeft0(String s, int n) {
        return String.format("%" + n + "s", s).replace(' ', '0');
    }
    // ***********************************

    // Return all log rows in the database.
    public Cursor getAllLogRows() {
        Cursor c = db.query(true, TABLE_LOG, LOG_KEYS, null, null, null, null, "_id DESC", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    // ***********************************

    // Delete all log rows in the database.
    public void Clear_Log() {
        db.delete(TABLE_LOG, null, null);
    }
}
