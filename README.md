# Formula Parser and Calculator

## Overview
This code implements a mathematical formula parser and calculator designed for spreadsheet cell operations. It handles basic arithmetic operations, parentheses, cell references, and error detection.

## Key Components

### Formula Validation
- `isNumber(String formula)`: Checks if string is a valid number
- `isText(String formula)`: Identifies text content that isn't a number or formula
- `isForm(String text)`: Validates formula syntax

### Formula Calculator
- `computeForm(String text)`: Evaluates mathematical expressions

## Formula Rules

### Supported Features
- Basic arithmetic operators (+, -, *, /)
- Parentheses for grouping
- Cell references (A1, B2, etc.)
- Decimal numbers
- Negative numbers

### Formula Validation Rules
- Must start with "=" sign
- Proper parentheses balancing
- No consecutive operators
- No operators at start/end (except -)
- Only valid characters allowed (numbers, operators, decimals, parentheses, letters A-Z)

### Calculation Process
1. Removes outer parentheses if balanced
2. Finds operator with lowest precedence
3. Splits formula at operator
4. Recursively calculates left and right parts
5. Applies operator to results

## Helper Methods
- `isOperator(char c)`: Identifies arithmetic operators
- `isValidChar(char c)`: Validates character usage in formulas
- `containsOperator(String text)`: Checks for presence of operators
- `indexOperatorMin(String text)`: Finds lowest precedence operator
- `calculate(double left, double right, char operator)`: Performs arithmetic operations

## Examples
```java
isForm("=1+2")      // true
isForm("1+2")       // false (no = prefix)
isForm("=A1+B2")    // true
isForm("=1++2")     // false (consecutive operators)

# Spreadsheet Class Documentation

This document provides an overview of the **Spreadsheet** class and its functionality, including the methods and their purposes.

## Overview
The Spreadsheet class represents a simple implementation of a spreadsheet system, allowing users to:
- Retrieve cell values.
- Compute cell values based on dependencies.
- Compute dependency depth for cells.
- Save and load the spreadsheet to and from files.

## Methods

### 1. `public String value(int x, int y)`
- **Purpose**: Returns the raw string value of the cell located at coordinates `(x, y)`.
- **Parameters**:
  - `x`: The x-coordinate (row) of the cell.
  - `y`: The y-coordinate (column) of the cell.
- **Returns**: The string value stored in the cell at `(x, y)`.

### 2. `public String eval(int x, int y)`
- **Purpose**: Computes and returns the evaluated value of the cell at coordinates `(x, y)`.
- **Parameters**:
  - `x`: The x-coordinate of the cell.
  - `y`: The y-coordinate of the cell.
- **Returns**: The computed/evaluated value of the cell.

### 3. `public void eval()`
- **Purpose**: Computes and evaluates all the cells in the spreadsheet.
- **Behavior**: Updates all cell values by resolving dependencies and performing calculations as necessary.

### 4. `public int[][] depth()`
- **Purpose**: Computes a 2D array where each entry represents the dependency depth of a corresponding cell.
- **Returns**: A 2D array where:
  - Depth is `0` if the cell has no dependencies.
  - Depth is `1 + max(depth(cell_1), depth(cell_2), ...)` if the cell depends on other cells.
  - Depth is `-1` if there is a circular dependency (e.g., `c1` depends on `c2` and `c2` depends on `c1`).

### 5. `public void load(String fileName)`
- **Purpose**: Loads the content of a saved spreadsheet from a file.
- **Parameters**:
  - `fileName`: A string representing the file path (absolute or relative) to load the spreadsheet from.
- **Behavior**: Clears all existing cells and populates the spreadsheet with data from the file. Skips malformed or invalid lines.
- **Exceptions**: Throws `IOException` if the file cannot be read.

### 6. `public void save(String fileName)`
- **Purpose**: Saves the current state of the spreadsheet to a file.
- **Parameters**:
  - `fileName`: A string representing the file path to save the spreadsheet to.
- **Behavior**: Writes all non-empty cells to the file in a serialized format.

## Notes
- **Dependency Handling**: Circular dependencies are detected, and cells with such dependencies are assigned a depth of `-1`.
- **File Format**:
  - The first line in the file is ignored (header line).
  - Each valid line follows the format: `<x>,<y>,<value>,<remarks>`.
  - Lines not matching this format are skipped during the `load` operation.
- **Evaluation**: Cells with formulas (e.g., `=2+a0`) are dynamically computed using the `eval` methods.

## Example Usage
```java
SpreadSheet sheet = new SpreadSheet();

// Load a spreadsheet from a file
sheet.load("spreadsheet.csv");

// Retrieve raw value
String rawValue = sheet.value(0, 1);
System.out.println("Raw Value: " + rawValue);

// Evaluate a specific cell
String evaluatedValue = sheet.eval(0, 1);
System.out.println("Evaluated Value: " + evaluatedValue);

// Save the spreadsheet to a file
sheet.save("output.csv");
