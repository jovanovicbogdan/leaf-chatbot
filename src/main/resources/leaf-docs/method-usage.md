# Method Usage

## Overview
This document provides an overview of key methods used in the project, detailing their parameters, return types, and usage examples.

## Key Methods

### `areAnagrams`
- **Location**: `src/tests/e2e/sample.js`
- **Description**: Checks if two strings are anagrams.
- **Parameters**:
  - `str1`: First string to compare.
  - `str2`: Second string to compare.
- **Returns**: `boolean` - `true` if the strings are anagrams, `false` otherwise.
- **Example**:
  ```javascript
  console.log(areAnagrams('listen', 'silent')); // true
  console.log(areAnagrams('hello', 'world')); // false
  ```

### `twoSum`
- **Location**: `src/tests/e2e/sample.js`
- **Description**: Finds two numbers in an array that add up to a target.
- **Parameters**:
  - `nums`: Array of numbers.
  - `target`: Target sum.
- **Returns**: `Array<number>` - Indices of the two numbers that add up to the target.
- **Example**:
  ```javascript
  console.log(twoSum([2, 7, 11, 15], 9)); // [0, 1]
  ```

### `getBaseUrl`
- **Location**: `src/core/configuration.ts`
- **Description**: Retrieves the base URL from the environment.
- **Returns**: `string` - The base URL.
- **Example**:
  ```javascript
  const baseUrl = getBaseUrl();
  ```

### `fn_registerUser`
- **Location**: `src/pages/pageobject/leaf-demo-register.page.ts`
- **Description**: Registers a user with provided details.
- **Parameters**:
  - `usernameInput`: Username for registration.
  - `passwordInput`: Password for registration.
  - `repeatedPasswordInput`: Repeated password for confirmation.
  - `firstNameInput`, `lastNameInput`, `addressStreetInput`, `addressCityInput`, `addressStateInput`, `addressZipCodeInput`, `phoneNumberInput`, `ssnInput`: Optional user details.
- **Returns**: `Promise<void>`
- **Example**:
  ```javascript
  await page.fn_registerUser('user', 'pass', 'pass');
  ```

### `g_measurePerformance`
- **Location**: `src/pages/common/base.page.ts`
- **Description**: Measures performance metrics for a navigation action.
- **Parameters**:
  - `navigationAction`: Async function that performs the navigation.
  - `options`: Optional configuration for performance measurement.
- **Returns**: `Promise<PerformanceMetrics>`
- **Example**:
  ```javascript
  const metrics = await page.g_measurePerformance(async () => {
    await page.goto('https://example.com');
  });
  ``` 