
package com.kitstop.score;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static com.kitstop.score.GlobalClass.getAwayGoals;
import static com.kitstop.score.GlobalClass.getAwayPoints;
import static com.kitstop.score.GlobalClass.getChannel;
import static com.kitstop.score.GlobalClass.getHomeGoals;
import static com.kitstop.score.GlobalClass.getHomePoints;
import static com.kitstop.score.GlobalClass.getSSID;
import static com.kitstop.score.GlobalClass.getStartTesting;
import static com.kitstop.score.GlobalClass.getTest;
import static com.kitstop.score.GlobalClass.getTimeToGo;
import static com.kitstop.score.GlobalClass.getUsb;
import static com.kitstop.score.GlobalClass.getWiFiRead;
import static com.kitstop.score.GlobalClass.getWiFiSetup;
import static com.kitstop.score.GlobalClass.setTest;
import static com.kitstop.score.GlobalClass.setUsb;
import static com.kitstop.score.GlobalClass.setWiFiRead;
import static com.kitstop.score.GlobalClass.setWiFiSetup;

public class Usb_Service extends Service {

    D2xxManager ftdid2xx = null;
    static FT_Device ftDevice = null;

//    private final int VID = 0x0403;   // WiFi module
    private final int VID = 0x0fe6;     // USB to Ethernet
    // private final int PID = 0x6001; // 0403 hex 1027 decimal 6001 hex

    private boolean dialogShowing = false;
    private boolean alarmSilenced = false;
    private AlertDialog cancelDialog = null;

    private ByteArrayOutputStream dArray = null;
    private byte[] head = new byte[]{(byte) 71, (byte) 74, (byte) 67,};    // GJC

    public static picTimer pic_counter;
    public static flashTimer flash_counter;
    public static timeoutTimer timeout_counter;

    public static final int readLength = 128;
    static byte[] readData = new byte[readLength];
    static char[] readDataToText = new char[readLength];

    public static int iavailable = 0;
    public static boolean bReadThreadGoing = false;
    static readThread read_thread;

    public static Integer stringCntr = -1;

    private String[] op_string = {"$$$", "", "", "save" + (char) 13, "exit" + (char) 13};
    private String[] return_string = {"CMD", "AOK", "AOK", "<4.41>", "EXIT"};

    public static String readString = "";

   ImageView i = (ImageView) Config.context.findViewById(R.id.usb);

    static int toastCnt = 0;

    // ******************************************************************************

    @Override
    public void onCreate() {
        super.onCreate();

        if (ftdid2xx == null) {
            // Create D2xx class

            try {
                ftdid2xx = D2xxManager.getInstance(this);
            } catch (D2xxManager.D2xxException e) {
                e.printStackTrace();
            }
        }
        ftDevice = null;
        start_picTimer();
    }

    // ******************************************************************************

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ******************************************************************************

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


// ReadThread??

