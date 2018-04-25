package com.arifullahjan.viewstravaflyby;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
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

        if (Intent.ACTION_SEND.equals(action) && type != null) {


            Bundle params = new Bundle();
            params.putString("opened_flyby", "");
            firebaseAnalytics.logEvent("opened_flyby", params);



            setContentView(R.layout.webview);

            ((TextView)findViewById(R.id.strava_labs)).setTypeface(EasyFonts.caviarDreams(this));
            if ("text/plain".equals(type)) {
                handleText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleText(intent); // Handle single image being sent
            }





        } else {

            Bundle params = new Bundle();
            params.putString("opened_without_flyby", "");
            firebaseAnalytics.logEvent("opened_without_flyby", params);


            setContentView(R.layout.activity_main);
            ((TextView)findViewById(R.id.how_to_use)).setTypeface(EasyFonts.caviarDreams(this));

            ((TextView)findViewById(R.id.step_1)).setTypeface(EasyFonts.caviarDreams(this));
            ((TextView)findViewById(R.id.step_2)).setTypeface(EasyFonts.caviarDreams(this));
            ((TextView)findViewById(R.id.other_apps)).setTypeface(EasyFonts.caviarDreams(this));






            ((LinearLayout)findViewById(R.id.btn_follow_strava)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle params = new Bundle();
                    params.putString("follow_on_strava", "");
                    firebaseAnalytics.logEvent("follow_on_strava", params);


                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.strava.com/athletes/20769265"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            });




            ((LinearLayout)findViewById(R.id.btn_follow_insta)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle params = new Bundle();
                    params.putString("follow_on_insta", "");
                    firebaseAnalytics.logEvent("follow_on_insta", params);

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.instagram.com/jan.arifullah/"));
                    startActivity(i);
                }
            });


            ((Button)findViewById(R.id.rate_this_app)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle params = new Bundle();
                    params.putString("rate_app", "");
                    firebaseAnalytics.logEvent("rate_app", params);

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("market://details?id=" + getPackageName()));
                    startActivity(i);
                }
            });
            ((TextView)findViewById(R.id.other_apps)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle params = new Bundle();
                    params.putString("other_apps", "");
                    firebaseAnalytics.logEvent("other_apps", params);

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Jan+Marwat"));
                    startActivity(i);

                }
            });
        }



        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);




//1167305026




    }

    void handleText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            long temp=0;
            String arr[] = sharedText.split("/");
            for(int i=0; i<arr.length;i++){
                try{
                    temp = Long.parseLong(arr[i]);
                    break;

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            sharedText=temp+"";
            if(sharedText.equalsIgnoreCase("0")){

            }
            show(sharedText);
        }


    }
    final Activity activity = this;
    void show(String id){
        url = "https://labs.strava.com/flyby/viewer/#"+id;

        try{

            final WebView myWebView = (WebView) findViewById(R.id.web_view);
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
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

                        if(progress == 100){
                            ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.GONE);
                            // hide element by id
                            myWebView.loadUrl("javascript:(function() { " +
                                    "document.getElementById('header').style.display='none';})()");
                        }

                    }
                });

            }
            catch (Exception ignored){

            }

            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
                {
                    Toast.makeText(getApplicationContext(),"Sorry!",Toast.LENGTH_LONG).show();
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    view.loadUrl(url);
                    return true;
                }
            });

            myWebView.loadUrl(url);
        }
        catch (Exception e){
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.colorStrava));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        }





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
