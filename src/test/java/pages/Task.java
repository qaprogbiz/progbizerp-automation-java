package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;

public class Task {

	
	WebDriver driver;

//	Object repository
	
//	By company_code=By.id("companycode");
//	By username=By.id("signin-username");
//	By password=By.id("signin-password");
//	By signin_btn=By.xpath("//*[@id=\"app\"]/div[1]/form/div/div/div/div/div[2]/div[4]/button");
	
//	By themeToggle = By.cssSelector("a.layout-setting");
	
	By home=By.id("nav-home");
	
	By create_new= By.id("new-task");
	By create_task= By.id("new-task-item");
	By task_type= By.id("taskType");
	By task_name = By.id("taskName");
	By task_save_btn= By.id("saveBtn");
	By confirm_task_save = By.cssSelector("button.swal2-confirm");
	
	
	By running_tasks= By.id("runningTasksSection");
	By running_task = By.xpath("//h4[normalize-space()='Automation Task 1.0']");
	By close_task_modal= By.cssSelector("div.modal.show button[aria-label='Close']");
	By pause_task= By.xpath("(//button[@aria-label='Pause Task'])[1]");
	By pause_confirm_btn= By.xpath("//*[@id=\"HomeTaskHold\"]/div/form/div/div[3]/button");
	By end_task= By.xpath("//h4[normalize-space()='Automation Task 1.0']" +"/ancestor::div[contains(@class,'d-flex')]" +"//button[@aria-label='End Task']");
	
//	Constructor
	public Task(WebDriver driver) {
		this.driver = driver;
	}
	
//	Methods
	
//	Login
//	public void login()throws Exception {
//		
//		Thread.sleep(2000);
//		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(20));
//		wait.until(ExpectedConditions.visibilityOfElementLocated(company_code)).click();
//		
//		driver.findElement(company_code).sendKeys("JMTest");
//		driver.findElement(username).sendKeys("admin");
//		driver.findElement(password).sendKeys("123");
//		
//		
//		Thread.sleep(1000);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(signin_btn));
//				
//		driver.findElement(signin_btn).click();
////		Login ends	
//	}

	
//	Create instant Task page
	public void create_instant_task() throws Exception {
		WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(home)).click();
	    
		WebElement Create_new= driver.findElement(create_new);
		WebElement Create_task=driver.findElement(create_task);
		
		
		Create_new.click();
		Create_task.click();
		Thread.sleep(2000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(task_type));
		WebElement Task_type= driver.findElement(task_type);
		Select sel= new Select(Task_type);
		sel.selectByValue("5");
		
		WebElement Task_Name = driver.findElement(task_name);
		WebElement Save_Task=driver.findElement(task_save_btn);

		Task_Name.sendKeys("Automation Task 1.0");
		Save_Task.click();
		wait.until(ExpectedConditions.elementToBeClickable(confirm_task_save)).click();
		
	}
	
//	Task actions
	public void task() throws Exception{
		
		WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(10));
	    Thread.sleep(2000);
	    driver.findElement(running_task).click();
	    
	    Thread.sleep(2000);
	    
	    List<WebElement> Close_Modal = driver.findElements(By.xpath("//*[@id=\"task-overview-modal\"]/div/div/div[1]/button"));
	    if (Close_Modal.size()>0) {
	    	System.out.println("Close task modal is displayed");
	    	
	    	
	    	WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(close_task_modal));

	    		closeBtn.click();
		    
	    }
	    else {
	    	System.out.println("Close task modal is not displayed");
	    }
	    List<WebElement> Pause=driver.findElements(pause_task);
	    if(Pause.size()>0) {
	    	System.out.println("Pause button is present");
	    wait.until(ExpectedConditions.elementToBeClickable(pause_task)).click();
	    wait.until(ExpectedConditions.elementToBeClickable(pause_confirm_btn)).click();
	    }
	    else {
	    	System.out.println("Pause button is not present");
	    }
	    
//	    List<WebElement> Resume= driver.findElements(close_task_modal);
	    
	    Thread.sleep(2000);
	    wait.until(ExpectedConditions.elementToBeClickable(end_task)).click();
		
	}
}
