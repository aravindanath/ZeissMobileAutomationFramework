package ZeissApp.library;

import com.relevantcodes.extentreports.ExtentReports;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

	static ExtentReports extent;
	
	static String path;

    static String filePath="";

    public static String screenPath;
    
    public static String timeStamp;
    
    private static String configXLPath=System.getProperty("user.dir")+File.separator+"config"+File.separator+"config.xlsx";

    public static synchronized void generateFilePath() {
    	
    	if(BaseTestFunctions.deviceTrialRun) return;
    	
    	timeStamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date()); 
    	path=System.getProperty("user.dir");
    	filePath=path+File.separator+"mobile-report"+File.separator+"Report_"+timeStamp+File.separator+"Report_"+timeStamp+".html";
    	screenPath=path+File.separator+"mobile-report"+File.separator+"Report_"+timeStamp+"/";
		System.out.println("Report File : "+filePath);
	}
    
    /**
     * Gets the reporter for Extent Reports.
     *
     * @return the reporter
     */
    public static synchronized ExtentReports getReporter() {
        if (extent == null) {
            extent = new ExtentReports(filePath, true);
                extent.loadConfig(new File(System.getProperty("user.dir")+File.separator+"config"+File.separator+"extent-config.xml"));
            System.out.println("Extent Initialized");
        }        
        return extent;
    }  




		
    }
    
    

