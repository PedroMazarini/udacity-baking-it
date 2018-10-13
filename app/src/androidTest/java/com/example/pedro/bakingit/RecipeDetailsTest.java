package com.example.pedro.bakingit;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsTest {
    @Rule public ActivityTestRule<MainActivity> mActivityTestRule
        = new ActivityTestRule<>(MainActivity.class);

    @Test
    public  void clickRecipeItem(){
        onView(withId(R.id.recycler_recipes)).perform(click());
        onView(withId(R.id.master_list_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public  void clickRecipeItemTitle(){
        onView(withId(R.id.recycler_recipes)).perform(click());
        onView(withId(R.id.recycler_ingredients)).check(matches(isDisplayed()));
    }
}
