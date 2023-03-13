package com.example.original;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainFlowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // Run test after granting permissions as Out of app changes could interfere with espresso
    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.CAMERA");

    @Test
    public void mainFlowTest() {
        // Start at Profile Fragment
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        //TODO: changes place holder test after updates
        ViewInteraction textView = onView(
                allOf(withId(R.id.text_profile), withText("This is Profile fragment"),
                        withParent(withParent(withId(R.id.nav_host_fragment_activity_main))),
                        isDisplayed()));
        textView.check(matches(withText("This is Profile fragment")));

        // move to Map fragment
        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.navigation_map), withContentDescription("Map"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.navigation_map), withContentDescription("Map"),
                        withParent(withParent(withId(R.id.nav_view))),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));
        frameLayout = onView(
                allOf(withId(R.id.navigation_map), withContentDescription("Map"),
                        withParent(withParent(withId(R.id.nav_view))),
                        isSelected()));
        frameLayout.check(matches(isSelected()));

        ViewInteraction frameLayout2 = onView(
                allOf(withId(R.id.navigation_map), withContentDescription("Map"),
                        withParent(withParent(withId(R.id.nav_view))),
                        isDisplayed()));
        frameLayout2.check(matches(isDisplayed()));

        // Move to QR fragment
        ViewInteraction bottomNavigationItemView3 = onView(
                allOf(withId(R.id.navigation_qr), withContentDescription("QR"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView3.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(com.google.zxing.client.android.R.id.zxing_status_view), withText("Volume up to flash on"),
                        withParent(allOf(withId(com.google.zxing.client.android.R.id.zxing_barcode_scanner),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView3.check(matches(withText("Volume up to flash on")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
