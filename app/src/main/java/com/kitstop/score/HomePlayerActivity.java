
package com.kitstop.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomePlayerActivity extends Activity {

    private DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_players);

        openDB();
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

    public void onClick_AddHomePlayer(View view) {
        if (GlobalClass.getHomeTeam() != 0) {
            Intent intent = new Intent(this, AddHomePlayerActivity.class);
            startActivity(intent);

            myDb.K_Log("Add Home Player");
        }

        finish();
    }

    // ***********************************

    public void onClick_DeleteHomePlayer(View view) {

        if (GlobalClass.getHomeTeam() != 0) {
            Intent intent = new Intent(this, DeleteHomePlayerActivity.class);
            startActivity(intent);

            myDb.K_Log("Delete Home Player");

        }

        finish();
    }

    // ***********************************

    public void onClick_NotPlayingHomePlayer(View view) {

        if (GlobalClass.getHomeTeam() != 0) {
            Intent intent = new Intent(this, NotPlayingHomePlayerActivity.class);
            startActivity(intent);

            myDb.K_Log("Not Playing Home Player");

        }

        finish();
    }
}
