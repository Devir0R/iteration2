package com.example.ron.Players365Client;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SimpleSearch extends AppCompatActivity {

    private ImageButton homepage;
    private ImageButton advSearch;
    private ImageButton settings;
    private ImageButton exit;
    private EditText editText;
    private ListView listView;

    public ArrayAdapter<String> adapter;
    public static ArrayList<String> names = new ArrayList<>();
    public static ArrayList<PlayerStatistics> playerStatisticslist = new  ArrayList<>();
    public static Integer c = 1;

    private static final String TAG=MainPageActivity.class.getSimpleName();
    ArrayList<Integer> ArrayListOfMyPlayers= new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplesearch);

        Bundle b2=getIntent().getExtras();
        ArrayListOfMyPlayers=b2.getIntegerArrayList("arrayList");

        // Bundle b3=getIntent().getExtras();
        ArrayListOfMyPlayers=b2.getIntegerArrayList("arrayList");

        editText = (EditText) findViewById(R.id.edittext);
        listView = (ListView) findViewById(R.id.list_view) ;

        names.clear();
        playerStatisticslist.clear();

        String allPlayersUrl = "http://132.72.233.113:53121/api/players";

        try {
            c = run3(allPlayersUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }


        settings = (ImageButton) findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsPage();
            }
        });


        exit = (ImageButton) findViewById(R.id.exitButton);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApplication();
            }
        });;

        homepage = (ImageButton) findViewById(R.id.homePageButton);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        advSearch = (ImageButton) findViewById(R.id.advancedSearchButtonb);
        advSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdvancedSearchPage();
            }
        });

        Log.d(TAG, "TAG1="+Integer.toString(playerStatisticslist.size()));
        //Log.d(TAG, "TAG22g="+Integer.toString(names.size()));

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String DeviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        final ListView list = (ListView) findViewById(R.id.list_view);
        EditText theFilter = (EditText) findViewById(R.id.edittext);

        //Getting All Players


        adapter = new ArrayAdapter(this, R.layout.list_item_layout, names);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(SimpleSearch.this, (CharSequence)Integer.toString(playerStatisticslist.size()), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "TAG4="+Integer.toString(playerStatisticslist.size()));
                PlayerStatistics h = findUsingIterator((String)list.getItemAtPosition(position), playerStatisticslist);
                Toast.makeText(SimpleSearch.this, (CharSequence)Integer.toString(h.getId()), Toast.LENGTH_SHORT).show();
                openPlayerPage(h.id);
            }
        });

        theFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "TAG5="+Integer.toString(playerStatisticslist.size()));

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (SimpleSearch.this).adapter.getFilter().filter(s);
                Log.d(TAG, "TAG5="+Integer.toString(playerStatisticslist.size()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    public void openSettingsPage() {
        startActivity(new Intent(this, Settings.class));
    };

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




    public void openPlayerPage(int idPlayer) {
        //Log.d(TAG, "TAG="+idPlayer);
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


    int run3(String url) throws IOException {
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
                final String myResponse = response.body().string();
                SimpleSearch.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //ParsingJsonFor5TopPlayers(myResponse);
                            c = getListPlayers(myResponse);
                            Log.d(TAG, "TAG2="+Integer.toString(playerStatisticslist.size()));

                            //Log.d(TAG, "TAG3g="+Integer.toString(c));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        Log.d(TAG, "TAG3="+Integer.toString(playerStatisticslist.size()));

        return c;

    }



    public int getListPlayers(String myResponse) throws JSONException {
        JSONArray json = new JSONArray(myResponse);
        int size = json.length();
        for (int i = 0; i < size; i++) {
            PlayerStatistics player = new PlayerStatistics((((JSONObject) json.get(i)).getString("name")), ((JSONObject) json.get(i)).getInt("player_Id"));
            playerStatisticslist.add(player);
            names.add(player.name);
        }
        return size;
    }


    public PlayerStatistics findUsingIterator(String name, List<PlayerStatistics> list) {
        Iterator<PlayerStatistics> iterator = list.iterator();
        while (iterator.hasNext()) {
            PlayerStatistics p = iterator.next();
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

}

