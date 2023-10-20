package com.cst438;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *  In SpringBootTest environment, the test program may use Spring repositories to 
 *  setup the database for the test and to verify the result.
 */

@SpringBootTest
public class EndToEndTestSubmitGrades {

	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/nickm/Documents/chromedriver-mac-arm64/chromedriver";

	public static final String URL = "http://localhost:3000";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "db design";
	public static final String NEW_GRADE = "99";


	@Test
	public void updateAssignment() throws InterruptedException{
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);



		WebElement w;

		try{
			driver.findElement(By.id("addButton")).click();
			WebElement name = driver.findElement(By.name("assignmentName"));
			WebElement dueDate = driver.findElement(By.name("dueDate"));
			WebElement courseID = driver.findElement(By.name("courseId"));

			name.sendKeys("test");
			dueDate.sendKeys("2021-01-02");
			courseID.sendKeys("31045");

			driver.findElement(By.id("saveButton")).click();
			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.id("closeButton")).click();
			Thread.sleep(SLEEP_DURATION);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement rowName =wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[contains(@class, 'Center')]/tbody/tr[5]/td[1]")));


			System.out.println(rowName.getText());
			assertEquals("test",rowName.getText());
			Thread.sleep(SLEEP_DURATION);



			driver.findElement(By.xpath("//table[contains(@class, 'Center')]/tbody/tr[5]/td[5]")).click();

			WebElement editName = driver.findElement(By.id("nameEdit"));
			WebElement editDueDate = driver.findElement(By.id("dueDateEdit"));


			editName.click();
			editName.sendKeys(Keys.BACK_SPACE);editName.sendKeys(Keys.BACK_SPACE);editName.sendKeys(Keys.BACK_SPACE);editName.sendKeys(Keys.BACK_SPACE);
			Thread.sleep(SLEEP_DURATION);
			editDueDate.click();
			editDueDate.sendKeys(Keys.BACK_SPACE);editDueDate.sendKeys(Keys.BACK_SPACE);editDueDate.sendKeys(Keys.BACK_SPACE);editDueDate.sendKeys(Keys.BACK_SPACE);editDueDate.sendKeys(Keys.BACK_SPACE);editDueDate.sendKeys(Keys.BACK_SPACE);editDueDate.sendKeys(Keys.BACK_SPACE);editDueDate.sendKeys(Keys.BACK_SPACE);editDueDate.sendKeys(Keys.BACK_SPACE);editDueDate.sendKeys(Keys.BACK_SPACE);
			Thread.sleep(SLEEP_DURATION);

			editName.sendKeys("testEdit");
			editDueDate.sendKeys("2021-01-03");


			driver.findElement(By.id("saveAssignmentButton")).click();
			driver.findElement(By.id("closeAssignmentButton")).click();

			WebElement nameCheck =wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[contains(@class, 'Center')]/tbody/tr[5]/td[1]")));
			WebElement dueDateCheck =wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[contains(@class, 'Center')]/tbody/tr[5]/td[3]")));

			assertEquals("testEdit",nameCheck.getText());
			assertEquals("2021-01-03",dueDateCheck.getText());




		}catch (Exception ex) {
			throw ex;
		} finally {
			driver.quit();
		}
	}

	@Test
	public void addAssignment() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);

//
//		WebElement name = driver.findElement(By.name("assignmentName"));
//		WebElement dueDate = driver.findElement(By.name("dueDate"));
//		WebElement courseID = driver.findElement(By.name("courseId"));


		WebElement w;

		try{
			  driver.findElement(By.id("addButton")).click();
			WebElement name = driver.findElement(By.name("assignmentName"));
			WebElement dueDate = driver.findElement(By.name("dueDate"));
			WebElement courseID = driver.findElement(By.name("courseId"));

			name.sendKeys("test");
			dueDate.sendKeys("2021-01-02");
			courseID.sendKeys("31045");

			driver.findElement(By.id("saveButton")).click();
			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.id("closeButton")).click();
			Thread.sleep(SLEEP_DURATION);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement rowName =wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[contains(@class, 'Center')]/tbody/tr[5]/td[1]")));

