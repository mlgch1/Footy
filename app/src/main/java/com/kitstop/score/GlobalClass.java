package com.kitstop.score;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import org.acra.*;
import org.acra.annotation.*;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@ReportsCrashes(formUri = "",
        reportSenderFactoryClasses = {GjcSenderFactory.class})

public class GlobalClass extends Application {


    public void onCreate() {
        super.onCreate();

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }


    // ***********************************

    private static String strClub = "";

    public static String getClub() {
        return strClub;
    }

    public static void setClub(String aClub) {
        strClub = aClub;
    }

    // ***********************************

    private static boolean wifiSetup = false;

    public static boolean getWiFiSetup() {
        return wifiSetup;
    }

    public static void setWiFiSetup(boolean aSetup) {
        wifiSetup = aSetup;
    }

    // ***********************************

    private static boolean wifiRead = false;

    public static boolean getWiFiRead() {
        return wifiRead;
    }

    public static void setWiFiRead(boolean aRead) {
        wifiRead = aRead;
    }

    // ***********************************

    private static String strGround = "";

    public static String getGround() {
        return strGround;
    }

    public static void setGround(String aGround) {
        strGround = aGround;
    }

    // ***********************************

    private static String strGameDate = "";

    public static String getGameDate() {
        return strGameDate;
    }

    public static void setGameDate(String aGameDate) {
        strGameDate = aGameDate;
    }

    // ***********************************

    private static int intMode = 1;

    public static int getMode() {
        return intMode;
    }

    public static void setMode(int aMode) {
        intMode = aMode;
    }

    // ***********************************

    private static int intClock = 0;

    public static int getClock_Up_Dn() {
        return intClock;
    }

    public static void setClock_Up_Dn(int aClock) {
        intClock = aClock;
    }

    // ***********************************

    private static int intQuarterTime = 20;

    public static int getQuarterTime() {
        return intQuarterTime;
    }

    public static void setQuarterTime(int aQuarterTime) {
        intQuarterTime = aQuarterTime;
    }

    // ***********************************

    private static boolean test = false;

    public static boolean getTest() {
        return test;
    }

    public static void setTest(boolean aTest) {
        test = aTest;
    }

    // ***********************************

    private static boolean testing = false;

    public static boolean getStartTesting() {
        return testing;
    }

    public static void setStartTesting(boolean aTesting) {
        testing = aTesting;
    }

    // ***********************************

    private static boolean usb = false;

    public static boolean getUsb() {
        return usb;
    }

    public static void setUsb(boolean ausb) {
        usb = ausb;
    }
    // ***********************************

    private static long lngHomeTeam = 0;

    public static long getHomeTeam() {
        return lngHomeTeam;
    }

    public static void setHomeTeam(long aHomeTeam) {
        lngHomeTeam = aHomeTeam;
    }

    // ***********************************

    private static long lngAwayTeam = 0;

    public static long getAwayTeam() {
        return lngAwayTeam;
    }

    public static void setAwayTeam(long aAwayTeam) {
        lngAwayTeam = aAwayTeam;
    }

    // ***********************************

    private static int intHomeGoals = 0;

    public static int getHomeGoals() {
        return intHomeGoals;
    }

    public static void setHomeGoals(int aHomeGoals) {
        intHomeGoals = aHomeGoals;
    }

    // ***********************************

    private static int intAwayGoals = 0;

    public static int getAwayGoals() {
        return intAwayGoals;
    }

    public static void setAwayGoals(int aAwayGoals) {
        intAwayGoals = aAwayGoals;
    }

    // ***********************************

    private static int intHomePoints = 0;

    public static int getHomePoints() {
        return intHomePoints;
    }

    public static void setHomePoints(int aHomePoints) {
        intHomePoints = aHomePoints;
    }

    // ***********************************

    private static int intAwayPoints = 0;

    public static int getAwayPoints() {
        return intAwayPoints;
    }

    public static void setAwayPoints(int aAwayPoints) {
        intAwayPoints = aAwayPoints;
    }

    // ***********************************

    private static int intQuarter = 0;

    public static int getQuarter() {
        return intQuarter;
    }

    public static void setQuarter(int aQuarter) {
        intQuarter = aQuarter;
    }

    // ***********************************

    private static int intThresh = 0;

    public static int getThresh() {
        return intThresh;
    }

    public static void setThresh(int aThresh) {
        intThresh = aThresh;
    }

    // ***********************************

    private static int intInc = 0;

    public static int getInc() {
        return intInc;
    }

    public static void setInc(int aInc) {
        intInc = aInc;
    }

    // ***********************************

    private static int intSSID = 0;

    public static int getSSID() {
        return intSSID;
    }

    public static void setSSID(int aSSID) {
        intSSID = aSSID;
    }

    // ***********************************

    private static int intChannel = 0;

    public static int getChannel() {
        return intChannel;
    }

    public static void setChannel(int aChannel) {
        intChannel = aChannel;
    }

    // ***********************************

    private static int intClock_Running = 0;

    public static int getClock_Running() {
        return intClock_Running;
    }

    public static void setClock_Running(int aClock_Running) {
        intClock_Running = aClock_Running;
    }

    // ***********************************

    private static long lngClock = 0;

    public static long getClock() {
        return lngClock;
    }

    public static void setClock(long aClock) {
        lngClock = aClock;
    }

    // ***********************************

    private static int intError = 0;

    public static int getError() {
        return intError;
    }

    public static void setError(int aError) {
        intError = aError;
    }

    // ***********************************

    private static String timeToGo = "00:00";

    public static String getTimeToGo() {
        return timeToGo;
    }

    public static void setTimeToGo(String atimeToGo) {
        timeToGo = atimeToGo;
    }
}

