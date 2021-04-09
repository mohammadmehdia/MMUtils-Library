package ir.alizadeh.mmui.cntrl;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import com.yariksoffice.lingver.Lingver;

import java.util.Locale;

public class MyLocaleManager {

    private static final String PREF_KEY_LANG = "locale_mgr_key_lang";
    public static final Locale LOCALE_ENGLISH = Locale.ENGLISH;
    public static final Locale LOCALE_GERMAN = Locale.GERMAN;
    public static final Locale LOCALE_PERSIAN = Locale.CANADA;

    public static void init(Application application) {
        try {
            Lingver.init(application, LOCALE_ENGLISH);
        } catch (Exception ignored){}
    }

    public static String getLanguage() {
        return Lingver.getInstance().getLanguage();
    }

    public static boolean isEnglish() {
        return LOCALE_ENGLISH.getDisplayName().equalsIgnoreCase(getLocale().getDisplayName());
    }

    private static void updateResources(Context context, Locale locale) {
        Lingver.getInstance().setLocale(context, locale);
    }

    public static Locale getLocale() {
        return Lingver.getInstance().getLocale();
    }

    public static void update(Context context) {
        Locale locale = getLocale();
        updateResources(context, locale);
        WebViewLocaleHelper.getInstance().fixWebViewLocale(context);
    }

    public static class WebViewLocaleHelper {
        private boolean isRequiredWorkaround = true;
        private static WebViewLocaleHelper instance;
        public static WebViewLocaleHelper getInstance() {
            if(instance == null) {
                instance = new WebViewLocaleHelper();
            }
            return instance;
        }
        public void fixWebViewLocale(Context context) {
            if(isRequiredWorkaround) {
                isRequiredWorkaround = false;
                new WebView(context).destroy();
                Lingver lingver = Lingver.getInstance();
                lingver.setLocale(context, lingver.getLocale());
                Log.d("WebViewLocaleHelper", "fixWebViewLocale: fixed -> " + lingver.getLocale());
            }
        }
    }

}
