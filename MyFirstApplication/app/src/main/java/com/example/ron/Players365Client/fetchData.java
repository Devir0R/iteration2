package com.example.ron.Players365Client;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ron on 28/03/2019.
 */

public abstract class fetchData extends AsyncTask<Void, Void, Void> {
    String data;
    String dataParsed = "";


    public fetchData() {

    }


    protected PlayerStatistics[] doInBackground() {


        PlayerStatistics[] playersArray = new PlayerStatistics[5];

        try {
            URL url = new URL("https://api.myjson.com/bins/7wnnm");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";


            while(line != null){
                line = bufferedReader.readLine();
                //The all json file in the data String
                data = data + line;
            }

            JSONArray JA = new JSONArray(data);


            for(int i=0 ; i<JA.length(); i++){
                JSONObject JO = (JSONObject) JA.get(i);
                 playersArray[i].setName(JO.getString("name"));
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return playersArray;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
