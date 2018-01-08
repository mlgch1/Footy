
package com.kitstop.score;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static com.kitstop.score.GlobalClass.getAwayTeam;
import static com.kitstop.score.GlobalClass.getClub;
import static com.kitstop.score.GlobalClass.getGameDate;
import static com.kitstop.score.GlobalClass.getGround;
import static com.kitstop.score.GlobalClass.getHomeTeam;
import static com.kitstop.score.GlobalClass.getQuarter;

public class PrintActivity extends Activity {

    private DBAdapter myDb;

    // ***********************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        myDb = new DBAdapter(this);
        myDb.open();
    }

    // ***********************************
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    // ***********************************

    private WebView mWebView;

    public void onClick_game_Report(View view) {
        if (getQuarter() == 1) {
            // Create a WebView object specifically for printing
            WebView webView = new WebView(this);
            webView.setWebViewClient(new WebViewClient() {

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    Log.i("gjc", "page finished loading " + url);
                    createWebPrintJob(view);
                    mWebView = null;
                }
            });
            int i_h_g_1 = myDb.readSystem(DBAdapter.KEY_SYSTEM_H_G_1);
            int i_h_g_2 = myDb.readSystem(DBAdapter.KEY_SYSTEM_H_G_2);
            int i_h_g_3 = myDb.readSystem(DBAdapter.KEY_SYSTEM_H_G_3);
            int i_h_g_4 = myDb.readSystem(DBAdapter.KEY_SYSTEM_H_G_4);
            int i_h_b_1 = myDb.readSystem(DBAdapter.KEY_SYSTEM_H_B_1);
            int i_h_b_2 = myDb.readSystem(DBAdapter.KEY_SYSTEM_H_B_2);
            int i_h_b_3 = myDb.readSystem(DBAdapter.KEY_SYSTEM_H_B_3);
            int i_h_b_4 = myDb.readSystem(DBAdapter.KEY_SYSTEM_H_B_4);
            int i_a_g_1 = myDb.readSystem(DBAdapter.KEY_SYSTEM_A_G_1);
            int i_a_g_2 = myDb.readSystem(DBAdapter.KEY_SYSTEM_A_G_2);
            int i_a_g_3 = myDb.readSystem(DBAdapter.KEY_SYSTEM_A_G_3);
            int i_a_g_4 = myDb.readSystem(DBAdapter.KEY_SYSTEM_A_G_4);
            int i_a_b_1 = myDb.readSystem(DBAdapter.KEY_SYSTEM_A_B_1);
            int i_a_b_2 = myDb.readSystem(DBAdapter.KEY_SYSTEM_A_B_2);
            int i_a_b_3 = myDb.readSystem(DBAdapter.KEY_SYSTEM_A_B_3);
            int i_a_b_4 = myDb.readSystem(DBAdapter.KEY_SYSTEM_A_B_4);
            String h_g_1 = "" + i_h_g_1;
            String h_b_1 = "" + i_h_b_1;
            String h_p_1 = "" + ((i_h_g_1 * 6) + i_h_b_1);
            String a_g_1 = "" + i_a_g_1;
            String a_b_1 = "" + i_a_b_1;
            String a_p_1 = "" + ((i_a_g_1 * 6) + i_a_b_1);
            String h_g_2 = "" + (i_h_g_2 - i_h_g_1);
            String h_b_2 = "" + (i_h_b_2 - i_h_b_1);
            String h_p_2 = "" + (((i_h_g_2 - i_h_g_1) * 6) + (i_h_b_2 - i_h_b_1));
            String a_g_2 = "" + (i_a_g_2 - i_a_g_1);
            String a_b_2 = "" + (i_a_b_2 - i_a_b_1);
            String a_p_2 = "" + (((i_a_g_2 - i_a_g_1) * 6) + (i_a_b_2 - i_a_b_1));
            String h_g_3 = "" + (i_h_g_3 - i_h_g_2);
            String h_b_3 = "" + (i_h_b_3 - i_h_b_2);
            String h_p_3 = "" + (((i_h_g_3 - i_h_g_2) * 6) + (i_h_b_3 - i_h_b_2));
            String a_g_3 = "" + (i_a_g_3 - i_a_g_2);
            String a_b_3 = "" + (i_a_b_3 - i_a_b_2);
            String a_p_3 = "" + (((i_a_g_3 - i_a_g_2) * 6) + (i_a_b_3 - i_a_b_2));
            String h_g_4 = "" + (i_h_g_4 - i_h_g_3);
            String h_b_4 = "" + (i_h_b_4 - i_h_b_3);
            String h_p_4 = "" + (((i_h_g_4 - i_h_g_3) * 6) + (i_h_b_4 - i_h_b_3));
            String a_g_4 = "" + (i_a_g_4 - i_a_g_3);
            String a_b_4 = "" + (i_a_b_4 - i_a_b_3);
            String a_p_4 = "" + (((i_a_g_4 - i_a_g_3) * 6) + (i_a_b_4 - i_a_b_3));
            String h_g_t = "" + (i_h_g_4);
            String h_b_t = "" + (i_h_b_4);
            String h_p_t = "" + ((i_h_g_4 * 6) + i_h_b_4);
            String a_g_t = "" + (i_a_g_4);
            String a_b_t = "" + (i_a_b_4);
            String a_p_t = "" + ((i_a_g_4 * 6) + i_a_b_4);
            String strHomeTeam = "";
            String strAwayTeam = "";
            if (getHomeTeam() != 0) {
                Cursor cursor = myDb.getTeamRow(getHomeTeam());
                strHomeTeam = cursor.getString(DBAdapter.COL_TEAM_NAME);
            }
            if (getAwayTeam() != 0) {
                Cursor cursor = myDb.getTeamRow(getAwayTeam());
                strAwayTeam = cursor.getString(DBAdapter.COL_TEAM_NAME);
            }
            // Generate an HTML document on the fly:
            String htmlDocument = "<html><body>" + "<p align='center'><font size='6'>"
                    + getClub()
                    + "</font></p>"
                    + "<p align='center'><font size='6', face='Arial, Helvetica, sans-serif'>AFL SCORING CARD&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;..................................Grade</font></p>"
                    + "<p align='center'><font size='4', face='Arial, Helvetica, sans-serif'>Ground&nbsp;&nbsp;"
                    + padRightnbs(getGround(), 40)
                    + "Date&nbsp;&nbsp;"
                    + getGameDate()
                    + "</font></p>"
                    + "<p align='center'><font size='6', face='Arial, Helvetica, sans-serif'>"
                    + padRightnbs(strHomeTeam, 30)
                    + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                    + strAwayTeam
                    + "</font></p>"
                    + "<table width='833' height='284' border='1' class='afl_table'>"
                    + "<tr><th width='60' height='41' scope='col'>QTR</th><th colspan='2' scope='col'>GOALS</th><th colspan='2' scope='col'>BEHINDS</th><th width='80' scope='col'>PTS</th><th width='8' scope='col'>&nbsp;</th>"
                    + "<th colspan='2' scope='col'>GOALS</th><th colspan='2' scope='col'>BEHINDS</th><th width='80' scope='col'>PTS</th></tr>"
                    + "<tr>"
                    + "<td><div align='center'>1st</div></td>"
                    + "<td width='80'><div align='center'>"
                    + h_g_1
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + h_b_1
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + h_p_1
                    + "</div></td>"
                    + "<td>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_g_1
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_b_1
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_p_1
                    + "</div></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td><div align='center'>2nd</div></td>"
                    + "<td width='80'><div align='center'>"
                    + h_g_2
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + h_b_2
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + h_p_2
                    + "</div></td>"
                    + "<td>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_g_2
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_b_2
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_p_2
                    + "</div></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td><div align='center'>3rd</div></td>"
                    + "<td width='80'><div align='center'>"
                    + h_g_3
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + h_b_3
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + h_p_3
                    + "</div></td>"
                    + "<td>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_g_3
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_b_3
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_p_3
                    + "</div></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td><div align='center'>4th</div></td>"
                    + "<td width='80'><div align='center'>"
                    + h_g_4
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + h_b_4
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + h_p_4
                    + "</div></td>"
                    + "<td>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_g_4
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_b_4
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_p_4
                    + "</div></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td><div align='center'>Total</div></td>"
                    + "<td width='80'><div align='center'>"
                    + h_g_t
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + h_b_t
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + h_p_t
                    + "</div></td>"
                    + "<td>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_g_t
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_b_t
                    + "</div></td>"
                    + "<td width='30'>&nbsp;</td>"
                    + "<td width='80'><div align='center'>"
                    + a_p_t
                    + "</div></td>"
                    + "</tr>"
                    + "</table>"
                    + "<p>&nbsp;</p>"
                    + "<p>&nbsp;</p>"
                    + "<p><div align='center'><strong>Goal Umpire........................................................&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Field Umpire........................................................</strong></div></p>"
                    + "<p><div align='center'><strong>SCORER by SC&T - Phone 03 9751 0048</strong></div></p>"
                    + "</body></html>";
            webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);
            // Keep a reference to WebView object until you pass the PrintDocumentAdapter to the
            // PrintManager
            mWebView = webView;
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Game must be finished to print a Game Report", Toast.LENGTH_LONG);
            toast.show();
        }
        finish();
    }
    // ***********************************

    private String padRightnbs(String str, int n) {
        int len = n - str.length();
        int cnt;
        String ret = "";
        for (cnt = 1; cnt <= len; cnt++) {
            ret = ret + "&nbsp;";
        }
        return str + ret;
    }
    // ***********************************

    private void createWebPrintJob(WebView webView) {
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";
        printManager.print(jobName, printAdapter, null);
    }
    // ***********************************

    public void onClick_HomeTeam_Report(View view) {
    }
    // ***********************************

    public void onClick_AwayTeam_Report(View view) {
    }
}
