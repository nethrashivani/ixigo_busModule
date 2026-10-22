# Ixigo Bus Module Automation Testing

A Selenium-based test automation framework for validating the bus booking flow on [Ixigo](https://www.ixigo.com/), using Java, Maven, Cucumber BDD, and TestNG.

## Overview

This project automates key user journeys in the Ixigo Bus module, from searching for buses to proceeding to the payment page. It uses Cucumber feature files to describe test scenarios in Gherkin syntax and Selenium WebDriver to interact with the browser.

## Tech Stack

- **Language:** Java 21
- **Automation:** Selenium WebDriver 4.21.0
- **BDD Framework:** Cucumber 7.20.1
- **Test Runner:** TestNG 7.10.2
- **Build Tool:** Apache Maven
- **Test Data:** Excel using Apache POI
- **Browser:** Chrome
- **Reporting:** Cucumber HTML report
- **Version Control:** Git and GitHub

## Features Tested

- Bus search between source and destination cities
- Travel date selection and search submission
- Mandatory bus filters, including AC, Sleeper, and Arrival/Departure
- Optional price filtering
- Bus and seat selection
- Boarding and dropping point selection
- Passenger details entry using Excel test data
- Policy and insurance section interaction
- Navigation to the payment page
- Screenshot capture for test evidence

## Project Structure

```text
IXIGO_BUSES_NEW/
├── src/
│   └── test/
│       ├── java/
│       │   └── com/ixigo/testing/
│       │       ├── pages/
│       │       ├── utilities/
│       │       ├── stepdefinition/
│       │       └── testrunner/
│       └── resources/
│           ├── featurefile/
│           │   └── BusModule.feature
│           └── Reader/
│               ├── personaldetails.xlsx
│               └── commonData.properties
├── .gitignore
├── pom.xml
└── README.md
```

## Prerequisites

Install the following before running the tests:

- JDK 21
- Apache Maven
- Google Chrome
- Git
- An IDE such as Visual Studio Code

Verify Java and Maven installation:

```bash
java -version
mvn -version
```

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/nethrashivani/ixigo_busModule.git
cd ixigo_busModule
```

### 2. Install dependencies and compile

```bash
mvn clean test-compile
```

Maven downloads the dependencies specified in `pom.xml`.

### 3. Configure test data

Passenger details are read from:

```text
src/test/resources/Reader/personaldetails.xlsx
```

Ensure the Excel workbook contains the expected sheet name, `Sheet1`, and the passenger name and age in the expected cells.

Use dummy test data and avoid committing personal or sensitive information.

## Running the Tests

Run all scenarios configured in the Cucumber runner:

```bash
mvn clean test
```

To run a specific tagged scenario, configure the desired tag in `RunnerIO.java`, for example:

```java
tags = "@passenger"
```

Remove the tag filter to execute all scenarios included by the runner configuration.

## Reports and Screenshots

After execution, Maven test results are available under:

```text
target/surefire-reports/
```

The Cucumber HTML report is configured at:

```text
target/cucumber-reports/Cucumber.html
```

If screenshot capture is enabled and working, screenshots can be stored under:

```text
target/screenshots/
```

These outputs are generated during test execution and are excluded from Git using `.gitignore`.

## Test Execution Notes

- Some scenarios may require manual OTP verification depending on Ixigo's authentication flow.
- Website UI changes may require updates to Selenium locators.
- Test duration depends on browser loading, explicit waits, network conditions, and any manual verification.
- A successful test confirms the assertions configured in the scenario; reaching the payment page does not mean a payment was completed.
- Run tests against an environment and test data you are authorized to use.

## Future Improvements

- Improve screenshot capture and failure evidence.
- Optimize explicit waits to reduce execution time.
- Add stronger assertions for search results, applied filters, and selected seats.
- Improve browser and driver lifecycle management.
- Generate richer test reports and integrate execution with CI/CD.

## Author

**Nethrashivani**

GitHub: [@nethrashivani](https://github.com/nethrashivani)

## Disclaimer

This project is intended for learning and test automation practice. Ixigo is a third-party service, and this project is not affiliated with or endorsed by Ixigo.
