
package com.kitstop.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import static com.kitstop.score.GlobalClass.*;

public class AwayPlayerActivity extends Activity {

    private DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_away_players);

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

    public void onClick_AddAwayPlayer(View view) {
        if (getAwayTeam() != 0) {
            Intent intent = new Intent(this, AddAwayPlayerActivity.class);
            startActivity(intent);

            myDb.K_Log("Add Away Player");
        }

        finish();
    }

    // ***********************************

    public void onClick_DeleteAwayPlayer(View view) {

        if (getAwayTeam() != 0) {
            Intent intent = new Intent(this, DeleteAwayPlayerActivity.class);
            startActivity(intent);

            myDb.K_Log("Delete Away Player");
        }
        finish();
    }

    // ***********************************

    public void onClick_NotPlayingAwayPlayer(View view) {

        if (getAwayTeam() != 0) {
            Intent intent = new Intent(this, NotPlayingAwayPlayerActivity.class);
            startActivity(intent);

            myDb.K_Log("Not Playing Away Player");
        }
        finish();
    }
}
