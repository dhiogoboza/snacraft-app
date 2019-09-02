package io.snacraft.game;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;

/**
 * Created by dhiogoboza on 20/06/18.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize mobile ads
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.ad_mob_id));
    }
}
