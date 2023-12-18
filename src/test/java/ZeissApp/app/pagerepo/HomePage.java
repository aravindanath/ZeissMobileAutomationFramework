package ZeissApp.app.pagerepo;


import ZeissApp.library.AssertionLibaries;
import ZeissApp.library.GenericWaits;
import ZeissApp.library.Log;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class HomePage extends BasePage {

    public HomePage(AppiumDriver driver) {
        super(driver);
    }

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeLink[@name='start-button' or @name='Start']")
    public WebElement startButton;

    @iOSXCUITFindBy(iOSNsPredicate = "name == 'Welcome, Lorem Ipsum'")
    public WebElement welcomeTitle;





    public void navigateToPatientPage() {
        Log.info("======== Verifying home screen=====");
        AssertionLibaries.assertTitle(welcomeTitle, "Welcome, Lorem Ipsum");
        GenericWaits.waitForVisibilityOfElement(driver,startButton);
        startButton.click();

    }



}
