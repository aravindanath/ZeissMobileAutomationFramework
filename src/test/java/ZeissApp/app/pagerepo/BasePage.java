package ZeissApp.app.pagerepo;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

/**
 * @Author: Aravindanath
 */

public class BasePage {


	protected AppiumDriver driver;

	public BasePage(AppiumDriver driver) {
		this.driver = driver;PageFactory.initElements(driver, this);
	}
}
