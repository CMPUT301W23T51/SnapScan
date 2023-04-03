package com.example.SnapScan;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.widget.Button;
import android.widget.EditText;

import androidx.test.rule.ActivityTestRule;

import com.example.SnapScan.ui.login.LoginActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegisteredAccountLogInUITest {
    private Solo solo;

    //Example registered account
    private final String testEmail1 = "rachel1@gmail.com";
    private final String testPassword1 = "maiphuong64";

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setUp() throws Exception {

        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    // Test log in for registered account
    // Important: Please uninstall, reinstall the app then run the app again before running test
    @Test
    public void testLoginRegisteredAccount2() throws Exception {

        // make sure current activity is right.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // type the email into the correct field
        solo.enterText((EditText) solo.getView(R.id.editText_login_email), testEmail1);
        Assert.assertTrue(solo.waitForText(testEmail1, 1, 2000));

        // type the password into the correct field
        solo.enterText((EditText) solo.getView(R.id.editText_loginPassword), testPassword1);
        Assert.assertTrue(solo.waitForText(testPassword1, 1, 2000));

        // wait for profile screen
        solo.clickOnView((Button) solo.getView(R.id.Button_login1));
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
