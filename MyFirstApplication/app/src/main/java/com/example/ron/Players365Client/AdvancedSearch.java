package com.example.ron.Players365Client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Ron on 10/01/2019.
 */

public class AdvancedSearch extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageButton homepage;
    private ImageButton settings;
    private ImageButton exit;
    private Button search;
    private Spinner s;
    private Spinner age1;
    private Spinner age2;
    private Spinner nationality;
    private Spinner league;
    private Spinner team;
    private String nationFromTheBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advancedsearch);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //spinner for position
        s= (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.positions,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);
        //spinner for ages

        age1= (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter1= ArrayAdapter.createFromResource(this,R.array.ages,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age1.setAdapter(adapter1);
        age1.setOnItemSelectedListener(this);

        age2= (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> adapter2= ArrayAdapter.createFromResource(this,R.array.ages,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age2.setAdapter(adapter2);
        age2.setOnItemSelectedListener(this);

        //spinner for nation
        nationality= (Spinner) findViewById(R.id.spinner6);
        ArrayAdapter<CharSequence> adapter3= ArrayAdapter.createFromResource(this,R.array.countries_array,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationality.setAdapter(adapter3);
        nationality.setOnItemSelectedListener(this);

        //spinner for leauge
        league= (Spinner) findViewById(R.id.spinner7);
        ArrayAdapter<CharSequence> adapter4= ArrayAdapter.createFromResource(this,R.array.leuages,android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        league.setAdapter(adapter4);
        //league.setOnItemSelectedListener(this);
        league.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                String itemLeague= adapterView.getItemAtPosition(position).toString();
                ArrayAdapter<CharSequence> adapter5;
                team= (Spinner) findViewById(R.id.spinner8);

                if(itemLeague.equals("LIGA BBVA"))
                {
                    adapter5= ArrayAdapter.createFromResource(view.getContext(),R.array.bbva,android.R.layout.simple_spinner_item);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    team.setAdapter(adapter5);
                    team.setOnItemSelectedListener(this);
                }
                else if(itemLeague.equals("SERIA A"))
                {
                    adapter5= ArrayAdapter.createFromResource(view.getContext(),R.array.seriaA,android.R.layout.simple_spinner_item);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    team.setAdapter(adapter5);
                    team.setOnItemSelectedListener(this);
                }
                else if(itemLeague.equals("PREMEIR LEAUGE"))
                {
                    adapter5= ArrayAdapter.createFromResource(view.getContext(),R.array.PrLeug,android.R.layout.simple_spinner_item);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    team.setAdapter(adapter5);
                    team.setOnItemSelectedListener(this);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });

        homepage = (ImageButton) findViewById(R.id.homePageButton);
        settings = (ImageButton) findViewById(R.id.settingsButton);
        exit = (ImageButton) findViewById(R.id.exitButton);
        search=(Button) findViewById(R.id.button7);

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
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsPage();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResultSearchPage();
            }
        });
    }

    private void openResultSearchPage() {

        String nation=(String)nationality.getSelectedItem();
       // Log.d("TAG30", "TAG="+nation );
        String minAge =(String)age1.getSelectedItem();
        String maxAge =(String)age2.getSelectedItem();
        String position = (String)s.getSelectedItem();

        String league = (String)nationality.getSelectedItem();
        String team = (String)nationality.getSelectedItem();
        Intent intent=new Intent(this,ResultAdvanceSearch.class);
        Bundle mBundle = new Bundle();
        String keyForNation="nation";
        String keyForMinAge = "minage";
        String keyForMaxAge = "maxage";
        String keyForPosition= "position";
        String keyForLeague = "league";
        String keyForTeam = "team";
        mBundle.putString(keyForNation, nation);
        mBundle.putString(keyForMinAge, minAge);
        mBundle.putString(keyForMaxAge, maxAge);
        mBundle.putString(keyForPosition, position);
        mBundle.putString(keyForLeague, league);
        mBundle.putString(keyForTeam, team);
        intent.putExtras(mBundle);
        startActivity(intent);
    };

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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text= adapterView.getItemAtPosition(i).toString();
        //nationFromTheBox=text;
        Log.d("TAG6", "TAG="+text );
        Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