        if (timeout_counter != null) {
            timeout_counter.cancel();
        }
        if (pic_counter != null) {
            pic_counter.cancel();
        }
    }

    // ******************************************************************************

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    // ******************************************************************************

    public void send_Usb() {

        check_Usb();

        String ssid = String.format("%02d", getSSID());
        String channel = String.format("%02d", getChannel());

        if (getUsb()) {
            if (!getWiFiSetup()) {
                String ss;
                if (getStartTesting()) {
                    if (getTest()) {
                        setTest(false);
                        ss = "GJC88888888888888888888";
                    } else {
                        setTest(true);
                        ss = "GJCBBBBBBBBBBBBBBBBBBBB";
                    }
                } else {
                    ss = setupString();
                }
                try {
                    sendString(ss);
                    start_flashTimer();
                } catch (Exception e) {
                }
                if (dArray != null) {
                    try {
                        dArray.close();
                    } catch (Exception e) {
                    }
                }
            } else {
                if (stringCntr == -1) {
                    if (timeout_counter == null) {
                        start_timeoutTimer();
                    }
                    Toast toast = Toast.makeText(getApplicationContext(), "WiFi Setup Starting", Toast.LENGTH_LONG);
                    toast.show();

                    toast = Toast.makeText(getApplicationContext(), "ch " + channel, Toast.LENGTH_LONG);
                    toast.show();

                    op_string[1] = "set a s scorer_" + ssid + (char) 13;
                    op_string[2] = "set w c " + channel + (char) 13;
                }
                if (!getWiFiRead()) {
                    stringCntr++;

                    if (stringCntr == 6) {
                        setWiFiSetup(false);
                        setWiFiRead(false);

                        Toast toast = Toast.makeText(getApplicationContext(), "WiFi Setup Complete - SSID = " + ssid + " Ch = " + channel, Toast.LENGTH_LONG);
                        toast.show();

                        toast = Toast.makeText(getApplicationContext(), "Turn WiFi Box Off, then On", Toast.LENGTH_LONG);
                        toast.show();

                        Alarm.soundAlarm(Config.context);

                        return;
                    }
                    try {
                        sendString(op_string[stringCntr]);

                        setWiFiRead(true);

                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    // ***********************************

    private void check_Usb() {

        int devCount = 0;

        setUsb(false);

        UsbManager aManager = (UsbManager) Config.context.getSystemService(Context.USB_SERVICE);
        UsbDevice aDevice;
        HashMap<String, UsbDevice> aDeviceList = aManager.getDeviceList();
        for (UsbDevice usbDevice : aDeviceList.values()) {
            aDevice = usbDevice;
            if (aDevice.getVendorId() == VID) {
                setUsb(true);
            }
        }

        aManager = null;
        aDeviceList = null;
        aDevice = null;

        if (getUsb()) {
            if (ftDevice == null) {
                devCount = ftdid2xx.createDeviceInfoList(this);

                // open our first device
                if (devCount == 1) {
                    ftDevice = ftdid2xx.openByIndex(this, 0);

                    if (ftDevice == null) {
                        setUsb(false);

                        if (toastCnt == 0) {
                            toastCnt++;

                            Toast toast = Toast.makeText(getApplicationContext(), "Turn WiFi Box Off, then On", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return;
                    }

                    toastCnt = 0;

                    // configure our port
                    // reset to UART mode for 232 devices
                    ftDevice.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
                    ftDevice.setBaudRate(9600);
                    byte dataBits = D2xxManager.FT_DATA_BITS_8;
                    byte stopBits = D2xxManager.FT_STOP_BITS_1;
                    byte parity = D2xxManager.FT_PARITY_NONE;
                    ftDevice.setDataCharacteristics(dataBits, stopBits, parity);
                    byte flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
                    ftDevice.setFlowControl(flowCtrlSetting, (byte) 0x0b, (byte) 0x0d);

                    if (ftDevice.isOpen()) {
                        if (!bReadThreadGoing) {
                            read_thread = new readThread(handler);
                            read_thread.start();
                            bReadThreadGoing = true;

                            ftDevice.purge((byte) (D2xxManager.FT_PURGE_TX | D2xxManager.FT_PURGE_RX));
                            ftDevice.restartInTask();
                        }
                    }
                }
                setUsb(true);
                Alarm.stopAlarm();
                if (dialogShowing) {
                    cancelDialog.dismiss();
                }
                alarmSilenced = false;
                dialogShowing = false;
            }
        } else {
            setUsb(false);
            setWiFiSetup(false);
            setWiFiRead(false);
            ftDevice = null;

            if (!alarmSilenced) {
                Alarm.soundAlarm(Config.context);
            }
            if (!dialogShowing) {
                cancelDialog = new AlertDialog.Builder(Config.context).setTitle("WiFi Box is not connected").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                dialogShowing = true;
            }
        }
    }

    // ***********************************

    public static void sendString(String writeData) {
        if (!ftDevice.isOpen()) {
           L.d("Device not open");
            return;
        }
        ftDevice.purge((byte) (D2xxManager.FT_PURGE_TX | D2xxManager.FT_PURGE_RX));

        byte[] OutData = writeData.getBytes();
        ftDevice.write(OutData, writeData.length());




        L.d(writeData);
    }

    // ******************************************************************************

    public String setupString() {
        dArray = new ByteArrayOutputStream();
        byte[] buff = dataString();
        dArray.write(head, 0, head.length);



        L.d("y " + dArray.toString() + "p");


        dArray.write(buff, 0, buff.length);



        L.d("z " + dArray.toString());


        return dArray.toString();
    }

    // ******************************************************************************

    private byte[] dataString() {
        int intGoals_ht = getHomeGoals();
        int intPoints_ht = getHomePoints();
        int intGoals_at = getAwayGoals();
        int intPoints_at = getAwayPoints();
        int intTotal_ht1 = (intGoals_ht * 6) + (intPoints_ht);
        int intTotal_at1 = (intGoals_at * 6) + (intPoints_at);
        String strTime = getTimeToGo();
        strTime = strTime.trim();
        if (strTime.length() == 4) {
            strTime = "0" + strTime;
        }
        String decMin = strTime.substring(0, 1);
        String unitMin = strTime.substring(1, 2);
        String decSec = strTime.substring(3, 4);
        String unitSec = strTime.substring(4, 5);

        // Length of string should be a multple of 4 characters

        String s1 = "  " + decMin + unitMin + decSec + unitSec + padLeftB(Integer.toString(intGoals_ht), 2) + padLeftB(Integer.toString(intPoints_ht), 2) + padLeftB(Integer.toString(intTotal_ht1), 3)
                + padLeftB(Integer.toString(intGoals_at), 2) + padLeftB(Integer.toString(intPoints_at), 2) + padLeftB(Integer.toString(intTotal_at1), 3);


        L.d("B" + s1 + "C");


        return s1.getBytes();
    }

    // ******************************************************************************

    public String padLeftB(String s, int n) {
        return String.format("%" + n + "s", s).replace(' ', 'B');
    }



/*
    // ******************************************************************************

   public Boolean isMainActive() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean active = sp.getBoolean("active", false);


        return active;
   }
*/



    // ******************************************************************************
    // Timer for repetitive transmissions
    // ******************************************************************************

    public void start_picTimer() {
        pic_counter = new picTimer(500, 250);
        pic_counter.start();
    }

// ******************************************************************************

    class picTimer extends CountDownTimer {
        picTimer(long pic_millisInFuture, long countDownInterval) {
            super(pic_millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            pic_counter = null;


/*
            if (!isMainActive()) {
                stopSelf();

                Toast toast = Toast.makeText(getApplicationContext(), "Stop USB ", Toast.LENGTH_SHORT);
                toast.show();

            }
*/

            start_picTimer();
            send_Usb();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    // ******************************************************************************
    // Timer for flashing WiFi Box Connected
    // ******************************************************************************

    public void start_flashTimer() {
        flash_counter = new flashTimer(250, 250);
        flash_counter.start();
        i.setVisibility(View.VISIBLE);
    }

// ******************************************************************************

    class flashTimer extends CountDownTimer {
        flashTimer(long flash_millisInFuture, long countDownInterval) {
            super(flash_millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            flash_counter = null;
            i.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    // ******************************************************************************
    // Timer for overall timeout
    // ******************************************************************************

    public void start_timeoutTimer() {
        timeout_counter = new timeoutTimer(10000, 5000);
        timeout_counter.start();
    }

// ******************************************************************************

    class timeoutTimer extends CountDownTimer {
        timeoutTimer(long timeout_millisInFuture, long countDownInterval) {
            super(timeout_millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            if (getWiFiSetup()) {
                timeout_counter = null;

                setWiFiRead(false);

                setWiFiSetup(false);

                Toast toast = Toast.makeText(getApplicationContext(), "WiFi Setup Unsuccesfull", Toast.LENGTH_LONG);
                toast.show();
            }
            Alarm.stopAlarm();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    // ******************************************************************************
    // Read Thread
    // ******************************************************************************

    private class readThread extends Thread {
        Handler mHandler;

        readThread(Handler h) {
            mHandler = h;
            this.setPriority(Thread.MIN_PRIORITY);
        }

        @Override
        public void run() {
            int i;
            while (bReadThreadGoing) {
                if (getUsb()) {
                    if (getWiFiRead()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }

                        readData = new byte[readLength];

                        synchronized (ftDevice) {
                            iavailable = ftDevice.getQueueStatus();

                            if (iavailable > 0) {
                                if (iavailable > readLength) {
                                    iavailable = readLength;
                                }
                                ftDevice.read(readData, iavailable);

                                for (i = 0; i < iavailable; i++) {
                                    readDataToText[i] = (char) readData[i];
                                }
                                readString = readString + String.copyValueOf(readDataToText, 0, iavailable);
                                readString = readString.replaceAll("[\n]", "");
                                readString = readString.replaceAll("[\r]", "");
                                readString = readString.replaceAll("\u0000", "");

                                if ((readString).contains(return_string[stringCntr])) {
                                    readString = "";

                                    setWiFiRead(false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

