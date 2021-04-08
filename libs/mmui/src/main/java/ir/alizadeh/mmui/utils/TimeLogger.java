package ir.alizadeh.mmui.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Locale;

public class TimeLogger {
    private static final String LOGTAG = "TimeLogger | ";
    private String tag = "";
    private boolean nanoFlag = false;
    private long t_start = 0;
    private long t_split = 0;

    public static TimeLogger getInstance(String tag, boolean nano) {
        return new TimeLogger(tag, nano);
    }

    public static TimeLogger getInstance(String tag) {
        return new TimeLogger(tag);
    }

    private TimeLogger(@NonNull String tag) {
        this(tag, false);
    }

    private TimeLogger(@NonNull String tag, boolean nano) {
        this.tag = tag;
        this.nanoFlag = nano;
        initTime();
    }

    public void initTime() {
        this.t_start = getCurrentTime();
        this.t_split = t_start;
        Log.d(LOGTAG , String.format(Locale.US, "%s: %5s", this.tag, "begin"));
    }

    private long getCurrentTime() {
        if(nanoFlag)
            return System.nanoTime();
        else
            return System.currentTimeMillis();
    }

    private long ms(long t0, long t1) {
        long value = Math.abs(t1 - t0);
        if(nanoFlag) value = value / 1000;
        return value;
    }

    public void addSplit(String tagName) {
        long t0 = this.t_split;
        this.t_split = getCurrentTime();
        long ms = ms(t0, t_split);
        Log.d(LOGTAG , String.format(Locale.US, "%s: %5s  %5d ms, %s", this.tag, "", ms, tagName));
    }

    public void end() {
        this.t_split = getCurrentTime();
        long ms = ms(t_start, t_split);
        Log.d(LOGTAG , String.format(Locale.US, "%s: %5s, %5d ms", this.tag, "end", ms));
    }

}
