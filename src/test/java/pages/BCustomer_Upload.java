package pages;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class BCustomer_Upload {

	
	WebDriver driver;
	ExtentTest test4;
	
//	Object repository
//	Object repository
	By excel_upload_menu = By.id("nav-excel-upload");
	By business_upload_btn = By.id("nav-excel-upload-business-customer");
	By branch = By.id("branch");
	
	By upload_btn = By.xpath("//button[contains(text(),'Upload')]");
	By branch_validation =By.xpath("//div[contains(@class,'validation-message') and contains(text(),'Please choose branch')]");
	By file_validation = By.id("swal2-content");
	By file_validation_ok_btn= By.xpath("//button[contains(@class,'swal2-confirm') and normalize-space()='OK']");
	
    By firstRow = By.xpath("//table//tbody//tr[1]");
    By upload_status=By.xpath("./td[5]");
    By table_error= By.xpath("./td[6]");
    By upload_file = By.xpath("//input[@type='file']");
	
	public BCustomer_Upload(WebDriver driver, ExtentTest test4) {
		this.driver = driver;
		this.test4 = test4;
	}
	
	public void excelUploadMenu() {
		
		WebElement ExcelUploadMenu = driver.findElement(excel_upload_menu);
		WebElement BusinessUploadBtn = driver.findElement(business_upload_btn);
		
		if (BusinessUploadBtn.isDisplayed()){
			System.out.println("Excel upload menu is displayed");
		}
		else {
			System.out.println("Excel upload menu is not displayed");
			ExcelUploadMenu.click();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(business_upload_btn)).click();
		}
		
	}
	
	public void uploadWithoutBranch() throws Exception {
		Thread.sleep(2000);

		driver.findElement(upload_btn).click();
		Thread.sleep(2000);
		WebElement Branch_Validation = driver.findElement(branch_validation);
		if(Branch_Validation.isDisplayed()) {
            System.out.println("Branch validation message is displayed as expected.");
            test4.log(Status.PASS, "Branch validation message is displayed as expected.");
		}
		else {
			System.out.println("Branch validation message is not displayed.");
			test4.log(Status.FAIL, "Branch validation message is not displayed.");
			}		
	}
	
	public void uploadWithoutFile() throws Exception {
		Thread.sleep(2000);

		Select sel= new Select(driver.findElement(branch));
		sel.selectByIndex(1);
		driver.findElement(upload_btn).click();
		Thread.sleep(2000);
		WebElement File_Validation = driver.findElement(file_validation);
		if (File_Validation.isDisplayed()) {
			driver.findElement(file_validation_ok_btn).click();
			System.out.println("File validation message is displayed as expected.");
			test4.log(Status.PASS, "File validation message is displayed as expected.");
		} else {
			System.out.println("File validation message is not displayed.");
			test4.log(Status.FAIL, "File validation message is not displayed.");
		}
	}
	
	public static String getUniquecompanyName() {
		return "TST_COMP_" +UUID.randomUUID().toString().substring(0,8);
	}
	
	public static String getUniqueContactPerson() {
		return "John_" +UUID.randomUUID().toString().substring(0,8);
	}
	
	public static String getuniqueContactNumber() {
        Random rand = new Random();
		return "8" + (100000000 + rand.nextInt(900000000));
	}
	
    public static String getUniquePhone() {
        Random rand = new Random();
        return "9" + (100000000 + rand.nextInt(900000000));
    }

    public static String createDynamicCustomerFile() throws Exception {

    	String filePath = System.getProperty("user.dir") + "/testdata/valid_business_contact_File.xlsx";
    	
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Customers");

        String[] headers = {"CompanyName*","ContactPerson*","ContactNo*","ContactEmail","Tax Reg No",
        	    "Phone*","EmailAddress","Grade*"};

        // Header
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        // Row with ONLY required fields
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(getUniquecompanyName());   
        row.createCell(1).setCellValue(getUniqueContactPerson());
        row.createCell(2).setCellValue(getuniqueContactNumber());
        row.createCell(5).setCellValue(getUniquePhone());
        row.createCell(7).setCellValue("Suspect");

        // Optional fields left empty intentionally

        FileOutputStream fos = new FileOutputStream(filePath);
        wb.write(fos);
        wb.close();
        fos.close();

        return filePath;
    }
    
	
	public void uploadValidBusinessCustomer() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Select select = new Select(driver.findElement(branch));
        select.selectByIndex(1);

        String filePath = createDynamicCustomerFile();

        driver.findElement(upload_file).sendKeys(filePath);

        driver.findElement(upload_btn).click();

