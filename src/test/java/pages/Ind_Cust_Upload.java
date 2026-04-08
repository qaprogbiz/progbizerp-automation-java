package pages;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Random;

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


public class Ind_Cust_Upload {
	
	WebDriver driver;
	ExtentTest test3;
	
//	Object repository
	By excel_upload_menu = By.id("nav-excel-upload");
	By ind_upload_btn = By.id("nav-excel-upload-individual-customer");
	By branch = By.id("branch");
	
	By upload_btn = By.xpath("//button[contains(text(),'Upload')]");
	By branch_validation =By.xpath("//div[contains(@class,'validation-message') and contains(text(),'Please choose branch')]");
	By file_validation = By.id("swal2-content");
	By file_validation_ok_btn= By.xpath("//button[contains(@class,'swal2-confirm') and normalize-space()='OK']");
	
    By firstRow = By.xpath("//table//tbody//tr[1]");
    By upload_status=By.xpath("./td[5]");
    By table_error= By.xpath("./td[6]");
    By upload_file = By.xpath("//input[@type='file']");

//	Constructor
	public Ind_Cust_Upload(WebDriver driver,ExtentTest test3) {
		this.driver = driver;
		this.test3= test3;
	}
	
//	Methods
	public void without_branch() throws Exception{
		Thread.sleep(2000);
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
	
	public void without_file() throws Exception {
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(branch));
		WebElement Branch = driver.findElement(branch);
		Select select_branch = new Select(Branch);
		select_branch.selectByIndex(1);
		
		driver.findElement(upload_btn).click();
		Thread.sleep(2000);
		WebElement File_Validation = driver.findElement(file_validation);
		if (File_Validation.isDisplayed()) {
			driver.findElement(file_validation_ok_btn).click();
			System.out.println("File validation message is displayed as expected.");
			test3.log(Status.PASS, "File validation message is displayed as expected.");
		} else {
			System.out.println("File validation message is not displayed.");
			test3.log(Status.FAIL, "File validation message is not displayed.");
		}
		
	}

    public static String getUniqueName() {
        return "John " + System.currentTimeMillis();
    }

    public static String getUniquePhone() {
        Random rand = new Random();
        return "9" + (100000000 + rand.nextInt(900000000));
    }

    public static String getUniqueEmail() {
        return "user" + System.currentTimeMillis() + "@test.com";
    }
    public static String createDynamicCustomerFile() throws Exception {

        String filePath = System.getProperty("user.dir") + "/testdata/valid_Individual_File.xlsx";

        String[] headers = {"Customer Name","Phone","Email address","Address Line1","Address Line2",
        	    "Address Line3","DOB","Phone2","Grade"};
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Customers");

        // Header
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        // Row with ONLY required fields
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(getUniqueName());   
        row.createCell(1).setCellValue(getUniquePhone());
        row.createCell(3).setCellValue("Kannur");
        row.createCell(8).setCellValue("Suspect");

        // Optional fields left empty intentionally

        FileOutputStream fos = new FileOutputStream(filePath);
        wb.write(fos);
        wb.close();
        fos.close();

        return filePath;
    }
    
