# AML Alerting System

## Overview

The AML (Anti-Money Laundering) Alerting System is a Java-based application designed to process financial transactions in real-time and generate alerts for potentially suspicious activity.
It monitors transactions and flags any account that exceeds a predefined threshold within a specified time window.
The threshold and time window are configurable.

## Features

- Real-time transaction processing
- Configurable alert threshold and time window
- Support for over 5000 different accounts
- Thread-safe implementation for concurrent transaction processing
- CSV file input support
- Logging of alerts and system activities

## Requirements

- Java Development Kit (JDK) 17 or higher
- JUnit 5 (for running tests)

## Project Structure

- `AMLAlertingSystem.java`: The main entry point of the AML Alerting System
- `Transaction.java`: Record class Representing a financial transaction
- `Account.java`: Represents a single bank account. Keeps track of all the related transactions
- `AMLTransactionProcessor.java`: Core logic for processing transactions and generating alerts
- `AMLCSVReader.java`: Utility for reading transactions from CSV files
- `AMLAlertingConfiguration.java`: Configuration parameters for AML Alerting system
- `CSVReaderTest.java`: Unit tests for the CSVReader class

## Usage on Unix

1. Compile the Java files:
   ```
   javac *.java
   ```

2. Run the application with an input CSV file:
   ```
   java AMLAlertingSystem <path/to/your/input.csv > results.txt
   ```
 3. The file results.txt contains the output of the program

 4. ## Usage on Windows

1. Import and build the project in your IDE

2. Create a run configuration with the path to your input file as the unique argument

3. Execute the run configuration. The output of the program will be printed to the stdout

## Generating Test Data

Use the `LargeCSVGenerator` class to generate a large test dataset:

```
javac LargeCSVGenerator.java
java LargeCSVGenerator > test_input.csv
```

This will create a CSV file with transactions for over 5000 different accounts.

## Configuration

You can modify the following constants in the `AMLAlertingConfiguration.java` file:

- `THRESHOLD_AMOUNT`: The amount threshold for triggering an alert (default: 50000)
- `ALERT_WINDOW_SECONDS`: The time window for considering transactions (default: 60 seconds)
- `MAX_ACCOUNTS`: Maximum number of accounts supported (default: 5000)
- `MAX_TRANSACTIONS_PER_ACCOUNT`: Maximum number allowed of transactions per account (default: 10000)

## Testing

The project includes JUnit tests for the CSVReader class. To run the tests:

1. Ensure JUnit 5 is in your classpath
2. Run the tests using your IDE's test runner or from the command line

## Logging

The system uses `java.util.logging.Logger` for logging. Configure logging levels in your application code or through a `logging.properties` file.
In order to validate results, the method processTransactions() of the AMLTransactionProcessor class prints the value Y or N next to each transaction to standard output as well as to a logger that can be directed to an output file

## Thread Safety

The application is designed to be thread-safe:
- `Account` class uses `ReadWriteLock` for concurrent access
- `AlertingSystem` uses `ConcurrentHashMap` for thread-safe account management
- Main application uses an `ExecutorService` for parallel transaction processing

## Performance Considerations

- The system is optimized for handling a large number of transactions across many accounts
- For very large datasets, consider increasing the JVM heap size using the `-Xmx` flag

## Future Improvements

- Implement a more sophisticated ordering mechanism for concurrent processing
- Add support for different input formats (e.g., JSON, XML)
- Develop a user interface for real-time monitoring
- Implement more advanced AML detection algorithms

