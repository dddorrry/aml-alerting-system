package org.rbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.rbc.AMLAlertingConfiguration.MAX_ACCOUNTS;
import static org.rbc.AMLAlertingConfiguration.MAX_TRANSACTIONS_PER_ACCOUNT;

class AMLCSVReaderTest {

    @TempDir
    Path tempDir;

    private Path csvFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        csvFile = tempDir.resolve("test.csv");
    }

    @Test
    void testReadTransactions_ValidCSV() throws IOException {
        String csvContent =
                "Time,Amount,Account\n" +
                        "10:00:00,1000,1\n" +
                        "10:00:01,2000,2\n" +
                        "10:00:02,3000,3";
        Files.writeString(csvFile, csvContent);

        List<Transaction> transactions = AMLCSVReader.readTransactions(csvFile.toString());

        assertEquals(3, transactions.size());
        assertEquals(new Transaction(LocalTime.of(10, 0, 0), 1000, 1),
                transactions.get(0));
        assertEquals(new Transaction(LocalTime.of(10, 0, 1), 2000, 2),
                transactions.get(1));
        assertEquals(new Transaction(LocalTime.of(10, 0, 2), 3000, 3),
                transactions.get(2));
    }

    @Test
    void testReadTransactions_ValidCSVFromClassPath() throws IOException {

        List<Transaction> transactions = AMLCSVReader
                .readTransactions("src/test/resources/test-transactions-csv.txt");

        assertEquals(12, transactions.size());

    }

    @Test
    void testReadTransactions_EmptyFile() throws IOException {
        String csvContent = "Time,Amount,Account\n";
        Files.writeString(csvFile, csvContent);

        List<Transaction> transactions = AMLCSVReader.readTransactions(csvFile.toString());

        assertTrue(transactions.isEmpty());
    }

    @Test
    void testReadTransactions_InvalidInput() throws IOException {
        String csvContent =
                "Time,Amount,AccountId\n" +
                        "10:00:00,1000,1\n" +
                        "InvalidLine\n" +
                        "10:00:02,3000,3";
        Files.writeString(csvFile, csvContent);

        List<Transaction> transactions = AMLCSVReader.readTransactions(csvFile.toString());

        assertEquals(2, transactions.size());
        assertEquals(new Transaction(LocalTime.of(10, 0, 0), 1000, 1),
                transactions.get(0));
        assertEquals(new Transaction(LocalTime.of(10, 0, 2), 3000, 3),
                transactions.get(1));
    }

    @Test
    void testReadTransactions_FileNotPresent() {
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            AMLCSVReader.readTransactions("non_existent_file.csv");
        });

        String expectedMessage = "The system cannot find the file specified";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testReadTransactions_LargeValidFile() throws IOException {
        StringBuilder csvContent = new StringBuilder("Time,Amount,AccountId\n");
        for (int i = 0; i < MAX_ACCOUNTS; i++) {
            csvContent.append(String.format("10:00:%02d,%d,%d\n", i % 60, 1000 + i, i % 100));
        }
        Files.writeString(csvFile, csvContent.toString());

        List<Transaction> transactions = AMLCSVReader.readTransactions(csvFile.toString());

        assertEquals(MAX_ACCOUNTS, transactions.size());
        assertEquals(new Transaction(LocalTime.of(10, 0, 0), 1000, 0), transactions.get(0));
        assertEquals(new Transaction(LocalTime.of(10, 0, 19), 5999, 99), transactions.get(4999));
    }

    @Test
    void testReadTransactions_InvalidInput_TooManyAccounts() throws IOException {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AMLCSVReader.readTransactions("src/test/resources/too-many-accounts.csv");
        });

        String expectedMessage = "The maximum number of accounts allowed in the input file is: " + MAX_ACCOUNTS;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));


    }

    @Test
    void testReadTransactions_InvalidInput_TooManyTransactionsPerAccount() throws IOException {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AMLCSVReader.readTransactions("src/test/resources/too-many-transactions-per-account.csv");
        });

        String expectedMessage = "The maximum number of transactions allowed per account is: "
                + MAX_TRANSACTIONS_PER_ACCOUNT;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


}