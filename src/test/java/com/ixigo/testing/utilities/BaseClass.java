
package com.ixigo.testing.utilities;

import org.openqa.selenium.WebDriver;

public class BaseClass {

    private static final ThreadLocal<WebDriver> DRIVER =
            new ThreadLocal<>();

    public void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void unload() {
        DRIVER.remove();
    }
}
