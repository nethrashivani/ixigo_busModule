package com.ixigo.testing.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BusSearchPage {

    // ✅ NO driver/wait fields — passed as parameters instead

    @FindBy(xpath = "//span[.='Price']")         private WebElement price;
    @FindBy(xpath = "//span[.='Seats']")         private WebElement seats;
    @FindBy(xpath = "//span[.='Ratings']")       private WebElement ratings;
    @FindBy(xpath = "//span[.='Arrival Time']")  private WebElement arrivaltime;
    @FindBy(xpath = "//span[.='Departure Time']") private WebElement depaturetime;
    @FindBy(xpath = "(//img[@width='auto' and @height='100'])[2]") private WebElement assuredfilter;
    @FindBy(xpath = "//span[contains(text(),'AC')]")      private WebElement acFilter;
    @FindBy(xpath = "//span[contains(text(),'Sleeper')]") private WebElement sleeperFilter;
    @FindBy(xpath = "//span[contains(text(),'10 AM - 5 PM')]") private WebElement arrdeptime;

    public void applyMandatoryFilters(WebDriver driver) {
        System.out.println("Applying mandatory filters: AC, Sleeper, Arrival/Departure");
        clickElement(driver, acFilter);
        clickElement(driver, sleeperFilter);
        clickElement(driver, arrdeptime);
    }

    public void applyOptionalFilter(WebDriver driver, String filterName) {
        System.out.println("Applying optional filter: " + filterName);
        switch (filterName.trim().toLowerCase()) {
            case "price":          clickElement(driver, price);        break;
            case "seats":          clickElement(driver, seats);        break;
            case "ratings":        clickElement(driver, ratings);      break;
            case "arrival time":   clickElement(driver, arrivaltime);  break;
            case "departure time": clickElement(driver, depaturetime); break;
            case "assured":        clickElement(driver, assuredfilter);break;
            default: System.out.println("Unknown filter skipped: " + filterName);
        }
    }

    private void clickElement(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.elementToBeClickable(element)));
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
            Thread.sleep(500);
            element.click();
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            System.out.println("Normal click failed — using JS click: " + e.getMessage());
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
        }
    }
}