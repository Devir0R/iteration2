package com.example.ron.Players365Client;

import org.junit.Test;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class SimpleSearchTestUnitTest {
    SimpleSearch s = new SimpleSearch();
    @Test
    public void getListPlayersTest() throws Exception {
        int size = s.getListPlayers("[{\"player_Id\":1,\"name\":\"Lionel Messi\",\"pic\":\"https://i.ibb.co/Qr0Phrb/messi.png\",\"club\":\"Barcelona\",\"jerseyNum\":11,\"nationality\":\"Argentina\",\"position\":11,\"date_of_birth\":\"1987-06-24T00:00:00\",\"injured\":false,\"suspended\":false,\"in_game\":false,\"CompetitionStatistics\":[{\"Players\":null,\"Player_id\":1,\"Red_cards\":8,\"Yellow_Cards\":3,\"Goals\":2,\"Assists\":9,\"Offsides\":44444,\"Competition_name\":\"CHAMPIONS LEAUGE\",\"Injury\":false,\"Suspension\":false},{\"Players\":null,\"Player_id\":1,\"Red_cards\":2,\"Yellow_Cards\":3,\"Goals\":4,\"Assists\":5,\"Offsides\":6,\"Competition_name\":\"LIGA BBVA\",\"Injury\":false,\"Suspension\":false},{\"Players\":null,\"Player_id\":1,\"Red_cards\":5,\"Yellow_Cards\":10,\"Goals\":25,\"Assists\":20,\"Offsides\":11,\"Competition_name\":\"COPA DEL REY\",\"Injury\":false,\"Suspension\":false}],\"Position1\":null,\"Users\":[]},{\"player_Id\":2,\"name\":\"Sadio Mane\",\"pic\":\"https://i.ibb.co/q72L2Wn/mane.png\",\"club\":\"Liverpool\",\"jerseyNum\":12,\"nationality\":\"Senegal\",\"position\":11,\"date_of_birth\":\"1987-06-24T00:00:00\",\"injured\":false,\"suspended\":false,\"in_game\":false,\"CompetitionStatistics\":[{\"Players\":null,\"Player_id\":2,\"Red_cards\":4,\"Yellow_Cards\":6,\"Goals\":7,\"Assists\":9,\"Offsides\":9,\"Competition_name\":\"CHAMPIONS LEAUGE\",\"Injury\":false,\"Suspension\":false},{\"Players\":null,\"Player_id\":2,\"Red_cards\":2,\"Yellow_Cards\":3,\"Goals\":10,\"Assists\":5,\"Offsides\":6,\"Competition_name\":\"PREMEIR LEAUGE\",\"Injury\":false,\"Suspension\":false},{\"Players\":null,\"Player_id\":2,\"Red_cards\":1,\"Yellow_Cards\":6,\"Goals\":13,\"Assists\":20,\"Offsides\":11,\"Competition_name\":\"FA CUP\",\"Injury\":false,\"Suspension\":false}],\"Position1\":null,\"Users\":[]},{\"player_Id\":3,\"name\":\"Cristiano Ronaldo\",\"pic\":\"https://i.ibb.co/d7qsH03/ronaldo3.png\",\"club\":\"Juventus\",\"jerseyNum\":13,\"nationality\":\"Portugal\",\"position\":11,\"date_of_birth\":\"1987-06-24T00:00:00\",\"injured\":false,\"suspended\":false,\"in_game\":false,\"CompetitionStatistics\":[{\"Players\":null,\"Player_id\":3,\"Red_cards\":12,\"Yellow_Cards\":8,\"Goals\":8,\"Assists\":8,\"Offsides\":6,\"Competition_name\":\"CHAMPIONS LEAUGE\",\"Injury\":false,\"Suspension\":false},{\"Players\":null,\"Player_id\":3,\"Red_cards\":5,\"Yellow_Cards\":12,\"Goals\":9,\"Assists\":5,\"Offsides\":2,\"Competition_name\":\"SERIA A\",\"Injury\":false,\"Suspension\":false},{\"Players\":null,\"Player_id\":3,\"Red_cards\":2,\"Yellow_Cards\":4,\"Goals\":12,\"Assists\":15,\"Offsides\":10,\"Competition_name\":\"COPA ITALIA\",\"Injury\":false,\"Suspension\":false}],\"Position1\":null,\"Users\":[]},{\"player_Id\":4,\"name\":\"Paulo Dybala\",\"pic\":\"https://i.ibb.co/ZgW6z1t/dybala1.png\",\"club\":\"Juventus\",\"jerseyNum\":14,\"nationality\":\"Argentina\",\"position\":11,\"date_of_birth\":\"1987-06-24T00:00:00\",\"injured\":false,\"suspended\":false,\"in_game\":false,\"CompetitionStatistics\":[{\"Players\":null,\"Player_id\":4,\"Red_cards\":11,\"Yellow_Cards\":7,\"Goals\":5,\"Assists\":14,\"Offsides\":21,\"Competition_name\":\"CHAMPIONS LEAUGE\",\"Injury\":false,\"Suspension\":false},{\"Players\":null,\"Player_id\":2,\"Red_cards\":1,\"Yellow_Cards\":4,\"Goals\":13,\"Assists\":1,\"Offsides\":5,\"Competition_name\":\"SERIA A\",\"Injury\":false,\"Suspension\":false},{\"Players\":null,\"Player_id\":4,\"Red_cards\":1,\"Yellow_Cards\":1,\"Goals\":11,\"Assists\":11,\"Offsides\":7,\"Competition_name\":\"COPA ITALIA\",\"Injury\":false,\"Suspension\":false}],\"Position1\":null,\"Users\":[]}]");
        assertEquals(size, 4);
        assertTrue(s.findUsingIterator("Lionel Messi",s.playerStatisticslist)!=null);
        assertTrue(s.findUsingIterator("Cristiano Ronaldo",s.playerStatisticslist)!=null);
        assertTrue(s.findUsingIterator("Moshe",s.playerStatisticslist)==null);
    }


    @Test
    public void findUsingIteratorTest() throws Exception {
        PlayerStatistics p1 = new PlayerStatistics("Reuven Atar");
        PlayerStatistics p2 = new PlayerStatistics("govani Roso");
        PlayerStatistics p3 = new PlayerStatistics("Alon Brumer");

        List<PlayerStatistics> myList = new ArrayList<>();

        myList.add(p1);
        myList.add(p2);
        myList.add(p3);

        assertEquals(myList.size(), 3);
        assertTrue(s.findUsingIterator("Reuven Atar", myList)!=null);
        assertTrue(s.findUsingIterator("Jovani Roso", myList)!=null);
        assertTrue(s.findUsingIterator("Amir Shelach", myList)==null);

        myList.remove(p1);
        assertEquals(myList.size(), 2);
        assertTrue(s.findUsingIterator("Reuven Atar", myList)==null);

    }

}