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

public class testCase_04 {
    public static RemoteWebDriver driver;
    public static ExtentTest test;
    public static ExtentReports report;

    @BeforeTest(alwaysRun = true)
    public void setup() throws IOException {
        driver = DriverSingleton.getInstance("CHROME");
        report = ReportSingleton.getInstance();

        test = report.startTest("testCase04");
    }

    @Test(dataProvider = "dpM", dataProviderClass = DP.class, groups = {"Reliability Flow"}, priority = 4)
    public void TestCase04(String NewUserName, String Password, String dataset1, String dataset2, String dataset3) throws InterruptedException {
        Assertion assertion = new Assertion();
        boolean status;

        try {
            HomePage home = new HomePage(driver);
            home.navigateToHomePage();
            test.log(LogStatus.INFO, "Navigated to Home Page");

            home.ClickRegister();
            test.log(LogStatus.INFO, "Clicked on Register");

            RegisterPage register = new RegisterPage(driver);
            Thread.sleep(2000);
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

            tc04Execution(dataset1, home);
            tc04Execution(dataset2, home);
            tc04Execution(dataset3, home);

            home.ClickReservation();
            test.log(LogStatus.INFO, "Clicked on Reservation");

            Thread.sleep(2000);
            HistoryPage hp = new HistoryPage(driver);

            Thread.sleep(2000);
            int c = hp.getTransactionCount();
            assertion.assertEquals(c, 3, "Transaction count is invalid");
            test.log(LogStatus.PASS, "Verified transaction count is 3");
        } catch (AssertionError e) {
            test.log(LogStatus.FAIL, test.addScreenCapture(capture(driver)) + "Test failed, reason: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            test.log(LogStatus.FAIL, "Test failed due to exception: " + e.getMessage());
            throw e;
        }
    }

    public static void tc04Execution(String Dataset, HomePage home) throws InterruptedException {
        Assertion assertion = new Assertion();
        boolean status;
        String[] data = Dataset.split(";");
        String SearchCity = data[0];
        String SearchAdventure = data[1];
        String guestName = data[2];
        String date = data[3];
        String count = data[4];

        home.searchCity(SearchCity);
        Thread.sleep(1000);
        status = home.assertAutoCompleteText(SearchCity);
        assertion.assertTrue(status, "Places are not found");
        test.log(LogStatus.PASS, "Verified auto-complete text for '" + SearchCity + "'");

        Thread.sleep(1000);
        home.selectCity(SearchCity);
        test.log(LogStatus.INFO, "Selected city '" + SearchCity + "'");

        AdventurePage adventurepg = new AdventurePage(driver);
        adventurepg.selectAdventure(SearchAdventure);
        test.log(LogStatus.INFO, "Selected adventure: " + SearchAdventure);

        AdventureDetailsPage adventureDetailspg = new AdventureDetailsPage(driver);
        Thread.sleep(3000);
        adventureDetailspg.bookAdventure(guestName, date, count);
        test.log(LogStatus.INFO, "Booked adventure with guest name: " + guestName);

        Thread.sleep(2000);
        status = adventureDetailspg.isBookingSucessful();
        Thread.sleep(2000);
        assertion.assertTrue(status, "Not Booked");
        test.log(LogStatus.PASS, "Adventure booking successful");

        home.navigateToHomePage();
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
