package io.snacraft.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private AdView mAdView;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        // =================== ADS =================
        mAdView = (AdView) findViewById(R.id.main_ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d("ADS", "loaded!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                mAdView.setVisibility(View.VISIBLE);
            }
        });
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.setAdUnitId(getString(R.string.ad_mod_interstitial_id));
        mInterstitialAd.loadAd(adRequest);


        // ================ WEBVIEW ===============
        mWebView = (WebView) findViewById(R.id.main_webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        //webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setNeedInitialFocus(true);
        webSettings.setUserAgentString("snacraft-app");

        mWebView.addJavascriptInterface(new WebViewJavaScriptInterface(this), "app");

        mWebView.loadUrl("http://snacraft.io");
    }

    public class WebViewJavaScriptInterface{

        private Context context;
        private boolean mMustShow = true;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context){
            this.context = context;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public void hideAds() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdView.setVisibility(View.GONE);
                }
            });
        }

        @JavascriptInterface
        public void showAds() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdView.setVisibility(View.VISIBLE);
                    if (mMustShow) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }

                        mMustShow = false;
                    } else {
                        mMustShow = true;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage(getString(R.string.close_game))
            .setCancelable(false)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    mWebView.destroy();
                    MainActivity.super.onBackPressed();
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .show();
    }
}
