package com.camant.moneycrab;

import android.app.Application;
import android.content.res.Configuration;

import com.camant.moneycrab.util.FontsOverride;

import java.util.Locale;

/**
 * Created by sreng on 11/13/2016.
 */

public class MoneyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/khmer_mef2.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/khmer_mef1.ttf");*/
        //FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/battambang.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "OpenSans-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "battambang.ttf");
        String languageToLoad  = "km"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
