package cucumber;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import csci310.model.Stock;
import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
	//http://www.software-testing-tutorials-automation.com/2015/01/how-to-capture-element-screenshot-using.html
	public void captureElementScreenshot(WebElement element) throws IOException{
		  //Capture entire page screenshot as buffer.
		  //Used TakesScreenshot, OutputType Interface of selenium and File class of java to capture screenshot of entire page.
		  File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		  //Used selenium getSize() method to get height and width of element.
		  //Retrieve width of element.
		  int ImageWidth = element.getSize().getWidth();
		  //Retrieve height of element.
		  int ImageHeight = element.getSize().getHeight();  
		  //Used selenium Point class to get x y coordinates of Image element.
		  //get location(x y coordinates) of the element.
		  Point point = element.getLocation();
		  int xcord = point.getX();
		  int ycord = point.getY();
		  //Reading full image screenshot.
		  BufferedImage img = ImageIO.read(screen);
		  //cut Image using height, width and x y coordinates parameters.
		  BufferedImage dest = img.getSubimage(xcord, ycord, ImageWidth, ImageHeight);
		  ImageIO.write(dest, "png", screen);
		  //Used FileUtils class of apache.commons.io.
		  //save Image screenshot In D: drive.
		  FileUtils.copyFile(screen, new File("to_test.png"));
		 }
	//https://stackoverflow.com/questions/8567905/how-to-compare-images-for-similarity-using-java
	public static boolean compareImage(File fileA, File fileB) {        
	    try {
	        // take buffer data from botm image files //
	        BufferedImage biA = ImageIO.read(fileA);
	        DataBuffer dbA = biA.getData().getDataBuffer();
	        int sizeA = dbA.getSize();                      
	        BufferedImage biB = ImageIO.read(fileB);
	        DataBuffer dbB = biB.getData().getDataBuffer();
	        int sizeB = dbB.getSize();
	        // compare data-buffer objects //
	        if(sizeA == sizeB) {
	            for(int i=0; i<sizeA; i++) { 
	                if(dbA.getElem(i) != dbB.getElem(i)) {
	                    return false;
	                }
	            }
	            return true;
	        }
	        else {
	            return false;
	        }
	    } 
	    catch (Exception e) { 
	        System.out.println("");
	        return  false;
	    }
	}

	private static final String ROOT_URL = "https://localhost:8443/";

	private WebDriver driver;
	
	@Before()
	public void before() {
		ChromeOptions capability = new ChromeOptions();
		capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capability.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
		driver = new ChromeDriver(capability);
	}

	@Given("I am on the index page")
	public void i_am_on_the_index_page() {
		driver.get(ROOT_URL);
	}

	@When("I click the link {string}")
	public void i_click_the_link(String linkText) throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.linkText(linkText)).click();
	}

	@Then("I should see header {string}")
	public void i_should_see_header(String header) throws InterruptedException {
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.cssSelector("h2")).getText().contains(header));
	}
	
	@Then("I should see text {string}")
	public void i_should_see_text(String text) throws InterruptedException {
		Thread.sleep(1000);
		assertTrue(driver.getPageSource().contains(text));
	}
	
	@Given("I am on the sign in page")
	public void i_am_on_the_sign_in_page() {
	    driver.get(ROOT_URL+"signIn.jsp");
	}

	@When("I enter an valid username {string}")
	public void i_enter_an_valid_username(String string) throws InterruptedException {
		WebElement queryBox = driver.findElement(By.id("username"));
		queryBox.sendKeys(string);
	}
	
	@When("I enter the correct password {string}")
	public void i_enter_the_correct_password(String string) {
		WebElement queryBox = driver.findElement(By.id("pass"));
		queryBox.sendKeys(string);
	}
	
	@Then("I should be taken to the home page")
	public void i_should_be_taken_to_the_home_page() throws InterruptedException {
		Thread.sleep(7000);
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase(ROOT_URL+"homepage.jsp"));
		Thread.sleep(3000);
	}
	
	@When("I enter an incorrect password {string}")
	public void i_enter_an_incorrect_password(String string) {
		WebElement queryBox = driver.findElement(By.id("pass"));
		queryBox.sendKeys(string);
	}
	
	@Then("I should see the error {string}")
	public void i_should_see_the_error(String string) throws InterruptedException {
		Thread.sleep(1000);
		WebElement errorBox = driver.findElement(By.id("Merror"));
		assertTrue(string.equalsIgnoreCase(errorBox.getText()));
	}
	
	@When("I enter an invalid username {string}")
	public void i_enter_an_invalid_username(String string) {
		WebElement queryBox = driver.findElement(By.id("username"));
		queryBox.sendKeys(string);
	}
	
	@When("I enter any password")
	public void i_enter_any_password() {
		WebElement queryBox = driver.findElement(By.id("pass"));
		queryBox.sendKeys("test2test");
	}
	
	@When("I click submit on login")
	public void i_click_submit_on_login() throws InterruptedException {
		WebElement searchButton = driver.findElement(By.id("b"));
	    searchButton.click();
	    Thread.sleep(7000);
	}
	
	@Given("I am on the home page")
	public void i_am_on_the_home_page() throws InterruptedException {
	    driver.get(ROOT_URL+"signIn.jsp");
	    WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("ttrojan");
		WebElement password = driver.findElement(By.id("pass"));
		password.sendKeys("12345678");
		WebElement searchButton = driver.findElement(By.id("b"));
	    searchButton.click();
	    Thread.sleep(5000);
	}
	
	@When("I click on the sign out button")
	public void i_click_on_the_sign_out_button() throws InterruptedException {
	    Thread.sleep(1000);
	    driver.findElement(By.id("signOutB")).click();
	    Thread.sleep(1000);
	}
	
	@Then("I should be signed out and taken back to the sign in page")
	public void i_should_be_signed_out_and_taken_back_to_the_sign_in_page() {
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase(ROOT_URL+"signIn.jsp"));
	}
	
	@When("I redirect to sign in")
	public void i_redirect_to_sign_in() {
		driver.get(ROOT_URL+"signIn.jsp");
	}

	@When("I redirect to sign up")
	public void i_redirect_to_sign_up() {
		driver.get(ROOT_URL+"signup.jsp");
	}

	@When("I click on the sign up link")
	public void i_click_on_the_sign_up_link() throws InterruptedException {
		WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"signUpLink\"]/a"));
	    searchButton.click();
	    Thread.sleep(5000);
	}

	@Then("I should be taken to sign up")
	public void i_should_be_taken_to_sign_up() throws InterruptedException {
		Thread.sleep(3000);
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase(ROOT_URL+"signup.jsp"));
		Thread.sleep(3000);
	}
	
	@When("I wait {int} minute")
	public void i_wait_minute(Integer int1) throws InterruptedException {
	    Thread.sleep(60000 * int1 + 1);
	}

	@Then("I should see the unlock message {string}")
	public void i_should_see_the_unlock_message(String string) throws InterruptedException {
		Thread.sleep(1000);
		WebElement errorBox = driver.findElement(By.id("Munlock"));
		assertTrue(string.equalsIgnoreCase(errorBox.getText()));
	}
	
	@Given("I am on the sign up page")
	public void i_am_on_the_sign_up_page() throws InterruptedException {
		driver.get(ROOT_URL+"signup.jsp");
		Thread.sleep(3000);
	}
	
	@When("I enter an already used username in SU")
	public void i_enter_an_already_used_username_in_SU() {
		WebElement queryBox = driver.findElement(By.id("username"));
		queryBox.sendKeys("test2");
	}

	@When("I enter a password in SU")
	public void i_enter_a_password_in_SU() {
		WebElement queryBox = driver.findElement(By.id("password"));
		queryBox.sendKeys("test2test");
	}

	@When("I enter a confirm password in SU")
	public void i_enter_a_confirm_password_in_SU() {
		WebElement queryBox = driver.findElement(By.id("confirmpassword"));
		queryBox.sendKeys("test2test");
	}

	@When("I click submit in SU")
	public void i_click_submit_in_SU() throws InterruptedException {
		WebElement searchButton = driver.findElement(By.id("b"));
	    searchButton.click();
	    Thread.sleep(7000);
	}

	@Then("I should see the SU error {string}")
	public void i_should_see_the_SU_error(String string) throws InterruptedException {
		Thread.sleep(1000);
		WebElement errorBox = driver.findElement(By.id("Merror"));
		assertTrue(string.equalsIgnoreCase(errorBox.getText()));
	}

	@When("I enter a valid username that does not exist in SU")
	public void i_enter_a_valid_username_that_does_not_exist_in_SU() {
		int random = (int)(Math.random() * 10001) + 10000;
		WebElement queryBox = driver.findElement(By.id("username"));
		queryBox.sendKeys(""+ random);
	}


	@Then("I should land on the sign in page")
	public void i_should_land_on_the_sign_in_page() throws InterruptedException {
		Thread.sleep(3000);
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase(ROOT_URL+"signIn.jsp"));
		Thread.sleep(3000);
	}

	@When("I enter a valid username in SU")
	public void i_enter_a_valid_username_in_SU() {
		WebElement queryBox = driver.findElement(By.id("username"));
		queryBox.sendKeys("test");
	}


	@When("I enter a non-matching password in confirm password in SU")
	public void i_enter_a_non_matching_password_in_confirm_password_in_SU() {
		WebElement queryBox = driver.findElement(By.id("confirmpassword"));
		queryBox.sendKeys("test2test222");
	}
	
	@When("I click the cancel button in SU")
	public void i_click_the_cancel_button_in_SU() throws InterruptedException {
		WebElement searchButton = driver.findElement(By.id("c"));
	    searchButton.click();
	    Thread.sleep(7000);
	}


	@Then("I should see the text on the cancel button say cancel")
	public void i_should_see_the_text_on_the_cancel_button_say_cancel() {
		WebElement searchButton = driver.findElement(By.id("c"));
		assertTrue(searchButton.getText().equals("  CANCEL"));
	}

	@Then("I should see the text on the create user button say create user")
	public void i_should_see_the_text_on_the_create_user_button_say_create_user() {
		WebElement searchButton = driver.findElement(By.id("b"));
		assertTrue(searchButton.getText().equals("  CREATE USER"));
	}

	@When("I click Add Stock in the Portfolio box")
	public void i_click_Add_Stock_in_the_Portfolio_box() throws InterruptedException {
		WebElement addStockButton = driver.findElement(By.id("add-stock-button"));
		addStockButton.click();
		Thread.sleep(1000);
	}

	@When("I enter a valid Ticker symbol in the Add Stocks popup")
	public void i_enter_a_valid_Ticker_symbol_in_the_Add_Stocks_popup() {
		WebElement addStockTicker = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div/div[1]/div/div[2]/form/div[1]/input"));
	    addStockTicker.sendKeys("AMZN");
	}

	@When("I enter a valid number of shares in the Add Stocks popup")
	public void i_enter_a_valid_number_of_shares_in_the_Add_Stocks_popup() {
		WebElement addStockShares = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div/div[1]/div/div[2]/form/div[2]/input"));
		addStockShares.sendKeys("2");
	}

	@When("I enter a valid purchase date in the Add Stocks popup")
	public void i_enter_a_valid_purchase_date_in_the_Add_Stocks_popup() {
		WebElement addStockBuyDate = driver.findElement(By.id("date-purchased-portfolio"));
		addStockBuyDate.sendKeys("10/10/2020");
	}
	
	@When("I enter a valid purchase date using the calendar popup in the Add Stocks popup")
	public void i_enter_a_valid_purchase_date_using_the_calendar_popup_in_the_Add_Stocks_popup() {
		WebElement addStockBuyDate = driver.findElement(By.id("date-purchased-portfolio"));
		addStockBuyDate.click();
		WebElement previous = driver.findElement(By.className("ui-datepicker-prev"));
		previous.click();
		WebElement tenth = driver.findElement(By.xpath("/html/body/div[4]/table/tbody/tr[2]/td[7]/a"));
		tenth.click();
	}
	
	@When("I enter a valid purchase date using the calendar popup in the View Stocks popup")
	public void i_enter_a_valid_purchase_date_using_the_calendar_popup_in_the_View_Stocks_popup() {
		WebElement addStockBuyDate = driver.findElement(By.id("date-purchased-viewed"));
		addStockBuyDate.click();
		WebElement previous = driver.findElement(By.className("ui-datepicker-prev"));
		previous.click();
		WebElement tenth = driver.findElement(By.xpath("/html/body/div[4]/table/tbody/tr[2]/td[7]/a"));
		tenth.click();
	}

	@When("I enter a valid sell date in the Add Stocks popup")
	public void i_enter_a_valid_sell_date_in_the_Add_Stocks_popup() throws InterruptedException {
		WebElement addStockSellDate = driver.findElement(By.id("date-sold-portfolio"));
		addStockSellDate.sendKeys("10/15/2020");
		addStockSellDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}
	
	@When("I enter a valid sell date using the calendar popup in the Add Stocks popup")
	public void i_enter_a_valid_sell_date_using_the_calendar_popup_in_the_Add_Stocks_popup() throws InterruptedException {
		WebElement addStockSoldDate = driver.findElement(By.id("date-sold-portfolio"));
		addStockSoldDate.click();
		WebElement previous = driver.findElement(By.className("ui-datepicker-prev"));
		previous.click();
		WebElement fifteenth = driver.findElement(By.xpath("/html/body/div[4]/table/tbody/tr[3]/td[5]/a"));
		fifteenth.click();
		Thread.sleep(500);
		addStockSoldDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}
	
	@When("I enter a valid sell date using the calendar popup in the View Stocks popup")
	public void i_enter_a_valid_sell_date_using_the_calendar_popup_in_the_View_Stocks_popup() throws InterruptedException {
		WebElement addStockSoldDate = driver.findElement(By.id("date-sold-viewed"));
		addStockSoldDate.click();
		WebElement previous = driver.findElement(By.className("ui-datepicker-prev"));
		previous.click();
		WebElement fifteenth = driver.findElement(By.xpath("/html/body/div[4]/table/tbody/tr[3]/td[5]/a"));
		fifteenth.click();
		Thread.sleep(500);
		addStockSoldDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}

	@When("I click Add Stock")
	public void i_click_Add_Stock() {
		WebElement addStockSubmit = driver.findElement(By.cssSelector("#add-stock-submit"));
		addStockSubmit.click();
	}

	@Then("the stock should be added to the Portfolio list")
	public void the_stock_should_be_added_to_the_Portfolio_list() throws InterruptedException {
		Thread.sleep(3000);
		WebElement checkStock = driver.findElement(By.xpath("//*[text()[contains(.,'AMZN')]]"));
		assertTrue(checkStock != null);
	}

	@When("I enter an invalid Ticker symbol in the Add Stocks popup")
	public void i_enter_an_invalid_Ticker_symbol_in_the_Add_Stocks_popup() {
		WebElement addStockTicker = driver.findElement(By.cssSelector("#add-stock-form #ticker"));
	    addStockTicker.sendKeys("ADSFSWDZFD");
	}

	@When("I enter any number of shares in the Add Stocks popup")
	public void i_enter_any_number_of_shares_in_the_Add_Stocks_popup() {
		WebElement addStockShares = driver.findElement(By.cssSelector("#add-stock-form #shares"));
		addStockShares.sendKeys("2");
	}

	@When("I enter any valid purchase date in the Add Stocks popup")
	public void i_enter_any_valid_purchase_date_in_the_Add_Stocks_popup() {
		WebElement addStockBuyDate = driver.findElement(By.id("date-purchased-portfolio"));
		addStockBuyDate.sendKeys("10/10/2020");
	}

	@When("I enter any valid sell date in the Add Stocks popup")
	public void i_enter_any_valid_sell_date_in_the_Add_Stocks_popup() throws InterruptedException {
		WebElement addStockSellDate = driver.findElement(By.id("date-sold-portfolio"));
		addStockSellDate.sendKeys("10/15/2020");
		addStockSellDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}

	@Then("I should see the error {string} in the Add Stocks popup")
	public void i_should_see_the_error_in_the_Add_Stocks_popup(String string) throws InterruptedException {
		Thread.sleep(3000);
		WebElement errorMessage = driver.findElement(By.xpath("//span[.='"+string+"']"));
		assertTrue(errorMessage != null);
	}

	@When("I enter zero shares in the Add Stocks popup")
	public void i_enter_zero_shares_in_the_Add_Stocks_popup() {
		WebElement addStockShares = driver.findElement(By.cssSelector("#add-stock-form #shares"));
		addStockShares.sendKeys("0");
	}
	
	@When("I enter a negative number of shares in the Add Stocks popup")
	public void i_enter_a_negative_number_of_shares_in_the_Add_Stocks_popup() {
		WebElement addStockShares = driver.findElement(By.cssSelector("#add-stock-form #shares"));
		addStockShares.sendKeys("-2");
	}
	
	@When("I do not enter a purchase date in the Add Stocks popup")
	public void i_do_not_enter_a_purchase_date_in_the_Add_Stocks_popup() {
		WebElement addStockBuyDate = driver.findElement(By.id("date-purchased-portfolio"));
		addStockBuyDate.sendKeys("");
	}
	
	@When("I do not enter a purchase date in the View Stocks popup")
	public void i_do_not_enter_a_purchase_date_in_the_View_Stocks_popup() {
		WebElement addStockBuyDate = driver.findElement(By.id("date-purchased-viewed"));
		addStockBuyDate.sendKeys("");
	}

	@When("I enter a sell date before the purchase date in the Add Stocks popup")
	public void i_enter_a_sell_date_before_the_purchase_date_in_the_Add_Stocks_popup() throws InterruptedException {
		WebElement addStockSellDate = driver.findElement(By.id("date-sold-portfolio"));
		addStockSellDate.sendKeys("10/09/2020");
		addStockSellDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}
	
	@Then("I should see the Add Stock button in the Add Stocks popup")
	public void i_should_see_the_add_stock_button_in_the_add_stocks_popup() {
		WebElement button = driver.findElement(By.id("add-stock-submit"));
		assertTrue(button.getText().equalsIgnoreCase("Add Stock"));
	}
	
	@Then("I should see the View Stock button in the View Stocks popup")
	public void i_should_see_the_view_stock_button_in_the_view_stocks_popup() {
		WebElement button = driver.findElement(By.id("view-stock-submit"));
		assertTrue(button.getText().equalsIgnoreCase("View Stock"));
	}
	
	@Then("I should see the Cancel button in the Add Stocks popup")
	public void i_should_see_the_cancel_button_in_the_add_stocks_popup() {
		WebElement button = driver.findElement(By.id("add-stock-cancel"));
		assertTrue(button.getText().equalsIgnoreCase("Cancel"));
	}
	
	@Then("I should see the Cancel button in the View Stocks popup")
	public void i_should_see_the_cancel_button_in_the_view_stocks_popup() {
		WebElement button = driver.findElement(By.id("view-stock-cancel"));
		assertTrue(button.getText().equalsIgnoreCase("Cancel"));
	}

	@When("I click Add Stock in the Viewed Stocks box")
	public void i_click_Add_Stock_in_the_Viewed_Stocks_box() throws InterruptedException {
		Thread.sleep(1000);
		WebElement addStockButton = driver.findElement(By.id("view-stock-button"));
		addStockButton.click();
		Thread.sleep(1000);
	}

	@When("I enter a valid Ticker symbol in the View Stocks popup")
	public void i_enter_a_valid_Ticker_symbol_in_the_View_Stocks_popup() {
		WebElement addStockTicker = driver.findElement(By.cssSelector("#view-stock-form #ticker"));
	    addStockTicker.sendKeys("F");
	}

	@When("I enter a valid number of shares in the View Stocks popup")
	public void i_enter_a_valid_number_of_shares_in_the_View_Stocks_popup() {
		WebElement addStockShares = driver.findElement(By.cssSelector("#view-stock-form #shares"));
		addStockShares.sendKeys("2");
	}

	@When("I enter a valid purchase date in the View Stocks popup")
	public void i_enter_a_valid_purchase_date_in_the_View_Stocks_popup() {
		WebElement addStockBuyDate = driver.findElement(By.id("date-purchased-viewed"));
		addStockBuyDate.sendKeys("10/10/2020");
	}

	@When("I enter a valid sell date in the View Stocks popup")
	public void i_enter_a_valid_sell_date_in_the_View_Stocks_popup() throws InterruptedException {
		WebElement addStockSellDate = driver.findElement(By.id("date-sold-viewed"));
		addStockSellDate.sendKeys("10/15/2020");
		addStockSellDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}

	@When("I click View Stock")
	public void i_click_View_Stock() throws InterruptedException {
		WebElement viewStockSubmit = driver.findElement(By.id("view-stock-submit"));
		viewStockSubmit.click();
	}

	@Then("the stock should be added to the Viewed Stocks list")
	public void the_stock_should_be_added_to_the_Viewed_Stocks_list() throws InterruptedException {
		Thread.sleep(3000);
		WebElement checkStock = driver.findElement(By.xpath("//*[text()[contains(.,'Ford Motor Co')]]"));
		assertTrue(checkStock != null);
	}

	@When("I enter an invalid Ticker symbol in the View Stocks popup")
	public void i_enter_an_invalid_Ticker_symbol_in_the_View_Stocks_popup() {
		WebElement addStockTicker = driver.findElement(By.cssSelector("#view-stock-form #ticker"));
	    addStockTicker.sendKeys("ADSFSWDZFD");
	}

	@When("I enter any number of shares in the View Stocks popup")
	public void i_enter_any_number_of_shares_in_the_View_Stocks_popup() {
		WebElement addStockShares = driver.findElement(By.cssSelector("#view-stock-form #shares"));
		addStockShares.sendKeys("2");
	}

	@When("I enter any valid purchase date in the View Stocks popup")
	public void i_enter_any_valid_purchase_date_in_the_View_Stocks_popup() {
		WebElement addStockBuyDate = driver.findElement(By.id("date-purchased-viewed"));
		addStockBuyDate.sendKeys("10/10/2020");
	}

	@When("I enter any valid sell date in the View Stocks popup")
	public void i_enter_any_valid_sell_date_in_the_View_Stocks_popup() throws InterruptedException {
		WebElement addStockBuyDate = driver.findElement(By.id("date-sold-viewed"));
		addStockBuyDate.sendKeys("10/15/2020");
		addStockBuyDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}

	@Then("I should see the error {string} in the View Stocks popup")
	public void i_should_see_the_error_in_the_View_Stocks_popup(String string) throws InterruptedException {
		Thread.sleep(4000);
		WebElement errorMessage = driver.findElement(By.xpath("//span[.='"+string+"']"));
		assertTrue(errorMessage != null);
	}

	@When("I enter zero shares in the View Stocks popup")
	public void i_enter_zero_shares_in_the_View_Stocks_popup() {
		WebElement addStockShares = driver.findElement(By.cssSelector("#view-stock-form #shares"));
		addStockShares.sendKeys("0");
	}
	
	@When("I enter a negative number of shares in the View Stocks popup")
	public void i_enter_a_negative_number_of_shares_in_the_View_Stocks_popup() {
		WebElement addStockShares = driver.findElement(By.cssSelector("#view-stock-form #shares"));
		addStockShares.sendKeys("-2");
	}

	@When("I enter a sell date before the purchase date in the View Stocks popup")
	public void i_enter_a_sell_date_before_the_purchase_date_in_the_View_Stocks_popup() throws InterruptedException {
		WebElement addStockBuyDate = driver.findElement(By.id("date-sold-viewed"));
		addStockBuyDate.sendKeys("10/05/2020");
		addStockBuyDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}
	
	@When("I enter a purchase date older than one year ago in the View Stocks popup")
	public void i_enter_a_purchase_date_older_than_one_year_ago_in_the_View_Stocks_popup() {
		WebElement addStockBuyDate = driver.findElement(By.id("date-purchased-viewed"));
		addStockBuyDate.sendKeys("10/10/2019");
	}
	
	@When("I enter a sell date older than one year ago in the View Stocks popup")
	public void i_enter_a_sell_date_older_than_one_year_ago_in_the_View_Stocks_popup() throws InterruptedException {
		WebElement addStockBuyDate = driver.findElement(By.id("date-sold-viewed"));
		addStockBuyDate.sendKeys("10/12/2019");
		addStockBuyDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}
	
	@When("I enter a purchase date older than one year ago in the Add Stocks popup")
	public void i_enter_a_purchase_date_older_than_one_year_ago_in_the_Add_Stocks_popup() {
		WebElement addStockBuyDate = driver.findElement(By.id("date-purchased-portfolio"));
		addStockBuyDate.sendKeys("10/10/2019");
	}
	
	@When("I enter a sell date older than one year ago in the Add Stocks popup")
	public void i_enter_a_sell_date_older_than_one_year_ago_in_the_Add_Stocks_popup() throws InterruptedException {
		WebElement addStockBuyDate = driver.findElement(By.id("date-sold-portfolio"));
		addStockBuyDate.sendKeys("10/12/2019");
		addStockBuyDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}

	@When("I click the trash icon on a stock row in the Portfolio list")
	public void i_click_the_trash_icon_on_a_stock_row_in_the_Portfolio_list() throws InterruptedException {
		WebElement trashButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/table/tbody/tr[1]/td[4]/a"));
	    trashButton.click();
	    Thread.sleep(1000);
	}
	
	@Then("I should see the Delete Stock button")
	public void i_should_see_the_delete_stock_button() {
		WebElement deleteStock = driver.findElement(By.cssSelector(".confirmation-buttons .btn-primary"));
		assertTrue(deleteStock.getText().equalsIgnoreCase("Delete stock"));
	}
	
	@Then("I should see the Cancel button")
	public void i_should_see_the_cancel_button() {
		WebElement cancel = driver.findElement(By.cssSelector(".confirmation-buttons .btn-secondary"));
		assertTrue(cancel.getText().equalsIgnoreCase("Cancel"));
	}
	
	@When("I click Delete Stock")
	public void i_click_delete_stock() throws InterruptedException {
		WebElement confirmButton = driver.findElement(By.cssSelector(".confirmation-buttons .btn-primary"));
		confirmButton.click();
		Thread.sleep(3000);
	}

	@Then("the stock should be removed from the Portfolio list and graph")
	public void the_stock_should_be_removed_from_the_Portfolio_list_and_graph() {
		assertTrue(driver.findElements(By.xpath("//td[.='Microsoft Corp']")).isEmpty());
	}

	@When("I click the trash icon on a stock row in the Viewed Stocks list")
	public void i_click_the_trash_icon_on_a_stock_row_in_the_Viewed_Stocks_list() throws InterruptedException {
		WebElement trashButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[3]/div/table/tbody/tr[1]/td[4]/a"));
	    trashButton.click();
	    Thread.sleep(1000);
	}

	@Then("the stock should be removed from the Viewed Stocks list and graph")
	public void the_stock_should_be_removed_from_the_Viewed_Stocks_list_and_graph() {
		assertTrue(driver.findElements(By.xpath("//td[.='Apple Inc']")).isEmpty());
	}

	@When("I click Import CSV")
	public void i_click_Import_CSV() {
		WebElement importButton = driver.findElement(By.id("import-stock-button"));
		importButton.click();
	}
	
	@Then("I should see an Upload File button in CSV Popup")
	public void i_should_see_an_upload_file_button_in_csv_popup() throws InterruptedException {
		Thread.sleep(3000);
																  
		WebElement uploadFileButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div/div[2]/div/div[2]/form/button"));
		assertTrue(uploadFileButton != null);
	}
	
	@Then("I should see a Cancel button in CSV Popup")
	public void i_should_see_a_cancel_button_in_csv_popup() throws InterruptedException {
		Thread.sleep(3000);
		WebElement uploadFileButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div/div[2]/div/div[3]/button"));
		assertTrue(uploadFileButton != null);
	}

	@When("I upload a valid CSV file")
	public void i_upload_a_valid_CSV_file() {
		WebElement uploadFile = driver.findElement(By.id("csvImport"));
		File file = new File("stockTests/stock1.csv");
		uploadFile.sendKeys(file.getAbsolutePath());
	}

	@When("I click Upload File")
	public void i_click_Upload_File() {
		WebElement uploadFileButton = driver.findElement(By.id("import-stock-submit"));
	    uploadFileButton.click();
	}

	@Then("the stocks in the CSV should be added to the Portfolio list")
	public void the_stocks_in_the_CSV_should_be_added_to_the_Portfolio_list() {
		WebElement checkStock1 = driver.findElement(By.xpath("//*[text()[contains(.,'MSFT')]]"));
		assertTrue(checkStock1 != null);
		WebElement checkStock2 = driver.findElement(By.xpath("//*[text()[contains(.,'TSLA')]]"));
		assertTrue(checkStock2 != null);
		WebElement checkStock3 = driver.findElement(By.xpath("//*[text()[contains(.,'AAPL')]]"));
		assertTrue(checkStock3 != null);
	}

	@When("I upload an invalid CSV file")
	public void i_upload_an_invalid_CSV_file() {
		WebElement uploadFile = driver.findElement(By.id("csvImport"));
		File file = new File("stockTests/stock2.csv");
		uploadFile.sendKeys(file.getAbsolutePath());
	}

	@Then("I should see an error for invalid CVS")
	public void i_should_see_an_error_for_invalid_csv() throws InterruptedException {
		Thread.sleep(3000);
	    WebElement errorMessage = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div/div[2]/div/div[2]/form/div[2]/span"));
	    String error = errorMessage.getText();
	    assertTrue(error.contains("Row 1: requires minimum of 5 parameters."));
	    assertTrue(error.contains("Row 2, Col A: stock name is a required field."));
	    assertTrue(error.contains("Row 2, Col B: stock ticker is a required field."));
	    assertTrue(error.contains("Row 2, Col C: stock quantity must be a Integer."));
	    assertTrue(error.contains("Row 2, Col D: stock buy date must be a Long."));
	    assertTrue(error.contains("Row 3, Col A: stock name is a required field."));
	    assertTrue(error.contains("Row 3, Col B: stock ticker is a required field."));
	    assertTrue(error.contains("Row 2, Col C: stock quantity must be a Integer."));
	    assertTrue(error.contains("Row 4, Col C: stock quantity must be a Integer."));
	    assertTrue(error.contains("Row 4, Col D: stock buy date must be a Long."));
	    assertTrue(error.contains("Row 4, Col E: stock sell date must be a Long."));
	    assertTrue(error.contains("Row 5, Col C: stock quantity must be a Integer."));
	    assertTrue(error.contains("Row 6, Col D: stock buy date must be a Long."));
	    assertTrue(error.contains("Row 6, Col E: stock sell date must be a Long."));
	}

	@When("I click the {int} week button of the home page.")
	public void i_click_the_week_button_of_the_home_page(Integer int1) throws InterruptedException {
	    WebElement button = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/form/div/input[2]"));
	    button.click();
	    Thread.sleep(4000);
	}
	
	@Then("the graph should re-adjust on the home page.")
	public void the_graph_should_re_adjust_on_the_home_page() throws InterruptedException {
		Thread.sleep(4000);
		String default_val = "[\"{\\\"borderColor\\\":[\\\"rgba(104,152,204, 1)\\\"],\\\"data\\\":[626.6999816894399,639.06001281738,686.73001098633,642.75,612.0899963379,601.16999816895,623.46002197266,618.57000732423,647.42999267577,658.98001098633,648.68998718262,607.4100036621,669.86997985839],\\\"borderWidth\\\":1,\\\"label\\\":\\\"Portfolio value in $\\\",\\\"fill\\\":\\\"false\\\"}\",\"{\\\"borderColor\\\":[\\\"rgba(238,96,6, 1)\\\"],\\\"data\\\":[1608.74005126954,1741.18003845212,1747.33996582026,1693.43998718258,1568,1495.75994873046,1571.9199829102,1582.27995300298,1637.5800170898,1666.27995300298,1610.56001281742,1524.0400085449,1666.4199829102],\\\"borderWidth\\\":1,\\\"label\\\":\\\"Apple Inc value in $\\\",\\\"fill\\\":\\\"false\\\"}\"]\r\n"
				+ "";
		String script = "return document.getElementById('hiddendiv').innerHTML";
		String val = (String) ((JavascriptExecutor) driver).executeScript(script);
		System.out.println(val);
		assertTrue(!default_val.equals(val));
	}
	
	@When("I click the {int} month button of the home page.")
	public void i_click_the_month_button_of_the_home_page(Integer int1) throws InterruptedException {
		WebElement button = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/form/div/input[3]"));
	    button.click();
	    Thread.sleep(4000);
	}
	
	@When("I click the {int} year button of the home page.")
	public void i_click_the_year_button_of_the_home_page(Integer int1) throws InterruptedException {
		WebElement button = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/form/div/input[4]"));
	    button.click();
	    Thread.sleep(4000);
	}
	
	@When("I click the {int} day button of the home page.")
	public void i_click_the_day_button_of_the_home_page(Integer int1) throws InterruptedException {
		WebElement button = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/form/div/input[1]"));
	    button.click();
	    Thread.sleep(4000);
	}
	
	@When("I click the zoom in button of the home page.")
	public void click_zoom_in() throws InterruptedException, IOException {
		WebElement button = driver.findElement(By.xpath("//*[@id=\"zoomin\"]"));
	    button.click();
	    Thread.sleep(400);
	    WebElement graph = driver.findElement(By.xpath("//*[@id=\"myChart\"]"));
	    captureElementScreenshot(graph);
	}
	
	@Then("the zoom in button should have a plus symbol.")
	public void the_zoom_in_button_should_have_a_plus_symbol() {
		WebElement button = driver.findElement(By.xpath("//*[@id=\"zoomin\"]"));
		assertTrue(button.getText().equalsIgnoreCase("+"));
	}
	
	@When("I click the zoom out button of the home page.")
	public void click_zoom_out() throws InterruptedException, IOException {
		Thread.sleep(4000);
		WebElement button = driver.findElement(By.xpath("//*[@id=\"zoomout\"]"));
	    button.click();
	    Thread.sleep(400);
	    WebElement graph = driver.findElement(By.xpath("//*[@id=\"myChart\"]"));
	    captureElementScreenshot(graph);
	}
	
	@Then("the zoom out button should have a minus symbol.")
	public void the_zoom_out_button_should_have_a_minus_symbol() throws InterruptedException {
		Thread.sleep(4000);
		WebElement button = driver.findElement(By.xpath("//*[@id=\"zoomout\"]"));
		assertTrue(button.getText().equalsIgnoreCase("-"));
	}
	
	@When("I click the custom range button.")
	public void click_cust_range() throws InterruptedException, IOException {
		Thread.sleep(4000);
		WebElement button = driver.findElement(By.xpath("//*[@id=\"customrange-button\"]"));
	    button.click();
	}
	
	@When("I enter proper start,end dates n submit")
	public void proper_info() throws InterruptedException {
		WebElement start = driver.findElement(By.xpath("//*[@id=\"date-purchased\"]")); 
		start.sendKeys("10/10/2020");
		WebElement end = driver.findElement(By.xpath("//*[@id=\"date-sold\"]")); 
		end.sendKeys("10/15/2020");
		driver.findElement(By.xpath("//*[@id=\"customrange-submit\"]")).click();
		Thread.sleep(4000);
	}
	
	@When("I enter a start date,leave end empty n submit")
	public void empty_end() throws InterruptedException {
		WebElement start = driver.findElement(By.xpath("//*[@id=\"date-purchased\"]")); 
		start.sendKeys("10/10/2020");
		WebElement end = driver.findElement(By.xpath("//*[@id=\"date-sold\"]")); 
		end.sendKeys("");
		driver.findElement(By.xpath("//*[@id=\"customrange-submit\"]")).click();
		Thread.sleep(4000);
	}
	
	@When("I enter a end date,leave start empty n submit")
	public void empty_start() throws InterruptedException {
		WebElement start = driver.findElement(By.xpath("//*[@id=\"date-purchased\"]")); 
		start.sendKeys("");
		WebElement end = driver.findElement(By.xpath("//*[@id=\"date-sold\"]")); 
		end.sendKeys("10/15/2020");
		driver.findElement(By.xpath("//*[@id=\"customrange-submit\"]")).click();
		Thread.sleep(4000);
		
	}
	
	@When("I enter proper start date, end date using the popup n submit")
	public void proper_info_popup() throws InterruptedException {
		WebElement start = driver.findElement(By.xpath("//*[@id=\"date-purchased\"]")); 
		start.click();
		driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/table/tbody/tr[1]/td[1]")).click();
		WebElement end = driver.findElement(By.xpath("//*[@id=\"date-sold\"]")); 
		end.click();
		driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/table/tbody/tr[2]/td[4]")).click();
		end.sendKeys(Keys.ESCAPE);
		driver.findElement(By.xpath("//*[@id=\"customrange-submit\"]")).click();
		Thread.sleep(4000);
	}
	
	
	
	@When("I enter incorrect start,end dates n submit")
	public void inproper_info() throws InterruptedException
	{
		WebElement start = driver.findElement(By.xpath("//*[@id=\"date-purchased\"]")); 
		start.sendKeys("10/10/2020");
		WebElement end = driver.findElement(By.xpath("//*[@id=\"date-sold\"]")); 
		end.sendKeys("10/10/2020");
		driver.findElement(By.xpath("//*[@id=\"customrange-submit\"]")).click();
		Thread.sleep(4000);
	}
	@Then("I should see an error message.")
	public void bad_inp()
	{
		WebElement err = driver.findElement(By.xpath("//*[@id=\"customrange-form\"]/div[3]/span"));  
		assertTrue(!err.getText().equals(""));
	}
	@Then("the graph should zoom in on the home page.")
	public void graph_zoom_in()
	{
		File A = new File("OG.PNG");
		File B = new File("to_test.PNG");
		assertTrue(!compareImage(A,B));
	}
	@Then("the graph should zoom out on the home page.")
	public void graph_zoom_out()
	{
		File A = new File("OG.PNG");
		File B = new File("to_test.PNG");
		assertTrue(!compareImage(A,B));
	}
	@When("I click the snp checkbox of the home page.")
	public void snp_check() throws InterruptedException, IOException
	{
		WebElement button = driver.findElement(By.xpath("//*[@id=\"graph-container\"]/form/div[2]/input[1]"));
	    button.click();
	    Thread.sleep(4000);
	}
	@When("I click on the toggle button of a portfolio stock.")
	public void portfolio_toggle() throws InterruptedException, IOException
	{
		Thread.sleep(4000);
		WebElement button = driver.findElement(By.id("snpcheck"));
	    button.click();
	    Thread.sleep(4000);
	}
	
	@Then("the graph should have a default range of 3 months.")
	public void graph_def()
	{
		String default_val = "[\"{\\\"borderColor\\\":[\\\"rgba(104,152,204, 1)\\\"],\\\"data\\\":[626.6999816894399,639.06001281738,686.73001098633,642.75,612.0899963379,601.16999816895,623.46002197266,618.57000732423,647.42999267577,658.98001098633,648.68998718262,607.4100036621,669.86997985839],\\\"borderWidth\\\":1,\\\"label\\\":\\\"Portfolio value in $\\\",\\\"fill\\\":\\\"false\\\"}\",\"{\\\"borderColor\\\":[\\\"rgba(238,96,6, 1)\\\"],\\\"data\\\":[1608.74005126954,1741.18003845212,1747.33996582026,1693.43998718258,1568,1495.75994873046,1571.9199829102,1582.27995300298,1637.5800170898,1666.27995300298,1610.56001281742,1524.0400085449,1666.4199829102],\\\"borderWidth\\\":1,\\\"label\\\":\\\"Apple Inc value in $\\\",\\\"fill\\\":\\\"false\\\"}\"]\r\n"
				+ "";
		String script = "return document.getElementById('hiddendiv').innerHTML";
		String val = (String) ((JavascriptExecutor) driver).executeScript(script);
		System.out.println(val);
		assertTrue(!default_val.equals(val));
	}
	
	@Then("the graph should have distinct, clearly visible dates.")
	public void clear_dates()
	{
		String default_val = "[\"{\\\"borderColor\\\":[\\\"rgba(104,152,204, 1)\\\"],\\\"data\\\":[626.6999816894399,639.06001281738,686.73001098633,642.75,612.0899963379,601.16999816895,623.46002197266,618.57000732423,647.42999267577,658.98001098633,648.68998718262,607.4100036621,669.86997985839],\\\"borderWidth\\\":1,\\\"label\\\":\\\"Portfolio value in $\\\",\\\"fill\\\":\\\"false\\\"}\",\"{\\\"borderColor\\\":[\\\"rgba(238,96,6, 1)\\\"],\\\"data\\\":[1608.74005126954,1741.18003845212,1747.33996582026,1693.43998718258,1568,1495.75994873046,1571.9199829102,1582.27995300298,1637.5800170898,1666.27995300298,1610.56001281742,1524.0400085449,1666.4199829102],\\\"borderWidth\\\":1,\\\"label\\\":\\\"Apple Inc value in $\\\",\\\"fill\\\":\\\"false\\\"}\"]\r\n"
				+ "";
		String script = "return document.getElementById('hiddendiv').innerHTML";
		String val = (String) ((JavascriptExecutor) driver).executeScript(script);
		System.out.println(val);
		assertTrue(!default_val.equals(val));
	}

	@When("{int} seconds of inactivity occurs")
	public void seconds_of_inactivity_occurs(Integer int1) throws InterruptedException {
		int ms = int1 * 1000;
		Thread.sleep(ms + 7000);
	}

	@Then("I should see an alert that I am being logged out")
	public void i_should_see_an_alert_that_I_am_being_logged_out() {
		boolean alert;
	    try {
	    	driver.switchTo().alert();
	    	alert = true;
	    } catch (NoAlertPresentException nape) {
	    	alert = false;
	    }
	    assertTrue(alert);
	}

	@Given("I am not logged in")
	public void i_am_not_logged_in() throws InterruptedException {
		driver.get(ROOT_URL+"signIn.jsp");
		Thread.sleep(3000);
	}

	@When("I attempt to access {string}")
	public void i_attempt_to_access(String string) {
		driver.get(string);
	}

	@Then("I should be redirected to the sign in page")
	public void i_should_be_redirected_to_the_sign_in_page() throws InterruptedException {
		Thread.sleep(3000);
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase(ROOT_URL+"signIn.jsp"));
		Thread.sleep(3000);
	}
	
	@Given("I have the brower open")
	public void i_have_the_brower_open() throws InterruptedException {
		Thread.sleep(500);
	}
	
	@Then("I should not be able to access the page")
	public void i_should_not_be_able_to_access_the_page() throws InterruptedException {
		Thread.sleep(2000);
		assertTrue(driver.findElements(By.xpath("//*[text()[contains(.,'Stock')]]")).isEmpty());
	}

	@When("I resize to mobile dimensions")
	public void i_resize_to_mobile_dimensions() throws InterruptedException {
		Dimension mobile = new Dimension(480,800);
		driver.manage().window().setSize(mobile);
		Thread.sleep(3000);
	}
	@Then("the navbar should still be visible")
	public void the_navbar_should_still_be_visible() throws InterruptedException {
		assertTrue(driver.findElements( By.className("navbar") ).size() != 0);
	}
	@Then("the signout button should still be visible")
	public void the_signout_button_should_still_be_visible(){
		assertTrue(driver.findElements( By.cssSelector(".navbar .button") ).size() != 0);
	}
	@Then("the graph container should still be visible")
	public void the_graph_container_should_still_be_visible() {
		assertTrue(driver.findElements( By.id("graph-container") ).size() != 0);
	}
	@Then("the portfolio list should still be visible")
	public void the_portfolio_list_should_still_be_visible() {
		assertTrue(driver.findElements( By.id("portfolio-container") ).size() != 0);
	}
	@Then("the viewed stocks list should still be visible")
	public void the_viewed_stocks_list_should_still_be_visible() {
		assertTrue(driver.findElements( By.id("viewed-container") ).size() != 0);
	}
	
	@Then("the login form should still be visible in SI")
	public void the_login_form_should_still_be_visible_in_SI() {
		assertTrue(driver.findElements( By.id("logInForm") ).size() != 0);
	}

	@Then("the button should still be visible in SI")
	public void the_button_should_still_be_visible_in_SI() {
		assertTrue(driver.findElements( By.id("b") ).size() != 0);
	}

	@Then("the submit button should still be visible in SU")
	public void the_submit_button_should_still_be_visible_in_SU() {
		assertTrue(driver.findElements( By.id("b") ).size() != 0);
	}

	@Then("the cancel button should still be visible in SU")
	public void the_cancel_button_should_still_be_visible_in_SU() {
		assertTrue(driver.findElements( By.id("c") ).size() != 0);
	}

	@Then("the reg form should still be visible in SU")
	public void the_reg_form_should_still_be_visible_in_SU() {
		assertTrue(driver.findElements( By.id("register") ).size() != 0);
	}
	
	
	
	@Given("I am on the home page with a new account")
	public void i_am_on_the_home_page_with_a_new_account() throws NoSuchAlgorithmException, SQLException, InterruptedException {
		String newUser = RandomStringUtils.randomAlphanumeric(10);
		System.out.println(newUser);
		PasswordAuthentication passAuth = new PasswordAuthentication();
		String hashedPass = passAuth.hash("12345678", null, null);
		DatabaseClient database = new DatabaseClient();
		database.createUser(newUser, hashedPass);
		
		int userID = database.getUser(passAuth, newUser, "12345678");
		System.out.println(userID);
		
		
		driver.get(ROOT_URL+"signIn.jsp");
	    WebElement username = driver.findElement(By.id("username"));
		username.sendKeys(newUser);
		WebElement password = driver.findElement(By.id("pass"));
		password.sendKeys("12345678");
		WebElement searchButton = driver.findElement(By.id("b"));
	    searchButton.click();
	    Thread.sleep(3000);
	    WebElement portfolioValue = driver.findElement(By.id("portfolio-value"));
	    assertTrue(portfolioValue.getText().equals("$0.0"));
	}
	
	@When("I enter a future sell date in the Add Stocks popup")
	public void i_enter_a_future_sell_date_in_the_Add_Stocks_popup() throws InterruptedException {
		WebElement addStockSellDate = driver.findElement(By.id("date-sold-portfolio"));
		addStockSellDate.sendKeys("03/15/2021");
		addStockSellDate.sendKeys(Keys.ESCAPE);
		Thread.sleep(1000);
	}
	
	@When("I click on the toggle-all button of a portfolio stock.") 
	public void i_click_on_the_toggleall_button() throws InterruptedException{
		WebElement deselectAll = driver.findElement(By.id("deselect-all"));
		deselectAll.click();
		Thread.sleep(1000);
	}
	
	@When("I click on the toggle button for the viewed portfolio stock.")
	public void i_click_on_the_view_portfolio_toggle() throws InterruptedException {
		WebElement toggleButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[3]/div/table/tbody/tr[1]/td[3]/label"));
		toggleButton.click();
		Thread.sleep(1000);
	}

	@Then("the porfolio value should no longer be zero")
	public void the_porfolio_value_should_no_longer_be_zero() throws InterruptedException {
		Thread.sleep(1000);
		WebElement portfolioValue = driver.findElement(By.id("portfolio-value"));
		assertTrue(!portfolioValue.getText().equals("$0.0"));
	}

	@Given("I am on the home page with only one stock in the porfolio")
	public void i_am_on_the_home_page_with_only_one_stock_in_the_porfolio() throws NoSuchAlgorithmException, SQLException, InterruptedException {
		String newUser = RandomStringUtils.randomAlphanumeric(10);
		System.out.println(newUser);
		PasswordAuthentication passAuth = new PasswordAuthentication();
		String hashedPass = passAuth.hash("12345678", null, null);
		DatabaseClient database = new DatabaseClient();
		database.createUser(newUser, hashedPass);
		
		int userID = database.getUser(passAuth, newUser, "12345678");
		Stock s = new Stock("Amazon.com Inc", "AMZN", null, 2, 1602313200000L, 1615791600000L);
		database.addStockToPortfolio(userID, s);
		
		
		driver.get(ROOT_URL+"signIn.jsp");
	    WebElement username = driver.findElement(By.id("username"));
		username.sendKeys(newUser);
		WebElement password = driver.findElement(By.id("pass"));
		password.sendKeys("12345678");
		WebElement searchButton = driver.findElement(By.id("b"));
	    searchButton.click();
	    Thread.sleep(3000);
	    WebElement portfolioValue = driver.findElement(By.id("portfolio-value"));
	    assertTrue(!portfolioValue.getText().equals("$0.0"));
	}

	@Then("the porfolio value should be back to zero")
	public void the_porfolio_value_should_be_back_to_zero() throws InterruptedException {
		Thread.sleep(1000);
		WebElement portfolioValue = driver.findElement(By.id("portfolio-value"));
		assertTrue(portfolioValue.getText().equals("$0.0"));
	}

	@Then("the porfolio percentage change should no longer be zero")
	public void the_porfolio_percentage_change_should_no_longer_be_zero() throws InterruptedException {
		Thread.sleep(1000);
		WebElement portfolioPercentage = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div[1]/div[1]/span"));
		assertTrue(!portfolioPercentage.getText().contains("0.0%"));
	}

	@Then("the porfolio percentage change should be back to zero")
	public void the_porfolio_percentage_change_should_be_back_to_zero() throws InterruptedException {
		Thread.sleep(1000);
		WebElement portfolioPercentage = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div[1]/div[1]/span"));
		assertTrue(portfolioPercentage.getText().contains("0.0%"));
	}
	
	@Given("I am on the home page with deselect all pressed")
	public void i_am_on_the_home_page_with_deselect_all_pressed() throws NoSuchAlgorithmException, SQLException, InterruptedException {
		String newUser = RandomStringUtils.randomAlphanumeric(10);
		System.out.println(newUser);
		PasswordAuthentication passAuth = new PasswordAuthentication();
		String hashedPass = passAuth.hash("12345678", null, null);
		DatabaseClient database = new DatabaseClient();
		database.createUser(newUser, hashedPass);
		
		int userID = database.getUser(passAuth, newUser, "12345678");
		Stock s1 = new Stock("Amazon.com Inc", "AMZN", null, 2, 1602313200000L, 1615791600000L);
		database.addStockToPortfolio(userID, s1);
		
		Stock s2 = new Stock("Apple Inc", "AAPL", null, 3, 1602313200000L, 1615791600000L);
		database.addStockToPortfolio(userID, s2);
		
		
		driver.get(ROOT_URL+"signIn.jsp");
	    WebElement username = driver.findElement(By.id("username"));
		username.sendKeys(newUser);
		WebElement password = driver.findElement(By.id("pass"));
		password.sendKeys("12345678");
		WebElement searchButton = driver.findElement(By.id("b"));
	    searchButton.click();
	    Thread.sleep(3000);
	    driver.get(ROOT_URL+"api/toggleStock?type=deSelectAll");
	    Thread.sleep(1000);
	}

	@When("I click the select all button")
	public void i_click_the_select_all_button() {
		WebElement selectAll = driver.findElement(By.id("select-all"));
		selectAll.click();
	}

	@Given("I am on the home page with select all pressed")
	public void i_am_on_the_home_page_with_select_all_pressed() throws NoSuchAlgorithmException, SQLException, InterruptedException {
		String newUser = RandomStringUtils.randomAlphanumeric(10);
		System.out.println(newUser);
		PasswordAuthentication passAuth = new PasswordAuthentication();
		String hashedPass = passAuth.hash("12345678", null, null);
		DatabaseClient database = new DatabaseClient();
		database.createUser(newUser, hashedPass);
		
		int userID = database.getUser(passAuth, newUser, "12345678");
		Stock s1 = new Stock("Amazon.com Inc", "AMZN", null, 2, 1602313200000L, 1615791600000L);
		database.addStockToPortfolio(userID, s1);
		
		Stock s2 = new Stock("Apple Inc", "AAPL", null, 3, 1602313200000L, 1615791600000L);
		database.addStockToPortfolio(userID, s2);
		
		
		driver.get(ROOT_URL+"signIn.jsp");
	    WebElement username = driver.findElement(By.id("username"));
		username.sendKeys(newUser);
		WebElement password = driver.findElement(By.id("pass"));
		password.sendKeys("12345678");
		WebElement searchButton = driver.findElement(By.id("b"));
	    searchButton.click();
	    Thread.sleep(3000);
	}

	@When("I click the deselect all button")
	public void i_click_the_deselect_all_button() {
		WebElement deSelectAll = driver.findElement(By.id("deselect-all"));
		deSelectAll.click();
	}
	
	@When("I leave the end date,start empty empty n submit.")
	public void proper_info_fllwed_empty() throws InterruptedException {
		driver.findElement(By.xpath("//*[@id=\"customrange-submit\"]")).click();
		Thread.sleep(4000);
	}
	
	@Then("the graph should have start as earliest start and end as curr day.")
	public void graph_erly_start_n_end() throws InterruptedException
	{
		Thread.sleep(4000);
		String default_val = "[\"{\\\"borderColor\\\":[\\\"rgba(104,152,204, 1)\\\"],\\\"data\\\":[626.6999816894399,639.06001281738,686.73001098633,642.75,612.0899963379,601.16999816895,623.46002197266,618.57000732423,647.42999267577,658.98001098633,648.68998718262,607.4100036621,669.86997985839],\\\"borderWidth\\\":1,\\\"label\\\":\\\"Portfolio value in $\\\",\\\"fill\\\":\\\"false\\\"}\",\"{\\\"borderColor\\\":[\\\"rgba(238,96,6, 1)\\\"],\\\"data\\\":[1608.74005126954,1741.18003845212,1747.33996582026,1693.43998718258,1568,1495.75994873046,1571.9199829102,1582.27995300298,1637.5800170898,1666.27995300298,1610.56001281742,1524.0400085449,1666.4199829102],\\\"borderWidth\\\":1,\\\"label\\\":\\\"Apple Inc value in $\\\",\\\"fill\\\":\\\"false\\\"}\"]\r\n"
				+ "";
		String script = "return document.getElementById('hiddendiv').innerHTML";
		String val = (String) ((JavascriptExecutor) driver).executeScript(script);
		System.out.println(val);
		assertTrue(!default_val.equals(val));
	}
	
	@Then("the color of text should be green n up if positive or red n down if negative.")
	public void color_n_triangle() throws InterruptedException
	{
		Thread.sleep(2000);
		WebElement info = driver.findElement(By.xpath("//*[@id=\"arrow2\"]"));  
		String text = info.getText();
		String color =  info.getCssValue("color");
		if(text.contains("+"))
		{
			assertTrue(!color.equals("red"));
		}
		else
		{
			assertTrue(!color.equals("green"));
		}
	}
	
	@After()
	public void after() {
		driver.quit();
	}
}
