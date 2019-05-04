package com.example.ron.Players365Client;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Settings s= new Settings();
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    public void ChangeTO0or1Test()
    {
        assertEquals("the function failed",1, s.ChangeTO0or1(true));
    }


}