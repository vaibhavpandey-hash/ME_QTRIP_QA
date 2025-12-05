package qtriptest.tests;

import qtriptest.DP;
import qtriptest.DriverSingleton;
import qtriptest.ReportSingleton;
import qtriptest.pages.AdventureDetailsPage;
import qtriptest.pages.AdventurePage;
import qtriptest.pages.HistoryPage;
import qtriptest.pages.HomePage;
import qtriptest.pages.LoginPage;
import qtriptest.pages.RegisterPage;
import java.io.File;
import java.io.IOException;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

public class testCase_03 {
    public static RemoteWebDriver driver;
    public static ExtentTest test;
    public static ExtentReports report;

    @BeforeTest(alwaysRun = true)
    public void setup() throws IOException {
        driver = DriverSingleton.getInstance("CHROME");
        report = ReportSingleton.getInstance();
        test = report.startTest("testCase03");
    }

    @Test(dataProvider = "dpM", dataProviderClass = DP.class, groups = {"Booking and Cancellation Flow"}, priority = 3)
    public void TestCase03(String NewUserName, String Password, String SearchCity, String AdventureName, String GuestName, String Date, String count) throws InterruptedException {
        Assertion assertion = new Assertion();
        boolean status;

        try {
            HomePage home = new HomePage(driver);
            home.navigateToHomePage();
            test.log(LogStatus.INFO, "Navigated to Home Page");

            home.ClickRegister();
            test.log(LogStatus.INFO, "Clicked on Register");

            RegisterPage register = new RegisterPage(driver);
            Thread.sleep(3000);
            boolean status1 = register.VerifyNavigationRegister();
            assertion.assertTrue(status1, "Not Moving to Register Page");
            test.log(LogStatus.PASS, "Navigated to Register Page");

            String username = register.RegisterUser(NewUserName, Password, true);
            test.log(LogStatus.INFO, "Registered user: " + username);

            LoginPage login = new LoginPage(driver);
            Thread.sleep(2000);
            login.PerformLogin(username, Password);
            test.log(LogStatus.INFO, "Performed Login");

            Thread.sleep(2000);
            status = home.isUserLoggedin();
            assertion.assertTrue(status, "User is not logged in");
            test.log(LogStatus.PASS, "User successfully logged in");

            home.searchCity(SearchCity);
            test.log(LogStatus.INFO, "Searched for city '" + SearchCity + "'");

            Thread.sleep(2000);
            status = home.assertAutoCompleteText(SearchCity);
            assertion.assertTrue(status, "Places are not found");
            test.log(LogStatus.PASS, "Auto-complete text for '" + SearchCity + "' checked");

            home.selectCity(SearchCity);
            test.log(LogStatus.INFO, "Selected city '" + SearchCity + "'");

            AdventurePage adventurepg = new AdventurePage(driver);
            adventurepg.selectAdventure(AdventureName);
            test.log(LogStatus.INFO, "Selected adventure: " + AdventureName);

            AdventureDetailsPage adventureDetailspg = new AdventureDetailsPage(driver);
            adventureDetailspg.bookAdventure(GuestName, Date, count);
            test.log(LogStatus.INFO, "Booked adventure");

            Thread.sleep(4000);
            status = adventureDetailspg.isBookingSucessful();
            assertion.assertTrue(status, "Not Booked");
            test.log(LogStatus.PASS, "Adventure successfully booked");

            adventureDetailspg.ClickReservation();
            test.log(LogStatus.INFO, "Clicked on Reservation");

            HistoryPage hp = new HistoryPage(driver);
            String TransactionID = hp.getReservation();
            hp.cancelReservation(TransactionID);
            test.log(LogStatus.INFO, "Cancelled reservation with Transaction ID: " + TransactionID);

            Thread.sleep(4000);
            status = hp.checkTransactioIDPresent(TransactionID);
            Thread.sleep(3000);
            assertion.assertFalse(status, "Reservation is failed");
            test.log(LogStatus.PASS, "Reservation successfully cancelled");

        } catch (AssertionError e) {
            test.log(LogStatus.FAIL, test.addScreenCapture(capture(driver)) + "Test failed, reason: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Test failed due to exception: " + e.getMessage());
            throw e;
        }
    }

    public static String capture(RemoteWebDriver driver) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File Dest = new File(System.getProperty("user.dir") + "/QKARTImages/" + System.currentTimeMillis() + ".png");
        String errflpath = Dest.getAbsolutePath();
        try {
            FileUtils.copyFile(scrFile, Dest);
        } catch (IOException e) {
            System.out.println("Error copying the file");
        }
        return errflpath;
    }

    @AfterSuite
    public void quitDriver() {
        driver.quit();

        // End the test
        report.endTest(test);
        // Write the test to filesystem
        report.flush();
    }
}
