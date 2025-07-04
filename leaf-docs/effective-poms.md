# Effective Page Object Models (POMs)

## Overview
This document provides guidelines and best practices for creating effective Page Object Models (POMs) in the project, along with examples.

## What is a Page Object Model?
A Page Object Model is a design pattern used in test automation to create an abstraction of a web page. It helps in reducing code duplication and improving test maintenance by encapsulating the page elements and actions in a separate class.

## Best Practices
- **Single Responsibility**: Each page object should represent a single page or a significant part of a page.
- **Encapsulation**: Keep the page elements and actions encapsulated within the page object.
- **Descriptive Method Names**: Use clear and descriptive names for methods to indicate the action being performed.
- **Avoid Assertions**: Page objects should not contain assertions. Keep them in the test cases.
- **Reusability**: Design page objects to be reusable across different tests.

## Examples

### BasePage
- **Location**: `src/pages/common/base.page.ts`
- **Description**: Abstract class providing common methods for all page objects.
- **Example Methods**:
  - `g_navigateToUrlPage`: Navigates to a specific URL.
  - `g_measurePerformance`: Measures performance metrics for a navigation action.

### LeafDemoRegisterPage
- **Location**: `src/pages/pageobject/leaf-demo-register.page.ts`
- **Description**: Represents the registration page with methods to register a user.
- **Example Methods**:
  - `fn_registerUser`: Registers a user with provided details.
  - `fn_registerUserSimple`: Registers a user with default details.

### PomContainer
- **Location**: `src/pages/common/pom-container.ts`
- **Description**: Container for all page objects, providing a centralized access point.
- **Example**:
  ```javascript
  const pomContainer = new PomContainer(page);
  await pomContainer.leafDemoHomePage.fn_goToRegister();
  ``` 