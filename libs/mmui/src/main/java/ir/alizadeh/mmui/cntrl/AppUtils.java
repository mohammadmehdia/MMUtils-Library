package ir.alizadeh.mmui.cntrl;

import android.app.Application;
import android.text.TextUtils;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import ir.alizadeh.mmui.R;

public class AppUtils {


    public static void init(Application application, String calligraphyFontPath) {
        initLocaleManager(application);
        initCalligraphy(calligraphyFontPath);
    }

    private static void initCalligraphy(String fontPath) {
        if (!TextUtils.isEmpty(fontPath)) {
            ViewPump.init(ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath(fontPath)
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build());
        }
    }

    private static void initLocaleManager(Application application) {
        MyLocaleManager.init(application);
    }

}
