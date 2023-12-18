package ZeissApp.library;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Pattern;

import static ZeissApp.library.Generic.*;

public class GenericWaits {

    public static String waitUntilTextInPage(WebDriver driver, int timeoutSeconds, String... args) {
        String textFound = "notFound";

        for (int i = 0; i < timeoutSeconds; i++) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            if (!(textFound = checkTextInPageSource(driver, args)).equals("notFound"))
                return textFound;
        }
        return textFound;
    }

    public static String waitUntilAnyTextVisible(WebDriver driver, WebElement element, int timeoutSeconds) {
        String content;
        for (int i = 0; i < timeoutSeconds; i++)
        {
            wait(1);
            content = element.getText();
            if (!content.isEmpty()) {
                Log.info("======== Element text found : " + content + " ========");
                return content;
            }
        }
        return "No Text found";
    }
    public static void wait(int Seconds) {
        try {
            Thread.sleep(Seconds * 1000);
        } catch (InterruptedException e) {
        }
    }

    public static void waitForElementToBeClickable(WebDriver driver, WebElement element) {
        Log.info("======== Wait for Element To Be Clickable ["+element+"=====");
        new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitFortextToBePresentInElement(WebDriver driver, WebElement element, String expectedData) {
        Log.info("======== wait For text ToBe Present In Element   ========");
      new WebDriverWait(driver,30).until(ExpectedConditions.textToBePresentInElement(element,expectedData));
    }
    public static void waitForPatternToBePresentInElement(WebDriver driver, By element, Pattern pattern) {
        Log.info("======== Wait For a Pattern to be present in element ========");
        new WebDriverWait(driver,30).until(ExpectedConditions.textMatches(element,pattern));
    }
    public static void waitForVisibilityOfElement(WebDriver driver, WebElement element){
        Log.info("======== wait For Visibility Of Element =====");
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitUntilIOSElementVisible(WebDriver driver, WebElement element, int timeout) {
        if (!isIos(driver))
            return;

        // for int i 1 to timeout
        for (int i = 0; i < timeout; i++)
            if (getAttribute(element, "visible").equals("true"))
                return;
            else
                wait(1);

        System.out.println("Warning : Element not yet visible");
    }

}