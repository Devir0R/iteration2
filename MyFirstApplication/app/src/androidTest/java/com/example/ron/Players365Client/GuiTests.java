package com.example.ron.Players365Client;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ScrollView;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.lang.Thread;

import java.util.Timer;


import java.util.TimerTask;
import java.util.regex.Matcher;

@RunWith(AndroidJUnit4.class)
public class GuiTests{


    @Rule
    public ActivityTestRule<MainPageActivity> m = new ActivityTestRule<>(MainPageActivity.class);

    @Test
    //Viewing One of Top Player
    public void ViewTopPlayer1DetailsTest() throws Exception{
        wait1();
        Espresso.onView(withId(R.id.TopButton2)).perform(click());
        wait3();
    }

    /*@Test
    //Viewing One of Top Player
    /*public void ViewMyPlayer1Details() throws Exception{
        wait1();
        Espresso.onView(withId(R.id.tableMyPlayers).).perform(click());
        wait1();
    }*/


    @Test
    // 1. enters to Home page
    // 2. Clicks on Simple Search Page
    // 3. Searches for Players who doesn't Exist
    // 4. There are no players at all on the result list.
    public void SearchingForPlayerWhoDoesntExistTest() throws Exception{
        Espresso.onView(withId(R.id.SimpleSearch)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.edittext)).perform(clearText());
        wait1();
        Espresso.onView(withId(R.id.edittext)).perform(typeText("Not Exist"));
        wait2();
    }




    @Test
    //The Test Searchs For in Simple Search Page for name "luka" and finds
    //"Luka Modrich" And clicks for Luka Modrich page Statistics
    public void SimpleSearchForLukaTest() throws Exception{
        Espresso.onView(withId(R.id.SimpleSearch)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.edittext)).perform(clearText());
        wait1();
        Espresso.onView(withId(R.id.edittext)).perform(typeText("Luka"));
        wait1();
        Espresso.onData(anything()).inAdapterView(allOf(withId(R.id.list_view), isCompletelyDisplayed())).atPosition(0).perform(click());
        wait3();
    }






    @Test
    //performs advanced search for CF position
    public void AdvancedSearchCFPositionsTest() throws Exception{
        wait1();
        Espresso.onView(withId(R.id.advancedSearchButton)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.spinner3)).perform(click());
        wait1();
        Espresso.onData(allOf(is(instanceOf(String.class)))).atPosition(9).perform(click());
        wait1();
        Espresso.onView(withId(R.id.button7)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.imageButton2)).perform(click());
        wait3();
    }


    @Test
    //Testing all the players in the La Liga sorted by  Goals
    public void SortLaLigaByGoalsTest() throws Exception{
        wait1();
        Espresso.onView(withId(R.id.advancedSearchButton)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.button7)).perform(click());
        wait2();
        Espresso.onView(withId(R.id.imageButton2)).perform(click());
        wait2();
        Espresso.onView(withId(R.id.spinner)).perform(click());
        wait1();
        Espresso.onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        Espresso.onView(withId(R.id.spinner1)).perform(click());
        wait2();
        Espresso.onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        wait2();
        Espresso.onView(withId(R.id.button2)).perform(click());
        wait3();
    }


    @Test
    //Testing all the players in the Champions League sorted by  Yellow carda
    public void SortChampionsLeagueByYellowCardsTest() throws Exception{
        wait1();
        Espresso.onView(withId(R.id.advancedSearchButton)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.button7)).perform(click());
        wait2();
        Espresso.onView(withId(R.id.imageButton2)).perform(click());
        wait2();
        Espresso.onView(withId(R.id.spinner)).perform(click());
        wait1();
        Espresso.onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
        Espresso.onView(withId(R.id.spinner1)).perform(click());
        wait2();
        Espresso.onData(allOf(is(instanceOf(String.class)))).atPosition(5).perform(click());
        wait2();
        Espresso.onView(withId(R.id.button2)).perform(click());
        wait3();
    }



    @Test
    // 1. enters to Lionel Messi Player Page.
    // 2. push the Button follow to follow Lionel Messi
    // 3. Push back to Main Page.
    // 4. checks if Lionel Messi was added to My Players List.
    public void AddingLionelMessiToMyPlayers() throws Exception{
        Espresso.onView(withId(R.id.SimpleSearch)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.edittext)).perform(clearText());
        wait1();
        Espresso.onView(withId(R.id.edittext)).perform(typeText("Lionel Messi"));
        wait1();
        Espresso.onData(anything()).inAdapterView(allOf(withId(R.id.list_view), isCompletelyDisplayed())).atPosition(0).perform(click());
        wait2();
        Espresso.onView(withId(R.id.button8)).perform(click());
        wait2();
        Espresso.onView(withId(R.id.homePageButton)).perform(click());
        wait3();
    }

    @Test
    // 1. enters to Cristiano Ronaldo Player Page (By Simple Search)
    // 2. Clicks the Follow button to follow Cristiano Ronaldo
    // 3. Goes back to home page
    // 4. goes back to Cristiano Ronaldo Player Page (By Simple Search) to see if Cristiano Ronaldo follow button turned to unffolow
    public void ChecksCristianoRonaldoFollowButton() throws Exception{
        Espresso.onView(withId(R.id.SimpleSearch)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.edittext)).perform(clearText());
        wait1();
        Espresso.onView(withId(R.id.edittext)).perform(typeText("Cristiano Ronaldo"));
        wait1();
        Espresso.onData(anything()).inAdapterView(allOf(withId(R.id.list_view), isCompletelyDisplayed())).atPosition(0).perform(click());
        wait1();
        Espresso.onView(withId(R.id.button8)).perform(click());
        wait2();
        Espresso.onView(withId(R.id.homePageButton)).perform(click());
        wait2();
        Espresso.onView(withId(R.id.SimpleSearch)).perform(click());
        wait2();
        Espresso.onView(withId(R.id.edittext)).perform(clearText());
        wait2();
        Espresso.onView(withId(R.id.edittext)).perform(typeText("Cristiano Ronaldo"));
        wait2();
        Espresso.onData(anything()).inAdapterView(allOf(withId(R.id.list_view), isCompletelyDisplayed())).atPosition(0).perform(click());
        wait3();
    }


    @Test
    // 1. enters to Settings page
    // 2. Turns on the switcher for goals notification
    // 3. Goes back to home page
    // 4. Goes back to Settings page for checking if switcher for goals is on
    public void TurnOnNotificationsForGoals() throws Exception{
        Espresso.onView(withId(R.id.settingsButton)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.switch1)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.button)).perform(click());
        wait1();
        Espresso.onView(withId(R.id.homePageButton)).perform(click());
        wait2();
        Espresso.onView(withId(R.id.settingsButton)).perform(click());
        wait3();
    }

    @Test
    // 1. enters to Home page
    // 2. Clicking for exit in the exit button
    // 3. Goes back to home page
    public void ClickExitButton() throws Exception{
        wait1();
        Espresso.onView(withId(R.id.exitButton)).perform(click());

    }




















    public void wait1(){
        for (int i=0; i<599999999; i++){
            //for (int j=0; j<1; j++){}
        }
    }

    public void wait2(){
        for (int i=0; i<799999999; i++){

        }
    }

    public void wait3(){
        for (int i=0; i<999999999; i++){
            for (int j=0; j<4; j++){}
        }
    }


}