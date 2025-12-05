package qtriptest.tests;

import qtriptest.DP;
import qtriptest.DriverSingleton;
import qtriptest.ReportSingleton;
import qtriptest.pages.AdventurePage;
import qtriptest.pages.HomePage;
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

public class testCase_02 {
    public static RemoteWebDriver driver;
    public static ExtentTest test;
    public static ExtentReports report;

    @BeforeTest(alwaysRun = true)
    public void setup() throws IOException {
        driver = DriverSingleton.getInstance("CHROME");
        report = ReportSingleton.getInstance();

        test = report.startTest("testCase02");
    }

    @Test(dataProvider = "dpM", dataProviderClass = DP.class, priority = 2, groups = {"Search and Filter flow"})
    public void TestCase02(String CityName, String Category_Filter, String DurationFilter, String ExpectedFilteredResults, String ExpectedUnFilteredResults) throws InterruptedException {
        Assertion assertion = new Assertion();
        boolean status;
        
        try {
            HomePage home = new HomePage(driver);
            home.navigateToHomePage();
            test.log(LogStatus.INFO, "Navigated to Home Page");

            Thread.sleep(2000);
            home.searchCity("chennai");
            test.log(LogStatus.INFO, "Searched for city 'chennai'");
            
            Thread.sleep(2000);
            status = home.assertAutoCompleteText("chennai");
            assertion.assertFalse(status, "Places found");
            test.log(LogStatus.PASS, "Auto-complete text for 'chennai' checked");

            Thread.sleep(2000);
            home.searchCity(CityName);
            test.log(LogStatus.INFO, "Searched for city '" + CityName + "'");

            Thread.sleep(2000);
            status = home.assertAutoCompleteText(CityName);
            assertion.assertTrue(status, "Places are not found");
            test.log(LogStatus.PASS, "Auto-complete text for '" + CityName + "' checked");

            home.selectCity(CityName);
            test.log(LogStatus.INFO, "Selected city '" + CityName + "'");

            AdventurePage adventurepg = new AdventurePage(driver);
            adventurepg.selectDuration_dropdown(DurationFilter);
            test.log(LogStatus.INFO, "Selected duration filter '" + DurationFilter + "'");

            adventurepg.selectCategory_dropdown(Category_Filter);
            test.log(LogStatus.INFO, "Selected category filter '" + Category_Filter + "'");

            String expectedFilterResult1 = adventurepg.getResultCount();
            assertion.assertEquals(ExpectedFilteredResults, expectedFilterResult1, "Result values are not equal");
            test.log(LogStatus.PASS, "Verified filtered results");

            adventurepg.clearCategory();
            adventurepg.clearDuration();
            test.log(LogStatus.INFO, "Cleared category and duration filters");

            String expectedUnfilterResult1 = adventurepg.getResultCount();
            assertion.assertEquals(ExpectedUnFilteredResults, expectedUnfilterResult1, "Result values are not equal");
            test.log(LogStatus.PASS, "Verified unfiltered results");

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
