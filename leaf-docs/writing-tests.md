# Writing Tests

## Overview
This document provides guidelines and examples for writing tests in the project, including handling errors and performance testing.

## General Guidelines
- Use descriptive test names to clearly indicate the purpose of the test.
- Group related tests using `describe` blocks for better organization.
- Use `beforeEach` and `afterEach` hooks for setup and teardown tasks.
- Prefer using async/await for asynchronous operations to improve readability.

## Examples

### Handling Errors
- **Location**: `src/tests/examples/ex-errorHandling.test.ts`
- **Description**: Example of handling timeout errors using Playwright's error classes.
- **Example**:
  ```javascript
  test('Handle timeout error', async ({ page }) => {
    try {
      await page.goto('https://example.com', { timeout: 1000 });
    } catch (error) {
      if (error instanceof errors.TimeoutError) {
        console.log('Navigation timed out.');
      } else {
        console.error('An unexpected error occurred:', error);
      }
    }
  });
  ```

### Performance Testing
- **Location**: `src/tests/examples/ex-performanceFCPnTTI.test.ts`
- **Description**: Example of measuring performance metrics for a page.
- **Example**:
  ```javascript
  test('Performance metrics for Demo About page', async ({ pomContainer }) => {
    const metrics = await pomContainer.leafDemoAboutPage.g_measurePerformance(
      async () => {
        await pomContainer.leafDemoAboutPage.g_navigateTo(NavigationOptionsEnum.Demo_About);
      },
    );
    expect(metrics.loadTime).toBeLessThan(5000);
    if (metrics.fcpTime) {
      expect(metrics.fcpTime).toBeLessThan(2000);
    }
  });
  ```

### Simple Registration Test
- **Location**: `src/tests/examples/ex-simpleRegistration.test.ts`
- **Description**: Example of a simple registration test using the POM pattern.
- **Example**:
  ```javascript
  test('Simple Register with only username and password', async ({ pomContainer }) => {
    await pomContainer.leafDemoHomePage.g_navigateTo(NavigationOptionsEnum.Demo_Home);
    await pomContainer.leafDemoHomePage.registerLink.click();
    await expect(pomContainer.leafDemoRegisterPage.registerPage).toBeVisible();
    const randomNumber = Math.random().toString().slice(2, 8);
    const usernameInput = 'username' + randomNumber;
    const passwordInput = 'password' + randomNumber;
    const repeatedPasswordInput = 'password' + randomNumber;
    await pomContainer.leafDemoRegisterPage.fn_registerUser(
      usernameInput,
      passwordInput,
      repeatedPasswordInput,
    );
    await expect(pomContainer.leafDemoRegisterPage.registerWelcomeText).toContainText('Welcome username' + randomNumber);
  });
  ``` 