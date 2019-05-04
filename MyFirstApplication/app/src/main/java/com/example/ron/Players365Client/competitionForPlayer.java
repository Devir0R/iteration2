package com.example.ron.Players365Client;

/**
 * Created by Ron on 24/03/2019.
 */

public class competitionForPlayer {
    private String name;
    private int goals;
    private int assists;
    private int appearances;
    private int yellowcards;
    private int redcards;


    //constructor
    public competitionForPlayer(String name, int goals, int assists, int redcards, int yellowcards) {
        this.name = name;
        this.yellowcards = yellowcards;
        this.redcards = redcards;
        this.goals = goals;
        this.assists = assists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAppearances() {
        return appearances;
    }

    public void setAppearances(int appearances) {
        this.appearances = appearances;
    }

    public int getYellowcards() {
        return yellowcards;
    }

    public void setYellowcards(int yellowcards) {
        this.yellowcards = yellowcards;
    }

    public int getRedcards() {
        return redcards;
    }

    public void setRedcards(int redcards) {
        this.redcards = redcards;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }


}
