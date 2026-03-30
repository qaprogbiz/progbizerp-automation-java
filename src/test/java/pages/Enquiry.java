package pages;



import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.time.Duration;
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


public class Enquiry {

	WebDriver driver;
	ExtentTest tes;
	public static String enq_no;

	
//	Object repository
	
	By sidebar=By.xpath("/html/body/div[1]/div[1]/header/div/div/div[2]/a");
	By themeToggle = By.xpath("//a[contains(@class,'layout-setting')]");
	
//	By crm=By.xpath("/html/body/div[1]/div[1]/aside/div[2]/div[1]/div[2]/div/div/div/nav/ul/li[4]/a");
	By crm=By.id("nav-crm");
	By new_enquiry=By.id("nav-crm-create-enquiry");
	
	By branchSelect = By.id("nav-master-branch");
	By enquiry_no=By.id("enquiry-number");
	By customer_phone=By.id("customer-phone");
	By customer_name=By.id("TxtCustomer");
	By place=By.id("Place");
	By lead_quality=By.id("lead-quality");
	By lead_source=By.id("leadsource");
	By assign_to=By.id("assignto");
	By multiple_assignee=By.id("assign-to-multiselect");
	By firstLi = By.xpath("//label[normalize-space()='You']/preceding-sibling::input[@type='checkbox']");
	By done = By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[2]/div/form/div/div[2]/div[3]/div/div[1]/div/div/input");
	By remarks=By.id("enquiry-description");
	By enquired_for=By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[2]/div/form/div/div[2]/div[7]/div/div/table/tfoot/tr/td[1]/div/div[1]/div/div[1]");
	By firstRow = By.id("popup-item-row-0");
	By quantity=By.id("new-item-quantity");
	By item_desc_btn = By.id("new-item-description");

	By description= By.xpath("//textarea[@placeholder='Enter description here']");
	By enquiry_save_btn=By.xpath("//button[@class='btn btn-primary-light me-2']");
	By confirm_btn=By.xpath("/html/body/div[9]/div/div[3]/button[1]");
	By confirm_btn1=By.xpath("/html/body/div[15]/div/div[3]/button[1]");
	By leads=By.xpath("//a[@href='leads']");
	By enquiries_table=By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[3]/div/div/div[2]/div[1]/div/div/table");
	
	By edit_enquiry=By.xpath("//button[text()='Edit Enquiry']");
	
	By enquiry_followup_btn=By.xpath("//button[@class='btn btn-sm btn-secondary me-1']");
	By enquiry_followup_date=By.id("followup-date");
	By followup_status=By.xpath("//*[@id=\"followupModal\"]/div/div/form/div[2]/div[3]/div/select");
	By followup_desc=By.id("followup-description");
	By next_followup_date=By.id("next-followup-date");
	By followup_save=By.id("btn-save-followup");
	
	By followup_details=By.id("followups-tab");
	By followup_edit=By.xpath("//a[normalize-space()='Edit']");
	By followup_edit_save_btn=By.xpath("//*[@id=\"followupModal\"]/div/div/form/div[3]/button[3]");
	By followup_delete=By.xpath("//*[@id=\"followups\"]/ul/li/div/div[2]/div/a[2]");
	By confirm_delete_followup=By.xpath("//button[contains(@class,'swal2-confirm') and normalize-space()='Yes, Delete']");
	
	By delete_enquiry=By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[1]/div[2]/button[1]");
	By confirm_enquiry_delete=By.xpath("/html/body/div[22]/div/div[3]/button[1]");
	
//	Constructor
	
