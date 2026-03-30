package pages;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class Quotation {

	
	WebDriver driver;
	ExtentTest test2;
	public static String qt_no;

	
//	Object repository
	
	By create_quotation_btn = By.id("btn-create-quotation");
	By quotation_valid_upto=By.id("expdate");
	By next_followup_date = By.id("firstfollowupdate");
	By lead_qualiy_quotation = By.id("quotation-quality");
	By quo_no=By.id("quotation-no");
	By billing_address_dropdown=By.id("billingaddress");
	By description = By.xpath("//textarea[@rows='3' and contains(@class,'textarea')]");
	By item_description=By.xpath("(//textarea[contains(@class,'textarea')])[1]");
	By rate = By.xpath("(//*[contains(@id,'item-rate')])[1]");
	By quotation_save_btn=By.id("btn-save-quotation");
	
	By edit_quotation=By.xpath("//button[normalize-space()='Edit Quotation']");
	By viewBtn = By.xpath("//a[.//i[contains(@class,'ri-eye-line')]]");

	By followupBtn = By.id("btn-add-followup");
	By followupStatus = By.id("followup-status");
	By qtn_description = By.id("followup-description");
	By saveBtn = By.id("btn-save-followup");

		
	public Quotation(WebDriver driver, ExtentTest test2) {
		this.driver=driver;
		this.test2=test2;
		
	}
//	Methods
	public void quotation()throws Exception {
        
        driver.findElement(create_quotation_btn).click();

		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(quotation_valid_upto));
		
		
		LocalDate today = LocalDate.now();

		// Get last day of current month
		LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());

		// Format it as dd-MM-yyyy
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDate = lastDay.format(fmt);

		// Pass to the field
		driver.findElement(quotation_valid_upto).clear();
		driver.findElement(quotation_valid_upto).sendKeys(formattedDate);		
//		WebElement Billing_address=driver.findElement(billing_address_dropdown);
		
		WebElement dateTimeField = driver.findElement(next_followup_date);

		// Get current date-time
		LocalDateTime currentTime = LocalDateTime.now();

		// Add 2 minutes
		LocalDateTime finalTime = currentTime.plusMinutes(2);

		// Format for input[type="datetime-local"]
		DateTimeFormatter formatter =
		        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

		String valueToSet = finalTime.format(formatter);

		// Set value using JS and trigger events
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
		        "arguments[0].value = arguments[1];" +
		        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
		        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
		        dateTimeField,
		        valueToSet
		);
		
		WebElement Quo_no=driver.findElement(quo_no);
		Actions act=new Actions(driver);
        act.click(Quo_no).perform();
        act.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
        act.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        qt_no = (String) clipboard.getData(DataFlavor.stringFlavor);

        System.out.println("Copied text: " + qt_no);

		
		if (qt_no.length()>0)
		{
			test2.log(Status.PASS, "Quotation no : "+qt_no);
		}
		else
		{
			test2.log(Status.FAIL, "The quotation number is not present");
		}
		
	List<WebElement> Lead_Quality = driver.findElements(lead_qualiy_quotation);
	if(Lead_Quality.size()>0)
	{
		Select select = new Select(driver.findElement(lead_qualiy_quotation));
		select.selectByVisibleText("Cold");
		test2.log(Status.INFO, "Lead quality selected as hot");
	} else {
		test2.log(Status.INFO, "Lead quality field is not present");
	}
		
		Thread.sleep(2000);
		WebElement Description = driver.findElement(description);
	   ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", Description);

		Actions act_desc=new Actions(driver);
		act_desc.click(Description).perform();
		act_desc.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
		act_desc.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();
		
		String desc_text = (String) clipboard.getData(DataFlavor.stringFlavor);
		

        System.out.println("Text in the textarea: " + desc_text);
		
		if(desc_text.length()>0)
		{
			test2.log(Status.INFO, "Description already entered : "+desc_text);
		}
		else
		{
			Description.sendKeys("test description");
			test2.log(Status.INFO, "The description field is empty and value passed");
		}
		
		
		Thread.sleep(2000);
		
//		WebElement Item_Description = driver.findElement(item_description);
//		String Item_desc_script = "return arguments[0].value;";
//        String Item_Desc = (String) js.executeScript(Item_desc_script, Item_Description);
//
//        System.out.println("Text in the textarea: " + Item_Desc);
//		
//		if(Item_Desc.length()>0)
//		{
//			test2.log(Status.INFO, "Item Description already entered : "+Item_Desc);
//		}
//		else
//		{
//			Item_Description.sendKeys("test description");
//			test2.log(Status.INFO, "The item description field is empty and value passed");
//		}

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", driver.findElement(rate));

		driver.findElement(rate).clear();
		driver.findElement(rate).sendKeys("10000");
		

		WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(quotation_save_btn));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", saveBtn);

		act.moveToElement(saveBtn).click().perform();		
		
		test2.log(Status.PASS, "Quotation created successfully");
		
		
	}
	
	public void edit_quotation() throws Exception {
		
		Actions act=new Actions(driver);

        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(edit_quotation));
        act.moveToElement(driver.findElement(edit_quotation)).click().perform();
        Thread.sleep(2000);
        JavascriptExecutor js=(JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", driver.findElement(quotation_save_btn));
		act.moveToElement(driver.findElement(quotation_save_btn)).click().perform();
		

		test2.log(Status.PASS, "Quotation edited successfully");
	}
	
	public void view_quotation()throws Exception {
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(viewBtn));
		// Store the current tab
		String mainTab = driver.getWindowHandle();

		// Click the view button
		driver.findElement(viewBtn).click();

		// Wait for new tab to appear
		wait.until(driver -> driver.getWindowHandles().size() > 1);

		// Switch to new tab
		for (String tab : driver.getWindowHandles()) {
		    if (!tab.equals(mainTab)) {
		        driver.switchTo().window(tab);
		        break;
		    }
		}

		System.out.println("New tab opened successfully");

		// Hold for 3 seconds
		Thread.sleep(3000);

		// Close the new tab
		driver.close();

		// Switch back to main tab
		driver.switchTo().window(mainTab);
		System.out.println("Returned to main tab");

	}
	
	public void quotation_followup() throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		wait.until(ExpectedConditions.visibilityOfElementLocated(followupBtn)).click();

		Thread.sleep(2000);

		WebElement dropdown = driver.findElement(followupStatus);
		Select select = new Select(dropdown);

//		for (WebElement option : select.getOptions()) {
//		    if (option.getText().contains("Customer")) {
//		        option.click();
//		        break;
//		    }
//		}

		WebElement firstGreenOption = driver.findElement(
			    By.xpath("(//option[contains(@style,'green')])[1]")
			);

			firstGreenOption.click();  //Clicking the first Won option from the lead status dropdown
			
		WebElement description_field = driver.findElement(qtn_description);
		description_field.sendKeys("Followup done");

		Thread.sleep(2000);
		List<WebElement> saveButtons =
		        driver.findElements(By.xpath("//button[normalize-space()='Save']"));

		for (WebElement btn : saveButtons) {
		    if (btn.isDisplayed() && btn.isEnabled()) {
		        btn.click();
		        System.out.println("Clicked visible Save button");
		        break;
		    }
		}

		test2.log(Status.PASS, "Quotation followup saved successfully");
	}
}
