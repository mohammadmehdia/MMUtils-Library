package ir.alizadeh.mmui.cntrl;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class MyPrefsManager {

    private static MyPrefsManager instance;

    private final SharedPreferences prefs;

    public MyPrefsManager(Context context) {
        prefs = context.getSharedPreferences("my-prefs-man", Context.MODE_PRIVATE);
    }

    public static MyPrefsManager getInstance(Context context) {
        if(instance == null && context != null) {
            instance = new MyPrefsManager(context);
        }
        return instance;
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    public void putString(String key, String val) {
        this.prefs.edit().putString(key, val).apply();
    }

    public String getString(String key, String defaultValue) {
        return this.prefs.getString(key, defaultValue);
    }

    public void putInt(String key, int val) {
        this.prefs.edit().putInt(key, val).apply();
    }

    public int getInt(String key, int defaultValue) {
        return this.prefs.getInt(key, defaultValue);
    }

    public void putLong(String key, long val) {
        this.prefs.edit().putLong(key, val).apply();
    }

    public long getLong(String key, long defaultValue) {
        return this.prefs.getLong(key, defaultValue);
    }

    public void putFloat(String key, float val) {
        this.prefs.edit().putFloat(key, val).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return this.prefs.getFloat(key, defaultValue);
    }

    public void putBoolean(String key, boolean val) {
        this.prefs.edit().putBoolean(key, val).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.prefs.getBoolean(key, defaultValue);
    }

    public void putStringSet(String key, Set<String> set) {
        this.prefs.edit().putStringSet(key, set).apply();
    }

    public Set<String> getStringSet(String key, Set<String> s) {
        return this.prefs.getStringSet(key, s);
    }


}
