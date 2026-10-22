
package com.ixigo.testing.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeatSelectionPage {

    private static final int WAIT_SECONDS = 20;

    private final By selectSeatsButton =
            By.xpath("(//button[contains(.,'Select Seats')])[3]");

    private final By availableSeats =
            By.cssSelector("div.seatsContainer button.seat-button");

    private final By boardingPoints =
            By.cssSelector("#place-container > div[id^='place-']");

    private final By droppingTab =
            By.xpath("//a[contains(.,'Dropping Points')]");

    private final By proceedButton =
            By.xpath("//button[normalize-space()='Proceed']");

    // Select the bus and open its seat layout.
    public void selectSeat(WebDriver driver, String busName) {
        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            WebElement button = wait.until(
                    ExpectedConditions.elementToBeClickable(selectSeatsButton));

            jsClick(driver, button);

            System.out.println(
                    "Select Seats clicked for requested bus: " + busName);

        } catch (TimeoutException e) {
            throw new RuntimeException(
                    "Could not find a Select Seats button for: " + busName, e);
        }
    }

    // Select the first visible, enabled, available seat.
    // Booked seats do not have the seat-button class.
    public void selectSeatNumber(WebDriver driver) {
        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECONDS));

        try {
            WebElement seat = wait.until(d -> {
                List<WebElement> seats = d.findElements(availableSeats);

                for (WebElement candidate : seats) {
                    try {
                        if (candidate.isDisplayed()
                                && candidate.isEnabled()) {
                            return candidate;
                        }
                    } catch (StaleElementReferenceException ignored) {
                        // Try the next element if the page refreshes.
                    }
                }

                return null;
            });

            jsScrollToCenter(driver, seat);
            jsClick(driver, seat);

            System.out.println("Available seat selected.");

        } catch (TimeoutException e) {
            throw new RuntimeException(
                    "No available seat appeared within "
                            + WAIT_SECONDS + " seconds.", e);
        }
    }

    // Select the first visible boarding point.
    public void selectBoardingPoint(WebDriver driver) {
        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECONDS));

        try {
            WebElement point = wait.until(d ->
                    findFirstVisible(d, boardingPoints));

            jsScrollToCenter(driver, point);
            jsClick(driver, point);

            // Do not call point.getText() here; the DOM may refresh.
            System.out.println("First available boarding point clicked.");

        } catch (TimeoutException e) {
            throw new RuntimeException(
                    "Boarding points did not load or none were visible.", e);
        }
    }

    // Open the dropping tab and select its first visible point.
    public void selectDroppingPoint(WebDriver driver) {
        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECONDS));

        try {
            WebElement tab = wait.until(
                    ExpectedConditions.elementToBeClickable(droppingTab));

            jsClick(driver, tab);

            // Wait for the list to render, then locate its first visible point.
            WebElement point = wait.until(d ->
                    findFirstVisible(d, boardingPoints));

            jsScrollToCenter(driver, point);
            jsClick(driver, point);

            // Do not read text from the element after clicking.
            System.out.println("First available dropping point clicked.");

        } catch (TimeoutException e) {
            throw new RuntimeException(
                    "Dropping points did not load or none were visible.", e);
        }
    }

    // Find the first currently visible element using a fresh DOM lookup.
    private WebElement findFirstVisible(
            WebDriver driver, By locator) {

        List<WebElement> elements = driver.findElements(locator);

        for (WebElement element : elements) {
            try {
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (StaleElementReferenceException ignored) {
                // The DOM changed; the next wait poll will try again.
            }
        }

        return null;
    }

    // Scroll an element into view.
    private void jsScrollToCenter(
            WebDriver driver, WebElement element) {

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                element);
    }

    // Click normally, with a JavaScript fallback.
    private void jsClick(
            WebDriver driver, WebElement element) {

        try {
            jsScrollToCenter(driver, element);
            element.click();

        } catch (StaleElementReferenceException e) {
            throw e;

        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", element);
        }
    }

    // Click Proceed after selecting the seat and both points.
    public void clickContinue(WebDriver driver) {
        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECONDS));

        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(proceedButton));

        jsScrollToCenter(driver, button);
        jsClick(driver, button);

        waitForManualOtpAndProceed(driver);
    }

    // Wait for manual OTP handling if the website requires it.
    public void waitForManualOtpAndProceed(WebDriver driver) {
        String sessionId = getSessionId(driver);

        driver.switchTo().defaultContent();

        try {
            ((JavascriptExecutor) driver).executeScript(
                    "window.scrollTo(0, 0);");

            ((JavascriptExecutor) driver).executeScript(
                    "var b=document.createElement('div');"
                    + "b.id='otp-banner';"
                    + "b.style.cssText='position:fixed;top:0;left:0;"
                    + "width:100%;background:orange;color:white;"
                    + "font-size:20px;font-weight:bold;text-align:center;"
                    + "padding:12px;z-index:999999;';"
                    + "b.textContent='ENTER OTP IF REQUESTED - SESSION: "
                    + sessionId + "';"
                    + "document.body.appendChild(b);");

        } catch (Exception ignored) {
            // The banner is optional.
        }

        try {
            new WebDriverWait(driver, Duration.ofSeconds(100))
                    .until(d ->
                            !d.getCurrentUrl().contains("passengerinfo"));

        } catch (TimeoutException e) {
            System.out.println(
                    "[" + sessionId + "] Timed out waiting for navigation.");
        }

        try {
            ((JavascriptExecutor) driver).executeScript(
                    "var b=document.getElementById('otp-banner');"
                    + "if(b) b.remove();");

        } catch (Exception ignored) {
            // The banner may already be gone.
        }
    }

    private String getSessionId(WebDriver driver) {
        try {
            return ((RemoteWebDriver) driver)
                    .getSessionId().toString().substring(0, 8);

        } catch (Exception e) {
            return "unknown";
        }
    }
}
