package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class Ind_Cust_Upload {
	
	WebDriver driver;
	ExtentTest test3;
	
//	Object repository
	By excel_upload_menu = By.id("nav-excel-upload");
	By ind_upload_btn = By.id("nav-excel-upload-individual-customer");
	By branch = By.id("branch");
	
	By upload_btn = By.xpath("//button[contains(text(),'Upload')]");
	By branch_validation =By.xpath("//div[contains(@class,'validation-message') and contains(text(),'Please choose branch')]");
	
//	Constructor
	public Ind_Cust_Upload(WebDriver driver,ExtentTest test3) {
		this.driver = driver;
		this.test3= test3;
	}
	
//	Methods
	public void without_branch() throws Exception{
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(excel_upload_menu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(ind_upload_btn)).click();

		driver.findElement(upload_btn).click();
		Thread.sleep(2000);
		WebElement Branch_Validation = driver.findElement(branch_validation);
		if(Branch_Validation.isDisplayed()) {
            System.out.println("Branch validation message is displayed as expected.");
            test3.log(Status.PASS, "Branch validation message is displayed as expected.");
		}
		else {
			System.out.println("Branch validation message is not displayed.");
			test3.log(Status.FAIL, "Branch validation message is not displayed.");
			}
	}
}
