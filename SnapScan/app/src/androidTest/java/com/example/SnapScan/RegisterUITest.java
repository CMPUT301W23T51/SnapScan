package com.example.SnapScan;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.test.rule.ActivityTestRule;

import com.example.SnapScan.ui.login.LoginActivity;
import com.example.SnapScan.ui.login.RegisterActivity;
import com.robotium.solo.Solo;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class RegisterUITest {
    private Solo solo;
    //Example unregistered account
    private final String testEmail = "test1@gmail.com";
    private final String testPassword = "test641";

    //Random
    // Generate a random name
    String randomName = RandomStringUtils.randomAlphabetic(5);

    // Generate a random email
    String randomEmail = RandomStringUtils.randomAlphabetic(5) + "@" + RandomStringUtils.randomAlphabetic(5) + ".com";

    // Generate random DOB
    Random random = new Random();
    int day = random.nextInt(28) + 1;
    int month = random.nextInt(12) + 1;
    int year = random.nextInt(40) + 1980;

    String randomDateOfBirth = day + "/" + month + "/" + year;

    // Generate a random phone number
    String randomPhoneNumber = RandomStringUtils.randomNumeric(10);

    // Generate a random password
    String randomPassword = RandomStringUtils.randomAlphanumeric(8);

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setUp() throws Exception {

        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    // Test for unregistered account and register process.
    // Important: Please uninstall, reinstall the app then run the app again before running test
    @Test
    public void testLoginUnregisteredAccount() throws Exception {
        // make sure current activity is right.
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // type the email into the correct field
        solo.enterText((EditText) solo.getView(R.id.editText_login_email), testEmail);

        // type the password into the correct field
        solo.enterText((EditText) solo.getView(R.id.editText_loginPassword), testPassword);

        solo.clickOnView((Button) solo.getView(R.id.Button_login1));

        // wait for error message indicating unregistered account
        Assert.assertTrue(solo.waitForText("ErrorThere is no user record corresponding to this identifier. The user may have been deleted.", 1, 2000));

        // click on the "Haven't registered?" button
        solo.clickOnView((Button) solo.getView(R.id.button_register));

        // check if the current activity is RegisterActivity
        Assert.assertTrue(solo.waitForText("Enter your Details to Register", 1, 2000));

        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", RegisterActivity.class);

        // type the username into the correct field
        solo.clearEditText((android.widget.EditText) solo.getView(R.id.editText_name));
        solo.enterText((EditText) solo.getView(R.id.editText_name), randomName);
        Assert.assertTrue(solo.waitForText(randomName, 1, 2000));

        // type the user email into the correct field
        solo.clearEditText((android.widget.EditText) solo.getView(R.id.editText_Email));
        solo.enterText((EditText) solo.getView(R.id.editText_Email), randomEmail);
        Assert.assertTrue(solo.waitForText(randomEmail, 1, 2000));

        // pick date of birth
        solo.clickOnView(solo.getView(R.id.editText_DateOfBirth));
        solo.setDatePicker(0, year, month - 1, day);
        solo.clickOnText("OK");
        Assert.assertTrue(solo.waitForText(randomDateOfBirth, 1, 2000));

        // choose gender
        // Generate random gender
        Random gender = new Random();
        int selectedRadioButtonId;
        if (gender.nextBoolean()) {
            selectedRadioButtonId = R.id.radio_female;
        } else {
            selectedRadioButtonId = R.id.radio_male;
        }
        RadioButton radioButtonRegisterSelectGender = (RadioButton) solo.getView(selectedRadioButtonId);
        solo.clickOnView(radioButtonRegisterSelectGender);

        // type the user phone number into the correct field
        solo.clearEditText((android.widget.EditText) solo.getView(R.id.editText_PhoneNUmber));
        solo.enterText((EditText) solo.getView(R.id.editText_PhoneNUmber), randomPhoneNumber);
        Assert.assertTrue(solo.waitForText(randomPhoneNumber, 1, 2000));

        // type the user password into the correct field
        solo.clearEditText((android.widget.EditText) solo.getView(R.id.editText_password));
        solo.enterText((EditText) solo.getView(R.id.editText_password), randomPassword);
        Assert.assertTrue(solo.waitForText(randomPassword, 1, 2000));

        // type the confirmed user password into the correct field
        solo.clearEditText((android.widget.EditText) solo.getView(R.id.editText_ConfirmPassword));
        solo.enterText((EditText) solo.getView(R.id.editText_ConfirmPassword), randomPassword);
        Assert.assertTrue(solo.waitForText(randomPassword, 1, 2000));

        // click register button
        solo.clickOnView((Button) solo.getView(R.id.Register_button));

    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


}
