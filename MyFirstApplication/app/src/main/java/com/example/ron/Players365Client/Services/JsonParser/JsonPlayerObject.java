package com.example.ron.Players365Client.Services.JsonParser;

/**
 * Created by Ron on 20/12/2018.
 */

public class JsonPlayerObject {

    private String playerName;
    private String goals;
    private String appearences;

    public JsonPlayerObject(String playerName, String appearences, String goals) {
        this.playerName = playerName;
        this.goals = goals;
        this.appearences = appearences;
    }

    public String getplayerName() {
        return playerName;
    }

    public void setplayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getApperences() {
        return appearences;
    }

    public void setApperences(String appearences) {
        this.appearences = appearences;
    }
}