//      test3.log(Status.INFO, "Individual customer file uploaded");
        Thread.sleep(2000);
		driver.navigate().refresh();
		
		Thread.sleep(1000);
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(firstRow));

        String status = row.findElement(upload_status).getText().trim();
        String errorCount = row.findElement(table_error).getText().trim();
        System.out.println("Status : " + status+" :: Errors : " + errorCount);

        if (status.equalsIgnoreCase("Uploaded") && errorCount.equals("0")) {
            test4.log(Status.PASS, "Upload successful with zero errors");
        } else {
            test4.log(Status.FAIL, "Upload failed. Status: " + status + ", Errors: " + errorCount);
        }		
	}
	
	public static String create_invalid_business_file() throws Exception {

		String filePath = System.getProperty("user.dir")+ "/testdata/invalid_business_contact_file.xlsx";
	    Workbook wb = new XSSFWorkbook();
	    Sheet sheet = wb.createSheet("Customers");

	    String[] headers = {"CompanyName*", "ContactPerson*", "ContactNo*", "ContactEmail",
	            "Tax Reg No", "Phone*", "EmailAddress", "Grade*"};

	    // Sample valid data for all fields
	    String validCompany     = "Acme Corp";
	    String validContact     = "John Doe";
	    String validContactNo   = "9876543210";
	    String validContactEmail= "contact@acme.com";
	    String validTaxReg      = "TAX123456";
	    String validPhone       = "0123456789";
	    String validEmail       = "info@acme.com";
	    String validGrade       = "A";

	    // Header row
	    Row header = sheet.createRow(0);
	    for (int i = 0; i < headers.length; i++) {
	        header.createCell(i).setCellValue(headers[i]);
	    }

	    // Required fields (by index): CompanyName*(0), ContactPerson*(1), ContactNo*(2), Phone*(5), Grade*(7)
	    // Each row leaves one required field blank

	    // Row 1 — Missing CompanyName* (index 0)
	    Row row1 = sheet.createRow(1);
	    row1.createCell(0).setCellValue("");             // MISSING
	    row1.createCell(1).setCellValue(validContact);
	    row1.createCell(2).setCellValue(validContactNo);
	    row1.createCell(3).setCellValue(validContactEmail);
	    row1.createCell(4).setCellValue(validTaxReg);
	    row1.createCell(5).setCellValue(validPhone);
	    row1.createCell(6).setCellValue(validEmail);
	    row1.createCell(7).setCellValue(validGrade);

	    // Row 2 — Missing ContactPerson* (index 1)
	    Row row2 = sheet.createRow(2);
	    row2.createCell(0).setCellValue(validCompany);
	    row2.createCell(1).setCellValue("");             // MISSING
	    row2.createCell(2).setCellValue(validContactNo);
	    row2.createCell(3).setCellValue(validContactEmail);
	    row2.createCell(4).setCellValue(validTaxReg);
	    row2.createCell(5).setCellValue(validPhone);
	    row2.createCell(6).setCellValue(validEmail);
	    row2.createCell(7).setCellValue(validGrade);

	    // Row 3 — Missing ContactNo* (index 2)
	    Row row3 = sheet.createRow(3);
	    row3.createCell(0).setCellValue(validCompany);
	    row3.createCell(1).setCellValue(validContact);
	    row3.createCell(2).setCellValue("");             // MISSING
	    row3.createCell(3).setCellValue(validContactEmail);
	    row3.createCell(4).setCellValue(validTaxReg);
	    row3.createCell(5).setCellValue(validPhone);
	    row3.createCell(6).setCellValue(validEmail);
	    row3.createCell(7).setCellValue(validGrade);

	    // Row 4 — Missing Phone* (index 5)
	    Row row4 = sheet.createRow(4);
	    row4.createCell(0).setCellValue(validCompany);
	    row4.createCell(1).setCellValue(validContact);
	    row4.createCell(2).setCellValue(validContactNo);
	    row4.createCell(3).setCellValue(validContactEmail);
	    row4.createCell(4).setCellValue(validTaxReg);
	    row4.createCell(5).setCellValue("");             // MISSING
	    row4.createCell(6).setCellValue(validEmail);
	    row4.createCell(7).setCellValue(validGrade);

	    // Row 5 — Missing Grade* (index 7)
	    Row row5 = sheet.createRow(5);
	    row5.createCell(0).setCellValue(validCompany);
	    row5.createCell(1).setCellValue(validContact);
	    row5.createCell(2).setCellValue(validContactNo);
	    row5.createCell(3).setCellValue(validContactEmail);
	    row5.createCell(4).setCellValue(validTaxReg);
	    row5.createCell(5).setCellValue(validPhone);
	    row5.createCell(6).setCellValue(validEmail);
	    row5.createCell(7).setCellValue("");             // MISSING
	    
	    FileOutputStream fos = new FileOutputStream(filePath);
	    wb.write(fos);
	    wb.close();
	    fos.close();

	    return filePath;
	}
	
	public void uploadInvalidBusinessCustomer() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		Select select = new Select(driver.findElement(branch));
		select.selectByIndex(1);

		String filePath = create_invalid_business_file();

		driver.findElement(upload_file).sendKeys(filePath);

		driver.findElement(upload_btn).click();

		Thread.sleep(2000);
		driver.navigate().refresh();
		
		Thread.sleep(1000);

		WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(firstRow));

		String status = row.findElement(upload_status).getText().trim();
		String errorCount = row.findElement(table_error).getText().trim();
		System.out.println("Status : " + status + " :: Errors : " + errorCount);

		if (status.equalsIgnoreCase("Has Error") && errorCount.equals("5")) {
			test4.log(Status.PASS, "Upload processed with expected errors for invalid data");
		} else {
			test4.log(Status.FAIL, "Upload did not process as expected. Status: " + status + ", Errors: " + errorCount);
		}
	}
	
	public static String create_mixed_file() throws Exception {

	    String filePath = System.getProperty("user.dir") + "/testdata/mixed_business_contact_file.xlsx";
	    Workbook wb = new XSSFWorkbook();
	    Sheet sheet = wb.createSheet("Customers");

	    String[] headers = {"CompanyName*", "ContactPerson*", "ContactNo*", "ContactEmail",
	            "Tax Reg No", "Phone*", "EmailAddress", "Grade*"};

	    // Header row
	    Row header = sheet.createRow(0);
	    for (int i = 0; i < headers.length; i++) {
	        header.createCell(i).setCellValue(headers[i]);
	    }

	    // Row 1 — Valid row (all required fields filled)
	    Row validRow = sheet.createRow(1);
	    validRow.createCell(0).setCellValue(getUniquecompanyName());
	    validRow.createCell(1).setCellValue(getUniqueContactPerson());
	    validRow.createCell(2).setCellValue(getuniqueContactNumber());
	    validRow.createCell(5).setCellValue(getUniquePhone());
	    validRow.createCell(7).setCellValue("Suspect");

	    // Row 2 — Invalid row (missing CompanyName*)
	    Row invalidRow = sheet.createRow(2);
	    invalidRow.createCell(0).setCellValue("");           // MISSING CompanyName*
	    invalidRow.createCell(1).setCellValue("Jane Smith");
	    invalidRow.createCell(2).setCellValue("9123456789");
	    invalidRow.createCell(3).setCellValue("jane@globex.com");
	    invalidRow.createCell(4).setCellValue("TAX654321");
	    invalidRow.createCell(5).setCellValue("0198765432");
	    invalidRow.createCell(6).setCellValue("hello@globex.com");
	    invalidRow.createCell(7).setCellValue("B");

        FileOutputStream fos = new FileOutputStream(filePath);
        wb.write(fos);
        wb.close();
        fos.close();

        return filePath;

	}
	
	public void uploadMixedBusinessCustomer() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		Select select = new Select(driver.findElement(branch));
		select.selectByIndex(1);

		String filePath = create_mixed_file();

		driver.findElement(upload_file).sendKeys(filePath);

		driver.findElement(upload_btn).click();

		Thread.sleep(2000);
		driver.navigate().refresh();

		Thread.sleep(1000);

		WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(firstRow));

		String status = row.findElement(upload_status).getText().trim();
		String errorCount = row.findElement(table_error).getText().trim();
		System.out.println("Status : " + status + " :: Errors : " + errorCount);

		if (status.equalsIgnoreCase("Has Error") && errorCount.equals("")) {
			test4.log(Status.PASS, "Upload processed with expected results for mixed data");
		} else {
			test4.log(Status.FAIL, "Upload did not process as expected. Status: " + status + ", Errors: " + errorCount);
		}

	}
	
}
