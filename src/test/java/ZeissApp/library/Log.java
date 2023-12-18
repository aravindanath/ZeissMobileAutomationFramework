package ZeissApp.library;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class Log 
{
	public static void info(String infoMsg)
	{
		System.out.println(infoMsg);
		if(BaseTestFunctions.deviceTrialRun) return;// Distinguish between Individual Run & Suite Run
		if( ExtentTestManager.getTest()!=null) {
			ExtentTestManager.getTest().log(LogStatus.INFO, infoMsg);
		}
	}
	
	public static void groupPass(String testName)
	{
		System.out.println("========== Passed: "+testName+" ==========");if(BaseTestFunctions.deviceTrialRun) return;
		ExtentTestManager.getTest().log(LogStatus.PASS,"========== Passed: "+testName+" ==========");
	}
	public static void testPass(String testName,String desc)
	{
		System.out.println("========== Passed: "+testName+":"+desc+" ==========");
		if( ExtentTestManager.getTest()!=null) {
			ExtentTestManager.getTest().log(LogStatus.PASS, "========== Passed: " + testName + ':' + desc + " ==========");
			ExtentTest childTest = ExtentTestManager.startTest(testName, desc);
			childTest.log(LogStatus.PASS, "========== Passed: " + testName + ':' + desc + " ==========");
			ExtentTestManager.getTest().appendChild(childTest);
		}
	}

	public static void skip(String skipMsg) // Please Note : This method is to be called from TestScripts only followed by script exit. 
	{
		System.out.println(skipMsg);if(BaseTestFunctions.deviceTrialRun) return;
		
		try
		{
			if(ExtentTestManager.getTest()==null) {System.err.println("Warning : Test not initialised"); return;}
			
			ExtentTestManager.getTest().log(LogStatus.SKIP,skipMsg);	
		}
		catch(Exception e)
		{
			System.out.println("Unable to log skip message to Extent  \n"+e.toString());e.printStackTrace();
		}			
	}
}
