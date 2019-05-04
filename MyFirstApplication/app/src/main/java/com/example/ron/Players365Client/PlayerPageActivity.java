package com.example.ron.Players365Client;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ron.Players365Client.databinding.PlayerPageViewBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.Response;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ron.Players365Client.databinding.PlayerPageViewBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayerPageActivity extends AppCompatActivity {

    private TextView name;
    private TextView name2;
    private TextView goal;
    private TextView appreances;

    private ImageButton homePage;
    private ImageButton exit;
    private ImageButton settings;
    private ImageButton advancedSearch;
    private Button folowButton;
    private PlayerStatistics playerstatistics;
    private PlayerPageViewBinding activityPlayerPageBinding;
    private Boolean followThisPlayer=false;
    String id;
    String urlImage = "-1";
    //logger
    private static final String TAG=PlayerPageActivity.class.getSimpleName();
    String idPlayer = "-1";
    ArrayList<Integer> ArrayListOfMyPlayers= new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.player_page_view);

       activityPlayerPageBinding = DataBindingUtil.setContentView(this, R.layout.player_page_view);

        name = (TextView)findViewById(R.id.player_name);
        //name2= (TextView)findViewById(R.id.FirstNameText);

        //String url ="https://api.myjson.com/bins/1gajco";


        String serial = Build.SERIAL;
        Bundle b2=getIntent().getExtras();

        id=b2.getString("a");

       // Bundle b3=getIntent().getExtras();
        ArrayListOfMyPlayers=b2.getIntegerArrayList("arrayList");
        idPlayer=id;
        /*

        if(idPlayer.equals("1")){
            url = "https://api.myjson.com/bins/zv6rs";
        }else if(idPlayer.equals("2")){
            url ="https://api.myjson.com/bins/1gajco";
        }else if(idPlayer.equals("3")){
            url = "https://api.myjson.com/bins/kdx1k";
        }else if(idPlayer.equals("4")){
            url = "https://api.myjson.com/bins/7xyi0";
        }else if(idPlayer.equals("5")){
            url = "https://api.myjson.com/bins/xjlig";
        }else if(idPlayer.equals("6")){
            url = "https://api.myjson.com/bins/nf888";
        }else if(idPlayer.equals("7")){
            url = "https://api.myjson.com/bins/jbc5k";
        }

*/
       String firstUrl= "http://132.72.233.113:53121/api/players/";
       String url =firstUrl+ id;
        String url2= "https://api.myjson.com/bins/kz084";
        //this function make a get req for player
        getPlayer(url);

        ImageView imageView = (ImageView)findViewById(R.id.imageView22);
        //Log.d(TAG, "TAAG="+urlImage);
        Picasso.with(this).load(urlImage).into(imageView);

        //Buttons Definitions
        exit = (ImageButton) findViewById(R.id.exitButton);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApplication();
            }
        });;

        settings = (ImageButton) findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsPage();
            }
        });;

        advancedSearch = (ImageButton) findViewById(R.id.advancedSearchButton);
        advancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdvancedSearchPage();
            }
        });

        homePage = (ImageButton) findViewById(R.id.homePageButton);
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        folowButton = (Button)findViewById(R.id.button8);

        //check if the player id in the followers of this player

        followThisPlayer= thePlayerInTheList();
        if(followThisPlayer)
        {
            folowButton.setText("unfollow");
            folowButton.setBackgroundColor(Color.parseColor("#d3d3d3"));
        }

        folowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we want to check if the button is "follow" or "unfollow"

                folowButton = (Button)v;
                if(folowButton.getText().toString().equalsIgnoreCase("follow"))
                {
                    follow();
                }
                else if(folowButton.getText().toString().equalsIgnoreCase("unfollow"))
                {
                    unfollow();
                }
            }
        });;
        // Preventing screen rotation on Android
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //Defining Pages Buttons Activity
    public void openActivity2() {
        startActivity(new Intent(this, MainPageActivity.class));
    };

    public void openAdvancedSearchPage()  {
        startActivity(new Intent(this, AdvancedSearch.class));
    };

    public void openSettingsPage() {
        startActivity(new Intent(this, Settings.class));
    };

    public void exitApplication(){
        finish();
        moveTaskToBack(true);
    }
