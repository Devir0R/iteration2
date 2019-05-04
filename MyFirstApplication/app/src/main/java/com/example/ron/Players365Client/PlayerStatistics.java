package com.example.ron.Players365Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ron on 24/03/2019.
 */

public class PlayerStatistics {

    public int id;
    public String name;
    public int jersynum;
    public int age;
    public String club;
    public String position;
    public String nationality;
    public boolean injured;
    public ArrayList<competitionForPlayer> competitions;
    public String urlImage;

    public PlayerStatistics()
    {

    }

    public PlayerStatistics(String name) {
        this.name = name;
    }

    public PlayerStatistics(int id, String name, int jersynum, int age, String club, String position, String nationality, boolean injured, ArrayList<competitionForPlayer> competitions, String urlImage) {
        this.id = id;
        this.name = name;
        this.jersynum = jersynum;
        this.age = age;
        this.club = club;
        this.position = position;
        this.nationality = nationality;
        this.injured = injured;
        this.competitions = competitions;
        this.urlImage = urlImage;
    }

    public PlayerStatistics(String name, int id) {

        this.name = name;
        this.id=id;
    }

    public PlayerStatistics(String name,int id, String urlImage) {

        this.name = name;
        this.id=id;
        this.urlImage = urlImage;
    }

    public PlayerStatistics(String name,int id, String urlImage, ArrayList<competitionForPlayer> competitions) {
        this.competitions=competitions;
        this.name = name;
        this.id=id;
        this.urlImage = urlImage;
    }



    public PlayerStatistics(String name, String club, String nationality) {
        this.name = name;
        this.club = club;
        this.nationality = nationality;

    }

    public PlayerStatistics(String name, int age, int jersynum, String club, String nationality) {
        this.name = name;
        this.age = age;
        this.jersynum = jersynum;
        this.club = club;
        this.nationality = nationality;
    }



    public PlayerStatistics(String name, int age, String position, int jersynum, String club, String nationality) {
        this.name = name;
        this.age = age;
        this.jersynum = jersynum;
        this.club = club;
        this.nationality = nationality;
        this.position=position;
    }

    public PlayerStatistics(String name, String club, int age, String position, String nationality,int id) {
        this.name = name;
        this.club = club;
        this.age = age;
        this.position = position;
        this.nationality = nationality;
        this.id=id;

    }




    //constructor

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public boolean isInjured() {
        return injured;
    }

    public void setInjured(boolean injured) {
        this.injured = injured;
    }

    public int getId()
    {
        return id;
    }

    public void setId()
    {
        this.id=id;
    }

    public List<competitionForPlayer> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(ArrayList<competitionForPlayer> competitions) {
        this.competitions = competitions;
    }

}