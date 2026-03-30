package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Master_Page {

	WebDriver driver;
	
//	Object repository
	By master_menu=By.xpath("//span[@class='side-menu__label' and text()='Master']");
	By usertype=By.xpath("//a[@href='usertypes']");
	
	
	By usertypes_table=By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/div/div/div[2]/div/div/table");
	
	
//	Constrcutor
	public Master_Page(WebDriver driver) {
		this.driver=driver;
	}
	
	public void role_assigning() throws Exception {
		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(master_menu)).click();
		
		Thread.sleep(2000);
		driver.findElement(usertype).click();
		
		
		List<WebElement> rows=driver.findElement(usertypes_table).findElements(By.tagName("tr"));
//		String value_to_find="Test Uer Types";
		
		for(int i=0;i<=rows.size();i++) {
			
		}
		
	}
	
}
