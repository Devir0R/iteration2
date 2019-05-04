package com.example.ron.Players365Client;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ResultAdvanceSearchUnitTest {

    private  ResultAdvanceSearch r =new ResultAdvanceSearch();

    @Test
    //tests for result advance search:
    public void ParsingPlayersTest()
    {


    }

    //haveThisCompetition
    //
    @Test
    //LIGA BBVA
    //we have empty list
    //the results- empty list
    public void sortByGoalsTest0()
    {
        ArrayList<PlayerStatistics> aP= new ArrayList<PlayerStatistics>();
        assertEquals("the function failed",0, aP.size());

        r.setPlayersResults(aP);
        int size= r.getSize();
        assertEquals("the function failed",0, size);
        //check the size
        ArrayList<PlayerStatistics> aP2= new ArrayList<PlayerStatistics>();
        aP2 = r.SortBY("LIGA BBVA","TOP GOALS");
        size= r.getSize();
        assertEquals("the function failed",0, size);
        //check the elemnts in the list and the order of them
    }

    @Test
    //tests for result advance search:
    //tests for sort by goal:
    //liga bbva
    //the results- 4 players sorted by goals
    public void sortByGoalsTest1()
    {
        ArrayList<PlayerStatistics> aP= new ArrayList<PlayerStatistics>();
        // add player statistics
        //messi
        competitionForPlayer c1= new competitionForPlayer("CHAMPIONS LEAUGE",2,9,8,3);
        competitionForPlayer c2= new competitionForPlayer("LIGA BBVA",4,5,2,3);
        competitionForPlayer c3= new competitionForPlayer("COPA DEL REY",25,20,5,10);
        ArrayList <competitionForPlayer> cp= new  ArrayList<competitionForPlayer>();
        cp.add(0,c3);
        cp.add(0,c2);
        cp.add(0,c1);
        PlayerStatistics Messi= new PlayerStatistics(1,"Lionel Messi",11,34,"Barcelona","CF","Argentina",false,cp,"https://i.ibb.co/Qr0Phrb/messi.png");
        //mane
        competitionForPlayer c4= new competitionForPlayer("CHAMPIONS LEAUGE",7,9,4,6);
        competitionForPlayer c5= new competitionForPlayer("PREMEIR LEAUGE",10,5,2,3);
        competitionForPlayer c6= new competitionForPlayer("FA CUP",13,20,1,6);
        ArrayList <competitionForPlayer> cp2= new  ArrayList<competitionForPlayer>();
        cp2.add(0,c6);
        cp2.add(0,c5);
        cp2.add(0,c4);
        PlayerStatistics Mane= new PlayerStatistics(2,"Sadio Mane",12,29,"Liverpool","CF","Senegal",false,cp2,"https://i.ibb.co/q72L2Wn/mane.png");
        //rakitic
        competitionForPlayer c7= new competitionForPlayer("CHAMPIONS LEAUGE",5,14,11,7);
        competitionForPlayer c8= new competitionForPlayer("LIGA BBVA",11,12,2,13);
        competitionForPlayer c9= new competitionForPlayer("COPA DEL REY",5,4,7,6);
        ArrayList <competitionForPlayer> cp3= new  ArrayList<competitionForPlayer>();
        cp3.add(0,c9);
        cp3.add(0,c8);
        cp3.add(0,c7);
        PlayerStatistics Rakitic= new PlayerStatistics(5,"Ivan Rakitic",15,29,"Barcelona","CM","Croatia",false,cp3,"https://i.ibb.co/PGRVcnd/rakitic.png");
        //Ronaldo
        competitionForPlayer c10= new competitionForPlayer("CHAMPIONS LEAUGE",8,8,12,8);
        competitionForPlayer c11= new competitionForPlayer("SERIA A",9,5,5,2);
        competitionForPlayer c12= new competitionForPlayer("COPA ITALIA",12,15,2,4);
        ArrayList <competitionForPlayer> cp4= new  ArrayList<competitionForPlayer>();
        cp4.add(0,c12);
        cp4.add(0,c11);
        cp4.add(0,c10);
        PlayerStatistics Ronaldo= new PlayerStatistics(3,"Cristiano Ronald",7,34,"Juventus","CF","Portugal",false,cp4,"https://i.ibb.co/d7qsH03/ronaldo3.png");
        //dybala
        competitionForPlayer c13= new competitionForPlayer("CHAMPIONS LEAUGE",5,14,11,7);
        competitionForPlayer c14= new competitionForPlayer("SERIA A",13,1,1,4);
        competitionForPlayer c15= new competitionForPlayer("COPA ITALIA",11,11,1,1);
        ArrayList <competitionForPlayer> cp5= new  ArrayList<competitionForPlayer>();
        cp5.add(0,c15);
        cp5.add(0,c14);
        cp5.add(0,c13);
        PlayerStatistics dybala= new PlayerStatistics(4,"Paulo Dybala",10,27,"Juventus","CF","Italia",false,cp5,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //marcelo
        competitionForPlayer c16= new competitionForPlayer("CHAMPIONS LEAUGE",7,6,15,16);
        competitionForPlayer c17= new competitionForPlayer("LIGA BBVA",15,15,13,15);
        competitionForPlayer c18= new competitionForPlayer("COPA DEL REY",4,5,9,13);
        ArrayList <competitionForPlayer> cp6= new  ArrayList<competitionForPlayer>();
        cp6.add(0,c18);
        cp6.add(0,c17);
        cp6.add(0,c16);
        PlayerStatistics Marcelo= new PlayerStatistics(6,"Marcelo",4,27,"Real Madrid","LB","Brazil",false,cp6,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //Modric
        competitionForPlayer c19= new competitionForPlayer("CHAMPIONS LEAUGE",9,9,14,14);
        competitionForPlayer c20= new competitionForPlayer("LIGA BBVA",9,15,8,7);
        competitionForPlayer c21= new competitionForPlayer("COPA DEL REY",4,9,7,12);
        ArrayList <competitionForPlayer> cp7= new  ArrayList<competitionForPlayer>();
        cp7.add(0,c21);
        cp7.add(0,c20);
        cp7.add(0,c19);

        PlayerStatistics Modric= new PlayerStatistics(7,"Luka Modric",6,27,"Real Madrid","CM","Croatia",false,cp7,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //add players to the list
        aP.add(Messi);
        aP.add(Mane);
        aP.add(Rakitic);
        aP.add(Ronaldo);
        aP.add(dybala);
        aP.add(Marcelo);
        aP.add(Modric);

        assertEquals("the function failed",7, aP.size());

        r.setPlayersResults(aP);
        int size= r.getSize();
        assertEquals("the function failed",7, size);
        //check the size
        ArrayList<PlayerStatistics> aP2= new ArrayList<PlayerStatistics>();
        aP2 = r.SortBY("LIGA BBVA","TOP GOALS");
        size= r.getSize();

        assertEquals("the function failed",4, size);
        //check the elemnts in the list and the order of them
        for(int i=0; i<aP2.size();i++) {
            if(i==3) {
                assertEquals("the function failed", "Lionel Messi", aP2.get(i).getName());
            }
            else if(i==0) {
                assertEquals("the function failed", "Marcelo", aP2.get(i).getName());
            }
            else if(i==2) {
                assertEquals("the function failed", "Luka Modric", aP2.get(i).getName());
            }
            else if(i==1) {
                assertEquals("the function failed", "Ivan Rakitic", aP2.get(i).getName());
            }
        }
    }

    @Test
    //EUROPA LEAUGE
    //the results- empty list
    public void sortByGoalsTest2()
    {
        ArrayList<PlayerStatistics> aP= new ArrayList<PlayerStatistics>();
        // add player statistics
        //messi
        competitionForPlayer c1= new competitionForPlayer("CHAMPIONS LEAUGE",2,9,8,3);
        competitionForPlayer c2= new competitionForPlayer("LIGA BBVA",4,5,2,3);
        competitionForPlayer c3= new competitionForPlayer("COPA DEL REY",25,20,5,10);
        ArrayList <competitionForPlayer> cp= new  ArrayList<competitionForPlayer>();
        cp.add(0,c3);
        cp.add(0,c2);
        cp.add(0,c1);
        PlayerStatistics Messi= new PlayerStatistics(1,"Lionel Messi",11,34,"Barcelona","CF","Argentina",false,cp,"https://i.ibb.co/Qr0Phrb/messi.png");
        //mane
        competitionForPlayer c4= new competitionForPlayer("CHAMPIONS LEAUGE",7,9,4,6);
        competitionForPlayer c5= new competitionForPlayer("PREMEIR LEAUGE",10,5,2,3);
        competitionForPlayer c6= new competitionForPlayer("FA CUP",13,20,1,6);
        ArrayList <competitionForPlayer> cp2= new  ArrayList<competitionForPlayer>();
        cp2.add(0,c6);
        cp2.add(0,c5);
        cp2.add(0,c4);
        PlayerStatistics Mane= new PlayerStatistics(2,"Sadio Mane",12,29,"Liverpool","CF","Senegal",false,cp2,"https://i.ibb.co/q72L2Wn/mane.png");
        //rakitic
        competitionForPlayer c7= new competitionForPlayer("CHAMPIONS LEAUGE",5,14,11,7);
        competitionForPlayer c8= new competitionForPlayer("LIGA BBVA",11,12,2,13);
        competitionForPlayer c9= new competitionForPlayer("COPA DEL REY",5,4,7,6);
        ArrayList <competitionForPlayer> cp3= new  ArrayList<competitionForPlayer>();
        cp3.add(0,c9);
        cp3.add(0,c8);
        cp3.add(0,c7);
        PlayerStatistics Rakitic= new PlayerStatistics(5,"Ivan Rakitic",15,29,"Barcelona","CM","Croatia",false,cp3,"https://i.ibb.co/PGRVcnd/rakitic.png");
        //Ronaldo
        competitionForPlayer c10= new competitionForPlayer("CHAMPIONS LEAUGE",8,8,12,8);
        competitionForPlayer c11= new competitionForPlayer("SERIA A",9,5,5,2);
        competitionForPlayer c12= new competitionForPlayer("COPA ITALIA",12,15,2,4);
        ArrayList <competitionForPlayer> cp4= new  ArrayList<competitionForPlayer>();
        cp4.add(0,c12);
        cp4.add(0,c11);
        cp4.add(0,c10);
        PlayerStatistics Ronaldo= new PlayerStatistics(3,"Cristiano Ronald",7,34,"Juventus","CF","Portugal",false,cp4,"https://i.ibb.co/d7qsH03/ronaldo3.png");
        //dybala
        competitionForPlayer c13= new competitionForPlayer("CHAMPIONS LEAUGE",5,14,11,7);
        competitionForPlayer c14= new competitionForPlayer("SERIA A",13,1,1,4);
        competitionForPlayer c15= new competitionForPlayer("COPA ITALIA",11,11,1,1);
        ArrayList <competitionForPlayer> cp5= new  ArrayList<competitionForPlayer>();
        cp5.add(0,c15);
        cp5.add(0,c14);
        cp5.add(0,c13);
        PlayerStatistics dybala= new PlayerStatistics(4,"Paulo Dybala",10,27,"Juventus","CF","Italia",false,cp5,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //marcelo
        competitionForPlayer c16= new competitionForPlayer("CHAMPIONS LEAUGE",7,6,15,16);
        competitionForPlayer c17= new competitionForPlayer("LIGA BBVA",15,15,13,15);
        competitionForPlayer c18= new competitionForPlayer("COPA DEL REY",4,5,9,13);
        ArrayList <competitionForPlayer> cp6= new  ArrayList<competitionForPlayer>();
        cp6.add(0,c18);
        cp6.add(0,c17);
        cp6.add(0,c16);
        PlayerStatistics Marcelo= new PlayerStatistics(6,"Marcelo",4,27,"Real Madrid","LB","Brazil",false,cp6,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //Modric
        competitionForPlayer c19= new competitionForPlayer("CHAMPIONS LEAUGE",9,9,14,14);
        competitionForPlayer c20= new competitionForPlayer("LIGA BBVA",9,15,8,7);
        competitionForPlayer c21= new competitionForPlayer("COPA DEL REY",4,9,7,12);
        ArrayList <competitionForPlayer> cp7= new  ArrayList<competitionForPlayer>();
        cp7.add(0,c21);
        cp7.add(0,c20);
        cp7.add(0,c19);

        PlayerStatistics Modric= new PlayerStatistics(7,"Luka Modric",6,27,"Real Madrid","CM","Croatia",false,cp7,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //add players to the list
        aP.add(Messi);
        aP.add(Mane);
        aP.add(Rakitic);
        aP.add(Ronaldo);
        aP.add(dybala);
        aP.add(Marcelo);
        aP.add(Modric);

        assertEquals("the function failed",7, aP.size());

        r.setPlayersResults(aP);
        int size= r.getSize();
        assertEquals("the function failed",7, size);
        //check the size
        ArrayList<PlayerStatistics> aP2= new ArrayList<PlayerStatistics>();
        aP2 = r.SortBY("EUROPA LEAUGE","TOP GOALS");
        size= r.getSize();
        assertEquals("the function failed",0, size);
        //check the elemnts in the list and the order of them
    }

    @Test
    //CHAMPIONS LEAUGE
    //the results-all the list sorted by goals
    public void sortByGoalsTest3()
    {
        ArrayList<PlayerStatistics> aP= new ArrayList<PlayerStatistics>();
        // add player statistics
        //messi
        competitionForPlayer c1= new competitionForPlayer("CHAMPIONS LEAUGE",2,9,8,3);
        competitionForPlayer c2= new competitionForPlayer("LIGA BBVA",4,5,2,3);
        competitionForPlayer c3= new competitionForPlayer("COPA DEL REY",25,20,5,10);
        ArrayList <competitionForPlayer> cp= new  ArrayList<competitionForPlayer>();
        cp.add(0,c3);
        cp.add(0,c2);
        cp.add(0,c1);
        PlayerStatistics Messi= new PlayerStatistics(1,"Lionel Messi",11,34,"Barcelona","CF","Argentina",false,cp,"https://i.ibb.co/Qr0Phrb/messi.png");
        //mane
        competitionForPlayer c4= new competitionForPlayer("CHAMPIONS LEAUGE",7,9,4,6);
        competitionForPlayer c5= new competitionForPlayer("PREMEIR LEAUGE",10,5,2,3);
        competitionForPlayer c6= new competitionForPlayer("FA CUP",13,20,1,6);
        ArrayList <competitionForPlayer> cp2= new  ArrayList<competitionForPlayer>();
        cp2.add(0,c6);
        cp2.add(0,c5);
        cp2.add(0,c4);
        PlayerStatistics Mane= new PlayerStatistics(2,"Sadio Mane",12,29,"Liverpool","CF","Senegal",false,cp2,"https://i.ibb.co/q72L2Wn/mane.png");
        //rakitic
        competitionForPlayer c7= new competitionForPlayer("CHAMPIONS LEAUGE",5,14,11,7);
        competitionForPlayer c8= new competitionForPlayer("LIGA BBVA",11,12,2,13);
        competitionForPlayer c9= new competitionForPlayer("COPA DEL REY",5,4,7,6);
        ArrayList <competitionForPlayer> cp3= new  ArrayList<competitionForPlayer>();
        cp3.add(0,c9);
        cp3.add(0,c8);
        cp3.add(0,c7);
        PlayerStatistics Rakitic= new PlayerStatistics(5,"Ivan Rakitic",15,29,"Barcelona","CM","Croatia",false,cp3,"https://i.ibb.co/PGRVcnd/rakitic.png");
        //Ronaldo
        competitionForPlayer c10= new competitionForPlayer("CHAMPIONS LEAUGE",8,8,12,8);
        competitionForPlayer c11= new competitionForPlayer("SERIA A",9,5,5,2);
        competitionForPlayer c12= new competitionForPlayer("COPA ITALIA",12,15,2,4);
        ArrayList <competitionForPlayer> cp4= new  ArrayList<competitionForPlayer>();
        cp4.add(0,c12);
        cp4.add(0,c11);
        cp4.add(0,c10);
        PlayerStatistics Ronaldo= new PlayerStatistics(3,"Cristiano Ronaldo",7,34,"Juventus","CF","Portugal",false,cp4,"https://i.ibb.co/d7qsH03/ronaldo3.png");
        //dybala
        competitionForPlayer c13= new competitionForPlayer("CHAMPIONS LEAUGE",5,14,11,7);
        competitionForPlayer c14= new competitionForPlayer("SERIA A",13,1,1,4);
        competitionForPlayer c15= new competitionForPlayer("COPA ITALIA",11,11,1,1);
        ArrayList <competitionForPlayer> cp5= new  ArrayList<competitionForPlayer>();
        cp5.add(0,c15);
        cp5.add(0,c14);
        cp5.add(0,c13);
        PlayerStatistics dybala= new PlayerStatistics(4,"Paulo Dybala",10,27,"Juventus","CF","Italia",false,cp5,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //marcelo
        competitionForPlayer c16= new competitionForPlayer("CHAMPIONS LEAUGE",7,6,15,16);
        competitionForPlayer c17= new competitionForPlayer("LIGA BBVA",15,15,13,15);
        competitionForPlayer c18= new competitionForPlayer("COPA DEL REY",4,5,9,13);
        ArrayList <competitionForPlayer> cp6= new  ArrayList<competitionForPlayer>();
        cp6.add(0,c18);
        cp6.add(0,c17);
        cp6.add(0,c16);
        PlayerStatistics Marcelo= new PlayerStatistics(6,"Marcelo",4,27,"Real Madrid","LB","Brazil",false,cp6,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //Modric
        competitionForPlayer c19= new competitionForPlayer("CHAMPIONS LEAUGE",9,9,14,14);
        competitionForPlayer c20= new competitionForPlayer("LIGA BBVA",9,15,8,7);
        competitionForPlayer c21= new competitionForPlayer("COPA DEL REY",4,9,7,12);
        ArrayList <competitionForPlayer> cp7= new  ArrayList<competitionForPlayer>();
        cp7.add(0,c21);
        cp7.add(0,c20);
        cp7.add(0,c19);

        PlayerStatistics Modric= new PlayerStatistics(7,"Luka Modric",6,27,"Real Madrid","CM","Croatia",false,cp7,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //add players to the list
        aP.add(Messi);
        aP.add(Marcelo);
        aP.add(Mane);
        aP.add(Ronaldo);
        aP.add(Rakitic);
        aP.add(dybala);
        aP.add(Modric);

        assertEquals("the function failed",7, aP.size());

        r.setPlayersResults(aP);
        int size= r.getSize();
        assertEquals("the function failed",7, size);
        //check the size
        ArrayList<PlayerStatistics> aP2= new ArrayList<PlayerStatistics>();
        aP2 = r.SortBY("CHAMPIONS LEAUGE","TOP GOALS");
        size= r.getSize();
        assertEquals("the function failed",7, size);
        //check the elemnts in the list and the order of them

        for(int i=0; i<aP2.size();i++) {
            if(i==6) {
                assertEquals("the function failed", "Lionel Messi", aP2.get(i).getName());
            }
            else if(i==3) {
                assertEquals("the function failed", "Marcelo", aP2.get(i).getName());
            }
            else if(i==2) {
                assertEquals("the function failed", "Sadio Mane", aP2.get(i).getName());
            }
            else if(i==4) {
                assertEquals("the function failed", "Paulo Dybala", aP2.get(i).getName());
            }
            else if(i==1) {
                assertEquals("the function failed", "Cristiano Ronaldo", aP2.get(i).getName());
            }
            else if(i==0) {
                assertEquals("the function failed", "Luka Modric", aP2.get(i).getName());
            }
            else if(i==5) {
                assertEquals("the function failed", "Ivan Rakitic", aP2.get(i).getName());
            }
        }
    }
    @Test
    //competition is empty and statistics is empty
    //the results-empty list
    public void sortByTest1()
    {
        ArrayList<PlayerStatistics> aP= new ArrayList<PlayerStatistics>();
        // add player statistics
        //messi
        competitionForPlayer c1= new competitionForPlayer("CHAMPIONS LEAUGE",2,9,8,3);
        competitionForPlayer c2= new competitionForPlayer("LIGA BBVA",4,5,2,3);
        competitionForPlayer c3= new competitionForPlayer("COPA DEL REY",25,20,5,10);
        ArrayList <competitionForPlayer> cp= new  ArrayList<competitionForPlayer>();
        cp.add(0,c3);
        cp.add(0,c2);
        cp.add(0,c1);
        PlayerStatistics Messi= new PlayerStatistics(1,"Lionel Messi",11,34,"Barcelona","CF","Argentina",false,cp,"https://i.ibb.co/Qr0Phrb/messi.png");
        //mane
        competitionForPlayer c4= new competitionForPlayer("CHAMPIONS LEAUGE",7,9,4,6);
        competitionForPlayer c5= new competitionForPlayer("PREMEIR LEAUGE",10,5,2,3);
        competitionForPlayer c6= new competitionForPlayer("FA CUP",13,20,1,6);
        ArrayList <competitionForPlayer> cp2= new  ArrayList<competitionForPlayer>();
        cp2.add(0,c6);
        cp2.add(0,c5);
        cp2.add(0,c4);
        PlayerStatistics Mane= new PlayerStatistics(2,"Sadio Mane",12,29,"Liverpool","CF","Senegal",false,cp2,"https://i.ibb.co/q72L2Wn/mane.png");
        //rakitic
        competitionForPlayer c7= new competitionForPlayer("CHAMPIONS LEAUGE",5,14,11,7);
        competitionForPlayer c8= new competitionForPlayer("LIGA BBVA",11,12,2,13);
        competitionForPlayer c9= new competitionForPlayer("COPA DEL REY",5,4,7,6);
        ArrayList <competitionForPlayer> cp3= new  ArrayList<competitionForPlayer>();
        cp3.add(0,c9);
        cp3.add(0,c8);
        cp3.add(0,c7);
        PlayerStatistics Rakitic= new PlayerStatistics(5,"Ivan Rakitic",15,29,"Barcelona","CM","Croatia",false,cp3,"https://i.ibb.co/PGRVcnd/rakitic.png");
        //Ronaldo
        competitionForPlayer c10= new competitionForPlayer("CHAMPIONS LEAUGE",8,8,12,8);
        competitionForPlayer c11= new competitionForPlayer("SERIA A",9,5,5,2);
        competitionForPlayer c12= new competitionForPlayer("COPA ITALIA",12,15,2,4);
        ArrayList <competitionForPlayer> cp4= new  ArrayList<competitionForPlayer>();
        cp4.add(0,c12);
        cp4.add(0,c11);
        cp4.add(0,c10);
        PlayerStatistics Ronaldo= new PlayerStatistics(3,"Cristiano Ronaldo",7,34,"Juventus","CF","Portugal",false,cp4,"https://i.ibb.co/d7qsH03/ronaldo3.png");
        //dybala
        competitionForPlayer c13= new competitionForPlayer("CHAMPIONS LEAUGE",5,14,11,7);
        competitionForPlayer c14= new competitionForPlayer("SERIA A",13,1,1,4);
        competitionForPlayer c15= new competitionForPlayer("COPA ITALIA",11,11,1,1);
        ArrayList <competitionForPlayer> cp5= new  ArrayList<competitionForPlayer>();
        cp5.add(0,c15);
        cp5.add(0,c14);
        cp5.add(0,c13);
        PlayerStatistics dybala= new PlayerStatistics(4,"Paulo Dybala",10,27,"Juventus","CF","Italia",false,cp5,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //marcelo
        competitionForPlayer c16= new competitionForPlayer("CHAMPIONS LEAUGE",7,6,15,16);
        competitionForPlayer c17= new competitionForPlayer("LIGA BBVA",15,15,13,15);
        competitionForPlayer c18= new competitionForPlayer("COPA DEL REY",4,5,9,13);
        ArrayList <competitionForPlayer> cp6= new  ArrayList<competitionForPlayer>();
        cp6.add(0,c18);
        cp6.add(0,c17);
        cp6.add(0,c16);
        PlayerStatistics Marcelo= new PlayerStatistics(6,"Marcelo",4,27,"Real Madrid","LB","Brazil",false,cp6,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //Modric
        competitionForPlayer c19= new competitionForPlayer("CHAMPIONS LEAUGE",9,9,14,14);
        competitionForPlayer c20= new competitionForPlayer("LIGA BBVA",9,15,8,7);
        competitionForPlayer c21= new competitionForPlayer("COPA DEL REY",4,9,7,12);
        ArrayList <competitionForPlayer> cp7= new  ArrayList<competitionForPlayer>();
        cp7.add(0,c21);
        cp7.add(0,c20);
        cp7.add(0,c19);

        PlayerStatistics Modric= new PlayerStatistics(7,"Luka Modric",6,27,"Real Madrid","CM","Croatia",false,cp7,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //add players to the list
        aP.add(Messi);
        aP.add(Marcelo);
        aP.add(Mane);
        aP.add(Ronaldo);
        aP.add(Rakitic);
        aP.add(dybala);
        aP.add(Modric);

        assertEquals("the function failed",7, aP.size());

        r.setPlayersResults(aP);
        int size= r.getSize();
        assertEquals("the function failed",7, size);
        //check the size
        ArrayList<PlayerStatistics> aP2= new ArrayList<PlayerStatistics>();
        aP2 = r.SortBY("","");
        size= r.getSize();
        assertEquals("the function failed",7, size);
        //check the elemnts in the list and the order of them
    }
    @Test
    //competition is empty and statistics is not empty
    //the results-all the list sorted by id
    public void sortByTest2()
    {
        ArrayList<PlayerStatistics> aP= new ArrayList<PlayerStatistics>();
        // add player statistics
        //messi
        competitionForPlayer c1= new competitionForPlayer("CHAMPIONS LEAUGE",2,9,8,3);
        competitionForPlayer c2= new competitionForPlayer("LIGA BBVA",4,5,2,3);
        competitionForPlayer c3= new competitionForPlayer("COPA DEL REY",25,20,5,10);
        ArrayList <competitionForPlayer> cp= new  ArrayList<competitionForPlayer>();
        cp.add(0,c3);
        cp.add(0,c2);
        cp.add(0,c1);
        PlayerStatistics Messi= new PlayerStatistics(1,"Lionel Messi",11,34,"Barcelona","CF","Argentina",false,cp,"https://i.ibb.co/Qr0Phrb/messi.png");
        //mane
        competitionForPlayer c4= new competitionForPlayer("CHAMPIONS LEAUGE",7,9,4,6);
        competitionForPlayer c5= new competitionForPlayer("PREMEIR LEAUGE",10,5,2,3);
        competitionForPlayer c6= new competitionForPlayer("FA CUP",13,20,1,6);
        ArrayList <competitionForPlayer> cp2= new  ArrayList<competitionForPlayer>();
        cp2.add(0,c6);
        cp2.add(0,c5);
        cp2.add(0,c4);
        PlayerStatistics Mane= new PlayerStatistics(2,"Sadio Mane",12,29,"Liverpool","CF","Senegal",false,cp2,"https://i.ibb.co/q72L2Wn/mane.png");
        //rakitic
        competitionForPlayer c7= new competitionForPlayer("CHAMPIONS LEAUGE",5,14,11,7);
        competitionForPlayer c8= new competitionForPlayer("LIGA BBVA",11,12,2,13);
        competitionForPlayer c9= new competitionForPlayer("COPA DEL REY",5,4,7,6);
        ArrayList <competitionForPlayer> cp3= new  ArrayList<competitionForPlayer>();
        cp3.add(0,c9);
        cp3.add(0,c8);
        cp3.add(0,c7);
        PlayerStatistics Rakitic= new PlayerStatistics(5,"Ivan Rakitic",15,29,"Barcelona","CM","Croatia",false,cp3,"https://i.ibb.co/PGRVcnd/rakitic.png");
        //Ronaldo
        competitionForPlayer c10= new competitionForPlayer("CHAMPIONS LEAUGE",8,8,12,8);
        competitionForPlayer c11= new competitionForPlayer("SERIA A",9,5,5,2);
        competitionForPlayer c12= new competitionForPlayer("COPA ITALIA",12,15,2,4);
        ArrayList <competitionForPlayer> cp4= new  ArrayList<competitionForPlayer>();
        cp4.add(0,c12);
        cp4.add(0,c11);
        cp4.add(0,c10);
        PlayerStatistics Ronaldo= new PlayerStatistics(3,"Cristiano Ronaldo",7,34,"Juventus","CF","Portugal",false,cp4,"https://i.ibb.co/d7qsH03/ronaldo3.png");
        //dybala
        competitionForPlayer c13= new competitionForPlayer("CHAMPIONS LEAUGE",5,14,11,7);
        competitionForPlayer c14= new competitionForPlayer("SERIA A",13,1,1,4);
        competitionForPlayer c15= new competitionForPlayer("COPA ITALIA",11,11,1,1);
        ArrayList <competitionForPlayer> cp5= new  ArrayList<competitionForPlayer>();
        cp5.add(0,c15);
        cp5.add(0,c14);
        cp5.add(0,c13);
        PlayerStatistics dybala= new PlayerStatistics(4,"Paulo Dybala",10,27,"Juventus","CF","Italia",false,cp5,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //marcelo
        competitionForPlayer c16= new competitionForPlayer("CHAMPIONS LEAUGE",7,6,15,16);
        competitionForPlayer c17= new competitionForPlayer("LIGA BBVA",15,15,13,15);
        competitionForPlayer c18= new competitionForPlayer("COPA DEL REY",4,5,9,13);
        ArrayList <competitionForPlayer> cp6= new  ArrayList<competitionForPlayer>();
        cp6.add(0,c18);
        cp6.add(0,c17);
        cp6.add(0,c16);
        PlayerStatistics Marcelo= new PlayerStatistics(6,"Marcelo",4,27,"Real Madrid","LB","Brazil",false,cp6,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //Modric
        competitionForPlayer c19= new competitionForPlayer("CHAMPIONS LEAUGE",9,9,14,14);
        competitionForPlayer c20= new competitionForPlayer("LIGA BBVA",9,15,8,7);
        competitionForPlayer c21= new competitionForPlayer("COPA DEL REY",4,9,7,12);
        ArrayList <competitionForPlayer> cp7= new  ArrayList<competitionForPlayer>();
        cp7.add(0,c21);
        cp7.add(0,c20);
        cp7.add(0,c19);

        PlayerStatistics Modric= new PlayerStatistics(7,"Luka Modric",6,27,"Real Madrid","CM","Croatia",false,cp7,"https://i.ibb.co/ZgW6z1t/dybala1.png");
        //add players to the list
        aP.add(Messi);
        aP.add(Marcelo);
        aP.add(Mane);
        aP.add(Ronaldo);
        aP.add(Rakitic);
        aP.add(dybala);
        aP.add(Modric);

        assertEquals("the function failed",7, aP.size());

        r.setPlayersResults(aP);
        int size= r.getSize();
        assertEquals("the function failed",7, size);
        //check the size
        ArrayList<PlayerStatistics> aP2= new ArrayList<PlayerStatistics>();
        aP2 = r.SortBY("","TOP GOALS");
        size= r.getSize();
        assertEquals("the function failed",0, size);
        //check the elemnts in the list and the order of them
    }
}