    public Enquiry(WebDriver driver, ExtentTest tes) { // Modify constructor arguments
        this.driver = driver;
        this.tes = tes; // Assign the passed ExtentTest instance
    }
	
	
//	Enquiry 
	public void enquiry() throws Exception {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    driver.findElement(themeToggle).click();

	
	        wait.until(ExpectedConditions.visibilityOfElementLocated(crm)).click();
	        Thread.sleep(2000);
	        wait.until(ExpectedConditions.visibilityOfElementLocated(new_enquiry)).click();
	        Thread.sleep(6000);
	        
	        
	        List<WebElement> branches = driver.findElements(branchSelect);

	        if (!branches.isEmpty() && branches.get(0).isDisplayed()) {

	        	Select branchDropdown = new Select(branches.get(0));

	        	long actualOptions = branchDropdown.getOptions()
	        	        .stream()
	        	        .filter(WebElement::isEnabled)
	        	        .count();

	        	System.out.println("Actual branch options (excluding Choose): " + actualOptions);
	        	tes.log(Status.INFO,
	        	        "Actual branch options (excluding Choose): " + actualOptions);

	        } else {
	            System.out.println("Branch selection dropdown not present for this user");
	            tes.log(Status.INFO, "Branch selection dropdown not present for this user");
	        }
//	        Fetching the enquiry number
	        
	        WebElement Enquiry_no=driver.findElement(enquiry_no);
	        Actions act1=new Actions(driver);
	        act1.click(Enquiry_no).perform();
	        act1.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
	        act1.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();
	        
	        Toolkit toolkit = Toolkit.getDefaultToolkit();
	        Clipboard clipboard = toolkit.getSystemClipboard();
	        enq_no = (String) clipboard.getData(DataFlavor.stringFlavor);

	        System.out.println("Copied text: " + enq_no);


	        // Mandatory fields
	        driver.findElement(customer_phone).sendKeys("909090909");
	        tes.log(Status.PASS, "Customer phone number entered");
	        driver.findElement(customer_name).sendKeys("Sunil");
	        tes.log(Status.PASS, "Customer name entered");

	        
//	        Place
	        List<WebElement> Place=driver.findElements(place);
	        if (Place.size()>0) {
				WebElement field=Place.get(0);
				field.sendKeys("Kannur");
				System.out.println("Place field is present");
				tes.log(Status.PASS, "Place field found and value passed.");
			}
	        else {
	        	tes.log(Status.INFO, "Place field is not present");
	        }
	        
//	        Lead quality
	        List<WebElement> Lead_quality=driver.findElements(lead_quality);
	        if(Lead_quality.size()>0)
	        {
	        	WebElement field=Lead_quality.get(0);
                Select sel = new Select(field);
                sel.selectByVisibleText("COLD");
                tes.log(Status.PASS, "Lead quality field found and value passed");
	        }
	        else {
				tes.log(Status.INFO, "Lead quality field is not present");
			}
	        
//		      Lead source
	        
	        List<WebElement> Lead_source=driver.findElements(lead_source);
	        
	        if(Lead_source.size()>0)
	        {
	        	WebElement field=Lead_source.get(0);
	        	Select sel=new Select(field);
	        	sel.selectByIndex(1);
	        	tes.log(Status.PASS, "Lead source field found and value passed");
	        }
	        else
	        {
	        	tes.log(Status.INFO, "Lead source field is not present");
	        }

//	         Assign To 
	        List<WebElement> Single_assignee=driver.findElements(assign_to);
	        if (Single_assignee.size() > 0) {
	            // Case 1: Single Assignee Dropdown
	            WebElement assignTo = Single_assignee.get(0);
	            Select sel1 = new Select(assignTo);
	            sel1.selectByVisibleText("You");
	            System.out.println("Single assignee available");
	            tes.log(Status.PASS, "Single assignee selected");

	        } else {
	            // Try multiple assignee
	            List<WebElement> multipleAssignees = driver.findElements(multiple_assignee);

	            if (multipleAssignees.size() > 0) {
	                // Case 2: Multiple Assignee Option
	                wait.until(ExpectedConditions.elementToBeClickable(multiple_assignee)).click();
	                wait.until(ExpectedConditions.elementToBeClickable(firstLi)).click();
	                Thread.sleep(2000);
	                driver.findElement(done).click();
	                System.out.println("Multiple assignee available");
	                tes.log(Status.PASS, "Multiple assignee selected");

	            } else {
	                // Case 3: No assignee options → Auto-assign enabled
	            	System.out.println("No assignee options available, auto-assign enabled");
	                tes.log(Status.INFO, "Auto-assignee is enabled. Assignee options are not present.");
	            }
	        }


	        Thread.sleep(1000);
//	        WebElement Next_followup_Date = driver.findElement(next_followup_date);
//	        Next_followup_Date.sendKeys("28-02-2026",Keys.TAB,"04:00:PM");
	
	        WebElement dateTimeField = driver.findElement(next_followup_date);

	     LocalDateTime currentTime = LocalDateTime.now();

	     // Add 2 minutes
	     LocalDateTime finalTime = currentTime.plusMinutes(2);
	     
	     DateTimeFormatter formatter =
	             DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

	     String valueToSet = finalTime.format(formatter);

	     JavascriptExecutor js = (JavascriptExecutor) driver;
	     js.executeScript(
	             "arguments[0].value = arguments[1];" +
	             "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
	             "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
	             dateTimeField,
	             valueToSet
	     );

	        // Scroll down
	        JavascriptExecutor js1 = (JavascriptExecutor) driver;
	        js1.executeScript("window.scrollBy(0,document.body.scrollHeight)");


//		      Enquired for
	        Thread.sleep(2000);
	            WebElement Enquired_for = driver.findElement(enquired_for);
	            Actions act=new Actions(driver);
	            act.moveToElement(Enquired_for).click().perform();
	        	wait.until(ExpectedConditions.elementToBeClickable(firstRow)).click();
	            tes.log(Status.PASS, "Enquired for selected");
//			      Remarks
		        
		        List<WebElement> Remarks = driver.findElements(remarks);

		        if (Remarks.size() > 0) 
		        {
		            // Field is present
		            WebElement field = Remarks.get(0);
		            field.sendKeys("remarks Test");
		            tes.log(Status.PASS, "Remarks field found and value passed");
		        }
		        else 
		        {
		        	tes.log(Status.INFO, "Remarks field is not present");
		        }
	        
//		      Quantity
	  
  	        List<WebElement> Quantity = driver.findElements(quantity);

	        if (Quantity.size() > 0) {
	            // Field is present
	            WebElement field = Quantity.get(0);
	            field.clear();
	            Thread.sleep(3000);
	            field.sendKeys("5");
	            tes.log(Status.PASS, "Quantity field found and value passed");
	        } 
	        else 
	        {
	        	tes.log(Status.INFO, "Quantiy field is not present");
	        }
	        	        	        
//Item description test update
	     // Try to find description field
	        List<WebElement> descriptionFields = driver.findElements(description);

	        if (!descriptionFields.isEmpty() && descriptionFields.get(0).isDisplayed()) {

	            // Case 1: Field already open
	            descriptionFields.get(0).sendKeys("Item description test");
	            tes.log(Status.PASS, "Item description field already open. Value entered.");

	        }
	        else {

	            // Field not open, try to find the button
	            List<WebElement> itemDescButtons = driver.findElements(item_desc_btn);

	            if (!itemDescButtons.isEmpty() && itemDescButtons.get(0).isDisplayed()) {

	                // Case 2: Button exists → open field
	                itemDescButtons.get(0).click();
	                tes.log(Status.INFO, "Item description button clicked.");

	                // Wait for field to appear
	                WebElement descriptionField = wait.until(
	                        ExpectedConditions.visibilityOfElementLocated(description)
	                );

	                descriptionField.sendKeys("Item description test");
	                tes.log(Status.PASS, "Item description field opened and value entered.");

	            } else {

	                // Case 3: Neither field nor button present
	                tes.log(Status.INFO, "Item description field and button are not present.");
	            }
	        }

	        Thread.sleep(2000);
	        System.out.println("Saving the enquiry");
	        driver.findElement(enquiry_save_btn).click();
	        tes.log(Status.PASS, "Enquiry save button clicked");
	}



