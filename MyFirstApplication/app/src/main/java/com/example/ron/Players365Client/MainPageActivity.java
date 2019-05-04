

package com.example.ron.Players365Client;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView;


import com.squareup.picasso.Picasso;

import com.example.ron.Players365Client.Services.JsonParser.JSONParserFunctions;
import com.example.ron.Players365Client.databinding.ActivityMainBinding;

import org.json.JSONArray;
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
import java.util.ArrayList; // import the ArrayList class
import java.util.Iterator;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    SearchView s;

    ListView listView;

    ArrayAdapter<String> adapter;

    //Buttons
    private ImageButton SimpleSearch;
    private ImageButton exit;
    private ImageButton settings;
    private ImageButton advancedSearch;
    private JSONParserFunctions jsonParserFunctions;
    private ImageButton TopPlayer1Button;
    private ImageButton TopPlayer2Button;
    private ImageButton TopPlayer3Button;
    private ImageButton TopPlayer4Button;
    private ImageButton TopPlayer5Button;
    private Button myPlayer1B;
    PlayerStatistics player1;
    PlayerStatistics myPlayer1;
    PlayerStatistics myPlayer2;
    PlayerStatistics myPlayer3;
    PlayerStatistics myPlayer4;
    PlayerStatistics myPlayer5;
    //this is for dynamic list
    ArrayList<Integer> ArrayListOfMyPlayers= new ArrayList<Integer>();
    private int size;
    private static final String TAG=MainPageActivity.class.getSimpleName();


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        String url5TopPlayers1 = "https://api.myjson.com/bins/vciag";
        String url5TopPlayers="http://132.72.233.113:53121/api/players?top=5";
        String allPlayersUrl= "https://api.myjson.com/bins/rf8n4";

        //this is my device id
        String DeviceId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        String urlMyPlayers="http://132.72.233.113:53121/api/users?deviceid=";
        urlMyPlayers= urlMyPlayers+ DeviceId;

        //add the deviceid to the database
        addUser();

        //make 2 get req : my players and top 5 players.
        try {
            //run3(allPlayersUrl);
            run(url5TopPlayers1);
            run2(urlMyPlayers);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO
        // initializeMyPlayers();
        //Defining Buttons Activity
        settings = (ImageButton) findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsPage();
            }
        });

        advancedSearch = (ImageButton) findViewById(R.id.advancedSearchButton);
        advancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdvancedSearchPage();
            }
        });

        SimpleSearch = (ImageButton) findViewById(R.id.SimpleSearch);
        SimpleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSimpleSearch();
            }
        });

        exit = (ImageButton) findViewById(R.id.exitButton);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApplication();
            }
        });

        //TopPlayer1Button
        TopPlayer1Button = (ImageButton) findViewById(R.id.TopButton1);
        TopPlayer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerPage(myPlayer1.getId());
            }
        });

        //TopPlayer2Button
        TopPlayer2Button = (ImageButton) findViewById(R.id.TopButton2);
        TopPlayer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerPage(myPlayer2.getId());
            }
        });

        //TopPlayer3Button
        TopPlayer3Button = (ImageButton) findViewById(R.id.TopButton3);
        TopPlayer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerPage(myPlayer3.getId());
            }
        });

        //TopPlayer4Button
        TopPlayer4Button = (ImageButton) findViewById(R.id.TopButton4);
        TopPlayer4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerPage(myPlayer4.getId());
            }
        });

        //TopPlayer5Button
        TopPlayer5Button = (ImageButton) findViewById(R.id.TopButton5);
        TopPlayer5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerPage(myPlayer5.getId());
            }
        });

        // Preventing screen rotation on Android
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    void run(String url) throws IOException {
        //intalaize the okhttpclient
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse  = response.body().string();
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ParsingJsonFor5TopPlayers(myResponse);
                            //ParsingJsonForMyPlayers(myResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    void run2(String url) throws IOException {
        //intalaize the okhttpclient
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse  = response.body().string();
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //ParsingJsonFor5TopPlayers(myResponse);
                            ParsingJsonForMyPlayers(myResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    void run3(String url) throws IOException {
        //intalaize the okhttpclient
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse  = response.body().string();
                MainPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //ParsingJsonFor5TopPlayers(myResponse);
                            getListPlayers(myResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    private void initialize5TopPlayers(PlayerStatistics myPlayer1, PlayerStatistics myPlayer2, PlayerStatistics myPlayer3, PlayerStatistics myPlayer4, PlayerStatistics myPlayer5 ){
        activityMainBinding.setMyplayer1(myPlayer1);
        ImageButton topPlayer1 = (ImageButton)findViewById(R.id.TopButton1);
        Picasso.with(this).load(myPlayer1.urlImage).into(topPlayer1);
        activityMainBinding.setMyplayer2(myPlayer2);
        ImageButton topPlayer2 = (ImageButton)findViewById(R.id.TopButton2);
        Picasso.with(this).load(myPlayer2.urlImage).into(topPlayer2);
        activityMainBinding.setMyplayer3(myPlayer3);
        ImageButton topPlayer3 = (ImageButton)findViewById(R.id.TopButton3);
        Picasso.with(this).load(myPlayer3.urlImage).into(topPlayer3);
        activityMainBinding.setMyplayer4(myPlayer4);
        ImageButton topPlayer4 = (ImageButton)findViewById(R.id.TopButton4);
        Picasso.with(this).load(myPlayer4.urlImage).into(topPlayer4);
        activityMainBinding.setMyplayer5(myPlayer5);
        ImageButton topPlayer5 = (ImageButton)findViewById(R.id.TopButton5);
        Picasso.with(this).load(myPlayer5.urlImage).into(topPlayer5);
    }

    //moving to pages (Fuctions)
    public void openAdvancedSearchPage()  {
        startActivity(new Intent(this, AdvancedSearch.class));
    };

    public  void openSimpleSearch(){
        Intent intent=new Intent(this,SimpleSearch.class);
        Bundle mBundle = new Bundle();
        String key2="arrayList";
        mBundle.putIntegerArrayList(key2,ArrayListOfMyPlayers);
        intent.putExtras(mBundle);
        startActivity(intent);

    }

    public void openPlayerPage(int idPlayer) {
        //we want to pass the parameter id
        String id2= Integer.toString(idPlayer);
        Intent intent=new Intent(this,PlayerPageActivity.class);
        Bundle mBundle = new Bundle();
        String key="a";
        mBundle.putString(key, id2);
        String key2="arrayList";
        mBundle.putIntegerArrayList(key2,ArrayListOfMyPlayers);
        intent.putExtras(mBundle);
        startActivity(intent);
    };

    public void openSettingsPage() {
        startActivity(new Intent(this, Settings.class));
    };

    public void exitApplication(){
        finish();
        moveTaskToBack(true);
    }



    //Parsing My Players
    //is not dynamic- is only for 15 player
    public void ParsingJsonForMyPlayers(String myResponse) throws JSONException {
        JSONArray json = new JSONArray(myResponse);
        size=json.length();
        PlayerStatistics MyPlayer = new PlayerStatistics();
        LinearLayout lind = (LinearLayout)findViewById(R.id.lin);
        TableLayout t = new TableLayout(this);
        t = (TableLayout)findViewById(R.id.tableMyPlayers);
        TableRow pic = new TableRow(this);
        TableRow playerName = new TableRow(this);


        Log.d("TAG19", "TAG="+Integer.toString(size));
        t.addView(pic);
        t.addView(playerName);

        for(int i=0 ; i<size ; i++) {
            ImageButton myPlayer = new ImageButton(this);

            MyPlayer = new PlayerStatistics(((JSONObject)json.get(i)).getString("name"), (((JSONObject)json.get(i)).getInt("player_Id")),(((JSONObject)json.get(i)).getString("pic")));
            final int idplayer = (((JSONObject)json.get(i)).getInt("player_Id"));
            //add to array list of my players.
            ArrayListOfMyPlayers.add(0,idplayer);
            Picasso.with(this).load(MyPlayer.urlImage).resize(245,240).into(myPlayer);
            TextView space1 = new TextView(this);
            space1.setText(" ");
            TextView space2 = new TextView(this);
            space2.setText(" ");


            myPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPlayerPage(idplayer);
                }
            });

            TextView l = new TextView(this);
            l.setWidth(245);
            l.setHeight(160);
            l.setTextSize(13);
            l.setGravity(Gravity.CENTER);

            l.setTextColor(Color.parseColor("#FFFFFF"));

            myPlayer.setPadding(0,0,0,0);
            myPlayer.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            l.setText(MyPlayer.name);

            Log.d("TAG19", "TAG="+l.getText() );
            playerName.addView(l);
            playerName.addView(space2);
            pic.addView(myPlayer);
            pic.addView(space1);
            //playerName.addView(l);
        }
    }

    //Parsing 5 Top Players
    public void ParsingJsonFor5TopPlayers(String myResponse) throws JSONException {
        JSONArray json = new JSONArray(myResponse);
        myPlayer1 = new PlayerStatistics((((JSONObject)json.get(0)).getString("name")), ((JSONObject)json.get(0)).getInt("player_Id"), ((JSONObject)json.get(0)).getString("pic"));
        myPlayer2 = new PlayerStatistics((((JSONObject)json.get(1)).getString("name")), ((JSONObject)json.get(1)).getInt("player_Id"), ((JSONObject)json.get(1)).getString("pic"));
        myPlayer3 = new PlayerStatistics((((JSONObject)json.get(2)).getString("name")), ((JSONObject)json.get(2)).getInt("player_Id"), ((JSONObject)json.get(2)).getString("pic"));
        myPlayer4 = new PlayerStatistics((((JSONObject)json.get(3)).getString("name")), ((JSONObject)json.get(3)).getInt("player_Id"), ((JSONObject)json.get(3)).getString("pic"));
        myPlayer5 = new PlayerStatistics((((JSONObject)json.get(4)).getString("name")), ((JSONObject)json.get(4)).getInt("player_Id"), ((JSONObject)json.get(4)).getString("pic"));
        initialize5TopPlayers(myPlayer1, myPlayer2, myPlayer3, myPlayer4, myPlayer5);
    }

    //this function add the user id to the database.
    private void addUser()
    {
        // post the device id
        String DeviceId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        //send: DeviceId
        //version 1 - without json:
        OkHttpClient client = new OkHttpClient();

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, "{\"DeviceID\":\""+ DeviceId +"\"}");
        String s= "{\"DeviceID\":\""+ DeviceId +"}";

        Request request2 = new Request.Builder()
                .url("http://132.72.233.113:53121/api/Users")
                .post(body)
                .build();
        Log.d("TAG2", "TAG="+s );

        client.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG",response.body().string());
            }
        });
    }
/*
    private void getTextFromSearchView(){
        SearchView s = new SearchView(this);
        s.findViewById(R.id.SearchView);

    }
*/
    private List getListPlayers(String myResponse) throws JSONException {
        JSONArray json = new JSONArray(myResponse);
        ListView allPlayers = new ListView(this);
        List list = new ArrayList();

        int size = json.length();
        //Log.d(TAG, "TAGg="+Integer.toString(size));
        for(int i=0; i<size; i++){
            //og.d(TAG, "TAGi="+Integer.toString(i));
            PlayerStatistics player = new PlayerStatistics((((JSONObject)json.get(i)).getString("name")), ((JSONObject)json.get(i)).getInt("player_Id"));
            list.add(player);
        }
        return list;
    }


    public PlayerStatistics findUsingIterator(String name, List<PlayerStatistics> list) {
        Iterator<PlayerStatistics> iterator = list.iterator();
        while (iterator.hasNext()) {
            PlayerStatistics p = iterator.next();
            //Log.d(TAG, "TAGg="+p.getName());
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }


}

