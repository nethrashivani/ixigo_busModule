
package com.ixigo.testing.stepdefinition;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.ixigo.testing.utilities.BaseClass;
import com.ixigo.testing.utilities.Pages;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hook {

    private final BaseClass baseClass = new BaseClass();

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        baseClass.setDriver(driver);
        Pages.allPages(driver);

        try {
            driver.get("https://www.ixigo.com");
        } catch (Exception e) {
            driver.quit();
            BaseClass.unload();
            Pages.clearPages();
            throw new IllegalStateException(
                    "Could not open the Ixigo website.", e);
        }
    }

    @After
    public void tearDown() {
        WebDriver driver = baseClass.getDriver();

        try {
            if (driver != null) {
                driver.quit();
            }
        } finally {
            BaseClass.unload();
            Pages.clearPages();
        }
    }
}
