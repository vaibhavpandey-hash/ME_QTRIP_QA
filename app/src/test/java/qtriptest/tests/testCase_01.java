package qtriptest.tests;

import qtriptest.DP;
import qtriptest.DriverSingleton;
import qtriptest.pages.HomePage;
import qtriptest.pages.LoginPage;
import qtriptest.pages.RegisterPage;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

public class testCase_01 {
    public static RemoteWebDriver driver;

    @BeforeTest(alwaysRun = true)
    public void setup() throws IOException {
        driver = DriverSingleton.getInstance("CHROME");
    }

    @Test(dataProvider = "dpM", dataProviderClass = DP.class, priority = 1, groups = {"Login Flow"})
    public void TestCase01(String username, String password) throws InterruptedException {
        Assertion assertion = new Assertion();
        boolean status;

        try {
            HomePage home = new HomePage(driver);
            home.navigateToHomePage();

            home.ClickRegister();

            RegisterPage register = new RegisterPage(driver);
            Thread.sleep(3000);
            register.VerifyNavigationRegister();

            String username1 = register.RegisterUser(username, password, true);

            LoginPage login = new LoginPage(driver);
            Thread.sleep(2000);
            login.PerformLogin(username1, password);

            Thread.sleep(2000);
            status = home.isUserLoggedin();
            assertion.assertTrue(status, "User is not logged in");
            home.logoutUser();
        } catch (Exception e) {
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
    }
}
