package com.ixigo.testing.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
   
	
	//create webelement locator for train module
	@FindBy(xpath="//ul/child::li/child::a[@href='/trains']/child::p[.='Trains' and @class='body-sm text-xl text-secondary']")
	private WebElement trains;
	
	@FindBy(css="[id='closeButton']")
	private WebElement popup;
	
	@FindBy(xpath="//button[.='Log in/Sign up']/..//div")
	private WebElement login;
	
	@FindBy(xpath="//label[.='Enter Mobile Number']")
	private WebElement phonenumber;
	
	@FindBy(xpath="//button[.='Continue']")
	private WebElement continuebutton;
	
	@FindBy(xpath="//button[.='Verify']")
	private WebElement verify;

	
	
	//getter method to access private varaible
	public WebElement getTrains() {
		return trains;
	}

	public WebElement getLogin() {
		return login;
	}

	public WebElement getPhonenumber() {
		return phonenumber;
	}

	public WebElement getContinuebutton() {
		return continuebutton;
	}

	public WebElement getVerify() {
		return verify;
	}
	
	public WebElement getpopup() {
		return popup;
	}
	
	//bussiness logic for home page
	public void clicklogin() {
		getLogin().click();
	}
	public void clickTrains(WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(150));
	    
	    // Retry up to 3 times — popup may reappear or load late
	    for (int attempt = 0; attempt < 3; attempt++) {
	        removeAllPopupinhomepage(driver);
	        try {
	            WebElement trainsBtn = wait.until(
	                ExpectedConditions.elementToBeClickable(getTrains())
	            );
	            trainsBtn.click();
	            return; // Success — exit
	        } catch (ElementClickInterceptedException e) {
	            // Popup intercepted — loop again to remove it
	            System.out.println("Popup intercepted click, retrying... attempt " + (attempt + 1));
	        } catch (Exception e) {
	            System.out.println("clickTrains failed: " + e.getMessage());
	        }
	    }
	    
	    // Final fallback: JS click bypasses any overlay entirely
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].click();", getTrains());
	}
	public void clickPhoneno() {
		getPhonenumber().click();
	}
	public void clickcontinue() {
		getContinuebutton().click();
	}
	public void clickpopup() {
		getpopup().click();
	}
	//popup handling
	// In HomePage.java

	private void removeAllPopupinhomepage(WebDriver driver) {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    
	    // Strategy 1: Remove the exact parent wrapper elements that CleverTap uses
	    try {
	        js.executeScript(
	            "var overlay = document.getElementById('intentOpacityDiv');" +
	            "if(overlay) overlay.remove();" +
	            "var wrapper = document.getElementById('intentPreview');" +
	            "if(wrapper) wrapper.remove();" +
	            "var iframe = document.getElementById('wiz-iframe-intent');" +
	            "if(iframe) iframe.remove();"
	        );
	        Thread.sleep(500); // Give DOM time to update
	    } catch (Exception ignored) {}

	    // Strategy 2: If iframe still exists, switch into it and click the close button
	    try {
	        WebElement iframe = driver.findElement(By.id("wiz-iframe-intent"));
	        driver.switchTo().frame(iframe);
	        WebElement closeBtn = new WebDriverWait(driver, Duration.ofSeconds(150))
	            .until(ExpectedConditions.elementToBeClickable(By.id("closeButton")));
	        closeBtn.click();
	        driver.switchTo().defaultContent();
	        Thread.sleep(500);
	    } catch (Exception ignored) {
	        driver.switchTo().defaultContent();
	    }
	}
	 

	    @FindBy(xpath = "//input[@placeholder='Enter Mobile Number']")
	    private WebElement mobileno;

	    @FindBy(xpath = "//button[.='Continue']")
	    private WebElement cont;

	    public void handlePopup(WebDriver driver) {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        try {
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("wiz-iframe-intent"));
	            WebElement closeBtn = wait.until(
	                ExpectedConditions.elementToBeClickable(By.id("closeButton"))
	            );
	            closeBtn.click();
	            driver.switchTo().defaultContent();
	            System.out.println("Popup closed successfully (iframe)");
	        } catch (Exception e) {
	            try {
	                driver.switchTo().defaultContent();
	                wait.until(ExpectedConditions.elementToBeClickable(popup));
	                popup.click();
	                System.out.println("Popup closed successfully (normal)");
	            } catch (Exception ex) {
	                System.out.println("Popup not present");
	            }
	        }
	    }

	    public void clickLogin(WebDriver driver) {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        wait.until(ExpectedConditions.elementToBeClickable(login)).click();
	        wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//input[@placeholder='Enter Mobile Number']")
	        ));
	        System.out.println("Login modal opened");
	    }

	   
	    public void loginAndContinue(WebDriver driver, String mobileNumber) {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        try {
	            wait.until(ExpectedConditions.visibilityOf(mobileno));
	            mobileno.clear();
	            mobileno.sendKeys(mobileNumber);
	            System.out.println("Mobile number entered: " + mobileNumber);

	            wait.until(ExpectedConditions.elementToBeClickable(cont)).click();
	            System.out.println("Continue clicked. Waiting for OTP screen...");

	            wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//input[@type='tel' or contains(@class,'otp') or @maxlength='1']")
	            ));
	            System.out.println("OTP screen detected. Enter OTP manually...");

	            Thread.sleep(30000);
	            System.out.println("OTP wait complete");

	            try {
	                wait.until(ExpectedConditions.invisibilityOfElementLocated(
	                    By.xpath("//input[@placeholder='Enter Mobile Number']")
	                ));
	                System.out.println("Login successful - modal closed");
	            } catch (Exception e) {
	                try {
	                    WebElement skipBtn = driver.findElement(
	                        By.xpath("//button[contains(text(),'Later') or contains(text(),'Skip')]")
	                    );
	                    skipBtn.click();
	                    System.out.println("Clicked Skip/Later button");
	                } catch (Exception ex) {
	                    System.out.println("No skip button - continuing safely");
	                }
	            }
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        } catch (Exception e) {
	            System.out.println("loginAndContinue error: " + e.getMessage());
	        }
	    }

	    public boolean isUserLoggedIn(WebDriver driver) {
	        String currentUrl = driver.getCurrentUrl();
	        return currentUrl.contains("app") ||
	               currentUrl.contains("home") ||
	               currentUrl.contains("dashboard");
	    }

	    public String getCurrentUrl(WebDriver driver) {
	        return driver.getCurrentUrl();
	    }
}
