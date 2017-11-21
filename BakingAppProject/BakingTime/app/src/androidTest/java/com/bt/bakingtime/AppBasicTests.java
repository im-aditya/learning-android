package com.bt.bakingtime;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bt.bakingtime.activities.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Aditya on 9/15/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AppBasicTests
{
    @Rule public ActivityTestRule<MainActivity> mMainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickCardShowsNextScreen()
    {
        onView(ViewMatchers.withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.tv_recipe_name)).check(matches(withText("Nutella Pie")));
    }

    @Test
    public void clickNextScreen()
    {
        onView(ViewMatchers.withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ll_recipe_steps_layout)).perform(scrollTo());
        onView(withId(R.id.ll_recipe_steps_layout)).perform(click());
        onView(withId(R.id.tv_recipe_name)).check(matches(withText("Nutella Pie")));
    }
}
