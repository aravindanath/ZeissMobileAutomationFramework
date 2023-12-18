package ZeissApp.library;

import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SuiteFileGenerator
{
  static List<XmlClass> classes = null;
  static Set<XmlClass> classSet = null;
  static Set<XmlTest> testSet = new LinkedHashSet<XmlTest>();
  static List<XmlTest> testList = new ArrayList<XmlTest>();
  static String pkgName = "";
  static int t = 0;
  static String configXlPath="./config/config.xlsx";
  static String env= DataReadWriteLibrary.getExcelData(configXlPath, "Modules", 2, 3).toLowerCase(),platform,testDataPath;

  public static void main(String[] args) {
    XmlSuite suite = new XmlSuite();
    System.err.println("Env : "+env);
    suite.setName("FullRegression");
    suite.setPreserveOrder(true); 		// 	Preserve Order is Default
    int mr = DataReadWriteLibrary.getExcelRowCount(configXlPath, "Modules");
	System.err.println("Row Count: "+ mr);
    for (int i = 2; i <= mr; i++) {
      if (DataReadWriteLibrary.getExcelData(configXlPath, "Modules", i, 1).equalsIgnoreCase("yes"))
      {
    	  LinkedHashSet<String> devices=devices();
    	  if(devices.size()>1)
    	  {
    		  suite.setThreadCount(devices.size());
    	  }
    	 System.out.println("Generating Test classes. Please Wait ... ");
    	 for(String device : devices)
    	 {
    		 platform=isIos(device)?"ios":"android";
    		 testDataPath="./excel_lib/"+env+'/'+platform+"/TestData.xlsx";
			 System.err.println("*** testDataPath:"+testDataPath);
	         pkgName = DataReadWriteLibrary.getExcelData(configXlPath, "Modules", i, 0);
			 System.err.println("pkgName: "+ pkgName);
	         XmlTest test = new XmlTest(suite);
	         test.setName("Device_"+device.substring(0,4)+"_Test");
	         test.setPreserveOrder(true);
			 HashMap<String, String> m = new HashMap();
			 m.put("device-id", device);
	         test.setParameters(m);
	         classes = new ArrayList();
			 classSet = new LinkedHashSet<>();

	       // for (int j = 0; j <= mr; j++) // traverse through Modules in config.xlsx
			 int j=i;
	        {
		       if ( DataReadWriteLibrary.getExcelData(configXlPath, "Modules", j, 1).equalsIgnoreCase("yes") &&
					   DataReadWriteLibrary.getExcelData(configXlPath, "Modules", j, 2).equalsIgnoreCase(device) )
			  {
	            t += 1;
	            String module = DataReadWriteLibrary.getExcelData(configXlPath, "Modules", j, 0);

	            int tr = DataReadWriteLibrary.getExcelRowCount(testDataPath, module);
				  System.out.println(module+" has " + tr+" rows");

	            for (int k = 0; k <= tr; k++) // traverse through testdata for in Module  sheet under TestData.xlsx
	            {
	              if (DataReadWriteLibrary.getExcelData(testDataPath, module, k, 3).equalsIgnoreCase("yes")) 		//  k,4 if mapping to the Regression column
	              {
	                XmlClass cls = new XmlClass(); // Modules list and TestData sheet names should match
					 String testClassName = DataReadWriteLibrary.getExcelData(testDataPath, module, k, 0);
					 if(testClassName.length()>2) {
						 cls.setName("ZeissApp.testScripts." + module + "." + testClassName);
						 classSet.add(cls);
					 }
	              }
	            }
	          }
	        }
			 classes.addAll(classSet);
			 if(classes.size()>0) {
				 test.setXmlClasses(classes);
				 testSet.add(test);
			 }
    	 }

      }
    }

    // ==== Write Suite generated to scripts.xml ==== //
	  testList.addAll(testSet);
	  suite.setTests(testList);
	  writeToXmlFile(suite);
  }

  public static LinkedHashSet<String> devices()
  {
	  LinkedHashSet<String> devices=new LinkedHashSet<String>();


	  int mr = DataReadWriteLibrary.getExcelRowCount(configXlPath, "Modules");

	 for (int j = 0; j <= mr; j++)
	        if (DataReadWriteLibrary.getExcelData(configXlPath, "Modules", j, 1).toLowerCase().contains("yes"))
	        		  devices.add(DataReadWriteLibrary.getExcelData(configXlPath, "Modules", j, 2));

	 System.out.println(":: Devices "+devices+" ::");

//	 checkDevices(devices);

	  return devices;
  }

    public static void checkDevices(LinkedHashSet<String> devices) {
        String checker = execCmd("adb devices");

        //=== IOS === //
        for (String iChk : devices)
            if (iChk.length() > 25)
                checker += execCmd("xcrun instruments -s devices");
        // ======== //

        for (String device : devices)
            if (!checker.contains(device)) {
                System.err.println("-------------------------------------------------------------");
                System.err.println(" -------- Device with udid " + device + " not detected -------- ");
                System.err.println("-------------------------------------------------------------");
                System.exit(0);
            }
        if (checker.contains("ffline"))
            System.out.println("Warning : Device Offline ");
    }

    /**
     * Executes a command line command and returns the output as a String
     *
     * @return String the command output
     */
    public static String execCmd(String cmd) {
        try {
            Scanner s = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
            return s.hasNext() ? s.next() : ""; // Implement close logic by storing in separate string before returning
        } catch (Exception e) {
            System.err.println("Error in Executing Cmd ");
            e.printStackTrace();
            return "";
        }
    }

	public static boolean isIos(String udid)
	{
		return udid.length()>25;	// || udid.contains("-");
	}

	public static void writeToXmlFile(XmlSuite suite){
		File f = new File("./scripts.xml");

		System.out.println("Writing to file \n"+suite.toXml());
		try
		{
			FileWriter writer = new FileWriter(f);
			writer.write(suite.toXml());
			writer.flush();
			writer.close();
		}
		catch (IOException localIOException) {localIOException.printStackTrace();}
		try
		{
			Thread.sleep(2000L);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println("TestNG file generated");
	}

}