//this the function when the client press on the follow button
    public void follow()
    {
        // post the device id and the player id
        String DeviceId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        //send: DeviceId,id
        //version 1 - without json:

        OkHttpClient client = new OkHttpClient();
         final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, "{\"DeviceID\":\""+ DeviceId + "\","+ "\"playerID\":" + id +"}");
        Request request2 = new Request.Builder()
                .url("http://132.72.233.113:53121/api/Subscribers")
                .post(body)
                .build();
        Log.d("TAG2", "TAG="+body );
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

        //change the button color to grey
       // Log.d("TAG8","follow");
        folowButton.setText("unfollow");
        folowButton.setBackgroundColor(Color.parseColor("#d3d3d3"));
    }

    //this the function when the client press on the unfollow button
    public void unfollow()
    {

        // delete the device id and the player id
        String DeviceId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        //send: DeviceId,id
        //version 1 - without json:
        OkHttpClient client = new OkHttpClient();
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, "{\"DeviceID\":\""+ DeviceId + "\","+ "\"playerID\":" + id +"}");
        Request request2 = new Request.Builder()
                .url("http://132.72.233.113:53121/api/Subscribers")
                .delete(body)
                .build();
        Log.d("TAG2", "TAG="+body );
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
        //change the button color to red

        folowButton.setText("follow");
        folowButton.setBackgroundColor(Color.parseColor("#a4c639"));
    }

    private void getPlayer(String url)
    {
        try {
            run(url);
        } catch (IOException e) {
            //txtString.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    void run(String url) throws IOException {
        //intalaize the okhttpclient
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse  = response.body().string();
                PlayerPageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ParsedJsonObject(myResponse);
                            ParsingCompetitions(myResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }




    private void ParsedJsonObject(String myResponse) throws JSONException {

        JSONObject json = new JSONObject(myResponse);
        //Log.d(TAG, "TAG1=");
        //Getting Age
        String DateOfBirth = json.getString("date_of_birth");
        int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.valueOf((DateOfBirth.substring(0,4)));
        String age1 =  Integer.toString(age);
        Log.d(TAG, "TAGJJJJJJ="+age);
        ImageView imageView = (ImageView) findViewById(R.id.imageView22);
        //Log.d(TAG, "TAAG="+urlImage);

        Log.d(TAG, "TAG="+json.getString("position"));

        int pos = json.getInt("position");
        String p = Integer.toString(pos);

        PlayerStatistics player = new PlayerStatistics(json.getString("name"), age , p ,json.getInt("jerseyNum"),json.getString("club") ,json.getString("nationality"));



        urlImage = json.getString("pic");
        Log.d(TAG, "TAKG="+urlImage);
        Picasso.with(this).load(urlImage).into(imageView);


        Log.d(TAG, "TAG="+json.getString("name"));
        Log.d(TAG, "TAG="+player.club);



        //After Taking ImageURL
        //PlayerStatistics player = new PlayerStatistics(json.getString("name"), age ,json.getInt("jerseyNum"),json.getString("club") ,json.getString("nationality") , json.getString("ImageURL"));


        //Log.d(TAG, "TAKG="+json.getInt("jerseyNum"));
        activityPlayerPageBinding.setPlayer(player);
        //goal.setText(json.getString("goals"));
        //appreances.setText(json.getString("appearences"));

    }

    //Parsing For Competitions
    private void ParsingCompetitions(String myResponse) throws JSONException {
        JSONObject json = new JSONObject(myResponse);
        TableLayout tableLeagueStatistics1 = null;
        TableLayout tableLeagueStatistics2 = null;
        TableLayout tableLeagueStatistics3 = null;

        /*int size = json.getJSONArray("CompetitionStatistics").length();


        String CompetitionName = (((JSONObject)(json.getJSONArray("CompetitionStatistics")).get(0)).getString("Competition_name"));
        int Goals = (((JSONObject)(json.getJSONArray("CompetitionStatistics")).get(0)).getInt("Goals"));
        int Assists = (((JSONObject)(json.getJSONArray("CompetitionStatistics")).get(0)).getInt("Assists"));
        int RedCards = (((JSONObject)(json.getJSONArray("CompetitionStatistics")).get(0)).getInt("Red_cards"));
        int YellowCards = (((JSONObject)(json.getJSONArray("CompetitionStatistics")).get(0)).getInt("Yellow_Cards"));

        competitionForPlayer com = new competitionForPlayer(CompetitionName, Goals, Assists, RedCards, YellowCards);


        activityPlayerPageBinding.setCompetition(com);

        //Log.d(TAG, "TAKG="+RED);

*/
        int size = json.getJSONArray("CompetitionStatistics").length();

        Log.d(TAG, "TAKG=" + Integer.toString(size));
        LinearLayout linleague1 = (LinearLayout) findViewById(R.id.linleague1);
        LinearLayout linleague2 = (LinearLayout) findViewById(R.id.linleague2);
        LinearLayout linleague3 = (LinearLayout) findViewById(R.id.linleague3);


        for (int i = 0; i < size; i++) {
            if (i == 0) {

                TextView t = (TextView) findViewById(R.id.textNamwLeague1);
                t.setText((((JSONObject) (json.getJSONArray("CompetitionStatistics")).get(i)).getString("Competition_name")));
                findLeagueLogoBackground((((JSONObject) (json.getJSONArray("CompetitionStatistics")).get(i)).getString("Competition_name")), linleague1);
                tableLeagueStatistics1 = (TableLayout) findViewById(R.id.tableLayout1);
                fillTableForLeague(tableLeagueStatistics1, json, i);

            } else if (i == 1) {
                TextView t = (TextView) findViewById(R.id.textNamwLeague2);
                t.setText((((JSONObject) (json.getJSONArray("CompetitionStatistics")).get(i)).getString("Competition_name")));
                findLeagueLogoBackground((((JSONObject) (json.getJSONArray("CompetitionStatistics")).get(i)).getString("Competition_name")), linleague2);
                tableLeagueStatistics2 = (TableLayout) findViewById(R.id.tableLayout2);
                fillTableForLeague(tableLeagueStatistics2, json, i);

            } else if (i == 2) {
                TextView t = (TextView) findViewById(R.id.textNamwLeague3);
                t.setText((((JSONObject) (json.getJSONArray("CompetitionStatistics")).get(i)).getString("Competition_name")));
                findLeagueLogoBackground((((JSONObject) (json.getJSONArray("CompetitionStatistics")).get(i)).getString("Competition_name")), linleague3);
                tableLeagueStatistics3 = (TableLayout) findViewById(R.id.tableLayout3);
                fillTableForLeague(tableLeagueStatistics3, json, i);
            }

        }

    }

    private void fillTableForLeague(TableLayout tableLeagueStatistics, JSONObject json, int i){
        String CompetitionName = null;
        int Goals = 0;
        int Assists = 0;
        int RedCards = 0;
        int YellowCards = 0;

        try {
            CompetitionName = (((JSONObject)(json.getJSONArray("CompetitionStatistics")).get(i)).getString("Competition_name"));
            Goals = (((JSONObject)(json.getJSONArray("CompetitionStatistics")).get(i)).getInt("Goals"));
            Assists = (((JSONObject)(json.getJSONArray("CompetitionStatistics")).get(i)).getInt("Assists"));
            RedCards = (((JSONObject)(json.getJSONArray("CompetitionStatistics")).get(i)).getInt("Red_cards"));
            YellowCards = (((JSONObject)(json.getJSONArray("CompetitionStatistics")).get(i)).getInt("Yellow_Cards"));
            //Log.d(TAG, "TAK="+CompetitionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Defining The Rows in The Table
        //TableRow LeagueNameRow = new TableRow(this);

        TableRow AppearecesRow = new TableRow(this);
        TableRow GoalsRow = new TableRow(this);
        TableRow AssistsRow = new TableRow(this);
        TableRow YellowCardsRow = new TableRow(this);
        TableRow RedCardsRow = new TableRow(this);

        //Static texts
        TextView AppearcenesTextStatic = new TextView(this);
        TextView GoalsTextStatic = new TextView(this);
        TextView AssistsTextStatic = new TextView(this);
        TextView YellowCardsTextStatic = new TextView(this);
        TextView RedCardsTextStatic = new TextView(this);

        //Inputs
        TextView LeagueNameInput = new TextView(this);
        TextView NumberOfAppearencesInput = new TextView(this);
        NumberOfAppearencesInput.setGravity(1);
        TextView GoalsInput = new TextView(this);
        GoalsInput.setGravity(1);
        TextView AssistsInput = new TextView(this);
        AssistsInput.setGravity(1);
        TextView YellowCardsInput = new TextView(this);
        YellowCardsInput.setGravity(1);
        TextView RedCardsInput = new TextView(this);
        RedCardsInput.setGravity(1);

        //LeagueNameInput
        LeagueNameInput.setText(CompetitionName);
        LeagueNameInput.setTextColor(Color.parseColor("#FFFFFF"));
        LeagueNameInput.setTextSize(35);
        //LeagueNameRow.addView(LeagueNameInput);


        //Finding Image For The League

        //Addind Static Texts
        AppearcenesTextStatic.setText("Appreances:");
        AppearcenesTextStatic.setTextSize(20);
        AppearcenesTextStatic.setTextColor(Color.parseColor("#FFFFFF"));
        GoalsTextStatic.setText("Goals:");
        GoalsTextStatic.setTextSize(20);
        GoalsTextStatic.setTextColor(Color.parseColor("#FFFFFF"));
        AssistsTextStatic.setText("Assists:");
        AssistsTextStatic.setTextSize(20);
        AssistsTextStatic.setTextColor(Color.parseColor("#FFFFFF"));
        YellowCardsTextStatic.setText("YellowCards:             ");
        YellowCardsTextStatic.setTextSize(20);
        YellowCardsTextStatic.setTextColor(Color.parseColor("#FFFFFF"));
        RedCardsTextStatic.setText("RedCards:");
        RedCardsTextStatic.setTextSize(20);
        RedCardsTextStatic.setTextColor(Color.parseColor("#FFFFFF"));



        //NumberOfAppearencesInput
        //TODO
        //NOT THE REAL Appearences
        NumberOfAppearencesInput.setText(Integer.toString(i));
        NumberOfAppearencesInput.setTextSize(20);
        NumberOfAppearencesInput.setTextColor(Color.parseColor("#FFFFFF"));
        //Number Of Goals
        GoalsInput.setText(Integer.toString(Goals));
        GoalsInput.setTextSize(20);
        GoalsInput.setTextColor(Color.parseColor("#FFFFFF"));
        //Number Of Assists
        AssistsInput.setText(Integer.toString(Assists));
        AssistsInput.setTextSize(20);
        AssistsInput.setTextColor(Color.parseColor("#FFFFFF"));
        //Yellow Cards Input
        YellowCardsInput.setText(Integer.toString(RedCards));
        YellowCardsInput.setTextSize(20);
        YellowCardsInput.setTextColor(Color.parseColor("#FFFFFF"));
        //Red Cards Input
        RedCardsInput.setText(Integer.toString(YellowCards));
        RedCardsInput.setTextSize(20);
        RedCardsInput.setTextColor(Color.parseColor("#FFFFFF"));

        //Addings Rows To Table
        //tableLeagueStatistics.addView(LeagueNameRow);
        tableLeagueStatistics.addView(AppearecesRow);
        tableLeagueStatistics.addView(GoalsRow);
        tableLeagueStatistics.addView(AssistsRow);
        tableLeagueStatistics.addView(YellowCardsRow);
        tableLeagueStatistics.addView(RedCardsRow);

        //Addings text to the table
        AppearecesRow.addView(AppearcenesTextStatic);
        AppearecesRow.addView(NumberOfAppearencesInput);

        GoalsRow.addView(GoalsTextStatic);
        GoalsRow.addView(GoalsInput);


        AssistsRow.addView(AssistsTextStatic);
        AssistsRow.addView(AssistsInput);

        YellowCardsRow.addView(YellowCardsTextStatic);
        YellowCardsRow.addView(YellowCardsInput);

        RedCardsRow.addView(RedCardsTextStatic);
        RedCardsRow.addView(RedCardsInput);
    }


    private void findLeagueLogoBackground(String CompetitionName, LinearLayout l1){
        if(CompetitionName.equals("EUROPA LEAUGE")) {
            l1.setBackgroundResource(R.drawable.europaleague);
            return;
        } else if(CompetitionName.equals("CHAMPIONS LEAUGE")) {
            l1.setBackgroundResource(R.drawable.championsleague);
            return;
        }else if(CompetitionName.equals("PREMEIR LEAUGE")) {
            l1.setBackgroundResource(R.drawable.prmierleaque);
            return;
        }else if(CompetitionName.equals("LIGA BBVA")) {
            l1.setBackgroundResource(R.drawable.laliga);
            return;
        }else if(CompetitionName.equals("COPA DEL REY")) {
            l1.setBackgroundResource(R.drawable.copadelrey);
            return;
        }else if(CompetitionName.equals("COPA ITALIA")) {
            l1.setBackgroundResource(R.drawable.italiancup);
            return;
        }else if(CompetitionName.equals("SERIA A")) {
            l1.setBackgroundResource(R.drawable.seriaa);
            return;
        }else if(CompetitionName.equals("FA CUP")) {
            l1.setBackgroundResource(R.drawable.facup);
            return;
        }else if(CompetitionName.equals("LEAUGE CUP")) {
            l1.setBackgroundResource(R.drawable.efl);
            return;
        }
        return;
    }

    public Boolean thePlayerInTheList()
    {
        boolean ans =false;

        for (int i=0;i<ArrayListOfMyPlayers.size();i++)
        {
            if(ArrayListOfMyPlayers.get(i)==Integer.parseInt(id))
            {
                return true;
            }
        }

        return ans;
    }

}
