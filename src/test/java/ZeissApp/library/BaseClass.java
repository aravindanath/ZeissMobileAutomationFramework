package ZeissApp.library;

import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.ios.IOSDriver;


import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.commons.io.FileUtils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static ZeissApp.library.AppiumDriverLaunchFunctions.*;
import static ZeissApp.library.BaseTestFunctions.getWdaLocalPort;

/**
 * @Author Aravindanath
 */

public class BaseClass {
    public static boolean apiCompression = false;
    public static String configXLPath = System.getProperty("user.dir")+File.separator+"config"+File.separator+"config.xlsx";
    public static String env = "qa";//DataReadWriteLibrary.getExcelData(configXLPath, "Modules", 2, 3).toLowerCase();
    public static String platform = DataReadWriteLibrary.getExcelData(configXLPath, "Modules", 2, 4);
    public static HashMap<String, String> devicePort = new HashMap<String, String>();
    public static HashMap<String, String> deviceBootStrap = new HashMap<String, String>();
    public static HashMap<String, String> deviceUiAuto2Port = new HashMap<String, String>();
    public static HashMap<String, WebDriver> driverHolder = new HashMap<String, WebDriver>();
    public static HashMap<String, Boolean> skipStatus = new HashMap<String, Boolean>();
    public static HashMap<String, String> udidModel = new HashMap<String, String>();
    public static HashMap<String, AppiumDriverLocalService> adls = new HashMap<String, AppiumDriverLocalService>();
    public static String packageName = " ";
    public IOSDriver driver;
    private static String envConfigFolder;
    public static String udid;
    public static String path = System.getProperty("user.dir"); // MAC
    public static String deviceInfoFilePath = path+File.separator+"config" +File.separator+ env + File.separator + "Devices.ini";
    public static String testDescription;
    public static String configPath;
    public static String appVersion = "18.8 Internal";
    public static boolean deviceTrialRun;
    public WebDriver getDriver() {
        return driver;
    }
    public String getTestScenario(String tcId) {
        DataReadWriteLibrary el = new DataReadWriteLibrary();
        try {
            String module = this.getClass().getCanonicalName().split("testScripts.")[1].split("\\.")[0],
                    path = "./excel_lib"+File.separator + env +File.separator +  platform.toLowerCase() +File.separator +  "TestData.xlsx";
           String scenario = el.getMapByColumn(path, module, "TestCaseName", tcId).get("TestDescription");
            return scenario ;
        }
        catch(Exception e){
            System.err.println("Excel Value not found\n");e.printStackTrace();
            return "Excel Value not found";
        }
    }

    @BeforeSuite
    public void executDefaultValTCeCleanUpFile() {
        ExtentManager.generateFilePath();
        initializeEnvPaths();
    }

