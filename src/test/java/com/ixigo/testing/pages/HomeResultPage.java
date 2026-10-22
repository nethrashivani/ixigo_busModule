package com.ixigo.testing.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomeResultPage {

    // ✅ NO driver/wait fields — passed as parameters instead

    @FindBy(xpath = "(//p[contains(text(),'Buses')])[2]")
    private WebElement buses;

    @FindBy(css = "[placeholder='Leaving From']")
    private WebElement fromCity;

    @FindBy(css = "[placeholder='Going To']")
    private WebElement toCity;

    @FindBy(xpath = "//a[.='Tomorrow']")
    private WebElement tomorrow;

    @FindBy(xpath = "//span[.='Search']")
    private WebElement searchBtn;

    public void clickBuses(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.switchTo().defaultContent();

        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));

        WebElement bus = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//p[contains(text(),'Buses')])[2]")));

        wait.until(ExpectedConditions.visibilityOf(bus));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", bus);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(bus));
            bus.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", bus);
        }

        try {
            Thread.sleep(3000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                "var selectors = ['iframe[src*=\"sso-login\"]','.login-modal'," +
                "'.modal-backdrop','.abrs-backdrop','[class*=\"Overlay\"]'," +
                "'[class*=\"backdrop\"]','[role=\"dialog\"]'];" +
                "selectors.forEach(function(sel){" +
                "  document.querySelectorAll(sel).forEach(function(el){" +
                "    if(el && el.parentNode){ el.parentNode.removeChild(el); }" +
                "  });" +
                "});" +
                "document.body.style.overflow='auto';" +
                "document.body.style.pointerEvents='auto';"
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterFromCity(WebDriver driver, String city) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement from = wait.until(ExpectedConditions.elementToBeClickable(fromCity));
        from.click();
        from.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        from.sendKeys(city);
        try { Thread.sleep(1500); } catch (Exception e) {}
        from.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        wait.until(d -> from.getAttribute("value") != null &&
                from.getAttribute("value").toLowerCase().contains(city.toLowerCase()));
    }

    public void enterToCity(WebDriver driver, String city) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement to = wait.until(ExpectedConditions.elementToBeClickable(toCity));
        to.click();
        to.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        to.sendKeys(city);
        try { Thread.sleep(1500); } catch (Exception e) {}
        to.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        wait.until(d -> to.getAttribute("value") != null &&
                to.getAttribute("value").toLowerCase().contains(city.toLowerCase()));
    }

    public void selectDateAndSearch(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.elementToBeClickable(tomorrow)).click();
        wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
    }
}