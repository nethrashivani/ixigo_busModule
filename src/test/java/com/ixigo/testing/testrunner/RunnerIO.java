
package com.ixigo.testing.testrunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;


@CucumberOptions(
    features = "src/test/resources/featurefile/BusModule.feature",
    glue = "com.ixigo.testing.stepdefinition",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/Cucumber.html"
    },
    monochrome = true
)

public class RunnerIO extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