	public void enquiry_overview() throws Exception {
		


		Thread.sleep(2000);
		WebElement Enq_table=driver.findElement(enquiries_table);
		 WebElement Enquiries=driver.findElement(leads);
		 WebElement CRM=driver.findElement(crm);
		 WebElement SideBar=driver.findElement(sidebar);
		 if(Enq_table.isDisplayed())
		 {
				Thread.sleep(6000);
				List<WebElement> rows = driver.findElement(enquiries_table).findElements(By.tagName("tr"));
				

				for (int i = 0; i < rows.size(); i++) {
				    // Re-locate the table and rows each time to avoid stale element
				    WebElement row = driver.findElement(enquiries_table).findElements(By.tagName("tr")).get(i);
				    List<WebElement> cells = row.findElements(By.tagName("td"));

				    for (WebElement cell : cells) {
				        if (cell.getText().equals(enq_no)) {
				            System.out.println("Enquiry found");
				            row.click();
				            return; // Exit after finding and clicking the desired cell
				        }
				    }
				}
		 }
		 
		 else {
			 
		 
		 
		 if(Enquiries.isDisplayed())
		 {
			 Enquiries.click();
		 }
		 else if (CRM.isDisplayed()) {
			 CRM.click();
			 Thread.sleep(2000);
			 Enquiries.click();
			
		}
		 else {
			SideBar.click();
			Thread.sleep(2000);
			CRM.click();
			Thread.sleep(2000);
			Enquiries.click();
		}
				
		
		 
		Thread.sleep(6000);
		List<WebElement> rows = driver.findElement(enquiries_table).findElements(By.tagName("tr"));
	

		for (int i = 0; i < rows.size(); i++) {
		    // Re-locate the table and rows each time to avoid stale element
		    WebElement row = driver.findElement(enquiries_table).findElements(By.tagName("tr")).get(i);
		    List<WebElement> cells = row.findElements(By.tagName("td"));

		    for (WebElement cell : cells) {
		    	if (cell.getText().equals(enq_no)) {
		    	    System.out.println("Enquiry found");
		    	    row.click();
		    	    return;
		    	}

		    }
		}
	  }
	}
	
