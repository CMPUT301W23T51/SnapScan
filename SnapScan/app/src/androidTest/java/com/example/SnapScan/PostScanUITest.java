package com.example.SnapScan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.example.SnapScan.ui.qr.PostScanFragment;
import com.example.SnapScan.ui.qr.QRScanFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PostScanUITest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    // Important: Please log in before running the test
    @Test
    public void testPostScan() {
        onView(withId(R.id.navigation_qr)).perform(click());

        // Wait for the Camera app to be visible and for user to scan qr.
        // Please scan a qr code and click 'OK'
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject cameraApp = device.findObject(new UiSelector().packageName("com.android.camera2"));
        cameraApp.waitForExists(10000); // Timeout in milliseconds

        // Test comment
        onView(withId(R.id.editText_qr_comment)).perform(typeText("Test comment"));
        onView(withId(R.id.editText_qr_comment)).perform(closeSoftKeyboard());

        // Test save post
        onView(withId(R.id.save_qr_button)).perform(click());
    }

}