    public static void initializeEnvPaths() {
        configPath = System.getProperty("user.dir") + File.separator + "config" + File.separator + env + File.separator + "config.properties";
        packageName = Generic.getPropValues("PACKAGENAME", configPath);
        System.out.println("Setting package name : " + packageName);
        System.out.println("Environment initialized to " + env);
        envConfigFolder = env;
        System.out.println("configPath set to " + configPath);
        packageName = Generic.getPropValues("PACKAGENAME", configPath);
        System.out.println("Setting package name : " + packageName);

    }
    @Parameters("device-id")
    @BeforeTest
    public void initializeLogger(String deviceId) {
        System.out.println("Device Name: "+ getDeviceName(deviceId));
        udid =deviceId;
    }
    @BeforeMethod
    public void startMethod(Method testMethod) {
        String tcName = testMethod.getName();
        if (!tcName.contains("_")) {
            System.out.println("Invalid TC name in startMethod : " + tcName);
            return;
        }
        try {
            testDescription=getTestScenario(tcName);
        } catch (Exception e) {
            System.out.println("== Error in invoking method ==");
            e.printStackTrace();
        }
        String category = this.getClass().getCanonicalName().split("testScripts.")[1].split("\\.")[0];// previously
        System.out.println("------------------------------------- " + tcName + " : " + testDescription
                + " ---------------------------------------");
        try {
            if (Generic.isIos(driver))
                category += " IOS";
            ExtentTestManager.startTest(tcName, testDescription);
            ExtentTestManager.getTest().assignCategory(category);
        } catch (Exception e) {
            System.out.println("Warning : Unable to write to Extent");
        }
    }
    @AfterMethod
    public void updateResults(ITestResult iRes) {
        int res = iRes.getStatus();
        String testName = iRes.getMethod().getMethodName();
        if (getSkipStatus(driver)) {
            ExtentManager.getReporter().endTest(ExtentTestManager.getTest());
            ExtentManager.getReporter().flush();
            return;
        }
        if (res == 2) // SUCCESS=1 , FAILURE=2, SKIP=3
        {
            if (driver == null) {
                System.out.println("No Screenshot for null driver ");
                return;
            }
            WebDriver driver = this.driver;
            System.out.println("Failure --- Capturing screenshot");
            try {
                if (driver != null) {
                    File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    try {
                        if (Generic.isIos(driver))
                            testName += "_IOS";
                        FileUtils.copyFile(f, new File(ExtentManager.screenPath + testName + ".jpg")); // Extent
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                        }
                    } catch (IOException e) {
                        System.out.println("== Error in copying screenshot ==\n" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.out.println("======== Error in obtaining screenshot ========\n");
                e.printStackTrace();
            }
            String extScreens = "./" + testName + ".jpg";
            ExtentTestManager.getTest().log(LogStatus.FAIL, "HTML",
                    "There is an error: <br/><br/> " + iRes.getThrowable().getMessage()
                            + " <br/><br/> Error Snapshot : "
                            + ExtentTestManager.getTest().addScreenCapture(extScreens));
        }
        if (res == 1) {
            System.out.println( "========== Passed: " + testName + " ==========");
            ExtentTestManager.getTest().log(LogStatus.PASS, "========== Passed: " + testName + " ==========");
        }
        try {

            if (Generic.containsIgnoreCase(driver.toString(), "android")
                    || Generic.containsIgnoreCase(driver.toString(), "ios")) // If Mobile Driver then get device details
            {
                ExtentTestManager.getTest().assignAuthor(getDeviceModel(driver));

                if (!appVersion.isEmpty())
                    ExtentManager.getReporter().addSystemInfo("App Version", appVersion);
                ExtentManager.getReporter().addSystemInfo("Device Name", getDeviceName(udid));
            } // HandleHomePopup.setHandledStatus(driver, true);
        } catch (Exception e) {
            System.out.println("== Unable to obtain device details ==\n" + e.getMessage());
        }
        ExtentManager.getReporter().endTest(ExtentTestManager.getTest());
        ExtentManager.getReporter().flush();
        VideoRecordUtils.stopRecording(iRes, driver);
    }
    public boolean getSkipStatus(WebDriver driver) {
        if (driver == null)
            return false;
        if (!Generic.isAndroid(driver))
            return false;
        if (skipStatus.get(getUdid(driver)) == null)
            return false;
        return skipStatus.get(getUdid(driver));
    }
    @Parameters("device-id")
    @BeforeClass // (dependsOnMethods="baseSetup")
    public void launchApplication(String deviceId) throws MalformedURLException {
        udid = deviceId;
        launchApp( deviceId);
        driverHolder.put(deviceId, driver);
        VideoRecordUtils.startRecording(driver);
    }

    public synchronized void launchApp( String udid) throws MalformedURLException {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("deviceName","iPhone 15");
        cap.setCapability("automationName","XCUITest");
        cap.setCapability("udid",udid);
        cap.setCapability("platformName","iOS");
        cap.setCapability("bundleId","com.example.apple-samplecode.Food-Truck");
        cap.setCapability("launchTimeout","800000");
        cap.setCapability("platformVersion","17.0");
        cap.setCapability("newCommandTimeout","100");
        driver =  new IOSDriver(new URL("http://127.0.0.1:4723/"),cap);
        driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
    }
    @AfterSuite(alwaysRun = true)
    public void close() {
        try {
            for (String device : adls.keySet()) {
                System.out
                        .println(" ~ Closing Appium Server at " + adls.get(device).getUrl() + " for " + device + " ~");
                adls.get(device).stop();
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Error in closing After Suite process  " + e.getMessage());
            e.printStackTrace();
        }
    }

}