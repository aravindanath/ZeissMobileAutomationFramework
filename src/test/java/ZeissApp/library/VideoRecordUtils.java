package ZeissApp.library;


import io.appium.java_client.screenrecording.CanRecordScreen;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class VideoRecordUtils {
	public static void startRecording(WebDriver driver) {
			((CanRecordScreen) driver).startRecordingScreen();
	}

	public static void stopRecording(ITestResult result,WebDriver driver) {
		if (true) {
			if (result.getStatus() == 2) {
				String media = ((CanRecordScreen)driver).stopRecordingScreen();
				Map<String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();
				String dir = "Videos" +  File.separator + BaseTestFunctions.platform + "_"
						+ params.get("device-id") + "_" + File.separator
						+ DateTimeManager.getDateTime() + File.separator
						+ result.getTestClass().getRealClass().getSimpleName();
				createDirectoryAndCopyFile(result, media, dir);
			}
		}
	}
	private static void createDirectoryAndCopyFile(ITestResult result, String media, String dir) {
		File videoDir = new File(dir);
		if (!videoDir.exists()) {
			videoDir.mkdirs();
		}
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			stream.write(Base64.decodeBase64(media));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
