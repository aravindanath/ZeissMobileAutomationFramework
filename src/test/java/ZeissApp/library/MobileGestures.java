package ZeissApp.library;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebElement;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;


public class MobileGestures {

    public static void scrollAndClick(WebDriver driver, String text) {
        String str = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains('" + text + "'));";
        ((AndroidDriver) driver).findElementByAndroidUIAutomator(str).click();
    }

    public static void scrollClick(WebDriver driver, String text) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap scrollObject = new HashMap<>();
        scrollObject.put("predicateString", "value == '" + text + "'"); // or name ==' '
        js.executeScript("mobile: scroll", scrollObject);
    }

    public static void scrollToEnd(WebDriver driver) {
        try {
            ((AndroidDriver) driver).findElementByAndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollToEnd(9);");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void scrollToText(WebDriver driver, String text) {
        String automatorString = "new UiScrollable(new UiSelector()).scrollTextIntoView('" + text + "')";
        System.out.println(automatorString);
        ((AndroidDriver) driver).findElementByAndroidUIAutomator(automatorString);
        MobileElement e = (MobileElement) ((AndroidDriver) driver).findElementByAndroidUIAutomator(automatorString);
    }

    public static void verticalSwipeByPercentages(WebDriver driver, double startPercentage, double endPercentage, double anchorPercentage) {
        Dimension size = driver.manage().window().getSize();
        int anchor = (int) (size.width * anchorPercentage);
        int startPoint = (int) (size.height * startPercentage);
        int endPoint = (int) (size.height * endPercentage);
        new TouchAction((PerformsTouchActions) driver)
                .press(point(anchor, startPoint))
                .waitAction(waitOptions(ofMillis(1000)))
                .moveTo(point(anchor, endPoint))
                .release().perform();
    }

    public static void horizontalScrolling(WebDriver driver, String text) {
        MobileElement element = (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).setAsHorizontalList()" +
                        ".scrollIntoView(new UiSelector().text('" + text + "'))"));
    }
    public static void verticalScrolling(WebDriver driver, String text) {
        MobileElement element = (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).setAsVerticalList()" +
                        ".scrollIntoView(new UiSelector().text('" + text + "'))"));
    }
    public static void longPress(WebDriver driver,WebElement ele) {
        TouchAction action = new TouchAction((PerformsTouchActions) driver);
        action.longPress(longPressOptions().withElement(element(ele)).withDuration(ofSeconds(2))).release().perform();
    }
    public static void mapGestures(WebDriver driver){
        TouchAction swipe = new TouchAction((PerformsTouchActions) driver)
                .press(PointOption.point(540, 1824))
                .waitAction(waitOptions(ofMillis(800))).moveTo(PointOption.point(540, 672)).release().perform();

    }
    private static void injectAudio(WebDriver driver, String path) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", path);
        ((AndroidDriver)driver).executeScript("mobile:audio:inject", params);
    }
    public static void swipeBasedOnElement(WebDriver driver, WebElement element,String direction) {
        ((JavascriptExecutor)driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId",((RemoteWebElement)element).getId(),
                "direction", direction,
                "percent", 0.75
        ));
    }

    public static void tap(WebDriver driver, WebElement element) {
        TouchActions action = new TouchActions(driver); action.singleTap(element); action.perform();
    }

    public static void zoomIn(WebDriver driver){
        TouchAction action = new TouchAction((PerformsTouchActions) driver);
        int x = driver.manage().window().getSize().getWidth() / 2;
        int y = driver.manage().window().getSize().getHeight() / 2;
        action.press(PointOption.point(x, y))
                .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                .moveTo(PointOption.point(x, y - 100))
                .release()
                .perform();
    }

    public static void zoomOut(WebDriver driver){
        TouchAction action = new TouchAction((PerformsTouchActions) driver);
        int x = driver.manage().window().getSize().getWidth() / 2;
        int  y = driver.manage().window().getSize().getHeight() / 2;
        action.press(PointOption.point(x, y))
                .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                .moveTo(PointOption.point(x, y + 100))
                .release()
                .perform();
    }
    public static void swipeToLeft(WebDriver driver, WebElement element){
        int startX = element.getLocation().getX() + element.getSize().getWidth() - 10; // Adjust the starting X coordinate as needed
        int startY = element.getLocation().getY() + element.getSize().getHeight() / 2;
        int endX = element.getLocation().getX() + 10; // Adjust the ending X coordinate as needed
        int endY = startY;
        TouchAction touchAction = new TouchAction((PerformsTouchActions) driver);
        touchAction.press(PointOption.point(startX, startY))
                .moveTo(PointOption.point(endX, endY))
                .release()
                .perform();
    }
    public static <AppiumDriver> void swipeLeftTillEnd(WebDriver driver) {
        System.out.println("Swiping Left");
        AppiumDriver adriver = (AppiumDriver) driver;
        TouchAction touchAction = new TouchAction((PerformsTouchActions) adriver);
        Dimension size = ((WebDriver) adriver).manage().window().getSize();
        int startx = (int) (size.width * 0.95);
        int endx = (int) (size.width * 0.1);
        int starty = size.height / 3;
        try {
            System.out.println("============Dimension :" + size);
            touchAction.press(PointOption.point(startx, starty))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1))).moveTo(PointOption.point(endx, starty))
                    .release().perform(); // Absolute final Co-ordinates
        } catch (Exception e) {
            System.err.println("Scroll error ");
            e.printStackTrace();
        }
    }

    public static void swipeElement(WebElement elementToSwipe, WebDriver driver){
        int startX = elementToSwipe.getLocation().getX() + elementToSwipe.getSize().getWidth() - 10; // Adjust the starting X coordinate as needed
        int startY = elementToSwipe.getLocation().getY() + elementToSwipe.getSize().getHeight() / 2;

        int endX = elementToSwipe.getLocation().getX() + 10; // Adjust the ending X coordinate as needed
        int endY = startY;

        TouchAction touchAction = new TouchAction((PerformsTouchActions) driver);
        touchAction.press(ElementOption.element(elementToSwipe, startX, startY))
                .moveTo(ElementOption.element(elementToSwipe, endX, endY))
                .release()
                .perform();

    }

    //Horizontal Swipe by percentages
    public void horizontalSwipeByPercentage (WebDriver driver,double startPercentage, double endPercentage, double anchorPercentage) {
        Dimension size = driver.manage().window().getSize();
        int anchor = (int) (size.height * anchorPercentage);
        int startPoint = (int) (size.width * startPercentage);
        int endPoint = (int) (size.width * endPercentage);
        new TouchAction((PerformsTouchActions) driver)
                .press(point(startPoint, anchor))
                .waitAction(waitOptions(ofMillis(1000)))
                .moveTo(point(endPoint, anchor))
                .release().perform();
    }

    public static void swipe(WebDriver driver, WebElement source, WebElement target){
        TouchAction touch = new TouchAction((PerformsTouchActions) driver);


        touch.longPress(ElementOption.element(source))
                .moveTo(ElementOption.element(target)).release().perform();
    }
}
