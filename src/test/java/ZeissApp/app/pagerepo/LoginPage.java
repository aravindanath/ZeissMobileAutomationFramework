package ZeissApp.app.pagerepo;



import ZeissApp.library.AssertionLibaries;
import ZeissApp.library.Log;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;


public class LoginPage  extends BasePage  {


    @iOSXCUITFindBy(iOSNsPredicate = "type ='XCUIElementTypeStaticText' and name contains 'your privacy is important to us' or value contains 'your privacy is important to us'")
    protected WebElement wibmologo;

    @iOSXCUITFindBy(iOSNsPredicate = "label == 'Stop'")
    protected WebElement toolbarTitle;





    public LoginPage(AppiumDriver driver) {
        super(driver);
    }

    public void login() {
        Log.info("======== User is trying to enter pin =====");
        for(int i = 1; i <=4;i++)
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name='"+i+"']")).click();
        Log.info("======== User has sucessfully loged-in  =====");
    }



}