    public void upload_valid_ind_customer() throws Exception {

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
            test3.log(Status.PASS, "Upload successful with zero errors");
        } else {
            test3.log(Status.FAIL, "Upload failed. Status: " + status + ", Errors: " + errorCount);
        }
    }
    
    public static String create_invalid_file() throws Exception {
    	
    	String filePath = System.getProperty("user.dir") + "/testdata/invalid_Individual_File.xlsx";

    	    Workbook wb = new XSSFWorkbook();
    	    Sheet sheet = wb.createSheet("Customers");

    	    String[] headers = {
    	        "Customer Name", "Phone", "Email address",
    	        "Address Line1", "Address Line2", "Address Line3",
    	        "DOB", "Phone2", "Grade"
    	    };

    	    Row header = sheet.createRow(0);
    	    for (int i = 0; i < headers.length; i++) {
    	        header.createCell(i).setCellValue(headers[i]);
    	    }
            // Row 1 → Missing Customer Name
            Row r1 = sheet.createRow(1);
            r1.createCell(1).setCellValue("989898");
            r1.createCell(3).setCellValue("Home address");
            r1.createCell(8).setCellValue("Suspect");

            // Row 2 → Missing Phone
            Row r2 = sheet.createRow(2);
            r2.createCell(0).setCellValue("John Doe");
            r2.createCell(3).setCellValue("Home address");
            r2.createCell(8).setCellValue("Suspect");

            // Row 3 → Missing Address Line1
            Row r3 = sheet.createRow(3);
            r3.createCell(0).setCellValue("John Doe");
            r3.createCell(1).setCellValue("989898");
            r3.createCell(8).setCellValue("Suspect");
            
            // Row 4 → Missing Grade
            Row r4 = sheet.createRow(4);
            r4.createCell(0).setCellValue("John Doe");
            r4.createCell(1).setCellValue("989898");
            r4.createCell(3).setCellValue("Home address");

    	    FileOutputStream fos = new FileOutputStream(filePath);
    	    wb.write(fos);
    	    wb.close();
    	    fos.close();

    	    return filePath;
    }
    
    public void upload_invalid_ind_customer() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        Thread.sleep(2000);
        Select select = new Select(driver.findElement(branch));
        select.selectByIndex(1);

        String filePath = create_invalid_file();

        driver.findElement(upload_file).sendKeys(filePath);
        driver.findElement(upload_btn).click();
        
        Thread.sleep(1000);
        driver.navigate().refresh();
        Thread.sleep(1000);
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(firstRow));

        String status1 = row.findElement(upload_status).getText().trim();
        String error_Count = row.findElement(table_error).getText().trim();
        System.out.println("Status : " + status1+" :: Errors : " + error_Count);

        if (status1.equalsIgnoreCase("Has Error") && error_Count.equals("4")) {
            test3.log(Status.PASS, "Upload failed as expected with 4 errors");
        } else {
            test3.log(Status.FAIL, "Invalid file uploaded " + status1 + ", Errors: " + error_Count);
        }

    }
    public static String create_mixed_file() throws Exception {

        String filePath = System.getProperty("user.dir") + "/testdata/mixed_Individual_File.xlsx";

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Customers");

        String[] headers = {
            "Customer Name", "Phone", "Email address",
            "Address Line1", "Address Line2", "Address Line3",
            "DOB", "Phone2", "Grade"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        // Row 1 → VALID
        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue(getUniqueName());
        r1.createCell(1).setCellValue(getUniquePhone());
        r1.createCell(3).setCellValue("Kannur");
        r1.createCell(8).setCellValue("Suspect");

        // Row 2 → INVALID (missing phone)
        Row r2 = sheet.createRow(2);
        r2.createCell(0).setCellValue("John Doe");
        r2.createCell(3).setCellValue("Home address");
        r2.createCell(8).setCellValue("Suspect");

        FileOutputStream fos = new FileOutputStream(filePath);
        wb.write(fos);
        wb.close();
        fos.close();

        return filePath;
    }
    
    public void upload_mixed_ind_customer() throws Exception {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Thread.sleep(2000);

        // Select branch
        Select select = new Select(driver.findElement(branch));
        select.selectByIndex(1);

        // Create mixed file
        String filePath = create_mixed_file();

        // Upload
        driver.findElement(upload_file).sendKeys(filePath);
        driver.findElement(upload_btn).click();

        Thread.sleep(1000);
        driver.navigate().refresh();
        Thread.sleep(1000);
        
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(firstRow));

        String status = row.findElement(upload_status).getText().trim();
        String errorCount = row.findElement(table_error).getText().trim();

        System.out.println("Status : " + status + " :: Errors : " + errorCount);

        if (status.equalsIgnoreCase("Has Error") && Integer.parseInt(errorCount) > 0) {
            test3.log(Status.PASS, "Mixed file handled correctly (valid + invalid rows)");
        } else {
            test3.log(Status.FAIL, "Mixed file validation failed. Status: " + status + ", Errors: " + errorCount);
        }
    }
	
}