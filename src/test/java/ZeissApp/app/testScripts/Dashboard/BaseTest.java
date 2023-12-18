package ZeissApp.app.testScripts.Dashboard;

import ZeissApp.library.BaseTestFunctions;
import ZeissApp.library.DataReadWriteLibrary;

import java.io.File;
import java.util.HashMap;


public class BaseTest extends BaseTestFunctions {

	/** The test data HashMap which consists of the TestCaseId and TestData as Key Value pairs . */
	private static HashMap<String, String> testData=new HashMap<String, String>();

	/** The test scenario HashMap which consists of the TestCaseId and TestScenario as Key Value pairs . */
	private static HashMap<String, String> testScenario=new HashMap<String, String>();


	public void initData() //Inputs values into the HashMaps testData and testScenario from the Excel file.
	{
		String path = System.getProperty("user.dir")+ File.separator+"excel_lib"+File.separator+env+File.separator+platform+File.separator+"TestData.xlsx";
		int rc= DataReadWriteLibrary.getExcelRowCount(path, "Accounts");
		for (int i = 0; i <= rc; i++)
		{
			if (!DataReadWriteLibrary.getExcelData(path, "Accounts",i,0).equals(" "))
			{
				testData.put(DataReadWriteLibrary.getExcelData(path, "Accounts",i,0), DataReadWriteLibrary.getExcelData(path, "Accounts",i,2));
				testScenario.put(DataReadWriteLibrary.getExcelData(path, "Accounts",i,0), DataReadWriteLibrary.getExcelData(path, "Accounts",i,1));
			}
		}

	}



}
