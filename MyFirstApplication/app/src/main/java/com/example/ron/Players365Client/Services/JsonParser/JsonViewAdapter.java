package com.example.ron.Players365Client.Services.JsonParser;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ron.Players365Client.R;

import java.util.List;

/**
 * Created by Ron on 20/12/2018.
 */

public class JsonViewAdapter extends BaseAdapter {

    private LayoutInflater lInflater;
    private List<JsonPlayerObject> _playerList;

    public JsonViewAdapter(Context context, List<JsonPlayerObject> _playerList) {
        lInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _playerList = _playerList;
    }

    @Override

    public int getCount() {

        return _playerList.size();

    }

    @Override

    public Object getItem(int position) {

        return position;

    }

    @Override

    public long getItemId(int position) {

        return position;

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        PlayerPageViewHolder listPlayerPageViewHolder;

        if(convertView == null){

            listPlayerPageViewHolder = new PlayerPageViewHolder();

            convertView = lInflater.inflate(R.layout.player_page_view, parent, false);


            convertView.setTag(listPlayerPageViewHolder);

        }else{

            listPlayerPageViewHolder = (PlayerPageViewHolder)convertView.getTag();

        }

        listPlayerPageViewHolder.first_name.setText("Player Name: " + _playerList.get(position).getplayerName());

        listPlayerPageViewHolder.goals.setText("Player Goals: " + _playerList.get(position).getGoals());

        listPlayerPageViewHolder.apperences.setText("Player Apperences: " + _playerList.get(position).getApperences());

        return convertView;

    }
    static class PlayerPageViewHolder{

        TextView first_name;

        TextView goals;

        TextView apperences;
    }
}
