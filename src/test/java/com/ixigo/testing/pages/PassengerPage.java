package com.ixigo.testing.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PassengerPage {

    // ✅ NO driver/wait fields — passed as parameters

    @FindBy(xpath = "//input[@placeholder='Name']") private WebElement name;
    @FindBy(xpath = "//input[@placeholder='Age']")  private WebElement age;
    @FindBy(xpath = "//button[.='Female']")         private WebElement gender;

    public void enterPassengerDetails(WebDriver driver, String nameValue, String ageValue) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.switchTo().defaultContent();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.className("abrs-backdrop")));
        } catch (Exception e) {
            js.executeScript(
                "let el=document.querySelector('.abrs-backdrop'); if(el) el.remove();");
        }

        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@placeholder='Name']")));
        nameField.click();
        nameField.clear();
        nameField.sendKeys(nameValue);
        System.out.println("Name entered: " + nameValue);

        String cleanedAge = ageValue.contains(".") ? ageValue.split("\\.")[0] : ageValue;
        WebElement ageField = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@placeholder='Age']")));
        ageField.click();
        ageField.clear();
        ageField.sendKeys(cleanedAge);
        System.out.println("Age entered: " + cleanedAge);

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[.='Female']"))).click();
        System.out.println("Gender selected: Female");
    }

    public void clickContinue(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // STEP 1: Scroll to bottom to reveal policy section
        try {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1500);
        } catch (Exception ignored) {}

        // STEP 2: Click policy div (Attempt 1)
        try {
            WebElement pol = driver.findElement(
                By.xpath("(//div[contains(@class,'row card-body  light  ')])[3]"));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", pol);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", pol);
            System.out.println("Policy div clicked");
            Thread.sleep(800);
        } catch (Exception e) {
            System.out.println("Policy div not found: " + e.getMessage());
        }

        // STEP 3: Click first checkbox (Assured)
        try {
            WebElement assured = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//input[@type='checkbox'])[1]")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", assured);
            js.executeScript("arguments[0].click();", assured);
            System.out.println("Assured checkbox clicked");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Assured checkbox not found: " + e.getMessage());
        }

        // STEP 4: Click second checkbox (Insurance)
        try {
            WebElement insurance = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//input[@type='checkbox'])[2]")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", insurance);
            js.executeScript("arguments[0].click();", insurance);
            System.out.println("Insurance checkbox clicked");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Insurance checkbox not found: " + e.getMessage());
        }

        // STEP 5: Click label texts as fallback
        String[] labelTexts = {
            "//label[contains(.,'agree')]",
            "//label[contains(.,'Terms')]",
            "//label[contains(.,'Policy')]",
            "//label[contains(.,'Insurance')]",
            "//span[contains(.,'agree')]",
            "//span[contains(.,'Terms')]"
        };
        for (String lx : labelTexts) {
            try {
                WebElement label = driver.findElement(By.xpath(lx));
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", label);
                js.executeScript("arguments[0].click();", label);
                System.out.println("Label clicked: " + lx);
                Thread.sleep(300);
            } catch (Exception ignored) {}
        }

        // STEP 6: Wait for Continue to Pay button to become ACTIVE
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                    "//a[contains(@class,'filled') and contains(@class,'primary')" +
                    " and not(contains(@class,'inactive'))]")));
            System.out.println("Continue to Pay button is ACTIVE");
        } catch (Exception e) {
            System.out.println("WARNING: Button may still be inactive — trying anyway");
        }

        // STEP 7: Find and click Continue to Pay button
        String[] xpaths = {
            "//a[contains(.,'Continue to Pay')]",
            "//button[contains(.,'Continue to Pay')]",
            "//a[contains(@class,'filled') and contains(@class,'primary')" +
                " and not(contains(@class,'inactive'))]",
            "//*[contains(.,'Continue to Pay')]"
        };

        WebElement continueBtn = null;
        for (String xpath : xpaths) {
            try {
                continueBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
                System.out.println("Continue button found: " + xpath);
                break;
            } catch (Exception ignored) {}
        }

        if (continueBtn == null)
            throw new RuntimeException("Continue to Pay button NOT found on page");

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", continueBtn);
        try { Thread.sleep(1000); } catch (Exception ignored) {}
        js.executeScript("arguments[0].click();", continueBtn);
        System.out.println("Continue to Pay clicked.");

        // STEP 8: Wait for page to navigate away from passengerinfo
        try {
            new WebDriverWait(driver, Duration.ofSeconds(60))
                .until(ExpectedConditions.not(
                    ExpectedConditions.urlContains("passengerinfo")));
            System.out.println("Payment page reached. URL: " + driver.getCurrentUrl());
        } catch (Exception e) {
            System.out.println("Still on passengerinfo — OTP may be needed. URL: "
                + driver.getCurrentUrl());
        }
    }

    public boolean isPaymentPageDisplayed(WebDriver driver) {
        String url = driver.getCurrentUrl().toLowerCase();
        return url.contains("payment") || url.contains("pay")
            || url.contains("checkout") || url.contains("verify");
    }
}