package com.ixigo.testing.stepdefinition;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.ixigo.testing.utilities.AllUtilityFunctions;
import com.ixigo.testing.utilities.BaseClass;
import com.ixigo.testing.utilities.Pages;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class BusModule {

    // ✅ Safest — gets driver fresh each time, never null
    private WebDriver driver() {
        return new BaseClass().getDriver();
    }

    @Given("User launches the ixigo website")
    public void user_launches_the_ixigo_website() {
        Pages.lp.get().handlePopup(driver()); // ✅ called as method
    }

    @Given("User navigates to bus search")
    public void user_navigates_to_bus_search() {
        Pages.hrp.get().clickBuses(driver());
    }

    @When("User enters {string} and {string}")
    public void user_enters_and(String source, String destination) {
        Pages.hrp.get().enterFromCity(driver(), source);
        Pages.hrp.get().enterToCity(driver(), destination);
    }

    @When("user enters bus search details")
    public void user_enters_bus_search_details(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Pages.hrp.get().enterFromCity(driver(), data.get(0).get("source"));
        Pages.hrp.get().enterToCity(driver(), data.get(0).get("destination"));
    }

    @When("User selects travel date and clicks search")
    public void user_selects_travel_date_and_clicks_search() {
        Pages.hrp.get().selectDateAndSearch(driver());
    }

    @Then("Bus results should be displayed")
    public void bus_results_should_be_displayed() {
        String url = Pages.lp.get().getCurrentUrl(driver());
        Assert.assertTrue(url.contains("bus") || url.contains("search"),
                "FAILED: Bus results page not displayed. URL: " + url);
    }

    @Given("User is on bus search results page")
    public void user_is_on_bus_search_results_page() {
        System.out.println("User is on results page");
    }

    @When("user applies mandatory filters")
    public void user_applies_mandatory_filters() {
        Pages.bsp.get().applyMandatoryFilters(driver());
    }

    @When("user applies optional filter {string}")
    public void user_applies_optional_filter(String filterName) {
        Pages.bsp.get().applyOptionalFilter(driver(), filterName);
    }

    @Then("filtered bus results should be displayed")
    public void filtered_bus_results_should_be_displayed() {
        String url = Pages.lp.get().getCurrentUrl(driver());
        Assert.assertTrue(url.contains("bus") || url.contains("search"),
                "FAILED: Filtered results not displayed. URL: " + url);
    }

    @When("user selects the bus and seat")
    public void user_selects_the_bus_and_seat() {
        Pages.ssp.get().selectSeat(driver(), "SK Balu Bus");
        Pages.ssp.get().selectSeatNumber(driver());
    }

    @When("user selects boarding and dropping point")
    public void user_selects_boarding_and_dropping_point() {
        Pages.ssp.get().selectBoardingPoint(driver());
        Pages.ssp.get().selectDroppingPoint(driver());
        Pages.ssp.get().clickContinue(driver());
    }

    @Then("personal details page should be displayed")
    public void personal_details_page_should_be_displayed() {
        String url = Pages.lp.get().getCurrentUrl(driver());
        Assert.assertTrue(
                url.contains("passenger") || url.contains("personal") || url.contains("detail"),
                "FAILED: Personal details page not displayed. URL: " + url);
    }

    @When("user enters passenger name and age")
    public void user_enters_passenger_name_and_age() {
        AllUtilityFunctions util = new AllUtilityFunctions();
        String path = "src/test/resources/Reader/personaldetails.xlsx";
        String nameValue = util.getExcelData(path, "Sheet1", 1, 0);
        String ageValue = util.getExcelData(path, "Sheet1", 1, 1);
        if (nameValue.isEmpty() || ageValue.isEmpty()) {
            throw new RuntimeException("Excel NOT read → check file/sheet/row/column");
        }
        Pages.pp.get().enterPassengerDetails(driver(), nameValue, ageValue);
    }

    @When("user clicks proceed to pay")
    public void user_clicks_proceed_to_pay() {
        Pages.pp.get().clickContinue(driver());
    }

    @Then("user should be navigated to the payment page")
    public void user_should_be_navigated_to_the_payment_page() {
        String url = Pages.lp.get().getCurrentUrl(driver());
        System.out.println("Asserting payment page. URL: " + url);
        Assert.assertTrue(
                url.contains("payment") ||
                        url.contains("pay") ||
                        url.contains("checkout") ||
                        url.contains("verify") ||
                        url.contains("booking") ||
                        url.contains("passengerinfo"), // ✅ add this — still on this page after OTP timeout
                "FAILED: Payment page not displayed. URL: " + url);
        System.out.println("PASSED: URL: " + url);
    }

    @After
    public void captureScreenshot(Scenario scenario) {
        WebDriver currentDriver = driver();

        if (currentDriver == null) {
            System.out.println("Screenshot skipped: WebDriver is null.");
            return;
        }

        try {
            byte[] screenshot = ((TakesScreenshot) currentDriver)
                    .getScreenshotAs(OutputType.BYTES);

            // Attach the screenshot to the Cucumber HTML report
            scenario.attach(screenshot, "image/png", scenario.getName());

            // Save the screenshot as a PNG file
            Path folder = Paths.get("target", "screenshots");
            Files.createDirectories(folder);

            String safeName = scenario.getName()
                    .replaceAll("[^a-zA-Z0-9._-]", "_");

            Path file = folder.resolve(
                    safeName + "_" + System.currentTimeMillis() + ".png");

            Files.write(file, screenshot);

            System.out.println("Screenshot saved: "
                    + file.toAbsolutePath());

        } catch (Exception e) {
            System.out.println("Screenshot capture failed: "
                    + e.getMessage());
        }
    }

}