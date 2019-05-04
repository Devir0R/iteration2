package com.example.ron.Players365Client;

import android.util.Log;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class SettingsUnitTest {

    private Settings s= new Settings();

    //tests for setting:
    //test for ChangeTO0or1 function

    @Test
    public void ChangeTO0or1Test()
    {
        assertEquals("the function failed",1, s.ChangeTO0or1(true));
    }
    @Test
    public void ChangeTO0or1Test2()
    {
        assertEquals("the function failed",0, s.ChangeTO0or1(false));
    }
    @Test
    // tests for ParsedJsonObject:
    public void ParsedJsonObjectTest() throws JSONException {
        String jsonSetting= "{'User_id':5,'Red_cards':true,'Yellow_cards':false,'Assists':false,'Goals':true,'Clean_sheets':false,'Users':null}";

            s.ParsedJsonObject(jsonSetting);

        assertEquals("the function failed",true, s.isNotForGoal());
        assertEquals("the function failed",false, s.isNotForAss());
        assertEquals("the function failed",false, s.isNotForCS());
        assertEquals("the function failed",true, s.isNotForRD());
        assertEquals("the function failed",false, s.isNotForYC());
    }

}
