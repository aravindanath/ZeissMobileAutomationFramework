package ZeissApp.library;

import java.io.IOException;
import java.util.Scanner;

public class AdbFunctions {


    public static String execCmd(String cmd) // throws java.io.IOException
    {
        try {
            Scanner s = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream())
                    .useDelimiter("\\A");

            String cmdOutput = s.hasNext() ? s.next() : "";
            s.close();
            return cmdOutput; // Implement close logic by storing in separate string before returning
        } catch (Exception e) {
            System.err.println("-- Unable to execute command " + cmd + " --" + e.getMessage());
            return "";
        }

    }


    public static void turnOnWifi() throws InterruptedException, IOException {
        String cmd = "adb shell am broadcast -a io.appium.settings.data_connection --es setstatus enable";
        Runtime.getRuntime().exec(cmd);
    }

    public static void turnOffWifi() throws InterruptedException, IOException {
        String cmd = "adb shell am broadcast -a io.appium.settings.data_connection --es setstatus enable";
        Runtime.getRuntime().exec(cmd);
    }

    public static void runcommand(String command) {
        System.out.println("Command: " + command);

        try {
            //  Scanner scan = new Scanner(Runtime.getRuntime().exec(command).getInputStream()).useDelimiter("//A");
//
            Scanner scan = new Scanner(Runtime.getRuntime().exec(command).getInputStream());
//            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println("Command: " + command + " Sucessfully executed!");

    }


}