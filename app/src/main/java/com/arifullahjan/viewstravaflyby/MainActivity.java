package com.arifullahjan.viewstravaflyby;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

public class MainActivity extends AppCompatActivity {

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleText(intent); // Handle single image being sent
            }
        } else {

        }



        ((TextView)findViewById(R.id.how_to_use)).setTypeface(EasyFonts.caviarDreams(this));


        ((TextView)findViewById(R.id.credits)).setTypeface(EasyFonts.caviarDreams(this));


        ((Button)findViewById(R.id.show_sample)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show("1167305026");
            }
        });

//1167305026



        ((Button)findViewById(R.id.tell_friends)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/*");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, easily view strava activity replays with this awesome app: https://play.google.com/store/apps/details?id=com.arifullahjan.viewstravaflyby");
                startActivity(Intent.createChooser(sharingIntent,"Share using"));
            }
        });


        ((Button)findViewById(R.id.rate_this_app)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(i);
            }
        });
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

        }

        show(sharedText);

    }

    void show(String id){
        url = "http://labs.strava.com/flyby/viewer/#"+id;


        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorStrava));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));


    }




}
