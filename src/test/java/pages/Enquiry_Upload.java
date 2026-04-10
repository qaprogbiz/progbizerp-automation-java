package pages;

import java.io.FileOutputStream;
import java.util.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
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

public class Enquiry_Upload {

	WebDriver driver;
	ExtentTest test5;
	
//	Object repository
	By excel_upload_menu = By.id("nav-excel-upload");
	By enquiry_upload_btn = By.id("nav-excel-upload-enquiry");
	By branch = By.id("branch");
	
	By upload_btn = By.xpath("//button[contains(text(),'Upload')]");
	By branch_validation =By.xpath("//div[contains(@class,'validation-message') and contains(text(),'Please choose branch')]");
	By file_validation = By.id("swal2-content");
	By file_validation_ok_btn= By.xpath("//button[contains(@class,'swal2-confirm') and normalize-space()='OK']");
	
    By firstRow = By.xpath("//table//tbody//tr[1]");
    By upload_status=By.xpath("./td[5]");
    By table_error= By.xpath("./td[6]");
    By upload_file = By.xpath("//input[@type='file']");

	
	public Enquiry_Upload(WebDriver driver, ExtentTest test5) {
		this.driver = driver;
		this.test5 = test5;
	}
	
//	Methods
	public void enquiryExcelUploadMenu() {
		
		WebElement ExcelUploadMenu = driver.findElement(excel_upload_menu);
		WebElement EnquiryUploadBtn = driver.findElement(enquiry_upload_btn);
		
		if (EnquiryUploadBtn.isDisplayed()){
			System.out.println("Excel upload menu is displayed");
		}
		else {
			System.out.println("Excel upload menu is not displayed");
			ExcelUploadMenu.click();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(enquiry_upload_btn)).click();
		}
		
	}
		
	public void uploadWithoutBranch() throws Exception {
		Thread.sleep(2000);

		driver.findElement(upload_btn).click();
		Thread.sleep(2000);
		WebElement Branch_Validation = driver.findElement(branch_validation);
		if(Branch_Validation.isDisplayed()) {
            System.out.println("Branch validation message is displayed as expected.");
            test5.log(Status.PASS, "Branch validation message is displayed as expected.");
		}
		else {
			System.out.println("Branch validation message is not displayed.");
			test5.log(Status.FAIL, "Branch validation message is not displayed.");
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
			test5.log(Status.PASS, "File validation message is displayed as expected.");
		} else {
			System.out.println("File validation message is not displayed.");
			test5.log(Status.FAIL, "File validation message is not displayed.");
		}
	}
	
	public static String createDynamicEnquiryFile() throws Exception {

	    String filePath = System.getProperty("user.dir") + "/testdata/valid_enquiry_File.xlsx";

	    Workbook wb = new XSSFWorkbook();
	    Sheet sheet = wb.createSheet("Enquiry");

	    String[] headers = {
	        "Date", "Customer Name", "Phone", "Phone2", "Assign to",
	        "Enquired For", "CurrentStatus", "LastFollowupDate",
	        "NextFollowupDate", "EnquiryNo", "Description",
	        "Lead Source", "Lead Quality", "Place"
	    };

	    Row headerRow = sheet.createRow(0);
	    for (int i = 0; i < headers.length; i++) {
	        headerRow.createCell(i).setCellValue(headers[i]);
	    }

	    Row row = sheet.createRow(1);

	    LocalDate todayLocal = LocalDate.now();
	    LocalDate nextLocal = LocalDate.now().plusDays(2);

	    Date todayDate = Date.from(todayLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
	    Date nextDate = Date.from(nextLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

	    CellStyle dateStyle = wb.createCellStyle();
	    CreationHelper createHelper = wb.getCreationHelper();
	    dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

	    Cell dateCell = row.createCell(0);
	    dateCell.setCellValue(todayDate);
	    dateCell.setCellStyle(dateStyle); // Date*

	    row.createCell(1).setCellValue("John Doe"); // Customer Name*
	    row.createCell(2).setCellValue("9988998899"); // Phone*

	    row.createCell(4).setCellValue("Junaid"); // Assign to*
	    row.createCell(5).setCellValue("Artificial grass"); // Enquired For*

	    Cell nextFollowCell = row.createCell(8);
	    nextFollowCell.setCellValue(nextDate);
	    nextFollowCell.setCellStyle(dateStyle); // NextFollowupDate*

	    //  Optional fields
	    row.createCell(3).setCellValue(""); // Phone2
	    row.createCell(6).setCellValue("New"); // CurrentStatus

	    Cell lastFollowCell = row.createCell(7);
	    lastFollowCell.setCellValue(todayDate);
	    lastFollowCell.setCellStyle(dateStyle); // LastFollowupDate

	    row.createCell(9).setCellValue(""); // EnquiryNo
	    row.createCell(10).setCellValue("Uploaded via test automation"); // Description
	    row.createCell(11).setCellValue(""); // Lead Source
	    row.createCell(12).setCellValue(""); // Lead Quality
	    row.createCell(13).setCellValue("Kannur"); // Place

	    // Write file
	    FileOutputStream fos = new FileOutputStream(filePath);
	    wb.write(fos);
	    wb.close();
	    fos.close();

	    return filePath;
	}
	
	public void uploadValidEnquiryFile() throws Exception {
        String filePath = createDynamicEnquiryFile();
        WebElement UploadFileInput = driver.findElement(upload_file);
        UploadFileInput.sendKeys(filePath);
        Thread.sleep(2000);
        driver.findElement(upload_btn).click();
        Thread.sleep(2000);
        driver.navigate().refresh();
        
        Thread.sleep(2000);
        WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(firstRow));

        String status = row.findElement(upload_status).getText().trim();
        String errorCount = row.findElement(table_error).getText().trim();
        System.out.println("Status : " + status+" :: Errors : " + errorCount);

        if (status.equalsIgnoreCase("Uploaded") && errorCount.equals("0")) {
            test5.log(Status.PASS, "Upload successful with zero errors");
        } else {
            test5.log(Status.FAIL, "Upload failed. Status: " + status + ", Errors: " + errorCount);
        }		
	}
	
	public static String createInvalidEnquiryFile() throws Exception {

    	String filePath = System.getProperty("user.dir")+"/testdata/invalid_enquiry_upload_File.xlsx";


	    Workbook wb = new XSSFWorkbook();
	    Sheet sheet = wb.createSheet("Enquiry");

	    String[] headers = {
	        "Date", "Customer Name", "Phone", "Phone2", "Assign to",
	        "Enquired For", "CurrentStatus", "LastFollowupDate",
	        "NextFollowupDate", "EnquiryNo", "Description",
	        "Lead Source", "Lead Quality", "Place"
	    };

	    // Header
	    Row headerRow = sheet.createRow(0);
	    for (int i = 0; i < headers.length; i++) {
	        headerRow.createCell(i).setCellValue(headers[i]);
	    }

	    String today = java.time.LocalDate.now().toString();
	    String nextDate = java.time.LocalDate.now().plusDays(2).toString();

	    // Common valid data
	    String phone = "9" + (100000000 + new java.util.Random().nextInt(900000000));

	    // Row 1 → Missing Date
	    Row r1 = sheet.createRow(1);
	    r1.createCell(1).setCellValue("User1");
	    r1.createCell(2).setCellValue(phone);
	    r1.createCell(4).setCellValue("Admin");
	    r1.createCell(5).setCellValue("Product A");
	    r1.createCell(8).setCellValue(nextDate);

	    // Row 2 → Missing Customer Name
	    Row r2 = sheet.createRow(2);
	    r2.createCell(0).setCellValue(today);
	    r2.createCell(2).setCellValue(phone);
	    r2.createCell(4).setCellValue("Admin");
	    r2.createCell(5).setCellValue("Product A");
	    r2.createCell(8).setCellValue(nextDate);

	    // Row 3 → Missing Phone
	    Row r3 = sheet.createRow(3);
	    r3.createCell(0).setCellValue(today);
	    r3.createCell(1).setCellValue("User3");
	    r3.createCell(4).setCellValue("Admin");
	    r3.createCell(5).setCellValue("Product A");
	    r3.createCell(8).setCellValue(nextDate);

	    // Row 4 → Missing Assign To
	    Row r4 = sheet.createRow(4);
	    r4.createCell(0).setCellValue(today);
	    r4.createCell(1).setCellValue("User4");
	    r4.createCell(2).setCellValue(phone);
	    r4.createCell(5).setCellValue("Product A");
	    r4.createCell(8).setCellValue(nextDate);

	    // Row 5 → Missing Enquired For
	    Row r5 = sheet.createRow(5);
	    r5.createCell(0).setCellValue(today);
	    r5.createCell(1).setCellValue("User5");
	    r5.createCell(2).setCellValue(phone);
	    r5.createCell(4).setCellValue("Admin");
	    r5.createCell(8).setCellValue(nextDate);

	    // Row 6 → Missing Next Follow-up Date
	    Row r6 = sheet.createRow(6);
	    r6.createCell(0).setCellValue(today);
	    r6.createCell(1).setCellValue("User6");
	    r6.createCell(2).setCellValue(phone);
	    r6.createCell(4).setCellValue("Admin");
	    r6.createCell(5).setCellValue("Product A");

	    FileOutputStream fos = new FileOutputStream(filePath);
	    wb.write(fos);
	    wb.close();
	    fos.close();

	    return filePath;
	}
	
	public void upload_invalid_enquiry_file() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		Select select = new Select(driver.findElement(branch));
		select.selectByIndex(1);

		String filePath = createInvalidEnquiryFile();

		driver.findElement(upload_file).sendKeys(filePath);

		driver.findElement(upload_btn).click();

		Thread.sleep(2000);
		driver.navigate().refresh();

		Thread.sleep(1000);

		WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(firstRow));

		String status = row.findElement(upload_status).getText().trim();
		String errorCount = row.findElement(table_error).getText().trim();
		System.out.println("Status : " + status + " :: Errors : " + errorCount);

		if (status.equalsIgnoreCase("Has Error") && errorCount.equals("6")) {
			test5.log(Status.PASS, "Upload processed with expected errors for invalid data");
		} else {
			test5.log(Status.FAIL, "Upload did not process as expected. Status: " + status + ", Errors: " + errorCount);
		}
	}
	
	public String createMixedDataFile() throws Exception {
		String filePath = System.getProperty("user.dir") + "/testdata/mixed_enquiry_file.xlsx";

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Enquiry");

		String[] headers = { "Date", "Customer Name", "Phone", "Phone2", "Assign to", "Enquired For", "CurrentStatus",
				"LastFollowupDate", "NextFollowupDate", "EnquiryNo", "Description", "Lead Source", "Lead Quality",
				"Place" };

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			headerRow.createCell(i).setCellValue(headers[i]);
		}

	    Row row = sheet.createRow(1);

	    LocalDate todayLocal = LocalDate.now();
	    LocalDate nextLocal = LocalDate.now().plusDays(2);

	    Date todayDate = Date.from(todayLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
	    Date nextDate = Date.from(nextLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

	    CellStyle dateStyle = wb.createCellStyle();
	    CreationHelper createHelper = wb.getCreationHelper();
	    dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

	    Cell dateCell = row.createCell(0);
	    dateCell.setCellValue(todayDate);
	    dateCell.setCellStyle(dateStyle); // Date*

	    row.createCell(1).setCellValue("John Doe"); // Customer Name*
	    row.createCell(2).setCellValue("9988998899"); // Phone*

	    row.createCell(4).setCellValue("Junaid"); // Assign to*
	    row.createCell(5).setCellValue("Artificial grass"); // Enquired For*

	    Cell nextFollowCell = row.createCell(8);
	    nextFollowCell.setCellValue(nextDate);
	    nextFollowCell.setCellStyle(dateStyle); // NextFollowupDate*
	    
	    
		// Row 2 → Invalid (Missing Phone)
		Row r2 = sheet.createRow(2);
		r2.createCell(0).setCellValue(LocalDate.now().toString());
		r2.createCell(1).setCellValue("Invalid User");
		r2.createCell(4).setCellValue("Junaid");
		r2.createCell(5).setCellValue("Artificial Grass");
		r2.createCell(8).setCellValue(LocalDate.now().plusDays(2).toString());

		FileOutputStream fos = new FileOutputStream(filePath);
		wb.write(fos);
		wb.close();
		fos.close();
		return filePath;
	}
	
	public void upload_mixed_data_file() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		Select select = new Select(driver.findElement(branch));
		select.selectByIndex(1);

		String filePath = createMixedDataFile();

		driver.findElement(upload_file).sendKeys(filePath);
		
		driver.findElement(upload_btn).click();

		Thread.sleep(2000);
		driver.navigate().refresh();

		Thread.sleep(2000);
		WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(firstRow));

		String status = row.findElement(upload_status).getText().trim();
		String errorCount = row.findElement(table_error).getText().trim();
		System.out.println("Status : " + status + " :: Errors : " + errorCount);

		if (status.equalsIgnoreCase("Has Error") && errorCount.equals("1")) {
			test5.log(Status.PASS, "Mixed data file processed with expected results");
		} else {
			test5.log(Status.FAIL,
					"Mixed data file did not process as expected. Status: " + status + ", Errors: " + errorCount);
		}
	}
}
