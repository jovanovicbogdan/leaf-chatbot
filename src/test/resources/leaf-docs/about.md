# LEAF - Lean Enterprise Automation Framework

LEAF (Lean Enterprise Automation Framework) is a scalable, enterprise-grade testing framework powered by Playwright and Node.js. It offers pre-configured solutions to streamline common testing challenges, featuring built-in authentication, human-like interaction patterns, custom fixtures, and a rich set of helper functions. Designed for efficiency, LEAF allows teams to concentrate on crafting meaningful tests while relying on a robust and maintainable test infrastructure.

Notes:
- Lam to add link to design doc (System Design - Playwright Automation Framework (ATF))
- Vewing this README in VSC: Ctrl+Shift+V

## How to run tests

1. Clone the repository and run `npm install` in project root
2. Download and install browser binaries `npx playwright install --with-deps`
3. Optionally: Install `msedge` browser `npx playwright install msedge`

- By default the projects are configured to run in Desktop, Tablet, and Mobile viewports for all major browsers (Google Chrome, Firefox, Safari, and Microsoft Edge)
- For example, by executing: `npx playwright test login.test.ts` test suite will be executed with one worker process in headless mode
- Currently, there are 12 configured projects meaning if we have 2 tests in test suite there will be 24 tests in total
- See details of projects configuration in `playwright.config.ts`
- Execute tests on CI by using the following command `npx cross-env ENV=staging npx playwright test login.test.ts --project="Google Chrome Desktop"`
- All test scripts must have the proper tag annotation in order for us to easily grep and run specific tests within a suite. Refer to `ex-0-templateStandard.test.ts`
- Refer to our System Design documentation for in-depth knowledge of our framework

## How to write tests

1. Create a new test file in `src/tests/e2e` directory
2. Define a new page object model in `src/pages/pageobject` directory. The class should extend `BasePage` from `src/pages/base.page.ts`
3. In `src/pages/common/pom-container.ts` add a new page object model so it can be "pre-loaded" and available in each test block
4. [Core ATS]: It's important to import correct `test` or pom-container from `src/core/fixtures.ts` when updating configuration and not the one from `@playwright/test`. Otherwise, custom fixtures won't be available.

## Useful Playwright CLI Commands

- Run tests in headed mode `npx playwright test login.test.ts --headed`
- Run specific project `npx playwright test login.test.ts --project="Google Chrome Desktop"`
- Run tests in parallel `npx playwright test login.test.ts --workers=10`. This will launch 10 parallel tests. Depends on how many CPUs are available on a hardware where tests are being executed.
- Run a test with specific title `npx playwright test login.test.ts -g "Login" --project="Google Chrome Desktop"`. It will run only tests that have "Login" in their name.

## Visual Studio Code Extensions
Recommended extension to install within VSCode
1. Playwright Test for VSCode
2. Eslint
- This will help promote code quality and consistency by identifying and reporting on patterns found in ECMAScript/JavaScript code
- You will noticed warning regarding syntax. Please try to fix this prior to submitting your code.
- Manually check: `npx eslint .`
- To quickly fix JavaScript lint/indent warning, highligh all code and hit `alt + shift + f`
3. Themes:
- Colors: beautiful dracula darker: no highligt
- Icons: material icon theme

## Framework Structure

- `src/config` - a place for configuration files such as feature flags, database config
- `src/core` - core framework implementation logic, fixtures, utility methods, feature flags management
- `src/env` - store environment-specific configuration files for the automation framework
- `src/data/csv` - CSV hard-coded test data
- `src/data/json` - JSON hard-coded test data
- `src/data/models` - models that represents test data structures provided by csv, json or database data source
- `src/data/providers` - implementation of various data providers such as csv, json and database that can be loaded in test with `provider.getTestData()`
- `src/pages/common` - reusable and shared pages and their properties
- `src/pages/pageobject` - page object models of a web application under test
- `src/tests/api` - API test directory
- `src/tests/e2e` - UI test directory

## Features

Below is the list of features implemented within the framework.

### Custom Fixtures

Page object models are implemented as part of test fixtures and they are "preloaded" and available to use in any test without need to instantiate them inside of the test itself.

### Multi Environment Support

The env folder is used to store environment-specific configuration files for the Playwright automation framework. These files, typically named .env.<environment>, contain key-value pairs that define environment-specific variables such as BASE_URL, BASE_API_URL, and other settings required for the tests to run in different environments (e.g., staging, production, development).

### Page Object Container

The pom-container.ts file serves as a centralized container for managing and providing access to all Page Object Model (POM) classes in the Playwright automation framework. It acts as a single entry point for accessing different page objects, ensuring better organization, scalability, and maintainability of the test code.

### Feature flags

Feature flags system lets you turn on or off certain features in framework like checking if TestRail case ID is present in test title. You can toggle features on or off in `src/config/feature-flags.yml` file or by specifying environment variable matching the variable name as defined in `feature-flags.yml` file. For example, `TESTRAIL_CHECK_CASE_ID` is the name of the environment variable that can be set to override the value set in `feature-flags.yml`:

```yaml
TESTRAIL:
  CHECK_CASE_ID: on
```

### Upload Test Results to TestRail

```sh
trcli -y -h "https://$TESTRAIL_INSTANCE_NAME.testrail.io/" -u "$TESTRAIL_USERNAME" -p "$TESTRAIL_PASSWORD" --project "$PROJECT_NAME" parse_junit --case-matcher "name" -f "./test-results/junit-report.xml" --title "$TESTRAIL_TEST_RUN_TITLE"
``
