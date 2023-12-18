package ZeissApp.library;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class AssertionLibaries {

    SoftAssert softAssert;

    public static void assertTitle(WebElement element, String expected) {
        String actual = element.getText();
        Log.info("======== Verifing title:  " + actual + " is displayed:  " + element.isDisplayed() + " ========");
        Assert.assertEquals(actual, expected, "MisMatch in actaul vs expected!");
    }

    public static void assertTrue(WebElement element, String btnName) {

        Log.info("======== Verifing " + btnName + "/ button / title is displayed:  " + element.isDisplayed()
                + " ========");
        Assert.assertTrue(element.isDisplayed(), btnName + " button / title is not present!");
    }

    public static void assertTrue(boolean condition, String message) {
        Log.info("======== Verifying " + condition + " to be true ========");
        Assert.assertTrue(condition, message);
    }

    public static void assertElementEnable(WebElement element, String btnName) {

        Log.info(
                "======== Verifing " + btnName + "/ button / title is enabled :  " + element.isEnabled() + " ========");
        Assert.assertTrue(element.isEnabled(), btnName + " button / title is not Enabled!");
    }

    public static void assertElementDisable(WebElement element, String btnName) {

        Log.info(
                "======== Verifing " + btnName + "/ button / title is disabled :  " + element.isEnabled() + " ========");
        Assert.assertFalse(element.isEnabled(), btnName + " button / title is not Enabled!");
    }


    public static void assertElementIsSelected(WebElement element, String btnName) {

        Log.info(
                "======== Verifing " + btnName + "/ button / title is enabled :  " + element.isEnabled() + " ========");
        Assert.assertTrue(element.isSelected(), btnName + " button / title is not Enabled!");
    }

    public static int characterCount(long expectedCount, String data, String expectedData) {
        Pattern pattern = Pattern.compile(data);
        java.util.regex.Matcher matcher = pattern.matcher(expectedData);
        int count = 0;
        while (matcher.find()) {
            count++;
        }

        Assert.assertEquals(expectedCount, count);
        Log.info("Character Count of " + expectedData + " = " + count);
        return count;

    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        Set<T> set = new LinkedHashSet<>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }


    public static void assertListOfElements(List<WebElement> actual, List<WebElement> excepted, String section) {
        for (WebElement li : actual) {
            Log.info("======== Verifing list of elements in" + section + " and elements are  " + li.getText()
                    + " ========");
        }
        Assert.assertEquals(actual, excepted, "List of Elements mismatch");
    }

    public static boolean verifyDecimal(String value) {
        String regex = "^\\d*\\.\\d+|\\d+\\.\\d*\\W$";
        boolean decimalVal = value.matches(regex);
        return decimalVal;
    }

    public static void assertElementDisplay(WebElement element, Boolean expected) {
        Boolean actual = element.isDisplayed();
        Log.info("======== Verifing title:  " + actual + " is displayed:  " + element.isDisplayed() + " ========");
        Assert.assertEquals(actual, expected, "Element is not Displayed");
    }

    public static void assertStatusCode(int actual, int excepted) {
        Log.info("======== Status Code:  " + actual + " ========");
        Assert.assertEquals(actual, excepted, "Assert failed!");
    }

    public static void assertValue(Integer num1, Integer num2) {
        Assert.assertEquals(num1, num2, "The number of files do not match");
    }

    public static void assetString(String actual, String excepted) {
        Log.info("Actual value: " + actual);
        Assert.assertEquals(actual, excepted, "The String do not match");
    }

    public static void assertDashboardTitleWithCount(WebElement element, String expected) {
        String actual = element.getText();
        Log.info("======== Verifing title:  " + actual + " is displayed:  " + element.isDisplayed() + " ========");
        Assert.assertEquals(actual.substring(2), expected, "MisMatch in actaul vs expected!");
    }

    public static void assertListOfString(List<String> actual, List<String> excepted) {

        Log.info("======== Verifing list of elements in" + excepted + " and elements are  " + actual
                + " ========");
        Assert.assertEquals(actual, excepted, "List of Elements mismatch");
    }



}