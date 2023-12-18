package ZeissApp.library;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.ini4j.Ini;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class BaseTestFunctions extends BaseClass // BaseClass=Execution . BaseDev=Development ,BaseClass_BrowserStack
{
	public String getTestData(String tcId) {
		return getTestVal(tcId, "data");
	}

	public String getValByKey(String tcId, String key) {
		String environment = env.contains("uat") ? "uat" : "qa";
		String platform = Generic.isIos(driver) ? "ios" : "android";
		path = System.getProperty("user.dir") + File.separator + "excel_lib" + File.separator + environment
				+ File.separator + platform + File.separator + "TestData.ini";
		System.out.println(" ******** platform: " + platform);
		System.out.println(" ******** path: " + path);
		System.out.println(" ******** environment: " + environment);
		Ini ini = null;
		try {
			System.out.println(path);
			ini = new Ini(new File(path));
		} catch (IOException e) {
			Assert.fail("TestData.ini file not found");
		}

		String value = ini.get(tcId, key);
		if ((value == null))
			Assert.fail(key + " value not found for " + tcId);

		return value;
	}
	public String getValByTitleKey(String tcId, String key) throws IOException {
		String environment = env.contains("uat") ? "uat" : "qa";
		String platform = Generic.isIos(driver) ? "ios" : "android";
		String path = System.getProperty("user.dir") + File.separator + "excel_lib" + File.separator + environment+ File.separator + platform + File.separator + "TestData.ini";
		System.out.println(" ******** platform: " + platform);
		System.out.println(" ******** path: " + path);
		System.out.println(" ******** environment: " + environment);
		Ini ini = new Ini(new File(path));
		String value = ini.get(tcId, key);
		if ((value == null))
			Assert.fail(key + " value not found for " + tcId);
		System.out.println(value);
		return value;
	}
	public String getVal(String csvData, String key) {
		if (!csvData.contains(key))
			Log.info("Warning : " + key + " not found in TestData ");
		return csvData.split(key + '=')[1].split(",")[0];
	}
	public String getDeviceNameByKey() {
		String environment = env.contains("uat") ? "uat" : "qa";
		String path = "./config"+ File.separator+ environment + File.separator+ "Devices.ini";
		Ini ini = null;
		try {
			ini = new Ini(new File(path));
		} catch (IOException e) {
			Assert.fail("Devices.ini file not found");
		}
		String value = ini.get(AppiumDriverLaunchFunctions.getUdid(driver), "deviceName");
		if ((value == null))
			Assert.fail("Device Name value not found for " + AppiumDriverLaunchFunctions.getUdid(driver));
		return value;
	}
	public String getPlatform() {
		String platform = "";
		if (Generic.isAndroid(driver))
			platform = "Android";
		if (Generic.isIos(driver))
			platform = "IOS";
		return platform;
	}
	public String getTestVal(String tcId, String val) {
		if (udid == null) {
			System.err.println("Warning : udid not initialised ");
			return "";
		}
		int col = val.equals("data") ? 2 : 1; // TestData:TestScenario
		String environment = env.contains("uat") ? "uat" : "qa";
		String tcRowVal;
		String platform = Generic.isIos(udid) ? "ios" : "android",
				module = this.getClass().getCanonicalName().split("testScripts.")[1].split("\\.")[0],
				path = "./excel_lib/" + environment + '/' + platform + '/' + "TestData.xlsx";
		System.err.println("platform: " +platform);
		System.err.println("module: " +module);
		System.err.println("path: " +path);
		int rc = DataReadWriteLibrary.getExcelRowCount(path, module);
		for (int i = 0; i <= rc; i++) {
			tcRowVal = DataReadWriteLibrary.getExcelData(path, module, i, 1);
			if (tcRowVal.equals(tcId))
				return DataReadWriteLibrary.getExcelData(path, module, i, col);
		}
		return "DefaultVal" + tcId + ':' + module;
	}
	public String getDeviceId() {
		String udid = AppiumDriverLaunchFunctions.getUdid(driver), sheet = "Modules", DeviceId = "", tcRowVal;
		int rc, udidColumn = 9, wDeviceIdColOffset = 4; // Column Offset from udid column
		try {
			rc = DataReadWriteLibrary.getExcelRowCount(configXLPath, sheet);
			for (int i = 0; i <= rc; i++) {
				tcRowVal = DataReadWriteLibrary.getExcelData(configXLPath, sheet, i, udidColumn);
				if (tcRowVal.contains(udid))
					DeviceId = DataReadWriteLibrary.getExcelData(configXLPath, sheet, i, udidColumn + wDeviceIdColOffset);
			}
		} catch (Exception e) {
			System.err.println("Error retrieving Device model for " + udid + '\n');
			e.printStackTrace();
		}
		return DeviceId;


	}

	public static synchronized int getWdaLocalPort(String iosUdid) {
		long x = 1023;
		long y = 65535;
		long wdaPort = ThreadLocalRandom.current().nextLong(x, y + 1);
		System.out.println("WDA Port for " + iosUdid + "  :  " + wdaPort);
		return (int) wdaPort;
	}

	public WebDriver launchWebDriver(){
		WebDriver driver;
		WebDriverManager.chromedriver().setup();
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
		chromeOptions.addArguments("--disable-dev-shm-usage");
		Map<String,Object> prefs= new HashMap<>();
		prefs.put("profile.default_content_setting_values.notifications", 1);
		chromeOptions.setExperimentalOption("prefs", prefs);
		driver = new ChromeDriver(chromeOptions);
		driver.manage().window().maximize();
		return driver;
	}

	public String getEnvWebUrl()
	{
		return Generic.getPropValues("webUrl",configPath);
	}
}