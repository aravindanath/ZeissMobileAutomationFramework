package ZeissApp.library;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.WebDriver;

import static ZeissApp.library.Generic.isAndroid;
import static ZeissApp.library.Generic.isIos;


/**
 * @author Aravindanath
 */
public class AppiumKeyboardEvents {

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
    public static boolean isKeyboardDisplayed(WebDriver driver) {
        return ((AndroidDriver) driver).isKeyboardShown();
    }
    public static void pressEnterKey(WebDriver driver) {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
    }
    public static void closeApp(WebDriver driver) {
        Log.info("======== Minimising App ========");
       Generic. wait(2);
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.HOME)); // KEYCODE_HOME
        GenericWaits.wait(2);
    }
    public static void pressSearch(WebDriver driver) {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.SEARCH));
    }

    public static void goBack(WebDriver driver){
        ((AndroidDriver) driver).pressKey(new KeyEvent((AndroidKey.BACK)));
    }
}
