package ZeissApp.library;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.ini4j.Ini;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedHashSet;
import java.util.Random;

import static ZeissApp.library.BaseClass.configXLPath;


public class AppiumDriverLaunchFunctions {


    public void appium() {
        String appiumNodeExecutablePath = "/Applications/Appium Server GUI.app/Contents/Resources/app/node_modules/appium/build/lib/main.js"; //ExcelLibrary.getExcelData(configXLPath, "Android_setup", 2, 1);
        System.out.println("Appium node executable at " + appiumNodeExecutablePath);
        LinkedHashSet<String> devices = SuiteFileGenerator.devices();
        for (String device : devices) {
            AppiumServiceBuilder builder = new AppiumServiceBuilder().withIPAddress("127.0.0.1").usingAnyFreePort()
                    .withAppiumJS(new File(appiumNodeExecutablePath))
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "debug") // debug
                    .withArgument(GeneralServerFlag.LOG_TIMESTAMP)
                    .withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER, getPort());
            AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
            service.start();
            if (service.isRunning()) {
                System.out.println("Appium started at  " + service.getUrl() + " for " + device);
                BaseClass.adls.put(device, service);
            } else {
                System.out.println("Unable to start Appium for " + device);
                System.exit(1);
            }
        }
    }


    private String getPort() {
        try {
            ServerSocket socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            String port = "";
            if (socket.getLocalPort() != -1) {
                port = Integer.toString(socket.getLocalPort());
                socket.close();
                return port;
            } else {
                socket.close();
                return getPort();
            }
        } catch (Exception e) {
            return getPort();
        } catch (Error e) {
            return getPort();
        } // ECONNRESET Error
    }

    public synchronized String getPort(String udid) {
        if (true)
            return getCommonPort(udid);
        System.out.println("=== Selecting port for " + udid + " ===");
        int portSeparator = ((int) udid.charAt(new Random().nextInt(udid.length() - 1)));
        try {
            ServerSocket socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            String port = "";
            if (socket.getLocalPort() != -1) {
                port = Integer.toString(socket.getLocalPort() + portSeparator);

                if (BaseClass.devicePort.containsValue(port) || BaseClass.deviceBootStrap.containsValue(port)
                        || BaseClass.deviceUiAuto2Port.containsValue(port)) {
                    System.out.println("Main Port Conflict . Current devicePort :" + BaseClass.devicePort);
                    port = Integer.toString(socket.getLocalPort() + 15);
                }
                socket.close();
                System.out.println("=== Returning port " + port + " for " + udid);
                BaseClass.devicePort.put(udid, port);
                return port;
            } else {
                socket.close();
                return getPort(udid);
            }
        } catch (Exception e) {
            return getPort(udid);
        } catch (Error e) {
            return getPort(udid);
        }
    }


    public String getCommonPort(String udid) {
        try {
            ServerSocket socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            String port = "";

            if (socket.getLocalPort() != -1) {
                port = Integer.toString(socket.getLocalPort());
                socket.close();
                System.out.println("=== Returning port " + port + " for " + udid);
                BaseClass.devicePort.put(udid, port);
                return port;
            } else {
                socket.close();
                return getPort(udid);
            }
        } catch (Exception e) {
            return getPort(udid);
        } catch (Error e) {
            return getPort(udid);
        }
    }


    public static synchronized int getUiAuto2Port(String udid) {
        long x = 1023;
        long y = 65535;
        long uiAuto2Port;
        Random r = new Random();
        long number = x + ((long) (r.nextDouble() * (y - x)));
        int portSeparator = ((int) udid.charAt(new Random().nextInt(udid.length() - 1)));
        uiAuto2Port = number + portSeparator;
        if (BaseClass.devicePort.containsValue(uiAuto2Port + "") || BaseClass.deviceBootStrap.containsValue(uiAuto2Port + "")
                || BaseClass.deviceUiAuto2Port.containsValue(uiAuto2Port + "")) {
            System.err.println("Port Conflict . Current deviceBootStrapPort : " + BaseClass.deviceBootStrap);
            uiAuto2Port += 15;
        }
        BaseClass.deviceUiAuto2Port.put(udid, uiAuto2Port + "");
        System.out.println("UIAutomator2 port for " + udid + " : " + uiAuto2Port);
        return (int) uiAuto2Port;
    }

    public static boolean checkUDIDConnected(String udid) {
        try {
            String chkCmd = Generic.isIos(udid) ? "xcrun instruments -s devices" : "adb devices";

            return AdbFunctions.execCmd(chkCmd).contains(udid);
        } catch (Exception e) {
            System.err.println("Error while checking for connected UDID " + udid + "\n" + e.getMessage());
            return false;
        }
    }

    public static String getPlatformVersion(String udid) {
        Ini ini = null;
        try {
            ini = new Ini(new File(BaseClass.deviceInfoFilePath));
        } catch (IOException e) {
            System.err.println("Devices.ini file not found");
            System.exit(1);
        }
        String value = ini.get(udid, "platformVersion");
        if ((value == null)) {
            System.err.println("Platform Version  not found for " + udid);
            System.exit(1);
        }
        return value;
    }


    public static String getDeviceName(String udid) {
        String env = DataReadWriteLibrary.getExcelData(configXLPath, "Modules", 2, 3).toLowerCase();
        String environment = env.contains("uat") ? "uat" : "qa";
        String path = System.getProperty("user.dir")+File.separator+"config"+ File.separator+ environment + File.separator+ "Devices.ini";
        Ini ini = null;
        try {
            ini = new Ini(new File(BaseClass.deviceInfoFilePath));
        } catch (IOException e) {
            System.err.println("Devices.ini file not found " + udid);
            System.err.println("Update device details --> " + path);
            System.exit(0);
        }
        System.out.println("Device udid: " + udid);
        String value = ini.get(udid, "deviceName");
        if ((value == null)) {
            System.err.println("Device Name value not found for " + udid + ", **Update device details at " + path);
            System.exit(0);
        }else{
            System.out.println("Device Name value: " + value);
        }
        return value;
    }


    public static String getDeviceModel(WebDriver driver) {
        String udid = getUdid(driver), sheet = "Modules", deviceModel = "", tcRowVal;
        int rc, udidColumn = 9, deviceModelColumn = 10;
        if (BaseClass.udidModel.get(udid) != null)
            return BaseClass.udidModel.get(udid);
        try {
            rc = DataReadWriteLibrary.getExcelRowCount(configXLPath, sheet);
            for (int i = 0; i <= rc; i++) {
                tcRowVal = DataReadWriteLibrary.getExcelData(configXLPath, sheet, i, udidColumn);
                if (tcRowVal.contains(udid)) {
                    deviceModel = DataReadWriteLibrary.getExcelData(configXLPath, sheet, i, deviceModelColumn);
                    BaseClass.udidModel.put(udid, deviceModel);
                    System.out.println("Device Model: " + deviceModel);
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving Device model for " + udid + '\n');
            e.printStackTrace();
        }
        return deviceModel;
    }

    public static String getUdid(WebDriver driver) {
        return "emulator-5554";
    }

}
