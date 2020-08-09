package com.arifullahjan.viewstravaflyby;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kobakei.ratethisapp.RateThisApp;
import com.squareup.picasso.Picasso;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;

import okhttp3.internal.Util;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    String url;


    FirebaseAnalytics firebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        MobileAds.initialize(this, "ca-app-pub-6856406816487089~4606223374");

        firebaseAnalytics= FirebaseAnalytics.getInstance(this);


        final Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        setContentView(R.layout.webview);


        show("https://airfa-project.firebaseapp.com",false);



        if (Intent.ACTION_SEND.equals(action) && type != null) {


            Bundle params = new Bundle();
            params.putString("opened_flyby", "");
            firebaseAnalytics.logEvent("opened_flyby_by_share", params);



            setContentView(R.layout.webview);

            if ("text/plain".equals(type)) {
                handleText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleText(intent); // Handle single image being sent
            }
        }
        else if (Intent.ACTION_VIEW.equals(action) ) {

            Bundle params = new Bundle();
            params.putString("opened_flyby", "");
            firebaseAnalytics.logEvent("opened_flyby_by_share", params);


            setContentView(R.layout.webview);

            Intent appLinkIntent = getIntent();
            String appLinkAction = appLinkIntent.getAction();
            Uri appLinkData = appLinkIntent.getData();
            String id = appLinkData.getLastPathSegment();
            show("https://airfa-project.firebaseapp.com/flyby/"+id,false);

        }
// else {
//
//
//
//            Bundle params = new Bundle();
//            params.putString("opened_without_flyby", "");
//            firebaseAnalytics.logEvent("opened_without_flyby", params);
//
//
//            setContentView(R.layout.activity_main);
//            ((TextView)findViewById(R.id.how_to_use)).setTypeface(EasyFonts.caviarDreams(this));
//
//            ((TextView)findViewById(R.id.step_1)).setTypeface(EasyFonts.caviarDreams(this));
//            ((TextView)findViewById(R.id.step_2)).setTypeface(EasyFonts.caviarDreams(this));
//            ((TextView)findViewById(R.id.other_apps)).setTypeface(EasyFonts.caviarDreams(this));
//
//
//
//
//
//
//            ((LinearLayout)findViewById(R.id.btn_follow_strava)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle params = new Bundle();
//                    params.putString("follow_on_strava", "");
//                    firebaseAnalytics.logEvent("follow_on_strava", params);
//
//
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse("https://www.strava.com/athletes/20769265"));
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
//                }
//            });
//
//
//
//
//            ((LinearLayout)findViewById(R.id.btn_follow_insta)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Bundle params = new Bundle();
//                    params.putString("follow_on_insta", "");
//                    firebaseAnalytics.logEvent("follow_on_insta", params);
//
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse("https://www.instagram.com/jan.arifullah/"));
//                    startActivity(i);
//                }
//            });
//
//
//            ((Button)findViewById(R.id.rate_this_app)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Bundle params = new Bundle();
//                    params.putString("rate_app", "");
//                    firebaseAnalytics.logEvent("rate_app", params);
//
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse("market://details?id=" + getPackageName()));
//                    startActivity(i);
//                }
//            });
//            ((TextView)findViewById(R.id.other_apps)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Bundle params = new Bundle();
//                    params.putString("other_apps", "");
//                    firebaseAnalytics.logEvent("other_apps", params);
//
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Jan+Marwat"));
//                    startActivity(i);
//
//                }
//            });
//        }



        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);




//1167305026




    }

    void handleText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            List<String> urls = Utils.extractUrls(sharedText);
            String activityDeepLink = urls.size()>0 ? urls.get(0):null;
            if(activityDeepLink!= null){
                List<String> ids = Utils.extractLong(activityDeepLink);
                show("https://airfa-project.firebaseapp.com/flyby/"+ids.get(0),false);
            }
        }


    }
    final Activity activity = this;
    void show(String url, final boolean traceDeepLink){
        Toast.makeText(getApplicationContext(),url,Toast.LENGTH_SHORT).show();
        Log.d(TAG,url);
        try{

            final WebView myWebView = (WebView) findViewById(R.id.web_view);
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setSupportZoom(true);
            webSettings.setDefaultTextEncodingName("utf-8");




            try{
                myWebView.setWebChromeClient(new WebChromeClient(){

                    public void onProgressChanged(WebView view, int progress)
                    {

                        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                        ((ProgressBar)findViewById(R.id.progressBar)).setProgress(progress);
                        Log.d(TAG,"progress: "+progress);

                        if(progress ==100 && traceDeepLink){

                            String activityURL = myWebView.getUrl();
                            Toast.makeText(getApplicationContext(),activityURL,Toast.LENGTH_LONG).show();
                            List<String> ids = Utils.extractLong(activityURL);
                            String id = ids.size()>0 ? ids.get(0) : null;

                            show("https://airfa-project.firebaseapp.com/flyby/"+id,false);
                        }
                        if(progress == 100){
                            ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.GONE);

                        }

                    }


                });





                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    myWebView.setWebViewClient(new WebViewClient() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                            boolean shouldOpen =shouldOpenInWebView(request.getUrl().toString());
                            if(shouldOpen){
                                view.loadUrl(request.getUrl().toString());;
                            }
                            return shouldOpen;
                        }
                    });
                } else {
                    myWebView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
                        {
                            Toast.makeText(getApplicationContext(),"Sorry!",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url)
                        {
                            boolean shouldOpen =shouldOpenInWebView(url);
                           if(shouldOpen){
                               view.loadUrl(url);
                           }
                            return shouldOpen;
                        }
                    });
                }

                myWebView.loadUrl(url);



            }
            catch (Exception ignored){
                Toast.makeText(getApplicationContext(),"Sorry!",Toast.LENGTH_LONG).show();
                myWebView.loadUrl(url);
            }

        }
        catch (Exception e){
            this.showInBrowser(url);
        }





    }

    void showInCustomTabs(String url){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorStrava));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    void showInBrowser(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }


    boolean shouldOpenInWebView(String url){
        Log.d(TAG,"ShouldOpen: "+ url);
        if(url.contains("https://www.strava.com/publishes")){
            return false;
        }
        else if(url.contains("https://www.strava.com/oauth/authorize?")){
            return true;
        }
        else if(url.contains("https://facebook.com")){
            return true;
        }
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try{
            final WebView myWebView = (WebView) findViewById(R.id.web_view);
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        if (myWebView.canGoBack()) {
                            myWebView.goBack();
                        } else {
                            finish();
                        }
                        return true;
                }
            }
        }
        catch (Exception ingnored){

        }
        return super.onKeyDown(keyCode, event);
    }
}