	public void edit_enquiry() throws Exception {
		
		Thread.sleep(3000);
		
		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOfElementLocated(edit_enquiry)).click();
		
		Thread.sleep(4000);
		
		JavascriptExecutor js=(JavascriptExecutor)driver;
//		js.executeScript("arguments[0].scrollIntoView();",driver.findElement(enquiry_save_btn));
		js.executeScript("window.scrollBy(0,document.body.scrollHeight)");


		Thread.sleep(1000);
		Actions act=new Actions(driver);
		act.moveToElement(driver.findElement(enquiry_save_btn)).click().perform();

	}
	
	public void enquiry_followup() throws Exception {
	    Thread.sleep(2000);
		String currentUrl = driver.getCurrentUrl();
		String expectedPart = "/enquiry-overview/";

		if (currentUrl.contains(expectedPart)) {
			System.out.println("Already on the enqiry overview page");
		}
		else {
	    List<WebElement> rows = driver.findElement(enquiries_table).findElements(By.tagName("tr"));
	    String valueToFind = enq_no;
	    boolean enquiryFound = false;

	    for (int i = 0; i < rows.size(); i++) {
	        // Re-locate the table and rows each time to avoid stale element
	        WebElement row = driver.findElement(enquiries_table).findElements(By.tagName("tr")).get(i);
	        List<WebElement> cells = row.findElements(By.tagName("td"));

	        for (WebElement cell : cells) {
	            if (cell.getText().equals(valueToFind)) {
	                System.out.println("Enquiry found");
	                row.click();
	                enquiryFound = true;
	                break; // Exit the inner loop
	            }
	        }
	        if (enquiryFound) {
	            break; // Exit the outer loop if enquiry is found
	        }
	    }

	    if (!enquiryFound) {
	    	tes.log(Status.FATAL, "Enquiry not found: " + valueToFind);
	        System.out.println("Enquiry not found: " + valueToFind);
	        return; // Exit the method if enquiry is not found
	    }
		}
//End of enquiry searching
	    System.out.println("Waiting for the Followup button to click");
	    Thread.sleep(2000);
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    wait.until(ExpectedConditions.visibilityOfElementLocated(enquiry_followup_btn));

	    Actions act = new Actions(driver);
	    act.moveToElement(driver.findElement(enquiry_followup_btn)).click().perform();

	    System.out.println("The followup button is clicked");

	    Thread.sleep(2000);
	    Select sel = new Select(driver.findElement(followup_status));
	    sel.selectByIndex(1);
	    
	    Thread.sleep(1000);
	    List<WebElement> Lead_Quality = driver.findElements(lead_quality);
		if (Lead_Quality.size() > 0) {
			WebElement Lead_Qty = driver.findElement(lead_quality);
			Lead_Qty.click();
			Select sel1 = new Select(Lead_Qty);
			sel1.selectByVisibleText("Cold");
			tes.log(Status.PASS, "Lead quality field found and value passed in followup.");
		} else {
			tes.log(Status.INFO, "Lead quality field is not present in followup.");
		}
	    driver.findElement(followup_desc).sendKeys("Followup desc");

        WebElement dateTimeField = driver.findElement(next_followup_date);

     LocalDateTime currentTime = LocalDateTime.now();

     // Add 2 minutes
     LocalDateTime finalTime = currentTime.plusMinutes(2);
     
     DateTimeFormatter formatter =
             DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

     String valueToSet = finalTime.format(formatter);

     JavascriptExecutor js = (JavascriptExecutor) driver;
     js.executeScript(
             "arguments[0].value = arguments[1];" +
             "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
             "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
             dateTimeField,
             valueToSet
     );

	    Thread.sleep(2000);
	    driver.findElement(followup_save).click();
	    
	    Thread.sleep(2000);
	}

	
	public void followup_edit_delete() throws Exception {
		
		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(followup_details)).click();
		Thread.sleep(2000);


		wait.until(ExpectedConditions.visibilityOfElementLocated(followup_edit)).click();
		Thread.sleep(1000);
        WebElement dateTimeField = driver.findElement(next_followup_date);

     LocalDateTime currentTime = LocalDateTime.now();

     // Add 2 minutes
     LocalDateTime finalTime = currentTime.plusMinutes(2);
     
     DateTimeFormatter formatter =
             DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

     String valueToSet = finalTime.format(formatter);

     JavascriptExecutor js = (JavascriptExecutor) driver;
     js.executeScript(
             "arguments[0].value = arguments[1];" +
             "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
             "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
             dateTimeField,
             valueToSet
     );

		wait.until(ExpectedConditions.visibilityOfElementLocated(followup_edit_save_btn)).click();
		Thread.sleep(1000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(followup_details)).click();
		Thread.sleep(1000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(followup_delete)).click();
		Thread.sleep(1000);
		
		Actions act=new Actions(driver);
		act.moveToElement(driver.findElement(confirm_delete_followup)).click().perform();
		
	}
	
	public void enquiry_delete() throws Exception {
		
		Thread.sleep(2000);
		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(20));
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(delete_enquiry));

		WebElement delete_enquiry_btn=driver.findElement(delete_enquiry);
		
		String exp_text="Delete Enquiry";
		String act_text=delete_enquiry_btn.getText();
		System.out.println("The text present on the button is "+act_text);
		if(exp_text.equalsIgnoreCase(act_text)) {
			Actions act=new Actions(driver);
			act.moveToElement(delete_enquiry_btn).click().perform();
			
			Thread.sleep(2000);
			driver.findElement(confirm_enquiry_delete).click();
			
			
		}
		else
		{
			tes.log(Status.INFO, "The button is not present, the enquiry nature might not be new");
		}

	}
	
	public void enquiry_hot_followup() throws Exception {
		
		Thread.sleep(2000);
		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOfElementLocated(enquiry_followup_btn)).click();
		
	    Thread.sleep(2000);
	    Select sel = new Select(driver.findElement(followup_status));
	    sel.selectByVisibleText("Hot (Hot)");
	    driver.findElement(followup_desc).sendKeys("Hot lead");
        WebElement dateTimeField = driver.findElement(next_followup_date);

     LocalDateTime currentTime = LocalDateTime.now();

     // Add 2 minutes
     LocalDateTime finalTime = currentTime.plusMinutes(2);
     
     DateTimeFormatter formatter =
             DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

     String valueToSet = finalTime.format(formatter);

     JavascriptExecutor js = (JavascriptExecutor) driver;
     js.executeScript(
             "arguments[0].value = arguments[1];" +
             "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
             "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
             dateTimeField,
             valueToSet
     );

	    Thread.sleep(2000);
	    Actions act=new Actions(driver);
	    act.moveToElement(driver.findElement(followup_save)).perform();
	    Thread.sleep(2000);
	    driver.findElement(followup_save).click();
	    System.out.println("Hot lead triggered");
	    
	    Thread.sleep(2000);
	
	    String actual_url= driver.getCurrentUrl();
	    System.out.println(actual_url+"\n");
	    String quotation_exp_url="https://erptest.prog-biz.com/quotation/0/";
	    boolean isExpected=actual_url.matches(quotation_exp_url+ ".*");
	    if (isExpected) {
			tes.log(Status.PASS, "The quotation page opens");
		}
	    else
	    {
	    	tes.log(Status.FATAL, "The quotation page is not opened after the enquiry is been hot");
	    }
	    
	}

}
