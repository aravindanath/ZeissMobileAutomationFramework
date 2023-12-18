package ZeissApp.library;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.SessionId;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.time.Duration.ofSeconds;

public class Generic {

    public static void navigateBack(WebDriver driver) {
        if (isIos(driver))
            return;
        driver.navigate().back();
    }

    public static void activateIOSApp(WebDriver driver, String bundleId) {
        System.out.println("=== Activating IOS App with bundleId " + bundleId + "===");
        ((IOSDriver) driver).activateApp(bundleId);
    }
    public static void switchBtApps(WebDriver driver, String bundleId) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("bundleId", bundleId);
        ((IOSDriver) driver).executeScript("mobile: launchApp", args);
    }
    public static String getOtpFromNotificationAdv(String smsContent) {
        String smsText = smsContent;
        System.out.println("==================== Sms Text: " + smsText + "====================");
        for (String str : smsText.replace(".", "").split(" ")) {
            if (str.matches("\\d{6}")) {
                return str;
            }
        }
        return "fb";
    }
    public static boolean isAppInstalled(WebDriver driver, String pkgBundle) {
        return ((AppiumDriver) driver).isAppInstalled(pkgBundle);
    }
    public static String getAttribute(WebElement element, String attribute) {
        if (element.toString().toLowerCase().contains("ios") && attribute.toLowerCase().contains("content"))
            return "attributeNotFound";
        String attributeValue = element.getAttribute(attribute);
        System.out.println("Retrieving " + attribute + " value  as " + attributeValue + " for " + element.toString());
        return attributeValue == null ? "" : attributeValue;
    }
    public static String getText(WebDriver driver, WebElement element) {
        String txtVal = element.getText();
        if (isIos(driver))
            return ((txtVal == null || txtVal.isEmpty()) ? getAttribute(element, "name") : txtVal);
        return txtVal;
    }

    public static void closeApp(WebDriver driver) {
        Log.info("======== Minimising App ========");
        wait(2);
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.HOME)); // KEYCODE_HOME
        wait(2);
    }

    public static void backgroundIOSApp(WebDriver driver, int seconds) {
        Log.info("======== Backgrounding IOS App for " + seconds + " seconds ========");
        ((IOSDriver) driver).runAppInBackground(ofSeconds(seconds));
    }
    public static void terminateIOSApp(WebDriver driver, String bundleId) {
        Log.info("======== Closing App with bundleId : " + bundleId + " ========");
        if (isAppInstalled(driver, bundleId))
            ((IOSDriver) driver).terminateApp(bundleId);
    }
    public static String getPropValues(String key, String path) {
        String value = "";
        Properties prop = new Properties();
        try {
            InputStream input = new FileInputStream(path);
            prop.load(input);
        } catch (Exception e) {
            System.err.println(
                    "--------------------------------------------------------------------------------------------------");
            System.err.println("Please Check The path of Properties file: Provided Path is: " + path);
            System.err.println(
                    "--------------------------------------------------------------------------------------------------");
            System.out.println(e.toString());
            System.exit(0);
        }
        value = prop.getProperty(key);
        return value;
    }
    /**
     *
     * @deprecated use GenericWaits instead of Generic
     */
    public static void wait(int Seconds) {
        try {
            Thread.sleep(Seconds * 1000);
        } catch (InterruptedException e) {
        }
    }

    public static void switchToNative(WebDriver driver) {
        if (!checkWebView(driver) || !checkAndroid(driver))
            return;
        AndroidDriver adriver = (AndroidDriver) driver;
        Set<String> contexts = adriver.getContextHandles();
        System.out.println("Switching to Native");
        for (String context : contexts)
            if (context.contains("NATIVE"))
                adriver.context(context);
        System.out.println("Context After Switch : " + adriver.getContext());
    }
    public static boolean checkWebView(WebDriver driver) {
        if (!checkAndroid(driver))
            return false;
        return ((AndroidDriver) driver).getContext().contains("WEB");
    }
    public static boolean checkNativeApp(WebDriver driver) {
        if (!checkAndroid(driver))
            return false;
        AndroidDriver adriver = (AndroidDriver) driver;
        System.out.println("Checking Native App context : " + adriver.getContext());
        return adriver.getContextHandles().contains("NATIVE") && adriver.getContextHandles().size() == 1;
    }
    public static String checkTextInPageSource(WebDriver driver, String... args) {
        if (isIos(driver)) {
            String txt = driver.findElement(MobileBy.iOSNsPredicateString("label == 'Get Started!' AND name == 'Get Started!' AND type == 'XCUIElementTypeButton'")).getText();
            return txt; // pageSource command not reliable for IOS
        }
        String pageSource = ((AndroidDriver) driver).getPageSource();
        for (String text : args) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            } // Prevent target server exception
            if (pageSource.contains(text))
                return text;
        }
        return "notFound";
    }
    public static void startAppActivity(WebDriver driver, String pkgName, String activityName) {
        GenericWaits.wait(5);
        ((AndroidDriver) driver).startActivity(new Activity(pkgName, activityName));
    }
    public static void startAppActivity(WebDriver driver, String bundleId) {
        ((IOSDriver<WebElement>) driver).activateApp(bundleId);
    }
    public static String getCurrentActivity(WebDriver driver) {
        return ((AndroidDriver) driver).currentActivity();
    }
    public static SessionId getSessionId(WebDriver driver) {
        if (!checkAndroid(driver))
            return null;
        System.out.println("Returning Session Id");
        return ((AndroidDriver) driver).getSessionId();
    }
    public static void openNotifications(WebDriver driver) {
        ((AndroidDriver) driver).openNotifications();
    }
    public static boolean containsIgnoreCase(String containerString, String containedString) {
        return containerString.toLowerCase().contains(containedString.toLowerCase());
    }
    public static boolean checkAndroid(WebDriver driver) {
        if (driver == null)
            return false;

        System.out.println("driver type : " + driver.toString());
        return Generic.containsIgnoreCase(driver.toString(), "Android");
    }
    public static boolean isIos(String udid) {
        return udid.length() > 24;
    }
    public static boolean isIos(WebDriver driver) {
        if (driver == null)
            System.err.println("Warning : Driver not initialized");
        return driver != null && driver.toString().toLowerCase().contains("ios");
    }
    public static boolean isAndroid(WebDriver driver) {
        return driver != null && driver.toString().toLowerCase().contains("android");
    }
    public static boolean isWebDriver(WebDriver driver) {
        if (driver == null)
            return false;
        String driverChk = driver.toString().toLowerCase();
        return !driverChk.contains("android") && !driverChk.contains("ios"); // AndroidDriver contains ChromeDriver path

    }
    public static boolean isUiAuto2(WebDriver driver) {
        if (checkAndroid(driver))
            return ((AndroidDriver) driver).getAutomationName().contains("2"); // UiAuto2 driver
        else
            return false;
    }
    public static void refreshAndroidDOM(WebDriver driver) {
        if (!isAndroid(driver))
            return;

        System.out.println("Refreshing Android DOM");
        driver.findElement(By.xpath("//*"));

    }
    public static String getExecutionDeviceId(WebDriver driver) {

        if (isAndroid(driver))
            return (String) ((AndroidDriver) driver).getCapabilities().getCapability("udid");

        if (isIos(driver))
            return (String) ((IOSDriver) driver).getCapabilities().getCapability("udid");

        System.err.println("Warning : Udid not present for the driver : " + driver.toString());
        return "--";

    }
    public static String getUdId(WebDriver driver) {
        return getExecutionDeviceId(driver);
    }
    public static void switchToApp(WebDriver driver, String packageName, String activity) {
        ((AndroidDriver) driver).startActivity(new Activity(packageName, activity));
    }
    public static void activateAndroidApp(WebDriver driver, String packageName) {
        ((AndroidDriver) driver).activateApp(packageName);
    }

    public static void clickHomeButton(WebDriver driver) {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.HOME));
    }
    public static void hideKeyboard(WebDriver driver) {
        if(isAndroid(driver)) {
            Log.info("======== Hiding the keyboard ========");
            ((AndroidDriver) driver).hideKeyboard();
        }else if(isIos(driver)){
            driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'return'`]")).click();
        }
    }
    public static void hideKeyboardByClickBackButton(WebDriver driver) {
        Log.info("======== Hiding the keyboard ========");
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
    }
    public static void runAppInBackGround(WebDriver driver) {
        ((AndroidDriver) driver).runAppInBackground(ofSeconds(10));
    }
    public static boolean isKeyboardDisplayed(WebDriver driver) {
        if(isIos(driver) == true){
            return false;
        }
        return ((AndroidDriver) driver).isKeyboardShown();
    }

    public static void pressSearch(WebDriver driver) {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.SEARCH));
    }
    public static void verifyToastMessageUsingPageSource(WebDriver driver, String toastMessage) throws InterruptedException {
        boolean found = false;
        for(int i =0 ; i <5; i++){
            if(driver.getPageSource().contains("class='android.widget.Toast' text='"+toastMessage+"'")){
                found = true;
                break;
            }
            Thread.sleep(5);
        }
        Log.info("=========Verifying toast message========");
        Log.info("Toast Message: "+toastMessage);
        Assert.assertTrue(found,"Incorrect Toast message: "+toastMessage);
    }




// API
    public static ArrayList<String> getListOfItems(String title ){
        ArrayList<String> titles = new ArrayList<>();
        for(String ti : title.split(",")){
            titles.add(ti.trim().replace("[","").replace("]",""));
        }
        return titles;
    }
// Mobile UI
    public static ArrayList<String> getListOfItems(List<WebElement> title ){
        ArrayList<String> titles = new ArrayList<>();
        for(WebElement ti : title){
            titles.add(ti.getText());
        }
        return titles;
    }
    public static String getDateInStringFromMilliseconds(long milliseconds, String format) {
        Date date = new Date(milliseconds);
        return stringFromDate(date, format);
    }
    public static String stringFromDate(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
    public static void switchToWebView(WebDriver driver) {
        AndroidDriver adriver = (AndroidDriver) driver;
        Set<String> contexts = adriver.getContextHandles();
        System.out.println("Switching to Webview");
        for (String context : contexts) {
            System.err.println("Context: "+context);
            if (context.contains("WEB")){
                adriver.context(context);
        }
        }
        System.out.println("Context After Switch : " + adriver.getContext());
    }

    public static boolean isListSorted(List<String> list){
        List<String> originalList = list;
        Collections.sort(list);
        return originalList.equals(list);
    }


    /**
     * Exception to be caught from calling method. IOS logic can be implemented for
     * wait until visible=true for the given element
     *
     * @param element
     */
    public synchronized static boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }


    public static void clearVal(WebDriver driver, WebElement element) {
        AndroidDriver adriver = (AndroidDriver) driver;
        element.click();
        element.clear();
    }
}
