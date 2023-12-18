package ZeissApp.app.testScripts.Patient;

import ZeissApp.app.pagerepo.HomePage;
import ZeissApp.app.pagerepo.LoginPage;
import org.testng.annotations.Test;


/**
 * 
 * @author Arvind
 * 
 *
 */

public class TC_Patient_002 extends BaseTest {
	@Test
	public void TC_Patient_001() throws Exception {

		LoginPage lp = new LoginPage(driver);
		lp.login();
		HomePage hp = new HomePage(driver);
		hp.navigateToPatientPage();

	}


	}

