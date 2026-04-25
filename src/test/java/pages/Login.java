package pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class Login {

	WebDriver driver;
	ExtentTest test1;
	
//	Object repository
	By company_code=By.id("companycode");
	By username=By.id("signin-username");
	By password=By.id("signin-password");
	By signin_btn=By.xpath("//*[@id=\"app\"]/div[1]/form/div/div/div/div/div[2]/div[4]/button");
	
//	Constructor
	public Login(WebDriver driver, ExtentTest test1) {
		this.driver=driver;
		this.test1=test1;
	}
	
	//Data driven test method
	public void data_driven_test() throws IOException, InterruptedException {

		File f = new File(System.getProperty("user.dir")+ "/src/test/resources/Datadriven.xlsx");
		System.out.println(System.getProperty("user.dir"));

		FileInputStream fi = new FileInputStream(f);
	    XSSFWorkbook wb = new XSSFWorkbook(fi);
	    XSSFSheet sh = wb.getSheet("Sheet1");

	    DataFormatter formatter = new DataFormatter();

	    System.out.println("Total rows found = " + sh.getLastRowNum());

	    for (int i = 1; i <= sh.getLastRowNum(); i++) {

	        // Create node FIRST
	        ExtentTest iterationTest =
	                test1.createNode("Login Test - Row " + i);

	        String Companycode = formatter.formatCellValue(sh.getRow(i).getCell(0));
	        String Username    = formatter.formatCellValue(sh.getRow(i).getCell(1));
	        String Password    = formatter.formatCellValue(sh.getRow(i).getCell(2));

	        iterationTest.log(Status.INFO, "Company Code: " + Companycode);
	        iterationTest.log(Status.INFO, "Username: " + Username);
	        iterationTest.log(Status.INFO, "Password: " + Password);

	        // Ensure we are on login page
	        driver.get("https://erptest.prog-biz.com/");
	        Thread.sleep(1500);

	        driver.findElement(company_code).clear();
	        driver.findElement(company_code).sendKeys(Companycode);

	        driver.findElement(username).clear();
	        driver.findElement(username).sendKeys(Username);

	        driver.findElement(password).clear();
	        driver.findElement(password).sendKeys(Password);

	        driver.findElement(signin_btn).click();
	        Thread.sleep(2000);

	        String currentURL = driver.getCurrentUrl();
	        iterationTest.log(Status.INFO, "URL after login: " + currentURL);

	        if (currentURL.contains("home") || currentURL.contains("dashboard")) {
	            iterationTest.fail("Login successful with invalid credentials");
	        } else {
	            iterationTest.pass("Login failed. Stayed on: " + currentURL);
	        }

	    }

	    wb.close();
	    fi.close();
	}



	
	public void login()throws Exception {
		
//		driver.navigate().refresh();
		Thread.sleep(2000);
		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOfElementLocated(company_code)).click();
		
		driver.findElement(company_code).sendKeys("JM_TEST");
		driver.findElement(username).sendKeys("Admin");
		driver.findElement(password).sendKeys("123");
		
		test1.log(Status.PASS, "Company code : JM_TEST");
		test1.log(Status.PASS, "Username : Admin");
		test1.log(Status.PASS, "Password : 123");
		
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(signin_btn));
				
		driver.findElement(signin_btn).click();
		
		Thread.sleep(3000);
        String currentURL = driver.getCurrentUrl();

        if (currentURL.contains("dashboard") || currentURL.contains("home")) {
        	System.out.println("Login successful - redirected to dashboard : "+currentURL);
            test1.log(Status.PASS, "Login successful — redirected to dashboard : " + currentURL);
        } else {
        	driver.quit();
            test1.log(Status.FAIL, "Login failed — did not reach dashboard");
        }
//		Login ends

		
	}
	
}
