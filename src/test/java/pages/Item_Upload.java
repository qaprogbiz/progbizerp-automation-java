package pages;

import java.io.FileOutputStream;
import java.time.Duration;

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

public class Item_Upload {

	
	WebDriver driver;
	ExtentTest test6;
	
//	Object repository
	By excel_upload_menu = By.id("nav-excel-upload");
	By item_upload_btn = By.id("nav-excel-upload-item");
	By branch = By.id("branch");
	
	By upload_btn = By.xpath("//button[contains(text(),'Upload')]");
	By branch_validation =By.xpath("//div[contains(@class,'validation-message') and contains(text(),'Please choose branch')]");
	By file_validation = By.id("swal2-content");
	By file_validation_ok_btn= By.xpath("//button[contains(@class,'swal2-confirm') and normalize-space()='OK']");
	
    By firstRow = By.xpath("//table//tbody//tr[1]");
    By upload_status=By.xpath("./td[5]");
    By table_error= By.xpath("./td[6]");
    By upload_file = By.xpath("//input[@type='file']");
	
	public Item_Upload(WebDriver driver, ExtentTest test6) {
		this.driver = driver;
		this.test6 = test6;
	}
	
//	Methods
	public void itemExcelUploadMenu() {
		
		WebElement ExcelUploadMenu = driver.findElement(excel_upload_menu);
		WebElement ItemUploadBtn = driver.findElement(item_upload_btn);
		
		if (ItemUploadBtn.isDisplayed()){
			System.out.println("Excel upload menu is displayed");
		}
		else {
			System.out.println("Excel upload menu is not displayed");
			ExcelUploadMenu.click();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(item_upload_btn)).click();
		}
		
	}
		
	public void uploadWithoutBranch() throws Exception {
		Thread.sleep(2000);

		driver.findElement(upload_btn).click();
		Thread.sleep(2000);
		WebElement Branch_Validation = driver.findElement(branch_validation);
		if(Branch_Validation.isDisplayed()) {
            System.out.println("Branch validation message is displayed as expected.");
            test6.log(Status.PASS, "Branch validation message is displayed as expected.");
		}
		else {
			System.out.println("Branch validation message is not displayed.");
			test6.log(Status.FAIL, "Branch validation message is not displayed.");
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
			test6.log(Status.PASS, "File validation message is displayed as expected.");
		} else {
			System.out.println("File validation message is not displayed.");
			test6.log(Status.FAIL, "File validation message is not displayed.");
		}
	}
	
	public static String getUniqueItemName() {
	    return "Item_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
	}
	
	public static String createDynamicItemFile() throws Exception {
	    // Setting the file path for the Item file
	    String filePath = System.getProperty("user.dir") + "/testdata/valid_Item_File.xlsx";

	    // Headers based on the provided image
	    String[] headers = {
	        "Item Name*", 
	        "Price", 
	        "Inter Tax Category", 
	        "Intra Tax Category", 
	        "Is Price Including Tax"
	    };

	    Workbook wb = new XSSFWorkbook();
	    Sheet sheet = wb.createSheet("Items");

	    // Create Header Row
	    Row headerRow = sheet.createRow(0);
	    for (int i = 0; i < headers.length; i++) {
	        headerRow.createCell(i).setCellValue(headers[i]);
	    }

	    Row dataRow = sheet.createRow(1);
	    
	    dataRow.createCell(0).setCellValue(getUniqueItemName()); 


	    FileOutputStream fos = new FileOutputStream(filePath);
	    wb.write(fos);
	    wb.close();
	    fos.close();

	    return filePath;
	}
	
	public void upload_valid_item() throws Exception {
		
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Select select = new Select(driver.findElement(branch));
        select.selectByIndex(1);

        String filePath = createDynamicItemFile();

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
            test6.log(Status.PASS, "Upload successful with zero errors");
        } else {
            test6.log(Status.FAIL, "Upload failed. Status: " + status + ", Errors: " + errorCount);
        }
	}
	
	public static String createInvalidItemFile() throws Exception {
	    String filePath = System.getProperty("user.dir") + "/testdata/invalid_Item_File.xlsx";

	    String[] headers = {
	        "Item Name*", 
	        "Price", 
	        "Inter Tax Category", 
	        "Intra Tax Category", 
	        "Is Price Including Tax"
	    };

	    Workbook wb = new XSSFWorkbook();
	    Sheet sheet = wb.createSheet("Items");

	    // Create Header Row
	    Row headerRow = sheet.createRow(0);
	    for (int i = 0; i < headers.length; i++) {
	        headerRow.createCell(i).setCellValue(headers[i]);
	    }

	    // Row 1: Missing Required Field (Item Name is null/empty)
	    Row row1 = sheet.createRow(1);
	    row1.createCell(0).setCellValue(""); // Empty required field
	    row1.createCell(1).setCellValue(100); // Optional field filled

	    // Row 2: Invalid Data Type (Putting text in a numeric Price field)
	    Row row2 = sheet.createRow(2);
	    row2.createCell(0).setCellValue("Invalid Price Item");
	    row2.createCell(1).setCellValue("NotANumber"); // Should be numeric

	    FileOutputStream fos = new FileOutputStream(filePath);
	    wb.write(fos);
	    wb.close();
	    fos.close();

	    return filePath;
	}
	
	public void upload_invalid_item() throws Exception {
		
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        Thread.sleep(2000);
        Select select = new Select(driver.findElement(branch));
        select.selectByIndex(1);

        String filePath = createInvalidItemFile();

        driver.findElement(upload_file).sendKeys(filePath);
        driver.findElement(upload_btn).click();
        
        Thread.sleep(1000);
        driver.navigate().refresh();
        Thread.sleep(1000);
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(firstRow));

        String status1 = row.findElement(upload_status).getText().trim();
        String error_Count = row.findElement(table_error).getText().trim();
        System.out.println("Status : " + status1+" :: Errors : " + error_Count);

        if (status1.equalsIgnoreCase("Has Error") && error_Count.equals("2")) {
            test6.log(Status.PASS, "Upload failed as expected with 2 errors");
        } else {
            test6.log(Status.FAIL, "Invalid file uploaded " + status1 + ", Errors: " + error_Count);
        }
	}
	
	public static String createMixedItemFile() throws Exception {
	    String filePath = System.getProperty("user.dir") + "/testdata/mixed_Item_File.xlsx";

	    String[] headers = {
	        "Item Name*", 
	        "Price", 
	        "Inter Tax Category", 
	        "Intra Tax Category", 
	        "Is Price Including Tax"
	    };

	    Workbook wb = new XSSFWorkbook();
	    Sheet sheet = wb.createSheet("Items");

	    // Create Header Row
	    Row headerRow = sheet.createRow(0);
	    for (int i = 0; i < headers.length; i++) {
	        headerRow.createCell(i).setCellValue(headers[i]);
	    }

	    // --- ROW 1: VALID DATA ---
	    Row validRow = sheet.createRow(1);
	    validRow.createCell(0).setCellValue("Premium Coffee Bean"); // Required field
	    validRow.createCell(1).setCellValue(250.00);               // Optional price
	    validRow.createCell(4).setCellValue("250.00");                // Optional tax flag

	    // --- ROW 2: INVALID DATA (Missing Required Field) ---
	    Row invalidRow = sheet.createRow(2);
	    invalidRow.createCell(0).setCellValue("");                // EMPTY REQUIRED FIELD
	    invalidRow.createCell(1).setCellValue(500.00);            // Price provided, but name is missing
	    invalidRow.createCell(2).setCellValue("GST_18");

	    // Write to file
	    FileOutputStream fos = new FileOutputStream(filePath);
	    wb.write(fos);
	    wb.close();
	    fos.close();

	    return filePath;
	}
	
	public void upload_mixed_item() throws Exception {
		
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Thread.sleep(2000);

        // Select branch
        Select select = new Select(driver.findElement(branch));
        select.selectByIndex(1);

        // Create mixed file
        String filePath = createMixedItemFile();

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
            test6.log(Status.PASS, "Mixed file handled correctly (valid + invalid rows)");
        } else {
            test6.log(Status.FAIL, "Mixed file validation failed. Status: " + status + ", Errors: " + errorCount);
        }
	}
}
