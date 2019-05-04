package com.example.ron.Players365Client;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.ron.Players365Client.databinding.ActivityMainBinding;
import com.example.ron.Players365Client.databinding.PlayerPageViewBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import com.squareup.picasso.Picasso;

public class ResultAdvanceSearch extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button Sort;
    private Button present;
    private Button Clear;
    private ImageButton homepage;
    private ImageButton settings;
    private ImageButton exit;
    private ImageButton advSearch;
    private String nation;
    PlayerStatistics myPlayer1;
   // PlayerStatistics myPlayer2;
    private PlayerPageViewBinding activityMainBinding;
    private ListView l;
    private int size;
    private  ArrayList<PlayerStatistics> playersResults = new ArrayList<PlayerStatistics>();
    private  ArrayList<PlayerStatistics> AllplayersResults = new ArrayList<PlayerStatistics>();
    //private  ArrayList<ImageButton> playersImageResults = new ArrayList<ImageButton>();
    private Spinner s;
    private Spinner s2;
    private int pos;
    private customAdapter c =new customAdapter();
    private int count=0;
    private boolean itsNotFirstTime=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultadvancesearch);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.resultadvancesearch);

        Bundle b2=getIntent().getExtras();
        nation=b2.getString("nation");

        String firstUrl= "http://132.72.233.113:53121/api/players?nationality=" + nation;
        String utl= "https://api.myjson.com/bins/kz084";
        String url= "https://api.myjson.com/bins/rf8n4";

        //spinner for competition
        s= (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.competition,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);
        //spinner for statistics:
        s2= (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter2= ArrayAdapter.createFromResource(this,R.array.statistics,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter2);
        s2.setOnItemSelectedListener(this);

        try {
            run(url);

        } catch (IOException e) {
            e.printStackTrace();
        }

        l= (ListView)findViewById(R.id.listview_search);
        present= (Button)findViewById(R.id.imageButton2);
        Clear=(Button)findViewById(R.id.button3);

        present.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      count++;
                      try {
                          if (count==1) {
                              for (int i2 = 0; i2 < playersResults.size(); i2++) {
                                  AllplayersResults.add(i2, playersResults.get(i2));
                              }
                              itsNotFirstTime=true;
                          }
                           if (count>1) {
                               for (int j = 0; j < AllplayersResults.size(); j++) {
                                   playersResults.add(j, AllplayersResults.get(j));
                               }
                               size = AllplayersResults.size();
                               itsNotFirstTime=true;
                           }

                          l.setAdapter(c);
                          c.notifyDataSetChanged();
/*
                          Log.d("TAG16",  "lll");
                          l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                  PlayerStatistics h = findUsingIterator((String)l.getItemAtPosition(position), playersResults);
                                  Log.d("TAG16",  h.getName());
                                  openPlayerPage(h.id);
                              }
                          });
*/
                      } catch (Exception e) {
                          Log.d("TAG16",  e.getMessage());
                      }
                   }
               });

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playersResults.clear();
                size=0;
                c.notifyDataSetChanged();
            }
        });

        Sort= (Button)findViewById(R.id.button2);
        Sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itsNotFirstTime=false;
                String competition = s.getSelectedItem().toString();
                String statisics = s2.getSelectedItem().toString();
                playersResults=SortBY(competition,statisics);

                try {
                    c.notifyDataSetChanged();


                } catch (Exception e) {
                    Log.d("TAG16",  e.getMessage());
                }
            }
        });
       // advSearch = (ImageButton) findViewById(R.id.advancedSearchButton);
      //  homepage = (ImageButton) findViewById(R.id.homePageButton);
      //  settings = (ImageButton) findViewById(R.id.settingsButton);
     //   exit = (ImageButton) findViewById(R.id.exitButton);

       // TopPlayer1Button = (Button) findViewById(R.id.button1);
      //  TopPlayer1Button.setOnClickListener(new View.OnClickListener() {
       //     @Override
      //      public void onClick(View v) {
     //           openPlayerPage(myPlayer1.getId());
     //       }
     //   });

     //   TopPlayer1Button = (Button) findViewById(R.id.button1);
     //   TopPlayer1Button.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
     //           openPlayerPage(myPlayer1.getId());
     //       }
     //   });

     //   homepage.setOnClickListener(new View.OnClickListener() {
     //       @Override
     //       public void onClick(View v) {
     //           openHomePage();
     //       }
     //   });

      //  exit.setOnClickListener(new View.OnClickListener() {
     //       @Override
     //       public void onClick(View v) {
     //           exitApplication();
      //      }
      //  });
        /*
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsPage();
            }
        });

        advSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdvancedSearchPage();
            }
        });
*/
    }

    public boolean haveThisCompetition(String Competitions,PlayerStatistics p)
    {
        boolean have= false;
        List<competitionForPlayer> competitions= p.getCompetitions();

        for(int i=0; i<competitions.size(); i++)
        {
            competitionForPlayer c = competitions.get(i);
            if(c.getName().equals(Competitions))
            {
                return true;
            }
        }
        return have;
    }

    public ArrayList<PlayerStatistics> SortBY(String competition, String statisics) {
        if(competition.equals("") && statisics.equals(""))
        {

        }
        else if (competition.equals("") && (!statisics.equals("")))
        {

            playersResults.clear();
            size=0;
        }
        else {
            //first we remove all the players that not partcipate in the competition
            int i = 0;
            while (i < playersResults.size()) {
                if (haveThisCompetition(competition, playersResults.get(i)) == false) {
                    PlayerStatistics p = playersResults.remove(i);
                    size = size - 1;
                } else {
                    i++;
                }
            }
            //sort that the string statistics;
            if (statisics.equals("TOP GOALS")) {
                //sort by goal:
                playersResults = sortByGoals(competition);
            } else if (statisics.equals("TOP ASSITS")) {
                //sort by assits:
                playersResults = sortByAssits(competition);
            } else if (statisics.equals("TOP RED CARDS")) {
                //sort by red card:
                playersResults = sortByRedCard(competition);
            } else if (statisics.equals("TOP YELLOW CARDS")) {
                //sort by yellow card
                playersResults = sortByYellowCard(competition);
            }
        }
        return playersResults;
    }


    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text= adapterView.getItemAtPosition(i).toString();
        //nationFromTheBox=text;
        //Log.d("TAG6666", "TAG="+text );
        Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class customAdapter extends BaseAdapter
    {
        public int getCount()
        {
            return size;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView( int position, View v, ViewGroup parent) {
            v= getLayoutInflater().inflate(R.layout.result_player,null);

            ImageButton i = (ImageButton)v.findViewById(R.id.productImage);
            TextView t1 = (TextView)v.findViewById(R.id.product_name);
            TextView t2 = (TextView)v.findViewById(R.id.parameter);

            Context context = parent.getContext();
            Picasso.with(context).load(playersResults.get(position).urlImage).into(i);
            t1.setText(playersResults.get(position).getName());

            int result=-1;
            if(itsNotFirstTime==false) {
                if (s2.getSelectedItem().equals("TOP GOALS")) {
                    for (int m = 0; m < playersResults.get(position).getCompetitions().size(); m++) {
                        if (playersResults.get(position).getCompetitions().get(m).getName().equals(s.getSelectedItem()) == true) {
                            result = playersResults.get(position).getCompetitions().get(m).getGoals();
                        }
                    }
                    String intToString = Integer.toString(result);
                    t2.setText(intToString);
                } else if (s2.getSelectedItem().equals("TOP ASSITS")) {
                    for (int m = 0; m < playersResults.get(position).getCompetitions().size(); m++) {
                        if (playersResults.get(position).getCompetitions().get(m).getName().equals(s.getSelectedItem()) == true) {
                            result = playersResults.get(position).getCompetitions().get(m).getAssists();
                        }
                    }
                    String intToString = Integer.toString(result);
                    t2.setText(intToString);
                } else if (s2.getSelectedItem().equals("TOP RED CARDS")) {
                    for (int m = 0; m < playersResults.get(position).getCompetitions().size(); m++) {
                        if (playersResults.get(position).getCompetitions().get(m).getName().equals(s.getSelectedItem()) == true) {
                            result = playersResults.get(position).getCompetitions().get(m).getRedcards();
                        }
                    }
                    String intToString = Integer.toString(result);
                    t2.setText(intToString);
                } else if (s2.getSelectedItem().equals("TOP YELLOW CARDS")) {
                    for (int m = 0; m < playersResults.get(position).getCompetitions().size(); m++) {
                        if (playersResults.get(position).getCompetitions().get(m).getName().equals(s.getSelectedItem()) == true) {
                            result = playersResults.get(position).getCompetitions().get(m).getYellowcards();
                        }
                    }
                    String intToString = Integer.toString(result);
                    t2.setText(intToString);
                } else {
                    String intToString = Integer.toString(playersResults.get(position).getId());
                    t2.setText(intToString);
                }
            }
            else
            {
                String intToString = Integer.toString(playersResults.get(position).getId());
                t2.setText(intToString);
            }

             pos =position;
            Log.d("TAG14",  Integer.toString(pos));
            /*
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<playersResults.size();i++)
                    {
                        Log.d("TAG15",  playersResults.get(i).getName());
                    }
                    Log.d("TAG16",  playersResults.get(position).getName());
                    openPlayerPage(playersResults.get(position).getId());
                }
            });
*/
            return v;
        }
    }

    private void openSettingsPage() {
        startActivity(new Intent(this, Settings.class));
    };

    private void openHomePage() {
        startActivity(new Intent(this, MainPageActivity.class));
    };

    public void exitApplication(){
        finish();
        moveTaskToBack(true);
    }

    private void openAdvancedSearchPage() {
        startActivity(new Intent(this, AdvancedSearch.class));
    };

    public void openPlayerPage(int idPlayer) {
        //Log.d(TAG, "TAG="+idPlayer);
        //we want to pass the parameter id
        String id2= Integer.toString(idPlayer);
        Intent intent=new Intent(this,PlayerPageActivity.class);
        Bundle mBundle = new Bundle();
        String key="a";
        mBundle.putString(key, id2);
        intent.putExtras(mBundle);
        startActivity(intent);
    };

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

                ResultAdvanceSearch.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ParsingPlayers(myResponse);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void initialize5TopPlayers(PlayerStatistics myPlayer1, PlayerStatistics myPlayer2 ){
        //activityMainBinding.setMyplayer1(myPlayer1);
        //activityMainBinding.setMyplayer2(myPlayer2);
    }
    private void ParsingPlayers(String myResponse) throws JSONException {
        JSONArray json = new JSONArray(myResponse);
        size=json.length();
        for (int i=0; i<json.length();i++ )
        {
            ArrayList < competitionForPlayer > competitions= new ArrayList<competitionForPlayer>() ;
            int SizeOf=((JSONObject)json.get(i)).getJSONArray("CompetitionStatistics").length();
            for(int j=0; j<SizeOf  ;j++) {
                String CompetitionName = ((JSONObject)((JSONObject)json.get(i)).getJSONArray("CompetitionStatistics").get(j)).getString("Competition_name");
                int Goals= ((JSONObject)((JSONObject)json.get(i)).getJSONArray("CompetitionStatistics").get(j)).getInt("Goals");
                int  Assists=((JSONObject)((JSONObject)json.get(i)).getJSONArray("CompetitionStatistics").get(j)).getInt("Assists");
                int RedCards=((JSONObject)((JSONObject)json.get(i)).getJSONArray("CompetitionStatistics").get(j)).getInt("Red_cards");
                int   YellowCards=((JSONObject)((JSONObject)json.get(i)).getJSONArray("CompetitionStatistics").get(j)).getInt("Yellow_Cards");
                competitionForPlayer cp = new competitionForPlayer(CompetitionName,Goals,Assists,RedCards,YellowCards);
                        competitions.add(cp);
            }
            myPlayer1 = new PlayerStatistics((((JSONObject)json.get(i)).getString("name")), ((JSONObject)json.get(i)).getInt("player_Id"),((JSONObject)json.get(i)).getString("pic"),competitions);
            playersResults.add(myPlayer1);
        }
    }

    public ArrayList<PlayerStatistics> sortByGoals(String competition)
    {
        int l, result, result2, j;
        result = -1;
        result2 = -1;
        for (l = 0; l < playersResults.size() - 1; l++) {
            for (int t = 0; t < playersResults.size() - l - 1; t++) {
                for (int m = 0; m < playersResults.get(t).getCompetitions().size(); m++) {
                    if (playersResults.get(t).getCompetitions().get(m).getName().equals(competition) == true) {
                        result = playersResults.get(t).getCompetitions().get(m).getGoals();
                    }
                }

                for (int m = 0; m < playersResults.get(t + 1).getCompetitions().size(); m++) {
                    if (playersResults.get(t + 1).getCompetitions().get(m).getName().equals(competition) == true) {
                        result2 = playersResults.get(t + 1).getCompetitions().get(m).getGoals();
                    }
                }

                if (result < result2) {
                    PlayerStatistics temp = playersResults.get(t);
                    playersResults.set(t, playersResults.get(t + 1));
                    playersResults.set(t + 1, temp);
                }
            }
        }
        return playersResults;
    }
/*
    private ArrayList<PlayerStatistics> sortByCleenSheet(String competition)
    {
        int l, result, result2, j;
        result = -1;
        result2 = -1;
        for (l = 0; l < playersResults.size() - 1; l++) {

            for (int t = 0; t < playersResults.size() - l - 1; t++) {

                Log.d("TAG50", playersResults.get(t).getName());

                for (int m = 0; m < playersResults.get(t).getCompetitions().size(); m++) {
                    if (playersResults.get(t).getCompetitions().get(m).getName().equals(competition) == true) {
                        Log.d("TAG80", playersResults.get(t).getCompetitions().get(m).getName());
                        result = playersResults.get(t).getCompetitions().get(m).getGoals();
                    }
                }

                for (int m = 0; m < playersResults.get(t + 1).getCompetitions().size(); m++) {
                    if (playersResults.get(t + 1).getCompetitions().get(m).getName().equals(competition) == true) {
                        Log.d("TAG80", playersResults.get(t + 1).getCompetitions().get(m).getName());
                        result2 = playersResults.get(t + 1).getCompetitions().get(m).getGoals();
                    }
                }

                if (result < result2) {
                    PlayerStatistics temp = playersResults.get(t);
                    playersResults.set(t, playersResults.get(t + 1));
                    playersResults.set(t + 1, temp);
                }
            }
        }
        return playersResults;
    }
*/
public ArrayList<PlayerStatistics> sortByRedCard(String competition)
    {
        int l, result, result2, j;
        result = -1;
        result2 = -1;
        for (l = 0; l < playersResults.size() - 1; l++) {
            for (int t = 0; t < playersResults.size() - l - 1; t++) {
                for (int m = 0; m < playersResults.get(t).getCompetitions().size(); m++) {
                    if (playersResults.get(t).getCompetitions().get(m).getName().equals(competition) == true) {
                        result = playersResults.get(t).getCompetitions().get(m).getRedcards();
                    }
                }

                for (int m = 0; m < playersResults.get(t + 1).getCompetitions().size(); m++) {
                    if (playersResults.get(t + 1).getCompetitions().get(m).getName().equals(competition) == true) {
                        result2 = playersResults.get(t + 1).getCompetitions().get(m).getRedcards();
                    }
                }

                if (result < result2) {
                    PlayerStatistics temp = playersResults.get(t);
                    playersResults.set(t, playersResults.get(t + 1));
                    playersResults.set(t + 1, temp);
                }
            }
        }
        return playersResults;
    }

    public ArrayList<PlayerStatistics> sortByYellowCard(String competition)
    {
        int l, result, result2, j;
        result = -1;
        result2 = -1;
        for (l = 0; l < playersResults.size() - 1; l++) {
            for (int t = 0; t < playersResults.size() - l - 1; t++) {
                for (int m = 0; m < playersResults.get(t).getCompetitions().size(); m++) {
                    if (playersResults.get(t).getCompetitions().get(m).getName().equals(competition) == true) {
                        result = playersResults.get(t).getCompetitions().get(m).getYellowcards();
                    }
                }

                for (int m = 0; m < playersResults.get(t + 1).getCompetitions().size(); m++) {
                    if (playersResults.get(t + 1).getCompetitions().get(m).getName().equals(competition) == true) {
                        result2 = playersResults.get(t + 1).getCompetitions().get(m).getYellowcards();
                    }
                }

                if (result < result2) {
                    PlayerStatistics temp = playersResults.get(t);
                    playersResults.set(t, playersResults.get(t + 1));
                    playersResults.set(t + 1, temp);
                }
            }
        }
        return playersResults;
    }
/*
    private ArrayList<PlayerStatistics> sortByApeerances(String competition)
    {
        int l, result, result2, j;
        result = -1;
        result2 = -1;
        for (l = 0; l < playersResults.size() - 1; l++) {

            for (int t = 0; t < playersResults.size() - l - 1; t++) {

                Log.d("TAG50", playersResults.get(t).getName());

                for (int m = 0; m < playersResults.get(t).getCompetitions().size(); m++) {
                    if (playersResults.get(t).getCompetitions().get(m).getName().equals(competition) == true) {
                        Log.d("TAG80", playersResults.get(t).getCompetitions().get(m).getName());
                        result = playersResults.get(t).getCompetitions().get(m).getGoals();
                    }
                }

                for (int m = 0; m < playersResults.get(t + 1).getCompetitions().size(); m++) {
                    if (playersResults.get(t + 1).getCompetitions().get(m).getName().equals(competition) == true) {
                        Log.d("TAG80", playersResults.get(t + 1).getCompetitions().get(m).getName());
                        result2 = playersResults.get(t + 1).getCompetitions().get(m).getGoals();
                    }
                }

                if (result < result2) {
                    PlayerStatistics temp = playersResults.get(t);
                    playersResults.set(t, playersResults.get(t + 1));
                    playersResults.set(t + 1, temp);
                }
            }
        }
        return playersResults;
    }
*/
public ArrayList<PlayerStatistics> sortByAssits(String competition)
    {
        int l, result, result2, j;
        result = -1;
        result2 = -1;
        for (l = 0; l < playersResults.size() - 1; l++) {
            for (int t = 0; t < playersResults.size() - l - 1; t++) {
                for (int m = 0; m < playersResults.get(t).getCompetitions().size(); m++) {
                    if (playersResults.get(t).getCompetitions().get(m).getName().equals(competition) == true) {
                        result = playersResults.get(t).getCompetitions().get(m).getAssists();
                    }
                }

                for (int m = 0; m < playersResults.get(t + 1).getCompetitions().size(); m++) {
                    if (playersResults.get(t + 1).getCompetitions().get(m).getName().equals(competition) == true) {
                        result2 = playersResults.get(t + 1).getCompetitions().get(m).getAssists();
                    }
                }

                if (result < result2) {
                    PlayerStatistics temp = playersResults.get(t);
                    playersResults.set(t, playersResults.get(t + 1));
                    playersResults.set(t + 1, temp);
                }
            }
        }
        return playersResults;
    }


    public void setPlayersResults(ArrayList<PlayerStatistics> playersResults2) {
        if (playersResults2.size()>0) {
            for (int i = 0; i < playersResults2.size(); i++) {
                this.playersResults.add(0,playersResults2.get(i));
            }
        }
        size=playersResults2.size();
    }

    public int getSize() {
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

    public ArrayList<PlayerStatistics> getPlayersResults() {
        return playersResults;
    }

}
