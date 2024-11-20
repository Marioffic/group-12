# Farm Dashboard Project

## Overview
This project implements a farm dashboard using the Singleton design pattern to ensure only one instance of the dashboard can be created at a time. This pattern is foundational to the dashboard’s structure, allowing it to serve as a centralized interface for managing farm-related data.

### Role 1: Singleton Pattern
As part of Role 1, the Singleton pattern is implemented in the `Dashboard` class. The Singleton pattern ensures that only a single instance of the dashboard can exist, which is essential for centralizing farm management operations in a single interface.

## Implementation Details
- **Singleton Pattern**: The `Dashboard` class includes:
    - **Private Static Instance**: A private static variable holds the single instance of `Dashboard`.
    - **Private Constructor**: The constructor is private to prevent external instantiation.
    - **getInstance() Method**: This method checks if an instance of `Dashboard` exists. If not, it creates one and returns it. This method ensures only one instance can ever be created.

- **Testing Singleton Pattern**: The `DashboardTest` class verifies that the Singleton pattern works correctly by:
    - Attempting to create two references to `Dashboard` through `getInstance()`.
    - Checking that both references point to the same instance to confirm only one instance exists.

## How to Run the Project
1. **Compile**:
    - Compile `Dashboard.java` and `DashboardTest.java`.
2. **Run the Test**:
    - Run `DashboardTest` to verify the Singleton implementation.

### Expected Output
When you run `DashboardTest`, you should see the following output if the Singleton pattern is implemented correctly:

Singleton test passed. Only one instance exists.

If this message appears, it confirms that the Singleton pattern is working as intended.

## Additional Information
- **Language**: Java
- **IDE Recommended**: IntelliJ or Eclipse
- **Dependencies**: None

## Authors
- DeVonte White