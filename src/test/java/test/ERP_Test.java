package test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import pages.Enquiry;
import pages.Ind_Cust_Upload;
import pages.Login;
import pages.Quotation;
import pages.Task;

public class ERP_Test {

	WebDriver driver;
	ExtentHtmlReporter reporter;
	ExtentTest test;
	ExtentReports extent;
	boolean isDataDriven = false;

	@BeforeTest
	public void urlloading() {

		reporter = new ExtentHtmlReporter("./Reporter/ERP CRM Report.html");
		reporter.config().setDocumentTitle("Automation Report");
		reporter.config().setReportName("ERP Report");
		reporter.config().setTheme(Theme.STANDARD);
		extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Hostname", "localhost");
		extent.setSystemInfo("OS", "windows11");
		extent.setSystemInfo("Tester Name", " ");
		extent.setSystemInfo("Browser Name", "Chrome");
		extent.setSystemInfo("Tool & Technology", "Selenium with POM Design");

		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--remote-allow-origins=*");
//		options.addArguments("--disable-notifications");

		Map<String, Object> prefs = new HashMap<>();
		prefs.put("profile.default_content_setting_values.notifications", 1); // 1 = allow
		options.setExperimentalOption("prefs", prefs);
		driver = new ChromeDriver(options);

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get("https://erptest.prog-biz.com/");

	}
	
//	@Test(priority = 0)
	public void data_driven_testing() throws Exception {
	    isDataDriven = true;

		test = extent.createTest("Data Driven testing");
		Login ddt = new Login(driver, test);
		ddt.data_driven_test();
	}

	@Test(priority = 1)
	public void login_test() throws Exception {

		test = extent.createTest("Login with valid credentials");
		Login lg = new Login(driver, test);
		lg.login();

	}
	
//	@Test(priority = 2)
	public void enquiry_create() throws Exception {

		test = extent.createTest("Create enquiry");
		Enquiry enq = new Enquiry(driver, test);
		enq.enquiry();
	}

//	@Test(priority = 3)
	public void enquiry_overview() throws Exception {

		test = extent.createTest("Enquiry overview");
		Enquiry enq = new Enquiry(driver, test);
		enq.enquiry_overview();

	}

//	@Test(priority = 4)
	public void edit_enquiry() throws Exception {
		test = extent.createTest("Edit enquiry");
		Enquiry enq = new Enquiry(driver, test);
		enq.edit_enquiry();

	}

//	@Test(priority = 5)
	public void followup_from_enquiry_overiew() throws Exception {

		test = extent.createTest("Enquiry followup from the enquiry overview");
		Enquiry enq = new Enquiry(driver, test);
		enq.enquiry_followup();

	}

//	@Test(priority = 6)
	public void followup_details() throws Exception {

		test = extent.createTest("Edit and delete a followup");
		Enquiry enq = new Enquiry(driver, test);
		enq.followup_edit_delete();

	}

//	@Test(priority = 7)
	public void delete_enquiry() throws Exception {

		test = extent.createTest("Delete enquiry");
		Enquiry enq = new Enquiry(driver, test);
		enq.enquiry_delete();
	}

//	@Test(priority = 8)
	public void enquiry_hot() throws Exception {

		test = extent.createTest("Make enquiry hot");
		Enquiry enq = new Enquiry(driver, test);
		enq.enquiry_hot_followup();
	}

//	@Test(priority = 9)
	public void quotation() throws Exception {

		test = extent.createTest("Quotation Test");
		Quotation qt = new Quotation(driver, test);
		qt.quotation();
	}

//	@Test(priority = 10)
	public void edit_quotation() throws Exception {

		test = extent.createTest("Edit Quotation");
		Quotation qt = new Quotation(driver, test);
		qt.edit_quotation();
	}

//	@Test(priority = 11)
	public void view_quotation() throws Exception {

		test = extent.createTest("View Quotation");
		Quotation qt = new Quotation(driver, test);
		qt.view_quotation();
	}

//	@Test(priority = 12)
	public void quotation_followup() throws Exception {

		test = extent.createTest("Quotation Followup");
		Quotation qt = new Quotation(driver, test);
		qt.quotation_followup();
	}

//	@Test(priority = 13)
	public void task_test() throws Exception {
		test=extent.createTest("Instant task creation and end");
		Task task= new Task(driver);
		task.create_instant_task();
		task.task();
	}
	
//	@Test(priority = 14)
	public void individual_customer_upload() throws Exception {
		test = extent.createTest("Individual customer upload");
		Ind_Cust_Upload ind = new pages.Ind_Cust_Upload(driver,test);
		ind.without_branch();
		ind.without_file();
		ind.upload_valid_ind_customer();
		ind.upload_invalid_ind_customer();
		ind.upload_mixed_ind_customer();
	}
	
	@Test(priority = 15)
	public void business_customer_upload() throws Exception {
        test = extent.createTest("Business customer upload");
        pages.BCustomer_Upload bus = new pages.BCustomer_Upload(driver,test);
        bus.excelUploadMenu();
        bus.uploadWithoutBranch();
        bus.uploadWithoutFile();
        bus.uploadValidBusinessCustomer();
        bus.uploadInvalidBusinessCustomer();
        bus.uploadMixedBusinessCustomer();
    }
	
	@AfterTest
	public void teardown() {
		extent.flush();
	}

	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {

	    // 🔒 If this test has child nodes, it is a data-driven test
	    if (test.getModel().hasChildren()) {

	        // Log only if the method itself crashed
	        if (result.getStatus() == ITestResult.FAILURE) {
	            test.log(Status.FAIL,
	                    "Data-driven test crashed: " + result.getThrowable());
	        }

	        return; // ⛔ absolutely nothing else must run
	    }

	    // ✅ Normal (non data-driven) tests only
	    String methodName = result.getMethod().getMethodName();

	    if (result.getStatus() == ITestResult.FAILURE) {

	        test.log(Status.FAIL, "Test case failed: " + methodName);
	        test.log(Status.FAIL, result.getThrowable());

	        String screenshotPath =
	                ERP_Test.screenshotMethod(driver, methodName);
	        test.addScreenCaptureFromPath(screenshotPath);

	    } else if (result.getStatus() == ITestResult.SKIP) {

	        test.log(Status.SKIP, "Test case skipped: " + methodName);
	        String screenshotPath =
	                ERP_Test.screenshotMethod(driver, methodName);
	        test.addScreenCaptureFromPath(screenshotPath);


	    } else if (result.getStatus() == ITestResult.SUCCESS) {

//	        test.log(Status.PASS, "Test case passed: " + methodName);
	        String screenshotPath =
	                ERP_Test.screenshotMethod(driver, methodName);
	        test.addScreenCaptureFromPath(screenshotPath);

	    }
	}



	public static String screenshotMethod(WebDriver driver, String screenshotName) throws IOException {

	    File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

	    String destination = "Screenshot/" + screenshotName + ".png";
	    File destFile = new File(System.getProperty("user.dir"), destination);

	    FileHandler.copy(src, destFile);

	    return destination; // 🔑 relative path
	}


}
