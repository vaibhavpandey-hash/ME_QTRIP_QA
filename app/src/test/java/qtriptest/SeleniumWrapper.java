package qtriptest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.JavascriptExecutor;

public class SeleniumWrapper {

    public boolean click(RemoteWebDriver driver, WebElement element) {
        try {
            // Check if element exists
            if (element.isDisplayed()) {
                // Scroll into view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                // Click on the element
                element.click();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error clicking on the element: " + e.getMessage());
        }
        return false;
    }

    public boolean sendKeys(WebElement element, String text) {
        try {
            // Clear existing text
            element.clear();
            // Type new text
            element.sendKeys(text);
        } catch (Exception e) {
            System.out.println("Error sending keys to the element: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean navigateToURL(RemoteWebDriver driver, String url) {
        try {
            String currentUrl = driver.getCurrentUrl();
            // Navigate to the new URL if different from the current URL
            if (!currentUrl.equals(url)) {
                driver.navigate().to(url);
            }
        } catch (Exception e) {
            System.out.println("Error navigating to the URL: " + e.getMessage());
            return false;
        }
        return true;
    }

    public WebElement findElementWithRetry(RemoteWebDriver driver, By locator, int retries) {
        WebElement element = null;
        int attempts = 0;
        while (attempts < retries) {
            try {
                element = driver.findElement(locator);
                if (element != null) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Attempt " + (attempts + 1) + ": Unable to find element. Retrying...");
            }
            attempts++;
        }
        if (element == null) {
            System.out.println("Unable to find element after " + retries + " attempts.");
        }
        return element;
    }
}
