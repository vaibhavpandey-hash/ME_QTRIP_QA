🌍 QTrip Travel Website Automation

This project automates the complete end-to-end testing of QTrip, a travel booking web application.
It uses a scalable and modular automation framework built with Selenium WebDriver, TestNG, Data Providers, Extent Reports, and Driver Singleton Pattern to ensure stable, maintainable, and reusable test execution.

🚀 Key Features

✔ End-to-End UI Automation of QTrip

✔ Page Object Model (POM) architecture

✔ Driver Singleton Pattern for WebDriver management

✔ Data-Driven Testing using TestNG Data Providers

✔ Advanced Reporting using ExtentReports

✔ Reusable Utilities for waits, interactions, and validations

✔ Centralized Configurations (URLs, credentials, driver paths)

✔ Modular Test Design to support scalability

✔ Cross-browser Ready framework structure

🧰 Tech Stack

Java

Selenium WebDriver

TestNG

ExtentReports

Gradle

Page Object Model (POM)

📂 Project Structure
QTrip-Automation/
 ├── src/test/java/
 │      ├── testCases/            → All TestNG test classes
 │      ├── pages/                → Page Object Model classes
 │      ├── data/                 → Test data + Data Providers
 │      └── utils/                → Driver manager, waits, helpers
 │
 ├── src/main/java/
 │      └── core/                 → Driver Singleton & Base classes
 │
 ├── reports/                     → Extent HTML test reports
 ├── resources/                   → Config files, test data JSON
 └── build.gradle                 → Gradle configuration

▶️ Running the Test Suite
Build project
gradle clean build

Run all TestNG tests
gradle test

View Extent Report

Open:

/reports/ExtentReport.html

🎯 Test Coverage Highlights

User registration

User login/logout

City search & filtering

Adventure listing validation

Booking workflow (with date, guests, reservation)

End-to-end flow from explore → book → confirm

💡 Framework Concepts Implemented

Singleton WebDriver

POM with dedicated action methods

TestNG annotations (@BeforeMethod, @AfterMethod, @DataProvider)

Custom Waits & Browser Utils

HTML Reporting with ExtentReports

Structured Logging

Clean separation of test logic and UI element locators
