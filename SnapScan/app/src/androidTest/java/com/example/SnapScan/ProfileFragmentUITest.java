package com.example.SnapScan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileFragmentUITest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    // Important: Please log in and go to profile page before running the test
    @Test
    public void testLeaderBoardButton() {
        // Make sure the user is on the profile page
        onView(withId(R.id.profile_photo)).check(matches(isDisplayed()));

        // Test LeaderBoard Button
        onView(withId(R.id.leaderboard_button)).perform(click());
        // Perform search for user name
        String usernameToSearch = "suvan5";
        onView(withId(R.id.search_edit_text)).perform(typeText(usernameToSearch), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.search_button)).perform(click());
    }

    @Test
    public void testMyQrsButton() {
        // Make sure the user is on the profile page
        onView(withId(R.id.profile_photo)).check(matches(isDisplayed()));

        // Test MY QRS Button
        onView(withId(R.id.myQr_button)).perform(click());
    }
}
