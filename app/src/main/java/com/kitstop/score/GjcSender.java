package com.kitstop.score;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collections.ImmutableSet;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.kitstop.score.GlobalClass.getClock_Running;
import static com.kitstop.score.GlobalClass.getError;
import static com.kitstop.score.GlobalClass.setClock;
import static com.kitstop.score.GlobalClass.setClock_Running;
import static com.kitstop.score.GlobalClass.setError;

/**
 * Created by GJC on 25/06/2016.
 */


// ******************************************************************************

public class GjcSender implements ReportSender {

    private final ACRAConfiguration config;

    private final Map<ReportField, String> mMapping = new HashMap<ReportField, String>();

    private FileWriter crashReport = null;

    // ***********************************

    public GjcSender(ACRAConfiguration config) {
        this.config = config;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy--HH_mm_ss");
        Date date = new Date();

        String filename = "MYRT_ERROR " + dateFormat.format(date) + ".txt";

        // the destination
        File logFile = new File(Environment.getExternalStorageDirectory(), filename);

        try {
            crashReport = new FileWriter(logFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ******************************************************************************

    @Override
    public void send(Context context, CrashReportData report) throws ReportSenderException {
        final Map<String, String> finalReport = remap(report);

        try {
            BufferedWriter buf = new BufferedWriter(crashReport);

            Set<Map.Entry<String, String>> set = finalReport.entrySet();
            Iterator<Map.Entry<String, String>> i = set.iterator();

            while (i.hasNext()) {
                Map.Entry<String, String> me = (Map.Entry<String, String>) i.next();
                buf.append("[" + me.getKey() + "]=" + me.getValue());
            }

            buf.flush();
            buf.close();

            DBAdapter myDb;

            myDb = new DBAdapter(context);
            myDb.open();

            myDb.updateSystem(DBAdapter.KEY_SYSTEM_ERROR, 1);
            setError(1);

////            View v = null;
////            v = v.findViewById(R.id.blink_bug);
////            v.setVisibility(TextView.VISIBLE);
//            if (                q_timerActive) {
//
//                myDb.updateSystem(DBAdapter.KEY_SYSTEM_CLOCK_RUNNING, 1);
////            setClock_Running(1);
//            }
////            myDb.updateSystemLng(DBAdapter.KEY_SYSTEM_CLOCK, GlobalClass.getClock());
////            setClock(GlobalClass.getClock());



// Toast toast = Toast.makeText(context.getApplicationContext(), "QQQQQ " +  getClock_Running(), Toast.LENGTH_LONG);
//   toast.show();





            myDb.close();

        } catch (IOException e) {
            Log.e("TAG", "IO ERROR", e);
        }
    }

    // ******************************************************************************

    private Map<String, String> remap(Map<ReportField, String> report) {

        Set<ReportField> fields = config.getReportFields();
        if (fields.isEmpty()) {
            fields = new ImmutableSet<ReportField>(ACRAConstants.DEFAULT_REPORT_FIELDS);
        }

        final Map<String, String> finalReport = new HashMap<String, String>(
                report.size());
        for (ReportField field : fields) {
            if (mMapping == null || mMapping.get(field) == null) {
                finalReport.put(field.toString(), report.get(field));
            } else {
                finalReport.put(mMapping.get(field), report.get(field));
            }
        }
        return finalReport;
    }
}