//			rowName.getText();
			System.out.println(rowName.getText());
			assertEquals("test",rowName.getText());


			Thread.sleep(SLEEP_DURATION);


		}catch (Exception ex) {
			throw ex;
		} finally {
			driver.quit();
		}


	}


	@Test
	public void delete() throws InterruptedException{
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);




		WebElement w;

		try{
			driver.findElement(By.id("addButton")).click();
			WebElement name = driver.findElement(By.name("assignmentName"));
			WebElement dueDate = driver.findElement(By.name("dueDate"));
			WebElement courseID = driver.findElement(By.name("courseId"));

			name.sendKeys("test");
			dueDate.sendKeys("2021-01-02");
			courseID.sendKeys("31045");

			driver.findElement(By.id("saveButton")).click();
			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.id("closeButton")).click();
			Thread.sleep(SLEEP_DURATION);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement rowName =wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[contains(@class, 'Center')]/tbody/tr[5]/td[1]")));


			System.out.println(rowName.getText());
			assertEquals("test",rowName.getText());


			Thread.sleep(SLEEP_DURATION);




			WebElement temp =wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[contains(@class, 'Center')]/tbody/tr[5]/td[6]")));
			Thread.sleep(SLEEP_DURATION);
			temp.click();
			driver.findElement(By.id("deleteButton")).click(); //Not sure why I have to delete twice to see the change
			temp.click();
			driver.findElement(By.id("deleteButton")).click();
			Thread.sleep(SLEEP_DURATION +3000);

			assertThrows(NoSuchElementException.class, () -> {
				driver.findElement(By.xpath("//table[contains(@class, 'Center')]/tbody/tr[5]"));
			});











		}catch (Exception ex) {
			throw ex;
		}finally {
			driver.quit();
		}
	}








	@Test
	public void addCourseTest() throws Exception {



		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on
		
		/*
		 * initialize the WebDriver and get the home page. 
		 */

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);

		WebElement w;
		

		try {
			/*
			* locate the <td> element for assignment title 'db design'
			* 
			*/
			
			List<WebElement> elements  = driver.findElements(By.xpath("//td"));
			boolean found = false;
			for (WebElement we : elements) {
				if (we.getText().equals("Add")) {
					found=true;
					we.findElement(By.xpath("..//a")).click();
					break;
				}
			}
			assertThat(found).withFailMessage("The test assignment was not found.").isTrue();

			/*
			 *  Locate and click Grade button to indicate to grade this assignment.
			 */
			
			Thread.sleep(SLEEP_DURATION);

			/*
			 *  enter grades for all students, then click save.
			 */
			ArrayList<String> originalGrades = new ArrayList<>();
			elements  = driver.findElements(By.xpath("//input"));
			for (WebElement element : elements) {
				originalGrades.add(element.getAttribute("value"));
				element.clear();
				element.sendKeys(NEW_GRADE);
				Thread.sleep(SLEEP_DURATION);
			}
			
			for (String s : originalGrades) {
				System.out.println("'"+s+"'");
			}

			/*
			 *  Locate submit button and click
			 */
			driver.findElement(By.id("sgrade")).click();
			Thread.sleep(SLEEP_DURATION);
			
			w = driver.findElement(By.id("gmessage"));
			assertThat(w.getText()).withFailMessage("After saving grades, message should be \"Grades saved.\"").startsWith("Grades saved");
			
			driver.navigate().back();  // back button to last page
			Thread.sleep(SLEEP_DURATION);
			
			// find the assignment 'db design' again.
			elements  = driver.findElements(By.xpath("//td"));
			found = false;
			for (WebElement we : elements) {
				if (we.getText().equals(TEST_ASSIGNMENT_NAME)) {
					found=true;
					we.findElement(By.xpath("..//a")).click();
					break;
				}
			}
			Thread.sleep(SLEEP_DURATION);
			assertThat(found).withFailMessage("The test assignment was not found.").isTrue();
			
			// verify the grades. Change grades back to original values

			elements  = driver.findElements(By.xpath("//input"));
			for (int idx=0; idx < elements.size(); idx++) {
				WebElement element = elements.get(idx);
				assertThat(element.getAttribute("value")).withFailMessage("Incorrect grade value.").isEqualTo(NEW_GRADE);
				
				// clear the input value by backspacing over the value
				while(!element.getAttribute("value").equals("")){
			        element.sendKeys(Keys.BACK_SPACE);
			    }
				if (!originalGrades.get(idx).equals("")) element.sendKeys(originalGrades.get(idx));
				Thread.sleep(SLEEP_DURATION);
			}
			driver.findElement(By.id("sgrade")).click();
			Thread.sleep(SLEEP_DURATION);
			
			w = driver.findElement(By.id("gmessage"));
			assertThat(w.getText()).withFailMessage("After saving grades, message should be \"Grades saved.\"").startsWith("Grades saved");


		} catch (Exception ex) {
			throw ex;
		} finally {

			driver.quit();
		}

	}
}
