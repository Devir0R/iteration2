package com.example.ron.Players365Client;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ron on 10/01/2019.
 */

public class Settings extends AppCompatActivity {

    private ImageButton homepage;
    private ImageButton advSearch;
    private ImageButton exit;
    private Button submitButton;
    private Switch sw1;
    private Switch sw2;
    private Switch sw3;
    private Switch sw4;
    private Switch sw5;
    private Switch sw6;
    private boolean notForGoal;
    private boolean notForAss ;
    private  boolean notForCS ;
    private  boolean notForRD;
    private boolean notForYC ;
    private boolean flag1;
    SettingsForNotification s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page_view);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String DeviceId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);


        String urlForSettings= "http://132.72.233.113:53121/api/NotificationsSetting?deviceID=";
        String url=urlForSettings + DeviceId;


        homepage = (ImageButton) findViewById(R.id.homePageButton);
        advSearch = (ImageButton) findViewById(R.id.advancedSearchButton);
        exit = (ImageButton) findViewById(R.id.exitButton);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApplication();
            }
        });;

        advSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdvancedSearchPage();
            }
        });

        sw1 = (Switch)findViewById(R.id.switch1);
        sw2 = (Switch)findViewById(R.id.switch2);
        sw3 = (Switch)findViewById(R.id.switch3);
        sw4 = (Switch)findViewById(R.id.switch4);
        sw5 = (Switch)findViewById(R.id.switch5);
        sw6 = (Switch)findViewById(R.id.switch6);

        submitButton = (Button)findViewById(R.id.button);


        //get req to the settings now
        getSettings(url);

        sw1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(sw1.isChecked()==true) {
                    s.setNotificationForGoal(true);
                }
                else
                {
                    s.setNotificationForGoal(false);
                }
            }
        });

        sw2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(sw2.isChecked()==true) {
                    s.setNotificationForassist(true);
                }
                else
                {
                    s.setNotificationForassist(false);
                }
            }
        });

        sw3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(sw3.isChecked()==true) {
                    s.setNotificationForCleanSheet(true);
                }
                else
                {
                    s.setNotificationForCleanSheet(false);
                }
            }
        });

        sw4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(sw4.isChecked()==true) {
                    s.setNotificationForingame(true);
                }
                else
                {
                    s.setNotificationForingame(false);
                }
            }
        });

        sw5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(sw5.isChecked()==true) {
                    s.setNotificationForRedCard(true);
                }
                else
                {
                    s.setNotificationForRedCard(false);
                }
            }
        });

        sw6.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(sw6.isChecked()==true) {
                    s.setNotificationForYellowCard(true);
                }
                else
                {
                    s.setNotificationForYellowCard(false);
                }
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // post the new settings after the change
                addSetting();
                //sw1.get
            }
        });

        // OnClickListener if we change the settings

    }

    private void openAdvancedSearchPage() {
        startActivity(new Intent(this, AdvancedSearch.class));
    };

    private void openHomePage() {
        startActivity(new Intent(this, MainPageActivity.class));
    };

    public void exitApplication(){
        finish();
        moveTaskToBack(true);
    }

    private void getSettings(String urlForSettings)
    {
        try {
            run(urlForSettings);
        } catch (IOException e) {
            //txtString.setText(e.getMessage());
            e.printStackTrace();
        }
    }


    void run(String urlForSettings) throws IOException {
        //intalaize the okhttpclient
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlForSettings)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse  = response.body().string();
                Settings.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ParsedJsonObject(myResponse);
                            setSwitch();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void ParsedJsonObject(String myResponse) throws JSONException {

        //parse
        JSONObject json = new JSONObject(myResponse );
         notForGoal = json.getBoolean("Goals");
         notForAss = json.getBoolean("Assists");
         notForCS = json.getBoolean("Clean_sheets");
         notForRD = json.getBoolean("Red_cards");
         notForYC = json.getBoolean("Yellow_cards");

         s= new SettingsForNotification(notForGoal,notForAss,false,notForCS,notForRD,notForYC);

        //Log.d("TAG1111", s.toString());
    }

    public void setSwitch()
    {
        // set the settings
        //sw1.setChecked(true) or sw1.setChecked(false);
        sw1.setChecked(s.getNotificationForGoal());
        sw2.setChecked(s.getNotificationForassist());
        sw3.setChecked(s.getNotificationForCleanSheet());
        sw4.setChecked(s.getNotificationForingame());
        sw5.setChecked(s.getNotificationForRedCard());
        sw6.setChecked(s.getNotificationForYellowCard());
    }


    public void addSetting()
    {
        // post the device id
        String DeviceId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        //send: DeviceId
        //version 1 - without json:
        OkHttpClient client = new OkHttpClient();

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
       //*********add to the body - the settings
        RequestBody body = RequestBody.create(JSON, "{}");


        Request request2 = new Request.Builder()
                .url("http://132.72.233.113:53121/api/NotificationsSetting?" + "deviceID=" + DeviceId +"&red=" + ChangeTO0or1(s.getNotificationForRedCard())+ "&yellow="+ ChangeTO0or1(s.getNotificationForYellowCard())+ "&assists="+ ChangeTO0or1(s.getNotificationForassist())+ "&goals="+ChangeTO0or1(s.getNotificationForGoal())+ "&sheets="+ChangeTO0or1(s.getNotificationForCleanSheet()) )
                .put(body)
                .build();

        client.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }

    public int ChangeTO0or1(Boolean flag)
    {
        if (flag==true)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    //the getters - for tests

    public boolean isNotForGoal() {
        return notForGoal;
    }

    public boolean isNotForAss() {
        return notForAss;
    }

    public boolean isNotForCS() {
        return notForCS;
    }

    public boolean isNotForRD() {
        return notForRD;
    }

    public boolean isNotForYC() {
        return notForYC;
    }

}
